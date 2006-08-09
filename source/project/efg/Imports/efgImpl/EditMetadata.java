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
package project.efg.Imports.efgImpl;

import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.DataManipulatorInterface;
import project.efg.Imports.efgInterface.SynopticKeyTreeInterface;
import project.efg.Imports.efgInterface.TableSorterMainInterface;
import project.efg.Imports.rdb.TableSorterMain;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 * 
 */
public class EditMetadata extends DataManipulatorInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger log;
	static {
		try {
			log = Logger.getLogger(EditMetadata.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * @param tree
	 */
	public EditMetadata(SynopticKeyTreeInterface tree) {
		super(tree);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project.efg.Imports.efgImpl.DataManipulatorInterface#processNode()
	 */
	public boolean processNode() {
		
		final SynopticKeyTreeInterface tree = this.getSynopticKeyTreeInterface();
		String message = "";
		// get the selected node
		DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (selNode == null) {
			this.selectDatasourceFirst();
			return false;
		}

		MutableTreeNode parent = (MutableTreeNode) (selNode.getParent());
		if (parent == null) {
			message = EFGImportConstants.EFGProperties
					.getProperty("SynopticKeyTree.editMetadata.error");
			this.rootNodeUnEditable(message);
			log.debug(message);
			return false;
		}

		final EFGDatasourceObjectInterface ds = (EFGDatasourceObjectInterface) selNode
				.getUserObject();
		Thread worker = new Thread(){
			public void run(){
				try{
					TableSorterMainInterface newContentPane = 
						new TableSorterMain(
							tree.getDBObject(), ds,tree.frame);
					
					newContentPane.show();
					Thread.sleep(5000);
				}catch(Exception ee){
					
				}
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						
					}
				});
			}
		};
		worker.start();		
		return true;
	}
}
