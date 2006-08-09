/**
 * 
 */
package project.efg.util;



import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.base.EntryRefreshPolicy;

/**
 * @author kasiedu
 *
 */
public class EFGSearchPageEntryRefreshPolicy implements EntryRefreshPolicy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public EFGSearchPageEntryRefreshPolicy() {
		super();
		
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.oscache.base.EntryRefreshPolicy#needsRefresh(com.opensymphony.oscache.base.CacheEntry)
	 */
	public boolean needsRefresh(CacheEntry arg0) {
		//log.debug("");
		return false;
	}

}
