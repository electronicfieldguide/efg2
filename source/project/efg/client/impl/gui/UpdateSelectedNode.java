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
package project.efg.client.impl.gui;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import project.efg.client.interfaces.gui.DataManipulatorInterface;
import project.efg.client.interfaces.gui.SynopticKeyTreeInterface;
import project.efg.client.interfaces.gui.SynopticKeyTreeModelInterface;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.util.interfaces.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class UpdateSelectedNode extends DataManipulatorInterface {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(UpdateSelectedNode.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * @param tree
	 */
	public UpdateSelectedNode(SynopticKeyTreeInterface tree) {
		super(tree);
		
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgImpl.DataManipulatorInterface#processNode()
	 */
	public boolean processNode() {
		SynopticKeyTreeInterface tree = this.getSynopticKeyTreeInterface();
			String message = "";
			// get the selected node
			DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();
			if (selNode == null) {
				return false;
			}
			EFGDatasourceObjectInterface ds = (EFGDatasourceObjectInterface) selNode
			.getUserObject();
			String oldName = ds.getDisplayName();
			// get the parent of the selected node
			MutableTreeNode parent = (MutableTreeNode)(selNode.getParent());
			if (parent == null) {
				message = EFGImportConstants.EFGProperties
						.getProperty("SynopticKeyTree.notAllowed");
				this.rootNodeUnEditable(message);
				return false;
			}
			
			String freshDisplayName = 
				JOptionPane.showInputDialog(tree.frame,"New Display Name:",oldName);
			if (freshDisplayName == null) {
				//log.debug("user terminated request for change in display name.");
				return false;
			} 
			freshDisplayName = freshDisplayName.trim();
			if (("").equals(freshDisplayName)) {
				message = EFGImportConstants.EFGProperties
						.getProperty("SynopticKeyTree.displayEmptyString");
				JOptionPane.showMessageDialog(tree.frame, message, "Error Message",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			

			int childCount = root.getChildCount();
			for (int j = 0; j < childCount; j++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
						.getChildAt(j);
				EFGDatasourceObjectInterface current = (EFGDatasourceObjectInterface) node
						.getUserObject();
				if (!current.equals(ds)) {
					if (current.getDisplayName().equalsIgnoreCase(
							freshDisplayName)) {
						// create a new display name
						while (tree.getLists().contains(freshDisplayName)) {
							freshDisplayName = this
									.generateNewDisplayName(freshDisplayName);
						}

						if (freshDisplayName == null) {
							message = message
									+ EFGImportConstants.EFGProperties
											.getProperty("SynopticKeyTree.renameErrorMessage");
							JOptionPane.showMessageDialog(tree.frame, message,
									"Error Message", JOptionPane.ERROR_MESSAGE);
							return false;
						}
						message = EFGImportConstants.EFGProperties
								.getProperty("SynopticKeyTree.tableAlreadyExists")
								+ ".\n";
						message = message
								+ EFGImportConstants.EFGProperties
										.getProperty("SynopticKeyTree.renameMessage")
								+ " " + freshDisplayName;

						JOptionPane.showMessageDialog(tree.frame, message,
								"Information Message",
								JOptionPane.INFORMATION_MESSAGE);


					}
				}
			}
			// get the sibling node to be selected after removing the
			// selected node
			MutableTreeNode toBeSelNode = getSibling(selNode);
			// if there are no siblings select the parent node after
			// removing the node
			if (toBeSelNode == null) {
				toBeSelNode = parent;
			}

			// make the node visible by scrolling to it
			TreeNode[] nodes = model.getPathToRoot(toBeSelNode);
			TreePath path = new TreePath(nodes);
			tree.scrollPathToVisible(path);
			tree.setSelectionPath(path);
			
			// make a call to database too
			//log.debug("oldName: " + oldName);
			//log.debug("newName: " + freshDisplayName);
			boolean bool = tree.getLists().replaceDisplayName(oldName, freshDisplayName);
			if (bool) {
				ds.setDisplayName(freshDisplayName);
			} else {
				message = EFGImportConstants.EFGProperties
						.getProperty("SynopticKeyTree.displayNotChange");
				JOptionPane.showMessageDialog(tree.frame, message, "Error Message",
						JOptionPane.ERROR_MESSAGE);
				ds.setDisplayName(oldName);
				log.error(message);
			}
			((SynopticKeyTreeModelInterface)tree.getModel()).reload();
		
		return false;
	}
}
