/**
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
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

package project.efg.digir;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import project.efg.servlets.efgImpl.EFGContextListener;
import project.efg.servlets.efgInterface.EFGDataSourceHelperInterface;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.util.EFGDisplayObject;
import project.efg.util.EFGDisplayObjectList;
import project.efg.util.EFGImportConstants;

/**
 * This class parses the DiGIR request and handles its elements while
 * constructing a DiGIR response.
 */
public class DigirParserHandler extends DefaultHandler {
	private String start;

	private String limit;

	private String count;

	private int _countType;

	private int _sourceType;

	private int _destinationType;

	private Stack stack;

	private Stack stack2;

	private Stack inStack;

	private Locator _locator = null;

	private int _type;

	private SAXParser _parser;

	private int ERROR_CODE;

	private int DATASOURCE_ERROR_CODE = 0;

	private String searchType;

	private boolean searchFlag;

	private boolean headerFlag;

	private boolean inOp;

	private EFGQueryList inList;

	private String requestSource;

	private String destinationSource;

	private String requestSourceText;

	private String requestDestinationText;

	private int _filterType;

	private List dataSources;

	private List requestedDataSources;

	private int _inventoryType;

	private String conditionalClause;

	private StringBuffer errorMessages;

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(DigirParserHandler.class);
		} catch (Exception ee) {
		}
	}

	/**
	 * Takes the xml document as an InputSource and begins parsing.
	 * 
	 * @param source
	 *            the InputSource object of the xml entity
	 */
	public DigirParserHandler(InputSource source) {
		init();
		startParsing(source);
	}

	/**
	 * Initialize the SAX parser.
	 */
	public void init() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			//factory.setValidating(true);
			//factory.setFeature(
			//		"http://apache.org/xml/features/validation/schema", true);
			factory.setNamespaceAware(true);
			_parser = factory.newSAXParser();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			LoggerUtilsServlet.logErrors(e);
		}
	}

	/**
	 * Gets the search condition to be used for the inventory requests.
	 * 
	 * @return the inventory's search condition if any
	 */
	public String getConditionalClause() {
		return conditionalClause;
	}

	/**
	 * Gets the resource attribute from the Source element of a request.
	 * 
	 * @return the resource attribute in the request's Source element
	 */
	public String getSourceResource() {
		return requestSource;
	}

	/**
	 * Gets the the text associated with the Source element of the request.
	 * 
	 * @return the text of the request's Source element
	 */
	public String getSourceText() {
		return requestSourceText;
	}

	/**
	 * Gets the resource attribute from the Destination element of the request
	 * The destination source is the same as the DataSource in EFG.
	 * 
	 * @return the resource attribute of the request's Destination element
	 */
	public String getDestinationResource() {
		return destinationSource;
	}

	/**
	 * Gets the the text associated with the Destination element of the request.
	 * 
	 * @return the text of the request's Destination element
	 */
	public String getDestinationText() {
		return requestDestinationText;
	}

	/**
	 * Gets the DiGIR request's search type (search, metadata, inventory).
	 * 
	 * @return the search type of the DiGIR request
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * Returns the start attribute specified in the request's record element.
	 * DiGIR response will display the result starting from this record number.
	 * 
	 * @return the string of the start attribute of the request's record
	 *         element.
	 */
	public String getStart() {
		return this.start;
	}

	/**
	 * Returns the limit attribute specified in the request's record element.
	 * DiGIR response will display the result ending at this record number.
	 * 
	 * @return the string of the limit attribute of the request's record
	 *         element.
	 */
	public String getLimit() {
		return this.limit;
	}

	/**
	 * Returns the count string (true/false) specified in DiGIR request. If the
	 * value is false, the reponse will not include the count element.
	 * 
	 * @return the value of the count element in the query
	 */
	public String getCount() {
		return this.count;
	}

	/**
	 * Returns the stack containing the parsed query or an error if one occured
	 * while parsing.
	 * 
	 * @return a Stack containing the parsed query
	 */
	public Stack getStack() {
		return stack;
	}

	/**
	 * Returns the error code.
	 * 
	 * @return -1 if an error occured, 0 otherwise
	 */
	public int getErrorCode() {
		return ERROR_CODE;
	}

	/**
	 * Returns the error code if all the DataSources specified not found in our
	 * database.
	 * 
	 * @return -1 if all requested datasources cannot be found in our database,
	 *         0 otherwise
	 */
	public int getDataSourceErrorCode() {
		return DATASOURCE_ERROR_CODE;
	}

	/**
	 * Return a list of requested datasources in the query.
	 * 
	 * @return a list of the requested datasources
	 */
	public List getRequestedDataSources() {
		return requestedDataSources;
	}

	/**
	 * Parse the xml content.
	 * 
	 * @param source
	 *            the InputSource object of the xml entity
	 */
	public void startParsing(InputSource source) {
		try {
			_parser.parse(source, this);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			LoggerUtilsServlet.logErrors(e);
		}
	}

	/**
	 * Receive notification of the beginning of the document to take any
	 * specific actions at the beginning of a document.
	 */
	public void startDocument() throws SAXException {
		errorMessages = new StringBuffer();

		// Get all the data sources in our Database
		EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
		EFGDisplayObjectList lists = dsHelper.getDataSourceNames();
		Iterator iter = lists.getIterator();

		dataSources = new ArrayList();

		while (iter.hasNext()) {
			EFGDisplayObject datasource = (EFGDisplayObject) iter.next();
			String str = datasource.getDatasourceName();
			dataSources.add(str);
		}

		requestedDataSources = new LinkedList();

		stack = new Stack();
		stack2 = new Stack();

		inStack = new Stack();
		/*
		 * holds the contents of the list of elements in the IN operator e.g
		 * <IN> <list...> <genus>Mechanitis</genus> <genus>Greta</genus>
		 * ...... </list> </IN>
		 */

		// used to get the start and limit attributes of a record element
		start = "0";
		limit = "0";

		_countType = -1; // signifies that we are processing a count element.
		_sourceType = 0; // signifies that we are processing a source
							// element.
		_destinationType = 0; // signifies that we are processing a
								// destination element
		_filterType = 0; // signifies that we are processing a filter element
		_inventoryType = 0; // signifies that we are processing an inventory
							// element

		_type = -1; // signifies that we are processing a DiGIR search type
		ERROR_CODE = 0; // set to -1 if Error occurs
		searchFlag = false; // We are processing a search element
		headerFlag = true; // we are processing a header
		inOp = false; // true if we see an IN operator
		inList = new EFGQueryList(); // Will hold a list of searchable type
										// when an IN operator is seen
	}

	/**
	 * Receive notification of the end of the document to take any specific
	 * actions at the end of a document.
	 */
	public void endDocument() throws SAXException {
		// The ff conditions will have to be met to signal a datasource request
		// error
		/*
		 * 1.At least one dataSource was requested 2.All the DataSources was
		 * requested do not exist in our Database.
		 */
		if (((requestedDataSources == null) || (requestedDataSources.size() == 0))
				&& (errorMessages.length() > 0)) {
			DATASOURCE_ERROR_CODE = -1;
			error(new SAXParseException(errorMessages.toString(), _locator));
		}
	}

	/**
	 * Receive a Locator object for document events.
	 * 
	 * @param locator
	 *            the locator for all SAX document events
	 */
	public void setDocumentLocator(Locator locator) {
		_locator = locator;
	}

	/**
	 * Receive notification of the start of an element.
	 * 
	 * @param namespaceURI
	 *            the Namespace URI, or the empty string if the element has no
	 *            Namespace URI or if Namespace processing is not being
	 *            performed
	 * @param localName
	 *            the local name (without prefix), or the empty string if
	 *            Namespace processing is not being performed
	 * @param qName
	 *            the qualified name (with prefix), or the empty string if
	 *            qualified names are not available
	 * @param atts
	 *            the attributes attached to the element. If there are no
	 *            attributes, it shall be an empty Attributes object
	 */
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		// add namespace checks
		if ("IN".equalsIgnoreCase(localName)) {
			inOp = true;
		} else if ("records".equalsIgnoreCase(localName)) {
			this.start = atts.getValue("start");
			this.limit = atts.getValue("limit");
		} else if ("header".equals(localName)) {
			headerFlag = true;
		} else if ("type".equals(localName)) {
			if (headerFlag) {
				searchFlag = true;
			}
		} else if ("filter".equalsIgnoreCase(localName)) {
			_filterType = 1;
		} else if ("count".equalsIgnoreCase(localName)) {
			_countType = 1;
		} else if ("inventory".equalsIgnoreCase(localName)) {
			_inventoryType = 1;
		}

		else if ("source".equalsIgnoreCase(localName)) {
			if (headerFlag) {
				if (atts != null) {
					requestSource = atts.getValue("resource");
				}
				_sourceType = 1;
			}
		} else if ("destination".equalsIgnoreCase(localName)) {
			if (headerFlag) {
				if (atts != null) {
					destinationSource = atts.getValue("resource");
					if (destinationSource != null) {
						if (!dataSources.contains(destinationSource)) {
							String message = "Error on line "
									+ _locator.getLineNumber() + ", column "
									+ _locator.getColumnNumber()
									+ ".The DataSource: " + destinationSource
									+ " cannot be found";
							errorMessages.append(message);
						} else {
							// load the table for the current datasource and get
							// the legalname
							requestedDataSources.add(destinationSource);
						}
					}
				}
				_destinationType = 1;
			}
		} else {
			if (localName.trim()
					.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1) {
				EFGContextListener.addToSet(localName.trim());
			}
			if (EFGContextListener.contains(localName.trim())) {// A federation
																// schema type
				if (_filterType == 1) {
					_type = 0;
					stack2.push(localName);
				} else {
					if (_inventoryType == 1) {
						conditionalClause = localName;
					}
				}
			}
		}
	}

	/**
	 * Receive notification of character data inside an element.
	 * 
	 * @param ch
	 *            the characters
	 * @param start
	 *            the start position in the character array
	 * @param length
	 *            the number of characters to use from the character array
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String str;
		String pop;
		try {
			if (_type == 0) {
				str = new String(ch, start, length);
				str = str.replaceAll(" ",
						EFGImportConstants.SERVICE_LINK_FILLER);
				if (inOp) { // We are inside an IN operator
					pop = (String) stack2.pop();
					inStack.push(new TempObject(pop, str));
				} else {
					pop = (String) stack2.pop();
					stack2.push(new TempObject(pop, str));
				}
			}

			if (_sourceType == 1) {
				if (ch != null)
					requestSourceText = new String(ch, start, length);
			}
			if (_destinationType == 1) {// We have found the datasource
				if (ch != null) {
					requestDestinationText = new String(ch, start, length);
				}
			}
			if (_countType == 1) { // If the count tag is encountered
				str = new String(ch, start, length);
				count = str;
				_countType = -1;
			}
			if (headerFlag && searchFlag) { // We have found the search type in
											// the header
				searchType = new String(ch, start, length);
			}
		} catch (Exception e) {
			// logerror(e.getMessage());
			LoggerUtilsServlet.logErrors(e);
		}
	}

	/**
	 * Receive notification of the end of an element.
	 * 
	 * @param namespaceURI
	 *            the Namespace URI, or the empty string if the element has no
	 *            Namespace URI or if Namespace processing is not being
	 *            performed
	 * @param localName
	 *            the local name (without prefix), or the empty string if
	 *            Namespace processing is not being performed
	 * @param qName
	 *            the qualified name (with prefix), or the empty string if
	 *            qualified names are not available
	 */
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		Object pop2 = null;
		if ("header".equals(localName)) {
			headerFlag = false;
		}
		if ("type".equals(localName)) {
			if (headerFlag)
				searchFlag = false;
		}
		if ("filter".equalsIgnoreCase(localName)) {
			_filterType = 0;
		}
		if ("source".equalsIgnoreCase(localName)) {
			if (headerFlag)
				_sourceType = 0;
		}
		if ("destination".equalsIgnoreCase(localName)) {
			if (headerFlag)
				_destinationType = 0;
		}
		if ("inventory".equalsIgnoreCase(localName)) {
			_inventoryType = 0;
		}
		if ("list".equalsIgnoreCase(localName)) {
			boolean done = false;
			while (done == false) {
				if (inStack.empty()) {
					done = true;
				} else {
					pop2 = inStack.pop();
					TempObject obj = (TempObject) pop2;
					String name = obj.getName();
					String value = obj.getValue();

					inList
							.add(new EFGString(name + " " + "EFG:EQUALS "
									+ value));
				}
			}
			stack.push(inList);
			inStack = null;
			inOp = false;
		}
		try {
			Class cls = null;
			try {
				cls = Class.forName("project.efg.digir."
						+ localName.toUpperCase());
			} catch (Exception ee) {
				cls = null;
			}
			if (cls != null) {
				Class supercls = cls.getSuperclass();
				if (supercls != null) {
					String superClassName = supercls.getName();
					if ("project.efg.digir.COP"
							.equalsIgnoreCase(superClassName)) {
						pop2 = stack2.pop();
						if (pop2 instanceof TempObject) {
							TempObject obj = (TempObject) pop2;
							String name = obj.getName();
							String value = obj.getValue();

							stack.push(new EFGString(name + " " + "EFG:"
									+ localName.toUpperCase() + " " + value));
						} else {
							stack2.push(pop2);
						}
					} else if (superClassName
							.equalsIgnoreCase("project.efg.digir.LOP")) {
						Object popFirst = stack.pop();
						Object popSecond = stack.pop();

						if ((popFirst instanceof EFGString)
								&& (popSecond instanceof EFGString)) {
							String obj2 = ((EFGString) popFirst).toString();

							String obj1 = ((EFGString) popSecond).toString();
							stack.push(new EFGString(" ( " + obj1 + " "
									+ "EFG:" + localName.toUpperCase() + " "
									+ obj2 + " ) "));
						} else if ((popFirst instanceof EFGString)
								&& (popSecond instanceof EFGQueryList)) {
							EFGQueryList list1 = (EFGQueryList) popSecond;
							String obj1 = ((EFGString) popFirst).toString();
							int i = 0;
							StringBuffer inBuff = new StringBuffer();
							inBuff.append(" ( ");
							inBuff.append((list1.get(i)).toString());
							i++;
							while (i < list1.size()) {
								inBuff.append(" EFG:OR" + " "
										+ (list1.get(i)).toString());
								i++;
							}
							inBuff.append(" ) ");
							stack.push(new EFGString(" ( " + inBuff.toString()
									+ " " + "EFG:" + localName.toUpperCase()
									+ " " + obj1 + " ) "));

							inList.clear();
						} else if ((popFirst instanceof EFGQueryList)
								&& (popSecond instanceof EFGString)) {
							EFGQueryList list1 = (EFGQueryList) popFirst;
							String obj1 = ((EFGString) popSecond).toString();
							int i = 0;
							StringBuffer inBuff = new StringBuffer();
							inBuff.append(" ( ");
							inBuff.append((list1.get(i)).toString());
							i++;
							while (i < list1.size()) {
								inBuff.append(" EFG:OR" + " "
										+ (list1.get(i)).toString());
								i++;
							}
							inBuff.append(" ) ");

							stack.push(new EFGString(" ( " + obj1 + " "
									+ "EFG:" + localName.toUpperCase() + " "
									+ inBuff.toString() + " ) "));
							inList.clear();
						} else {
							stack2.push(popSecond);
							stack2.push(popFirst);
						}
					}
				}
			}
		} catch (Exception e) {
			// logerror(e.getMessage());
			LoggerUtilsServlet.logErrors(e);
		} finally {
			if (_type == 0) {
				_type = -1;
			}
		}
	}

	/**
	 * Receive notification of ignorable whitespace in element content.
	 * 
	 * @param ch
	 *            the whitespace characters
	 * @param start
	 *            the start position in the character array
	 * @param length
	 *            the number of characters to use from the character array
	 */
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		
	}

	/**
	 * Receive notification of the start of a Namespace mapping.
	 * 
	 * @param prefix
	 *            the Namespace prefix being declared
	 * @param uri
	 *            the Namespace URI mapped to the prefix
	 */
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		
	}

	/**
	 * Receive notification of the end of a Namespace mapping.
	 * 
	 * @param prefix
	 *            the Namespace prefix being declared
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
	}

	/**
	 * Receive notification of a processing instruction.
	 * 
	 * @param instruction
	 *            the processing instruction target
	 * @param data
	 *            the processing instruction data, or null if none is supplied
	 */
	public void processingInstruction(String instruction, String data)
			throws SAXException {
	}

	/**
	 * Receive notification of a skipped entity.
	 * 
	 * @param name
	 *            the name of the skipped entity
	 */
	public void skippedEntity(String name) throws SAXException {
	}

	/**
	 * Receive notification of a recoverable parser error.
	 * 
	 * @param e
	 *            the warning information encoded as an SAXParseException
	 */
	public void error(SAXParseException e) throws SAXException {
		if (DATASOURCE_ERROR_CODE == -1) {
			stack.clear();
			stack.push(new EFGString(e.getMessage()));
			throw e;
		}
	}

	/**
	 * Receive notification of a parser warning.
	 * 
	 * @param e
	 *            the warning information encoded as an SAXParseException
	 */
	public void warning(SAXParseException e) throws SAXException {
		// System.err.println("Warning on line " + _locator.getLineNumber() + ",
		// column " +
		// _locator.getColumnNumber() + "\n\t" + e.getMessage());
	}

	/**
	 * Report a fatal XML parsing error.
	 * 
	 * @param e
	 *            the warning information encoded as an SAXParseException
	 */
	public void fatalError(SAXParseException e) throws SAXException {
		ERROR_CODE = -1;
		stack.clear();
		stack.push(new EFGString("Fatal error on line "
				+ _locator.getLineNumber() + ", column "
				+ _locator.getColumnNumber() + ":\n\t" + e.getMessage()));
		System.err.println("Fatal error on line " + _locator.getLineNumber()
				+ ", column " + _locator.getColumnNumber() + ":\n\t"
				+ e.getMessage());
		throw e;
	}
}

// $Log$
// Revision 1.4  2007/01/17 06:21:55  kasiedu
// no message
//
// Revision 1.3  2006/12/08 03:51:00  kasiedu
// no message
//
// Revision 1.1.1.1.2.5  2006/11/07 14:37:22  kasiedu
// no message
//
// Revision 1.1.1.1.2.4  2006/08/26 22:12:24  kasiedu
// Updates to xsl files
//
// Revision 1.1.1.1.2.3 2006/08/09 18:55:24 kasiedu
// latest code confimrs to what exists on Panda
//
// Revision 1.1.1.1.2.2 2006/07/11 21:44:19 kasiedu
// "Added more configuration info"
//
// Revision 1.1.1.1.2.1 2006/06/08 13:13:55 kasiedu
// New files
//
// Revision 1.1.1.1 2006/01/25 21:03:48 kasiedu
// Release for Costa rica
//
// Revision 1.4 2005/04/27 19:41:22 ram
// Recommit all of ram's allegedly working copy of efgNEW...
//
// Revision 1.1.1.1 2003/10/17 17:03:09 kimmylin
// no message
//
