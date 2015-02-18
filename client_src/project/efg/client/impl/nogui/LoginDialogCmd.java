/**
 * 
 */
package project.efg.client.impl.nogui;

import org.apache.log4j.Logger;

import project.efg.client.interfaces.nogui.LoginDialogInterface;
import project.efg.client.utils.nogui.LoggerUtils;
import project.efg.util.interfaces.EFGImportConstants;

/**
 * @author jacob.asiedu
 *
 */
public class LoginDialogCmd implements LoginDialogInterface {
	static Logger log = null;
	static String instance_Message =null;
	static String usage_message = null;
	//read initiliazation params
	static {
		try {
			LoggerUtils utils = new LoggerUtils();
			utils.toString();
			
			instance_Message =
				EFGImportConstants.EFGProperties.getProperty(
						"LoginDialog.instance");
			usage_message = EFGImportConstants.EFGProperties.getProperty(
					"LoginDialog.usage");
			
			log = Logger.getLogger(LoginDialogCmd.class);
		} catch (Exception ee) {
		}
	}
	/* (non-Javadoc)
	 * @see project.efg.client.interfaces.nogui.LoginDialogInterface#getLoginName()
	 */
	public String getLoginName() {
		// FIXME Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see project.efg.client.interfaces.nogui.LoginDialogInterface#getPassword()
	 */
	public String getPassword() {
		// FIXME Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see project.efg.client.interfaces.nogui.LoginDialogInterface#isSuccess()
	 */
	public boolean isSuccess() {
		// FIXME Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see project.efg.client.interfaces.nogui.LoginDialogInterface#setLoginName(java.lang.String)
	 */
	public void setLoginName(String login) {
		// FIXME Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see project.efg.client.interfaces.nogui.LoginDialogInterface#setPassword(java.lang.String)
	 */
	public void setPassword(String password) {
		// FIXME Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see project.efg.client.interfaces.nogui.LoginDialogInterface#setSuccess(boolean)
	 */
	public void setSuccess(boolean bool) {
		// FIXME Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see project.efg.client.interfaces.nogui.LoginDialogInterface#closeEFGDialog()
	 */
	public void closeEFGDialog() {
		// FIXME Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see project.efg.client.interfaces.nogui.LoginDialogInterface#setServerRoot(java.lang.String, boolean)
	 */
	public void setServerRoot(String pathToServer, boolean isDefault) {
		// FIXME Auto-generated method stub
		
	}

}
