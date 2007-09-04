/**
 * $Id: MapQuery.java,v 1.1.1.1 2007/08/01 19:11:23 kasiedu Exp $
 * $Name:  $
 *
 * Copyright (c) 2006  University of Massachusetts Boston
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
package project.efg.server.rdb;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import project.efg.server.factory.EFGSpringFactory;
import project.efg.server.interfaces.ServletAbstractFactoryInterface;
import project.efg.server.utils.LoggerUtilsServlet;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;


public class MapQuery extends SQLQuery
{
    private ServletAbstractFactoryInterface servFactory;
	/**
	 * Constructor.
	 */

    public MapQuery(HttpServletRequest req)
    {
        super(req);
    }

	/**
	 * Set the datasourceName and the metadataSourceName if they were not passed
	 * in the query request.
	 * 
	 * @throws Exception
	 * 
	 */
    protected boolean initQueryParameters()
    {
    	
		try {
			boolean bool = super.initQueryParameters();
			if (!bool) {
				if (this.datasourceName == null) {

					if (this.servFactory == null) {
						this.servFactory = EFGSpringFactory
								.getServletAbstractFactoryInstance();
						this.servFactory.setMainDataTableName(this.getMainTableName());
					}
					List dataSources = toLists(this.servFactory
							.getListOfDatasources());
					if (dataSources == null) {
						return false;
					}
					//do a better job here query all datasources until you find one that can han
					this.datasourceName = (String) dataSources.get(0);
				}
			}
			return true;
		} catch (Exception ee) {
			// log.error(ee.getMessage());
			LoggerUtilsServlet.logErrors(ee);
		}
		return false;
    }
    /**
     * Map - map of fieldName as key and legal name as 
     * value
     * 
     * @param datasourceName
     * @return
     */
    private Map makeLegalNameMap(){
    	Map map = new HashMap();
    	List list = this.servFactory.getAllFields(this.displayName, this.datasourceName);
    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			EFGQueueObjectInterface queue = (EFGQueueObjectInterface)iterator.next();
			String legalName = queue.getObject(0);
			String key = queue.getObject(1);
			map.put(key.toLowerCase(), legalName);
		}
    	return  map;
    }
	/**
	 * This method builds a query from the request such as
	 * /efg/servlet/search?Genus=Solanum&Species=chrysotrichum
	 * /efg/servlet/search?searchStr=( Genus=Solanum && Species=chrysotrichum )
	 * 
	 * @param req
	 *            the servlet request object
	 * @return the query string
	 */
    public String buildQuery(HttpServletRequest req)
    {
    	this.datasourceName = req
		.getParameter(EFGImportConstants.DATASOURCE_NAME);
    	this.displayName = req.getParameter(EFGImportConstants.DISPLAY_NAME);

        if(!super.initQueryParameters()){
            return null;
        }
        String maxDispStr = req.getParameter(EFGImportConstants.MAX_DISPLAY);
        int maxDisplay = getMaxDisplay(maxDispStr);
        StringBuffer querySB = new StringBuffer();
  
    		try {
    			querySB.append(this.getCommonQuery());
    			
    			Enumeration paramEnum = req.getParameterNames();
    			int paramNo = 0;
    			Map map = makeLegalNameMap();
    			//make a sql query here
    			while (paramEnum.hasMoreElements()) {
    				String fieldName = (String) paramEnum.nextElement();
    				// log.debug("paramName: " + legalName);
    				if (isIgnoreParam(fieldName.toLowerCase())) {// ignore this parameter name
    					continue;
    				}
    				
    				
    				// log.debug("paramaValues length: " + paramValues.length);
    				
    				String legalName = (String)map.get(fieldName.toLowerCase());
    				if(legalName == null){
    					continue;
    				}
    				String[] paramValues = req.getParameterValues(fieldName);
    				//String legalName = EFGUtils.encodeToJavaName(fieldName);
    				 
    			//find a datasource with that field if is absent
    				//if this is a new nae find out if an old name has been found
    				//find out if th
    				String orBuffer = this.getORQuery(paramValues, legalName.toLowerCase());

    				if (!"".equals(orBuffer.trim())) {
    					if (paramNo == 0) {
    						querySB.append(" where ");
    						querySB.append("( ");
    						querySB.append(orBuffer);
    						querySB.append(" ) ");
    					} else {
    						querySB.append(" and ( ");
    						querySB.append(orBuffer);
    						querySB.append(" ) ");
    					}
    					++paramNo;
    				}

    			}
    			if (maxDisplay > 0) {
    				String database = EFGImportConstants.EFGProperties
    						.getProperty("database");
    				if (EFGImportConstants.MYSQL.equalsIgnoreCase(database)) {
    					querySB.append(" limit ");
    					querySB.append(maxDisplay + "");
    				}
    			}
    		}
        catch(Exception e)
        {
            LoggerUtilsServlet.logErrors(e);
        }
        return querySB.toString();
    }


}
