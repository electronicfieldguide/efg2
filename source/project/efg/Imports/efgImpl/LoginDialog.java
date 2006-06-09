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
import java.awt.Frame;
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

import project.efg.Imports.efgInterface.LoginListenerInterface;
import project.efg.util.EFGImportConstants;

import project.efg.Imports.efgImportsUtil.LoggerUtils;
import project.efg.Imports.factory.LoginListenerFactory;

/**
 * Dialog to login to the system.
 */
public class LoginDialog extends JDialog {
	static final long serialVersionUID = 1;

	private JTextField m_loginNameBox;

	private JPasswordField m_passwordBox;

	private boolean isSuccess = false;

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

	/**
	 * Constructor.
	 */
	public LoginDialog(Frame parent) {
		super(parent, "Login", true);
		// setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
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

		JButton saveButton = new JButton(EFGImportConstants.EFGProperties.getProperty("LoginDialog.saveBtn"));
		//use a factory?
		LoginListenerInterface loginList = LoginListenerFactory.getLoginListener(this);
		saveButton.addActionListener(loginList);
		getRootPane().setDefaultButton(saveButton);
		getRootPane().registerKeyboardAction(loginList,
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		p.add(saveButton);

		JButton cancelButton = new JButton(EFGImportConstants.EFGProperties.getProperty("LoginDialog.cancelBtn"));

		ActionListener lst = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		};
		cancelButton.addActionListener(lst);
		getRootPane().registerKeyboardAction(lst,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		p.add(cancelButton);

		getContentPane().add(p, BorderLayout.CENTER);
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	public String getLoginName() {
		return m_loginNameBox.getText();
	}
	public void setLoginName(String login){
		this.m_loginNameBox.setText(login);
	}
	public String getPassword() {
		return new String(m_passwordBox.getPassword());
	}
	public void setPassword(String password){
		this.m_passwordBox.setText(password);
	}

	public void setSuccess(boolean bool) {
		this.isSuccess = bool;
	}

	public boolean isSuccess() {
		return this.isSuccess;
	}
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
	public static void callImportMenu(String args[]) {
		ImportMenu menu = null;
		FileLock lock = null;
		String catalina_home = null;
		try {
			lock = getLock();
			if (lock == null) {
				JOptionPane
						.showMessageDialog(
								null,
								instance_Message,
								"Instance Already Running!!",
								JOptionPane.WARNING_MESSAGE);
				System.exit(1);
			}

			 if (args.length > 0) {
				catalina_home = args[0].toString();
				log.debug("Cat home: " + catalina_home);
				int index = catalina_home.lastIndexOf("\"");
				if (index > -1) {
					catalina_home = catalina_home.substring(0, index);
					log.debug("Cat home after: " + catalina_home);
				}
			} else {
				log.error(usage_message);
				releaseLock(lock);
				log.error("Exiting application..");
				
				System.err.println(usage_message);
				System.exit(1);
			}
			if ((catalina_home != null) && (!catalina_home.trim().equals(""))) {
				// construct from catalina home
				log.debug("Cat home installer: " + catalina_home);
				LoginDialog dlg = new LoginDialog(null);
				dlg.show();
				if (dlg.isSuccess()) {
					String url = EFGImportConstants.EFGProperties.getProperty("dburl");
					log.debug("url: " + url);
					
					DBObject dbObject = new DBObject(url, dlg.getLoginName(),
							dlg.getPassword());
					menu = new ImportMenu("Import Menu", catalina_home,
							dbObject);
					menu.show();
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