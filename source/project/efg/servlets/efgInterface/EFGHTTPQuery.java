/**
 * $Id$
 * $Name$
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
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
package project.efg.servlets.efgInterface;
import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import project.efg.efgDocument.DatasourceType;
import project.efg.efgDocument.DatasourcesType;
import project.efg.efgDocument.EFGDocument;
import project.efg.efgDocument.TaxonEntries;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.util.EFGImportConstants;
import project.efg.util.RegularExpresionConstants;

/**
 * @author kasiedu
 *
 */
public abstract class EFGHTTPQuery {
	
	private EFGDocument efgDocument;
	protected String datasourceName;
	protected String displayName;
	protected String metadatasourceName;
	private String mainTableName;
	static Logger log = null;
	static {
		try {
			//log = Logger.getLogger(EFGHTTPQuery.class);
		} catch (Exception ee) {
			//log.error(ee.getMessage());
			LoggerUtilsServlet.logErrors(ee);
		}
	}
	/**
	 * Replace % sign in the string with a space character
	 * @param toReplace
	 * @return replaced string
	 */
	public String replacePercent(String toReplace){
		
			return toReplace.replaceAll(RegularExpresionConstants.HTTP_QUERY_PERCENT_SIGN,
					RegularExpresionConstants.REPLACEMENT_PERCENT_SIGN);
		
	}
	public void setMainDataTableName(String mainTableName) {
		this.mainTableName = mainTableName;
	}
	public String getMainTableName() {
		
		return this.mainTableName;
	}
	public EFGHTTPQuery(HttpServletRequest req){
		String allTableName= req.getParameter(EFGImportConstants.ALL_TABLE_NAME);
		if(allTableName != null && !allTableName.trim().equals("")){
			this.setMainDataTableName(allTableName);
		}
		else {
			this.setMainDataTableName(EFGImportConstants.EFG_RDB_TABLES);
		}
		 String query = this.buildQuery(req);
		 //	call cache here
		 //System.out.println("Query: " + query);
		 this.efgDocument = new EFGDocument();
		 //log.debug("Query: " + query);
		 TaxonEntries entries = this.executeQuery(query);
		
		 if(entries != null){
			 this.efgDocument.setTaxonEntries(entries);
		 }
		 this.addDatasources();
		
	}
	public EFGDocument getEFGDocument(){
		return this.efgDocument;	
	}
	/**
	 * Factory methods
	 * @param req - The request to parse into the appropriate query
	 * @return a query built for the appropriate system
	 */
	protected abstract String buildQuery(HttpServletRequest req);
	/**
	 * @return an EFGDocument holding the results of executing the query
	 */
	protected abstract TaxonEntries executeQuery(String query);
	/**
	 * 
	 * @return the EFGDocument object built from the query
	 */
	protected boolean matchNumber(String states) {
		try {
			Matcher matcher = RegularExpresionConstants.matchNumberPattern.matcher(states);
			return matcher.find();
		} catch (Exception vvv) {
		}
		return false;
	}
	private void addDatasources(){
		 if(this.datasourceName != null){
			 DatasourcesType datasourceTypes = new DatasourcesType();
			 DatasourceType datasourceType = new DatasourceType();
			 datasourceType.setName(this.datasourceName);
			 datasourceType.setEfgKey(1);
			 datasourceTypes.addDatasource(datasourceType);
			 this.efgDocument.setDatasources(datasourceTypes);
		 }
	}
	 
	
}
