/**
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Matthew Passell<mpassell@cs.umb.edu>
 *
 *          Change from GUI to console, and process all the data files
 *          in the current directory or from list file  -htang 07/22/2002
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
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * This class assists in the conversion of ODBC-accessible data to a pair of 
 * XML files for import into the EFG.  -htang 2002/07/22
 */
public class EFGDataPrepareTool
{
    private String _dataTable, _metadataTable;

    public EFGDataPrepareTool(String dt, String mt)
    {
	_dataTable = dt;
	_metadataTable = mt;
    }

    /**
     * fill in the metadata in the metadata table.
     */
    public void process()
    {
	// Fill in the metadata files (fp5)
	MetaFileFiller mff = new MetaFileFiller(_dataTable, _metadataTable);
	mff.fill();
    }

    /**
     * Main method
     */
    public static void main(String[] args) 
    {
	try{
	    System.out.println("About to process: " +  args[0]);
	    EFGDataPrepareTool edpt =  new EFGDataPrepareTool(args[0], args[1]);
	    edpt.process();
	}
	catch(Exception ee){
	    System.out.println("An error occured while processing: " + args[0] );
	    System.err.println(ee.getMessage());
	}
    }
}

/*
$Log$
Revision 1.1  2006/01/25 21:03:48  kasiedu
Initial revision

*/
