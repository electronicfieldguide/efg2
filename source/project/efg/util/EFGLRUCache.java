/**
 * 
 */
package project.efg.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.opensymphony.oscache.base.algorithm.LRUCache;

/**
 * @author kasiedu
 *
 */
public class EFGLRUCache extends LRUCache{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
    private Collection fifoList = Collections.synchronizedSet(new LinkedHashSet());
    
	/**
	 * 
	 */
	public EFGLRUCache() {
		super();
	}

	  /**
     * Constructors a LRU Cache of the specified capacity.
     *
     * @param capacity The maximum cache capacity.
     */
	public EFGLRUCache(int capacity) {
		super(capacity);
	}
	/**
     * An item was retrieved from the list. The LRU implementation moves
     * the retrieved item's key to the front of the list.
     *
     * @param key The cache key of the item that was retrieved.
     */
    protected void itemRetrieved(Object key) {
    	 String mutex ="";
         synchronized (mutex) {
        	 String str = (String)key; 
        	 if(str.endsWith("_ID_EFG_UNIQUE")){
        		 this.fifoRetrieved(key);
        	 }
        	 else{
        		 super.itemRetrieved(key);
        	 }
         }
    }

    /**
     * An object was put in the cache. This implementation adds/moves the
     * key to the end of the list.
     *
     * @param key The cache key of the item that was put.
     */
    protected void itemPut(Object key) {
    	 String mutex ="";
         synchronized (mutex) {
        	 String str = (String)key; 
        	 if(str.endsWith("_ID_EFG_UNIQUE")){
        		 this.fifoPut(key);
        	 }
        	 else{
        		 super.itemPut(key);
        	 }
         }
    }

    /**
     * An item needs to be removed from the cache. The LRU implementation
     * removes the first element in the list (ie, the item that was least-recently
     * accessed).
     *
     * @return The key of whichever item was removed.
     */
    protected Object removeItem() {
    	if(this.fifoList.isEmpty()){
    		return super.removeItem();
    	}
    	return this.fifoRemoveItem();
    }
    /**
     * Remove specified key since that object has been removed from the cache.
     *
     * @param key The cache key of the item that was removed.
     */
    protected void itemRemoved(Object key) {
    	 String mutex ="";
         synchronized (mutex) {
        	 String str = (String)key; 
        	 if(str.endsWith("_ID_EFG_UNIQUE")){
        		 this.fifoItemRemoved(key);
        		return; 
        	 }
        	 super.itemRemoved(key);
         }
    }
  
    /**
     * An object was retrieved from the cache. This implementation
     * does noting since this event has no impact on the FIFO algorithm.
     *
     * @param key The cache key of the item that was retrieved.
     */
    protected void fifoRetrieved(Object key) {
    }

    /**
     * An object was put in the cache. This implementation just adds
     * the key to the end of the list if it doesn't exist in the list
     * already.
     *
     * @param key The cache key of the item that was put.
     */
    protected void fifoPut(Object key) {
        if (!fifoList.contains(key)) {
            fifoList.add(key);
        }
    }

    /**
     * An item needs to be removed from the cache. The FIFO implementation
     * removes the first element in the list (ie, the item that has been in
     * the cache for the longest time).
     *
     * @return The key of whichever item was removed.
     */
    protected Object fifoRemoveItem() {
        Iterator it = fifoList.iterator();
        Object toRemove = it.next();
        it.remove();

        return toRemove;
    }

    /**
     * Remove specified key since that object has been removed from the cache.
     *
     * @param key The cache key of the item that was removed.
     */
    protected void fifoItemRemoved(Object key) {
        fifoList.remove(key);
    }
}
