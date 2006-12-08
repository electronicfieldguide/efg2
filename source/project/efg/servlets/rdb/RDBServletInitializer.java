/**
 * $Id$
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

package project.efg.servlets.rdb;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import project.efg.servlets.efgInterface.EFGServletInitializerInterface;

/**
 * This class is used to intialize a relational database that will be used in
 * this application.
 */
public class RDBServletInitializer implements EFGServletInitializerInterface {
	private ServletContext servletContext;
	
	private Context ctx;
	private  Context env;
	private String resourceName;
	private  DataSource ds;
	
	/**
	 * Constructor.
	 * 
	 * @param sctx
	 *            the ServletContext object of EFG servlets
	 */
	public RDBServletInitializer(ServletContext sctx) {
		this.servletContext = sctx;
		init();
	}
	private  synchronized void closeRDBContext() throws Exception {
		this.servletContext.log("Cleaning up Context Resources");
		Class cl = Class.forName("org.apache.commons.dbcp.BasicDataSource");
		
		if (cl.isInstance(ds)) {
			ds = null;
		}
	}
	/**
	 * Close Connection object passed in. It must be called after a connection 
	 * is no longer used.
	 *
	 * @param conn the Connection to be closed
	 * @throws Exception
	 */
	public void closeConnections(Connection conn) throws Exception {
		if (conn != null) {
			conn.close();
		}
	}
	/**
	 * Initializes the jndi context to our Database. Looks up the environment, 
	 * reads the resource name from JNDI and makes the DataSource available for use.
	 * This method must be called after setting the resourceName.
	 *
	 * @see javax.naming.Context
	 * @throws an Exception
	 */
	private void initRDBContext() throws Exception//USE SPRING
	{
		this.ctx = new InitialContext();
		this.env = (Context) ctx.lookup("java:/comp/env");
		if (this.env == null) {
			throw new Exception("No Context found for 'java:/comp/env' ");
		}
		this.resourceName = (String) env.lookup("resourceName");
		
		//get the resource name using the Utilities method
		this.ds = (DataSource)this.env.lookup(this.resourceName);
		EFGRDBUtils.setDatasource(this.ds);
	}
	/**
	 * All intializationas are done here.
	 */
	public void init() {
		try {
			this.initRDBContext();
			servletContext.log("initialization parameters read in EFGContextListener..");
		} catch (Exception e) { // log any errors that may have occured
			servletContext.log(e.getMessage());
		}
	}

	/**
	 * Called when the current context is about to be destroyed.
	 */
	public void contextDestroyed() {
		try {
			this.closeRDBContext();
			servletContext
					.log("initialization parameters destroyed in EFGContextListener..");
		} catch (Exception e) {
			servletContext
					.log("Exception occured in EFGContextListener.contextDestroyed:  "
							+ e.getMessage());
		
		}
	}

}

// $Log$
// Revision 1.2  2006/12/08 03:51:02  kasiedu
// no message
//
// Revision 1.1.2.2  2006/08/09 18:55:25  kasiedu
// latest code confimrs to what exists on Panda
//
// Revision 1.1.2.1  2006/06/08 13:27:44  kasiedu
// New files
//
// Revision 1.1.1.1 2006/01/25 21:03:42 kasiedu
// Release for Costa rica
//
// Revision 1.3 2005/04/27 19:41:21 ram
// Recommit all of ram's allegedly working copy of efgNEW...
//
// Revision 1.1.1.1 2003/10/17 17:03:05 kimmylin
// no message
//
// Revision 1.5 2003/08/20 18:45:42 kimmylin
// no message
//
