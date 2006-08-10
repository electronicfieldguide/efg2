package project.efg.util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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
	final JButton deleteBtn = 
		new JButton(EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.deleteBtn"));

	final JButton doneBtn =
		new JButton(EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.doneBtn"));

	static int maxDim = 0;
	JProgressBar progressBar = new JProgressBar();
	
	JComponent imageView;
	EFGJLabel imageLabel;
	URL helpURL;
	JEditorPane htmlPane;
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
	
	public DnDFileBrowserMain(JFrame frame, String title, boolean modal,
			String imagesDirectory) {
		
		super(frame,modal);
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
		htmlPane = new JEditorPane();
	    htmlPane.setEditable(false);
	    initHelp();
	    this.progressBar.setSize(300,300);
		this.browser = DnDFileBrowser.getFileBrowser(imagesDirectory,progressBar);
		this.browser.setRootVisible(false);
		ToolTipManager.sharedInstance().registerComponent(this.browser);
		
		this.browser.addTreeSelectionListener(new FileTreeSelectionListener(
				this.browser));
		
	
		JScrollPane browserPane = new JScrollPane(this.browser);
		JScrollPane imageViewPane = new JScrollPane(this.imageView);		
		JScrollPane htmlViewPane = new JScrollPane(htmlPane);
	
	        //Add the scroll panes to a split pane.
	        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
	        		imageViewPane,htmlViewPane);
	        splitPane.setDividerLocation(200);
	        JScrollPane otherPane = new JScrollPane(splitPane);
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			browserPane, otherPane);
		
		pane.setOneTouchExpandable(true);
		pane.setDividerLocation(300);
		this.getContentPane().add(pane, BorderLayout.CENTER);
		this.getContentPane().add(addButtons(),BorderLayout.SOUTH);
		
		this.setSize(1100, 600);
		
	}

	public DnDFileBrowserMain(String imagesDirectory, JFrame frame) {
		this(frame, "", false, imagesDirectory);
	}
	public void close() {
		this.dispose();
	}
	public JComponent addImagePanel() {
		// Create the HTML viewing pane.
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.progressBar.setSize(300,300);
	    this.progressBar.setStringPainted(true);
	    this.progressBar.setString("");          //but don't paint it
		this.imageLabel = new EFGJLabel(imageL);
		panel.add(this.imageLabel);
		
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

	private JPanel addButtons(){
	

		JPanel btnPanel = new JPanel();

		deleteBtn.addActionListener(new DeleteListener(this.browser));
		deleteBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.deleteBtn.tooltip"));
		btnPanel.add(deleteBtn);


		doneBtn.addActionListener(new DoneListener(this));
		doneBtn.setToolTipText(
				EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.doneBtn.tooltip")
				);
		btnPanel.add(doneBtn);
		return btnPanel;
		
	}
	 private void initHelp() {
		
	        helpURL = this.getClass().getResource(EFGImportConstants.IMAGE_DEPLOY_HELP);
	        if (helpURL == null) {
	            log.error("Couldn't open help file: " + EFGImportConstants.IMAGE_DEPLOY_HELP);
	            return;
	        } 
	        displayURL(helpURL);
	    }
	private void displayURL(URL url) {
        try {
            if (url != null) {
                htmlPane.setPage(url);
            } else { //null url
            	htmlPane.setText("File Not Found");
            }
        } catch (Exception e) {
            log.error("Attempted to read a bad URL: " + url);
        }
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
	class DeleteListener implements ActionListener {
		private FileBrowser tree;

		public DeleteListener(FileBrowser tree) {
			this.tree = tree;
		}

		public void actionPerformed(ActionEvent evt) {
			this.tree.deleteSelectedFiles();
		}

	}

	class DoneListener implements ActionListener {
		private DnDFileBrowserMain treeBrowser;

		public DoneListener(DnDFileBrowserMain dndFile) {
			this.treeBrowser = dndFile;
		}

		public void actionPerformed(ActionEvent evt) {
			this.treeBrowser.close();
		}
	}
}