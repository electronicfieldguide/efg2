/**
 * 
 */
package project.efg.client.interfaces.nogui;

/**
 * @author jacob.asiedu
 *
 */
public interface LoginDialogInterface {
	public abstract void closeEFGDialog();
	public abstract void setServerRoot(String pathToServer, boolean isDefault);
	/**
	 * Get the login name
	 * @return
	 */
	public abstract String getLoginName();

	/**
	 * Set the login name
	 * @param login
	 */

	public abstract void setLoginName(String login);

	/**
	 * Get the password
	 * @return
	 */
	public abstract String getPassword();

	/**
	 * Set the password
	 * @param password
	 */
	public abstract void setPassword(String password);

	/**
	 * Indicate whether database connection was successful
	 * @param bool - true if successful.
	 */
	public abstract void setSuccess(boolean bool);

	/**
	 * 
	 * @return true if successfully connected to database
	 */
	public abstract boolean isSuccess();

}