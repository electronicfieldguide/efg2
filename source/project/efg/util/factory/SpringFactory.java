/**
 * 
 */
package project.efg.util.factory;

import java.io.FilenameFilter;
import java.util.Comparator;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;
import project.efg.util.interfaces.EFGRowMapperInterface;
import project.efg.util.interfaces.OperatorInterface;
import project.efg.util.interfaces.QueryExecutorInterface;
import project.efg.util.utils.EFGDisplayObject;
import project.efg.util.utils.EFGParseObjectList;
import project.efg.util.utils.EFGParseStates;
import project.efg.util.utils.EFGRowMapperImpl;
import project.efg.util.utils.TemplateModelHandler;
import project.efg.util.utils.TemplateObject;
import project.efg.util.utils.UnicodeToASCIIFilter;
import project.efg.util.utils.XMLFileNameFilter;


/**
 * @author jacob.asiedu
 *
 */
public class SpringFactory {
	static Logger log;
	private static ApplicationContext   context;
	static {
		try {
			
			log = Logger.getLogger(SpringFactory.class);
			doSpring();
		} catch (Exception ee) {
			
		}
	}
	
	public SpringFactory(){
		
	}

	/**
	 * @return
	 */
	private static void doSpring() {
		try {
			if(EFGImportConstants.EFGProperties.getProperty("server.factory.properties") != null){
				context = 
					new ClassPathXmlApplicationContext(
							"springconfig_util.xml", project.efg.util.factory.SpringFactory.class);			
				
			}
			else{
				context = 
					new ClassPathXmlApplicationContext(
							"springconfig_util.xml");			
	
			}
		}
		catch(Exception ee) {
			log.error(ee.getMessage());
			
		}
	}

	/**
	 * 
	 * @return
	 */
	public static QueryExecutorInterface getQueryExecutor(){
		try {
			
			return (QueryExecutorInterface)context.getBean("queryexecutor");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
				ee.printStackTrace();
			}
		return null;
	}
	/**
	 * 
	 * @param springID
	 * @return
	 */
	public static Comparator getComparator(String springID) {
		try {		
			return (Comparator)context.getBean(springID);
		}
		catch(Exception ee) {
			
			log.error(ee.getMessage());
		}
		return (Comparator)context.getBean("default_comparator");
	}
	/**
	 * 
	 * @return
	 */
	public static  EFGQueueObjectInterface getEFGQueueObject(){
		try {
			
			return (EFGQueueObjectInterface)context.getBean("queue_object");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}
			return null;
	}
	/**
	 * 
	 * @param springID
	 * @return
	 */
	public static  OperatorInterface getOperatorInstance(String springID){
		try {
		
			return (OperatorInterface)context.getBean(springID);
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}
			
			return (OperatorInterface)context.getBean("defaultOperator");
	}
	/**
	 * 
	 * @return
	 */
	public static TemplateObject getTemplateObject(){
		
			try {
			
			return (TemplateObject)context.getBean("templateobject");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}	
			return new TemplateObject();
	}
	/**
	 * 
	 * @return
	 */
	public static EFGDisplayObject getDisplayObject(){
			try {
			
			return (EFGDisplayObject)context.getBean("displayobject");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}	
			return new EFGDisplayObject();
	}
	/**
	 * 
	 * @return a TemplateModelExport object 
	 */
	public static TemplateModelHandler createExportTemplateHandler(){
		try {
		
			return (TemplateModelHandler)context.getBean("templateModelHandler");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}
			return null;
		//return new TemplateModelExport();
	}
	/**
	 * 
	 * @return
	 */
	public static DriverManagerDataSource getDriverManagerDatasource(){
		try {
		
			return (DriverManagerDataSource)context.getBean("datasourceManager");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}
			return new DriverManagerDataSource();
	}
	/**
	 * 
	 * @return
	 */
	public static EFGRowMapperInterface getRowMapper(){
		
		try {
		
			return (EFGRowMapperInterface)context.getBean("rowmapper");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
				return new EFGRowMapperImpl();
			}
	}
	/**
	 * 
	 * @return
	 */
	public static EFGParseStates getEFGParseStatesInstance(){
		try {
			
			return (EFGParseStates)context.getBean("efgparsestates");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
				return new EFGParseStates();
			}
	}

	/**
	 * @return
	 */
	public static UnicodeToASCIIFilter getUnicode2Ascii() {
try {
			
			return (UnicodeToASCIIFilter)context.getBean("unicode2ascii");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
				return new UnicodeToASCIIFilter();
			}
	}

	/**
	 * @return
	 */
	public static EFGParseObjectList getParseObjectList() {
		try {
			
			return (EFGParseObjectList)context.getBean("parseobjectlist");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
				return new EFGParseObjectList();
			}
	}


	public static FilenameFilter getFileNameFilter() {
		try {
			
			return (FilenameFilter)context.getBean("xmlfilenamefilter");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
				return new XMLFileNameFilter();
			}
	}
	

	
}
