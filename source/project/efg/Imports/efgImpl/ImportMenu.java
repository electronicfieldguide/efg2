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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgImportsUtil.LoggerUtils;
import project.efg.Imports.efgInterface.EFGWebAppsDirectoryInterface;
import project.efg.Imports.factory.EFGWebAppsDirectoryFactory;
import project.efg.Imports.rdb.RunSetUp;
import project.efg.util.DnDFileBrowserMain;
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
      
	private String catalina_home = null;

	private EFGWebAppsDirectoryInterface webappsDirectory;

	final public static Hashtable imageCacheTable = new Hashtable();
	private static GeneralCacheAdministrator cacheAdmin;
		
	
	private DBObject dbObject;
  
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

	public ImportMenu(String title, String catalina_home) {
		this(title, catalina_home, null);

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
	public ImportMenu(String title, String catalina_home, DBObject dbObject) {
		super(title);
		this.catalina_home = catalina_home;
		//set the catalina home 
		EFGUtils.setCatalinaHome(this.catalina_home);
		//setSize(new Dimension(220, 150));
		setSize(new Dimension(400, 400));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		// create the webapps directory here
		String pathToServer = this.catalina_home + File.separator
				+ project.efg.util.EFGImportConstants.EFG_WEB_APPS
				+ File.separator + project.efg.util.EFGImportConstants.EFG_APPS;

		this.webappsDirectory = EFGWebAppsDirectoryFactory.getEFGWebAppsDirectory(pathToServer);
		this.webappsDirectory.setImagesDirectory(EFGImportConstants.EFGIMAGES);
		this.dbObject = dbObject;
		if(this.dbObject == null){
			log.error("DBObject is null inside ImportMenu");
		}
	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setSize(650, 450);
		//this.setContentPane(this.addPanel());
		this.setContentPane(this.createContentPane());
		this.setResizable(false);
		this.setVisible(true);
	
	} // ImportMenu constructor
	
    private JPanel createContentPane() {
    	JPanel content = new JPanel(new BorderLayout());
    	JPanel top = new JPanel();
    	JPanel bottom = new JPanel();
    	JPanel left = new JPanel();
    	JPanel right = new JPanel();
    	JPanel selection = new JPanel(new GridLayout(0, 1, 0, 6));
    	

    	
    	  JButton addNewDatasourceBtn = new JButton(
    	    		EFGImportConstants.EFGProperties.getProperty(
    				"ImportMenu.addNewDatasourceBtn"
    		));
    	addNewDatasourceBtn.setToolTipText(
    			EFGImportConstants.EFGProperties.getProperty(
    					"ImportMenu.addNewDatasourceBtn.tooltip"));
    	addNewDatasourceBtn.addActionListener(new HandleDatasourceListener(
				this.dbObject, this));


    	JButton deployImagesBtn = new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deployImagesBtn"));
		deployImagesBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deployImagesBtn.tooltipText"));
		deployImagesBtn.setHorizontalAlignment(SwingConstants.CENTER);
		deployImagesBtn.addActionListener(new DeployImagesListener(
				webappsDirectory.getImagesDirectory(), this));

		JButton efgUserBtn =
			new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.efgUserBtn"));
		efgUserBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.efgUserBtn.tooltipText"));
		efgUserBtn.setHorizontalAlignment(SwingConstants.CENTER);
		efgUserBtn.addActionListener(new CreateUserListener(
				this.dbObject, this));
		
		JButton deleteEfgUserBtn =
			new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deleteEFGUserBtn"));
		deleteEfgUserBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deleteEFGUserBtn.tooltipText"));
		deleteEfgUserBtn.setHorizontalAlignment(SwingConstants.CENTER);
		deleteEfgUserBtn.addActionListener(new DeleteUserListener(
				this.dbObject, this));
		
		JButton helpBtn = new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.helpBtn"));
		helpBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.helpBtn.tooltip"));
		helpBtn.setHorizontalAlignment(SwingConstants.CENTER);
		helpBtn.addActionListener(new HelpEFGListener());
	
		JButton aboutBtn = new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.aboutBtn"));
		aboutBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.aboutBtn.tooltip"));
		aboutBtn.setHorizontalAlignment(SwingConstants.CENTER);
		aboutBtn.addActionListener(new AboutEFGListener(this));

		JButton exitBtn = new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.exitBtn"));
		exitBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.exitBtn.tooltip"));
		exitBtn.setHorizontalAlignment(SwingConstants.CENTER);
		exitBtn.addActionListener(new ExitListener(this));
    	
    	
    	
    	
    	
    	selection.add(addNewDatasourceBtn);
    	selection.add(deployImagesBtn);
    	selection.add(efgUserBtn);
    	selection.add(deleteEfgUserBtn);
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
	/*private JPanel addPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 1));

	
		JLabel addNewDatasourceLabel = new JLabel(
				EFGImportConstants.EFGProperties.getProperty(
						"ImportMenu.addNewDatasourceBtn"
						)
						);
		addNewDatasourceLabel
				.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.addNewDatasourceBtn.tooltip"));
		addNewDatasourceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
	//	addNewDatasourceLabel.addMouseListener(new HandleDatasourceListener(
		//		this.dbObject, this));

		JLabel deployImagesLabel = new JLabel(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deployImagesBtn"));
		deployImagesLabel.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deployImagesBtn.tooltipText"));
		deployImagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
	//	deployImagesLabel.addMouseListener(new DeployImagesListener(
		//		webappsDirectory.getImagesDirectory(), this));

		JLabel helpLabel = new JLabel(EFGImportConstants.EFGProperties.getProperty("ImportMenu.helpBtn"));
		helpLabel.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.helpBtn.tooltip"));
		helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//helpLabel.addMouseListener(new HelpEFGListener());
	
		JLabel aboutLabel = new JLabel(EFGImportConstants.EFGProperties.getProperty("ImportMenu.aboutBtn"));
		aboutLabel.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.aboutBtn.tooltip"));
		aboutLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//aboutLabel.addMouseListener(new AboutEFGListener(this));

		JLabel exitLabel = new JLabel(EFGImportConstants.EFGProperties.getProperty("ImportMenu.exitBtn"));
		exitLabel.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.exitBtn.tooltip"));
		exitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//exitLabel.addMouseListener(new ExitListener(this));
		
		
		
		panel.add(addNewDatasourceLabel);
		panel.add(deployImagesLabel);
		panel.add(helpLabel);
		panel.add(aboutLabel);
		panel.add(exitLabel);
		panel.setSize(270,200);
		
		JScrollPane btnPane = new JScrollPane(panel);
		btnPane.setAutoscrolls(false);
		btnPane.setToolTipText("Drag and drop key here");
		return panel;
	}*/

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
					JOptionPane.showMessageDialog(null, buffer.toString(),
							"Error Message", JOptionPane.ERROR_MESSAGE);
					log.error(buffer.toString());
					return;
				}
				
				DBObject superuserInfo = null;
				CreateEFGUserDialog dialog = new CreateEFGUserDialog(this.frame);
				dialog.setVisible(true);
				
				
				if(dialog.isSuccess()){
					superuserInfo = dialog.getDbObject();
						
				}
				dialog.dispose();
				if(superuserInfo != null){
					RunSetUp.createSuperUser(dbObject,superuserInfo);
				}
			
			} catch (Exception ee) {
				log.error(ee.getMessage());
				JOptionPane.showMessageDialog(null, ee.getMessage(),
						"Error Message", JOptionPane.ERROR_MESSAGE);
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
	public class DeleteUserListener implements ActionListener {
		
		private DBObject dbObject;
		private JFrame frame;


		/**
		 * 
		 */
		public DeleteUserListener(DBObject dbObject, 
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
				
				deleteUser();
			
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
			 this.handleInput();
			
			
		}
		private void deleteUser(){
			
			Object[] possibilities = getUsers();
			String userName = null;
			if((possibilities == null) || (possibilities.length == 0)){
				JOptionPane.showMessageDialog(this.frame, "No EFG users exists", "Message",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			userName = (String)JOptionPane.showInputDialog(
                    this.frame,
                    "Select User Name To Delete:",
                    "Delete a User",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    possibilities[0].toString());
			if ((userName!= null) && (userName.trim().length() > 0)) {
			  RunSetUp.deleteSuperUser(this.dbObject,userName);
			}
			return ;
		}
		/**
		 * @return
		 */
		private Object[] getUsers() {
			return RunSetUp.getEFGUsers(this.dbObject);
		}
	}
	class DeployImagesListener implements ActionListener {
		private String imagesDirectory;

		private JFrame frame;

		public DeployImagesListener(String imagesDirectory, JFrame frame) {
			this.frame = frame;
			this.imagesDirectory = imagesDirectory;
		}

		public void actionPerformed(ActionEvent evt) {
			 this.handleInput();
		}
		

		/**
		 * 
		 */
		private void handleInput() {
			try {
				
				DnDFileBrowserMain ftb = 
					new DnDFileBrowserMain(
						this.frame,
						EFGImportConstants.EFGProperties.getProperty("DeployImagesListener.title"),
						true, 
						imagesDirectory);
			
				ftb.show();
			} catch (Exception ee) {
				log.error(ee.getMessage());
			}
			
		}
	}

	class HandleDatasourceListener implements ActionListener{
		private DBObject dbObject;

		private JFrame frame;

		public HandleDatasourceListener(DBObject dbObject, 
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
					JOptionPane.showMessageDialog(null, buffer.toString(),
							"Error Message", JOptionPane.ERROR_MESSAGE);
					log.error(buffer.toString());
					return;
				}
				//run this in another thread?
				SynopticKeyTreeMain ftb = new SynopticKeyTreeMain(this.frame,
						EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.SynopticKeyTreeMain.title")	
		, true, this.dbObject);
				ftb.show();
			} catch (Exception ee) {
				log.error(ee.getMessage());
				JOptionPane.showMessageDialog(null, ee.getMessage(),
						"Error Message", JOptionPane.ERROR_MESSAGE);
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
    		about.show();		
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
		menu.show();
	}
} // ImportMenu

