package project.efg.Imports.efgImpl;
/**
 * $Id$
 * $Name$
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu, Kimmy Lin
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2, or
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 */
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgImportsUtil.LoggerUtils;
import project.efg.Imports.efgInterface.EFGWebAppsDirectoryInterface;
import project.efg.Imports.factory.EFGWebAppsDirectoryFactory;
import project.efg.util.DnDFileBrowserMain;
import project.efg.util.EFGImagesConstants;
import project.efg.util.EFGImportConstants;

import com.Ostermiller.util.Browser;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;


/**
 * ImportMenu.java
 * 
 * 
 * Created: Sat Feb 18 10:38:07 2006
 * 
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */
public class ImportMenu extends JFrame {
	static final long serialVersionUID = 1;
	final BevelBorder   bevel = new BevelBorder(BevelBorder.RAISED);
	final EmptyBorder empty = new EmptyBorder(5, 5, 5, 5);
      
	private String pathToServer = null;

	private EFGWebAppsDirectoryInterface webappsDirectory;
	boolean isLocalTomcatServerExists = false;
	final public static Hashtable imageCacheTable = new Hashtable();
	private static GeneralCacheAdministrator cacheAdmin;
		
	DnDFileBrowserMain fileBrowser;
	SerializeDeserializeHandler serializerManager;
	private DBObject dbObject;
	private boolean browserreload=false;
  
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ImportMenu.class);
			Browser.init();
		} catch (Exception ee) {
		}
	}

	public ImportMenu(String title) {
		this(EFGImportConstants.IMPORT_TITLE, null);
	}

	public ImportMenu() {
		this(EFGImportConstants.IMPORT_TITLE, null);
	}

	public ImportMenu(String title, String pathToserVer) {
		this(title, pathToserVer, null);

	}
	public void close() {
		this.dispose();
		System.exit(0);
	}

	public  static GeneralCacheAdministrator getCacheAdmin(){
		if(cacheAdmin == null){
			cacheAdmin = new GeneralCacheAdministrator();
			cacheAdmin.setAlgorithmClass("com.opensymphony.oscache.base.algorithm.LRUCache");
			cacheAdmin.setCacheCapacity(1000);
		}
		return cacheAdmin;
	}
	public ImportMenu(String title, String pathToserVer, DBObject dbObject) {
		super(title);
		URL url = null;
		
		try {
			url = this.getClass().getResource(EFGImportConstants.EFG_TINY_ICON);
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image image = kit.getImage(url);
			this.setIconImage(image);
		} catch (Exception ee) {
			
		}
		this.serializerManager = 
			new SerializeDeserializeHandler();
		this.pathToServer = pathToserVer;
		//set the catalina home 
		EFGUtils.setCatalinaHome(this.pathToServer);
		//setSize(new Dimension(220, 150));
		setSize(new Dimension(400, 400));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		this.setMediaResourcesDirectory(true);

		this.dbObject = dbObject;
		if(this.dbObject == null){
			log.error("DBObject is null inside ImportMenu");
		}
	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(this.createContentPane());
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	
	} 
	public void setMediaResourcesParentDirectory(String parentDirectory) {
		this.webappsDirectory.setPathToServer(parentDirectory);
		this.webappsDirectory.setImagesDirectory(EFGImagesConstants.EFG_IMAGES_DIR);
	}
	// ImportMenu constructor
	public String getMediaResourcesDirectory() {
		return this.webappsDirectory.getImagesDirectory();
	}
	public String getLocalMediaResourceDirectory() {
		URL url = this.getClass().getResource("/resource/" + EFGImagesConstants.LOCAL_IMAGES_DIR);
		String dir = null;
		try {
			dir = URLDecoder.decode(url.getFile(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// 
			e.printStackTrace();
		}
		
		return dir;
	}
	public String getLocalTomcatServerPath() {
		return (this.pathToServer + File.separator
		+ project.efg.util.EFGImportConstants.EFG_WEB_APPS
		+ File.separator + project.efg.util.EFGImportConstants.EFG_APPS);

	}
	public boolean isLocatTomcatExists(boolean isInit) {
		String path = this.getLocalTomcatServerPath()+ 
		File.separator + EFGImagesConstants.EFG_IMAGES_DIR;
		
		File f1 = new File(path);
		//bit set?
		File bitSetter = 
			new File(new File(getLocalMediaResourceDirectory()),
				EFGImagesConstants.BIT_SET_FILE);
		
		if(f1.exists()) {
			this.isLocalTomcatServerExists = true;
			if(bitSetter.exists()) {//clear bit setter if it exists
				bitSetter.delete();
			}
		}
		else {

			if(!bitSetter.exists()) {//set the bit
				try {
					bitSetter.createNewFile();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			if(!isInit) {//show this message only when we are inside ImportMenu
			StringBuffer buffer = new StringBuffer();
			buffer.append("The application cannot find " +
					"Tomcat " +
					"on your " +
					"local machine.\n" +
					"It will place media resources in " +
					"'" + this.getLocalMediaResourceDirectory() + "' \n directory and you will " +
							"have to copy them to the server yourself." +
							" .\n See docs on " +
							"how to rectify this problem if " +
							"you want media resources to be\n " +
							"copied to your Tomcat server by the application."
					);
			JOptionPane.showMessageDialog(this, buffer.toString(),
					"Information", JOptionPane.INFORMATION_MESSAGE);
			}
			this.isLocalTomcatServerExists = false;
		}
		return this.isLocalTomcatServerExists;
	}
	private void deleteAllSerializedBoxes() {
		File serializedCheckBox = 
			new File(EFGImagesConstants.CHECK_BOX_1_FNAME);
		
		if(serializedCheckBox.exists()) {
			serializedCheckBox.delete();
		}
		serializedCheckBox = 
			new File(EFGImagesConstants.RADIO_BUTTON_1_FNAME);
		
		if(serializedCheckBox.exists()) {
			serializedCheckBox.delete();
		}
		
		serializedCheckBox = 
			new File(EFGImagesConstants.RADIO_BUTTON_2_FNAME);
		
		if(serializedCheckBox.exists()) {
			serializedCheckBox.delete();
		}
	}
	public void setMediaResourcesDirectory(boolean bool) {
		String imagesDirectory = null;
		try {
			//find out what was last selected and set to it
			
			
			if(!isLocatTomcatExists(bool)) {//tomcat server/efg2/EFGImages does not exists
				deleteAllSerializedBoxes();//remove all serializations
				imagesDirectory = this.getLocalMediaResourceDirectory();
				//delete all checks
			}
			else {
//			already serialized?
			File serializedCheckBox = 
				new File(EFGImagesConstants.CHECK_BOX_1_FNAME);
			
			if(serializedCheckBox.exists()) {/*There is a serialized check box.
												Find out which radio button was selected
			 									*/
				SerializeDeserializeHandler sd = 
					new SerializeDeserializeHandler();
				
				JCheckBox checkBox = sd.getCheckBox(EFGImagesConstants.CHECK_BOX_1_FNAME, ""); 
				JRadioButton jr= 
					sd.getRadioButton(EFGImagesConstants.RADIO_BUTTON_1_FNAME, "");
			
				if(!jr.isSelected()) {//means copy to local images directory
					imagesDirectory = this.getLocalMediaResourceDirectory(); 
				}
				else {//copy to local Tomcat efg images directory
					imagesDirectory = this.getLocalTomcatServerPath();
				}

			}
			else {//assume local tomcat server
				imagesDirectory = this.getLocalTomcatServerPath();
			}
			}
		}catch(Exception ee) {
			imagesDirectory = this.getLocalTomcatServerPath();
			deleteAllSerializedBoxes();
			
		}
		if(this.webappsDirectory == null) {
			this.webappsDirectory = 
				EFGWebAppsDirectoryFactory.getEFGWebAppsDirectory(imagesDirectory);
			this.webappsDirectory.setImagesDirectory(EFGImagesConstants.EFG_IMAGES_DIR);
		}
		else {
			this.webappsDirectory.setPathToServer(imagesDirectory);
			this.webappsDirectory.setImagesDirectory(EFGImagesConstants.EFG_IMAGES_DIR);
		}
	}
	private JButton createButton(String title, String tooltipText){
	  	  JButton jbutton = new JButton(		
	  			title
		);
	
	jbutton.setToolTipText(
			tooltipText);

	return jbutton;
	}
    private JPanel createContentPane() {
        BufferedImage image = null;
 			   try {
				   URL url = this.getClass().getResource(EFGImportConstants.EFG_MAIN_BKGD_ICON);
				   URI uri = new URI(url.toString());
			 
			image = javax.imageio.ImageIO.read( new java.io.File(uri) );
		} catch (Exception e) {
			log.error(e.getMessage());
			
		}
       ImagePanel content = new ImagePanel(image, ImagePanel.ACTUAL);

    	JPanel top = new JPanel();
    	JPanel bottom = new JPanel();
    	JPanel left = new JPanel();
    	JPanel right = new JPanel();
    	JPanel selection = new JPanel(new GridLayout(0, 1, 0, 6));
    	

    	
    	  JButton addNewDatasourceBtn = this.createButton(
    	    		EFGImportConstants.EFGProperties.getProperty(
    				"ImportMenu.addNewDatasourceBtn"),
    			EFGImportConstants.EFGProperties.getProperty(
    					"ImportMenu.addNewDatasourceBtn.tooltip"));
    	  
    	addNewDatasourceBtn.addActionListener(new HandleDatasourceListener(
				this.dbObject, this));
    	
    	
    	 JButton addNewGlossaryBtn =this.createButton(
 	    		EFGImportConstants.EFGProperties.getProperty(
 				"ImportMenu.addNewGlossaryBtn"),
 			EFGImportConstants.EFGProperties.getProperty(
 					"ImportMenu.addNewGlossaryBtn.tooltip"));
    	 addNewGlossaryBtn.addActionListener(new HandleDatasourceListener(
 				this.dbObject, this));

    	JButton deployImagesBtn = this.createButton(
    			EFGImportConstants.EFGProperties.getProperty("ImportMenu.deployImagesBtn"),
		EFGImportConstants.EFGProperties.getProperty("ImportMenu.deployImagesBtn.tooltipText"));
		deployImagesBtn.setHorizontalAlignment(SwingConstants.CENTER);
		deployImagesBtn.addActionListener(new DeployImagesListener(this));

		JButton efgUserBtn =
			this.createButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.efgUserManagementBtn"),
		EFGImportConstants.EFGProperties.getProperty("ImportMenu.efgUserManagementBtn.tooltipText"));
		efgUserBtn.setHorizontalAlignment(SwingConstants.CENTER);
		efgUserBtn.addActionListener(new CreateUserListener(
				this.dbObject, this));
		
	
		
		
		
		
		JButton helpBtn = this.createButton(
				EFGImportConstants.EFGProperties.getProperty("ImportMenu.helpBtn"),
				EFGImportConstants.EFGProperties.getProperty("ImportMenu.helpBtn.tooltip"));
		helpBtn.setHorizontalAlignment(SwingConstants.CENTER);
		helpBtn.addActionListener(new HelpEFGListener());
	
		JButton aboutBtn = this.createButton(
				EFGImportConstants.EFGProperties.getProperty("ImportMenu.aboutBtn"),
				EFGImportConstants.EFGProperties.getProperty("ImportMenu.aboutBtn.tooltip"));
		aboutBtn.setHorizontalAlignment(SwingConstants.CENTER);
		aboutBtn.addActionListener(new AboutEFGListener(this));

		JButton exitBtn = this.createButton(
				EFGImportConstants.EFGProperties.getProperty("ImportMenu.exitBtn"),
		EFGImportConstants.EFGProperties.getProperty("ImportMenu.exitBtn.tooltip"));
		exitBtn.setHorizontalAlignment(SwingConstants.CENTER);
		exitBtn.addActionListener(new ExitListener(this));
    	
    	
    	selection.add(addNewDatasourceBtn);
    	selection.add(addNewGlossaryBtn);
    	selection.add(deployImagesBtn);
    	selection.add(efgUserBtn);
    
    	selection.add(helpBtn);
    	selection.add(aboutBtn);
    	selection.add(exitBtn);

    	content.setPreferredSize(new Dimension(300, 200));
    	top.setPreferredSize(new Dimension(200, 30));
    	bottom.setPreferredSize(new Dimension(200, 30));
    	left.setPreferredSize(new Dimension(30, 200));
    	right.setPreferredSize(new Dimension(30, 200));
    	content.add(top, BorderLayout.NORTH);
    	content.add(bottom, BorderLayout.SOUTH);
    	content.add(left, BorderLayout.WEST);
    	content.add(right, BorderLayout.EAST);
    	content.add(selection, BorderLayout.CENTER);
    	return content;
        }
    class ImagePanel extends JPanel
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public static final int TILED = 0;
        public static final int SCALED = 1;
        public static final int ACTUAL = 2;

        private BufferedImage image;
        private int style;
        private float alignmentX = 0.5f;
        private float alignmentY = 0.5f;

        public ImagePanel(BufferedImage image)
        {
            this(image, TILED);
        }

        public ImagePanel(BufferedImage image, int style)
        {
            this.image = image;
            this.style = style;
            setLayout( new BorderLayout() );
           // setLayout(new GridLayout(0, 1, 0, 6));
        }

    	public void setImageAlignmentX(float alignmentX)
    	{
    		this.alignmentX = alignmentX > 1.0f ? 1.0f : alignmentX < 0.0f ? 0.0f : alignmentX;
    	}

    	public void setImageAlignmentY(float alignmentY)
    	{
    		this.alignmentY = alignmentY > 1.0f ? 1.0f : alignmentY < 0.0f ? 0.0f : alignmentY;

    	}

        public void add(JComponent component)
        {
            add(component, null);
        }

        public void add(JComponent component, Object constraints)
        {
            component.setOpaque( false );

            if (component instanceof JScrollPane)
            {
                JScrollPane scrollPane = (JScrollPane)component;
                JViewport viewport = scrollPane.getViewport();
                viewport.setOpaque( false );
                Component c = viewport.getView();

                if (c instanceof JComponent)
                {
                    ((JComponent)c).setOpaque( false );
                }
            }

            super.add(component, constraints);
        }

        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            if (image == null ) return;

            switch (style)
            {
                case TILED  :
                    drawTiled(g);
                    break;

                case SCALED :
                    Dimension d = getSize();
                    g.drawImage(image, 0, 0, d.width, d.height, null);
                    break;

                case ACTUAL :
                	drawActual(g);
                    break;
            }
        }

        private void drawTiled(Graphics g)
        {
               Dimension d = getSize();
               int width = image.getWidth( null );
               int height = image.getHeight( null );

               for (int x = 0; x < d.width; x += width)
               {
                   for (int y = 0; y < d.height; y += height)
                   {
                       g.drawImage( image, x, y, null, null );
                   }
               }
        }

    	private void drawActual(Graphics g)
    	{
    		Dimension d = getSize();
            float x = (d.width - image.getWidth()) * alignmentX;
            float y = (d.height - image.getHeight()) * alignmentY;
    		g.drawImage(image, (int)x, (int)y, this);
    	}

    }

	/**
	 * @author kasiedu
	 *
	 */
	public class CreateUserListener implements ActionListener {
	
		private DBObject dbObject;
		private JFrame frame;


		/**
		 * 
		 */
		public CreateUserListener(DBObject dbObject, 
				JFrame frame) {
			this.dbObject = dbObject;
			this.frame = frame;
		}
		private void handleInput(){
			try {
				if (this.dbObject == null) {
					StringBuffer buffer = new StringBuffer(
						EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.buffermessage.1") +	
						"\n");
					buffer
							.append(EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.buffermessage.1") +	
							"\n");
					JOptionPane.showMessageDialog(this.frame, buffer.toString(),
							"Error Message", JOptionPane.ERROR_MESSAGE);
					log.error(buffer.toString());
					return;
				}
				//find out if we are a super user
				EFGUsersList ftb = new EFGUsersList(this.frame,"Manage EFG Users",true,this.dbObject);
				ftb.setVisible(true);
			
			} catch (Exception ee) {
				log.error(ee.getMessage());
				
				return;
			}
		}
		
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			 this.handleInput();
			
			
		}
	
	}

	class DeployImagesListener implements ActionListener {
		
		private ImportMenu importMenu;
		public DeployImagesListener(ImportMenu importMenu
				) {
			this.importMenu = importMenu;
		}

		public void actionPerformed(ActionEvent evt) {
			 this.handleInput();
		}
		
		private boolean isMediaResourceLocationPromptCheckBoxSelected(){
			
			
			JCheckBox checkBox = 
				serializerManager.getCheckBox(EFGImagesConstants.CHECK_BOX_1_FNAME, "");
			
			if(checkBox == null) {
				return false;
			}
		
			return checkBox.isSelected();
		}

		/**
		 * 
		 */
		private void handleInput() {
			try { 
				
				this.importMenu.setMediaResourcesDirectory(false);
				if(this.importMenu.isLocatTomcatExists(true)){
					if(!isMediaResourceLocationPromptCheckBoxSelected()) {//if user wants to be reminded everytime
						MediaResourceLocationHandler mdr = 
							new MediaResourceLocationHandler(importMenu,"",true,null);
						mdr.setVisible(true);
					}
				}
				fileBrowser = 
					new DnDFileBrowserMain(
						this.importMenu,
						EFGImportConstants.EFGProperties.getProperty("DeployImagesListener.title"),
						true
						);
			
				fileBrowser.setVisible(true);
				
				boolean isBrowserReload = isBrowserReload();//find out if a bit is set to reload browser
				while(isBrowserReload) {//reload browser if user changed directories
					if(!fileBrowser.isVisible()) {
						this.importMenu.setMediaResourcesDirectory(false);
							fileBrowser = new DnDFileBrowserMain(
									this.importMenu,
									EFGImportConstants.EFGProperties.getProperty("DeployImagesListener.title"),
									true
									);
						
							fileBrowser.setVisible(true);
							isBrowserReload = isBrowserReload();
						}
				}
				
			} catch (Exception ee) {
				ee.printStackTrace();
				log.error(ee.getMessage());
			}
			
		}
	}

	class HandleDatasourceListener implements ActionListener{
		private DBObject dbObject;

		private JFrame frame;
		private String mainTableName;
		
		public final String getMainTableName() {
			return this.mainTableName;
		}
		public final DBObject getDBObject() {
			return this.dbObject;
		}
		public final JFrame getFrame() {
			return this.frame;
		}
		public HandleDatasourceListener(DBObject dbObject, 
				JFrame frame) {
			this.dbObject = dbObject;
			this.frame = frame;
			
		}
		protected void handleInput(){
			try {
				if (this.getDBObject() == null) {
					StringBuffer buffer = new StringBuffer(
						EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.buffermessage.1") +	
						"\n");
					buffer
							.append(EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.buffermessage.1") +	
							"\n");
					JOptionPane.showMessageDialog(this.frame, buffer.toString(),
								"Error Message", JOptionPane.ERROR_MESSAGE);
					log.error(buffer.toString());
					return;
				}
		
				EFGUtils.setTableName(this.getMainTableName());
				//run this in another thread?
				SynopticKeyTreeMain ftb = new SynopticKeyTreeMain(this.getFrame(),
						EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.SynopticKeyTreeMain.title")	
		, true, this.getDBObject());
				ftb.setVisible(true);
			} catch (Exception ee) {
				log.error(ee.getMessage());
				JOptionPane.showMessageDialog(this.frame, ee.getMessage(),
						"Error Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			String buttonString = e.getActionCommand();
			String propName =EFGImportConstants.EFGProperties
			.getProperty(buttonString.toLowerCase()); 
			this.mainTableName = EFGImportConstants.EFGProperties
			.getProperty(propName);
			this.handleInput();
			
			
		}
	}

	class ExitListener implements ActionListener {
		private ImportMenu iMenu;

		public ExitListener(ImportMenu iMenu) {
			this.iMenu = iMenu;
		}
		
	
		 public void mouseClicked(MouseEvent e) {
			
	         
	        }
		/**
		 * 
		 */
		private void handleInput() {
			
			this.iMenu.close();
		}


		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			 this.handleInput();
			
		}
	}
	 // Inner class to respond to mouse events for the "rollover" effect
    class RolloverListener extends MouseAdapter {
        public void mouseEntered(MouseEvent e) {
            ((JLabel)e.getComponent()).setBorder(bevel);
            repaint();
	}
	
        public void mouseExited(MouseEvent e) {
            ((JLabel)e.getComponent()).setBorder(empty);
            repaint();
        }

       
    }

    
    class AboutEFGListener implements ActionListener{
    	private JFrame frame;

    	public AboutEFGListener(JFrame frame) {
    		this.frame = frame;
    	}


		/**
		 * 
		 */
		private void handleInput() {
	   		AboutBox about = new AboutBox(this.frame);
    		about.setVisible(true);		
		}


		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			this.handleInput();
			
		}
    }
    class HelpEFGListener  implements ActionListener{
    	

    	public HelpEFGListener() {
    		
    	}

		/**
		 * 
		 */
		private void handleInput() {
			try {
				URL helpURL = this.getClass().getResource(EFGImportConstants.MAIN_DEPLOY_HELP);
			        if (helpURL == null) {
			            log.error("Couldn't open help file: " + EFGImportConstants.MAIN_DEPLOY_HELP);
			            return;
			        } 
			        Browser.displayURL(helpURL.getFile(), "target");
			} catch (Exception ee) {
				log.error(ee.getMessage());
			}
		}


		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			 this.handleInput();
			
		}
    }
	public static void main(String[] args) {
	String catHome = "C:\\Program Files\\Apache Software Foundation\\Tomcat 5.0";
	LoggerUtils utils = new LoggerUtils();
	utils.toString();
	ImportMenu menu = new ImportMenu(EFGImportConstants.IMPORT_TITLE,catHome,
				null);
		menu.setVisible(true);
	}



	/**
	 * @param b
	 */
	public void setBrowserReload(boolean b) {
		this.browserreload = b;
	}
	public boolean isBrowserReload() {
		return this.browserreload;
	}
} // ImportMenu

