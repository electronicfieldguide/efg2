/**
 * $Id$
 * $Name:  $
 * 
 * Copyright (c) 2007  University of Massachusetts Boston
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
package project.efg.client.drivers.nogui;

import java.util.ArrayList;
import java.util.List;

import project.efg.util.factory.TemplateModelFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;
import project.efg.util.utils.DBObject;
import project.efg.util.utils.TaxonPageDefaultConfig;
import project.efg.util.utils.TemplateModelHandler;

public class TemplateLoader {
	private List rdbDatasources = new ArrayList();
	private List allDatasources = new ArrayList();

	private TemplateModelHandler handler;
	private DBObject dbObject;
	public TemplateLoader(DBObject dbObject) {
		this.dbObject=dbObject;
		this.handler = TemplateModelFactory.createImportTemplateHandler(dbObject);

	}
	private List getListsOfDatasources(String tableName){
		
		try {
			return this.handler.executeQueryForList(makeQuery(tableName), 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void run(){
		List list = this.getListsOfDatasources(EFGImportConstants.EFG_RDB_TABLES);
		if(list != null){
			this.allDatasources.addAll(list);
			for(int i = 0; i < list.size();i++){
				EFGQueueObjectInterface queue = 
					(EFGQueueObjectInterface)list.get(i);
				System.out.println("Adding: " + queue.getObject(0));
				this.rdbDatasources.add(queue.getObject(0));
			}
		}

		list = this.getListsOfDatasources(EFGImportConstants.EFG_GLOSSARY_TABLES);
		if(list != null){
			this.allDatasources.addAll(list);
		}		
		loadHelperTables();
	}
	/**
	 * 
	 */
	private void alterRDBTable(String tableName) {	
		StringBuilder buffer = new StringBuilder("ALTER TABLE ");
		buffer.append(tableName);
	
		buffer.append(" ADD COLUMN ");
		buffer.append(EFGImportConstants.TEMPLATE_OBJECT_FIELD);
		buffer.append(" BLOB  AFTER `JAVASCRIPT_FILENAME`");
	
		try {
			this.handler.executeStatement(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 */
	private void loadHelperTables(){
		this.alterRDBTable(EFGImportConstants.EFG_RDB_TABLES);
		this.alterRDBTable(EFGImportConstants.EFG_GLOSSARY_TABLES);		
		

		for(int i = 0; i < this.allDatasources.size();i++){
			EFGQueueObjectInterface queue = 
				(EFGQueueObjectInterface)this.allDatasources.get(i);
			String datasourceName = queue.getObject(0);
			String displayName = queue.getObject(1);
			String tableName = this.getTableName(datasourceName);
			TaxonPageDefaultConfig taxonConfig = 
				new TaxonPageDefaultConfig(this.dbObject, 
					tableName);
			taxonConfig.processNew(datasourceName, displayName);
		}
	}
	
	private String makeQuery(String tableName){
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(EFGImportConstants.DS_DATA_COL);
		query.append(",");
		query.append(EFGImportConstants.DISPLAY_NAME_COL);
		query.append(" FROM ");
		query.append(tableName);
		return query.toString();
	}

	/**
	 * @param datasourceName
	 * @return
	 */
	private String getTableName(String datasourceName) {
		if (this.rdbDatasources.contains(datasourceName)) {
			return EFGImportConstants.EFG_RDB_TABLES;
		}
		return EFGImportConstants.EFG_GLOSSARY_TABLES;
	}
}
