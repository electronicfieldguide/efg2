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

package project.efg.servlet;

import project.efg.util.*;
import org.jdom.*;
import org.jdom.output.*;
import java.io.*;
import java.net.*;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.xml.transform.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.*;
import java.text.SimpleDateFormat;
import java.text.*;
import java.util.*;
import project.efg.templates.taxonPageTemplates.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * This servlet retrieve the xml and xsl file from the HttpServletRequest and make transformation.
 * Added an option in getXSLSource(HttpRequest) to allow xsl file to be read outside of the 
 * servlet context(perhaps even remotely)--kasiedu
 */
public class ApplyXSL extends HttpServlet 
{
    private static TransformerFactory tFactory;
    private static Namespace digir = null;
    private Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
    static Logger log = null;
    String realPath = null;
    /**
     * This method is called when the servlet is first started up.
     * 
     * @params config the ServletConfig object for this servlet.
     */
    public void init(ServletConfig config) throws ServletException 
    {
	super.init(config);
	try{
	    log = Logger.getLogger(ApplyXSL.class); 
	}
	catch(Exception ee){
	}
	digir = Namespace.getNamespace(EFGImportConstants.DIGIR_NAMESPACE);
	realPath = getServletContext().getRealPath("/");
	tFactory = TransformerFactory.newInstance();
    }
    
    /**
     * Handles an HTTP GET request - Based most likely on a clicked link.
     *
     * @param req the servlet request object
     * @param res the servlet response object
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException 
    {
	doPost(req, res);
    }

    /**
     * Handles an HTTP POST request - Based most likely on a form submission.
     * 
     * @param req the servlet request object
     * @param res the servlet response object
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException 
    {
      
      	String dsName = (String)req.getAttribute("dataSourceName");
	
	
	javax.xml.transform.Source xsl = getXSLSource(req);
	javax.xml.transform.Source xml = getXMLSource(req);
	
	if ( (xsl != null) && (xml != null) ) {
	    Transformer transformer;
	    try {
		String header = getHeader(req);	
		log.debug("Setting transformer");
		transformer = tFactory.newTransformer(xsl);
		log.debug("Setting transformer done");
		String serverContext = req.getContextPath();
		String server = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
		
		transformer.setParameter("datasource",dsName);
		log.debug("Datasource: " + dsName);
		transformer.setParameter("server", server);
		log.debug("server: " + server);
		transformer.setParameter("header",header);
		log.debug("header: " + header);
		transformer.setParameter("serverContext",serverContext);
		log.debug("serverContext: " + serverContext);
		String query = EFGImportConstants.SEARCH_STR + dsName + EFGImportConstants.AMP;
		transformer.setParameter("query",query);
		log.debug("query: " + query);
		transformer.setParameter("serverbase",server + serverContext);
		
		String searchPage = (String)req.getAttribute(EFGImportConstants.SEARCH_PAGE_STR);
		if(searchPage != null){
		    log.debug("Setting search type: " + EFGImportConstants.SEARCH_PAGE_STR);
		    log.debug("Setting search type: " + searchPage);
		    transformer.setParameter(EFGImportConstants.SEARCH_PAGE_STR,searchPage);
		}
		if(req.getAttribute("mediaResourceField") != null){
		    transformer.setParameter("mediaResourceField",(String)req.getAttribute("mediaResourceField"));
		}
		if(req.getAttribute("fieldName") != null){
		    transformer.setParameter("fieldName",(String)req.getAttribute("fieldName"));
		}
		//set more parameters
		//if more than one means go to search results page
	    } catch (TransformerConfigurationException tce) {
		PrintWriter out = res.getWriter();
		// Tell the Browser that I'm sending back HTML
		out.println("<H3>Error on Server side .Please consult systems administrator</H3>");
		out.flush();
		res.flushBuffer();
		log.error("Could not parse stylesheet (TransformerConfigurationException)");
		LoggerUtilsServlet.logErrors(tce);
		
		return;
	    }
	    catch (Exception ex) {
		PrintWriter out = res.getWriter();
		// Tell the Browser that I'm sending back HTML
		out.println("<H3>Error on Server side .Please consult systems administrator</H3>");
		out.flush();
		res.flushBuffer();
		log.error("Could not parse stylesheet (TransformerConfigurationException)");
		LoggerUtilsServlet.logErrors(ex);
		return;
	    }

	    StringWriter sw = new StringWriter();
	    try { 
		transformer.transform(xml, new StreamResult(sw));
	    } catch (TransformerException te) {
		LoggerUtilsServlet.logErrors(te);
		te.printStackTrace();
		log.error("Error while performing transformation (TransformerException)");
		PrintWriter out = res.getWriter();
		// Tell the Browser that I'm sending back HTML
		out.println("<H3>Error on Server side .Please consult systems administrator</H3>");
		out.flush();
		res.flushBuffer();
		log.error("Could not parse stylesheet (TransformerConfigurationException)");
	
		return;
	    } catch (Exception ee) {
		LoggerUtilsServlet.logErrors(ee);
		PrintWriter out = res.getWriter();
		// Tell the Browser that I'm sending back HTML
		out.println("<H3>Error on Server side .Please consult systems administrator</H3>");
		out.flush();
		res.flushBuffer();
		log.error("Could not parse stylesheet (TransformerConfigurationException)");
		return;
	    }
	    String result = sw.toString();
	    res.setContentType("text/html");
	    res.setContentLength(result.length());
	    PrintWriter out = new PrintWriter(res.getWriter());
	    out.print(result);
	    out.close();
	}
    }
    private String getHeader(HttpServletRequest req) throws Exception {

	Element source = null;
	String clientIPaddr = req.getRemoteAddr(); // 123.123.123.123
	// Get client's hostname
	String clientDomainName = req.getRemoteHost(); // hostname.com
	String dsName = (String)req.getAttribute("dataSourceName");
   
	//If the resource is null and there is an error send the defaultvresource shown below.
  	StringBuffer buf1 = new StringBuffer();
	buf1.append(req.getScheme()); // http
	buf1.append("://");
	buf1.append(req.getServerName()); //tiger.cs.umb.edu
	buf1.append(":");
	buf1.append(req.getServerPort()); // 8080
	buf1.append(req.getContextPath()); //efg
	buf1.append(req.getServletPath()); //efg
	String resource = buf1.toString();
  
    
	Element header = new Element("header",digir);
    
	//Read from servlet context or from a properties file
	Element version = new Element("version",digir);
	version.setText("version 1.0"); 
	header.addContent(version);
    
	String s = formatter.format(new Date());
	Element sendTime = new Element("sendTime",digir);
	sendTime.setText(s);
	header.addContent(sendTime);
	source = new Element("source",digir);
	source.setAttribute("resource",resource);
	source.setText(resource);
    
	header.addContent(source);
    
	Element destination = new Element("destination",digir);
	destination.setAttribute("resource",dsName);
	destination.setText(clientIPaddr);
	header.addContent(destination);
    
	Element type = new Element("type",digir);
	type.setText("search");
	header.addContent(type);
      
	XMLOutputter outputter = new XMLOutputter();  
	StringWriter sw = new StringWriter();
	outputter.output(header, sw);
    
	return sw.toString();
    
    }
    /**
     * Get the xml document as a String or a jdom Document from the HttpServletRequest 
     * object and transform it to a jdom Source. 
     *
     * @param req the servlet request object
     * @return the jdom Source transformed from the xml document
     */
    private Source getXMLSource(HttpServletRequest req)
    {
	String filename = req.getParameter("xml");

	if (filename == null) {
	    log.debug("retrieving xml source as jdom tree from request");
	    org.jdom.Document srcDoc;
	    DOMOutputter xmlOut;
	    javax.xml.transform.dom.DOMSource src= null;
	    srcDoc = (org.jdom.Document) req.getAttribute("xml");
	    if (srcDoc == null) {
		log.error("No xml source specified. Transformation failed");
		return null;
	    }

	    xmlOut = new DOMOutputter();
	    try {
		src = new javax.xml.transform.dom.DOMSource(xmlOut.output(srcDoc));
	    } catch (JDOMException je) {
		log.error("error on jdom to w3c dom conversion on xml source");
		LoggerUtilsServlet.logErrors(je);
	    }
	    return src;
	}

	else {
	    InputStream is = null;
	    try {
		is = (new URL(filename)).openStream();
	    } 
	    catch (MalformedURLException mue) {
		log.error("malformed url for xml source");
		return null;
	    }
	    catch (IOException ioe) {
		log.error("error while trying to access xml via url");
		return null;
	    }
	    return new javax.xml.transform.stream.StreamSource(is);
	}
    }
    /**
     * Get the xsl filename as a String from the HttpServletRequest 
     * object and transform it to a jdom Source. 
     *
     * @param req the servlet request object
     * @return the jdom Source transformed from the xml document
     */
    private Source getXSLSource(HttpServletRequest req)
    {
	String filename = req.getParameter("xsl");
	String xslURL = null;
	if (filename == null) return buildXSL(req);
	else {
	    //added by J. Asiedu
	    if(filename.indexOf(":") == -1) { //then a relative path to file is being given(Added by J.Asiedu)
		xslURL = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + filename;
		log.debug("trying to get xsl from specified url " + xslURL);
	    }
	    else{ //A full path is being given (Added by J.Asiedu)
		xslURL = filename;
	    }
	    return new javax.xml.transform.stream.StreamSource(xslURL);
	}
    }

    /**
     * Build the xsl file from the parameters in HttpServletRequest 
     * object and transform it to a jdom Source. 
     *
     * @param req the servlet request object
     * @return the jdom Source transformed from the xml document
     */	
    private Source buildXSL(HttpServletRequest req)
    {
	String xsl;
	log.debug("xsl built on the fly");
	xsl="<?xml version='1.0' encoding='UTF-8'?>"+
	    "<xsl:stylesheet version='1.0' "+ 
	    "xmlns:xsl='http://www.w3.org/1999/XSL/Transform' >"+
	    "<xsl:output method='html' encoding='utf-8' />"+
	    "<xsl:param name='resource-path'>"+
	    getParameter(req, "resource-path")+"</xsl:param>"+
	    "<xsl:param name='image-base'>"+
	    getParameter(req, "image-base")+"</xsl:param>"+
	    "<xsl:param name='images-per-row'>"+
	    getParameter(req, "images-per-row")+"</xsl:param>"+
	    "<xsl:include href='"+
	    getParameter(req, "image-lib-url")+"' />"+
	    "<xsl:include href='"+
	    getParameter(req, "core-lib-url")+"' />"+
	    "</xsl:stylesheet>";
	
	return new javax.xml.transform.stream.StreamSource(new StringReader(xsl));
    }
    
    /**
     * Get the HttpServletRequest parameters and return the value.
     *
     * @param req the servlet request object
     * @param name the name of the parameter of the HttpServletRequest
     * @return the value of the parameter as a string
     */	
    private String getParameter(HttpServletRequest req, String name) 
    {
	String value=null;
	
	log.debug("Retrieving value for parameter '"+name+"'");
	value = req.getParameter(name);
	if (value == null) {
	    log.debug("not found in request, looking for init param");
	    value = getInitParameter(name);
	}
	if (value== null){
	    log.error("Could not find value for parameter '"+name+"'");
	}
	return value;
    }	
}
//$Log$
//Revision 1.1  2006/01/25 21:03:48  kasiedu
//Initial revision
//
//Revision 1.8  2005/08/19 14:33:09  kasiedu
//Enhancement that sets the servlet context parameter in stylesheets
//
//Revision 1.7  2005/04/27 19:41:22  ram
//Recommit all of ram's allegedly working copy of efgNEW...
//
//Revision 1.1.1.1  2003/10/17 17:03:09  kimmylin
//no message
//
//Revision 1.4  2003/08/20 18:45:42  kimmylin
//no message
//
//Revision 1.3  2003/08/08 17:28:39  kimmylin
//no message
//


























