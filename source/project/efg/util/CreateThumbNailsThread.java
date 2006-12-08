/**
 * 
 */
package project.efg.util;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.efg.Imports.efgInterface.ThumbNailGeneratorInterface;

/**
 * @author kasiedu
 *
 */
public class CreateThumbNailsThread extends SwingWorker 
implements EFGImportConstants,WindowListener{
	static Logger log;
	static {
		try {
			log = Logger.getLogger(CreateThumbNailsThread.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	private int maxDim = -1;
	private ThumbNailGeneratorInterface thm;
	private boolean isDone = false;
	String srcFile, destFile;
	JFrame frame;
	   JPanel panel;
	private JProgressBar progressBar;
	private FileNode destNode;
	   private DnDFileBrowser browser;
	private List objectsToDrop;
	 public CreateThumbNailsThread( DnDFileBrowser browser,File srcFile, File destFile) {
		 this(browser,srcFile,destFile,null);
	 }
	    	
	

	public CreateThumbNailsThread(DnDFileBrowser browser,File srcFile, File destFile, FileNode node) {
    		
		this.objectsToDrop = new ArrayList();
    	DropFileObject drop = new DropFileObject(srcFile,destFile, destNode);
    	this.objectsToDrop.add(drop);
        this.browser = browser;
     
        this.init();
    }
	/**
	 * @param browser2
	 * @param list
	 */
	public CreateThumbNailsThread(DnDFileBrowser browser2, List list) {
		this.browser = browser2;
		this.objectsToDrop = list;
		this.init();
	}
	private void init(){
		this.maxDim =DnDFileBrowserMain.getMaxDim();
		
		
		//do spring stuff here
		//this.thm = ThumbNailGeneratorInterface.Factory.newInstance();
		this.thm = this.doSpring();
        this.progressBar = new JProgressBar();
        JLabel label = new JLabel("Please wait while application generates Thumbnails");
        label.setSize(300,300);
        this.progressBar.setStringPainted(true);
        this.progressBar.setString("");  
        
       
        this.panel = new JPanel(new BorderLayout());
        this.panel.setSize(400,400);
        this.panel.add(label, BorderLayout.NORTH);
        this.panel.add(this.progressBar, BorderLayout.CENTER);
  
  
    
    //Create and set up the window.
    this.frame = new JFrame("Generating Thumbnails");
   
    this.frame.setSize(600,600);
    this.frame.setLocationRelativeTo(this.browser);
    frame.addWindowListener(this);
    this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    //Create and set up the content pane.
   
    panel.setOpaque(true); //content panes must be opaque
    frame.getContentPane().add(panel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);  
	}


	



	/**
	 * @return
	 */
	private ThumbNailGeneratorInterface doSpring() {
		try {
		ApplicationContext    context = 
			new ClassPathXmlApplicationContext("springconfig.xml");
		return (ThumbNailGeneratorInterface)context.getBean("thumbsGenerator");
		}
		catch(Exception ee) {
			log.error(ee.getMessage());
			ee.printStackTrace();
		}
		return null;
	}



	public Object construct() {
		if (this.progressBar.isIndeterminate()) {
			progressBar.setIndeterminate(false);
			progressBar.setString(null); // display % string
		}
		progressBar.setString("");
		progressBar.setCursor(null);
		progressBar.setIndeterminate(true);

		for(int i = 0; i < this.objectsToDrop.size();i++){
			DropFileObject drop = (DropFileObject)this.objectsToDrop.get(i);
			if(this.destNode == null){
				
				this.destNode = drop.getDestinationNode();
			
			}
		
			this.srcFile = drop.getSourceFile().getAbsolutePath();
			File destFile1 = drop.getDestinationFile();
			this.destFile = this.replace(destFile1.getAbsolutePath(), EFGIMAGES, EFGIMAGES_THUMBS);
			this.generateThumbs(new File(this.srcFile),new File(this.destFile));
		}
		Toolkit.getDefaultToolkit().beep();
		this.progressBar.setValue(0);
		this.isDone = true;
		String message = "ThumbNail Generation done!!!";
		
		this.frame.dispose();
		JOptionPane.showMessageDialog(this.browser.frame, message, "Done",
				JOptionPane.INFORMATION_MESSAGE);
		FileNode root = (FileNode)((DefaultTreeModel)this.browser.getModel()).getRoot();

		if(this.destNode != null){	
			
			if(this.destNode.getParent() != null){
			
				
				int rows[] = this.browser.getSelectionRows();
				int row = rows[0];
				FileNode selPath = (FileNode)this.browser.getSelectionPath().getLastPathComponent();
				((DefaultTreeModel)this.browser.getModel()).reload(selPath.getParent());
				this.browser.expandRow(row);
			}
			else{
				
				((DefaultTreeModel)this.browser.getModel()).reload();//(root);
			}
		}
		else{
			
			if(root.getChildCount() > 0){
				this.browser.expandRow(1);
			}
			else{
				this.browser.expandRow(0);
			}
			((DefaultTreeModel)this.browser.getModel()).reload();
		}
		this.browser.setVisible(true);
		return this.destNode;
	}
	/**
	 * Expands a given node in a JTree.
	 *
	 * @param tree      The JTree to expand.
	 * @param model     The TreeModel for tree.     
	 * @param node      The node within tree to expand.     
	 * @param row       The displayed row in tree that represents
	 *                  node.     
	 * @param depth     The depth to which the tree should be expanded. 
	 *                  Zero will just expand node, a negative
	 *                  value will fully expand the tree, and a positive
	 *                  value will recursively expand the tree to that
	 *                  depth relative to node.
	 */
	public int expandJTreeNode (javax.swing.JTree tree,
	                                   javax.swing.tree.TreeModel model,
	                                   Object node, int row, int depth)
	{
		
	    if (node != null  &&  !model.isLeaf(node)) {
	        tree.expandRow(row);
	        if (depth != 0)
	        {
	            for (int index = 0;
	                 row + 1 < tree.getRowCount()  &&  
	                            index < model.getChildCount(node);
	                 index++)
	            {
	                row++;
	                Object child = model.getChild(node, index);
	                if (child == null)
	                    break;
	                javax.swing.tree.TreePath path;
	                while ((path = tree.getPathForRow(row)) != null  &&
	                        path.getLastPathComponent() != child)
	                    row++;
	                if (path == null)
	                    break;
	                row = expandJTreeNode(tree, model, child, row, depth - 1);
	            }
	        }
	    }
	    return row;
	}
	private void generateThumbs(File srcFile, File destFile){
		
		
		try {
			
			 progressBar.setString("");
				
				 if(srcFile.isDirectory()){	
					// destFile.mkdirs();
					 destFile = new File(destFile,srcFile.getName()); 
					 File[] list = srcFile.listFiles();
					
					 for(int i = 0; i < list.length; i++){
						 File file = list[i];
						
						generateThumbs(file,destFile);
					 }
				 }
				 else{
					 String srcDir = srcFile.getParent();
					 String fileName = srcFile.getName();
					 
					 destFile = new File(destFile,srcFile.getName()); 
					 String destDir = destFile.getParent();
					
					 boolean isTemp = false;
					 try{
						isTemp = this.generate(srcDir,destDir,fileName);
						if(!isTemp){
							log.error("Thumbnails for " + srcDir + File.separator + fileName + " could not be created!!");
						}
					 }
					 catch(Exception ee){
					 }
					
					 if(isTemp){
					 }
					 else{
							log.error("Could not generate thumbnails for '" + srcFile.getAbsolutePath() + "'");
					 }
				 }
		}
		catch (Exception e) {
		
		  log.error( e.getMessage());
			
		} 
	}
	/**
	 * Adapted from Filter code @
	 * @param srcImagesDir -
	 *            The full path to where all images reside
	 * @param destImagesDir -
	 *            Where the transformed images will be placed
	 * @param srcFileName -
	 *            The name of the current image file
	 * @param maxDim -
	 *            The maximum dimension for the thumnail image
	 */
	private boolean generate(String srcDir, String destDir, String fileName) {
		if (check(srcDir, fileName)) {
			//use a factory here
			return this.thm.generateThumbNail(srcDir, destDir,fileName, this.maxDim);
		}
		return false;
	}
	// Check for existence of image
	private boolean check(String dir, String fileName) {
		File checker = new File(dir,fileName);
		return checker.exists();
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
		if(!isDone){
			JOptionPane.showMessageDialog(this.frame, "Thumbnail generation is in process. Interrupting it could cause some problems!", "Done",
					JOptionPane.INFORMATION_MESSAGE);
		}
		
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
