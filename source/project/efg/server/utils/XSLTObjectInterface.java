/**
 * $Id: XSLTObjectInterface.java,v 1.1.1.1 2007/08/01 19:11:26 kasiedu Exp $
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

package project.efg.server.utils;

import java.util.Map;

import project.efg.server.factory.EFGSpringFactory;
import project.efg.server.interfaces.EFGDataObjectListInterface;
import project.efg.server.interfaces.ServletAbstractFactoryInterface;
import project.efg.templates.taxonPageTemplates.XslPage;

public abstract class XSLTObjectInterface
{
	private ServletAbstractFactoryInterface servFactory;
    public XSLTObjectInterface()
    {
        servFactory = EFGSpringFactory.getServletAbstractFactoryInstance();
    }

    public void setMainDataTableName(String mainTableName)
    {
        servFactory.setMainDataTableName(mainTableName);
    }

    public String getMainTableName()
    {
        return servFactory.getMainTableName();
    }
    /**
	 * @param datasourceName
	 * @param xslType = must be one of plates, lists or taxonPage
	 * @return
	 */
    public XslPage getXSLFile(String datasourceName, String xslType)
    {
        FindTemplate template = new FindTemplate(datasourceName, xslType);
        return template.getXSLFileName();
    }

    public EFGDataObjectListInterface getSearchableLists(String displayName, String datasourceName)
    {
        return servFactory.getSearchableLists(displayName, datasourceName);
    }

    public EFGDataObjectListInterface getMediaResourceLists(String displayName, String datasourceName)
    {
        return servFactory.getMediaResourceLists(displayName, datasourceName);
    }

    public abstract XSLProperties getXSLProperties(Map map);

	protected EFGMediaResourceSearchableObject getFirstField(String displayName, 
			String datasourceName) {
		EFGMediaResourceSearchableObject medSearchField = null;
		try{
			medSearchField = servFactory.getFirstField(displayName, datasourceName);
		}
		catch(Exception ee){
			LoggerUtilsServlet.logErrors(ee);
		}
		return medSearchField;
	}
    
}
