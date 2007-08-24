/**
 * 
 */
package project.efg.server.servlets;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

/**
 * @author jacob.asiedu
 *
 */
public class EFGSpringContextLoader extends ContextLoaderListener {
	/**
	 * Create the ContextLoader to use. Can be overridden in subclasses.
	 * @return the new ContextLoader
	 */
	protected ContextLoader createContextLoader() {
		//initialize properties here?
		return new ContextLoader();
	}
}
