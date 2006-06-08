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
package project.efg.servlets.efgServletsUtil;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class SearchPageHtmlLists extends XSLTObjectInterface {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(SearchPageHtmlLists.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public SearchPageHtmlLists() {
		super();
		
	}
	
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.XSLTObjectInterface#getForwardString(javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	public XSLProperties getXSLProperties(
			Map parameters,
			String realPath
			) {	
		if(parameters == null){
			log.error("Map parameters is null");
		}
		String[] displayNames =(String[])parameters.get(EFGImportConstants.DISPLAY_NAME); 
		String[] datasourceNames = (String[])parameters.get(EFGImportConstants.DATASOURCE_NAME);
		String datasourceName = null;
		String displayName = null;
		
		if((datasourceNames != null) &&(datasourceNames.length > 0)){
			datasourceName = datasourceNames[0];
			
		}
		if((displayNames != null) &&(displayNames.length > 0)){
			displayName = displayNames[0];
		}
		String xslFileName = null;
		boolean isDefault = false;
		try {
			String[] xslFileNames = 
				(String[])parameters.get(EFGImportConstants.XSL_STRING);
			//xslFileName = (String)parameters.get(EFGImportConstants.XSL_STRING);
			//check to see that it exists
			if ((xslFileNames == null) || (xslFileNames[0].trim().equals(""))) {
				xslFileName = this.getXSLFileName(
						displayName,
						datasourceName, 
						EFGImportConstants.SEARCHPAGE_LISTS_XSL);
				if ((xslFileName == null) || (xslFileName.trim().equals(""))) {
				
					log.debug("xslFileNames is null.using default!!");
					xslFileName = "defaultSearchFile.xsl";
					isDefault = true;
				}
			}
			else{
				xslFileName = xslFileNames[0];
				if(!this.isXSLFileExists(realPath,xslFileName)){
					xslFileName = "defaultSearchFile.xsl";
					isDefault = true;
				}
			}
		}
		catch (Exception ee) {
			xslFileName = "defaultSearchFile.xsl";
			isDefault = true;
		}
		Properties properties = new Properties();
		properties.setProperty(EFGImportConstants.SEARCH_PAGE_STR,
				EFGImportConstants.SEARCHPAGE_LISTS_FILLER);
		if(isDefault){
			String fieldName =this.getFirstSearchableState(displayName,datasourceName);
			if (fieldName != null) {
				properties.setProperty("fieldName", fieldName);
			}
		}
		
		try {
			XSLProperties xslProps = new XSLProperties();
			xslProps.setXSLFileName(xslFileName);
			xslProps.setXSLParameters(properties);
			return xslProps;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			LoggerUtilsServlet.logErrors(e);
		}
		return null;
	}
	
}
