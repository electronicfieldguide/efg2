/**
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu, Kimmy Lin
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2, or
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 */

package project.efg.servlet;

import project.efg.util.*;
import project.efg.digir.*;
import project.efg.efgInterface.*;
import javax.servlet.*;
import java.util.Properties;
import java.io.*;


/** 
 * Method contextInitialized() is called whenever the efg web application is starting up.
 * First the servlet context is retrieved from the servlet context event:
 *      ServletContext servletContext = servletContextEvent.getServletContext();
 * Then the initialization parameters are read from web.xml.
 * The XSLProperties file is read and its contents are added to the System properties.
 * Method contextDestroyed() is called whenever the efg web application is shutting down.
 */

public class EFGContextListener implements ServletContextListener, EFGConstants 
{
    private EFGServletInitializer sit;
    private ServletContext servletContext;

    /**
     * Set the servlet context's parameters in EFGServletUtils. 
     * Called whenever the efg web application is starting up.
     *
     * @param servletContextEvent the ServletContextEvent object 
     */
    public void contextInitialized (ServletContextEvent servletContextEvent)
    {
	EFGUtils.log("Initializing Listener");
	servletContext = servletContextEvent.getServletContext();
	java.util.Enumeration enum = servletContext.getInitParameterNames();
	while(enum.hasMoreElements()) {
	    String paramName = (String)enum.nextElement();
	    this.setInitialAttribute(servletContext,paramName);
	}
       
	//DiGIR initialization stuff
	String schemaNames = servletContext.getInitParameter("schemaNames");
	String efgMetaDataFileName = servletContext.getInitParameter("efgMetadataFileName");

	String pathToSchema = servletContext.getRealPath("/WEB-INF/fedSchema/");
	String pathToMetaDataFiles = servletContext.getRealPath("/WEB-INF/metadata/");
	String efgMetaDataFile = pathToMetaDataFiles + File.separator+ efgMetaDataFileName;
	
	EFGServletUtils.setEFGMetaDataFileName(efgMetaDataFile);
	EFGServletUtils.setPathToResourceFiles(pathToMetaDataFiles);
	parseAndFillSet(schemaNames,pathToSchema);
	createKeyWordMap();

	EFGUtils.log("Context parameters added");
	//Set an EFGServletInitializer instance which initializes the Database etc. 
	//We want to make sure it is a singleton
	EFGServletUtils.setEFGServletInitializer(servletContext);
	EFGServletUtils.createConfigFileMap();
	clean();
    }
 // Looks for a servlet context init parameter with a
    // given name. If it finds it, it puts the value into
    // a servlet context attribute with the same name. If
    // the init parameter is missing, it puts a default
    // value into the servlet context attribute.
    /**
     *Taken from More Servlets and JavaServer Pages
     *  from Prentice Hall and Sun Microsystems Press,
     *  http://www.moreservlets.com/.
     *  &copy; 2002 Marty Hall; may be freely used or adapted.
     */
    private void setInitialAttribute(ServletContext context,
				     String initParamName) {
	String initialValue =
	    context.getInitParameter(initParamName);
	if (initialValue != null) {
	    context.setAttribute(initParamName, initialValue);
	}
    }
    /**
     *Static method that returns the servlet context
     *  attribute named "EFG_IMAGES_THUMBNAIL_DIR" if it is available.
     *  Returns an empty string if the attribute is
     *  unavailable.
     *Taken from More Servlets and JavaServer Pages
     *  from Prentice Hall and Sun Microsystems Press,
     *  http://www.moreservlets.com/.
     *  &copy; 2002 Marty Hall; may be freely used or adapted.
     */
    public static String getImagesMaxDim(ServletContext context){
	String name =
	    (String)context.getAttribute(EFGConstants.MAX_DIM_NAME);
	if (name == null) {
	    name = "";
	}
	return(name);
    }
    /**
     *Parse a schema and populate a Set with the members of the schema
     *@param schemaNames - The name of the schema to be parsed
     *@param path - The path to the schema file
     */
    public void parseAndFillSet(String schemaNames,String path){
	String []arr = schemaNames.split("\\s");
	EFGServletUtils.fillSet(arr,path);
    }
    /**
     *Create a list of keywords from the keywords properties file
     */
    public void createKeyWordMap(){
	//get the path to properties file
	String fullPath = servletContext.getRealPath("/WEB-INF/classes/" + File.separator+ EFGConstants.EFG_KEYWORDS_PROPERTIES);
	try{
	    //Read the file
	    BufferedReader propFile = new BufferedReader(new FileReader(fullPath));
	    String word = null;
	    while((word = propFile.readLine()) != null){
		
		if(!word.startsWith("#")){//Strings that starts with the "#" sign are comments
		    String[] tokens = word.split("\\s");
		    
		    if(tokens.length >= 2){
			StringBuffer buf = new StringBuffer();
			int i = 1;
			while(i < tokens.length){
			    String temp = tokens[i];
			    
			    int index = temp.indexOf("#"); 
			    if( index != -1){
				buf.append(temp.substring(0,index-1));
				break;
			    }
			    else {
				if(!"".equals(temp.trim())){
				    buf.append(temp + " ");
				}
			    }
			    i++;
			}
			if(buf.length() > 0){
			    EFGServletUtils.addToKeyWordsMap(tokens[0],new String(buf.toString()));
			}
		    }
		}
	    }
	}catch(Exception e){
	    System.err.println(e.getMessage());
	}
    }
    /**
     * This method is called when the Context is destroyed. The Database is closed, 
     * all threads that are bound to the session are removed and the Session object
     * is terminated.
     *
     * @param servletContextEvent the ServletContextEvent object
     */
    public void contextDestroyed (ServletContextEvent servletContextEvent)
    {
	try{
	    EFGUtils.log(this.getClass().getName()+", destroy() finished.");     
	    EFGUtils.log("EFG context destroyed");
	    clean();
	    
	    sit = EFGServletUtils.getEFGServletInitializer();
	    sit.contextDestroyed();
	    
	}
	catch(Exception e){
	    EFGUtils.log(e.getMessage());
	}
    }
    protected void clean() {
	String fullPath = servletContext.getRealPath("/");
	// Retrieve directory path and get full list of files
	File staleFiles = new File(fullPath);
	String thmDir = EFGImportConstants.EFGIMAGES_THUMBS;
	File[] list = staleFiles.listFiles();

	for( int f = 0; f < list.length; f++ ) {
	    if(list[f].getName().equals(thmDir)){
		this.deleteDir(list[f]);
	    }
	}
	staleFiles = new File(fullPath + File.separator + EFGImportConstants.TEMPLATES_FOLDER_NAME);
	if(!staleFiles.exists()){
	    return;
	}
	list = staleFiles.listFiles();
	
	for( int f = 0; f < list.length; f++ ) {
	    File staleFile = list[f];
	    
	    if(staleFile.getName().endsWith("_old")){
		if((staleFile.getName().indexOf(EFGImportConstants.SEARCHPAGE_LISTS_FILLER) > -1) ||
		   (staleFile.getName().indexOf(EFGImportConstants.SEARCHPAGE_PLATES_FILLER) > -1) ||
		   (staleFile.getName().indexOf(EFGImportConstants.SEARCHPAGE_FILLER) > -1) ||
		   (staleFile.getName().indexOf(EFGImportConstants.TAXONPAGE_FILLER) > -1)){
		    this.deleteDir(list[f]);
		}
	    }
	}
    }
    // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
	// The directory is now empty so delete it
        return dir.delete();
    }
    // Remove all image thumbnails on undeploy or container shutdown
    public void destroy() {
	
    }
}

//$Log$
//Revision 1.1  2006/01/25 21:03:48  kasiedu
//Initial revision
//