/**
 * 
 */
package project.efg.servlets.efgImpl;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jacob.asiedu
 *
 */
public class EFGServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	 * @param response
	 * @param message
	 * @param message2
	 * @throws IOException
	 */
	protected void sendCompleteResponse(HttpServletResponse response, String message, String message2) throws IOException
	{
	    if(message == null)
	    {
	      response.getOutputStream().print(message2);
	    }
	    else
	    {
	      response.getOutputStream().print(message + " " + message2);
	    }
	    response.getOutputStream().flush();
	}
}
