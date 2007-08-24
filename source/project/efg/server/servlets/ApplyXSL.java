/**
 * $Id: ApplyXSL.java,v 1.1.1.1 2007/08/01 19:11:23 kasiedu Exp $
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
package project.efg.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import project.efg.server.factory.EFGSpringFactory;
import project.efg.server.interfaces.ApplyXSLInterface;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGSessionBeanInterface;
import project.efg.util.utils.EFGDocTemplate;


/**
 * This servlet retrieve the xml and xsl file from the HttpServletRequest and
 * make transformation. Added an option in getXSLSource(HttpRequest) to allow
 * xsl file to be read outside of the servlet context(perhaps even
 * remotely)--kasiedu
 */
public class ApplyXSL extends HttpServlet
{

 /*
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
	//	digir = Namespace.getNamespace(EFGImportConstants.DIGIR_NAMESPACE);
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
	


    private void transform(HttpServletRequest req, HttpServletResponse res, Source xsl, Source xml)
        throws IOException
    {
        if(xsl != null && xml != null)
        {
            Transformer transformer = null;
            try
            {
                String header = null;
                transformer = tFactory.newTransformer(xsl);
                String dsName = (String)req.getAttribute(EFGImportConstants.DATASOURCE_NAME);
                String alldbname = (String)req.getAttribute("ALL_TABLE_NAME");
                String displayName = (String)req.getAttribute(EFGImportConstants.DISPLAY_NAME);
                String xslName = (String)req.getAttribute(EFGImportConstants.XSL_STRING);
                String guid = (String)req.getAttribute(EFGImportConstants.GUID);
                String searchPage = (String)req.getAttribute("search");
                String fieldName = (String)req.getAttribute("fieldName");
                String mediaresource = (String)req.getAttribute("mediaResourceField");
                String serverContext = req.getContextPath();
                String server = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
                transformer.setParameter("ALL_TABLE_NAME", alldbname);
                transformer.setParameter(EFGImportConstants.XSL_STRING, xslName);
                transformer.setParameter(EFGImportConstants.GUID, guid);
                transformer.setParameter(EFGImportConstants.DATASOURCE_NAME, dsName.toLowerCase());
                if(req.getAttribute("taxon_count") != null)
                    transformer.setParameter("taxon_count", (String)req.getAttribute("taxon_count"));
                transformer.setParameter(EFGImportConstants.DISPLAY_NAME, displayName);
                transformer.setParameter("server", server);
                String nextVal = (String)req.getAttribute("nextVal");
                if(nextVal != null)
                    transformer.setParameter("nextVal", nextVal);
                String prevVal = (String)req.getAttribute("prevVal");
                if(prevVal != null)
                    transformer.setParameter("prevVal", prevVal);
                String totalNumberOfPages = (String)req.getAttribute("totalNumberOfPages");
                if(totalNumberOfPages != null)
                    transformer.setParameter("totalNumberOfPages", totalNumberOfPages);
                else
                    transformer.setParameter("totalNumberOfPages", "1");
                String currentPageNumber = (String)req.getAttribute("currentPageNumber");
                if(currentPageNumber != null)
                    transformer.setParameter("currentPageNumber", currentPageNumber);
                else
                    transformer.setParameter("currentPageNumber", "1");
                String sessionID = (String)req.getAttribute("efgSessionID");
                if(sessionID != null)
                    transformer.setParameter("efgSessionID", sessionID);
                if(header != null)
                    transformer.setParameter("header", header);
                transformer.setParameter("serverContext", serverContext);
                String query = EFGImportConstants.SEARCH_STR + dsName + "&";
                transformer.setParameter("query", query);
                transformer.setParameter("serverbase", server + serverContext);
                if(searchPage != null)
                    transformer.setParameter("search", searchPage);
                if(req.getAttribute("mediaResourceField") != null)
                    transformer.setParameter("mediaResourceField", req.getAttribute("mediaResourceField"));
                if(fieldName != null)
                    transformer.setParameter("fieldName", fieldName);
            }
            catch(TransformerConfigurationException tce)
            {
                PrintWriter out = res.getWriter();
                out.println("<H3>Error on Server side .Please consult systems administrator</H3>");
                out.flush();
                res.flushBuffer();
                LoggerUtilsServlet.logErrors(tce);
                return;
            }
            catch(Exception ex)
            {
                PrintWriter out = res.getWriter();
                out.println("<H3>Error on Server side .Please consult systems administrator</H3>");
                out.flush();
                res.flushBuffer();
                LoggerUtilsServlet.logErrors(ex);
                return;
            }
            StringWriter sw = new StringWriter();
            PrintWriter out;
            try
            {
                transformer.transform(xml, new StreamResult(sw));
            }
            catch(TransformerException te)
            {
                LoggerUtilsServlet.logErrors(te);
                out = res.getWriter();
                out.println("<H3>Error on Server side .Please consult systems administrator</H3>");
                out.flush();
                res.flushBuffer();
                return;
            }
            catch(Exception ee)
            {
                LoggerUtilsServlet.logErrors(ee);
                out = res.getWriter();
                out.println("<H3>Error on Server side .Please consult systems administrator</H3>");
                out.flush();
                res.flushBuffer();
                return;
            }
            String result = sw.toString();
            res.setContentType("text/html");
            res.setContentLength(result.length());
            out = new PrintWriter(res.getWriter());
            out.print(result);
            out.close();
        } else
        {
            PrintWriter out = res.getWriter();
            out.println("<H3>Datasource contains illegal XMLcharacters.Please consult documentation on how to deal with it.</H3>");
            out.flush();
            res.flushBuffer();
            return;
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
        throws ServletException, IOException
    {
        String searchType = req.getParameter(EFGImportConstants.SEARCHTYPE);
        if("searches".equalsIgnoreCase(searchType))
            applySearchPage(req, res);
        else
            applyOtherPages(req, res);
    }
    /**
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
    private void applySearchPage(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        ApplyXSLInterface applyXSL = EFGSpringFactory.createApplyXSLInterface("searchpage");
        Source xml = applyXSL.getXMLSource(req);
        Source xsl = applyXSL.getXSLSource(req);
        transform(req, res, xsl, xml);
    }

    private void applyOtherPages(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        ApplyXSLInterface applyXSL = EFGSpringFactory.createApplyXSLInterface("notsearchpage");
        Source xml = null;
        Source xsl = null;
        String nextVal = null;
        String prevVal = null;
        EFGDocTemplate docT = null;
        EFGSessionBeanInterface sessionBean = null;
        String sessionID = (String)req.getAttribute("efgSessionID");
        if(sessionID != null)
        {
            HttpSession session = req.getSession(true);
            sessionBean = (EFGSessionBeanInterface)session.getAttribute(sessionID);
            if(sessionBean != null)
            {
                xsl = (Source)sessionBean.getAttribute("xslsource");
                List list = (List)sessionBean.getAttribute("docsList");
                setSessionAttributes2Request(req, sessionBean);
                req.setAttribute("totalNumberOfPages", list.size() + "");
                nextVal = (String)req.getAttribute("nextVal");
                if(nextVal != null)
                {
                    int nt = Integer.parseInt(nextVal);
                    if(nt > list.size())
                    {
                        req.setAttribute("currentPageNumber", list.size() + "");
                        nt = list.size();
                    } else
                    {
                        req.setAttribute("currentPageNumber", nt + "");
                    }
                    if(nt <= 0)
                    {
                        nt = 1;
                        req.setAttribute("currentPageNumber", nt + "");
                    }
                    docT = (EFGDocTemplate)list.get(nt - 1);
                    xml = applyXSL.getXMLSource(docT);
                    if(nt > 1)
                    {
                        prevVal = (nt - 1) + "";
                        req.setAttribute("prevVal", prevVal);
                    }
                    nt++;
                    if(nt <= list.size())
                    {
                        nextVal = nt + "";
                        req.setAttribute("nextVal", nextVal);
                    } else
                    {
                        nextVal = null;
                    }
                }
            }
        } else
        {
            xml = applyXSL.getXMLSource(req);
            sessionID = (String)req.getAttribute("efgSessionID");
            if(sessionID == null)
            {
                xsl = applyXSL.getXSLSource(req);
            } else
            {
                HttpSession session = req.getSession();
                sessionBean = (EFGSessionBeanInterface)session.getAttribute(sessionID);
                if(sessionBean != null)
                {
                    xsl = (Source)sessionBean.getAttribute("xslsource");
                    List list = (List)sessionBean.getAttribute("docsList");
                    if(list != null)
                    {
                        nextVal = "2";
                        req.setAttribute("nextVal", nextVal);
                        req.setAttribute("currentPageNumber", "1");
                        req.setAttribute("totalNumberOfPages", list.size() + "");
                        docT = (EFGDocTemplate)list.get(0);
                        xml = applyXSL.getXMLSource(docT);
                    }
                }
            }
        }
        transform(req, res, xsl, xml);
    }

    private void setSessionAttributes2Request(HttpServletRequest req, EFGSessionBeanInterface efgSession)
    {
        if(efgSession != null)
        {
            req.setAttribute(EFGImportConstants.DATASOURCE_NAME, efgSession.getAttribute(EFGImportConstants.DATASOURCE_NAME));
            req.setAttribute("ALL_TABLE_NAME", efgSession.getAttribute("ALL_TABLE_NAME"));
            req.setAttribute(EFGImportConstants.DISPLAY_NAME, efgSession.getAttribute(EFGImportConstants.DISPLAY_NAME));
            req.setAttribute(EFGImportConstants.XSL_STRING, efgSession.getAttribute(EFGImportConstants.XSL_STRING));
            req.setAttribute(EFGImportConstants.GUID, efgSession.getAttribute(EFGImportConstants.GUID));
            req.setAttribute("search", efgSession.getAttribute("search"));
            req.setAttribute("fieldName", efgSession.getAttribute("fieldName"));
            req.setAttribute("mediaResourceField", efgSession.getAttribute("mediaResourceField"));
        }
    }

    private static final long serialVersionUID = 1L;
    private static TransformerFactory tFactory;
    static Logger log = null;
    String realPath;

}
