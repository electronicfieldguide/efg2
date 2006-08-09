/**
 * 
 */
package project.efg.util;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

/**
 * @author kasiedu
 *
 */
public class DeleteNodeThread extends SwingWorker{
	static Logger log;
	static {
		try {
			log = Logger.getLogger(DeleteNodeThread.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	
	//private boolean isDone = false;
	private TreePath[] path;
	private DefaultTreeModel model;
	 private JFrame dialog;
	 private JProgressBar progressBar;
	 public DeleteNodeThread(TreePath path[],DefaultTreeModel model) {
		 this.model = model;
		 this.path = path;
	        this.progressBar = new JProgressBar();
	        this.progressBar.setStringPainted(true);
	        this.progressBar.setString("");  
	        
	       
	        JPanel panel = new JPanel(new BorderLayout());
	        panel.setSize(400,400);
	        JLabel label = new JLabel("Please wait while selected files are deleted!!", 
	        		SwingConstants.CENTER);
	        panel.add(this.progressBar, BorderLayout.NORTH);
	        panel.add(label,BorderLayout.CENTER);
	     
	      
	    //Create and set up the window.
	    this.dialog = new JFrame("Delete Files");
	   
	    this.dialog.setSize(600,600);
	   
	   
	    //this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    //Create and set up the content pane.
	   
	    panel.setOpaque(true); //content panes must be opaque
	   dialog.getContentPane().add(panel);

	    //Display the window.
	    dialog.pack();
	    dialog.setVisible(true);  
	    }
	
	 public Object construct() {
		if (this.progressBar.isIndeterminate()) {
			progressBar.setIndeterminate(false);
			progressBar.setString(null); // display % string
		}
		progressBar.setString("Please wait while we delete the selected files.....");
		//progressBar.setCursor(null);
	  this.deleteSelectedFiles();
	  Toolkit.getDefaultToolkit().beep();
		//this.progressBar.setValue(0);
		
		String message = "All Files removed !!!";
		this.progressBar.setString(message);
		
		JOptionPane.showMessageDialog(null, message, "Done",
				JOptionPane.INFORMATION_MESSAGE);
		
		this.dialog.dispose();
	  return null;
	}

	private void deleteSelectedFiles(){
		
			for (int i = 0; i < path.length; i++) {
				FileNode fnode = (FileNode) path[i].getLastPathComponent();
				File file = fnode.getFile();
				if (file != null) {
					MutableTreeNode node = (MutableTreeNode) path[i]
							.getLastPathComponent();
					MutableTreeNode parentNode = (MutableTreeNode)node.getParent();
					this.model.removeNodeFromParent(node);
					this.model.reload(parentNode);
					deleteFile(file);
					deleteFromThumbNailsDir(file);
				} else {
					JOptionPane.showMessageDialog(null, "Can't delete!",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
	 }
	
		// if not a known path
		/*
		 * public String getPathName(TreePath path) { Object o =
		 * path.getLastPathComponent(); if (o instanceof FileTree.FileTreeNode) {
		 * return ((FileTree.FileTreeNode)o).getFullName(); } return null; }
		 */
		private void deleteFile(File toDelete) {
			
			if (toDelete.isDirectory()) {
				File[] files = toDelete.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			//also delete from efgthumbnails directory
			this.progressBar.setString(toDelete.getAbsolutePath() + " has been deleted!!!");
			toDelete.delete();
		}
		private void deleteFromThumbNailsDir(File file){
			String imageName = file.getAbsolutePath();
			String thumbsName = replace(imageName,
					EFGImportConstants.EFGIMAGES,
					EFGImportConstants.EFGIMAGES_THUMBS);
			
			File thumbsFile = new File(thumbsName);
			if (thumbsFile.exists()) {
				try {
					deleteFile(thumbsFile);
				} catch (Exception ee) {
					log.error("Could not delete '" + thumbsName + "'");
					return;
				}
			}
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


}
