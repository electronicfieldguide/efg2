/**
 * 
 */
package project.efg.util;

import project.efg.servlets.efgImpl.EFGContextListener;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslFileNamesType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * @author kasiedu
 * 
 */
public class FindTemplate {


	private String dsName;

	private String xslType;

	private TaxonPageTemplates tps;

	private XslPage page;
	private static GeneralCacheAdministrator cacheAdmin = EFGContextListener.getCacheAdmin();

	public FindTemplate(String dsName, String xslType) {
		this.dsName = dsName;
		this.xslType = xslType;
		this.tps = this.getTaxonPageTemplate();
	}

	public XslPage getXSLFileName() {
		if (this.page == null) {
			if (this.tps != null) {
				this.page = this.getXSL();
			}
		}
		return this.page;
	}

	private XslPage getXSL() {

		XslPageType xslPageType = null;
		
		try {

			int counter = tps.getTaxonPageTemplateCount();
			XslPage currentPage = null;
			for (int i = 0; i < counter; i++) {

				TaxonPageTemplateType tp = tps.getTaxonPageTemplate(i);
				String ds = tp.getDatasourceName();
				if (ds.equalsIgnoreCase(this.dsName.trim())) {
					//log.debug("Found datasource: " + ds);
					XslFileNamesType xslFileNames = tp.getXSLFileNames();

					if (EFGImportConstants.TAXONPAGE_XSL
							.equalsIgnoreCase(this.xslType)) {
							//log.debug("It is a taxon Page");
						xslPageType = xslFileNames.getXslTaxonPages();

					} else if (EFGImportConstants.SEARCHPAGE_PLATES_XSL
							.equalsIgnoreCase(this.xslType)) {
						//log.debug("It is a palte");
						xslPageType = xslFileNames.getXslPlatePages();
					} else{
						//log.debug("It is a list");
						xslPageType = xslFileNames.getXslListPages();
						
					}

					for (int j = 0; j < xslPageType.getXslPageCount(); ++j) {// find
						currentPage = xslPageType.getXslPage(j);
						
						boolean isDefault = currentPage.getIsDefault();
						//log.debug("currentXSLName : " + currentXSLFile);
						if (isDefault) {// if
							this.page = currentPage;
							break;
						}
					}
					if(this.page == null){
						this.page = currentPage;
					}
					break;
				}
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
			//log.error("Returning null because of previous error!!");
			return null;
		}
		
		return page;
	}

	private TaxonPageTemplates getTaxonPageTemplate() {
	
			try {
				String fileName = this.dsName.toLowerCase() + EFGImportConstants.XML_EXT;
				return(TaxonPageTemplates)cacheAdmin.getFromCache(fileName.toLowerCase());
			} catch (Exception ee) {
				LoggerUtilsServlet.logErrors(ee);
			}
			return null;
	}
}
