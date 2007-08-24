/**
 * $Id$
 *
 * Copyright (c) 2007  University of Massachusetts Boston
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

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.efg.efgDocument.EFGDocument;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.server.utils.ServletCacheManager;
import project.efg.templates.taxonPageTemplates.*;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.*;

public class PDFPageTemplateConfig extends EFGTemplateConfig
{

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
    }

    public void processParams(HttpServletRequest req, HttpServletResponse res)
        throws IOException
    {
        XslPage xslPage;
        try
        {
            String isSavePDF = req.getParameter("savepdf");
            xslPage = getXSLPageParams(req);
            if(xslPage == null)
                throw new Exception("xslPage is null");
            GroupsType groups = xslPage.getGroups();
            if(groups == null)
                throw new Exception("Groups is null");
            add2Groups(req, xslPage);
            if(isSavePDF != null && !isSavePDF.trim().equals(""))
            {
                savePDF(req, res, xslPage);
                return;
            }
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
            try
            {
                forwardPage(req, res, false, true);
            }
            catch(Exception e)
            {
                res.getOutputStream().println(ee.getMessage());
                res.getOutputStream().flush();
                res.getOutputStream().close();
            }
            return;
        }
        req.setAttribute("isPreview", "isPreview");
        try {
			handlePDFTests(req, res, xslPage);
		} catch (Exception e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
        return;
    }

    private void handlePDFTests(HttpServletRequest req, HttpServletResponse res, XslPage xslPage)
        throws Exception
    {
        String dsName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
        String displayName = req.getParameter(EFGImportConstants.DISPLAY_NAME);
        if(displayName == null || "".equals(displayName))
        {
            Map map = ServletCacheManager.getDatasourceCache(req.getParameter("ALL_TABLE_NAME").toLowerCase());
            displayName = (String)map.get(dsName.toLowerCase());
        }
        String xml = req.getParameter("searchqueryresultsxml");
        String guid = xslPage.getGuid();
        if(guid != null && !guid.trim().equals(""))
            req.setAttribute(EFGImportConstants.GUID, guid);
        String uniqueName = xslPage.getDisplayName();
        if(uniqueName != null && !uniqueName.trim().equals(""))
            req.setAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME, uniqueName);
        req.setAttribute(EFGImportConstants.DATASOURCE_NAME, dsName);
        req.setAttribute("DISPLAY_NAME", displayName);
        String authors = findAuthors(dsName);
        if(authors == null)
            authors = "";
        EFGDocument efgDoc = null;
        if(xml != null)
            efgDoc = toEFGDoc(xml);
        if(efgDoc == null)
        {
            throw new Exception("efgDoc attribute is null");
        } else
        {
            req.setAttribute("authors", authors);
            req.setAttribute("xslPage", xslPage);
            req.setAttribute("efgDoc", efgDoc);
            forwardPage(req, res, false, false);
            return;
        }
    }

    private void savePDF(HttpServletRequest req, HttpServletResponse res, XslPage xslPage)
        throws Exception
    {
        String dsName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
        String displayName = req.getParameter(EFGImportConstants.DISPLAY_NAME);
        String tableName = req.getParameter("ALL_TABLE_NAME");
        project.efg.templates.taxonPageTemplates.TaxonPageTemplates tps = getTaxonPageTemplateRoot(dsName, tableName);
        XslPageType xslPageType = null;
        if(req.getParameter("pdfbook") != null)
            xslPageType = getCurrentXSLPageType(tps, dsName, "pdfbook");
        else
            xslPageType = getCurrentXSLPageType(tps, dsName, "pdfs");
        String guid = xslPage.getGuid();
        if(guid == null || guid.trim().equals(""))
        {
            guid = EFGUniqueID.getID() + "";
            xslPage.setGuid(guid);
        }
        boolean found = false;
        XslPage currentPage = null;
        int z = -1;
        int j = 0;
        do
        {
            if(j >= xslPageType.getXslPageCount())
                break;
            currentPage = xslPageType.getXslPage(j);
            String currentGUIDName = currentPage.getGuid();
            String currentUniqueName = currentPage.getDisplayName();
            if(currentGUIDName.equalsIgnoreCase(guid))
            {
                found = true;
                z = j;
                break;
            }
            j++;
        } while(true);
        String templateName = null;
        boolean isUpdate = false;
        if(found)
        {
            if(currentPage.getDisplayName().equals(xslPage.getDisplayName()))
            {
                currentPage.setDisplayName(xslPage.getDisplayName());
                xslPageType.setXslPage(z, xslPage);
                templateName = xslPage.getDisplayName();
                isUpdate = true;
            } else
            {
                XslPage xs = cloneXslPage(xslPage);
                guid = xs.getGuid();
                templateName = xs.getDisplayName();
                xslPageType.addXslPage(xs);
            }
        } else
        {
            templateName = xslPage.getDisplayName();
            xslPageType.addXslPage(xslPage);
        }
        if(saveToDB(dsName, tps))
        {
            String efg2Req = req.getParameter("searchQuery");
            if(req.getParameter("pdfbook") != null)
            {
                String context = req.getContextPath();
                StringBuffer querySearch = new StringBuffer(context);
                querySearch.append("/");
                querySearch.append("books");
                querySearch.append("/");
                querySearch.append(dsName);
                querySearch.append(".pdf");
                insertIntoTemplateTable(querySearch.toString(), guid, dsName, displayName, templateName, isUpdate);
                handlePDFTests(req, res, xslPage);
            } else
            {
                StringBuffer querySearch = new StringBuffer();
                querySearch.append("/pdfDisplay?");
                querySearch.append(formatQuery(efg2Req).replaceAll("=xml", "=HTML"));
                if(guid != null)
                {
                    querySearch.append("&");
                    querySearch.append(EFGImportConstants.GUID);
                    querySearch.append("=");
                    querySearch.append(guid);
                }
                insertIntoTemplateTable(querySearch.toString(), guid, dsName, displayName, templateName, isUpdate);
                forwardPage(req, res, true, false);
            }
        } else
        {
            forwardPage(req, res, true, true);
        }
    }

    private void insertIntoTemplateTable(String key, String guid, String dsName, String displayName, String uniqueName, boolean isUpdate)
    {
        if(guid != null)
        {
            TemplateObject templateObject = new TemplateObject();
            EFGDisplayObject displayObject = new EFGDisplayObject();
            displayObject.setDisplayName(displayName);
            displayObject.setDatasourceName(dsName);
            templateObject.setTemplateName(uniqueName);
            templateObject.setGUID(guid);
            templateObject.setDisplayObject(displayObject);
            if(isUpdate)
                TemplateMapObjectHandler.updateTemplateDatabase(key, templateObject, null);
            else
                TemplateMapObjectHandler.add2TemplateMap(key, templateObject, null);
        }
    }

    private XslPage cloneXslPage(XslPage xslPage)
    {
        XslPage xPage = new XslPage();
        xPage.setDisplayName(xslPage.getDisplayName());
        xPage.setFileName(xslPage.getFileName());
        xPage.setGroups(xslPage.getGroups());
        xPage.setGuid(EFGUniqueID.getID() + "");
        xPage.setIsDefault(xslPage.getIsDefault());
        xPage.setJspName(xslPage.getJspName());
        return xPage;
    }

    private String formatQuery(String query)
    {
        query = query.replaceAll("_EFG_EQUALS_", "=");
        query = query.replaceAll("_EFG_AMP_", "&");
        return query;
    }

    private String findAuthors(String dsName)
    {
        return "";
    }

    private EFGDocument toEFGDoc(String searchqueryresultsxml)
        throws Exception
    {
        EFGDocument efgDoc = null;
        if(searchqueryresultsxml != null && !searchqueryresultsxml.equals(""))
            efgDoc = (EFGDocument)EFGDocument.unmarshalEFGDocument(new StringReader(searchqueryresultsxml.trim()));
        return efgDoc;
    }

    private XslPage getXSLPageParams(HttpServletRequest req)
    {
        String dsName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
        String uniqueName = req.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
        String guid = req.getParameter(EFGImportConstants.GUID);
        String jspName = req.getParameter(EFGImportConstants.JSP_NAME);
        String isDefault = req.getParameter("isDefault");
        return getXSLPageType(dsName, uniqueName, guid, jspName, isDefault);
    }

    private XslPage getXSLPageType(String dsName, String uniqueName, String guid, String jspName, String isDefault)
    {
        Boolean bool = new Boolean(isDefault);
        boolean defaultFile = bool.booleanValue();
        XslPage page = new XslPage();
        page.setFileName("xslName");
        if(guid == null || guid.trim().equals(""))
            guid = EFGUniqueID.getID() + "";
        page.setGuid(guid);
        if(uniqueName == null)
            uniqueName = "Some displayName " + guid;
        page.setDisplayName(uniqueName);
        if(jspName != null && !jspName.trim().equals(""))
            page.setJspName(jspName);
        page.setIsDefault(defaultFile);
        GroupsType groupsType = new GroupsType();
        page.setGroups(groupsType);
        return page;
    }

    private void forwardPage(HttpServletRequest req, HttpServletResponse res, boolean isSave, boolean isError)
        throws Exception
    {
        RequestDispatcher dispatcher = null;
        try
        {
            if(isSave)
            {
                if(isError)
                    dispatcher = getServletContext().getRequestDispatcher("/PDFFailurePage.html");
                else
                if(req.getParameter("pdfbook") != null)
                    dispatcher = getServletContext().getRequestDispatcher("/bookservlet");
                else
                    dispatcher = getServletContext().getRequestDispatcher("/PDFSuccessPage.html");
            } else
            if(isError)
                dispatcher = getServletContext().getRequestDispatcher("/templateJSP/error/TemplateError.jsp");
            else
            if(!isError)
                if(req.getParameter("pdfbook") != null)
                    dispatcher = getServletContext().getRequestDispatcher("/bookservlet");
                else
                    dispatcher = getServletContext().getRequestDispatcher("/efg2pdf.pdf");
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
            dispatcher = getServletContext().getRequestDispatcher("/templateJSP/error/TemplateError.jsp");
        }
        dispatcher.forward(req, res);
    }

    private static final long serialVersionUID = 1L;
}
