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

import org.apache.log4j.Logger;

import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;

import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslFileNamesType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;
import project.efg.util.EFGImportConstants;

/**
 * This servlet receives input from author about configuration of a Taxon page
 * for a datasource and creates a TaxonPageTemplate for that datasource.
 */
public class TaxonPageDefaultConfig {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger log = null;

	static {
		try {
			log = Logger.getLogger(TaxonPageDefaultConfig.class);
		} catch (Exception ee) {
		}
	}

	// private String datasourceName;
	// private String displayName;

	private String realPath;

	private TaxonPageTemplates tps;

	/**
	 * 
	 */
	public TaxonPageDefaultConfig(String realPath) {
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

		log.debug("DatasourceName: " + dsName);

		boolean isDone = false;
		try {
			isDone = createFile(dsName, displayName);
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
		return isDone;
	}

	public boolean cloneOldFile(String oldDsName, String newDSName,
			String displayName) {

		log.debug("DatasourceName: " + newDSName);
		log.debug("oldDatasourcename: " + oldDsName);

		boolean isDone = false;
		try {
			isDone = cloneFile(oldDsName, newDSName, displayName);
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
		return isDone;
	}

	private boolean cloneFile(String oldDSName, String newDSName,
			String displayName) {
		String mute = "";
		synchronized (mute) {
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
				log.debug("NewDSName: " + newDSName);
				return this.writeToFile(newDSName.toLowerCase());
			} catch (Exception ee) {
				log.error(ee.getMessage());
			}
			return false;
		}

	}

	private boolean writeToFile(String datasourceName) {
		log.debug("Real Path: " + this.realPath);
		StringBuffer fileLocationBuffer = new StringBuffer();
		fileLocationBuffer.append(this.realPath);

		fileLocationBuffer.append(datasourceName.toLowerCase());
		fileLocationBuffer.append(EFGImportConstants.XML_EXT);

		String fileLocation = fileLocationBuffer.toString();
		log.debug("xml template file: " + fileLocation);
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
					log.debug(eeer.getMessage());
				}
			}
			if (!success) {
				isExists = false;
				log.debug("File could not be renamed");
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
					log.error(eee.getMessage());
				}
			}
		} catch (Exception ee) {
			done = false;
			log.error(ee.getMessage());
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
						log
								.debug("File could not be renamed when exception occured");
					}
				}
			} catch (Exception ff) {
				log.error(ff.getMessage());
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
	private boolean createFile(String dsName, String displayName) {
		this.tps = new TaxonPageTemplates();
		TaxonPageTemplateType tptype = createEFGTemplate(dsName, displayName);

		this.tps.addTaxonPageTemplate(tptype);
		return this.writeToFile(dsName.toLowerCase());

	}

	private TaxonPageTemplateType createEFGTemplate(String dsName,
			String displayName) {
		// if it already exists add stuff to it..
		TaxonPageTemplateType tp = new TaxonPageTemplateType();
		tp.setDatasourceName(dsName.toLowerCase());
		
		XslFileNamesType xsls = new XslFileNamesType();

		XslPageType xslPageType = getXSLPageType(EFGImportConstants.DEFAULT_TAXON_PAGE_FILE);
		xsls.setXslTaxonPages(xslPageType);

		xslPageType = getXSLPageType(EFGImportConstants.DEFAULT_SEARCH_FILE);
		xsls.setXslPlatePages(xslPageType);

		xslPageType = getXSLPageType(EFGImportConstants.DEFAULT_SEARCH_FILE);
		xsls.setXslListPages(xslPageType);

		tp.setXSLFileNames(xsls);

		return tp;
	}

	private XslPageType getXSLPageType(String xslFile) {
		XslPageType xslPageType = new XslPageType();
		XslPage xslPage = new XslPage();
		xslPage.setIsDefault(true);
		xslPage.setFileName(xslFile);
		xslPageType.addXslPage(xslPage);
		return xslPageType;
	}

}
// $Log$
// Revision 1.1.2.1  2006/07/11 21:46:12  kasiedu
// "Added more configuration info"
//

