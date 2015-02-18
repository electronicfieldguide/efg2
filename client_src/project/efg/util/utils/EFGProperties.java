/**
 * 
 */
package project.efg.util.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.RegularExpresionConstants;

/**
 * @author jacob.asiedu
 *
 */
public class EFGProperties {

	static Logger log = null;
	private Properties efgProperties =null;
	private Set sqlKeywords;

	static {
		try {
			log = Logger.getLogger(EFGProperties.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 * @param propertiesDirectory
	 */
	public EFGProperties(String propertiesDirectory){
		this.loadProperties(propertiesDirectory);
	}
	
	/**
	 * 
	 * @param propertiesDirectory
	 */
	public EFGProperties(File propertiesDirectory){
		this.loadProperties(propertiesDirectory);
	}
	/**
	 * 
	 * @return
	 */
	public Set getSQLKeyWords(){
		return this.sqlKeywords;
	}
	/**
	 * 
	 * @return
	 */
	public Properties getProperties(){
		return this.efgProperties;
	}
	private void loadProperties(File file) {
		//loop through properties file and recursively call method that adds them
		try {
				this.addProperties(file);		
			} catch (Exception e) {
		   	e.printStackTrace();
		   	log.error(e.getMessage());
		
		}
	}
	private void addProperties(File file){
		try {
			this.addPropertyFiles(file);
			this.addSQLKeyWords();			
		} catch (Exception e) {
	   	e.printStackTrace();
	   	log.error(e.getMessage());
	
	}
		
	}
	private void loadProperties(String propertiesFolder) {
		//loop through properties file and recursively call method that adds them
		try {
				log.debug("Properties file name: " + propertiesFolder);
				efgProperties = System.getProperties();
				File file = new File(propertiesFolder);
				this.addProperties(file);		
			} catch (Exception e) {
		   	e.printStackTrace();
		   	log.error(e.getMessage());
		
		}
	}
	/**
	 * Reads,loads an sets application wide properties file
	 * from all files located in the properties directory.
	 * 
	 * @return true if all of the files were read and loaded successfully
	 */
	private boolean addPropertyFiles(File file){
		try{
			if(!file.isDirectory()){
				throw new Exception("Properties directory could not be found!!");
			}
			
			processFiles(file);
			return true;
		}
		catch(Exception ee){
			log.error(ee.getStackTrace());
			ee.printStackTrace();
		
		}
		return false;
	}

	private void addSQLKeyWords(){
		sqlKeywords = new HashSet();
		String sql =  
			efgProperties.getProperty(EFGImportConstants.SQL_KEYWORD_KEY);
		String[] csv = RegularExpresionConstants.commaPattern.split(sql);
		for(int i=0; i < csv.length; i++){
			sqlKeywords.add(csv[i].trim().toLowerCase());
		}
	}

	/**
	 * recursively iterates over all files in the properties directory
	 * 
	 * @param file
	 *            the current property file
	 */
	private void processFiles(File file) throws Exception{
		String[] propertyFiles = file.list();
	
		for(int i=0; i < propertyFiles.length; i++){
			String currentFile = propertyFiles[i];
			File newFile = new File(file,currentFile);
	
			if(newFile.isDirectory()){
				processFiles(newFile);
			}
			else{
				try {
					InputStream input = newFile.toURI().toURL().openStream();
					efgProperties = new Properties(efgProperties);
					
					efgProperties.load(input);
					if(input != null){
						input.close();
					}
				} catch (MalformedURLException e) {
				
					throw e;
				} catch (IOException e) {
				
					throw e;
				}
			}
		}
	}


}
