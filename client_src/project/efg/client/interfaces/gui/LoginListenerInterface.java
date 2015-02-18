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
package project.efg.client.interfaces.gui;

import java.awt.event.ActionListener;

import project.efg.util.interfaces.EFGImportConstants;

/**
 * LoginAbstractListener.java $Id: LoginAbstractListener.java,v 1.1 2006/02/25
 * 13:16:20 kasiedu Exp $
 * 
 * Created: Thu Feb 23 09:31:55 2006
 * 
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 * 
 * This abstract class should be implemented to allow LoginDialog to call them.
 * Implemented classes should override actionPerformed
 */
public interface LoginListenerInterface extends ActionListener {
	String loginFailureMessage =
		EFGImportConstants.EFGProperties.getProperty("LoginListener.LoginFailure_Message");
	
	String createuserFailureMessage =
		EFGImportConstants.EFGProperties.getProperty("CreateUserListener.LoginFailure_Message");
	
	String numberOfAttemptsMessage = 
		EFGImportConstants.EFGProperties.getProperty("LoginListener.NumberOfAttemptsMessage");

}
