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
//import project.efg.templates.util.*;
import project.efg.digir.*;
import project.efg.util.*;
import project.efg.efgInterface.*;
//import project.efg.db.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import org.jdom.*;
import org.jdom.Document;
import org.xml.sax.InputSource;
import java.text.SimpleDateFormat;
import java.text.*;
import java.util.*;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import project.efg.templates.taxonPageTemplates.*;
/**
 * This servlet handles requests to display a list of taxa with 
 * attribute values matching a set of input parameters regardless
 * of the backend.
 */
public class SearchEngine  extends HttpServlet implements EFGImportConstants
{
    private Format formatter = new SimpleDateFormat(EFGImportConstants.DIGIR_TIME);//Used as part of DiGIR response
    private String maxDispStr, applyXSLServlet, fopServlet, displayFormat, realPath;
    private int defaultMaxDisplay, maxDisplay;
    private static ServletConfig servletConfig;
    //private static String currentDatabase;
    //DiGIR response field names
    
    private static Namespace digir ;
    
    static Logger log = null;
   
    
    /**
     * This method is called when the servlet is first started up.
     * 
     * @params config the ServletConfig object for this servlet.
     */
    public void init(ServletConfig config) throws ServletException 
    {
	super.init(config);
	try{
	    log = Logger.getLogger(SearchEngine.class); 
	}
	catch(Exception ee){
	}

	digir = Namespace.getNamespace(EFGImportConstants.DIGIR_NAMESPACE);
	servletConfig = config;

	realPath = getServletContext().getRealPath("/");
	String defMaxDisplayString = config.getInitParameter(EFGImportConstants.DEFAULT_MAX_DISPLAY);
	try {
	    defaultMaxDisplay = Integer.parseInt(defMaxDisplayString);
	}
	catch (Exception e) {
	    throw new ServletException("Integer defaultMaxDisplay servlet "+
				       "init parameter required.");
	}
	if ((applyXSLServlet = config.getInitParameter("applyXSLServlet")) == null)
	    throw new ServletException("applyXSLServlet servlet parameter required.");

	if ((fopServlet = config.getInitParameter("fopServlet")) == null)
	    throw new ServletException("fopServlet servlet parameter required.");
    }

    /**
     * This method is called when the servlet is shutdown.  Closes the 
     * database then disconnects the servlet's thread from its Session.
     * It's complement is the init method.
     */
    public synchronized void destroy() 
    {
	super.destroy();
    }

    /**
     * Handles an HTTP POST request - Based most likely on a form submission.
     * 
     * @param req the servlet request object
     * @param res the servlet response object
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
	try{
	    String displayFormat = req.getParameter(EFGImportConstants.DISPLAY_FORMAT);
	    String dsName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);

	    if( EFGServletUtils.createConfigFileMap()){
		String currentHandler = EFGServletUtils.getQueryHandlerName();
		EFGQueryHandler qHandler = new EFGQueryHandlerFactory().getEFGQueryHandler(currentHandler);
	  
		ServletContext sctxt = getServletConfig().getServletContext();
	  
		String digirRequest = req.getParameter(EFGImportConstants.DIGIR);
	  
		if (digirRequest != null){//There is a DiGIR request
		    try{
			if (EFGImportConstants.HTML.equalsIgnoreCase(displayFormat)) {
			    present(req, res, qHandler.buildResult(req, res, servletConfig));
			}
			else{
			    presentXML(req, res, qHandler.buildResult(req, res, servletConfig));
			}
		    }
		    catch(Exception e){
			LoggerUtilsServlet.logErrors(e);
		    }
		}	
		else if (displayFormat == null || (displayFormat.equalsIgnoreCase(EFGImportConstants.HTML))) {
		    res.setContentType(EFGImportConstants.TEXT_HTML);
		    present(req, res, qHandler.buildResult(req, res, servletConfig));
		}
		else {
		    presentXML(req, res, qHandler.buildResult(req, res, servletConfig));
		}
	    }
	}
	catch(Exception ee){
	    res.setContentType(EFGImportConstants.TEXT_HTML);
	    PrintWriter out = res.getWriter();
	    out.println("<H3>There are no results matching your search criteria.</H3>");
	    out.flush();
	    res.flushBuffer();
	}
    }
    /**
     * Handles an HTTP GET request - Based most likely on a clicked link.
     *
     * @param req the servlet request object
     * @param res the servlet response object
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException 
    {
	doPost(req, res);
    }
    /**
     * This class forward the result to ApplyXSL servlet with appropriate xsl file 
     * depending on the number of the taxon in the result document. If there is one 
     * taxa, the xsl will transform to display the specie page. Otherwise a page of
     * thumb pictures for the taxon.
     *
     * @param req the servlet request object
     * @param res the servlet response object
     * @param xmlDoc the result EFGDocument of the http query
     */
    public void present(HttpServletRequest req, HttpServletResponse res, Object obj)
	throws IOException
    {
	if (obj == null) {
	    log.error("xml document is null");
	    return;
	}
	String context = req.getContextPath();
	Element xmlDoc = (Element) obj;
	// check the size of the taxon in the result document
	
	int taxonSize = xmlDoc.getChildren(EFGImportConstants.TAXONENTRY).size();
	log.debug("taxon Size:" + taxonSize);
	PrintWriter out = res.getWriter();
	if (taxonSize == 0){
	    try {
		// Tell the Browser that I'm sending back HTML
		out.println("<H3>There are no results matching your search criteria.</H3>");
		out.flush();
		res.flushBuffer();
		return;
	    }
	    catch (Exception ex) {
		ex.printStackTrace();
		return;
	    }
	}
	else {
	    String dsName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
	    String searchType = req.getParameter(EFGImportConstants.SEARCHTYPE);
	    // need to change it later to support an EFGDocument with taxon from multiple data sources
	    if (dsName == null){
		dsName = xmlDoc.getChild(EFGImportConstants.TAXONENTRY).getAttributeValue(EFGImportConstants.DATASOURCE_NAME);
	    }
	    // need to change it later to use the same xsl regardless of the taxon size and taxon's data sources
	    String xslFileName = null;
	    StringBuffer fileLocationBuffer = new StringBuffer();
	    fileLocationBuffer.append(realPath);
	    fileLocationBuffer.append(EFGImportConstants.TEMPLATES_FOLDER_NAME);
	    fileLocationBuffer.append(File.separator);
	    fileLocationBuffer.append(dsName);
	    if (taxonSize == 1){//NEEDS ENHANCEMENT..CURRENTLY ASSUMES A 1-1 MAPPING BETWEEN FILE AND DATASOURCE
		//if pdf
		fileLocationBuffer.append(EFGImportConstants.TAXONPAGE_FILLER);
	    }
	    else{
		if(EFGImportConstants.SEARCH_PLATES_TYPE.equalsIgnoreCase(searchType)){
		    req.setAttribute(EFGImportConstants.SEARCH_PAGE_STR,EFGImportConstants.SEARCHPAGE_PLATES_FILLER);
		    fileLocationBuffer.append(EFGImportConstants.SEARCHPAGE_PLATES_FILLER);
		}
		else{
		    req.setAttribute(EFGImportConstants.SEARCH_PAGE_STR,EFGImportConstants.SEARCHPAGE_LISTS_FILLER);
		    fileLocationBuffer.append(EFGImportConstants.SEARCHPAGE_LISTS_FILLER);
		}
	    }
	    try{
		FileReader reader = new FileReader(fileLocationBuffer.toString());
		project.efg.templates.taxonPageTemplates.TaxonPageTemplatesType  tptp=
		    (TaxonPageTemplatesType)TaxonPageTemplatesType.unmarshalTaxonPageTemplatesType(reader);
		project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem  tpi = 
		    tptp.getTaxonPageTemplatesTypeItem(0);
		project.efg.templates.taxonPageTemplates.TaxonPageTemplateType tp = tpi.getTaxonPageTemplate();
		
		xslFileName = tp.getXslFileName();
	    }
	    catch(Exception ee){
		
	    }
	   
	    StringBuffer fileLocationBuffer1 = new StringBuffer();
	    fileLocationBuffer1.append(realPath);
	    fileLocationBuffer1.append(EFGImportConstants.TEMPLATES_FOLDER_NAME);
	    fileLocationBuffer1.append(File.separator);
	    fileLocationBuffer1.append(xslFileName);

	    File filer = new File(fileLocationBuffer1.toString());
	    if((xslFileName == null) || (xslFileName.trim().equals("")) || (!filer.exists())){
		if(taxonSize > 1){
		    try{
		
			project.efg.efgInterface.EFGDSHelperFactory dsHelperFactory = 
			    new project.efg.efgInterface.EFGDSHelperFactory();
			RDBDataSourceHelper dsHelper = (RDBDataSourceHelper)dsHelperFactory.getDataSourceHelper(); 
			TreeMap searchFields =(TreeMap)dsHelper.getSearchable(dsName);
			EFGObject entry = null;
			String fieldName = null;
			String imageField = null;
		    
			if(searchFields != null){
			    if(searchFields.size() > 0){
				entry = (EFGObject)searchFields.firstKey();
				fieldName = entry.getName();
				log.debug("FieldName: " + fieldName);
			    }
			}
			searchFields =(TreeMap)dsHelper.getMediaResourceFields(dsName);
			if(searchFields != null){
			    if(searchFields.size() > 0){
				entry = (EFGObject)searchFields.firstKey();
				imageField = entry.getName();
			    }
			}
			searchType = req.getParameter(EFGImportConstants.SEARCHTYPE);
			if(searchType != null){
			    xslFileName = "defaultSearchFile.xsl";//use default search page xsl
			    if(EFGImportConstants.SEARCH_PLATES_TYPE.equalsIgnoreCase(searchType)){
				if(imageField != null){
				    req.setAttribute("mediaResourceField",imageField);
				}
			    }
			    if(fieldName != null){
				req.setAttribute("fieldName",fieldName);
			    }
			}
			if((xslFileName == null) || (xslFileName.trim().equals(""))){
			    throw new Exception("Could not find an XSL File");
			}
		    }
		    catch(Exception eee){
			log.error(eee.getMessage());
			eee.printStackTrace();
		    }
		}
		else{//use default taxon page xsl
		    xslFileName = "defaultTaxonPageFile.xsl";
		}
	    }
	    //forward the xml doc ApplyXSL servlet
	    try {
		xmlDoc.detach();
		req.setAttribute(EFGImportConstants.DATASOURCE_NAME, dsName);
		req.setAttribute(EFGImportConstants.XML, new Document(xmlDoc));
		
		//get xsl file name from the templateConfig file name
		String forwardString = context + "/" + EFGImportConstants.TEMPLATES_FOLDER_NAME +"/" + xslFileName;
		String fwdStrEncoded = "?xsl=" + URLEncoder.encode(forwardString, "UTF-8");
		if(xslFileName.endsWith(".fo")){
		    //if fop set fop pdf file name
		    //make up a pdfFile name
		    String xslFileLoc = fileLocationBuffer1.toString();
		    int lastInd = xslFileLoc.lastIndexOf(File.separator);
		    StringBuffer pdfFileBuffer = new StringBuffer();
		    pdfFileBuffer.append(xslFileLoc.substring(0,lastInd));
		    pdfFileBuffer.append(File.separator);
		    pdfFileBuffer.append(dsName);
		    pdfFileBuffer.append("_");
		    if (taxonSize == 1){
			pdfFileBuffer.append("taxonPage");
		    }
		    else{
			pdfFileBuffer.append("searchPage");
		    }
		    pdfFileBuffer.append(System.currentTimeMillis());
		    pdfFileBuffer.append(".pdf");
		    req.setAttribute("pdfFile",pdfFileBuffer.toString());
		    req.setAttribute("xslFileLocation", xslFileLoc);
		    fwdStrEncoded = "/" + fopServlet + fwdStrEncoded;
		}
		else{
		    //added "UTF-8" to make the source compile without warnings
		    //if xsl file ends in .fo forward to fop servlet..otherwise forward to applyXSl servlet
		 
		    fwdStrEncoded = "/" + applyXSLServlet + fwdStrEncoded;
		}
		ServletContext sctxt = getServletConfig().getServletContext();
		RequestDispatcher rd = sctxt.getRequestDispatcher(fwdStrEncoded);
		rd.forward(req, res);
	    }
	    catch(Exception e) {
		log.error(e.getMessage());
		e.printStackTrace();
		LoggerUtilsServlet.logErrors(e);
	    }
	}
    }

    /**
     * This class forward the result to presentXML.jsp to show the EFGDocument.
     *
     * @param req the servlet request object
     * @param res the servlet response object
     * @param xmlDoc the result EFGDocument of the http query
     */
    public void presentXML(HttpServletRequest req, HttpServletResponse res, Element xmlDoc)
	throws IOException
    {
	// return if the result document is null
	if (xmlDoc == null) {
	    log.error("xml document is null");
	    return;
	}
	try {
	    xmlDoc.detach();
	    req.setAttribute(EFGImportConstants.RESULT_SET, new Document(xmlDoc));
	    
	    // servlet interface provides getServletConfig method,
	    ServletContext sctxt = getServletConfig().getServletContext();
	    RequestDispatcher rd = sctxt.getRequestDispatcher("/presentXML.jsp");
	    rd.forward(req, res);
	} 
	catch (Exception ex) {
	    LoggerUtilsServlet.logErrors(ex);
	}
    }
}
//$Log$
//Revision 1.3  2006/02/10 03:47:39  kasiedu
//Added templates for generating PDF files
//
//Revision 1.2  2006/02/10 02:10:16  kasiedu
//Added a set-up for Fop templates
//
//Revision 1.1.1.1  2006/01/25 21:03:48  kasiedu
//Release for Costa rica
//
//Revision 1.8  2005/08/19 14:34:53  kasiedu
//Removed the dependence on the string 'efg' which was being used as servlet context.
//Application now reads the servlet context from the request
//
//Revision 1.7  2005/04/27 19:41:22  ram
//Recommit all of ram's allegedly working copy of efgNEW...
//
//Revision 1.1.1.1  2003/10/17 17:03:09  kimmylin
//no message
//
//Revision 1.13  2003/09/01 21:15:28  kasiedu
//*** empty log message ***
  //
  //Revision 1.12  2003/08/28 23:13:46  kasiedu
  //*** empty log message ***
  //
  //Revision 1.11  2003/08/28 20:11:17  kasiedu
  //*** empty log message ***
  //
  //Revision 1.9  2003/08/28 18:05:22  kimmylin
  //no message
  //
  //Revision 1.8  2003/08/28 17:41:32  kimmylin
  //no message
  //
  //Revision 1.7  2003/08/28 16:34:30  kimmylin
  //no message
  //
  //Revision 1.6  2003/08/08 17:28:56  kimmylin
  //no message
  //
  //Revision 1.3  2003/08/01 20:39:34  kasiedu
  //*** empty log message ***
  //
  //Revision 1.2  2003/07/31 21:08:23  kimmylin
  //no message
  //
  //Revision 1.1.1.1  2003/07/30 17:04:03  kimmylin
  //no message
  //
  //Revision 1.1.1.1  2003/07/28 18:58:08  kasiedu
  //EFG Relational Database
  //
  //Revision 1.6  2003/07/25 16:01:13  kimmylin
  //no message
  //
  //Revision 1.5  2003/07/20 21:14:15  kasiedu
  //*** empty log message ***
  //
  //Revision 1.4  2003/07/20 16:02:20  kasiedu
  //*** empty log message ***
  //
  //Revision 1.3  2003/07/20 15:56:42  kasiedu
  //*** empty log message ***
  //
  //Revision 1.2  2003/07/19 00:01:08  kasiedu
  //*** empty log message ***
  //
  //Revision 1.1.1.2  2003/07/18 21:50:16  kimmylin
  //RDB added
  //
  //Revision 1.11  2003/06/20 20:16:31  kimmylin
  //1. comment out unused imports
  //2. made the file independent of backend
  //3. add Log at the end
  //4. dead code cleaned
  //
