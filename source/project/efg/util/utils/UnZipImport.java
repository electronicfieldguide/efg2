package project.efg.util.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.ZipInterface;


public class UnZipImport implements ZipInterface {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(UnZipImport.class);
		} catch (Exception ee) {
		}
	}
	private File sqlDirectory;
	private File zipFile, importsDirectory, destinationOnServer;
	private DBObject dbObject;
	/**
	 * @param object 
	 * 
	 */
	public UnZipImport(DBObject dbObject, File zipFile, 
			File importsDirectory, 
			File destinationOnServer) {
		this.dbObject = dbObject;
		this.zipFile = zipFile;
		this.importsDirectory = importsDirectory;
		this.destinationOnServer = destinationOnServer;
	}

	/**
	 * 
	 * @param zipFile - The name of the zip file
	 * @param importsDirectory - The directory where the zip file will be unzipped
	 * @param destinationOnServer - Where the unzipped files will be copied to.
	 * @throws IOException
	 */
	public synchronized void processZipFile(
			) throws IOException {
		
		
		ZipFile zip = new ZipFile(this.zipFile);
		Set directoryNames =Collections.synchronizedSet(new HashSet());
		try {
			Enumeration zipEntriesEnum = zip.entries();
			
			while (zipEntriesEnum.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) zipEntriesEnum.nextElement();
				File file = (importsDirectory != null) ? new File(this.importsDirectory, entry
						.getName()) : new File(entry.getName());
				
				if (entry.isDirectory()) {
					
					if(!file.exists()) {
					if (!file.mkdirs()) {
						
						throw new IOException("Error creating directory: "
								+ file);
					}
					}
				} else {
					File parent = file.getParentFile();	
					if (parent != null && !parent.exists()) {
						if (!parent.mkdirs()) {
							throw new IOException("Error creating directory: "
									+ parent);
						}
					}
					if(parent.isDirectory()) {
						findRootDirectoryOfZip(parent,directoryNames);
					}
						
					InputStream in = zip.getInputStream(entry);
					try {
						OutputStream out = new BufferedOutputStream(
								new FileOutputStream(file), BUF_SIZE);
					
						try {
							copy(in, out, BUF_SIZE);
						} finally {
							out.close();
						}
	
					} finally {
						in.close();
					}
				}
			}
		} finally {
			zip.close();
		}
		
		moveToDestinationOnServer(
				this.importsDirectory,
				new ArrayList(directoryNames),
				this.destinationOnServer);
		importIntoDatabase();
	}
	/**
	 * @param sandBoxDirectory
	 * @param directoryNames 
	 * @param destinationOnServer 
	 */
	private void moveToDestinationOnServer(File sandBoxDirectory, 
			List directoryNames, 
			File destinationOnServer)throws IOException {
		
		File directoryToCopy = getTopLevelFileName(sandBoxDirectory, directoryNames);
	
		if(directoryToCopy != null) {
			this.sqlDirectory = new File(directoryToCopy, SQL_DIR);
		
			File[] filesToCopy = directoryToCopy.listFiles();
			for(int i = 0; i < filesToCopy.length; ++i) {
				copyFile(filesToCopy[i], destinationOnServer);
			}
			
		}
	}
	private void copyFile(File srcFile, File destFile) {
		  FileChannel sourceChannel = null;
		  FileChannel destinationChannel = null;
		  
		 try{
			
			 destFile = new File(destFile,srcFile.getName()); 
			 if(destFile.getName().equals(SQL_DIR) || 
					 destFile.getParent().equalsIgnoreCase(SQL_DIR)) {
				
				 return;
			 }
			log.debug("src: " + srcFile.getAbsolutePath());
			log.debug("dest: " + destFile.getAbsolutePath());
		 
			 if(srcFile.isDirectory()){	
				 destFile.mkdirs();
				 File[] list = srcFile.listFiles();
				
				 for(int i = 0; i < list.length; i++){
					 File file = list[i];
					copyFile(file,destFile);
				 }
			 }
			 else{
				 File parent = destFile.getParentFile();
				 if(!parent.exists()) {
					 parent.mkdirs();
				 }
				 sourceChannel = new
				 FileInputStream(srcFile).getChannel();
				
				 destinationChannel = new
				 FileOutputStream(destFile).getChannel();
				 sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
				 sourceChannel.close();
				 destinationChannel.close();
			 }
		 }
		 catch(Exception ee){
			 ee.printStackTrace();
			 log.error(ee.getMessage());
			return;
			 
		 }
		 finally{
			 try{
				 if(sourceChannel != null){
				 sourceChannel.close();
				 }
				 if(destinationChannel != null){
					 destinationChannel.close();
				 }
			 }
			 catch(Exception eee){
				 log.error(eee.getMessage());
			 }
		 }
	  }
    private File getTopLevelFileName(File file, List directoryNames) {
    	if(directoryNames != null) {
		for(int index = 0; index < directoryNames.size();index++) {
			String newName =(String)directoryNames.get(index);
			File f = new File(file,newName);
			if(f.exists()) {
				return f;
			}
		}
    	}
		return null;

    }

	/**
	 * Reads from input and writes read data to the output, until the stream
	 * end.
	 * 
	 * @param in
	 * @param out
	 * @param bufSizeHint
	 * @throws IOException
	 */
	private void copy(InputStream in, OutputStream out,
			int bufSizeHint) throws IOException {
		IOUtils.copy(in,out);
		out.flush();
	}

	private File getSqlDirectory() {
		return this.sqlDirectory;
	}
	/**
	 * @throws IOException 
	 * 
	 */
	private void importIntoDatabase() throws IOException {
		File sqlDirectory = this.getSqlDirectory();
		if(sqlDirectory != null) {
			
			File[] sqlFiles = sqlDirectory.listFiles(new SQLFileNameFilter());
			if(sqlFiles != null && sqlFiles.length > 0) {
				for(int i = 0; i < sqlFiles.length; i++) {
					BufferedReader in = new BufferedReader(
							new InputStreamReader(
									new FileInputStream(sqlFiles[i]), "UTF8"));
					ImportData data = new ImportData(this.dbObject);
					data.importData(in);
				}
			}
			File[] serFiles = sqlDirectory.listFiles(new SerializedFileNameFilter());
			if(serFiles != null && serFiles.length > 0) {
				File outFileInfo = new File(sqlDirectory,
						EFGImportConstants.OUT_FILE_INFO);
				putSerializedFilesIntoDB(outFileInfo,serFiles);
			}
		}
	}
	/**
	 * @param outFileInfo
	 * @param taxonConfigLists
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void putSerializedFilesIntoDB(
			File outFileInfo, 
			File[] serFiles ) throws FileNotFoundException, IOException {
		Properties props= new Properties();
		props.load(new FileInputStream(outFileInfo));
		
	
		String[] datasources = new String[serFiles.length];
		TaxonPageTemplates[] templateLists = 
			new  TaxonPageTemplates[serFiles.length];
		boolean isError = false;
		for(int i = 0; i < serFiles.length; i++) {
			try {
				File serFile = serFiles[i];
				String fileName = serFile.getName();
				String dsName = (String)props.get(fileName);

				if(dsName != null && !dsName.trim().equals("")){
					 FileInputStream fin = new FileInputStream(serFiles[i]);
					 ObjectInputStream ois = new ObjectInputStream(fin);
					 TaxonPageTemplates tps  = 
						 (TaxonPageTemplates)ois.readObject();
					datasources[i] = dsName;
					templateLists[i] = tps;
				}
			} catch (ClassNotFoundException e) {
				isError = true;
				e.printStackTrace();
			}
		}
		if(!isError){
			TemplateMapObjectHandler.batchUpdateDatabase(
				this.dbObject,
				datasources, 
				EFGImportConstants.EFG_RDB_TABLES, 
				templateLists);
		}
	}
	/**
	 * @param parent
	 * @param directoryNames
	 */
	private void findRootDirectoryOfZip(File current, Set directoryNames) {
		if(current == null || current.getName().equalsIgnoreCase("imports")) {
			return;
		}
		File parent = current.getParentFile();	
		if(parent != null && parent.getName().equalsIgnoreCase("imports")) {
			directoryNames.add(current.getName());
			return;
		}

		findRootDirectoryOfZip(parent,directoryNames);
	}


	class SQLFileNameFilter implements FilenameFilter {

		/* (non-Javadoc)
		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
		 */
		public boolean accept(File dir, String name) {
			if(name == null){
				return false;
			}
			name = name.toLowerCase();
			return name.endsWith(EFGImportConstants.SQL_EXT);
		}
		
	}
	class SerializedFileNameFilter implements FilenameFilter {

		/* (non-Javadoc)
		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
		 */
		public boolean accept(File dir, String name) {
			if(name == null){
				return false;
			}
			name = name.toLowerCase();
			return name.endsWith(EFGImportConstants.OUT_EXT);
		}
		
	}
}
