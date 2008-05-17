/**
 * $Id$
 *
 * Copyright (c) 2007 University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
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

import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.efg.server.utils.*;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.interfaces.EFGImportConstants;

public class TaxonPageTemplateConfig extends EFGTemplateConfig
{

   
    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
    }

    public void processParams(HttpServletRequest req, HttpServletResponse res)
    {
        try
        {
            String dsName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
            if(dsName == null || dsName.trim().equals(""))
                dsName = (String)req.getAttribute(EFGImportConstants.DATASOURCE_NAME);
            String displayName = req.getParameter(EFGImportConstants.DISPLAY_NAME);
            if(displayName == null || displayName.trim().equals(""))
                displayName = (String)req.getAttribute(EFGImportConstants.DISPLAY_NAME);
            String mTableName = req.getParameter("ALL_TABLE_NAME");
            if(displayName == null || "".equals(displayName))
            {
                Map map = ServletCacheManager.getDatasources(mTableName);
                displayName = (String)map.get(dsName.toLowerCase());
            }
            project.efg.templates.taxonPageTemplates.TaxonPageTemplates tps = getTaxonPageTemplateRoot(dsName, mTableName);
            if(tps == null)
                throw new Exception("Datasource not found");
            XslPage xslPage = getXSLPageParams(req, tps);
            if(xslPage == null)
                throw new Exception("xslPage is null");
            String guid = xslPage.getGuid();
            if(guid != null && !guid.trim().equals(""))
                req.setAttribute(EFGImportConstants.GUID, guid);
            String uniqueName = xslPage.getDisplayName();
            if(uniqueName != null && !uniqueName.trim().equals(""))
                req.setAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME, uniqueName);
            project.efg.templates.taxonPageTemplates.GroupsType groups = xslPage.getGroups();
            if(groups == null)
                throw new Exception("Groups is null");
            req.setAttribute(EFGImportConstants.DATASOURCE_NAME, dsName);
            req.setAttribute("DISPLAY_NAME", displayName);
            EFGFieldObject field = add2Groups(req, xslPage);
            boolean done = saveToDB(dsName, tps);
            if(done)
            {
                if(field != null)
                {
                    req.setAttribute("fieldName", field.getFieldName());
                    req.setAttribute("fieldValue", field.getFieldValue());
                }
                forwardPage(req, res, false);
            } else
            {
                throw new Exception("Error occured during saving of file");
            }
        }
        catch(Exception ee)
        {
            ee.printStackTrace();
            try
            {
                forwardPage(req, res, true);
            }
            catch(Exception eef) { }
        }
    }

    private void forwardPage(HttpServletRequest req, HttpServletResponse res, boolean isError)
        throws Exception
    {
        RequestDispatcher dispatcher = null;
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        try
        {
            if(!isError)
            {
                String searchType = req.getParameter("searchType");
                String searchPage = req.getParameter("search");
                String allTableName = req.getParameter("ALL_TABLE_NAME");
                if(allTableName == null || allTableName.trim().equals(""))
                    allTableName = EFGImportConstants.EFG_RDB_TABLES;
                req.setAttribute("ALL_TABLE_NAME", allTableName);
                if(searchPage != null)
                {
                    if("plates".equalsIgnoreCase(searchType))
                    {
                        req.setAttribute("searchType", "plates");
                        dispatcher = getServletContext().getRequestDispatcher("/templateJSP/tests/TestSearchResultsPage.jsp");
                    } else
                    if("searches".equalsIgnoreCase(searchType))
                    {
                        req.setAttribute("searchType", "searches");
                        dispatcher = getServletContext().getRequestDispatcher("/templateJSP/tests/TestSearchPage.jsp");
                    } else
                    if("pdfs".equalsIgnoreCase(searchType) || "pdfbook".equalsIgnoreCase(searchType))
                        dispatcher = getServletContext().getRequestDispatcher("/PDFSuccessPage.html");
                    else
                    if("lists".equalsIgnoreCase(searchType))
                    {
                        req.setAttribute("searchType", "lists");
                        dispatcher = getServletContext().getRequestDispatcher("/templateJSP/tests/TestSearchResultsPage.jsp");
                    } else
                    {
                        dispatcher = getServletContext().getRequestDispatcher("/templateJSP/error/TemplateError.jsp");
                    }
                } else
                if("pdfs".equalsIgnoreCase(searchType) || "pdfbook".equalsIgnoreCase(searchType))
                    dispatcher = getServletContext().getRequestDispatcher("/PDFSuccessPage.html");
                else
                    dispatcher = getServletContext().getRequestDispatcher("/templateJSP/tests/TestTaxonPage.jsp");
            } else
            {
                dispatcher = getServletContext().getRequestDispatcher("/templateJSP/error/TemplateError.jsp");
            }
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
            dispatcher = getServletContext().getRequestDispatcher("/templateJSP/error/TemplateError.jsp");
        }
        dispatcher.forward(req, res);
        out.flush();
        res.flushBuffer();
    }

    private static final long serialVersionUID = 1L;
}
