/**
 * 
 */
package project.efg.Imports.efgImpl;

import java.io.File;
//import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectStateInterface;
import project.efg.Imports.efgInterface.SynopticKeyTreeInterface;
import project.efg.Imports.factory.StateObjectFactory;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class HandleDataImport {
	static Logger log;
	static {
		try {
			log = Logger.getLogger(HandleDataImport.class);
		} catch (Exception ee) {
		}
	}
	public static boolean handleImport(SynopticKeyTreeInterface tree, 
			List data, StateObjectFactory stateFactory) {
		
		// Grab the tree, its model and the root node
		
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		String message = "";
		File f = null;
		EFGDatasourceObjectInterface obj = null;
		boolean isSuccess = false;
		try {
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

		return true;
		
	}
}
