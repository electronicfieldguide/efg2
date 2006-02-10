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
package project.efg.servlet;
import project.efg.util.*;
import org.jdom.*;
import org.jdom.output.*;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.transform.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FormattingResults;
import org.apache.fop.messaging.MessageHandler;

//            GenerateFO, StylesheetCache

public class FOPServlet extends HttpServlet
{
    private static Driver driver;
    private static TransformerFactory tFactory;
    /**
     * This method is called when the servlet is first started up.
     * 
     * @params config the ServletConfig object for this servlet.
     */
    public void init(ServletConfig config) throws ServletException 
    {
	super.init(config);
	driver = new Driver();
	tFactory = TransformerFactory.newInstance();
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
	
	return new javax.xml.transform.stream.StreamSource(new StringReader(xslBuffer.toString()));
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
	    org.jdom.Document srcDoc;
	    DOMOutputter xmlOut;
	    javax.xml.transform.dom.DOMSource src= null;
	    srcDoc = (org.jdom.Document) req.getAttribute("xml");
	    if (srcDoc == null) {
		return null;
	    }

	    xmlOut = new DOMOutputter();
	    try {
		src = new javax.xml.transform.dom.DOMSource(xmlOut.output(srcDoc));
	    } catch (JDOMException je) {
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
		return null;
	    }
	    catch (IOException ioe) {
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
	    }
	    else{ //A full path is being given (Added by J.Asiedu)
		xslURL = filename;
	    }
	    return new javax.xml.transform.stream.StreamSource(xslURL);
	}
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	
        synchronized(this){
	    try{
		File pdfFile = new File((String)request.getAttribute("pdfFile"));
		File xslFile = new File((String)request.getAttribute("xslFileLocation"));
		if(pdfFile.exists()){
		    if(pdfFile.lastModified() >= xslFile.lastModified()){
			redirect(response,pdfFile.getName());
			return;
		    }
		}
		driver.reset();
                OutputStream out = new FileOutputStream(pdfFile);
		driver.setOutputStream(out);
                Logger logger = new ConsoleLogger(2);
                MessageHandler.setScreenLogger(logger);
                driver.setLogger(logger);
                driver.setRenderer(1);
		javax.xml.transform.Source xsl = getXSLSource(request);
		javax.xml.transform.Source xml = getXMLSource(request);
		Transformer transformer= tFactory.newTransformer(xsl);
                
                javax.xml.transform.Result res = new SAXResult(driver.getContentHandler());
                transformer.transform(xml, res);
                FormattingResults results = driver.getResults();
		out.close();
		redirect(response,pdfFile.getName());
            }
            catch(Exception ioe)
            {
                ioe.printStackTrace();
            }
        }
    }
    private void redirect(HttpServletResponse response, String pdfFile)throws Exception{
	StringBuffer buf = new StringBuffer();
	buf.append("/html/");
	buf.append(pdfFile);
	String encodedURL = response.encodeRedirectURL(buf.toString());
	response.sendRedirect(encodedURL);		      
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doPost(request, response);
    }

   

}
