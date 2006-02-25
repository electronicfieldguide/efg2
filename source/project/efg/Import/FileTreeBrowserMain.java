package project.efg.Import;

import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * FileTreeBrowserMain.java
 *
 *
 * Created: Sat Feb 18 16:41:14 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */
public class FileTreeBrowserMain extends JDialog {
  
    FileTree tree;
    FileTreeBrowser target;
    final JButton deleteBtn = new JButton("Delete File/Folder");
    final JButton doneBtn = new JButton("Done");

    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(FileTreeBrowserMain.class); 
	}
	catch(Exception ee){
	}
    }
    public FileTreeBrowserMain(JFrame frame, 
			       boolean modal,
			       String imagesDirectory) {
	this(frame,"",modal,imagesDirectory);
    }
    public FileTreeBrowserMain(String imagesDirectory){
	this(null,"",false,imagesDirectory);
    }
    public FileTreeBrowserMain(JFrame frame, 
			       String title,
			       boolean modal, 
			       String imagesDirectory) {
	super(frame,title,modal);
	setSize(new Dimension(500, 400));
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    close();
		}
	    });
	try{
	    this.tree = new FileTree(imagesDirectory);
	    this.target = new FileTreeBrowser(tree);
	    initTree();
	}
	catch(Exception ee){
	    System.out.println(ee.getMessage());
	}
	JComponent newContentPane = addPanel();
	setContentPane(newContentPane);
	//read catalina home environment variable
    } // FileTreeBrowserMain constructor
    public void close(){
	this.dispose();
    }
    public void initTree(){
	this.tree.getSelectionModel().setSelectionMode(
						       TreeSelectionModel.SINGLE_TREE_SELECTION);
	this.tree.setEditable(true);
    }
    private JPanel addPanel(){
	JPanel panel = new JPanel(new BorderLayout());
	

	JPanel btnPanel = new JPanel();
	deleteBtn.addActionListener(new DeleteListener(this.tree));
	btnPanel.add(deleteBtn);
	
	doneBtn.addActionListener(new DoneListener(this)); 
	btnPanel.add(doneBtn);
	
	
	panel.add(new JScrollPane(this.tree), BorderLayout.CENTER);
	panel.add(btnPanel, BorderLayout.SOUTH);
	return panel;
    }
    class DeleteListener implements ActionListener {
	private FileTree tree;

	public DeleteListener(FileTree tree) {
	    this.tree = tree;
	}
	public void actionPerformed(ActionEvent evt) {
	    removeSelectedNode(this.tree);
	}
	/**
	 * This method removes the selected node from the JTree
	 */
	protected void removeSelectedNode(FileTree tree) 
	{
	    //get the selected node
	    DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
	    
	    if (selNode != null) { 
		//get the parent of the selected node
		MutableTreeNode parent = (MutableTreeNode)(selNode.getParent());
        
		// if the parent is not null
		if (parent != null) 
		    {
			TreePath selectionPath =tree.getSelectionPath();
			//get the sibling node to be selected after removing the
			//selected node
			MutableTreeNode toBeSelNode = getSibling(selNode);
                
			//if there are no siblings select the parent node after removing the node
			if(toBeSelNode == null){
			    toBeSelNode = parent;
			}
			//make the node visible by scrolling to it
			//DefaultTreeModel getModel()
			TreeNode[] nodes = ((DefaultTreeModel)tree.getModel()).getPathToRoot(toBeSelNode);
			TreePath path = new TreePath(nodes); 
			tree.scrollPathToVisible(path); 
			tree.setSelectionPath(path);
			String fileName = getPathName(selectionPath);
			File file = new File(fileName);
			if(file.exists()){
			    try{
				//delete from file system
				deleteFile(file);
				//remove the node from the parent
				((DefaultTreeModel)tree.getModel()).removeNodeFromParent(selNode);
			    }
			    catch(Exception ee){
				System.err.println(ee.getMessage());
			    }
			}
			else{
			    System.out.println("File: " + fileName + " does not exists");
			}
			
		    }  
		else{
		    JOptionPane.showMessageDialog(
						  null,
						  "You are not allowed to delete the root directory of " + 
						  "the images folder!! ", 
						  "Warning Message",
						  JOptionPane.WARNING_MESSAGE
						  );
		}
	    }
	    else{
		JOptionPane.showMessageDialog(
					      null,
					      "Please select a directory to delete!! ",
					      "Error Message",
					      JOptionPane.ERROR_MESSAGE
					      ); 
	    }
	}
	 // Returns the full pathname for a path, or null
    // if not a known path
    public String getPathName(TreePath path) {
	Object o = path.getLastPathComponent();
	if (o instanceof FileTree.FileTreeNode) {
	    return ((FileTree.FileTreeNode)o).getFullName();
	}
	return null; 
    } 
	protected void deleteFile(File toDelete){
	    if(toDelete.isDirectory()){
		File[] files =  toDelete.listFiles();
		for(int i = 0; i < files.length; i++){
		    deleteFile(files[i]);
		}
	    }
	    toDelete.delete();
	}
	/**
	 * This method returns the previous sibling node 
	 * if there is no previous sibling it returns the next sibling
	 * if there are no siblings it returns null
	 * 
	 * @param selNode selected node
	 * @return previous or next sibling, or parent if no sibling
	 */
	protected MutableTreeNode getSibling(DefaultMutableTreeNode selNode)
	{
	    //get previous sibling
	    MutableTreeNode sibling = (MutableTreeNode)selNode.getPreviousSibling();
	    if(sibling == null){
		//if previous sibling is null, get the next sibling
		sibling    = (MutableTreeNode)selNode.getNextSibling();
	    }
	    return sibling;
	} 
    }
    class DoneListener implements ActionListener{
	private FileTreeBrowserMain treeBrowser;
	public 	DoneListener(FileTreeBrowserMain treeBrowser){
	    this.treeBrowser = treeBrowser;
	}
	public void actionPerformed(ActionEvent evt) {
	    this.treeBrowser.close();
	}
    }
} // FileTreeBrowserMain
