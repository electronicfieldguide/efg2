/**
 * 
 */
package project.efg.servlets.efgServletsUtil;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @author kasiedu
 *
 */
public class XSLTObjectHelper {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(XSLTObjectHelper.class);
		} catch (Exception ee) {
		}
	}
	public XSLTObjectHelper(){}
	/**
	 * 
	 * @param dsName
	 * @param realPath
	 * @param searchType
	 * @return
	 */
	public boolean isXSLFileExists( 
			  String realPath, 
			  String xslFileName){
		try {
			 StringBuffer fileLocationBuffer = new StringBuffer();
			 fileLocationBuffer.append(constructXSLFileName(realPath,xslFileName)); 
			 log.debug("Full Path to xslFile: " +fileLocationBuffer.toString());
			 File file = new File(fileLocationBuffer.toString());
			return file.exists();
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
		return false;
	}
	/**
	 * 
	 * @param dsName
	 * @param realPath
	 * @return
	 */
	  private String constructXSLFileName( 
			  String realPath, String xslFileName){
		StringBuffer fileLocationBuffer = new StringBuffer();
		fileLocationBuffer.append(realPath);
		fileLocationBuffer.append(xslFileName);
		return fileLocationBuffer.toString();
	}
}
