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
package project.efg.servlets.efgServletsUtil;



import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.servlets.efgInterface.EFGDataObject;

/**
 * @author kasiedu
 *
 */
public class EFGDataObjectImpl implements EFGDataObject {
	private ItemsType statesList;
	private StatisticalMeasuresType statsList;
	private MediaResourcesType media;
	private EFGListsType efgLists;
	private String name;
	private String legalName;
	private int order = -1;
	

	public EFGDataObjectImpl(){
		
	}
	/**
	 * 
	 * @param order in which this object appears in a list
	 */
	public void setOrder(int order){
		this.order = order;
	}
	/**
	 * 
	 * @return the order in which this object appears in a list 
	 */
	public int getOrder(){
		return this.order;
	}
	
	/**
	 * 
	 * @return the name for the current object
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * 
	 * @param name - The name for the current object
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * 
	 * @return the legalName for the current object
	 */
	public String getLegalName(){
		return this.legalName;
	}
	
	/**
	 * 
	 * @param legalName - The legalName for the current object
	 */
	public void setLegalName(String legalName){
		this.legalName = legalName;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGDataObject#equals(project.efg.servlets.efgInterface.EFGDataObject)
	 */
	public boolean equals(EFGDataObject object) {
		return this.getOrder() == object.getOrder();
	}
	public int hashCode(){
		return this.getOrder();
	}
	/** 
	 * 
	 * @return a list of values for the key
	 */
	public ItemsType getStates() {
		
		return this.statesList;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGDataObject#setItems(project.efg.efgDocument.ItemsType)
	 */
	public void setStates(ItemsType list) {
		if(list != null){
			
		}
		//sort here
		this.statesList = list;
	}
	/** 
	 * 
	 * @return a list of values for the key
	 */
	public StatisticalMeasuresType getStatisticalMeasures() {
		return this.statsList;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGDataObject#setStatisticalMeasures(project.efg.efgDocument.StatisticalMeasuresType)
	 */
	public void setStatisticalMeasures(StatisticalMeasuresType list) {
		//sort here
		this.statsList = list;
		
	}
	
	
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGDataObject#getEFGLists()
	 */
	public EFGListsType getEFGLists() {
		return this.efgLists;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGDataObject#setEFGListsType(project.efg.efgDocument.EFGListsType)
	 */
	public void setEFGListsType(EFGListsType lists) {
		//sort here
		this.efgLists = lists;
		
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGDataObject#getMediaResources()
	 */
	public MediaResourcesType getMediaResources() {
		return this.media;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGDataObject#setMediaResources(project.efg.efgDocument.MediaResourcesType)
	 */
	public void setMediaResources(MediaResourcesType mediaResources) {
		//sort here
		this.media = mediaResources;
		
	}
	
}
