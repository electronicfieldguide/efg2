package project.efg.client.impl.gui;
/**
 * $Id$
 * $Name:  $
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import org.apache.log4j.Logger;

import project.efg.client.factory.nogui.SpringNoGUIFactory;
import project.efg.client.interfaces.nogui.ChoiceCommandAbstract;
import project.efg.util.interfaces.EFGImportConstants;

/*
 * DialogDemo.java is a 1.4 application that requires these files:
 *   Customdialog.java
 *   images/middle.gif
 */
/**
 * Importdialog.java
 * 
 * 
 * Created: Sat Mar 18 19:31:56 2006
 * 
 * Modified for EFG by Jacob K Asiedu
 * 
 */
public class ImportDialog extends JDialog {
	static final long serialVersionUID = 1;
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ImportDialog.class);
		} catch (Exception ee) {
		}
	}
	final String importOnlyCommandSpringID ="importOnlyCommand";
	final String importUseMetadataCommandSpringID ="importUseMetadataCommand";
	final String importUpdateCommandSpringID ="importUpdateCommand";
	final String importReplaceCommandSpringID ="importReplaceCommand";
	
	final JButton closeButton = new JButton(
			EFGImportConstants.EFGProperties.getProperty("ImportDialog.closeBtn")
	);

	String simpleDialogDesc = 
		EFGImportConstants.EFGProperties.getProperty("ImportDialog.simpleDialogDesc");
	
	JFrame frame = null;
	final ButtonGroup group = new ButtonGroup();
	final int numButtons = 3;
	ChoiceCommandAbstract choiceCMD;
	
	public ImportDialog(JFrame frame) {
		super(frame, "Select an Option", true);
		this.frame = frame;
		// Create the components.
		ActionListener lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		closeButton.addActionListener(lst);
		getRootPane().setDefaultButton(closeButton);
		getRootPane().registerKeyboardAction(lst,
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		// That will transer focus from first component upon dialog's show
		WindowListener wl = new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				closeButton.requestFocus();
			}
		};
		addWindowListener(wl);
		JPanel frequentPanel = createSimpleDialogBox();
		// Lay them out.
		Border padding = BorderFactory.createEmptyBorder(20, 20, 5, 20);
		frequentPanel.setBorder(padding);

		this.getContentPane().add(frequentPanel, BorderLayout.CENTER);
		setLocationRelativeTo(frame);
		setSize(350, 200);
	}

	/**
	 * @return the selection made by user 0 - For import only. 1 - For import
	 *         using an existing metadata table. 2 - For update of an existing
	 *         table.
	 */
	public ChoiceCommandAbstract getChoiceCommand() {
		return this.choiceCMD;
	}

	/** Creates the panel shown by the first tab. */
	private JPanel createSimpleDialogBox() {
		JRadioButton[] radioButtons = new JRadioButton[numButtons];
		
		final JButton showItButton = new JButton(
				EFGImportConstants.EFGProperties.getProperty("ImportDialog.showItBtn")
				);
		showItButton.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportDialog.showItBtn.tooltip")
		);

	

		radioButtons[0] = new JRadioButton(
				EFGImportConstants.EFGProperties.getProperty("ImportDialog.radioBtn.0")
			);
		radioButtons[0].setActionCommand(importOnlyCommandSpringID);
		radioButtons[0]
				.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportDialog.radioBtn.0.tooltip"));

		radioButtons[1] = new JRadioButton(
				EFGImportConstants.EFGProperties.getProperty("ImportDialog.radioBtn.1")
				);
		radioButtons[1].setActionCommand(importUseMetadataCommandSpringID);
		radioButtons[1]
		             .setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportDialog.radioBtn.1.tooltip"));

		
		radioButtons[2] = new JRadioButton(EFGImportConstants.EFGProperties.getProperty("ImportDialog.radioBtn.3"));
		radioButtons[2]
		             .setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportDialog.radioBtn.3.tooltip"));
		radioButtons[2].setActionCommand(importReplaceCommandSpringID);

		for (int i = 0; i < numButtons; i++) {
			group.add(radioButtons[i]);
		}
		radioButtons[0].setSelected(true);
		showItButton.addActionListener(new CommandListener(this));
		return createPane(simpleDialogDesc + ":", radioButtons, showItButton);
	}

	/**
	 * Used by createSimpleDialogBox and createFeatureDialogBox to create a pane
	 * containing a description, a single column of radio buttons, and the Show
	 * it! button.
	 */
	private JPanel createPane(String description, JRadioButton[] radioButtons,
			JButton showButton) {

		int numChoices = radioButtons.length;
		JPanel box = new JPanel();
		JLabel label = new JLabel(description);

		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
		box.add(label);

		for (int i = 0; i < numChoices; i++) {
			box.add(radioButtons[i]);
		}

		JPanel pane = new JPanel(new BorderLayout());
		pane.add(box, BorderLayout.PAGE_START);
		JPanel pane1 = new JPanel();
		pane1.add(showButton);
		closeButton.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportDialog.closeBtn.tooltip"));
		pane1.add(closeButton);
		pane.add(pane1, BorderLayout.SOUTH);
		return pane;
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the content pane.
		ImportDialog newContentPane = new ImportDialog(null);
		newContentPane.setVisible(true);
		
	}
	public static void main(String[] args) {
		createAndShowGUI();
	}
	class CommandListener implements ActionListener {
		private ImportDialog dialog;
		public CommandListener(ImportDialog dialog) {
			this.dialog = dialog;
		}
		public void actionPerformed(ActionEvent e) {
				String springID = group.getSelection().getActionCommand();
				//log.debug("Selected Command is: " + command);
				//use strategy pattern here
				if(springID == null){
					choiceCMD = null;
				}
				else{
				choiceCMD = 
					SpringNoGUIFactory.getImportCommand(springID);
				
				}
				this.dialog.dispose();
			}
		}
	}
 // ImportDialog
