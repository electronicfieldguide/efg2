/**
 * 
 */
package project.efg.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author kasiedu
 *
 */
public class XMLFileNameFilter implements FilenameFilter {

	/**
	 * 
	 */
	public XMLFileNameFilter() {
		super();
	
	}
	public boolean accept(File dir, String name) {
		if(name == null){
			return false;
		}
		name = name.toLowerCase();
		return name.endsWith(EFGImportConstants.XML_EXT);
	}
}
