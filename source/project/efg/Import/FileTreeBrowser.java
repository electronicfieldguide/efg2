package project.efg.Import;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.*;//DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class FileTreeBrowser implements DropTargetListener, 
					   PropertyChangeListener {
    protected FileTree tree;
    protected DropTarget dropTarget;
    protected boolean acceptableType;  // Indicates whether data is acceptable
    TreePath[] selections;        // Initially selected rows
    TreePath leadSelection;        // Initial lead selection
    boolean copyOverExistingFiles;    

  static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(FileTreeBrowser.class); 
	}
	catch(Exception ee){
	}
    }
    public FileTreeBrowser(FileTree tree) {
	this.tree = tree;
	// Listen for changes in the enabled property
	tree.addPropertyChangeListener(this);

	// Create the DropTarget and register 
	// it with the FileTree.
	dropTarget = new DropTarget(tree,
				    DnDConstants.ACTION_COPY_OR_MOVE, 
				    this, 
				    tree.isEnabled(), null);
    }

    // Implementation of the DropTargetListener interface
    public void dragEnter(DropTargetDragEvent dtde) {
	DnDUtils.debugPrintln("dragEnter, drop action = " 
			      + DnDUtils.showActions(dtde.getDropAction()));

	// Save the list of selected items
	saveTreeSelection();

	// Get the type of object being transferred and determine
	// whether it is appropriate.
	checkTransferType(dtde);

	// Accept or reject the drag.
	boolean acceptedDrag = acceptOrRejectDrag(dtde);
	// Do drag-under feedback
	dragUnderFeedback(dtde, acceptedDrag);
    }

    public void dragExit(DropTargetEvent dte) {
	DnDUtils.debugPrintln("DropTarget dragExit");
	// Do drag-under feedback
	dragUnderFeedback(null, false);

	// Restore the original selections
	restoreTreeSelection();
    }

    public void dragOver(DropTargetDragEvent dtde) {
	DnDUtils.debugPrintln("DropTarget dragOver, drop action = "
			      + DnDUtils.showActions(dtde.getDropAction()));

	// Accept or reject the drag
	boolean acceptedDrag = acceptOrRejectDrag(dtde);

	// Do drag-under feedback
	dragUnderFeedback(dtde, acceptedDrag);
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
	DnDUtils.debugPrintln("DropTarget dropActionChanged, drop action = "
			      + DnDUtils.showActions(dtde.getDropAction()));

	// Accept or reject the drag
	boolean acceptedDrag = acceptOrRejectDrag(dtde);
	// Do drag-under feedback
	dragUnderFeedback(dtde, acceptedDrag);
    }

    public void drop(DropTargetDropEvent dtde) {
	DnDUtils.debugPrintln("DropTarget drop, drop action = "
			      + DnDUtils.showActions(dtde.getDropAction()));

	// Check the drop action
	if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
	    // Accept the drop and get the transfer data
	    dtde.acceptDrop(dtde.getDropAction());
	    Transferable transferable = dtde.getTransferable();
	    boolean dropSucceeded = false;
	    try {
		tree.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Save the user's selections
		saveTreeSelection();
		dropSucceeded = dropFile(dtde.getDropAction(),
					 transferable, dtde.getLocation());
		DnDUtils.debugPrintln("Drop completed, success: " 
				      + dropSucceeded);
	    } catch (Exception e) {
		DnDUtils.debugPrintln("Exception while handling drop " + e);
	    } finally {
		tree.setCursor(Cursor.getDefaultCursor());

		// Restore the user's selections
		restoreTreeSelection();
		dtde.dropComplete(dropSucceeded);
	    }
	} else {
	    DnDUtils.debugPrintln("Drop target rejected drop");
	    dtde.dropComplete(false);
	}
    }
    // PropertyChangeListener interface
    public void propertyChange(PropertyChangeEvent evt) {
	String propertyName = evt.getPropertyName();
	if (propertyName.equals("enabled")) {
	    // Enable the drop target if the FileTree is enabled
	    // and vice versa.    
	    dropTarget.setActive(tree.isEnabled());
	}
    }

    // Internal methods start here

    protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
	int dropAction = dtde.getDropAction();
	int sourceActions = dtde.getSourceActions();
	boolean acceptedDrag = false;

	DnDUtils.debugPrintln("\tSource actions are " + 
			      DnDUtils.showActions(sourceActions) + 
			      ", drop action is " + 
			      DnDUtils.showActions(dropAction));
	Point location = dtde.getLocation();
	boolean acceptableDropLocation = isAcceptableDropLocation(location);

	// Reject if the object being transferred 
	// or the operations available are not acceptable.
	if (!acceptableType || 
	    (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
	    DnDUtils.debugPrintln("Drop target rejecting drag");
	    dtde.rejectDrag();
	} else if (!tree.isEditable()) {
	    // Can't drag to a read-only FileTree
	    DnDUtils.debugPrintln("Drop target rejecting drag");      
	    dtde.rejectDrag();
	} else if (!acceptableDropLocation) {
	    // Can only drag to writable directory
	    DnDUtils.debugPrintln("Drop target rejecting drag");      
	    dtde.rejectDrag();
	} else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
	    // Not offering copy or move - suggest a copy
	    DnDUtils.debugPrintln("Drop target offering COPY");
	    dtde.acceptDrag(DnDConstants.ACTION_COPY);
	    acceptedDrag = true;
	} else {
	    // Offering an acceptable operation: accept
	    DnDUtils.debugPrintln("Drop target accepting drag");
	    dtde.acceptDrag(dropAction);
	    acceptedDrag = true;
	}

	return acceptedDrag;
    }
    protected void dragUnderFeedback(DropTargetDragEvent dtde, 
				     boolean acceptedDrag) {
	if (dtde != null && acceptedDrag) {
	    Point location = dtde.getLocation();
	    if (isAcceptableDropLocation(location)) {
		tree.setSelectionRow(
				     tree.getRowForLocation(location.x, location.y));
	    } else {
		tree.clearSelection();
	    }
	} else {
	    tree.clearSelection();
	}
    }
    protected void checkTransferType(DropTargetDragEvent dtde) {
	// Accept a list of files
	acceptableType = false;
	if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
	    acceptableType = true;
	}
	DnDUtils.debugPrintln("Data type acceptable - " + acceptableType);
    }

    // This method handles a drop for a list of files
    protected boolean dropFile(int action,
			       Transferable transferable, Point location) 
	throws IOException, UnsupportedFlavorException,
	       MalformedURLException {
	List files = (List)transferable.getTransferData(
							DataFlavor.javaFileListFlavor);
	TreePath treePath = tree.getPathForLocation(
						    location.x, location.y);
	File targetDirectory = findTargetDirectory(location); 
	if (treePath == null || targetDirectory == null) {
	    return false;
	}
	FileTree.FileTreeNode node = 
	    (FileTree.FileTreeNode)treePath.getLastPathComponent();
	// Highlight the drop location while we perform the drop
	tree.setSelectionPath(treePath);

	// Get File objects for all files being
	// transferred, eliminating duplicates.
	File[] fileList = getFileList(files);
	// Don't overwrite files by default
	copyOverExistingFiles = false;
	// Copy or move each source object to the target
	for (int i = 0; i < fileList.length; i++) {
	    File f = fileList[i];
	    if (f.isDirectory()) {
		transferDirectory(action, f, targetDirectory, node);
	    } else {
		try {
		    transferFile(action, fileList[i], 
				 targetDirectory, node);
		} catch (IllegalStateException e) {
		    // Cancelled by user
		    return false;
		}
	    }
	}
	return true;
    }

    protected File findTargetDirectory(Point location) {
	TreePath treePath = tree.getPathForLocation(location.x, location.y);
	if(treePath != null) {
	    FileTree.FileTreeNode node = 
		(FileTree.FileTreeNode)treePath.getLastPathComponent();
	    // Only allow a drop on a writable directory
	    if (node.isDir()) {
		try {
		    File f = new File(node.getFullName());
		    if (f.canWrite()) {
			return f;
		    }
		} catch (Exception e) {
		}
	    }
	}
	return null;
    }
    protected boolean isAcceptableDropLocation(Point location) {
	return findTargetDirectory(location) != null;
    }
    protected void saveTreeSelection() {
	selections = tree.getSelectionPaths();
	leadSelection = tree.getLeadSelectionPath();
	tree.clearSelection();
    }

    protected void restoreTreeSelection() {
	tree.setSelectionPaths(selections);

	// Restore the lead selection
	if (leadSelection != null) {
	    tree.removeSelectionPath(leadSelection);
	    tree.addSelectionPath(leadSelection);
	}
    }

    // Get the list of files being transferred and
    // remove any duplicates. For example, if the
    // list contains /a/b/c and /a/b/c/d, the
    // second entry is removed.
    protected File[] getFileList(List files) {
	int size = files.size();

	// Get the files into an array for sorting
	File[] f = new File[size];
	Iterator iter = files.iterator();
	int count = 0;
	while (iter.hasNext()) {
	    f[count++] = (File)iter.next();
	}

	// Sort the files into alphabetical order
	// based on pathnames.
	Arrays.sort(f, new Comparator() {
		public boolean equals(Object o1) {
		    return false;
		}

		public int compare(Object o1, Object o2) {
		    return ((File)o1).getAbsolutePath().compareTo(
								  ((File)o2).getAbsolutePath());
		}
	    });

	// Remove duplicates, retaining the results in a Vector
	Vector v = new Vector();
	char separator = System.getProperty("file.separator").charAt(0);
	outer:
	for (int i = f.length - 1 ; i >= 0; i--) {
	    String secondPath = f[i].getAbsolutePath();
	    int secondLength = secondPath.length();
	    for (int j = i - 1 ; j >= 0; j--) {
		String firstPath = f[j].getAbsolutePath();
		int firstLength = firstPath.length();
		if (secondPath.startsWith(firstPath) 
		    && firstLength != secondLength
		    && secondPath.charAt(firstLength) == separator) {
		    continue outer;
		}
	    }
	    v.add(f[i]);
	}
	// Copy the retained files into an array
	f = new File[v.size()];
	v.copyInto(f);

	return f;
    }

    // Copy or move a file 
    protected void transferFile(int action, File srcFile,
				File targetDirectory,
				FileTree.FileTreeNode targetNode) {
	DnDUtils.debugPrintln(
			      (action == DnDConstants.ACTION_COPY ? "Copy" : "Move") +
			      " file " + srcFile.getAbsolutePath() +
			      " to " + targetDirectory.getAbsolutePath());
	// Create a File entry for the target
	String name = srcFile.getName();
	File newFile = new File(targetDirectory, name);
	if (newFile.exists()) {
	    // Already exists - is it the same file?
	    if (newFile.equals(srcFile)) {
		// Exactly the same file - ignore
		return;
	    }
	    // File of this name exists in this directory
	    if (copyOverExistingFiles == false) {
		int res = JOptionPane.showOptionDialog(tree,
						       "A file called\n   " + name +
						       "\nalready exists in the directory\n   " +
						       targetDirectory.getAbsolutePath() +
						       "\nOverwrite it?",
						       "File Exists",
						       JOptionPane.DEFAULT_OPTION,
						       JOptionPane.QUESTION_MESSAGE,
						       null, new String[] {
							   "Yes", "Yes to All", "No", "Cancel"
						       }, 
						       "No");
		switch (res) {
		case 1:  // Yes to all
		    copyOverExistingFiles = true;
		case 0:  // Yes
		    break;
		case 2:  // No
		    return;
		default: // Cancel
		    throw new IllegalStateException("Cancelled");
		}
	    }      
	} else {
	    // New file - create it
	    try {
		newFile.createNewFile();
	    } catch (IOException e) {
		JOptionPane.showMessageDialog(tree, 
					      "Failed to create new file\n  " +
					      newFile.getAbsolutePath(),
					      "File Creation Failed",
					      JOptionPane.ERROR_MESSAGE);
		return;
	    }
	}
	// Copy the data and close file.
	BufferedInputStream is = null;
	BufferedOutputStream os = null;
	try {
	    is = new BufferedInputStream(
					 new FileInputStream(srcFile));
	    os = new BufferedOutputStream(
					  new FileOutputStream(newFile));
	    int size = 4096;
	    byte[] buffer = new byte[size];
	    int len;
	    while ((len = is.read(buffer, 0, size)) > 0) {
		os.write(buffer, 0, len);
	    }
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(tree, 
					  "Failed to copy file\n  " +
					  name + "\nto directory\n  " +
					  targetDirectory.getAbsolutePath(),
					  "File Copy Failed",
					  JOptionPane.ERROR_MESSAGE);
	    return;
	} finally {
	    try {
		if (is != null) {
		    is.close();
		}
		if (os != null) {
		    os.close();
		}
	    } catch (IOException e) {
	    }      
	}
	// Remove the source if this is a move operation.
	if (action == DnDConstants.ACTION_MOVE &&
	    System.getProperty("DnDExamples.allowRemove") != null) {
	    srcFile.delete();
	}
	// Update the tree display
	if (targetNode != null) {
	    tree.addNode(targetNode, name);
	}
    }

    protected void transferDirectory(int action, File srcDir,
				     File targetDirectory,
				     FileTree.FileTreeNode targetNode) {
	DnDUtils.debugPrintln(
			      (action == DnDConstants.ACTION_COPY ? "Copy" : "Move") +
			      " directory " + srcDir.getAbsolutePath() +
			      " to " + targetDirectory.getAbsolutePath());

	// Do not copy a directory into itself or 
	// a subdirectory of itself.
	File parentDir = targetDirectory;
	while (parentDir != null) {
	    if (parentDir.equals(srcDir)) {
		DnDUtils.debugPrintln("-- SUPPRESSED");
		return;
	    }
	    parentDir = parentDir.getParentFile();
	}
	// Copy the directory itself, then its contents
	// Create a File entry for the target
	String name = srcDir.getName();
	File newDir = new File(targetDirectory, name);
	if (newDir.exists()) {
	    // Already exists - is it the same directory?
	    if (newDir.equals(srcDir)) {
		// Exactly the same file - ignore
		return;
	    }
	} else {
	    // Directory does not exist - create it
	    if (newDir.mkdir() == false) {
		// Failed to create - abandon this directory
		JOptionPane.showMessageDialog(tree, 
					      "Failed to create target directory\n  " +
					      newDir.getAbsolutePath(),
					      "Directory creation Failed",
					      JOptionPane.ERROR_MESSAGE);
		return;    
	    }
	}
	// Add a node for the new directory
	if (targetNode != null) {
	    targetNode = tree.addNode(targetNode, name);
	}
	// Now copy the directory content.
	File[] files = srcDir.listFiles();
	for (int i = 0; i < files.length; i++) {
	    File f = files[i];
	    if (f.isFile()) {
		transferFile(action, f, newDir, targetNode);
	    } else if (f.isDirectory()) {
		transferDirectory(action, f, newDir, targetNode);
	    }
	}
	// Remove the source directory after moving
	if (action == DnDConstants.ACTION_MOVE &&
	    System.getProperty("DnDExamples.allowRemove") != null) {
	    srcDir.delete();
	}
    }
}
class FileTree extends JTree implements Autoscroll {
    public static final Insets defaultScrollInsets = new Insets(8, 8, 8, 8);
    protected Insets scrollInsets = defaultScrollInsets;
    protected DefaultTreeModel model;
    public FileTree(String path) throws FileNotFoundException, SecurityException {
	super((TreeModel)null);      // Create the JTree itself

	// Use horizontal and vertical lines
	putClientProperty("JTree.lineStyle", "Angled");
	// Create the first node
	int index = path.lastIndexOf(File.separator);	
	String parent = null;
	if(index > -1){
	    parent = path.substring(0, index);
	    path = path.substring(index + 1, path.length());
	}
	FileTreeNode rootNode = new FileTreeNode(parent, path);
	// Populate the root node with its subdirectories
	boolean addedNodes = rootNode.populateDirectories(true);
	this.model = new DefaultTreeModel(rootNode);
	setModel(this.model);

	// Listen for Tree Selection Events
	addTreeExpansionListener(new TreeExpansionHandler());
    }
    // Returns the full pathname for a path, or null if not a known path
    public String getPathName(TreePath path) {
	Object o = path.getLastPathComponent();
	if (o instanceof FileTreeNode) {
	    return ((FileTreeNode)o).fullName;
	}
	return null;
    }
    // Adds a new node to the tree after construction.
    // Returns the inserted node, or null if the parent
    // directory has not been expanded.
    public FileTreeNode addNode(FileTreeNode parent, String name) {
	int index = parent.addNode(name);
	if (index != -1) {
	    ((DefaultTreeModel)getModel()).nodesWereInserted(
							     parent, 
							     new int[] { index }
							     );
	    return (FileTreeNode)parent.getChildAt(index);
	}
	// No node was created
	return null;
    }
    // Autoscrolling support
    public void setScrollInsets(Insets insets) {
	this.scrollInsets = insets;
    }

    public Insets getScrollInsets() {
	return scrollInsets;
    }

    // Implementation of Autoscroll interface
    public Insets getAutoscrollInsets() {
	Rectangle r = getVisibleRect();
	Dimension size = getSize();
	Insets i = new Insets(r.y + scrollInsets.top, r.x + scrollInsets.left, 
			      size.height - r.y - r.height + scrollInsets.bottom, 
			      size.width - r.x - r.width + scrollInsets.right);
	return i;
    }

    public void autoscroll(Point location) {
	JScrollPane scroller = 
	    (JScrollPane)SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
	if (scroller != null) {
	    JScrollBar hBar = scroller.getHorizontalScrollBar();
	    JScrollBar vBar = scroller.getVerticalScrollBar();
	    Rectangle r = getVisibleRect();
	    if (location.x <= r.x + scrollInsets.left) {
		// Need to scroll left
		hBar.setValue(hBar.getValue() - hBar.getUnitIncrement(-1));
	    }
	    if (location.y <= r.y + scrollInsets.top) {
		// Need to scroll up
		vBar.setValue(vBar.getValue() - vBar.getUnitIncrement(-1));
	    }
	    if (location.x >= r.x + r.width - scrollInsets.right) {
		// Need to scroll right
		hBar.setValue(hBar.getValue() + hBar.getUnitIncrement(1));
	    }
	    if (location.y >= r.y + r.height - scrollInsets.bottom) {
		// Need to scroll down
		vBar.setValue(vBar.getValue() + vBar.getUnitIncrement(1));
	    }
	}
    }
    // Inner class that handles Tree Expansion Events
    protected class TreeExpansionHandler implements TreeExpansionListener {
	public void treeExpanded(TreeExpansionEvent evt) {
	    TreePath path = evt.getPath();      // The expanded path
	    JTree tree = (JTree)evt.getSource();  // The tree

	    // Get the last component of the path and
	    // arrange to have it fully populated.
	    FileTreeNode node = (FileTreeNode)path.getLastPathComponent();
	    if (node.populateDirectories(true)) {
		((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);  
	    }
	}

	public void treeCollapsed(TreeExpansionEvent evt) {
	    // Nothing to do
	}
    }
    public static class FileTreeNode extends DefaultMutableTreeNode {
	protected String name;    // Name of this component
	protected String fullName;  // Full pathname
	protected boolean populated;// true if we have been populated
	protected boolean interim;  // true if we are in interim state
	protected boolean isDir;  // true if this is a directory
	
	public FileTreeNode(String parent, String name) throws SecurityException,
							       FileNotFoundException {
	    this.name = name;
	    // See if this node exists and whether it is a directory
	    fullName = parent == null ? name : parent + File.separator + name;      
	    
	    File f = new File(fullName);
	    if (f.exists() == false) {
		throw new FileNotFoundException("File " + fullName + " does not exist");
	    }
	    
	    isDir = f.isDirectory();
	    
	    // Hack for Windows which doesn't consider a drive to be a directory!
	    if (isDir == false && f.isFile() == false) {
		isDir = true;
	    }      
	}
	
	// Override isLeaf to check whether this is a directory
	public boolean isLeaf() {
	    return !isDir;
	}

	// Override getAllowsChildren to check whether this is a directory
	public boolean getAllowsChildren() {
	    return isDir;
	}

	// Return whether this is a directory
	public boolean isDir() {
	    return isDir;
	}

	// Get full path
	public String getFullName() {
	    return fullName;
	}

	// For display purposes, we return our own name
	public String toString() {      
	    return name;
	}

	// If we are a directory, scan our contents and populate 
	// with children. In addition, populate those children
	// if the "descend" flag is true. We only descend once,
	// to avoid recursing the whole subtree. 
	// Returns true if some nodes were added
	boolean populateDirectories(boolean descend) {
	    boolean addedNodes = false;

	    // Do this only once
	    if (populated == false) {
		File f;
		try {
		    f = new File(fullName);          
		} catch (SecurityException e) {
		    populated = true;
		    return false;
		}

		if (interim == true) {
		    // We have had a quick look here before:
		    // remove the dummy node that we added last time
		    removeAllChildren();
		    interim = false;          
		}        
		String[] names = f.list();    // Get list of contents
		// Process the contents
		ArrayList list = new ArrayList();          
		for (int i = 0; i < names.length; i++) {
		    String name = names[i];
		    File d = new File(fullName, name);
		    try {
			FileTreeNode node = new FileTreeNode(fullName, name);
			list.add(node);
			if (descend && d.isDirectory()) {
			    node.populateDirectories(false);
			}
			addedNodes = true;
			if (descend == false) {
			    // Only add one node if not descending
			    break;
			}
		    } catch (Throwable t) {
			// Ignore phantoms or access problems
		    }
		}

		if (addedNodes == true) {
		    // Now sort the list of contained files and directories
		    Object[] nodes = list.toArray();
		    Arrays.sort(nodes, new Comparator() {
			    public boolean equals(Object o) {
				return false;
			    }

			    public int compare(Object o1, Object o2) {
				FileTreeNode node1 = (FileTreeNode)o1;
				FileTreeNode node2 = (FileTreeNode)o2;

				// Directories come first
				if (node1.isDir != node2.isDir) {
				    return node1.isDir ? -1 : +1;
				}

				// Both directories or both files -
				// compare based on pathname
				return node1.fullName.compareTo(node2.fullName);
			    }
			});
		    // Add sorted items as children of this node
		    for (int j = 0; j < nodes.length; j++) {
			this.add((FileTreeNode)nodes[j]);
		    }
		}
		// If we were scanning to get all subdirectories,
		// or if we found no content, there is no
		// reason to look at this directory again, so
		// set populated to true. Otherwise, we set interim
		// so that we look again in the future if we need to
		if (descend == true || addedNodes == false) {
		    populated = true;          
		} else {
		    // Just set interim state
		    interim = true;          
		}
	    }
	    return addedNodes;
	}
	// Adding a new file or directory after
	// constructing the FileTree. Returns
	// the index of the inserted node.
	public int addNode(String name) {
	    // If not populated yet, do nothing
	    if (populated == true) {
		// Do not add a new node if
		// the required node is already there
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
		    FileTreeNode node = (FileTreeNode)getChildAt(i);
		    if (node.name.equals(name)) {
			// Already exists - ensure
			// we repopulate
			if (node.isDir()) {
			    node.interim = true;
			    node.populated = false;
			}
			return -1;
		    }
		}
		// Add a new node
		try {
		    FileTreeNode node = new FileTreeNode(fullName, name);
		    add(node);
		    return childCount;
		} catch (Exception e) {
		}
	    }
	    return -1;
	}

    }

}
class DnDUtils {
    public static String showActions(int action) {
	String actions = "";
	if ((action & (DnDConstants.ACTION_LINK|DnDConstants.ACTION_COPY_OR_MOVE)) == 0) {
	    return "None";
	}

	if ((action & DnDConstants.ACTION_COPY) != 0) {
	    actions += "Copy ";
	}

	if ((action & DnDConstants.ACTION_MOVE) != 0) {
	    actions += "Move ";
	}

	if ((action & DnDConstants.ACTION_LINK) != 0) {
	    actions += "Link";
	}

	return actions;
    }

    public static boolean isDebugEnabled() {
	return debugEnabled;
    }

    public static void debugPrintln(String s) {
	if (debugEnabled) {
	    System.out.println(s);
	}
    }

    private static boolean debugEnabled = 
	(System.getProperty("DnDExamples.debug") != null);
}
