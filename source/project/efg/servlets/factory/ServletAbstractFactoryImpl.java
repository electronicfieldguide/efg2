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
package project.efg.servlets.factory;

import java.util.ArrayList;
import java.util.List;



import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.servlets.efgImpl.EFGContextListener;
import project.efg.servlets.efgInterface.SearchableInterface;
import project.efg.servlets.efgInterface.EFGDataObjectListInterface;
import project.efg.servlets.efgInterface.ServletAbstractFactoryInterface;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.servlets.rdb.QueryExecutor;
import project.efg.servlets.rdb.SearchableImpl;
import project.efg.util.EFGDisplayObject;
import project.efg.util.EFGDisplayObjectList;
import project.efg.util.EFGImportConstants;
import project.efg.util.EFGMediaResourceSearchableObject;

import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * @author kasiedu
 *
 */
public class ServletAbstractFactoryImpl extends
		ServletAbstractFactoryInterface {
	
	private String commonQuery = "SELECT DISTINCT LEGALNAME,NAME FROM ";
	private QueryExecutor queryExecutor;
	/**
	 * 
	 */
	public ServletAbstractFactoryImpl() {
		super();
		this.queryExecutor = new QueryExecutor();
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ServletAbstractFactoryInterface#createListOfDatasources()
	 */
	public EFGDisplayObjectList createListOfDatasources() {
		return this.getLists();
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ServletAbstractFactoryInterface#createMediaResourceLists(java.lang.String, java.lang.String)
	 */
	protected EFGDataObjectListInterface createMediaResourceLists(String displayName, 
			String datasourceName) {
		return examineListsCache(displayName, 
				datasourceName,false);
		
	}
	/**
	 * This is called in instances where a datasource uses the default template
	 * @param displayName - The display name for the current datasource
	 * @param datasourceName - The datasource name created by database
	 */
	protected EFGMediaResourceSearchableObject createFirstField(String displayName, String datasourceName) {
		EFGMediaResourceSearchableObject searchObj = new EFGMediaResourceSearchableObject();
		String metaName = getMetadataNameFromCache(displayName, datasourceName);
		
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT DISTINCT  ");
		queryBuffer.append(EFGImportConstants.LEGALNAME);
		queryBuffer.append(",");
		queryBuffer.append(EFGImportConstants.MEDIARESOURCE);
		queryBuffer.append(",");
		queryBuffer.append(EFGImportConstants.SEARCHABLE);
		queryBuffer.append( " FROM ");
		queryBuffer.append(metaName);
		queryBuffer.append(" ORDER BY ORDERVALUE");
		try {
			List lists = this.queryExecutor.executeQueryForList(
					queryBuffer.toString(),3);
			for (java.util.Iterator iter = lists.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface)iter.next();
				if(queue.getObject(1).equalsIgnoreCase("true")){//mediaresource
					if(searchObj.getMediaResourceField().equals("")){
						searchObj.setMediaResourceField(queue.getObject(0));
					}					
				}
				if(queue.getObject(2).equalsIgnoreCase("true")){
					if(searchObj.getSearchableField().equals("")){
						searchObj.setSearchableField(queue.getObject(0));
					}
				}
				if((!searchObj.getMediaResourceField().equals("")) && 
						(!searchObj.getSearchableField().equals(""))){
					break;
				}
			}
			 
		} catch (Exception e) {
		
			LoggerUtilsServlet.logErrors(e);
		}
		return searchObj;
	}


	
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ServletAbstractFactoryInterface#createSearchableLists(java.lang.String, java.lang.String)
	 */
	protected EFGDataObjectListInterface createSearchableLists(String displayName, 
			String datasourceName) {
		return this.examineListsCache(displayName, 
				datasourceName,true);
		
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ServletAbstractFactoryInterface#createAllFields(java.lang.String, java.lang.String)
	 */
	protected List createAllFields(String displayName, String datasourceName) {
		
		List arrList = new ArrayList();
		try {
			String metaName = getMetadataNameFromCache(displayName, datasourceName);
			
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append("SELECT DISTINCT LEGALNAME,NAME FROM ");
			queryBuffer.append(metaName);
			queryBuffer.append(" ORDER BY NAME");
			
			
			List lists = this.queryExecutor.executeQueryForList(
					queryBuffer.toString(),2);
			for (java.util.Iterator iter = lists.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface)iter.next();
				arrList.add(queue);
			}
		} catch (Exception e) {
			LoggerUtilsServlet.logErrors(e);
		}
		return arrList;
		
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ServletAbstractFactoryInterface#createTaxonPageFields(java.lang.String, java.lang.String)
	 */
	protected List createTaxonPageFields(String displayName, String datasourceName) {
		List arrList = new ArrayList();
		try {
			
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(this.getCommonQuery(displayName, datasourceName));
			queryBuffer.append(" WHERE ONTAXONPAGE=\"true\" ORDER BY NAME");
			arrList = this.getQuery(queryBuffer.toString());
			
		} catch (Exception e) {
			LoggerUtilsServlet.logErrors(e);
		}
		return arrList;
		
	}

	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ServletAbstractFactoryInterface#createEFGListsFields(java.lang.String, java.lang.String)
	 */
	protected List createEFGListsFields(String displayName, String datasourceName) {
		List arrList = new ArrayList();
		try {
			
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(this.getCommonQuery(displayName, datasourceName));
			queryBuffer.append(" WHERE ISLISTS=\"true\" ORDER BY NAME");
			arrList = this.getQuery(queryBuffer.toString());
			
		} catch (Exception e) {
			LoggerUtilsServlet.logErrors(e);
		}
		return arrList;
		
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ServletAbstractFactoryInterface#createStatisticalMeasuresFields(java.lang.String, java.lang.String)
	 */
	protected List createStatisticalMeasuresFields(String displayName, String datasourceName) {
		List arrList = new ArrayList();
		try {
			
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(this.getCommonQuery(displayName, datasourceName));
			queryBuffer.append(" WHERE ");
			queryBuffer.append("(NUMERICValue=\"true\" or NUMERICRANGE=\"true\")");
			arrList = this.getQuery(queryBuffer.toString());
			
		} catch (Exception e) {
			LoggerUtilsServlet.logErrors(e);
		}
		return arrList;
		
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ServletAbstractFactoryInterface#createCategoricalItemFields(java.lang.String, java.lang.String)
	 */
	protected List createCategoricalItemFields(String displayName, String datasourceName) {
		
		List arrList = new ArrayList();
		try {
			
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(this.getCommonQuery(displayName, datasourceName));
			queryBuffer.append(" WHERE CATEGORICAL=\"true\" ORDER BY NAME");
			arrList = this.getQuery(queryBuffer.toString());
			
		} catch (Exception e) {
			LoggerUtilsServlet.logErrors(e);
		}
		return arrList;
	
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ServletAbstractFactoryInterface#createNarrativeItemFields(java.lang.String, java.lang.String)
	 */
	protected List createNarrativeItemFields(String displayName, String datasourceName) {
		List arrList = new ArrayList();
		try {
			
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(this.getCommonQuery(displayName, datasourceName));
			queryBuffer.append(" WHERE NARRATIVE=\"true\" ORDER BY NAME");
			arrList = this.getQuery(queryBuffer.toString());
			
		} catch (Exception e) {
			LoggerUtilsServlet.logErrors(e);
		}
		return arrList;
	
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ServletAbstractFactoryInterface#createMediaResourceLists(java.lang.String, java.lang.String)
	 */
	protected List createMediaResourceFields(String displayName, 
			String datasourceName) {
		List arrList = new ArrayList();
		try {
			
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(this.getCommonQuery(displayName, datasourceName));
			queryBuffer.append(" WHERE MEDIARESOURCE=\"true\" ORDER BY NAME");
			arrList = this.getQuery(queryBuffer.toString());
			
		} catch (Exception e) {
			LoggerUtilsServlet.logErrors(e);
		}
		return arrList;
		
		
		
	}
	private List getQuery(String query){
		List arrList = new ArrayList();
		try {
			List lists = this.queryExecutor.executeQueryForList(
					query,2);
			for (java.util.Iterator iter = lists.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface)iter.next();
				arrList.add(queue);
			}
		} catch (Exception e) {
			LoggerUtilsServlet.logErrors(e);
		}
		return arrList;
	}
	private String getCommonQuery(String displayName, String datasourceName) {
		
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append(this.commonQuery);
		queryBuffer.append(getMetadataNameFromCache(displayName, datasourceName));
		return queryBuffer.toString();
	}
	private String buildAllDataSourcesQuery(){
		String efgRDBTable = null;
		if(this.getMainTableName() == null || "".equals(this.getMainTableName().trim())) {
			efgRDBTable = EFGImportConstants.EFG_RDB_TABLES;
		}
		else {
			efgRDBTable = this.getMainTableName();
		}
		
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT DS_DATA, DS_METADATA,DISPLAY_NAME FROM ");
		queryBuffer.append(efgRDBTable);
		
		return queryBuffer.toString();
		
	}
	private EFGDisplayObjectList getLists(){
		EFGDisplayObjectList objectLists = null;
		EFGDisplayObject obj = null;
		try {
			List lists = this.queryExecutor.executeQueryForList(
					buildAllDataSourcesQuery(),3);
			for (java.util.Iterator iter = lists.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface)iter.next();
				String ds = queue.getObject(0);
				String display = queue.getObject(2);
				obj = new EFGDisplayObject();
				obj.setDisplayName(display);
				obj.setDatasourceName(ds);
				if(objectLists == null){
					objectLists = new EFGDisplayObjectList();
				}
				objectLists.add(obj);
			}
			
		} catch (Exception e) {
			//log.error(e.getMessage());
			LoggerUtilsServlet.logErrors(e);
		}
		return objectLists;
	}
	private String getMetadataQuery(String displayName, String datasourceName) {
		String query = "SELECT DS_METADATA FROM ";
		return getRDBQuery(query,displayName,datasourceName);
	}
	private String getRDBQuery(String query,String displayName, String datasourceName) {
		
		String str2="DISPLAY_NAME";
		String str3 = displayName;
		
		if((displayName == null) || (displayName.trim().equals(""))){
			str2 = "DS_DATA";
			str3 = datasourceName; 
		}
		String efgRDBTable = null;
		if(this.getMainTableName() == null || "".equals(this.getMainTableName().trim())) {
			efgRDBTable = EFGImportConstants.EFG_RDB_TABLES;
		}
		else {
			efgRDBTable = this.getMainTableName();
		}
		
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append(query);
		queryBuffer.append(" ");
		queryBuffer.append(efgRDBTable);
		queryBuffer.append(" WHERE ");
		queryBuffer.append(str2);
		queryBuffer.append(" = \"");
		queryBuffer.append(str3);
		queryBuffer.append("\"");
		return queryBuffer.toString();
	}
	private String constructCacheKey(String dplay, String ds, String bool){
		String first = dplay;
		String second = ds;
		
		if(first == null){
			first = ds;
			second = "null";
		}
		else if(second == null){
			second = "null";
		}
		StringBuffer key = new StringBuffer();
		key.append(first);
		key.append(second);
		key.append(bool);
		return key.toString();
	}

	private EFGDataObjectListInterface examineListsCache(String displayName, 
			String datasourceName,boolean isSearchable){
		EFGDataObjectListInterface lists = null;
		String key = this.constructCacheKey(displayName, 
				datasourceName,isSearchable+"");
		
		GeneralCacheAdministrator cacheAdmin = 
			EFGContextListener.getCacheAdmin();//get cache
		try {
		  
			lists = (EFGDataObjectListInterface)cacheAdmin.getFromCache(key,
					CacheEntry.INDEFINITE_EXPIRY);
			//log.debug("Object obtained from cache ");
		} catch (NeedsRefreshException nre) {
		    try {
		    	//do logic 
		    	  // Get from the cache
		    	SearchableInterface search = new SearchableImpl();
		    	search.setMainDataTableName(this.getMainTableName());
				if(isSearchable){
					lists = search.getSearchables(displayName,datasourceName);
				}
				else{
					lists = search.getMediaResources(displayName,datasourceName);
				}
		    	String[] groups = new String[]{"prepareItems"};
		    	cacheAdmin.putInCache(key, lists,groups);
		    	//log.debug("Object put in cache");
		    } catch (Exception ex) {
		        // We have the current content if we want fail-over.
		    	lists = (EFGDataObjectListInterface)nre.getCacheContent();
		    	//log.debug("Object fail over obejct ");
		        // It is essential that cancelUpdate is called if the
		        // cached content is not rebuilt
		    	cacheAdmin.cancelUpdate(key);
		    }
		}
		return lists;
	}
	private String getMetadataNameFromCache(String displayName, String datasourceName){
	
		String key = this.constructCacheKey(displayName, 
				datasourceName,"_metadataName");
		String metaName = null;
		GeneralCacheAdministrator cacheAdmin = 
			EFGContextListener.getCacheAdmin();//get cache
		try {
		  
			metaName = (String)cacheAdmin.getFromCache(key,
					CacheEntry.INDEFINITE_EXPIRY);
			//log.debug("Object obtained from cache ");
		} catch (NeedsRefreshException nre) {
		    try {
		    	metaName = getMetadataName(displayName,datasourceName); 
		    	String[] groups = new String[]{"metadataName"};
		    	cacheAdmin.putInCache(key, metaName,groups);
		    	//log.debug("Object put in cache");
		    } catch (Exception ex) {
		        // We have the current content if we want fail-over.
		    	metaName = (String)nre.getCacheContent();
		    	//log.debug("Object fail over obejct ");
		        // It is essential that cancelUpdate is called if the
		        // cached content is not rebuilt
		    	cacheAdmin.cancelUpdate(key);
		    }
		}
		catch(Exception ee){
			LoggerUtilsServlet.logErrors(ee);
		}
		
		return metaName;
		
	}
	private String getMetadataName(String displayName, String datasourceName)throws Exception {
		String metaName = null;
		try{
		List lists = this.queryExecutor.executeQueryForList(
				getMetadataQuery(displayName,datasourceName),1);
		for (java.util.Iterator iter = lists.iterator(); iter.hasNext();) {
			EFGQueueObjectInterface queue = (EFGQueueObjectInterface)iter.next();
			metaName = queue.getObject(0);
			break;
		}
		
		}
		catch(Exception ee){
			throw ee;
		}
		return metaName;
	}
	

	



}
