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



import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;





import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.efg.efgDocument.EFGDocument;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.interfaces.EFGImagesConstants;
import project.efg.util.pdf.factory.EFGPDFSpringFactory;
import project.efg.util.pdf.interfaces.EFG2PDFInterface;

public class EFG2PDFConverter extends EFGServlet
{

    private static final long serialVersionUID = 1L;
    private File mediaResourceDirectory;



	
	/**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */

    public void init(ServletConfig config)
        throws ServletException
    {
		super.init(config);
		String realPath = getServletContext().getRealPath("/");
		this.mediaResourceDirectory = new File(realPath,EFGImagesConstants.EFG_IMAGES_DIR); 
    }
	/**
	 * Handles an HTTP GET request - Based most likely on a clicked link.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doPost(request, response);
    }
	/**
	 * 
	 * @param session
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
    private void doPDF(HttpServletRequest req, HttpServletResponse response)
        throws ServletException, IOException
    {
        ServletOutputStream out = null;
        try
        {
            EFGDocument efgDoc = (EFGDocument)req.getAttribute("efgDoc");
            XslPage xslPage = (XslPage)req.getAttribute("xslPage");
            String authors = (String)req.getAttribute("authors");
            if(efgDoc == null || xslPage == null)
                throw new Exception("Error in Generating pdf. XslPage and efgDoc request attributes must be present!!");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/pdf");
            out = response.getOutputStream();
            EFG2PDFInterface efg2pdf = EFGPDFSpringFactory.newEFG2PDFInstance();
            efg2pdf.writePdfToStream(efgDoc, xslPage, out, mediaResourceDirectory, authors);
            out.flush();
            out.close();
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
            if(out == null)
                out = response.getOutputStream();
            out.println(ee.getMessage());
            out.flush();
            out.close();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            doPDF(req, res);
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
            sendCompleteResponse(res, "Error", isError());
        }
    }

    private String isError()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("An error occured during PDF processing. Consult the efg team for help");
        return buffer.toString();
    }
}
