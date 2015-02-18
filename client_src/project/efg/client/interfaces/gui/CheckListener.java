/**
 * 
 */
package project.efg.client.interfaces.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import project.efg.client.factory.nogui.NoGUIFactory;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.client.utils.nogui.DataCheckerCaller;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.DBObject;

/**
 * @author kasiedu
 *
 */
public class CheckListener implements ActionListener {
	static Logger log;
	static {
		try {
			log = Logger.getLogger(CheckListener.class);
		} catch (Exception ee) {
		}
	}
	private DBObject dbObject;
	
	private EFGDatasourceObjectInterface ds;
	private SynopticKeyTreeInterface tree;
	private String checkerType;

	private boolean init(){
	
			DefaultMutableTreeNode selNode = 
				(DefaultMutableTreeNode)this.tree.getLastSelectedPathComponent();
			
			if (selNode == null) {
				this.selectDatasourceFirst();
				return false;
			}

			this.ds = (EFGDatasourceObjectInterface) selNode
					.getUserObject();
			return true;
	}
	

	protected void selectDatasourceFirst() {
		String message = EFGImportConstants.EFGProperties
				.getProperty("SynopticKeyTree.selectClick");
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error Message",
				JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * @param object
	 * @param main
	 */
	public CheckListener(DBObject dbObject, 
			SynopticKeyTreeInterface tree, String checkerType) {
		this.dbObject = dbObject;
		this.checkerType = checkerType;
		this.tree = tree;
		
	}

	public void actionPerformed(ActionEvent evt) {
		if(this.init()){
			DataCheckerCaller dCaller = 
				NoGUIFactory.getDataCheckerCallerInstance(this.dbObject, 
						this.ds.getDisplayName(), this.checkerType);
					 dCaller.start();
		
		}
	}
}
