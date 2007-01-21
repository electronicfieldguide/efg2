/**
 * 
 */
package project.efg.util;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.rdb.LoadSampleData;



/**
 * @author kasiedu
 *
 */
public class CreateSampleDataThread extends SwingWorker 
implements EFGImportConstants,WindowListener{
	static Logger log;
	static {
		try {
			log = Logger.getLogger(CreateSampleDataThread.class);				
		} catch (Exception ee) {
		}
	}
	private boolean isDone;
	private	JFrame frame;
	private JFrame frame2;
	private JPanel panel;
	private JProgressBar progressBar;
	private DBObject dbobject;
	public CreateSampleDataThread(JFrame frame2,DBObject dbobject) {
		this.dbobject = dbobject;
		this.frame2 = frame2;
        this.progressBar = new JProgressBar();
        JLabel label = new JLabel("Please wait while application loads  sample data..");
        label.setSize(300,300);
        this.progressBar.setStringPainted(true);
        this.progressBar.setString("");  
        
       
        this.panel = new JPanel(new BorderLayout());
        this.panel.setSize(400,400);
        this.panel.add(label, BorderLayout.NORTH);
        this.panel.add(this.progressBar, BorderLayout.CENTER);
  
      
	    //Create and set up the window.
	    this.frame = new JFrame("Loading Sample Data.");
	   
	    this.frame.setSize(600,600);
	    this.frame.setLocationRelativeTo(this.frame2);
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
		progressBar.setString("");
		progressBar.setCursor(null);
		progressBar.setIndeterminate(true);

		LoadSampleData sampleData = new LoadSampleData(this.dbobject);
		sampleData.loadData();
		if(sampleData.isError()){
			JOptionPane.showMessageDialog(this.frame2, 
					"Error in Loading Sample Data." +
					" View logs for more details", 
					"Error in Loading Sample Data",
					JOptionPane.ERROR_MESSAGE);
		}
		else {
			EFGImportConstants.EFGProperties.setProperty(
					"efg.sampledata.loaded", EFGImportConstants.EFG_TRUE
					);
		}
		//load stuff here
		Toolkit.getDefaultToolkit().beep();
		this.progressBar.setValue(0);
		this.isDone = true;
		String message = "Sample Data Loaded!!!";
		
		this.frame.dispose();
		JOptionPane.showMessageDialog(this.frame2, message, "Done",
				JOptionPane.INFORMATION_MESSAGE);

		return null;
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
