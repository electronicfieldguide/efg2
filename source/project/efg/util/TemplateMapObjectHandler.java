/**
 * 
 */
package project.efg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImportsUtil.EFGUtils;

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
	private static Map templateObjectMap;

	public static void createTemplateObjectMap(String mapLocation) {
		String mutex = "";
		synchronized (mutex) {
			String message="";
			if (templateObjectMap == null) {
				message="Getting TemplateObject Map";
				EFGUtils.log(message);
				log.debug(message);
				templateObjectMap = initTemplateObjectMap(mapLocation);
			}
			message = "TemplateMapObject size : " + templateObjectMap.size();
			EFGUtils.log(message);
			log.debug(message);
			
		}
	}
	public static void add2TemplateMap(String key, TemplateObject templateObject){
		if(templateObject == null){
			log.error("Cannot add null objects to Template Map");
			return;
		}
		if(templateObjectMap != null){
			String message ="About to add key : " + key + " object: " + templateObject.toString(); 
			log.debug(message);
			EFGUtils.log(message);
			templateObjectMap.put(key,templateObject);
			message ="Added key : " + key + " object: " + templateObject.toString(); 
			log.debug(message);
			EFGUtils.log(message);
		}
		else{
			log.error(" call setTemplateObject() before you call this method!! ");
			
		}
	}
	public static void removeFromTemplateMap(String key){
	
		if(templateObjectMap != null){			
			templateObjectMap.remove(key);
		}
		else{
			log.error(" call setTemplateObject() before you call this method!! ");
			
		}
	}
	//unmodifiable map
	public static Map getTemplateObjectMap(){
		return Collections.unmodifiableMap(templateObjectMap);
	}
	public static void writeTemplateObject(String mapLocation) {
		String mutex = "";
		synchronized (mutex) {
			String message="";
			try {
				if (templateObjectMap == null) {
					message = "Template Object Map is null and will not be serialized!!";
					EFGUtils.log(message);
					log.debug(message);
					return;
				}
				message = "About to write Template Object to: "+ mapLocation;
				EFGUtils.log(message);
				log.debug(message);
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(mapLocation));
				out.writeObject(templateObjectMap);
				message = "Template Object written to : "
					+ mapLocation;
				EFGUtils.log(message);
				log.debug(message);
				out.close();

			} catch (Exception ee) {
				message = "An error occured during serialization.";
				EFGUtils.log(message);
				log.debug(message);
				EFGUtils.log(ee.getMessage());
				log.error(ee.getMessage());
				ee.printStackTrace();
			}

		}
	}

	/**
	 * 
	 */
	private static Map initTemplateObjectMap(String mapLocation) {
		// find out if it exists load it if it does
		String message="";
		try {
			File mapFile = new File(mapLocation);
			if (!mapFile.exists()) {
				message ="Creating a new Map";
				EFGUtils.log(message);
				log.debug(message);
				return Collections.synchronizedMap(new HashMap());
			} else {
				message ="Deserialize the Map";
				EFGUtils.log(message);
				log.debug(message);
				return deSerializeTemplateMapObject(mapLocation);
			}
		} catch (Exception ee) {
			EFGUtils.log(ee.getMessage());
			log.error(ee.getMessage());
			ee.printStackTrace();
		}
		return null;
	}

	private static Map deSerializeTemplateMapObject(String mapLocation) {
		String mutex = "";
		synchronized (mutex) {
			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(mapLocation));
				Map map = (Map) in.readObject();
				in.close();
				return map;
			} catch (Exception ee) {
				String message = "An error occured during deserialization";
				EFGUtils.log(message);
				log.debug(message);
				EFGUtils.log(ee.getMessage());
				log.error(ee.getMessage());
				message = "Creating a new map with an 'imaginary' location";
				EFGUtils.log(message);
				log.debug(message);
				return initTemplateObjectMap("xxx_ZZZZ");
			}

		}
	}
}
