/**
 * 
 */
package project.efg.client.factory.nogui;

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.efg.client.interfaces.nogui.CSV2DatabaseAbstract;
import project.efg.client.interfaces.nogui.ChoiceCommandAbstract;
import project.efg.client.interfaces.nogui.EFGDataExtractorInterface;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.client.interfaces.nogui.LoginAbstractModule;
import project.efg.client.interfaces.nogui.ThumbNailGeneratorInterface;

/**
 * @author jacob.asiedu
 *
 */
public class SpringNoGUIFactory {
	static Logger log;
	static {
		try {
			log = Logger.getLogger(SpringNoGUIFactory.class);
			doSpring();
		} catch (Exception ee) {
		}
	}
	private static ApplicationContext   context;
	
	public SpringNoGUIFactory(){
		
	}
	

	
	/**
	 * @return
	 */
	private  static void doSpring() {
		
	
			try {
			context = 
				new ClassPathXmlApplicationContext("springconfig_nogui.xml");
				
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
				
			}
		
		
	}
	public static CSV2DatabaseAbstract getCSV2DatabaseAbstract() {
		try {
			return (CSV2DatabaseAbstract)context.getBean("csvImporter");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}
			return null;
	}
	public static EFGDataExtractorInterface getCSVProcessor() {
		try {
			return (EFGDataExtractorInterface)context.getBean("csvProcessor");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}
			return null;
	}
	public static EFGDatasourceObjectInterface getEFGDatasourceObject() {
		try {
			return (EFGDatasourceObjectInterface)context.getBean("datasourceobjectimpl");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}
			return null;
	}
	public static synchronized ChoiceCommandAbstract
	getImportCommand(String springID){
		
	

		try{
			return (ChoiceCommandAbstract)context.getBean(springID);
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return (ChoiceCommandAbstract)context.getBean("importDefaultCommand");	
	}
	public static synchronized LoginAbstractModule getLoginModule() {
	

		try{
			return (LoginAbstractModule)context.getBean("loginmodule");
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Returning default!!");
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
	 * @return
	 */
	public static ThumbNailGeneratorInterface getThumbNailGenerator() {
		try {
	
		return (ThumbNailGeneratorInterface)context.getBean("thumbsGenerator");
		}
		catch(Exception ee) {
			log.error(ee.getMessage());
		}
		return null;
	}
}
