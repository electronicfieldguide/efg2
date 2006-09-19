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

import javax.sql.DataSource;



/**
 * This class is a utility class for EFG relation database.
 * It would probably be a good idea to periodically go through this file
 * and make sure that the fields and methods present shouldn't be in a
 * particular class.
 */
public class EFGRDBUtils {

	
	private static DataSource ds;
	public static synchronized void setDatasource(DataSource datasource){
		ds = datasource;
	}
	public static synchronized DataSource getDatasource( ){
		return ds;
	}
	
	
	
	
	
}

//$Log$
//Revision 1.1.2.2  2006/09/19 22:36:40  kasiedu
//no message
//
//Revision 1.1.2.3  2006/09/10 12:03:23  kasiedu
//no message
//
//Revision 1.1.2.2  2006/08/09 18:55:25  kasiedu
//latest code confimrs to what exists on Panda
//
//Revision 1.1.2.1  2006/06/08 13:27:44  kasiedu
//New files
//
//Revision 1.1.1.1  2003/10/17 17:03:05  kimmylin
//no message
//
//Revision 1.7  2003/08/20 18:45:42  kimmylin
//no message
//

