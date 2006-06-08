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

package project.efg.servlets.efgServletsUtil;

import javax.servlet.ServletContext;

import project.efg.servlets.efgInterface.EFGServletInitializerInterface;
import project.efg.servlets.rdb.RDBServletInitializer;

/**
 * This class provide an EFGServletInitializer with the ServletContext object of
 * EFG servlets.
 */
public class EFGServletInitializerInstance {
	private static EFGServletInitializerInterface servInit;

	/**
	 * Create a RDBServletInitializer with the ServletContext object of EFG
	 * servlets.
	 * 
	 * @return an EFGServletInitializer
	 */
	public static synchronized EFGServletInitializerInterface getInstance(ServletContext sctx) {
		if(servInit == null){
			servInit = new RDBServletInitializer(sctx);
		}
		return servInit;
	}
}

// $Log$
// Revision 1.1.2.1  2006/06/08 13:27:43  kasiedu
// New files
//
// Revision 1.1.1.1 2006/01/25 21:03:42 kasiedu
// Release for Costa rica
//
// Revision 1.1.1.1 2003/10/17 17:03:05 kimmylin
// no message
//
// Revision 1.2 2003/08/20 18:45:42 kimmylin
// no message
//

