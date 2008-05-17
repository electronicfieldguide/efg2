/**
 * $Id$
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

package project.efg.server.utils;

import java.util.Hashtable;

import project.efg.templates.taxonPageTemplates.CharacterValue;
import project.efg.templates.taxonPageTemplates.GroupType;
import project.efg.templates.taxonPageTemplates.GroupTypeItem;
import project.efg.templates.taxonPageTemplates.GroupsType;
import project.efg.templates.taxonPageTemplates.GroupsTypeItem;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslFileNamesType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.TemplateMapObjectHandler;

public class TemplatePopulator implements EFGImportConstants {

	public TemplatePopulator() {
		tableStore = new Hashtable();
		numberOfGroups = 0;
	}

	public Hashtable populateTable(String fileName, String guid,
			String itemType, String dsName, String dbTableName) {
		String mutex = "";
		synchronized (mutex) {
			StringBuffer key = new StringBuffer(fileName);
			key.append(EFGImportConstants.UNDER_SCORE);
			key.append(guid);
			key.append(EFGImportConstants.UNDER_SCORE);
			key.append(itemType);
			key.append(EFGImportConstants.UNDER_SCORE);
			key.append(dsName);
			Hashtable table = (Hashtable) tableStore.get(key);
			if (table == null) {
				table = getTable(guid, itemType, dsName, dbTableName);
				if (table != null)
					tableStore.put(key, table);
			}
			return table;
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getNumberOfGroups() {
		return numberOfGroups;
	}

	/**
	 * 
	 * @param guid
	 * @param itemType
	 * @param dsName
	 * @param dbTableName
	 * @return
	 */
	private Hashtable getTable(String guid, String itemType, String dsName,
			String dbTableName) {
		TaxonPageTemplates tps = getTaxonPageTemplate(dsName, dbTableName);
		if (tps == null)
			return null;
		XslPage xslPage = getXSLPage(tps, guid, itemType, dsName);
		if (xslPage != null) {
			numberOfGroups = xslPage.getGroups().getGroupsTypeItemCount();
			return populateTable(xslPage);
		} else {
			return new Hashtable();
		}
	}

	private Hashtable populateTable(XslPage xslPage) {
		GroupsType groups = xslPage.getGroups();
		boolean isDefault = xslPage.getIsDefault();
		Hashtable table = new Hashtable();
		table.put("isDefault", isDefault + "");
		if (groups != null)
			handleGroups(groups, table);
		return table;
	}

	private void handleGroups(GroupsType groups, Hashtable table) {
		int counter = groups.getGroupsTypeItemCount();
		for (int i = 0; i < counter; i++) {
			GroupsTypeItem items = groups.getGroupsTypeItem(i);
			if (items == null)
				continue;
			GroupType group = items.getGroup();
			if (group == null)
				continue;
			int groupCount = group.getGroupTypeItemCount();
			String groupID = group.getId();
			int groupRank = group.getRank();
			String groupLabel = group.getLabel();
			String groupText = group.getText();
			String key = null;
			String constructor = ":" + groupID + ":" + groupRank;
			if (groupLabel != null && !groupLabel.trim().equals("")) {
				key = "gl" + constructor;
				table.put(key, groupLabel);
			}
			if (groupText != null && !groupText.trim().equals("")) {
				key = "gtl" + constructor;
				table.put(key, groupText);
			}
			for (int jj = 0; jj < groupCount; jj++) {
				GroupTypeItem item = group.getGroupTypeItem(jj);
				CharacterValue characterVal = item.getCharacterValue();
				String charLabel = characterVal.getLabel();
				int charRank = characterVal.getRank();
				String charText = characterVal.getText();
				String charValue = characterVal.getValue();
				if (charLabel != null && !charLabel.trim().equals("")) {
					key = "cl" + constructor + ":" + charRank;
					table.put(key, charLabel);
				}
				if (charText != null && !charText.trim().equals("")) {
					key = "ctl" + constructor + ":" + charRank;
					table.put(key, charText);
				}
				if (charValue != null && !charValue.trim().equals("")) {
					key = "group" + constructor + ":" + charRank;
					table.put(key, charValue);
				}
				GroupsType groupsT = item.getGroups();
				if (groupsT != null)
					handleGroups(groupsT, table);
			}

		}

	}

	private XslPage getXSLPage(TaxonPageTemplates tps, String guid,
			String xslType, String dsName) {
		XslPage page = null;
		try {
			int counter = tps.getTaxonPageTemplateCount();
			int i = 0;
			do {
				if (i >= counter)
					break;
				XslPageType xslPageType = null;
				TaxonPageTemplateType tp = tps.getTaxonPageTemplate(i);
				String ds = tp.getDatasourceName();
				if (ds.equalsIgnoreCase(dsName.trim())) {
					XslFileNamesType xslFileNames = tp.getXSLFileNames();
					if ("XSL_FILENAME_TAXON".equalsIgnoreCase(xslType))
						xslPageType = xslFileNames.getXslTaxonPages();
					else if ("XSL_FILENAME_SEARCHPAGE_PLATES"
							.equalsIgnoreCase(xslType))
						xslPageType = xslFileNames.getXslPlatePages();
					else if ("XSL_FILENAME_SEARCHPAGE_PDF"
							.equalsIgnoreCase(xslType))
						xslPageType = xslFileNames.getXslPdfPages();
					else if ("XSL_FILENAME_SEARCHPAGE_PDFBOOK"
							.equalsIgnoreCase(xslType))
						xslPageType = xslFileNames.getXslBookPages();
					else if ("XSL_FILENAME_SEARCHPAGE"
							.equalsIgnoreCase(xslType))
						xslPageType = xslFileNames.getXslSearchPages();
					else
						xslPageType = xslFileNames.getXslListPages();
					if (xslPageType != null) {
						int j = 0;
						do {
							if (j >= xslPageType.getXslPageCount())
								break;
							XslPage currentPage = xslPageType.getXslPage(j);
							String currentGuid = currentPage.getGuid();
							if (currentGuid.equalsIgnoreCase(guid)) {
								page = currentPage;
								break;
							}
							j++;
						} while (true);
					}
					break;
				}
				i++;
			} while (true);
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
			return null;
		}
		return page;
	}

	private TaxonPageTemplates getTaxonPageTemplate(String dsName, String tName) {
		if (dsName == null) {
			return null;
		}
		return TemplateMapObjectHandler.getTemplateFromDB(null, null, dsName,
				tName);
	}

	private Hashtable tableStore;
	private int numberOfGroups;

}
