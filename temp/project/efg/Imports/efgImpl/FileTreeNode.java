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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;


import project.efg.Imports.factory.ComparatorFactory;
import project.efg.util.EFGImportConstants;



public  class FileTreeNode extends DefaultMutableTreeNode implements
		Transferable {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(FileTreeNode.class);
		} catch (Exception ee) {
		}
	}
	static final long serialVersionUID = 1;

	protected String name; // Name of this component

	protected String fullName; // Full pathname

	protected boolean populated = false;// true if we have been populated

	protected boolean interim; // true if we are in interim state

	protected boolean isDir; // true if this is a directory

	final static int TREE = 0;
	
	final public static DataFlavor TREENODE_FLAVOR = new DataFlavor(
			FileTreeNode.class, "Tree Node");

	static DataFlavor flavors[] = { TREENODE_FLAVOR,
			DataFlavor.javaFileListFlavor };
	
	
	public FileTreeNode(String parent, String name) throws SecurityException,
			FileNotFoundException {
		this.name = name;
		//this.compFactory= new ComparatorFactoryImpl();
		// See if this node exists and whether it is a directory
		fullName = parent == null ? name : parent + File.separator + name;

		File f = new File(fullName);
		if (f.exists() == false) {
			throw new FileNotFoundException("File " + fullName
					+ " does not exist");
		}

		isDir = f.isDirectory();

		// Hack for Windows which doesn't consider a drive to be a
		// directory!
		if (isDir == false && f.isFile() == false) {
			isDir = true;
		}
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		String str = this.getFullName();
		ArrayList list = new ArrayList();
		try {
			list.add(new File(str));
		} catch (Exception ff) {
			log.error(ff.getMessage());
		}
		return list;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		boolean returnValue = false;
		for (int i = 0, n = flavors.length; i < n; i++) {
			if (flavor.equals(flavors[i])) {
				returnValue = true;
				break;
			}
		}
		return returnValue;
	}

	// Override isLeaf to check whether this is a directory
	public boolean isLeaf() {
		return !isDir;
	}

	// Override getAllowsChildren to check whether this is a directory
	public boolean getAllowsChildren() {
		return isDir;
	}

	// Return whether this is a directory
	public boolean isDir() {
		return isDir;
	}

	// Get full path
	public String getFullName() {
		return fullName;
	}

	// For display purposes, we return our own name
	public String toString() {
		return name;
	}

	// If we are a directory, scan our contents and populate
	// with children. In addition, populate those children
	// if the "descend" flag is true. We only descend once,
	// to avoid recursing the whole subtree.
	// Returns true if some nodes were added
	public boolean populateDirectories(boolean descend) {
		boolean addedNodes = false;

		// Do this only once
		if (populated == false) {
			File f;
			try {
				
				f = new File(fullName);
			} catch (SecurityException e) {
				populated = true;
			
				return false;
			}

			if (interim == true) {
				
				// We have had a quick look here before:
				// remove the dummy node that we added last time
				removeAllChildren();
				interim = false;
			}
			String[] names = f.list(); // Get list of contents
			// Process the contents
			ArrayList list = new ArrayList();
		
			for (int i = 0; i < names.length; i++) {
				
				String name = names[i];
				
				File d = new File(fullName, name);
				try {
					FileTreeNode node = new FileTreeNode(fullName, name);
					list.add(node);
					
					if (descend && d.isDirectory()) {
						
						node.populateDirectories(false);
					}
					addedNodes = true;
					if (descend == false) {
						
						// Only add one node if not descending
						continue;
					}
				} catch (Throwable t) {
					
					// Ignore phantoms or access problems
				}
			}

			if (addedNodes == true) {
				
				// Now sort the list of contained files and directories
				Object[] nodes = list.toArray();
				Arrays.sort(nodes, 
						ComparatorFactory.getComparator(EFGImportConstants.EFGProperties.getProperty("filetreenode.comparator")));
				// Add sorted items as children of this node
				for (int j = 0; j < nodes.length; j++) {
					
					this.add((FileTreeNode) nodes[j]);
				}
			}
			// If we were scanning to get all subdirectories,
			// or if we found no content, there is no
			// reason to look at this directory again, so
			// set populated to true. Otherwise, we set interim
			// so that we look again in the future if we need to
			if (descend == true || addedNodes == false) {
				
				populated = true;
			} else {
			
				// Just set interim state
				interim = true;
			}
			
		}
		
		return addedNodes;
	}

	// Adding a new file or directory after
	// constructing the FileTree. Returns
	// the index of the inserted node.
	public int addNode(String name) {
		// If not populated yet, do nothing
		
		if (populated == true) {
			
			// Do not add a new node if
			// the required node is already there
			int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				FileTreeNode node = (FileTreeNode) getChildAt(i);
				if (node.name.equals(name)) {
					// Already exists - ensure
					// we repopulate
					if (node.isDir()) {
						node.interim = true;
						node.populated = false;
					}
					
					return -1;
				}
				
			}
			// Add a new node
			try {
				FileTreeNode node = new FileTreeNode(fullName, name);
				
				add(node);
				return childCount;
			} catch (Exception e) {
			}
		}
		return -1;
	}

}
