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

package project.efg.servlets.efgImpl;

//import java.beans.Introspector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgImportsUtil.LoggerUtils;
import project.efg.Imports.rdb.EFGRDBImportUtils;
import project.efg.digir.FederationParser;
import project.efg.servlets.efgInterface.EFGServletInitializerInterface;
import project.efg.servlets.efgServletsUtil.EFGServletInitializerInstance;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.util.EFGImportConstants;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * Method contextInitialized() is called whenever the efg web application is
 * starting up. First the servlet context is retrieved from the servlet context
 * event: ServletContext servletContext =
 * servletContextEvent.getServletContext(); Then the initialization parameters
 * are read from web.xml. The XSLProperties file is read and its contents are
 * added to the System properties. Method contextDestroyed() is called whenever
 * the efg web application is shutting down.
 */

public class EFGContextListener implements ServletContextListener {
	private EFGServletInitializerInterface sit;

	private static ServletContext servletContext;
	
	private static String metadataFileName;

	private static String pathToResourceFiles;

	private static Hashtable configMap;

	private static String path;

	private static Set set;

	public static Set configuredDatasources = Collections
			.synchronizedSet(new HashSet(20));

	private static Map keyWordsMap= Collections.synchronizedMap(new HashMap());

	public static Hashtable cacheTemplateInfo = new Hashtable();
    private static GeneralCacheAdministrator cacheAdmin;
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGContextListener.class);
		} catch (Exception ee) {
		}
	}
	public  static GeneralCacheAdministrator getCacheAdmin(){
		return cacheAdmin;
	}
	/**
	 * Set the servlet context's parameters in EFGServletUtils. Called whenever
	 * the efg web application is starting up.
	 * 
	 * @param servletContextEvent
	 *            the ServletContextEvent object
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		EFGUtils.log("Initializing Listener");
		servletContext = servletContextEvent.getServletContext();
		this.sit = getEFGServletInitializer();
		java.util.Enumeration enum1 = servletContext.getInitParameterNames();
		while (enum1.hasMoreElements()) {
			String paramName = (String) enum1.nextElement();
			this.setInitialAttribute(servletContext, paramName);
		}
		cacheAdmin = new GeneralCacheAdministrator();
		// DiGIR initialization stuff
		String schemaNames = servletContext.getInitParameter("schemaNames");
		String efgMetaDataFileName = servletContext
				.getInitParameter("efgMetadataFileName");

		String pathToSchema = servletContext.getRealPath("/WEB-INF/fedSchema/");
		String pathToMetaDataFiles = servletContext
				.getRealPath("/WEB-INF/metadata/");
		String efgMetaDataFile = pathToMetaDataFiles + File.separator
				+ efgMetaDataFileName;
		metadataFileName = efgMetaDataFile;
		
		pathToResourceFiles = pathToMetaDataFiles;
		parseAndFillSet(schemaNames, pathToSchema);
		createKeyWordMap();

		EFGUtils.log("Context parameters added");
		// Set an EFGServletInitializer instance which initializes the Database
		// etc.
		// We want to make sure it is a singleton
	
		addConfiguredDatasources();
		createConfigFileMap();
		EFGRDBImportUtils.init();
		//http://localhost:8080/efg2/
		//clean();
		
		
	}
	 /** Static method that returns the servlet context
     *  attribute named "EFG_IMAGES_DIR" if it is available.
     *  Returns an empty string if the attribute is
     *  unavailable.
     *
     *Taken from More Servlets and JavaServer Pages
     *  from Prentice Hall and Sun Microsystems Press,
     *  http://www.moreservlets.com/.
     *  &copy; 2002 Marty Hall; may be freely used or adapted.
     */
    public static String getEFGImagesDirectory(){
	String name =
	    (String)servletContext.getAttribute(EFGImportConstants.EFG_IMAGES_DIR_NAME);
	if (name == null) {
	    name = "";
	}
	return(name);
    }
    /** Static method that returns the servlet context
     *  attribute named "EFG_IMAGES_THUMBNAIL_DIR" if it is available.
     *  Returns an empty string if the attribute is
     *  unavailable.
     *Taken from More Servlets and JavaServer Pages
     *  from Prentice Hall and Sun Microsystems Press,
     *  http://www.moreservlets.com/.
     *  &copy; 2002 Marty Hall; may be freely used or adapted.
     */
    public static String getEFGThumbnailsDiretcory(){
	String name =
	    (String)servletContext.getAttribute(EFGImportConstants.EFG_THUMB_NAILS_DIR_NAME);
	if (name == null) {
	    name = "";
	}
	return(name);
    }
	

	/**
	 * Static method that returns the servlet context attribute named
	 * "EFG_IMAGES_THUMBNAIL_DIR" if it is available. Returns an empty string if
	 * the attribute is unavailable. Taken from More Servlets and JavaServer
	 * Pages from Prentice Hall and Sun Microsystems Press,
	 * http://www.moreservlets.com/. &copy; 2002 Marty Hall; may be freely used
	 * or adapted.
	 */
	public synchronized static String getImagesMaxDim() {
		String name = (String)servletContext.getAttribute(EFGImportConstants.MAX_DIM_NAME);
		if (name == null) {
			name = "";
		}
		return (name);
	}

	
	/**
	 * This method is called when the Context is destroyed. The Database is
	 * closed, all threads that are bound to the session are removed and the
	 * Session object is terminated.
	 * 
	 * @param servletContextEvent
	 *            the ServletContextEvent object
	 */
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		try {
			EFGUtils.log(this.getClass().getName() + ", destroy() finished.");
			EFGUtils.log("EFG context destroyed");
			clean();
			sit.contextDestroyed();
			cacheAdmin.destroy();

		} catch (Exception e) {
			EFGUtils.log(e.getMessage());
		}
	}

	

	// Remove all image thumbnails on undeploy or container shutdown
	public void destroy() {

	}
	

	/**
	 * Writes lines of HTML to a PrintWriter.
	 * 
	 * @param lines
	 *            the actual HTML (excluding <HTML><BODY> and </BODY></HTML>)
	 *            to write
	 * @param pw
	 *            where to write the HTML
	 */
	public static void presentHTML(List lines, PrintWriter pw) {
		pw.println("<HTML>");
		pw.println("  <BODY BGCOLOR=#ffffff>");
		for (int i = 0; i < lines.size(); i++)
			pw.println((String) lines.get(i));
		pw.println("  </BODY>");
		pw.println("</HTML>");
		pw.flush();
		pw.close();
	}


	/**
	 * This method takes a String and returns an encoded version of the String
	 * that can be used as a Java variable name.
	 * 
	 * @param origString
	 *            the pre-encoded string
	 * @return the encoded string to be used as a java variable
	 */
	/*public synchronized static String encodeToJavaName(String origString) {
		return Introspector.decapitalize(encodeToJavaClassName(origString));
	}*/

	/**
	 * Write the given message to the Servlet Response Stream.
	 * 
	 * @param message
	 *            the error message to display
	 * @param res
	 *            the servlet response stream object
	 */
	public static void presentError(String servletName, String message,
			HttpServletResponse res) throws IOException {
		List lines = new ArrayList(2);
		lines.add("<H2>Server-Side Error</H2>");
		lines.add(servletName + ": " + message);

		res.setContentType("text/html");
		presentHTML(lines, new PrintWriter(res.getOutputStream()));
	}
	/**
	 * Get the EFGServletInitializer to initialize the backend database.
	 * 
	 * @return the EFGServletInitializer object
	 */
	public  EFGServletInitializerInterface getEFGServletInitializer(){
			return EFGServletInitializerInstance.getInstance(servletContext);
	}

	public boolean createConfigFileMap() {
		String fileName = "config.properties";
		if (path == null) {
			path = servletContext.getRealPath("/WEB-INF/classes/")
					+ System.getProperty("file.separator");
		}

		String fullPath = path + fileName;
		if (configMap != null) {
			Long lg = (Long) (configMap.get(fileName));
			Long now = new Long((new File(fullPath)).lastModified());
			if (now.compareTo(lg) > 0) {
				configMap.clear();
				configMap.put(fileName, new Long(System.currentTimeMillis()));
				readConfigFile(fileName);
			}
		} else {
			configMap = new Hashtable();
			Long time = new Long(System.currentTimeMillis());
			configMap.put(fileName, time);
			readConfigFile(fileName);
		}
		return true;
	}

	private  void addConfiguredDatasources() {
		
		/*  try{ String templateFile =
		  servletContext.getRealPath("/templateConFigFiles") + File.separator +
		  EFGImportConstants.TEMPLATES_CONFIG_SRC; FileReader reader = new
		  FileReader(new File(templateFile));
		 project.efg.templates.searchPageConfig.EFGSearchPageConfig espc =
		  (EFGSearchPageConfig)EFGSearchPageConfig.unmarshalEFGSearchPageConfig(reader);
		  if(espc != null){ int dsCount = espc.getDatasourceCount(); for(int i =
		  0; i < dsCount; i++){
		  project.efg.templates.searchPageConfig.Datasource datasource =
		  espc.getDatasource(i); String name = datasource.getName().trim();
		  configuredDatasources.add(name.toLowerCase()); } } } catch(Exception
		  ee){ if(log != null){ LoggerUtils.logErrors(ee); } else{
		 LoggerUtilsServlet.logErrors(ee); } }
		 */
	}



	
		private void clean() {
			String fullPath = servletContext.getRealPath("/");
			// Retrieve directory path and get full list of files
			File staleFiles = new File(fullPath);
			String thmDir = EFGImportConstants.EFGIMAGES_THUMBS;
			File[] list = staleFiles.listFiles();
	
			for (int f = 0; f < list.length; f++) {
				if (list[f].getName().equals(thmDir)) {
					this.deleteDir(list[f]);
				}
			}
			staleFiles = new File(fullPath + File.separator
					+ EFGImportConstants.TEMPLATES_XML_FOLDER_NAME);
			if (!staleFiles.exists()) {
				return;
			}
			list = staleFiles.listFiles();
	
			for (int f = 0; f < list.length; f++) {
				File staleFile = list[f];
	
				if (staleFile.getName().endsWith("_old")) {				
						this.deleteDir(list[f]);
				
				}
			}
		}



	/*public synchronized String getQueryHandlerName() {
		return (String) configMap.get("className");
	}*/
//	 Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns
	// false.
	private boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}
	/**
	 * This method takes a String and returns an encoded version of the String
	 * that can be used as a Java class name.
	 * 
	 * @param origString
	 *            the pre-encoded string
	 * @return the encoded string to be used as a java class name
	 */
	/*private static String encodeToJavaClassName(String origString) {
		StringBuffer sb = new StringBuffer(origString);
		int strLength = sb.length();

		if (strLength > 0 && !Character.isJavaIdentifierStart(sb.charAt(0)))
			sb.setCharAt(0, '_');

		for (int i = 1; i < strLength; i++)
			if (!Character.isJavaIdentifierPart(sb.charAt(i)))
				sb.setCharAt(i, '_');

		return sb.toString();
	}*/

	private void readConfigFile(String fileName) {
		// get the path to properties file
		if (path == null)
			path = servletContext.getRealPath("/WEB-INF/classes/")
					+ System.getProperty("file.separator");

		String fullPath = path + fileName;

		try {
			// Read the file
			BufferedReader propFile = new BufferedReader(new FileReader(
					fullPath));
			String word = null;
			while ((word = propFile.readLine()) != null) {
				if (!word.startsWith("#")) {// Strings that starts with the "#"
											// sign are comments
					String[] tokens = word.split("=");
					String temp1 = null;
					String temp2 = null;
					if (tokens.length == 2) {
						temp1 = tokens[0];
						temp2 = tokens[1];
						int index = temp2.indexOf("#");
						if (index != -1) {
							temp2 = tokens[1].substring(0, index - 1);
						}
						configMap.put(temp1, temp2);
					} else {
						log.debug("property file content incorrect");
					}
				}
			}// end while
		} catch (Exception e) {
			if (log != null) {
				log.error(e.getMessage());
				LoggerUtils.logErrors(e);
			} else {
				LoggerUtilsServlet.logErrors(e);
			}
		}
	}

	

	/**
	 * Returns the fullPath to the metadata file
	 * 
	 * @return String the full path to the metadatafile
	 */
	public static String getEFGMetaDataFileName() throws Exception {
		if (metadataFileName != null)
			return metadataFileName;
		else
			throw new Exception("Metadata File Name is not set..");
	}



	/**
	 * Retrieves the full path to the directory holding all the metadata files
	 * 
	 * @return the full path to the directory holding all metadata files
	 */
	public static String getPathToResourceFiles() throws Exception {
		if (pathToResourceFiles != null)
			return pathToResourceFiles;
		else
			throw new Exception("pathToResourceFiles is not set..");
	}

	/**
	 * Populate the set
	 */
	public static void fillSet(String[] fedSchemaNames, String schemaLocation) {
		if (set != null) {
			return;
		}
		FederationParser fdp = new FederationParser();
		for (int i = 0; i < fedSchemaNames.length; i++) {
			fdp.startParsing(schemaLocation + File.separator
					+ fedSchemaNames[i]);
		}
		set = fdp.getSet();
	}

	/**
	 * Return the set populated with the keywords
	 */
	public static Set getSet() {
		return set;
	}

	/**
	 * Returns true if this key is in the Set
	 */
	public static boolean contains(String word) {
		if (set == null) {
			return false;
		}
		return set.contains(word);
	}

	public static synchronized void addToSet(String name) {
		set.add(name);
	}

	/**
	 * Add the key and value to the map
	 */
	public static void addToKeyWordsMap(String key, String value) {
		if (keyWordsMap == null) {
			
		}
		keyWordsMap.put(key, value);
	}

	/**
	 * Returns true if key is a keyword
	 */
	public static boolean containsKeyWord(String key) {
		return keyWordsMap.containsKey(key);
	}
	 public static String getPathToServlet(){
			return servletContext.getRealPath("/");
		   }
	/**
	 * Returns the value associated with this key
	 */
	public static Object getKeyWordValue(String key) {
		return keyWordsMap.get(key);
	}
    /**
     * Obtain the URL of the servlet that received the passed in HttpServletRequest.
     *
     * @param request an HttpServletRequest object that contains a user's request paramters
     * @return a string containg the full URL of the servlet that received the request.
     * @throws ServletException
     * @throws IOException
     */
  

    /**
     * Obtain the context path of the passed in HttpServletRequest.
     *
     * @param request an HttpServletRequest object that contains a user's request paramters
     * @return a string containg the context path of the servlet that receive the request 
     * @throws ServletException
     * @throws IOException
     */
   /* public static String getContextPath(HttpServletRequest request)
	throws ServletException, IOException 
    {
    	return request.getContextPath();
    }*/
	
	/**
	 * Parse a schema and populate a Set with the members of the schema
	 * 
	 * @param schemaNames -
	 *            The name of the schema to be parsed
	 * @param path -
	 *            The path to the schema file
	 */
	private void parseAndFillSet(String schemaNames, String path) {
		String[] arr = schemaNames.split("\\s");
		fillSet(arr, path);
	}
//	 Looks for a servlet context init parameter with a
	// given name. If it finds it, it puts the value into
	// a servlet context attribute with the same name. If
	// the init parameter is missing, it puts a default
	// value into the servlet context attribute.
	/**
	 * Taken from More Servlets and JavaServer Pages from Prentice Hall and Sun
	 * Microsystems Press, http://www.moreservlets.com/. &copy; 2002 Marty Hall;
	 * may be freely used or adapted.
	 */
	private void setInitialAttribute(ServletContext context,
			String initParamName) {
		String initialValue = context.getInitParameter(initParamName);
		if (initialValue != null) {
			context.setAttribute(initParamName, initialValue);
		}
	}
	/**
	 * Create a list of keywords from the keywords properties file
	 */
	private void createKeyWordMap() {
		// get the path to properties file
		String fullPath = servletContext.getRealPath("/WEB-INF/classes/"
				+ File.separator + EFGImportConstants.EFG_KEYWORDS_PROPERTIES);
		try {
			// Read the file
			BufferedReader propFile = new BufferedReader(new FileReader(
					fullPath));
			String word = null;
			while ((word = propFile.readLine()) != null) {

				if (!word.startsWith("#")) {// Strings that starts with the "#"
											// sign are comments
					String[] tokens = word.split("\\s");

					if (tokens.length >= 2) {
						StringBuffer buf = new StringBuffer();
						int i = 1;
						while (i < tokens.length) {
							String temp = tokens[i];

							int index = temp.indexOf("#");
							if (index != -1) {
								buf.append(temp.substring(0, index - 1));
								break;
							} else {
								if (!"".equals(temp.trim())) {
									buf.append(temp + " ");
								}
							}
							i++;
						}
						if (buf.length() > 0) {
							addToKeyWordsMap(tokens[0],
									new String(buf.toString()));
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

}

// $Log$
// Revision 1.1.2.2  2006/07/11 21:48:22  kasiedu
// "Added more configuration info"
//
// Revision 1.1.2.1  2006/06/08 13:27:42  kasiedu
// New files
//
// Revision 1.1.1.1 2006/01/25 21:03:48 kasiedu
// Release for Costa rica
//