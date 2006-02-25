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
import java.util.*;
import java.beans.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.nio.channels.*;
import java.io.*;

/**
 * This file contains the class EFGUtils, which has many static fields
 * and methods available to the EFG.  Usually, items are added here when
 * it's noticed that similar functionality is present in many other classes.
 * It would probably be a good idea to periodically go through this file
 * and make sure that the fields and methods present shouldn't be in a
 * particular class.
 */
public class EFGUtils 
{
    private static String catalina_home = null;
    static Logger log = null;			
    static{
	try{
	    log = Logger.getLogger(EFGUtils.class); 
	}
	catch(Exception ee){
	}
    }
    public static Properties getEnvVars(){
    	Process p = null;
	Properties envVars = new Properties();
	try{
	    Runtime r = Runtime.getRuntime();
	    String OS = System.getProperty("os.name").toLowerCase();
	    if (OS.indexOf("windows 9") > -1) {
		p = r.exec( "command.com /c set" );
		log.debug("Windows 9");
	    }
	    else if ( (OS.indexOf("nt") > -1)
		      || (OS.indexOf("windows 20") > -1 )
		      || (OS.indexOf("windows xp") > -1) ) {
		p = r.exec( "cmd.exe /c set" );
		log.debug("unix");
	    }
	    else {
		// our last hope, we assume Unix (thanks to H. Ware for the fix)
		p = r.exec( "env" );
	    }
	    if(p != null){
		BufferedReader br = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
		String line;
		while( (line = br.readLine()) != null ) {
		    int idx = line.indexOf( '=' );
		    if(idx > 0){
			String key = line.substring( 0, idx );
			String value = line.substring( idx+1 );
			envVars.setProperty( key, value);
		    }
		}
	    }
	}
	catch(Exception ee){
	    String message = ee.getMessage();
	    log.error(message); 
	}
	return envVars;
    }
    public static String getCatalinaHome(){
	if(catalina_home != null){
	    return catalina_home;
	}
	Properties props = getEnvVars();
	if(props != null){
	    catalina_home = props.getProperty("CATALINA_HOME");
	}
	if(props == null){
	    log.error("Catalina home environment variable isnot set!!");
	}
	return catalina_home;
    }
    /**
     * Writes msg.toString() to System.err and then flushes the stream.
     *
     * @param msg msg object to write
     */
    public static void log(Object msg) 
    { 
	if (msg == null){
	    System.err.println("");
        }
	else{ 
	    System.err.println(new Date().toString() + "-------- " + msg.toString());
	}
	System.err.flush();
    }
 /**	
     * This method takes a String and returns an encoded version
     * of the String that can be used as a Java variable name.
     *
     * @param origString the pre-encoded string
     * @return the encoded string to be used as a java variable
     */
    public static String encodeToJavaName(String origString) 
    {
	return Introspector.
	    decapitalize(encodeToJavaClassName(origString));
    }
 /**
     * This method takes a String and returns an encoded 
     * version of the String that can be used as a Java class name.
     *
     * @param origString the pre-encoded string
     * @return the encoded string to be used as a java class name
     */
    private static String encodeToJavaClassName(String origString) 
    {
	StringBuffer sb = new StringBuffer(origString);
	int strLength = sb.length();
	
	if (strLength > 0 && 
	    !Character.isJavaIdentifierStart(sb.charAt(0)))
	    sb.setCharAt(0, '_');
	
	for (int i = 1; i < strLength; i++)
	    if (!Character.isJavaIdentifierPart(sb.charAt(i)))
		sb.setCharAt(i, '_');
	
	return sb.toString();
    }

}

//$Log$
//Revision 1.2  2006/02/25 13:16:42  kasiedu
//no message
//
//Revision 1.1.1.1  2006/01/25 21:03:48  kasiedu
//Release for Costa rica
//
//Revision 1.1.1.1  2003/10/17 17:03:09  kimmylin
//no message
//
//Revision 1.3  2003/08/20 18:45:42  kimmylin
//no message
//
