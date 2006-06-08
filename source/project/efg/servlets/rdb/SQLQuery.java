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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
//import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface;
import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.efgDocument.TaxonEntries;
import project.efg.efgDocument.TaxonEntryType;
import project.efg.efgDocument.TaxonEntryTypeItem;
import project.efg.servlets.efgInterface.EFGHTTPQuery;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.servlets.factory.TaxonEntryItemBuilder;
import project.efg.util.EFGImportConstants;
import project.efg.util.EFGObject;
import project.efg.util.UnicodeToASCIIFilter;

/**
 * @author kasiedu
 *
 */
public class SQLQuery extends EFGHTTPQuery {
	protected Hashtable paramValuesTable; //holds the parameter values of this query

	private Collection specialParams;

	protected QueryExecutor queryExecutor;

	protected UnicodeToASCIIFilter filter;

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(SQLQuery.class);
		} catch (Exception ee) {
		}
	}

	/**
	 * Constructor.
	 */
	public SQLQuery(HttpServletRequest req) {
		super(req);

	}

	/**
	 * This method builds a query from the request such as
	 * /efg/servlet/search?Genus=Solanum&Species=chrysotrichum
	 * /efg/servlet/search?searchStr=( Genus=Solanum && Species=chrysotrichum )
	 * 
	 * @param req
	 *            the servlet request object
	 * @return the query string
	 */
	public String buildQuery(HttpServletRequest req) {
		this.datasourceName = req
				.getParameter(EFGImportConstants.DATASOURCE_NAME);
		this.displayName = req.getParameter(EFGImportConstants.DISPLAY_NAME);

		if (!this.initQueryParameters()) {
			return null;
		}

		String maxDispStr = req.getParameter(EFGImportConstants.MAX_DISPLAY);
		int maxDisplay = this.getMaxDisplay(maxDispStr);

		StringBuffer querySB = new StringBuffer();
		try {
			querySB.append(this.getCommonQuery());
			
			Enumeration paramEnum = req.getParameterNames();
			int paramNo = 0;

			while (paramEnum.hasMoreElements()) {
				String legalName = (String) paramEnum.nextElement();
				log.debug("paramName: " + legalName);
				if (isIgnoreParam(legalName)) {//ignore this parameter name
					continue;
				}

				if (legalName.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1) {
					legalName = legalName.replaceAll(
							EFGImportConstants.SERVICE_LINK_FILLER, " ");
				}
				String[] paramValues = req.getParameterValues(legalName);
				log.debug("paramaValues length: " + paramValues.length);

				String orBuffer = this.getORQuery(paramValues, legalName);

				if (!"".equals(orBuffer.trim())) {
					if (paramNo == 0) {
						querySB.append(" where ");
						querySB.append("( ");
						querySB.append(orBuffer);
						querySB.append(" ) ");
					} else {
						querySB.append(" and ( ");
						querySB.append(orBuffer);
						querySB.append(" ) ");
					}
					++paramNo;
				}

			}
			if (maxDisplay > 0) {
				String database = EFGImportConstants.EFGProperties
						.getProperty("database");
				if (EFGImportConstants.MYSQL.equalsIgnoreCase(database)) {
					querySB.append(" limit ");
					querySB.append(maxDisplay + "");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			LoggerUtilsServlet.logErrors(e);
		}
		log.debug("Query: " + querySB.toString());
		return querySB.toString();
	}

	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGHTTPQuery#executeQuery()
	 */
	public TaxonEntries executeQuery(String query) {

		try {
			
			return this.buildDocument(query);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
/**
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public TaxonEntries buildDocument(String query) throws Exception {

		Hashtable typeTable = getDataTypes(this.metadatasourceName);
		TaxonEntryItemBuilder itemBuilder = new TaxonEntryItemBuilder();
		log.debug("Query: " + query);
		SqlRowSet rowset = this.queryExecutor.executeQueryForRowSet(query);
		SqlRowSetMetaData metadata = rowset.getMetaData(); // Get metadata

		TaxonEntries taxonEntries = new TaxonEntries();// on
		int tableSize = this.paramValuesTable.size();
		log.debug("paramvaluesTable Size: " + tableSize);

		int w = 0;
		while (rowset.next()) {
			//for each row enumerate the members of the hashtable and if the row matches any of the me
			//members of the hashtable process it, otherwise skip it
			//skip the row if the paramValue  exists and is not matched
			//Add a recordID
		
			TaxonEntryType taxonEntry = new TaxonEntryType();
			Enumeration enums = this.paramValuesTable.keys();
			boolean isAnd = false;
			List orLists = new ArrayList();
			boolean isParamSkip = false; // a flag to indicate that we should continue
			//processing even though the orLists may be empty. This occurs in cases where there is
			//a wild card parameter value or when there is only a uniqueID specified, like when going directly
			//from a Search page to a taxon page
			if(tableSize > 0){
				while (enums.hasMoreElements()) {//loop through the client request parameters

					String legalName = (String) enums.nextElement();
					String paramVals = (String) this.paramValuesTable.get(legalName
						.toLowerCase());
					boolean isFound = false;
					if(paramVals == null){
						log.debug("paramVals is null..skipping");
						continue;//skip client request parameter
					}
					if (EFGImportConstants.UNIQUEID_STR.equalsIgnoreCase(legalName)) {
						isFound = true;
						isParamSkip = true;
						log.debug("UniqueID field.Setting isParamSkip to true");
					}
					else if (paramVals.trim().equals("")) {//means match everything
						isFound = true;
						isParamSkip = true;
						log.debug("paramVals.trim() is empty.Setting isParamSkip to true");
					}
					else{
						//parsing is done for the cases where a parameter has more than one 
						//value separated by the pipe separator
						log.debug("Before splitting: " + paramVals);
						String[] params = paramVals.split(EFGImportConstants.PIPESEP);
						isFound  = processStates(params, 
								legalName,
								rowset,
								typeTable,
								itemBuilder,
								orLists);
					}
					isAnd = isFound;
					if (!isAnd) {
						/*
						 *means that no match was found for some client 'and' request
						 * We need to exit the loop;
						 **/
							log.debug("Breaking..Stop processing");
							break; //out of inner while
					}
				}//end inner while
			}
			else{//means get everything
				isAnd = true;
				isParamSkip = true;
			}
			if (!isAnd) {/*
						*means that no match was found for some client request
						* We need to skip this row;
						**/
					continue;
			}
			if(((this.addORListsToTaxonEntry(taxonEntry, orLists)) || (isParamSkip))){
				try {
					log.debug("About to add other items");
					this.addOtherColumns(rowset, metadata, taxonEntry, typeTable,
							itemBuilder);

					if (taxonEntry.getTaxonEntryTypeItemCount() > 0) {
						taxonEntries.addTaxonEntry(taxonEntry);
					}
				} catch (Exception eex) {
					log.error(eex.getMessage());
				}
			}
			else{//means empty orLists and we cannot skip the client parameter
				continue;
			}
			++w;
		}
		if (taxonEntries.getTaxonEntryCount() > 0) {
			return taxonEntries;
		}
		return null;
	}

	protected boolean addORListsToTaxonEntry(TaxonEntryType taxonEntry,
			List orLists) {
		boolean isSuccess = false;
		if ((orLists == null) || (orLists.size() == 0)) {
			return isSuccess;
		}

		for (int i = 0; i < orLists.size(); i++) {
			TaxonEntryTypeItem taxonEntryItem = (TaxonEntryTypeItem) orLists
					.get(i);
			if (taxonEntryItem != null) {
				taxonEntry.addTaxonEntryTypeItem(taxonEntryItem);
				isSuccess = true;//if any one is not null
			}
		}

		return isSuccess;
	}

	protected Hashtable getDataTypes(String metadatasourceName)
			throws Exception {
		Hashtable typeTable = new Hashtable();

		StringBuffer buffer = new StringBuffer();
		buffer
				.append("Select name,legalName,isLists,NumericValue,NumericRange,MediaResource,Categorical,Narrative From ");
		buffer.append(metadatasourceName);

		log.debug("Query: " + buffer.toString());

		SqlRowSet rowset = this.queryExecutor.executeQueryForRowSet(buffer
				.toString());
		SqlRowSetMetaData metadata = rowset.getMetaData(); // Get metadata
		int numcols = metadata.getColumnCount(); // How many columns?
		log.debug("Number of Columns: " + numcols);

		while (rowset.next()) {
			String dataType = null;
			String legalName = rowset.getString("legalName");
			String name = rowset.getString("name");
			String state = rowset.getString("isLists");
			if (state.equalsIgnoreCase("true")) {//if it is a lists return it
				dataType = "isLists";
				EFGObject efgObject = new EFGObject();
				efgObject.setDatabaseName(legalName);
				efgObject.setName(name);
				efgObject.setDataType(dataType);
				typeTable.put(legalName.toLowerCase().trim(), efgObject);
			} else {
				for (int i = 1; i <= numcols; i++) {
					String columnName = metadata.getColumnName(i);
					if (columnName.equalsIgnoreCase("legalName")) {
						continue;
					}
					//log.debug("ColumnName: " + columnName);
					state = rowset.getString(columnName);
					//log.debug("State: " + state);
					if (state.equalsIgnoreCase("true")) {
						log.debug("Found datatype for legalName: " + legalName
								+ " and it is '" + columnName + "'");
						dataType = columnName;
						EFGObject efgObject = new EFGObject();
						efgObject.setDatabaseName(legalName);
						efgObject.setName(name);
						efgObject.setDataType(dataType);
						typeTable
								.put(legalName.toLowerCase().trim(), efgObject);
						break;
					}
				}
			}
			if (dataType == null) {
				log
						.debug("Datatype for legalName: " + legalName
								+ " not found");
			}
		}
		return typeTable;
	}

	protected List toLists(EFGDatasourceObjectListInterface listinter) {
		List list = new ArrayList(listinter.getCount());
		for (java.util.Iterator iter = listinter
				.getEFGDatasourceObjectListIterator(); iter.hasNext();) {
			EFGDatasourceObjectInterface queue = (EFGDatasourceObjectInterface) iter
					.next();

			list.add(queue.getDisplayName());
		}
		return list;
	}

	/**
	 * Set the datasourceName and the metadataSourceName if they were not passed
	 * in the query request.
	 * @throws Exception 
	 *
	 */
	protected boolean initQueryParameters() {
		try {
			this.addSpecialParams();
			this.queryExecutor = new QueryExecutor();
			this.filter = new UnicodeToASCIIFilter();
			this.paramValuesTable = new Hashtable();
			String efgRDBTable = EFGImportConstants.EFGProperties
					.getProperty("ALL_EFG_RDB_TABLES");
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append("SELECT DS_DATA,DS_METADATA,DISPLAY_NAME FROM ");
			queryBuffer.append(efgRDBTable);

			if ((this.displayName != null)
					&& (!this.displayName.trim().equals(""))) {
				queryBuffer.append(" WHERE DISPLAY_NAME ='");
				queryBuffer.append(this.displayName);
				queryBuffer.append("'");
			} else if ((this.datasourceName != null)
					&& (!this.datasourceName.trim().equals(""))) {
				queryBuffer.append(" WHERE DS_DATA ='");
				queryBuffer.append(this.datasourceName);
				queryBuffer.append("'");
			}
			log.debug("Query: " + queryBuffer.toString());
			List lists = this.queryExecutor.executeQueryForList(queryBuffer
					.toString(), 3);
			for (java.util.Iterator iter = lists.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
						.next();
				this.datasourceName = queue.getObject(0);
				this.metadatasourceName = queue.getObject(1);
				this.displayName = queue.getObject(2);
				break;
			}
			return true;
		} catch (Exception ee) {
			log.error(ee.getMessage());
			LoggerUtilsServlet.logErrors(ee);
		}
		return false;
	}

	protected Hashtable getNameMapping(String metadataSource) {
		Hashtable mapping = new Hashtable();

		String metaQuery = "SELECT NAME,LEGALNAME FROM " + metadataSource;
		log.debug("Query: " + metaQuery);
		try {
			List lists = this.queryExecutor.executeQueryForList(metaQuery, 2);
			for (java.util.Iterator iter = lists.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
						.next();
				String name = queue.getObject(0);
				String legalName = queue.getObject(1);
				mapping.put(name, legalName);
			}

		} catch (Exception mex) {
			log.error(mex.getMessage());
			LoggerUtilsServlet.logErrors(mex);
		}
		return mapping;
	}

	protected Set getCategorical(String metadataSource) {
		Set set = new HashSet();
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT legalName FROM ");
		queryBuffer.append(metadataSource);
		queryBuffer.append(" where ");
		queryBuffer.append(EFGImportConstants.CATEGORICAL);
		queryBuffer.append("='");
		queryBuffer.append(EFGImportConstants.YES);
		queryBuffer.append("')");

		try {
			log.debug("Select query: " + queryBuffer.toString());
			List lists = this.queryExecutor.executeQueryForList(queryBuffer
					.toString(), 1);
			for (java.util.Iterator iter = lists.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
						.next();
				String legalName = queue.getObject(0);
				set.add(legalName.trim());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			LoggerUtilsServlet.logErrors(e);
		}
		return set;
	}

	private boolean processStates(String[] params, 
			String legalName,
			SqlRowSet rowset,
			Hashtable typeTable,
			TaxonEntryItemBuilder itemBuilder,List orLists) throws Exception{
		EFGObject efgObject = (EFGObject)typeTable.get(legalName
				.trim().toLowerCase());
		if (efgObject == null) {
			log.debug("EFGObject is null for : " + legalName);
		}
		boolean isFound = false;
		for (int j = 0; j < params.length; j++) {
			/* iterate over all of the 'or' requests 
			 * If  any of them return true add 
			 * begin processing of the row
			 * You need to check that all of the and's also return true
			 * before you create an xml document for that row
			 */
	
			String paramValue = params[j];
			log.debug("paramValue: " + paramValue);
			log.debug("LegalName: " + legalName);
			String states = rowset.getString(legalName);
			log.debug("States: " + states);
	
			if (states != null) {
				StringWriter writer = new StringWriter();
				this.filter.filter(new StringReader(states),
						writer);
				states = writer.getBuffer().toString();
				log
						.debug("States after filteration: "
								+ states);
				log.debug("efgObject types: " + " DBNAME: "
						+ efgObject.getDatabaseName()
						+ " NAME: " + efgObject.getName()
						+ " TYPE: " + efgObject.getDataType());
				TaxonEntryTypeItem taxonEntryItem = itemBuilder
						.buildTaxonEntryItem(paramValue,
								states, efgObject);
				if (taxonEntryItem != null) {
					orLists.add(taxonEntryItem);
					if (!isFound) {
						log.debug("match found for state: "
								+ paramValue
								+ " and columnName: "
								+ legalName);
						isFound = true;
					}
				} else {
					log.debug("No match found for state: "
							+ paramValue + " and columnName: "
							+ legalName);
				}
			}
		}
		return isFound;
	}

	private String getORQuery(String[] paramValues, String legalName) {
		int orCounter = 0;
		StringBuffer orBuffer = new StringBuffer();
		//iterate over it and add ors where necessary
		for (int jj = 0; jj < paramValues.length; jj++) {//add or query

			String pVal = paramValues[jj];
			log.debug("pVal: " + pVal);
			if (pVal == null) {
				log.debug("skiping");
				continue;
			}

			if (pVal.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1) {
				pVal = pVal.replaceAll(EFGImportConstants.SERVICE_LINK_FILLER,
						" ");
			}
			if(pVal.indexOf(EFGImportConstants.EFG_ANY) > -1){
				log.debug("Contains any will replace it with a wildcard");
				pVal = pVal.replaceAll(EFGImportConstants.EFG_ANY,
				" ");
			}
			if (orCounter > 0) {//this is not the first time we are here
				orBuffer.append(" OR ");
			}

			orBuffer.append(" ( ");
			orBuffer.append(legalName);
			if (EFGImportConstants.UNIQUEID_STR.equalsIgnoreCase(legalName)) {//handle uniqueid
				orBuffer.append("=");
				orBuffer.append(pVal);
			} else {
				orBuffer.append(" LIKE ");
				orBuffer.append("'%");
				if ((this.matchNumber(pVal)) || ("".equals(pVal.trim()))) {//if there is a number or if it is blank
					orBuffer.append("'");// match on 'any'
				} else {
					orBuffer.append(pVal + "%'");
				}
			}
			orBuffer.append(" ) ");
			if (!"".equals(pVal.trim())) {
				if (this.paramValuesTable.containsKey(legalName.toLowerCase())) {//could have parameter with multiple values
					String oldVal = (String) paramValuesTable.get(legalName
							.toLowerCase());
					if (oldVal.indexOf(pVal) == -1) {//does not exists
						log.debug("pval already exists in paramValuesTable: "
								+ pVal);
						pVal = oldVal + EFGImportConstants.PIPESEP_PARSE + pVal;
						log.debug("pval in paramValuesTable is now: " + pVal);
						this.paramValuesTable
								.put(legalName.toLowerCase(), pVal);
					}
				} else {
					this.paramValuesTable.put(legalName.toLowerCase(), pVal);
				}
			}
			++orCounter;
		}
		return orBuffer.toString().trim();
	}

	private boolean isIgnoreParam(String paramName) {
		if ((specialParams.contains(paramName))
				|| (paramName.equals(EFGImportConstants.SEARCHSTR))
				|| (paramName == null) || ("".equals(paramName.trim()))) {
			log.debug("Skipping current paramName: " + paramName);
			return true;
		}
		return false;
	}

	private String getCommonQuery() {
		StringBuffer commonBuffer = new StringBuffer();
		commonBuffer.append("SELECT * FROM ");
		commonBuffer.append(this.datasourceName);
		return commonBuffer.toString();
	}
	
	private int getMaxDisplay(String maxDispStr) {
		int maxDisplay = -1;
		
		if (maxDispStr != null) {
			try {
				if (!EFGImportConstants.MAX_DISPLAY_IGNORE
						.equalsIgnoreCase(maxDispStr.trim())) {
					maxDisplay = Integer.parseInt(maxDispStr);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return maxDisplay;
	}

	private void addOtherColumns(SqlRowSet rowset, SqlRowSetMetaData metadata,
			TaxonEntryType taxonEntry, Hashtable typeTable,
			TaxonEntryItemBuilder itemBuilder) throws Exception {
		/* 
		 * If we are here means that a match has been found in
		 * DB from the client 'OR' and 'AND" requests for the 
		 * current row so we create xml elements out of it.
		 */
		int numcols = metadata.getColumnCount(); // How many columns?	 
		for (int i = 1; i <= numcols; i++) {
			String columnName = metadata.getColumnName(i);
			String states = rowset.getString(columnName);//States in current column
			if (columnName.equalsIgnoreCase(EFGImportConstants.UNIQUEID_STR)) {
				if ((taxonEntry.getRecordID() == null)
						|| (taxonEntry.getRecordID().trim().equals(""))) {
					taxonEntry.setRecordID(states);
				}
				continue;
			}
			if (this.paramValuesTable.get(columnName.toLowerCase()) != null) {//already processed
				log.debug("Skip column name: " + columnName);
				continue;
			}
		
			EFGObject efgObject = (EFGObject) typeTable.get(columnName.trim()
					.toLowerCase());
			if (efgObject.getDataType() == null) {//if no column name information skip
				log.debug("no column information for: " + columnName);
				continue;
			}
			String paramValue = null;
			if ((states == null) || ("".equals(states.trim()))) {
				log.debug("Character: " + columnName
						+ " has null or empty states for row: "
						+ rowset.getRow() + " of results. Skip it!!");
				continue;
			}
			log
					.debug("About to process states which aren't part of users request: "
							+ states + " for columnName: " + columnName);
			StringWriter writer = new StringWriter();
			this.filter.filter(new StringReader(states), writer);
			states = writer.getBuffer().toString();

			TaxonEntryTypeItem taxonEntryItem = itemBuilder
					.buildTaxonEntryItem(paramValue, states, efgObject);
			if (taxonEntryItem != null) {
				taxonEntry.addTaxonEntryTypeItem(taxonEntryItem);
			}
		}
	}

	/**
	 * Add special parameters to a list so that special parameters can be
	 * excluded when building the query.
	 */
	private void addSpecialParams() {
		// Construct collection of special parameter names
		specialParams = new ArrayList();
		specialParams.add(EFGImportConstants.DATASOURCE_NAME);
		specialParams.add(EFGImportConstants.MAX_DISPLAY);
		specialParams.add(EFGImportConstants.DISPLAY_FORMAT);
		specialParams.add(EFGImportConstants.SEARCHSTR);
		specialParams.add(EFGImportConstants.SEARCHTYPE);
		specialParams.add(EFGImportConstants.DISPLAY_NAME);
		specialParams.add(EFGImportConstants.XSL_STRING);
	}

}
