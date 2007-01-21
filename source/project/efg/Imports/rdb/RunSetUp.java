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
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import project.efg.Imports.efgImpl.CreateEFGUserDialog;
import project.efg.Imports.efgImpl.DBObject;
import project.efg.util.EFGImportConstants;


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
	 * Find out if efg database alread exists 
	 * @param dbObject
	 * @return true if there is a user with the name and password
	 */
	private static boolean checkDB(DBObject dbObject) {
		
		Object[] obj = getEFGUsers(dbObject);
		if((obj == null) || (obj.length == 0)){
			return false;
		}
		
		
		return true;
	}
	/**
	 * 
	 * @param db
	 * @param superuserInfo
	 * @return
	 */
	public static boolean createSuperUser(DBObject db,DBObject superuserInfo){
		if(superuserInfo == null){
			return true;
		}
		Object[] obj = getEFGUsers(db);
		checkSuperUser(db,superuserInfo,obj);
		return true;
	} 
	/**
	 * Called setting or changing privileges
	 *
	 */
	private static void flushPrivileges(DBObject db) {
		try {
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(EFGImportConstants.EFGProperties.getProperty("flushprivileges"));
			
			if(!queryBuffer.toString().trim().equals("")) {
				if(jdbcTemplate == null){
					jdbcTemplate=
						EFGRDBImportUtils.getJDBCTemplate(db);
				}
				jdbcTemplate.execute(queryBuffer.toString());
			}
		} catch (Exception e) {
			
		}
		
	}
	/**
	 * Create a super user from the information supplied by user
	 * @param db
	 * @param superuserInfo
	 * @return
	 */
	private static boolean createASuperUser(DBObject db, DBObject superuserInfo){
		if(superuserInfo == null){
			return true;
		}
		
		StringBuffer queryBuffer = null;
		try {
			queryBuffer = new StringBuffer();
			queryBuffer.append("CREATE USER ");
			queryBuffer.append(superuserInfo.getUserName());
			queryBuffer.append(" IDENTIFIED BY '");
			queryBuffer.append(superuserInfo.getPassword());
			queryBuffer.append("'");
			if(jdbcTemplate == null){
				jdbcTemplate=
					EFGRDBImportUtils.getJDBCTemplate(db);
			}
			jdbcTemplate.execute(queryBuffer.toString());
			
		} catch (Exception ee) {
			log.error(ee.getMessage());
			
			return false;
		}
		
		try{
			queryBuffer = new StringBuffer();
			
			queryBuffer.append(EFGImportConstants.EFGProperties.getProperty("grantsuperusermysqlcmd"));
			queryBuffer.append( " " );
			queryBuffer.append(superuserInfo.getUserName());
			queryBuffer.append(" WITH GRANT OPTION ");
			if(jdbcTemplate == null){
				jdbcTemplate=
					EFGRDBImportUtils.getJDBCTemplate(db);
			}
			jdbcTemplate.execute(queryBuffer.toString());
			flushPrivileges(db);
		
			
			
			queryBuffer = new StringBuffer();
			queryBuffer.append(EFGImportConstants.EFGProperties.getProperty("grantsuperusercmd"));
			queryBuffer.append( " " );
			queryBuffer.append(superuserInfo.getUserName());
			queryBuffer.append(" WITH GRANT OPTION ");
			
			if(jdbcTemplate == null){
				jdbcTemplate=
					EFGRDBImportUtils.getJDBCTemplate(db);
			}
			jdbcTemplate.execute(queryBuffer.toString());
			flushPrivileges(db);
		}
		catch (Exception ee) {
			log.error(ee.getMessage());
			return false;
		
		}
		return true;
	} 
	/**
	 * Get an Array of current efg users. i.e users created by root user for instance
	 * @param db
	 * @return
	 */
	public  static Object[] getEFGUsers(DBObject db) {
		try{
			
			
			if(jdbcTemplate == null){
				jdbcTemplate=
					EFGRDBImportUtils.getJDBCTemplate(db);
			}
			StringBuffer queryBuffer = new StringBuffer("SELECT User FROM MYSQL.DB WHERE DB='efg'");
			 
			List list = jdbcTemplate.queryForList(queryBuffer.toString(), String.class);
			if(list != null){
				
				list.remove(EFGImportConstants.EFGProperties.getProperty("dbusername"));
				
				return list.toArray();
			}
		}
		catch (Exception ee) {
			log.error(ee.getMessage());
			
			JOptionPane.showMessageDialog(null, ee.getMessage(), "Error Message",
			JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	/**
	 * Delete a user
	 * @param db
	 * @param userName
	 */
	public  static void deleteSuperUser(DBObject db, String userName) {
		try{
			
			if(jdbcTemplate == null){
				jdbcTemplate=
					EFGRDBImportUtils.getJDBCTemplate(db);
			}
			StringBuffer queryBuffer = new StringBuffer();
			
			queryBuffer.append("DROP USER '");
			queryBuffer.append(userName);
			queryBuffer.append( "'");
			jdbcTemplate.execute(queryBuffer.toString());
			flushPrivileges(db);
			String message = "The user : '" + userName + 
			"' \n successfully deleted from system!!";
			JOptionPane.showMessageDialog(null, message, "Success Message",
			JOptionPane.INFORMATION_MESSAGE);
			
		}
		catch (Exception ee) {
			log.error(ee.getMessage());
			String message = "The user : '" + userName + 
			"' \n could not be deleted due to an error. Consult log file for error message";
			JOptionPane.showMessageDialog(null, message, "Error Message",
			JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Get information needed to find a Super user
	 * @return
	 */
	private static DBObject getSuperUser() {
		
		CreateEFGUserDialog dialog = new CreateEFGUserDialog(null);
		dialog.setVisible(true);
		
		
		if(dialog.isSuccess()){
			DBObject db = dialog.getDbObject();
			dialog.dispose();
			return db;
		}
		
		
		return null;
	}
/**
 * Find out if there is a super user..If not prompt user to create one.
 * @param dbObject
 * @param superuserInfo
 * @param obj
 */
	private static void checkSuperUser(DBObject dbObject,DBObject superuserInfo,Object[] obj) {
		
		if(superuserInfo == null){
		 superuserInfo = getSuperUser();
		}
		
		
		if(superuserInfo != null){
			if(obj != null && obj.length > 0){
				int i = 0;
				boolean found = false;
				while(i < obj.length){
					String str = (String)obj[i];
					if(str.equalsIgnoreCase(superuserInfo.getUserName())){
						found = true;
						break;
					}
					++i;
				}
				if(found){
					JOptionPane.showMessageDialog(null,
							"User name already exists. Please create a new one", 
							"User already Exists",
						    JOptionPane.WARNING_MESSAGE);
					checkSuperUser(dbObject,null,obj);
				}
				else{
					createASuperUser(dbObject,superuserInfo);
				}
			}
			else{
				createASuperUser(dbObject,superuserInfo);
			}
		}
	
	}
	/**
	 * Create all tables needed by application
	 * @param dbObject
	 */
	private static void createUserTables(DBObject dbObject) {
		if(dbObject == null){
			return;
		}
		
		try{
		DBObject newDb = dbObject.clone(
				EFGImportConstants.EFGProperties.getProperty("dburl"));
		
		JdbcTemplate newjdbcTemplate=
			EFGRDBImportUtils.getJDBCTemplate(newDb);	
	
		// read from properties file
		 StringBuffer query = new StringBuffer("CREATE TABLE ");
		 query.append(" IF NOT EXISTS ");
		 query.append(EFGImportConstants.EFGProperties.getProperty("users_table"));
		 query.append(" ( user_name varchar(15) not null primary key, user_pass varchar(15) not null)");
		
		 newjdbcTemplate.execute(query.toString());
		 
		 query = new StringBuffer("CREATE TABLE ");
		 query.append(" IF NOT EXISTS ");
		 query.append(EFGImportConstants.EFGProperties.getProperty("role_table"));
		 query.append(" (");
		 query.append("user_name  varchar(15) not null, role_name varchar(15) not null, ");
		 query.append("primary key (user_name, role_name) ");
		 query.append(")");
		 newjdbcTemplate.execute(query.toString());
		 
		 query = new StringBuffer("INSERT INTO ");
		 query.append(EFGImportConstants.EFGProperties.getProperty("role_table"));
		 query.append(" VALUES('");
		 query.append(EFGImportConstants.EFGProperties.getProperty("dbusername"));
		 query.append("','");
		 query.append(EFGImportConstants.EFGProperties.getProperty("dbpassword"));
		 query.append("')");	
		 newjdbcTemplate.execute(query.toString());
		 
		 
		 query = new StringBuffer("INSERT INTO ");
		 query.append(EFGImportConstants.EFGProperties.getProperty("users_table"));
		 query.append(" VALUES('");
		 query.append(EFGImportConstants.EFGProperties.getProperty("dbusername"));
		 query.append("','");
		 query.append(EFGImportConstants.EFGProperties.getProperty("db_role"));
		 query.append("')");	
		 newjdbcTemplate.execute(query.toString());
		
		}
		catch (Exception e) {
			
		}
		
		
	}
/**
 * Create all the helper tables needed by import
 * @param dbObject
 */
		private static void createHelperTables(DBObject dbObject) {
			//creeate helper tables
			createUserTables(dbObject);
			String query = null;
			
			
			Object[] obj = getEFGUsers(dbObject);
			
			checkSuperUser(dbObject,null,obj);
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
			//load sample data
		/*	LoadSampleData sampleData = new LoadSampleData(dbObject);
			sampleData.loadData();
			if(sampleData.isError()){
				JOptionPane.showMessageDialog(null, "Error in Loading Sample Data. View logs for more details", "Error in Loading Sample Data",
						JOptionPane.ERROR_MESSAGE);
			}*/
		}	
	
	/**
	 * 
	 * @param dbObject - contains enough information to connect to database
	 * @return true if connection to database was successfully made, false otherwise
	 * Run setup only if there is no user
	 */
	public synchronized static boolean runSetUp(DBObject dbObject) {
		boolean  isDBExists= false;
		//check if you can get a connection from the efg database
		//create efg database, users etc in main database
		
	isDBExists = checkEFGDB(dbObject);
	//remove me	
	//if(checkDB(dbObject)){//if this user already exists
	//	return true;
	//}
	if(jdbcTemplate == null){
		jdbcTemplate=
			EFGRDBImportUtils.getJDBCTemplate(dbObject);
	}
		//invoke the other login
		// if user chooses cancel warn user and ask if they will like to return
		//if no continue with null
		//if yes show it again
		
		//see if database exists
		//create a super user command
		String query = null;
		// read from properties file
		if(!isDBExists) {
			query = EFGImportConstants.EFGProperties.getProperty("createdatabasecmd");
			try {
				//create the database
				jdbcTemplate.execute(query);
				createHelperTables(dbObject);
			} catch (Exception ee) {
			
				createHelperTables(dbObject);
				log.error(ee.getMessage());	
				return false;
			}
		}
		
		
		return true;
	}
	/**
	 * 
	 * @param dbObject2
	 * @return
	 */
	private static DBObject getClone(DBObject dbObject2) {
		String url = EFGImportConstants.EFGProperties
		.getProperty("dburl");
		return dbObject2.clone(url);
	}
	/**
	 * If the efg database exists return true.
	 * @param dbObject
	 * @return
	 */
	private static boolean checkEFGDB(DBObject dbObject) {
	try{
				DBObject cloneDB = getClone(dbObject);
				
				JdbcTemplate jdbcTemplateNew =
					EFGRDBImportUtils.getJDBCTemplate(cloneDB);
				if(jdbcTemplateNew != null) {
					
					return true;
				}
		}
		catch (Exception ee) {
			System.err.println(ee.getMessage());
			log.error(ee.getMessage());
		}
		
		return false;
	}

}
// $Log$
// Revision 1.3  2007/01/21 02:10:45  kasiedu
// no message
//
// Revision 1.2  2006/12/08 03:50:59  kasiedu
// no message
//
// Revision 1.1.2.5  2006/09/18 18:15:26  kasiedu
// no message
//
// Revision 1.1.2.4  2006/09/13 17:11:11  kasiedu
// no message
//
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