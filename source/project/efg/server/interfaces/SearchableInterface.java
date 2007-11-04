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
package project.efg.server.interfaces;

import org.apache.log4j.Logger;

import project.efg.util.interfaces.EFGDataObject;



public abstract class SearchableInterface
{
	  protected EFGDataObjectListInterface searchableList;
	   protected EFGDataObjectListInterface mediaResourceList;
	  
	    private String mainTableName;

	static Logger log = null;
	static {
		try {
			//log = Logger.getLogger(SearchableInterface.class);
		} catch (Exception ee) {
		}
	}

    public void setMainDataTableName(String mainTableName)
    {
        this.mainTableName = mainTableName;
    }

    public String getMainTableName()
    {
        return mainTableName;
    }

    public SearchableInterface()
    {
        searchableList = createSearchableList();
        mediaResourceList = createMediaResourcesList();
    }
	/**
	 * Return a list of searchable items given a display name
	 * @param datasourceName - The name of the datasource Table
	 * @return EFGDataObjectListInterface that encapsulate the lists.
	 * @throws Exception 
	 */
	public abstract EFGDataObjectListInterface getSearchables( String displayName, String datasourceName) throws Exception;
	/**
	 * Return a list of Medairesource items given a display name
	 * @param datasourceName - The name of the datasource Table
	 * @return EFGDataObjectListInterface that encapsulate the lists.
	 * @throws Exception 
	 */
	public abstract EFGDataObjectListInterface getMediaResources(String displayName,String datasourceName) throws Exception;
	/**
	 * A factory method
	 * @return the EFGDataObject created by the implementation class
	 */
	public abstract EFGDataObject createEFGDataObject();
	/**
	 * A factory for creating a searchable list by implementing classes
	 * @return
	 */
	public abstract EFGDataObjectListInterface createSearchableList();
	/**
	 * A factory for creating a MediaResource list by implementing classes
	 * @return
	 */
	public abstract EFGDataObjectListInterface createMediaResourcesList();
   
}
