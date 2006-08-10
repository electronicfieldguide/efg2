/**
 * 
 */
package project.efg.util;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

/**
 * @author kasiedu
 *
 */
public class CreateThumbNailsThread extends SwingWorker implements EFGImportConstants,WindowListener{
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
	private ThumbNailGenerator thm;
	private boolean isDone = false;
	String srcFile, destFile;
	   JFrame frame;
	private JProgressBar progressBar;
	 public CreateThumbNailsThread(File srcFile, File destFile) {
	    	
	    	this.srcFile = srcFile.getAbsolutePath();
	    	
	    	this.destFile = this.replace(destFile.getAbsolutePath(), EFGIMAGES, EFGIMAGES_THUMBS);
	    	
	    	this.maxDim =DnDFileBrowserMain.getMaxDim();
			log.debug("Max Dim: " + this.maxDim);
			this.thm = new ThumbNailGenerator();
	        this.progressBar = new JProgressBar();
	        this.progressBar.setStringPainted(true);
	        this.progressBar.setString("");  
	        this.progressBar.setMinimum(0);
	        //this.progressBar.setSize(350,350);
	        //this.progressBar.setMinimumSize(new Dimension(200,200));
	        JPanel panel = new JPanel(new BorderLayout());
	        //panel.setSize(400,400);
	        JLabel label = new JLabel("Please wait while application generates Thumbnails!!", 
	        		SwingConstants.CENTER);
	        panel.add(this.progressBar, BorderLayout.NORTH);
	        panel.add(label,BorderLayout.CENTER);
	     
	        //Make sure we have nice window decorations.
	        //JFrame.setDefaultLookAndFeelDecorated(true);
	  
	    
	    //Create and set up the window.
	    this.frame = new JFrame("Generating Thumbnails");
	   
	    this.frame.setSize(600,600);
	   
	    frame.addWindowListener(this);
	    this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    //Create and set up the content pane.
	   
	    panel.setOpaque(true); //content panes must be opaque
	    frame.getContentPane().add(panel);

	    //Display the window.
	    frame.pack();
	    frame.setVisible(true);  
	    }
	

	public Object construct() {
		if (this.progressBar.isIndeterminate()) {
			progressBar.setIndeterminate(false);
			progressBar.setString(null); // display % string
		}
		progressBar.setString("Please wait while we generate thumbnails for uploaded images.....");
		progressBar.setCursor(null);
		this.generateThumbs(new File(this.srcFile),new File(this.destFile));
		
		Toolkit.getDefaultToolkit().beep();
		this.progressBar.setValue(0);
		this.isDone = true;
		String message = "ThumbNail Generation done!!!";
		this.progressBar.setString(message);
		
		JOptionPane.showMessageDialog(this.frame, message, "Done",
				JOptionPane.INFORMATION_MESSAGE);
		
		this.frame.dispose();
		return null;
	}
	private void generateThumbs(File srcFile, File destFile){
		
		
		try {
			
			 progressBar.setString("Please wait while  the application generates thumbnails...");
				
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
					 log.debug("SrcDir: " + srcDir);
					 log.debug("destDir: " + destDir);
					log.debug("FileName: " + fileName);
					
					 progressBar.setString("Generating thumbnails for : '" + srcFile.getAbsolutePath() + "'");
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
						progressBar.setString("Thumbnails for '" + srcFile.getAbsolutePath() + "' successfully done");
					 }
					 else{
						 progressBar.setString("Could not generate thumbnails for '" + srcFile.getAbsolutePath() + "'");
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
	 *            The name of the urrent image file
	 * @param maxDim -
	 *            The maximum dimension for the thumnail image
	 */
	private boolean generate(String srcDir, String destDir, String fileName) {
		if (check(srcDir, fileName)) {
			return this.thm.generateThumbNail(srcDir, destDir,fileName, this.maxDim);
		}
		return false;
	}
	// Check for existance of image
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
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


}
