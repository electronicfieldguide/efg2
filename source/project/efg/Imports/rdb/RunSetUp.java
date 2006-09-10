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

package project.efg.Imports.rdb;
import project.efg.Imports.efgImpl.DBObject;
import project.efg.util.EFGImportConstants;
import org.springframework.jdbc.core.JdbcTemplate;
import org.apache.log4j.Logger;


/**
 * Creates the efg database if it does not already exists, create a user with
 * username efg with only select and update privileges on the tables in the efg
 * database This user is used to access the web application
 */
public class RunSetUp {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(RunSetUp.class);
		} catch (Exception ee) {
		}
	}
	private static JdbcTemplate jdbcTemplate;
	/**
	 * 
	 * @param dbObject - contains enough information to connect to database
	 * @return true if connection to database was successfully made, false otherwise
	 */
	public synchronized static boolean runSetUp(DBObject dbObject) {
		//create efg database, users etc in main database
	
		jdbcTemplate=
			EFGRDBImportUtils.getJDBCTemplate(dbObject);
	
		
		// read from properties file
		String query = EFGImportConstants.EFGProperties.getProperty("createdatabasecmd");
		try {
			jdbcTemplate.execute(query);
			
		} catch (Exception ee) {
			log.error(ee.getMessage());
			
			return false;
		}
		try{
			query = EFGImportConstants.EFGProperties.getProperty("createusercmd");
			jdbcTemplate.execute(query);
		}
		catch (Exception ee) {
			log.error("Warning: user probably already exists");
			
		}
		try{
			query = EFGImportConstants.EFGProperties.getProperty("grantcmd");
			jdbcTemplate.execute(query);
		}
		catch (Exception ee) {
			log.error(ee.getMessage());
		
		}
	
		return true;
	}

}
// $Log$
// Revision 1.1.2.3  2006/09/10 12:02:28  kasiedu
// no message
//
// Revision 1.1.2.2  2006/08/09 18:55:24  kasiedu
// latest code confimrs to what exists on Panda
//
// Revision 1.1.2.1  2006/06/08 13:27:42  kasiedu
// New files
//
// Revision 1.1 2006/02/25 13:13:31 kasiedu
// New classes for import GUI
//