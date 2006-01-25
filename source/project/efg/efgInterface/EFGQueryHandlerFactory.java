/**
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu, Kimmy lin
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

//import project.efg.db.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * This interface when implemented sets the EFGQueryHandlerFactory
 * that produce EFGQueryHandler.
 *
 * @see #EFGQueryHandler
 */
public class EFGQueryHandlerFactory 
{
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(EFGQueryHandlerFactory.class); 
	}
	catch(Exception ee){
	}
    }
    /**
     * Hands out an EFGQueryHandler to handle the query.
     *
     * @return the EFGQueryHandler that handles the query
     */
    public EFGQueryHandler getEFGQueryHandler(String name){
    try { 
      Class cl = Class.forName(name);
      return (EFGQueryHandler)cl.newInstance();
    }
    catch( ClassNotFoundException cle){
	LoggerUtilsServlet.logErrors(cle);
    }
    catch(InstantiationException ie){
	LoggerUtilsServlet.logErrors(ie);
    }
    catch(Exception e){
	LoggerUtilsServlet.logErrors(e);
    }
    return null;
  }
}

//$Log$
//Revision 1.1  2006/01/25 21:03:48  kasiedu
//Initial revision
//
//Revision 1.1.1.1  2003/10/17 17:03:09  kimmylin
//no message
//
//Revision 1.2  2003/08/20 18:45:42  kimmylin
//no message
//
