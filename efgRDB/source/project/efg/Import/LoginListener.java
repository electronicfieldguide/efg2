package project.efg.Import;

/**
 * LoginListener.java
 *
 *
 * Created: Thu Feb 23 10:07:17 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */
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

public class LoginListener extends project.efg.Import.LoginAbstractListener {
    private Connection conn;
    private JTextField m_loginNameBox;
    private JPasswordField m_passwordBox;
    private LoginDialog dialog;

    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(project.efg.Import.LoginListener.class); 
	}
	catch(Exception ee){
	}
    }
    public LoginListener(LoginDialog dialog){
	this.dialog = dialog;
	EFGRDBImportUtils.init();
    }
    public void actionPerformed(ActionEvent evt) {
	String m_loginName = this.dialog.getLoginName();
	String m_password = new String(this.dialog.getPassword());
	String url = System.getProperty("urltodb");
	
	
	LoginModule myLog = new LoginModule();
	this.conn = (Connection)myLog.login(m_loginName, m_password,url);
	if (this.conn == null){
	    JOptionPane.showMessageDialog(
					  null,
					  "System cannot login probably " + 
					  "because your username " + 
					  "and password is wrong or " + 
					  "that MySQL is not running..Quiting application.!!", 
					  "Login Error",
					  JOptionPane.ERROR_MESSAGE
					  );
	    this.dialog.setSuccess(false);
	    System.exit(1);
	}
	File file = new File(".setup");
	if(!file.exists()){//so run setup
	    if(!runSetUp()){
		JOptionPane.showMessageDialog(null,
					      "An error occured while running set up." +  
					      "Please view the logs to find errors",
					      "Login Error",
					      JOptionPane.ERROR_MESSAGE); 
		this.dialog.setSuccess(false);
		System.exit(1);
	    }
	}
	this.dialog.setSuccess(true);
	this.dialog.dispose();
	log.info("Set up run successfully");

	
	//this.dialog.dispose();
	//start a new thread?

    }
    private boolean runSetUp(){
	boolean isRun = true;
	if(!RunSetUp.runSetUp(this.conn)){
	    isRun = false;
	    File file = new File(".setup");
	    if(file.exists()){
		file.delete();
	    }
	}
	else{
	    try{
		File file = new File(".setup");
		file.createNewFile();
	    }
	    catch(IOException ee){
		
	    }
	}
	return isRun;
    }
} // LoginListener
