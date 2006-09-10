/**
 * 
 */
package project.efg.util;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.rdb.TemplateModelImport;
import project.efg.servlets.rdb.TemplateModelExport;


/**
 * @author jacob.asiedu
 *
 */
public class TemplateModelFactory {
	public TemplateModelFactory() {
		
	}
	/**
	 * 
	 * @return a TemplateModelExport object 
	 */
	public static TemplateModelHandler createExportTemplateHandler(){
		return new TemplateModelExport();
	}
/**
 * 
 * @param dbObject
 * @return a TemplateModelImport object 
 */
	public static TemplateModelHandler createImportTemplateHandler(DBObject dbObject){
		return new TemplateModelImport(dbObject);
	}
}
