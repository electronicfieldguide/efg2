/**
 * 
 */
package project.efg.Imports.factory;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgImportsUtil.DataCheckerCaller;
import project.efg.Imports.efgImportsUtil.IllegalCharactersDataCheckerCaller;
import project.efg.Imports.efgImportsUtil.MediaResourceDataCheckerCaller;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class DataCheckerCallerFactory {

	/**
	 * 
	 */
	private DataCheckerCallerFactory() {
	
	}
	public static DataCheckerCaller
	getInstance(DBObject dbObject, String ds,String checkerType){
		
		if((checkerType == null) || 
				(checkerType.trim().equals(""))){
			
			return new MediaResourceDataCheckerCaller(dbObject,ds);
		}
		else if(checkerType.trim().equalsIgnoreCase(EFGImportConstants.MEDIARESOURCE)){
			
			return new MediaResourceDataCheckerCaller(dbObject,ds);
		}
		else if(checkerType.trim().equalsIgnoreCase(EFGImportConstants.ILLEGALCHARACTER_STRING)){
			return new IllegalCharactersDataCheckerCaller(dbObject,ds);
		}
	
		return new MediaResourceDataCheckerCaller(dbObject,ds);
		
		
	}

}
