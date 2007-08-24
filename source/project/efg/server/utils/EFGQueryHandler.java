/**
 * $Id: EFGQueryHandler.java,v 1.1.1.1 2007/08/01 19:11:25 kasiedu Exp $
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

package project.efg.server.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import project.efg.server.factory.EFGFactory;
import project.efg.server.interfaces.EFGHTTPQuery;
import project.efg.util.utils.EFGDocTemplate;

public class EFGQueryHandler
{
	

    /**
     * Interpret the passed in parameters, find matching taxa, and build
     * an appropriate response page.
     *
     * @param req the servlet request object
     * @param res the servlet response object
     * @param sc the ServletConfig object that holds that init parameters
     * @return a jdom Document that conforms to our schema EFGDocument.xsd
     */
    public EFGDocTemplate buildResult(HttpServletRequest req, HttpServletResponse res){
    	
    	EFGHTTPQuery query = this.buildQueryObject(req);
    	if(query == null){
    		return null;
    	}
		return query.getEFGDocument();
    }
    public EFGHTTPQuery buildQueryObject(HttpServletRequest req){
    	return EFGFactory.getEFGHTTPQueryInstance(req);
    }
}

//$Log: EFGQueryHandler.java,v $
//Revision 1.1.1.1  2007/08/01 19:11:25  kasiedu
//efg2.1.1.0 version of efg2
//
//Revision 1.2  2006/12/08 03:51:01  kasiedu
//no message
//
//Revision 1.1.2.2  2006/08/09 18:55:25  kasiedu
//latest code confimrs to what exists on Panda
//
//Revision 1.1.2.1  2006/06/08 13:27:43  kasiedu
//New files
//
//Revision 1.1.1.1  2006/01/25 21:03:48  kasiedu
//Release for Costa rica
//
//Revision 1.1.1.1  2003/10/17 17:03:09  kimmylin
//no message
//
//Revision 1.6  2003/08/28 23:13:46  kasiedu
//*** empty log message ***
//
//Revision 1.5  2003/08/28 16:33:47  kimmylin
//no message
//
//Revision 1.4  2003/08/20 18:45:42  kimmylin
//no message
//
