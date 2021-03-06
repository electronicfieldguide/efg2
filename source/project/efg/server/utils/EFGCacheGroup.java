/**
 * 
 */
package project.efg.server.utils;

import javax.servlet.http.HttpServletRequest;

import project.efg.util.interfaces.EFGImportConstants;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.web.ServletCacheAdministrator;
import com.opensymphony.oscache.web.filter.ICacheGroupsProvider;

/**
 * @author kasiedu
 *
 */
public class EFGCacheGroup implements ICacheGroupsProvider {
	
	/**
	 * 
	 */
	public EFGCacheGroup() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.oscache.web.filter.ICacheGroupsProvider#createCacheGroups(javax.servlet.http.HttpServletRequest, com.opensymphony.oscache.web.ServletCacheAdministrator, com.opensymphony.oscache.base.Cache)
	 */
	public String[] createCacheGroups(HttpServletRequest req,
			ServletCacheAdministrator admin, Cache cache) {
		return getGroup(req);
	}
	private String[] getGroup(HttpServletRequest req){
		String uniqueID = req.getParameter(EFGImportConstants.UNIQUEID_STR);
		if((uniqueID == null) || (uniqueID.trim().equals(""))){//use the default cache
			return null;
		}//return null which is the default in the Filter
		return new String[]{"uniqueIDs"};//return this group. If cache fills up remove uniqueID's first from  the cache
	}

}
