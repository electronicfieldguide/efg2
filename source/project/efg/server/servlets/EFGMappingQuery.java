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
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.efg.server.interfaces.EFGHTTPQuery;
import project.efg.server.rdb.MapQuery;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.util.interfaces.EFGImportConstants;

public class EFGMappingQuery extends SearchEngine
{

    public EFGMappingQuery()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
    }

    public void destroy()
    {
        super.destroy();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            String displayFormat = req.getParameter(EFGImportConstants.DISPLAY_FORMAT);
            EFGHTTPQuery query = new MapQuery(req);
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
            out.println("<H3>There are no results matching your search criteria.</H3>");
            out.flush();
            res.flushBuffer();
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        doPost(req, res);
    }

    private static final long serialVersionUID = 1L;
}
