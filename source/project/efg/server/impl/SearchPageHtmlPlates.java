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
package project.efg.server.impl;

import java.util.Map;
import java.util.Properties;

import project.efg.server.utils.EFGMediaResourceSearchableObject;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.server.utils.XSLProperties;
import project.efg.server.utils.XSLTObjectInterface;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.interfaces.EFGImportConstants;

public class SearchPageHtmlPlates extends XSLTObjectInterface
{

    public SearchPageHtmlPlates()
    {
    	super();
    }
	/*
	 * (non-Javadoc)
	 * 
	 * @see project.efg.servlets.efgInterface.XSLTObjectInterface#getForwardString(javax.servlet.http.HttpServletRequest,
	 *      java.lang.String)
	 */
	public XSLProperties getXSLProperties(Map parameters) {
		if (parameters == null) {
			//log.error("Map parameters is null");
		}
		String guid = null;
		String[] displayNames = (String[]) parameters
		.get(EFGImportConstants.DISPLAY_NAME);
		
		String[] datasourceNames = (String[]) parameters
		.get(EFGImportConstants.DATASOURCE_NAME);
		
		String datasourceName = null;
		String displayName = null;
		if ((datasourceNames != null) && (datasourceNames.length > 0)) {
			datasourceName = datasourceNames[0];
		}
		
		if ((displayNames != null) && 
				(displayNames.length > 0)) {	
			displayName = displayNames[0];
		}
		
		String xslFileName = null;
		
		try {
			String[] xslFileNames = 
				(String[])parameters.get(EFGImportConstants.XSL_STRING);
			
			if ((xslFileNames == null) || (xslFileNames[0].trim().equals(""))) {
				XslPage currentPage = this.getXSLFile(
						datasourceName, 
						EFGImportConstants.SEARCHPAGE_PLATES_XSL);
				if(currentPage != null){
					xslFileName = currentPage.getFileName();
					guid = currentPage.getGuid();
					if ((xslFileName == null) || (xslFileName.trim().equals(""))) {
						throw new Exception("Cannot find xslFile..Use defaults..");
					}
				}
				
				if ((xslFileName == null) || (xslFileName.trim().equals(""))) {
				 throw new Exception("Cannot find xslFile..Use defaults..");
				}
			}
			else{
				xslFileName = xslFileNames[0];
			}
			
		}
		catch (Exception ee) {
			xslFileName = EFGImportConstants.DEFAULT_SEARCH_FILE;//"defaultSearchFile.xsl";
			
		}
		Properties properties = new Properties();
		properties.setProperty(EFGImportConstants.SEARCH_PAGE_STR,
				EFGImportConstants.SEARCHPAGE_PLATES_FILLER);
		if(guid != null){
			properties.setProperty(EFGImportConstants.GUID, guid);
		}
		EFGMediaResourceSearchableObject firstField = this.getFirstField(displayName,datasourceName);
			if(firstField != null){
				try {
					//log.debug("using default stylesheet");
					//log.debug("Display Name: " + displayName);
					//log.debug("DatasourceName: " + datasourceName);
					String imageField = firstField.getMediaResourceField();
					//log.debug("MediaResourceField: " + imageField);
					if ((imageField != null) && (!imageField.trim().equals(""))){
						properties
						.setProperty("mediaResourceField", imageField);
					}
					String fieldName = firstField.getSearchableField();
					
					if ((fieldName != null) && (!fieldName.trim().equals(""))){
						properties.setProperty("fieldName", fieldName);
					}
					
				} catch (Exception eee) {
					LoggerUtilsServlet.logErrors(eee);
				}
			}
		// forward the xml doc ApplyXSL servlet
		try {
			if ((xslFileName == null) || (xslFileName.trim().equals(""))) {
				
				throw new Exception("Could not find an XSL File");
			}
			
			XSLProperties xslProps = new XSLProperties();
			
			xslProps.setXSLFileName(xslFileName);
			xslProps.setXSLParameters(properties);
			
			return xslProps;
		} catch (Exception e) {
			
			//log.error(e.getMessage());
			//e.printStackTrace();
			LoggerUtilsServlet.logErrors(e);
		}
		
		return null;
	}
 
}
