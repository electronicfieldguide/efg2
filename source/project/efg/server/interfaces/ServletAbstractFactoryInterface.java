/**
 * $Id: ServletAbstractFactoryInterface.java,v 1.1.1.1 2007/08/01 19:11:22 kasiedu Exp $
 * $Name:  $
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
package project.efg.server.interfaces;

import java.util.List;
import org.apache.log4j.Logger;
import project.efg.server.utils.EFGDisplayObjectList;
import project.efg.server.utils.EFGMediaResourceSearchableObject;

public abstract class ServletAbstractFactoryInterface
{

    public ServletAbstractFactoryInterface()
    {
    }

    public void setMainDataTableName(String mainTableName)
    {
        //log.debug("Set Main Table Name: " + mainTableName);
        this.mainTableName = mainTableName;
    }

    public String getMainTableName()
    {
        return mainTableName;
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
    public EFGDataObjectListInterface getSearchableLists(String displayName, String datasourceName)
    {
        return createSearchableLists(displayName, datasourceName);
    }

    public EFGDataObjectListInterface getMediaResourceLists(String displayName, String datasourceName)
    {
        return createMediaResourceLists(displayName, datasourceName);
    }

    public List getMediaResourceFields(String displayName, String datasourceName)
    {
        return createMediaResourceFields(displayName, datasourceName);
    }
	/**
	 * 
	 * @param dataSourceName
	 * @return a list of MediaResource objects for the current datasource
	 */
    public List getEFGListsFields(String displayName, String datasourceName)
    {
        return createEFGListsFields(displayName, datasourceName);
    }

    public List getStatisticalMeasuresFields(String displayName, String datasourceName)
    {
        return createStatisticalMeasuresFields(displayName, datasourceName);
    }
	/**
	 * 
	 * @param dataSourceName
	 * @return a list of StatisticalMeasures objects for the current datasource
	 */
    public List getCategoricalItemFields(String displayName, String datasourceName)
    {
        return createCategoricalItemFields(displayName, datasourceName);
    }

    public List getNarrativeItemFields(String displayName, String datasourceName)
    {
        return createNarrativeItemFields(displayName, datasourceName);
    }

    public List getAllFields(String displayName, String dataSourceName)
    {
        return createAllFields(displayName, dataSourceName);
    }

    public List getTaxonPageFields(String displayName, String dataSourceName)
    {
        return createTaxonPageFields(displayName, dataSourceName);
    }

    public EFGMediaResourceSearchableObject getFirstField(String displayName, String datasourceName)
    {
        return createFirstField(displayName, datasourceName);
    }

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
	
	protected abstract List createAllFields(String displayName,
			String dataSourceName);

	protected abstract List createTaxonPageFields(String displayName,
			String dataSourceName);

	

	/**
	 * @param displayName
	 * @param datasourceName
	 * @return
	 */
	protected abstract EFGMediaResourceSearchableObject createFirstField(String displayName, String datasourceName);
	 protected static Logger log = null;
	    private String mainTableName;

	    static 
	    {
	        try
	        {
	            log = Logger.getLogger(project.efg.server.interfaces.ServletAbstractFactoryInterface.class);
	        }
	        catch(Exception ee) { }
	    }
}
