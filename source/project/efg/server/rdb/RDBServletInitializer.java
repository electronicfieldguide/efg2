/**
 * $Id: RDBServletInitializer.java,v 1.1.1.1 2007/08/01 19:11:23 kasiedu Exp $
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

package project.efg.server.rdb;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import project.efg.server.interfaces.EFGServletInitializerInterface;
import project.efg.util.utils.EFGRDBUtils;

/**
 * This class is used to intialize a relational database that will be used in
 * this application.
 */
public class RDBServletInitializer implements EFGServletInitializerInterface {
	private  DataSource ds;
	
	/**
	 * Constructor.
	 * 
	 * @param sctx
	 *            the ServletContext object of EFG servlets
	 */
	public RDBServletInitializer() {
		init();
	}
	private  synchronized void closeRDBContext() throws Exception {
		Class cl = Class.forName("org.apache.commons.dbcp.BasicDataSource");
		
		if (cl.isInstance(this.ds)) {
			this.ds = null;
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
		Context ctx = new InitialContext();
		Context env = (Context) ctx.lookup("java:/comp/env");
		if (env == null) {
			throw new Exception("No Context found for 'java:/comp/env' ");
		}
		String resourceName = (String) env.lookup("resourceName");
		
		//get the resource name using the Utilities method
		this.ds = (DataSource)env.lookup(resourceName);
		EFGRDBUtils.setDatasource(this.ds);
	}
	/**
	 * All intializationas are done here.
	 */
	private void init() {
		try {
			this.initRDBContext();
			//servletContext.log("initialization parameters read in EFGContextListener..");
		} catch (Exception e) { // log any errors that may have occured
			//servletContext.log(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Called when the current context is about to be destroyed.
	 */
	public void close() {
		try {
			this.closeRDBContext();
			//servletContext
				//	.log("initialization parameters destroyed in EFGContextListener..");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			//servletContext
				//	.log("Exception occured in EFGContextListener.contextDestroyed:  "
					//		+ e.getMessage());
		
		}
	}

}
