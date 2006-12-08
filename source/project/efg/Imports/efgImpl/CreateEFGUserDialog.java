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

import project.efg.Imports.efgInterface.LoginListenerInterface;
import project.efg.Imports.factory.LoginListenerFactory;
import project.efg.util.EFGImportConstants;

/**
 * Dialog to login to the system.
 */
public class CreateEFGUserDialog extends LoginDialog {
	
	static final long serialVersionUID = 1;

	
	private JPasswordField m_confirmpasswordBox;

	static Logger log = null;
	static String instance_Message =null;
	static String usage_message = null;
	private JFrame frame;
	
	

	/**
	 * Constructor.
	 */
	public CreateEFGUserDialog(JFrame parent) {
		super(parent,"Create a New EFG Super User",true);
		this.frame = parent;
		getContentPane().add(addPanel(), BorderLayout.CENTER);
		pack();
		setResizable(false);
		setLocationRelativeTo(this.frame);	
	}
public JPanel addPanel(){
	this.setDefaultCloseOperation(
		    JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent we) {
		       showWarningMessage();
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
	
	
	pp.add(new JLabel("Confirm Password:"));
	m_confirmpasswordBox = new JPasswordField(16);
	pp.add(m_confirmpasswordBox);

	JPanel p = new JPanel(new EFGDialogLayout());
	p.setBorder(new EmptyBorder(10, 10, 10, 10));
	p.add(pp);

	JButton loginButton = new JButton(EFGImportConstants.EFGProperties.getProperty("LoginDialog.createEFGUserBtn"));
	//use a factory?
	LoginListenerInterface loginList = LoginListenerFactory.getEFGCreateUserListener(this);
	loginButton.addActionListener(loginList);
	getRootPane().setDefaultButton(loginButton);
	getRootPane().registerKeyboardAction(loginList,
			KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW);
	p.add(loginButton);

	JButton cancelButton = new JButton(EFGImportConstants.EFGProperties.getProperty("LoginDialog.cancelBtn"));

	ActionListener lst = new CancelDialogListener(this);
	cancelButton.addActionListener(lst);
	getRootPane().registerKeyboardAction(lst,
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW);
	p.add(cancelButton);
	return p;

	}
	public final void showWarningMessage(){
		if(!this.isSuccess()){
		
		int n = JOptionPane.showConfirmDialog(
			    this.frame,
			    "We strongly recommend that you create an EFG power user \n" + 
			    "instead of using root to log into MySQL.\n" + " Would you like to create an EFG power user?"
			    ,
			    "Warning Message!!",
			    JOptionPane.YES_NO_OPTION);
		
		if(n == JOptionPane.YES_OPTION){
			return;
		}
		
		}
		this.dispose();
	}

	public String getConfirmPassword() {
		if(m_confirmpasswordBox.getPassword() == null){
			return null;
		}
		
		return new String(m_confirmpasswordBox.getPassword());
	}
	
	public void setConfirmPassword(String password) {
		if(password == null){
			log.error("password cannot be null");
			JOptionPane.showMessageDialog(this.frame, 
					"Password cannot be null",
					"Login Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		 m_confirmpasswordBox.setText(password);
	}
	public DBObject getDbObject(){
		if(this.isSuccess()){
		String m_loginName = this.getLoginName();	
		String m_password = new String(this.getPassword());
		String url = EFGImportConstants.EFGProperties.getProperty("urltodb");
		return new DBObject(url, m_loginName,m_password);
		}
		return null;
	}
	
	/**
	 * @author kasiedu
	 *
	 */
	public class CancelDialogListener implements ActionListener {
		CreateEFGUserDialog dialog;
		/**
		 * 
		 */
		public CancelDialogListener(CreateEFGUserDialog dialog) {
			super();
			this.dialog = dialog;
		
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {	
				this.dialog.showWarningMessage();
		}

	}
}