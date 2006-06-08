package project.efg.Imports.efgImpl;
/**
 * $Id$
 * $Name$
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
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


import org.apache.log4j.Logger;

import project.efg.util.EFGImportConstants;
import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgImportsUtil.LoggerUtils;

/**
 * TomcatUsers.java
 * 
 * 
 * Created: Mon Mar 27 09:50:20 2006
 * 
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */
public class TomcatUsers {
	private String catalina_home;

	private String userName;

	private String password;

	private String roleName;

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(TomcatUsers.class);
		} catch (Exception ee) {
		}
	}

	/**
	 * @param catalina_home -
	 *            The path to where tomcat is installed on the users system. If
	 *            this is null or the empty string system should be able to find
	 *            it
	 */
	public TomcatUsers(String catalina_home) {
		this.catalina_home = catalina_home;
	}
	private void init() {
		this.userName = project.efg.util.EFGImportConstants.EFGProperties.getProperty("username");
		this.password = project.efg.util.EFGImportConstants.EFGProperties.getProperty("password");
		this.roleName = project.efg.util.EFGImportConstants.EFGProperties.getProperty("role");
	}

	/**
	 * Add a user whose information is contained in tomcat-users.properties file
	 * to the list of tomcat users in $CATALINA_HOME/conf/tomcat-users.xml. If
	 * the tomcat-users.xml file does not exists, the system should create it.
	 */
	public boolean editTomcatUsers() {
		if (this.catalina_home == null) {
			log.error("System could not find CATALINA_HOME");
			return false;
		}
		try {
			this.init();
			String url = this.catalina_home
					+ EFGImportConstants.TOMCAT_USERS_FILE_LOCATION;
			File file = new File(url);

			if (file.exists()) {
				File file2 = new File(EFGImportConstants.SETUP_FILE);
				if (file2.exists()) {// server contains latest tomcat-users
										// file
					return true;
				} else {// regenerate tomcat-users file
					return this.processFile(url, false);
				}
			} else {
				return this.processFile(url, true);
			}
		} catch (Exception ee) {
			LoggerUtils.logErrors(ee);

		}
		return false;
	}

	private void addRole(project.efg.tomcatusers.TomcatUsers tomcatUsers) {
		project.efg.tomcatusers.RoleType efgRole = new project.efg.tomcatusers.RoleType();
		efgRole.setRolename(this.roleName);
		tomcatUsers.addRole(efgRole);
	}

	private void addUser(project.efg.tomcatusers.TomcatUsers tomcatUsers) {
		project.efg.tomcatusers.UserType efgUser = new project.efg.tomcatusers.UserType();
		efgUser.setUsername(this.userName);
		efgUser.setPassword(this.password);
		efgUser.setRoles(this.roleName);
		tomcatUsers.addUser(efgUser);
	}

	/**
	 * @param file -
	 *            The file to create or edit
	 * @param isNew -
	 *            true, If this file does not already exists
	 * @return true if file was written to location successfully, false
	 *         othwerwise
	 */
	private boolean processFile(String file, boolean isNew) {

		String renamedFile = file
				+ EFGImportConstants.TOMCAT_USERS_FILE_RENAME_EXT;
		FileWriter writer = null;
		FileReader reader = null;
		project.efg.tomcatusers.TomcatUsers tomcatUsers = null;
		boolean isFound = false;
		try {

			if (!isNew) {// file already exists on server get it and append
							// current roles and user to them
				reader = new FileReader(file);
				tomcatUsers = (project.efg.tomcatusers.TomcatUsers) project.efg.tomcatusers.TomcatUsers
						.unmarshalTomcatUsers(new FileReader(file));

				writer = new FileWriter(renamedFile);
				tomcatUsers.marshal(writer);// rename the file
				writer.flush();
				writer.close();

				for (int i = 0; i < tomcatUsers.getRoleCount(); i++) {
					project.efg.tomcatusers.RoleType efgRole = tomcatUsers
							.getRole(i);
					String rolename = efgRole.getRolename();
					if (rolename.equalsIgnoreCase(this.roleName)) {
						isFound = true;
						break;
					}
				}
				if (!isFound) {// this role does not exist add it
					//project.efg.tomcatusers.RoleType efgRole = new project.efg.tomcatusers.RoleType();
					this.addRole(tomcatUsers);
				}

				isFound = false;
				for (int i = 0; i < tomcatUsers.getUserCount(); i++) {
					project.efg.tomcatusers.UserType efgUser = tomcatUsers
							.getUser(i);
					String username = efgUser.getUsername();
					if (username.equalsIgnoreCase(this.userName)) {
						isFound = true;
						break;
					}
				}
				if (!isFound) {// this user doe snot exist add it
					this.addUser(tomcatUsers);
				}
			} else {
				tomcatUsers = new project.efg.tomcatusers.TomcatUsers();
				this.addRole(tomcatUsers);
				this.addUser(tomcatUsers);

			}
			if (!isFound) {
				writer = new FileWriter(file);
				tomcatUsers.marshal(writer);
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				if (reader != null) {
					reader.close();
				}
			}
			return true;
		} catch (Exception ee) {
			try {
				System.err.println(ee.getMessage());
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				if (reader != null) {
					reader.close();
				}

				File reFile = new File(renamedFile);
				if (reFile.exists()) {
					reader = new FileReader(renamedFile);
					tomcatUsers = tomcatUsers = (project.efg.tomcatusers.TomcatUsers) project.efg.tomcatusers.TomcatUsers
							.unmarshalTomcatUsers(new FileReader(file));

					writer = new FileWriter(file);
					tomcatUsers.marshal(writer);// rename the file

					if (writer != null) {
						writer.flush();
						writer.close();
					}
					if (reader != null) {
						reader.close();
					}
				}
			} catch (Exception eee) {
			}
		}
		return false;
	}

	public static void main(String args[]) {
		String catalina_home = null;

		if (args.length > 0) {
			//System.out.println(":" + args[0]);
			catalina_home = args[0];

		} else {
			catalina_home = EFGUtils.getCatalinaHome();
		}
		TomcatUsers users = new TomcatUsers(catalina_home);
		if (!users.editTomcatUsers()) {
			log.debug("Could not write Tomcat users file to :" + catalina_home
					+ EFGImportConstants.TOMCAT_USERS_FILE_LOCATION);
		}
	}
} // TomcatUsers
