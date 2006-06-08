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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import project.efg.servlets.efgInterface.SearchableObject;

/**
 * @author kasiedu
 *
 */
public abstract class SearchableListInterface {
	private String displayName;
	private String datasourceName;
	private String metadatasourceName;
	private Comparator compare;
	private List list;
	
	public SearchableListInterface(Comparator compare){
		this.compare = compare;
		this.list = new ArrayList();
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgImpl.SearchableListInterafce#addSearchable(project.efg.servlets.efgInterface.SearchableObject)
	 */
	public void addSearchable(SearchableObject searchable){
		this.list.add(searchable);
		Collections.sort(this.list,this.compare);
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgImpl.SearchableListInterafce#removeSearchable(project.efg.servlets.efgInterface.SearchableObject)
	 */
	public boolean removeSearchable(SearchableObject searchable){
		return this.list.remove(searchable);
	}
	public void setDisplayName(String display){
		this.displayName =display;
	}
	public void setDatasourceName(String datasource){
		this.datasourceName = datasource;
	}
	public void setMetadatasourceName(String metadata){	
		this.metadatasourceName =metadata;
	}
	
	public String getDisplayName(){
		return this.displayName;
	}
	public String getDatasourceName(){
		return this.datasourceName;
	}
	public String getMetadatasourceName(){
		return this.metadatasourceName ;
	}
	
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgImpl.SearchableListInterafce#getSearchableObject(int)
	 */
	public SearchableObject getSearchableObject(int order){
		return (SearchableObject)this.list.get(order);
	}

	
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgImpl.SearchableListInterafce#iterator()
	 */
	public Iterator iterator(){
		return this.list.iterator();
	}

	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.SearchableListInterafce#getSearchleObjectsCount()
	 */
	public int getSearchleObjectsCount() {
		return this.list.size();
	}

	/* (non-Javadoc)
	 * @see project.efg.servlets.efgImpl.SearchableListInterafce#getSearchableList()
	 */
	public SearchableListInterface getSearchableList(){
		return this;
	}
	
	

}