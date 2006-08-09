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

import java.util.Iterator;



import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.StatisticalMeasureType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.efgDocument.TaxonEntryTypeItem;
import project.efg.servlets.efgInterface.OperatorInterface;
import project.efg.servlets.efgServletsUtil.EFGParseObject;
import project.efg.servlets.efgServletsUtil.EFGParseObjectList;
import project.efg.servlets.efgServletsUtil.EFGParseStates;
import project.efg.servlets.efgServletsUtil.StatisticalMeasureComparator;
import project.efg.util.EFGImportConstants;
import project.efg.util.EFGObject;

/**
 * @author kasiedu
 * 
 */
public class TaxonEntryItemBuilder {
	private EFGParseObjectFactory parseFactory; 
	private StatisticalMeasureComparator measureComparator;
	
	private EFGParseStates efgParseStates;
	
		
	public TaxonEntryItemBuilder(){
		this.measureComparator = new StatisticalMeasureComparator();
		this.parseFactory = CreateEFGParseObjectFactory
		.getInstance();
		this.efgParseStates = new EFGParseStates();
	}

	
	/**
	 * 
	 * @param paramValues - if null no parsing will be done. Elements will be created with the
	 * data in the states variable
	 * @param states - If paramvalues is not null then there must be a match with some of the data in states.
	  * @param efgObject - Contains the name, the datatype and the database name
	 * @return null if paramValues is not null and it does not match exactly anything in the states
	 */
	public TaxonEntryTypeItem buildTaxonEntryItem(String paramValues,
			String states, 
			EFGObject efgObject) {
		TaxonEntryTypeItem taxonItem = null;
		EFGParseObjectList lists = null;
		if (EFGImportConstants.ISLISTS.equalsIgnoreCase(efgObject.getDataType())) {
				boolean flag = true;
			lists =
				this.efgParseStates.parseStates(EFGImportConstants.LISTSEP,states,true);
		
			if(paramValues != null){
				if(!this.compareString(lists,paramValues)){
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
		else if (EFGImportConstants.NUMERICRANGE.equalsIgnoreCase(efgObject.getDataType()) ||
			(EFGImportConstants.NUMERIC.equalsIgnoreCase(efgObject.getDataType()))) {
			boolean flag = true;
			lists =
				this.efgParseStates.parseStates(EFGImportConstants.ORCOMMAPATTERN,states,false); 
	
				StatisticalMeasuresType dbStats = 
					this.createStatisticalMeasure(lists);
				if(dbStats == null){
					flag = false;
				}
				else if(paramValues != null){
					EFGParseObject userInputStat =
						this.efgParseStates.parseUserStats(EFGImportConstants.ORCOMMAPATTERN,paramValues); 
					EFGParseObjectList userInputList = new EFGParseObjectList();
					userInputList.add(userInputStat);
					
						StatisticalMeasuresType userInputStats = 
							this.createStatisticalMeasure(userInputList);
						
						if ( userInputStats == null) {
							flag = false;
						}
						else{
							flag = compareStats(dbStats,userInputStats,userInputStat.getOperator());
						}
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
				this.efgParseStates.parseStates(EFGImportConstants.LISTSEP,states,true); 
			if(paramValues != null){
				if(!this.compareString(lists,paramValues)){
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
				this.efgParseStates.parseStates(EFGImportConstants.ORCOMMAPATTERN,states,false); 
		
			if(paramValues != null){
				flag = this.compareString(lists,paramValues);
			
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
				this.efgParseStates.parseStates(EFGImportConstants.NOPATTERN,states,true);
			
			if(paramValues != null){
				flag = this.compareString(lists,paramValues);
			}
			if(flag){
				lists.setDatabaseName(efgObject.getDatabaseName());
				lists.setName(efgObject.getName());
				ItemsType items = this.createNarrative(states,efgObject);
				if (items != null) {
					taxonItem = new TaxonEntryTypeItem();
					taxonItem.setItems(items);
				}
			}
		}
		return taxonItem;
		
	}


	private boolean compareString(EFGParseObjectList lists,String userValue){
		Iterator iter = lists.iterator();
		while(iter.hasNext()){
			EFGParseObject obj = (EFGParseObject)iter.next();
			if(obj.getState().equalsIgnoreCase(userValue)){
				return true; //there is a match
			}
		}
		return false;//no match
	}
	private boolean compareStats(StatisticalMeasuresType dbStats,
			StatisticalMeasuresType userValues,OperatorInterface operator){
		
		for(int i = 0; i < userValues.getStatisticalMeasureCount(); i++){
			StatisticalMeasureType userValue = userValues.getStatisticalMeasure(i);
			for(int j = 0; j < dbStats.getStatisticalMeasureCount();j++){
				StatisticalMeasureType databaseValue = dbStats.getStatisticalMeasure(i);
				//is userValue in the range of the value in the database?
				try{
					if(databaseValue.getUnits() == null){
						databaseValue.setUnits("");
					}
					if(userValue.getUnits() == null){
						userValue.setUnits("");
					}
					if(this.measureComparator.isInRange(operator,userValue,databaseValue)){
						return true;
					}
				}
				catch(Exception ee){
					
				}
			}
		}
		return false;
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
