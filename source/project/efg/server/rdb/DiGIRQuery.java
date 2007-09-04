/**
 * $Id: DiGIRQuery.java,v 1.1.1.1 2007/08/01 19:11:23 kasiedu Exp $
 * $Name:  $
 *
 * Copyright (c) 2006  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
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
package project.efg.server.rdb;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.xml.sax.InputSource;

import project.efg.server.digir.DigirParserHandler;
import project.efg.server.digir.EFGString;
import project.efg.server.factory.EFGSpringFactory;
import project.efg.server.interfaces.ServletAbstractFactoryInterface;
import project.efg.server.servlets.EFGContextListener;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;
import project.efg.util.interfaces.RegularExpresionConstants;

/**
 * @author kasiedu
 *
 */
public class DiGIRQuery extends SQLQuery {
	
	  private ServletAbstractFactoryInterface servFactory;
	
	/**
	 * @param req
	 */
	public DiGIRQuery(HttpServletRequest req) {
		super(req);
	
	}

	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGHTTPQuery#buildQuery(javax.servlet.http.HttpServletRequest)
	 */
	public String buildQuery(HttpServletRequest req) {
		
		this.datasourceName  = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
		this.displayName  = req.getParameter(EFGImportConstants.DISPLAY_NAME);
		
		try{
			this.initQueryParameters();
		}
		catch(Exception ee){
			//log.error(ee.getMessage());
			LoggerUtilsServlet.logErrors(ee);
		}
		String digirRequest = req.getParameter(EFGImportConstants.DIGIR);
		
		    InputSource source = new InputSource(new StringReader(digirRequest));
		    DigirParserHandler dph = new DigirParserHandler(source);
		   
		    if((dph.getErrorCode() != -1)&&(dph.getDataSourceErrorCode() != -1)){
		    	String digirQuery = processDigirRequest(dph);
				List dataSources = dph.getRequestedDataSources(); // get list of
																	// DataSources if
																	// any
				if (dataSources.size() == 0) {// means user did not specify any datasource
					
					if(this.servFactory == null){
						this.servFactory =
							EFGSpringFactory.getServletAbstractFactoryInstance();
						this.servFactory.setMainDataTableName(this.getMainTableName());
					}
					dataSources = toLists(this.servFactory.getListOfDatasources());
				}
				
				Iterator dsNameIter = dataSources.iterator();
				String sqlString = null;
				while (dsNameIter.hasNext()) {
					this.datasourceName = (String) dsNameIter.next();
					this.displayName = null;
					this.metadatasourceName = null;
					try{
						this.initQueryParameters();
					}
					catch(Exception ee){
						
					}
					
					
					sqlString = getSQLQuery(
								digirQuery,
								this.datasourceName);
						if (sqlString != null && !sqlString.trim().equals("")) {
							break;// handle only one datasource for now..
						}
				}
				return sqlString;
		    }
			try{
				ServletContext context = req.getSession().getServletContext();
				 context.log("Error code is: " + dph.getErrorCode());
			}
			catch(Exception ee){
				
			}
		return null;
	}

	/**
	 * Parses and Processes all DiGIR requests
	 * 
	 * @param DigirParserHandler
	 *            which is an implementation of a SAXParser Handler
	 * @return a String containing the DiGIR query
	 */
	private String processDigirRequest(DigirParserHandler dph) {
		//log.debug("Inside Digir request");
		Stack stack = dph.getStack();
		StringBuffer buf = new StringBuffer();

		while (!stack.empty()) {
			EFGString efg = (EFGString) stack.pop();
			if (efg != null) {
				buf.append(efg.toString());
			}
		}
		////log.debug("Inside Digir request: " + buf.toString());
		return buf.toString();
	}
    /**
     * Map - map of fieldName as key and legal name as 
     * value
     * 
     * @param datasourceName
     * @return
     */
    private Map makeLegalNameMap(){
    	Map map = new HashMap();
    	List list = this.servFactory.getAllFields(this.displayName, this.datasourceName);
    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			EFGQueueObjectInterface queue = (EFGQueueObjectInterface)iterator.next();
			String legalName = queue.getObject(0);
			String key = queue.getObject(1);
			map.put(key.toLowerCase(), legalName);
		}
    	return  map;
    }
	protected String getSQLQuery(
			String digirQuery,
			String datasource) {

		
		StringBuffer queryString = new StringBuffer();
		Map map = makeLegalNameMap();
		String arr[] = RegularExpresionConstants.spacePattern.split(digirQuery);
		for (int i = 0; i < arr.length; i++) {
			String keyword = (String) EFGContextListener.getKeyWordValue(arr[i]);
			
			if (keyword != null) {// if it is a keyword in the properties file
				if ((keyword.trim()).equalsIgnoreCase("and")
						|| (keyword.trim()).equalsIgnoreCase("or")
						|| (keyword.trim()).equalsIgnoreCase("or not")
						|| (keyword.trim()).equalsIgnoreCase("and not")) {// it
																			// is a
																			// lop
					queryString.append(" " + keyword + " ");
				} else { // it is a cop operator
					String fieldName = arr[i - 1];
					if (fieldName.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1) {
						fieldName = fieldName.replaceAll(
								EFGImportConstants.SERVICE_LINK_FILLER, " ");
					}
					//convert to javaIdentifier
					//make a sqlquery here
					//String legalName = EFGUtils.encodeToJavaName(fieldName);
    				String legalName = (String)map.get(fieldName.toLowerCase());
    				if(legalName == null){
    					continue;
    				}
					queryString.append("( ");
					queryString.append(legalName);
					queryString.append(" ");
				
						
						if ((i + 1) < arr.length) {// get the right column name
													// from the mapping table
							String pVal = arr[i + 1];
							if ((pVal == null )|| ("".equals(pVal.trim()))) {
								continue;
							}
							
							if(this.paramValuesTable.containsKey(legalName)){//could have parameter with multiple values
								String oldVal = (String)paramValuesTable.get(legalName);
								if(oldVal.indexOf(pVal) == -1){//does not exists
									pVal = oldVal + RegularExpresionConstants.PIPESEP + pVal;
									paramValuesTable.put(legalName,pVal);
								}
							}
							else{
								this.paramValuesTable.put(legalName,pVal);
							}
							queryString.append("\")");
							queryString.append(pVal);
							queryString.append("\")");
						}
				}
			} else {
				if ((arr[i].trim().equals("(")) || (arr[i].trim().equals(")"))) {
					String toAppend = arr[i];

					if (toAppend
							.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1) {
						toAppend = toAppend.replaceAll(
								EFGImportConstants.SERVICE_LINK_FILLER, " ");
					}
					queryString.append(toAppend);
				}
			}
		}
		StringBuffer insertB = new StringBuffer();
		insertB.append("SELECT * FROM ");
		insertB.append(datasource);
		
		if(!queryString.toString().trim().equals("")){	
			insertB.append(" where ");
			queryString.insert(0,insertB.toString());
		}
		//log.debug("query: " + queryString.toString());
		return queryString.toString();
	}
}
