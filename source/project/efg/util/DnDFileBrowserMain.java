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
import project.efg.Imports.efgImpl.ImportMenu;
import project.efg.Imports.efgImpl.MediaResourceLocationHandler;

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
	final ImageInfo imageInfo = new ImageInfo();
	private static final long serialVersionUID = 1L;
	static JLabel currentDimLabel; 
	final JButton deleteBtn = new JButton(EFGImportConstants.EFGProperties
			.getProperty("FileTreeBrowserMain.deleteBtn"));

	final JButton doneBtn = new JButton(EFGImportConstants.EFGProperties
			.getProperty("FileTreeBrowserMain.doneBtn"));

	public static int maxDim = 0;

	JProgressBar progressBar = new JProgressBar();

	JComponent imageView;

	JLabel imageLabel;
	JPanel displayPanel;
	URL helpURL;
	private ImagePanel iPanel;

	//JEditorPane htmlPane;
	Vector userItems = new Vector();
	static String thumsStr = "Current Generated Thumbnail Size: "; 
	public static String imageL = EFGImportConstants.EFGProperties
			.getProperty("FileTreeBrowserMain.imageL");

	//
	// Constructor
	//
	FileBrowser browser;
	private String currentImagesDirectory;
	protected ImportMenu importMenu;
	


	/**
	 * 
	 */
	public DnDFileBrowserMain(ImportMenu importMenu, boolean modal) {
		this(importMenu, "", modal);
	}
	private JScrollPane browserPane;
	/**
	 * 
	 * @param frame
	 * @param title
	 * @param modal
	 * @param currentImagesDirectory
	 */
	public DnDFileBrowserMain(ImportMenu importMenu, 
			String title, 
			boolean modal) {
		super(importMenu, modal);
		this.importMenu = importMenu;
		//always set to false
		this.importMenu.setBrowserReload(false);
		this.currentImagesDirectory =
			this.importMenu.getMediaResourcesDirectory();
		this.setTitle("Drag and Drop Image Folders here");
		this.setModal(true);
		imageView = addImagePanel();	
		initHelp();
		this.progressBar.setSize(300, 300);
		//this.reloadBrowser();
		this.iPanel = this.loadPanel();
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
	/**
	 * 
	 * @param currentImagesDirectory
	 * @param frame
	 */
	public DnDFileBrowserMain(String imagesDirectory, ImportMenu frame) {
		this(frame, "", false);
	}
	/**
	 * if !imagesDirectory.equals(currentImagesDirectory)
	 * then reload browser because directory has changed.
	 * 
	 * @param imagesDirectory
	 */
	public void setBrowserReload(String imagesDirectory) {
		
		if(!this.currentImagesDirectory.equalsIgnoreCase(imagesDirectory)) {

			importMenu.setBrowserReload(true);
		
		}
	}

	private ImagePanel loadPanel() {

		this.browser = DnDFileBrowser.getFileBrowser(this.currentImagesDirectory,
				progressBar,this.importMenu);
		this.browser.setRootVisible(false);
		this.browser.setSelectionRow(0);
		
		this.browser.addTreeSelectionListener(new FileTreeSelectionListener(
				this.browser));
		//this.browser.addMouseListener(new EditMouseListener(this));
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
		
		JMenuItem deleteMenu = 
			new JMenuItem("Delete");
		JMenuItem closeMenu = 
			new JMenuItem("Close");
		JMenuItem helpItem = 
			new JMenuItem("Help Contents");

		thumbNailMenu.addActionListener(new ThumbsListener());
		fileMenu.add(thumbNailMenu);
		
		deleteMenu.addActionListener(new DeleteListener(this.browser));
		deleteBtn.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("FileTreeBrowserMain.deleteBtn.tooltip"));
		helpItem.addActionListener(new HelpEFG2ItemListener
				(EFGImportConstants.IMAGE_DEPLOY_HELP));
		
		closeMenu.addActionListener(new DoneListener(this));
		File bitSetter = 
			new File(new File(this.importMenu.getLocalMediaResourceDirectory()),
				EFGImagesConstants.BIT_SET_FILE);
		if(!bitSetter.exists()) {//show only if the bit is not set
			JMenuItem imagesRootDirectoryMenu = 
				new JMenuItem("Change MediaResource Root Directory");
			imagesRootDirectoryMenu.addActionListener(new ChangeImagesRootListener(this));
			
			fileMenu.add(imagesRootDirectoryMenu);
			
		}
		
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
	 * 
	 * @return
	 */
	private JComponent addImagePanel() {
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
	 * @return
	 */
	public static synchronized int getMaxDim() {
		
		if (maxDim <= 0) {
			String maxDimStr = EFGImportConstants.EFGProperties
					.getProperty(EFGImagesConstants.MAX_DIM_STR);


			try {				
				return Integer.parseInt(maxDimStr);
			} catch (Exception ee) {
			}
			
		}
		return maxDim;
	}
	/**
	 * 
	 * @return
	 */
	public static synchronized int[] getDefaultDimensions() {
		
			int[] defaults = null;
			String maxDimStr = EFGImportConstants.EFGProperties
					.getProperty(EFGImagesConstants.MAX_DIM_STR);


			try {
				String[] defaultDims = maxDimStr.split(RegularExpresionConstants.COMMASEP);
				defaults = new int[defaultDims.length];
				for(int i=0; i < defaultDims.length; i++) {
					try {
						int defaultDim = Integer.parseInt(defaultDims[i]);
						defaults[i] = defaultDim;
					}
					catch(Exception ee) {
					}
					
				}
				
				
				log.debug("MaxDim is set from properties file to: " + maxDim);
			} catch (Exception ee) {
				defaults = new int[1];
			
				
			}
			log.debug("MaxDim is set to: " + maxDim);

		return defaults;
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
		JPanel btnPanel =  new JPanel(new GridLayout(0, 1));
		
		
		String currentD = this.currentImagesDirectory;
		JLabel currentDirectory = new JLabel(); 
		currentDirectory.setText("Current Mediaresource directory: " + 
				this.importMenu.getMediaResourcesDirectory());
		/*if(currentD.indexOf(EFGImagesConstants.LOCAL_IMAGES_DIR) > -1) {
			currentDirectory.setText("Current Mediaresource directory: " + 
					new File(importMenu.importMenu.getMediaResourcesDirectory() , 
							EFGImagesConstants.EFG_IMAGES_DIR).getAbsolutePath()+ 
					"     ");
		}
		else {
			currentDirectory.setText("Current Mediaresource directory: "+ 
					importMenu.getMediaResourcesDirectory() +  
					File.separator + EFGImagesConstants.EFG_IMAGES_DIR);
		}*/
		//current directory
		btnPanel.add(currentDirectory);
		
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
		
	
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			EFGThumbNailDimensions thd = 
				new EFGThumbNailDimensions(importMenu,
						"Enter Thumbnail Dimension",true);
			thd.setVisible(true);			
		}
		

	}
	/**
	 * @author kasiedu
	 *
	 */
	class ChangeImagesRootListener implements ActionListener {
		private DnDFileBrowserMain browser;
		/**
		 * 
		 */
		public ChangeImagesRootListener(DnDFileBrowserMain browser) {
			this.browser = browser;
		}
		 /* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			MediaResourceLocationHandler mdr = 
				new MediaResourceLocationHandler(importMenu,"",true,this.browser);
			mdr.setVisible(true);
			if(importMenu.isBrowserReload()) {
				this.browser.close();
			}
		}

	}
	/**
	 * @param currentSelection
	 */
	public static void setCurrentDimLabel(String currentSelection) {
		currentDimLabel.setText(thumsStr + currentSelection);
		
	}
}