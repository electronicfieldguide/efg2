/**
 * 
 */
package project.efg.Imports.efgImportsUtil;

import org.apache.log4j.Logger;

import com.Ostermiller.util.Browser;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.util.SwingWorker;
/**
 * @author kasiedu
 * 
 */
public abstract class DataCheckerCaller extends SwingWorker {
	static Logger log = null;
	static {
		try {
			Browser.init();
			log = Logger.getLogger(DataCheckerCaller.class);
		} catch (Exception ee) {
		}
	}
	
	
	protected DBObject dbObject;

	

	protected String displayName;

	public DataCheckerCaller(DBObject dbObject, String displayName) {

		this.dbObject = dbObject;
		this.displayName = displayName;
		
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

	public abstract void execute(); 

}
