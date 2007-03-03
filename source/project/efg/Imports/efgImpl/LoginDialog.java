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
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLDecoder;
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
import org.springframework.jdbc.core.JdbcTemplate;

import project.efg.Imports.efgImportsUtil.LoggerUtils;
import project.efg.Imports.efgImportsUtil.ResourceWarning;
import project.efg.Imports.efgInterface.LoginListenerInterface;
import project.efg.Imports.factory.LoginListenerFactory;
import project.efg.Imports.rdb.EFGRDBImportUtils;
import project.efg.util.CreateSampleDataThread;
import project.efg.util.EFGImportConstants;
import project.efg.util.RegularExpresionConstants;
import project.efg.util.ServerLocator;
import project.efg.util.WorkspaceResources;

/**
 * Dialog to login to the system.
 */
public class LoginDialog extends JDialog {

		/**
		 * Remove file locations that no longer exists
		 *
		 */
		private static int pruneServerLocationsProperties(
				String[] properties,
				StringBuffer buffer,
				String pathToServer) {
			String current = 
				EFGImportConstants.EFGProperties.getProperty(
						"efg.serverlocations.current");
			if(current != null) {
				current = WorkspaceResources.removeLastSpaceFromPath(current.trim());
			}
			 int j = 0;
			 boolean isFound = false;
			 for(int i = 0 ; i < properties.length;i++) {
				 String property = properties[i];
				 if(property.trim().equals("")) {
					 continue;
				 }
				 File file = new File(property);
				 if(file.exists()) {//write only the directories that exists
					 if(property.equalsIgnoreCase(
							 WorkspaceResources.convertFileNameToURLString(pathToServer))) {//skip over the path to server if cound
						 continue;
					 }
					 if(property.equalsIgnoreCase(current)) {
						 
						 EFGImportConstants.EFGProperties.setProperty(
									"efg.serverlocations.current", 
									WorkspaceResources.convertFileNameToURLString(current));
						 WorkspaceResources.computeMediaResourcesHome();
						 WorkspaceResources.computeTemplatesHome();
						 isFound = true;
					 }
					 else {
						 if(!isFound) {//set the current to something else
							 EFGImportConstants.EFGProperties.setProperty(
										"efg.serverlocations.current",
										WorkspaceResources.convertFileNameToURLString(current));
							 WorkspaceResources.computeMediaResourcesHome();
							 WorkspaceResources.computeTemplatesHome();
						 }
					 }
					
					 if(j > 0) {//add comma if we have more than one valid location
						 buffer.append(",");
					 }
					 buffer.append(property);//add the property
					 j++;
				 }
			 }
			 return j;
		}
		/**
		 * @param pathToServer
		 */
		private void writeDefaultMessage() {
			
			String property = 
				EFGImportConstants.EFGProperties.getProperty("efg.local.resource",
						EFGImportConstants.EFG_RESOUCRES_REPOSITORY);
			

			StringBuffer buffer = new StringBuffer();
			buffer.append("Application will place generated resources\n( media resources, generated templates etc) in \n");
			buffer.append("'");
			buffer.append(property);
			buffer.append("' directory  \n");
			buffer.append("The directories inside the above named folder must be copied\n " +
					"to the efg2 web application running on your server\n");
			buffer.append("See the docs on how to copy folders to the web application\n"); 
			
			
			JOptionPane.showConfirmDialog(frame, 
					buffer.toString(), 
					"Resources Will be placed in a local repository", 
					JOptionPane.YES_OPTION, 
					JOptionPane.INFORMATION_MESSAGE);
	
			
		}
		/**
		 * @param isDefault
		 * @param string 
		 * 
		 */
		private void setServerRoot(String pathToServer, boolean isDefault) {
			String property = 
				EFGImportConstants.EFGProperties.getProperty(
						"efg.serverlocation.checked",
						EFGImportConstants.EFG_TRUE);
	       boolean isSelected = true;
	        if(!property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
	        	isSelected = false;
	        }
	       pathToServer =  checkServerLocation(pathToServer);

	        property = 
				EFGImportConstants.EFGProperties.getProperty(
						"efg.showchangedirectorymessage.checked",EFGImportConstants.EFG_TRUE);
	       
			if(isSelected) {
				
			
			if(property.equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("<html>");
				buffer.append("<p>If the folder you are about to select is not the root</p>");
				buffer.append("<p>of your Tomcat server , then be aware of the following: </p>");
				buffer.append("<p>1. Application generated resources ( media resources,</p>");
				buffer.append("<p>generated templates etc)</p>");
				buffer.append("<p>will be placed in the folder you are about to select. </p>");
				buffer.append("<p>2. You will have to physically copy these resources</p>" +
						"<p> to an efg2 web application.</p>");
				buffer.append("<p> See the docs on how to copy resources to the web application</p>"); 
				buffer.append("</html>");
				
				ResourceWarning rw = 
					new ResourceWarning(frame,
						"Changing Directory",buffer.toString(),true);
				rw.setVisible(true);
				isDefault = false;
				
			}
			if(isDefault) {
				this.writeDefaultMessage();
			}
		
			ServerLocator locator = new ServerLocator(frame,
					pathToServer,
					true,
					"efg.serverlocations.lists",
					"efg.serverlocations.current",
					"efg.serverlocation.checked",
					"Prompt me for server location every time");
			
	
			locator.setVisible(true);
		}		
	}
	

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

		JButton cancelButton = new JButton(
				EFGImportConstants.EFGProperties.getProperty("LoginDialog.cancelBtn"));

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

	private String checkServerLocation(String pathToServer) {
		
		 String propertyStr = 
				EFGImportConstants.EFGProperties.getProperty(
						"efg.serverlocations.lists"
						);
		if(pathToServer != null ) {
			pathToServer = WorkspaceResources.removeLastSpaceFromPath(pathToServer);
		}
		 if(propertyStr != null && 
				 !propertyStr.trim().equalsIgnoreCase("")) {
			 
			 String[] properties = 
				 propertyStr.split(RegularExpresionConstants.COMMASEP);
			 	
			 StringBuffer buffer = new StringBuffer();
	
			int j =  pruneServerLocationsProperties(properties,
						buffer,
						pathToServer);
			 if(j > 0) {//if we already added something to the buffer
				 buffer.append(","); 
			 }
			 
			 buffer.append(WorkspaceResources.convertFileNameToURLString(pathToServer));
			 EFGImportConstants.EFGProperties.setProperty(
						"efg.serverlocations.current",
						WorkspaceResources.convertFileNameToURLString(pathToServer));//set current
			
			 EFGImportConstants.EFGProperties.setProperty(
					"efg.serverlocations.lists",buffer.toString());
			 WorkspaceResources.computeMediaResourcesHome();
			 WorkspaceResources.computeTemplatesHome();
			return pathToServer;
		 }
	
		EFGImportConstants.EFGProperties.setProperty(
						"efg.serverlocations.lists",
						WorkspaceResources.convertFileNameToURLString(pathToServer));
		EFGImportConstants.EFGProperties.setProperty(
				"efg.serverlocations.current",
				WorkspaceResources.convertFileNameToURLString(pathToServer)); 
		 WorkspaceResources.computeMediaResourcesHome();
		 WorkspaceResources.computeTemplatesHome();
			return pathToServer;
	}
	/**
	 * 
	 */

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
			try {
				catalina_home = args[0];
			}
			catch(Exception eex) {
				
			}
			
			String serverRoot = EFGImportConstants.EFGProperties.getProperty(
							"efg.serverlocations.current");

			
			
			
			if((serverRoot != null && 
					!serverRoot.trim().equals(""))){
				serverRoot = parseServerRoot(serverRoot);
				serverRoot = WorkspaceResources.removeLastSpaceFromPath(serverRoot);
				EFGImportConstants.EFGProperties.setProperty(
				"efg.serverlocations.current",serverRoot);
			}
		
			if((catalina_home == null || 
					catalina_home.trim().equals(""))){
				if((serverRoot != null && 
						!serverRoot.trim().equals(""))){
					catalina_home = serverRoot;
				}
			}
			File file = new File(catalina_home);
			boolean isCatExists = true;
			boolean isDefault = false;
			URL url = null;
			if(!file.exists()) {//suplied args does not exists
				isCatExists = false;
				try {
					String property = 
						EFGImportConstants.EFGProperties.getProperty("efg.local.repository",
								EFGImportConstants.EFG_LOCAL_REPOSITORY);
					
					url = 
						LoginDialog.class.getResource(property);
					catalina_home  = URLDecoder.decode(url.getFile(), "UTF-8");
					file = new File(catalina_home);
					if(file.exists()) {//if default exists
						isCatExists = true;
						isDefault = true;
					}
				}
				catch(Exception ee) {
					
				}
				
			}
			if((serverRoot == null ||
					serverRoot.trim().equals("")) && 
					(!isCatExists)) {
				
				throw new Exception("Application could not find the Tomcat server.");
			}
			else if((serverRoot == null ||
					serverRoot.trim().equals("")) && 
					(isCatExists)) {
				serverRoot = catalina_home;
				
				
			}
			
				
				/*
				 * see if catalina_home environment
				 * variable is checked
				*/
		

			 
			if ((serverRoot != null) &&
					(!serverRoot.trim().equals(""))) {//if catalina_home is found
				// construct from catalina home
				//log.debug("Cat home installer: " + catalina_home);
				
				LoginDialog dlg = new LoginDialog(null); //create a new dialog
				dlg.setVisible(true);//make it visible
				if (dlg.isSuccess()) {//if user name and password are correct
					String urldb = 
						EFGImportConstants.EFGProperties.getProperty("dburl");
					//log.debug("url: " + url);
					
					DBObject dbObject = new DBObject(
							urldb, dlg.getLoginName(),
							dlg.getPassword());
					
					dlg.setServerRoot(serverRoot,isDefault);
				
					//if it is written 
					
					dlg.loadSampleData(dbObject);
					menu = new ImportMenu(
							EFGImportConstants.IMPORT_TITLE, 
							serverRoot,
							dbObject);
					menu.setVisible(true);
				}
				else{
					releaseLock(lock);
				}
			} else {
				StringBuffer message = new StringBuffer();
				
				message.append("The application cannot find your workspace directory\n");
				message.append("Please read the docs on how to set your workspace directory\n");
				System.err.println(message.toString());
				releaseLock(lock);
				System.exit(1);
			}
		} catch (Exception ee) {
			releaseLock(lock);
			log.error(ee.getMessage());
			JOptionPane
			.showMessageDialog(
					null,
					ee.getMessage(),
					"Error Message",
					JOptionPane.ERROR_MESSAGE);
		} 
	}
	/**
	 * @param serverRoot
	 * @return
	 */
	private static String parseServerRoot(String serverRoot) {
		if(serverRoot != null) {
			serverRoot = serverRoot.trim();
			int index = serverRoot.lastIndexOf(RegularExpresionConstants.FORWARD_SLASH);
			if(index > -1) {
				if(index >= (serverRoot.length()-1)) {
					serverRoot = serverRoot.substring(0,index);
					serverRoot = serverRoot.trim();
					serverRoot = serverRoot + RegularExpresionConstants.FORWARD_SLASH;
				}
			}
		}
		
		return serverRoot;
	}
	private boolean alreadyLoaded(DBObject dbObject) {
		JdbcTemplate jdbcTemplate =  EFGRDBImportUtils.getJDBCTemplate(dbObject);
		String displayName= readSampleDataDisplayName();
		StringBuffer query = new StringBuffer();
		query.append("SELECT DS_METADATA");
		query.append(" FROM ");
		query.append(EFGImportConstants.EFG_RDB_TABLES);
		query.append(" WHERE DISPLAY_NAME = \"");
		query.append(displayName);
		query.append("\"");
		try {
			java.util.List list = 
				EFGRDBImportUtils.executeQueryForList(
					jdbcTemplate, query.toString(), 1);
			if(list == null || list.size()== 0){
				return false;
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
			//e.printStackTrace();
		}
		
		return true;
	}
	/**
	 * 
	 */
	private String readSampleDataDisplayName() {
		return EFGImportConstants.EFGProperties
				.getProperty(EFGImportConstants.SAMPLE_NEW_DISPLAY_NAME);

	}
	/**
	 * 
	 */
	private void loadSampleData(DBObject dbObject) {
		if(!alreadyLoaded(dbObject)){
			String property =
				EFGImportConstants.EFGProperties.getProperty(
						"efg.sampledata.loaded", EFGImportConstants.EFG_FALSE
						);
			if(property.equals(EFGImportConstants.EFG_FALSE)) {
				//put progress bar here
				CreateSampleDataThread dataT =
					new CreateSampleDataThread(this.frame,dbObject);
				 dataT .start();
				 EFGImportConstants.EFGProperties.setProperty(
							"efg.sampledata.loaded", EFGImportConstants.EFG_TRUE
							);
			}
		}
	}
	public static void main(String args[]) {
		LoginDialog.callImportMenu(args);
	}
}
