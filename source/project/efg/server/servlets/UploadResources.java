/**
 * 
 */
package project.efg.server.servlets;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;


/**
 * @author kasiedu
 *
 */
public class UploadResources extends UploadServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * This method is called when the servlet is first started up.
	 */
	/**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	/**
	 * 
	 */
	public void doCustomProcessing(HttpServletResponse response,File[] importedFiles) {
		
	}
	/**
	 * 
	 */
	public File getRepository(FileItem fileItem) {
		String fieldName = fileItem.getFieldName();
		File servletPath = new File(realPath);
		File repository = new File(servletPath,fieldName);
		return repository;
	}
}
