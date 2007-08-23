/**
 * $Id$
 * $Name$
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
package project.efg.client.utils.nogui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;

import project.efg.util.interfaces.EFGImportConstants;

/**
 * @author jacob.asiedu
 *
 */
public class FlushServerCache {
	private static String serverURL;
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(FlushServerCache.class);
		} catch (Exception ee) {
		}
		try {
			serverURL = 
				EFGImportConstants.EFGProperties.getProperty("cacheserverurl");
		} catch (Exception e) {
			log.error(e.getMessage());	
		}
	}
	/**
	 * 
	 * @param datasourceName
	 * @param tableName
	 * @return true if the server cache was flushed successfully
	 */
	public static boolean flushServerCache(
			String datasourceName, String tableName){
		if(serverURL == null){
			return false;
		}
		datasourceName = toTableName(datasourceName);
		StringBuffer str = new StringBuffer(serverURL);
		//TODO remove hard coded strings
		str.append("/Flush.jsp?");
		str.append(EFGImportConstants.DATASOURCE_NAME);
		str.append("=");
		str.append(datasourceName);
		if(tableName != null){
			str.append("&");
			str.append(EFGImportConstants.ALL_TABLE_NAME);
			str.append("=");
			str.append(tableName);
		}
		try{
			
	         URL url = new URL(str.toString());
	         
		         BufferedReader in = new BufferedReader(
                     new InputStreamReader(
                     url.openStream()));
			in.close();
	         return true;
	         
         }catch(Exception e){
        	 log.error(e.getMessage());
         }
 		return false;
	}
	/**
	 * @param tableName
	 * @return
	 */
	private static String toTableName(String tableName) {
		if(tableName.toLowerCase().indexOf(EFGImportConstants.METAFILESUFFIX.toLowerCase()) == -1){
			return tableName;
		}
		String mutex ="";
		synchronized (mutex) {
			int index = tableName.toLowerCase().indexOf(EFGImportConstants.METAFILESUFFIX.toLowerCase());
			return tableName.substring(0,index);
		}		
	}
}
