/**
 * $Id$
 *
 * Copyright (c) 2007  University of Massachusetts Boston
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

package project.efg.servlets.efgImpl;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import project.efg.exports.ZipExport;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.util.EFGImportConstants;


/**
 * This servlet handles requests to display a list of taxa with attribute values
 * matching a set of input parameters regardless of the backend.
 */
public class ExportEFG extends HttpServlet implements EFGImportConstants {
	static final long serialVersionUID = 1;
	
	protected String realPath;
	protected ZipExport zip;
	static Logger log = null;
	
	/**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.zip = new ZipExport();
		try {
			log = Logger.getLogger(project.efg.servlets.efgImpl.ExportEFG.class);
		} catch (Exception ee) {
		}
		realPath = getServletContext().getRealPath("/");
	
	}
	
	/**
	 * This method is called when the servlet is shutdown. Closes the database
	 * then disconnects the servlet's thread from its Session. It's complement
	 * is the init method.
	 */
	public void destroy() {
		super.destroy();
	}
	private String exportFiles(String[] datasources,String[] glossaries,String[] mediaresources,String[] otherResources, String context) {
		try {
		
			
		   	checkNull(mergeDatasourcesAndGlossaries(datasources, glossaries),mediaresources,otherResources);
		   	String exports_dir = EFGImportConstants.EFGProperties.getProperty("exports_directory");
		   	File serverDirectory = new File(realPath);
			File exportDirectory = new File(realPath,
					exports_dir);
				
				
				String zipFileName = this.zip.zipExports(
							serverDirectory,
							exportDirectory,
							datasources, 
							mediaresources, 
							otherResources);
				StringBuffer successMessage = new StringBuffer();
				successMessage.append("<div id=\"success\">");
				successMessage.append(context);
				successMessage.append("/");
				successMessage.append(exports_dir);
				successMessage.append("/");
				successMessage.append(zipFileName);
				successMessage.append("</div>");
				return successMessage.toString();
				

		} catch (Exception ee) {
			log.error(ee.getMessage());
			LoggerUtilsServlet.logErrors(ee);
			StringBuffer errorMessage = new StringBuffer();
			errorMessage.append("<div id=\"error\">");
			errorMessage.append(ee.getMessage());
			errorMessage.append(".Please see server logs for more information.");
			errorMessage.append("</div>");
			return errorMessage.toString();

		}
	}
	/**
	 * Handles an HTTP POST request - Based most likely on a form submission.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		res.setContentType(EFGImportConstants.TEXT_XML);
		PrintWriter out = res.getWriter();
		
		String context = req.getContextPath();
		String[] datasources =req.getParameterValues("datasources");
		String[] glossaries =req.getParameterValues("glossaries");
		
		String[] mediaresources =req.getParameterValues("mediaresources");
		String[] otherResources = req.getParameterValues("otherresources");				
		out.println(exportFiles(datasources,glossaries,mediaresources,otherResources,context));
		out.flush();
		res.flushBuffer();

	}
	/**
	 * @param datasources
	 * @param glossaries
	 * @return
	 */
	private String[] mergeDatasourcesAndGlossaries(String[] datasources, String[] glossaries) {
		if(datasources == null && glossaries == null) {
			return null;
		}
		if(datasources != null && glossaries != null  ) {
			String[] merger = new String[datasources.length + glossaries.length];
			
			System.arraycopy(datasources,0,merger,0,datasources.length);
			System.arraycopy(glossaries,0,merger,datasources.length,glossaries.length);
			return merger;
		}
		if(glossaries != null && (glossaries.length > 0)) {
			return glossaries;
		}
		if(datasources != null && (datasources.length > 0)) {
			return datasources;
		}
		return datasources;
	}

	/**
	 * @param datasources
	 * @param mediaresources
	 * @param otherResources
	 */
	private void checkNull(String[] datasources, 
			String[] mediaresources, 
			String[] otherResources)throws Exception {
		
		if((datasources == null || datasources.length == 0)&&
				(mediaresources == null || mediaresources.length == 0)&&	
				(otherResources == null || otherResources.length == 0)) {
			throw new Exception("Please select at least one resource to export");
		}
	}

	/**
	 * Handles an HTTP GET request - Based most likely on a clicked link.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		doPost(req, res);
	}



}
//$Log$
//Revision 1.1  2007/01/12 15:04:04  kasiedu
//no message
//
