/**
 * 
 */
package project.efg.servlets.efgImpl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.efg.efgDocument.EFGDocument;
import project.efg.efgpdf.pdf.EFG2PDF;
import project.efg.efgpdf.pdf.EFG2PDFInterface;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.EFGImagesConstants;

/**
 * @author jacob.asiedu
 *
 */
public class EFG2PDFConverter extends EFGServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private File mediaResourceDirectory;

	
	/**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String realPath = getServletContext().getRealPath("/");
		this.mediaResourceDirectory = new File(realPath,EFGImagesConstants.EFG_IMAGES_DIR); 
	}

	/**
	 * Handles an HTTP GET request - Based most likely on a clicked link.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	doPost(request, response);
	}
	/**
	 * 
	 * @param session
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doPDF(HttpServletRequest req, 
			HttpServletResponse response)throws ServletException, IOException 
	{
		 ServletOutputStream out = null;
		 try {
	        	EFGDocument efgDoc = (EFGDocument)req.getAttribute("efgDoc");
	    		XslPage xslPage = (XslPage)req.getAttribute("xslPage");
	    		
	    		if(efgDoc == null || xslPage == null){
	    			throw new Exception("Error in Generating pdf. XslPage and efgDoc request attributes must be present!!");
	    		}
	    		//comment me out
	    		
				java.io.Writer stringw = new StringWriter();
				xslPage.marshal(stringw);
				System.out.println(stringw.toString());
				
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				//setting the content type
				response.setContentType("application/pdf");
				out =  response.getOutputStream();
	    		EFG2PDFInterface efg2pdf = new EFG2PDF();
	    		 efg2pdf.writePdfToStream(efgDoc,xslPage,out,this.mediaResourceDirectory);
	    		 out.flush();
	    		 out.close();
		 }
		 catch(Exception ee){
			 
			 LoggerUtilsServlet.logErrors(ee);
			 if(out == null){
				 out =  response.getOutputStream();
			 }
			 out.println(ee.getMessage()) ;
			 out.flush();
    		 out.close();
		 }
	}

	/**
	 * 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		 try {
		    doPDF(req, res);
        }
        catch(Exception ee){
        	 
        	LoggerUtilsServlet.logErrors(ee);
        	sendCompleteResponse(res,"Error",isError());        	//output error message
        }
	}

	/**
	 * Sends an error message in HTML to the browser
	 * @param stream	the outputstream of the servlet
	 * @throws IOException
	 */
	private String isError(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("An error occured during PDF processing." +
				" Consult the efg team for help");
		return buffer.toString();
	}

}
