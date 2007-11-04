/**
 * $Id$
 *
 * Copyright (c) 2005  University of Massachusetts Boston
 *
 * Authors: Jacob Asiedu<kasiedu@cs.umb.edu>
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

// import java specific packages
import java.sql.SQLException;

import org.apache.log4j.Logger;

import project.efg.util.utils.EFGUtils;

/**
 * Logs errors in servlet
 * 
 * @author <a HREF="mailto:kasiedu@cs.umb.edu">Jacob K. Asiedu</a>
 */

public class LoggerUtilsServlet {
	private static Logger log = null;
	static {
		try {
			log = Logger.getLogger(project.efg.server.utils.LoggerUtilsServlet.class);
			EFGUtils.log("Logger initiliazed for servlet in servlet context");
		} catch (Exception ee) {
			EFGUtils.log("Logger not initiliazed in servlet context");
		}
	}

	protected LoggerUtilsServlet() {

	}

	public synchronized static void logErrors(Exception ee) {
		StackTraceElement[] ste = ee.getStackTrace();
		StringBuffer buff = new StringBuffer();
		buff.append(ee.getMessage());
		buff.append("\n");
		for (int ii = 0; ii < ste.length; ii++) {
			buff.append(ste[ii].toString());
			buff.append("\n");
		}
		log.error(buff.toString());
	}

	/**
	 * Display SQL error messages within a SQLException.
	 * 
	 * @param ex
	 *            the exception to display
	 */
	public synchronized static void printSQLErrors(SQLException ex) {
		if (ex != null) {
			logErrors(ex);
		}
	/*	while (ex != null) {
			log.error("SQLState: " + ex.getSQLState());
			log.error("Message:  " + ex.getMessage());
			log.error("Vendor:   " + ex.getErrorCode());
			ex = ex.getNextException();
			
		}*/
	}
}
