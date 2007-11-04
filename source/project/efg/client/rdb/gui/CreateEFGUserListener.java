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

import project.efg.client.impl.gui.CreateEFGUserDialog;
import project.efg.client.interfaces.gui.LoginListenerInterface;
import project.efg.client.interfaces.nogui.LoginAbstractModule;



public class CreateEFGUserListener implements LoginListenerInterface{
	
	private CreateEFGUserDialog dialog;
	
	private int errorCounter = 0;
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(project.efg.client.rdb.gui.CreateEFGUserListener.class);
		} catch (Exception ee) {
		}
	}

	public CreateEFGUserListener(CreateEFGUserDialog dialog) {
		
		this.dialog = dialog;
		
		
			
	}
	private boolean comparePasswords() {
		return new String(this.dialog.getPassword()).equals(new String(this.dialog.getConfirmPassword()));
	}
	
	public void actionPerformed(ActionEvent evt) {
		String pwd = new String(this.dialog.getPassword().trim());
		String cpwd = new String(this.dialog.getConfirmPassword().trim());
		String userName = new String(this.dialog.getLoginName());
		if((pwd == null) || (pwd.equals("")) ||
				(userName==null) || (userName.trim().equalsIgnoreCase("efg"))||
				(cpwd == null)|| 
				(cpwd.equals("")) || 
				(!this.comparePasswords())){
			if(++this.errorCounter >= LoginAbstractModule.MAX_LOGIN_ATTEMPTS){
				
				
				JOptionPane.showMessageDialog(this.dialog, 
						numberOfAttemptsMessage,
						"Login Error", JOptionPane.ERROR_MESSAGE);
				this.dialog.setSuccess(false);
			
				
				this.dialog.dispose();
			}
			else{
				this.dialog.setPassword("");
				this.dialog.setConfirmPassword("");
				
			}
			if((userName!=null) && (userName.trim().equalsIgnoreCase("efg"))){
				JOptionPane.showMessageDialog(this.dialog, 
						"EFG2 does not allow the creation of a user called '" + userName  +"'",
						"Login Error", 
						JOptionPane.ERROR_MESSAGE);
			}
			else{
			JOptionPane.showMessageDialog(this.dialog, 
					createuserFailureMessage,
					"Login Error", 
					JOptionPane.ERROR_MESSAGE);
			}
			this.dialog.setSuccess(false);
			return;//try again
			
		}
	
		this.dialog.setSuccess(true);
		this.dialog.dispose();
	}
	
	
} // LoginListener
