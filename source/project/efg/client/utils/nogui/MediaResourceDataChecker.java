/*
 *  $Id$
* $Name:  $
* @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
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
* 
* 
* Checks file system for media resources in data file.
* 
*/
package project.efg.client.utils.nogui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


import project.efg.util.factory.SpringFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;
import project.efg.util.interfaces.RegularExpresionConstants;
import project.efg.util.utils.DBObject;
import project.efg.util.utils.EFGParseObject;
import project.efg.util.utils.EFGParseObjectList;
import project.efg.util.utils.EFGParseStates;
import project.efg.util.utils.EFGRDBImportUtils;
/**
 * @author kasiedu
 * 
 */
public class MediaResourceDataChecker extends DataChecker {
	

	private static String imagesHome;
	private static String thumbsHome;
	private String mediaQuery;
	static Logger log = null;
	private EFGParseStates efgParseStates;
	private Map errorTableBigImage;
	private Map errorTableThumbNails;

	static {
		try {
			log = Logger.getLogger(MediaResourceDataChecker.class);
		} catch (Exception ee) {
		}
	}
	static String images_home = 
		EFGImportConstants.EFGProperties.getProperty(
				"efg.images.home");
	static String thumbs_home = 
		EFGImportConstants.EFGProperties.getProperty(
				"efg.mediaresources.thumbs.home");
	public MediaResourceDataChecker(DBObject dbObject, String displayName) {
		super(dbObject,displayName);
		this.efgParseStates = SpringFactory.getEFGParseStatesInstance();
		this.errorTableBigImage = new HashMap();
		this.errorTableThumbNails = new HashMap();
	}





	private String getMediaQuery() {
		if (this.mediaQuery == null) {

			StringBuilder queryBuffer = new StringBuilder();
			queryBuffer.append("SELECT DISTINCT ");
			queryBuffer.append(EFGImportConstants.LEGALNAME);
			queryBuffer.append(" FROM ");
			queryBuffer.append(this.metadatasourceName);
			queryBuffer.append(" WHERE ");
			queryBuffer.append(EFGImportConstants.MEDIARESOURCE);
			queryBuffer.append(" = 'true'");
			this.mediaQuery = queryBuffer.toString();
		}
		return this.mediaQuery;
	}

	private static String getWebImagesHome() {
		if (imagesHome == null) {
			StringBuilder imagesHomeBuffer = new StringBuilder(getServerHome());
			
			imagesHomeBuffer.append(File.separator);
			imagesHomeBuffer.append(images_home);
			imagesHomeBuffer.append(File.separator);
			imagesHome = imagesHomeBuffer.toString();
		}
		return imagesHome;

	}
	
	private static String getWebThumbsHome() {
		if (thumbsHome == null) {
			StringBuilder imagesHomeBuffer = new StringBuilder(getServerHome());
			
			imagesHomeBuffer.append(File.separator);
			imagesHomeBuffer.append(thumbs_home);
			imagesHomeBuffer.append(File.separator);
			thumbsHome = imagesHomeBuffer.toString();
		}
		return thumbsHome;

	}
	private String printErrors(Map map){
		StringBuilder errorBuffer = new StringBuilder();
		errorBuffer.append("<table border=\"1\">");
		errorBuffer
				.append("<tr><th>Field Name</th> <th>Missing Files</th></tr>");

		Iterator fieldIter = map.keySet().iterator();

		while (fieldIter.hasNext()) {
			String fieldName = (String) fieldIter.next();
			errorBuffer.append("<tr><td>");
			errorBuffer.append(fieldName);
			errorBuffer.append("</td> <td></td></tr>");

			Set dataset = (Set)map.get(fieldName);

			Iterator datasetIter = dataset.iterator();
			while (datasetIter.hasNext()) {
				String name = (String) datasetIter.next();
				errorBuffer.append("<tr><td>");
				errorBuffer.append("</td>");
				errorBuffer.append("<td>");
				errorBuffer.append(name);
				errorBuffer.append("</td></tr>");
			}

		}
		errorBuffer.append("</table>");
		return errorBuffer.toString();
	}
	public String displayErrors() {

		StringBuilder errorBuffer = new StringBuilder();
		errorBuffer.append("<html><body>");
		errorBuffer.append("<h1>Application Found ");
		errorBuffer.append(this.getNumberOfErrors() + "");
		errorBuffer.append(" potential errors </h1>");
		errorBuffer.append("<hr></hr>");
		if(this.errorTableBigImage.size() > 0){
			errorBuffer.append("<h2>");
			errorBuffer.append("Missing Original Media Resource Files");
			errorBuffer.append("</h2>");
			errorBuffer.append(printErrors(this.errorTableBigImage));
			errorBuffer.append("<br></br>");
		}
		if(this.errorTableThumbNails.size() > 0){
			errorBuffer.append("<h2>");
			errorBuffer.append("Missing thumb nail files." +
					"This was probably due to a communications " +  
					"failure during the original import, when the system was " +
					"automatically generating thumbnails. In the current " +
					"release, the only remedy is to re-import the original image.");
			errorBuffer.append("</h2>");
			errorBuffer.append(printErrors(this.errorTableThumbNails));
		}
		
		errorBuffer.append("</body>");
		errorBuffer.append("</html>");
		BufferedWriter out = null;
		try {
			String newFileName = this.datasourceName
					+ EFGImportConstants.IMAGEAPPENDER
					+ EFGImportConstants.HTML_EXT;
			String dir1 = URLDecoder.decode(getTempFilesHome().getFile(),"UTF-8");
			File dir = new File(dir1);
			if(!dir.exists()){
				
				dir.createNewFile();
			}
			File file = new File( dir,newFileName);
			out = new BufferedWriter(new FileWriter(file));
			out.write(errorBuffer.toString());
			out.flush();
			out.close();

			return file.toURI().toString();
		} catch (Exception ee) {
			log.error(ee.getMessage());
			
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

				}
			}
		}
		// write to a file
		return null;
	}
	private void checkImages(List listOfImages,Set mSet, String imagesDir) throws MalformedURLException, UnsupportedEncodingException{
		for (java.util.Iterator iter1 = listOfImages.iterator(); iter1
		.hasNext();) {
			EFGQueueObjectInterface image = (EFGQueueObjectInterface) iter1
					.next();
			String states = image.getObject(0);
			EFGParseObjectList lists =
				this.efgParseStates.parseStates(RegularExpresionConstants.LISTSEP,states,true); 
	
			for(int i = 0; i < lists.getSize(); i++){
				EFGParseObject obj = lists.getEFGParseObject(i);
				String imageName = obj.getState();
				
				File dir = new File(imagesDir + File.separator + imageName.trim());
				URL dirURL = dir.toURI().toURL();
				String dir1 = URLDecoder.decode(dirURL.getFile(),"UTF-8");
		
				File file = new File(dir1);
				
				if (!file.exists()) {
					mSet.add(imageName);
				}
				else {
					try {
						if(!file.getCanonicalPath().equals(file.getAbsolutePath()) ){
							mSet.add(imageName);
						}
					}
					catch(Exception ee) {
						mSet.add(imageName);
					}
				}							
			}
		}
	}
	public boolean checkMediaResources() {
		if (!isReady()) {
			return true;
		}
		String mediaQuery = getMediaQuery();

		if (mediaQuery != null) {
		
			try {
				List listOfFields = EFGRDBImportUtils.executeQueryForList(
						this.jdbcTemplate, mediaQuery, 1);
				for (java.util.Iterator iter = listOfFields.iterator(); iter
						.hasNext();) {
					EFGQueueObjectInterface field = (EFGQueueObjectInterface) iter
							.next();
					String fieldName = field.getObject(0);
					String query = makeQuery(fieldName);
					List listOfImages = EFGRDBImportUtils.executeQueryForList(
							this.jdbcTemplate, query, 1);
					Set mSet = new HashSet();
					//check Large images
					checkImages(listOfImages,mSet,getWebImagesHome());
					if (mSet.size() > 0) {
						this.errorTableBigImage.put(fieldName, mSet);
						this.numberOfErrors = this.numberOfErrors + mSet.size();
					}
					
					//check thumb nails
					mSet = new HashSet();
					checkImages(listOfImages,mSet,getWebThumbsHome());
					
					if (mSet.size() > 0) {
						this.errorTableThumbNails.put(fieldName, mSet);
						this.numberOfErrors = this.numberOfErrors + mSet.size();
					}
					
				}
				if (this.numberOfErrors > 0) {
					return false;
				}

			} catch (Exception e) {
				log.error(e.getMessage());
				
				return true;
			}
		}

		return true;
	}


	

	

	
	
	
}
