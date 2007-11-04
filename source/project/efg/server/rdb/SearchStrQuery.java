/**
 * $Id$
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

import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.RegularExpresionConstants;

/**
 * @author kasiedu
 *
 */
public class SearchStrQuery extends SQLQuery {

	/**
	 * @param req
	 */
	public SearchStrQuery(HttpServletRequest req) {
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
			
			LoggerUtilsServlet.logErrors(ee);
		}
		StringBuffer querySB = new StringBuffer("SELECT * FROM ");
		querySB.append(this.datasourceName);
		
		String searchStrVal = req.getParameter(EFGImportConstants.SEARCHSTR);
		String moreQuery = this.getSearchStrParamsQuery(searchStrVal);
		if(moreQuery != null){
			querySB.append(moreQuery);
		}
		
		return querySB.toString();
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
	protected String getSearchStrParamsQuery(String searchStrValue) {
		StringBuffer querySB = new StringBuffer();
		
		int paramNo = 0;
		int paramCount = paramNo;
		// starting to construct the taxon query from parameters like
		// 
		if (paramNo == 0){
			querySB.append(" where ((");
		}
		else{
			querySB.append(" and ((");
		}
	
		StringTokenizer st = new StringTokenizer(searchStrValue, "()=&| ", true);
		int andCount = 0;
		int orCount = 0;
		while (st.hasMoreTokens()) {
			String current = st.nextToken();
			if (current.equals("(") || current.equals(")")){
				
			}
			else if (current.equals(" ") || current.equals("&")
					|| current.equals("|")) {
				if (current.equals("&")) {
					andCount++;
					if (andCount == 2) {
						querySB.append(" and (");
						andCount = 0;
					}
				} else if (current.equals("|")) {
					orCount++;
					if (orCount == 2) {
						querySB.append(" or (");
						orCount = 0;
					}
				}
			} else {
				String legalName = current;
				if (legalName == null || "".equals(legalName.trim())){
					return null;
				}
				querySB.append(legalName + " LIKE ");
				current = st.nextToken();
				while (current.equals(" ") || current.equals("=")){
					current = st.nextToken();
				}
				if ((current == null )|| ("".equals(current.trim()))) {
					continue;
				}
				//if current is a number or contains a number
				//use %
				querySB.append("\"%");
				if(this.matchNumber(current)){
					querySB.append("\")");// value
				}
				else{
					querySB.append(current.trim() + "%\")");
				}
				if(this.paramValuesTable.containsKey(legalName)){//could have parameter with multiple values
					String oldVal = (String)paramValuesTable.get(legalName);
					if(oldVal.indexOf(current) == -1){//does not exists
						current = oldVal + RegularExpresionConstants.PIPESEP + current;
						paramValuesTable.put(legalName,current);
					}
				}
				else{
					this.paramValuesTable.put(legalName,current);
				}
				
				
				paramNo++; // increment the number of search conditions
			}
		} // while ends
		if (paramCount < paramNo){ //new params added in searchStr query
			querySB.append(")");
		}
		return querySB.toString();
	}

}
