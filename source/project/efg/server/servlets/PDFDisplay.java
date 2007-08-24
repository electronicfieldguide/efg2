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
package project.efg.server.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslFileNamesType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.EFGDocTemplate;





public class PDFDisplay extends SearchEngine
{
	/**
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 * @param xmlDoc
	 *            the result EFGDocument of the http query
	 * @throws ServletException 
	 */
    protected void present(HttpServletRequest req, HttpServletResponse res, Object obj)
        throws IOException, ServletException
    {
        String forwardPage = null;
        try
        {
            if(obj == null)
                throw new Exception("request could not be processed");
            EFGDocTemplate efgDocument = (EFGDocTemplate)obj;
            boolean isBook = false;
            if(req.getParameter("pdfbook") != null)
                isBook = true;
            String dsName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
            String guid = req.getParameter(EFGImportConstants.GUID);
            XslPage page = getXSLPage(efgDocument.getTaxonPageTemplates(), guid, dsName, isBook);
            if(page == null)
            {
                StringBuffer errorBuffer = new StringBuffer();
                errorBuffer.append("Could not find configuration for: ");
                errorBuffer.append(dsName);
                errorBuffer.append(" with guid: ");
                errorBuffer.append(guid);
                throw new Exception(errorBuffer.toString());
            }
            req.setAttribute("efgDoc", efgDocument.getEFGDocument());
            req.setAttribute("xslPage", page);
            if(isBook)
                forwardPage = "/bookservlet";
            else
                forwardPage = "/efg2pdf.pdf";
        }
        catch(Exception e)
        {
            LoggerUtilsServlet.logErrors(e);
            forwardPage = "/ErrorPage.jsp";
        }
        ServletContext sctxt = getServletConfig().getServletContext();
        RequestDispatcher rd = sctxt.getRequestDispatcher(forwardPage);
        rd.forward(req, res);
    }

    private XslPage getXSLPage(TaxonPageTemplates tps, String guid, String dsName, boolean isBook)
    {
        XslPageType xslPageType = getCurrentXSLPageType(tps, dsName, isBook);
        XslPage page = null;
        if(xslPageType != null)
        {
            int j = 0;
            do
            {
                if(j >= xslPageType.getXslPageCount())
                    break;
                XslPage currentPage = xslPageType.getXslPage(j);
                String currentGUIDName = currentPage.getGuid();
                String currentUniqueName = currentPage.getDisplayName();
                if(currentGUIDName.equalsIgnoreCase(guid))
                {
                    page = currentPage;
                    break;
                }
                j++;
            } while(true);
        }
        return page;
    }

    private XslPageType getCurrentXSLPageType(TaxonPageTemplates tps, String dsName, boolean isBook)
    {
        XslPageType xslPageType = null;
        try
        {
            int i = 0;
            do
            {
                if(i >= tps.getTaxonPageTemplateCount())
                    break;
                TaxonPageTemplateType tp = tps.getTaxonPageTemplate(i);
                String ds = tp.getDatasourceName();
                if(ds.equalsIgnoreCase(dsName.trim()))
                {
                    XslFileNamesType xslFileNames = tp.getXSLFileNames();
                    if(isBook)
                        xslPageType = xslFileNames.getXslBookPages();
                    else
                        xslPageType = xslFileNames.getXslPdfPages();
                    break;
                }
                i++;
            } while(true);
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
            return null;
        }
        return xslPageType;
    }
}
