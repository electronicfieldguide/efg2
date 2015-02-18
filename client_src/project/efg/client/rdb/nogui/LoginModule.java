package project.efg.client.rdb.nogui;
/* $Id$
* $Name:  $
* Created: Tue Feb 28 13:14:19 2006
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
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
* Imports a csv file into a relational database
* 
*/
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import project.efg.client.interfaces.nogui.LoginAbstractModule;
import project.efg.util.utils.DBObject;
/**
 * LoginModule.java
 *
 *
 * Created: Thu Feb 23 09:36:27 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */
/**
 * Emulator for login module
 */
public class LoginModule extends LoginAbstractModule {
	static Logger log = null;

	static {
		try {
			log = Logger.getLogger(LoginModule.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 * @param dbObject
	 * @return true if connection to database was successful
	 */
	public  boolean login(DBObject dbObject) {
		try {
			JdbcTemplate template =
				project.efg.util.utils.EFGRDBImportUtils.getJDBCTemplate(dbObject);
			if(template == null){
				throw new Exception("Either user name and password were wrong " +
						"or the Database server is not running");
				}
			return true;
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
		return false;
	}
} // LoginModule
