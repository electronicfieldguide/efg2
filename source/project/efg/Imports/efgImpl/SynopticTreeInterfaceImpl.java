/**
 * SynopticKeyTree.java
 * 
 * Borrowed from SynopticKeyTree.java in javaswing by Marc Loy et al Edited :
 * Mon Feb 20 09:35:00 2006 Edited for EFG by
 * 
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
 * @version 1.0
 */
package project.efg.Imports.efgImpl;

import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgInterface.ChoiceCommandAbstract;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectStateInterface;
import project.efg.Imports.efgInterface.SynopticKeyTreeInterface;
import project.efg.Imports.factory.EFGDatasourceObjectFactory;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 * 
 */
public class SynopticTreeInterfaceImpl extends SynopticKeyTreeInterface {
	static Logger log = null;

	static final long serialVersionUID = 1;
	static {
		try {
			log = Logger.getLogger(SynopticTreeInterfaceImpl.class);
		} catch (Exception ee) {
		}
	}
	public SynopticTreeInterfaceImpl(DBObject dbObject) {
		this(dbObject, null);
	}

	public SynopticTreeInterfaceImpl(DBObject dbObject, JFrame frame) {
		super(dbObject, frame);
	
	}

	public SynopticTreeInterfaceImpl(TreeModel newModel, DBObject dbObject,
			JFrame frame) {
		super(newModel, dbObject, frame);
	
	}

	public SynopticTreeInterfaceImpl(TreeNode root, DBObject dbObject,
			JFrame frame) {
		this(root, false, dbObject, frame);
	}

	public SynopticTreeInterfaceImpl(TreeNode root, boolean asks,
			DBObject dbObject, JFrame frame) {
		super(root, asks, dbObject, frame);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project.efg.Imports.efgImpl.SynopticKeyTreeInterface#importIntoDatabase(java.net.URI)
	 */
	public EFGDatasourceObjectInterface importIntoDatabase(URI file) {
		// Ask this question only when not doing an update

		String fileName = EFGUtils.getName(file);
		int index = fileName.lastIndexOf(".");
		String displayName = fileName;
		if (index > -1) {
			displayName = fileName.substring(0, index);
		}

		EFGDatasourceObjectInterface obj = EFGDatasourceObjectFactory
				.getEFGDatasourceObject();
		obj.setDisplayName(displayName);
		obj.setDataName(file);

		ImportDialog newContentPane = new ImportDialog(this.frame);
		newContentPane.show();
		ChoiceCommandAbstract choiceCMD = newContentPane.getChoiceCommand();
		EFGDatasourceObjectStateInterface state = new NeutralStateObject();
		if (choiceCMD == null) {
			log.debug("Command chosen is null !!!");
			JOptionPane.showMessageDialog(frame,
					EFGImportConstants.EFGProperties
							.getProperty("SynopticKeyTree.terminateImport"));
		} else {
			log.debug("Command chosen: " + choiceCMD.getClass().getName());
			state = choiceCMD.execute(this.lists, obj);

			if ((choiceCMD.getResponseMessage() != null)
					&& !choiceCMD.getResponseMessage().trim().equals("")) {
				JOptionPane.showMessageDialog(frame, choiceCMD
						.getResponseMessage());
			}
			
		}
		obj.setState(state);
		if(state.isSuccess()){
			//if state is not an update then reload
			
			DefaultTreeModel model = (DefaultTreeModel) this.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			state.handleState(obj,root);
		
			this.model.reload();
		}
	
		return obj;

	}
}
