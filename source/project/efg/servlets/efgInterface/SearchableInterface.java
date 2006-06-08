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
package project.efg.servlets.efgInterface;

import org.apache.log4j.Logger;





/**
 * @author kasiedu
 *
 */
public abstract class SearchableInterface {
	protected SearchableListInterface searchableList;
	protected SearchableListInterface mediaResourceList;
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(SearchableInterface.class);
		} catch (Exception ee) {
		}
	}
	public SearchableInterface() {
		this.searchableList = this.createSearchableList();
		this.mediaResourceList = this.createMediaResourcesList();
		
	}
	/**
	 * Return a list of searchable items given a display name
	 * @param datasourceName - The name of the datasource Table
	 * @return SearchableListInterface that encapsulate the lists.
	 * @throws Exception 
	 */
	public abstract SearchableListInterface getSearchables( String displayName, String datasourceName) throws Exception;
	/**
	 * Return a list of Medairesource items given a display name
	 * @param datasourceName - The name of the datasource Table
	 * @return SearchableListInterface that encapsulate the lists.
	 * @throws Exception 
	 */
	public abstract SearchableListInterface getMediaResources(String displayName,String datasourceName) throws Exception;
	/**
	 * A factory method
	 * @return the SearchableObject created by the implementation class
	 */
	public abstract SearchableObject createSearchableObject();
	/**
	 * A factory for creating a searchable list by implementing classes
	 * @return
	 */
	public abstract SearchableListInterface createSearchableList();
	/**
	 * A factory for creating a MediaResource list by implementing classes
	 * @return
	 */
	public abstract SearchableListInterface createMediaResourcesList();
	
	
}
