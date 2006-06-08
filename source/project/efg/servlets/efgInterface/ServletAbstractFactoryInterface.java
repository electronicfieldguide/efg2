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

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface;

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
	public ServletAbstractFactoryInterface(){
		
	}
	/**
	 * 
	 * @return alist of datasources from the database
	 */
	public EFGDatasourceObjectListInterface getListOfDatasources(){
		
		return this.createListOfDatasources();
	}
	/**
	 * 
	 * @return a list of searchable fields given a 
	 */
	public SearchableListInterface getSearchableLists(String displayName,String datasourceName){
		return this.createSearchableLists(displayName,datasourceName);
	}
	public SearchableListInterface getMediaResourceLists(String displayName, String datasourceName){
		return this.createMediaResourceLists(displayName,datasourceName);
	}
	public String getFirstFieldName(String displayName, String datasourceName){
		return this.createFirstFieldName(displayName,datasourceName);
	}
	public String getFirstMediaResourceFieldName(String displayName, String datasourceName){
		return this.createFirstMediaResourceFieldName(displayName,datasourceName);
	}
	public String getXSLFileName(String displayName, String datasourceName,String fieldName){
		return this.getXSLFileNameFromDB(displayName,datasourceName,fieldName);
	}
	protected abstract String getXSLFileNameFromDB(String displayName,String datasourceName,String fieldName);

	protected abstract SearchableListInterface createMediaResourceLists(String displayName,String datasourceName);
	protected abstract SearchableListInterface createSearchableLists(String displayName,String datasourceName);
	protected abstract EFGDatasourceObjectListInterface createListOfDatasources();
	protected abstract String createFirstFieldName(String displayName,String datasourceName);
	protected abstract String createFirstMediaResourceFieldName(String displayName,String datasourceName);
	
}
