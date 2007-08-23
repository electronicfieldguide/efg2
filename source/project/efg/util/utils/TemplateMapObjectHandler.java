/**
 * 
 */
package project.efg.util.utils;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.util.factory.TemplateModelFactory;



/**
 * @author kasiedu
 * 
 */
public class TemplateMapObjectHandler {
	
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(TemplateMapObjectHandler.class);
		} catch (Exception ee) {
		}
	}
	public static TaxonPageTemplates getTemplateFromDB(DBObject dbObject,
			String displayName, String datasourceName, String dbTableName){
		

	
		try {
			TemplateModelHandler tempHandler = getHandler(dbObject);
			return tempHandler.getTemplateConfigFromDB(dbObject,displayName,datasourceName,dbTableName);
		} catch (Exception ee) {
			log.error(ee.getMessage());
			ee.printStackTrace();
		} 
		return null;
	
		
	}
	private static TemplateModelHandler getHandler(DBObject dbObject){
		TemplateModelHandler tempHandler = null;
		if(dbObject == null){
			tempHandler = TemplateModelFactory.createExportTemplateHandler();
		}
		else{
			tempHandler = TemplateModelFactory.createImportTemplateHandler(dbObject);
		}
		return tempHandler;
	}
	/**
	 * 
	 * @param key
	 * @return the object removed from cache Sync with loaded class too
	 */
	public static boolean removeGuidFromTemplateMap(
			String guid,
			DBObject dbObject) {

		String mutex = "";
		synchronized (mutex) {
		
			try {
				TemplateModelHandler tempHandler = getHandler(dbObject);
				
				boolean bool = tempHandler.removeGuidFromTable(guid);
				if(!bool){
					throw new Exception("Template could not be removed");
				}
				return true;
			} catch (Exception ee) {
				log.error(ee.getMessage());
			} 
			return false;
		}
	}



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
				TemplateModelHandler tempHandler = getHandler(dbObject);
				boolean bool = tempHandler.removeFromDB(datasourceName);
				if(!bool){
					throw new Exception("Template could not be removed");
				}
				return true;
			} catch (Exception ee) {
				log.error(ee.getMessage());
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
				//update template table also
				TemplateModelHandler tempHandler = getHandler(dbObject);
				
				boolean bool = tempHandler.changeDisplayName(datasourceName,displayName);
				
				if(bool){
					//update templateobject
					//FIXME
				}
				else{
					throw new Exception("Template name could not be changed successfully");
				}
			
				return true;
				
			} catch (Exception ee) {
				log.error(ee.getMessage());
				

			} 
			return false;
		}

	}
	public static void batchUpdateDatabase(DBObject dbObject,
			String[] datasourceName, 
			 String tableName,
			 TaxonPageTemplates[] templateObject
	){
		TemplateModelHandler tempHandler = getHandler(dbObject);
		
		tempHandler.bacthUpdateTemplateObject(tableName, 
				datasourceName, templateObject);
	}
	public static void updateDatabase(DBObject dbObject,
			TaxonPageTemplates templateObject, 
			String datasourceName, String dbTableName){
		if (templateObject == null) {
			return;
		}
		String mutex = "";
		synchronized (mutex) {
			try {
				TemplateModelHandler tempHandler = getHandler(dbObject);
				boolean bool =tempHandler.updateTemplateObject(
						datasourceName, 
						dbTableName, 
						templateObject);
				if(!bool){
					throw new Exception("Template not added/removed/updated successfully");
				}
				
			} catch (Exception ee) {
				
				log.error(ee.getMessage());
				
			}
		}
	}
	
	/**
	 * @param key
	 * @param templateObject
	 * @param object
	 */
	public static void updateTemplateDatabase(String key, 
			TemplateObject templateObject, DBObject dbObject) {
		if (templateObject == null) {
			
			return;
		}
		String mutex = "";
		synchronized (mutex) {
			try {
				TemplateModelHandler tempHandler = getHandler(dbObject);
				boolean bool = tempHandler.updateTemplateTable(key,templateObject);
				if(!bool){
					throw new Exception("Template not added successfully");
				}
				
			} catch (Exception ee) {
				log.error(ee.getMessage());
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
				TemplateModelHandler tempHandler = getHandler(dbObject);
				boolean bool = tempHandler.add2DB(key,templateObject);
				if(!bool){
					throw new Exception("Template not added successfully");
				}
				
			} catch (Exception ee) {
				log.error(ee.getMessage());
			}
		}

	}

	// unmodifiable map
	public static Hashtable getTemplateObjectMap(DBObject dbObject) {
		String mutex = "";
		
		synchronized (mutex) {
			try {
				TemplateModelHandler tempHandler =getHandler(dbObject);
				return tempHandler.getAll();
			} catch (Exception ee) {
				
				log.error(ee.getMessage());
			}
			return null;
		}
	}



}
