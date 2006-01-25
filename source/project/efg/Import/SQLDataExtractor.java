/**
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Matthew Passell<mpassell@cs.umb.edu>
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
import java.util.Map;
import java.util.HashMap;
import java.io.PrintWriter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * This class allows you to open a connection to a database using
 * JDBC and issue queries to the database.  It was inspired by a JDBC 
 * sample program from Sun.
 */
public class SQLDataExtractor implements EFGImportConstants{
    
    private String sourceURI;
    private Connection con;
    private Statement stmt;
    private boolean dbOpen = false, verbose = false;
    private static Map SQLToJavaTypeMap = null;
    
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(SQLDataExtractor.class); 
	}
	catch(Exception ee){
	}
    }
    /**
     * Create new SQLDataExtractor object.  Next, code should call openDB,
     * then make some requests, then call closeDB when done.
     * @param jdbcSourceURI URI of a JDBC source, such as 
     *    "jdbc:odbc:FileMaker_Files" or FILEMAKERURI
     * @param driverClassName the Java class name for the JDBC driver, such
     *    as "sun.jdbc.odbc.JdbcOdbcDriver" or JDBCODBCCLASS
     * @param verbose true turns verbose mode on
     */
    public SQLDataExtractor(String jdbcSourceURI, String driverClassName,
			    boolean verbose) {
	sourceURI = jdbcSourceURI;
	this.verbose = verbose;

	try {
	    // Load the jdbc driver
	    Class.forName(driverClassName);
	}
	catch (ClassNotFoundException e) {
	    log.error("Can't find class "+driverClassName);
	    LoggerUtils.logErrors(e);
	}

	//Use ...LogStream with JDBC 1
	if (verbose){
	    // Log displayed to stdout
	    DriverManager.setLogWriter(new PrintWriter(System.out));
	}
	else{
	    DriverManager.setLogWriter(null);  //No log displayed
	}
    }

    /**
     * Create new SQLDataExtractor object.  Next, code should call openDB,
     * then make some requests, then call closeDB when done.  Uses default
     * verbose mode off.
     * @param jdbcSourceURI URI of a JDBC source, such as 
     *    "jdbc:odbc:FileMaker_Files" or FILEMAKERURI
     * @param driverClassName the Java class name for the JDBC driver, such
     *    as "sun.jdbc.odbc.JdbcOdbcDriver" or JDBCODBCCLASS
     */
    public SQLDataExtractor(String jdbcSourceURI, String driverClassName) {
	this(jdbcSourceURI, driverClassName, false);
    }

    /**
     * Check if the database is open.
     */
    public boolean isDBOpen() {
	return dbOpen;
    }

    /**
     * Opens the database.  This must be called before any query requests
     * can be made.
     */
    public void openDB() throws SQLException {
	if (dbOpen) return; //Don't open database connection twice!
    
	// Attempt to connect to a driver.  Each one
	// of the registered drivers will be loaded until
	// one is found that can process this SOURCEURI
	con = DriverManager.getConnection(sourceURI, "", ""); //no UID or passwd

	// If we were unable to connect, an exception
	// would have been thrown.  So, if we get here,
	// we are successfully connected to the source

	// Check for, and display and warnings generated
	// by the connect.
	checkForSQLWarning(con.getWarnings());

	// Create a Statement object so we can submit
	// SQL statements to the driver
	stmt = con.createStatement();
    
	dbOpen = true;
    }

    /**
     * Closes the database.  This must be called after all query requests
     * have completed.
     */
    public void closeDB() throws SQLException {
	if (!dbOpen) return; //Don't close unopened db connection!

	// Close the result set, statement, and connection
	stmt.close();
	con.close();

	dbOpen = false;
    }
  
    /**
     * Issues a query to the database and returns a ResultSet.<BR>
     * Note: the openDB method must be called before this method and
     * and closeDB must be called after ResultSet no longer needed.
     * @param queryString the SQL query to be made
     */
    public ResultSet makeSQLQuery(String queryString) 
	throws SQLException {
	// returning the ResultSet object
	return stmt.executeQuery(queryString);
    }

    /**
     * Opens database, makes a query, and fills the nameTypes and 
     * javaSQLNames Maps with appropriate values for the SQL table.
     * Assumes that the two Maps start out null.
     * In verbose mode, displays Java fieldname, SQL column name, and
     * Java object type.
     * @param dbName the database (really the table) to be queried
     * @param nameTypes a map from Java field names to Java object types
     * @param javaSQLNames a map from Java field names to SQL column names
     */
    public void getSQLColumnNames(String dbName, Map nameTypes,
				  Map javaSQLNames) 
	throws SQLException {
	openDB();

	try {
	    // Submit a query, creating a ResultSet object
	    ResultSet rs = makeSQLQuery(EFGImportConstants.DEFAULTQUERYBASE+dbName);

	    ResultSetMetaData rsmd = rs.getMetaData();
	    int numCols = rsmd.getColumnCount();

	    for (int i=1; i<=numCols; i++) {
		String colName = new String(rsmd.getColumnLabel(i));
	
		//Use with JDBC 1 or JDBC/ODBC Bridge
		int sqlTypeNum = rsmd.getColumnType(i);
		String objType = SQLNumToJavaTypeName(sqlTypeNum);

		//Use with JDBC 2.0 and JDBC Driver supporting this feature
		//  	String objType = rsmd.getColumnClassName(i);
		//  	if (objType.startsWith("java.lang."))
		//  	  objType = objType.substring(10);

		String javaName = EFGUtils.encodeToJavaName(colName);

		nameTypes.put(javaName, objType);
		javaSQLNames.put(javaName, colName);	
	
		if (verbose){
		    log.info(javaName+"\t"+colName+"\t"+objType);
		}
	    }
	    rs.close();
	}
	catch (SQLException sql) {
	    printSQLErrors(sql);
	    System.err.println(sql.getMessage());
	   
	}
    	closeDB();
    }

    /**
     * SQLNumToJavaTypeName takes the integer code for a SQL Data Type 
     * and returns a String giving the name of the equivalent Java class.
     * WHEN WE MOVE TO JDK 1.2 (AND THUS JDBC 2.0), CHECK OUT METHOD 
     * getColumnClassName IN java.sql.ResultSetMetaData!!
     * @param SQLTypeInt - SQL Data Type number<BR>
     *    See javadoc for java.sql.Types
     */
    public static String SQLNumToJavaTypeName(int SQLTypeInt) {
	if (SQLToJavaTypeMap == null)
	    initSQLToJavaTypeMap();
	
	Integer SQLType = new Integer(SQLTypeInt);
	String value = (String)SQLToJavaTypeMap.get(SQLType);
	if (value != null){ //Java Type not found
	    return value;
	}
	else{
	    return "String";
	}
    }  

    /**
     * Set up the mapping from SQL numeric types to Strings
     * giving associated Java class names.
     */
    private static void initSQLToJavaTypeMap() {
	Object[][] types = {
	    //The Character Types
	    {new Integer(Types.CHAR), "java.lang.String"},
	    {new Integer(Types.VARCHAR), "java.lang.String"},
	    {new Integer(Types.LONGVARCHAR), "java.lang.String"},


	    //The Large Numeric Types
	    {new Integer(Types.NUMERIC), "java.math.BigDecimal"},
	    {new Integer(Types.DECIMAL), "java.math.BigDecimal"},

	    //The Boolean Type
	    {new Integer(Types.BIT), "Boolean"},

	    //The Integer Types
	    {new Integer(Types.INTEGER), "Integer"},
	    {new Integer(Types.SMALLINT), "Integer"},
	    {new Integer(Types.TINYINT), "Integer"},

	    //The Long Type
	    {new Integer(Types.BIGINT), "Long"},

	    //The Float Type
	    {new Integer(Types.REAL), "Float"},

	    //The Double Types
	    {new Integer(Types.FLOAT), "Double"},
	    {new Integer(Types.DOUBLE), "Double"},

	    //The byte[] Types
	    {new Integer(Types.BINARY), "byte[]"},
	    {new Integer(Types.VARBINARY), "byte[]"},
	    {new Integer(Types.LONGVARBINARY), "byte[]"},

	    //The java.sql.Date Type
	    {new Integer(Types.DATE), "java.sql.Date"},

	    //The java.sql.Time Type
	    {new Integer(Types.TIME), "java.sql.Time"},
 
	    //The java.sql.Timestamp Type
	    {new Integer(Types.TIMESTAMP), "java.sql.Timestamp"}
	};

	//Set up the SQL to Java Type map
	SQLToJavaTypeMap = new HashMap(types.length);

	//Populate Map
	for (int i = 0; i < types.length; i++){
	    SQLToJavaTypeMap.put(types[i][0], types[i][1]);
	}
    }

    /**
     * Count and return the rows in the given table.<BR>
     * <B>Note:</B> the openDB method must be called before this method.
     * @param tablename the name of the table
     */
    public int countRows(String tablename) throws SQLException {
	ResultSet countTable = makeSQLQuery("SELECT count(*) FROM "+tablename);

	countTable.next(); //Advance to first and only row
	return countTable.getInt(1);
    }

    /**
     * Checks for and displays SQL warnings.  Returns true if a warning
     * existed.
     * @param warn the SQLWarning to display
     */
    public boolean checkForSQLWarning(SQLWarning warn) 
	throws SQLException {
	boolean rc = false;

	// If a SQLWarning object was given, display the
	// warning messages.  Note that there could be
	// multiple warnings chained together
	if (warn != null) {
	    log.error ("\n *** Warning ***\n");
	    rc = true;
	    while (warn != null) {
		log.debug("SQLState: " + warn.getSQLState());
		log.debug("Message:  " + warn.getMessage());
		log.debug("Vendor:   " + warn.getErrorCode());
		log.debug("");
		warn = warn.getNextWarning();
	    }
	}
	return rc;
    }

    /**
     * Display SQL error messages within a SQLException.
     * @param ex the exception to display
     */
    public void printSQLErrors(SQLException ex) {
	LoggerUtils.printSQLErrors(ex);
    }
}
//$Log$
//Revision 1.1  2006/01/25 21:03:48  kasiedu
//Initial revision
//