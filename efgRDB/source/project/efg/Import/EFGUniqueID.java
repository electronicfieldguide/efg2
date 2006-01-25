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

package project.efg.Import;
import project.efg.util.*;
import java.util.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * The ID that this class provides is designed to be immutable and unique.
 */
public class EFGUniqueID 
{
    // The amount of time that thread will sleep.
    // Small value may not guarantee the uniqueness of ID.
    private static final int IDLE_TIME = 15;             // milli seconds
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(EFGUniqueID.class); 
	}
	catch(Exception ee){
	}
    }
    /**
     * Create a unique ID of a taxon.
     *
     * @return a string representing the unique ID of a taxon 
     */
    public synchronized static String getID() 
    {
	String id = "";

	try {
	    // Here 9 and 978 are just separators.
	    id = (InetAddress.getLocalHost().getHostAddress()).replace('.','9') +
		"978" + new GregorianCalendar(TimeZone.getTimeZone("GMT")).getTimeInMillis();

	    Thread.sleep(IDLE_TIME); // To guarantee timestamps are unique.
	} 
	catch (UnknownHostException uhe) {
	    LoggerUtils.logErrors(uhe);
	    System.err.println(uhe.getMessage());
	    
	} 
	catch (InterruptedException e) {
	    
	}
	
	return id;
    }

    /** 
     * User may use this test routine to find the appropriate IDLE_TIME 
     * for a specific machine.
     */
    public static void main(String[] args)
    {
	String id = "";
	int n = 100;
	HashSet hs = new HashSet(500);
	for(int i = 0; i < n; i++) {
	    id = EFGUniqueID.getID();
	    hs.add(id);
	}
	hs.add(id);
	System.out.println("The size of set should still be " + n + " : " + hs.size());
	hs.add("weird string");
	n++;
	System.out.println("Now the size of set should be " + n + "   : " + hs.size());
    }
}

//$Log$
//Revision 1.1  2006/01/25 21:03:42  kasiedu
//Initial revision
//
//Revision 1.1.1.1  2003/10/17 17:03:05  kimmylin
//no message
//
//Revision 1.3  2003/08/20 18:45:41  kimmylin
//no message
//
