package project.efg.client.rdb.gui;

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

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import project.efg.client.factory.nogui.SpringNoGUIFactory;
import project.efg.client.impl.gui.LoginDialog;
import project.efg.client.interfaces.gui.LoginListenerInterface;
import project.efg.client.interfaces.nogui.LoginAbstractModule;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.DBObject;

public class LoginListenerImpl implements LoginListenerInterface {

	private LoginDialog dialog;
	private project.efg.client.interfaces.nogui.LoginAbstractModule myLogin;
	private int errorCounter = 0;

	protected static String setUpError_Message = EFGImportConstants.EFGProperties
			.getProperty("LoginListener.SetUpError_Message");

	static Logger log = null;
	static {
		try {
			log = Logger
					.getLogger(project.efg.client.rdb.gui.LoginListenerImpl.class);
		} catch (Exception ee) {
		}
	}

	public LoginListenerImpl(LoginDialog dialog) {
		this.dialog = dialog;

		this.myLogin = SpringNoGUIFactory.getLoginModule();

	}

	public void actionPerformed(ActionEvent evt) {
		String m_loginName = this.dialog.getLoginName();
		String m_password = new String(this.dialog.getPassword());
		String url = EFGImportConstants.EFGProperties.getProperty("urltodb");

		// factory ?
		project.efg.util.utils.DBObject dbObject = new DBObject(url,
				m_loginName, m_password);

		if (!this.myLogin.login(dbObject)) {// if a connection is successfully
											// made to database
			if (++this.errorCounter >= LoginAbstractModule.MAX_LOGIN_ATTEMPTS) {// 3
																				// attempts
																				// log
																				// out
																				// if
																				// unsuccessful
				JOptionPane.showMessageDialog(this.dialog,
						numberOfAttemptsMessage, "Login Error",
						JOptionPane.ERROR_MESSAGE);
				this.dialog.setSuccess(false);
				System.exit(1);
			} else {
				this.dialog.setPassword("");
				// this.dialog.setLoginName("");
			}

			JOptionPane.showMessageDialog(this.dialog, loginFailureMessage,
					"Login Error", JOptionPane.ERROR_MESSAGE);
			return;// try again
		}
		// invoke the other
		if (!runSetUp(dbObject)) {// runset up if a connection was obtained
									// from database
			JOptionPane.showMessageDialog(this.dialog, setUpError_Message,
					"Login Error", JOptionPane.ERROR_MESSAGE);
			this.dialog.setSuccess(false);

			System.exit(1);
		}
		log.info("Set up run successfully");

		this.dialog.setSuccess(true);
		this.dialog.dispose();
	}

	/**
	 * Do some initialization when a connection is obtained from the database
	 * the first time.
	 * 
	 * @param dbObject
	 * @return true if set up runs successfully
	 */
	private boolean runSetUp(DBObject dbObject) {
		boolean isRun = true;
		if (!RunSetUp.runSetUp(dbObject)) {
			isRun = false;
		}
		return isRun;
	}
} // LoginListener
