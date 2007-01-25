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
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.SynopticKeyTreeInterface;
import project.efg.Imports.factory.StateObjectFactory;

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
			log.error("Returning false");
			return false;
		}
		if (!t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			log.error("Returning false list not supported");
			return false;
		}

		// Grab the tree, its model and the root node
		SynopticKeyTreeInterface tree = (SynopticKeyTreeInterface) comp;
		List data;
		try {
		
			data = (List)t.getTransferData(DataFlavor.javaFileListFlavor);
			if(data == null || data.size() == 0) {
				log.error("Empty lists");
			}
			return HandleDataImport.handleImport(tree, 
					data, this.stateFactory);
		} catch (UnsupportedFlavorException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			
		log.error(e.getMessage());
		}
		return false;
	}

	// We only support file lists on SynopticKeyTrees...
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		if (comp instanceof SynopticKeyTreeInterface) {
			for (int i = 0; i < transferFlavors.length; i++) {
				
				if (transferFlavors[i].equals(DataFlavor.javaFileListFlavor)) {
					
					return true;
				}
			}
		}
		return false;
	}
}