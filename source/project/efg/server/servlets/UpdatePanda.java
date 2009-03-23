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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.util.factory.TemplateModelFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.TemplateModelHandler;

public class UpdatePanda extends HttpServlet
{

    public UpdatePanda()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        String realPath = getServletContext().getRealPath("/");
        realPath = File.separator + "backups" + File.separator;
        rdb_path = new File(realPath, "efg_rdb_table");
        if(!rdb_path.exists())
            rdb_path.mkdirs();
        glosssary_path = new File(realPath, "efg_glossary_table");
        if(!glosssary_path.exists())
            glosssary_path.mkdirs();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException, IOException
    {
        if(request.getParameter("reload") != null)
        {
            reloadDB(EFGImportConstants.EFG_RDB_TABLES, rdb_path);
            reloadDB(EFGImportConstants.EFG_GLOSSARY_TABLES, glosssary_path);
            reponse.getOutputStream().println("Database reloaded");
            reponse.getOutputStream().flush();
            reponse.getOutputStream().close();
        } else
        {
            saveOldTemplates(EFGImportConstants.EFG_RDB_TABLES, rdb_path);
            saveOldTemplates(EFGImportConstants.EFG_GLOSSARY_TABLES, glosssary_path);
            reponse.getOutputStream().println("Database saved");
            reponse.getOutputStream().flush();
            reponse.getOutputStream().close();
        }
    }

    protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
        throws ServletException, IOException
    {
        doGet(arg0, arg1);
    }

    private void saveOldTemplates(String tableName, File folderName)
    {
        try
        {
            TemplateModelHandler temp = TemplateModelFactory.createExportTemplateHandler();
            StringBuilder query = new StringBuilder("SELECT ");
            query.append("DS_DATA");
            query.append(" ,");
            query.append("TEMPLATE_OBJECT");
            query.append(" FROM ");
            query.append(tableName);
            SqlRowSet rs = temp.executeQueryForRowSet(query.toString());
            do
            {
                if(!rs.next())
                    break;
                String datasourceName = rs.getString("DS_DATA");
                Object binStream = rs.getObject("TEMPLATE_OBJECT");
                if(binStream != null)
                {
                    byte byteArray[] = (byte[])binStream;
                    ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
                    ObjectInputStream objS = new ObjectInputStream(stream);
                    Object obj = objS.readObject();
                    TaxonPageTemplates tps = (TaxonPageTemplates)obj;
                    File f = new File(folderName, datasourceName + ".xml");
                    FileWriter writer = new FileWriter(f);
                    tps.marshal(writer);
                    writer.flush();
                    writer.close();
                }
            } while(true);
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
        }
    }

    private void reloadDB(String tableName, File folderName)
    {
        TemplateModelHandler temp = TemplateModelFactory.createExportTemplateHandler();
        File f[] = folderName.listFiles();
        for(int i = 0; i < f.length; i++)
            try
            {
                FileReader reader = new FileReader(f[i]);
                TaxonPageTemplates tps = (TaxonPageTemplates)TaxonPageTemplates.unmarshalTaxonPageTemplates(reader);
                String dsName = tps.getTaxonPageTemplate(0).getDatasourceName();
                temp.updateTemplateObject(dsName, tableName, tps);
            }
            catch(Exception e)
            {
                LoggerUtilsServlet.logErrors(e);
            }

    }

    protected File rdb_path;
    protected File glosssary_path;
}
