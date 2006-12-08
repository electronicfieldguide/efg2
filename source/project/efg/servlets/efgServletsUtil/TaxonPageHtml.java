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

import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.EFGImportConstants;
import project.efg.util.EFGMediaResourceSearchableObject;

/**
 * @author kasiedu
 *
 */
public class TaxonPageHtml extends XSLTObjectInterface {
	

	/**
	 * 
	 */
	public TaxonPageHtml() {
		super();

	}

	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.XSLTObjectInterface#getForwardString(javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	public XSLProperties getXSLProperties(Map parameters, String realPath) {
		String []xslFileNames = null;
		String xslFileName = null;
		String guid = null;
		String datasourceName = null;
		String displayName = null;
		Properties properties = new Properties();
		
		try { 
			
			
			String[] displayNames =(String[])parameters.get(EFGImportConstants.DISPLAY_NAME); 
			String[] datasourceNames = (String[])parameters.get(EFGImportConstants.DATASOURCE_NAME);
			
			
			if((datasourceNames != null) &&(datasourceNames.length > 0)){
				datasourceName = datasourceNames[0];	
			}
			if((displayNames != null) &&(displayNames.length > 0)){
				displayName = displayNames[0];
			}
			try {
				xslFileNames = 
					(String[])parameters.get(EFGImportConstants.XSL_STRING);
				
				if ((xslFileNames == null) || (xslFileNames[0].trim().equals(""))) {
					XslPage currentPage = this.getXSLFile(
							datasourceName, 
							EFGImportConstants.TAXONPAGE_XSL);
					if(currentPage != null){
						xslFileName = currentPage.getFileName();
						guid = currentPage.getGuid();
						if ((xslFileName == null) || (xslFileName.trim().equals(""))) {
							throw new Exception("Cannot find xslFile..Use defaults..");
						}
					}
				}
				else{
					xslFileName = xslFileNames[0];
				}
					
					if(!this.isXSLFileExists(realPath,xslFileName)){
						//log.debug("Xsl file does not exists using defaults");
						 throw new Exception("Cannot find xslFile..Use defaults..");
					}
				
			}
			catch (Exception ee) {
				xslFileName = EFGImportConstants.DEFAULT_TAXON_PAGE_FILE;//"defaultTaxonPageFile.xsl";
				
			}
			
		}
		catch (Exception ee) {
			xslFileName = "defaultTaxonPageFile.xsl";
			LoggerUtilsServlet.logErrors(ee);
		
		}
		try {
			
				if(guid != null){
					properties.setProperty(EFGImportConstants.GUID, guid);
				}
				EFGMediaResourceSearchableObject medSearch = 
					this.getFirstField(displayName,datasourceName);
				if(medSearch != null){
					String fieldName =medSearch.getSearchableField();
					if ((fieldName != null) && (!fieldName.trim().equals(""))){
						properties.setProperty("fieldName", fieldName);
					}
				}
			
			XSLProperties xslProps = new XSLProperties();
			xslProps.setXSLFileName(xslFileName);
			xslProps.setXSLParameters(properties);
			return xslProps;
		} catch (Exception e) {
			
			LoggerUtilsServlet.logErrors(e);
		}
		return null;
	}

}
