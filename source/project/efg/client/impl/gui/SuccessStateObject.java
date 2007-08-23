/**
 * $Id: SuccessStateObject.java,v 1.1.1.1 2007/08/01 19:11:15 kasiedu Exp $
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

import org.apache.log4j.Logger;

import project.efg.client.factory.gui.SpringGUIFactory;
import project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.util.interfaces.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class SuccessStateObject implements 
EFGDatasourceObjectStateInterface {
	static Logger log;
	static {
		try {			
			log = Logger.getLogger(SuccessStateObject.class);
		} catch (Exception ee) {
		}
	}
	
	public SuccessStateObject(){
		
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGDatasourceObjectStateInterface#handleState(project.efg.util.interfaces.EFGDatasourceObjectInterface, javax.swing.tree.DefaultMutableTreeNode)
	 */
	public void handleState(EFGDatasourceObjectInterface obj, 
			DefaultMutableTreeNode root) {
		boolean isFound = false;
		String fileName = obj.getDataName().toString();
		StringBuffer message = new StringBuffer();
		int childCount = root.getChildCount();
		for (int j = 0; j < childCount; j++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
					.getChildAt(j);
			if (((EFGDatasourceObjectInterface) node
					.getUserObject()).equals(obj)) {//node already exists
				isFound = true;
				message.append(fileName); 
				message.append(" "); 
				message.append(EFGImportConstants.EFGProperties.
						getProperty("SynopticKeyTransferHandler.update.success"));
				log.debug(message.toString());
				break;
			}	
		}
		if(!isFound){
			try {//node was added to database successfully
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(
						obj);
				root.add(node);
				StringBuffer successMessage = 
					new StringBuffer(fileName);
				successMessage.append(" "); 
				successMessage.append(EFGImportConstants.EFGProperties.
						getProperty("SynopticKeyTransferHandler.add.success"));
				log.debug(successMessage.toString());
				JOptionPane.showMessageDialog(null,
						successMessage.toString(), 
						EFGImportConstants.EFGProperties.getProperty(
						"SynopticKeyTransferHandler.add.success.title"),
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				//failure on State
				log.error(e.getMessage());
				//use a factory
				EFGDatasourceObjectStateInterface state = 
					SpringGUIFactory.getFailureObject();
					obj.setState(state);
					state.handleState(obj,root);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGDatasourceObjectStateInterface#isSuccess()
	 */
	public boolean isSuccess() {
		return true;
	}
}
