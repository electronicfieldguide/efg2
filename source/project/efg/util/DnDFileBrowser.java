package project.efg.util;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.EFGThumbNailDimensions;
import project.efg.Imports.efgImportsUtil.EFGUtils;


/**
 * @version $Revision$
 * @author Benoît Mahé (bmahe@w3.org)
 */
public class DnDFileBrowser extends FileBrowser implements DragGestureListener,
		DropTargetListener, DragSourceListener,WindowListener {
	static Logger log = null;

	

	static {
		try {
			log = Logger.getLogger(DnDFileBrowser.class);
		} catch (Exception ee) {
		}
	}
	  private final String images_home = 
			EFGImportConstants.EFGProperties.getProperty(
					"efg.images.home");
	  private final	String thumbshome = 
			EFGImportConstants.EFGProperties.getProperty(
					"efg.mediaresources.thumbs.home");

	//
	// DragSourceListener
	//

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	DropTarget dropTarget;
	
	DragSource dragSource = DragSource.getDefaultDragSource();

	private boolean isDragging = false;
	boolean copyOverExistingFiles = false;
	FileNode dragged = null;
	int current = 0;
	int max = 0;
	int min = 0;
	String message = "";
	FileNode root;
	FileNode destNode;
	//
	// Constructor
	//
	private JProgressBar progressBar;
	public DnDFileBrowser(FileNode root,JProgressBar progressBar) {
		this(root,progressBar,null);
		
	}
	
	protected JFrame importMenu;
	protected JFrame getFrame() {
		return this.importMenu;
	}
	public DnDFileBrowser(FileNode root,JProgressBar progressBar,JFrame importMenu) {
		super(root);
		this.root = root;
		this.importMenu = importMenu;
		this.progressBar = progressBar;
		
		this.progressBar.setMinimum(0);
		if (this.progressBar.isIndeterminate()) {
			progressBar.setIndeterminate(false);
			progressBar.setString(null); // display % string
		}
		this.progressBar.setIndeterminate(true);
		// DND
		dragSource.createDefaultDragGestureRecognizer(this,
				DnDConstants.ACTION_MOVE, this);
		dropTarget = new DropTarget(this, this);
		
	}
	
	
	public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
		
		if (dragSourceDropEvent.getDropSuccess()) {
			if (dragSourceDropEvent.getDropAction() == DnDConstants.ACTION_MOVE) {
				removeNodeFromParent(dragged);
			}
		} else {
			// output some message
			log.debug("Drop failed");
		}
		dragged = null;
	}

	public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
		
	}

	public void dragExit(DragSourceEvent dragSourceEvent) {

	}

	public void dragOver(DragSourceDragEvent dragSourceDragEvent) {

	}

	public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {

	}

	//
	// DragGestureListener
	//

	/**
	 * a Drag operation has encountered the DropTarget
	 */
	public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
		dropTargetDragEvent.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
		isDragging = true;
	}

	/**
	 * Is the mouse dragging something on the resource tree?
	 * 
	 * @return a boolean
	 */
	public boolean isDragging() {
		return isDragging;
	}

	/**
	 * The Drag operation has departed the DropTarget without dropping.
	 */
	public void dragExit(DropTargetEvent dropTargetEvent) {
		isDragging = false;
	}

	/**
	 * a Drag operation is ongoing on the DropTarget
	 */
	public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
		Point location = dropTargetDragEvent.getLocation();
		TreePath path = getClosestPathForLocation(location.x, location.y);
		setSelectionPath(path);
	}

	/**
	 * The user has modified the current drop gesture
	 */
	public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {

	}
	private boolean isThumbNailsPromptCheckBoxSelected(){
		String property = 
			EFGImportConstants.EFGProperties.getProperty(
					"efg.thumbnails.dimensions.checked", 
					EFGImportConstants.EFG_TRUE);

		if(property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
			return true;
		}
		return false;
	}

	
	/**
	 * The Drag operation has terminated with a Drop on this DropTarget
	 */
	public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
		
		isDragging = false;
		Transferable tr = dropTargetDropEvent.getTransferable();
		
		try {
			//if the drop is on a file reject it..Drops must always be on a directory
			if(!isDirectorySelected()){
				String msg = "Please select a directory to drop file." + 
				" Application does not allow files to be droped on other files!!";
				this.dropError(msg,dropTargetDropEvent);	
				return;
			}
			if (tr.isDataFlavorSupported(TransferableFileNode.FILENODE_FLAVOR)) {
				//
				// Order is important!!! acceptDrop before getTransferData
				//
				FileNode node = (FileNode) tr
				.getTransferData(TransferableFileNode.FILENODE_FLAVOR);
				TreePath path = getSelectionPath();
				this.destNode = (FileNode)path.getLastPathComponent();
				String nodeToMove = node.getFile().getAbsolutePath();
				String destPath = destNode.getFile().getAbsolutePath();
				if(nodeToMove.equalsIgnoreCase(destPath)){
					dropTargetDropEvent.rejectDrop();
					return;
				}
				int result = JOptionPane.showConfirmDialog(null,
						"Do you really want to move the file: " + node.getFile().getName() + " to " +
						destNode.getFile().getName() + "?", "Move Resource(s)",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					
					dropFile(node, dropTargetDropEvent);
				}
				else{
					dropTargetDropEvent.rejectDrop();
					return;
				}
			} else if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				//
				// Order is important!!! acceptDrop before getTransferData
				//
				dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY);
				String path = (String) tr
						.getTransferData(DataFlavor.stringFlavor);
				List objectsToDrop = new ArrayList();
				DropFileObject dropObject = dropFile(path,
						dropTargetDropEvent);
				if(dropObject != null){
					objectsToDrop.add(dropObject);
				}
				this.prepareAndShow(objectsToDrop);
					
			} else if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				//
				// Order is important!!! acceptDrop before getTransferData
				//
				dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY);
				List list = (List) tr
						.getTransferData(DataFlavor.javaFileListFlavor);
				//do in a new thread
				Iterator iter = list.iterator();
				List objectsToDrop = new ArrayList();
				while (iter.hasNext()) {
					File file = (File)iter.next();
				    String path = file.getAbsolutePath();
					DropFileObject dropObject = dropFile(path,
							dropTargetDropEvent);
					if(dropObject != null){
						objectsToDrop.add(dropObject);
					}
				}
				this.prepareAndShow(objectsToDrop);
				

			} else {
				dropTargetDropEvent.rejectDrop();
			}
		} catch (IOException ex) {
			log.error(ex.getMessage());
			dropTargetDropEvent.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			log.error(ufe.getMessage());
			dropTargetDropEvent.rejectDrop();
		}	
	}

	private void prepareAndShow(List objectsToDrop) {
		if(objectsToDrop.size() > 0){
		
			if(isMediaMagickPromptCheckBoxSelected()) {
				Properties props = EFGUtils.getEnvVars();
				String property = null;
				if (props != null) {
					String propertyToUse = 
						EFGImportConstants.EFGProperties.getProperty(
								"efg.images.magickhome.variable"
								);
					property = props.getProperty(propertyToUse);
				}
				String pathToServer = 
					EFGImportConstants.EFGProperties.getProperty(
							"efg.imagemagicklocation.lists",
							property);
				ServerLocator locator = new ServerLocator(
						importMenu,
						pathToServer,
						true,
						"efg.imagemagicklocation.lists",
						"efg.imagemagicklocation.current",
						"efg.imagemagicklocation.checked",
						"Prompt Me For Image Magick Location Every Time");
					locator.setVisible(true);
	
			}
				
			if(isThumbNailsPromptCheckBoxSelected()){
				EFGThumbNailDimensions thd = 
					new EFGThumbNailDimensions(this.importMenu,"Enter Max Dimension",true);
				thd.setVisible(true);
				
				String currentDim = EFGImportConstants.EFGProperties.getProperty(
				"efg.thumbnails.dimensions.current");
				DnDFileBrowserMain.setCurrentDimLabel(currentDim);

			}
		    EFGCopyFilesThread copyFiles = new EFGCopyFilesThread(this,objectsToDrop,this.progressBar);
		    copyFiles.start();
		    copyOverExistingFiles = false;
		}

	}
	/**
	 * @return
	 */
	private boolean isMediaMagickPromptCheckBoxSelected() {
		String property = 
			EFGImportConstants.EFGProperties.getProperty(
					"efg.imagemagicklocation.checked", 
					EFGImportConstants.EFG_TRUE);

		if(property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
			return true;
		}
		return false;
	}
	public void copyFile(File srcFile, File destFile) {
		  FileChannel sourceChannel = null;
		  FileChannel destinationChannel = null;
		  
		 try{
		
			 destFile = new File(destFile,srcFile.getName()); 
			
			 if(srcFile.isDirectory()){	
				 destFile.mkdirs();
				 File[] list = srcFile.listFiles();
				
				 for(int i = 0; i < list.length; i++){
					 File file = list[i];
					copyFile(file,destFile);
				 }
			 }
			 else{
				 sourceChannel = new
				 FileInputStream(srcFile).getChannel();
				
				 destinationChannel = new
				 FileOutputStream(destFile).getChannel();
				 sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
				 // or
	    	//  destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
				 sourceChannel.close();
				 destinationChannel.close();
				 
				
			 }
		 }
		 catch(Exception ee){
			log.error(ee.getMessage());
			return;
			 
		 }
		 finally{
			 try{
				 if(sourceChannel != null){
				 sourceChannel.close();
				 }
				 if(destinationChannel != null){
					 destinationChannel.close();
				 }
			 }
			 catch(Exception eee){
				 
			 }
		 }
	  }





	public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
		try {
			// no drag with button3
			int modifier = dragGestureEvent.getTriggerEvent().getModifiers();
			if ((modifier & InputEvent.BUTTON3_MASK) != 0)
				return;
			startDrag(dragGestureEvent);
		} catch (InvalidDnDOperationException ex) {
			log.error(ex.getMessage());
		}
	}

	protected Image getDragImage() {
		return null;
		// return getImage("/org/w3c/tools/swingwidgets/cursor/dragdir.gif");
	}

	protected Image getImage(String name) {
		URL location = getClass().getResource(name);
		return Toolkit.getDefaultToolkit().getImage(location);
	}

	//
	// DropTargetListener
	//

	/**
	 * Drop a file (from another application) on the selected FileNode
	 * 
	 * @param path
	 *            the file path dropped
	 * @param dropTargetDropEvent
	 *            the drop event
	 */
	protected DropFileObject dropFile(String path, DropTargetDropEvent dropTargetDropEvent) {
		TreePath tpath = getSelectionPath();
		FileNode destNode = (FileNode)tpath.getLastPathComponent();
		File destFile =  destNode.getFile();
		File srcFile = new File(path);
		return this.dropFile(srcFile ,destFile,null,
		true, dropTargetDropEvent);
	}

	/**
	 * Drop a file (from this application) on the selected FileNode
	 * 
	 * @param dropnode
	 *            the node to drop
	 * @param dropTargetDropEvent
	 *            the drop event
	 */
	protected DropFileObject dropFile(FileNode srcNode,
			DropTargetDropEvent dropTargetDropEvent) {
		TreePath path = getSelectionPath();
		FileNode destNode = (FileNode) path.getLastPathComponent();
		return dropFile(srcNode, destNode, false, dropTargetDropEvent);
	}
	/**
	 * Drop a FileNode on another.
	 * 
	 * @param sourcenode
	 *            the node dragged
	 * @param destnode
	 *            the node where the sourcenode was dropped.
	 * @param external
	 *            does the dragged node come from an external application?
	 * @param dropTargetDropEvent
	 *            the drop event
	 */
	protected DropFileObject dropFile(FileNode srcNode , FileNode destNode,
			boolean external, DropTargetDropEvent dropTargetDropEvent) {
		
		File srcFile = srcNode.getFile();
		String srcFileName = srcFile.getName();
		File destFile = destNode.getFile();
		File newFile = null;
		if (destFile.isDirectory()) {//it is a directory create it
			newFile = new File(destFile, srcFileName);
		}
		else {//it is a file
			newFile = destFile;
		}	
		return dropFile(srcFile ,newFile,srcNode,
				external,dropTargetDropEvent);
	
	}


	/**
	 * Drop a FileNode on another.
	 * 
	 * @param sourcenode
	 *            the node dragged
	 * @param destnode
	 *            the node where the sourcenode was dropped.
	 * @param external
	 *            does the dragged node come from an external application?
	 * @param dropTargetDropEvent
	 *            the drop event
	 */
	protected DropFileObject dropFile(File srcFile , File destFile,FileNode srcNode,
			boolean external, DropTargetDropEvent dropTargetDropEvent) {
	
		
		
		String srcFileName = srcFile.getName();
		String fileType = (srcFile.isDirectory() ? "directory" : "file");
		
		
		if (!destFile.equals(srcFile)) {//src and dest are not the same
			
			File newFile = null;
			if (destFile.isDirectory()) {//it is a directory create it
				newFile = new File(destFile, srcFileName);
			}
			else {//it is a file
				newFile = destFile;
			}
			if (newFile.equals(srcFile)) {//if the src and destination are the same
				String msg = "Cannot move " + fileType + ", " + srcFileName + " and "
						+ srcFileName + " are identical";
				this.dropError(msg,dropTargetDropEvent);
			
				return null;
			} else if (newFile.exists()) {//if the new file already exists ask before overwriting it
				if (!copyOverExistingFiles) {
				int res = overWrite(srcFileName,destFile);
				boolean isReturn = true;
				
					switch (res) {
					case 1: // Yes to all
						copyOverExistingFiles = true;
					
						isReturn = false;
						break;
					case 0: // Yes
						copyOverExistingFiles = false;
						
						isReturn = false;
						break;
					case 2: // No
						copyOverExistingFiles = false;
						
						break;
					default: // Cancel
						
						copyOverExistingFiles = false;
						break;
					}
					if (isReturn) {
						dropTargetDropEvent.dropComplete(false);
						String msg = "File " + newFile.getName() + " exists";
						JOptionPane.showMessageDialog(this, msg);
						return null;
					}
				}	
			}
			boolean isRename = false;
			if(external){//allow only external copies
				isRename = false;
			}
			else{
				if (dropTargetDropEvent.getDropAction() == DnDConstants.ACTION_MOVE) {
					isRename = srcFile.renameTo(newFile);
					//rename directory in thumbnails folder
					
					 String thumbSrc = this.replace(srcFile.getAbsolutePath(),
							images_home, 
							thumbshome);
					 String thumbDest = this.replace(
							 newFile.getAbsolutePath(),images_home, 
							thumbshome);
					 File thumbSrcFile = new File(thumbSrc);
					 File thumbDestFile = new File(thumbDest);
					 thumbSrcFile.renameTo(thumbDestFile);
					//for internal moves , move thumbnails too..
				}
			}
			
			if(!isRename){//rename is not supported or it is a copy
		    
				if(destNode == null){
					destNode = getSelectedDirectory();
				}
		       return new DropFileObject(srcFile, destFile,destNode);
				
			}
			if(isRename){//file successfully copied or moved
				TreePath tpath = getSelectionPath();
				FileNode destNode = (FileNode)tpath.getLastPathComponent();
				//create my nodes here
				addNode(destNode,srcNode,external,
						newFile,srcFile,dropTargetDropEvent);
			} else {
				//physically copy files
				//String msg = "An error occured while copying or moving files";
				//this.dropError(msg,dropTargetDropEvent);
				return null;
			}
		} else {
			String msg = "Cannot move " + fileType + ", " + srcFileName + " and " + srcFileName
			+ " are identical";
			this.dropError(msg,dropTargetDropEvent);
		}
		return null;
	}


	/**
	 * Find the node relative to the given file.
	 * 
	 * @param file
	 *            the file
	 * @return a FileNode instance
	 */
	protected FileNode findNode(File file) {
		FileNode root = (FileNode) getModel().getRoot();
		return root.findNode(file);
	}
	/**
	 * Replace the last occurence of aOldPattern in aInput with aNewPattern
	 * 
	 * @param aInput
	 *            is the original String which may contain substring aOldPattern
	 * @param aOldPattern
	 *            is the non-empty substring which is to be replaced
	 * @param aNewPattern
	 *            is the replacement for aOldPattern
	 */
	private String replace(final String aInput, final String aOldPattern,
			final String aNewPattern) {
		if (aOldPattern.equals("")) {
			log.error("Old pattern must have content.");
		}
		return aInput.replaceAll(aOldPattern, aNewPattern);
	}

	private void addNode(FileNode destNode, FileNode sourceNode, boolean external,
			File destFile,File srcFile,DropTargetDropEvent dropTargetDropEvent){
		dropTargetDropEvent.dropComplete(true);
		FileNode newNode = new FileNode(this, destNode, destFile);
		addNode(destNode, newNode);
		// not owner
		if (external) {//ignore this allow operating system to do removal
			//removeNodeFromParent(sourceNode);
		} else if (!sourceNode.isOwner(this)) {
			// we have to remove the node
			FileNode toRemove = findNode(srcFile);
			if (toRemove != null){
			
				removeNodeFromParent(toRemove);
			}
		}
		
	}

   private int overWrite(String name, File targetDirectory){
	 //		 File of this name exists in this directory
		
			return  JOptionPane.showOptionDialog(null,
					"A file called\n   " + name
					+ "\nalready exists in the directory\n   "
					+ targetDirectory.getAbsolutePath()
					+ "\nOverwrite it?", "File Exists",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, new String[] {
				"Yes", "Yes to All", "No", "Cancel" }, "No");
		
		
	}


private boolean isDirectorySelected(){
	TreePath path = getSelectionPath();
	FileNode destNode = (FileNode) path.getLastPathComponent();
	File f = destNode.getFile();
	return f.isDirectory();
}
private FileNode getSelectedDirectory(){
	TreePath path = getSelectionPath();
	return (FileNode) path.getLastPathComponent();
}

private void dropError(String message,DropTargetDropEvent dropTargetDropEvent){
	   dropTargetDropEvent.dropComplete(false);
		
		JOptionPane.showMessageDialog(this.importMenu, message, "Error",
				JOptionPane.ERROR_MESSAGE); 
   }
	private void startDrag(DragGestureEvent dragGestureEvent) {
		TreePath path = getSelectionPath();
		if (path != null) {
			// take the first only (test)
			dragged = (FileNode) path.getLastPathComponent();
			TransferableFileNode tnode = new TransferableFileNode(dragged);
			dragSource.startDrag(dragGestureEvent, null, tnode, this);
		}
	}

	public static FileBrowser getFileBrowser(String rootname,JProgressBar progressBar ) {
		return getFileBrowser(rootname,progressBar,null);
	}
	public static FileBrowser getFileBrowser(String rootname,
			JProgressBar progressBar,JFrame importMenu ) {
		
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.charAt(0) != '.');
			}
		};
		
		FileNode root = new FileNode(rootname, filter);
		
		FileBrowser browser = new DnDFileBrowser(root,progressBar,importMenu);
		root.initializeRootNode(browser);
		return browser;
	}
	public static void main(String args[]) {
		Utils.unBoldSpecificFonts();
		JFrame frame = new JFrame("File Browser");
		JScrollPane scrollpane = new JScrollPane(getFileBrowser("c:\\"));
	//	imageView = addImagePanel();
		JScrollPane scrollpane2 = new JScrollPane(getFileBrowser("c:\\"));
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				scrollpane, scrollpane2);
		pane.setOneTouchExpandable(true);
		pane.setDividerLocation(300);
		frame.getContentPane().add(pane, BorderLayout.CENTER);
		frame.setSize(600, 600);
		frame.setVisible(true);
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent e) {
		
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent e) {
		
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent e) {
		
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent e) {
		
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent e) {
		
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent e) {
		
		
	}
}