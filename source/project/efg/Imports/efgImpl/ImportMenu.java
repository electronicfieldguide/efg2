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

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgImportsUtil.LoggerUtils;
import project.efg.Imports.efgInterface.EFGWebAppsDirectoryInterface;
import project.efg.Imports.factory.EFGWebAppsDirectoryFactory;
import project.efg.util.DnDFileBrowserMain;
import project.efg.util.EFGImportConstants;

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
		
	private JEditorPane htmlPane;
	private URL helpURL;
	private DBObject dbObject;

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ImportMenu.class);
		} catch (Exception ee) {
		}
	}

	public ImportMenu(String title) {
		this("Import Menu", null);
	}

	public ImportMenu() {
		this("Import Menu", null);
	}

	public ImportMenu(String title, String catalina_home) {
		this(title, catalina_home, null);

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
		setSize(new Dimension(220, 150));
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
		this.setSize(650, 450);
		this.setContentPane(this.addPanel());
		this.setResizable(false);
		this.setVisible(true);
	
	} // ImportMenu constructor
	 private void initHelp() {
			
	        helpURL = this.getClass().getResource(EFGImportConstants.MAIN_DEPLOY_HELP);
	        if (helpURL == null) {
	            log.error("Couldn't open help file: " + EFGImportConstants.MAIN_DEPLOY_HELP);
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
	private JSplitPane addPanel() {
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
		
		addNewDatasourceLabel.addMouseListener(new HandleDatasourceListener(
				this.dbObject, this));

		JLabel deployImagesLabel = new JLabel(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deployImagesBtn"));
		deployImagesLabel.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deployImagesBtn.tooltip"));
		deployImagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		deployImagesLabel.addMouseListener(new DeployImagesListener(
				webappsDirectory.getImagesDirectory(), this));

	
		JLabel aboutLabel = new JLabel(EFGImportConstants.EFGProperties.getProperty("ImportMenu.aboutBtn"));
		aboutLabel.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.aboutBtn.tooltip"));
		aboutLabel.setHorizontalAlignment(SwingConstants.CENTER);
		aboutLabel.addMouseListener(new AboutEFGListener(this));

		JLabel exitLabel = new JLabel(EFGImportConstants.EFGProperties.getProperty("ImportMenu.exitBtn"));
		exitLabel.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.exitBtn.tooltip"));
		exitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		exitLabel.addMouseListener(new ExitListener(this));
		
		
		
		panel.add(addNewDatasourceLabel);
		panel.add(deployImagesLabel);
		
		panel.add(aboutLabel);
		panel.add(exitLabel);
		panel.setSize(270,200);
		
		JScrollPane btnPane = new JScrollPane(panel);
		btnPane.setAutoscrolls(false);
		btnPane.setToolTipText("Drag and drop key here");
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		
		initHelp();
			
		JScrollPane htmlViewPane = new JScrollPane(htmlPane);
	
		JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
						     btnPane, htmlViewPane);
			
		
		mainPane.setDividerLocation(250);
		return mainPane;
		
	}

	public void close() {
		this.dispose();
		System.exit(0);
	}

	class DeployImagesListener extends RolloverListener implements ActionListener {
		private String imagesDirectory;

		private JFrame frame;

		public DeployImagesListener(String imagesDirectory, JFrame frame) {
			this.frame = frame;
			this.imagesDirectory = imagesDirectory;
		}

		public void actionPerformed(ActionEvent evt) {
			 this.handleInput();
		}
		 public void mouseClicked(MouseEvent e) {
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

	class HandleDatasourceListener extends RolloverListener{
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
		
		 public void mouseClicked(MouseEvent e) {
			 this.handleInput();
	        
	        }
	}

	class ExitListener extends RolloverListener {
		private ImportMenu iMenu;

		public ExitListener(ImportMenu iMenu) {
			this.iMenu = iMenu;
		}
		
	
		 public void mouseClicked(MouseEvent e) {
			 this.handleInput();
	         
	        }
		/**
		 * 
		 */
		private void handleInput() {
			
			this.iMenu.close();
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
    class AboutEFGListener  extends RolloverListener{
    	private JFrame frame;

    	public AboutEFGListener(JFrame frame) {
    		this.frame = frame;
    	}

    
    	 public void mouseClicked(MouseEvent e) {
			 this.handleInput();
	         
	        }

		/**
		 * 
		 */
		private void handleInput() {
	   		AboutBox about = new AboutBox(this.frame);
    		about.show();		
		}
    }
    
	public static void main(String[] args) {
	String catHome = "C:\\Program Files\\Apache Software Foundation\\Tomcat 5.0";
	LoggerUtils utils = new LoggerUtils();
	utils.toString();
	ImportMenu menu = new ImportMenu("Import Menu",catHome,
				null);
		menu.show();
	}
} // ImportMenu

