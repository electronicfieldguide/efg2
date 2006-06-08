/*
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Hua Tang<htang@cs.umb.edu>
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

package project.efg.Imports.rdb;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class create a SAX handler to parse a xml document created by filemaker.
 */
public class EFGParserHandler extends DefaultHandler {
	// field tag used in the XML export from FileMaker
	public static final String ELE_DOC = "FMPXMLRESULT";

	public static final String ELE_DATABASE = "DATABASE";

	public static final String ELE_METADATA = "METADATA";

	public static final String ELE_FIELD = "FIELD";

	public static final String ELE_RESULT = "RESULTSET";

	public static final String ELE_ROW = "ROW";

	public static final String ELE_COL = "COL";

	public static final String ELE_DATA = "DATA";

	public static final String ATT_FIELD_NAME = "NAME";

	public static final String ATT_RESULT_FOUND = "FOUND";

	private Locator _locator = null;

	private String[] _header;

	private String[] _record;

	private int _recordIndex;

	private List _records;

	private List _headerList;

	private int _fieldNum;

	private int _type = -1;

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGParserHandler.class);
		} catch (Exception ee) {
		}
	}

	public void startDocument() throws SAXException {
		log.debug("startDocument");
	}

	public void endDocument() throws SAXException {
		log.debug("endDocument");
	}

	public void setDocumentLocator(Locator locator) {
		_locator = locator;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (_type == 0) {
			_record[_recordIndex] += new String(ch, start, length);
			log.debug("In char, index is " + _recordIndex + ", value is "
					+ _record[_recordIndex]);
		}
		String charString = new String(ch, start, length);
		log.debug("characters: " + charString);
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {

		if (localName.toUpperCase().equals(ELE_METADATA)) {
			_headerList = new LinkedList();
		} else if (localName.toUpperCase().equals(ELE_FIELD)) {
			String field = atts.getValue(ATT_FIELD_NAME);
			log.debug("Adding: " + field);
			_headerList.add(field);
		} else if (localName.toUpperCase().equals(ELE_RESULT)) {
			_records = new LinkedList();
		} else if (localName.toUpperCase().equals(ELE_ROW)) {
			_record = new String[_fieldNum];
			for (int i = 0; i < _record.length; i++) {
				_record[i] = "";
			}
			_recordIndex = -1;
		} else if (localName.toUpperCase().equals(ELE_COL)) {
			_recordIndex++;
		} else if (localName.toUpperCase().equals(ELE_DATA)) {
			_type = 0;
		}

		log.debug("startElement: \n\tnamespace: " + namespaceURI
				+ "\n\tlocalName: " + localName);
		log.debug("qualified name is " + qName);

		// list out the attributes and their values
		for (int i = 0; i < atts.getLength(); i++) {
			log.debug("Attribute: " + atts.getLocalName(i));
			log.debug("\tValue: " + atts.getValue(i));
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.toUpperCase().equals(ELE_METADATA)) {
			int n = _headerList.size();
			_header = new String[n];
			for (int i = 0; i < n; i++) {
				_header[i] = (String) _headerList.get(i);
			}
			_fieldNum = _header.length;
		} else if (localName.toUpperCase().equals(ELE_ROW)) {
			log.debug("Record size before: " + _records.size());
			_records.add(_record);
			log.debug("Record size after: " + _records.size());
		} else if (localName.toUpperCase().equals(ELE_DATA)) {
			_type = -1;
		}

		log.debug("endElement: \n\tnamespace: " + namespaceURI
				+ "\n\tlocalName: " + localName);
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		log.debug(length + " characters of ignorable whitespace");
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		log.debug("Begin namespace prefix: " + prefix);
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		log.debug("End namespace prefix: " + prefix);
	}

	public void processingInstruction(String instruction, String data)
			throws SAXException {
		log.debug("Instruction: " + instruction + ", data: " + data);
	}

	public void skippedEntity(String name) throws SAXException {
		log.debug("Skipped entity: " + name);
	}

	public void error(SAXParseException e) throws SAXException {
		log.debug("Recoverable error on line " + _locator.getLineNumber()
				+ ", column " + _locator.getColumnNumber() + "\n\t"
				+ e.getMessage());
	}

	public void warning(SAXParseException e) throws SAXException {
		log.debug("Warning on line " + _locator.getLineNumber() + ", column "
				+ _locator.getColumnNumber() + "\n\t" + e.getMessage());
	}

	public void fatalError(SAXParseException e) throws SAXException {
		log.error("Fatal error on line " + _locator.getLineNumber()
				+ ", column " + _locator.getColumnNumber() + ":\n\t"
				+ e.getMessage());
		throw e;
	}

	public String[] getHeader() {
		return _header;
	}

	public List getRecords() {
		return _records;
	}
}

// $Log$
// Revision 1.1.2.1  2006/06/08 13:27:42  kasiedu
// New files
//
// Revision 1.1.1.1 2006/01/25 21:03:42 kasiedu
// Release for Costa rica
//
// Revision 1.1.1.1 2003/10/17 17:03:05 kimmylin
// no message
//
// Revision 1.3 2003/08/20 18:45:41 kimmylin
// no message
//
// Revision 1.2 2003/08/01 21:53:30 kasiedu
// *** empty log message ***
//
// Revision 1.1.1.1 2003/07/30 17:03:58 kimmylin
// no message
//
// Revision 1.1.1.1 2003/07/18 21:50:15 kimmylin
// RDB added
//
