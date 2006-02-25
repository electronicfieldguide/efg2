/**
 * EFGDatasourceObjectLists.java
 *
 *
 * Created: Sun Feb 19 09:22:49 2006
 *
 * @author <a href="mailto:kasiedu@ccs.umb.edu">Jacob K. Asiedu</a>
 * @version 1.0
 */
package project.efg.Import;
import project.efg.efgInterface.*;
import java.util.*;
import java.sql.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class EFGDatasourceObjectList implements EFGDatasourceObjectListInterface {
    private ArrayList lists;
    private DBObject dbObject;
    private Connection conn = null;
    private Statement stmt = null;
    static Logger log = null;
    
    static{
	try{
	    log = Logger.getLogger(EFGDatasourceObjectList.class); 
	}
	catch(Exception ee){
	}
    }
    public EFGDatasourceObjectList(DBObject dbObject){
	this.dbObject = dbObject;
	this.lists = new ArrayList();
	this.populateLists();
    }
    private Connection getConnection()throws Exception{
	return EFGRDBImportUtils.getConnection(
					       this.dbObject.url, 
					       this.dbObject.userName, 
					       this.dbObject.password
					       );
    }
    private ResultSet executeQuery(String query)throws Exception{
	
	try{
	    this.conn = this.getConnection();
	    this.stmt = conn.createStatement();
	    return stmt.executeQuery(query.toString());
	}
	catch(Exception ee){
	    log.error(ee.getMessage());
	}
	return null;
    }
    private void populateLists(){
	ResultSet rs = null;
	try{
	    StringBuffer query = new StringBuffer();
	    query.append("SELECT DS_METADATA, DS_DATA, DISPLAYNAME FROM ");
	    query.append(System.getProperty("ALL_EFG_RDB_TABLES"));
	    query.append(" ORDER BY DISPLAYNAME");
	    rs = executeQuery(query.toString());
	    if(rs != null){
		while(rs.next()){
		    String name=rs.getString("DS_DATA");
		    String md_name=rs.getString("DS_METADATA");
		    String display=rs.getString("DISPLAYNAME");
		    this.lists.add(new EFGDatasourceObject(name,md_name,display));
		}
	    }
	}
	catch(Exception ee){
	    log.error(ee.getMessage());
	}
	finally{
	    try{
		if(rs != null){
		    rs.close();
		}
		if(this.stmt != null){
		    this.stmt.close();
		}
		if(this.conn != null){
		    this.conn.close();
		}
	    }
	    catch(Exception ee){
		
	    }
	}
    }
    /**
     *@return an iterator over the members of this lists.
     */
    public Iterator getEFGDatasourceObjectListIterator(){
	return lists.iterator();
    }
    public boolean contains(EFGDatasourceObjectInterface datasource){
	return this.lists.contains(datasource);
    }
    /**
     *@retur true if the display name for the object is changed in the list
     */
    public boolean changeDisplayName(EFGDatasourceObjectInterface datasource){
	try{
	    int index = this.lists.indexOf(datasource);
	    if(index > -1){
		EFGDatasourceObjectInterface obj = (EFGDatasourceObjectInterface)this.lists.get(index);
		String displayName = datasource.getDisplayName();
		obj.setDisplayName(displayName);
		String dataName = datasource.getDataName();
		String metadataName = datasource.getMetadataName();	    
		String efgRDBTable = System.getProperty("ALL_EFG_RDB_TABLES");
		
		String query =  "UPDATE " + efgRDBTable +   
		    " SET DISPLAYNAME = '" + displayName + 
		    "' " + "WHERE DS_DATA = '" + dataName +
		    "' and DS_METADATA ='" + metadataName + "'";
		this.conn = this.getConnection();
		this.conn.setAutoCommit(false);;
		this.stmt = conn.createStatement();
		//will hold the query
		this.stmt.executeUpdate(query);
		this.conn.commit();//end Transaction
		if(this.stmt != null){
		    this.stmt.close();
		}
		if(this.conn != null){
		    this.conn.close();
		}
		return true;
	    }
	}
	catch(Exception ee){
	    log.error(ee.getMessage());
	    try{
		this.conn.rollback();
	    }
	    catch(Exception sss){}
	}
	finally{
	    try{
		if(this.stmt != null){
		    this.stmt.close();
		}
		if(this.conn != null){
		    this.conn.close();
		}
	    }
	    catch(Exception ee){
		
	    }
	}
	return false;
    }
    /**
     *Add an object to the database
     *@param datasource - The datasource object to add 
     *@return true if this datasource was successfully added, false otherwise
     */
    public boolean addEFGDatasourceObject(EFGDatasourceObjectInterface datasource){
	
	int index = this.lists.indexOf(datasource);
	EFGImportTool tool = new EFGImportTool();
	String[] args = {"2", datasource.getDataName(), datasource.getMetadataName()};
	tool.process(args);
	
	if(index == -1){
	    this.lists.add(datasource);
	}
	//find some way to signal errors
	//re-write import code
	return true;
    }
    /**
     * 
     * @param datafn the data fileName to check .
     * @param metadatafn the metadata fileName to check .
     * @return true if delete was successful.
     */
    private boolean deleteTables(String datafn, String metadatafn)
    {
	if ((datafn == null) || (metadatafn == null)) {
	    log.error("Either or both datafn and metadatafn is null");
	    return false;
	}
	ResultSet rst = null;
	PreparedStatement statement = null;
	ResultSet rs = null;
	Statement stmt2=null;
	StringBuffer query = null;
	try{
	    this.conn = this.getConnection();
	    this.conn.setAutoCommit(false);
	    
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
		this.stmt = conn.createStatement();
		//this is a MySQL command
		//read from properties file
	
		if(System.getProperty("MySQL") != null){
		    rs = this.stmt.executeQuery("SHOW TABLES");
		}
		else{
		    DatabaseMetaData dbm = conn.getMetaData();
		    String types[] = { "TABLE" };
		    rs = dbm.getTables(null, null, "", types);
		}
		String tbName = null;
		while (rs.next()) {
		    stmt2 = conn.createStatement();
		    tbName = rs.getString("Tables_in_efg");
		    if ((tbName.toUpperCase()).indexOf(datafn.toUpperCase()) != -1){
			stmt2.execute("DROP TABLE IF EXISTS " + tbName);
		    }
		}
	    }
	    this.stmt.execute("DROP TABLE IF EXISTS " + metadatafn);
	    String qr = "DELETE FROM " + efgRDBTable + " WHERE DS_DATA = '" + datafn + "';";
	    this.stmt.execute(qr);
	    conn.commit();
	    return true;
	}
	catch(Exception ex){
	    if(this.conn !=null){
		try{
		    this.conn.rollback();
		}
		catch(Exception sss){
		    log.error(sss.getMessage());
		    System.err.println("Fatal exception : " + sss.getMessage());
		}
	    } 
	}
	finally {
	    try{
		if(rst != null){
		    rst.close();
		}
		if(rs != null){
		    rs.close();
		}
		
		if(stmt2 != null){
		    stmt2.close();
		}
		if(statement != null){
		    statement.close();
		}
		if(this.stmt != null){
		    this.stmt.close();
		}
		if(this.conn != null){
		    this.conn.close();
		}
	    }
	    catch(Exception e){
		
	    }
	}
	return false;
    }
    /**
     * Remove an object from the database
     *@param datasource - The datasource object to remove
     *@return true if the datasource object was part of the list and was removed , false otherwise.
     */    
    public boolean removeEFGDatasourceObject(EFGDatasourceObjectInterface datasource){
	int index = this.lists.indexOf(datasource);
	if(index > -1){
	    String displayName = datasource.getDisplayName();
	    String dataName = datasource.getDataName();
	    String metadataName = datasource.getMetadataName();
	    boolean bool = this.deleteTables(dataName,metadataName);
	    if(bool){
		this.lists.remove(index);
	    }
	    return bool;
	}
	return false;
    }
}// EFGDatasourceObject

