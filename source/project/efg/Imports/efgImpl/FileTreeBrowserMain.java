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
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import project.efg.util.EFGImportConstants;



/**
 * FileTreeBrowserMain.java
 * 
 * 
 * Created: Sat Feb 18 16:41:14 2006
 * 
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
 * @version 1.0
 */
public class FileTreeBrowserMain extends JDialog {

	static final long serialVersionUID = 1;
	
	FileTree tree;

	FileTreeBrowser target;

	static String imageL = 
		EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.imageL");

	final JButton deleteBtn = 
		new JButton(EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.deleteBtn"));

	final JButton doneBtn =
		new JButton(EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.doneBtn"));


	final JButton helpBtn = 
		new JButton(EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.helpBtn"));


	JFrame frame;
	JComponent treeView;
	JComponent imageView;
	EFGJLabel imageLabel;
	static int maxDim = 0;
	protected JPanel panel = new JPanel();
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(FileTreeBrowserMain.class);
		} catch (Exception ee) {
		}
	}

	public FileTreeBrowserMain(JFrame frame, boolean modal,
			String imagesDirectory) {
		this(frame, "", modal, imagesDirectory);
	}

	public FileTreeBrowserMain(String imagesDirectory, JFrame frame) {
		this(frame, "", false, imagesDirectory);
	}

	public FileTreeBrowserMain(JFrame frame, String title, boolean modal,
			String imagesDirectory) {
		super(frame, title, modal);
		this.frame = frame;
		setSize(new Dimension(720, 400));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		try {
			this.tree = new FileTree(imagesDirectory);
			this.tree.setEditable(false);
			this.tree.setShowsRootHandles(true);
			this.tree.addTreeSelectionListener(new FileTreeSelectionListener(
					tree));
			this.target = new FileTreeBrowser(tree);
			initTree();
		} catch (Exception ee) {
			showErrorMessage(ee.getMessage());
			return;
		}
		treeView = addTreePanel();
		imageView = addImagePanel();
		// Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(treeView);
		splitPane.setRightComponent(imageView);

		/*
		 * Dimension minimumSize = new Dimension(100, 50);
		 * htmlView.setMinimumSize(minimumSize);
		 * treeView.setMinimumSize(minimumSize);
		 */
		splitPane.setDividerLocation(350); // XXX: ignored in some releases
		// of Swing. bug 4101306
		// workaround for bug 4101306:
		// treeView.setPreferredSize(new Dimension(100, 100));

		splitPane.setPreferredSize(new Dimension(700, 300));

		// Add the split pane to this panel.
		setContentPane(splitPane);
		// read catalina home environment variable
	} // FileTreeBrowserMain constructor

	public synchronized void showErrorMessage(String message) {
		log.error(message);
		JOptionPane.showMessageDialog(frame, message, "Error Message",
				JOptionPane.ERROR_MESSAGE);
	}

	public synchronized void showWarningMessage(String message) {
		log.info(message);
		JOptionPane.showMessageDialog(frame, message, "Warning Message",
				JOptionPane.WARNING_MESSAGE);
	}

	public void close() {
		this.dispose();
	}

	public void initTree() {
		this.tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	private JScrollPane addImagePanel() {
		// Create the HTML viewing pane.
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.imageLabel = new EFGJLabel(FileTreeBrowserMain.imageL);
		this.imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(this.imageLabel, BorderLayout.CENTER);
		JScrollPane imgView = new JScrollPane(panel);
		return imgView;

	}
	public static int getMaxDim(){
		
		if(maxDim <= 0){
			String maxDimStr = 
				EFGImportConstants.EFGProperties.getProperty(EFGImportConstants.MAX_DIM_STR);
		
			if((maxDimStr == null)){
				maxDimStr = EFGImportConstants.MAX_DIM;
			}
			try{
				maxDim = Integer.parseInt(maxDimStr);
				log.debug("MaxDim is set from properties file to: " + maxDim);
			}
			catch(Exception ee){
				try{
				maxDimStr = EFGImportConstants.MAX_DIM;
				maxDim = Integer.parseInt(maxDimStr);
				}
				catch(Exception eex){
					log.error(eex.getMessage());
					log.error("Default max dimension is not set!!!");
				}
			}
				log.debug("MaxDim is set to: " + maxDim);
		}
		
		return maxDim;
	}
	private JPanel addTreePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel btnPanel = new JPanel();

		deleteBtn.addActionListener(new DeleteListener(this.tree));
		deleteBtn
				.setToolTipText(				
						EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.deleteBtn.tooltip")
);
		btnPanel.add(deleteBtn);

		helpBtn.addActionListener(new HelpListener());
		helpBtn.setToolTipText(
				EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.helpBtn.tooltip")
				);


		btnPanel.add(helpBtn);

		doneBtn.addActionListener(new DoneListener(this));
		doneBtn.setToolTipText(
				EFGImportConstants.EFGProperties.getProperty("FileTreeBrowserMain.doneBtn.tooltip")
				);
		
		btnPanel.add(doneBtn);

		panel.add(new JScrollPane(this.tree), BorderLayout.CENTER);
		panel.add(btnPanel, BorderLayout.SOUTH);
		return panel;
	}

	class FileTreeSelectionListener implements TreeSelectionListener {
		private FileTree tree;

		public FileTreeSelectionListener(FileTree tree) {
			super();
			this.tree = tree;
		}
		public void valueChanged(TreeSelectionEvent e) {
			try{
				Object o = tree.getLastSelectedPathComponent();
				if (o instanceof FileTreeNode) {// only do this when it
												// is an instance
					FileTreeNode node = (FileTreeNode) o;
					String path = node.getFullName();
					if (path == null) {
						imageLabel.setText(FileTreeBrowserMain.imageL);
					} else {
						imageLabel.setText("");
					}
					imageLabel.setEFGJLabel(path,getMaxDim());
					imageLabel.setLocation(60,60);
					imageLabel.repaint();
				}
			}
			catch(Exception ee){
				imageLabel.setText("Image too large to draw");
			}
		}
	}
	class DeleteListener implements ActionListener {
		private FileTree tree;

		public DeleteListener(FileTree tree) {
			this.tree = tree;
		}

		public void actionPerformed(ActionEvent evt) {
			this.tree.removeSelectedNode();
		}

	}

	class DoneListener implements ActionListener {
		private FileTreeBrowserMain treeBrowser;

		public DoneListener(FileTreeBrowserMain treeBrowser) {
			this.treeBrowser = treeBrowser;
		}

		public void actionPerformed(ActionEvent evt) {
			this.treeBrowser.close();
		}
	}
} // FileTreeBrowserMain
