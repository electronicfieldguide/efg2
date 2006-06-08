/**
 * $Id$
 * $Name$
 * 
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
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
 * Imports a csv file into a relational database
 * 
 */
package project.efg.Imports.factory;

import java.io.Reader;

import com.Ostermiller.util.CSVParse;
import com.Ostermiller.util.ExcelCSVParser;
import com.Ostermiller.util.LabeledCSVParser;

/**
 * @author kasiedu
 *
 */
public class CSVParserFactory {

	/**
	 * A factory method that creates CSVParse objects
	 * @param csvFileName - The csv file to parse
	 * @param delimiter - The delimiter to use to parse date
	 * @return a CSVParse object
	 * @throws Exception
	 * @see com.Ostermiller.util.CSVParse
	 */
	public static synchronized CSVParse getCSVParser
	(Reader csvFileName, char delimiter)throws Exception{
			return new LabeledCSVParser(new ExcelCSVParser(csvFileName,
					delimiter));
	}
}
