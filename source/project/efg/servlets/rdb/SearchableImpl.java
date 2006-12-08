/*
 * $Id$
 *
 * Copyright (c) 2006  University of Massachusetts Boston
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
package project.efg.servlets.rdb;

import java.util.List;

import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.Imports.factory.ComparatorFactory;
import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.servlets.efgInterface.EFGDataObject;
import project.efg.servlets.efgInterface.EFGDataObjectListInterface;
import project.efg.servlets.efgInterface.SearchableInterface;
import project.efg.servlets.efgServletsUtil.EFGDataObjectImpl;
import project.efg.servlets.efgServletsUtil.SearchableList;
import project.efg.servlets.factory.EFGDocumentTypesFactory;
import project.efg.util.EFGImportConstants;
import project.efg.util.EFGObject;

/**
 * @author kasiedu
 * 
 */
public class SearchableImpl extends SearchableInterface {

	private QueryExecutor queryExecutor;

	private EFGDocumentTypesFactory typesFactory;

	private String datasourceName;

	private String metadatasourceName;

	private String displayName;

	public SearchableImpl() {
		super();
		this.queryExecutor = new QueryExecutor();
		this.typesFactory = new EFGDocumentTypesFactory();
	}

/**
 * Get a list of Mediaresource fields for the current datasource
 * 
 * @param displayName
 * @param datasourceName
 */
	public EFGDataObjectListInterface getMediaResources(String displayName,
			String datasourceName) throws Exception {
		// cache me
		this.addCommonItems(displayName, datasourceName, false);
		this.setNames(this.mediaResourceList);
		return mediaResourceList;
	}

	/**
	 * Get a list of searchable fields for the current datasource
	 * 
	 * @param displayName
	 * @param datasourceName
	 */
	public EFGDataObjectListInterface getSearchables(String displayName,
			String datasourceName) throws Exception {
		this.addCommonItems(displayName, datasourceName, true);
		this.setNames(this.searchableList);
		return this.searchableList;
	}

	/**
	 * Create a list of mediaresource fields for the current datasource
	 * 
	 */
	public EFGDataObjectListInterface createMediaResourcesList() {

		return new SearchableList(ComparatorFactory
				.getComparator(EFGImportConstants.EFGProperties
						.getProperty("searchableobject.comparator")));
	}

	/**
	 * Create an EFGDataObject for the current datasource
	 * 
	 */
	public EFGDataObject createEFGDataObject() {
		return new EFGDataObjectImpl();
	}

	/**
	 * Create a Searchable List for the current datasource
	 * 
	 */
	public EFGDataObjectListInterface createSearchableList() {
		return new SearchableList(ComparatorFactory
				.getComparator(EFGImportConstants.EFGProperties
						.getProperty("searchableobject.comparator")));

	}
	/**
	 * Set the datasource and display names for the current list of searchable items
	 * @param searchLists
	 */
	private void setNames(EFGDataObjectListInterface searchLists) {
		searchLists.setDatasourceName(this.datasourceName);
		searchLists.setDisplayName(this.displayName);
		searchLists.setMetadatasourceName(this.metadatasourceName);
	}
	/**
	 * Generate a query to query metadataTable for some fields
	 */
	private String getSearchableDataQuery(String metadataTable) {
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT LEGALNAME, NAME, ORDERVALUE,NUMERICVALUE,"
				+ "NUMERICRANGE,MEDIARESOURCE,ISLISTS,CATEGORICAL FROM ");
		queryBuffer.append(metadataTable);
		queryBuffer.append(" WHERE ");
		queryBuffer.append("(ISSEARCHABLE= 'true')");
		return queryBuffer.toString();

	}

	/**
	 * Create a list of Items from the results of a query
	 * @param list
	 * @param string
	 */
	private void prepareCurrentItems(List list, String ds) {

		for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
			boolean isSearchable = true;
			EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
					.next();
			String legal = queue.getObject(0);
			String name = queue.getObject(1);
			int order = Integer.parseInt(queue.getObject(2));
			String numericval = queue.getObject(3);
			String numericRange = queue.getObject(4);
			String mediaResource = queue.getObject(5);
			String isLists = queue.getObject(6);
			String cat = queue.getObject(7);

			EFGDataObject search = createEFGDataObject();

			search.setLegalName(legal);
			search.setOrder(order);
			search.setName(name);
			EFGObject efgObject = new EFGObject();
			efgObject.setDatabaseName(legal);
			efgObject.setName(name);
			String type = null;
			if (isLists.equalsIgnoreCase("true")) {
				type = EFGImportConstants.ISLISTS;
			} else if (mediaResource.equalsIgnoreCase("true")) {
				isSearchable = false;
				type = EFGImportConstants.MEDIARESOURCE;
			} else if (numericRange.equalsIgnoreCase("true")) {
				type = EFGImportConstants.NUMERIC;
			} else if (numericval.equalsIgnoreCase("true")) {
				type = EFGImportConstants.NUMERIC;
			} else if (cat.equalsIgnoreCase("true")) {
				type = EFGImportConstants.CATEGORICAL;
			}
			efgObject.setDataType(type);
			Object ob = this.typesFactory.getInstance(efgObject, ds);

			if (ob instanceof StatisticalMeasuresType) {
				StatisticalMeasuresType st = (StatisticalMeasuresType) ob;

				search.setStatisticalMeasures(st);
			} else if (ob instanceof ItemsType) {
				ItemsType items = (ItemsType) ob;

				if (items.getItemCount() > 0) {
					search.setStates(items);
				}
			} else if (ob instanceof MediaResourcesType) {
				MediaResourcesType items = (MediaResourcesType) ob;

				if (items.getMediaResourceCount() > 0) {
					search.setMediaResources(items);
				}
			} else if (ob instanceof EFGListsType) {

				EFGListsType items = (EFGListsType) ob;

				if (items.getEFGListCount() > 0) {
					search.setEFGListsType(items);
				}
			} else {

				continue;
			}
			if (!isSearchable) {
				this.mediaResourceList.addEFGDataObject(search);
			} else {
				this.searchableList.addEFGDataObject(search);
			}
		}
	}
/**
 * Add some common items to the lists
 * @param displayName
 * @param datasourceName
 * @param isSearchable
 * @throws Exception
 */
	private void addCommonItems(String displayName, String datasourceName,
			boolean isSearchable) throws Exception {
		try {

			List list = this.queryExecutor.executeQueryForList(getQuery(
					displayName, datasourceName), 2);
			for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
						.next();

				this.metadatasourceName = queue.getObject(0);
				if ((displayName == null) || (displayName.trim().equals(""))) {
					this.displayName = queue.getObject(1);
					this.datasourceName = datasourceName;
				} else {
					this.datasourceName = queue.getObject(1);
					this.displayName = displayName;
				}
				prepareCurrentItems(this.queryExecutor.executeQueryForList(
						getSearchableDataQuery(this.metadatasourceName), 8),
						this.datasourceName);

			}
		} catch (Exception e) {
			throw e;
		}

	}

/**
 * Get a query to get a tableName from the master table
 * @param displayName
 * @param datasourceName
 * @return
 */
	private String getQuery(String displayName, String datasourceName) {
		String str1 = "DS_DATA";
		String str2 = "DISPLAY_NAME";
		String str3 = displayName;

		if ((displayName == null) || (displayName.trim().equals(""))) {
			str1 = "DISPLAY_NAME";
			str2 = "DS_DATA";
			str3 = datasourceName;
		}

		String efgRDBTable = null;
		
		if(this.getMainTableName() == null || "".equals(this.getMainTableName())) {
			efgRDBTable = EFGImportConstants.EFG_RDB_TABLES;
		}
		else {
			efgRDBTable = this.getMainTableName();
		}
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT DS_METADATA, ");
		queryBuffer.append(str1);
		queryBuffer.append(" FROM ");
		queryBuffer.append(efgRDBTable);
		queryBuffer.append(" WHERE ");
		queryBuffer.append(str2);
		queryBuffer.append(" = \"");
		queryBuffer.append(str3);
		queryBuffer.append("\"");
		return queryBuffer.toString();

	}

}
