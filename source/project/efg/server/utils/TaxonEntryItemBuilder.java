/**
 * $Id$
 * $Name:  $
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
package project.efg.server.utils;

import java.util.Iterator;

import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.efgDocument.TaxonEntryTypeItem;
import project.efg.server.factory.EFGSpringFactory;
import project.efg.server.interfaces.EFGParseObjectFactory;
import project.efg.server.interfaces.HandleStatInputAbstract;
import project.efg.server.interfaces.StatisticalMesureComparatorInterface;
import project.efg.util.factory.SpringFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.RegularExpresionConstants;
import project.efg.util.utils.EFGObject;
import project.efg.util.utils.EFGParseObject;
import project.efg.util.utils.EFGParseObjectList;
import project.efg.util.utils.EFGParseStates;

/**
 * @author kasiedu
 * 
 */
public class TaxonEntryItemBuilder {
	private EFGParseObjectFactory parseFactory; 
	
	private EFGParseStates efgParseStates;
	private HandleStatInputAbstract handleStats;
		
	public TaxonEntryItemBuilder(){
		this.parseFactory = EFGSpringFactory
		.getParseObjectFactory();//use Spring
		this.efgParseStates = SpringFactory.getEFGParseStatesInstance();//use Spring
		StatisticalMesureComparatorInterface stats = new StatisticalMeasureComparator();
		this.handleStats = new HandleStatInput(stats);//use Spring
	}

	/**
	 * 
	 * @param userValues - if null no parsing will be done. Elements will be created with the
	 * data in the states variable
	 * @param databaseValues - If paramvalues is not null then there must be a match with some of the data in states.
	 * @param efgObject - Contains the name, the datatype and the database name
	 * @param isLike - true to indicate that 'contains' should be used to match strings instead of equals
	 * @return null if paramValues is not null and it does not match exactly anything in the states
	 */
	public TaxonEntryTypeItem buildTaxonEntryItem(String userValues,
			String databaseValues, 
			EFGObject efgObject, boolean isLike) {
		TaxonEntryTypeItem taxonItem = null;
		EFGParseObjectList lists = null;
		if (EFGImportConstants.ISLISTS.equalsIgnoreCase(efgObject.getDataType())) {
				boolean flag = true;
			lists =
				this.efgParseStates.parseStates(RegularExpresionConstants.LISTSEP,databaseValues,true);
		
			if(userValues != null){
				if(!this.compareString(lists,userValues, isLike)){
					flag = false;
				}
			}
			if(flag){
				EFGListsType items = this.createLists(lists);
				if (items != null) {
					items.setName(efgObject.getName());
					items.setDatabaseName(efgObject.getDatabaseName());
					taxonItem = new TaxonEntryTypeItem();
					taxonItem.setEFGLists(items);
				}
			}
		}
		else if(EFGImportConstants.NUMERIC.equalsIgnoreCase(efgObject.getDataType())
			||(EFGImportConstants.NUMERICRANGE.equalsIgnoreCase(efgObject.getDataType()))){
			boolean flag = true;
				lists =
					this.efgParseStates.parseStates(RegularExpresionConstants.ORCOMMAPATTERN,databaseValues,false); 
		
					StatisticalMeasuresType dbStats = 
						this.createStatisticalMeasure(lists);
					if(dbStats == null){
						flag = false;
					}
					else if(userValues != null && !userValues.trim().equals("")){
						flag = this.handleStats.isInRange(dbStats, userValues);
					}
					if(flag){
						taxonItem = new TaxonEntryTypeItem();
						dbStats.setName(efgObject.getName());
						dbStats.setDatabaseName(efgObject.getDatabaseName());
						taxonItem.setStatisticalMeasures(dbStats);
					}
				
			}
		else if (EFGImportConstants.MEDIARESOURCE.equalsIgnoreCase(efgObject.getDataType())) {
			boolean flag = true;
			lists =
				this.efgParseStates.parseStates(RegularExpresionConstants.LISTSEP,databaseValues,true); 
			if(userValues != null){
				if(!this.compareString(lists,userValues, isLike)){
					flag = false;
				}
			}
			if(flag){
				lists.setDatabaseName(efgObject.getDatabaseName());
				lists.setName(efgObject.getName());
				MediaResourcesType items =
					this.createMediaResources(lists);
				if (items != null) {
					taxonItem = new TaxonEntryTypeItem();
					taxonItem.setMediaResources(items);
				}
			}
		}
		else if (EFGImportConstants.CATEGORICAL.equals(efgObject.getDataType())) {
			boolean flag = true;
			lists =
				this.efgParseStates.parseStates(RegularExpresionConstants.ORCOMMAPATTERN,databaseValues,false); 
		
			if(userValues != null){
				flag = this.compareString(lists,userValues, isLike);
			
			}
			if(flag){
				lists.setDatabaseName(efgObject.getDatabaseName());
				lists.setName(efgObject.getName());
				ItemsType items = this.createCategorical(lists);
				if (items != null) {
					taxonItem = new TaxonEntryTypeItem();
					taxonItem.setItems(items);
				}

			}
		}
		else{
			boolean flag = true;
			lists =
				this.efgParseStates.parseStates(RegularExpresionConstants.NOPATTERN,databaseValues,true);
			
			if(userValues != null){
				flag = this.compareString(lists,userValues, isLike);
			}
			if(flag){
				lists.setDatabaseName(efgObject.getDatabaseName());
				lists.setName(efgObject.getName());
				ItemsType items = this.createNarrative(databaseValues,efgObject);
				if (items != null) {
					taxonItem = new TaxonEntryTypeItem();
					taxonItem.setItems(items);
				}
			}
		}
		return taxonItem;
		
	}


	private boolean compareString(EFGParseObjectList lists,String userValue, boolean isLike){
		Iterator iter = lists.iterator();
		while(iter.hasNext()){
			EFGParseObject obj = (EFGParseObject)iter.next();
			String dbState = obj.getState().toLowerCase();
			String userState = userValue.toLowerCase();
			
			if(isLike) {
				if(dbState.indexOf(userState) > -1 ){
					return true;
				}
			}
			else if(dbState.equals(userState)){
				return true; //there is a match
			}
		}
		return false;//no match
	}
	private MediaResourcesType createMediaResources(EFGParseObjectList lists) {
		return parseFactory.createMediaResources(lists);
	}
	private ItemsType createCategorical(EFGParseObjectList lists) {
		return parseFactory.createItems(lists);
	}

	private EFGListsType createLists(EFGParseObjectList lists) {

		return parseFactory.createEFGLists(lists);

	}

	private ItemsType createNarrative(String states,EFGObject efgObject) {
		return parseFactory.createItemsNoParse(states,efgObject.getName(), efgObject.getDatabaseName());
	}

	private StatisticalMeasuresType createStatisticalMeasure(
			EFGParseObjectList lists) {

		return parseFactory.createStatisticalMeasures(lists);

	}
}
