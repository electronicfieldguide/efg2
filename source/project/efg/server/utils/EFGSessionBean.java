/**
 * 
 */
package project.efg.server.utils;

import project.efg.util.interfaces.EFGSessionBeanInterface;
import project.efg.util.utils.EFGUniqueID;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jacob.asiedu
 *
 */
public class EFGSessionBean implements EFGSessionBeanInterface {
	private long id;
	private Map map;
	
	public EFGSessionBean(){
		this.id = EFGUniqueID.getID();
		this.map = new HashMap();
	}
	/* (non-Javadoc)
	 * @see project.efg.server.utils.EFGSessionBeanInterface#getSessionBeanID()
	 */
	public long getSessionBeanID(){
		return this.id;
	}
	/* (non-Javadoc)
	 * @see project.efg.server.utils.EFGSessionBeanInterface#setAttribute(java.lang.Object, java.lang.Object)
	 */
	public void setAttribute(Object key, Object value){
		if((key != null) && (value != null)){
			map.put(key, value);
		}
	}
	/* (non-Javadoc)
	 * @see project.efg.server.utils.EFGSessionBeanInterface#getAttribute(java.lang.Object)
	 */
	public Object getAttribute(Object key){
		return map.get(key);
	}
	/* (non-Javadoc)
	 * @see project.efg.server.utils.EFGSessionBeanInterface#getAttributes()
	 */
	public Map getAttributes(){
		return Collections.unmodifiableMap(this.map);
	}
	/* (non-Javadoc)
	 * @see project.efg.server.utils.EFGSessionBeanInterface#getAttributeKeys()
	 */
	public java.util.Set getAttributeKeys(){
		return  Collections.unmodifiableSet(map.keySet());
	}
}
