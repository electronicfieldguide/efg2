/** 
 *  Copyright 1999-2002 Matthew Robinson and Pavel Vorobiev. 
 *  All Rights Reserved. 
 * 
 *  =================================================== 
 *  This program contains code from the book "Swing" 
 *  2nd Edition by Matthew Robinson and Pavel Vorobiev 
 *  http://www.spindoczine.com/sbe 
 *  =================================================== 
 * 
 *  The above paragraph must be included in full, unmodified 
 *  and completely intact in the beginning of any source code 
 *  file that references, copies or uses (in any way, shape 
 *  or form) code contained in this file. 
 */

package project.efg.client.impl.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.channels.FileLock;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import project.efg.client.factory.gui.GUIFactory;
import project.efg.client.interfaces.gui.LoginListenerInterface;
import project.efg.client.interfaces.nogui.LoginDialogInterface;
import project.efg.client.utils.nogui.LoginDialogConstants;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.DBObject;

/**
 * Dialog to login to the system.
 */
public class LoginDialog extends JDialog implements LoginDialogInterface {


  static final long serialVersionUID = 1;
  static Logger log = null;
	protected JTextField m_loginNameBox;
	protected boolean isSuccess = false;
	JFrame frame;
	protected JPasswordField m_passwordBox;

	static {
		try {
		
			log = Logger.getLogger(LoginDialog.class);
		} catch (Exception ee) {
		}
	}
	public LoginDialog(JFrame parent,String title, boolean bool) {
		super(parent, title, bool);
		this.frame = parent;
		getContentPane().add(addPanel(), BorderLayout.CENTER);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		
	}
	/**
	 * Constructor.
	 */
	public LoginDialog() {
		this(null, "Login", true);
	
	}
	/**
	 * Add a panel to the current JFrame
	 * @return
	 */
	public JPanel addPanel(){
		
		//do nothing if close icon is clicked
		this.setDefaultCloseOperation(
			    WindowConstants.DO_NOTHING_ON_CLOSE);
			this.addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent we) {
			       closeEFGDialog();
			    }
			});
		
		JPanel pp = new JPanel(new EFGDialogLayout());
		pp.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED),
				new EmptyBorder(5, 5, 5, 5)));
		pp.add(new JLabel("User name:"));
		m_loginNameBox = new JTextField(16);
		pp.add(m_loginNameBox);

		pp.add(new JLabel("Password:"));
		m_passwordBox = new JPasswordField(16);
		pp.add(m_passwordBox);

		JPanel p = new JPanel(new EFGDialogLayout());
		p.setBorder(new EmptyBorder(10, 10, 10, 10));
		p.add(pp);

		JButton loginButton = new JButton(EFGImportConstants.EFGProperties.getProperty("LoginDialog.loginBtn"));
		//Get a Listener for the button
		LoginListenerInterface loginList = GUIFactory.getLoginListener(this);
		loginButton.addActionListener(loginList);
		getRootPane().setDefaultButton(loginButton);
		getRootPane().registerKeyboardAction(loginList,
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		p.add(loginButton);

		JButton cancelButton = new JButton(
				EFGImportConstants.EFGProperties.getProperty("LoginDialog.cancelBtn"));

		ActionListener lst = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				closeEFGDialog();
			}
		};
		cancelButton.addActionListener(lst);
		getRootPane().registerKeyboardAction(lst,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		p.add(cancelButton);
		return p;
	}
	
	/* (non-Javadoc)
	 * @see project.efg.client.impl.gui.LoginDialogInterface#getLoginName()
	 */
	public String getLoginName() {
		return m_loginNameBox.getText();
	}
	/* (non-Javadoc)
	 * @see project.efg.client.impl.gui.LoginDialogInterface#setLoginName(java.lang.String)
	 */
	
	public void setLoginName(String login){
		this.m_loginNameBox.setText(login);
	}
	/* (non-Javadoc)
	 * @see project.efg.client.impl.gui.LoginDialogInterface#getPassword()
	 */
	public String getPassword() {
		if(m_passwordBox.getPassword() == null){
			return null;
		}
		return new String(m_passwordBox.getPassword());
	}
	/* (non-Javadoc)
	 * @see project.efg.client.impl.gui.LoginDialogInterface#setPassword(java.lang.String)
	 */
	public void setPassword(String password){
		if(password == null){
			log.error("password cannot be null");
			JOptionPane.showMessageDialog(this.frame, 
					"Password cannot be null",
					"Login Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.m_passwordBox.setText(password);
	}
	/* (non-Javadoc)
	 * @see project.efg.client.impl.gui.LoginDialogInterface#setSuccess(boolean)
	 */
	public void setSuccess(boolean bool) {
		this.isSuccess = bool;
	}
	/* (non-Javadoc)
	 * @see project.efg.client.impl.gui.LoginDialogInterface#isSuccess()
	 */
	public boolean isSuccess() {
		return this.isSuccess;
	}
	/**
	 * 
	 */
	
	/* (non-Javadoc)
	 * @see project.efg.client.interfaces.nogui.LoginDialogInterface#closeEFGDialog()
	 */
	public void closeEFGDialog() {
		this.dispose();
		
	}
	/**
	 * Start the import application
	 * @param args
	 */
	public static void callImportMenu(String args[]) {
		ImportMenu menu = null;
		FileLock lock = null;
		String catalina_home = null;
		try {
			if(!LoginDialogConstants.isLocked()){
				JOptionPane
						.showMessageDialog(
								null,
								LoginDialogConstants.instance_Message,
								"Instance Already Running!!",
								JOptionPane.WARNING_MESSAGE);
				System.exit(1);
			}
			try {
				catalina_home = args[0];
			}
			catch(Exception eex) {
				
			}
			String serverRoot = LoginDialogConstants.getServerLocation(catalina_home);
			boolean isDefault = false;

			if ((serverRoot != null) &&
					(!serverRoot.trim().equals(""))) {//if catalina_home is found

				LoginDialog dlg = new LoginDialog(); //create a new dialog
				dlg.setVisible(true);//make it visible
				if (dlg.isSuccess()) {//if user name and password are correct
					String urldb = 
						EFGImportConstants.EFGProperties.getProperty("dburl");
					DBObject dbObject = new DBObject(
							urldb, dlg.getLoginName(),
							dlg.getPassword());
					
					dlg.setServerRoot(serverRoot,isDefault);
					//find out if a file is present. If it is load sample data
					

					
					menu = new ImportMenu(
							EFGImportConstants.IMPORT_TITLE, 
							serverRoot,
							dbObject);
					menu.setVisible(true);
				}
				else{
					LoginDialogConstants.releaseLock(lock);
				}
			} else {
				StringBuilder message = new StringBuilder();
				
				message.append("The application cannot find your workspace directory\n");
				message.append("Please read the docs on how to set your workspace directory\n");
				System.err.println(message.toString());
				LoginDialogConstants.releaseLock(lock);
				System.exit(1);
			}
		} catch (Exception ee) {
			LoginDialogConstants.releaseLock(lock);
			ee.printStackTrace();
			log.error(ee.getMessage());
			JOptionPane
			.showMessageDialog(
					null,
					ee.getMessage(),
					"Error Message",
					JOptionPane.ERROR_MESSAGE);
		} 
	}

	public static void main(String args[]) {
		LoginDialog.callImportMenu(args);
	}

	
	/**
	 * @param isDefault
	 * @param string 
	 * 
	 */
	public void setServerRoot(String pathToServer, boolean isDefault) {
		
		
		String buffer = 
			LoginDialogConstants.checkServerRoot(pathToServer,isDefault);
		if(buffer != null){
			isDefault = false;
		}
		
		
		if(isDefault) {
			this.writeDefaultMessage();
		}
	}
	/**
	 * @param pathToServer
	 */
	private void writeDefaultMessage() {
		String buffer = LoginDialogConstants.getDefaultMessage();
		JOptionPane.showConfirmDialog(frame, 
				buffer, 
				"Resources Will be placed in a local repository", 
				JOptionPane.YES_OPTION, 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	
}
