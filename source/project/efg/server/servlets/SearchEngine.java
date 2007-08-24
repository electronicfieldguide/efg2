/**
 * $Id: SearchEngine.java,v 1.1.1.1 2007/08/01 19:11:24 kasiedu Exp $
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
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import project.efg.server.factory.EFGFactory;
import project.efg.server.interfaces.EFGHTTPQuery;
import project.efg.server.interfaces.ResponseObject;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.EFGDocTemplate;
public class SearchEngine extends EFGSearchServletInterface
    implements EFGImportConstants
{

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            String displayFormat = req.getParameter(EFGImportConstants.DISPLAY_FORMAT);
            EFGHTTPQuery query = EFGFactory.getQueryInstance(req);
            if("xml".equalsIgnoreCase(displayFormat))
            {
                presentXML(req, res, query.getEFGDocument());
            } else
            {
                req.setAttribute("ALL_TABLE_NAME", query.getMainTableName());
                present(req, res, query.getEFGDocument());
            }
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<h3>There are no results matching your search criteria.</h3>");
            out.flush();
            res.flushBuffer();
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        doPost(req, res);
    }

    protected void presentXML(HttpServletRequest req, HttpServletResponse res, Object obj)
        throws IOException
    {
        if(obj == null)
            return;
        try
        {
            EFGDocTemplate xmlDoc = (EFGDocTemplate)obj;
            req.setAttribute("resultSet", xmlDoc.getEFGDocument());
            ServletContext sctxt = getServletConfig().getServletContext();
            RequestDispatcher rd = sctxt.getRequestDispatcher("/presentXML.jsp");
            rd.forward(req, res);
        }
        catch(Exception ex)
        {
            LoggerUtilsServlet.logErrors(ex);
            EFGContextListener.presentError(getClass().getName(), ex.getMessage(), res);
        }
    }

    protected void present(HttpServletRequest req, HttpServletResponse res, Object obj)
        throws IOException, ServletException
    {
        if(obj == null)
            return;
        EFGDocTemplate efgDocument = (EFGDocTemplate)obj;
        ResponseObject resObject = null;
        try
        {
            resObject = EFGFactory.getResponseObject(req, efgDocument, realPath);
        }
        catch(Exception e)
        {
            efgDocument = null;
            resObject = EFGFactory.getResponseObject(req, efgDocument, realPath);
        }
        ServletContext sctxt = getServletConfig().getServletContext();
        RequestDispatcher rd = sctxt.getRequestDispatcher(resObject.getForwardPage());
        rd.forward(req, res);
    }

    static final long serialVersionUID = 1L;
    static Logger log = null;

}
