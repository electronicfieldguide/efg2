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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.efgDocument.EFGDocument;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
/**
 * @author kasiedu
 *
 */
public class ApplyXSLInterface {
	
	public  static ApplyXSLInterface createApplyXSLInterface() {
		return new ApplyXSLInterface();
	}
	private ApplyXSLInterface(){
		
	}
	/**
	 * Get the xml document as a String or a jdom Document from the
	 * HttpServletRequest object and transform it to a jdom Source.
	 * 
	 * @param req
	 *            the servlet request object
	 * @return the jdom Source transformed from the xml document
	 */
	public Source getXMLSource(HttpServletRequest req){
		String filename = req.getParameter("xml");
		
		StringWriter writer = new StringWriter();
	
		if (filename == null) {
			
			EFGDocument	srcDoc = (EFGDocument) req.getAttribute("xml");
			if (srcDoc == null) {
				EFGUtils.log("No xml source specified. Transformation failed");
				return null;
			}

			
			try {
				//Properties props = LocalConfiguration.getInstance().getProperties();					
				//props.setProperty(Configuration.Property.Indent, "true");

				srcDoc.marshal(writer);
				Reader reader = new StringReader(writer.getBuffer().toString());			
				return new StreamSource(reader);
			} catch (Exception je) {
				//EFGUtils.log("error on jdom to w3c dom conversion on xml source");
				LoggerUtilsServlet.logErrors(je);
			}
			return null;
		}
		InputStream is = null;
		try {
			is = (new URL(filename)).openStream();
		} catch (MalformedURLException mue) {
			EFGUtils.log("malformed url for xml source");
			LoggerUtilsServlet.logErrors(mue);
			return null;
		} catch (IOException ioe) {
			EFGUtils.log("error while trying to access xml via url");
			LoggerUtilsServlet.logErrors(ioe);
			return null;
		}
		return new javax.xml.transform.stream.StreamSource(is);
	}

	/**
	 * Get the xsl filename as a String from the HttpServletRequest object and
	 * transform it to a jdom Source.
	 * 
	 * @param req
	 *            the servlet request object
	 * @return the jdom Source transformed from the xml document
	 */
	public Source getXSLSource(HttpServletRequest req){
		String filename = req.getParameter("xsl");
		String xslURL = null;
		if (filename == null){
			return buildXSL(req);
		}
		if (filename.indexOf(":") == -1) { 
			xslURL = req.getScheme() + "://" + req.getServerName() + ":"
					+ req.getServerPort() + filename;
		
		} else { 
			xslURL = filename;
		}
		return new javax.xml.transform.stream.StreamSource(xslURL);
	}
	/**
	 * Build the xsl file from the parameters in HttpServletRequest object and
	 * transform it to a jdom Source.
	 * 
	 * @param req
	 *            the servlet request object
	 * @return the jdom Source transformed from the xml document
	 */
	private Source buildXSL(HttpServletRequest req) {
		StringBuffer xslBuffer = new StringBuffer();
		xslBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
		xslBuffer.append("<xsl:stylesheet version='1.0' ");
		xslBuffer.append("xmlns:xsl='http://www.w3.org/1999/XSL/Transform' >");
		xslBuffer.append("<xsl:output method='html' encoding='utf-8' />");
		xslBuffer.append("<xsl:param name='resource-path'>");
		xslBuffer.append(req.getParameter("resource-path"));
		xslBuffer.append("</xsl:param>");
		xslBuffer.append("<xsl:param name='image-base'>");
		xslBuffer.append(req.getParameter("image-base"));
		xslBuffer.append("</xsl:param>");
		xslBuffer.append("<xsl:param name='images-per-row'>");
		xslBuffer.append(req.getParameter("images-per-row"));
		xslBuffer.append("</xsl:param>");
		xslBuffer.append("<xsl:include href='");
		xslBuffer.append(req.getParameter("image-lib-url"));
		xslBuffer.append("' />");
		xslBuffer.append("<xsl:include href='");
		xslBuffer.append(req.getParameter("core-lib-url"));
		xslBuffer.append("' />");
		xslBuffer.append("</xsl:stylesheet>");

		return new javax.xml.transform.stream.StreamSource(new StringReader(
				xslBuffer.toString()));
	}

}