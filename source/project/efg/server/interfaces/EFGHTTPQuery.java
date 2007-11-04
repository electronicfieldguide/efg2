/**
 * $Id$
 * $Name:  $
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

package project.efg.server.interfaces;

import java.util.Collection;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import project.efg.efgDocument.DatasourceType;
import project.efg.efgDocument.DatasourcesType;
import project.efg.efgDocument.EFGDocument;
import project.efg.efgDocument.TaxonEntries;
import project.efg.server.servlets.EFGContextListener;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.RegularExpresionConstants;
import project.efg.util.utils.EFGDocTemplate;
import project.efg.util.utils.TemplateMapObjectHandler;

public abstract class EFGHTTPQuery
{
    private EFGDocTemplate efgDocument;
    protected String datasourceName;
    protected String displayName;
    protected String metadatasourceName;
    protected String mainTableName;
    protected static Collection specialParams;
    static Logger log = null;

    static 
    {
        try
        {
            addSpecialParams();
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
        }
    }
	/**
	 * Add special parameters to a list so that special parameters can be
	 * excluded when building the query.
	 * TODO: This should be read from the servlet context
	 */
    protected static void addSpecialParams()
    {
        specialParams = EFGContextListener.getSpecialParams();
    }
	/**
	 * Replace % sign in the string with a space character
	 * @param toReplace
	 * @return replaced string
	 */
	public String replacePercent(String toReplace){
		
			return toReplace.replaceAll(RegularExpresionConstants.HTTP_QUERY_PERCENT_SIGN,
					RegularExpresionConstants.REPLACEMENT_PERCENT_SIGN);
		
	}


    public void setMainDataTableName(String mainTableName)
    {
        this.mainTableName = mainTableName;
    }

    public String getMainTableName()
    {
        return mainTableName;
    }
    /**
     * Create a query object from the request
     * @param req
     */
	public EFGHTTPQuery(HttpServletRequest req){
		
		String allTableName= req.getParameter(EFGImportConstants.ALL_TABLE_NAME);
		if(allTableName != null && !allTableName.trim().equals("")){
			this.setMainDataTableName(allTableName);
		}
		else {
			this.setMainDataTableName(EFGImportConstants.EFG_RDB_TABLES);
		}
		 String query = this.buildQuery(req);
		 //	call cache here
		 this.efgDocument = new EFGDocTemplate();
		 EFGDocument efgDoc = new EFGDocument();
		 //log.debug("Query: " + query);
		 TaxonEntries entries = this.executeQuery(query);
		
		 if(entries != null){
			 efgDoc.setTaxonEntries(entries);
		 }
		 this.efgDocument.setEFGDocument(efgDoc);
		 this.addDatasources();
		 this.addTemplates();
		
	}
	/**
	 * 
	 */
	private void addTemplates() {
		
		//datasourcename, templateName
		TaxonPageTemplates taxonPageTemplates = 
			TemplateMapObjectHandler.getTemplateFromDB(null, 
				this.displayName, this.datasourceName, this.mainTableName);
		//set the only template of interest
		this.efgDocument.setTaxonPageTemplates(taxonPageTemplates);
	}


    public EFGDocTemplate getEFGDocument()
    {
        return efgDocument;
    }
    /*
     * Template methods starts here
     * 
     */
	/**
	 * 
	 * @param req - The request to parse into the appropriate query
	 * @return a query built for the appropriate system
	 */
    protected abstract String buildQuery(HttpServletRequest httpservletrequest);
	/**
	 * @return an EFGDocument holding the results of executing the query
	 */
    protected abstract TaxonEntries executeQuery(String s);

	/**
	 * 
	 * @return the EFGDocument object built from the query
	 */
	protected boolean matchNumber(String states) {
		try {
			Matcher matcher = RegularExpresionConstants.matchNumberPattern.matcher(states);
			return matcher.find();
		} catch (Exception vvv) {
		}
		return false;
	}
	private void addDatasources(){
		 if(this.datasourceName != null){
			 DatasourcesType datasourceTypes = new DatasourcesType();
			 DatasourceType datasourceType = new DatasourceType();
			 datasourceType.setName(this.datasourceName);
			 datasourceType.setEfgKey(1);
			 datasourceTypes.addDatasource(datasourceType);
			 this.efgDocument.getEFGDocument().setDatasources(datasourceTypes);
		 }
	}


}
