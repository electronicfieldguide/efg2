/**
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Hua Tang, Jacob K Asiedu, Kimmy Lin
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
import java.util.*;
import javax.xml.parsers.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * Import information from FileMaker XML and export into MySQL database.
 */
public class EFGImportTool
{
    // The file extension for the data and metadata files
    private static final String DATASRC_EXT = ".xml";

    // Specify the type of data source
    private static final int DATA_DS        = 0; //Importing Data Table 
    private static final int METADATA_DS    = 1; //Importing MetaData
    private static final int BOTH_DS        = 2; //Importing both Data and Meta_data tables

    // The header name in the metadata table
    private static final String METADATA_H_NAME  = "name";
    private static final String METADATA_H_LNAME = "legalName";

    private SAXParser _parser;
    private Hashtable mappingTable;
   
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(project.efg.Import.EFGImportTool.class); 
	}
	catch(Exception ee){
	}
    }
    /**
     * Constructor.
     */
    public EFGImportTool()
    {
	this.mappingTable = new Hashtable();
	//initialize database
	EFGRDBImportUtils.init();
	
	// Create a new parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
	factory.setNamespaceAware(true);
	try {
	    _parser = factory.newSAXParser();
	} catch (ParserConfigurationException pce) {
	    LoggerUtils.logErrors(pce);
	    System.err.println(pce.getMessage());
	    // pce.printStackTrace();
	} catch (SAXException se) {
	    LoggerUtils.logErrors(se);
	    System.err.println(se.getMessage());
	    //se.printStackTrace();
	}
    }

    /**
     * Import both data and metadata tables. Importing single table not supported.
     *
     * @param args the command line arguments, should contain the number and names for tables
     */
    public void process(String[] args)
    {
	int datasource = -1;	
	try {
	    datasource = Integer.parseInt(args[0]);
	} catch(NumberFormatException nfe) {
	    LoggerUtils.logErrors(nfe);
	    // nfe.printStackTrace();
	}

	String efgDBTableNames = System.getProperty("ALL_EFG_RDB_TABLES");

	switch(datasource) {
	    // create warning when user try to import a single table	
	case DATA_DS:
	    //find metadata table
	    //Get table headers of datatable, convert to acceptable table columns, create a metadata table
	    //with default values
	    //import it and do what you already do with data tables
	    log.error("Please import the metadata table together with the data table.");
	    System.err.println("Please import the metadata table together with the data table.");
	    break;
	case METADATA_DS:
	    log.error("Please import the data table together with the metadata table.");
	    System.err.println("Please import the data table together with the metadata table.");
	    break;
	case BOTH_DS:
	    System.out.println(""); 
	    System.out.println("About to Import : " + args[1] + " and " + args[2] + " into Relational Database.");
	    if (!EFGRDBImportUtils.isExistTable(efgDBTableNames)) {
		EFGRDBImportUtils.createEFGMappingTable(efgDBTableNames);
	    }
	    if (setPair(getTableName(args[1]), getTableName(args[2])) == false) {
		System.out.println("Unsuccessful in importing : " + args[1] + " and " + args[2] + " into Relational Database.");
		//message
		return;
	    }
	    
	    boolean m_bool = importData(args[2], METADATA_DS);
	    if(m_bool){
		m_bool = importData(args[1], DATA_DS);
		if(!m_bool){
		    dropMetadataTable(args[1],args[2]);
		}
	    }
	    System.out.println("");
	    break;
	default:
	    System.err.println("Unrecognized arguments to EFGImportTool");
	    break;
	    
	}
	
    }
    private void dropMetadataTable(String fn, String mfn){
	String d_tableName = getTableName(fn);
	String m_tableName = getTableName(mfn);
	EFGRDBImportUtils.isCheckPair(fn, mfn); 
    }
    /**
     * Check whether datafn and metadatafn have been created and well populated in
     * the ALL_EFG_RDB_TABLES.
     *
     * @param datafn the name for the data table
     * @param metadatafn the name for the metadata table
     */
    private boolean setPair(String datafn, String metadatafn)
    {
	return EFGRDBImportUtils.setPair(datafn, metadatafn); 
    }
    /**
     * Translates an array of names into its corresponding legal names that can be used to 
     * create a database table column.
     *
     * @param h the String[] contains the original column names
     * @param tableName the name of the datasource (DS_DATA) used to obtain the Hashtable that contains the mappings
     * @return header the String[] containing the translated names (legal names)
     */
    private String[] translateHeader(String[] h, String tableName)
    {
	if(this.mappingTable.size() == 0){
	    return h;
	}
        String[] header = new String[h.length];
	for(int i = 0; i < h.length; i++) {
	    header[i] = (String)this.mappingTable.get(h[i].trim());
        }
        return header;
    }

    /**
     * Import the xml file to MySQL table.
     *
     * @param fn the table name for the xml file
     * @param datasource the constant signaling for data or metadata
     *    0 means search DS_DATA column of Mapping Table
     *    1 means search DS_METADATA column of Mapping Table
     */
    private boolean importData(String fn, int datasource) 
    {
	String[] header;
	List records;
	String[] row;
	String tableName = null;
	try {
	    EFGParserHandler handler = new EFGParserHandler();
	    _parser.parse(fn, handler);

	    // Get all the table information stored in the file
	    header = handler.getHeader();
	    records = handler.getRecords();
	    log.debug("Records size: " + records.size());
	    // create database table. The database table name is the same as the name of the
            // file but with the .xml removed from it
            tableName = getTableName(fn);
	   
	    if (datasource == METADATA_DS) {
		boolean mbool = createMapping(header, records, tableName);
		if(!mbool){
		    System.out.println("The Database table: " + tableName + " could not be created.");
		    return false;
		}
	    }
	    if (datasource == DATA_DS) {
		//use the names in the metadata file
		header = this.translateHeader(header, tableName);
	    }
	  
	    boolean boolT = EFGRDBImportUtils.createAndPopulateTable(tableName, header, records);
	    //create Helper tables
	    if(boolT){
		System.out.println("The Database table: " + tableName + " was successfully created.");
		return true;
	    }
	    System.out.println("The Database table: " + tableName + " could not be created successfully.");
	    return false;
	    
	    
	} catch (Exception e) {
	    System.out.println("The Database table: " + tableName + " could not be created.");
	    LoggerUtils.logErrors(e);
	    System.err.println(e.getMessage());
	}
	return false;
    }

    /**
     * Create a Hashtable mapping taxon's name to legal name and save it into
     * the ALL_EFG_RDB_TABLES table where ds_data field == tableName.
     *
     * @param header the header of the data table
     * @param records the values of the data table
     * @param tableName the name fo the data table
     */
    private boolean createMapping(String[] header, List records, String tableName)
    {
	try{
	    int nameIndex = -1, legalnameIndex = -1;
	    
	    for(int i = 0; i < header.length; i++) {
		if(header[i].equals(METADATA_H_NAME)){
		    nameIndex = i;
		}
		if(header[i].equals(METADATA_H_LNAME)){
		    legalnameIndex = i;
		}		   
	    }
	    
	    String[] row;
	    int nRows = records.size();
	    //Hashtable mapping = new Hashtable(2 * nRows); // The load factor will be 0.5
	    for(int j = 0; j < nRows; j++) {
		row = (String[]) records.get(j);
		log.debug("Adding: " + row[nameIndex] + " as name");
		log.debug("Adding: " + row[legalnameIndex] + " as legalname");
		this.mappingTable.put(row[nameIndex].trim(), row[legalnameIndex].trim());
	    }
	    return true;
	}
	 catch (Exception e) {
	     LoggerUtils.logErrors(e);
	    System.err.println(e.getMessage());
	 }
	return false;
    }

    /**
     * Extract the table name from the file name by removing the file extension.
     * 
     * @param filename the name of the import file
     * @return the name of the table extracted from the filename
     */
    private String getTableName(String filename)
    {
	return filename.substring(0, filename.indexOf(DATASRC_EXT));
    }

    /**
     * The driver for importing EFG data into MySQL database.
     * Run with EFGImportTool 2 Data Metadata
     */
    public static void main(String[] args)
    {		
	//initialize logger
	try{
	    LoggerUtils loggerU = new LoggerUtils();
	    EFGImportTool f = new EFGImportTool();
	    f.process(args);
	    EFGRDBImportUtils.closeConnection();
	}
	catch(Exception e){
	    LoggerUtils.logErrors(e);
	    System.err.println(e.getMessage());
	    //e.printStackTrace();
	}
    }
}
//$Log$
//Revision 1.3  2006/02/25 13:14:31  kasiedu
//New classes for import GUI
//
//Revision 1.2  2006/01/26 04:20:46  kasiedu
//no message
//
//Revision 1.1.1.1  2006/01/25 21:03:42  kasiedu
//Release for Costa rica
//
//Revision 1.1.1.1  2003/10/17 17:03:05  kimmylin
//no message
//
//Revision 1.13  2003/08/26 14:44:40  kasiedu
//*** empty log message ***
//
//Revision 1.12  2003/08/20 18:45:41  kimmylin
//no message
//
//Revision 1.11  2003/08/06 20:06:45  kimmylin
//no message
//
//Revision 1.10  2003/08/05 21:03:59  kimmylin
//no message
//
//Revision 1.8  2003/08/05 14:23:00  kasiedu
//*** empty log message ***
//
//Revision 1.7  2003/08/05 01:47:29  kasiedu
//*** empty log message ***
//
//Revision 1.6  2003/08/02 20:14:17  kasiedu
//*** empty log message ***
//
//Revision 1.4  2003/08/01 21:53:30  kasiedu
//*** empty log message ***
//
//Revision 1.3  2003/08/01 18:39:41  kimmylin
//no message
//
//Revision 1.2  2003/07/31 21:08:22  kimmylin
//no message
//
//Revision 1.1.1.1  2003/07/30 17:03:58  kimmylin
//no message
//
//Revision 1.1.1.1  2003/07/18 21:50:15  kimmylin
//RDB added 
//

