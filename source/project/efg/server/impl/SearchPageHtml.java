/**
 * $Id: SearchPageHtml.java,v 1.1.1.1 2007/08/01 19:11:21 kasiedu Exp $
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

import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.server.utils.XSLProperties;
import project.efg.server.utils.XSLTObjectInterface;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.interfaces.EFGImportConstants;


public class SearchPageHtml extends XSLTObjectInterface
{

    public SearchPageHtml()
    {
    	super();
    }
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.XSLTObjectInterface#getForwardString(javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	public XSLProperties getXSLProperties(
			Map parameters
			) {	
  		String[] displayNames =
			(String[])parameters.get(EFGImportConstants.DISPLAY_NAME); 
		//log.debug("Got display names1");
		String[] datasourceNames = 
			(String[])parameters.get(EFGImportConstants.DATASOURCE_NAME);
		//log.debug("Got datasource names1");
		String datasourceName = null;
		String displayName = null;
		String guid = null;
		if((datasourceNames != null) &&(datasourceNames.length > 0)){
			datasourceName = datasourceNames[0];
		}
		if((displayNames != null) &&(displayNames.length > 0)){
			displayName = displayNames[0];
		}
		String xslFileName = null;
		
		
		try {
			String[] xslFileNames = 
				(String[])parameters.get(EFGImportConstants.XSL_STRING);
			if ((xslFileNames == null) || 
					(xslFileNames[0].trim().equals(""))) {
				XslPage currentPage = this.getXSLFile(
						datasourceName, 
						EFGImportConstants.SEARCHPAGE_XSL);
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
			xslFileName = EFGImportConstants.DEFAULT_SEARCH_PAGE_FILE;
		}
  
		Properties properties = new Properties();
		properties.setProperty(EFGImportConstants.SEARCH_PAGE_STR,
				EFGImportConstants.SEARCHPAGE_FILLER);

		if(guid != null){
			properties.setProperty(EFGImportConstants.GUID, guid);
		}

		try {
			XSLProperties xslProps = new XSLProperties();
			xslProps.setXSLFileName(xslFileName);
			xslProps.setXSLParameters(properties);
			return xslProps;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtilsServlet.logErrors(e);
		}
		return null;
	}
}
