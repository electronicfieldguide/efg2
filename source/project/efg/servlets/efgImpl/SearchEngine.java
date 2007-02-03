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

package project.efg.servlets.efgImpl;

//import project.efg.digir.*;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import project.efg.efgDocument.EFGDocument;
import project.efg.servlets.efgInterface.EFGHTTPQuery;
import project.efg.servlets.efgInterface.ResponseObject;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.servlets.factory.EFGHTTPQueryFactory;
import project.efg.servlets.factory.ResponseObjectFactory;
import project.efg.util.EFGImportConstants;


/**
 * This servlet handles requests to display a list of taxa with attribute values
 * matching a set of input parameters regardless of the backend.
 */
public class SearchEngine extends HttpServlet implements EFGImportConstants {
	static final long serialVersionUID = 1;
	
	protected String realPath;
	
	static Logger log = null;
	
	/**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			//log = Logger.getLogger(SearchEngine.class);
		} catch (Exception ee) {
		}
		realPath = getServletContext().getRealPath("/");
		
		if ((config.getInitParameter("applyXSLServlet")) == null)
			throw new ServletException(
			"applyXSLServlet servlet parameter required.");
		
		if ((config.getInitParameter("fopServlet")) == null)
			throw new ServletException("fopServlet servlet parameter required.");
	}
	
	/**
	 * This method is called when the servlet is shutdown. Closes the database
	 * then disconnects the servlet's thread from its Session. It's complement
	 * is the init method.
	 */
	public void destroy() {
		super.destroy();
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
		try {
			String displayFormat = req
			.getParameter(EFGImportConstants.DISPLAY_FORMAT);
			
			//TO DO: MERGE present and presentXML
			//log.debug("Get query instance");
			EFGHTTPQuery query = EFGHTTPQueryFactory.getQueryInstance(req);
			
			//log.debug("After get query instance");
			if (EFGImportConstants.XML.equalsIgnoreCase(displayFormat)) {
				presentXML(req, res, query.getEFGDocument());
			} else {
				req.setAttribute(
						EFGImportConstants.ALL_TABLE_NAME,
						query.getMainTableName());
				present(req, res, query.getEFGDocument());
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
			LoggerUtilsServlet.logErrors(ee);
			res.setContentType(EFGImportConstants.TEXT_HTML);
			PrintWriter out = res.getWriter();
			out
			.println("<H3>There are no results matching your search criteria.</H3>");
			out.flush();
			res.flushBuffer();
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
	/**
	 * This class forward the result to presentXML.jsp to show the EFGDocument.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 * @param xmlDoc
	 *            the result EFGDocument of the http query
	 */
	protected void presentXML(HttpServletRequest req, HttpServletResponse res,
			EFGDocument xmlDoc) throws IOException {
		
		if (xmlDoc == null) {
			//log.error("xml document is null");
			return;
		}
		try {
			req.setAttribute(EFGImportConstants.RESULT_SET, xmlDoc);
			
			// servlet interface provides getServletConfig method,
			ServletContext sctxt = getServletConfig().getServletContext();
			RequestDispatcher rd = sctxt
			.getRequestDispatcher("/presentXML.jsp");
			rd.forward(req, res);
		} catch (Exception ex) {
			//log.error(ex.getMessage());
			LoggerUtilsServlet.logErrors(ex);
			EFGContextListener.presentError(this.getClass().getName(), ex
					.getMessage(), res);
		}
	}

	/**
	 * This class forward the result to ApplyXSL servlet with appropriate xsl
	 * file depending on the number of the taxon in the result document. If
	 * there is one taxa, the xsl will transform to display the specie page.
	 * Otherwise a page of thumb pictures for the taxon.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 * @param xmlDoc
	 *            the result EFGDocument of the http query
	 */
	protected void present(HttpServletRequest req, HttpServletResponse res,
			EFGDocument efgDocument) throws Exception {
		ResponseObject resObject = null;
		
		try{
			resObject = ResponseObjectFactory.getResponseObject(req,
					efgDocument, realPath);
			
		} catch (Exception e) {//forward to error page
			//log.error(e.getMessage());
			efgDocument = null;
			resObject = ResponseObjectFactory.getResponseObject(req,
					efgDocument, realPath);
		
		}
		ServletContext sctxt = getServletConfig().getServletContext();
		RequestDispatcher rd = 
			sctxt.getRequestDispatcher(resObject.getForwardPage());
		rd.forward(req, res);
	
	}
}
//$Log$
//Revision 1.4  2007/02/03 23:46:25  kasiedu
//*** empty log message ***
//
//Revision 1.3  2007/02/03 00:04:27  kasiedu
//*** empty log message ***
//
//Revision 1.2  2006/12/08 03:51:00  kasiedu
//no message
//
//Revision 1.1.2.5  2006/08/26 22:12:24  kasiedu
//Updates to xsl files
//
//Revision 1.1.2.4  2006/08/09 18:55:25  kasiedu
//latest code confimrs to what exists on Panda
//
//Revision 1.1.2.3  2006/07/11 21:48:22  kasiedu
//"Added more configuration info"
//
//Revision 1.1.2.2  2006/06/09 14:39:49  kasiedu
//New Properties files
//
//Revision 1.1.2.1  2006/06/08 13:27:42  kasiedu
//New files
//
