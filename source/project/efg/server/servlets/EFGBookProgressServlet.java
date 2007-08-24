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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.*;
import javax.servlet.http.*;
import project.efg.efgDocument.EFGDocument;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.pdf.bookmaker.BookMaker;
import project.efg.util.pdf.bookmaker.ConfigHandler;

public class EFGBookProgressServlet extends HttpServlet
{
    public class BookMakerThread
        implements Runnable
    {
    	/**
    	 * 
    	 */
        public void run()
        {
            OutputStream out = null;
            ConfigHandler config = new ConfigHandler(xslPage);
            BookMaker bookmaker = new BookMaker(efgDoc, config, mediaResourceDirectory);
            try {
				out = new FileOutputStream(new File(EFGBookProgressServlet.pdfFolderName, bookName));
			} catch (FileNotFoundException e) {
				// FIXME Auto-generated catch block
				e.printStackTrace();
			}
            bookmaker.writeDocument(out, false);
            try
            {
                out.flush();
                out.close();
            }
            catch(Exception ee) { 
           
            	LoggerUtilsServlet.logErrors(ee);
            	try
            	{
            		out.flush();
            		out.close();
            	}
            	catch(Exception eee) { }
            }         
        }
        /**
         * 
         * @return
         */
        public String getBookName()
        {
            return "/" + EFGBookProgressServlet.pdfFolderName.getName() + "/" + bookName;
        }

        EFGDocument efgDoc;
        XslPage xslPage;
        URL mediaResourceDirectory;
        String bookName;

        public BookMakerThread(EFGDocument efgDoc, XslPage xslPage, URL mediaResourceDirectory, String bookName)
        {
            this.efgDoc = null;
            this.xslPage = null;
            this.mediaResourceDirectory = null;
            this.bookName = null;
            this.efgDoc = efgDoc;
            this.xslPage = xslPage;
            this.mediaResourceDirectory = mediaResourceDirectory;
            this.bookName = bookName + ".pdf";
        }
    }


    public EFGBookProgressServlet()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        String realPath = getServletContext().getRealPath("/");
        File mediaResource = new File(realPath + File.separator + "EFGImages");
        try
        {
            mediaResourceDirectory = mediaResource.toURL();
        }
        catch(MalformedURLException e) { }
        pdfFolderName = new File(realPath, "books");
        if(!pdfFolderName.exists())
            pdfFolderName.mkdirs();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME);
        String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME);
        EFGDocument efgDoc = (EFGDocument)request.getAttribute("efgDoc");
        XslPage xslPage = (XslPage)request.getAttribute("xslPage");
        String isPreview = (String)request.getAttribute("isPreview");
        if(isPreview == null)
            runThread(request, response, efgDoc, xslPage, dsName);
        else
            runNoThread(request, response, efgDoc, xslPage, dsName);
    }

    private void runThread(HttpServletRequest request, HttpServletResponse response, EFGDocument efgDoc, XslPage xslPage, String dsName)
        throws IOException, ServletException
    {
        BookMakerThread pdf = new BookMakerThread(efgDoc, xslPage, mediaResourceDirectory, dsName);
        Thread t = new Thread(pdf);
        t.start();
        response.setContentType("text/html");
        isFinished(response.getOutputStream(), request.getContextPath() + pdf.getBookName());
    }

    private void runNoThread(HttpServletRequest request, HttpServletResponse response, EFGDocument efgDoc, XslPage xslPage, String bookName)
        throws ServletException, IOException
    {
        ServletOutputStream out = null;
        try
        {
            bookName = bookName + ".pdf";
            ConfigHandler config = new ConfigHandler(xslPage);
            BookMaker bookmaker = new BookMaker(efgDoc, config, mediaResourceDirectory);
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/pdf");
            out = response.getOutputStream();
            bookmaker.writeDocument(out, true);
            out.flush();
            out.close();
        }
        catch(Exception e)
        {
            LoggerUtilsServlet.logErrors(e);
            if(out == null)
                out = response.getOutputStream();
            out.println(e.getMessage());
            out.flush();
            out.close();
        }
    }

    private void isFinished(ServletOutputStream stream, String url)
    {
        try
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<div class='makingbookmsg'>Your book is now being created. Please wait <span class='makingbookmsg'>5 minutes</span> and then ");
            buffer.append("<a  class='makingbookmsg' title='Download Book'");
            buffer.append(" href=\"");
            buffer.append(url);
            buffer.append("\"> click here</a> to download.");
            buffer.append("<p class='makingbookmsg'>If your pdf reader returns an error message indicating the file is damaged,it is typically because you downloaded a partial file - please wait and try again.</p></div>");
            stream.print(buffer.toString());
        }
        catch(Exception ee)
        {
            try
            {
                isError(stream);
            }
            catch(IOException e)
            {
                LoggerUtilsServlet.logErrors(e);
            }
        }
    }

    private void isError(ServletOutputStream stream)
        throws IOException
    {
        stream.print("<html>\n\t<head>\n\t\t<title>Error</title>\n\t</head>\n\t<body>");
        stream.print("An error occured.\n\t</body>\n</html>");
    }

    private static final long serialVersionUID = 0x570bbb22149c5b7bL;
    private static URL mediaResourceDirectory;
    private static File pdfFolderName;

}
