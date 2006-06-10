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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImportsUtil.LoggerUtils;
import project.efg.Imports.efgInterface.EFGWebAppsDirectoryInterface;
import project.efg.Imports.factory.EFGWebAppsDirectoryFactory;
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

	private String catalina_home = null;

	private EFGWebAppsDirectoryInterface webappsDirectory;

	final public static Hashtable imageCacheTable = new Hashtable();
	 private static GeneralCacheAdministrator cacheAdmin;
		
	
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
			//cacheAdmin.setOverflowPersistence()
		}
		
		return cacheAdmin;
	}
	public ImportMenu(String title, String catalina_home, DBObject dbObject) {
		super(title);
		this.catalina_home = catalina_home;
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
		JComponent newContentPane = this.addPanel();
		setContentPane(newContentPane);
	} // ImportMenu constructor

	private JPanel addPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 1));

		JButton addNewDatasourceBtn = new JButton(
				EFGImportConstants.EFGProperties.getProperty(
						"ImportMenu.addNewDatasourceBtn"
						)
						);
		addNewDatasourceBtn
				.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.addNewDatasourceBtn.tooltip"));
		addNewDatasourceBtn.setHorizontalAlignment(SwingConstants.CENTER);
		addNewDatasourceBtn.addActionListener(new HandleDatasourceListener(
				this.dbObject, this));

		JButton deployImagesBtn = new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deployImagesBtn"));
		deployImagesBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deployImagesBtn.tooltip"));
		deployImagesBtn.setHorizontalAlignment(SwingConstants.CENTER);
		deployImagesBtn.addActionListener(new DeployImagesListener(
				webappsDirectory.getImagesDirectory(), this));

		JButton helpBtn = new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.helpBtn"));
		helpBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.helpBtn.tooltip"));
		helpBtn.setHorizontalAlignment(SwingConstants.CENTER);
		helpBtn.addActionListener(new HelpListener());

		JButton aboutBtn = new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.aboutBtn"));
		aboutBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.aboutBtn.tooltip"));
		aboutBtn.setHorizontalAlignment(SwingConstants.CENTER);
		aboutBtn.addActionListener(new AboutListener(this));

		JButton exitBtn = new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.exitBtn"));
		exitBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.exitBtn.tooltip"));
		exitBtn.setHorizontalAlignment(SwingConstants.CENTER);
		exitBtn.addActionListener(new ExitListener(this));

		panel.setBackground(Color.lightGray);
		panel.add(addNewDatasourceBtn);
		panel.add(deployImagesBtn);
		panel.add(helpBtn);
		panel.add(aboutBtn);
		panel.add(exitBtn);
		return panel;
	}

	public void close() {
		this.dispose();
		System.exit(0);
	}

	class DeployImagesListener implements ActionListener {
		private String imagesDirectory;

		private JFrame frame;

		public DeployImagesListener(String imagesDirectory, JFrame frame) {
			this.frame = frame;
			this.imagesDirectory = imagesDirectory;
		}

		public void actionPerformed(ActionEvent evt) {
			try {
				FileTreeBrowserMain ftb = 
					new FileTreeBrowserMain(
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

	class HandleDatasourceListener implements ActionListener {
		private DBObject dbObject;

		private JFrame frame;

		public HandleDatasourceListener(DBObject dbObject, 
				JFrame frame) {
			this.dbObject = dbObject;
			this.frame = frame;
		}

		public void actionPerformed(ActionEvent evt) {
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
	}

	class ExitListener implements ActionListener {
		private ImportMenu iMenu;

		public ExitListener(ImportMenu iMenu) {
			this.iMenu = iMenu;
		}

		public void actionPerformed(ActionEvent evt) {
			this.iMenu.close();
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

