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

package project.efg.servlets.factory;

import project.efg.servlets.efgInterface.EFGDataSourceHelperInterface;
//import project.efg.servlets.rdb.RDBDataSourceHelperImpl;

/**
 * Objects of this class can be directly accessed from a servlet, but it was
 * really intended for use in jsp:useBean tags in JSPs.
 * 
 * @see #TypePage.jsp
 */
public class EFGDataSourceHelperFactoryImpl {//implements EFGDataSourceHelperFactoryInterface,
		
	static final long serialVersionUID = 1;

	private EFGDataSourceHelperInterface dsHelper;

	

	/**
	 * Create a EFGDataSourceHelper object to help working with multiple data
	 * sources.
	 */
	public void setDataSourceHelper() {
		//dsHelper = new RDBDataSourceHelperImpl();
	}

	/**
	 * Set the EFGDataSourceHelper and return it.
	 * 
	 * @return an EFGDataSourceHelper
	 */
	public EFGDataSourceHelperInterface getDataSourceHelper() {
		setDataSourceHelper();
		return dsHelper;
	}
}

// $Log$
// Revision 1.1.2.1  2006/06/08 13:27:44  kasiedu
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
