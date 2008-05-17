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

package project.efg.server.servlets;

import java.beans.Introspector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import project.efg.server.factory.EFGSpringFactory;
import project.efg.server.interfaces.EFGServletInitializerInterface;
import project.efg.server.interfaces.ServletAbstractFactoryInterface;
import project.efg.server.utils.EFGDisplayObjectList;
import project.efg.server.utils.EFGServletInitializerInstance;
import project.efg.util.interfaces.RegularExpresionConstants;
import project.efg.util.utils.EFGDisplayObject;
import project.efg.util.utils.EFGRDBImportUtils;
import project.efg.util.utils.EFGUtils;

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
		specialParams = new ArrayList();
		sit = getEFGServletInitializer();
		for (Enumeration enum1 = servletContext.getInitParameterNames(); enum1
				.hasMoreElements();) {
			String paramName = (String) enum1.nextElement();
			if (paramName.endsWith("_query_ignore"))
				add2IgnoreParams(servletContext, paramName);
			else
				setInitialAttribute(servletContext, paramName);
		}

		createKeyWordMap();
		EFGUtils.log("Context parameters added");
		EFGRDBImportUtils.init();
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
		destroyDriverManager();
		try {
			servletContext.log("Context is being destroyed");
			clean();
			servletContext.log("Save template cache to database");
			sit.close();
		} catch (Exception e) {
			EFGUtils.log(e.getMessage());
		}
		EFGUtils.log("EFG context destroyed");
	}

	/**
	 * Returns true if this key is in the Set
	 */
	public static boolean contains(String word) {
		if (set == null)
			return false;
		else
			return set.contains(word);
	}

	/**
	 * Returns the value associated with this key
	 */
	public static Object getKeyWordValue(String key) {
		return keyWordsMap.get(key);
	}

	public static synchronized void addToSet(String name) {
		set.add(name);
	}

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
		lines.add("<h2>Server-Side Error</h2>");
		lines.add(servletName + ": " + message);
		res.setContentType("text/html");
		presentHTML(lines, new PrintWriter(res.getOutputStream()));
	}

	public static Map populateMapDatasources(String tableName) {
		Map map = new HashMap();
		try {
			ServletAbstractFactoryInterface servFactory = EFGSpringFactory
					.getServletAbstractFactoryInstance();
			EFGDisplayObjectList listInter = null;
			if (servFactory != null) {
				servFactory.setMainDataTableName(tableName);
				listInter = servFactory.getListOfDatasources();
				if (listInter != null && listInter.getCount() > 0) {
					Iterator dsNameIter = listInter.getIterator();
					do {
						if (!dsNameIter.hasNext())
							break;
						EFGDisplayObject obj = (EFGDisplayObject) dsNameIter
								.next();
						String displayName = obj.getDisplayName();
						String datasourceName = obj.getDatasourceName();
						if (datasourceName != null
								&& !"".equals(datasourceName)
								&& displayName != null
								&& !"".equals(displayName)) {
							servletContext.log("Adding: "
									+ datasourceName.toLowerCase());
							map.put(datasourceName.toLowerCase(), displayName);
						}
					} while (true);
				}
			}
		} catch (Exception ee) {
			servletContext.log(ee.getMessage());
		}
		return map;
	}

	private void cleanCommonImportExport(File dir) {
		if (!dir.exists())
			return;
		File list[] = dir.listFiles();
		for (int f = 0; f < list.length; f++) {
			EFGUtils.log("Removing: " + list[f].getAbsolutePath());
			deleteDir(list[f]);
		}

	}

	private synchronized void cleanImportExportDirectories() {
		StringBuffer fileLocationBuffer = new StringBuffer();
		fileLocationBuffer.append(servletContext.getRealPath("/"));
		fileLocationBuffer.append(File.separator);
		StringBuffer cBuffer = new StringBuffer(fileLocationBuffer.toString());
		cBuffer.append("imports");
		cBuffer.append(File.separator);
		File dir = new File(cBuffer.toString());
		cleanCommonImportExport(dir);
		cBuffer = new StringBuffer(fileLocationBuffer.toString());
		cBuffer.append("exports");
		cBuffer.append(File.separator);
		dir = new File(cBuffer.toString());
		cleanCommonImportExport(dir);
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
	private static void presentHTML(List lines, PrintWriter pw) {
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
	 * Get the EFGServletInitializer to initialize the backend database.
	 * 
	 * @return the EFGServletInitializer object
	 */
	private static EFGServletInitializerInterface getEFGServletInitializer() {
		return EFGServletInitializerInstance.getInstance();
	}

	/**
	 * Add the key and value to the map
	 */
	private static void addToKeyWordsMap(String key, String value) {
		keyWordsMap.put(key, value);
	}

	/**
	 * Obtain the URL of the servlet that received the passed in
	 * HttpServletRequest.
	 * 
	 * @param request
	 *            an HttpServletRequest object that contains a user's request
	 *            paramters
	 * @return a string containg the full URL of the servlet that received the
	 *         request.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void destroyDriverManager() {
		try {
			Introspector.flushCaches();
			Enumeration e = DriverManager.getDrivers();
			do {
				if (!e.hasMoreElements())
					break;
				Driver driver = (Driver) e.nextElement();
				if (driver.getClass().getClassLoader() == getClass()
						.getClassLoader())
					DriverManager.deregisterDriver(driver);
			} while (true);
		} catch (Throwable e) {
			System.err.println("Failed to cleanup ClassLoader for webapp");
			e.printStackTrace();
		}
	}

	private void clean() {
		cleanImportExportDirectories();
	}

	/**
	 * Deletes all files and subdirectories under dir. Returns true if all
	 * deletions were successful. If a deletion fails, the method stops
	 * attempting to delete and returns false.
	 */
	private synchronized boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String children[] = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success)
					return false;
			}

		}
		return dir.delete();
	}

	// Looks for a servlet context init parameter with a
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
		if (initialValue != null)
			context.setAttribute(initParamName, initialValue);
	}

	/**
	 * @param servletContext2
	 * @param paramName
	 */
	private void add2IgnoreParams(ServletContext context, String paramName) {
		String initialValue = context.getInitParameter(paramName);
		if (initialValue != null)
			specialParams.add(initialValue);
	}

	/**
	 * Create a list of keywords from the keywords properties file
	 */
	private void createKeyWordMap() {
		String fullPath = servletContext
				.getRealPath("/WEB-INF/classes/properties/" + File.separator
						+ "queryKeyWords.properties");
		try {
			BufferedReader propFile = new BufferedReader(new FileReader(
					fullPath));
			String word = null;
			do {
				if ((word = propFile.readLine()) == null)
					break;
				if (!word.startsWith("#")) {
					String tokens[] = RegularExpresionConstants.spacePattern
							.split(word);
					if (tokens.length >= 2) {
						StringBuffer buf = new StringBuffer();
						for (int i = 1; i < tokens.length; i++) {
							String temp = tokens[i];
							int index = temp.indexOf("#");
							if (index != -1) {
								buf.append(temp.substring(0, index - 1));
								break;
							}
							if (!"".equals(temp.trim()))
								buf.append(temp + " ");
						}

						if (buf.length() > 0)
							addToKeyWordsMap(tokens[0], new String(buf
									.toString()));
					}
				}
			} while (true);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static Collection getSpecialParams() {
		return specialParams;
	}

	private EFGServletInitializerInterface sit;
	private static Collection specialParams;
	private static ServletContext servletContext;
	public static String templateFilesGroup[] = { "templateFiles" };
	private static Set set;
	public static Hashtable lastModifiedTemplateFileTable = new Hashtable();
	public static Set configuredDatasources = Collections
			.synchronizedSet(new HashSet(20));
	private static Map keyWordsMap = Collections.synchronizedMap(new HashMap());

}

// $Log: EFGContextListener.java,v $
// Revision 1.1.1.1 2007/08/01 19:11:23 kasiedu
// efg2.1.1.0 version of efg2
//
// Revision 1.7 2007/02/20 16:34:01 kasiedu
// no message
//
// Revision 1.6 2007/02/14 02:44:40 kasiedu
// no message
//
// Revision 1.5 2007/01/14 15:56:31 kasiedu
// no message
//
// Revision 1.4 2007/01/13 03:22:14 kasiedu
// no message
//
// Revision 1.3 2007/01/12 15:04:04 kasiedu
// no message
//
// Revision 1.2 2006/12/08 03:51:00 kasiedu
// no message
//
// Revision 1.1.2.10 2006/11/07 14:38:18 kasiedu
// no message
//
// Revision 1.1.2.9 2006/09/10 12:03:23 kasiedu
// no message
//
// Revision 1.1.2.8 2006/08/30 13:53:34 kasiedu
// bug id 224-236
//
// Revision 1.1.2.7 2006/08/26 22:12:24 kasiedu
// Updates to xsl files
//
// Revision 1.1.2.6 2006/08/21 19:32:55 kasiedu
// Updates to files
//
// Revision 1.1.2.5 2006/08/13 23:53:15 kasiedu
// *** empty log message ***
//
// Revision 1.1.2.4 2006/08/09 18:55:25 kasiedu
// latest code confimrs to what exists on Panda
//
// Revision 1.1.2.3 2006/07/15 13:42:09 kasiedu
// no message
//
// Revision 1.1.2.2 2006/07/11 21:48:22 kasiedu
// "Added more configuration info"
//
// Revision 1.1.2.1 2006/06/08 13:27:42 kasiedu
// New files
//
// Revision 1.1.1.1 2006/01/25 21:03:48 kasiedu
// Release for Costa rica
//
