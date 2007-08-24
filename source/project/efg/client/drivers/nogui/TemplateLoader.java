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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;


import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslFileNamesType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;
import project.efg.util.factory.SpringFactory;
import project.efg.util.factory.TemplateModelFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;
import project.efg.util.utils.DBObject;
import project.efg.util.utils.EFGDisplayObject;
import project.efg.util.utils.EFGUniqueID;
import project.efg.util.utils.TemplateMapObjectHandler;
import project.efg.util.utils.TemplateModelHandler;
import project.efg.util.utils.TemplateObject;

public class TemplateLoader {
	private List rddDatasources;

	private TemplateModelHandler handler;
	private DBObject dbObject;
	public TemplateLoader(DBObject dbObject) {
		this.dbObject=dbObject;
		this.handler = TemplateModelFactory.createImportTemplateHandler(dbObject);

		List list;
		try {
			list = this.handler.executeQueryForList(makeQuery(), 1);
			this.rddDatasources = getLists(list);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		this.alterTables();
	}
	private String makeQuery(){
		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		query.append(EFGImportConstants.DS_DATA_COL);
		query.append(" FROM ");
		query.append(EFGImportConstants.EFG_RDB_TABLES);
		return query.toString();
	}
	/**
	 * @param rddDatasources2
	 * @return
	 */
	private List getLists(List list) {
		if (list == null) {
			return new ArrayList();
		}
		List li = new ArrayList();
		
		for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
			EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
					.next();
			String datasoucreName = queue.getObject(0);
			li.add(datasoucreName);
		}
	
		return li;
	}

	private void alterRDBTable() {
		alterTable(EFGImportConstants.EFG_RDB_TABLES);
		alterTable(EFGImportConstants.EFG_GLOSSARY_TABLES);
	}

	/**
	 * 
	 */
	private void alterTables() {
		alterRDBTable();
		alterTemplateTable();

	}

	private void alterTable(String tableName) {
		StringBuffer buffer = new StringBuffer("ALTER TABLE ");
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

	private void alterTemplateTable() {
		StringBuffer buffer = new StringBuffer("ALTER TABLE ");
		buffer.append(EFGImportConstants.TEMPLATE_TABLE);
	
		buffer.append(" ADD COLUMN ");
		buffer.append(EFGImportConstants.QUERY_STR);
		buffer.append(" TEXT  AFTER `templateName`");
	
		try {
			this.handler.executeStatement(buffer.toString());
		} catch (Exception e) {
	
			e.printStackTrace();
		}
	}

	/**
	 * Replace all the old template names with the new one's
	 * 
	 * @param templateFolderDirectory
	 * @param filter
	 * @param map
	 */
	public void loadTemplateFilesIntoDB(
			File templateFolderDirectory, FilenameFilter filter, Properties map) {
		updateTemplateTable(map);
		updateRDBTable(templateFolderDirectory, filter, map);
	}

	/**
	 * @param dbObject
	 * @param map
	 */
	private void updateTemplateTable(Properties map) {
		Hashtable table = TemplateMapObjectHandler
				.getTemplateObjectMap(dbObject);
		Set set = map.keySet();
		Set tableSet = table.keySet();
	
		Iterator iterT = tableSet.iterator();
	
		while (iterT.hasNext()) {
			String key = (String) iterT.next();
			TemplateObject templateObject = (TemplateObject) table.get(key);
			Iterator iter = set.iterator();
			while (iter.hasNext()) {
				String patternStr = (String) iter.next();
				String replacementStr = (String) map.get(patternStr);
				Pattern pattern = Pattern.compile(patternStr);
				Matcher matcher = pattern.matcher(key);
				key = matcher.replaceAll(replacementStr);
			}
			TemplateMapObjectHandler.updateTemplateDatabase(key,
					templateObject, dbObject);
		}
	}

	/**
	 * @param dbObject
	 * @param templateFolderDirectory
	 * @param map
	 */
	private void updateRDBTable(
			File templateFolderDirectory, FilenameFilter filter, Properties map) {
		File[] files = templateFolderDirectory.listFiles(filter);

		for (int i = 0; i < files.length; i++) {
			File filename = files[i];
			StringBuffer buffer = new StringBuffer();
			try {
				BufferedReader rd = new BufferedReader(new FileReader(filename));
				String line = null;
				while ((line = rd.readLine()) != null) {
					buffer.append(line);
				}
			} catch (IOException e) {
			}
			Set set = map.keySet();
			Iterator iter = set.iterator();

			while (iter.hasNext()) {
				String patternStr = (String) iter.next();
				String replacementStr = (String) map.get(patternStr);
				Pattern pattern = Pattern.compile(patternStr);
				Matcher matcher = pattern.matcher(buffer.toString());
				String output = matcher.replaceAll(replacementStr);
				buffer = new StringBuffer(output);
			}
			StringReader reader = new StringReader(buffer.toString());
			try {
				TaxonPageTemplates tps = (TaxonPageTemplates) TaxonPageTemplates
						.unmarshalTaxonPageTemplates(reader);
				this.addDefaults(tps);
				this.insertIntoTemplateTable(tps);
				reader.close();
				removeFile(filename);
				// insert a line of it and taxon pages into template table
			} catch (MarshalException e) {
				reader.close();
				e.printStackTrace();
			} catch (ValidationException e) {
				reader.close();
				e.printStackTrace();
			} catch (Exception ee) {
				reader.close();
				ee.printStackTrace();
			}
		}
	}

	/**
	 * @param files
	 */
	private void removeFile(File file) {
		file.delete();
	}

	private void insertDefaultSearchPage(XslFileNamesType xsls,
			String tableName, String datasourceName) {

		StringBuffer constantsB = new StringBuffer(
				"/efg2/searchPageBuilder?ALL_TABLE_NAME=");
		constantsB.append(tableName);
		constantsB
				.append("&displayFormat=HTML&maxDisplay=100&searchType=searches&dataSourceName=");
		constantsB.append(datasourceName);

		XslPageType xpage = xsls.getXslTaxonPages();

		for (int i = 0; i < xpage.getXslPageCount(); i++) {
			XslPage xslPage = xpage.getXslPage(i);

			TemplateObject templateObject = SpringFactory.getTemplateObject();
			templateObject.setGUID(xslPage.getGuid());
			templateObject.setTemplateName(xslPage.getDisplayName());
			EFGDisplayObject displayObject = SpringFactory.getDisplayObject();
			displayObject.setDatasourceName(datasourceName);
			templateObject.setDisplayObject(displayObject);
			StringBuffer key = new StringBuffer(constantsB.toString());
			if (xslPage.getGuid() != null) {
				key.append("&guid=");
				key.append(xslPage.getGuid());
			}
			String xslName = xslPage.getFileName();
			if (xslName != null) {
				key.append("&xslName=");
				key.append(xslName);
			}

			TemplateMapObjectHandler.add2TemplateMap(key.toString(),
					templateObject, dbObject);
		}
	}

	private static XslPageType insertDefaultTaxonPage() {
		XslPageType xslPageT = new XslPageType();
		XslPage pa = new XslPage();
		pa.setFileName(EFGImportConstants.DEFAULT_TAXON_PAGE_FILE);
		pa.setGuid(EFGUniqueID.getID() + "");
		pa.setDisplayName(EFGImportConstants.DEFAULT_TAXON_DISPLAY);
		pa.setIsDefault(true);
		xslPageT.addXslPage(pa);
		return xslPageT;
	}

	private void insertIntoTemplateTable(TaxonPageTemplates tps) {
		TaxonPageTemplateType tp = tps.getTaxonPageTemplate(0);
		String datasourceName = tp.getDatasourceName();

		// find out if it is a glossary or an rdb
		String tableName = this.getTableName(datasourceName);
		XslFileNamesType xsls = tp.getXSLFileNames();
		if (xsls.getXslTaxonPages() != null
				&& xsls.getXslTaxonPages().getXslPageCount() > 0) {
			insertTaxonPagesIntoTemplatesTable(xsls, tableName,
					datasourceName);
		} else {
			xsls.setXslTaxonPages(insertDefaultTaxonPage());
		}
		if (xsls.getXslSearchPages() == null
				|| xsls.getXslSearchPages().getXslPageCount() == 0) {
			insertDefaultSearchPage(xsls,tableName, datasourceName);
		}
		TemplateMapObjectHandler.updateDatabase(dbObject, tps, datasourceName,
				tableName);
	}

	/**
	 * @param tps
	 */
	private void insertTaxonPagesIntoTemplatesTable(XslFileNamesType xsls,
			String tableName, String datasourceName) {
	

	

		StringBuffer constantsB = new StringBuffer(
				"/efg2/search?ALL_TABLE_NAME=");
		constantsB.append(tableName);
		constantsB
				.append("&displayFormat=HTML&maxDisplay=1&searchType=taxon&dataSourceName=");
		constantsB.append(datasourceName);
		XslPageType xpage = xsls.getXslTaxonPages();

		for (int i = 0; i < xpage.getXslPageCount(); i++) {
			XslPage xslPage = xpage.getXslPage(i);

			TemplateObject templateObject = SpringFactory.getTemplateObject();
			templateObject.setGUID(xslPage.getGuid());
			templateObject.setTemplateName(xslPage.getDisplayName());
			EFGDisplayObject displayObject = SpringFactory.getDisplayObject();
			displayObject.setDatasourceName(datasourceName);
			templateObject.setDisplayObject(displayObject);
			StringBuffer key = new StringBuffer(constantsB.toString());
			if (xslPage.getGuid() != null) {
				key.append("&guid=");
				key.append(xslPage.getGuid());
			}
			String xslName = xslPage.getFileName();
			if (xslName != null) {
				key.append("&xslName=");
				key.append(xslName);
			}
			
			TemplateMapObjectHandler.add2TemplateMap(key.toString(),
					templateObject, dbObject);
		}
	}

	/**
	 * @param datasourceName
	 * @return
	 */
	private String getTableName(String datasourceName) {
		if (this.rddDatasources.contains(datasourceName)) {
			return EFGImportConstants.EFG_RDB_TABLES;
		}
		return EFGImportConstants.EFG_GLOSSARY_TABLES;
	}

	private static boolean addDefaultTaxonPage(XslFileNamesType xsls) {
		XslPageType xslPageT = xsls.getXslTaxonPages();
		if (xslPageT == null || xslPageT.getXslPageCount() == 0) {

			xslPageT = new XslPageType();
			XslPage pa = new XslPage();
			pa.setFileName(EFGImportConstants.DEFAULT_TAXON_PAGE_FILE);
			pa.setGuid(EFGUniqueID.getID() + "");
			pa.setDisplayName(EFGImportConstants.DEFAULT_TAXON_DISPLAY);
			pa.setIsDefault(true);
			xslPageT.addXslPage(pa);
			xsls.setXslTaxonPages(xslPageT);
			return true;
		}
		return false;
	}

	private void addDefaults(TaxonPageTemplates tps) {

		TaxonPageTemplateType tp = tps.getTaxonPageTemplate(0);
		XslFileNamesType xsls = tp.getXSLFileNames();
		if (xsls.getXslSearchPages() == null
				|| xsls.getXslSearchPages().getXslPageCount() == 0) {
			addDefaultSearchPage(xsls);
		}
		if (xsls.getXslTaxonPages() == null
				|| xsls.getXslTaxonPages().getXslPageCount() == 0) {
			addDefaultTaxonPage(xsls);
		}
	}

	private void addDefaultSearchPage(XslFileNamesType xsls) {
		XslPageType xslPageType = 
			getXSLPageType(EFGImportConstants.DEFAULT_SEARCH_PAGE_FILE);
		xsls.setXslSearchPages(xslPageType);
	}

	private XslPageType getXSLPageType(String xslFile) {
		XslPageType xslPageType = new XslPageType();
		XslPage xslPage = new XslPage();
		xslPage.setIsDefault(true);
		xslPage.setFileName(xslFile);
		xslPage.setDisplayName(EFGImportConstants.DEFAULT_SEARCH_DISPLAY);
		xslPage.setGuid(EFGUniqueID.getID() + "");
		xslPageType.addXslPage(xslPage);
		return xslPageType;
	}
}
