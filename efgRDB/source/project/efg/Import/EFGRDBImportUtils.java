/**
 * $Id$
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

package project.efg.Import;
import project.efg.util.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class is a utility class for creating and populating Relational Databases for EFG.
 * It is the responsibility of user to have a properties file "RDBProps.properties" created 
 * with the relevant information before this class is used.
 */
public class EFGRDBImportUtils implements EFGRDBConstants
{
    private static Connection conn;
    private static EFGRDBTableCreator rdb;
    private static String[] header;
    private static String dbDriverName;
    private static String dburl;
    private static boolean initialized = false;
    static Logger log = null;
    
    static{
	try{
	    log = Logger.getLogger(EFGRDBImportUtils.class); 
	}
	catch(Exception ee){
	}
    }
    /**
     * Initializes connection to Database, sets the driver etc
     * Parameters are read from a properties file (RDBProps.properties) in the same directory 
     * as this file.
     * Properties file should have the following information:
     * dbusername=your_username
     * dbpasswd=your_password
     * dburl=your_url(should include Database name and port number
     * dbDriverName = your_jdbc_drivername
     */
    public static void init()
    {
	try {
	    initialized = true; //Indicates that this method has been called
	    setProperties(EFGRDBConstants.RDBPROPERTIES_FILE); //set application wide properties
	    Class.forName(System.getProperty("dbDriverName")).newInstance();	  
	    rdb = new EFGRDBTableCreator();
	}
	catch(Exception e){
	    System.err.println(e.getMessage());
	    // LoggerUtils.logErrors(e);
	}
    }
    /** 
     * This method reads and set System Properties required by this application. 
     * The file must be named "RDBProps.properties" and must exist in the same directory as this file. 
     *
     * @param name the file name of the property file
     */
    private static void setProperties(String name){
	try{
	    String user_dir = System.getProperty("user.dir");
	    String props = user_dir + File.separator + name;
	    Properties p = new Properties(System.getProperties()); 
	    //String stream = (project.efg.Import.EFGRDBImportUtils.class.getResource(name)).getFile();
	    p.load(new FileInputStream(props));
	    System.setProperties(p);	
	}
	catch(Exception ee){
	    System.err.println(ee.getMessage());
	    LoggerUtils.logErrors(ee);
	}
    }
    /**
     * Obtains a connection from the database.
     *
     * @return a Connection object
     * @throws Exception
     */
    public synchronized static Connection getConnection() throws Exception 
    {
	if (!initialized){
	    init();
	}
	return getConnection(
			     System.getProperty("dburl"), 
			     System.getProperty("dbusername"), 
			     System.getProperty("dbpassword")
			     );
    }
    /**
     * Gets and returns a connection to the Database.
     *
     * @param url the full url to the database(includes Database name and port)     
     * @param username the username to connect to Database
     * @param password the password to connect to Database
     * @return a Connection object to the database, null if no connection could be created
     * @throws Exception if cannot create connection
     */
    public static Connection getConnection(String url, String username, String password)
    {
	try{
	    return DriverManager.getConnection(url, username, password);
	}
	catch(Exception ex){
	    LoggerUtils.logErrors(ex);
	}
	return null;
    }
    /**
     *Creates a given database if it does not already exists.
     * @return false if current user does not have enough privilegs to create a databse.
     */
    public static boolean createDatabase(String databaseName){
	try{
	    if (!initialized){
		init();
	    }
	    return rdb.createDatabase(databaseName);
	}
	catch(Exception ee){
	    //find out the type of error
	}
	return false;
    }
    /**
     * Creates a mapping table (ALL_EFG_RDB_TABLES) for efg database.
     *
     * @param tableName the name of the table to create. 
     * @return true if table already exist or is succesfully created, false otherwise
     */
    public static boolean createEFGMappingTable(String tableName)
    {
	if (!initialized){
	    init();
	}
	return rdb.createEFGMappingTable(tableName);
    }
    /**
     * Checks for the Existence of a Table in the Database.
     *
     * @param tableName the name of the table to check.
     * @return true if table exist, false otherwise
     */
    public static boolean isExistTable(String tableName)
    {
	if (!initialized){
	    init();
	}
	return rdb.isExistTable(tableName);
    }

    /**
     * Inserts the pair(data source file name, Meta data source file name).
     *
     * @param datafn the name of the datasource data file
     * @param metadatafn the name of the datasource metadata file
     * @return true if insertion is successful false otherwise     
     */
    public static boolean setPair(String datafn, String metadatafn)
    {
	if (!initialized){
	  init();
	}
	return rdb.setPair(datafn, metadatafn);
    }
    /**
     * Check if the pair exists in tabe. If not remove the one that exists
     */
    public static boolean isCheckPair(String datafn, String metadatafn){
	return rdb.isCheckPair(datafn,metadatafn);
    }
    /**
     * This method creates and populates a given table Name with the given table headers(column names) 
     * and a given list of records
     *
     * @param tableName the name of the table to be created
     * @param header a String[] containing the names of the columns of the Database Table to be created.
     * @param records a List, each of which's member is a String Array containing the data for each column in the header.
     * @see EFGImportTool#importData(String , int)
     */
    public static void createAndPopulateTable(String tableName, String[] header, List records)
    {
	if(!initialized ){
	    init();
	}
	//prepare to create the table
	rdb.setTableName(tableName);
	rdb.createTable(header);
	//populate current table
	rdb.populateTable(records);
    }
   /**
     * Closes the connection object used in this application. It is the responsibility of the user 
     * to call closeConnection after using this class.
     */
    public synchronized static void closeConnection() throws Exception
    {
	if (conn != null) {
            conn.close();
        }
    }
    /**
     * Inserts the specfied mapping Hashtable with the specified Metadata file name into the efg 
     * mapping table "ALL_EFG_RDB_TABLES".
     *
     * @param tableName the name of the Metadata file
     * @param mapping the mapping (Hashtable) created for this metadata file
     */
    /* public static void insertIntoEFGRDBTable(String tableName, Hashtable mapping)
   //  {
// 	if(!initialized){
// 	    init();
// 	}
// 	rdb.insertMappingObject(tableName, mapping);
// 	}*/
    
    /**
     * Creates all the helper tables required by the efg Application.
     *
     * @param datasource the name of the datasource for the helper tables
     */
    /* public static void createRDBHelperTables(String datasource)
//     {
// 	EFGRDBHelperTablesCreator.createRDBHelperTables(datasource);
// 	}*/

    /**
     * Creates a Hashtable mapping the searchable fields to its values for the named dataSource.
     * It is assumed that user already has the named datasource in the mapping table 
     * "ALL_EFG_RDB_TABLES".
     *
     * @param dataSourceName the name of the datasource
     */
    /* public static void createSearchPage(String dataSourceName)
  //   {
// 	if(!initialized){
// 	    init();
// 	}

// 	Hashtable mapping = createDBSearchPage(dataSourceName);
// 	if (mapping.isEmpty()){
// 	    return;
// 	}
// 	rdb.insertSearchPage(dataSourceName, mapping);
// 	}*/

    /**
     * Creates and inserts a search page for the given datasource into the efg mapping table
     * "ALL_EFG_RDB_TABLES"
     *
     * @param dataSourceName the datasource name to be used to query the mapping Table
     * @return a Hashtable mapping the searchable fields to its values for the named datasource
     */
    /* private static Hashtable createDBSearchPage(String dataSourceName)
 //    {
// 	if(!initialized){
// 	    init();
// 	}
// 	return rdb.createDBSearchPage(dataSourceName);  
// 	}*/
    
    /**
     * Translates an array of names into its corresponding legal names that can be used to 
     * create a database table column.
     *
     * @param h the String[] contains the original column names
     * @param tableName the name of the datasource (DS_DATA) used to obtain the Hashtable that contains the mappings
     * @return header the String[] containing the translated names (legal names)
     */
    /* public static String[] translateHeader(String[] h, String tableName)
  //   {
// 	if (!initialized){
// 	    init();
// 	}
//         String[] header = new String[h.length];

//         Hashtable mapping;

// 	//get the Hashtable mapping taxon's name to legal name
//         mapping = rdb.getMapping(tableName);

//         for(int i = 0; i < h.length; i++) {
//             header[i] = (String) mapping.get(h[i]);
//         }
//         return header;
//	}*/

 
}

//$Log$
//Revision 1.1  2006/01/25 21:03:42  kasiedu
//Initial revision
//
//Revision 1.1.1.1  2003/10/17 17:03:05  kimmylin
//no message
//
//Revision 1.8  2003/08/20 18:45:41  kimmylin
//no message
//

