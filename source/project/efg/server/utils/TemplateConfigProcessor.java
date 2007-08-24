// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TemplateConfigProcessor.java

package project.efg.server.utils;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import java.io.StringWriter;
import java.util.*;
import project.efg.server.servlets.EFGContextListener;
import project.efg.templates.taxonPageTemplates.*;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.TemplateMapObjectHandler;

// Referenced classes of package project.efg.server.utils:
//            GuidObject, ServletCacheManager, LoggerUtilsServlet

public class TemplateConfigProcessor
{

    public TemplateConfigProcessor(String dsName, String searchType)
    {
        this.dsName = dsName;
        this.searchType = searchType;
    }

    private boolean saveToDB()
    {
        if(tps == null){
            return false;
        }
        try{
        String tName = EFGImportConstants.EFG_RDB_TABLES;
        Map map = ServletCacheManager.getDatasourceCache(tName.toLowerCase());
        if(map != null && !map.containsKey(tName.toLowerCase()))
            tName = EFGImportConstants.EFG_GLOSSARY_TABLES;
        TemplateMapObjectHandler.updateDatabase(null, tps, dsName, tName);
        try
        {
            cacheAdmin.removeEntry(dsName.toLowerCase());
        }
        catch(Exception ee) { }
        cacheAdmin.putInCache(dsName.toLowerCase(), tps, EFGContextListener.templateFilesGroup);
        EFGContextListener.lastModifiedTemplateFileTable.put(dsName.toLowerCase(), new Long(System.currentTimeMillis()));
        return true;
        }
		catch(Exception ee){
			LoggerUtilsServlet.logErrors(ee);
		}
		return false;
    }

    private XslPageType getCurrentXSLPageType(String dsName, String searchType)
    {
        XslPageType xslPageType = null;
        try
        {
            int counter = tps.getTaxonPageTemplateCount();
            int i = 0;
            if(i < counter)
            {
                TaxonPageTemplateType tp = tps.getTaxonPageTemplate(i);
                String ds = tp.getDatasourceName();
                if(ds.equalsIgnoreCase(dsName.trim()))
                {
                    XslFileNamesType xslFileNames = tp.getXSLFileNames();
                    if("taxon".equalsIgnoreCase(searchType))
                        xslPageType = xslFileNames.getXslTaxonPages();
                    else
                    if("plates".equalsIgnoreCase(searchType))
                        xslPageType = xslFileNames.getXslPlatePages();
                    else
                    if("lists".equalsIgnoreCase(searchType))
                        xslPageType = xslFileNames.getXslListPages();
                    else
                    if("pdfs".equalsIgnoreCase(searchType))
                        xslPageType = xslFileNames.getXslPdfPages();
                    else
                    if("pdfbook".equalsIgnoreCase(searchType))
                        xslPageType = xslFileNames.getXslBookPages();
                    else
                    if("searches".equalsIgnoreCase(searchType))
                        xslPageType = xslFileNames.getXslSearchPages();
                    else
                        xslPageType = xslFileNames.getXslListPages();
                }
            }
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
        }
        return xslPageType;
    }

    private void setTaxonPageTemplateRoot()
    {
        if(dsName == null)
            return;
        try
        {
            tps = (TaxonPageTemplates)cacheAdmin.getFromCache(dsName.toLowerCase());
        }
        catch(NeedsRefreshException nre)
        {
            reLoad();
        }
        return;
    }

    private void reLoad()
    {
        try
        {
            String tName = EFGImportConstants.EFG_RDB_TABLES;
            Map map = ServletCacheManager.getDatasourceCache(tName.toLowerCase());
            if(map != null && !map.containsKey(tName.toLowerCase()))
                tName = EFGImportConstants.EFG_GLOSSARY_TABLES;
            tps = TemplateMapObjectHandler.getTemplateFromDB(null, null, dsName, tName);
            if(tps != null)
            {
                try
                {
                    cacheAdmin.removeEntry(dsName.toLowerCase());
                }
                catch(Exception ee) { }
                cacheAdmin.putInCache(dsName.toLowerCase(), tps, EFGContextListener.templateFilesGroup);
                EFGContextListener.lastModifiedTemplateFileTable.put(dsName.toLowerCase(), new Long(System.currentTimeMillis()));
            }
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
        }
    }

    public String getJspPage(String guid)
    {
        if(tps == null)
            setTaxonPageTemplateRoot();
        if(tps != null)
        {
            XslPageType xslPageType = getCurrentXSLPageType(dsName, searchType);
            if(xslPageType != null)
            {
                XslPage currentPage = null;
                int j = 0;
                do
                {
                    if(j >= xslPageType.getXslPageCount())
                        break;
                    currentPage = xslPageType.getXslPage(j);
                    if(currentPage.getGuid().equalsIgnoreCase(guid))
                        break;
                    currentPage = null;
                    j++;
                } while(true);
                if(currentPage != null)
                    return currentPage.getJspName();
            }
        }
        return null;
    }

    public boolean removeAConfig(String guid)
    {
        if(tps == null)
            setTaxonPageTemplateRoot();
        if(tps != null)
        {
            XslPageType xslPageType = getCurrentXSLPageType(dsName, searchType);
            if(xslPageType != null)
            {
                XslPage currentPage = null;
                int j = 0;
                do
                {
                    if(j >= xslPageType.getXslPageCount())
                        break;
                    currentPage = xslPageType.getXslPage(j);
                    if(currentPage.getGuid().equalsIgnoreCase(guid))
                        break;
                    currentPage = null;
                    j++;
                } while(true);
                if(currentPage != null)
                {
                    xslPageType.removeXslPage(currentPage);
                    if("pdfs".equalsIgnoreCase(searchType))
                    {
                        if(xslPageType.getXslPageCount() == 0)
                            tps.getTaxonPageTemplate(0).getXSLFileNames().setXslPdfPages(null);
                    } else
                    if("pdfbook".equalsIgnoreCase(searchType) && xslPageType.getXslPageCount() == 0)
                        tps.getTaxonPageTemplate(0).getXSLFileNames().setXslBookPages(null);
                    return saveToDB();
                }
            }
        }
        LoggerUtilsServlet.logErrors(new Exception("Could not remove template with GUID: " + guid));
        return false;
    }

    public List getTemplateList()
    {
        List list = new ArrayList();
        if(dsName == null || "".equals(dsName.trim()))
            return null;
        try
        {
            Long lastMod = (Long)EFGContextListener.lastModifiedTemplateFileTable.get(dsName.toLowerCase());
            if(lastMod == null)
                reLoad();
            if(tps == null)
                setTaxonPageTemplateRoot();
            if(tps != null)
            {
                StringWriter writer = new StringWriter();
                tps.marshal(writer);
                XslPageType xslPageType = getCurrentXSLPageType(dsName, searchType);
                if(xslPageType != null)
                {
                    for(int j = 0; j < xslPageType.getXslPageCount(); j++)
                    {
                        XslPage currentPage = xslPageType.getXslPage(j);
                        String guid = currentPage.getGuid();
                        String displayName = currentPage.getDisplayName();
                        String jspName = currentPage.getJspName();
                        if(jspName != null && !jspName.trim().equals(""))
                        {
                            GuidObject guidObject = new GuidObject(guid, displayName, jspName);
                            list.add(guidObject);
                        }
                    }

                    if(list.size() > 0)
                        Collections.sort(list);
                }
            }
        }
        catch(Exception ee)
        {
            LoggerUtilsServlet.logErrors(ee);
        }
        return list;
    }

    private static GeneralCacheAdministrator cacheAdmin = EFGContextListener.getCacheAdmin();
    private TaxonPageTemplates tps;
    private String dsName;
    private String searchType;

}
