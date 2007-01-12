/**
 * $Id$
 *
 * Copyright (c) 2007  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu, Kimmy Lin
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

package project.efg.servlets.efgImpl;


import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import project.efg.exports.UnZipImport;
import project.efg.util.EFGImportConstants;


/**
 * This servlet handles requests to display a list of taxa with attribute values
 * matching a set of input parameters regardless of the backend.
 */
public class ImportEFG extends UploadServlet implements EFGImportConstants {
	static final long serialVersionUID = 1;

	static Logger log1 = null;
	
	/**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			log1 = Logger.getLogger(project.efg.servlets.efgImpl.ImportEFG.class);
		} catch (Exception ee) {
		}

	}
	/**
	 * 
	 * @param zipFile
	 * @param repository
	 * @param servletPath
	 * @throws Exception
	 */
	private void importFiles(File zipFile, File repository, File servletPath)throws Exception {
		UnZipImport unzip = new UnZipImport();
		unzip.unzipFile(zipFile, repository, servletPath);
	}	
	/**
	 * 
	 */
	public void doCustomProcessing(HttpServletResponse response,File[] files) {
	
		if(files == null || files.length == 0) {
			
			try {
				sendCompleteResponse(response, "Files could not be processed");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			return;
			//throw new Exception("No files uploaded");
		}
		StringBuffer messageBuffer = new StringBuffer();
		
		for(int i = 0; i < files.length; i++) {
			File zipFile =files[i];
			
			try {
				
				this.importFiles(zipFile,getRepository(null), new File(realPath));
				
			} catch (Exception e) {
				log1.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}


	/* (non-Javadoc)
	 * @see project.efg.servlets.efgImpl.UploadServlet#getRepository(org.apache.commons.fileupload.FileItem)
	 */
	public File getRepository(FileItem fileItem) {
		File servletPath = new File(realPath);
		File repository = new File(servletPath,EFGImportConstants.IMPORT_FOLDER);
		
		return repository;
	}
}
//$Log$
//Revision 1.1  2007/01/12 15:04:04  kasiedu
//no message
//
