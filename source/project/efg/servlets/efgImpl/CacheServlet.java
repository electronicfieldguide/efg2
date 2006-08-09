/**
 * 
 */
package project.efg.servlets.efgImpl;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import project.efg.util.CacheStarter;

/**
 * @author kasiedu
 *
 */
public class CacheServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String url;
	static Logger log = null;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			//log = Logger.getLogger(CacheServlet.class);
			
		} catch (Exception ee) {
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
		this.doInit(getServletURL(req));
	}

	/**
	 * Handles an HTTP POST request - Based most likely on a form submission.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	private void doInit(String url) {
		//log.debug("About to execute cache starter");
		//String url = getServletURL(req);
		CacheStarter cacheStarter = new CacheStarter();
		////log.debug("url: " + url);
		//cacheStarter.executeCache(url);
		//log.debug("cache starter execution done");
	}
	private static synchronized String getServletURL(HttpServletRequest request)
		throws ServletException, IOException 
	    {
		if(url == null){	 
		ServletRequestWrapper srw = new ServletRequestWrapper(request);
		String server = srw.getServerName();
		int port = srw.getServerPort();
		String scheme =srw.getScheme();
		
		StringBuffer buf = new StringBuffer();
		buf.append(scheme);
		buf.append("://");
		buf.append(server);
		buf.append(":");
		buf.append(port);
		String path = request.getContextPath();
		buf.append(path);
		buf.append("/");
		url =  buf.toString();
		}
		return url;
	   }
}
