/**
 * $Id$
 *
 * Copyright (c) 2005  University of Massachusetts Boston
 *
 * Authors: Jacob Asiedu<kasiedu@cs.umb.edu>
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
//REMOVE ME
import project.efg.Import.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import java.sql.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom.output.XMLOutputter;

/**
* This class allows you to open a connection to an Excel document.
*/
public class SQL2XML implements EFGImportConstants {
    
    protected String datasourceName;
    protected String metadatasourceName;
    protected boolean verbose = false;
    protected org.jdom.Document dataDoc;
    protected org.jdom.Document metaDoc;
    protected boolean debug;
    protected boolean add=false;
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(SQL2XML.class); 
	}
	catch(Exception ee){
	}
    }
    public void setDatasourceName(String datasourceName, boolean add){
	this.datasourceName = datasourceName;
	this.add = add;
    }
    /**
     * Create new SQL2XMLData object.  Next, code should call openDB,
     * then make some requests, then call closeDB when done.
     * @param datasourceName name of the datasource
     * @param metadatasourceName name of the metadatasource
     *@param debug - true if in debug mode, false otherwise
     */
    public SQL2XML(String datasourceName,
		   String metadatasourceName,
		   boolean debug){
	this.datasourceName = datasourceName;
	this.metadatasourceName = metadatasourceName;
	this.debug=debug;
    }
    /**
     * Create new SQL2XMLData object.  Next, code should call openDB,
     * then make some requests, then call closeDB when done.
     * @param datasourceName 
     * @param metadatasourceName
     */
    public SQL2XML(String datasourceName,
		   String metadatasourceName){
	this(datasourceName,metadatasourceName,false);
    }
   /**
     * Create new SQL2XMLData object.  Next, code should call openDB,
     * then make some requests, then call closeDB when done.
     * @param datasourceName 
     * @param metadatasourceName
     */
    public SQL2XML(){
	this("","",false);
    }
    
    protected void addOrderedElement(Element metaFieldElem, Element newFieldElem){
	if (metaFieldElem.getChild(EFGImportConstants.ORDERED).getTextTrim().
	    equalsIgnoreCase(EFGImportConstants.YES)){//it is an image
	    newFieldElem.setAttribute(EFGImportConstants.ORDERED, EFGImportConstants.TRUE+"");			
	}
	else{
	    newFieldElem.setAttribute(EFGImportConstants.ORDERED, EFGImportConstants.FALSE+"");			
	}
    }
    public Document getMetadataDoc(String query){
	this.buildBaseMetadataDoc();
	this.buildMetadataDoc(query);
        return this.metaDoc;
    }
    public Document getDataDoc(String query, String metaQuery,Hashtable table, boolean isDigir){
	try{
	    //log.debug("metaQuery: " + metaQuery);
	    this.getMetadataDoc(metaQuery);
	    if(this.dataDoc == null){
		this.buildBaseDoc();
	    }
	    buildDataDoc(query,table,isDigir);
	}
	catch(Exception ee){
	    LoggerUtilsServlet.logErrors(ee);
	}
        return this.dataDoc;
    }
    private void buildBaseMetadataDoc(){
	Element root = new Element("EFGTypeInfo");
	if(!this.add){
	    root.setAttribute(EFGImportConstants.DATASOURCE, this.datasourceName);
	}
	this.metaDoc = new Document(root);
    }
    private void buildBaseDoc(){
	Element importDocument = new Element("EFGDocument", "http://www.cs.umb.edu/efg");
	importDocument.setAttribute("submitterName", "");
	if(!this.add){
	    importDocument.setAttribute(EFGImportConstants.DATASOURCE, this.datasourceName);
	}
	this.dataDoc = new Document(importDocument);
    }
  
    private void buildMetadataDoc(String query){
      	Connection conn=null;
	Statement stmt=null;
	ResultSet rs=null;
	try {
	    conn = EFGRDBUtils.getConnection();
	    if(conn == null){
		log.error("Connection is null");
		return;
	    }
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(query);
	    //log.debug("metaQuery2: " + query);
	    while(rs.next()){
		try{
		    Element fieldEntry = new Element(EFGImportConstants.FIELDELEMENT);
		    String uid = rs.getString("uniqueID");
		
		    Element elem = new Element(EFGImportConstants.WEIGHT);
		    String weight=rs.getString(EFGImportConstants.WEIGHT);
		    elem.addContent(weight);
		    fieldEntry.addContent(elem);
		    
		    elem = new Element(EFGImportConstants.NAME);
		    String name = rs.getString(EFGImportConstants.NAME);
		    elem.addContent(name);
		    fieldEntry.addContent(elem);

		    elem = new Element(EFGImportConstants.LEGALNAME);
		    String legalName= rs.getString(EFGImportConstants.LEGALNAME);
		    elem.addContent(legalName);
		    fieldEntry.addContent(elem);
		    
		    elem = new Element(EFGImportConstants.SEARCHABLE);
		    String searchable = rs.getString(EFGImportConstants.SEARCHABLE);
		    elem.addContent(searchable);
		    fieldEntry.addContent(elem);
		    
		    elem = new Element(EFGImportConstants.SPECIESPAGEDATA);
		    String speciesPageData = rs.getString(EFGImportConstants.SPECIESPAGEDATA);
		    elem.addContent(speciesPageData);
		    fieldEntry.addContent(elem);
		    
		    elem = new Element(EFGImportConstants.HOLDSMULTIPLEVALUES);
		    String holdsMultipleValues= rs.getString(EFGImportConstants.HOLDSMULTIPLEVALUES);
		    elem.addContent(holdsMultipleValues);
		    fieldEntry.addContent(elem);
		
		    elem = new Element(EFGImportConstants.PARSERABLE);
		    String parserable = rs.getString(EFGImportConstants.PARSERABLE);
		    elem.addContent(parserable);
		    fieldEntry.addContent(elem);

		    elem = new Element(EFGImportConstants.ORDERED);
		    String ordered = rs.getString(EFGImportConstants.ORDERED);
		    elem.addContent(ordered);
		    fieldEntry.addContent(elem);

		    elem = new Element(EFGImportConstants.NUMERIC);
		    String numeric=rs.getString(EFGImportConstants.NUMERIC);
		    elem.addContent(numeric);
		    fieldEntry.addContent(elem);

		    elem = new Element(EFGImportConstants.NUMERICRANGE);
		    String numericRange=rs.getString(EFGImportConstants.NUMERICRANGE);
		    elem.addContent(numericRange);
		    fieldEntry.addContent(elem);
		
		    elem = new Element(EFGImportConstants.DATATYPE);
		    String dataType= rs.getString(EFGImportConstants.DATATYPE);
		    elem.addContent(dataType);
		    fieldEntry.addContent(elem);
		    
		    elem = new Element(EFGImportConstants.JAVATYPE);
		    String javaType= rs.getString(EFGImportConstants.JAVATYPE);
		    elem.addContent(javaType);
		    fieldEntry.addContent(elem);
		    if (fieldEntry.getChildren().size() > 0){
			this.metaDoc.getRootElement().addContent(fieldEntry);
		    }
		    else{
			//log.debug("No children");
		    }
		}
		catch(Exception ee){
		    //log.debug("metad: " + ee.getMessage());
		    LoggerUtilsServlet.logErrors(ee);
		}
	    }
	    if (rs != null) {
		rs.close();
	    }
	    if (stmt != null) {
		stmt.close();
	    }

	    if(conn != null){
		try{
		    EFGRDBUtils.closeConnections(conn);
		}
		catch(Exception eeee){}
	    }
	}
	catch (Exception sql) {
	    LoggerUtilsServlet.logErrors(sql);
	    try{
		if (rs != null) {
		    rs.close();
		}
		if (stmt != null) {
		    stmt.close();
		}
		if(this.debug){
		    EFGRDBImportUtils.closeConnection();
		}
		else{
		    EFGRDBUtils.closeConnections(conn);
		}
	    }
	    catch(Exception exx){
		//ggerUtils.logErrors(exx);
	    }
	}
    }
    private String[] isInNumericRange(
				      String state, 
				      String legalName,
				      String obj
				      ){
	try{
	    //log.debug("About to process statistical measure: " + state);
	    //log.debug("Selected value: " + obj);
	    double minVal = -1;
	    double maxVal = -1;
	    double currentVal = -1;
	    boolean add = true;
	    
	    if((state == null) || (state.trim().equals(""))){
		log.error("State cannot be the empty string or null");
		return null;
	    }
	    
	    String[] measures = this.parseStatsMeasure(state);
	    if(measures == null){
		log.error("State: " + state + " not added to stats measure because it could not be parsed successfully");
		return null;
	    }
	    
	    try{//convert current value to a double
		currentVal = Double.parseDouble(obj.trim());
	    }
	    catch(Exception eee){
		//log.error("Values for " + legalName + " must be a valid nummber. The supplied value of: " + obj + " is not a number");
		//return null;
	    }
	    try{//convert min value to a double
		minVal = Double.parseDouble(measures[0].trim());
	    }
	    catch(Exception eee){
		log.error("Values for " + legalName + " must be a valid nummber. The database value of: " + measures[0].trim() + " is not a number");
		return null;
	    }
	    try{//convert max value to a double
		maxVal = Double.parseDouble(measures[1]);
	    }
	    catch(Exception eee){
		log.error("Values for " + legalName + " must be a valid nummber. The database value of: " + measures[1].trim() + " is not a number");
		return null;
	    }
	    //check only if obj is not null
	    if((obj == null) || (obj.trim().equals(""))){
		return measures;
	    }
	    else{
		if((minVal <= currentVal) && (currentVal <= maxVal)){
		    return measures;
		}
	    }
	}
	catch(Exception ee){
	    log.error(ee.getMessage());
	}
	return null;
    }
    private void processStatisticalMeasure(Element statisticalMeasures,
					   String state, 
					   String legalName,
					   Hashtable table, boolean isDigir){
	
	String obj = (String)table.get(legalName.trim());
	
	log.debug("State: " + state);
	log.debug("legalName: " + legalName);
	
	String[] measures = this.isInNumericRange(state,legalName,obj); 
	
	if(measures == null){
	    log.debug("Measures is null");
	    return;
	}
	String units = this.parseUnits(state);
	if(units == null){
	    units = "";
	}
	log.debug("Measure[0]: " + measures[0]);
	log.debug("Measure[1]: " + measures[1]);

	this.addStatisticalMeasure(
				   statisticalMeasures,
				   measures[0], 
				   measures[1],
				   units
				   );
    }
    private void startDocumentBuild(ResultSet rs,Hashtable table,boolean isDigir){
	java.util.List metaFieldElems = this.metaDoc.getRootElement().getChildren();
	try{
	    Element entry = new Element(EFGImportConstants.TAXONENTRY);
	    if(this.datasourceName != null){
		if(!this.datasourceName.trim().equals("")){
		    if(this.add){
			entry.setAttribute(EFGImportConstants.DATASOURCE, this.datasourceName);
		    }
		}
	    }
	    for (int j = 0; j < metaFieldElems.size(); j++) {
		Element metaFieldElem = (Element)metaFieldElems.get(j);
		String name; // field name
		String legalname; //original field name
		String dataString = null;
		String dataType = null;
		try {
		    legalname = metaFieldElem.getChild(EFGImportConstants.LEGALNAME).getTextTrim();
		    
		    //parse this if contains table.get(legalname);
		    name = metaFieldElem.getChild(EFGImportConstants.NAME).getTextTrim();
		    dataType = metaFieldElem.getChild(EFGImportConstants.DATATYPE).getTextTrim();
		    if(dataType == null){
			dataType = "";
		    }
		    dataString = (String)rs.getObject(legalname);
		    if((dataString == null)||(dataString.trim().equals(""))){
			continue;
		    }
		}
		catch (Exception npe) {
		    continue;
		} 
		try {
		    //get the type
		    if (metaFieldElem.getChild(EFGImportConstants.DATATYPE).getTextTrim().
			equalsIgnoreCase(EFGImportConstants.MEDIARESOURCETYPE)){//it is an image
			//log.debug("MediaResource value");
			boolean isParsed = false;
			Element mediaresources = new Element(EFGImportConstants.MEDIARESOURCES);//media resources
			mediaresources.setAttribute(EFGImportConstants.NAME, name.trim());
			//	mediaresources.setAttribute(EFGImportConstants.EFGTYPE, dataType.trim());
			String caption = null;
			String states =dataString.trim(); 
			if(states.indexOf(EFGImportConstants.COLONSEP) > -1){
			    String[] captions = this.parseMediaResourceCaptions(states);
			    if(captions.length == 2){
				caption = captions[0];
				states=captions[1];
			    }
			}
			if (metaFieldElem.getChild(EFGImportConstants.HOLDSMULTIPLEVALUES).getTextTrim().
			    equalsIgnoreCase(EFGImportConstants.YES)) {
			    this.addOrderedElement(metaFieldElem,mediaresources);
			    isParsed = this.handleParserables(metaFieldElem,mediaresources, 
							     legalname,dataString.trim(),
							     EFGImportConstants.MEDIARESOURCEINTTYPE, 
							      table,isDigir);
			    if(!isParsed){
				this.addMediaResourceElement(mediaresources,states,caption);
			    }
			  
			}
			else{
			    this.addMediaResourceElement(mediaresources,states,caption);
			}
			if(mediaresources.getChildren().size() > 0){
			    entry.addContent(mediaresources);
			}
		    }
		    else if (metaFieldElem.getChild(EFGImportConstants.DATATYPE).getTextTrim().
			     equalsIgnoreCase(EFGImportConstants.EFGLISTTYPE)){//it is a list such as similar species etc
			boolean isParsed = false;
			//log.debug("List value");
			Element items = new Element(EFGImportConstants.EFGLISTS);
			items.setAttribute(EFGImportConstants.NAME,name.trim());
			items.setAttribute(EFGImportConstants.EFGTYPE, dataType.trim());
			String caption = null;
			String states =dataString.trim();
			
			if(states.indexOf(PIPESEP) > -1){
			    String[] captions = this.parseEFGListsCaptions(states);
			    if(captions.length == 2){
				caption = captions[0];
				states=captions[1];
			    }
			}

			if (metaFieldElem.getChild(EFGImportConstants.HOLDSMULTIPLEVALUES).getTextTrim().
			    equalsIgnoreCase(EFGImportConstants.YES)) {
			    this.addOrderedElement(metaFieldElem,items);
			    //find out if there is only one title present in the current row of data
			    //parse semiColons
			    
			    isParsed = this.handleEFGListParserables(
								     metaFieldElem,
								     items, 
								     legalname,
								     dataString.trim(),
								     EFGImportConstants.LISTTYPE,
								     table,isDigir
								     ); 
			}
			if(!isParsed){
			    this.addEFGListElement(items,states,caption);			    
			}
			if(items.getChildren().size() > 0){
			    entry.addContent(items);
			}
		    }
		    else if (metaFieldElem.getChild(EFGImportConstants.NUMERIC).getTextTrim().
			     equalsIgnoreCase(EFGImportConstants.YES) ||
			     metaFieldElem.getChild(EFGImportConstants.NUMERICRANGE).getTextTrim().
			     equalsIgnoreCase(EFGImportConstants.YES)) {//numeric value or range 
			log.debug("Numeric value");
			boolean isParsed = false;
			Element statisticalMeasures = new Element(EFGImportConstants.STATISTICALMEASURES);
			statisticalMeasures.setAttribute(EFGImportConstants.NAME, name.trim());
			//statisticalMeasures.setAttribute(EFGImportConstants.EFGTYPE, dataType.trim());
					
			if (metaFieldElem.getChild(EFGImportConstants.HOLDSMULTIPLEVALUES).getTextTrim().
			    equalsIgnoreCase(EFGImportConstants.YES)) {
			    this.addOrderedElement(metaFieldElem,statisticalMeasures);
			    isParsed = this.handleParserables(
							     metaFieldElem,
							     statisticalMeasures, 
							     legalname,
							     dataString.trim(),
							     EFGImportConstants.NUMERICTYPE,
							     table,
							     isDigir
							     ); 
			}
			if(!isParsed){
			    this.processStatisticalMeasure(statisticalMeasures,
							   dataString.trim(),legalname,table,isDigir);
			}
			if(statisticalMeasures.getChildren().size() > 0){
			    entry.addContent(statisticalMeasures);
			}
		    }
		    else if (metaFieldElem.getChild(EFGImportConstants.HOLDSMULTIPLEVALUES).getTextTrim().
			     equalsIgnoreCase(EFGImportConstants.YES)) {//not numeric just holds multiple states
			boolean isParsed = false;
			//log.debug("holds multiple value");
			Element items = new Element(EFGImportConstants.ITEMS);
			items.setAttribute(EFGImportConstants.NAME,name.trim());
			this.addOrderedElement(metaFieldElem,items);
			isParsed = this.handleParserables(
							  metaFieldElem,
							  items, 
							  legalname,
							  dataString.trim(),
							  EFGImportConstants.ANYTYPE,
							  table,isDigir
							  ); 
		    	if(!isParsed){
			    this.handleItems(metaFieldElem,items,dataString.trim(),legalname,table,isDigir);
			}
			if(items.getChildren().size() > 0){
			    entry.addContent(items);
			}
		    }    
		    else {//neither a media resource, a numeric character, a character with parseable states
			//log.debug("None of the above");
			Element items = new Element(EFGImportConstants.ITEMS);
			items.setAttribute(EFGImportConstants.NAME,name.trim());
			this.handleItems(metaFieldElem,items,dataString.trim(),legalname,new Hashtable(),isDigir);
			if(items.getChildren().size() > 0){
			    entry.addContent(items);
			}
		    }
		}
		catch (NullPointerException npe) {
		    npe.printStackTrace();
		    break;
		}
		
	    }
	    if (entry.getChildren().size() > 0){
		this.dataDoc.getRootElement().addContent(entry);
	    }
	}
	catch(Exception xee){
	    LoggerUtilsServlet.logErrors(xee);
	    // System.err.println(xee.getMessage());
	}
    }
    protected String[] parseMultiples(String states){
	String patternStr = "([,]|([ ]+(or)[ ]))";
	return states.split(patternStr, -1);
    }
    protected String[] parseMediaResourceCaptions(String state){
	String patternStr = EFGImportConstants.COLONSEP;
	return state.split(patternStr, -1);
    }
    protected String[] parseEFGListsCaptions(String state){
	String patternStr = "\\|";
	return state.split(patternStr, -1);
    }
    protected String[] parseSemiColon(String state){
	String patternStr = EFGImportConstants.SEMICOLON;
	return state.split(patternStr, -1);
    }

    private boolean handleEFGListParserables(Element metaFieldElem, 
					     Element parent, 
					     String legalName, 
					     String states, 
					     int efgType, 
					     Hashtable table,boolean isDigir){
	

	if(metaFieldElem.getChild(EFGImportConstants.PARSERABLE).getTextTrim().
	   equalsIgnoreCase(EFGImportConstants.YES)) { 
	    String[] stateArr = this.parseSemiColon(states.trim());
	    for(int i = 0; i < stateArr.length; i++){
		String state = stateArr[i].trim();
		this.parseSearchable(parent,
				     state,
				     legalName,
				     table,
				     efgType, isDigir);
	    }
	    return true;
	}
	return false;
    }
    private boolean handleParserables(Element metaFieldElem, 
				      Element parent, 
				      String legalName, 
				      String states, 
				      int efgType, 
				      Hashtable table,boolean isDigir){

	if(metaFieldElem.getChild(EFGImportConstants.PARSERABLE).getTextTrim().
	   equalsIgnoreCase(EFGImportConstants.YES)) {  
	    this.parseSearchable(parent,
				 states.trim(),
				 legalName,
				 table,
				 efgType,isDigir);
	    return true;
	}
	return false;
    }
    private String parseUnits(String inputStr){
	StringBuffer buffer = new StringBuffer();
	try{
	    String patternStr = "[A-Z]+";
	    Pattern p = Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);
	    Matcher matcher = p.matcher(inputStr);
	    boolean matchFound = false;
	    String match = null;
	    while(matcher.find()){
		try{
		    match = matcher.group();
		    if(buffer.length() > 0){
			buffer.append(",");
		    }
		    buffer.append(match); 
		}
		catch(Exception ee){

		}
	    }
	}
	catch(Exception vvv){}
	return buffer.toString();
    }
    private String[] parseStatsMeasure(String inputStr1){
	// Parse a comma-separated string
	//String inputStr = "123 ft";
	Double minValD = null;
	Double  maxValD = null;
	Double tempD = null;
	Double currentD = null;
	String minVal = "";
	String maxVal = "";
	
	try{
	    String patternStr = "[A-Z]+";//remove everything that is an alpahabet
	    Pattern p = Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);
	    String[] fields = p.split(inputStr1);
	    // if greater than or equals 2 find out if last value is a non number or a '-'
	  
	    
	    for(int j =0; j < fields.length; j++){
		try{
		    String inputStr = fields[j].trim();
		    String[] minVals = inputStr.split("-");//split string with '-'
		    for(int i = 0; i < minVals.length; i++){
			try{
			    patternStr = "^\\d+$";
			    String newStr = minVals[i].trim();
			    p = Pattern.compile(patternStr);
			    Matcher matcher = p.matcher(newStr);//find strings with only digits
			    boolean matchFound = matcher.find(); 
			    String match = matcher.group();
			    if((match != null) || (!match.trim().equals(""))){
				if(minValD == null){
				    minValD = new Double(match);
				}
				if(maxValD == null){
				    maxValD = new Double(match);
				    if(minValD.compareTo(maxValD) > 0){
					tempD = minValD;
					minValD = maxValD;
					maxValD = tempD;
				    }
				}
				if((minValD != null ) && (maxValD != null)){
				    currentD = new Double(match);
				    if(minValD.compareTo(currentD) > 0){
					minValD = currentD;
				    }
				    else if(currentD.compareTo(maxValD) > 0){
					maxValD = currentD;
				    }
				}
			    }
			}
			catch(Exception ex){
			    
			}  
		    }
		}
		catch(Exception ex){
		    
		}
	    }
	  
	}
	catch(Exception ee){

	}
	if(minValD == null){
	    minVal = "";
	}
	else{
	    minVal = minValD.toString();
	}
	if(maxValD == null){
	    maxVal = "";
	}
	else{
	    maxVal = maxValD.toString();
	}
	String[] minMax = new String[2];
	minMax[0] = minVal;
	minMax[1] = maxVal;
	return minMax;

    }
    private boolean parseCompare(String states,String state){
	String[] statesArray = this.parseMultiples(states);
	boolean bool = false;
	
	for(int j=0; j < statesArray.length;j++){
	    String current = statesArray[j].trim();
	    //log.debug("Current value before removal of parens: " + current);
	    if(current.indexOf("(") > -1){
		current = current.replaceAll(EFGImportConstants.LEFTPARENSEP,"");
	    }
	    if(current.indexOf(")") > -1){
		current = current.replaceAll(EFGImportConstants.RIGHTPARENSEP,"");
	    }
	    //log.debug("Current value after removal of parens: " + current);
	    if(current.trim().equals(state)){
		bool = true;
		break;
	    }
	}
	return bool;
    }
    //parse into min and max and check value
    private boolean matchNumber(String states){
	String patternStr = "^\\d+";
	Pattern pattern = Pattern.compile(patternStr);
	Matcher matcher = pattern.matcher(states);
	try{
	    return matcher.find(); 
	}
	catch(Exception vvv){}
	return false;
    }
    private boolean isNumericType(String fieldName){
	Connection conn =null;
	Statement stmt=null;
	ResultSet rs=null;
	StringBuffer query = new StringBuffer();
	query.append("SELECT ");
	query.append(EFGImportConstants.NUMERIC);
	query.append(" , ");
	query.append(EFGImportConstants.NUMERICRANGE);
	query.append(" FROM ");
	query.append(this.metadatasourceName);
	query.append(" where ");
	query.append(EFGImportConstants.LEGALNAME);
	query.append("='");
	query.append(fieldName);
	query.append("'");
	boolean isNumeric = false;
	try{
	    conn = EFGRDBUtils.getConnection();
	    if(conn == null){
		log.error("Connection is null");
		return false;
	    }
	    //log.debug("Query: " + query.toString()); 
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(query.toString());
	    
	    while(rs.next()){
		String numeric = rs.getString(EFGImportConstants.NUMERIC);
		String numericRange = rs.getString(EFGImportConstants.NUMERICRANGE);
		if(numeric.equalsIgnoreCase(EFGImportConstants.YES) || 
		   (numericRange.equalsIgnoreCase(EFGImportConstants.YES))){
		    isNumeric = true;
		    break;
		}
	    }
	    if (rs != null) {
		rs.close();
	    }
	    if (stmt != null) {
		stmt.close();
	    }
	    if(conn != null){
		try{
		    EFGRDBUtils.closeConnections(conn);
		}
		catch(Exception eeee){}
	    }
	}
	catch (Exception sql) {
	    LoggerUtilsServlet.logErrors(sql);
	    try{
		if (rs != null) {
		    rs.close();
		}
		if (stmt != null) {
		    stmt.close();
		}
		if(conn != null){
		    EFGRDBUtils.closeConnections(conn);
		}
	    }
	    catch(Exception exx){
	
	    }
	}
	return isNumeric;
    }
    private void buildDataDoc(String query,Hashtable table,boolean isDigir) 
	throws JDOMException {
			     
	Connection conn =null;
	Statement stmt=null;
	ResultSet rs=null;
	Document root = null;
	
	try {
	    conn = EFGRDBUtils.getConnection();
	    if(conn == null){
		log.error("Connection is null");
		return;
	    }
	    log.debug("Query: " + query);
	    log.debug("table.size(): " + table.size());
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(query);
	  
	    while(rs.next()){
		boolean isWildCard = false;
		String wildCardValue = null;
		String wildCardKey = null;
		boolean bool = false;
		if(table.size() > 0){
		
		    for (Enumeration e = table.keys() ; e.hasMoreElements() ;) {
			String fieldName = (String)e.nextElement();
			String fieldValue = (String)table.get(fieldName);
			log.debug("FieldValue: " + fieldValue);
			if(fieldValue == null){
			    continue;
			}
			String states = rs.getString(fieldName.trim());
			log.debug("States: " + states);
			if(states != null){
			    if(fieldValue.equalsIgnoreCase(EFGImportConstants.WILDCARD)){
				fieldValue = states.trim();
				log.debug("Wildcard");

				log.debug("inserting: " + states);
				wildCardValue = fieldValue;
				wildCardKey = fieldName;
				isWildCard = true;
			    }
			    if(isDigir){
				bool = true;
			    }
			    else if(states.trim().equals(fieldValue.trim())){
				bool = true;
				log.debug("State to compare: " + states.trim());
				log.debug("Fieldvalue to compare: " + fieldValue.trim());
				
				log.debug("Compare states to fieldValue returned: " + bool);
			    }
			    else if(states.indexOf(fieldValue) > - 1){
				bool = parseCompare(states.trim(),fieldValue.trim());
				log.debug("State to compare: " + states.trim());
				log.debug("Fieldvalue to compare: " + fieldValue.trim());
				
				log.debug("Compare states to fieldValue returned: " + bool);
				
			    }
			    if(!bool){
				//check if the states matches a number
				if(this.matchNumber(states)){//states contains a number
				    if(this.isNumericType(fieldName)){//the field is a numeric type
					if(isInNumericRange(states, 
							    fieldName,
							    fieldValue) != null){//if fieldValue is in the range of the states
					    bool = true;
					}
				    }
				}
			    }  
			}
			if(!bool){
			    break;
			}
		    }
		}
		else{
		    log.debug("No selections were made on user interface");
		    bool = true;
		}
		if(bool){
		    if((wildCardKey != null) && (wildCardValue != null)){
			table.put(wildCardKey,wildCardValue);
		    }
		    this.startDocumentBuild(rs,table,isDigir);  
		}
	    }
	    if (rs != null) {
		rs.close();
	    }
	    if (stmt != null) {
		stmt.close();
	    }
	    if(conn != null){
		try{
		    EFGRDBUtils.closeConnections(conn);
		}
		catch(Exception eeee){}
	    }
	}
	catch (Exception sql) {
	    LoggerUtilsServlet.logErrors(sql);
	    try{
		if (rs != null) {
		    rs.close();
		}
		if (stmt != null) {
		    stmt.close();
		}
		if(conn != null){
		    EFGRDBUtils.closeConnections(conn);
		}
	    }
	    catch(Exception exx){
	
	    }
	    
	}
    }
    protected void handleItems(Element metaFieldElem, 
			       Element items,
			       String data, 
			       String legalName, 
			       Hashtable table, boolean isDigir){
	if(metaFieldElem.getChild(EFGImportConstants.PARSERABLE).
	   getTextTrim().equalsIgnoreCase(EFGImportConstants.YES)) {
	    this.parseSearchable(items,data,legalName,table,EFGImportConstants.ANYTYPE,isDigir);
	}
	else{
	    this.addItemElement(items,data);
	}
    }						
    protected void addStatisticalMeasure(Element statsMeasures,
					 String minValue, 
					 String maxValue,
					 String units
					 ){
	if(minValue == null){
	    minValue="";
	}
	if(maxValue == null){
	    maxValue="";
	}
	if(units == null){
	    units = "";
	}
	Element ele = new Element(EFGImportConstants.STATISTICALMEASURE);
	ele.setAttribute("min",minValue.trim());
	ele.setAttribute("max",maxValue.trim());
	ele.setAttribute("units",units.trim());
	statsMeasures.addContent(ele);
    }
    /**
     * Add a State to an Items element
     *@param items - The current Items element
     *@param state - The state to be added
     */
    protected void addElementToParent(Element parent,String childElementName,
				    String childAttributeName,
				      String childAttributeValue,String state,String type){
	
	if((state == null) || (state.trim().equals(""))){
	    return;
	}
	state = state.trim();
	String itemLabel = state.replaceAll(" ", "_");
	Element ele = new Element(childElementName);
	ele.setAttribute(EFGImportConstants.CAPTION,itemLabel.trim());
	
	if((childAttributeName != null) && (!childAttributeName.trim().equals(""))){
	    if(childAttributeValue == null){
		childAttributeValue="";
	    }
	    ele.setAttribute(childAttributeName.trim(),childAttributeValue.trim());
	}
	if(type != null){
	    ele.setAttribute("type",type);
	}
	ele.addContent(state);
	parent.addContent(ele);	
    }
    protected void addMediaResourceElement(Element parent,String current, String oldCaption){
	String[] attr = this.split(current,EFGImportConstants.COLONSEP);
	String caption = oldCaption;
	if(attr.length > 1){
	    current = attr[1];
	    caption = attr[0];
	}
	this.addElementToParent(
				parent,
				EFGImportConstants.MEDIARESOURCE,
				EFGImportConstants.CAPTION,
				caption,
				current,EFGImportConstants.IMAGETYPE
				);
	if((current == null) || (current.trim().equals(""))){
	    return;
	}

	String pathToServlet = EFGServletUtils.getPathToServlet();
	String destImagesDir = pathToServlet +  File.separator + 
	    EFGImportConstants.EFGIMAGES_THUMBS +File.separator;
	
	String srcImagesDir = pathToServlet + File.separator + 
	    EFGImportConstants.EFGIMAGES + File.separator;
	String newCurrent = current.replace('/',File.separatorChar);
	ThumbNailGeneratorWrapper thw = new ThumbNailGeneratorWrapper();
	log.debug("SRC: " + srcImagesDir);
	log.debug("DEST: " + destImagesDir);
		  
	int index = newCurrent.indexOf("#");
	if(index > -1){
	    String splits[] = newCurrent.split("#");
	    for(int i = 0; i < splits.length; i++){
		boolean bool = thw.startGeneration(srcImagesDir,destImagesDir,splits[i]);
	    }
	}
	else{
	    boolean bool = thw.startGeneration(srcImagesDir,destImagesDir,newCurrent);
	}
	//add Image if it has not been already added
    }
    protected void addItemElement(Element parent, String current){
	this.addElementToParent(parent,EFGImportConstants.ITEM,null,null,current,null);
    }
    protected void addEFGListElement(Element parent, String current, String oldCaption){
	String[] attr = this.split(current,"\\|");
	String caption = oldCaption;
	if(attr.length > 1){
	    current = attr[1];
	    caption = attr[0];
	}
	this.addElementToParent(parent,EFGImportConstants.EFGLIST,EFGImportConstants.SERVICE_LINK,caption,current,null);
    }
    protected void addEFGElement(Element parent,
				 String states,
				 String legalName,
				 Hashtable table, 
				 int efgtype,
				 String caption,
				 boolean isDigir
				 ){
      
	String current  = states.replaceAll(EFGImportConstants.LEFTPARENSEP,"");
	current = current.replaceAll(EFGImportConstants.RIGHTPARENSEP,"");
      
	if(efgtype == EFGImportConstants.NUMERICTYPE){//it is a statistical measure
	    log.debug("Numeric type for: " + legalName);
	    if(table.size() > 0){
		String obj = (String)table.get(legalName);
		if(obj != null){
		    //log.debug("obj is not null and is: " + obj);
		    if(obj.equals(current.trim())){
			//log.debug("obj is equal to current: " + current);
			this.processStatisticalMeasure(parent,
						       current.trim(),legalName,table,isDigir);
		    }
		    else{
			if(isDigir){
			    this.processStatisticalMeasure(parent,
							   current.trim(),legalName,table,isDigir);
			}
		    }
		}
		else{
		    //log.debug("obj is null");
		    this.processStatisticalMeasure(parent,
						   current.trim(),legalName,table,isDigir);
		}
	    }
	    else{
		log.debug("Table size is zero");
		this.processStatisticalMeasure(parent,
					       current.trim(),legalName,table, isDigir);
	    }
	}
	else if(efgtype == EFGImportConstants.MEDIARESOURCEINTTYPE){//it is a media resource
	    if(table.size() > 0){
		String obj = (String)table.get(legalName);
		if(obj != null){
		    if(obj.equals(current.trim())){
			this.addMediaResourceElement(parent,current,caption);
		    }
		    else{
			if(isDigir){
			    this.addMediaResourceElement(parent,current,caption);
			}
		    }
		}
		else{
		    this.addMediaResourceElement(parent,current,caption);
		}   
	    }
	    else{
		this.addMediaResourceElement(parent,current,caption);
	    }
	}
	else if(efgtype == EFGImportConstants.LISTTYPE){//
	    
	    log.debug("EFG LIST TYPE");
	    if(table.size() > 0){
		String obj = (String)table.get(legalName);
		if(obj != null){
		    if(obj.equals(current.trim())){
			this.addEFGListElement(parent,current,caption);
		    }
		    else{
			if(isDigir){
			    this.addEFGListElement(parent,current,caption);
			}
		    }
		}
		else{
		    this.addEFGListElement(parent,current,caption);
		}
	    }
	    else{
		this.addEFGListElement(parent,current,caption);
	    }
	}
	else{//it is an item
	    if(table.size()  > 0){
		String obj = (String)table.get(legalName);
		if(obj != null){
		    if(obj.equals(current.trim())){
			this.addItemElement(parent,current);
		    }   
		    else{
			if(isDigir){
			    this.addItemElement(parent,current);
			}
		    }
		}
		else{
		    this.addItemElement(parent,current);
		}
	    }
	    else{
		this.addItemElement(parent,current);
	    }
	}
    }
    protected String[] split(String originalString, 
			     String separator){
	return originalString.split(separator);
    }
   /**
     *Allows a character to have more than one state 
     *@param Items - The current Items element
     *@param states - The current state to be parsed ( Contains ' or ' or ',' separators )
     *Note: Added by J. Asiedu as part of EFG
     */
    protected void parseSearchable(Element parent,String states, String legalName,Hashtable table,int efgtype, boolean isDigir){
	
	String[] parentArray = this.parseMultiples(states);
	if(parentArray == null){
	    log.error("States: " + states + " cannot be parsed!!");
	}
	String caption = null;
	for(int i = 0; i < parentArray.length;i++){
	    String state = parentArray[i];
	    //parse only when the type is a list or mediaresource
	    if(efgtype == EFGImportConstants.MEDIARESOURCEINTTYPE){
		if(state.indexOf(EFGImportConstants.COLONSEP) > -1){
		    String[] captions = this.parseMediaResourceCaptions(state);
		    if(captions.length == 2){
			caption = captions[0];
			state=captions[1];
		    }
		}
	    }
	    else if(efgtype == EFGImportConstants.LISTTYPE){
		if(state.indexOf("|") > -1){
		    String[] captions = this.parseEFGListsCaptions(state);
		    if(captions.length == 2){
			caption = captions[0];
			state=captions[1];
		    }
		}
	    }
	    this.addEFGElement(parent,state,legalName,table,efgtype,caption,isDigir);
	}
    }
    /**
     * Display Excel error messages within a Exception.
     * @param ex the exception to display
     */
    public void printExcelErrors(Exception ex) {
	LoggerUtilsServlet.logErrors(ex);
    }
    public static boolean outputDoc(Document doc, OutputStream out)
    {
	try{
	    XMLOutputter output = new XMLOutputter();
	    output.output(doc, out);
	    out.write(10);
	    return true;
	}catch(Exception e){
	    LoggerUtilsServlet.logErrors(e);
	}
        return false;
    }
  

    /*  Just for testing.*/
    public static void main (String args[]) {
	LoggerUtils utils = new LoggerUtils();
	try {
	    String datasourceName = args[0];
	    String metadatasourceName = args[1];

	    String metaQuery = "SELECT * FROM " + metadatasourceName;
	    String docQuery = "SELECT * FROM " + datasourceName;
	    
	    SQL2XML de = new SQL2XML(datasourceName,metadatasourceName,true);
	    Document metadataDoc = de.getMetadataDoc(metaQuery);
	    Document dataDoc = de.getDataDoc(docQuery,metaQuery,new Hashtable(),false);
       

	    outputDoc(metadataDoc, System.out);
	    System.out.println("\n\n");
	    
	    outputDoc(dataDoc, System.out);
	    System.out.println("\n\n");
	}
	catch(Exception ee){
	    LoggerUtilsServlet.logErrors(ee);
	    System.err.println(ee.getMessage());
	}
    }
}


