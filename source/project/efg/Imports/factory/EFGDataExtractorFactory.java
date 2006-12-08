package project.efg.Imports.factory;
/* $Id$
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
*/
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.efg.Imports.efgInterface.EFGDataExtractorInterface;

public class EFGDataExtractorFactory {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGDataExtractorFactory.class);
		} catch (Exception ee) {
		}
	}
	private static EFGDataExtractorInterface getType() {
		try {
			ApplicationContext    context = 
				new ClassPathXmlApplicationContext("springconfig.xml");
			return (EFGDataExtractorInterface)context.getBean("csvProcessor");
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}
			return null;
	}
	
	public synchronized static  EFGDataExtractorInterface getDataExtractor(URI csvFileName) throws Exception {
		EFGDataExtractorInterface efg = getType();
		efg.setFile(csvFileName);
		return efg;
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
	public synchronized static EFGDataExtractorInterface getDataExtractor(URI csvFileName, char delimiter)
			throws Exception {
		EFGDataExtractorInterface efg = getType();
		efg.setFile(new FileReader(new File(csvFileName)),delimiter);
		return efg;
	}

	/**
	 * Defaults to a comma separated delimiter if none is specified.
	 * 
	 * @param in -
	 *            A stream containing data to be imported
	 * @throws Exception 
	 */
	public  synchronized static EFGDataExtractorInterface getDataExtractor(InputStream in) throws Exception {
		EFGDataExtractorInterface efg = getType();
		efg.setFile(new InputStreamReader(in));
		return efg;
	}

	/**
	 * 
	 * @param in -
	 *            A stream containing data to be imported
	 * @param delimiter -
	 *            The delimiter to use in parsing stream
	 * @throws Exception 
	 */
	public synchronized static EFGDataExtractorInterface getDataExtractor(InputStream in, char delimiter) throws Exception {
		EFGDataExtractorInterface efg = getType();
		efg.setFile(in, delimiter);
		return efg;
	}

	/**
	 * Defaults to a comma separated delimiter if none is specified.
	 * 
	 * @param in -
	 *            A Reader object containing data to be imported
	 * @throws Exception 
	 */
	public synchronized static EFGDataExtractorInterface getDataExtractor(Reader in) throws Exception {
		EFGDataExtractorInterface efg = getType();
		efg.setFile(in, ',');
		return efg;
	}
	



}
