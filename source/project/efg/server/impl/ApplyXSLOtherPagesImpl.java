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
package project.efg.server.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import project.efg.efgDocument.EFGDocument;
import project.efg.efgDocument.TaxonEntries;
import project.efg.efgDocument.TaxonEntryType;
import project.efg.server.factory.EFGSpringFactory;
import project.efg.server.interfaces.ApplyXSLInterface;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGSessionBeanInterface;
import project.efg.util.utils.EFGDocTemplate;
import project.efg.util.utils.EFGUtils;


public class ApplyXSLOtherPagesImpl
    implements ApplyXSLInterface
{

    public ApplyXSLOtherPagesImpl()
    {
    }
	/* (non-Javadoc)
	 * @see project.efg.server.utils.ApplyXSLAbstract#getXMLSource(project.efg.server.utils.EFGDocTemplate)
	 */
	public Source getXMLSource(Object obj){
		if (obj == null) {
			EFGUtils.log("No xml source specified. Transformation failed");
			return null;
		}

		
		EFGDocTemplate srcDoc = (EFGDocTemplate)obj;
		try {
			StringWriter writer = new StringWriter();
			srcDoc.marshal(writer);
			StringBuilder buffer =new StringBuilder(writer.getBuffer().toString());
			Reader reader = new StringReader(buffer.toString());			
			return new StreamSource(reader);
		}
		catch(Exception ee){
			LoggerUtilsServlet.logErrors(ee);
		}
		return null;

	}

	/* (non-Javadoc)
	 * @see project.efg.server.utils.ApplyXSLAbstract#getXMLSource(javax.servlet.http.HttpServletRequest)
	 */
	public Source getXMLSource(HttpServletRequest req){
		String filename = req.getParameter("xml");
		
		
	
		if (filename == null) {
			
			Object obj = req.getAttribute("xml");
			
			EFGDocTemplate	srcDoc = (EFGDocTemplate) req.getAttribute("xml");
			
			if (srcDoc == null) {
				EFGUtils.log("No xml source specified. Transformation failed");
				return null;
			}

			
			try {
				if(req.getParameter(EFGImportConstants.SHOW_ALL) == null){
					int defaultOnEachPage = 					
						Integer.parseInt(EFGImportConstants.EFGProperties.getProperty("numberoneachPage", "100"));
					int numberOnEachPage = -1;
					if(req.getParameter(EFGImportConstants.MAX_DISPLAY)!= null ){
						try{
						numberOnEachPage =
							Integer.parseInt(req.getParameter(EFGImportConstants.MAX_DISPLAY));
						}
						catch(Exception ee){
							numberOnEachPage = -1;
						}
					}
					if(numberOnEachPage <= 1){
						numberOnEachPage = defaultOnEachPage;
					}
					if(srcDoc.getEFGDocument().getTaxonEntries().getTaxonEntryCount() >numberOnEachPage){
						save2Session(req,srcDoc,numberOnEachPage);
					}
				}
				return getXMLSource(srcDoc);
			} catch (Exception je) {
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
	private void save2Session(HttpServletRequest req, EFGDocTemplate srcDoc,int numberOnEachPage){
		EFGDocument doc1 = new EFGDocument();
		TaxonEntries entries = new TaxonEntries();
		doc1.setTaxonEntries(entries);
		doc1.setDatasources(srcDoc.getEFGDocument().getDatasources());
		doc1.setSubmitters(srcDoc.getEFGDocument().getSubmitters());
		List docsList = new ArrayList();
		
		if(req.getAttribute("taxon_count") == null){
			req.setAttribute("taxon_count",
					srcDoc.getEFGDocument().getTaxonEntries().getTaxonEntryCount() +"");
		}
		
	       
	        
		for(int i = 1; i <=srcDoc.getEFGDocument().getTaxonEntries().getTaxonEntryCount(); i++){
			TaxonEntryType t = srcDoc.getEFGDocument().getTaxonEntries().getTaxonEntry(i-1);
			entries.addTaxonEntry(t);
			if((i % numberOnEachPage) == 0){
				EFGDocument doc2 = new EFGDocument();
				doc2.setTaxonEntries(doc1.getTaxonEntries());
				doc2.setDatasources(doc1.getDatasources());
				doc2.setSubmitters(doc1.getSubmitters());
				EFGDocTemplate dt = new EFGDocTemplate();
				dt.setEFGDocument(doc2);
				dt.setTaxonPageTemplates(srcDoc.getTaxonPageTemplates());
				docsList.add(dt);
				
				doc1 = new EFGDocument();
				entries = new TaxonEntries();
				doc1.setTaxonEntries(entries);
				doc1.setDatasources(srcDoc.getEFGDocument().getDatasources());
				doc1.setSubmitters(srcDoc.getEFGDocument().getSubmitters());
			}
		}
		if(entries.getTaxonEntryCount() > 0){
			EFGDocTemplate dt1 = new EFGDocTemplate();
			dt1.setEFGDocument(doc1);
			dt1.setTaxonPageTemplates(srcDoc.getTaxonPageTemplates());
			docsList.add(dt1);
		}
		HttpSession prevNext = req.getSession(true);
		EFGSessionBeanInterface sessionBean = 
			EFGSpringFactory.getSessionBean();
		
		//"efgSessionID"
		sessionBean.setAttribute("docsList",docsList);
		sessionBean.setAttribute(EFGImportConstants.DATASOURCE_NAME, 
				(String) req.getAttribute(EFGImportConstants.DATASOURCE_NAME));
		
		sessionBean.setAttribute(EFGImportConstants.ALL_TABLE_NAME,
				(String) req.getAttribute(EFGImportConstants.ALL_TABLE_NAME));
		sessionBean.setAttribute(EFGImportConstants.DISPLAY_NAME,
				(String) req.getAttribute(EFGImportConstants.DISPLAY_NAME));
		sessionBean.setAttribute(EFGImportConstants.XSL_STRING, 
				(String)req.getAttribute(EFGImportConstants.XSL_STRING));
		sessionBean.setAttribute(EFGImportConstants.GUID,
				(String)req.getAttribute(EFGImportConstants.GUID));
		sessionBean.setAttribute(EFGImportConstants.SEARCH_PAGE_STR,
				(String) req.getAttribute(EFGImportConstants.SEARCH_PAGE_STR));

		sessionBean.setAttribute("fieldName",
				(String) req.getAttribute("fieldName"));
		sessionBean.setAttribute("mediaResourceField",  
				(String)req.getAttribute("mediaResourceField"));
		javax.xml.transform.Source xsl = getXSLSource(req);
		
		sessionBean.setAttribute("xslsource", xsl);
		//store the session
		prevNext.setAttribute(sessionBean.getSessionBeanID()+"",sessionBean);
		//store in request
		req.setAttribute("efgSessionID",sessionBean.getSessionBeanID()+"");
	}

	/* (non-Javadoc)
	 * @see project.efg.server.utils.ApplyXSLAbstract#getXSLSource(javax.servlet.http.HttpServletRequest)
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

    private Source buildXSL(HttpServletRequest req)
    {
        StringBuilder xslBuffer = new StringBuilder();
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
        return new StreamSource(new StringReader(xslBuffer.toString()));
    }
}
