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

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.Imports.factory.ComparatorFactory;
import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.servlets.efgInterface.SearchableInterface;
import project.efg.servlets.efgInterface.EFGDataObjectListInterface;
import project.efg.servlets.efgInterface.EFGDataObject;
import project.efg.servlets.efgServletsUtil.SearchableList;
import project.efg.servlets.efgServletsUtil.EFGDataObjectImpl;
import project.efg.servlets.factory.EFGDocumentTypesFactory;
import project.efg.util.EFGImportConstants;
import project.efg.util.EFGObject;

/**
 * @author kasiedu
 * 
 */
public class SearchableImpl extends SearchableInterface {

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(SearchableImpl.class);
		} catch (Exception ee) {
		}
	}
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see project.efg.servlets.efgInterface.SearchableInterface#getMediaResources(java.lang.String)
	 */
	public EFGDataObjectListInterface getMediaResources(String displayName, String datasourceName)
			throws Exception {
		//cache me
		this.addCommonItems(displayName, datasourceName,false);
		this.setNames(this.mediaResourceList);
		return mediaResourceList;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see project.efg.servlets.rdb.DisplayLists#getSearchables(java.lang.String)
	 */
	public EFGDataObjectListInterface getSearchables(String displayName, String datasourceName)
			throws Exception {
		//cache me ..make displayName and datasourceName same
		this.addCommonItems(displayName, datasourceName,true);
		this.setNames(this.searchableList);
		return this.searchableList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project.efg.servlets.efgInterface.SearchableInterface#createMediaResourcesList()
	 */
	public EFGDataObjectListInterface createMediaResourcesList() {
		return new SearchableList(ComparatorFactory
				.getComparator("searchableobject.comparator"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project.efg.servlets.efgInterface.SearchableInterface#createEFGDataObject()
	 */
	public EFGDataObject createEFGDataObject() {
		return new EFGDataObjectImpl();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see project.efg.servlets.efgInterface.SearchableInterface#createSearchableList()
	 */
	public EFGDataObjectListInterface createSearchableList() {
		return new SearchableList(ComparatorFactory
				.getComparator("searchableobject.comparator"));
	}
	
	private void setNames(EFGDataObjectListInterface searchLists){
		searchLists.setDatasourceName(this.datasourceName);
		searchLists.setDisplayName(this.displayName);
		searchLists.setMetadatasourceName(this.metadatasourceName);
	}
	private void addCommonItems(String displayName, String datasourceName,boolean isSearchable)
			throws Exception {
try {
			
			List list = this.queryExecutor.executeQueryForList(getQuery(displayName,datasourceName),
					2);
			for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
						.next();
				
				this.metadatasourceName = queue.getObject(0);
				if((displayName == null) || (displayName.trim().equals(""))){
					this.displayName   = queue.getObject(1);
					this.datasourceName   = datasourceName;
				}
				else{
					this.datasourceName   = queue.getObject(1);
					this.displayName = displayName;
				}
				
				
				prepareItems(
						this.queryExecutor
								.executeQueryForList(
										getSearchableMetadataCategoricalQuery(this.metadatasourceName),
										3), this.datasourceName,
						EFGImportConstants.CATEGORICAL, isSearchable);
				prepareItems(this.queryExecutor.executeQueryForList(
						getSearchableMetadataNumericQuery(this.metadatasourceName),
						3),this.datasourceName, EFGImportConstants.NUMERIC,
						isSearchable);
				prepareItems(this.queryExecutor.executeQueryForList(
						getSearchableMetadataMediaResourceQuery(
								this.metadatasourceName, isSearchable), 3),
								this.datasourceName, EFGImportConstants.MEDIARESOURCE,
						isSearchable);
				prepareItems(
						this.queryExecutor
								.executeQueryForList(
										getSearchableMetadataListsQuery(this.metadatasourceName),
										3), this.datasourceName,
						EFGImportConstants.ISLISTS, isSearchable);
			}
		} catch (Exception e) {
			throw e;
		}	
			
			
			
		
	}

	private String getQuery(String displayName, String datasourceName) {
		String str1 = "DS_DATA";
		String str2="DISPLAY_NAME";
		String str3 = displayName;
		
		if((displayName == null) || (displayName.trim().equals(""))){
			str1="DISPLAY_NAME";
			str2 = "DS_DATA";
			str3 = datasourceName; 
		}
		
		
		String efgRDBTable = EFGImportConstants.EFGProperties
		.getProperty("ALL_EFG_RDB_TABLES");
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT DS_METADATA, ");
		queryBuffer.append(str1);
		queryBuffer.append(" FROM ");
		queryBuffer.append(efgRDBTable);
		queryBuffer.append(" WHERE ");
		queryBuffer.append(str2);
		queryBuffer.append(" = '");
		queryBuffer.append(str3);
		queryBuffer.append("'");
		return queryBuffer.toString();

	}
	
	private String getSearchableMetadataNumericQuery(String name) {
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT LEGALNAME, NAME, ORDERVALUE FROM ");
		queryBuffer.append(name);
		queryBuffer.append(" WHERE ");
		queryBuffer
				.append("(ISSEARCHABLE= 'true' and (NUMERICValue='true' or NUMERICRANGE='true'))");
		return queryBuffer.toString();

	}

	private  String getSearchableMetadataMediaResourceQuery(String name,
			boolean isSearchable) {
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT LEGALNAME, NAME, ORDERVALUE FROM ");
		queryBuffer.append(name);
		queryBuffer.append(" WHERE ");
		if (isSearchable) {
			queryBuffer.append("ISSEARCHABLE= 'true' and ");
		}
		queryBuffer.append("MEDIARESOURCE = 'true'");
		return queryBuffer.toString();

	}

	private  String getSearchableMetadataListsQuery(String name) {
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT LEGALNAME, NAME, ORDERVALUE FROM ");
		queryBuffer.append(name);
		queryBuffer.append(" WHERE ");
		queryBuffer.append("ISSEARCHABLE= 'true' and ISLISTS = 'true'");
		return queryBuffer.toString();

	}

	private  String getSearchableMetadataCategoricalQuery(String name) {
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT LEGALNAME, NAME, ORDERVALUE FROM ");
		queryBuffer.append(name);
		queryBuffer.append(" WHERE ");
		queryBuffer.append("ISSEARCHABLE= 'true' and CATEGORICAL = 'true'");
		return queryBuffer.toString();

	}

	private void prepareItems(List resultSet, String ds, String type,
			boolean isSearchable) throws Exception {
		for (java.util.Iterator iter = resultSet.iterator(); iter.hasNext();) {
			EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
					.next();
			String legal = queue.getObject(0);
			String name = queue.getObject(1);
			int order = Integer.parseInt(queue.getObject(2));

			EFGDataObject search = createEFGDataObject();

			search.setLegalName(legal);
			search.setOrder(order);
			search.setName(name);
			EFGObject efgObject = new EFGObject();
			efgObject.setDatabaseName(legal);
			efgObject.setName(name);
			efgObject.setDataType(type);
			Object ob = this.typesFactory.getInstance(efgObject,ds);

			if (ob instanceof StatisticalMeasuresType) {
				StatisticalMeasuresType st = (StatisticalMeasuresType) ob;
				log.debug("Is a StatisticalMeasure: " + st.getName());
				search.setStatisticalMeasures(st);
			} else if (ob instanceof ItemsType) {
				ItemsType items = (ItemsType) ob;
				log.debug("Is a items: " + items.getName());
				if (items.getItemCount() > 0) {
					search.setStates(items);
				}
			} else if (ob instanceof MediaResourcesType) {
				MediaResourcesType items = (MediaResourcesType) ob;
				log.debug("Is a media resource: " + items.getName());
				if (items.getMediaResourceCount() > 0) {
					search.setMediaResources(items);
				}
			} else if (ob instanceof EFGListsType) {

				EFGListsType items = (EFGListsType) ob;
				log.debug("Is a lists: " + items.getName());
				if (items.getEFGListCount() > 0) {
					search.setEFGListsType(items);
				}
			} else {
				if (ob == null) {
					log.error("Null object returned");
				} else {
					log.error("Unknown type for: " + ob.getClass().getName());
				}
				continue;
			}
			if (!isSearchable) {
				this.mediaResourceList.addEFGDataObject(search);
			} else {
				this.searchableList.addEFGDataObject(search);
			}
		}

	}

}
