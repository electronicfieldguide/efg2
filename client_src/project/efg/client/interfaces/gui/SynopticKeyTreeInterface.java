package project.efg.client.interfaces.gui;
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
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import project.efg.client.factory.gui.GUIFactory;
import project.efg.client.factory.gui.SpringGUIFactory;
import project.efg.client.factory.nogui.NoGUIFactory;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.DBObject;

/**
 * SynopticKeyTree.java
 * 
 * Borrowed from SynopticKeyTree.java in javaswing by Marc Loy et al Edited :
 * Mon Feb 20 09:35:00 2006 Edited for EFG by
 * 
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
 * @version 1.0
 */
public abstract class SynopticKeyTreeInterface extends JTree{
	static Logger log = null;

	static final long serialVersionUID = 1;
	static {
		try {
			log = Logger.getLogger(SynopticKeyTreeInterface.class);
		} catch (Exception ee) {
		}
	}
	
	
	private DBObject dbObject;
	public JFrame frame;
	protected SynopticKeyTreeModelInterface model;

	protected EFGDatasourceObjectListInterface lists;

	public SynopticKeyTreeInterface(DBObject dbObject,JFrame frame) {
		super((TreeModel) null);
		this.frame = frame;
		this.dbObject = dbObject;
		
		this.init();
	}
	public EFGDatasourceObjectListInterface getLists(){
		return this.lists;
	}
	public SynopticKeyTreeInterface(TreeModel newModel, DBObject dbObject, JFrame frame) {
		super(newModel);
		this.frame = frame;
		this.dbObject = dbObject;
	
		this.init();
		
	}

	public SynopticKeyTreeInterface(TreeNode root, DBObject dbObject, JFrame frame) {
		this(root, false, dbObject, frame);
	}

	public SynopticKeyTreeInterface(TreeNode root, boolean asks, DBObject dbObject,
			JFrame frame) {
		super(root, asks);
		this.frame = frame;
		this.dbObject = dbObject;
		
		this.init();
		
	}
	public String getToolTipText(MouseEvent evt) {
		return getToolTipText();
	}
	/**
	 * @param file - The file whose contents will be imported into database
	 */
	public abstract  EFGDatasourceObjectInterface importIntoDatabase(URI file);
	public DBObject getDBObject(){
		return this.dbObject;
	}
	/**
	 * A template method that must be implemented by all sub-classes
	 * 
	 * @see project.efg.Imports.efgImpl.SynopticKeyTreeInterface#createNodesFromDB()
	 */
	private void createNodesFromDB() {
		EFGDatasourceObjectInterface rootDS = NoGUIFactory
				.getEFGDatasourceObject();
		rootDS.setDisplayName(EFGImportConstants.EFG_DATABASE_ROOT_NAME);
		rootDS.setDataName(URI
				.create(EFGImportConstants.EFG_DATABASE_DATA_NAME));
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootDS);
		// reset the model
		this.model = GUIFactory.getSynopticKeyTreeModel(root);

		this.setModel(this.model);
		if (this.getDBObject() == null) {
			log.debug("dbObject is null");
		} else {
			log.debug("dbObject is not null");
		}
		this.lists = NoGUIFactory
				.getEFGObjectList(this.getDBObject());

		Iterator iter = this.lists.getEFGDatasourceObjectListIterator();

		// for each element create a TreeNode element and add to tree
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		root = (DefaultMutableTreeNode) model.getRoot();
		while (iter.hasNext()) {
			EFGDatasourceObjectInterface obj = (EFGDatasourceObjectInterface) iter
					.next();
			root.add(new DefaultMutableTreeNode(obj));
		}
		log.debug("Reloading model!!");
		this.model.reload();
	}
	private void init() {
		this.setRowHeight(0);
		this.putClientProperty("JTree.lineStyle", "Angled");
		this.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setEditable(false);
		this.setShowsRootHandles(true);

		// create nodes
		this.createNodesFromDB();

		// make the root node the selected node
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		TreePath path = new TreePath(root);
		this.scrollPathToVisible(path);
		this.setSelectionPath(path);

		setDragEnabled(false);
		//read properties
		String isLinux = EFGImportConstants.EFGProperties.getProperty("efg2.system.os","windowsflavor");
		if(!isLinux.equalsIgnoreCase("islinuxflavor")){
			setTransferHandler(SpringGUIFactory.getTransferHandler());
		}
		addTreeExpansionListener(SpringGUIFactory.getTreeExpansionListener());
	}
}
