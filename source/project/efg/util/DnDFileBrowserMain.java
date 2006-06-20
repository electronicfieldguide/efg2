package project.efg.util;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.EFGJLabel;




/**
 * @version $Revision$
 * @author Benoît Mahé (bmahe@w3.org)
 */
public class DnDFileBrowserMain extends  JDialog {


	/**
	 * 
	 */
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(DnDFileBrowserMain.class);
		} catch (Exception ee) {
		}
	}
	private static final long serialVersionUID = 1L;
	static int maxDim = 0;
	JProgressBar progressBar = new JProgressBar();
	JComponent imageView;
	EFGJLabel imageLabel;
	public static String imageL = 
		EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.imageL");

	//
	// Constructor
	//
	FileBrowser browser;
	protected JFrame frame;
	public DnDFileBrowserMain(JFrame frame, boolean modal,String imagesDirectory) {
		this(frame, "", modal, imagesDirectory);
	}
	
	public DnDFileBrowserMain(String imagesDirectory, JFrame frame) {
		this(frame, "", false, imagesDirectory);
	}
	public void close() {
		this.dispose();
	}
	public DnDFileBrowserMain(JFrame frame, String title, boolean modal,
			String imagesDirectory) {
		
	
		this.frame = frame;
		this.setTitle("Drag and Drop Image Folders here");
	
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		ToolTipManager.sharedInstance().setInitialDelay(0);
		this.setModal(true);
	
		imageView = addImagePanel();
		this.browser = DnDFileBrowser.getFileBrowser(imagesDirectory,progressBar);
		ToolTipManager.sharedInstance().registerComponent(this.browser);
		
		this.browser.addTreeSelectionListener(new FileTreeSelectionListener(
				this.browser));
	
		JScrollPane scrollpane = new JScrollPane(this.browser);
		
		JScrollPane scrollpane2 = new JScrollPane(this.imageView);
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			scrollpane, scrollpane2);
		
		pane.setOneTouchExpandable(true);
		pane.setDividerLocation(300);
		this.getContentPane().add(pane, BorderLayout.CENTER);
		this.setSize(600, 600);
	
	}
	
	
	public JComponent addImagePanel() {
		// Create the HTML viewing pane.
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
	    this.progressBar.setStringPainted(true);
        this.progressBar.setString("");          //but don't paint it
		this.imageLabel = new EFGJLabel(imageL);
		this.imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(this.imageLabel, BorderLayout.CENTER);
		
		return panel;
	}

  public static int getMaxDim(){
		
		if(maxDim <= 0){
			String maxDimStr = 
				EFGImportConstants.EFGProperties.getProperty(EFGImportConstants.MAX_DIM_STR);
		
			if((maxDimStr == null)){
				maxDimStr = EFGImportConstants.MAX_DIM;
			}
			try{
				maxDim = Integer.parseInt(maxDimStr);
				log.debug("MaxDim is set from properties file to: " + maxDim);
			}
			catch(Exception ee){
				try{
				maxDimStr = EFGImportConstants.MAX_DIM;
				maxDim = Integer.parseInt(maxDimStr);
				}
				catch(Exception eex){
					log.error(eex.getMessage());
					log.error("Default max dimension is not set!!!");
				}
			}
			log.debug("MaxDim is set to: " + maxDim);
		}
		
		return maxDim;
	}
  
	class FileTreeSelectionListener implements TreeSelectionListener {
		private FileBrowser tree;
		
		public FileTreeSelectionListener(FileBrowser tree) {
			super();
			this.tree = tree;
		}
		public void valueChanged(TreeSelectionEvent e) {
			try{
				Object o = tree.getLastSelectedPathComponent();
				
				
				if (o instanceof FileNode) {// only do this when it
												// is an instance
					FileNode node = (FileNode) o;
					String path = node.getFile().getAbsolutePath();
					if (path == null) {
						imageLabel.setText(imageL);
					} else {
						imageLabel.setText("");
					}
					imageLabel.setEFGJLabel(path,getMaxDim());
					imageLabel.setLocation(60,60);
					imageLabel.repaint();
				}
			}
			catch(Exception ee){
				imageLabel.setText("Image too large to draw");
			}
		}
	}
}