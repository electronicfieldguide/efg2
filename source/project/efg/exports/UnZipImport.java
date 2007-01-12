package project.efg.exports;

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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import project.efg.util.EFGImportConstants;

public class UnZipImport implements ZipInterface {
	private File sqlDirectory;

	/**
	 * 
	 */
	public UnZipImport() {
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
				 eee.printStackTrace();
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
		int read = -1;
		byte[] buf = new byte[bufSizeHint];
		while ((read = in.read(buf, 0, bufSizeHint)) >= 0) {
			out.write(buf, 0, read);
		}
		out.flush();
		
	}
	




	private void copyListFiles(File[] filesToCopy, File destFile) {
		if(filesToCopy != null) {
			for(int j = 0; j < filesToCopy.length; ++j) {
				copyFile(filesToCopy[j], destFile);
			}
		}
	}
	public File getSqlDirectory() {
		return this.sqlDirectory;
	}
	/**
	 * 
	 * @param zipFile - The name of the zip file
	 * @param importsDirectory - The directory where the zip file will be unzipped
	 * @param destinationOnServer - Where the unzipped files will be copied to.
	 * @throws IOException
	 */
	public synchronized void unzipFile(
			File zipFile, 
			File importsDirectory, 
			File destinationOnServer) throws IOException {
		
		
		ZipFile zip = new ZipFile(zipFile);
		Set directoryNames =Collections.synchronizedSet(new HashSet());
		try {
			Enumeration zipEntriesEnum = zip.entries();
			
			while (zipEntriesEnum.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) zipEntriesEnum.nextElement();
				File file = (importsDirectory != null) ? new File(importsDirectory, entry
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
				importsDirectory,
				new ArrayList(directoryNames),
				destinationOnServer);
		importIntoDatabase();
	}
	/**
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	private void importIntoDatabase() throws UnsupportedEncodingException, FileNotFoundException {
		File sqlDirectory = this.getSqlDirectory();
		if(sqlDirectory != null) {
			
			File[] sqlFiles = sqlDirectory.listFiles(new SQLFileNameFilter());
			if(sqlFiles != null && sqlFiles.length > 0) {
				for(int i = 0; i < sqlFiles.length; i++) {
					BufferedReader in = new BufferedReader(
							new InputStreamReader(new FileInputStream(sqlFiles[i]), "UTF8"));
					ImportData data = new ImportData();
					data.importData(in);
				}
			}
			else {
				System.out.println("Files zero!!");
			}
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

	public static void main(String[] args) {
		UnZipImport unzip = new UnZipImport();
		File zipFile = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 5.0\\webapps\\efg2\\exports\\test.zip");
		File sandBoxDirectory = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 5.0\\webapps\\efg2\\imports");
		File destinationOnServer = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 5.0\\webapps\\efg2");
		try {
			unzip.unzipFile(zipFile, sandBoxDirectory, destinationOnServer);
			System.out.println(unzip.getSqlDirectory());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
