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

package project.efg.Import;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



/**
 * Dialog to login to the system.
 */
public class LoginDialog extends JDialog {
    private JTextField     m_loginNameBox;
    private JPasswordField m_passwordBox;
    private boolean isSuccess = false;
    
    static Logger log = null;

    static{
	try{
	    log = Logger.getLogger(LoginDialog.class); 
	}
	catch(Exception ee){
	}
    }
    /**
     * Constructor.
     */
    public LoginDialog(Frame parent) {
	super(parent, "Login", true);
	

	JPanel pp = new JPanel(new DialogLayout2());
	pp.setBorder(new CompoundBorder(
					new EtchedBorder(EtchedBorder.RAISED),
					new EmptyBorder(5,5,5,5)
					)
		     );
	pp.add(new JLabel("User name:"));
	m_loginNameBox = new JTextField(16);
	pp.add(m_loginNameBox);

	pp.add(new JLabel("Password:"));
	m_passwordBox = new JPasswordField(16);
	pp.add(m_passwordBox);

	JPanel p = new JPanel(new DialogLayout2());
	p.setBorder(new EmptyBorder(10, 10, 10, 10));
	p.add(pp);

	JButton saveButton = new JButton("Login");
	LoginListener loginList = new LoginListener(this);
	saveButton.addActionListener(loginList);
	getRootPane().setDefaultButton(saveButton);
	getRootPane().registerKeyboardAction(loginList,
					     KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
					     JComponent.WHEN_IN_FOCUSED_WINDOW);
	p.add(saveButton);

	JButton cancelButton = new JButton("Cancel");
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
    public String getLoginName(){
	return m_loginNameBox.getText();
    }
    public String getPassword(){
	return new String(m_passwordBox.getPassword());
    }
    public void setSuccess(boolean bool){
	this.isSuccess = bool;
    }
    public boolean isSuccess(){
	return this.isSuccess;
    }
    public static void main( String args[] ) {
	LoginDialog dlg = new LoginDialog(null);
	dlg.show();
	if(dlg.isSuccess()){
	    String url =  System.getProperty("dburl") ;
	    DBObject dbObject = new DBObject(url,dlg.getLoginName(), dlg.getPassword());
	    EFGWebAppsDirectoryObject obj = new EFGWebAppsDirectoryObject(null);
	    obj.setImagesDirectory("EFGImages");
	    ImportMenu menu = new ImportMenu(null,"Import Menu" ,true,obj, dbObject);
	    menu.show();
	}
    }
}