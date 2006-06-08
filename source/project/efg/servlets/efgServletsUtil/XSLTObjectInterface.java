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
package project.efg.servlets.efgServletsUtil;

import java.util.Map;

import org.apache.log4j.Logger;

import project.efg.servlets.efgInterface.SearchableListInterface;
import project.efg.servlets.efgInterface.ServletAbstractFactoryInterface;
import project.efg.servlets.factory.ServletAbstractFactoryCreator;

/**
 * @author kasiedu
 *
 */
public abstract class XSLTObjectInterface {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(XSLTObjectInterface.class);
		} catch (Exception ee) {
		}
	}
	private ServletAbstractFactoryInterface servFactory;
	private XSLTObjectHelper xsltHelper;
	public  XSLTObjectInterface( ){
		this.servFactory = ServletAbstractFactoryCreator.getInstance();
		this.xsltHelper = new XSLTObjectHelper();
	}
	public String getXSLFileName(String displayName, String datasourceName, String fieldName){
		
		return servFactory.getXSLFileName(displayName,datasourceName,fieldName);
	}
	public SearchableListInterface getSearchableLists(String displayName, String datasourceName){
		return servFactory.getSearchableLists(displayName,datasourceName);
	}
	public  SearchableListInterface 
	getMediaResourceLists(String displayName, String datasourceName){
		return servFactory.getMediaResourceLists(displayName,datasourceName);
	}
	public abstract XSLProperties 
	getXSLProperties(Map parameters,String realPath);
	
	/**
	 * 
	 * @param dsName
	 * @param realPath
	 * @param searchType
	 * @return
	 */
	public final boolean isXSLFileExists(
			  String realPath, 
			  String xslFileName){
		log.debug("Real Path: " + realPath);
		log.debug("FileName: " + xslFileName);
		return this.xsltHelper.isXSLFileExists(realPath,xslFileName);
	}
	protected String getFirstImageField(String displayName, String datasourceName) {
		String imageField = null;
		try{
			imageField = servFactory.getFirstMediaResourceFieldName(displayName, datasourceName);
		}
		catch(Exception ee){
			log.error(ee.getMessage());
		}
		return imageField;
	}
	protected String getFirstSearchableState(String displayName, String datasourceName) {
		String fieldName = null;
		try{
			
			fieldName= servFactory.getFirstFieldName(displayName,datasourceName);
		}
		catch(Exception ee){
			log.error(ee.getMessage());
		}
		return fieldName;
	}
	
}
