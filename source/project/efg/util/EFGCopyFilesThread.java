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

/*
 * ProgressBarDemo.java is a 1.4 application that requires these files:
 *   LongTask.java
 *   SwingWorker.java
 */
public class EFGCopyFilesThread extends SwingWorker implements WindowListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

    private JProgressBar progressBar;
   
  
    private DnDFileBrowser browser;
    boolean isDone = false;
   File srcFile, destFile;
   JFrame frame;
  
    public EFGCopyFilesThread(DnDFileBrowser browser, File srcFile, File destFile, JProgressBar progressBar) {
    	
    	this.srcFile = srcFile;
    	
    	this.destFile = destFile;
        this.browser = browser;
        this.progressBar = progressBar;
      
        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(550,500);
        JLabel label = new JLabel("Please wait while application copies image files!!", 
        		SwingConstants.CENTER);
        label.setSize(300,300);
        panel.add(this.progressBar, BorderLayout.NORTH);
        panel.add(label,BorderLayout.CENTER);
    //Make sure we have nice window decorations.
     //   JFrame.setDefaultLookAndFeelDecorated(true);
        
    
    //Create and set up the window.
    this.frame = new JFrame("Copying Files");
    this.frame.addWindowListener(this);
    this.frame.setSize(600, 600);
    this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.frame.setLocationRelativeTo(this.browser);
    //Create and set up the content pane.
   
    panel.setOpaque(true); //content panes must be opaque
    this.frame.getContentPane().add(panel);
  
    //Display the window.
    this.frame.pack();
    this.frame.setVisible(true);  
    }
	public Object construct() {
		progressBar.setString("Please wait while files are copied.....");
		progressBar.setCursor(null);
		this.browser.copyFile(this.srcFile, this.destFile);
		
		Toolkit.getDefaultToolkit().beep();
		this.progressBar.setValue(0);
		this.progressBar.setString("Copying done!!!");
		//String message = "Copying done";
		//JOptionPane.showMessageDialog(this.frame, message, "Copying Done",
			//	JOptionPane.INFORMATION_MESSAGE);
		
		CreateThumbNailsThread thumbsThread = new CreateThumbNailsThread(srcFile,destFile);
	    thumbsThread.start();
	    isDone = true;
		this.frame.dispose();
		this.browser.setVisible(true);
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		if(!isDone){
			JOptionPane.showMessageDialog(this.frame, "Application is copying image files. Interrupting it could cause some problems!", "Done",
					JOptionPane.INFORMATION_MESSAGE);
		}
		
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
		
		
	}
	
	

}
