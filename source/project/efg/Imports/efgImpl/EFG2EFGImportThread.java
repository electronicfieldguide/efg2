/**
 * 
 */
package project.efg.Imports.efgImpl;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

import project.efg.exports.UnZipImport;
import project.efg.util.SwingWorker;

/**
 * @author jacob.asiedu
 *
 */
public class EFG2EFGImportThread extends SwingWorker{
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFG2EFGImportThread.class);
		} catch (Exception ee) {
		}
	}

	
	private String title;
	private JFrame frame;

	private JProgressBar progressBar;
	private JPanel panel;
	private JFrame frame1;
	private UnZipImport unzip;
	public EFG2EFGImportThread(
			UnZipImport unzip, 
			JFrame frame, 
			String title) {
		
		this.unzip = unzip;
        JLabel label = new JLabel("Please wait while your data is imported ...");
        label.setSize(300,300);

		this.progressBar = new JProgressBar();
		
		this.frame = frame;
		this.title = title;
		
		this.progressBar.setStringPainted(true);
	    this.progressBar.setString("");  
	        	       
        this.panel = new JPanel(new BorderLayout());
        this.panel.setSize(400,400);
        this.panel.add(label, BorderLayout.NORTH);
        this.panel.add(this.progressBar, BorderLayout.CENTER);
        this.frame1 = new JFrame();
        frame1.setTitle(this.title);
        this.frame1.setSize(600,600);
        this.frame1.setLocationRelativeTo(this.frame);
        frame1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame1.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
        this.frame1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //Create and set up the content pane.
       
        panel.setOpaque(true); //content panes must be opaque
        frame1.getContentPane().add(panel);

        //Display the window.
        frame1.pack();
        frame1.setVisible(true);
	}
	private void close(){
		this.frame1.dispose();
	}
	/* (non-Javadoc)
	 * @see project.efg.util.SwingWorker#construct()
	 */
	public Object construct() {
		if (this.progressBar.isIndeterminate()) {
			progressBar.setIndeterminate(false);
			progressBar.setString(null); // display % string
		}
		progressBar.setString("");
		progressBar.setCursor(null);
		progressBar.setIndeterminate(true);
		
		try {
			this.unzip.processZipFile();
			
		Toolkit.getDefaultToolkit().beep();
		this.progressBar.setValue(0);
		this.frame1.dispose();
		String message = "Importation done!!!";
		
		
		JOptionPane.showMessageDialog(this.frame, message, "Done",
				JOptionPane.INFORMATION_MESSAGE);
		
		} catch (IOException e) {
			
			log.error(e.getMessage());
			JOptionPane.showMessageDialog(this.frame, e.getMessage(),
					"Error Message", JOptionPane.ERROR_MESSAGE);

		}	
	;
		return "";
	}

}
