/**
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Hua Tang<htang@cs.umb.edu>
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

package project.efg.Imports.efgInterface;

/**
 * This class defines the ODBC drivers and DB source URI
 */
public interface EFGdbDriverInterface {
	public static final String BRIDGE_DRIVER_NAME = "sun.jdbc.odbc.JdbcOdbcDriver";

	public static final String FM_SOURCE_URI = "jdbc:odbc:FileMaker_Files";
}

// $Log$
// Revision 1.2  2006/12/08 03:50:58  kasiedu
// no message
//
// Revision 1.1.2.1  2006/06/08 13:27:41  kasiedu
// New files
//
// Revision 1.1.1.1 2006/01/25 21:03:48 kasiedu
// Release for Costa rica
//

