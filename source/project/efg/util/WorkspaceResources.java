/**
 * 
 */
package project.efg.util;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;


/**
 * @author kasiedu
 *
 */
public class WorkspaceResources {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(WorkspaceResources.class);
		} catch (Exception ee) {
	}
	}
	private static String[] defaultDimensions;
	public static void computeTemplatesHome() {
		
		String mutex = "";
		synchronized (mutex) {
			String applicationHome = 
				EFGImportConstants.EFGProperties.getProperty("efg.serverlocations.current");
			
	
			
			if(applicationHome == null || applicationHome.trim().equals("")) {
				JOptionPane.showConfirmDialog(null,
						"Application cannot find the server root because it was deleted.\n " +
						"Please read our documentation on how to set the server " +
						" directory\n",
						"Cannot find server root directory", 
						JOptionPane.ERROR_MESSAGE);
				
				return;
			}
			
			StringBuffer tmpFile = new StringBuffer();
			tmpFile.append(applicationHome);
			tmpFile.append(File.separator);
			tmpFile.append(EFGImportConstants.EFG_WEB_APPS);
			tmpFile.append(File.separator);
			tmpFile.append(EFGImportConstants.EFG_APPS);
			
			File f = new File(convertFileNameToURLString(tmpFile.toString()));
			if(!f.exists()) {
				f.mkdirs();
			}
			String templatesHome = EFGImportConstants.EFGProperties.getProperty("efg.templates.home");
			tmpFile.append(File.separator);
			tmpFile.append(templatesHome.replace('/', File.separatorChar));
	
			//convert to uri
			String newFile = convertFileNameToURLString(tmpFile.toString());
			f = new File(newFile);
			if(!f.exists()) {
				f.mkdirs();
			}
			EFGImportConstants.EFGProperties.setProperty("efg.templates.home.current",newFile);
		}
		
	}
	public static void computeMediaResourcesHome() {
		String mutex = "";
		synchronized (mutex) {
			String applicationHome = 
				EFGImportConstants.EFGProperties.getProperty("efg.serverlocations.current");
			
	
			
			if(applicationHome == null || applicationHome.trim().equals("")) {
				JOptionPane.showConfirmDialog(null,
						"Application cannot find the server root because it was deleted.\n " +
						"Please read our documentation on how to set the server " +
						" directory\n",
						"Cannot find server root directory", 
						JOptionPane.ERROR_MESSAGE);
				
				return;
			}
			
			StringBuffer tmpFile = new StringBuffer();
			tmpFile.append(applicationHome);
			tmpFile.append(File.separator);
			tmpFile.append(EFGImportConstants.EFG_WEB_APPS);
			tmpFile.append(File.separator);
			tmpFile.append(EFGImportConstants.EFG_APPS);
			
			File f = new File(convertFileNameToURLString(tmpFile.toString()));
			if(!f.exists()) {
				f.mkdirs();
			}
			String imagesHome = EFGImportConstants.EFGProperties.getProperty("efg.images.home");
			tmpFile.append(File.separator);
			tmpFile.append(imagesHome);
	
			//convert to uri
			String newFile = convertFileNameToURLString(tmpFile.toString());
			f = new File(newFile);
			if(!f.exists()) {
				f.mkdirs();
			}
			EFGImportConstants.EFGProperties.setProperty("efg.mediaresources.home.current",newFile);
		}
		
	}
	public static synchronized String[] getDefaultDimensions() {
		
		if(defaultDimensions == null) {
			String maxDimStr = EFGImportConstants.EFGProperties
					.getProperty(EFGImagesConstants.MAX_DIM_STR);
	
	
			try {
				defaultDimensions = maxDimStr.split(RegularExpresionConstants.COMMASEP);
			} catch (Exception ee) {
				
				
			}
		}
		
	return defaultDimensions;
	}

	private static void writeDefaultDimesions() {
		String[] dimensions = getDefaultDimensions();
		
		StringBuffer buffer = new StringBuffer();
		for(int i= dimensions.length;i > 0;i--) {
			
			if(i < dimensions.length) {
				buffer.append(",");
			}
			buffer.append(dimensions[i-1]);
		}
		EFGImportConstants.EFGProperties.setProperty(
				"efg.thumbnails.dimensions.lists",
				buffer.toString());
		EFGImportConstants.EFGProperties.setProperty(
				"efg.thumbnails.dimensions.current",
				dimensions[dimensions.length-1]);
		
		
		
		
	}
	public static String convertFileNameToURLString(String filename) {
		if(filename == null || filename.trim().equals("")) {
			return null;
		}
		try {
			File file = new File(filename);
			URI uri = file.toURI();
			URL url = uri.toURL();
			return URLDecoder.decode(url.getFile(), "UTF-8");
		} catch (Exception e) {
			
			log.error(e.getMessage());
		}
		
		return null;
	}
	public static synchronized void doHouseKeepingbeforeClosing() {
       	try {
        	
       		StringBuffer buffer = new StringBuffer();
			
       		
        	/**
       		 * The history of server locations chosen by user
       		 */
      		String property =
       			EFGImportConstants.EFGProperties.getProperty("efg.serverlocations.lists");
       		

       		if(property != null && !property.trim().equals("")) {
	       		buffer.append("efg.serverlocations.lists=");
	       		buffer.append(property);
	       		buffer.append("\n");
       		}
        	/**
       		 * The current server location chosen by user
       		 */
	
       		property = EFGImportConstants.EFGProperties.getProperty(
					"efg.serverlocations.current");
       		if(property != null && !property.trim().equals("")) {
				buffer.append("efg.serverlocations.current=");
				buffer.append(property);
				buffer.append("\n");
       		}
			/*
			 * Write the current media resources home
			 * this depends on the current server root 
			 * being set correctly 
			 */
       		property = EFGImportConstants.EFGProperties.getProperty(
			"efg.mediaresources.home.current");
       		if(property != null && !property.trim().equals("")) {
				buffer.append("efg.mediaresources.home.current=");
				buffer.append(EFGImportConstants.EFGProperties.getProperty(
						"efg.mediaresources.home.current"));
				buffer.append("\n"); 
       		}

			//efg.mediaresources.home.current
			/*
			 * Write the current templates home
			 * this depends on the current server root 
			 * being set correctly 
			 */
       		property = EFGImportConstants.EFGProperties.getProperty(
			"efg.templates.home.current");
       		if(property != null && !property.trim().equals("")) {	
				buffer.append("efg.templates.home.current=");
				buffer.append(EFGImportConstants.EFGProperties.getProperty(
						"efg.templates.home.current"));
				buffer.append("\n");
	       	}
       		
       		
       		property = EFGImportConstants.EFGProperties.getProperty(
			"efg.thumbnails.dimensions.lists");
       		if(property == null || property.trim().equals("")) {
       			writeDefaultDimesions();
       		}
       		
       		
			buffer.append("efg.thumbnails.dimensions.lists=");
			buffer.append(EFGImportConstants.EFGProperties.getProperty(
			"efg.thumbnails.dimensions.lists"));
			buffer.append("\n");
			
			//true if sample is loaded, false otherwise
			buffer.append("efg.sampledata.loaded=");
			buffer.append(EFGImportConstants.EFGProperties.getProperty(
					"efg.sampledata.loaded"));
			buffer.append("\n");
			

			buffer.append("efg.thumbnails.dimensions.current=");
			buffer.append(EFGImportConstants.EFGProperties.getProperty(
					"efg.thumbnails.dimensions.current"));
			buffer.append("\n");
       		
			
			if(EFGImportConstants.EFGProperties.getProperty(
					"efg.data.last.file") != null) {
				buffer.append("efg.data.last.file=");	
				buffer.append(EFGImportConstants.EFGProperties.getProperty(
						"efg.data.last.file",null));
				buffer.append("\n");
			}
			
       		buffer.append("efg.serverlocation.checked=");	
			buffer.append(EFGImportConstants.EFGProperties.getProperty(
					"efg.serverlocation.checked",EFGImportConstants.EFG_TRUE));
			buffer.append("\n");
			

			buffer.append("efg.thumbnails.dimensions.checked="); 
			buffer.append(EFGImportConstants.EFGProperties.getProperty(
		        			"efg.thumbnails.dimensions.checked",
		        			EFGImportConstants.EFG_TRUE));
			buffer.append("\n");   		
		        		
			
			buffer.append("efg.showdismiss.checked="); 
			buffer.append(EFGImportConstants.EFGProperties.getProperty(
					"efg.showdismiss.checked",EFGImportConstants.EFG_TRUE));
			buffer.append("\n");  
			
			
			buffer.append("efg.imagemagicklocation.checked="); 
			buffer.append(EFGImportConstants.EFGProperties.getProperty(
					"efg.imagemagicklocation.checked",EFGImportConstants.EFG_TRUE));
			buffer.append("\n");  
			
			buffer.append("efg.showchangedirectorymessage.checked=");
			buffer.append(EFGImportConstants.EFGProperties.getProperty(
					"efg.showchangedirectorymessage.checked",EFGImportConstants.EFG_TRUE));
			buffer.append("\n");  
			
			File file = getWorkspaceFileName();
			FileWriter writer = new FileWriter(file);
			writer.write(buffer.toString());
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			
			log.error(e.getMessage());
		}
    	
    }
    /**
	 * @return
	 */
	private static File getWorkspaceFileName() throws Exception{
		String workspaceFileName = 
			EFGImportConstants.EFGProperties.getProperty("workspace.file");

		
		String dir = null;
		try {
			URL url = WorkspaceResources.class.getResource(workspaceFileName);
			return new File(URLDecoder.decode(url.getFile(), "UTF-8"));
		} catch (Exception e) {
			log.error("Application could not find file : " + workspaceFileName );
			throw e;
		}
	}
	public static String toFileName(String str) {
		File f = new File(str);
		
		return f.getAbsolutePath();
	}
	/**
	 * @param serverLocations
	 * @return
	 */
	public static String[] convertURIToString(String[] serverLocations) {
		
		if((serverLocations != null) && (serverLocations.length > 0)) {
			
			String[] stringFiles = new String[serverLocations.length];
			for (int i = 0; i < stringFiles.length; i++) {
				
				stringFiles[i]= toFileName(serverLocations[i]);
				
			}
			return stringFiles;
		}
		
		return null;
	}
}
