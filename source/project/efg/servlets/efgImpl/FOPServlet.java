/**
 *$Id$
 * Copyright (c) 2006  University of Massachusetts Boston
 *
 * Author: Jacob K Asiedu
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.fop.apps.Driver;
import org.apache.fop.messaging.MessageHandler;

import project.efg.servlets.efgServletsUtil.ApplyXSLInterface;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.util.EFGImportConstants;

// GenerateFO, StylesheetCache

public class FOPServlet extends HttpServlet {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	private static Driver driver;

	private static TransformerFactory tFactory;
	private static String realPath;

	/**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		realPath = getServletContext().getRealPath("/");
		driver = new Driver();
		tFactory = TransformerFactory.newInstance();
	}

	private String getPDFFile(String dsName, int taxonSize){
	
		StringBuffer pdfFileBuffer = new StringBuffer();
		pdfFileBuffer.append(realPath);
		pdfFileBuffer
				.append(EFGImportConstants.TEMPLATES_FOLDER_NAME);
		
		pdfFileBuffer.append(File.separator);
		pdfFileBuffer.append(dsName);
		pdfFileBuffer.append("_");
		if (taxonSize == 1) {
			pdfFileBuffer.append("taxonPage");
		} else {
			pdfFileBuffer.append("searchPage");
		}
		pdfFileBuffer.append(System.currentTimeMillis());
		pdfFileBuffer.append(".pdf");
		return pdfFileBuffer.toString();
	}
	private void doTransformation(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		int taxonSize = 1;
		String dsName =(String)request.getAttribute(EFGImportConstants.DATASOURCE_NAME);
		String taxonSizeStr = 
			(String)request.getAttribute(EFGImportConstants.TAXONSIZE_STR);
		if((taxonSizeStr != null) && (!taxonSizeStr.trim().equals(("")))){
			taxonSize = Integer.parseInt(taxonSizeStr);
		}
		String pdfFileStr = this.getPDFFile(dsName,taxonSize);
		String serverContext = request.getContextPath();
		StringBuffer serverBuffer = new StringBuffer(request.getScheme());
		serverBuffer.append("://");
		serverBuffer.append(request.getServerName());
		serverBuffer.append(":");
		serverBuffer.append(request.getServerPort());
		serverBuffer.append(serverContext);
		synchronized (this) {
			File pdfFile = new File(pdfFileStr);
			driver.reset();
			OutputStream out = new FileOutputStream(pdfFile);
			driver.setOutputStream(out);
			Logger logger = new ConsoleLogger(2);
			MessageHandler.setScreenLogger(logger);
			driver.setLogger(logger);
			driver.setRenderer(1);
		
			ApplyXSLInterface applyXSL = ApplyXSLInterface.createApplyXSLInterface();
		
			javax.xml.transform.Source xsl = applyXSL.getXSLSource(request);
			javax.xml.transform.Source xml =applyXSL.getXMLSource(request);
			Transformer transformer = tFactory.newTransformer(xsl);
			transformer.setParameter("serverbase", serverBuffer.toString());
			javax.xml.transform.Result res = new SAXResult(driver
				.getContentHandler());
			transformer.transform(xml, res);
			driver.getResults();
			out.close();
			redirect(response, pdfFile.getName(), serverBuffer.toString());
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
			try {
				this.doTransformation(request,response);
			} catch (Exception ioe) {
				
				LoggerUtilsServlet.logErrors(ioe);
			}
	}

	private void redirect(HttpServletResponse response, String pdfFile,
			String server) throws Exception {

		StringBuffer buf = new StringBuffer();
		buf.append(server);
		buf.append("/templateConfigFiles/");
		buf.append(pdfFile);
		String encodedURL = response.encodeRedirectURL(buf.toString());
		response.sendRedirect(encodedURL);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}

}
