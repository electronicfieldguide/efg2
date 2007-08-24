/**
 * $Id$
 * $Name:  $
 * 
 * Copyright (c) 2007  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2, or
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 */
package project.efg.server.exports;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.util.factory.TemplateModelFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.ZipInterface;
import project.efg.util.utils.EFGUniqueID;
import project.efg.util.utils.ExportData;
import project.efg.util.utils.TemplateModelHandler;

public class ZipExport implements ZipInterface{
	
	/**
	 * 
	 */
	public ZipExport() {
	}

	public void copyFile(File srcFile, File destFile) {
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
		IOUtils.copy(in, out);
		out.flush();
		
	}
	private void zipDirectory(ZipOutputStream out, String stripPath,
			File dir, char pathSeparator) throws IOException {

		String[] entries = dir.list();

		if (entries == null || entries.length == 0) {
			return;
		}

		// recurse via entries
		for (int i = 0; i < entries.length; i++) {
			File file = new File(dir, entries[i]);
			if (file.isDirectory()) {
				zipDirectory(out, stripPath, file, pathSeparator);
			} else {
				zipFile(out, stripPath, file, pathSeparator);
			}
		}
	}

	private void zipFile(ZipOutputStream out, String stripPath,
			File file, char pathSeparator) throws IOException {
		ZipEntry zipEntry = new ZipEntry(processPath(file.getPath(), stripPath,
				pathSeparator));
		zipEntry.setTime(file.lastModified());
		out.putNextEntry(zipEntry);

		byte[] buffer = new byte[8 * 1024];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				file), buffer.length);

		try {
			int count = 0;
			while ((count = in.read(buffer, 0, buffer.length)) >= 0) {
				if (count != 0) {
					out.write(buffer, 0, count);
				}
			}
		} finally {
			in.close();
		}
	}

	private String processPath(String path, String stripPath,
			char pathSeparator) {
		if (!path.startsWith(stripPath)) {
			throw new IllegalArgumentException("Invalid entry: " + path
					+ "; expected to start with " + stripPath);
		}

		return path.substring(stripPath.length()).replace(File.separatorChar,
				pathSeparator);
	}
	/**
	 * Recursively zips a set of root entries into a zipfile, compressing the
	 * contents.
	 * 
	 * @param zipFile
	 * @param srcDir
	 * @param parentDir
	 *            a directory used as a root for the file name resolution. If
	 *            null, current directory is assumed.
	 * @throws IOException
	 */
	public void zip(File zipFile, File parentDir, File[] sources,
			char pathSeparator) throws IOException {
		String stripPath = (parentDir != null) ? parentDir.getPath() : "";
		if (stripPath.length() > 0 && !stripPath.endsWith(File.separator)) {
			stripPath += File.separator;
		}
	
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
		out.setMethod(ZipOutputStream.DEFLATED);
	
		try {
			// something like an Ant directory scanner wouldn't hurt here
			for (int i = 0; i < sources.length; i++) {
				if (!sources[i].exists()) {
					throw new IllegalArgumentException(
							"File or directory does not exist: " + sources[i]);
				}
	
				if (sources[i].isDirectory()) {
					zipDirectory(out, stripPath, sources[i], pathSeparator);
				} else {
					zipFile(out, stripPath, sources[i], pathSeparator);
				}
			}
		} finally {
			out.close();
		}
		
	}
	/**
	 * 
	 * @param serverDirectory
	 * @param exportDirectory
	 * @param datasources
	 * @param mediaRDirs
	 * @param otherResources
	 * @return
	 */
	public synchronized String zipExports(
			
			File serverDirectory,
			File exportDirectory,
			String[] datasources, 
			String[] mediaRDirs, 
			String[] otherResources) {
		
		String currentExportName = makeExportName(); 
		if(currentExportName == null) {
			return null;
		}
		File destinationDirectory = new File(exportDirectory, currentExportName);
		if(destinationDirectory.exists()) {
			destinationDirectory.delete();	
		}
		destinationDirectory.mkdirs();
		createSQLFiles(destinationDirectory,datasources,
				currentExportName);
		copyTemplateFiles(destinationDirectory,datasources);
		copyMediaResourceFiles(serverDirectory,destinationDirectory,mediaRDirs);
		copyOtherResourceFiles(serverDirectory,destinationDirectory,otherResources);
		
		
		try {
			currentExportName = currentExportName+ EFGImportConstants.ZIP_EXT;
			File zipFile = new File(exportDirectory,currentExportName);
			if(zipFile.exists()) {
				zipFile.delete();
			}
			this.zip(
					zipFile,
					exportDirectory, 
					destinationDirectory.listFiles(),
					'/');
			return currentExportName;
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @param datasources
	 * @return
	 */
	private String makeExportName() {
		return EFGUniqueID.getUniqueID();
	}
	private void copyListFiles(File[] filesToCopy, File destFile) {
		if(filesToCopy != null) {
		for(int j = 0; j < filesToCopy.length; ++j) {
			copyFile(filesToCopy[j], destFile);
		}
		}
	}
	/**
	 * @param destinationDirectory 
	 * @param mediaRDirs 
	 * 
	 */
	private void copyMediaResourceFiles(File serverDirectory, File destinationDirectory, String[] mediaRDirs) {
		if(mediaRDirs != null) {
			for(int i = 0; i < mediaRDirs.length; ++i) {
				File destfilelarge = new File(destinationDirectory, EFG_IMAGES_DIR + File.separator +mediaRDirs[i]);			
				File destfilethumbs = new File(destinationDirectory, EFG_THUMS_DIR + File.separator +mediaRDirs[i]);
				File origfilelarge = new File(serverDirectory, EFG_IMAGES_DIR + File.separator +mediaRDirs[i]);
				File origfilethumbs = new File(serverDirectory, EFG_THUMS_DIR + File.separator +mediaRDirs[i]);
				
				copyListFiles(origfilelarge.listFiles(),destfilelarge);
				copyListFiles(origfilethumbs.listFiles(),destfilethumbs);
			}
		}
	}
	/**
	 * @param dsFile 
	 * @param dsFile2 
	 * @param datasources 
	 * 
	 */
	private void copyTemplateFiles(
			File destinationDirectory, 
			String[] datasources) {
		
		if(datasources != null) {
			File srcDir = new File(destinationDirectory,SQL_DIR);
			srcDir.mkdirs();
			
		
			Properties props = new Properties();
			String query = makeQuery(datasources,props);
			this.writeProperties(srcDir,props);
			this.writeTemplateObjects(srcDir,query);
		}
		
	}
	/**
	 * 
	 */
	private void writeTemplateObjects(File srcDir, String query) {
		TemplateModelHandler temp = 
			TemplateModelFactory.createExportTemplateHandler();
		SqlRowSet rs;
		try {
			rs = temp.executeQueryForRowSet(query);
			while(rs.next()){
				String datasourceName = rs.getString(EFGImportConstants.DS_DATA_COL);
				Object binStream  =  rs.getObject(EFGImportConstants.TEMPLATE_OBJECT_FIELD);
				if(binStream != null){	
					byte[] byteArray = (byte[])binStream;
				    final ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
				    ObjectInputStream objS = new ObjectInputStream(stream);
				    Object obj = objS.readObject();
				    TaxonPageTemplates tps  = (TaxonPageTemplates)obj;

					File srcFile = 
						new File(srcDir,
								(datasourceName + 
										EFGImportConstants.OUT_EXT));
				    FileOutputStream fos = new FileOutputStream(srcFile);
				
				      //  Next, create an object that can write to that file.
				      ObjectOutputStream outStream = 
				        new ObjectOutputStream( fos );

				      //  Save each object.
				      outStream.writeObject(tps);
				      outStream.flush();
				      fos.close();
				      outStream.close();
				}
			
			}

		} catch (Exception e) {
			LoggerUtilsServlet.logErrors(e);
			//e.printStackTrace();
		}

		
	}

	/**
	 * @param datasources
	 * @param props
	 * @return
	 */
	private String makeQuery(String[] datasources, Properties props) {
		StringBuffer query = new StringBuffer("SELECT ");
		query.append(EFGImportConstants.DS_DATA_COL);
		query.append(",");
		query.append(EFGImportConstants.TEMPLATE_OBJECT_FIELD);
		query.append(" FROM ");
		query.append(EFGImportConstants.EFG_RDB_TABLES);
		query.append(" WHERE ");
		

		
		
		for(int i = 0; i < datasources.length; ++i) {
			String ds = datasources[i];
			
			if(i > 0){
				query.append(" or ");
			}
			query.append(EFGImportConstants.DS_DATA_COL);
			query.append("='");
			query.append(ds);
			query.append("'");
			props.put(ds +EFGImportConstants.OUT_EXT,ds);
		}
		return query.toString();

	}

	/**
	 * @param destinationDirectory
	 * @param props
	 */
	private boolean writeProperties(File destinationDirectory, 
			Properties props) {
		
		try {
			File outFileInfo = new File(destinationDirectory,
					EFGImportConstants.OUT_FILE_INFO);

			FileOutputStream ofi = new FileOutputStream(outFileInfo);
			props.store(ofi, "Properties File For Import");
			ofi.flush();
			ofi.close();
			return true;
		} catch (Exception e) {
			LoggerUtilsServlet.logErrors(e);
			e.printStackTrace();
		}
		return false;
		
	}

	/**
	 * @param otherResources 
	 * @param dsFile 
	 * @param object 
	 * 
	 */
	private void copyOtherResourceFiles(File serverDirectory,File destinationDirectory, String[] otherResources) {
		if(otherResources != null) {
		for(int i = 0; i < otherResources.length; ++i) {
			String otherResource = otherResources[i].replace('/', File.separatorChar);
			//File  destFile = new File(destinationDirectory,otherResource);
			File srcFile = new File(serverDirectory,otherResource);
			String parent = srcFile.getParentFile().getName();
			File file = new File(destinationDirectory,parent);
			file.mkdirs();
			
			this.copyFile(srcFile,file);
		}
		}
	}
	/**
	 * 
	 */
	
	/**
	 * @param dsFile 
	 * @param datasources 
	 * @param currentExportName 
	 * 
	 */
	private void createSQLFiles(
			File destinationDirectory,
			String[] datasources,
			String currentExportName) {
		if(datasources != null) {
		File srcFile = new File(destinationDirectory,SQL_DIR);
		srcFile.mkdirs();
		StringBuffer buffer = new StringBuffer();
		ExportData export = new ExportData();
		for(int i = 0; i < datasources.length; ++i) {
			String datasource = datasources[i];
			String metadatasource = datasource +  EFGImportConstants.METAFILESUFFIX;
			buffer.append(export.dumpDB(datasource.toLowerCase()));
			buffer.append(export.dumpDB(metadatasource.toLowerCase()));
			
			String templatesTable = 
				EFGImportConstants.EFGProperties.getProperty(
			"efg_template_table_name");
			StringBuffer query = new StringBuffer( "SELECT * FROM ");
			query.append(templatesTable);
			query.append(" WHERE ");
			query.append(EFGImportConstants.DATASOURCE_NAME);
			query.append("='");
			query.append(datasource);
			query.append("'");
			
			try {
				//log.debug("About to get template");
				//log.debug("Query is: " + query.toString());
				buffer.append(export.dumpTable(
						templatesTable,
						query.toString()));
			} catch (Exception e) {
				LoggerUtilsServlet.logErrors(e);
			}
			
			query = new StringBuffer(
					this.getDataQuery(
							EFGImportConstants.EFGProperties.getProperty(
								"ALL_EFG_RDB_TABLES"),
							datasource, metadatasource));
			try {
			buffer.append(export.dumpTable(
						EFGImportConstants.EFGProperties.getProperty(
						"ALL_EFG_RDB_TABLES"),query.toString()));
			} catch (Exception e) {
			}
			query = new StringBuffer(
					this.getDataQuery(
							EFGImportConstants.EFGProperties.getProperty(
									"ALL_EFG_GLOSSARY_TABLES"), 
							datasource, 
							metadatasource));
			try {
				buffer.append(export.dumpTable(
						EFGImportConstants.EFGProperties.getProperty(
								"ALL_EFG_GLOSSARY_TABLES"),query.toString()));
			} catch (Exception e) {
			}			
		}
		//add other tables
		writeToSqlFile(new File(srcFile,currentExportName + EFGImportConstants.SQL_EXT),buffer.toString());
		}
	}
	private String getDataQuery(String tableName, String datasource, String metadatasource) {
		StringBuffer query = new StringBuffer( "SELECT * FROM ");
		query.append(tableName);
		query.append(" WHERE ");
		query.append(EFGImportConstants.DS_DATA_COL);
		query.append("='");
		query.append(datasource.toLowerCase());
		query.append("' AND ");
		query.append(EFGImportConstants.DS_METADATA_COL); 
		query.append("='");
		query.append(metadatasource.toLowerCase());
		query.append("'");
		return query.toString();
	}
	/**
	 * @param file
	 * @param buffer
	 */
	private void writeToSqlFile(File file, String queries) {
		OutputStream out = null;
		try {

			ByteArrayInputStream in = 
				new ByteArrayInputStream(queries.toString().getBytes("UTF-8"));

			try {
				out = new BufferedOutputStream(
						new FileOutputStream(file), BUF_SIZE);

				try {
					copy(in, out, BUF_SIZE);
					out.flush();
					out.close();
				} 
				catch(Exception ee) {
					ee.printStackTrace();
					out.close();
				}
				finally {
					out.close();
				}

			}
			catch(Exception ee) {
				ee.printStackTrace();
			}finally {
				in.close();
			}
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
}