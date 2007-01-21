package project.efg.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
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
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.EFGThumbNailDimensions;
import project.efg.Imports.efgImpl.ImagePanel;
import project.efg.Imports.efgImportsUtil.PreferencesListener;


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
	private static final long serialVersionUID = 1L;
	final private JPopupMenu popup = new JPopupMenu();
	final private ImageInfo imageInfo = new ImageInfo();

	private JLabel currentDimLabel; 
	final private JButton deleteBtn = new JButton(EFGImportConstants.EFGProperties
			.getProperty("FileTreeBrowserMain.deleteBtn"));

	final private JButton doneBtn = new JButton(EFGImportConstants.EFGProperties
			.getProperty("FileTreeBrowserMain.doneBtn"));

	JProgressBar progressBar = new JProgressBar();

	JComponent imageView;

	JLabel imageLabel;
	JPanel displayPanel;
	URL helpURL;
	private ImagePanel iPanel;

	//JEditorPane htmlPane;
	Vector userItems = new Vector();
	String thumsStr = "Current Generated Thumbnail Size: "; 
	public String imageL = EFGImportConstants.EFGProperties
			.getProperty("FileTreeBrowserMain.imageL");

	//
	// Constructor
	//
	FileBrowser browser;
	private String currentImagesDirectory;
	protected JFrame importMenu;
	


	private JScrollPane browserPane;
	/**
	 * 
	 * @param frame
	 * @param title
	 * @param modal
	 */
	public DnDFileBrowserMain(JFrame importMenu, 
			String title, 
			boolean modal) {
		super(importMenu, modal);
		this.importMenu = importMenu;
		//always set to false
	
		this.currentImagesDirectory = 
			this.computeCurrentMediaResourceDirectory();
		this.setTitle("Drag and Drop Image Folders here");
		this.setModal(true);
		imageView = addImageDisplayPanel();	
		initHelp();
		this.progressBar.setSize(300, 300);
		
		this.iPanel = this.addTreeBkgdImagePanel();
		this.browserPane = new JScrollPane(this.iPanel);
		JScrollPane imageViewPane = new JScrollPane(this.imageView);
		JSplitPane pane = 
			new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				browserPane, imageViewPane);
		pane.setOneTouchExpandable(true);
		pane.setDividerLocation(300);
		this.addMenus();	
		this.createPopUp();
		this.getContentPane().add(pane, BorderLayout.CENTER);
		this.getContentPane().add(addButtons(), BorderLayout.SOUTH);
		this.setSize(700, 600);
		addWindowListener(new WndCloser(this));
		this.setLocationRelativeTo(importMenu);
	}
	private String computeCurrentMediaResourceDirectory() {
		String property = 
			EFGImportConstants.EFGProperties.getProperty("efg.mediaresources.home.current");
		
		if(property == null || property.trim().equals("")) {
			WorkspaceResources.computeMediaResourcesHome();
		}
		property = 
			EFGImportConstants.EFGProperties.getProperty("efg.mediaresources.home.current");
		if(property == null || property.trim().equals("")) {
			this.close();
		}
		return property;
	}
	/**
	 * 
	 * @return
	 */
	private JComponent addImageDisplayPanel() {
		// Create the HTML viewing pane.
		displayPanel = new JPanel();
		displayPanel.setLayout(new BorderLayout());
		this.progressBar.setSize(300, 300);
		this.progressBar.setStringPainted(true);
		this.progressBar.setString(""); // but don't paint it
		this.imageLabel = new JLabel(imageL, SwingConstants.CENTER);
		this.imageLabel.setVerticalTextPosition(JLabel.BOTTOM);
		this.imageLabel.setHorizontalTextPosition(JLabel.CENTER);
		this.displayPanel.add(this.imageLabel, BorderLayout.CENTER);
	
		return displayPanel;
	}



	private ImagePanel addTreeBkgdImagePanel() {

		this.browser = DnDFileBrowser.getFileBrowser(this.currentImagesDirectory,
				progressBar,this.importMenu);
		this.browser.setRootVisible(false);
		this.browser.setSelectionRow(0);
		
		this.browser.addTreeSelectionListener(new FileTreeSelectionListener(
				this.browser));
		
		this.browser.expandRow(0);

		ImagePanel iPanel = 
			new ImagePanel(EFGImportConstants.IMAGE_DROP_BACKGROUND_IMAGE);
		iPanel.setLayout(new BorderLayout());
		iPanel.add(this.browser,BorderLayout.CENTER);
		iPanel.setBackground(Color.white);
	
		
		this.browser.setOpaque(false);
		iPanel.setOpaque(true);
		return iPanel;
	}

	/**
	 * 
	 *
	 */
	private void addMenus(){
		JMenu fileMenu = 
			new JMenu("File");		
		JMenu helpMenu = 
			new JMenu("Help");		
		JMenuItem thumbNailMenu = 
			new JMenuItem("Thumbnails");
		JMenuItem preferencesMenu = 
			new JMenuItem("Change/View Preferences");

		JMenuItem deleteMenu = 
			new JMenuItem("Delete");
		JMenuItem closeMenu = 
			new JMenuItem("Close");
		JMenuItem helpItem = 
			new JMenuItem("Help Contents");

		thumbNailMenu.addActionListener(new ThumbsListener(this));
		preferencesMenu.addActionListener(new PreferencesListener(this.importMenu, false, true));
		
		fileMenu.add(thumbNailMenu);
		fileMenu.add(preferencesMenu);
		deleteMenu.addActionListener(new DeleteListener(this.browser));
		deleteBtn.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("FileTreeBrowserMain.deleteBtn.tooltip"));
		helpItem.addActionListener(new HelpEFG2ItemListener
				(EFGImportConstants.IMAGE_DEPLOY_HELP));
		
		closeMenu.addActionListener(new DoneListener(this));
		
		
		/*	JMenuItem imagesRootDirectoryMenu = 
				new JMenuItem("Change Server Root Directory");
			imagesRootDirectoryMenu.addActionListener(
					new ChangeServerRootListener(this.importMenu));
			
			fileMenu.add(imagesRootDirectoryMenu);*/
			
		
		
		fileMenu.add(deleteMenu);
		helpMenu.add(helpItem);
		
		fileMenu.addSeparator();	
		fileMenu.add(closeMenu);
		
		JMenuBar mBar = new JMenuBar();
		mBar.add(fileMenu);
		mBar.add(helpMenu);
		this.setJMenuBar(mBar);
	}
	/**
	 * 
	 *
	 */
	public void close() {
		this.dispose();
	}

	/**
	 * @param currentSelection
	 */
	protected void setCurrentDimLabel(String currentSelection) {
		this.currentDimLabel.setText(thumsStr + currentSelection);
	}


	private void setDefaultThumbnailDimensions() {
	
			String[] dimensions = WorkspaceResources.getDefaultDimensions();
			String maxDim = null;
			StringBuffer buffer = new StringBuffer();
			for(int i= dimensions.length;i > 0;i--) {

				if(i < dimensions.length) {
					buffer.append(",");
				}
				buffer.append(dimensions[i-1]);
			}
			EFGImportConstants.EFGProperties.setProperty(
					"efg.thumbnails.dimensions.lists",
					buffer.toString());
			EFGImportConstants.EFGProperties.setProperty(
					"efg.thumbnails.dimensions.current",
					dimensions[dimensions.length -1]);
		
	}
	
	/**
	 * 
	 * @return
	 */
	private JPanel addButtons() {
		
		String maxDim = 
			EFGImportConstants.EFGProperties.getProperty("efg.thumbnails.dimensions.current");
		
		if(maxDim == null || maxDim.trim().equals("")) {
			setDefaultThumbnailDimensions();
			maxDim = 
				EFGImportConstants.EFGProperties.getProperty("efg.thumbnails.dimensions.current");
		}
		this.currentDimLabel = new JLabel(thumsStr + maxDim + " ", JLabel.LEADING);
		this.currentDimLabel.setForeground(Color.blue);
		JPanel btnPanel =  new JPanel(new GridLayout(0, 1));
		
		
		
		JLabel currentDirectory = new JLabel(); 
		currentDirectory.setText("Current Mediaresource directory: " + 
				WorkspaceResources.toFileName(this.currentImagesDirectory));
		
		btnPanel.add(currentDirectory);
		
		btnPanel.add(this.currentDimLabel);
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
					
						path = path.replaceAll(EFGImagesConstants.EFG_IMAGES_DIR,
								EFGImagesConstants.EFGIMAGES_THUMBS);

						File imageFile = new File(path);
						if(imageFile.isDirectory()){//not an image
							return;
						}
						BufferedImage image = javax.imageio.ImageIO.read(imageFile);
						StringBuffer buffer = 
							new StringBuffer("<html>");
						buffer.append(ImageInfo.getFileInfoHtml(imageFile,imageInfo));
						buffer.append("</html>");
						
						
						imageLabel.setIcon(new ImageIcon(image));
						imageLabel.setText(buffer.toString());
					}					
				}
			} catch (Exception ee) {
				imageLabel.setIcon(null);
				imageLabel.setText("");
			}
			displayPanel.revalidate();
		}
	}

	class DeleteListener implements ActionListener {
		private FileBrowser tree;

		public DeleteListener(FileBrowser tree) {
			this.tree = tree;
		}

		public void actionPerformed(ActionEvent evt) {
			imageLabel.setIcon(null);
			imageLabel.setText("");
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
		private DnDFileBrowserMain dnd;
		/**
		 * 
		 */
		public ThumbsListener(DnDFileBrowserMain dnd) {
			this.dnd = dnd;
		}
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			EFGThumbNailDimensions thd = 
				new EFGThumbNailDimensions(importMenu,
						"Enter Thumbnail Dimension",true);
			thd.setVisible(true);
			
			String currentDim = EFGImportConstants.EFGProperties.getProperty(
			"efg.thumbnails.dimensions.current");
			this.dnd.setCurrentDimLabel(currentDim);
		}
	}


}