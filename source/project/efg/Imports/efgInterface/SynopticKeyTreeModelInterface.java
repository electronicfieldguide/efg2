// Borrowed from SortTreeModel.java  by Marc Loy et al
// This class is similar to the DefaultTreeModel, but it keeps
// a node's children in alphabetical order.
//
package project.efg.Imports.efgInterface;

import java.util.Comparator;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public abstract class SynopticKeyTreeModelInterface extends DefaultTreeModel {
	static final long serialVersionUID = 1;

	protected Comparator comparator;

	public SynopticKeyTreeModelInterface(TreeNode node, Comparator c) {
		this(node,false,c);
	}
	
	public SynopticKeyTreeModelInterface(TreeNode node, boolean asksAllowsChildren,
			Comparator c) {
		super(node, asksAllowsChildren);
		this.comparator = c;
	}

	public abstract void insertNodeInto(MutableTreeNode child, MutableTreeNode parent); 
	
}
