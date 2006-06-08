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

package project.efg.servlets.efgInterface;

import project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface;
import project.efg.servlets.factory.ServletAbstractFactoryCreator;



/**
 * This class gives access to an iterator over the data source names in the EFG
 * and a utility function for converting those names to readable names.<BR>
 * Objects of this class can be directly accessed from a servlet, but
 * it was really intended for use in jsp:useBean tags in JSPs.
 *
 * @see #TypePage.jsp
 * @see #SearchPage.jsp
 */
public class  EFGDataSourceHelperInterface
{
	private ServletAbstractFactoryInterface servFactory;
	public EFGDataSourceHelperInterface(){
		this.servFactory = ServletAbstractFactoryCreator.getInstance();
	}
    /**
     * Return a List of the datasource names in the EFG database.
     *
     * @return a list ot data source names.
     */
    public EFGDatasourceObjectListInterface getDataSourceNames(){
    	return this.servFactory.getListOfDatasources();
    }
  
 /**
  * 
  * @param dataSourceName
  * @return a list of MediaResource objects for the current datasource
  */
    public  SearchableListInterface getMediaResourceFields(String displayName,String dataSourceName){
    	return this.servFactory.getMediaResourceLists(displayName,dataSourceName);
    }
    /**
     * @param dataSourceName the name of the data source
     * @return a SearchableListInterface mapping searchable fields to its values
     */
    public SearchableListInterface getSearchable(String displayName,String dataSourceName){
    	return this.servFactory.getSearchableLists(displayName,dataSourceName);
    }

  
}
//$Log$
//Revision 1.1.2.1  2006/06/08 13:27:42  kasiedu
//New files
//
//Revision 1.1.1.1  2006/01/25 21:03:48  kasiedu
//Release for Costa rica
//
//Revision 1.1.1.1  2003/10/17 17:03:09  kimmylin
//no message
//
//Revision 1.5  2003/08/20 18:45:42  kimmylin
//no message
//
//Revision 1.4  2003/08/05 16:01:21  kasiedu
//*** empty log message ***
//
//Revision 1.3  2003/08/05 15:49:45  kasiedu
//*** empty log message ***
//
//Revision 1.2  2003/08/01 20:47:07  kasiedu
//*** empty log message ***
//
//Revision 1.1.1.1  2003/07/30 17:04:03  kimmylin
//no message
//
//Revision 1.1.1.1  2003/07/18 21:50:16  kimmylin
//RDB added 
//