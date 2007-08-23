/**
 * 
 */
package project.efg.util.utils;

import java.io.File;
import java.io.FilenameFilter;

import project.efg.util.interfaces.EFGImportConstants;


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
