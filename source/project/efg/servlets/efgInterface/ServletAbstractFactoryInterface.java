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

import java.util.List;

import org.apache.log4j.Logger;

import project.efg.util.EFGDisplayObjectList;

/**
 * @author kasiedu
 * 
 */
public abstract class ServletAbstractFactoryInterface {
	static Logger log = null;

	static {
		try {
			log = Logger.getLogger(ServletAbstractFactoryInterface.class);
		} catch (Exception ee) {
		}
	}

	public ServletAbstractFactoryInterface() {

	}

	/**
	 * 
	 * @return alist of datasources from the database
	 */
	public EFGDisplayObjectList getListOfDatasources() {

		return this.createListOfDatasources();
	}

	/**
	 * 
	 * @return a list of searchable fields given a
	 */
	public EFGDataObjectListInterface getSearchableLists(String displayName,
			String datasourceName) {
		return this.createSearchableLists(displayName, datasourceName);
	}

	public EFGDataObjectListInterface getMediaResourceLists(String displayName,
			String datasourceName) {
		return this.createMediaResourceLists(displayName, datasourceName);
	}
	public List getMediaResourceFields(String displayName,
			String datasourceName) {
		return this.createMediaResourceFields(displayName, datasourceName);
	}

	/**
	 * 
	 * @param dataSourceName
	 * @return a list of MediaResource objects for the current datasource
	 */
	public List getEFGListsFields(String displayName,
			String datasourceName) {
		
		return this.createEFGListsFields(displayName,datasourceName);
	}

	/**
	 * 
	 * @param dataSourceName
	 * @return a list of MediaResource objects for the current datasource
	 */
	public List getStatisticalMeasuresFields(
			String displayName, String datasourceName) {
		return this.createStatisticalMeasuresFields(displayName,datasourceName);
	}

	/**
	 * 
	 * @param dataSourceName
	 * @return a list of MediaResource objects for the current datasource
	 */
	public List getCategoricalItemFields(
			String displayName, String datasourceName) {
		
		return this.createCategoricalItemFields(displayName,datasourceName);
	}

	/**
	 * 
	 * @param dataSourceName
	 * @return a list of MediaResource objects for the current datasource
	 */
	public List getNarrativeItemFields(
			String displayName, String datasourceName) {
		
		return this.createNarrativeItemFields(displayName,datasourceName);
	}

	public List getAllFields(String displayName, String dataSourceName) {
		return this.createAllFields(displayName, dataSourceName);
	}

	public List getTaxonPageFields(String displayName, String dataSourceName) {
		return this.createTaxonPageFields(displayName, dataSourceName);
	}

	public String getFirstFieldName(String displayName, String datasourceName) {
		return this.createFirstFieldName(displayName, datasourceName);
	}

	public String getFirstMediaResourceFieldName(String displayName,
			String datasourceName) {
		return this.createFirstMediaResourceFieldName(displayName,
				datasourceName);
	}

	public String getXSLFileName(String displayName, String datasourceName,
			String fieldName) {
		return this
				.getXSLFileNameFromDB(displayName, datasourceName, fieldName);
	}

	protected abstract String getXSLFileNameFromDB(String displayName,
			String datasourceName, String fieldName);

	protected abstract EFGDataObjectListInterface createMediaResourceLists(
			String displayName, String datasourceName);
	protected abstract List createMediaResourceFields(
			String displayName, String datasourceName);
	protected abstract EFGDataObjectListInterface createSearchableLists(
			String displayName, String datasourceName);

	protected abstract EFGDisplayObjectList createListOfDatasources();

	/**
	 * 
	 * @param dataSourceName
	 * @return a list of MediaResource objects for the current datasource
	 */
	protected abstract List createEFGListsFields(String displayName,
			String datasourceName);

	/**
	 * 
	 * @param dataSourceName
	 * @return a list of MediaResource objects for the current datasource
	 */
	protected abstract List createStatisticalMeasuresFields(
			String displayName, String datasourceName);

	/**
	 * 
	 * @param dataSourceName
	 * @return a list of MediaResource objects for the current datasource
	 */
	protected abstract List createCategoricalItemFields(
			String displayName, String datasourceName);
	/**
	 * 
	 * @param dataSourceName
	 * @return a list of MediaResource objects for the current datasource
	 */
	protected abstract List createNarrativeItemFields(
			String displayName, String datasourceName);
	
	protected abstract String createFirstFieldName(String displayName,
			String datasourceName);

	protected abstract String createFirstMediaResourceFieldName(
			String displayName, String datasourceName);

	protected abstract List createAllFields(String displayName,
			String dataSourceName);

	protected abstract List createTaxonPageFields(String displayName,
			String dataSourceName);

}
