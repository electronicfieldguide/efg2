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
 * This class is used to create and populate a Relational Database Table.
 */
public class EFGRDBTableCreator implements Serializable 
{
    private Statement stmt; 
    private Connection conn;
    private ResultSet rst;
    private PreparedStatement statement;  
    private String tableName; //The name of the Database Table
    private StringBuffer query;
    
    // Specify the type of data source
    private static final int DATA_DS        = 0;
    private static final int METADATA_DS    = 1;
    private static final int BOTH_DS        = 2;
    

    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(EFGRDBTableCreator.class); 
	}
	catch(Exception ee){
	}
    }
    /**
     * Constructor. Obtain a connection to the database.
     */
    public EFGRDBTableCreator( )
    {
	try{
	    //Only one connection is needed while all the tables are being created
	    conn = EFGRDBImportUtils.getConnection();
	}
	catch(Exception e){
	    LoggerUtils.logErrors(e);
	}
    }

    /**
     * Check if a Table exist in Database.
     *
     * @param tableName the name of the table
     * @return true if the table exists, false otherwise
     */
    public boolean isExistTable(String tableName)
    {
	try {
 	    // Gets the database metadata
	    DatabaseMetaData dbmd = conn.getMetaData();
	    
	    // Specify the type of object; in this case we want "Table"
	    String[] types = {"TABLE"};
	    rst = dbmd.getTables(null, null, tableName, types);
	    
	    // Get the table names
	    if (rst.next()) {
		closeRDBresources();
		return true;
	    }
	    else{
		closeRDBresources();
		return false;
 	    }
	} catch(Exception ex) {
	    try{
		LoggerUtils.logErrors(ex);
		closeRDBresources();
	    }
	    catch(Exception e){
		LoggerUtils.logErrors(e);
	    }
 	}
	return false;
    }
    /**
     * Checks the Database to see if all columns are populated in the mapping table "ALL_EFG_RDB_TABLES" 
     * for the specified datasource(data & metadata). If the population is complete, return true;
     * if the population is incomplete, delete the existing incomplete population and return false.
     * 
     * @param datafn the data fileName to check .
     * @param metadatafn the metadata fileName to check .
     * @return true if datafn, metadafn and the rest column values already exist in ALL_EFG_RDB_TABLES on the same row.
     */
    public synchronized boolean isCheckPair(String datafn, String metadatafn)
    {
	if ((datafn == null) || (metadatafn == null)) {
	    log.error("Either or both datafn and metadatafn is null");
	    return true;
	}

	try{
	    String efgRDBTable = System.getProperty("ALL_EFG_RDB_TABLES");
	    query = new StringBuffer("SELECT * FROM ");
	    query.append(efgRDBTable);
	    query.append(" WHERE DS_METADATA = ? and ");
	    query.append(" DS_DATA = ? GROUP BY DS_METADATA ");
	    
	    statement = conn.prepareStatement(query.toString());  
	    statement.setString(1, metadatafn);      
	    statement.setString(2, datafn);      
	    rst = statement.executeQuery();
	    if (rst.next()) {
		// delete all the tables created for this datasource
		stmt = conn.createStatement();
		//this is a MySQL command
		//read from properties file
		ResultSet rs = null;
		if(System.getProperty("MySQL") != null){
		    rs = stmt.executeQuery("SHOW TABLES");
		}
		else{
		    DatabaseMetaData dbm = conn.getMetaData();
		    String types[] = { "TABLE" };
		    rs = dbm.getTables(null, null, "", types);
		}
		String tbName = null;
		while (rs.next()) {
		    Statement stmt2 = conn.createStatement();
		    tbName = rs.getString("Tables_in_efg");
		    if ((tbName.toUpperCase()).indexOf(datafn.toUpperCase()) != -1){
			stmt2.execute("DROP TABLE IF EXISTS " + tbName);
		    }
		}
		stmt.execute("DROP TABLE IF EXISTS " + metadatafn);
		try{
		    stmt.execute("DELETE FROM " + efgRDBTable + " WHERE DS_DATA = '" + datafn + "'");
		}	
		catch(Exception te){}
	    }
	}
	catch(Exception ex){
	    LoggerUtils.logErrors(ex);
	}
	finally {
	    try{
		closeRDBresources();
	    }
	    catch(Exception e){
		LoggerUtils.logErrors(e);
	    }
	}
	return false;
    }
  /**
     * Creates a mapping table (ALL_EFG_RDB_TABLES) for efg database.
     *
     * @param tableName the name of the table to create. 
     * @return true if table already exist or is succesfully created, false otherwise
     */
    public synchronized boolean createDatabase(String databaseName)
    {
	try{
	    conn.setAutoCommit(false);;
	    stmt = conn.createStatement();
	    stmt.executeUpdate(EFGImportConstants.CREATE_DATABASE_QUERYBASE + databaseName); 	    
	    conn.commit();//end Transaction
	    closeRDBresources(); 
	    return true;
	}
	catch(Exception e){
	    try{
		LoggerUtils.logErrors(e);
		conn.rollback(); //if transaction fails rollback
		closeRDBresources(); 
	    }
	    catch(Exception ex){
		LoggerUtils.logErrors(ex);
	    }
	}
	return false;
    }
    /**
     * Creates a mapping table (ALL_EFG_RDB_TABLES) for efg database.
     *
     * @param tableName the name of the table to create. 
     * @return true if table already exist or is succesfully created, false otherwise
     */
    public synchronized boolean createEFGMappingTable(String tableName)
    {
	try{
	    conn.setAutoCommit(false);;
	    stmt = conn.createStatement();
	    //will hold the query
	    StringBuffer query = new StringBuffer();
	    
	    //create the query
	    query.append("CREATE TABLE ");
	    query.append(tableName);
	    query.append("( DS_METADATA VARCHAR(100) unique not null, DS_DATA VARCHAR(100) unique not null)");
	    
	    //drop the table if it already exists
	    stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName); 	    
	    stmt.executeUpdate(query.toString());
	    
	    conn.commit();//end Transaction
	    closeRDBresources(); 
	    return true;
	}
	catch(Exception e){
	    try{
		LoggerUtils.logErrors(e);
		conn.rollback(); //if transaction fails rollback
		closeRDBresources(); 
	    }
	    catch(Exception ex){
		LoggerUtils.logErrors(ex);
	    }
	}
	return false;
    }

    /**
     * Create a Database table. It must be called after setTableName(String tableName)
     *
     * @param tableHeader a String array holding the names of the columns to be created.
     */
    public void createTable(String[] tableHeader)
    {
	try {
	    if (this.tableName == null) {
		log.error("setTableName() not called before calling this method");
		return;
	    }
	    
	    //will hold the query
	    StringBuffer query = new StringBuffer();
	    
	    //create the query
	    //create a uniqueid for each row
	    
	    query.append("uniqueID VARCHAR(255) PRIMARY KEY");
	    log.debug("table size: " + tableHeader.length);
	    for (int i = 0; i < tableHeader.length;i++){
		String th = tableHeader[i];
		if((th == null) || (th.trim().equals(""))){
		    log.debug("table header is: " + th);
		    StringBuffer errBuffer = new StringBuffer();
		    errBuffer.append("The table: ");
		    errBuffer.append(this.tableName);
		    errBuffer.append(" contains a blank column name.\n");
		    if(i != 0){
		    errBuffer.append(" This column comes after the column named: " + tableHeader[i-1]  + "\n");
			}
			if((i+1) < tableHeader.length){
			errBuffer.append( " and before "  + tableHeader[i + 1] + "\n");
			}
		    errBuffer.append("It is prefered that you  remove the column and restart the import application!!\n");
		    System.err.println(errBuffer.toString());
		    //throw new Exception(errBuffer.toString());
		}
		query.append( ", `");
		query.append(th);
		query.append("` ");
		//	query.append("VARCHAR(255)");
		query.append("TEXT");
	    }



	    conn.setAutoCommit(false);;
	    stmt = conn.createStatement();
	    
	    //drop the table if it already exists
	    stmt.executeUpdate("DROP TABLE IF EXISTS " + getTableName()); 	    
	    
	    stmt.executeUpdate("CREATE TABLE " + getTableName() + "( " +
			       query.toString() +
			       ")");
	    
	    conn.commit();//end Transaction
	    closeRDBresources(); 
	}
	catch (Exception e) {
	    try{
		//log.error("query: " + query.toString());
		LoggerUtils.logErrors(e);
		conn.rollback(); //if transaction fails rollback
		closeRDBresources(); 
		if (e instanceof SQLException) {
		    SQLException se = (SQLException) e;
		    log.error("SQLException: " + se.getMessage()); 
		    log.error("SQLState: " + se.getSQLState()); 
		    log.error("VendorError: " + se.getErrorCode()); 
		}
		System.err.println(e.getMessage());
	    }
	    catch(Exception ex) {
		LoggerUtils.logErrors(ex);
	    }
	}

    }
    private void rollBackTransaction(String tableName){
    }
    /**
     * Populate the table created in createTable(). It must be called after setTableName().
     *
     * @param row a list containg the data to populate the table. 
     */
    public boolean populateTable(List row) 
    {
	boolean bool = true;
   	try{
	    //start a transactionALL_EFG_RDB_TABLES
	    conn.setAutoCommit(false);
	    stmt = conn.createStatement();
	    Iterator rowList = row.iterator(); //get an iterator
	    
	    log.debug("Row Size: " + row.size());
	    
	    while (rowList.hasNext()) { //iterate through each row element
		String []currentRow =(String[])rowList.next();
		
		StringBuffer query = new StringBuffer();
		//Sets up the uniqueID
		query.append("\"");
		query.append(EFGUniqueID.getID());
		query.append("\"");
		for (int i = 0; i < currentRow.length;i++) {
		    query.append( ",");
		    query.append("\"");
		    String cur = currentRow[i].replace('\"', ' ');
		    query.append(cur.trim());
		    query.append("\"");
		}
		try{
		    if (query.length() > 0) {
			String stmtQuery = "INSERT INTO " + 
			    getTableName() +
			    " VALUES(" + 
			    query.toString() + 
			    ")";
			log.debug(stmtQuery);
			stmt.executeUpdate(stmtQuery);
		    }
		}
		catch(Exception eex){
		    
		    /* log.error("eex error: " + eex.getMessage());
		    if (eex instanceof SQLException) {
			SQLException se = (SQLException) eex;
			log.error("SQLException: " + se.getMessage()); 
			log.error("SQLState: " + se.getSQLState()); 
			log.error("VendorError: " + se.getErrorCode()); 
			}*/
		   
		}
	    }
	    conn.commit();
	    closeRDBresources();
	}
	catch(Exception e){
	    bool = false;
	    try{
		System.err.println(e.getMessage());
		LoggerUtils.logErrors(e);
		conn.rollback(); 
		closeRDBresources();
	    }
	    catch(Exception ex){
		LoggerUtils.logErrors(ex);
	    }
	}
	return bool;
    }
    
    /**
     * Inserts the pair(data source file name, Meta data source file name) into the efg
     * mapping table "ALL_EFG_RDB_TABLES"
     *
     * @param datafn String DataSource data file Name
     * @param metadatafn String DataSource Metadata file name
     * @return true if insertion is successful false otherwise
     */
    public boolean setPair(String datafn, String metadatafn)
    {
   
	if ((datafn == null) && (metadatafn == null)) {
	    log.error("datafn or matadatafn is null");
	    System.err.println("Please specify a data and a metadata file to process");
	    System.err.println("Import to relational database(e.g MySQL) could not be done!!");
	    return false;
	}

	//If an error occured while checking for existence of pair return false;
	if (isCheckPair(datafn, metadatafn)) {
	    return false;
	}

	StringBuffer query = new StringBuffer("INSERT INTO ");
	query.append(System.getProperty("ALL_EFG_RDB_TABLES"));
	query.append(" VALUES( ");
	query.append("\"");
	query.append(metadatafn.replace('\"', ' '));
	query.append("\"");
	query.append(",");
	query.append("\"");
	query.append(datafn.replace('\"', ' '));
	query.append("\"");

	query.append(")");
	log.debug("Query: " + query.toString());
	try{
	    //start Transaction
	    conn.setAutoCommit(false);;
	    stmt = conn.createStatement();
	    stmt.executeUpdate(query.toString());
	    conn.commit();
	    //end Transaction
	    closeRDBresources();
	    return true;
	} catch(Exception e){
	    try{
		System.err.println(e.getMessage());
		LoggerUtils.logErrors(e);
		conn.rollback(); //if transaction fails rollback 
		closeRDBresources();
	    }
	    catch(Exception ex){
		LoggerUtils.logErrors(ex);
	    }
	}
	return false;
    } 
    /**
     * Returns the Hashtable (mapping taxon's name to legal name) associated to with  
     * the supplied name that matches the DS_DATA column of the efg mapping table.
     * "ALL_EFG_RDB_TABLES"
     *
     * @param tableName the name to be used in the "where" clause of the query
     * @return a Hashtable containing the mapping for the supplied tableName
     */
    public synchronized Hashtable getMapping(String tableName)
    {
	return null;
	//return (Hashtable) getMappingFromDB(tableName);
    }
   /**
     * Change the table name to the current one. It should be set before any changes made 
     * to the database table.
     *
     * @param tableName the Database table name to be queried
     */
    public synchronized void setTableName(String tableName)
    {
	this.tableName = tableName;
    }

    /**
     * Retrieve the table name.
     *
     * @return the current database table name.
     */
    public synchronized String getTableName()
    {
	return this.tableName;
    }

    /**
     * Closes ResultSet, PreparedStatement and Statement objects. 
     *
     * @throws Exception
     */
    public synchronized void closeRDBresources() throws Exception
    {
	if (rst != null) {
	    rst.close();
	}
	if (stmt != null) {
	    stmt.close();
	}
	if (statement != null) {
	    statement.close();
	}
    }
}

//$Log$
//Revision 1.2  2006/01/26 04:20:46  kasiedu
//no message
//
//Revision 1.1.1.1  2006/01/25 21:03:42  kasiedu
//Release for Costa rica
//
//Revision 1.1.1.1  2003/10/17 17:03:05  kimmylin
//no message
//
//Revision 1.10  2003/08/20 18:45:41  kimmylin
//no message
//







































