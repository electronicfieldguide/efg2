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
import project.efg.digir.*;
//import project.efg.db.*;
import project.efg.servlet.*;

import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.jdom.Element;
import org.jdom.Document;


import java.net.*;
import org.jdom.*;
import org.xml.sax.InputSource;
import java.text.SimpleDateFormat;
import java.text.*;
import java.util.*;
import org.jdom.input.SAXBuilder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * This class handles requests to Relational DB and return an EFGDocument of
 * one or more taxa. 
 */
public class RDBQueryHandler implements EFGQueryHandler, EFGImportConstants 
{
    protected Collection specialParams; //a collection to hold special parameters 
    
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(RDBQueryHandler.class); 
	}
	catch(Exception ee){
	}
    }
    /**
     * Constructor.
     */
    public RDBQueryHandler()
    {
	addSpecialParams();
    }

    /**
     * Add special parameters to a list so that special parameters can be
     * excluded when building the query.
     */
    protected void addSpecialParams()
    {
	//Construct collection of special parameter names
	specialParams = new ArrayList();
	specialParams.add(EFGImportConstants.DATASOURCE_NAME);
	specialParams.add(EFGImportConstants.MAX_DISPLAY);
	specialParams.add(EFGImportConstants.DISPLAY_FORMAT);
    }
    protected synchronized Hashtable getNameMapping(String metadataSource){
	Hashtable mapping = new Hashtable();
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String metaQuery = "SELECT * FROM " + metadataSource;
	try{ 
	    conn = EFGRDBUtils.getConnection();
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(metaQuery);
	    while (rs.next()) {
		String legalName = rs.getString(EFGImportConstants.LEGALNAME);
		String name = rs.getString(EFGImportConstants.NAME);
		mapping.put(name,legalName);
	    }
	    if(rs != null){
		rs.close();
	    }
	    if( stmt != null){
		stmt.close();
	    }
	    if(conn != null){
		try{
		    EFGRDBUtils.closeConnections(conn);
		}
		catch(Exception xxxx){}
	    }
	}
	catch(Exception mex){
	    LoggerUtilsServlet.logErrors(mex);
	}
	finally{
	    try{
		if(rs != null){
		    rs.close();
		}
		if( stmt != null){
		    stmt.close();
		}
		if(conn != null){
		    EFGRDBUtils.closeConnections(conn);
		}
	    }
	    catch(Exception nnn){
		//	LoggerUtilsServlet.logErrors(nnn);
	    }
	}
	return mapping;
    }
    protected String getSQLWhereString(String digirQuery, String metadataSource, 
				       HashSet set, 
				       Hashtable table){
	
	
	String arr[] = (digirQuery).split("\\s");
        StringBuffer queryString = new StringBuffer();
	Hashtable mapping = this.getNameMapping(metadataSource);

	for(int i = 0 ; i < arr.length; i++){
            String keyword = (String) EFGServletUtils.getKeyWordValue(arr[i]);
            if(keyword != null){//if it is a keyword in the properties file
		if((keyword.trim()).equalsIgnoreCase("and") || 
		   (keyword.trim()).equalsIgnoreCase("or") 
		   || (keyword.trim()).equalsIgnoreCase("or not") || 
		   (keyword.trim()).equalsIgnoreCase("and not")){//it is a lop
		    queryString.append(" " + keyword + " ");
		}
		else { //it is a cop operator
		    String name = arr[i-1];
		    if(name.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1){
			name = name.replaceAll(EFGImportConstants.SERVICE_LINK_FILLER," ");
		    }
		    String legalName = (String) mapping.get(name);
		    if ((legalName == null) || ("".equals(legalName.trim()))){
			continue;
		    }
		    
		    queryString.append("( " + legalName + " "); 
		    if(set.contains(legalName.trim())){
			queryString.append(" LIKE ");
			if((i + 1) < arr.length){//get the right column name from the mapping table
			    String pVal = arr[i + 1];
			    if(pVal.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1){
				pVal = pVal.replaceAll(EFGImportConstants.SERVICE_LINK_FILLER," ");
			    }
			    String getValue = (String)table.get(legalName);
			    if(getValue != null){
				getValue = getValue + "," + pVal;
			    }
			    else{
				getValue = pVal;
			    }
			    table.put(legalName, getValue);
			    queryString.append("'%')");//value
			}
		    }
		    else{
			queryString.append(keyword.trim() + " ");
			if((i + 1) < arr.length){//get the right column name from the mapping table
			    String pVal = arr[i + 1];
			    if(pVal.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1){
				pVal = pVal.replaceAll(EFGImportConstants.SERVICE_LINK_FILLER," ");
			    }
			    String getValue = (String)table.get(legalName);
			    if(getValue != null){
				getValue = getValue + "," + pVal;
			    }
			    else{
				getValue = pVal;
			    }
			    table.put(legalName.trim(), getValue.trim());
			    queryString.append("'" + pVal.trim() + "')");//value
			}			
		    }
		}
            }
            else{
		if( (arr[i].trim().equals("(")) || (arr[i].trim().equals(")"))){
		    String toAppend = arr[i];
		    
		     if(toAppend.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1){
			 toAppend = toAppend.replaceAll(EFGImportConstants.SERVICE_LINK_FILLER," ");
		     }
		     queryString.append(toAppend);
		}
            }
	}
	log.debug("query: " + queryString.toString());
	return queryString.toString();
    }
    protected synchronized String getMetadatasource(String datasource){

	int index = datasource.length()-4;//subtract "Data" and add "Info";
	String newString = datasource.substring(0,index);
	return (newString + EFGImportConstants.METAFILESUFFIX);
    }
    /**
     * Handles DiGIR queries
     *
     * @param req the servlet request object
     * @param res the servlet response object
     * @param sc the ServletConfig object that holds that init parameters
     *@param obj which is an instance of DigirParserHandler
     *@param digirQuery a parsed digir query
     * @return an EFGDocument of the result taxa
     */
    protected Object buildResult(HttpServletRequest req, HttpServletResponse res,
			       ServletConfig s,DigirParserHandler dph){
	
	Document efgDocument = null;
	String digirQuery = processDigirRequest(dph);
	String sqlWhereString = null;
	String query = null;
	int count = 0; //will keep a running count if Digir request is an Inventory type
	boolean inventory = false; //becomes true when the request is an inventory type
	List dataSources = dph.getRequestedDataSources(); //get list of DataSources if any
    
	if(dataSources.size()==0){//means user did not specify any dataSources    
	    dataSources = this.getAllDataSourceNames();
	}
	String condClause = dph.getConditionalClause();
	String sqlSelect=null;
      
	if((condClause != null)&&(!condClause.equals(""))){
	    inventory = true;
	}
      
	Iterator dsNameIter = dataSources.iterator(); 
	SQL2XML sql2xml = null;
	
	while (dsNameIter.hasNext()) {
	    String dataSource = (String)dsNameIter.next();
	    String metadataSource = this.getMetadatasource(dataSource);
	    HashSet set =  this.getMultiple(metadataSource);
	    Hashtable table = new Hashtable();
	    sqlWhereString = getSQLWhereString(digirQuery, metadataSource,set,table);
	    
	    if(!inventory) {
		if (sqlWhereString != null && !sqlWhereString.equals("")){
		    if(sql2xml == null){
			sql2xml = new SQL2XML();
		    }
		    efgDocument = createSQL(sql2xml,sqlWhereString,dataSource,metadataSource,table);
		}
	    }
	    else{
		count = count +  getInventoryResponse(sqlWhereString,dataSource,metadataSource,condClause);
	    }
	}
	if(!inventory){
	    return efgDocument.getRootElement();
	}
	return new Integer(count);
    }
    /**
     * Parses and Processes all DiGIR requests
     *
     *@param DigirParserHandler which is an implementation of a SAXParser Handler
     *@return a String containing the DiGIR query
     */
    public String processDigirRequest(DigirParserHandler dph)
    {
	log.debug("Inside Digir request");
	Stack stack = dph.getStack();
	StringBuffer buf = new StringBuffer();
    
	while (!stack.empty()){
	    EFGString efg = (EFGString)stack.pop();
	    if(efg != null){
		buf.append(efg.toString());
	    }
	}
	log.debug("Inside Digir request: " + buf.toString());
	return buf.toString();
    }
    
    //kimmy find a better name
    protected Document createSQL(SQL2XML sql2xml,
				 String sqlWhereString,
				 String dataSource, 
				 String metadataSource,
				 Hashtable table){

	String dataQuery = null;
	String sqlSelect = "SELECT * FROM " ;  
	if((sqlWhereString != null) &&(!"".equals(sqlWhereString))){
	    dataQuery = sqlSelect + dataSource + " where " + sqlWhereString;
	}
	else{
	    dataQuery = sqlSelect + dataSource;
	}
	String metaQuery = "SELECT * FROM " + metadataSource;
    	return buildEFGDocument(sql2xml,dataQuery,metaQuery,table,true);
    }
    /**
     * Fix this to handle the instances where we have to get the mapping to get the right column names
     */
    public synchronized int getInventoryResponse(String sqlWhereString,
						 String dataSource,
						 String metadataSource,
						 String condClause){
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	if (sqlWhereString == null || sqlWhereString.equals("")){
	    return 0;
	}
	Hashtable ht = this.getNameMapping(metadataSource);
	int count = 0;
	try{
	    if(condClause.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1){
		condClause = condClause.replaceAll(EFGImportConstants.SERVICE_LINK_FILLER," ");
	    }
	    String legalName = (String)ht.get(condClause);
	    if(legalName == null){
		return count;
	    }
	    String sqlSelect = "SELECT count(distinct " + legalName +") as count FROM " + dataSource + " where ";
	    String constantClause = legalName + " <>'' and " + legalName + " is not null";
	    String query = null;
	    if((sqlWhereString != null) &&(!"".equals(sqlWhereString))){
		query = sqlSelect + sqlWhereString + " and " + constantClause;
	    }
	    else{
		query = sqlSelect + constantClause;
	    }  
	    conn = EFGRDBUtils.getConnection();
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(query);
	    while (rs.next()) {
		count = rs.getInt("count");
		break;
	    }
	    if(rs != null){
		rs.close();
	    }
	    if( stmt != null){
		stmt.close();
	    }
	    if(conn != null){
		try{
		    EFGRDBUtils.closeConnections(conn);
		}
		catch(Exception ee){}
	    }
	} catch (SQLException ex) {
	    LoggerUtilsServlet.logErrors(ex);
	    log.error("SQLException: " + ex.getMessage()); 
	    log.error("SQLState: " + ex.getSQLState()); 
	    log.error("VendorError: " + ex.getErrorCode()); 
	} catch (Exception e) {
	    LoggerUtilsServlet.logErrors(e);
	}
	finally{
	    try{
		if(rs != null){
		    rs.close();
		}
		if( stmt != null){
		    stmt.close();
		}
		if(conn != null){
		    EFGRDBUtils.closeConnections(conn);
		}
	    }
	    catch(Exception ee){
		log.error(ee.getMessage());
	    }
	}
	return count;
    }
    /**
     * Handle the request and return an EFGDocument of the taxa matching the request.
     *
     * @param req the servlet request object
     * @param res the servlet response object
     * @param sc the ServletConfig object that holds that init parameters
     * @return an EFGDocument of the result taxa
     */
    public Element buildResult(HttpServletRequest req, HttpServletResponse res, 
			       ServletConfig sc)
    {
	log.debug("About to process request");
	String currentDatabase = sc.getInitParameter(EFGImportConstants.DATABASENAME);
	
	String digirRequest = req.getParameter(EFGImportConstants.DIGIR);
	log.debug("Digir request: " + digirRequest);
	if (digirRequest != null){//There is a DiGIR request
	    InputSource source = new InputSource(new StringReader(digirRequest));
	    DigirParserHandler dph = new DigirParserHandler(source);
	    Object obj = null;
	    if((dph.getErrorCode() != -1)&&(dph.getDataSourceErrorCode() != -1)){
		obj = buildResult(req, res, sc,dph);
		String displayFormat = req.getParameter(EFGImportConstants.DISPLAY_FORMAT);
		if (EFGImportConstants.HTML.equalsIgnoreCase(displayFormat)) {
		    return (Element)obj;
		}
	    }
	    else {
	       log.error("Error code is: " + dph.getErrorCode());
	    }
	    return (new DiGIRProcessor(dph,req)).presentDigirResponse(obj);
	}
	//pass the datasourceName as argument
	Document efgDocument = null;
	String dataSource = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
	
	SQL2XML sql2xml = null;
	if (dataSource != null){
	    String metadataSource = this.getMetadatasource(dataSource);
	    sql2xml = new SQL2XML(dataSource,metadataSource);
	    //log.debug("SQL xml created with datasource");
	    efgDocument = buildDocument(sql2xml,req, dataSource,currentDatabase);
	}
	else{
	    List allDataSources = this.getAllDataSourceNames();
	    Iterator dsNameIter = allDataSources.iterator(); 
	    
	    while (dsNameIter.hasNext()) {
		dataSource = (String)dsNameIter.next();
		if(sql2xml == null){
		    sql2xml = new SQL2XML();
		}
		log.debug("Datasource: " + dataSource);
		sql2xml.setDatasourceName(dataSource,true);
		efgDocument = buildDocument(sql2xml,req, dataSource,currentDatabase);
	    }
   	}
	if(efgDocument == null){
	    return null;
	}
	return efgDocument.getRootElement();
    }
    protected List getAllDataSourceNames(){
	EFGDataSourceHelper dsHelper = (new EFGDSHelperFactory()).getDataSourceHelper(); 
	return dsHelper.getDSNames();
    }
    protected HashSet getMultiple(String metadataSource){
	ResultSet trst = null;
	Connection tconn = null;
	Statement tstmt = null;
	HashSet  set = new HashSet();
	String query = "";

	StringBuffer queryBuffer = new StringBuffer();
	queryBuffer.append("SELECT legalName FROM ");
	queryBuffer.append(metadataSource);
	queryBuffer.append(" where ( "); 
	queryBuffer.append(EFGImportConstants.HOLDSMULTIPLEVALUES); 
	queryBuffer.append("='"); 
	queryBuffer.append(EFGImportConstants.YES);
	queryBuffer.append("' and ");
	queryBuffer.append(EFGImportConstants.SEARCHABLE); 
	queryBuffer.append("='");
	queryBuffer.append(EFGImportConstants.YES);
	queryBuffer.append("' and ");
	queryBuffer.append(EFGImportConstants.PARSERABLE);
	queryBuffer.append("='");
	queryBuffer.append(EFGImportConstants.YES);
	queryBuffer.append("') or (");
	queryBuffer.append(EFGImportConstants.NUMERIC);
	queryBuffer.append( "='"); 
	queryBuffer.append(EFGImportConstants.YES); 
	queryBuffer.append( "=' or " );
	queryBuffer.append(EFGImportConstants.NUMERICRANGE); 
	queryBuffer.append("='");
	queryBuffer.append(EFGImportConstants.YES); 
	queryBuffer.append("')");
	    	 
	try{
	    query = queryBuffer.toString();
	    log.debug("Select query: " + query);
	    tconn = EFGRDBUtils.getConnection();
	    tstmt = tconn.createStatement();
	    
	    // Get the table names
	    trst = tstmt.executeQuery(query);
	    
	    while (trst.next()) {
		String lName = trst.getString(EFGImportConstants.LEGALNAME);
		set.add(lName.trim());
	    }
	    if(trst != null){
		trst.close();
	    }
	    if(tstmt != null){
		tstmt.close();
	    }
	    if(tconn != null){
		tconn.close();
	    }
	}
	catch (SQLException ex) {
	    LoggerUtilsServlet.printSQLErrors(ex) ;
	} catch (Exception e) {
	    LoggerUtilsServlet.logErrors(e);
	}
	finally{
	    try{
		if(trst != null){
		    trst.close();
		}
		if( tstmt != null){
		    tstmt.close();
		}
		if(tconn != null){
		    EFGRDBUtils.closeConnections(tconn);
		}
	    }
	    catch(Exception ee){
	    }
	}
	return set;
    }
    /**
     * Build a query from the request and build the root element "efgDocument".
     *
     * @param req the servlet request object
     * @param dataSource the name of the data source
     */
    protected Document buildDocument(SQL2XML sql2xml,HttpServletRequest req, String dataSource, String currentDatabase)
    {
	Document doc = null;
	String metadataSource = this.getMetadatasource(dataSource);
	try{
	    HashSet set =  getMultiple(metadataSource);
	    Hashtable table = new Hashtable();
	    String dataQuery = buildQuery(req, dataSource,metadataSource,set,table,currentDatabase);
	    log.debug("dataQuery: " + dataQuery);
	    if (dataQuery == null || "".equals(dataQuery.trim())){
		return null;
	    }
	    String metaQuery = "SELECT * FROM " + metadataSource;
	    doc = buildEFGDocument(sql2xml,dataQuery,metaQuery,table,false);
	}
	catch (Exception e) {
	    LoggerUtilsServlet.logErrors(e);
	}
	return doc;
    }

    /**
     * This method builds a query from the request such as
     * /efg/servlet/search?Genus=Solanum&Species=chrysotrichuming
     * /efg/servlet/search?searchStr=( Genus=Solanum && Species=chrysotrichuming )
     *
     * @param req the servlet request object
     * @return the query string
     */
    protected String buildQuery(
				HttpServletRequest req, 
				String dataSource,
				String metadataSource, 
				HashSet set,
				Hashtable table,
				String database
				) 
    {
	Connection conn = EFGRDBUtils.getConnection();
	if (conn == null){
	    log.error("Couldn't get connection to database.");
	    return null;
	}
	String maxDispStr = req.getParameter(EFGImportConstants.MAX_DISPLAY);
	int maxDisplay = -1;
	
	if (maxDispStr != null) {
	    try {  
		if(!EFGImportConstants.MAX_DISPLAY_IGNORE.equalsIgnoreCase(maxDispStr.trim())){
		    maxDisplay = Integer.parseInt(maxDispStr);
		}
	    }
	    catch (Exception e) {
	    }
	}

	StringBuffer querySB = new StringBuffer();

	try {
	    
	    // get the hashtable mapping taxon's name to scientific name
	    Hashtable ht = this.getNameMapping(metadataSource);
	    // starting to construct the taxon query
	  
	    String fieldName = req.getParameter(EFGImportConstants.WILDCARD_STR);
	  
	    int paramNo = 0;
	    querySB.append("SELECT * FROM " + dataSource);	    
	    if((fieldName == null) || (fieldName.trim().equals(""))){//means there may be a where clause
		Enumeration paramEnum = req.getParameterNames();
		while (paramEnum.hasMoreElements()) {
		    String paramName = (String) paramEnum.nextElement();
		    if (specialParams.contains(paramName) || 
			paramName.equals(EFGImportConstants.SEARCHSTR)){
			continue;
		    }
		    if(paramName.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1){
			paramName = paramName.replaceAll(EFGImportConstants.SERVICE_LINK_FILLER," ");
		    }
		    String pVal = req.getParameter(paramName);
		    if(pVal.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1){
			pVal = pVal.replaceAll(EFGImportConstants.SERVICE_LINK_FILLER," ");
		    }
		    // get the legal name for the paramName from the hashtable
		    String paramLegalName = (String) ht.get(paramName);
		    if (paramLegalName == null || "".equals(paramLegalName.trim())){
			continue;
		    }
		    if (pVal == null || "".equals(pVal.trim())){
			continue;
		    }
		    
		    if(set.contains(paramLegalName.trim())){
			log.debug("Skipping: " + paramLegalName);
			table.put(paramLegalName.trim(),pVal.trim());
			continue;
		    }
		    paramNo = appendParams(paramLegalName, pVal, querySB, paramNo);
		}
	    }
	    else{
		fieldName = (String) ht.get(fieldName);//get legal name for current field
		table.put(fieldName.trim(),EFGImportConstants.WILDCARD);
		querySB.append(" where ");
		querySB.append(fieldName.trim());
		querySB.append(" like '%' ");
	    }
	   
	    if (paramNo > 0){
		querySB.append(")");
	    }
	    if(maxDisplay > 0){  //FIX ME.. only works for MySQL, add fix for other relational databases
		if(EFGImportConstants.MYSQL.equalsIgnoreCase(database)){
		    querySB.append(" limit ");
		    querySB.append(maxDisplay + "");
		}
	    }

	    if ((req.getParameter(EFGImportConstants.SEARCHSTR)) != null) {
		String searchStrVal = req.getParameter(EFGImportConstants.SEARCHSTR);
		paramNo = appendSearchStrParams(searchStrVal, querySB, ht, paramNo);
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
	return querySB.toString();
    }
    
    /**
     * Append request parameters (Genus=Solanum&Species=chrysotrichuming) to 
     * the query string, except for the "searchStr" parameter.
     *
     * @param paramLegalName the legal name of the parameter
     * @param paramValue the value of the parameter
     * @param querySB the StringBuffer object for the query string
     * @param paramNo the number of parameters appended to the query string
     * @return the number of parameters appended to the query string
     */ 
    protected int appendParams(String paramLegalName, String paramValue, 
			       StringBuffer querySB, int paramNo)
    { 
	// ignore null parameters
	if ((paramLegalName == null) || "".equals(paramLegalName)){
	    return paramNo;
	}
	// ignore parameters w/o values
	if ((paramValue == null) || "".equals(paramValue)){
	    return paramNo;
	}
	
	if (paramNo == 0){
	    querySB.append(" where ((");
	}
	else{ // concatnate search conditions
	    querySB.append(" and (");
	}
	String qr = paramLegalName + " = " + "'" + paramValue + "'" ;
	log.debug("Where clause: " + qr);
	querySB.append(qr+ ")" );

	// increment the number of search conditions and return
	return ++paramNo;
    }

    /**
     * Append request parameters (searchStr=( Genus=Solanum && Species=chrysotrichuming ))
     * to the query string, except for the "searchStr" parameter.
     *
     * @param searchStrValue the value of the "searchStr" parameter
     * @param querySB the StringBuffer object for the query string
     * @param ht the hashtable that contains the name-to-legal_name mapping
     * @param paramNo the number of parameters appended to the query string
     * @return the number of parameters appended to the query string
     */ 
    protected int appendSearchStrParams(String searchStrValue, StringBuffer querySB,
				      Hashtable ht, int paramNo)
    {
	int paramCount = paramNo;
	// starting to construct the taxon query from parameters like
	// 
	if (paramNo == 0)
	    querySB.append(" where ((");
	else
	    querySB.append(" and ((");

	StringTokenizer st = new StringTokenizer(searchStrValue, "()=&| ", true);
	int andCount = 0;
	int orCount = 0;
	while (st.hasMoreTokens()) {
	    String current = st.nextToken();
	    if (current.equals("(") || current.equals(")"));
	    //		querySB.append(current);
	    else if (current.equals(" ") || current.equals("&") || current.equals("|")) {
		if (current.equals("&")) {
		    andCount++;
		    if (andCount == 2) {
			querySB.append(" and (");
			andCount = 0;
		    }
		}
		else if (current.equals("|")) {
		    orCount++;
		    if (orCount == 2) {
			querySB.append(" or (");
			orCount = 0;
		    }
		}
	    }
	    else {
		String legalName = (String) ht.get(current);
		if (legalName == null || "".equals(legalName.trim()))
		    return paramNo;
		querySB.append(legalName + " = ");
		current = st.nextToken();
		while (current.equals(" ") || current.equals("=")) 
		    current = st.nextToken();
		querySB.append("'" + current + "')");
		paramNo++; // increment the number of search conditions
	    }
	} // while ends
	if (paramCount < paramNo) //new params added in searchStr query
	    querySB.append(")");
	return paramNo;
    }
 
    /**
     * This method is used to inform the user in case an error occurs 
     * while processing his/her request.
     * 
     * @param errorMessage thge error message
     * @param res the servlet response object
     */
    public void presentError(String errorMessage, HttpServletResponse res) 
    {
    	List lines = new ArrayList(1);
    	lines.add("<H1>" + errorMessage + "</H1");
    	
    	try {
    	    PrintWriter pw = res.getWriter();
	    EFGServletUtils.presentHTML(lines, pw);
    	}
    	catch (IOException ioe) {
    	    LoggerUtilsServlet.logErrors(ioe);
    	}
    }

    /**
     * Build the EFGDocument from the query.
     *
     * @param query the query string 
     * @param dataSource the name of the dataSource
     */
    protected Document buildEFGDocument(SQL2XML sql2xml,String dataQuery, 
					String metaQuery, Hashtable table, boolean isDigir)
    {   
	return sql2xml.getDataDoc(dataQuery,metaQuery,table,isDigir);
    }

   
    public void main(String[] args){
	RDBQueryHandler rdb = new RDBQueryHandler();
	String datasource = "catalogofinvasivesinfo";
	String metadataSource  = rdb.getMetadatasource(datasource);
	
	Hashtable ht = rdb.getNameMapping(metadataSource);
	
	StringBuffer buffer = new StringBuffer();
	buffer.append("<request xmlns=");
	buffer.append("\"");
	buffer.append("http://digir.net/schema/protocol/2003/1.0");
	buffer.append("\" ");
	buffer.append("xmlns:darwin=");
	buffer.append("\"");
	buffer.append("http://digir.net/schema/conceptual/darwin/2003/1.0");
	buffer.append("\" ");
	buffer.append("xmlns:xsi=");
	buffer.append("\"");
	buffer.append("http://www.w3.org/2001/XMLSchema-instance");
	buffer.append("\" ");
	buffer.append("xsi:schemaLocation=");
	buffer.append("\"");
	buffer.append("digir.xsd darwin2.xsd");
	buffer.append("\"");
	buffer.append(">");
	buffer.append("<header><version>0.91</version><sendTime>1997-07-16T19:20:30.45+01:00</sendTime>" + 
		      "<source>128.32.214.123</source>" + 
		      "<destination resource="
		      );
	buffer.append("\"");
	buffer.append("MammalsDwC2");
	buffer.append("\"");
	buffer.append(">http://128.32.214.123:80/DiGIRprov/ttuwww/DiGIR.php</destination>" + 
		      "<type>search</type>" + 
		      "</header>" + 
		      "<search><filter><and><equals><darwin:Genus>Mechanitis</darwin:Genus>" + 
		      "</equals><and><notEquals><darwin:Species>polymnia</darwin:Species>" +
		      "</notEquals><notEquals><darwin:Species>lysimnia</darwin:Species>" +
		      "</notEquals></and></and></filter><records start=");
	buffer.append("\"");
	buffer.append("0");
	buffer.append("\" ");
	buffer.append("limit=");
	buffer.append("\"");
	buffer.append("10");
	buffer.append("\"");
	buffer.append(">");
	buffer.append("<structure/>"  +
		      "</records>" + 
		      "<count>true</count>" + 
		      "</search>" + 
		      "</request>");
	//String SQLWhere = rdb.getSQLWhereString(buffer.toString(),metadataSource, new HashSet());
	
    }
}

//$Log$
//Revision 1.1  2006/01/25 21:03:42  kasiedu
//Initial revision
//
//Revision 1.1.1.1  2003/10/17 17:03:05  kimmylin
//no message
//
//Revision 1.7  2003/08/29 17:35:31  kasiedu
//*** empty log message ***
  //
  //Revision 1.6  2003/08/28 23:13:45  kasiedu
  //*** empty log message ***
  //
  //Revision 1.5  2003/08/28 16:37:51  kimmylin
  //no message
  //
  //Revision 1.4  2003/08/20 18:45:42  kimmylin
  //no message
  //
  //Revision 1.3  2003/08/05 14:23:00  kasiedu
  //*** empty log message ***
  //
  //Revision 1.2  2003/08/02 20:14:17  kasiedu
  //*** empty log message ***
  //
  //Revision 1.1  2003/08/01 20:45:36  kasiedu
  //*** empty log message ***
  //
  //Revision 1.4  2003/08/01 18:39:41  kimmylin
  //no message
  //
  //Revision 1.3  2003/08/01 14:27:15  kasiedu
  //*** empty log message ***
  //
  //Revision 1.2  2003/08/01 00:34:02  kasiedu
  //*** empty log message ***
  //
  //Revision 1.1  2003/07/31 21:22:58  kimmylin
  //no message
  //
  //Revision 1.1.1.1  2003/07/28 18:58:08  kasiedu
  //EFG Relational Database
  //
  //Revision 1.3  2003/07/25 16:01:13  kimmylin
  //no message
  //
  //Revision 1.2  2003/07/20 21:14:14  kasiedu
  //*** empty log message ***
  //
  //Revision 1.1.1.2  2003/07/18 21:50:15  kimmylin
  //RDB added
  //
