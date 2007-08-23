package project.efg.client.utils.gui;

import java.awt.Cursor;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import project.efg.client.utils.nogui.Sorter;


/**
 * @version $Revision: 1.1.1.1 $
 * @author Benoît Mahé (bmahe@w3.org)
 */
public class FileNode implements MutableTreeNode {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(FileNode.class);
		} catch (Exception ee) {
		}
	}
	protected String name = null;

	protected FilenameFilter filter = null;

	protected File file = null;

	protected FileNode parent = null;

	protected Vector children = null;

	protected FileBrowser browser = null;

	// Children

	/**
	 * Child Constructor
	 * 
	 * @param browser
	 *            the FileBrowser
	 * @param parent
	 *            the parent Node
	 * @param file
	 *            the encapsuled file
	 * @param filter
	 *            the Filename filter
	 */
	public FileNode(FileBrowser browser, FileNode parent, File file) {
		this.browser = browser;
		this.file = file;
		this.filter = parent.filter;
		this.name = file.getName();
		this.parent = parent;
	}

	/**
	 * Root Node Constructor
	 * 
	 * @param name
	 *            the root node name
	 * @param filter
	 *            the Filename filter
	 */
	public FileNode(String name, FilenameFilter filter) {
		this.name = name;
		this.file = null;
		this.parent = null;
		this.filter = filter;
		this.children = new Vector(3);
		
		try {
			
			File root = new File(name);
			
				try{
					if (root.canRead()) { 
						//this.name = root.getName();
						//String path = root.getAbsolutePath();
						FileNode node = new FileNode(this, root, filter);
						//node.setName(path);
						node.setName(root.getName());
						children.addElement(node);
					}
				}
				catch(SecurityException xxx){
					log.error(xxx.getMessage());
				}
				catch(Exception eee){
					log.error(eee.getMessage());
				}
			
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
	}

	/**
	 * Internal Constructor
	 * 
	 * @param parent
	 *            the parent Node
	 * @param file
	 *            the encapsuled file
	 * @param filter
	 *            the Filename filter
	 */
	public FileNode(FileNode parent, File file, FilenameFilter filter) {
		this.file = file;
		this.filter = filter;
		this.name = file.getName();
		this.parent = parent;
	}

	/**
	 * Invoked whenever this node is about to be expanded.
	 */
	public void nodeWillExpand() {
		children = null;
	}

	/**
	 * Invoked whenever this node is about to be collapsed.
	 */
	public void nodeWillCollapse() {
		// children = null;
	}

	//
	// Constructors
	//

	public void initializeRootNode(FileBrowser browser) {
		
		if (parent == null) { // we are a root node
			this.browser = browser;
			for (int i = 0; i < children.size(); i++) {
				FileNode node = (FileNode) children.elementAt(i);
				node.setBrowser(browser);
			}
		}
	}

	/**
	 * Returns the child TreeNode at index childIndex.
	 * 
	 * @param childIndex
	 *            the index of the child to return
	 * @return a TreeNode instance
	 */
	public TreeNode getChildAt(int childIndex) {
		acquireChildren();
		return (TreeNode) children.elementAt(childIndex);
	}

	/**
	 * Returns the number of children TreeNodes the receiver contains.
	 * 
	 * @return the number of children TreeNodes the receiver contains
	 */
	public int getChildCount() {
		acquireChildren();
		return children.size();
	}

	/**
	 * Returns the parent TreeNode of the receiver.
	 * 
	 * @return a TreeNode
	 */
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * Returns the index of node in the receivers children. If the receiver does
	 * not contain node, -1 will be returned.
	 * 
	 * @return an int.
	 */
	public int getIndex(TreeNode node) {
		acquireChildren();
		return children.indexOf(node);
	}

	/**
	 * Returns true if the receiver allows children.
	 * 
	 * @return an int.
	 */
	public boolean getAllowsChildren() {
		return (parent == null ? true : file.isDirectory());
	}

	/**
	 * Returns true if the receiver is a leaf.
	 * 
	 * @return a boolean
	 */
	public boolean isLeaf() {
		return (parent == null ? false : file.isFile());
	}

	/**
	 * Returns the children of the reciever as an Enumeration.
	 * 
	 * @return an Enumeration
	 */
	public Enumeration children() {
		acquireChildren();
		return children.elements();
	}

	// MutableTreeNode part

	/**
	 * Adds child to the receiver at index. child will be messaged with
	 * setParent.
	 * 
	 * @param child
	 *            the child to add.
	 * @param index
	 *            the index of the new child.
	 */
	public void insert(MutableTreeNode child, int index) {
		acquireChildren();
		children.insertElementAt(child, index);
	}

	/**
	 * Removes the child at index from the receiver.
	 * 
	 * @param the
	 *            index of the child to remove.
	 */
	public void remove(int index) {
		acquireChildren();
		children.remove(index);
	}

	/**
	 * Removes node from the receiver. setParent will be messaged on node
	 * 
	 * @param node
	 *            the node to remove
	 */
	public void remove(MutableTreeNode node) {
		children.remove(node);
	}

	/**
	 * Resets the user object of the receiver to object.
	 * 
	 * @param object
	 *            the new user object, actually the new identifier.
	 */
	public void setUserObject(Object object) {
		if (object instanceof String) {
			if (file != null) {
				String newname = (String) object;
				File newfile = new File(file.getParent(), newname);
				if (file.renameTo(newfile)) {
					name = newname;
					file = newfile;
					updateChildrenFile();
				}
			}
		}
	}

	/**
	 * Removes the receiver from its parent.
	 */
	public void removeFromParent() {
		if (parent != null){
			parent.remove(this);
		}
	}

	/**
	 * Sets the parent of the receiver to newParent.
	 * 
	 * @param newParent
	 *            the new parent.
	 */
	public void setParent(MutableTreeNode newParent) {
		this.parent = (FileNode) newParent;
		if (parent.hasFile()) {
			this.file = new File(parent.getFile(), name);
			if (children == null){
				loadChildren();
			}
			else{
				updateChildrenFile();
			}
		}
	}

	// Object part

	/**
	 * Return the string reoresentation of this node.
	 * 
	 * @return its name
	 */
	public String toString() {
		return name;
	}

	/**
	 * Get the file
	 * 
	 * @return the encapsuled file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Is the node associated to a file
	 */
	public boolean hasFile() {
		return (file != null);
	}

	/**
	 * Get the node name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public boolean equals(FileNode node) {
		if (file == null){
			return (!node.hasFile());
		}
		return file.equals(node.getFile());
	}

	// Children

	/**
	 * Load the children of this node and sort them, first directory then files
	 * in alphabetical order.
	 */
	protected synchronized void loadChildren() {
		children = new Vector(0);
		if (file == null){
			return;
		}
		File files[] = file.listFiles(filter);
		if (files == null){
			return;
		}
		// sort
		int len = files.length;
		Vector vfiles = new Vector(len);
		for (int i = 0; i < len; i++) {
			Sorter.orderedFileInsert(files[i], vfiles);
		}
		// create Nodes
		children = new Vector(files.length);
		for (int i = 0; i < len; i++) {
			children.addElement(new FileNode(browser, this, (File) vfiles
					.elementAt(i)));
		}
	}

	protected synchronized void updateChildrenFile() {
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				FileNode child = (FileNode) children.elementAt(i);
				child.updateFile();
			}
		}
	}

	protected void updateFile() {
		if (parent != null) {
			file = new File(parent.getFile(), name);
		}
		updateChildrenFile();
	}

	protected void setCursor(int cursor) {
		if (browser != null){
			browser.setCursor(cursor);
		}
	}

	/**
	 * Load the children if needed.
	 */
	protected void acquireChildren() {
		if (children == null) {
			setCursor(Cursor.WAIT_CURSOR);
			loadChildren();
			setCursor(Cursor.DEFAULT_CURSOR);
		}
	}

	/**
	 * Set the node's name
	 * 
	 * @param name
	 *            the node name
	 */
	protected void setName(String name) {
		this.name = name;
	}

	protected void setBrowser(FileBrowser browser) {
		this.browser = browser;
	}

	protected boolean isOwner(FileBrowser browser) {
		return (this.browser == browser);
	}

	protected FileNode findNode(File tofind) {
		if (hasFile() && (file.equals(tofind))){
			return this;
		}
		acquireChildren();
		String path = tofind.getAbsolutePath();
		for (int i = 0; i < children.size(); i++) {
			FileNode node = (FileNode) children.elementAt(i);
			String nodepath = node.getFile().getAbsolutePath();
			if (path.startsWith(nodepath)){
				return node.findNode(tofind);
			}
		}
		return null;
	}

}
