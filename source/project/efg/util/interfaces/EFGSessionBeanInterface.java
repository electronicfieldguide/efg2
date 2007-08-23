/**
 * 
 */
package project.efg.util.interfaces;

import java.util.Map;

/**
 * @author jacob.asiedu
 *
 */
public interface EFGSessionBeanInterface {

	public abstract long getSessionBeanID();

	public abstract void setAttribute(Object key, Object value);

	public abstract Object getAttribute(Object key);

	public abstract Map getAttributes();

	public abstract java.util.Set getAttributeKeys();

}