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

package project.efg.Imports.efgImpl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
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
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImportsUtil.LoggerUtils;
import project.efg.Imports.efgInterface.LoginListenerInterface;
import project.efg.Imports.factory.LoginListenerFactory;
import project.efg.util.EFGImportConstants;

/**
 * Dialog to login to the system.
 */
public class LoginDialog extends JDialog {
	static final long serialVersionUID = 1;

	protected JTextField m_loginNameBox;
	JFrame frame;
	protected JPasswordField m_passwordBox;

	protected boolean isSuccess = false;

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
			
			log = Logger.getLogger(LoginDialog.class);
		} catch (Exception ee) {
		}
	}
	public LoginDialog(JFrame parent,String title, boolean bool) {
		super(parent, title, bool);
		
	}
	/**
	 * Constructor.
	 */
	public LoginDialog(JFrame parent) {
		super(parent, "Login", true);
		this.frame = parent;
		
		
		getContentPane().add(addPanel(), BorderLayout.CENTER);
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}
	/**
	 * Add a panel to the current JFrame
	 * @return
	 */
	public JPanel addPanel(){
		
		//do nothing if close icon is clicked
		this.setDefaultCloseOperation(
			    JDialog.DO_NOTHING_ON_CLOSE);
			this.addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent we) {
			       closeDialog();
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
		LoginListenerInterface loginList = LoginListenerFactory.getLoginListener(this);
		loginButton.addActionListener(loginList);
		getRootPane().setDefaultButton(loginButton);
		getRootPane().registerKeyboardAction(loginList,
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		p.add(loginButton);

		JButton cancelButton = new JButton(EFGImportConstants.EFGProperties.getProperty("LoginDialog.cancelBtn"));

		ActionListener lst = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				closeDialog();
			}
		};
		cancelButton.addActionListener(lst);
		getRootPane().registerKeyboardAction(lst,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		p.add(cancelButton);
		return p;
	}
	public final void closeDialog(){
		this.dispose();
	}
	/**
	 * Get the login name
	 * @return
	 */
	public String getLoginName() {
		return m_loginNameBox.getText();
	}
	/**
	 * Set the login name
	 * @param login
	 */
	
	public void setLoginName(String login){
		this.m_loginNameBox.setText(login);
	}
	/**
	 * Get the password
	 * @return
	 */
	public String getPassword() {
		if(m_passwordBox.getPassword() == null){
			return null;
		}
		return new String(m_passwordBox.getPassword());
	}
	/**
	 * Set the password
	 * @param password
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
	/**
	 * Indicate whether database connection was successful
	 * @param bool - true if successful.
	 */
	public void setSuccess(boolean bool) {
		this.isSuccess = bool;
	}
	/**
	 * 
	 * @return true if successfully connected to database
	 */
	public boolean isSuccess() {
		return this.isSuccess;
	}
	/**
	 * Used to ensure that only one instance of application is running.
	 * File lock mechanism is used.
	 * @return return the lock on the file
	 */
	private static FileLock getLock(){
		FileLock lock = null;
		try {
			RandomAccessFile raf = new RandomAccessFile(EFGImportConstants.LOCK_FILE, "rw");
			FileChannel channel = raf.getChannel();
			
			lock = channel.tryLock();
			
		}
		catch(Exception ee){
			log.error("Error ocurred while getting a lock");
			log.error(ee.getMessage());
		}
		return lock;
	}
	/**
	 * Release the lock on the file if any. 
	 * @param lock
	 */
	private static void releaseLock(FileLock lock){
		
		if(lock == null){
			return;
		}
		try {
			lock.release();
			log.error("Lock released");
		} catch (IOException e) {
			log.error("Could not release locks");
			log.error(e.getMessage());
		}
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
			lock = getLock();
			if (lock == null) {//means a instance is already running
				JOptionPane
						.showMessageDialog(
								null,
								instance_Message,
								"Instance Already Running!!",
								JOptionPane.WARNING_MESSAGE);
				System.exit(1);
			}

			 if (args.length > 0) { //we need more than one args
				catalina_home = args[0].toString();
				
				int index = catalina_home.lastIndexOf("\"");
				if (index > -1) {
					catalina_home = catalina_home.substring(0, index);
					//log.debug("Cat home after: " + catalina_home);
				}
			} else {//display usage message and exit
				
				log.error(usage_message);
				releaseLock(lock);
				log.error("Exiting application..");
				
				System.err.println(usage_message);
				System.exit(1);
			}
			if ((catalina_home != null) && (!catalina_home.trim().equals(""))) {//if catalina_home is found
				// construct from catalina home
				//log.debug("Cat home installer: " + catalina_home);
				LoginDialog dlg = new LoginDialog(null); //create a new dialog
				dlg.setVisible(true);//make it visible
				if (dlg.isSuccess()) {//if user name and password are correct
					String url = EFGImportConstants.EFGProperties.getProperty("dburl");
					//log.debug("url: " + url);
					
					DBObject dbObject = new DBObject(url, dlg.getLoginName(),
							dlg.getPassword());
					menu = new ImportMenu(EFGImportConstants.IMPORT_TITLE, catalina_home,
							dbObject);
					menu.setVisible(true);
				}
				else{
					releaseLock(lock);
				}
			} else {
				StringBuffer message = new StringBuffer();
				message.append("The 'CATALINA_HOME' environment variable  must be set for this application\n");
				message.append("to run successfully.");
				message.append("Consult your administrator on how to set environment variables\n");
				System.err.println(message.toString());
				releaseLock(lock);
				System.exit(1);
			}
		} catch (Exception ee) {
			releaseLock(lock);
			log.error(ee.getMessage());
		} 
	}

	public static void main(String args[]) {
		LoginDialog.callImportMenu(args);
	}
}
