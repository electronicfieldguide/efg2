/**
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Matthew Passell<mpassell@cs.umb.edu>
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

package project.efg.Import;

import project.efg.util.*;
import java.sql.*;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;

/**
 * This class fills out a FileMaker metadata file based on an 
 * existing FileMaker database.
 */
public class MetaFileFiller implements EFGdbDriver, EFGImportConstants
{
    
    private String _dataTable;
    private String _metadataTable;
 /**
     * The standard columnnames for representing field attributes 
     * within an ODBC source.
     */
    public MetaFileFiller(String dt, String mt) 
    {
	_dataTable = dt;
	_metadataTable = mt;
    }

    public void fill() 
    {
	SQLDataExtractor de = new SQLDataExtractor(EFGdbDriver.FM_SOURCE_URI, EFGdbDriver.BRIDGE_DRIVER_NAME);
	Map javaClassMap = new HashMap();
	Map sqlNameMap = new TreeMap();
   
	Iterator keyIter;
   
	try {
	    //Opens and closes database on its own
	    de.getSQLColumnNames(_dataTable, javaClassMap, sqlNameMap);
	}
	catch (SQLException e) {
	    System.err.println("Problems creating metadata FileMaker file for: " + _dataTable + ". Check log file for a detailed message");
	    de.printSQLErrors(e);
	    return;
	}

	boolean success = true;
	keyIter = sqlNameMap.keySet().iterator();

	try {
	    //Open the JDBC source
	    de.openDB();
 	    while (keyIter.hasNext()) {
		Object key = keyIter.next();
		
		String fieldName = (String)sqlNameMap.get(key);
		String legalName = EFGUtils.encodeToJavaName(fieldName);
		String javaClassType = (String)javaClassMap.get(key);
		
		String queryString = "INSERT INTO "+_metadataTable+" "+
		    " ("+EFGImportConstants.WEIGHT+", "+ 
		    EFGImportConstants.NAME+", "+EFGImportConstants.LEGALNAME+", "+ 
		    EFGImportConstants.SEARCHABLE+", "+EFGImportConstants.SPECIESPAGEDATA+", "+ 
		    EFGImportConstants.HOLDSMULTIPLEVALUES+", "+ 
		    EFGImportConstants.PARSERABLE+", "+EFGImportConstants.ORDERED+", "+ 
		    EFGImportConstants.NUMERIC+", "+EFGImportConstants.NUMERICRANGE+", "+ 
		    EFGImportConstants.DATATYPE+", "+ 
		    EFGImportConstants.JAVATYPE+") "+ 
		    "VALUES (0, '"+
		    fieldName+"', '"+legalName+ 
		    "', '"+EFGImportConstants.YES+"', '" + EFGImportConstants.YES+ 
		    "', '"+EFGImportConstants.NO +  
		    "', '"+EFGImportConstants.NO+"', '" + EFGImportConstants.NO + 
		    "', '"+EFGImportConstants.NO+"', '" + EFGImportConstants.NO+ 
		    "', '" + EFGImportConstants.SIMPLETYPE+"', '"+ 
		    javaClassType+"');"; 
		try {
		    de.makeSQLQuery(queryString);
		}
		catch (SQLException sqle) {
		    if (!sqle.getMessage().equals("No ResultSet was produced")) {
			System.err.println(sqle.getMessage());
			de.printSQLErrors(sqle);
			success = false;
			break;
		    }
		}
	    }
	    if(!success){
		System.err.println("Problems creating metadata FileMaker file for: " + _dataTable + ". Check log file for a detailed message");
	    }
	    else{
		System.out.println("Metadata Filemaker file for: " + _dataTable + " was succesfully created");
	    }
	    de.closeDB();
	}
	catch (SQLException sqle) {
	    System.err.println("Problems creating metadata table for: " + _dataTable + ". Check log file for a detailed message");
	    de.printSQLErrors(sqle);
	}
    }
}

//$Log$
//Revision 1.1  2006/01/25 21:03:48  kasiedu
//Initial revision
//
