/**
 * 
 */
package project.efg.server.utils;

import java.util.HashMap;
import java.util.Map;

import project.efg.server.servlets.EFGContextListener;

/**
 * @author jacob.asiedu
 * 
 */
public class ServletCacheManager {
	private static Map map;
	public static Map getDatasources(String whichDatabase) {
		String mutex = "";
		synchronized (mutex) {
			map = EFGContextListener
					.populateMapDatasources(whichDatabase.toLowerCase());
			if (map == null) {
				return new HashMap();
			}
			return map;
		}

	}
	public static Map getDatasourceCache(String database){
		return getDatasources(database);
	}
}
