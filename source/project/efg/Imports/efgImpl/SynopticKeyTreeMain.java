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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.DataManipulatorInterface;
import project.efg.Imports.efgInterface.SynopticKeyTreeInterface;
import project.efg.Imports.factory.DataManipulatorFactory;
import project.efg.Imports.factory.SynopticKeyTreeFactory;
import project.efg.util.EFGImportConstants;

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

	private SynopticKeyTreeInterface tree;
	//private SynopticKeyTreeFactoryInterface treeFactory;
	final JButton deleteBtn = 
		new JButton(EFGImportConstants.EFGProperties.getProperty("SynopticKeyTreeMain.deleteBtn"));
	final JButton updateBtn = 
		new JButton(EFGImportConstants.EFGProperties.getProperty("SynopticKeyTreeMain.updateBtn"));

	final JButton doneBtn = 
		new JButton(EFGImportConstants.EFGProperties.getProperty("SynopticKeyTreeMain.doneBtn"));

	final JButton helpBtn = 
		new JButton(EFGImportConstants.EFGProperties.getProperty("SynopticKeyTreeMain.helpBtn"));

	final JButton editMetadataBtn = 
		new JButton(EFGImportConstants.EFGProperties.getProperty("SynopticKeyTreeMain.editMetadataBtn"));

	JFrame parentFrame;
	private DBObject dbObject;
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
		this(frame, "", false, db);
	}
	
	public SynopticKeyTreeMain(JFrame frame, String title, boolean modal,
			DBObject dbObject) {
		super(frame, title, modal);
		this.parentFrame = frame;

		setSize(new Dimension(600, 400));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		this.dbObject = dbObject;
		String message = "";
		try {
			
			this.tree = SynopticKeyTreeFactory.getSynopticKeyTree(this.dbObject);
			this.tree
					.setToolTipText(EFGImportConstants.EFGProperties.getProperty
							("SynopticKeyTreeMain.tooltip")
							);
		} catch (Exception ee) {
			ee.printStackTrace();
			message = ee.getMessage();
			log.error(message);
			JOptionPane.showMessageDialog(null, message, "Error Message",
					JOptionPane.ERROR_MESSAGE);
		}
		JComponent newContentPane = addPanel();
		setContentPane(newContentPane);
	} // SynoptcKeyTreeMain constructor

	public void close() {
		this.dispose();
	}

	private JPanel addPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(0, 2));

		editMetadataBtn.addActionListener(
				new DataManipulatorListener(
						DataManipulatorFactory.getInstance(this.tree,
								EFGImportConstants.EFGProperties.getProperty("editMetadataClass"))
						)
						);
		editMetadataBtn
				.setToolTipText(EFGImportConstants.EFGProperties.getProperty("SynopticKeyTreeMain.editMetadataBtn.tooltip"));
		btnPanel.add(editMetadataBtn);

		updateBtn.addActionListener(
				new DataManipulatorListener(
						DataManipulatorFactory.getInstance(this.tree,
								EFGImportConstants.EFGProperties.getProperty("updateDataClass"))
					)
				);
		updateBtn
				.setToolTipText(EFGImportConstants.EFGProperties.getProperty("SynopticKeyTreeMain.updateBtn.tooltip"));
		btnPanel.add(updateBtn);

		deleteBtn.addActionListener(
				new DataManipulatorListener(
						DataManipulatorFactory.getInstance(this.tree,
								EFGImportConstants.EFGProperties.getProperty("deleteDataClass"))
					)
				);
		deleteBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("SynopticKeyTreeMain.deleteBtn.tooltip"));
		btnPanel.add(deleteBtn);

		helpBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("SynopticKeyTreeMain.helpBtn.tooltip"));
		helpBtn.addActionListener(new HelpListener());
		btnPanel.add(helpBtn);

		doneBtn.addActionListener(new DoneListener(this));
		doneBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty(
				"SynopticKeyTreeMain.doneBtn.tooltip"));
		btnPanel.add(doneBtn);
		JScrollPane pane = new JScrollPane(this.tree);

		panel.add(pane, BorderLayout.CENTER);
		panel.add(btnPanel, BorderLayout.SOUTH);
		return panel;
	}

/**
 * 
 * @author kasiedu
 * Edit Metadata table
 */
	class DataManipulatorListener implements ActionListener {
		private DataManipulatorInterface manipulator;
		
		public DataManipulatorListener(DataManipulatorInterface manipulator) {
			this.manipulator = manipulator;
		}

		public void actionPerformed(ActionEvent evt) {
			this.manipulator.processNode();
		}
	}
/**
 * 
 * @author kasiedu
 * Done with changes
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
} // SynopticKeyTreeMain
