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
package project.efg.server.factory;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import project.efg.efgDocument.EFGDocument;
import project.efg.server.impl.XSLTResponseObject;
import project.efg.server.interfaces.EFGHTTPQuery;
import project.efg.server.interfaces.ResponseObject;
import project.efg.server.rdb.DiGIRQuery;
import project.efg.server.rdb.SQLQuery;
import project.efg.server.rdb.SearchStrQuery;
import project.efg.server.utils.ErrorResponseObject;
import project.efg.server.utils.NoMatchResponseObject;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.EFGDocTemplate;



public class EFGFactory
{

    public EFGFactory()
    {
    }
    /**
     * 
     * @param directory
     * @param itemType
     * @return
     */
    public static File getDiskFileItemFactoryInstance(String directory, String itemType)
    {
		 String mutex="";
		 synchronized (mutex) {
			 File file = null;
			 
			 if((itemType != null ) && (!itemType.trim().equals(""))){
				 //log.debug("It is a js");
				 file = new File(directory + File.separator + 
						 itemType);
			 }
			
			 //log.debug("ItemType: " + itemType);
			 if(file == null){
				 file = new File(directory);
			 }
			return file;
		}

    }
    /**
     * 
     * @param req
     * @return
     */
    public static EFGHTTPQuery getQueryInstance(HttpServletRequest req)
    {
    	String mutex = "";
    	synchronized (mutex) {
    		String digirType = req
    		.getParameter(EFGImportConstants.DIGIR);
    		String searchStrType = req
    		.getParameter(EFGImportConstants.SEARCHSTR);
    	
    		if((digirType != null) && (!digirType.trim().equals(""))){
    			return new DiGIRQuery(req);
    		}
    		if((searchStrType != null) && (!searchStrType.trim().equals(""))){
    			//log.debug("Return a Search String query instance");
    			return new SearchStrQuery(req);
    		}
    		//log.debug("Return a SQL query query instance");
    		return new SQLQuery(req);
    	}
     }
    /**
     * 
     * @param req
     * @return
     */
    public static synchronized EFGHTTPQuery getEFGHTTPQueryInstance(HttpServletRequest req)
    {
    	String mutex = "";
    	synchronized (mutex) {
    		String request = req.getParameter(EFGImportConstants.REQUEST_TYPE);
    		
    		if (EFGImportConstants.DIGIR.equalsIgnoreCase(request)) {//There is a DiGIR request
    			//log.debug("Digir request: " + request);
    			return new DiGIRQuery(req);
    			
    		}
    		else if(EFGImportConstants.SEARCHSTR.equalsIgnoreCase(request)){
    			//log.debug("Search str request: " + request);
    			return new SearchStrQuery(req);
    		}
    		else{
    			//log.debug("Plain old query");
    			return new SQLQuery(req);
    		}
    	}
    }
    /**
     * 
     * @param req
     * @param efgDocument
     * @param realPath
     * @return
     */
    public static ResponseObject getResponseObject(HttpServletRequest req, 
    		EFGDocTemplate efgDocument, 
    		String realPath)
    {
		int taxonSize = EFGFactory.getTaxonSize(efgDocument.getEFGDocument());
		if(taxonSize== -1){//forward to Error page
			//log.debug("Forward to error page");
			return new ErrorResponseObject(req,efgDocument,realPath);
		}
		else if(taxonSize == 0){//forward to no results page
			//log.debug("Forward to no match page");
			return new NoMatchResponseObject(req,efgDocument,realPath);
		}
		else{//forward to XSLTResponseObject
			//log.debug("Forward to response page");
			return new XSLTResponseObject(req,efgDocument,realPath);
		}
    }
    /**
     * Get the number of taxa
     * @param efgDocument
     * @return
     */
    private static int getTaxonSize(EFGDocument efgDocument)
    {
    	String mutex = "";
    	synchronized (mutex) {
            int taxonSize = -1;
            if(efgDocument != null)
                try
                {
                    taxonSize = efgDocument.getTaxonEntries().getTaxonEntryCount();
                }
                catch(Exception ee)
                {
                    taxonSize = 0;
                }
            return taxonSize;

		}
    }
}
