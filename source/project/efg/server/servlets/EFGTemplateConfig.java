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

package project.efg.server.servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.efg.server.utils.EFGFieldObject;
import project.efg.server.utils.GroupTypeComparator;
import project.efg.server.utils.GroupTypeSorter;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.server.utils.ServletCacheManager;
import project.efg.templates.taxonPageTemplates.CharacterValue;
import project.efg.templates.taxonPageTemplates.GroupType;
import project.efg.templates.taxonPageTemplates.GroupTypeItem;
import project.efg.templates.taxonPageTemplates.GroupsType;
import project.efg.templates.taxonPageTemplates.GroupsTypeItem;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslFileNamesType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.RegularExpresionConstants;
import project.efg.util.utils.EFGUniqueID;
import project.efg.util.utils.TemplateMapObjectHandler;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

// Referenced classes of package project.efg.server.servlets:
//            EFGContextListener

public abstract class EFGTemplateConfig extends HttpServlet
{


    /**
	 * This method is called when the servlet is first started up.
	 */
	/**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */
    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        realPath = getServletContext().getRealPath("/");
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
        throws ServletException, IOException
    {
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
        throws ServletException, IOException
    {
        processParams(req, res);
    }
    /**
	 * @param req
	 * @param res
	 */
    public abstract void processParams(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException;

    protected GroupsType sortGroups(GroupsType groupsType)
    {
        GroupTypeSorter cvt = new GroupTypeSorter();
        groupsType = cvt.sort(new GroupTypeComparator(), groupsType);
        return groupsType;
    }
	protected boolean saveToDB(String dsName, TaxonPageTemplates tps) {
		try{
			 String tName = EFGImportConstants.EFG_RDB_TABLES;
		        Map map = ServletCacheManager.getDatasourceCache(tName.toLowerCase());
		        if(map != null && !map.containsKey(dsName.toLowerCase()))
		            tName = EFGImportConstants.EFG_GLOSSARY_TABLES;
		        TemplateMapObjectHandler.updateDatabase(null, tps, dsName, tName);
				return true;
		} catch (Exception ff) {
				LoggerUtilsServlet.logErrors(ff);
				
		}
		return false;
	}
     protected TaxonPageTemplates getTaxonPageTemplateRoot(String dsName, String tableName)
    {
        TaxonPageTemplates ts = null;
        if(dsName == null)
            return null;
        try
        {
            ts = (TaxonPageTemplates)cacheAdmin.getFromCache(dsName.toLowerCase());
        }
        catch(NeedsRefreshException nre)
        {
            try
            {
                if(tableName == null)
                    tableName = EFGImportConstants.EFG_RDB_TABLES;
                ts = TemplateMapObjectHandler.getTemplateFromDB(null, null, dsName, tableName);
                cacheAdmin.putInCache(dsName.toLowerCase(), ts, EFGContextListener.templateFilesGroup);
            }
            catch(Exception ee) { }
        }
        return ts;
    }

    protected XslPage getXSLPageType(TaxonPageTemplates tps, String dsName, String searchPage, String searchType, String xslName, String uniqueName, String guid, 
            String jspName, String isDefault)
    {
        String mute = "";
       synchronized(mute){
       
        XslPage page;
        XslPageType xslPageType = getCurrentXSLPageType(tps, dsName, searchType);
        page = null;
        Boolean bool = new Boolean(isDefault);
        boolean defaultFile = bool.booleanValue();
        for(int j = 0; j < xslPageType.getXslPageCount(); j++)
        {
            XslPage currentPage = xslPageType.getXslPage(j);
            String currentGUIDName = currentPage.getGuid();
            String currentUniqueName = currentPage.getDisplayName();
            if(currentGUIDName.equalsIgnoreCase(guid) || currentUniqueName.equalsIgnoreCase(uniqueName))
            {
                currentPage.setGroups(null);
                currentPage.setIsDefault(defaultFile);
                page = currentPage;
                continue;
            }
            if(defaultFile)
                currentPage.setIsDefault(false);
        }

        if(page == null)
        {
            page = new XslPage();
            page.setFileName(xslName);
            page.setDisplayName(uniqueName);
            if(guid == null || guid.trim().equals(""))
                guid = EFGUniqueID.getID() + "";
            page.setGuid(guid);
            if(jspName != null && !jspName.trim().equals(""))
                page.setJspName(jspName);
            page.setIsDefault(defaultFile);
            xslPageType.addXslPage(page);
        }
        GroupsType groupsType = new GroupsType();
        page.setGroups(groupsType);
        return page;
       }
    }
	protected XslPageType getCurrentXSLPageType(TaxonPageTemplates tps, 
			String dsName,
			 String searchType) {
		String mute = "";
		synchronized (mute) {

			XslPageType xslPageType = null;
			try {

				int counter = tps.getTaxonPageTemplateCount();

				for (int i = 0; i < counter; i++) {

					TaxonPageTemplateType tp = tps.getTaxonPageTemplate(i);
					String ds = tp.getDatasourceName();
					if (ds.equalsIgnoreCase(dsName.trim())) {
						//log.debug("Found datasource: " + ds);
	                    XslFileNamesType xslFileNames = tp.getXSLFileNames();
	                    if(xslFileNames == null)
	                    {
	                        xslFileNames = new XslFileNamesType();
	                        tp.setXSLFileNames(xslFileNames);
	                    }
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
	                    {
	                        xslPageType = xslFileNames.getXslPdfPages();
	                        if(xslPageType == null)
	                        {
	                            xslPageType = new XslPageType();
	                            xslFileNames.setXslPdfPages(xslPageType);
	                        }
	                    } else
	                    if("pdfbook".equalsIgnoreCase(searchType))
	                    {
	                        xslPageType = xslFileNames.getXslBookPages();
	                        if(xslPageType == null)
	                        {
	                            xslPageType = new XslPageType();
	                            xslFileNames.setXslBookPages(xslPageType);
	                        }
	                    } else
	                    if("searches".equalsIgnoreCase(searchType))
	                    {
	                        xslPageType = xslFileNames.getXslSearchPages();
	                        if(xslPageType == null)
	                        {
	                            xslPageType = new XslPageType();
	                            xslFileNames.setXslSearchPages(xslPageType);
	                        }
	                    } else
	                    {
	                        xslPageType = xslFileNames.getXslTaxonPages();
	                    }
						break;
					}
				}
			} catch (Exception ee) {
				LoggerUtilsServlet.logErrors(ee);
				return null;
			}
			return xslPageType;
		}

	}
  

    protected GroupType checkGroup(GroupsType groups, String groupID, int groupRank)
    {
        GroupType gt = null;
        GroupsTypeItem key = null;
        boolean found = false;
        String id = null;
        Enumeration e = groups.enumerateGroupsTypeItem();
        do
        {
            if(!e.hasMoreElements())
                break;
            key = (GroupsTypeItem)e.nextElement();
            gt = key.getGroup();
            id = gt.getId();
            int rank = gt.getRank();
            if(!id.equals(groupID) || rank != groupRank)
                continue;
            found = true;
            break;
        } while(true);
        if(!found)
        {
            id = groupID;
            gt = new GroupType();
            gt.setId(groupID);
            gt.setRank(groupRank);
            key = new GroupsTypeItem();
            key.setGroup(gt);
            groups.addGroupsTypeItem(key);
        }
        return gt;
    }

    protected CharacterValue findCharacter(GroupType group, int characterRank)
    {
        GroupTypeItem key = null;
        CharacterValue cv = null;
        boolean found = false;
        Enumeration e = group.enumerateGroupTypeItem();
        do
        {
            if(!e.hasMoreElements())
                break;
            key = (GroupTypeItem)e.nextElement();
            if(key == null)
                continue;
            cv = key.getCharacterValue();
            if(cv == null || cv.getRank() != characterRank)
                continue;
            found = true;
            break;
        } while(true);
        if(found)
            return cv;
        else
            return null;
    }

    protected String createGroup(GroupsType groups, String key, String text)
    {
        String returnValue = null;
        String identifier = "";
        String groupID = "";
        String groupRank = "";
        String characterRank = "";
        String splits[] = RegularExpresionConstants.colonPattern.split(key);
        if(splits.length == 0)
            return null;
        identifier = splits[0];
        if(splits.length > 1)
            groupID = splits[1];
        if(splits.length > 2)
            groupRank = splits[2];
        if(splits.length > 3)
            characterRank = splits[3];
        if("".equals(groupID))
            return null;
        if("".equals(groupRank))
            return null;
        GroupType group = checkGroup(groups, groupID, Integer.parseInt(groupRank));
        if("gl".equalsIgnoreCase(identifier.trim()))
        {
            if(text != null || !text.trim().equals(""))
                group.setLabel(text);
        } else
        if("gtl".equalsIgnoreCase(identifier.trim()))
        {
            if(text != null || !text.trim().equals(""))
                group.setText(text);
        } else
        {
            if("".equals(characterRank) || characterRank == null)
                return null;
            if(text == null || text.trim().equals(""))
                return null;
            CharacterValue cv = findCharacter(group, Integer.parseInt(characterRank));
            if(cv == null)
            {
                GroupTypeItem vGroupTypeItem = new GroupTypeItem();
                cv = new CharacterValue();
                cv.setRank(Integer.parseInt(characterRank));
                vGroupTypeItem.setCharacterValue(cv);
                group.addGroupTypeItem(vGroupTypeItem);
            }
            if("ctl".equalsIgnoreCase(identifier.trim()))
                cv.setText(text);
            else
            if("cl".equalsIgnoreCase(identifier.trim()) || "gtl".equalsIgnoreCase(identifier.trim()))
            {
                if("cl".equalsIgnoreCase(identifier.trim()))
                    cv.setLabel(text);
                else
                    cv.setText(text);
            } else
            if("group".equalsIgnoreCase(identifier.trim()) && text != null && !text.trim().equals(""))
            {
                if(returnValue == null)
                    returnValue = text;
                cv.setValue(text);
            }
        }
        return returnValue;
    }

    protected EFGFieldObject add2Groups(HttpServletRequest req, XslPage xslPage)
    {
        EFGFieldObject field = null;
        GroupsType groups = xslPage.getGroups();
        Enumeration e = req.getParameterNames();
        do
        {
            if(!e.hasMoreElements())
                break;
            String key = ((String)e.nextElement()).trim();
            if(!key.equalsIgnoreCase(EFGImportConstants.XSL_STRING) && !key.equalsIgnoreCase(EFGImportConstants.DATASOURCE_NAME) && !key.equalsIgnoreCase("submit") && !key.equalsIgnoreCase("ALL_TABLE_NAME") && !key.equalsIgnoreCase(EFGImportConstants.DISPLAY_NAME) && !key.equalsIgnoreCase("HTML") && !key.equalsIgnoreCase(EFGImportConstants.TEMPLATE_UNIQUE_NAME) && !key.equalsIgnoreCase(EFGImportConstants.GUID))
            {
                String valuePair = req.getParameter(key);
                if(valuePair != null && !"".equals(valuePair.trim()) && key.trim().indexOf(":") != -1)
                {
                    String currentValue = createGroup(groups, key, valuePair);
                    if(currentValue != null)
                    {
                        currentValue = currentValue.trim();
                        if(field == null && !currentValue.equals("") && currentValue.indexOf(".") == -1)
                        {
                            field = new EFGFieldObject();
                            field.setFieldName(key);
                            field.setFieldValue(currentValue);
                        }
                    }
                }
            }
        } while(true);
        groups = sortGroups(groups);
        xslPage.setGroups(groups);
        return field;
    }

    protected XslPage getXSLPageParams(HttpServletRequest req, TaxonPageTemplates tps)
    {
        String dsName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
        if(dsName == null || dsName.trim().equals(""))
            dsName = (String)req.getAttribute(EFGImportConstants.DATASOURCE_NAME);
        String xslName = req.getParameter(EFGImportConstants.XSL_STRING);
        String searchPage = req.getParameter("search");
        String searchType = req.getParameter("searchType");
        String uniqueName = req.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
        String guid = req.getParameter(EFGImportConstants.GUID);
        String jspName = req.getParameter(EFGImportConstants.JSP_NAME);
        String isDefault = req.getParameter("isDefault");
        return getXSLPageType(tps, dsName, searchPage, searchType, xslName, uniqueName, guid, jspName, isDefault);
    }

    private static final long serialVersionUID = 1L;
    protected static GeneralCacheAdministrator cacheAdmin = EFGContextListener.getCacheAdmin();
    protected String realPath;

}
