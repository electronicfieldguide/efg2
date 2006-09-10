/**
 * $Id$
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

package project.efg.Imports.efgImportsUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslFileNamesType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;
import project.efg.util.EFGDisplayObject;
import project.efg.util.EFGImportConstants;
import project.efg.util.EFGUniqueID;
import project.efg.util.TemplateMapObjectHandler;
import project.efg.util.TemplateObject;

/**
 * This servlet receives input from author about configuration of a Taxon page
 * for a datasource and creates a TaxonPageTemplate for that datasource.
 */
public class TaxonPageDefaultConfig {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String realPath;
	private TaxonPageTemplates tps;

	private DBObject dbObject;

	/**
	 * 
	 */
	public TaxonPageDefaultConfig(DBObject dbObject, String realPath) {
		this.dbObject= dbObject;
		this.realPath = realPath;
	}

	/**
	 * Handles an HTTP POST request - Based most likely on a form submission.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	public boolean processNew(String dsName, String displayName) {

		boolean isDone = false;
		try {
			isDone = initFile(dsName);
			if(isDone){
				this.writeToMapObject(dsName,displayName);
			}
			
		} catch (Exception ee) {
			LoggerUtils.logErrors(ee);
		}
		return isDone;
	}
	public boolean cloneOldFile(
			String oldDsName, 
			String newDSName,
			String displayName
			) {
		
		boolean isDone = false;
		try {
			isDone = cloneFile(oldDsName, newDSName, displayName);
			if(isDone){
				this.writeToMapObject(newDSName,displayName);
			}
		} catch (Exception ee) {
			LoggerUtils.logErrors(ee);
		}
		return isDone;
	}

	private void add2TemplateObject(String datafn, 
			String displayName,
			String type, 
			String uniqueName){
		
		String xslName = EFGImportConstants.DEFAULT_SEARCH_FILE;
		
		StringBuffer querySearch = new StringBuffer("/");
		querySearch.append(EFGImportConstants.EFG_APPS);
		querySearch.append("/search?dataSourceName=");
		querySearch.append(datafn);
		querySearch.append("&");
		querySearch.append(EFGImportConstants.XSL_STRING);
		querySearch.append("=");
		querySearch.append(xslName);
		querySearch.append("&");
		querySearch.append(EFGImportConstants.SEARCH_TYPE_STR);
		querySearch.append("=");
		querySearch.append(type);
		querySearch.append("&");
		querySearch.append(EFGImportConstants.DISPLAY_FORMAT);
		querySearch.append("=");
		querySearch.append(EFGImportConstants.HTML);
		querySearch.append("&");
		querySearch.append(EFGImportConstants.MAX_DISPLAY);
		querySearch.append("=");
		querySearch.append(EFGImportConstants.NUMBER_OF_TAXON_ON_PAGE+"");
		String key = querySearch.toString();
		
		TemplateObject templateObject = new TemplateObject();
		EFGDisplayObject displayObject = new EFGDisplayObject();
		displayObject.setDisplayName(displayName);
		displayObject.setDatasourceName(datafn);
		
		templateObject.setTemplateName(uniqueName);
		templateObject.setDisplayObject(displayObject);
		TemplateMapObjectHandler.add2TemplateMap(key,templateObject,this.dbObject);
	}
	
	private void writeToMapObject(String datafn,String displayName){
	
		try {
		
		
			String uniqueName = EFGImportConstants.DEFAULT_PLATES_DISPLAY;
			
			String plates = EFGImportConstants.SEARCH_PLATES_TYPE;
			String lists = EFGImportConstants.SEARCH_LISTS_TYPE;
			
			this.add2TemplateObject(datafn, 
					displayName,
					plates, 
					uniqueName);
			
			uniqueName = EFGImportConstants.DEFAULT_LISTS_DISPLAY;
			this.add2TemplateObject(datafn, 
					displayName,
					lists, 
					uniqueName);
			//create the file here
		} catch (Exception ee) {
			return;
		}
	}
	private boolean cloneFile(String oldDSName, String newDSName,
			String displayName) {
		String mute = "";
		synchronized (mute) {
			
			if(this.tps == null){
				StringBuffer fileLocationBuffer = new StringBuffer();
				fileLocationBuffer.append(realPath);
				fileLocationBuffer.append(oldDSName.toLowerCase());
				fileLocationBuffer.append(EFGImportConstants.XML_EXT);

				try {
					FileReader reader = new FileReader(fileLocationBuffer
							.toString());
					this.tps = (TaxonPageTemplates) TaxonPageTemplates
							.unmarshalTaxonPageTemplates(reader);

					TaxonPageTemplateType tp = this.tps.getTaxonPageTemplate(0);
					tp.setDatasourceName(newDSName.toLowerCase());
				
				} catch (Exception ee) {
					LoggerUtils.logErrors(ee);
				}
			}
			if(this.tps != null){
				if(this.writeToFile(newDSName.toLowerCase())){
					reloadTps(newDSName);
				}
			}
			if(this.tps != null){
				return true;
			}
			return false;
		}

	}
	/**
	 * @param newDSName
	 */
	private void reloadTps(String newDSName) {
	try {
			StringBuffer fileLocationBuffer = new StringBuffer();
			fileLocationBuffer.append(realPath);
			fileLocationBuffer.append(newDSName.toLowerCase());
			fileLocationBuffer.append(EFGImportConstants.XML_EXT);
			
			FileReader reader = new FileReader(fileLocationBuffer
					.toString());
			this.tps = (TaxonPageTemplates) TaxonPageTemplates
					.unmarshalTaxonPageTemplates(reader);
	}  catch (Exception e) {
		}	
	}

	
	private boolean initFile(String fileName){
		boolean isDone = false;
		if(fileName == null){
			return isDone;
		}
		String key = fileName.toLowerCase();
		return this.createFile(key);
		
	}
	private boolean writeToFile(String datasourceName) {
		//log.debug("Real Path: " + this.realPath);
		StringBuffer fileLocationBuffer = new StringBuffer();
		fileLocationBuffer.append(this.realPath);

		fileLocationBuffer.append(datasourceName.toLowerCase());
		fileLocationBuffer.append(EFGImportConstants.XML_EXT);

		String fileLocation = fileLocationBuffer.toString();
		//log.debug("xml template file: " + fileLocation);
		// lock file to block this operation from happening
		File file = new File(fileLocation);
		String renamedFile = fileLocation + System.currentTimeMillis() + "_old";
		File file2 = new File(renamedFile);
		boolean isExists = false;
		boolean success = false;
		boolean done = true;
		if (file.exists()) {
			isExists = true;
			// Rename file (or directory)
			String mutex = "";
			synchronized (mutex) {
				try {
					success = file.renameTo(file2);
				} catch (Exception eeer) {
					//log.debug(eeer.getMessage());
				}
			}
			if (!success) {
				isExists = false;
				//log.debug("File could not be renamed");
			}
		}
		FileWriter writer = null;
		try {
			String mutex = "";
			synchronized (mutex) {
				try {
					writer = new FileWriter(fileLocation);
					org.exolab.castor.xml.Marshaller marshaller = new org.exolab.castor.xml.Marshaller(
							writer);
					marshaller.setNoNamespaceSchemaLocation(EFGImportConstants.TEMPLATE_SCHEMA_NAME);

					marshaller.setNamespaceMapping("xsi",
							org.exolab.castor.xml.Marshaller.XSI_NAMESPACE);
					// suppress the printing of xsi:type
					marshaller.setMarshalExtendedType(false);
					marshaller.setSuppressXSIType(true);
					marshaller.marshal(tps);
					writer.flush();
					writer.close();

				} catch (Exception eee) {
					done = false;
					LoggerUtils.logErrors(eee);
				}
			}
		} catch (Exception ee) {
			done = false;
			LoggerUtils.logErrors(ee);
			try {
				// rename file to a new one
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				if (isExists) {
					String mutex = "";
					synchronized (mutex) {
						success = file2.renameTo(file);
					}
					if (!success) {
						isExists = false;
						//log
							//	.debug("File could not be renamed when exception occured");
					}
				}
			} catch (Exception ff) {
				LoggerUtils.logErrors(ff);
			}
		}

		return done;
	}

	
	/**
	 * Get the HttpServletRequest parameters and return the value.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param name
	 *            the name of the parameter of the HttpServletRequest
	 * @return the value of the parameter to be used as a test query
	 */
	private boolean createFile(String dsName) {
		
		return createEFGTemplate(dsName);
	}

	private boolean createEFGTemplate(String dsName) {
		this.tps = new TaxonPageTemplates();
		// if it already exists add stuff to it..
		TaxonPageTemplateType tp = new TaxonPageTemplateType();
		tp.setDatasourceName(dsName.toLowerCase());
		
		XslFileNamesType xsls = new XslFileNamesType();

		XslPageType xslPageType = getXSLPageType(EFGImportConstants.DEFAULT_TAXON_PAGE_FILE);
		xsls.setXslTaxonPages(xslPageType);
		//write to templateObject

		xslPageType = getXSLPageType(EFGImportConstants.DEFAULT_SEARCH_FILE);
		xsls.setXslPlatePages(xslPageType);

		xslPageType = getXSLPageType(EFGImportConstants.DEFAULT_SEARCH_FILE);
		xsls.setXslListPages(xslPageType);

		tp.setXSLFileNames(xsls);
		this.tps.addTaxonPageTemplate(tp);
		return this.writeToFile(dsName.toLowerCase());
	}

	private XslPageType getXSLPageType(String xslFile) {
		XslPageType xslPageType = new XslPageType();
		XslPage xslPage = new XslPage();
		xslPage.setIsDefault(true);
		xslPage.setFileName(xslFile);
		xslPage.setDisplayName(xslFile);
		xslPage.setGuid(EFGUniqueID.getID()+"");
		//generate a guid
	
		xslPageType.addXslPage(xslPage);
		return xslPageType;
	}

}
// $Log$
// Revision 1.1.2.6  2006/09/10 12:02:28  kasiedu
// no message
//
// Revision 1.1.2.5  2006/08/26 22:12:24  kasiedu
// Updates to xsl files
//
// Revision 1.1.2.4  2006/08/21 19:26:37  kasiedu
// no message
//
// Revision 1.1.2.3  2006/08/13 23:53:16  kasiedu
// *** empty log message ***
//
// Revision 1.1.2.2  2006/08/09 18:55:24  kasiedu
// latest code confimrs to what exists on Panda
//
// Revision 1.1.2.1  2006/07/11 21:46:12  kasiedu
// "Added more configuration info"
//

