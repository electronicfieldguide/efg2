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

package project.efg.efgInterface;

import project.efg.util.*;
import javax.servlet.*;

/**
 * This class is used to intialize a relational database that will be used in this application.
 */
public class RDBServletInitializer implements EFGServletInitializer 
{
    private ServletContext servletContext;
    
    /**
     * Constructor.
     * 
     * @param sctx the ServletContext object of EFG servlets
     */
    public RDBServletInitializer(ServletContext sctx)
    {
	this.servletContext = sctx;
	init();
    }
    
    /**
     * All intializationas are done here.
     */
    public void init()
    {
	try {
	    EFGRDBUtils.initRDBContext();
	    EFGRDBUtils.retrieveAllDataSourceNames();
	    servletContext.log("initialization parameters read in EFGContextListener.." );
	} catch (Exception e) { //log any errors that may have occured
	    servletContext.log ("Exception occured in EFGContextListener.contextInitialized:  " + e.getMessage());
	    e.printStackTrace();
	}
    }
    
    /**
     * Called when the current context is about to be destroyed.
     */
  public void contextDestroyed() {
    try{
      EFGRDBUtils.closeRDBContext();
      servletContext.log("initialization parameters destroyed in EFGContextListener.." );
    }
    catch(Exception e){
    servletContext.log ("Exception occured in EFGContextListener.contextDestroyed:  " + e.getMessage());
	    e.printStackTrace();
    }
  }

}

//$Log$
//Revision 1.1  2006/01/25 21:03:42  kasiedu
//Initial revision
//
//Revision 1.3  2005/04/27 19:41:21  ram
//Recommit all of ram's allegedly working copy of efgNEW...
//
//Revision 1.1.1.1  2003/10/17 17:03:05  kimmylin
//no message
//
//Revision 1.5  2003/08/20 18:45:42  kimmylin
//no message
//
