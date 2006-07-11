/**
 * 
 */
package project.efg.servlets.factory;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @author kasiedu
 *
 */
public class EFGDiskItemFileFactory {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGDiskItemFileFactory.class);
		} catch (Exception ee) {
		}
	}
 public static File getInstance(String directory,String itemType){
	 String mutex="";
	 synchronized (mutex) {
		 File file = null;
		 
		 if((itemType != null ) && (!itemType.trim().equals(""))){
			 log.debug("It is a js");
			 file = new File(directory + File.separator + 
					 itemType);
		 }
		
		 log.debug("ItemType: " + itemType);
		 if(file == null){
			 file = new File(directory);
		 }
		return file;
	}
	
 }
}
