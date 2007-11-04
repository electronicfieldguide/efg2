/**
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu, Kimmy Lin
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
 */

package project.efg.server.servlets;

//import project.efg.digir.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.EFGType;
import project.efg.efgDocument.Item;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourceType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.searchableDocument.Searchable;
import project.efg.searchableDocument.Searchables;
import project.efg.server.factory.EFGSpringFactory;
import project.efg.server.interfaces.EFGDataObjectListInterface;
import project.efg.server.utils.EFGDataSourceHelperInterface;
import project.efg.server.utils.EFGSearchableTemplate;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.server.utils.ServletCacheManager;
import project.efg.server.utils.XSLProperties;
import project.efg.server.utils.XSLTObjectInterface;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.interfaces.EFGDataObject;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.TemplateMapObjectHandler;

/**
 * This servlet handles requests to display a list of taxa with attribute values
 * matching a set of input parameters regardless of the backend.
 */
public class SearchPageBuilder extends EFGSearchServletInterface implements EFGImportConstants{
	static final long serialVersionUID = 1;
	
	static Logger log = null;
	
	/**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	
	}

	 /* 
	 */
	private TaxonPageTemplates getTemplates(
			String displayName, 
			String datasourceName,
			String mainTableName) {
				return TemplateMapObjectHandler.getTemplateFromDB(null, 
				displayName,datasourceName, mainTableName);
	
		
	
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
			
			if(displayFormat == null){
				displayFormat = (String)req
				.getAttribute(EFGImportConstants.DISPLAY_FORMAT);
			}
			/*String guid = req.getParameter(EFGImportConstants.GUID);
			if(guid == null && !displayFormat.equals(EFGImportConstants.XML)){
				forward2Default(req, res);
				return;
			}*/
			String mainTableName = req
			.getParameter(EFGImportConstants.ALL_TABLE_NAME);
			//TO DO: MERGE present and presentXML
			//log.debug("Get query instance");
			EFGSearchableTemplate xmlDoc = this.getSearchables(req);
			//log.debug("After get query instance");
			if (EFGImportConstants.XML.equalsIgnoreCase(displayFormat)) {
				
				presentXML(req, res,xmlDoc);
			} else {
				
				//use Spring
					req.setAttribute(
						EFGImportConstants.ALL_TABLE_NAME,
						mainTableName);
				present(req, res,xmlDoc);
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
			LoggerUtilsServlet.logErrors(ee);
			res.setContentType(EFGImportConstants.TEXT_HTML);
			PrintWriter out = res.getWriter();
			out
			.println("<h3>There are no results matching your search criteria.</h3>");
			out.flush();
			res.flushBuffer();
		}
	}
	private EFGSearchableTemplate getSearchables(HttpServletRequest request){
		
		   String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME); 
		   String datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
		  
		   String context = request.getContextPath();
		   String tableName = request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
		   if(tableName == null || tableName.trim().equals("")){
			   tableName = EFGImportConstants.EFG_RDB_TABLES;
		   }
			if(displayName == null || "".equals(displayName)){
			
				Map map = ServletCacheManager.getDatasourceCache(tableName.toLowerCase());
				 	
				 	displayName = (String)map.get(datasourceName.toLowerCase());
			}

		   EFGDataSourceHelperInterface dsHelper =
				EFGSpringFactory.getDatasourceHelper();
		   dsHelper.setMainDataTableName(tableName);
		   EFGDataObjectListInterface doSearches = dsHelper.getSearchable(displayName,datasourceName);	
		   if(datasourceName == null){
			datasourceName=doSearches.getDatasourceName();		
		   }  
		   

	 Searchables searchables = new Searchables();
	 searchables.setDatasourceName(datasourceName);
		for(int s = 0 ; s < doSearches.getEFGDataObjectCount(); s++){
			EFGDataObject searchable = doSearches.getEFGDataObject(s);
	   		
		 	String fieldName =searchable.getName();
			String legalName = searchable.getLegalName();
			int order = searchable.getOrder();
			Searchable searchable1 = new Searchable();
			searchable1.setDbFieldName(legalName);
			searchable1.setFieldName(fieldName);
			searchable1.setOrder(order);
			add2searchable(searchable1 ,searchable.getEFGLists());
			add2searchable(searchable1 ,searchable.getMediaResources());
			add2searchable(searchable1 ,searchable.getStates());
			add2searchable(searchable1 ,searchable.getStatisticalMeasures());
			searchables.addSearchable(s,searchable1);
		}
		
		EFGSearchableTemplate efgT = new EFGSearchableTemplate();
		efgT.setSeacrchableDocument(searchables);
		TaxonPageTemplates templates = getTemplates(displayName,datasourceName,tableName);

		efgT.setTaxonPageTemplates(templates);

		return efgT ;
	}
	
	/**
	 * @param statisticalMeasures
	 */
	private void add2searchable(Searchable searchable,StatisticalMeasuresType states) {
		if((states != null)&&(states.getStatisticalMeasureCount() > 0)){
			for(int i = 0; i < states.getStatisticalMeasureCount();i++ ){
				double max = states.getMax();
				
				double min = states.getMin();
				String units = states.getUnit();
				String state = min + "-" + max + " " + units;
				searchable.addSearchableState(state.trim());
			}
		}
		
	}

	/**
	 * @param states
	 */
	private void add2searchable(Searchable searchable,ItemsType states) {
		
		if((states != null)&&(states.getItemCount() > 0)){
			for(int i = 0; i < states.getItemCount();i++ ){
				Item item = states.getItem(i);
				String state = item.getContent();
				searchable.addSearchableState(state);
			}
		}
	}

	/**
	 * @param mediaResources
	 */
	private void add2searchable(Searchable searchable,MediaResourcesType mediaResources) {
		if(( mediaResources != null)&&( mediaResources.getMediaResourceCount() > 0)){
			for(int i = 0; i <mediaResources.getMediaResourceCount();i++ ){
				MediaResourceType item = mediaResources.getMediaResource(i);
				String state = item.getContent();
				searchable.addSearchableState(state);
			}
		}

		
	}
	/**
	 * @param lists
	 */
	private void add2searchable(Searchable searchable,EFGListsType lists) {
		if(( lists != null)&&( lists.getEFGListCount() > 0)){
			for(int i = 0; i < lists.getEFGListCount();i++ ){
				EFGType item = lists.getEFGList(i);
				String state = item.getContent();
				searchable.addSearchableState(state);
			}
		}
	}

	/**
	 * This class forward the result to presentXML.jsp to show the EFGDocument.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 * @param xmlDoc
	 *            the result EFGDocument of the http query
	 */
	protected void presentXML(HttpServletRequest req, HttpServletResponse res,Object obj) 
	throws IOException {

		try {
			EFGSearchableTemplate xmlDoc = (EFGSearchableTemplate)obj;
			req.setAttribute(EFGImportConstants.XML, xmlDoc.getSearchableDocument());
			// servlet interface provides getServletConfig method,
			ServletContext sctxt = getServletConfig().getServletContext();
			RequestDispatcher rd = sctxt
			.getRequestDispatcher("/SearchPageXML.jsp");
			rd.forward(req, res);
		} catch (Exception ex) {
			//log.error(ex.getMessage());
			LoggerUtilsServlet.logErrors(ex);
			EFGContextListener.presentError(this.getClass().getName(), ex
					.getMessage(), res);
		}
	}
	/**
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	private void forward2Default(HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException{
		String mainTableName = req
		.getParameter(EFGImportConstants.ALL_TABLE_NAME);
		StringBuffer fwdStrEncodedBuffer = 
			new StringBuffer("/SearchPage.jsp?pageType=option");
		StringBuffer forwardStringBuffer = 
			new StringBuffer();
		forwardStringBuffer.append(EFGImportConstants.ALL_TABLE_NAME);
		forwardStringBuffer.append("=");
		forwardStringBuffer.append(mainTableName);
		forwardStringBuffer.append("&");
		forwardStringBuffer.append("displayFormat=HTML&");
		if(req.getParameter(EFGImportConstants.DISPLAY_NAME) != null){
			forwardStringBuffer.append(EFGImportConstants.DISPLAY_NAME);
			forwardStringBuffer.append("=");
			forwardStringBuffer.append(req.getParameter(EFGImportConstants.DISPLAY_NAME));
			forwardStringBuffer.append("&");
		}
		forwardStringBuffer.append(EFGImportConstants.DATASOURCE_NAME);
		forwardStringBuffer.append("=");
		forwardStringBuffer.append(req.getParameter(EFGImportConstants.DATASOURCE_NAME));
		

		fwdStrEncodedBuffer.append(
				URLEncoder.encode(forwardStringBuffer.toString(), 
						"UTF-8")
						);
		ServletContext sctxt = getServletConfig().getServletContext();
		RequestDispatcher rd = 
			sctxt.getRequestDispatcher(fwdStrEncodedBuffer.toString());
		rd.forward(req, res);

	}
	/**
	 * This class forward the result to ApplyXSL servlet with appropriate xsl
	 * file depending on the number of the taxon in the result document. If
	 * there is one taxa, the xsl will transform to display the specie page.
	 * Otherwise a page of thumb pictures for the taxon.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 * @param xmlDoc
	 *            the result EFGDocument of the http query
	 */
	protected void present(HttpServletRequest req, 
			HttpServletResponse res,Object obj) throws Exception {
		
		
		
		XSLTObjectInterface  xslto = 
			EFGSpringFactory.getXSLTObjectInstance(5, 
					EFGImportConstants.SEARCH_SEARCH_TYPE);
		
		
		String mainTableName = req
		.getParameter(EFGImportConstants.ALL_TABLE_NAME);
		xslto.setMainDataTableName(mainTableName);	
		
		XSLProperties xslProps = xslto.getXSLProperties(req.getParameterMap());
		String xslFileName = xslProps.getXSLFileName();
		
		boolean isDefault = false;

		XslPage page = xslto.getXSLFile(req.getParameter(EFGImportConstants.DATASOURCE_NAME),
		EFGImportConstants.SEARCHPAGE_XSL);
		if(page == null){
			isDefault = true;
			}
		else{
			if(page.getGuid() == null){
				isDefault = true;
			}

		}
		//if it is the default then forward to deafult page
		if(isDefault || xslFileName == null || 
				xslFileName.endsWith(
						EFGImportConstants.DEFAULT_SEARCH_PAGE_FILE
						)){//forward to the default search page
			
			
			forward2Default(req,res);
			return;
		}
		EFGSearchableTemplate xmlDoc = (EFGSearchableTemplate)obj;
		req.setAttribute(EFGImportConstants.XML, xmlDoc);
		//log.debug("call xslFileName: " + xslFileName);
		req.setAttribute(EFGImportConstants.XSL_STRING, xslFileName);
		setApplXSLAttributes(req,page.getGuid());
		// get xsl file name from the templateConfig file name
		StringBuffer forwardStringBuffer = 
		new StringBuffer(req.getContextPath());
		forwardStringBuffer.append("/");
		forwardStringBuffer
		.append(EFGImportConstants.TEMPLATES_FOLDER_NAME);
		forwardStringBuffer.append("/");
		forwardStringBuffer.append(xslFileName);
		
		StringBuffer fwdStrEncodedBuffer = new StringBuffer("/");
		//forward to pdf or html xslt servlet
		fwdStrEncodedBuffer.append(applyXSLServlet);
		fwdStrEncodedBuffer.append("?xsl=");
		fwdStrEncodedBuffer.append(
				URLEncoder.encode(forwardStringBuffer.toString(), 
						"UTF-8")
						);
		ServletContext sctxt = getServletConfig().getServletContext();
		RequestDispatcher rd = 
			sctxt.getRequestDispatcher(fwdStrEncodedBuffer.toString());
		rd.forward(req, res);	
	}
	/**
	 * 
	 * @param req
	 * @param guidx
	 */
	private void setApplXSLAttributes(HttpServletRequest req, String guidx) {
		req.setAttribute(EFGImportConstants.DATASOURCE_NAME, 
				req.getParameter(EFGImportConstants.DATASOURCE_NAME));
		req.setAttribute(EFGImportConstants.ALL_TABLE_NAME,
				req.getParameter(EFGImportConstants.ALL_TABLE_NAME));
		req.setAttribute(EFGImportConstants.DISPLAY_NAME,
				req.getParameter(EFGImportConstants.DISPLAY_NAME));
		String guid = req.getParameter(EFGImportConstants.GUID);
		if(guid == null){
			guid = guidx;
		}
		req.setAttribute(EFGImportConstants.GUID,guid);
		req.setAttribute(EFGImportConstants.SEARCHTYPE,
				EFGImportConstants.SEARCH_SEARCH_TYPE);
	}
}
