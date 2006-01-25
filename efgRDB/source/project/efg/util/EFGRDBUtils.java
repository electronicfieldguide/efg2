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

import project.efg.efgInterface.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class is a utility class for EFG relation database.
 * It would probably be a good idea to periodically go through this file
 * and make sure that the fields and methods present shouldn't be in a
 * particular class.
 */
public class EFGRDBUtils{
    
    private static Context ctx;
    private static Context env;
    private static String resourceName;
    private static DataSource ds;
    private static String mappingTableName;    
    private static List allDataSourceNames;
    private static Hashtable nameMapping;
    
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(EFGRDBUtils.class); 
	}
	catch(Exception ee){
	}
    }
    /**
     * Initializes the jndi context to our Database. Looks up the environment, 
     * reads the resource name from JNDI and makes the DataSource available for use.
     * This method must be called after setting the resourceName.
     *
     * @see javax.naming.Context
     * @throws an Exception
     */
    public static void initRDBContext() throws Exception
    {
	ctx = new InitialContext();
	env = (Context)ctx.lookup("java:/comp/env");
	
	if (env == null ) 
	    throw new Exception("No Context");
	resourceName = (String) env.lookup("resourceName");
	
	//get the resource name using the Utilities method
	ds = (DataSource) env.lookup(resourceName);

    }
    public static void closeRDBContext()throws Exception 
    {
	log.debug("clean up " + ds.toString());
	log.info("Cleaning up Context Resources");
	Class cl = Class.forName("org.apache.commons.dbcp.BasicDataSource");
	
	if(cl.isInstance(ds)){ 
	    log.debug("Found connection pool for " + ds.toString());
	    ds = null;
	    log.debug("Close All  connections ");    
	}
	else{
	    log.debug("Found connection pool for " + ds.toString());
	}
    }
    /**
     * Retrieve table name from the jndi context associated with this application.
     *
     * @param tableName the database Table Name the method name in this class - name in jndi.
     * @return the table name in database - value in jndi
     */    
    public static String getTableName(String tableName)
    {
	try {
	    if (tableName != null){
		return (String) env.lookup(tableName.trim());
	    }
	}
	catch(Exception e) {
	    log.error("Exception in getting Context in ServletUtilities.getTableName(): " + e.getMessage());
	    LoggerUtilsServlet.logErrors(e);
	}
	return null;
    }

    /**
     * Obtain the URL of the servlet that received the passed in HttpServletRequest.
     *
     * @param request an HttpServletRequest object that contains a user's request paramters
     * @return a string containg the full URL of the servlet that received the request.
     * @throws ServletException
     * @throws IOException
     */
    public static String getServletURL(HttpServletRequest request)
	throws ServletException, IOException 
    {
	ServletRequestWrapper srw = new ServletRequestWrapper(request);
	String server = srw.getServerName();
	int port = srw.getServerPort();
	String scheme =srw.getScheme();
	
	StringBuffer buf = new StringBuffer();
	buf.append(scheme);
	buf.append("://");
	buf.append(server);
	buf.append(":");
	buf.append(port);
	String path = request.getContextPath();
	buf.append(path);
	return buf.toString();
    }

    /**
     * Obtain the context path of the passed in HttpServletRequest.
     *
     * @param request an HttpServletRequest object that contains a user's request paramters
     * @return a string containg the context path of the servlet that receive the request 
     * @throws ServletException
     * @throws IOException
     */
    public static String getContextPath(HttpServletRequest request)
	throws ServletException, IOException 
    {
	return request.getContextPath();
    }  

    /**
     * Get the datatype from the efg mapping table "ALL_EFG_RDB_TABLES" associated
     * with the named datasource.
     *
     * @param dataSource the datasource name of the required datatype info
     * @return a Hashtable that contains the datatype info for the named datasource
     */
    public synchronized static Hashtable getTypeRegistry(String dataSource)
    {
	Hashtable datatypes = null;
	Connection conn = null;
	ResultSet rst = null;
	Statement stmt = null;
	StringBuffer query = new StringBuffer(); 
	try {
	    conn = getConnection();
	    conn.setAutoCommit(false);
	    stmt = conn.createStatement();
	    query.append("SELECT * FROM ");
	    query.append(getTableName("ALL_EFG_RDB_TABLES"));
	    query.append(" WHERE DS_DATA = \"");
	    query.append(dataSource);
	    query.append("\";");
	    
	    rst = stmt.executeQuery(query.toString());
	    
	    rst.next();
	    Object binStream = rst.getBinaryStream("datatype");
	    
	    // cast the blob to a Hashtable
	    ObjectInputStream objS = new ObjectInputStream((InputStream) binStream);
	    Object result = objS.readObject();
	    datatypes = (Hashtable) result;
	    
	    conn.commit();
	}
	catch(Exception ex){
	    LoggerUtilsServlet.logErrors(ex);
	}
	finally{
	    try{
		if(stmt != null)
		    stmt.close();
		if(rst != null)
		    rst.close();
		closeConnections(conn);
	    }
	    catch(Exception e){
		stmt = null;
		rst = null;
		//EFGUtils.log(e.getMessage());
		// e.printStackTrace();
	    }
	}
	return datatypes;
    }

    /**
     * Sets the resourceName. This is implemented as a singleton.
     *
     * @param resName the name of the resource
     */
    public static void setResourceName(String resName)
    {
	if (resourceName != null)
	    return;
	resourceName = resName;
    }

    /**
     * Sets the MappingTable Name. This is implemented as a singleton.
     *
     * @param resName the name of the resource
     */
    public static void setMappingTableName(String resName)
    {
	if (mappingTableName != null)
	    return;
	mappingTableName = resName;
    }

    /**
     * Gets the resource Name. 
     *
     * @return the resourceName null if the resourceName has not been set.
     */
    public static String getResourceName()
    {
	if (resourceName == null)
	    return null;
	else
	    return resourceName;
    }

    /**
     * Returns a connection to the database. User must call closeConnection() 
     * after using the connection obtained.
     *
     * @return a connection object
     */
    public static Connection getConnection()
    {
	try{
	    if(ds == null){
		initRDBContext();
	    }
	    return ds.getConnection();   
	}
	catch(Exception e){
	    LoggerUtilsServlet.logErrors(e);
	}
	return null;
    }

    /**
     * Close Connection object passed in. It must be called after a connection 
     * is no longer used.
     *
     * @param conn the Connection to be closed
     * @throws Exception
     */
    public static void closeConnections(Connection conn) throws Exception
    {
	if (conn != null) {
	    conn.close();
	}
    }

    /**
     * Returns a list of available datasources in the efg database.
     *
     * @return a list of datasource names(string)
     */
    public static List getAllDataSourceNames()
    {
	if (allDataSourceNames == null){
	    retrieveAllDataSourceNames();
	}
	return allDataSourceNames;
    }

    /**
     * Retrieve a list of available datasources in the efg database.
     */
    public static void retrieveAllDataSourceNames()
    {
	EFGDataSourceHelper dsHelper = (new EFGDSHelperFactory()).getDataSourceHelper(); 
	allDataSourceNames = dsHelper.getDSNames();
    }
    
    /**
     * Retrieve the Hashtable mapping taxon's name to legal name associated with the
     * named datasource from the efg mapping table "ALL_EFG_RDB_TABLES".
     *
     * @param dataSourceName the name of the datasource, used in the where clause of the sql query
     * @return a Hashtable mapping taxon's name to its legal name
     */
    public synchronized static Hashtable getNameMapping(String dataSourceName)
    {
	if (nameMapping == null) {
	    nameMapping = new Hashtable();
	    Hashtable result = null;
	    Statement stmt =null;
	    ResultSet rst = null;
	    Connection conn = null;
	    
	    try {
		conn = getConnection();
		StringBuffer query = new StringBuffer("SELECT ds_data, mapping FROM "); 
		query.append(getTableName("ALL_EFG_RDB_TABLES"));
		stmt = conn.createStatement();
		rst = stmt.executeQuery(query.toString());
		
		while (rst.next()) {
		    String dsName = rst.getString(1);
		    Object binStream = rst.getBinaryStream(2);
		    ObjectInputStream objS = new ObjectInputStream((InputStream)binStream);
		    result = (Hashtable) objS.readObject();
		    nameMapping.put(dsName, result);
		}
	    }
	    catch (Exception ex){
		LoggerUtilsServlet.logErrors(ex);
	    }
	    finally {
		try{
		    if (stmt != null)
			stmt.close();
		    if (rst != null)
			rst.close();
		    EFGRDBUtils.closeConnections(conn);
		}
		catch(Exception e) {
		    stmt = null;
		    rst = null;
		    //EFGUtils.log(e.getMessage());
		}
	    }
	}
	return (Hashtable) nameMapping.get(dataSourceName);
    }
}

//$Log$
//Revision 1.1  2006/01/25 21:03:42  kasiedu
//Initial revision
//
//Revision 1.1.1.1  2003/10/17 17:03:05  kimmylin
//no message
//
//Revision 1.7  2003/08/20 18:45:42  kimmylin
//no message
//

