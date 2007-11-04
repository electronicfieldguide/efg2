package project.efg.server.digir;
/**
 * $Id$
 * $Name:  $
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
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import project.efg.server.utils.LoggerUtilsServlet;

public class FederationParser extends DefaultHandler {
	private Set set;

	private Locator _locator = null;

	static Logger log = null;
	static {
		try {
		log = Logger.getLogger(FederationParser.class);
		} catch (Exception ee) {
		}
	}

	public FederationParser() {
		set = Collections.synchronizedSet(new TreeSet());
	}

	public FederationParser(String[] fedSchemaNames) {
		set = Collections.synchronizedSet(new TreeSet());
		for (int i = 0; i < fedSchemaNames.length; i++) {
			startParsing(fedSchemaNames[i]);
		}
	}

	public void startParsing(String schemaName) {
		SAXParser _parser;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);
			factory.setFeature(
					"http://apache.org/xml/features/validation/schema", true);
			factory.setNamespaceAware(true);
			_parser = factory.newSAXParser();
			_parser.parse(schemaName, this);
		} catch (Exception e) {
			log.error(e.getMessage());
			LoggerUtilsServlet.logErrors(e);
		}
	}

	public Set getSet() {
		return set;
	}

	public void startDocument() throws SAXException {

	}

	public void endDocument() throws SAXException {

	}

	public void setDocumentLocator(Locator locator) {
		_locator = locator;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {

	}

	/**
	 * Load the Set with all the names in the schema that have a local type
	 * 
	 */
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equalsIgnoreCase("element")) {
			String str = atts.getValue("name");

			if (str != null) {
				String str1 = atts.getValue("abstract");
				if (str1 == null) {
					set.add(str.trim());
				}
			}
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {

	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {

	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {

	}

	public void endPrefixMapping(String prefix) throws SAXException {

	}

	public void processingInstruction(String instruction, String data)
			throws SAXException {

	}

	public void skippedEntity(String name) throws SAXException {

	}

	public void error(SAXParseException e) throws SAXException {

	}

	public void warning(SAXParseException e) throws SAXException {

	}

	public void fatalError(SAXParseException e) throws SAXException {
		
		LoggerUtilsServlet.logErrors(e);
		log.error("Fatal error on line " + _locator.getLineNumber()
				+ ", column " + _locator.getColumnNumber() + ":\n\t"
				+ e.getMessage());
		throw e;
	}
}
