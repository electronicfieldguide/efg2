/*
 * $Id$
 *
 * Copyright (c) 2007  University of Massachusetts Boston
 *
 * Authors: Jacob K. Asiedu
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

import javax.servlet.http.HttpServletRequest;

import project.efg.server.factory.EFGSpringFactory;
import project.efg.server.utils.XSLTObjectInterface;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.EFGDocTemplate;

public abstract class ResponseObject
{

	

	
	 protected HttpServletRequest req;
	    protected XSLTObjectInterface xslt;
	    protected EFGDocTemplate efgDocument;
	    private int taxonSize;
	    protected String realPath;
	    protected String forwardPage;
	    private String searchType;
	    protected String mainTableName;

    public ResponseObject(HttpServletRequest req, EFGDocTemplate efgDocument, String realPath)
    {
        taxonSize = -1;
		this.req = req;
		this.mainTableName = this.req.getParameter(EFGImportConstants.ALL_TABLE_NAME);
		this.efgDocument = efgDocument;
		
		this.realPath = realPath;
		this.searchType = req.getParameter(EFGImportConstants.SEARCHTYPE);
		this.createForwardPage();
		

    }
	public XSLTObjectInterface getXSLTObject(){
		XSLTObjectInterface  xslto = EFGSpringFactory.getXSLTObjectInstance(this.taxonSize, this.searchType);
		xslto.setMainDataTableName(this.mainTableName);	
		return xslto;
	 
	}
	protected int getTaxonSize(){
		if(this.efgDocument != null){
			this.taxonSize = efgDocument.getEFGDocument().getTaxonEntries().getTaxonEntryCount();
		}
		return taxonSize;
	}
	protected abstract void createForwardPage();
	
	public String getForwardPage(){
		return this.forwardPage;
	}

}
