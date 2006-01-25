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

package project.efg.efgInterface;

import project.efg.util.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.regex.*;
/**
 * This class gives access to an iterator over the datasource names in the EFG
 * and a utility function for converting those names to readable names.
 */
public class RDBDataSourceHelper implements EFGDataSourceHelper
{
    /**
     * Construct a new EFGRDBDSHelper object.<BR>
     * <B>Note:</B> You must call cleanup() when you have completed
     * your use of this object.
     */
    public RDBDataSourceHelper() {}

    /**
     * Take a computer-readable String and return a more human-readable
     * String.  Inserts a space before capital letters in position
     * greater than zero.
     *
     * @param compString the String to modify
     */
    public String makeReadable(String compString)
    {
        StringBuffer sb = new StringBuffer(compString);

        //Add spaces before capital letters (going from end to begin)
        for (int i = sb.length() - 1; i > 0; i--) {
            if (Character.isUpperCase(sb.charAt(i)) &&
                !Character.isSpaceChar(sb.charAt(i-1)))
                sb.insert(i, ' ');
        }
        return sb.toString();
    }
  
    /**
     * Return a List of the datasource names in the EFG database.
     *
     * @return a list ot data source names.
     */
    public List getDSNames()
    {
	String query = "select DS_DATA from " + EFGRDBUtils.getTableName("ALL_EFG_RDB_TABLES");
	List results = new ArrayList();
	Connection conn = null;
	Statement stmt = null;
	ResultSet rst = null;

	// Connect to the database
	try {
	    conn = EFGRDBUtils.getConnection();
	    stmt = conn.createStatement();
	    rst = stmt.executeQuery(query);
	    while (rst.next()) {
		results.add(rst.getString(1));
	    }
	} catch (SQLException ex) {
	    LoggerUtilsServlet.printSQLErrors(ex);
	}
	catch(Exception e){
	    LoggerUtilsServlet.logErrors(e);
	}
	finally{
	    try{
		if(rst != null){
		    rst.close();
		}
		if(stmt != null){
		    stmt.close();
		}
		if(conn != null){
		    
		    EFGRDBUtils.closeConnections(conn);
		}
	    }
	    catch(Exception exc){
	
	    }
	}
	return results;
    }
    /**
     *return a metadatasource name given a datasource name.
     *@param a datasource name
     *@return the metadatasource name of a given datasource name.
     */
    private String getMetadatasource(String datasource){
	int index = datasource.length()-4;//subtract "Data" and add "Info";
	String newString = datasource.substring(0,index);
	return (newString + "Info");
    }
    /**
     * Parse the given comma separated or 'or' states into states
     * and return an array of the states
     *@param states - The statesto parse
     *@return a String array of states
     */
    protected String[] parseMultiples(String states){
	String patternStr = "([,]|([ ]+(or)[ ]))";
	return states.split(patternStr, -1);
    }
    /**
     *Allows a character to have more than one state 
     *@param states - The current state to be parsed ( Contains ' or ' or ',' separators )
     *@param list - The list that will contain the parsed states.
     */
    protected void parseSearchable(String states, ArrayList list){
	String[] statesArray = this.parseMultiples(states);
	for(int j=0; j < statesArray.length;j++){
	    String current = statesArray[j].trim();
	    if(current.indexOf("(") > -1){
		current = current.replaceAll(EFGImportConstants.LEFTPARENSEP,"");
	    }
	    if(current.indexOf(")") > -1){
		current = current.replaceAll(EFGImportConstants.RIGHTPARENSEP,"");
	    }
	    list.add(current);
	}
    }
    /**
     * Returns a Set containing the names of the fields with 'numericValues' or 'numericRange' values.
     *
     * @param String the name of the dataSource
     * @return a Set containing DB column names with numeric or numericRange types.
     */
    public Set getNumericFields(String dataSourceName){
	Connection conn = null;  
	Statement stmt = null;
	ResultSet rs = null;
	
	
	HashSet set = new HashSet();
	
	try {
	    String metadatasourceName = this.getMetadatasource(dataSourceName);
	    StringBuffer queryBuffer  = new StringBuffer();

	    queryBuffer.append("SELECT distinct ");
	    queryBuffer.append(EFGImportConstants.NAME);
	    queryBuffer.append(" FROM ");
	    queryBuffer.append(metadatasourceName);
	    queryBuffer.append(" where ");
	    queryBuffer.append(EFGImportConstants.NUMERIC);
	    queryBuffer.append("= '");
	    queryBuffer.append(EFGImportConstants.YES);
	    queryBuffer.append("' or ");
	    queryBuffer.append(EFGImportConstants.NUMERICRANGE);
	    queryBuffer.append("='");
	    queryBuffer.append(EFGImportConstants.YES);
	    queryBuffer.append("'");
	    
	    conn = EFGRDBUtils.getConnection();
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(queryBuffer.toString());
	    
	    while(rs.next()){
		String name=rs.getString(EFGImportConstants.NAME);
		if(name != null){
		    set.add(name.trim());
		}
	    }
	    
	} catch (Exception e) {
	    LoggerUtilsServlet.logErrors(e);
	}
	finally{
	    try{
		if(conn != null){
		    EFGRDBUtils.closeConnections(conn);
		}
		if(stmt != null){
		    stmt.close();
		}
		if(rs != null){
		    rs = null;
		}
	    }
	    catch(Exception exxx){}
	}
	return set;
    }
    /**
     *Create an EFGField object from a row of a result set
     *@param rs - The current result set object
     *@return an EFGField object, built from the current result set.
     */
    protected EFGField makeInfoField(ResultSet rs){
	EFGField field = new EFGField();
	try{
	    String name = rs.getString(EFGImportConstants.NAME);
	    field.setName(name);
	    String legalName = rs.getString(EFGImportConstants.LEGALNAME);
	    field.setLegalName(legalName);
	    String parserable=rs.getString(EFGImportConstants.PARSERABLE);
	    field.setParserable(parserable);
	    String holdsMultipleValues =rs.getString(EFGImportConstants.HOLDSMULTIPLEVALUES);
	    field.setHoldsMultipleValues(holdsMultipleValues);
	    String dataType = rs.getString(EFGImportConstants.DATATYPE);
	    field.setDataType(dataType);
	    String javaType = rs.getString(EFGImportConstants.JAVATYPE);
	    field.setJavaType(javaType);
	    String numericValue=rs.getString(EFGImportConstants.NUMERIC);
	    field.setNumericValue(numericValue);
	    String ordered=rs.getString(EFGImportConstants.ORDERED);
	    field.setOrdered(ordered);
	    String numericRange=rs.getString(EFGImportConstants.NUMERICRANGE);
	    field.setNumericRange(numericRange);
	    String searchable=rs.getString(EFGImportConstants.SEARCHABLE);
	    field.setSearchable(searchable);
	    String speciesPageData=rs.getString(EFGImportConstants.SPECIESPAGEDATA);
	    field.setSpeciesPageData(speciesPageData);
	    String weight = rs.getString(EFGImportConstants.WEIGHT);
	    field.setWeight(new Double(weight));
	}
	catch(Exception ee){
	    LoggerUtilsServlet.logErrors(ee);
	}
	return field;
    }
    /**
     * Query the EFG tables for fields that are neither efgLists nor mediaresources.
     * Returns a Hashtable mapping the  field names to an EFGField object
     *
     * @param dataSourceName the name of the data source
     * @return a Hashtable mapping selected fields with EFGField objects.
     */
    public Map getFieldsInfo(String dataSourceName){
	Connection conn = null;  
	Statement stmt = null;
	ResultSet rs = null;
	
	TreeMap fields = new TreeMap();
	
	try {
	    String metadatasourceName = this.getMetadatasource(dataSourceName);
	    StringBuffer buffer = new StringBuffer();
	    
	    buffer.append("SELECT * FROM ");
	    buffer.append(metadatasourceName);
	    /*buffer.append(" where ");
	    buffer.append(EFGImportConstants.DATATYPE);
	    buffer.append(" <> '");
	    buffer.append(EFGImportConstants.MEDIARESOURCETYPE);
	    buffer.append("' and ");
	    buffer.append(EFGImportConstants.DATATYPE);
	    buffer.append(" <> '");
	    buffer.append(EFGImportConstants.EFGLISTTYPE);
	    buffer.append("'");*/
	    
	    conn = EFGRDBUtils.getConnection();
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(buffer.toString());
	    
	    while(rs.next()){
		String name=rs.getString(EFGImportConstants.NAME);
		EFGField infoField = makeInfoField(rs);
		fields.put(name.trim(),infoField);
	    }
	    if(stmt != null){
		stmt.close();
	    }
	    if(rs != null){
		rs = null;
	    }
	    if(conn != null){
		try{
		    EFGRDBUtils.closeConnections(conn);
		}
		catch(Exception xxx){}
	    }
	}
	catch (Exception e) {
	    LoggerUtilsServlet.logErrors(e);
	}
	finally{
	    try{
		if(conn != null){
		    EFGRDBUtils.closeConnections(conn);
		}
		if(stmt != null){
		    stmt.close();
		}
		if(rs != null){
		    rs = null;
		}
	    }
	    catch(Exception exxx){}
	}
	return fields;
    }
    public Map getMediaResourceFields(String dataSourceName){
	Connection conn = null;  
	Statement stmt = null;
	ResultSet rs = null;
	TreeMap searchFields = new TreeMap(new EFGObjectComparator());
	
	try {
	    String metadatasourceName = this.getMetadatasource(dataSourceName);
	    StringBuffer buffer = new StringBuffer();
	    
	    buffer.append("SELECT ");
	    buffer.append(EFGImportConstants.WEIGHT);
	    buffer.append(" , ");
	    buffer.append(EFGImportConstants.LEGALNAME);
	    buffer.append(" , ");
	    buffer.append(EFGImportConstants.NAME);
	    buffer.append(" FROM ");
	    buffer.append(metadatasourceName);
	    buffer.append(" where ");
	    buffer.append(EFGImportConstants.DATATYPE);
	    buffer.append("='");
	    buffer.append(EFGImportConstants.MEDIARESOURCETYPE);
	    buffer.append("'");
	    
	    conn = EFGRDBUtils.getConnection();
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(buffer.toString());
	    
	    while(rs.next()){
		String name=rs.getString(EFGImportConstants.NAME);
		String weight=rs.getString(EFGImportConstants.WEIGHT);
		String legalName = rs.getString(EFGImportConstants.LEGALNAME);
		EFGObject efgObject = new EFGObject();
		
		efgObject.setName(name);
		efgObject.setWeight(weight);
		TreeSet tSet = new TreeSet();
		searchFields.put(efgObject,tSet);
	    }
	    if(conn != null){
		try{
		    EFGRDBUtils.closeConnections(conn);
		}
		catch(Exception xxx){}
	    }
	    
	} catch (Exception e) {
	    LoggerUtilsServlet.logErrors(e);
	}
	finally{
	    try{
		if(conn != null){
		    EFGRDBUtils.closeConnections(conn);
		}
		if(stmt != null){
		    stmt.close();
		}
		if(rs != null){
		    rs = null;
		}
	    }
	    catch(Exception exxx){}
	}
	return searchFields;
    }
    /**
     * Query the EFG tables for the searchable fields for specified data source
     * name and return a Hashtable mapping the searchable field to its values.
     *
     * @param dataSourceName the name of the data source
     * @return a Hashtable mapping searchable fields to its values
     */
    public Map getSearchable(String dataSourceName) 
    {
	Connection conn = null;  
	Connection conn1 = null;  
	Statement stmt = null;
	ResultSet rs = null;
	Statement stmt1 = null;
	ResultSet rs1 = null;
	
	Hashtable table = new Hashtable();
	TreeMap searchFields = new TreeMap(new EFGObjectComparator());
	
	try {
	    String metadatasourceName = this.getMetadatasource(dataSourceName);
	    StringBuffer buffer = new StringBuffer();
	    
	    buffer.append("SELECT ");
	    buffer.append(EFGImportConstants.WEIGHT);
	    buffer.append(" , ");
	    buffer.append(EFGImportConstants.LEGALNAME);
	    buffer.append(" , ");
	    buffer.append(EFGImportConstants.NAME);
	    buffer.append(" FROM ");
	    buffer.append(metadatasourceName);
	    buffer.append(" where ");
	    buffer.append(EFGImportConstants.SEARCHABLE);
	    buffer.append("='");
	    buffer.append(EFGImportConstants.YES);
	    buffer.append("'");
	    
	    conn = EFGRDBUtils.getConnection();
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(buffer.toString());
	    conn1 = EFGRDBUtils.getConnection();
	
	    while(rs.next()){
		String name=rs.getString(EFGImportConstants.NAME);
		String weight=rs.getString(EFGImportConstants.WEIGHT);
		String legalName = rs.getString(EFGImportConstants.LEGALNAME);
		EFGObject efgObject = new EFGObject();
		
		efgObject.setName(name);
		efgObject.setWeight(weight);
		//make query to data table and get data. Put in TreeSet.
		StringBuffer dataQuery = new StringBuffer();
		dataQuery.append("SELECT ");
		dataQuery.append(legalName.trim());
		dataQuery.append(" FROM ");
		dataQuery.append(dataSourceName);
		try{
		    stmt1 = conn1.createStatement();
		    rs1 = stmt1.executeQuery(dataQuery.toString());
		    TreeSet tSet = new TreeSet();
		    while(rs1.next()){
			String states = rs1.getString(legalName.trim());
			//parse val
			ArrayList list = new ArrayList();
			parseSearchable(states,list);
			for(int i = 0; i < list.size();i++){
			    String val = (String)list.get(i);
			    if(val != null){
				if(!val.trim().equals("")){
				    tSet.add(val.trim());
				}
			    }
			}
		    }
		    searchFields.put(efgObject,tSet);
		    if(rs1 != null){
			rs1.close();
		    }
		    if(stmt1 != null){
			stmt1.close();
		    }
		}
		catch(Exception ee){
		    LoggerUtilsServlet.logErrors(ee);
		}
	    }
	    if(conn != null){
		try{
		    EFGRDBUtils.closeConnections(conn);
		}
		catch(Exception xxx){}
	    }
	    if(conn1 != null){
		try{
		    EFGRDBUtils.closeConnections(conn1);
		}
		catch(Exception xxxe){}
	    }
	    
	} catch (Exception e) {
	    LoggerUtilsServlet.logErrors(e);
	}
	finally{
	    try{
		if(conn != null){
		    EFGRDBUtils.closeConnections(conn);
		}
		if(conn1 != null){
		    EFGRDBUtils.closeConnections(conn1);
		}
		if(stmt != null){
		    stmt.close();
		}
		if(rs != null){
		    rs = null;
		}
		if(stmt1 != null){
		    stmt1.close();
		}
		if(rs1 != null){
		    rs1 = null;
		}
	    }
	    catch(Exception exxx){}
	}
	/*	for (Iterator iter = searchFields.keySet().iterator(); iter.hasNext();){ 
	    EFGObject entry = (EFGObject)iter.next();
	    TreeSet treeVal = (TreeSet)searchFields.get(entry);
	    table.put(entry.toString(),treeVal);
	}
	return table;*/
	return searchFields;
    }

} 
//$Log$
//Revision 1.1  2006/01/25 21:03:42  kasiedu
//Initial revision
//
//Revision 1.1.1.1  2003/10/17 17:03:05  kimmylin
//no message
//
//Revision 1.2  2003/08/20 18:45:42  kimmylin
//no message
//
//Revision 1.1  2003/08/01 20:45:36  kasiedu
//*** empty log message ***
  //
  //Revision 1.2  2003/08/01 18:39:41  kimmylin
  //no message
  //
  //Revision 1.1.1.1  2003/07/30 17:03:58  kimmylin
  //no message
  //
  //Revision 1.1.1.1  2003/07/18 21:50:16  kimmylin
  //RDB added 
  //
