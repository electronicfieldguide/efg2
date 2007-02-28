/**
 * 
 */
package project.efg.Imports.efgImportsUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.Imports.rdb.EFGRDBImportUtils;
import project.efg.servlets.efgServletsUtil.EFGParseObject;
import project.efg.servlets.efgServletsUtil.EFGParseObjectList;
import project.efg.servlets.efgServletsUtil.EFGParseStates;
import project.efg.util.EFGImportConstants;
import project.efg.util.RegularExpresionConstants;
/**
 * @author kasiedu
 * 
 */
public class MediaResourceDataChecker extends DataChecker {
	

	private static String imagesHome;
	private String mediaQuery;
	static Logger log = null;
	private EFGParseStates efgParseStates;
	private HashMap errorTable;

	static {
		try {
			log = Logger.getLogger(MediaResourceDataChecker.class);
		} catch (Exception ee) {
		}
	}
	static String images_home = 
		EFGImportConstants.EFGProperties.getProperty(
				"efg.images.home");
	static String thumbshome = 
		EFGImportConstants.EFGProperties.getProperty(
				"efg.mediaresources.thumbs.home");
	public MediaResourceDataChecker(DBObject dbObject, String displayName) {
		super(dbObject,displayName);
		this.efgParseStates = new EFGParseStates();
		this.errorTable = new HashMap();
	}





	private String getMediaQuery() {
		if (this.mediaQuery == null) {

			StringBuffer queryBuffer = new StringBuffer();
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
			StringBuffer imagesHomeBuffer = new StringBuffer(getServerHome());
			
			imagesHomeBuffer.append(File.separator);
			imagesHomeBuffer.append(images_home);
			imagesHomeBuffer.append(File.separator);
			imagesHome = imagesHomeBuffer.toString();
		}
		return imagesHome;

	}



	



	
	public String displayErrors() {

		StringBuffer errorBuffer = new StringBuffer();
		errorBuffer.append("<html><body>");
		errorBuffer.append("<h1>Application Found ");
		errorBuffer.append(this.getNumberOfErrors() + "");
		errorBuffer.append(" potential errors </h1>");
		errorBuffer.append("<hr></hr>");

		errorBuffer.append("<table border=\"1\">");
		errorBuffer
				.append("<tr><th>Field Name</th> <th>Missing Files</th></tr>");

		Iterator fieldIter = this.errorTable.keySet().iterator();

		while (fieldIter.hasNext()) {
			String fieldName = (String) fieldIter.next();
			errorBuffer.append("<tr><td>");
			errorBuffer.append(fieldName);
			errorBuffer.append("</td> <td></td></tr>");

			Set dataset = (Set) this.errorTable.get(fieldName);

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
							
							File dir = new File(getWebImagesHome() + File.separator + imageName.trim());
							URL dirURL = dir.toURL();
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
					if (mSet.size() > 0) {
						this.errorTable.put(fieldName, mSet);
					}
					this.numberOfErrors = this.numberOfErrors + mSet.size();

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
