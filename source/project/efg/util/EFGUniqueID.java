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

package project.efg.util;

//import java.net.InetAddress;
//import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;



//import project.efg.Imports.efgImportsUtil.LoggerUtils;

/**
 * The ID that this class provides is designed to be immutable and unique.
 */
public class EFGUniqueID {
	// The amount of time that thread will sleep.
	// Small value may not guarantee the uniqueness of ID.
	
	private static long id = -1;

	

	/**
	 * Create a unique ID.
	 * 
	 * @return a string representing a unique ID
	 */
	public synchronized static long getID() {
		//String id = "";
		if(id == -1){
			try {
				String id_str = new GregorianCalendar(TimeZone.getTimeZone("GMT"))
						.getTimeInMillis() + "";		
				id = Long.parseLong(id_str);
			} 
			catch (Exception e) {
				
			}
		}
		else{
			id = id + 1;
		}
		return id;
	}
	/**
	 * Create a unique ID.
	 * 
	 * @return a string representing a unique ID
	 */
	public synchronized static String getUniqueID() {
		 Calendar tempCalendar = 
		      new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		    long timeStamp = tempCalendar.getTime().getTime();
		    String localAddress = "";
		    try {
		      localAddress = InetAddress.getLocalHost().getHostAddress();
		    }
		    catch (UnknownHostException e) {}
		    return "efg2_"+localAddress+timeStamp;
	}
	
}

// $Log$
// Revision 1.3  2007/01/12 15:03:40  kasiedu
// no message
//
// Revision 1.2  2006/12/08 03:51:02  kasiedu
// no message
//
// Revision 1.1.2.1  2006/08/09 18:55:26  kasiedu
// latest code confimrs to what exists on Panda
//
// Revision 1.1.2.1  2006/06/08 13:27:42  kasiedu
// New files
//
// Revision 1.1.1.1 2006/01/25 21:03:42 kasiedu
// Release for Costa rica
//
// Revision 1.1.1.1 2003/10/17 17:03:05 kimmylin
// no message
//
// Revision 1.3 2003/08/20 18:45:41 kimmylin
// no message
//
