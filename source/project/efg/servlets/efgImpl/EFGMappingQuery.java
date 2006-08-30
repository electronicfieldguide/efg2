/**
 * 
 */
package project.efg.servlets.efgImpl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.efg.servlets.efgInterface.EFGHTTPQuery;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.servlets.rdb.MapQuery;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class EFGMappingQuery extends SearchEngine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	/**
	 * This method is called when the servlet is shutdown. Closes the database
	 * then disconnects the servlet's thread from its Session. It's complement
	 * is the init method.
	 */
	public void destroy() {
		super.destroy();
	}
	
	/**
	 * Handles an HTTP POST request - Based most likely on a form submission.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		try {
			String displayFormat = req
			.getParameter(EFGImportConstants.DISPLAY_FORMAT);
			
			EFGHTTPQuery query = new MapQuery(req);
			//log.debug("After get query instance");
			if (EFGImportConstants.XML.equalsIgnoreCase(displayFormat)) {
				presentXML(req, res, query.getEFGDocument());
			} else {	
				present(req, res, query.getEFGDocument());
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
			LoggerUtilsServlet.logErrors(ee);
			res.setContentType(EFGImportConstants.TEXT_HTML);
			PrintWriter out = res.getWriter();
			out
			.println("<H3>There are no results matching your search criteria.</H3>");
			out.flush();
			res.flushBuffer();
		}
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

}
