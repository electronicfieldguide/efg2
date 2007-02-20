/**
 * 
 */
package project.efg.servlets.efgServletsUtil;

import java.io.File;

import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;



/**
 * @author kasiedu
 *
 */
public class XSLTObjectHelper {
	
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
			 //log.debug("Full Path to xslFile: " +fileLocationBuffer.toString());
			 File file = new File(fileLocationBuffer.toString());
			return file.exists();
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
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
