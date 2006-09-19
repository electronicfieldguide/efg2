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

import project.efg.servlets.efgInterface.EFGDataObjectListInterface;
import project.efg.servlets.efgInterface.ServletAbstractFactoryInterface;
import project.efg.servlets.factory.ServletAbstractFactoryCreator;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.EFGMediaResourceSearchableObject;
import project.efg.util.FindTemplate;

/**
 * @author kasiedu
 *
 */
public abstract class XSLTObjectInterface {
	
	private ServletAbstractFactoryInterface servFactory;
	private XSLTObjectHelper xsltHelper;
	public  XSLTObjectInterface( ){
		this.servFactory = ServletAbstractFactoryCreator.getInstance();
		this.xsltHelper = new XSLTObjectHelper();
	}
	/**
	 * @param displayName
	 * @param datasourceName
	 * @param xslType = must one of plates, lists or taxonPage
	 * @return
	 */
	public XslPage getXSLFile(String datasourceName, 
			String xslType) {	
		
		FindTemplate template = new FindTemplate(datasourceName,xslType);
		return template.getXSLFileName();
	}

	public EFGDataObjectListInterface getSearchableLists(String displayName, String datasourceName){
		return servFactory.getSearchableLists(displayName,datasourceName);
	}
	public  EFGDataObjectListInterface 
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
		
		
		return this.xsltHelper.isXSLFileExists(realPath,xslFileName);
	}
	protected EFGMediaResourceSearchableObject getFirstField(String displayName, 
			String datasourceName) {
		EFGMediaResourceSearchableObject medSearchField = null;
		try{
			medSearchField = servFactory.getFirstField(displayName, datasourceName);
		}
		catch(Exception ee){
			LoggerUtilsServlet.logErrors(ee);
		}
		return medSearchField;
	}

	
}
