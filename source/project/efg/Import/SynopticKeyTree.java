package project.efg.Import;
import project.efg.efgInterface.EFGDatasourceObjectInterface;
import javax.swing.JTree;


import javax.swing.*;
import javax.swing.tree.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.util.*;
import javax.swing.event.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * SynopticKeyTree.java
 *
 * Borrowed from  SynopticKeyTree.java in javaswing by Marc Loy et al
 * Edited : Mon Feb 20 09:35:00 2006
 * Edited for EFG by
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
 * @version 1.0
 */
public class SynopticKeyTree extends JTree {
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(SynopticKeyTree.class); 
	}
	catch(Exception ee){
	}
    }
    DefaultTreeModel model;
    private DBObject dbObject;
    // protected DefaultTreeModel model;
    protected EFGDatasourceObjectList lists; 

    public SynopticKeyTree(		   
			   DBObject dbObject){
	super((TreeModel)null); 
	this.init(dbObject); 
    }
    public SynopticKeyTree(TreeModel newModel,
			   DBObject dbObject){
	super(newModel); 
	this.init(dbObject); 
    }

    public SynopticKeyTree(TreeNode root,	
			   DBObject dbObject){
	this(root,false,dbObject);
    }
    public SynopticKeyTree(TreeNode root, 
			   boolean asks, 
			   DBObject dbObject){
	super(root, asks); 
	this.init(dbObject); 
    }
  
    private void init(DBObject dbObject) {
	putClientProperty("JTree.lineStyle", "Angled");
	this.getSelectionModel().setSelectionMode(
						  TreeSelectionModel.SINGLE_TREE_SELECTION);
	this.setEditable(false);
	//this.setEditable(true);
	this.setShowsRootHandles(true);

	this.dbObject = dbObject;
	//create the root node
	this.createRootNode();
	
	//createOtherNodes
	this.createNodesFromDB();
	setDragEnabled(false); 
	setTransferHandler(new SynopticKeyTransfer());
	addTreeExpansionListener(new TreeExpansionHandler());
    }
    protected void createRootNode(){
	EFGDatasourceObjectInterface rootDS = new EFGDatasourceObject();
	rootDS.setDisplayName(project.efg.util.EFGImportConstants.EFG_DATABASE_ROOT_NAME);
	rootDS.setMetadataName(project.efg.util.EFGImportConstants.EFG_DATABASE_METADATA_NAME);
	rootDS.setDataName(project.efg.util.EFGImportConstants.EFG_DATABASE_DATA_NAME);
	DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootDS);
	this.model = new DefaultTreeModel(root);
	this.setModel(this.model);
    }
    public void createNodesFromDB(){
	//get a list of Database objects
	//the root node is already created at this point
	this.lists = 
	    new EFGDatasourceObjectList(this.dbObject);

	Iterator iter = this.lists.getEFGDatasourceObjectListIterator();
	boolean isEmpty = true;
	
	//for each element create a TreeNode element and add to tree
	DefaultTreeModel model = (DefaultTreeModel)this.getModel();
	DefaultMutableTreeNode root = ( DefaultMutableTreeNode)model.getRoot();
	while (iter.hasNext()) {
	    EFGDatasourceObjectInterface obj = (EFGDatasourceObjectInterface)iter.next();
	    root.add(new DefaultMutableTreeNode(obj));
	    isEmpty = false;
	}
	model.reload();
    }
    /**
     * This method returns the previous sibling node 
     * if there is no previous sibling it returns the next sibling
     * if there are no siblings it returns null
     * 
     * @param selNode selected node
     * @return previous or next sibling, or parent if no sibling
     */
    protected MutableTreeNode getSibling(DefaultMutableTreeNode selNode){
	//get previous sibling
	MutableTreeNode sibling = (MutableTreeNode)selNode.getPreviousSibling();
	if(sibling == null){
	    //if previous sibling is null, get the next sibling
	    sibling    = (MutableTreeNode)selNode.getNextSibling();
	}
	return sibling;
    } 
    public void updateSelectedNode(){
	String message = "";
	//get the selected node
	DefaultMutableTreeNode selNode = 
	    (DefaultMutableTreeNode)this.getLastSelectedPathComponent(); 
	
	if (selNode != null) { 
	    //get the parent of the selected node
	    MutableTreeNode parent = (MutableTreeNode)(selNode.getParent());
	    // if the parent is not null
	    if (parent != null) {
		TreePath selectionPath =this.getSelectionPath();
		//get the sibling node to be selected after removing the
		//selected node
		MutableTreeNode toBeSelNode = getSibling(selNode);
		//if there are no siblings select the parent node after removing the node
		if(toBeSelNode == null){
		    toBeSelNode = parent;
		}
		//make the node visible by scrolling to it
		TreeNode[] nodes = ((DefaultTreeModel)this.getModel()).getPathToRoot(toBeSelNode);
		TreePath path = new TreePath(nodes); 
		this.scrollPathToVisible(path); 
		this.setSelectionPath(path);
		EFGDatasourceObjectInterface ds =
		    (EFGDatasourceObjectInterface)selNode.getUserObject();
		System.out.println("DisplayName: " + ds.getDisplayName());
		
		/*if(this.lists.removeEFGDatasourceObject(ds)){
		    ((DefaultTreeModel)this.getModel()).removeNodeFromParent(selNode);
		}
		else{
		    message = "Cannot be removed from database"; 
		    log.error(message);
		    JOptionPane.showMessageDialog(
					  null,
					  message,
					  "Error Message",
					  JOptionPane.ERROR_MESSAGE
					  ); 
		    
					  }*/
	    } 
	    else{
		message = "You are not allowed to edit the Database display name!!! " ;
		log.info(message);
		JOptionPane.showMessageDialog(
					      null,
					      message,
					      "Warning Message",
					      JOptionPane.WARNING_MESSAGE
					      );
	    }
	}
	else{
	    message = "Please select a datasource and edit the " + 
		" display name before you click on this button!! ";
	    log.error(message);
	    JOptionPane.showMessageDialog(
					  null,
					  message,
					  "Error Message",
					  JOptionPane.ERROR_MESSAGE
					  ); 
	}
    }
    /**
     * This method removes the selected node from the JTree
     */
    public void removeSelectedNode() 
    {
	String message = "";
	//get the selected node
	DefaultMutableTreeNode selNode = 
	    (DefaultMutableTreeNode)this.getLastSelectedPathComponent(); 
	
	if (selNode != null) { 
	    //get the parent of the selected node
	    MutableTreeNode parent = (MutableTreeNode)(selNode.getParent());
	    // if the parent is not null
	    if (parent != null) {
		TreePath selectionPath =this.getSelectionPath();
		//get the sibling node to be selected after removing the
		//selected node
		MutableTreeNode toBeSelNode = getSibling(selNode);
		//if there are no siblings select the parent node after removing the node
		if(toBeSelNode == null){
		    toBeSelNode = parent;
		}
		//make the node visible by scrolling to it
		TreeNode[] nodes = ((DefaultTreeModel)this.getModel()).getPathToRoot(toBeSelNode);
		TreePath path = new TreePath(nodes); 
		this.scrollPathToVisible(path); 
		this.setSelectionPath(path);
		EFGDatasourceObjectInterface ds =
		    (EFGDatasourceObjectInterface)selNode.getUserObject();
		if(this.lists.removeEFGDatasourceObject(ds)){
		    ((DefaultTreeModel)this.getModel()).removeNodeFromParent(selNode);
		}
		else{
		    message = "Cannot be removed from database"; 
		    log.error(message);
		    JOptionPane.showMessageDialog(
					  null,
					  message,
					  "Error Message",
					  JOptionPane.ERROR_MESSAGE
					  ); 
		    
		}
	    } 
	    else{
		message = "You are not allowed to delete the Database!!! " ;
		log.info(message);
		JOptionPane.showMessageDialog(
					      null,
					      message,
					      "Warning Message",
					      JOptionPane.WARNING_MESSAGE
					      );
	    }
	}
	else{
	    message = "Please select a datasource to delete!! ";
	    log.error(message);
	    JOptionPane.showMessageDialog(
					  null,
					  message,
					  "Error Message",
					  JOptionPane.ERROR_MESSAGE
					  ); 
	}
    }
    public EFGDatasourceObjectInterface importIntoDatabase(File file){
	String fileName = file.getName();
	int index = fileName.lastIndexOf(".");
	String displayName = fileName;
	if(index > -1){
	    displayName = fileName.substring(0,index);
	}
	String dataName = displayName + "Data";
	String metadataName = displayName + "Info";
	EFGDatasourceObjectInterface obj = new  EFGDatasourceObject();
	obj.setDisplayName(displayName);
	obj.setMetadataName(metadataName);
	obj.setDataName(dataName);
	if(this.lists.contains(obj)){
	    int res = JOptionPane.showOptionDialog(this,
						   "A datasource called\n   " + fileName +
						   "\nalready exists in the EFG Database\n   " +
						   "\nReplace it?",
						   "Datasource Exists",
						   JOptionPane.DEFAULT_OPTION,
						   JOptionPane.QUESTION_MESSAGE,
						   null, new String[] {
						       "Yes","No", "Cancel"
						   }, 
						   "No");
	    switch (res) {
	    case 0:  // Yes 
		break;
	    case 1:  // No
		return null;
	    default: // Cancel
		return null;
	    } //prompt user
	}
	boolean bool = this.lists.addEFGDatasourceObject(obj);
	if(bool){
	    return obj;
	}
	return null;
    }
    // Inner class that handles Tree Expansion Events
    protected class TreeExpansionHandler implements TreeExpansionListener {
	public void treeExpanded(TreeExpansionEvent evt) {
	    TreePath path = evt.getPath();      // The expanded path
	    JTree tree = (JTree)evt.getSource();  // The tree
	    
	    // Get the last component of the path and
	    // arrange to have it fully populated.
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
	    ((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);  
	}

	public void treeCollapsed(TreeExpansionEvent evt) {
	    // Nothing to do
	}
    }
    public class SynopticKeyTransfer extends TransferHandler {
	public SynopticKeyTransfer(){
	}
	public boolean importData(JComponent comp, Transferable t) {
	    String message = "";
	    // Make sure we have the right starting points
	    if (!(comp instanceof SynopticKeyTree)) {
		return false;
	    }
	    if (!t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
		return false;
	    }

	    // Grab the tree, its model and the root node
	    SynopticKeyTree tree = (SynopticKeyTree)comp;
	    DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
	    DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
	    try {
		List data = (List)t.getTransferData(DataFlavor.javaFileListFlavor);
		Iterator i = data.iterator();
		while (i.hasNext()) {
		    boolean isFound = false;
		    File f = (File)i.next();
		    //if file is a directory log and skip
		    if(f.isDirectory()){
			message = f.getAbsolutePath() + " is a directory and will not be imported!!";
			log.error(message);
			JOptionPane.showMessageDialog(
						      null,
						      message,
						      "Error Message",
						      JOptionPane.ERROR_MESSAGE
						      ); 
			continue;
		    }
		    EFGDatasourceObjectInterface obj = tree.importIntoDatabase(f);
		    if(obj != null){
			int childCount = root.getChildCount();
			for (int j = 0; j < childCount; j++) {
			    DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(j);
			    if (((EFGDatasourceObjectInterface)node.getUserObject()).equals(obj)){
				isFound = true;
				break;
			    }
			}
		    }
		    else{
			continue;
		    }
		    if(isFound){
			continue;
		    }
		    // Add a new node
		    try {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(obj);
			root.add(node);
		    } catch (Exception e) {
			message = e.getMessage();
			log.error(message);
			JOptionPane.showMessageDialog(
						      null,
						      message,
						      "Error Message",
						      JOptionPane.ERROR_MESSAGE
						      ); 
		    }
		}
		model.reload();
		return true;
	    }
	    catch (UnsupportedFlavorException ufe) {
		log.error(ufe.getMessage());
	    }
	    catch (IOException ioe) {
		message = ioe.getMessage();
		log.error(message);
		JOptionPane.showMessageDialog(
					      null,
					      message,
					      "Error Message",
					      JOptionPane.ERROR_MESSAGE
					      ); 
	    }
	    return false;
	}
	
	// We only support file lists on SynopticKeyTrees...
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
	    if (comp instanceof SynopticKeyTree) {
		for (int i = 0; i < transferFlavors.length; i++) {
		    if (!transferFlavors[i].equals(DataFlavor.javaFileListFlavor)) {
			return false;
		    }
		}
		return true;
	    }
	    return false;
	}
    }
    class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)
		(e.getTreePath().getLastPathComponent());
	    
            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */
            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)
                       (node.getChildAt(index));
            } catch (NullPointerException exc) {}

            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
}
