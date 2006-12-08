package project.efg.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Vector;

import javax.swing.JButton;
//import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.EFGJLabel;
import project.efg.Imports.efgImpl.EFGThumbNailDimensions;

/**
 * @version $Revision$
 * @author Benoît Mahé (bmahe@w3.org)
 */
public class DnDFileBrowserMain extends JDialog {

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
	final JPopupMenu popup = new JPopupMenu();
	
	private static final long serialVersionUID = 1L;
	static JLabel currentDimLabel; 
	final JButton deleteBtn = new JButton(EFGImportConstants.EFGProperties
			.getProperty("FileTreeBrowserMain.deleteBtn"));

	final JButton doneBtn = new JButton(EFGImportConstants.EFGProperties
			.getProperty("FileTreeBrowserMain.doneBtn"));

	public static int maxDim = 0;

	JProgressBar progressBar = new JProgressBar();

	JComponent imageView;

	EFGJLabel imageLabel;

	URL helpURL;

	JEditorPane htmlPane;
	Vector userItems = new Vector();
	static String thumsStr = "Current Thumb size: "; 
	public static String imageL = EFGImportConstants.EFGProperties
			.getProperty("FileTreeBrowserMain.imageL");

	//
	// Constructor
	//
	FileBrowser browser;

	protected JFrame frame;

	//private String currentSelection;
	
	/**
	 * 
	 */
	public DnDFileBrowserMain(JFrame frame, boolean modal,
			String imagesDirectory) {
		this(frame, "", modal, imagesDirectory);
	}
	/**
	 * 
	 * @param frame
	 * @param title
	 * @param modal
	 * @param imagesDirectory
	 */
	public DnDFileBrowserMain(JFrame frame, String title, boolean modal,
			String imagesDirectory) {

		super(frame, modal);
		this.frame = frame;
		this.setTitle("Drag and Drop Image Folders here");

		this.setModal(true);

		imageView = addImagePanel();
		htmlPane = new JEditorPane();
		
		htmlPane.setContentType("text/html");
		htmlPane.setEditable(false);
	
		initHelp();
		this.progressBar.setSize(300, 300);
		this.browser = DnDFileBrowser.getFileBrowser(imagesDirectory,
				progressBar,this.frame);
		this.browser.setRootVisible(false);
		
		this.browser.addTreeSelectionListener(new FileTreeSelectionListener(
				this.browser));
		this.browser.addMouseListener(new EditMouseListener(this));
		JScrollPane browserPane = new JScrollPane(this.browser);
		JScrollPane imageViewPane = new JScrollPane(this.imageView);

		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				browserPane, imageViewPane);

		pane.setOneTouchExpandable(true);
		pane.setDividerLocation(300);
		
		this.addMenus();	
		this.createPopUp();
		this.getContentPane().add(pane, BorderLayout.CENTER);
		this.getContentPane().add(addButtons(), BorderLayout.SOUTH);

		this.setSize(700, 600);
		
		
		addWindowListener(new WndCloser(this));

	}
	/**
	 * 
	 *
	 */
	private void addMenus(){
		
	/*	JMenuItem tmenu = new JMenuItem("Thumbnails");
		tmenu.setMnemonic(KeyEvent.VK_T);	
		tmenu.addActionListener(new DeployImagesListener(this,true));
		
		
		
		fmenu.add(tmenu);
		fmenu.addSeparator();

		JMenuItem cmenuItem = new JMenuItem("Close", KeyEvent.VK_C);
		cmenuItem.addActionListener(new ExitListener(this.frame));

		fmenu.add(cmenuItem);
	*/
		JMenu fileMenu = new JMenu("File");
		//JMenu toolsMenu = new JMenu("Tools");
		JMenu helpMenu = new JMenu("Help");
		
		//menu Items
		//prompt for dimension input
		//don't prompt use last value
		//
		
		JMenuItem thumbNailMenu = new JMenuItem("Thumbnails");
		thumbNailMenu.addActionListener(new ThumbsListener());
	
		JMenuItem deleteMenu = new JMenuItem("Delete");
		deleteMenu.addActionListener(new DeleteListener(this.browser));
		deleteBtn.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("FileTreeBrowserMain.deleteBtn.tooltip"));
		JMenuItem closeMenu = new JMenuItem("Close");
			
		//checkbox - prompt me for thumbnail size
		//if user says don't ask me again uncheck this..Serialize checkbox
	//	JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("Use last thumbnail size");
		//thumbNailMenu.add(cbMenuItem);
		
		fileMenu.add(thumbNailMenu);
		fileMenu.add(deleteMenu);
		
	

		
		
		
		
		JMenuItem helpItem = new JMenuItem("Help Contents");
		helpItem.addActionListener(new HelpEFG2ItemListener(EFGImportConstants.IMAGE_DEPLOY_HELP));
		helpMenu.add(helpItem);
		fileMenu.addSeparator();
		closeMenu.addActionListener(new DoneListener(this));
		//sub menus
		
		
		
		fileMenu.add(closeMenu);
		JMenuBar mBar = new JMenuBar();
		mBar.add(fileMenu);
		mBar.add(helpMenu);
	
	
		this.setJMenuBar(mBar);
		
	}
	/**
	 * 
	 * @param imagesDirectory
	 * @param frame
	 */
	public DnDFileBrowserMain(String imagesDirectory, JFrame frame) {
		this(frame, "", false, imagesDirectory);
	}
	/**
	 * 
	 *
	 */
	public void close() {
		//comboList.save(EFGImportConstants.THUMBS_FILE_NAME);
		this.dispose();
	}
	/**
	 * 
	 * @return
	 */
	private JComponent addImagePanel() {
		// Create the HTML viewing pane.
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.progressBar.setSize(300, 300);
		this.progressBar.setStringPainted(true);
		this.progressBar.setString(""); // but don't paint it
		this.imageLabel = new EFGJLabel(imageL);
		panel.add(this.imageLabel);

		return panel;
	}
	/**
	 * 
	 * @param currentSelection
	 */
	public static void setCurrentDim(String currentSelection){
		try{
			maxDim =Integer.parseInt(currentSelection);
		}
		catch(Exception ee){
			
		}
	}
	/**
	 * 
	 * @param newMaxDim
	 */
	public static synchronized void setMaxDim(int newMaxDim) {
		maxDim = newMaxDim;
	}
	/**
	 * 
	 * @return
	 */
	public static synchronized int getMaxDim() {
		
		if (maxDim <= 0) {
			String maxDimStr = EFGImportConstants.EFGProperties
					.getProperty(EFGImportConstants.MAX_DIM_STR);

			if ((maxDimStr == null)) {
				maxDimStr = EFGImportConstants.MAX_DIM;
			}
			try {
				maxDim = Integer.parseInt(maxDimStr);
				log.debug("MaxDim is set from properties file to: " + maxDim);
			} catch (Exception ee) {
				try {
					maxDimStr = EFGImportConstants.MAX_DIM;
					maxDim = Integer.parseInt(maxDimStr);
				} catch (Exception eex) {
					log.error(eex.getMessage());
					log.error("Default max dimension is not set!!!");
				}
			}
			log.debug("MaxDim is set to: " + maxDim);
		}
		return maxDim;
	}
	/**
	 * 
	 * @return
	 */
	private JPanel addButtons() {
		//get Max Dim
		//unserialiaze and set the max dimension
		EFGComboBox efgComboBox = new EFGComboBox();
		efgComboBox.deserialize(EFGImportConstants.THUMBS_FILE_NAME);
		String maxDim = (String)efgComboBox.getSelectedItem();
		
		currentDimLabel = new JLabel(thumsStr + maxDim + " ", JLabel.LEADING);
		currentDimLabel.setForeground(Color.blue);
		JPanel btnPanel = new JPanel();
		
		btnPanel.add(currentDimLabel);
		return btnPanel;

	}
	/**
	 * 
	 *
	 */
	private void initHelp() {

		helpURL = this.getClass().getResource(
				EFGImportConstants.IMAGE_DEPLOY_HELP);
		if (helpURL == null) {
			log.error("Couldn't open help file: "
					+ EFGImportConstants.IMAGE_DEPLOY_HELP);
			return;
		}
		displayURL(helpURL);
	}
	/**
	 * 
	 * @param url
	 */
	private void displayURL(URL url) {
		try {
			if (url != null) {
				htmlPane.setPage(url);
			} else { // null url
				htmlPane.setText("File Not Found");
			}
		} catch (Exception e) {
			log.error("Attempted to read a bad URL: " + url);
		}
	}
	/**
	 * 
	 *
	 */
	public void createPopUp() {

		JMenuItem menuItem = new JMenuItem(this.deleteBtn.getText());
		menuItem.addActionListener(new DeleteListener(this.browser));
		this.popup.add(menuItem);
	}
	/**
	 * 
	 * @author jacob.asiedu
	 *
	 */
	class  WndCloser extends WindowAdapter{
		/**
		 * 
		 */
		private DnDFileBrowserMain main;
		/**
		 *	 
		 * @param main
		 */
		public WndCloser(DnDFileBrowserMain main) {
			this.main = main;
		}
		/**
		 * 
		 */
		public void windowClosing(WindowEvent e) {	
			this.main.close();			
		}
	}
	class EditMouseListener extends MouseAdapter {

		private DnDFileBrowserMain treeBrowser;
		/**
		 * 
		 * @param treeBrowser
		 */
		public EditMouseListener(DnDFileBrowserMain treeBrowser) {
			this.treeBrowser = treeBrowser;
		}
		/**
		 * 
		 */
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopUp(e);
			}
		}
		/**
		 * 
		 * @param e
		 */
		private void showPopUp(MouseEvent e) {
			TreePath path = browser.getPathForLocation(e.getX(), e.getY());
			if (path != null) {
				browser.getSelectionModel().setSelectionPath(path);
				popup.show(this.treeBrowser, e.getX(), e.getY());
			}
		}
		/**
		 * 
		 */
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopUp(e);
			}
		}

	}

	
	class FileTreeSelectionListener implements TreeSelectionListener {
		private FileBrowser tree;

		public FileTreeSelectionListener(FileBrowser tree) {
			super();
			this.tree = tree;
		}

		public void valueChanged(TreeSelectionEvent e) {
			try {
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
					imageLabel.setEFGJLabel(path, getMaxDim());
					imageLabel.setLocation(60, 60);
					imageLabel.repaint();
				}
			} catch (Exception ee) {
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

	
	class ThumbsListener implements ActionListener {
		
	
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			EFGThumbNailDimensions thd = 
				new EFGThumbNailDimensions(frame,
						"Enter Thumbnail Dimension",true);
			thd.setVisible(true);			
		}
		

	}
	/**
	 * @param currentSelection
	 */
	public static void setCurrentDimLabel(String currentSelection) {
		currentDimLabel.setText(thumsStr + currentSelection);
		
	}
}