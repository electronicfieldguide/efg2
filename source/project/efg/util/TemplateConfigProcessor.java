/**
 * 
 */
package project.efg.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.efg.servlets.efgImpl.EFGContextListener;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslFileNamesType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * @author kasiedu
 * 
 */
public class TemplateConfigProcessor {

	private static GeneralCacheAdministrator cacheAdmin = EFGContextListener.getCacheAdmin();
	private TaxonPageTemplates tps;

	private String dsName,  searchType;

	public TemplateConfigProcessor(String dsName, 
			String searchType) {
		this.dsName = dsName;
		this.searchType = searchType;
		
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
	public List getTemplateList() {
		List list = new ArrayList();
		try {

			if (this.tps == null) {
				//log.debug("Creating new tps");
				this.tps = this.getTaxonPageTemplateRoot();
			}
			if (this.tps != null) {
				XslPageType xslPageType = this.getCurrentXSLPageType(
						this.dsName,  this.searchType);
				
				if (xslPageType != null) {
					for (int j = 0; j < xslPageType.getXslPageCount(); j++) {// find
						XslPage currentPage = xslPageType.getXslPage(j);
						String guid = currentPage.getGuid();
						String displayName = currentPage.getDisplayName();
						String jspName = currentPage.getJspName();
						if((jspName != null) && (!jspName.trim().equals(""))){
							GuidObject guidObject = 
								new GuidObject(guid,displayName,jspName);
							list.add(guidObject);
						}
					}
					if(list.size() > 0){
						Collections.sort(list);
					}
				}
			}
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
		}
		return list;
	}



	private TaxonPageTemplates getTaxonPageTemplateRoot() {
		TaxonPageTemplates ts = null;
			try {
				if(this.dsName == null){
					return null;
				}
				String templateName = this.dsName.toLowerCase() + EFGImportConstants.XML_EXT;
				System.out.println("Template Name: " + templateName);
				if(templateName != null){
					ts = (TaxonPageTemplates)cacheAdmin.getFromCache(templateName.toLowerCase());
				}

			}  catch (NeedsRefreshException nre) {
				System.err.println(nre.getMessage());
				LoggerUtilsServlet.logErrors(nre);
			}
			return ts;
		}
	
	private XslPageType getCurrentXSLPageType(String dsName,
			String searchType) {

		XslPageType xslPageType = null;
		try {

			int counter = this.tps.getTaxonPageTemplateCount();

			for (int i = 0; i < counter; i++) {

				TaxonPageTemplateType tp = tps.getTaxonPageTemplate(i);
				String ds = tp.getDatasourceName();
				if (ds.equalsIgnoreCase(dsName.trim())) {
					//log.debug("Found datasource: " + ds);
					XslFileNamesType xslFileNames = tp.getXSLFileNames();

					if (EFGImportConstants.SEARCH_TAXON_TYPE
							.equalsIgnoreCase(searchType)) {
						//log.debug("It is a taxon Page");
						xslPageType = xslFileNames.getXslTaxonPages();
					} else if (EFGImportConstants.SEARCH_PLATES_TYPE
							.equalsIgnoreCase(searchType)) {
						//log.debug("It is a plate");
						xslPageType = xslFileNames.getXslPlatePages();
					} else {
						//log.debug("It is a list");
						xslPageType = xslFileNames.getXslListPages();
					}
				}

				break;
			}
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
		}
		return xslPageType;

	}
}
