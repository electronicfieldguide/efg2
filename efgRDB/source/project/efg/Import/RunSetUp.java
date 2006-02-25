/**
 * $Id$
 *
 * Copyright (c) 2006  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
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
import java.sql.*;
// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * Creates the efg database if it does not already exists, create a user with username efg
 * with only select and update privileges on the tables in the efg database
 * This user is used to access the web application
 */
public class RunSetUp
{
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(RunSetUp.class); 
	}
	catch(Exception ee){
	}
    }
    public synchronized static boolean runSetUp(Connection conn){
	Statement statement;
	//read from properties file
	String query = System.getProperty("createdatabasecmd");	
	//String query = "CREATE DATABASE IF NOT EXISTS efg";
	try{
	    statement = conn.createStatement();
	    statement.execute(query);
	}
	catch(SQLException ee){
	    log.error("Error Code: " + ee.getErrorCode());
	    log.error("SQL State: " + ee.getSQLState());
	    System.err.println("Error Code: " + ee.getErrorCode());
	    System.err.println("SQL State: " + ee.getSQLState());
	    return false;
	}
	//read from properties file
	query = System.getProperty("createusercmd");
	//	query = "CREATE USER efg IDENTIFIED BY 'efg'";
	try{
	    statement.execute(query);
	}
	catch(SQLException ee){
	    String sqlState = ee.getSQLState();
	    if(!sqlState.equalsIgnoreCase("HY000")){
		log.error("Error Code: " + ee.getErrorCode());
		log.error("SQL State: " + ee.getSQLState());
		System.err.println("Error Code: " + ee.getErrorCode());
		System.err.println("SQL State: " + ee.getSQLState());
		return false;
	    }
	    
	}
	//read from properties file
	query = System.getProperty("grantcmd");
	//query = "GRANT SELECT, UPDATE ON efg.* TO efg";
	try{
	    statement.execute(query);
	}
	catch(SQLException ee){
	    log.error("Error Code: " + ee.getErrorCode());
	    log.error("SQL State: " + ee.getSQLState());
	    System.err.println("Error Code: " + ee.getErrorCode());
	    System.err.println("SQL State: " + ee.getSQLState());
	    return false;
	}
	return true;
    }
}
//$Log$
//Revision 1.1  2006/02/25 13:13:31  kasiedu
//New classes for import GUI
//