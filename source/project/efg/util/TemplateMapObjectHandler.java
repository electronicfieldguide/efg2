/**
 * 
 */
package project.efg.util;

import java.util.Hashtable;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;

/**
 * @author kasiedu
 * 
 */
public class TemplateMapObjectHandler {
	


	/**
	 * 
	 * @param key
	 * @return the object removed from cache Sync with loaded class too
	 */
	public static boolean removeFromTemplateMap(
			String datasourceName,
			DBObject dbObject) {

		String mutex = "";
		synchronized (mutex) {
		
			try {
				TemplateModelHandler tempHandler = null;
				if(dbObject == null){
					tempHandler = TemplateModelFactory.createExportTemplateHandler();
				}
				else{
					tempHandler = TemplateModelFactory.createImportTemplateHandler(dbObject);
				}
				boolean bool = tempHandler.removeFromDB(datasourceName);
				if(!bool){
					throw new Exception("Template could not be removed");
				}
				return true;
			} catch (Exception ee) {
				LoggerUtilsServlet.logErrors(ee);
			} 
			return false;
		}
	}

	/**
	 * 
	 * @param key
	 * @param templateObject
	 *            sync with file, by saving it to file
	 */
	public static boolean changeDisplayName(
			String datasourceName,
			String displayName, 
			DBObject dbObject) {

		String mutex = "";
		synchronized (mutex) {
			

			try {
				TemplateModelHandler tempHandler = null;
				if(dbObject == null){
					tempHandler = TemplateModelFactory.createExportTemplateHandler();
				}
				else{
					tempHandler = TemplateModelFactory.createImportTemplateHandler(dbObject);
				}
				boolean bool = tempHandler.changeDisplayName(datasourceName,displayName);
				if(!bool){
					throw new Exception("Template name could not be changed successfully");
				}
			
				return true;
				
			} catch (Exception ee) {

				
				LoggerUtilsServlet.logErrors(ee);
				return false;

			} 
		}

	}

	/**
	 * 
	 * @param key
	 * @param templateObject
	 *            sync with file, by saving it to file
	 */
	public static void add2TemplateMap(
			String key,
			TemplateObject templateObject, 
			DBObject dbObject) {
		if (templateObject == null) {
			return;
		}
		String mutex = "";
		synchronized (mutex) {
			try {
				TemplateModelHandler tempHandler = null;
				if(dbObject == null){
					tempHandler = TemplateModelFactory.createExportTemplateHandler();
				}
				else{
					tempHandler = TemplateModelFactory.createImportTemplateHandler(dbObject);
				}
				boolean bool = tempHandler.add2DB(key,templateObject);
				if(!bool){
					throw new Exception("Template not added successfully");
				}
				// release lock
			} catch (Exception ee) {
				LoggerUtilsServlet.logErrors(ee);
			}
		}

	}

	// unmodifiable map
	public static Hashtable getTemplateObjectMap(DBObject dbObject) {
		String mutex = "";
		
		synchronized (mutex) {
			try {
				TemplateModelHandler tempHandler = null;
				if(dbObject == null){
					
					tempHandler = TemplateModelFactory.createExportTemplateHandler();
				}
				else{
					
					tempHandler = TemplateModelFactory.createImportTemplateHandler(dbObject);
				}
				return tempHandler.getAll();
			} catch (Exception ee) {
				System.err.println(ee.getMessage());
				LoggerUtilsServlet.logErrors(ee);
			}
			return null;
		}
	}

	/*private static String getMapKey(String datafn) {
		StringBuffer querySearch = new StringBuffer("/");
		querySearch.append(EFGImportConstants.EFG_APPS);
		querySearch.append("/search?dataSourceName=");
		querySearch.append(datafn);
	
		return querySearch.toString().toLowerCase();
	}*/

}
