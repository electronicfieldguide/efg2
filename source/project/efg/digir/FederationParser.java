package project.efg.digir;
import project.efg.util.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class FederationParser extends DefaultHandler 
{
    private Set set;
    private Locator _locator = null;
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(FederationParser.class); 
	}
	catch(Exception ee){
	}
    }
    public FederationParser(){
      set = Collections.synchronizedSet(new TreeSet());
    }
  public FederationParser(String[] fedSchemaNames){
    set = Collections.synchronizedSet(new TreeSet());  
    for ( int i = 0 ; i < fedSchemaNames.length;i++){
      startParsing(fedSchemaNames[i]);
    }
  }
  public void startParsing(String schemaName){
    SAXParser _parser;
    try{  
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setValidating(true);
      factory.setFeature("http://apache.org/xml/features/validation/schema",true);
      factory.setNamespaceAware(true);
      _parser = factory.newSAXParser();
      _parser.parse(schemaName, this);
    }
    catch(Exception e){
	LoggerUtilsServlet.logErrors(e);
    }
  }
  public Set getSet(){
    return set;
  } 
  public void startDocument() throws SAXException  
  {
    
  }
  
  public void endDocument() throws SAXException
  {
    
  }
  
  public void setDocumentLocator(Locator locator)  
  {
    _locator = locator;
  }

    public void characters(char[] ch, int start, int length) 
	throws SAXException
    {

    }
  /**
   * Load the Set with all the names in the schema that have a local type
   *
   */
    public void startElement(String namespaceURI, String localName, 
			     String qName, Attributes atts)
	throws SAXException
    {
      if(localName.equalsIgnoreCase("element")) {
	String str = atts.getValue("name");

	if(str != null){
	 String str1 = atts.getValue("abstract");
	 if(str1 == null){
	   set.add(str.trim());
	 }
	}
      }
    }

  public void endElement(String namespaceURI, String localName, 
			 String qName)
    throws SAXException
  {
  
  }
    public void ignorableWhitespace (char[] ch, int start, int length)
	throws SAXException
    {

    }

    public void startPrefixMapping(String prefix, String uri)
	throws SAXException
    {

    }

    public void endPrefixMapping(String prefix) throws SAXException 
    {

    }
    
    public void processingInstruction(String instruction, String data)
	throws SAXException
    {

    }

    public void skippedEntity(String name) throws SAXException 
    {

    }

    public void error(SAXParseException e) throws SAXException 
    {

    }

    public void warning(SAXParseException e) throws SAXException 
    {

    }
    
    public void fatalError(SAXParseException e) throws SAXException 
    {
	LoggerUtilsServlet.logErrors(e);
	System.err.println("Fatal error on line " +
	         _locator.getLineNumber() + ", column " + 
		   _locator.getColumnNumber() + ":\n\t" +
	         e.getMessage());
	throw e;
    }
}
