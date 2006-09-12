/**
 * 
 */
package project.efg.util;

import project.efg.servlets.efgImpl.EFGContextListener;
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
public class FindTemplate {


	private String dsName;

	private String xslType;

	private TaxonPageTemplates tps;

	private XslPage page;
	private static GeneralCacheAdministrator cacheAdmin = 
		EFGContextListener.getCacheAdmin();

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
		
		if(this.page == null){
		
			createXSLDefaultPage();
			
		}
	
		return this.page;
	}

	private XslPage getXSL() {

		XslPageType xslPageType = null;
		
		try {

			int counter = tps.getTaxonPageTemplateCount();
			XslPage currentPage = null;
			for (int i = 0; i < counter; i++) {

				TaxonPageTemplateType tp = 
					tps.getTaxonPageTemplate(i);
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
			return null;
		}
		
		return page;
	}

	
	private TaxonPageTemplates getTaxonPageTemplate() {
		TaxonPageTemplates tps = null;
			
				//if the file does not exists 
				//get the defaults from database
				//if that fails then return null;
				String fileName = this.dsName.toLowerCase() + EFGImportConstants.XML_EXT;
				try {
					  
					tps = (TaxonPageTemplates)cacheAdmin.getFromCache(fileName);
					//log.debug("Object obtained from cache ");
				} catch (NeedsRefreshException nre) {
				    try {
				    	//create and write the default file and load
				    	cacheAdmin.putInCache(fileName.toLowerCase(),tps,EFGContextListener.templateFilesGroup);
						//EFGContextListener.lastModifiedTemplateFileTable.put(fileName.toLowerCase(),new Long(f.lastModified()));
				    	//cacheAdmin.putInCache(fileName.toLowerCase(), tps,EFGContextListener.templateFilesGroup);
				    	//log.debug("Object put in cache");
				    } catch (Exception ex) {
				        // We have the current content if we want fail-over.
				    	tps = (TaxonPageTemplates)nre.getCacheContent();
				    	//log.debug("Object fail over obejct ");
				        // It is essential that cancelUpdate is called if the
				        // cached content is not rebuilt
				    	cacheAdmin.cancelUpdate(fileName.toLowerCase());
				    }
				}
				return tps;
	}
	/**
	 * 
	 */
	private void createXSLDefaultPage() {
		
		this.page = new XslPage();
		String xslName = EFGImportConstants.DEFAULT_SEARCH_FILE;
		if (EFGImportConstants.TAXONPAGE_XSL
				.equalsIgnoreCase(this.xslType)) {
			xslName = EFGImportConstants.DEFAULT_TAXON_PAGE_FILE;
		} 
		this.page.setFileName(xslName);
		this.page.setIsDefault(true);
	}
}
