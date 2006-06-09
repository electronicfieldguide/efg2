/**
 * 
 */
package project.efg.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.opensymphony.oscache.base.Cache;
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
		String xslName = req.getParameter(EFGImportConstants.XSL_STRING);
		if(xslName == null){
			xslName ="";
		}
		String displayName = req.getParameter(EFGImportConstants.DISPLAY_NAME);
		if(displayName == null){
			displayName ="";
		}
		String displayFormat=req.getParameter(EFGImportConstants.DISPLAY_FORMAT);
		if(displayFormat== null){
			displayFormat="";
		}
		String datasourceName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
		if(datasourceName== null){
			datasourceName="";
		}
		String uniqueID = req.getParameter(EFGImportConstants.UNIQUEID_STR);
		if(uniqueID== null){
			uniqueID="";
		}
		StringBuffer key = new StringBuffer();
		key.append(displayName.trim());
		key.append("_");
		key.append(displayFormat.trim());
		key.append("_");
		key.append(xslName.trim());
		key.append("_");
		key.append(datasourceName.trim());
		key.append("_");
		key.append(uniqueID.trim());
		
		log.debug("Generated key : " + key.toString());
		return key.toString();
	}

}
