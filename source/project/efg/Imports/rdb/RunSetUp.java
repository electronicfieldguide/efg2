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
	
	private static boolean checkDB(DBObject dbObject) {
		
		Object[] obj = getEFGUsers(dbObject);
		if((obj == null) || (obj.length == 0)){
			return false;
		}
		
		
		return true;
	}

	public static boolean createSuperUser(DBObject db,DBObject superuserInfo){
		if(superuserInfo == null){
			return true;
		}
		Object[] obj = getEFGUsers(db);
		checkSuperUser(db,superuserInfo,obj);
		return true;
	} 
	
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
			if(jdbcTemplate == null){
				jdbcTemplate=
					EFGRDBImportUtils.getJDBCTemplate(db);
			}
			jdbcTemplate.execute(queryBuffer.toString());
			
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
		}
		catch (Exception ee) {
			log.error(ee.getMessage());
			return false;
		
		}
		return true;
	} 
	
	public  static Object[] getEFGUsers(DBObject db) {
		try{
			
			
			if(jdbcTemplate == null){
				jdbcTemplate=
					EFGRDBImportUtils.getJDBCTemplate(db);
			}
			StringBuffer queryBuffer = new StringBuffer();
			
			queryBuffer.append("SELECT User FROM MYSQL.DB WHERE DB='efg'");
			 
			List list = jdbcTemplate.queryForList(queryBuffer.toString(), String.class);
			if(list != null){
				list.remove("efg");
				list.remove(db.getUserName().toLowerCase());
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
							"User name already existss. Please create a new one", 
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
	 * 
	 * @param dbObject - contains enough information to connect to database
	 * @return true if connection to database was successfully made, false otherwise
	 * Run setup only if there is no user called efg
	 */
	public synchronized static boolean runSetUp(DBObject dbObject) {
		//create efg database, users etc in main database
	if(checkDB(dbObject)){
		return true;
	}
	if(jdbcTemplate == null){
		jdbcTemplate=
			EFGRDBImportUtils.getJDBCTemplate(dbObject);
	}
		//invoke the other login
		// if user chooses cancel warn user and ask if they will like to return
		//if no continue with null
		//if yes show it again
		
		
		//create a super user command
		String query = null;
		// read from properties file
		query = EFGImportConstants.EFGProperties.getProperty("createdatabasecmd");
		try {
			jdbcTemplate.execute(query);
			createUserTables(dbObject);
			
		} catch (Exception ee) {
			createUserTables(dbObject);
			//createusertables
			//create the tables if they do not exists
			log.error(ee.getMessage());
			
			return false;
		}
		//load sample data
		LoadSampleData sampleData = new LoadSampleData(dbObject);
		sampleData.loadData();
		if(sampleData.isError()){
			JOptionPane.showMessageDialog(null, "Error in Loading Sample Data. View logs for more details", "Error in Loading Sample Data",
					JOptionPane.ERROR_MESSAGE);
		}
		
	
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
		
		return true;
	}

}
// $Log$
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