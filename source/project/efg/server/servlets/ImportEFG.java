/**
 * $Id: ImportEFG.java,v 1.1.1.1 2007/08/01 19:11:24 kasiedu Exp $
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

package project.efg.server.servlets;


import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.UnZipImport;


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
			log1 = Logger.getLogger(project.efg.server.servlets.ImportEFG.class);
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
	private void importFiles(File zipFile, 
			File repository, 
			File servletPath)throws Exception {
		//FIXME
		UnZipImport unzip = 
			new UnZipImport(null,
				zipFile, 
				repository, 
				servletPath);
		unzip.processZipFile();
	}	
	/**
	 * 
	 */
	public void doCustomProcessing(HttpServletResponse response,
			File[] files) {
	
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
//$Log: ImportEFG.java,v $
//Revision 1.1.1.1  2007/08/01 19:11:24  kasiedu
//efg2.1.1.0 version of efg2
//
//Revision 1.3  2007/02/20 16:34:01  kasiedu
//no message
//
//Revision 1.2  2007/02/03 00:04:27  kasiedu
//*** empty log message ***
//
//Revision 1.1  2007/01/12 15:04:04  kasiedu
//no message
//
