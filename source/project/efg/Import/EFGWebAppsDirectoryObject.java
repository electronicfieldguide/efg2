package project.efg.Import;

/**
 * EFGWebAppsDirectoryObject.java
 *
 *
 * Created: Thu Feb 23 14:45:53 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.*;
public class EFGWebAppsDirectoryObject {
  
    private String pathToServer;
    private String imagesDirectory;
    private String cssDirectory;
    private String catalina_home;
     static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(ImportMenu.class); 
	}
	catch(Exception ee){
	}
    }

    public EFGWebAppsDirectoryObject(String pathToServer) {
	this.pathToServer = pathToServer;
    } // EFGWebAppsDirectoryObject constructor
 
    /**
     * @param imagesDirectory - relative path to the images directory from the server
     */
    public void setImagesDirectory(String imagesDirectory){
	if(imagesDirectory == null){
	    return;
	}
	if((this.getPathToServer() != null)&& (!"".equals(this.getPathToServer().trim()))){
	    this.imagesDirectory = this.getPathToServer() + File.separator + imagesDirectory.trim();
	}
	else{
	    String catalina_home = project.efg.util.EFGUtils.getCatalinaHome();
	    if((catalina_home != null)&& (!"".equals(catalina_home.trim()))){
		this.setPathToServer(
				     catalina_home + 
				     File.separator + 
				     project.efg.util.EFGImportConstants.EFG_WEB_APPS +
				     File.separator+
				     project.efg.util.EFGImportConstants.EFG_APPS
				     );
		this.setImagesDirectory(imagesDirectory.trim());
	    }
	}
    }
    /**
     *@param cssDirectory - relative path of the css directory from the server
     */
    public void setCSSDirectory(String cssDirectory){
	if(cssDirectory == null){
	    return;
	}
	if((this.getPathToServer() != null)&& (!"".equals(this.getPathToServer().trim()))){
	    this.cssDirectory = this.getPathToServer() + File.separator + cssDirectory;
	}
	else{
	    String catalina_home =project.efg.util.EFGUtils.getCatalinaHome();
	    if((catalina_home != null)&& (!"".equals(catalina_home.trim()))){
		this.setPathToServer(
				     catalina_home + 
				     File.separator+ 
				     project.efg.util.EFGImportConstants.EFG_WEB_APPS +
				     File.separator + 
				     project.efg.util.EFGImportConstants.EFG_APPS
				     );
		this.setCSSDirectory(cssDirectory.trim());
	    }
	}
    }
    /**
     *@return the full path to the images directory
     */
    public String getImagesDirectory(){
	return this.imagesDirectory;
    }
    /**
     *@return the full path to the css directory
     */
    public String getCSSDirectory(){
	return this.cssDirectory;
    }
    /**
     *@param pathToServer - The full path to server. If this is set after calls
     * to setCSSDirectory(String) or setImagesDirectory(String) then call 
     *those methods again
     */
    public void setPathToServer(String pathDirectory){
	this.pathToServer = pathDirectory;
    }
    /**
     *@return the full path to the server
     */
    public String getPathToServer(){
	return this.pathToServer;
    }
} // EFGWebAppsDirectoryObject
