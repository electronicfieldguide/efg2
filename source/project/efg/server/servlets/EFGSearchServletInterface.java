/**
 * $Id$
 * $Name:  $
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class EFGSearchServletInterface extends HttpServlet
{
    protected String realPath;
    protected static String applyXSLServlet;

    public EFGSearchServletInterface()
    {
    }
    /**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */
    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        realPath = getServletContext().getRealPath("/");
        if(config.getInitParameter("applyXSLServlet") == null)
            throw new ServletException("applyXSLServlet servlet parameter required.");
        applyXSLServlet = config.getInitParameter("applyXSLServlet");
        if(config.getInitParameter("fopServlet") == null)
            throw new ServletException("fopServlet servlet parameter required.");
     }
	/**
	 * This method is called when the servlet is shutdown. Closes the database
	 * then disconnects the servlet's thread from its Session. It's complement
	 * is the init method.
	 */
    public void destroy()
    {
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
	public abstract void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException;
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
	protected abstract void present(HttpServletRequest req, HttpServletResponse res, Object obj) throws Exception ;
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
    protected abstract void presentXML(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj)
        throws IOException;
    /**
	 * Handles an HTTP GET request - Based most likely on a clicked link.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        doPost(req, res);
    }

}
