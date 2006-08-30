/**
 * 
 */
package project.efg.Imports.efgImportsUtil;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.util.SwingWorker;

import com.Ostermiller.util.Browser;
/**
 * @author kasiedu
 * 
 */
public class DataCheckerCaller extends SwingWorker {
	static Logger log = null;
	static {
		try {
			Browser.init();
			log = Logger.getLogger(DataCheckerCaller.class);
		} catch (Exception ee) {
		}
	}
	private DBObject dbObject;

	private DataChecker dataChecker;

	private String displayName;

	public DataCheckerCaller(DBObject dbObject, EFGDatasourceObjectInterface ds) {

		this.dbObject = dbObject;

		this.displayName = ds.getDisplayName();
		this.dataChecker = new DataChecker(this.dbObject, this.displayName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project. efg.util.SwingWorker#construct()
	 */
	public Object construct() {
		execute();
		return null;
	}

	private void execute() {
		if (this.dataChecker.isReady()) {
			boolean bool = this.dataChecker.checkMediaResources();
			if (bool) {
				JOptionPane.showMessageDialog(null,
						"No errors found for data in Mediaresources field",
						"Success", JOptionPane.INFORMATION_MESSAGE);
			} else {
				 String fileName = this.dataChecker.displayErrors();
				 if(fileName == null){
						JOptionPane.showMessageDialog(null,
								"Application could not create error file ",
								"Error", JOptionPane.ERROR_MESSAGE);
				 }
				 else{
					 JOptionPane.showMessageDialog(null,
								"Application will open a browser window and display error results.\n " +
								"You can also view errors at '" + fileName  + "'",
								"Error", JOptionPane.INFORMATION_MESSAGE); 
					 try{
					 Browser.displayURL(fileName, "target");
					 }
					 catch(Exception ee){
						 JOptionPane.showMessageDialog(null,
									"Application could not find error file '" + fileName  + "'",
									"Error", JOptionPane.ERROR_MESSAGE);  
					 }
				 }
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"Application could check data for errors ",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
