/**
 * 
 */
package project.efg.client.factory.gui;

import java.util.Comparator;

import javax.swing.TransferHandler;
import javax.swing.event.TreeExpansionListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface;



/**
 * @author jacob.asiedu
 *
 */
public class SpringGUIFactory {
	static Logger log;
	private static ApplicationContext   context;
	static {
		try {
			log = Logger.getLogger(SpringGUIFactory.class);
			doSpring();
		} catch (Exception ee) {
		}
	}
	public SpringGUIFactory(){
		
	}
	
	
	/**
	 * @return
	 */
	private static void doSpring() {
		
	
			try {
			context = 
				new ClassPathXmlApplicationContext("springconfig_gui.xml");
				
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
				
			}
	
		
	}
	
	public static synchronized TransferHandler getTransferHandler(){

		try{
			return (TransferHandler)context.getBean("transferHandler");
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return null;
	
	}
	public static synchronized TreeExpansionListener  getTreeExpansionListener(){
	
		try{
			return (TreeExpansionListener)context.getBean("tree_expansionlistener");
		}
		catch(Exception ee){
			ee.printStackTrace();
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return null;
	}
	public static project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface getFailureObject(){		
	
		try{
			return (EFGDatasourceObjectStateInterface)context.getBean("failure_stateobject");
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return null;
	
	}
	/**
	 * 
	 * @return
	 */
	public static project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface getSuccessObject(){
	
		try{
			return (EFGDatasourceObjectStateInterface)context.getBean("success_stateObject");
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return null;
	
	}
	/**
	 * 
	 * @return
	 */
	public static project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface getNeutralObject(){
	
		try{
			return (EFGDatasourceObjectStateInterface)context.getBean("neutral_stateObject");
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

}
