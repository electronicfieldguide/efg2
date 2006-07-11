/**
 * 
 */
package project.efg.servlets.efgImpl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.log4j.Logger;

import project.efg.servlets.factory.EFGDiskItemFileFactory;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class UploadResources extends HttpServlet {
	static Logger log = null;

	private String realPath;
	
	
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
		try {
			log = Logger.getLogger(UploadResources.class);
		} catch (Exception ee) {
		}
		realPath = getServletContext().getRealPath("/");
	}

	/**
	 * Handles an HTTP GET request - Based most likely on a clicked link.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	/**
	 * Handles an HTTP POST request - Based most likely on a form submission.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		RequestDispatcher dispatcher = null;
		res.setContentType(EFGImportConstants.TEXT_HTML);
		PrintWriter out = null;
		String message ="";
		try{
			out = res.getWriter();
			if( isMultipartContent(req)){
				message = this.doProcess(req,res);
				dispatcher = getServletContext().getRequestDispatcher(
						EFGImportConstants.TEMPLATE_SUCCESS_PAGE);
				req.setAttribute("Message",message);
			}
			else{
				throw new Exception("");
			}
		}
		catch(Exception ee){
			dispatcher = getServletContext().getRequestDispatcher(
					EFGImportConstants.TEMPLATE_ERROR_PAGE);
			req.setAttribute("Message","The request you sent is not a multipart request!!");
			
		}
		try{
			dispatcher.forward(req, res);
			out.flush();
			res.flushBuffer();
		}
		catch(Exception eee){
			dispatcher = getServletContext().getRequestDispatcher(
					EFGImportConstants.TEMPLATE_ERROR_PAGE);
			req.setAttribute("Message",eee.getMessage());
			try {
				dispatcher.forward(req, res);
			} catch (ServletException e) {
				
			} catch (IOException e) {
				
			}
		}
	}
	 public static final boolean isMultipartContent(
	          HttpServletRequest request) {
	       if (!"post".equals(request.getMethod().toLowerCase())) {
	           return false;
	      }
	       return FileUploadBase.isMultipartContent(
	               new ServletRequestContext(request));
	   }
	/**
	 * @param req
	 * @param res
	 */
	private String doProcess(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		
		List itemsToProcess = new ArrayList();
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(EFGImportConstants.TEMPLATE_IMAGES_DEFAULT_SIZE);
//		 Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		
//		 Set overall request size constraint
		upload.setSizeMax(EFGImportConstants.TEMPLATE_IMAGES_DEFAULT_SIZE);

//		 Parse the request
		List items = null;
		File repository = null;
		 FileItem item = null;
		try{
			
			items = upload.parseRequest(req);
			log.debug("Request parsed successfully");
//			 Process the uploaded items
			Iterator iter = items.iterator();
			
			while (iter.hasNext()) {
			   item = (FileItem) iter.next();

			    if (!item.isFormField()) {
			    	log.debug("It is a form upload");
			      itemsToProcess.add(item);
			    }
			    else{
			    	if((repository == null) || (repository.getAbsolutePath().trim().equals(""))){
			    		String fieldName = item.getFieldName();
			    		if(EFGImportConstants.ITEMTYPE.equalsIgnoreCase(fieldName)){
			    			log.debug("FieldName: " + fieldName);
			    			String fieldValue = item.getString();
			    			log.debug("FieldValue: " + fieldValue);
			    			repository  = EFGDiskItemFileFactory.getInstance(realPath,fieldValue);
			    			log.debug("Repository found and it is: " + repository.getAbsolutePath());
			    		}
			    	}
			    	
			    }
			}
			if(repository == null){
				repository = new File(realPath);
			}
			factory.setRepository(repository);
			return startUploadProcess(repository,itemsToProcess);
		}
		catch(Exception ee){
			String message = "";
			if(item != null){
				message = item.getName();
			}
			message= message+ " " + ee.getMessage();
			log.error(message);
			message = message  + "<br/>";
			return message;
		}
	}

	/**
	 * @param repository
	 * @param itemsToProcess
	 */
	private String startUploadProcess(File repository, List items) {
		// TODO Auto-generated method stub
		StringBuffer message = new StringBuffer();
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = (FileItem) iter.next();
		    String retMessage = processUploadedFile(repository.getAbsolutePath(),item);
		    if((retMessage != null) && (!retMessage.trim().equals(""))){
		    	message.append(retMessage);
		    	message.append("<br/>");
		    }
		}
		return message.toString();
	}

	/**
	 * @param item
	 */
	private String processUploadedFile(String repos,FileItem item){
		
		String message = "";
		String itemName =item.getName();
		log.debug("repos: " + repos);
		log.debug("File path: " + itemName);
		try{
			
			if(itemName == null){
				return message;
			}
			if(itemName.trim().equals("")){
				return message;
			}
			File fullFile  = new File(itemName);  
			File savedFile = new File(repos,fullFile.getName());
			item.write(savedFile);
			message = fullFile.getName() + " successfully saved!!";
		}
		catch(Exception ee){	
			if(ee != null){
				message= itemName + " " + ee.getMessage();
				log.error(message);
				message = message  + "<br/>";
			}
		}
		if(message == null){
			message = "";
		}
		return message.trim();
	}


}
