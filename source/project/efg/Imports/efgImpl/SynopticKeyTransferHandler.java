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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectStateInterface;
import project.efg.Imports.efgInterface.SynopticKeyTreeInterface;
import project.efg.Imports.factory.StateObjectFactory;
import project.efg.util.EFGImportConstants;

public class SynopticKeyTransferHandler extends TransferHandler {

	static Logger log;
	static {
		try {
			log = Logger.getLogger(SynopticKeyTransferHandler.class);
		} catch (Exception ee) {
		}
	}

	static final long serialVersionUID = 1;
	private StateObjectFactory stateFactory;
	
	public SynopticKeyTransferHandler(){
		super();
		this.stateFactory = new StateObjectFactory();
	}
	public boolean importData(JComponent comp, Transferable t) {
		String message = "";
		// Make sure we have the right starting points
		if (!(comp instanceof JTree)) {
			// log an error message
			return false;
		}
		if (!t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			// log an error message
			return false;
		}

		// Grab the tree, its model and the root node
		SynopticKeyTreeInterface tree = (SynopticKeyTreeInterface) comp;
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		File f = null;
		EFGDatasourceObjectInterface obj = null;
		boolean isSuccess = false;
		try {

			List data = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
			Iterator i = data.iterator();
			while (i.hasNext()) {
				// boolean isFound = false;
				f = (File) i.next();
				// if file is a directory log and skip
				if (f.isDirectory()) {
					message = f.getAbsolutePath()
							+ " "
							+ EFGImportConstants.EFGProperties
									.getProperty("SynopticKeyTransferHandler.directoryNoImport");
					log.error(message);
					JOptionPane.showMessageDialog(null, message,
							"Import Information",
							JOptionPane.INFORMATION_MESSAGE);
					continue;
				}
				//f.toURI()
				//find out if this is a valid uri
				//write to a temp file and then 
				//upload the temp file instead
				URI uri = f.toURI();
			
				obj = tree.importIntoDatabase(f.toURI());

				EFGDatasourceObjectStateInterface state = obj.getState();
				state.handleState(obj, root);
				if(!isSuccess){
					isSuccess = state.isSuccess();
				}
			}
		} catch (Exception ee) {
			message = ee.getMessage();
			ee.printStackTrace();
			log.error(message);
			obj = new EFGDatasourceObjectImpl();
			if (f != null) {
				obj.setDataName(f.toURI());
				EFGDatasourceObjectStateInterface state =
					stateFactory.getFailureObject();
					
				state.handleState(obj, root);
				if(!isSuccess){
					isSuccess = state.isSuccess();
				}
			}
			return isSuccess;
		}
		/*
		 * boolean success = false; StringBuffer messageBuffer = new
		 * StringBuffer(); if(addedList.size() > 0){ messageBuffer.append(
		 * this.getInformationFromLists(addedList,EFGImportConstants.
		 * EFGProperties.getProperty(
		 * "SynopticKeyTransferHandler.add.success.title")));
		 * 
		 * success = true; }
		 * messageBuffer.append(this.getInformationFromLists(notAddedList,EFGImportConstants.
		 * EFGProperties.getProperty(
		 * "SynopticKeyTransferHandler.add.failure.title")));
		 * 
		 * messageBuffer.append(this.getInformationFromLists(neutralList,EFGImportConstants.
		 * EFGProperties.getProperty(
		 * "SynopticKeyTransferHandler.add.neutral.title")));
		 * 
		 * if(success){ JOptionPane.showMessageDialog(null,
		 * messageBuffer.toString(), "Import Information",
		 * JOptionPane.INFORMATION_MESSAGE); } else{
		 * JOptionPane.showMessageDialog(null, messageBuffer.toString(), "Error
		 * Message", JOptionPane.ERROR_MESSAGE); }
		 */
		return true;
	}

	/*
	 * private String getInformationFromLists(ArrayList list, String message){
	 * StringBuffer messageBuffer = new StringBuffer(); if(list.size() > 0){
	 * if(messageBuffer.length() > 0){ messageBuffer.append("\n\n"); }
	 * messageBuffer.append(message); messageBuffer.append("\n\n"); for(int i=0;
	 * i < list.size(); i++){ messageBuffer.append((i +1) + ":" +
	 * (String)list.get(i)); messageBuffer.append("\n"); }
	 *  } return messageBuffer.toString(); } private void
	 * handleNeutral(java.util.ArrayList neutralList, String fileName){
	 * StringBuffer message = new StringBuffer(fileName); message.append(" ");
	 * message.append(EFGImportConstants.EFGProperties.
	 * getProperty("SynopticKeyTransferHandler.update.neutral"));
	 * neutralList.add(message.toString()); //log.debug(message.toString()); }
	 * private void handleFailure(java.util.ArrayList notAddedList, String
	 * fileName){ StringBuffer failureMessage = new StringBuffer(fileName);
	 * failureMessage.append(" ");
	 * failureMessage.append(EFGImportConstants.EFGProperties.
	 * getProperty("SynopticKeyTransferHandler.add.failure"));
	 * notAddedList.add(failureMessage); //log.error(failureMessage.toString()); }
	 * private boolean handleSuccess(java.util.ArrayList addedList,
	 * EFGDatasourceObjectInterface obj, DefaultMutableTreeNode root, String
	 * fileName){ boolean isFound = false; StringBuffer message = new
	 * StringBuffer(); int childCount = root.getChildCount(); for (int j = 0; j <
	 * childCount; j++) { DefaultMutableTreeNode node = (DefaultMutableTreeNode)
	 * root .getChildAt(j); if (((EFGDatasourceObjectInterface) node
	 * .getUserObject()).equals(obj)) {//node already exists isFound = true;
	 * message.append(fileName); message.append(" ");
	 * message.append(EFGImportConstants.EFGProperties.
	 * getProperty("SynopticKeyTransferHandler.update.success"));
	 * //log.debug(message.toString()); addedList.add(message.toString()); break; } }
	 * return isFound;
	 *  }
	 */
	// We only support file lists on SynopticKeyTrees...
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		if (comp instanceof SynopticKeyTreeInterface) {
			for (int i = 0; i < transferFlavors.length; i++) {
				if (!transferFlavors[i].equals(DataFlavor.javaFileListFlavor)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}