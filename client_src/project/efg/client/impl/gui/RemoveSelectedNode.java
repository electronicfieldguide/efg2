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
public class RemoveSelectedNode extends DataManipulatorInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger log;
	static {
		try {
			log = Logger.getLogger(RemoveSelectedNode.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * @param tree
	 */
	public RemoveSelectedNode(SynopticKeyTreeInterface tree) {
		super(tree);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project.efg.Imports.efgImpl.DataManipulatorInterface#processNode()
	 */
	public boolean processNode() {
		SynopticKeyTreeInterface tree = this.getSynopticKeyTreeInterface();
		String message = "";
		// get the selected node
		DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (selNode == null) {
			this.selectDatasourceFirst();
			return false;
		}
	
			
			//warn user
		

		// get the parent of the selected node
		MutableTreeNode parent = (MutableTreeNode) (selNode.getParent());
		// if the parent is not null
		if (parent == null) {
			message = EFGImportConstants.EFGProperties
					.getProperty("SynopticKeyTree.dbdeletemessage");

			this.rootNodeUnEditable(message);
			log.debug(message);
			return false;
		}

		EFGDatasourceObjectInterface ds = 
			(EFGDatasourceObjectInterface)selNode.getUserObject();
		
			
			int result = JOptionPane.showConfirmDialog(null,
					"Do you really want to delete the data source:\n " + ds.getDisplayName() + "?", "Delete Data source?",
					JOptionPane.YES_NO_OPTION);
			if (result != JOptionPane.YES_OPTION) {
				return false;
			}
		// TreePath selectionPath =tree.getSelectionPath();
		// get the sibling node to be selected after removing the
		// selected node
		MutableTreeNode toBeSelNode = getSibling(selNode);
		// if there are no siblings select the parent node after
		// removing the node
		if (toBeSelNode == null) {
			toBeSelNode = parent;
		}
		// make the node visible by scrolling to it
		TreeNode[] nodes = ((DefaultTreeModel) tree.getModel())
				.getPathToRoot(toBeSelNode);
		TreePath path = new TreePath(nodes);
		tree.scrollPathToVisible(path);
		tree.setSelectionPath(path);
		

		if (tree.getLists().removeEFGDatasourceObject(ds.getDisplayName())) {// make
			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(selNode);
			((SynopticKeyTreeModelInterface)tree.getModel()).reload(parent);
			
		
			//remove the datasource template file from the file system
			//find out if server is running
			//if it is remove from cached object
			//if not remove from Template.out
			//also empty cache of all resources that start with that name
			//look for all caches
			
			return true;
		}
		message = EFGImportConstants.EFGProperties
				.getProperty("SynopticKeyTree.dbremovemessage");
		log.error(message);
		JOptionPane.showMessageDialog(tree.frame, message, "Error Message",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

}
