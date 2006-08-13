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

package project.efg.Imports.efgImportsUtil;

import java.beans.Introspector;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import project.efg.util.EFGImportConstants;

import com.Ostermiller.util.ExcelCSVParser;
import com.Ostermiller.util.LabeledCSVParser;

/**
 * This file contains the class EFGUtils, which has many static fields and
 * methods available to the EFG. Usually, items are added here when it's noticed
 * that similar functionality is present in many other classes. It would
 * probably be a good idea to periodically go through this file and make sure
 * that the fields and methods present shouldn't be in a particular class.
 */
public class EFGUtils {
	private static String catalina_home = null;

	private static String[] metadataTableHeaders = null;

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGUtils.class);
		} catch (Exception ee) {
		}
	}

	/**
	 * @return a string with a maximum of 240 characters
	 * @param string -
	 *            The string to parse
	 */
	public synchronized static String parse240(String string) {
		if (string == null) {
			return "";
		}
		string = string.trim();
		if (string.length() > 240) {
			string = string.substring(0, 240);
		}
		return string;
	}

	/**
	 * @return a string form of a uri where uri separators are replaced with EFg
	 *         separators
	 * @param string -
	 *            The uri form of a string to parse
	 */
	public synchronized static String parseEFGSEP(String string) {
		string = string.replaceAll(project.efg.util.EFGImportConstants.FORWARD_SLASH,
				project.efg.util.EFGImportConstants.EFG_SEP);
		return string.replaceAll(project.efg.util.EFGImportConstants.COLONSEP,
				project.efg.util.EFGImportConstants.EFG_COLON);
	}

	/**
	 * @return a string with EFG separators replaced with uri separators
	 * @param string -
	 *            The string to parse
	 */
	public synchronized static String reverseParseEFGSEP(String string) {
		string = string.replaceAll(project.efg.util.EFGImportConstants.EFG_SEP,
				project.efg.util.EFGImportConstants.FORWARD_SLASH);
		return string.replaceAll(project.efg.util.EFGImportConstants.EFG_COLON,
				project.efg.util.EFGImportConstants.COLONSEP);
	}

	/**
	 * @param fullName -
	 *            The full path to a file
	 * @return the name of a file without the extension
	 */
	public synchronized static String getName(URI fullFileName) {
		String name = fullFileName.toString();
		try {
			File f = new File(name);
			name = f.getName();
			int index = name.lastIndexOf(".");
			if (index > -1) {
				return name.substring(0, index);
			}
		} catch (Exception ee) {

		}
		return name;
	}

	public static String[] getMetadataTableHeaders() {
		if (metadataTableHeaders == null) {
			log.debug("About to read metadata files for the first time");
			metadataTableHeaders = readMetadataTableHeaders();
			log.debug("Read metadata files for the first time");
			if (metadataTableHeaders == null) {
				log.debug("MetadataTable file Could not be read");
			}
		}
		return metadataTableHeaders;
	}

	private static String[] readMetadataTableHeaders() {
		try {
			InputStream stream = project.efg.Imports.efgImportsUtil.EFGUtils.class
					.getResourceAsStream(project.efg.util.EFGImportConstants.CSV_METADATA_HEADERS);

			LabeledCSVParser lcsvp = new LabeledCSVParser(new ExcelCSVParser(
					stream));
			if (lcsvp != null) {
				return lcsvp.getLabels();
			}
		} catch (Exception ee) {
			log.error(" An error occured during importation of : "
					+ project.efg.util.EFGImportConstants.CSV_METADATA_HEADERS + "\n");
			log.error(ee.getMessage());
		}
		return null;
	}

	public static Properties getEnvVars() {
		Process p = null;
		Properties envVars = new Properties();
		try {
			Runtime r = Runtime.getRuntime();
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("windows 9") > -1) {
				p = r.exec("command.com /c set");
			} else if ((OS.indexOf("nt") > -1)
					|| (OS.indexOf("windows 20") > -1)
					|| (OS.indexOf("windows xp") > -1)) {
				p = r.exec("cmd.exe /c set");
			} else {
				// our last hope, we assume Unix (thanks to H. Ware for the fix)
				p = r.exec("env");
			}
			if (p != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(p
						.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					int idx = line.indexOf('=');
					if (idx > 0) {
						String key = line.substring(0, idx);
						String value = line.substring(idx + 1);
						envVars.setProperty(key, value);
					}
				}
			}
		} catch (Exception ee) {
			String message = ee.getMessage();
			log.error(message);
		}
		return envVars;
	}
	public static void setCatalinaHome(String catalina){
		catalina_home = catalina;
	}
	public static String getCatalinaHome() {
		if (catalina_home != null) {
			return catalina_home;
		}
		Properties props = getEnvVars();
		if (props != null) {
			catalina_home = props.getProperty("CATALINA_HOME");
		}
		if (props == null) {
			log.error("Catalina home environment variable is not set!!");
		}
		return catalina_home;
	}

	/**
	 * Writes msg.toString() to System.err and then flushes the stream.
	 * 
	 * @param msg
	 *            msg object to write
	 */
	public static void log(Object msg) {
		if (msg == null) {
			System.err.println("");
		} else {
			System.err.println(new Date().toString() + "-------- "
					+ msg.toString());
		}
		System.err.flush();
	}

	/**
	 * This method takes a String and returns an encoded version of the String
	 * that can be used as a Java variable name.
	 * 
	 * @param origString
	 *            the pre-encoded string
	 * @return the encoded string to be used as a java variable
	 */
	public static String encodeToJavaName(String origString) {
		String str = origString;
		while(EFGImportConstants.SQL_KEY_WORDS.contains(str.toLowerCase())){
			str = str + "_";
		}
		return Introspector.decapitalize(encodeToJavaClassName(str));
	}

	/**
	 * This method takes a String and returns an encoded version of the String
	 * that can be used as a Java class name.
	 * 
	 * @param origString
	 *            the pre-encoded string
	 * @return the encoded string to be used as a java class name
	 */
	private static String encodeToJavaClassName(String origString) {
		StringBuffer sb = new StringBuffer(origString);
		int strLength = sb.length();

		if (strLength > 0 && !Character.isJavaIdentifierStart(sb.charAt(0)))
			sb.setCharAt(0, '_');

		for (int i = 1; i < strLength; i++)
			if (!Character.isJavaIdentifierPart(sb.charAt(i)))
				sb.setCharAt(i, '_');

		return sb.toString();
	}

}

// $Log$
// Revision 1.1.2.1  2006/08/13 23:53:12  kasiedu
// *** empty log message ***
//
// Revision 1.1.2.3  2006/08/09 18:55:24  kasiedu
// latest code confimrs to what exists on Panda
//
// Revision 1.1.2.2  2006/07/11 21:46:12  kasiedu
// "Added more configuration info"
//
// Revision 1.1.2.1  2006/06/08 13:27:40  kasiedu
// New files
//
// Revision 1.2 2006/02/25 13:16:42 kasiedu
// no message
//
// Revision 1.1.1.1 2006/01/25 21:03:48 kasiedu
// Release for Costa rica
//
// Revision 1.1.1.1 2003/10/17 17:03:09 kimmylin
// no message
//
// Revision 1.3 2003/08/20 18:45:42 kimmylin
// no message
//
