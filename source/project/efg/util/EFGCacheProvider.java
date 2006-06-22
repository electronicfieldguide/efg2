/**
 * 
 */
package project.efg.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.extra.ScopeEventListenerImpl;
import com.opensymphony.oscache.web.ServletCacheAdministrator;
import com.opensymphony.oscache.web.filter.ICacheKeyProvider;

/**
 * @author kasiedu
 *
 */
public class EFGCacheProvider implements ICacheKeyProvider {
	 
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGCacheProvider.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public EFGCacheProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.oscache.web.filter.ICacheKeyProvider#createCacheKey(javax.servlet.http.HttpServletRequest, com.opensymphony.oscache.web.ServletCacheAdministrator, com.opensymphony.oscache.base.Cache)
	 */
	public String createCacheKey(HttpServletRequest req,
			ServletCacheAdministrator admin, Cache cache) {
		String uniqueID = req.getParameter(EFGImportConstants.UNIQUEID_STR);
		String key = null;
		if((uniqueID == null) || (uniqueID.trim().equals(""))){//use the default cache
			key = admin.generateEntryKey(null, req,ScopeEventListenerImpl.APPLICATION_SCOPE);
		}
		else{
		    key = uniqueID.trim();
		    
		}
		log.debug("Key is : " + key);
		return key;
	}

}