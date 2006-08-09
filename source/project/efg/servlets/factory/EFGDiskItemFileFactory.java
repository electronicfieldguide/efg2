/**
 * 
 */
package project.efg.servlets.factory;

import java.io.File;



/**
 * @author kasiedu
 *
 */
public class EFGDiskItemFileFactory {
	
 public static File getInstance(String directory,String itemType){
	 String mutex="";
	 synchronized (mutex) {
		 File file = null;
		 
		 if((itemType != null ) && (!itemType.trim().equals(""))){
			 //log.debug("It is a js");
			 file = new File(directory + File.separator + 
					 itemType);
		 }
		
		 //log.debug("ItemType: " + itemType);
		 if(file == null){
			 file = new File(directory);
		 }
		return file;
	}
	
 }
}
