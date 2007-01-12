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
 * 
 * Extracts data from a csv file. 
 */
package project.efg.Imports.rdb;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.EFGDataExtractorInterface;
import project.efg.Imports.factory.CSVParserFactory;

import com.Ostermiller.util.LabeledCSVParser;

/**
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
 * 
 */
public class EFGCSVDataExtractorImpl implements EFGDataExtractorInterface {

	
	static Logger log;
	static {
		try {
			log = Logger.getLogger(EFGCSVDataExtractorImpl.class);
		} catch (Exception ee) {
		}
	}
	private String[] fieldNames;
	private LabeledCSVParser lcsvp;
	private static String defaultException() {
		
		return "Call setFile before you call this method";
	}
	private static void printUnderScoreLine(String[] labels) {
		
	}
	private String[] getParsedFieldNames() throws Exception {
		
		try {
			return processFields(this.lcsvp.getLabels());
		} catch (Exception ee) {
			log.error(ee.getMessage());
			throw new Exception(defaultException());
		}
		
	}
	private static void printLine(String[] labels) {
	
		
	}
	private String[] processFields(String[] csvFields) {
		ArrayList lists = new ArrayList(csvFields.length);
		int size = csvFields.length; 
		log.debug("Field Size before trim: " + size);
		for(int i=0; i < csvFields.length; i++){
			String field = csvFields[i];
			if((field == null) || (field.trim().equals(""))){
				--size;
			}
			else{
				lists.add(field);
			}
		}
		if(size < 1){
			return null;
		}
		log.debug("Field Size after trim: " + size);
		String[] toReturn = new String[size];
		for(int j=0; j < size; j++){
			String field = (String)lists.get(j);
			toReturn[j]= field;
		}
		return toReturn;
	}
	private void setUp(Reader csvFileName, char delimiter) {
		try {
			//use a factory?
			this.lcsvp =(LabeledCSVParser)CSVParserFactory.getCSVParser(csvFileName,
					delimiter);
			
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
	}
	public EFGCSVDataExtractorImpl() {
		
	}
	/**
	 * Get the index of the column having the given label. The first field has
	 * the index 0.
	 * 
	 * @param label-
	 *            The field name.
	 * @return The index of the field name, or -1 if the field name does not
	 *         exist.
	 */
	public int getFieldNameIndex(String label)throws Exception {
		try {
			return this.lcsvp.getLabelIdx(label);
		} catch (Exception ee) {
			log.error(ee.getMessage());
			throw new Exception(defaultException());
		}
		
	}

	/**
	 * Given the label for the column, get the column from the current row. If
	 * the column cannot be found in the line, null is returned.
	 * 
	 * @param label -
	 *            The field name.
	 * @return the value from the column or null if there is no such value
	 */
	public String getValueByFieldName(String label)throws Exception {
		try {
			return this.lcsvp.getValueByLabel(label);
		} catch (Exception ee) {
			log.error(ee.getMessage());
			throw new Exception(defaultException());
		}
		
	}
	/**
	 * A string array of field names to be used to create a database table. The
	 * order of the strings in the array must match the order of the data values
	 * (see getRowValues below)
	 * 
	 * @return a string array of field names
	 */
	public String[] getFieldNames() throws Exception{
		if(this.fieldNames == null){
			this.fieldNames = getParsedFieldNames();
		}
		return this.fieldNames;
	
		
	}

	/**
	 * Return the values of the next row, much like the next() method in
	 * java.util.Iterator
	 * 
	 * @return a string array of data values null if there are no more values to
	 *         return
	 */
	public String[] nextValue() throws Exception{
		try {
			return this.lcsvp.getLine();
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
		return null;
	}

	/**
	 * The maximum number of columns in this object
	 * 
	 * @return the number of coumns in this object
	 */
	public int getNumberOfColumns() throws Exception{
		try {
			return this.getFieldNames().length;
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGDataExtractorInterface#close()
	 */
	public void close() throws Exception{
		try{
			if(this.lcsvp != null){
			
				this.lcsvp.close();
			}
		}
		catch(Exception ee){
			throw new Exception(defaultException());
		}
		
	}
	/**
	 * Defaults to a comma separated delimiter if none is specified.
	 * 
	 * @param csvFileName -
	 *            The full URI to the file to be parsed
	 * @throws Exception
	 */
	public void setFile(URI csvFileName) throws Exception {
		this.setFile(csvFileName, ',');
		
	}

	/**
	 * Defaults to a comma separated delimiter if none is specified.
	 * 
	 * @param csvFileName-
	 *            The full URI to the file to be parsed
	 * @param delimiter -
	 *            The delimiter to use in parsing file
	 * @throws Exception
	 */
	public void setFile(URI csvFileName, char delimiter) throws Exception {
		this.setFile(new FileReader(new File(csvFileName)), delimiter);
		
	}
	/**
	 * Defaults to a comma separated delimiter if none is specified.
	 * 
	 * @param in -
	 *            A stream containing data to be imported
	 */
	public void setFile(InputStream in) throws Exception {
		this.setFile(new InputStreamReader(in));
		
	}
	/**
	 * 
	 * @param in -
	 *            A stream containing data to be imported
	 * @param delimiter -
	 *            The delimiter to use in parsing stream
	 */
	
	public void setFile(InputStream in, char delimiter) throws Exception {
		this.setFile(in, delimiter);
		
	}
	/**
	 * Defaults to a comma separated delimiter if none is specified.
	 * 
	 * @param in -
	 *            A Reader object containing data to be imported
	 */
	public void setFile(Reader in) throws Exception {
		this.setFile(in, ',');
		
	}
	/**
	 * 
	 * @param in -
	 *            A Reader object containing data to be imported
	 * @param delimiter -
	 *            The delimiter to use in parsing reader
	 */
	public void setFile(Reader in, char delimiter) throws Exception {
		this.setUp(in, delimiter);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String csvFileName = "C:\\cvscheckout\\efg2\\lib\\data\\Ith.csv";
		EFGCSVDataExtractorImpl extractor = new EFGCSVDataExtractorImpl();
		extractor.setFile(new URI(
				csvFileName));
		String[] labels = extractor.getFieldNames();
		printLine(labels);
		printUnderScoreLine(labels);
	
		String[] currentValues = extractor.nextValue();
		while (currentValues != null) {
			printLine(currentValues);
		}
	}

}
