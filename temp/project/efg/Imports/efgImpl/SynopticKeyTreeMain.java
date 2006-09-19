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
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.CheckListener;
import project.efg.Imports.efgInterface.DataManipulatorInterface;
import project.efg.Imports.efgInterface.SynopticKeyTreeInterface;
import project.efg.Imports.factory.DataManipulatorFactory;
import project.efg.Imports.factory.SynopticKeyTreeFactory;
import project.efg.util.EFGImportConstants;
import project.efg.util.HelpEFG2ItemListener;
//import project.efg.util.DnDFileBrowserMain.DoneListener;

/**
 * SynopticKeyTreeMain.java
 * 
 * 
 * Created: Sat Feb 18 16:41:14 2006
 * 
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
 * @version 1.0
 */
public class SynopticKeyTreeMain extends JDialog {
	static final long serialVersionUID = 1;

	DataManipulatorInterface deleteManipulator;

	DataManipulatorInterface updateManipulator;

	DataManipulatorInterface editManipulator;

	private SynopticKeyTreeInterface tree;

	final JButton deleteBtn = new JButton(EFGImportConstants.EFGProperties
			.getProperty("SynopticKeyTreeMain.deleteBtn"));

	final JButton updateBtn = new JButton(EFGImportConstants.EFGProperties
			.getProperty("SynopticKeyTreeMain.updateBtn"));

	final JButton doneBtn = new JButton(EFGImportConstants.EFGProperties
			.getProperty("SynopticKeyTreeMain.doneBtn"));

	final JButton helpBtn = new JButton(EFGImportConstants.EFGProperties
			.getProperty("SynopticKeyTreeMain.helpBtn"));

	final JButton editMetadataBtn = new JButton(
			EFGImportConstants.EFGProperties
					.getProperty("SynopticKeyTreeMain.editMetadataBtn"));

	final JPopupMenu popup = new JPopupMenu();

	JFrame parentFrame;

	private DBObject dbObject;

	private JEditorPane htmlPane;

	private URL helpURL;

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(SynopticKeyTreeMain.class);
		} catch (Exception ee) {
		}
	}

	public SynopticKeyTreeMain(JFrame frame, boolean modal, DBObject db) {
		this(frame, "", modal, db);
	}

	public SynopticKeyTreeMain(DBObject db, JFrame frame) {
		this(frame, "", true, db);
	}
	private void addMenus(){
		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
		
		
		JMenu checkMenu = new JMenu("Check Data For Errors");
		//sub menus
		JMenuItem checkMediaMenu = new JMenuItem("Check Images");
		checkMediaMenu.setToolTipText("Check if images in data exists on server");
		checkMediaMenu.addActionListener(
				new CheckListener(
				this.dbObject,
				this.tree, 
				EFGImportConstants.MEDIARESOURCE));
		
		
		checkMenu.add(checkMediaMenu);
		JMenuItem checkIllegalCharactersMenu = new JMenuItem("Check IllegalCharacters");
		checkIllegalCharactersMenu.setToolTipText("Check if illegal characters exists in data");
		checkIllegalCharactersMenu.addActionListener(
				new CheckListener(
				this.dbObject,
				this.tree, 
				EFGImportConstants.ILLEGALCHARACTER_STRING));
		//checkMenu.add(checkIllegalCharactersMenu);
		
		fileMenu.add(checkMenu);
		
		
	
		
		
		
		JMenuItem closeMenu = new JMenuItem("Close");
		JMenuItem helpItem = new JMenuItem("Help Contents");
		helpItem.addActionListener(new HelpEFG2ItemListener(EFGImportConstants.KEYTREE_DEPLOY_HELP));
		helpMenu.add(helpItem);
		
		closeMenu.addActionListener(new DoneListener(this));
	
		fileMenu.add(closeMenu);
		JMenuBar mBar = new JMenuBar();
		mBar.add(fileMenu);
		mBar.add(helpMenu);
	
	
		this.setJMenuBar(mBar);
		
	}
	/**
	 * @return
	 */
	private DataManipulatorInterface getEditManipulator() {
		if (this.editManipulator == null) {
			this.editManipulator = DataManipulatorFactory.getInstance(
					this.tree, EFGImportConstants.EFGProperties
							.getProperty("editMetadataClass"));
		}
		return this.editManipulator;
	}

	public DataManipulatorInterface getUpdateManipulator() {
		if (this.updateManipulator == null) {
			this.updateManipulator = DataManipulatorFactory.getInstance(
					this.tree, EFGImportConstants.EFGProperties
							.getProperty("updateDataClass"));
		}
		return this.updateManipulator;
	}

	public DataManipulatorInterface getDeleteManipulator() {
		if (this.deleteManipulator == null) {
			this.deleteManipulator = DataManipulatorFactory.getInstance(
					this.tree, EFGImportConstants.EFGProperties
							.getProperty("deleteDataClass"));
		}
		return this.deleteManipulator;
	}

	public SynopticKeyTreeMain(JFrame frame, String title, boolean modal,
			DBObject dbObject) {
		super(frame, title, modal);

		this.parentFrame = frame;

		setSize(new Dimension(800, 800));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		this.dbObject = dbObject;
		String message = "";
		try {

			this.tree = SynopticKeyTreeFactory.getSynopticKeyTree(
					this.dbObject, frame);
			this.tree.setToolTipText(EFGImportConstants.EFGProperties
					.getProperty("SynopticKeyTreeMain.tooltip"));
			this.tree.setRootVisible(false);

			this.tree.addMouseListener(new EditMouseListener(this, this
					.getEditManipulator()));

		} catch (Exception ee) {
			ee.printStackTrace();
			message = ee.getMessage();
			log.error(message);
			JOptionPane.showMessageDialog(null, message, "Error Message",
					JOptionPane.ERROR_MESSAGE);
		}
		this.addMenus();
		JComponent newContentPane = addPanel();
		this.createPopUp();
		setContentPane(newContentPane);
	} // SynoptcKeyTreeMain constructor

	public void close() {

		this.dispose();
	}

	private JPanel addPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(0, 2));

		editMetadataBtn.addActionListener(new DataManipulatorListener(this,
				this.getEditManipulator()));
		editMetadataBtn.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("SynopticKeyTreeMain.editMetadataBtn.tooltip"));
		btnPanel.add(editMetadataBtn);

		updateBtn.addActionListener(new DataManipulatorListener(this, this
				.getUpdateManipulator()));
		updateBtn.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("SynopticKeyTreeMain.updateBtn.tooltip"));
		btnPanel.add(updateBtn);

		deleteBtn.addActionListener(new DataManipulatorListener(this, this
				.getDeleteManipulator()));
		deleteBtn.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("SynopticKeyTreeMain.deleteBtn.tooltip"));
		btnPanel.add(deleteBtn);

		doneBtn.addActionListener(new DoneListener(this));
		doneBtn.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("SynopticKeyTreeMain.doneBtn.tooltip"));
		btnPanel.add(doneBtn);
		
		JScrollPane treePane = new JScrollPane(this.tree);
		treePane.setColumnHeaderView(new JLabel(
				EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.SynopticKeyTreeMain.tableHeader"),
				JLabel.CENTER));
		htmlPane = new JEditorPane();
		ToolTipManager.sharedInstance().registerComponent(htmlPane);
		htmlPane.setContentType("text/html");
		htmlPane.setEditable(false);
		initHelp();

		//JScrollPane htmlViewPane = new JScrollPane(htmlPane);

	//	JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		//		treePane, htmlViewPane);

		//mainPane.setDividerLocation(300);

		panel.add(treePane, BorderLayout.CENTER);
		panel.add(btnPanel, BorderLayout.SOUTH);
		return panel;
	}

	private void initHelp() {

		helpURL = this.getClass().getResource(
				EFGImportConstants.KEYTREE_DEPLOY_HELP);
		if (helpURL == null) {
			log.error("Couldn't open help file: "
					+ EFGImportConstants.KEYTREE_DEPLOY_HELP);
			return;
		}
		displayURL(helpURL);
	}

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

	public void createPopUp() {

		JMenuItem menuItem = new JMenuItem(this.editMetadataBtn.getText());
		menuItem.addActionListener(new DataManipulatorListener(this, this
				.getEditManipulator()));
		this.popup.add(menuItem);

		menuItem = new JMenuItem(this.updateBtn.getText());
		menuItem.addActionListener(new DataManipulatorListener(this, this
				.getUpdateManipulator()));
		this.popup.add(menuItem);

		menuItem = new JMenuItem(this.deleteBtn.getText());
		menuItem.addActionListener(new DataManipulatorListener(this, this
				.getDeleteManipulator()));
		this.popup.add(menuItem);

	}

	/**
	 * 
	 * @author kasiedu Edit Metadata table
	 */
	class DataManipulatorListener implements ActionListener {
		private SynopticKeyTreeMain treeBrowser;

		private DataManipulatorInterface manipulator;

		public DataManipulatorListener(SynopticKeyTreeMain treeBrowser,
				DataManipulatorInterface manipulator) {
			this.treeBrowser = treeBrowser;
			this.manipulator = manipulator;
		}

		public void actionPerformed(ActionEvent evt) {

			this.treeBrowser.processNode(this.manipulator);

		}
	}

	/**
	 * 
	 * @author kasiedu Done with changes
	 */
	class DoneListener implements ActionListener {
		private SynopticKeyTreeMain treeBrowser;

		public DoneListener(SynopticKeyTreeMain treeBrowser) {
			this.treeBrowser = treeBrowser;
			
		}

		public void actionPerformed(ActionEvent evt) {
			this.treeBrowser.close();
		}
	}

	class EditMouseListener extends MouseAdapter {

		private SynopticKeyTreeMain treeBrowser;

		private DataManipulatorInterface manipulator;

		public EditMouseListener(SynopticKeyTreeMain treeBrowser,
				DataManipulatorInterface manipulator) {
			this.treeBrowser = treeBrowser;
			this.manipulator = manipulator;
		}

		public void mousePressed(MouseEvent e) {
			int selRow = tree.getRowForLocation(e.getX(), e.getY());

			if (selRow != -1) {
				if (e.getClickCount() == 2) {
					this.treeBrowser.processNode(this.manipulator);
				}
			}
			if (e.isPopupTrigger()) {
				showPopUp(e);
			}
		}

		private void showPopUp(MouseEvent e) {
			TreePath path = tree.getPathForLocation(e.getX(), e.getY());

			if (path != null) {

				tree.getSelectionModel().setSelectionPath(path);
				// TreeNode node = (TreeNode) path.getLastPathComponent();

				popup.show(this.treeBrowser, e.getX(), e.getY());
			}

		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopUp(e);
			}
		}

	}

	/**
	 * @param interface1
	 */
	public void processNode(DataManipulatorInterface manipulator) {

		manipulator.processNode();
	}

} // SynopticKeyTreeMain
