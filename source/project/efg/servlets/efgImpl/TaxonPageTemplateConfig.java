/**
 * 
 */
package project.efg.servlets.efgImpl;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.GroupsType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.EFGFieldObject;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 * 
 */
public class TaxonPageTemplateConfig extends EFGTemplateConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//protected static GeneralCacheAdministrator cacheAdmin = EFGContextListener.getCacheAdmin();

	

	//protected String realPath;

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
		//realPath = getServletContext().getRealPath("/");
	}
	/**
	 * Get the HttpServletRequest parameters and return the value.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param name
	 *            the name of the parameter of the HttpServletRequest
	 * @return the value of the parameter to be used as a test query
	 */
	public void processParams(HttpServletRequest req, HttpServletResponse res) {
		try {
		
			String dsName = req
					.getParameter(EFGImportConstants.DATASOURCE_NAME);
		
			String displayName = req
					.getParameter(EFGImportConstants.DISPLAY_NAME);
		
			TaxonPageTemplates tps = this.getTaxonPageTemplateRoot(dsName);

			if (tps == null) {
				throw new Exception("Datasource File not found");
			}

			XslPage xslPage = getXSLPageParams(req, tps);
			if (xslPage == null) {
				throw new Exception("xslPage is null");
			}
			String guid = xslPage.getGuid();
			if ((guid != null) && (!guid.trim().equals("")) ){
				req.setAttribute(EFGImportConstants.GUID,guid);
			}
			String uniqueName = xslPage.getDisplayName();
			if ((uniqueName != null) && (!uniqueName.trim().equals("")) ){
				req.setAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME,uniqueName);
			}
			GroupsType groups = xslPage.getGroups();
			if (groups == null) {
				throw new Exception("Groups is null");
			}

			req.setAttribute(EFGImportConstants.DATASOURCE_NAME, dsName);
			req.setAttribute(EFGImportConstants.DISPLAY_NAME_COL, displayName);
			EFGFieldObject field = this.add2Groups(req, xslPage);
			//allow writing to streams also
			boolean done = this.writeFile(dsName, tps);
			//log.debug("Done writing");
			if (done) {
				if (field != null) {
					req.setAttribute("fieldName", field.getFieldName());
					req.setAttribute("fieldValue", field.getFieldValue());
				}
				this.forwardPage(req, res, false);
			} else {
				throw new Exception("Error occured during saving of file");
			}
		} catch (Exception ee) {
			ee.printStackTrace();
			
			try {
				this.forwardPage(req, res, true);
			} catch (Exception eef) {

			}
		}
	}
	private void forwardPage(HttpServletRequest req, HttpServletResponse res,
			boolean isError) throws Exception {
		RequestDispatcher dispatcher = null;
		res.setContentType(EFGImportConstants.TEXT_HTML);
		PrintWriter out = res.getWriter();
		try {
			// forward to TestConfigPage.jsp
			if (!isError) {
				String searchType = req
				.getParameter(EFGImportConstants.SEARCH_TYPE_STR);
				//log.debug("No errors");
				String searchPage = req
						.getParameter(EFGImportConstants.SEARCH_PAGE_STR);// "search"
				String allTableName = req.getParameter(EFGImportConstants.ALL_TABLE_NAME);
				
				if(allTableName == null || allTableName.trim().equals("")) {
					allTableName = EFGImportConstants.EFG_RDB_TABLES;
				}
				req.setAttribute(EFGImportConstants.ALL_TABLE_NAME,
						allTableName);	
				if (searchPage != null) {// allow multiple xslNames
					//log.debug("Forward to Test Search page");
					
					if (EFGImportConstants.SEARCH_PLATES_TYPE
							.equalsIgnoreCase(searchType)) {
						req.setAttribute(EFGImportConstants.SEARCH_TYPE_STR,
								EFGImportConstants.SEARCH_PLATES_TYPE);
						dispatcher = getServletContext().getRequestDispatcher(
								EFGImportConstants.TEST_SEARCH_CONFIG_PAGE);
					} 
					else if (EFGImportConstants.SEARCH_SEARCH_TYPE
							.equalsIgnoreCase(searchType)) {//FIX ME
						req.setAttribute(EFGImportConstants.SEARCH_TYPE_STR,
								EFGImportConstants.SEARCH_SEARCH_TYPE);
						dispatcher = getServletContext().getRequestDispatcher(
								EFGImportConstants.TEST_TAXON_CONFIG_PAGE);
					}
					else if (EFGImportConstants.SEARCH_PDFS_TYPE
							.equalsIgnoreCase(searchType)) {
						req.setAttribute(EFGImportConstants.SEARCH_TYPE_STR,
								EFGImportConstants.SEARCH_PDFS_TYPE);
						dispatcher = getServletContext().getRequestDispatcher(
								EFGImportConstants.PDF_SUCCESS_PAGE);
					}
					else if (EFGImportConstants.SEARCH_LISTS_TYPE
							.equalsIgnoreCase(searchType)) {
						req.setAttribute(EFGImportConstants.SEARCH_TYPE_STR,
								EFGImportConstants.SEARCH_LISTS_TYPE);
					
					dispatcher = getServletContext().getRequestDispatcher(
							EFGImportConstants.TEST_SEARCH_CONFIG_PAGE);
					}
					else{
						if (EFGImportConstants.SEARCH_PDFS_TYPE
								.equalsIgnoreCase(searchType)) {
							req.setAttribute(EFGImportConstants.SEARCH_TYPE_STR,
									EFGImportConstants.SEARCH_PDFS_TYPE);
							dispatcher = getServletContext().getRequestDispatcher(
									EFGImportConstants.PDF_SUCCESS_PAGE);
						}
						else{
						dispatcher = getServletContext().getRequestDispatcher(
								EFGImportConstants.TEMPLATE_ERROR_PAGE);
						}
					}

				} else {
					//log.debug("Forward to Test taxon page");
					dispatcher = getServletContext().getRequestDispatcher(
							EFGImportConstants.TEST_TAXON_CONFIG_PAGE);
				}
			} else {
				//log.debug("Forward to error page1");
				dispatcher = getServletContext().getRequestDispatcher(
						EFGImportConstants.TEMPLATE_ERROR_PAGE);
			}
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
			dispatcher = getServletContext().getRequestDispatcher(
					EFGImportConstants.TEMPLATE_ERROR_PAGE);
		}
		dispatcher.forward(req, res);
		out.flush();
		res.flushBuffer();
	}


}
