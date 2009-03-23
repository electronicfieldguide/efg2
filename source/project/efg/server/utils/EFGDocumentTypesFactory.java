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

import java.util.List;

import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.server.factory.EFGSpringFactory;
import project.efg.server.interfaces.EFGParseObjectFactory;
import project.efg.util.factory.SpringFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;
import project.efg.util.interfaces.QueryExecutorInterface;
import project.efg.util.interfaces.RegularExpresionConstants;
import project.efg.util.utils.EFGObject;
import project.efg.util.utils.EFGParseObjectList;
import project.efg.util.utils.EFGParseStates;

/**
 * @author kasiedu
 * 
 */
public class EFGDocumentTypesFactory {
	

	private EFGParseObjectFactory parseFactory; 

	private QueryExecutorInterface queryExecutor;

	private EFGParseStates efgParseStates;

	public EFGDocumentTypesFactory() {
		this.parseFactory = EFGSpringFactory
		.getParseObjectFactory();
		this.queryExecutor = SpringFactory.getQueryExecutor();
		this.efgParseStates = SpringFactory.getEFGParseStatesInstance();
	}

	private String getSearchableDataQuery(String name, String ds) {
		StringBuilder queryBuffer = new StringBuilder();
		queryBuffer.append("SELECT ");
		queryBuffer.append(name);
		queryBuffer.append(" FROM ");
		queryBuffer.append(ds);
		return queryBuffer.toString();
	}

	private EFGParseObjectList parseObject(List list, String separator,
			EFGObject efgObject,boolean notRemoveParen) {
		EFGParseObjectList oldList = new EFGParseObjectList();
		oldList.setDatabaseName(efgObject.getDatabaseName());
		oldList.setName(efgObject.getName());

		for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
			EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
					.next();
			String states = queue.getObject(0);
			if ((states != null) && (!states.trim().equals(""))) {
				EFGParseObjectList lists1 = this.efgParseStates.parseStates(
						separator, states,notRemoveParen);
				oldList.addEFGParseObjectList(lists1);
			}
		}
		return oldList;
	}

	private ItemsType createCategorical(List list, EFGObject efgObject) {
		ItemsType items = null;
		try {
			EFGParseObjectList oldList = parseObject(list,
					RegularExpresionConstants.ORCOMMAPATTERN, efgObject,false);
			if (oldList.getSize() > 0) {
				items = parseFactory.createItems(oldList);
				if (items != null) {
					items.setName(oldList.getName());
					items.setDatabaseName(oldList.getDatabaseName());
				}
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
		}
		return items;
	}
	private ItemsType createNarrative(List list, EFGObject efgObject) {
		ItemsType items = null;
		try {
			EFGParseObjectList oldList = parseObject(list,
					RegularExpresionConstants.NOPATTERN, efgObject,true);
			if (oldList.getSize() > 0) {
				items = parseFactory.createItems(oldList);
				if (items != null) {
					items.setName(oldList.getName());
					items.setDatabaseName(oldList.getDatabaseName());
				}
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
		}
		return items;
	}
	private MediaResourcesType createMediaResources(List list,
			EFGObject efgObject) {
		MediaResourcesType media = null;

		try {
			EFGParseObjectList oldList = parseObject(list,
					RegularExpresionConstants.LISTSEP, efgObject,true);
			if (oldList.getSize() > 0) {
				media = parseFactory.createMediaResources(oldList);
				if (media != null) {
					media.setName(oldList.getName());
					media.setDatabaseName(oldList.getDatabaseName());
				}
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
		}
		return media;
	}

	private EFGListsType createEFGLists(List list, EFGObject efgObject) {
		EFGListsType lists = null;
		try {
			EFGParseObjectList oldList = parseObject(list,
					RegularExpresionConstants.LISTSEP, efgObject,true);

			if (oldList.getSize() > 0) {
				lists = parseFactory.createEFGLists(oldList);
				if (lists != null) {
					lists.setName(oldList.getName());
					lists.setDatabaseName(oldList.getDatabaseName());
				}
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
		}
		return lists;
	}

	private StatisticalMeasuresType createStatisticalMeasures(List list,
			EFGObject efgObject) {
		StatisticalMeasuresType stats = null;

		try {
			EFGParseObjectList oldList = parseObject(list,
					RegularExpresionConstants.ORCOMMAPATTERN, efgObject,false);

			if (oldList.getSize() > 0) {
				stats = parseFactory.createStatisticalMeasures(oldList);
				if (stats != null) {
					stats.setName(oldList.getName());
					stats.setDatabaseName(oldList.getDatabaseName());
				}
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
		}
		return stats;
	}

	public Object getEFGDocumentTypesFactory(EFGObject efgObject,
			String datasource) {

		List list;
		try {
			list = this.queryExecutor.executeQueryForList(
					getSearchableDataQuery(efgObject.getDatabaseName(),
							datasource), 1);
			if (list == null) {
				throw new Exception("List is null for field name: "
						+ efgObject.getDatabaseName());
			}
			if (EFGImportConstants.ISLISTS.equalsIgnoreCase(efgObject
					.getDataType())) {
				return createEFGLists(list, efgObject);
			} else if (EFGImportConstants.NUMERIC.equalsIgnoreCase(efgObject
					.getDataType())) {
				return createStatisticalMeasures(list, efgObject);

			} else if (EFGImportConstants.NUMERICRANGE
					.equalsIgnoreCase(efgObject.getDataType())) {
				return createStatisticalMeasures(list, efgObject);

			} else if (EFGImportConstants.MEDIARESOURCE
					.equalsIgnoreCase(efgObject.getDataType())) {
				return createMediaResources(list, efgObject);
			} else if (EFGImportConstants.CATEGORICAL.equals(efgObject
					.getDataType())) {
				return createCategorical(list, efgObject);
			}
			 else if (EFGImportConstants.NARRATIVE.equals(efgObject
				.getDataType())) {
			return createNarrative(list, efgObject);
		}
		} catch (Exception e) {
			//log.error(e.getMessage());
		}
		return null;

	}

}