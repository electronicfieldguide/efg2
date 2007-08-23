/**
 * 
 */
package project.efg.util.factory;

import project.efg.util.utils.DBObject;

import project.efg.util.utils.TemplateModelExport;
import project.efg.util.utils.TemplateModelHandler;
import project.efg.util.utils.TemplateModelImport;



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
