/**
 * $Id$
 * $Name:  $
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
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
package project.efg.server.utils;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import java.util.Map;
import project.efg.server.servlets.EFGContextListener;
import project.efg.templates.taxonPageTemplates.*;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.TemplateMapObjectHandler;

// Referenced classes of package project.efg.server.utils:
//            ServletCacheManager

public class FindTemplate
{
	   private String dsName;
	    private String xslType;
	    private TaxonPageTemplates tps;
	    private XslPage page;
	    private static GeneralCacheAdministrator cacheAdmin = EFGContextListener.getCacheAdmin();



    public FindTemplate(String dsName, String xslType)
    {
        this.dsName = dsName;
        this.xslType = xslType;
        this.tps = getTaxonPageTemplate();
    }
    /**
     * 
     * @return
     */
    public XslPage getXSLFileName()
    {
        if(this.page == null && this.tps != null){
            this.page = this.getXSL();
        }
        if(this.page == null){
            createXSLDefaultPage();
        }
        return this.page;
    }

    public XslPageType getXSLPageType()
    {
    	
		try {

			int counter = tps.getTaxonPageTemplateCount();
			
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
						return  xslFileNames.getXslTaxonPages();

					} 
					if (EFGImportConstants.SEARCHPAGE_PLATES_XSL
							.equalsIgnoreCase(this.xslType)) {
						//log.debug("It is a palte");
						return xslFileNames.getXslPlatePages();
					} 
					if (EFGImportConstants.SEARCHPAGE_LISTS_XSL
							.equalsIgnoreCase(this.xslType)) {
						//log.debug("It is a list");
						return xslFileNames.getXslListPages();
						
					}
					if (EFGImportConstants.SEARCHPAGE_XSL
							.equalsIgnoreCase(this.xslType)) {
						//log.debug("It is a list");
						return xslFileNames.getXslSearchPages();
						
					}
					if (EFGImportConstants.SEARCHPAGE_PDF_XSL 
							.equalsIgnoreCase(this.xslType)) {
						//log.debug("It is a list");
						return xslFileNames.getXslPdfPages();
						
					}
					 if("XSL_FILENAME_SEARCHPAGE_PDFBOOK".equalsIgnoreCase(xslType)){
				            return xslFileNames.getXslBookPages();
					 }
					 break;
				}
			}
		} catch (Exception ee) {
			
		}
		
		return null;

    }
	private XslPage getXSL() {
		try {
			XslPageType xslPageType = getXSLPageType();
			XslPage currentPage = null;
			
			if(xslPageType != null){			
				for (int j = 0; j < xslPageType.getXslPageCount(); ++j) {// find
					currentPage = xslPageType.getXslPage(j);					
					boolean isDefault = currentPage.getIsDefault();
					if (isDefault) {// if
						this.page = currentPage;
						break;
					}
				}
			}
			if(this.page == null){
				this.page = currentPage;
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
		
		try {
			  
			tps = (TaxonPageTemplates)cacheAdmin.getFromCache(this.dsName.toLowerCase());
			//log.debug("Object obtained from cache ");
		} catch (NeedsRefreshException nre) {
			
            try
            {
                String tName = EFGImportConstants.EFG_RDB_TABLES;
                Map map = ServletCacheManager.getDatasourceCache(tName.toLowerCase());
                if(map != null && !map.containsKey(dsName.toLowerCase())){
                    tName = EFGImportConstants.EFG_GLOSSARY_TABLES;
                }
                tps = TemplateMapObjectHandler.getTemplateFromDB(null, null, dsName, tName);
                cacheAdmin.putInCache(dsName.toLowerCase(), tps, EFGContextListener.templateFilesGroup);
            }
            catch(Exception ex)
            {
                tps = (TaxonPageTemplates)nre.getCacheContent();
                cacheAdmin.cancelUpdate(dsName.toLowerCase());
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
			else if(EFGImportConstants.SEARCHPAGE_XSL
					.equalsIgnoreCase(this.xslType)){
				xslName = EFGImportConstants.DEFAULT_SEARCH_PAGE_FILE;
			}
			this.page.setFileName(xslName);
			this.page.setIsDefault(true);
		}

}
