/**
 * 
 */
package project.efg.server.utils;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import project.efg.server.servlets.EFGContextListener;

/**
 * @author jacob.asiedu
 *
 */
public class ServletCacheManager {
	private static GeneralCacheAdministrator cacheAdmin;
	
	static{
		cacheAdmin = EFGContextListener.getCacheAdminMap();
	}
	
	public static Map getDatasourceCache(String whichDatabase){
		String mutex = "";
		synchronized (mutex) {
			Map map = null;
			try {
				map = (Map)cacheAdmin.getFromCache(whichDatabase.toLowerCase());
				
			} catch (NeedsRefreshException nre) {
			    try {
					 	map = EFGContextListener.populateCacheWithDatasources(whichDatabase.toLowerCase());
				 	if(map != null && map.size() > 0){
				 		cacheAdmin.putInCache(whichDatabase.toLowerCase(),map);
				 	}
			    } catch (Exception ex) {
			    	 map =(Map)nre.getCacheContent();	
			    	 cacheAdmin.cancelUpdate(whichDatabase.toLowerCase());
			    }
			}
			if(map == null){
				return new HashMap();
			}
			return map;
		}
		
	}
}
