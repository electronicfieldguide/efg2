/**
 * $Id$
 *
 * Copyright (c) 2007  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
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

package project.efg.server.factory;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import project.efg.server.exports.ZipExport;
import project.efg.server.impl.*;
import project.efg.server.interfaces.*;
import project.efg.server.rdb.SearchableImpl;
import project.efg.server.utils.*;
import project.efg.util.interfaces.EFGDataObject;
import project.efg.util.interfaces.EFGSessionBeanInterface;

public class EFGSpringFactory
{

    private EFGSpringFactory()
    {
    }

    public static EFGDataObject getEFGDataObject()
    {
    	try{
        return (EFGDataObject)appcontext.getBean("efgdataobject");
    	}catch(Exception ee){
            log.error(ee.getMessage());
   		
    	}
        return new EFGDataObjectImpl();
    }

    public static SearchableInterface getSearchables()
    {
    	try{
        return (SearchableInterface)appcontext.getBean("searchables");
    }catch(Exception ee){
        log.error(ee.getMessage());
		
	}
        return new SearchableImpl();
    }

    public static EFGDataSourceHelperInterface getDatasourceHelper()
    {
    	try{
        return (EFGDataSourceHelperInterface)appcontext.getBean("datasourcehelper");
    	  }catch(Exception ee){
    	        log.error(ee.getMessage());
    			
    		}
        return new EFGDataSourceHelperInterface();
    }

    public static ZipExport getZipExportInstance()
    {
    	try{
    
        return (ZipExport)appcontext.getBean("zipexport");
    }catch(Exception ee){
        log.error(ee.getMessage());
		
	}
        return new ZipExport();
    }

    public static EFGDocumentTypesFactory getEFGDocumentTypesFactoryInstance()
    {
    	try{
        return (EFGDocumentTypesFactory)appcontext.getBean("efgdocumenttypesfactory");
    }catch(Exception ee){
        log.error(ee.getMessage());
		
	}
        return new EFGDocumentTypesFactory();
    }

    public static EFGDisplayObjectList getDisplayObjectList()
    {
    	try{
        return (EFGDisplayObjectList)appcontext.getBean("displayobjectlist");
    	 }catch(Exception ee){
    	        log.error(ee.getMessage());
    			
    		}
        return new EFGDisplayObjectList();
    }

    public static EFGSessionBeanInterface getSessionBean()
    {
        return (EFGSessionBeanInterface)appcontext.getBean("efgsessionbean");
     }

    public static EFGParseObjectFactory getParseObjectFactory()
    {
        return new EFGParseObjectFactoryImpl();
    }

    public static ServletAbstractFactoryInterface getServletAbstractFactoryInstance()
    {
    	try{
        return (ServletAbstractFactoryInterface)appcontext.getBean("servletabstractfactory");
    	}
    	catch(Exception ee){
        log.error(ee.getMessage());
    	}
        return new ServletAbstractFactoryImpl();
    }

    public static synchronized XSLTObjectInterface getXSLTObjectInstance(int taxonSize, String searchType)
    {
        if(taxonSize == 1)
            return createTaxonPage();
        if("plates".equalsIgnoreCase(searchType))
            return createSearchPlatesPage();
        if("searches".equalsIgnoreCase(searchType))
            return createSearchPage();
        else
            return createSearchListsPage();
    }

    public static ApplyXSLInterface createApplyXSLInterface(String springID)
    {
        return (ApplyXSLInterface)appcontext.getBean(springID);
    }

    private static XSLTObjectInterface createTaxonPage()
    {
        try{
        	return (XSLTObjectInterface)appcontext.getBean("createtaxonpage");
        }
         catch(Exception ee){
    		log.error(ee.getMessage());
    	}
        return new TaxonPageHtml();
    }

    private static XSLTObjectInterface createSearchPlatesPage()
    {
    	try{
   
        return (XSLTObjectInterface)appcontext.getBean("createsearchplatespage");
    	}catch(Exception ee){
    		log.error(ee.getMessage());
    	}
        return new SearchPageHtmlPlates();
    }

    private static XSLTObjectInterface createSearchPage()
    {
    	try{
        return (XSLTObjectInterface)appcontext.getBean("createsearchpage");
    	}catch(Exception ee){
    		log.error(ee.getMessage());
    	}
        return new SearchPageHtml();
    }

    private static XSLTObjectInterface createSearchListsPage()
    {
    	try{
        return (XSLTObjectInterface)appcontext.getBean("createsearchlistspage");
    	}
    	catch(Exception ee){
    		log.error(ee.getMessage());
    	}
        return new SearchPageHtmlLists();
    }



    static Logger log;
    static ApplicationContext appcontext;

    static 
    {
        try
        {
            log = Logger.getLogger(project.efg.server.factory.EFGSpringFactory.class);
            appcontext = new ClassPathXmlApplicationContext("springconfig.xml", project.efg.server.factory.EFGSpringFactory.class);
        }
        catch(Exception ee) { }
    }
}
