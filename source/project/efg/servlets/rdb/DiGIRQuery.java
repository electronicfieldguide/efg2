/**
 * $Id$
 * $Name$
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
package project.efg.servlets.rdb;

import java.io.StringReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import project.efg.digir.DigirParserHandler;
import project.efg.digir.EFGString;
import project.efg.servlets.efgImpl.EFGContextListener;
import project.efg.servlets.efgInterface.ServletAbstractFactoryInterface;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.servlets.factory.ServletAbstractFactoryCreator;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class DiGIRQuery extends SQLQuery {
	  static Logger log = null;
	  private ServletAbstractFactoryInterface servFactory;
	  static{
		try{
		    log = Logger.getLogger(DiGIRQuery.class); 
		}
		catch(Exception ee){
		}
	   }
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
		this.paramValuesTable = new Hashtable();
		this.datasourceName  = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
		this.displayName  = req.getParameter(EFGImportConstants.DISPLAY_NAME);
		
		try{
			this.initQueryParameters();
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			LoggerUtilsServlet.logErrors(ee);
		}
		String digirRequest = req.getParameter(EFGImportConstants.DIGIR);
		log.debug("Digir request: " + digirRequest);
		
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
							ServletAbstractFactoryCreator.getInstance();
					}
					dataSources = toLists(this.servFactory.getListOfDatasources());
					//return the first one 
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
					//String metadataSource = this.getMetadataTableName(dataSource);
					
					Set set = this.getCategorical(this.metadatasourceName);
					this.paramValuesTable = new Hashtable();
					sqlString = getSQLQuery(
								digirQuery,
								this.datasourceName, 
								this.metadatasourceName,
								set);
						if (sqlString != null && !sqlString.trim().equals("")) {
							break;
						}
				}
				return sqlString;
		    }
		    else {
		       log.error("Error code is: " + dph.getErrorCode());
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
		log.debug("Inside Digir request");
		Stack stack = dph.getStack();
		StringBuffer buf = new StringBuffer();

		while (!stack.empty()) {
			EFGString efg = (EFGString) stack.pop();
			if (efg != null) {
				buf.append(efg.toString());
			}
		}
		//log.debug("Inside Digir request: " + buf.toString());
		return buf.toString();
	}
	protected String getSQLQuery(
			String digirQuery,
			String datasource,
			String metadataSource, 
			Set set) {

		
		StringBuffer queryString = new StringBuffer();
		Hashtable mapping = this.getNameMapping(metadataSource);
		String arr[] = (digirQuery).split("\\s");
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
					String name = arr[i - 1];
					if (name.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1) {
						name = name.replaceAll(
								EFGImportConstants.SERVICE_LINK_FILLER, " ");
					}
					String legalName = (String)mapping.get(name);
					if ((legalName == null) || ("".equals(legalName.trim()))) {
						continue;
					}

					queryString.append("( " + legalName + " ");
					if (set.contains(legalName.trim())) {
						
						if ((i + 1) < arr.length) {// get the right column name
													// from the mapping table
							String pVal = arr[i + 1];
							if (pVal
									.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1) {
								pVal = pVal.replaceAll(
										EFGImportConstants.SERVICE_LINK_FILLER,
										" ");
							}
							if ((pVal == null )|| ("".equals(pVal.trim()))) {
								continue;
							}
							queryString.append(" LIKE ");
							queryString.append("'%");
							if(this.matchNumber(pVal)){
								queryString.append("')");// value
							}
							else{
								queryString.append(pVal + "%')");
							}
							if(this.paramValuesTable.containsKey(legalName)){//could have parameter with multiple values
								String oldVal = (String)paramValuesTable.get(legalName);
								if(oldVal.indexOf(pVal) == -1){//does not exists
									pVal = oldVal + EFGImportConstants.PIPESEP + pVal;
									paramValuesTable.put(legalName,pVal);
								}
							}
							else{
								this.paramValuesTable.put(legalName,pVal);
							}
							//queryString.append("'%')");// value
						}
					} else {
						queryString.append(keyword.trim() + " ");
						if ((i + 1) < arr.length) {// get the right column name
													// from the mapping table
							String pVal = arr[i + 1];
							if (pVal
									.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1) {
								pVal = pVal.replaceAll(
										EFGImportConstants.SERVICE_LINK_FILLER,
										" ");
							}
							if ((pVal == null )|| ("".equals(pVal.trim()))) {
								continue;
							}
							queryString.append("'%");
							if(this.matchNumber(pVal.trim())){
								queryString.append("')");// value
							}
							else{
								queryString.append(pVal.trim() + "%')");
							}
							if(this.paramValuesTable.containsKey(legalName)){//could have parameter with multiple values
								String oldVal = (String)paramValuesTable.get(legalName);
								if(oldVal.indexOf(pVal) == -1){//does not exists
									pVal = oldVal + EFGImportConstants.PIPESEP + pVal;
									paramValuesTable.put(legalName,pVal);
								}
							}
							else{
								this.paramValuesTable.put(legalName,pVal);
							}
							//queryString.append("'" + pVal.trim() + "')");// value
						}
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
		log.debug("query: " + queryString.toString());
		return queryString.toString();
	}
}
