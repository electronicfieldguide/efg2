/**
 * 
 */
package project.efg.servlets.efgImpl;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.efg.efgDocument.EFGDocument;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.GroupsType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.EFGImportConstants;
import project.efg.util.EFGUniqueID;

/**
 * @author kasiedu
 * 
 */
public class PDFPageTemplateConfig extends EFGTemplateConfig {

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
	 * Get the HttpServletRequest parameters and return the value.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param name
	 *            the name of the parameter of the HttpServletRequest
	 * @return the value of the parameter to be used as a test query
	 * @throws IOException 
	 * @throws IOException 
	 */
	public void processParams(HttpServletRequest req, HttpServletResponse res) throws IOException{
		try {
			String isSavePDF = req
			.getParameter(EFGImportConstants.IS_SAVE_PDF);
			if(isSavePDF != null && 
					!isSavePDF.trim().equals("")){//we are not saving we are doing testing
				this.forwardPage(req,res,true,true);
				return;
			}
			String dsName = req
					.getParameter(EFGImportConstants.DATASOURCE_NAME);
		
			String displayName = req
					.getParameter(EFGImportConstants.DISPLAY_NAME);
			
			
				String xml = req.getParameter(EFGImportConstants.QUERY_RESULTS_XML);
				
				EFGDocument efgDoc = toEFGDoc(xml);
				//EFGImportConstants.QUERY_RESULTS_XML
				
				if(efgDoc == null){
					throw new Exception("efgDoc attribute is null");
				
				}
				
				//if we are doin
				XslPage xslPage = getXSLPageParams(req);
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
				this.add2Groups(req, xslPage);
				//allow writing to streams also
				req.setAttribute("xslPage", xslPage);
				req.setAttribute("efgDoc", efgDoc);
				//come with forward page as parameter
				//if it does not exists then forward to the 
				//default page
				this.forwardPage(req, res, false,false);
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
			
			try {
				this.forwardPage(req, res, false,true);
			} catch (Exception e) {
				res.getOutputStream().println(ee.getMessage());
				res.getOutputStream().flush();
				res.getOutputStream().close();

			}
		}
	}
	/**
	 * @param xml
	 * @return
	 */
	private EFGDocument toEFGDoc(String searchqueryresultsxml) throws Exception {
		EFGDocument efgDoc = null;
		
		if(searchqueryresultsxml != null && 
				!searchqueryresultsxml.equals("")){
				efgDoc =
				(project.efg.efgDocument.EFGDocument)EFGDocument.unmarshalEFGDocument(
				new StringReader(searchqueryresultsxml.trim()));		
		}
		return efgDoc;
	}


	/**
	 * 
	 * @param req
	 * @return
	 */
	private XslPage getXSLPageParams(HttpServletRequest req) {
		String dsName = req
				.getParameter(EFGImportConstants.DATASOURCE_NAME);

		String uniqueName = req
				.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
		String guid = req.getParameter(EFGImportConstants.GUID);
	
		String jspName = req.getParameter(EFGImportConstants.JSP_NAME);
		String isDefault = req.getParameter(EFGImportConstants.ISDEFAULT_STR);
		
		return this.getXSLPageType( 
				dsName,
				uniqueName, 
				guid,
				jspName, 
				isDefault);
	}
	/**
	 * 
	 * @param dsName
	 * @param uniqueName
	 * @param guid
	 * @param jspName
	 * @param isDefault
	 * @return
	 */
	//call when doing testing call other when doing saving
	private XslPage getXSLPageType( 
			String dsName,
			String uniqueName, 
			String guid, 
			String jspName,
			String isDefault) {
		
		Boolean bool = new Boolean(isDefault);
		boolean defaultFile = bool.booleanValue();
	
		XslPage page = new XslPage();
		page.setFileName("xslName");
		page.setDisplayName(uniqueName);

		if ((guid == null) || (guid.trim().equals("")) ){
			guid= EFGUniqueID.getID() + "";		
		}				
		page.setGuid(guid);
		
		if((jspName != null) && (!jspName.trim().equals(""))){
			page.setJspName(jspName);
		}
		// generate a guid
		page.setIsDefault(defaultFile);
	
		GroupsType groupsType = new GroupsType();
		page.setGroups(groupsType);
		return page;
	}
	/**
	 * 
	 * @param req
	 * @param res
	 * @param isError
	 * @throws Exception
	 */
	private void forwardPage(HttpServletRequest req, HttpServletResponse res,
			boolean isSave, boolean isError) throws Exception {
		RequestDispatcher dispatcher = null;

		try {
			if(isError){//forward to error page
				dispatcher = getServletContext().getRequestDispatcher(
						EFGImportConstants.TEMPLATE_ERROR_PAGE);
			}
			else if (isSave) {
				dispatcher = getServletContext().getRequestDispatcher(
						EFGImportConstants.TEMPLATE_CONFIG_PAGE);
			}
			else if (!isError) {//forward to pdf page
				dispatcher = getServletContext().getRequestDispatcher(
						EFGImportConstants.TO_PDF_PAGE);
			} 
		} catch (Exception ee) {
			 
			LoggerUtilsServlet.logErrors(ee);
			dispatcher = getServletContext().getRequestDispatcher(
					EFGImportConstants.TEMPLATE_ERROR_PAGE);
		}
		dispatcher.forward(req, res);
	}
}
