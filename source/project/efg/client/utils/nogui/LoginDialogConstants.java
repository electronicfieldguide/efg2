/**
 * 
 */
package project.efg.client.utils.nogui;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;


import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.RegularExpresionConstants;
import project.efg.util.utils.DBObject;
import project.efg.util.utils.EFGRDBImportUtils;

/**
 * @author jacob.asiedu
 *
 */
public class LoginDialogConstants {
	

	static Logger log = null;
	public static String instance_Message =null;
	public static String usage_message = null;
	
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
			
			log = Logger.getLogger(LoginDialogConstants.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * @param pathToServer
	 */
	public static String getDefaultMessage() {
		
		String property = 
			EFGImportConstants.EFGProperties.getProperty("efg.local.resource",
					EFGImportConstants.EFG_RESOUCRES_REPOSITORY);
		

		StringBuffer buffer = new StringBuffer();
		buffer.append("Application will place generated resources\n( media resources, generated templates etc) in \n");
		buffer.append("\"");
		buffer.append(property);
		buffer.append("\" directory  \n");
		buffer.append("The directories inside the above named folder must be copied\n " +
				"to the efg2 web application running on your server\n");
		buffer.append("See the docs on how to copy folders to the web application\n"); 
		
	return buffer.toString();	
		
	}
	
	private static  String checkServerLocation(String pathToServer) {
		
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
	
			int j = 
				LoginDialogConstants.pruneServerLocationsProperties(
						properties, buffer, pathToServer);
				
			 if(j > 0) {//if we already added something to the buffer
				 buffer.append(","); 
			 }
			 
			 buffer.append(WorkspaceResources.convertFileNameToURLString(pathToServer));
			 EFGImportConstants.EFGProperties.setProperty(
						"efg.serverlocations.current",
						WorkspaceResources.convertFileNameToURLString(pathToServer));//set current
			
			 EFGImportConstants.EFGProperties.setProperty(
					"efg.serverlocations.lists",buffer.toString());
			 boolean bool = WorkspaceResources.computeMediaResourcesHome();
			 if(!bool){
				 return null;
			 }
			 bool = WorkspaceResources.computeTemplatesHome();
			 if(!bool){
				 return null;
			 }
			return pathToServer;
		 }
	
		EFGImportConstants.EFGProperties.setProperty(
						"efg.serverlocations.lists",
						WorkspaceResources.convertFileNameToURLString(pathToServer));
		EFGImportConstants.EFGProperties.setProperty(
				"efg.serverlocations.current",
				WorkspaceResources.convertFileNameToURLString(pathToServer)); 
		 boolean bool = WorkspaceResources.computeMediaResourcesHome();
		 if(!bool){
			 return null;
		 }
		 bool = WorkspaceResources.computeTemplatesHome();
		 if(!bool){
			 return null;
		 }
			return pathToServer;
	}
	/**
	 * Used to ensure that only one instance of application is running.
	 * File lock mechanism is used.
	 * @return return the lock on the file
	 */
	public static FileLock getLock(){
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
	public static void releaseLock(FileLock lock){
		
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
	 * @param isDefault
	 * @param string 
	 * 
	 */
	public static String checkServerRoot(String pathToServer, boolean isDefault) {
		String property = 
			EFGImportConstants.EFGProperties.getProperty(
					"efg.serverlocation.checked",
					EFGImportConstants.EFG_TRUE);
       boolean isSelected = true;
        if(!property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
        	isSelected = false;
        }
       pathToServer =  checkServerLocation(pathToServer);

       if(pathToServer == null){
    	   return null;
       }
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
			
			return buffer.toString();
		}
		
	
	}	
		return null;
}

	/**
	 * Remove file locations that no longer exists
	 *
	 */
	public static int pruneServerLocationsProperties(
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
	public static boolean isLocked(){
		FileLock lock = LoginDialogConstants.getLock();
		if (lock == null) {//means a instance is already running
			return false;
		}
		return true;
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
	public static String getServerLocation(String catalina_home) throws Exception{
		String serverRoot = 
			EFGImportConstants.EFGProperties.getProperty(
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
		File file = null;
		try {
		file = new File(catalina_home);
		}
		catch(Exception ee) {
			
		}
		boolean isCatExists = true;
		boolean isDefault = false;
		URL url = null;
	
			if(file == null || !file.exists()) {//suplied args does not exists
				isCatExists = false;
				try {
					String property = 
						EFGImportConstants.EFGProperties.getProperty("efg.local.repository",
								EFGImportConstants.EFG_LOCAL_REPOSITORY);
					
					url = 
						LoginDialogConstants.class.getResource(property);
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
		
		
		return serverRoot;
	}
	/**
	 * 
	 */
	public static boolean alreadyLoaded(DBObject dbObject) {
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
	/*public static  void loadSampleData(DBObject dbObject) {
		if(!LoginDialogConstants.alreadyLoaded(dbObject)){
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
	}*/
	/**
	 * 
	 */
	private static String readSampleDataDisplayName() {
		return EFGImportConstants.EFGProperties
				.getProperty(EFGImportConstants.SAMPLE_NEW_DISPLAY_NAME);

	}

}
