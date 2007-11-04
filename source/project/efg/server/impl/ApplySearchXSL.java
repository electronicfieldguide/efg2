/**
 * $Id$
 * $Name:  $
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
package project.efg.server.impl;

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


import project.efg.server.interfaces.ApplyXSLInterface;
import project.efg.server.utils.EFGSearchableTemplate;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.EFGUtils;
/**
 * @author kasiedu
 *
 */
public class ApplySearchXSL implements ApplyXSLInterface {

    public ApplySearchXSL()
    {
    }
    /**
     * TODO make Object an Interface type
     * 
     * @param obj - 
     * @return
     */
	public Source getXMLSource(Object obj){
		if (obj == null) {
			EFGUtils.log("No xml source specified. Transformation failed");
			return null;
		}

		EFGSearchableTemplate srcDoc = (EFGSearchableTemplate)obj;
		try {
			
			StringWriter writer = new StringWriter();
			srcDoc.marshal(writer);
			StringBuffer buffer =writer.getBuffer();
			Reader reader = new StringReader(buffer.toString());			
			return new StreamSource(reader);
		}
		catch(Exception ee){
			LoggerUtilsServlet.logErrors(ee);
		}
		return null;

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
			if(req.getAttribute(EFGImportConstants.XML) != null){
		
				Object obj = req.getAttribute(EFGImportConstants.XML);
			}
			EFGSearchableTemplate	srcDoc = (EFGSearchableTemplate)req.getAttribute(EFGImportConstants.XML);
			if (srcDoc == null) {
				EFGUtils.log("No xml source specified. Transformation failed");
				return null;
			}

			
			try {
				return getXMLSource(srcDoc);
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
	 * Get the xsl Source 
	 * @param req
	 *            the servlet request object
	 * @return the  Source objectto be used to  
	 * transform the xml document
	 */
	public Source getXSLSource(HttpServletRequest req){
		String filename = req.getParameter("xsl");
		String xslURL = null;
		if (filename == null){
			//return default
			filename = EFGImportConstants.DEFAULT_SEARCH_PAGE_FILE;
		}
		if (filename.indexOf(":") == -1) { 
			xslURL = req.getScheme() + "://" + req.getServerName() + ":"
					+ req.getServerPort() + filename;
		
		} else { 
			xslURL = filename;
		}
		return new javax.xml.transform.stream.StreamSource(xslURL);
	}


}
