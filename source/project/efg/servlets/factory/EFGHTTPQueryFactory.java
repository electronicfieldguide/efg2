/**
 * $Id$
 * $Name$
 *
 * Copyright (c) 2006  University of Massachusetts Boston
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
package project.efg.servlets.factory;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import project.efg.servlets.efgInterface.EFGHTTPQuery;
import project.efg.servlets.rdb.DiGIRQuery;
import project.efg.servlets.rdb.SQLQuery;
import project.efg.servlets.rdb.SearchStrQuery;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class EFGHTTPQueryFactory {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGHTTPQueryFactory.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	private EFGHTTPQueryFactory() {}
	
	public synchronized static EFGHTTPQuery  
	getQueryInstance(HttpServletRequest req){
		String digirType = req
		.getParameter(EFGImportConstants.DIGIR);
		String searchStrType = req
		.getParameter(EFGImportConstants.SEARCHSTR);

		if(EFGImportConstants.DIGIR.equalsIgnoreCase(digirType)){
			log.debug("Return a DiGIR query instance");
			return new DiGIRQuery(req);
		}
		else if(EFGImportConstants.SEARCHSTR.equalsIgnoreCase(searchStrType)){
			log.debug("Return a Search String query instance");
			return new SearchStrQuery(req);
		}
		else{
			log.debug("Return a SQL query query instance");
			return new SQLQuery(req);
		}
	}

}
