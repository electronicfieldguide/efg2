package project.efg.client.utils.gui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;



/**
 * @version $Revision: 1.1.1.1 $
 * @author Benoît Mahé (bmahe@w3.org)
 */
public class FileBrowser extends JTree {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	public static final String DELETE_CMD = "delete";

	private JFrame frame = null;

	/**
	 * Our TreeWillExpandListener
	 */
	TreeWillExpandListener twel = new TreeWillExpandListener() {
		public void treeWillExpand(TreeExpansionEvent event)
				throws ExpandVetoException {
			TreeNode node = (TreeNode) event.getPath().getLastPathComponent();
			((FileNode) node).nodeWillExpand();
			((DefaultTreeModel) getModel()).reload(node);
		}

		public synchronized void treeWillCollapse(TreeExpansionEvent event)
				throws ExpandVetoException {
			TreeNode node = (TreeNode) event.getPath().getLastPathComponent();
			((FileNode) node).nodeWillCollapse();
			((DefaultTreeModel) getModel()).reload(node);
		}
	};

	/**
	 * Our ActionListener
	 */
	ActionListener al = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			String command = evt.getActionCommand();
			if (command.equals(DELETE_CMD)) {
				deleteSelectedFiles();
			}
		}
	};

	/**
	 * The popup menu action listener.
	 */
	ActionListener pmal = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			setCursor(Cursor.WAIT_CURSOR);
			String command = evt.getActionCommand();
			if (command.equals("del")) {
				deleteSelectedFiles();
			} else if (command.equals("newfolder")) {
				
			}
			setCursor(Cursor.DEFAULT_CURSOR);
		}
	};

	/**
	 * Our MouseListener
	 */
	MouseAdapter mouseAdapter = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	};

	public FileBrowser(FileNode root) {
			super(root);
			//ToolTipManager.sharedInstance().registerComponent(this);
	//		 Use horizontal and vertical lines
			putClientProperty("JTree.lineStyle", "Angled");
			this.setRowHeight(0);
			setShowsRootHandles(true);
			setRootVisible(true);
			setEditable(false);
			setLargeModel(true);
			setToolTipText("Drag and Drop one or more Image(s) Folder into this Window.");
			
			setScrollsOnExpand(true);
			addTreeWillExpandListener(twel);
			KeyStroke delK = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
			registerKeyboardAction(al, DELETE_CMD, delK, WHEN_FOCUSED);
			addMouseListener(mouseAdapter);
		}

	public static FileBrowser getFileBrowser(String rootname) {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.charAt(0) != '.');
			}
		};
		FileNode root = new FileNode("My Computer", filter);
		FileBrowser browser = new FileBrowser(root);
		root.initializeRootNode(browser);
		return browser;
	}

	public void setCursor(int cursor) {
		getFrame().setCursor(Cursor.getPredefinedCursor(cursor));
	}

	/**
	 * Filter the TreePath array. Remove all nodes that have one of their parent
	 * in this array.
	 * 
	 * @param paths
	 *            the TreePath array
	 * @return the filtered array
	 */
	protected TreePath[] removeDescendants(TreePath[] paths) {
		if (paths == null)
			return null;
		Vector newpaths = new Vector();
		for (int i = 0; i < paths.length; i++) {
			TreePath currentp = paths[i];
			boolean hasParent = false;
			for (int j = 0; j < paths.length; j++) {
				if ((!(j == i)) && (paths[j].isDescendant(currentp)))
					hasParent = true;
			}
			if (!hasParent)
				newpaths.addElement(currentp);
		}
		TreePath[] filteredPath = new TreePath[newpaths.size()];
		newpaths.copyInto(filteredPath);
		return filteredPath;
	}

	

	protected void deleteSelectedFiles() {
		//do in a new thread if children are more than 50
		
		TreePath path[] = removeDescendants(getSelectionPaths());
		if (path != null) {
			
			int result = JOptionPane.showConfirmDialog(this.getFrame(),
					"Delete selected resource(s)?", "Delete Resource(s)",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				DefaultTreeModel model = (DefaultTreeModel)getModel();
				DeleteNodeThread dnt = new DeleteNodeThread(path,model,this.getFrame());
				dnt.start();
			
			}
		}
	}

	//
	// node operations
	//
	protected void removeNodeFromParent(FileNode node) {
		((DefaultTreeModel) getModel()).removeNodeFromParent(node);
	}

	protected void addNode(FileNode parent, FileNode child) {
		((DefaultTreeModel) getModel()).insertNodeInto(child, parent, 0);
		child.setParent(parent);
	}

	//
	// Frame
	//

	protected JFrame getFrame() {
		return frame;
	}

}