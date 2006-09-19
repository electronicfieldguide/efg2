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
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.plaf.basic.BasicTreeUI.TreeExpansionHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.SynopticKeyTreeModelInterface;
import project.efg.Imports.factory.SynopticKeyTreeModelFactory;
import project.efg.util.EFGImportConstants;

public class FileTree extends JTree implements Autoscroll {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Insets defaultScrollInsets = new Insets(8, 8, 8, 8);
	static Logger log = null;
	static {
		try {
			//log = Logger.getLogger(FileTree.class);
		} catch (Exception ee) {
		}
	}
	private Insets scrollInsets = defaultScrollInsets;

	private SynopticKeyTreeModelInterface model;

	public FileTree(String path) throws FileNotFoundException,
			SecurityException {
		super((TreeModel) null); // Create the JTree itself
		BasicTreeUI ui = new BasicTreeUI();
		TreeExpansionHandler handler = ui.new TreeExpansionHandler();
		// Use horizontal and vertical lines
		putClientProperty("JTree.lineStyle", "Angled");
		this
				.setToolTipText("Drag and Drop one or more Image(s) Folder into this Window.");
		// Create the first node
		int index = path.lastIndexOf(File.separator);
		String parent = null;
		if (index > -1) {
			parent = path.substring(0, index);
			path = path.substring(index + 1, path.length());
		}
		FileTreeNode rootNode = null;
		try{
		rootNode = new FileTreeNode(parent, path);
		
		rootNode.populateDirectories(true);
		//READ FROM PROPERTIES FILE
		
		this.model =SynopticKeyTreeModelFactory.getSynopticKeyTreeModel(rootNode); 
		this.setModel(this.model);
		// make the root node the selected node

		TreePath pathT = new TreePath(rootNode);
		this.scrollPathToVisible(pathT);
		this.setSelectionPath(pathT);
		// Listen for Tree Selection Events
		addTreeExpansionListener(handler);
		}
		catch(Exception ee){
			
		}
	}

	/**
	 * This method removes the selected node from the JTree
	 */
	public void removeSelectedNode() {
		// get the selected node
		DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) this
				.getLastSelectedPathComponent();
		TreePath selectionPath = this.getSelectionPath();
		String fileName = getPathName(selectionPath);
		File file = new File(fileName);
		
		this.removeSelectedNode(selNode, file);
	}

	public void removeSelectedNode(DefaultMutableTreeNode selNode, File file) {
		if (selNode != null) {
			
			// get the parent of the selected node
			MutableTreeNode parent = (MutableTreeNode) (selNode.getParent());

			// if the parent is not null
			if (parent != null) {
				if(file.isDirectory()){
					if (JOptionPane.showConfirmDialog(this,
							"Do you want to delete the directory\n '" + file.getName()+ "' ?",
							"Delete Directory?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
				
						return;
					}
				}
				MutableTreeNode toBeSelNode = getSibling(selNode);

				// if there are no siblings select the parent node after
				// removing the node
				if (toBeSelNode == null) {
					toBeSelNode = parent;
				}
				// make the node visible by scrolling to it
				TreeNode[] nodes = ((SynopticKeyTreeModelImpl) this.getModel())
						.getPathToRoot(toBeSelNode);
				TreePath path = new TreePath(nodes);
				this.scrollPathToVisible(path);
				this.setSelectionPath(path);
				if (file.exists()) {
					try {
						
						
						// delete from file system
						deleteFile(file);
						//replace EFGImages with efgthumbnails
						// remove the node from the parent
						((SynopticKeyTreeModelImpl) this.getModel())
								.removeNodeFromParent(selNode);
						deleteFromThumbNailsDir(file);
					} catch (Exception ee) {
						showErrorMessage(ee.getMessage());
						return;
					}
				} else {
					showErrorMessage("File: " + file.getAbsolutePath()
							+ " does not exists");
				}
			} else {
				showWarningMessage("You are not allowed to delete the root directory of "
						+ "the images folder!!");
				return;

			}
		} else {// we should never get here
			showErrorMessage("Please select a directory to delete!!");
		}
	}
	public synchronized void showErrorMessage(String message) {
		//log.error(message);
		JOptionPane.showMessageDialog(this, message, "Error Message",
				JOptionPane.ERROR_MESSAGE);
	}

	public synchronized void showWarningMessage(String message) {
		//log.info(message);
		JOptionPane.showMessageDialog(this, message, "Warning Message",
				JOptionPane.WARNING_MESSAGE);
	}

	public String getToolTipText(MouseEvent evt) {
		return getToolTipText();
	}

	// Returns the full pathname for a path, or null if not a known path
	public String getPathName(TreePath path) {
		Object o = path.getLastPathComponent();
		if (o instanceof FileTreeNode) {
			return ((FileTreeNode) o).fullName;
		}
		return null;
	}

	// Adds a new node to the tree after construction.
	// Returns the inserted node, or null if the parent
	// directory has not been expanded.
	public FileTreeNode addNode(FileTreeNode parent, String name) {
		int index = parent.addNode(name);
		if (index != -1) {
			((SynopticKeyTreeModelImpl) getModel()).nodesWereInserted(parent,
					new int[] { index });
			return (FileTreeNode) parent.getChildAt(index);
		}
		// No node was created
		return null;
	}

	// Autoscrolling support
	public void setScrollInsets(Insets insets) {
		this.scrollInsets = insets;
	}

	public Insets getScrollInsets() {
		return scrollInsets;
	}

	// Implementation of Autoscroll interface
	public Insets getAutoscrollInsets() {
		Rectangle r = getVisibleRect();
		Dimension size = getSize();
		Insets i = new Insets(r.y + scrollInsets.top, r.x + scrollInsets.left,
				size.height - r.y - r.height + scrollInsets.bottom, size.width
						- r.x - r.width + scrollInsets.right);
		return i;
	}

	public void autoscroll(Point location) {
		JScrollPane scroller = (JScrollPane) SwingUtilities.getAncestorOfClass(
				JScrollPane.class, this);
		if (scroller != null) {
			JScrollBar hBar = scroller.getHorizontalScrollBar();
			JScrollBar vBar = scroller.getVerticalScrollBar();
			Rectangle r = getVisibleRect();
			if (location.x <= r.x + scrollInsets.left) {
				// Need to scroll left
				hBar.setValue(hBar.getValue() - hBar.getUnitIncrement(-1));
			}
			if (location.y <= r.y + scrollInsets.top) {
				// Need to scroll up
				vBar.setValue(vBar.getValue() - vBar.getUnitIncrement(-1));
			}
			if (location.x >= r.x + r.width - scrollInsets.right) {
				// Need to scroll right
				hBar.setValue(hBar.getValue() + hBar.getUnitIncrement(1));
			}
			if (location.y >= r.y + r.height - scrollInsets.bottom) {
				// Need to scroll down
				vBar.setValue(vBar.getValue() + vBar.getUnitIncrement(1));
			}
		}
	}

	/**
	 * This method returns the previous sibling node if there is no previous
	 * sibling it returns the next sibling if there are no siblings it returns
	 * null
	 * 
	 * @param selNode
	 *            selected node
	 * @return previous or next sibling, or parent if no sibling
	 */
	protected MutableTreeNode getSibling(DefaultMutableTreeNode selNode) {
		// get previous sibling
		MutableTreeNode sibling = (MutableTreeNode) selNode
				.getPreviousSibling();
		if (sibling == null) {
			// if previous sibling is null, get the next sibling
			sibling = (MutableTreeNode) selNode.getNextSibling();
		}
		return sibling;
	}

	// Returns the full pathname for a path, or null
	// if not a known path
	/*
	 * public String getPathName(TreePath path) { Object o =
	 * path.getLastPathComponent(); if (o instanceof FileTree.FileTreeNode) {
	 * return ((FileTree.FileTreeNode)o).getFullName(); } return null; }
	 */
	private void deleteFile(File toDelete) {
		if (toDelete.isDirectory()) {
			File[] files = toDelete.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		//also delete from efgthumbnails directory
		toDelete.delete();
	}

	private void deleteFromThumbNailsDir(File file){
		String imageName = file.getAbsolutePath();
		String thumbsName = replace(imageName,
				EFGImportConstants.EFGIMAGES,
				EFGImportConstants.EFGIMAGES_THUMBS);
		
		File thumbsFile = new File(thumbsName);
		if (thumbsFile.exists()) {
			try {
				deleteFile(thumbsFile);
			} catch (Exception ee) {
				//log.error("Could not delete '" + thumbsName + "'");
				return;
			}
		}
	}

	/**
	  *Replace the last occurence of aOldPattern in aInput with aNewPattern
	  * @param aInput is the original String which may contain substring aOldPattern
	  * @param aOldPattern is the non-empty substring which is to be replaced
	  * @param aNewPattern is the replacement for aOldPattern
	  */
	/**
	  *Replace the last occurence of aOldPattern in aInput with aNewPattern
	  * @param aInput is the original String which may contain substring aOldPattern
	  * @param aOldPattern is the non-empty substring which is to be replaced
	  * @param aNewPattern is the replacement for aOldPattern
	  */
	  private  String replace(
	    final String aInput,
	    final String aOldPattern,
	    final String aNewPattern
	  ){
	     if ( aOldPattern.equals("") ) {
	        throw new IllegalArgumentException("Old pattern must have content.");
	     }
	     //log.debug("aInput: " + aInput);
	     //log.debug("aOldPattern: " + aOldPattern);
	     //log.debug("aNewPattern: " + aNewPattern);
	     return aInput.replaceAll(aOldPattern,aNewPattern);
	  }
	  
}
