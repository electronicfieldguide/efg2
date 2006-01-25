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

package project.efg.util;

import project.efg.servlet.*; 
import project.efg.efgInterface.*; 
import project.efg.digir.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.beans.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
//import project.efg.templates.searchPageConfig.*;
/** 
 * A class for static servlet utilities.
 */
public class EFGServletUtils 
{ 
    private static ServletContext servletContext;    
    private static EFGServletInitializer sit;
    private static String metadataFileName;
    private static String pathToResourceFiles;
    private static Hashtable configMap;
    private static String path;
    private static String pathToServlet;
    private static Set set;
    public static Set configuredDatasources = Collections.synchronizedSet(new HashSet(20));
    private static Map keyWordsMap;
    private static String localDigirSchemaLocation;
    public static Hashtable cacheTemplateInfo = new Hashtable();
    private String mutex = "";
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(EFGServletUtils.class); 
	}
	catch(Exception ee){
	}
    }

    /**
     * Writes lines of HTML to a PrintWriter.
     *
     * @param lines the actual HTML (excluding <HTML><BODY> and 
     *   </BODY></HTML>) to write
     * @param pw where to write the HTML
     */
    public static void presentHTML(List lines, PrintWriter pw) 
    {
	pw.println("<HTML>");
	pw.println("  <BODY BGCOLOR=#ffffff>");
	for (int i = 0; i < lines.size(); i++)
	    pw.println((String)lines.get(i));
	pw.println("  </BODY>");
	pw.println("</HTML>");
	pw.flush();
	pw.close();
    }

    /**	
     * This method takes a String and returns an encoded version
     * of the String that can be used as a Java variable name.
     *
     * @param origString the pre-encoded string
     * @return the encoded string to be used as a java variable
     */
    public static String encodeToJavaName(String origString) 
    {
	return Introspector.
	    decapitalize(encodeToJavaClassName(origString));
    }

    /**
     * This method takes a String and returns an encoded 
     * version of the String that can be used as a Java class name.
     *
     * @param origString the pre-encoded string
     * @return the encoded string to be used as a java class name
     */
    private static String encodeToJavaClassName(String origString) 
    {
	StringBuffer sb = new StringBuffer(origString);
	int strLength = sb.length();
	
	if (strLength > 0 && 
	    !Character.isJavaIdentifierStart(sb.charAt(0)))
	    sb.setCharAt(0, '_');
	
	for (int i = 1; i < strLength; i++)
	    if (!Character.isJavaIdentifierPart(sb.charAt(i)))
		sb.setCharAt(i, '_');
	
	return sb.toString();
    }

    /**
     * Write the given message to the Servlet Response Stream.
     *
     * @param message the error message to display
     * @param res the servlet response stream object
     */
    public static void presentError(String servletName, String message, 
				    HttpServletResponse res)
	throws IOException {
	List lines = new ArrayList(2);
	lines.add("<H2>Server-Side Error</H2>");
	lines.add(servletName+": "+message);
	
	res.setContentType("text/html");
	presentHTML(lines, new PrintWriter(res.getOutputStream()));
    }

    /**
     * Set the EFGServletInitializer to initialize the backend database.
     *
     * @param sctx the ServletContext object
     */
    public static void setEFGServletInitializer(ServletContext sctx)
    {
	if (sit == null) {
	    servletContext = sctx;
	    sit = new EFGServletInitializerInstance(sctx).getInstance();
	    addConfiguredDatasources();
	}
    }
    public static String getImagesMaxDim(){
	return EFGContextListener.getImagesMaxDim(servletContext);
    }
    public static String getPathToServlet(){
	return servletContext.getRealPath("/");
    }
    private static void addConfiguredDatasources(){
	/*	try{
	    String templateFile = servletContext.getRealPath("/templateConFigFiles") + 
		File.separator + 
		EFGImportConstants.TEMPLATES_CONFIG_SRC;
	    FileReader reader = new FileReader(new File(templateFile));
	    project.efg.templates.searchPageConfig.EFGSearchPageConfig espc = 
		(EFGSearchPageConfig)EFGSearchPageConfig.unmarshalEFGSearchPageConfig(reader);
	    if(espc != null){
		int dsCount = espc.getDatasourceCount();
		for(int i = 0; i < dsCount; i++){
		    project.efg.templates.searchPageConfig.Datasource datasource = espc.getDatasource(i);
		    String name = datasource.getName().trim(); 
		    configuredDatasources.add(name.toLowerCase());
		}
	    }
	}
	catch(Exception ee){
	    if(log != null){
		LoggerUtils.logErrors(ee);
	    }
	    else{
		LoggerUtilsServlet.logErrors(ee);
	    }
	    }*/
    }
    /**
     * Get the EFGServletInitializer to initialize the backend database.
     *
     * @return the EFGServletInitializer object
     */
    public static EFGServletInitializer getEFGServletInitializer()throws Exception
    {
	if(sit != null)
	    return sit;
	else
	    throw new Exception("EFGServletInitializer not set..");
    }

    public static boolean createConfigFileMap()
    {
	String fileName ="config.properties";
	if (path == null){
	    path = servletContext.getRealPath("/WEB-INF/classes/") + System.getProperty("file.separator");
	}
	
	String fullPath = path + fileName;
	if (configMap != null) {
	    Long lg = (Long)(configMap.get(fileName));
	    Long now = new Long((new File(fullPath)).lastModified()); 
	    if (now.compareTo(lg) > 0) {
		configMap.clear();
		configMap.put(fileName, new Long(System.currentTimeMillis()));
		readConfigFile(fileName);
	    }
	}
	else {
	    configMap = new Hashtable();
	    Long time = new Long(System.currentTimeMillis());
	    configMap.put(fileName, time);
	    readConfigFile(fileName);
	}
	return true;
    }

    public static String getQueryHandlerName()
    {
	if (configMap == null)
	    createConfigFileMap();
	return (String)configMap.get("className");
    }

    public static void readConfigFile(String fileName)
    {
	//get the path to properties file
	if (path == null)
	    path = servletContext.getRealPath("/WEB-INF/classes/") + System.getProperty("file.separator");
	
	String fullPath = path + fileName;
	
	try {
	    //Read the file
	    BufferedReader propFile = new BufferedReader(new FileReader(fullPath));
	    String word = null;
	    while ((word = propFile.readLine()) != null) {
		if (!word.startsWith("#")) {//Strings that starts with the "#" sign are comments
		    String[] tokens = word.split("=");
		    String temp1 = null;
		    String temp2= null;
		    if (tokens.length == 2) {
			temp1 = tokens[0];
			temp2= tokens[1];
			int index = temp2.indexOf("#"); 
			if (index != -1) {
			    temp2 = tokens[1].substring(0,index-1);
			}
			configMap.put(temp1,temp2);
		    }
		    else{
			log.debug("property file content incorrect");
		    }
		}
	    }//end while
	} catch(Exception e) {
	    if(log != null){
		LoggerUtils.logErrors(e);
	    }
	    else{
		LoggerUtilsServlet.logErrors(e);
	    }
	}
    }

    /**
     * Sets the metadata file name for this application. This file contains info about the EFG application 
     * It is set at startup time
     * @param fileName the fullPath to the metadata file
     */
    public static void setEFGMetaDataFileName(String fileName)
    {
        if (metadataFileName == null)
	    metadataFileName = fileName;
    }

    /**
     * Returns the fullPath to the metadata file
     *
     * @return String the full path to the metadatafile
     */
    public static String getEFGMetaDataFileName()throws Exception 
    {
	if (metadataFileName != null)
	    return metadataFileName;
	else
	    throw new Exception("Metadata File Name is not set..");
    }

    /**
     * Sets the path to  the metadata files for each DataSource.
     *
     * @param path the full path to the directory in which the metadata files for all the DataSources in this application are held  
     */
    public static void setPathToResourceFiles(String path)
    {
	if (pathToResourceFiles == null)
	    pathToResourceFiles = path;
    }

    /**
     * Retrieves the full path to the directory holding all the metadata files
     *
     * @return the full path to the directory holding all metadata files
     */
    public static String getPathToResourceFiles()throws Exception 
    {
	if (pathToResourceFiles != null)
	    return pathToResourceFiles;
	else
	    throw new Exception("pathToResourceFiles is not set..");
    }

    /**
     * Populate the set
     */
    public static void fillSet(String[] fedSchemaNames, String schemaLocation)
    {
	if (set != null) {
	    return;
	}
	FederationParser fdp = new FederationParser();
	for(int i = 0 ; i < fedSchemaNames.length;i++) {
	    fdp.startParsing(schemaLocation + File.separator + fedSchemaNames[i]);
	}
	set = fdp.getSet();
    }
	
    /**
     * Return the set populated with the keywords
     */
    public static Set getSet()
    {
	return set;
    }

    /**
     * Returns true if this key is in the Set
     */
    public static boolean contains(String word)
    {
	if (set == null){
	    return false;
	}
	return set.contains(word);
    }
    public static synchronized void addToSet(String name){
	set.add(name);
    }
    /**
     * Add the key and value to the map
     */
    public static void addToKeyWordsMap(String key,String value)
    {
	if (keyWordsMap == null) {
	    keyWordsMap = Collections.synchronizedMap(new HashMap());
	}
	keyWordsMap.put(key,value);
    }

    /**
     * Returns true if key is a keyword
     */
    public static boolean containsKeyWord(String key)
    {
	if (keyWordsMap == null)
	    return false;
	return keyWordsMap.containsKey(key);
    }

    /**
     * Returns the value associated with this key
     */
    public static Object getKeyWordValue(String key)
    {
	if (keyWordsMap == null)
	    return null;
	return keyWordsMap.get(key);
    }
}

//$Log$
//Revision 1.1  2006/01/25 21:03:48  kasiedu
//Initial revision
//
//Revision 1.4  2005/04/27 19:41:22  ram
//Recommit all of ram's allegedly working copy of efgNEW...
//
//Revision 1.1.1.1  2003/10/17 17:03:09  kimmylin
//no message
//
//Revision 1.4  2003/08/20 18:45:42  kimmylin
//no message
//

