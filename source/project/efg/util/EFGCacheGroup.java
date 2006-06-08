/**
 * 
 */
package project.efg.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.web.ServletCacheAdministrator;
import com.opensymphony.oscache.web.filter.ICacheGroupsProvider;

/**
 * @author kasiedu
 *
 */
public class EFGCacheGroup implements ICacheGroupsProvider {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGCacheGroup.class);
		} catch (Exception ee) {
		}
	}
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
		 
		String[] groups = {EFGImportConstants.XSL_SEARCH_GROUPS,
				EFGImportConstants.SEARCH_GROUP};
		log.debug("groups created with defaults: '" + 
				EFGImportConstants.XSL_SEARCH_GROUPS + 
				"' and '" + 
				EFGImportConstants.SEARCH_GROUP + 
				"'"
				);
		return groups;
	}

}
