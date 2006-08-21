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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

import project.efg.servlets.efgServletsUtil.ApplyXSLInterface;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.util.EFGImportConstants;

/**
 * This servlet retrieve the xml and xsl file from the HttpServletRequest and
 * make transformation. Added an option in getXSLSource(HttpRequest) to allow
 * xsl file to be read outside of the servlet context(perhaps even
 * remotely)--kasiedu
 */
public class ApplyXSL extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			//log = Logger.getLogger(ApplyXSL.class);
		} catch (Exception ee) {
		}
		digir = Namespace.getNamespace(EFGImportConstants.DIGIR_NAMESPACE);
		realPath = getServletContext().getRealPath("/");
		tFactory = TransformerFactory.newInstance();
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
	 * Handles an HTTP POST request - Based most likely on a form submission.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		//do splitting to fop here
		
		String dsName = (String) req.getAttribute(EFGImportConstants.DATASOURCE_NAME);
		String displayName = (String) req.getAttribute(EFGImportConstants.DISPLAY_NAME);
		String xslName  = (String)req.getAttribute(EFGImportConstants.XSL_STRING);
        String guid = (String)req.getAttribute(EFGImportConstants.GUID);
		ApplyXSLInterface applyXSL = ApplyXSLInterface
				.createApplyXSLInterface();

		javax.xml.transform.Source xsl = applyXSL.getXSLSource(req);
		javax.xml.transform.Source xml = applyXSL.getXMLSource(req);

		if ((xsl != null) && (xml != null)) {
			Transformer transformer = null;
			try {
				String header = getHeader(req);
			
				transformer = tFactory.newTransformer(xsl);
				
				
				String serverContext = req.getContextPath();
				String server = req.getScheme() + "://" + req.getServerName()
						+ ":" + req.getServerPort();
				
				transformer.setParameter(EFGImportConstants.XSL_STRING,xslName);
				
				
				transformer.setParameter(EFGImportConstants.GUID,guid);
				
				
				transformer.setParameter(EFGImportConstants.DATASOURCE_NAME, dsName.toLowerCase());
				
				
				transformer.setParameter(EFGImportConstants.DISPLAY_NAME, displayName);
			
				
				transformer.setParameter("server", server);
			
				
				transformer.setParameter("header", header);
			
				
				transformer.setParameter("serverContext", serverContext);
			
				
				String query = EFGImportConstants.SEARCH_STR + dsName
						+ EFGImportConstants.AMP;
				
				transformer.setParameter("query", query);
			
				
				transformer.setParameter("serverbase", server + serverContext);

				String searchPage = (String) req
						.getAttribute(EFGImportConstants.SEARCH_PAGE_STR);
				if (searchPage != null) {
					transformer.setParameter(
							EFGImportConstants.SEARCH_PAGE_STR, searchPage);
				}
				if (req.getAttribute("mediaResourceField") != null) {
					transformer.setParameter("mediaResourceField", (String) req
							.getAttribute("mediaResourceField"));
				}
				else{
					//System.out.println("mediaresource field is null!!");
				}
				String fieldName = (String) req.getAttribute("fieldName");
				if (fieldName != null) {
	
					transformer.setParameter("fieldName",fieldName);
				}
				else{
					//System.out.println("fieldName is null!!");
				}
				// set more parameters
				// if more than one means go to search results page
			} catch (TransformerConfigurationException tce) {
				PrintWriter out = res.getWriter();
				// Tell the Browser that I'm sending back HTML
				out
						.println("<H3>Error on Server side .Please consult systems administrator</H3>");
				out.flush();
				res.flushBuffer();
				LoggerUtilsServlet.logErrors(tce);
				return;
			} catch (Exception ex) {
				PrintWriter out = res.getWriter();
				// Tell the Browser that I'm sending back HTML
				out
						.println("<H3>Error on Server side .Please consult systems administrator</H3>");
				out.flush();
				res.flushBuffer();
				//log
					//	.error("Could not parse stylesheet (TransformerConfigurationException)");
				LoggerUtilsServlet.logErrors(ex);
				return;
			}

			StringWriter sw = new StringWriter();
			try {
				transformer.transform(xml, new StreamResult(sw));
			} catch (TransformerException te) {
				LoggerUtilsServlet.logErrors(te);
				PrintWriter out = res.getWriter();
				// Tell the Browser that I'm sending back HTML
				out
						.println("<H3>Error on Server side .Please consult systems administrator</H3>");
				out.flush();
				res.flushBuffer();
			
				return;
			} catch (Exception ee) {
				LoggerUtilsServlet.logErrors(ee);
				PrintWriter out = res.getWriter();
				// Tell the Browser that I'm sending back HTML
				out
						.println("<H3>Error on Server side .Please consult systems administrator</H3>");
				out.flush();
				res.flushBuffer();
				return;
			}
			String result = sw.toString();
			res.setContentType("text/html");
			res.setContentLength(result.length());
			PrintWriter out = new PrintWriter(res.getWriter());
			out.print(result);
			out.close();
		}
		else{
			PrintWriter out = res.getWriter();
			// Tell the Browser that I'm sending back HTML
			out
					.println("<H3>Datasource contains illegal XMLcharacters.Please consult systems administrator</H3>");
			out.flush();
			res.flushBuffer();
		
			return;
		}
	}

	private String getHeader(HttpServletRequest req) throws Exception {
		
		Element source = null;
		String clientIPaddr = req.getRemoteAddr(); // 123.123.123.123
		// Get client's hostname
		// String clientDomainName = req.getRemoteHost(); // hostname.com
		String dsName = (String) req.getAttribute("dataSourceName");

		// If the resource is null and there is an error send the
		// defaultvresource shown below.
		StringBuffer buf1 = new StringBuffer();
		buf1.append(req.getScheme()); // http
		buf1.append("://");
		buf1.append(req.getServerName()); // tiger.cs.umb.edu
		buf1.append(":");
		buf1.append(req.getServerPort()); // 8080
		buf1.append(req.getContextPath()); // efg
		buf1.append(req.getServletPath()); // efg
		String resource = buf1.toString();

		Element header = new Element("header", digir);

		// Read from servlet context or from a properties file
		Element version = new Element("version", digir);
		version.setText("version 1.0");
		header.addContent(version);

		String s = formatter.format(new Date());
		Element sendTime = new Element("sendTime", digir);
		sendTime.setText(s);
		header.addContent(sendTime);
		source = new Element("source", digir);
		source.setAttribute("resource", resource);
		source.setText(resource);

		header.addContent(source);

		Element destination = new Element("destination", digir);
		destination.setAttribute("resource", dsName);
		destination.setText(clientIPaddr);
		header.addContent(destination);

		Element type = new Element("type", digir);
		type.setText("search");
		header.addContent(type);

		XMLOutputter outputter = new XMLOutputter();
		StringWriter sw = new StringWriter();
		outputter.output(header, sw);
		return sw.toString();
	}
}
// $Log$
// Revision 1.1.2.4  2006/08/21 19:32:55  kasiedu
// Updates to  files
//
// Revision 1.1.2.3  2006/08/09 18:55:24  kasiedu
// latest code confimrs to what exists on Panda
//
// Revision 1.1.2.2  2006/07/11 21:48:22  kasiedu
// "Added more configuration info"
//
// Revision 1.1.2.1  2006/06/08 13:27:42  kasiedu
// New files
//
// Revision 1.1.1.1 2006/01/25 21:03:48 kasiedu
// Release for Costa rica
//
// Revision 1.8 2005/08/19 14:33:09 kasiedu
// Enhancement that sets the servlet context parameter in stylesheets
//
// Revision 1.7 2005/04/27 19:41:22 ram
// Recommit all of ram's allegedly working copy of efgNEW...
//
// Revision 1.1.1.1 2003/10/17 17:03:09 kimmylin
// no message
//
// Revision 1.4 2003/08/20 18:45:42 kimmylin
// no message
//
// Revision 1.3 2003/08/08 17:28:39 kimmylin
//no message
//

