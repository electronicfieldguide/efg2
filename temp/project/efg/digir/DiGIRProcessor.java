package project.efg.digir;
/**
 * $Id$
 * $Name$
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

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import project.efg.Imports.efgImportsUtil.LoggerUtils;
import project.efg.servlets.efgImpl.EFGContextListener;
import project.efg.servlets.efgInterface.EFGDataSourceHelperInterface;
import project.efg.util.EFGDisplayObject;
import project.efg.util.EFGDisplayObjectList;

public class DiGIRProcessor {
  private DigirParserHandler dph;
  private HttpServletRequest req;
  private Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");//Used as part of DiGIR response
  private static String resource; //used as part of DiGIR response
  private static Namespace digir = Namespace.getNamespace("http://www.namespaceTBD.org/digir");
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(DiGIRProcessor.class); 
	}
	catch(Exception ee){
	}
    }
  public DiGIRProcessor(DigirParserHandler dph,HttpServletRequest req){
    this.dph = dph;
    this.req = req;
  }
  /**
   *Create a DiGIR response based on a DiGIR request.
   *
   *@param the HttpServletRequest object
   *@param the HttpServletResponse object
   *@param an EFGDocument obtained after executing the DiGIR request
   *@param DigirParserHandler containing information about the parsed DiGIR request
   */
  public Element presentDigirResponse(Object obj)
  {

      int numRecords = 0;
      boolean unKnownError = false; //signifies an error while a DiGIR request is being processed after parsing    
      String searchType = null;
      List list = null;
      
      Element responseWrapper = null;
      Element response = null;
      Element content = null;
      Element header = null;
      Element diagnostics = null;
      
      if(dph != null){ //get the DiGIR search type(search, metadata or inventory)
	  searchType = dph.getSearchType();
      }

      responseWrapper = getResponseWrapperElement();//get the responseWrapper element which will be the root of the response
      response = new Element("response",digir);
      
      if (obj != null) {
	  if (obj instanceof Element) {
	      list = ((Element) obj).getChildren("TaxonEntry"); //get all TaxonEntry elements    
	      numRecords = list.size();
	  }
	  else if (obj instanceof Integer) {
	      numRecords = ((Integer) obj).intValue();
	  }
      }

      header = getHeader();//get the header element which contains header information for response    
      response.addContent(header);

      if((dph.getErrorCode() != -1) && (dph.getDataSourceErrorCode() != -1)){
	  if(("search".equalsIgnoreCase(searchType))|| ("inventory".equalsIgnoreCase(searchType))){
	      if (obj != null){
		  if (obj instanceof Element)
		      content = getSearchResponseContents((Element)obj);
		  else if (obj instanceof Integer)
		      content = getInventoryResponseContents((Integer)obj);
	      }    
	  }
	  else if("metadata".equalsIgnoreCase(searchType.trim())){
	      content = getMetaDataResponseContents();
	      response.addContent(content);
	      responseWrapper.addContent(response);
	   	      
	      return responseWrapper;
	  }
	  else{
	      unKnownError = true;
	  }
	  response.addContent(content);
      }

      diagnostics = getDiagnostics(numRecords,unKnownError); //get the diagnostics
      response.addContent(diagnostics);    
      responseWrapper.addContent(response);
      return responseWrapper;
    
  }

  /**
   *Get the JDOM header element. This is used as part of the DiGIR response
   *@param the HttpServletRequest object
   *@param searchType, a String specifying the search type(search, metadata,inventory)
   *@param DigirParserHandler containing information about the parsed DiGIR request
   *
   *@return a JDOM element containing header information to be used for DiGIR response
   */
  private Element getHeader(){
      
      // Set hashSet = Collections.synchronizedSet(new HashSet());
    Element source = null;
    resource = dph.getSourceResource();
    String clientIPaddr = req.getRemoteAddr(); // 123.123.123.123
    // Get client's hostname
    String clientDomainName = req.getRemoteHost(); // hostname.com

    //If the resource is null and there is an error send the defaultvresource shown below.
    if(resource == null){
	StringBuffer buf1 = new StringBuffer();
	buf1.append(req.getScheme()); // http
	buf1.append("://");
	buf1.append(req.getServerName()); //tiger.cs.umb.edu
	buf1.append(":");
	buf1.append(req.getServerPort()); // 8080
	buf1.append(req.getContextPath()); //efg
	buf1.append(req.getServletPath()); //efg
	resource = buf1.toString();
    }
    
    Element header = new Element("header",digir);
    
    //Read from servlet context or from a properties file
    Element version = new Element("version",digir);
    version.setText("version 1.0"); 
    header.addContent(version);
    
    String s = formatter.format(new Date());
    Element sendTime = new Element("sendTime",digir);
    sendTime.setText(s);
    header.addContent(sendTime);
    

    if((dph.getDataSourceErrorCode()!= -1) &&(dph.getErrorCode() != -1)){
      source = new Element("source",digir);
      source.setAttribute("resource",resource);
      source.setText(resource);
      
      header.addContent(source);
    }
    else{//there is an error 
	source = new Element("source",digir);
	source.setAttribute("resource",resource);
	if(dph.getDestinationText() != null){
	  source.setText(dph.getDestinationText());
	}
	else{
	  source.setText(resource);
	}
	header.addContent(source);
    }
    
    Element destination = new Element("destination",digir);
    destination.setAttribute("resource",clientDomainName);
    destination.setText(clientIPaddr);
    header.addContent(destination);
    
    Element type = new Element("type",digir);
    type.setText(dph.getSearchType());
    header.addContent(type);

    return header;
    
  }
  /**
   *Gets and returns the responseWrapper JDOm element as the root of the DiGIR response
   *
   *@return a JDOM element which is the root of the DiGIR response
   */
  private Element getResponseWrapperElement(){

    //Namespace declarations
    //Read from the context at startup
    Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    Namespace darwin = Namespace.getNamespace("darwin", "http://www.namespaceTBD.org/darwin2" );
  
    //Schema location.Read from the context at start up time
    String location = "http://www.namespaceTBD.org/digir http://digir.sourceforge.net/prot/beta3/digir.xsd http://www.namespaceTBD.org/darwin2 http://digir.sourceforge.net/fed/beta3/darwin2.xsd";
    
    Element responseWrapper = new Element("responseWrapper",digir);
    responseWrapper.addNamespaceDeclaration(darwin);
    responseWrapper.addNamespaceDeclaration(xsi);
    
    responseWrapper.setAttribute("schemaLocation",location,xsi);  
    return responseWrapper;       
  }
  /**
   * Get the content element to be used to build the DiGIR reponse
   *
   *@param DigirParserHandler containing information about the parsed DiGIR request
   *@param List a list of TaxonEntry JDOM elements obtained after executing the DiGIR query
   *
   *@return the JDOM content element to be used as part of the DiGIR response
   */
  private Element getSearchResponseContents(Element efgDocument) {
      Element content = new Element("content",digir);
      List list = efgDocument.getChildren("TaxonEntry");
      Element count = null;

      Element cloneEFG = (Element)(efgDocument.clone());
      (cloneEFG.getChildren()).clear();//Remove all the children of the cloned root element

      //We need the element alone for processing
      if("true".equalsIgnoreCase(dph.getCount())){
	  count = new Element("count",digir);
	  count.setText(list.size() + "");
	  content.addContent(count);
      }

      cloneEFG.detach();
      //Get the value of the start attribute of the request if any
      int start = Integer.parseInt(dph.getStart());
      int limit = Integer.parseInt(dph.getLimit());
      
      for( int i = start ; i < limit ; i++){
	  if(i < list.size()){
	    Element elem = (Element)(((Element)list.get(i)).clone());
	    cloneEFG.addContent(elem.detach());
	  }
	  else {
	    break;
	  }
      }
      content.addContent(cloneEFG);
      return content;
  }
  
  private Element getInventoryResponseContents(Integer count) {
      Element content = new Element("content",digir);
      Element countElem = new Element("count",digir);
      countElem.setText(count.toString());
      content.addContent(countElem);
      return content;
  }

  /**
   * Creates and return a metadata JDOM element to be used for as part of a metadata response.
   *
   *@param a List of requestedDataSources 
   *@return a metadata JDOM element containing all the metadata information about the requested resources.
   *
   */
  private Element getMetaDataResponseContents(){
   
    List requestedDataSources = dph.getRequestedDataSources();
    Element content = new Element("content",digir);   
    Element metadata = new Element("metadata",digir);
    Element provider = new Element("provider",digir);
   
    try{
      SAXBuilder parser = null;
      Document doc = null;
      
      String metadataFile = EFGContextListener.getEFGMetaDataFileName(); //Get metadata file name. 
    String pathToResourceFiles = EFGContextListener.getPathToResourceFiles();//Get the path to all the metadata files containing info about each resource
    
     
     parser = new SAXBuilder();
     doc = parser.build(new File(metadataFile));
     Element root = doc.getRootElement();
     List efgList = root.getChildren();
     Iterator efgIter = efgList.iterator();
     Element temp = null;
     
     while(efgIter.hasNext()){
       temp = (Element)(((Element)efgIter.next()).clone());
       temp.detach();
       provider.addContent(temp);
       
     } 
     
     //If the list of DataSources is empty then get all the DataSources from our Database
     //otherwise use the DataSources from the request that exist in our DataBase
     if((requestedDataSources == null) || (requestedDataSources.size() == 0)){
         EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
         EFGDisplayObjectList lists = dsHelper.getDataSourceNames();
         Iterator iter = lists.getIterator();      
    	 requestedDataSources = new ArrayList();
    	
    	 while(iter.hasNext()){
    		 EFGDisplayObject datasource = (EFGDisplayObject)iter.next();
    		 String str = datasource.getDatasourceName();
    		 requestedDataSources.add(str);
    	 }
     }
     //for each resource get the associated metadata resource information.
     efgIter = requestedDataSources.iterator();
     Element resource = null;
     while(efgIter.hasNext()){
       String str = (String)efgIter.next();
       metadataFile = pathToResourceFiles + System.getProperty("file.separator") + str +".xml";
       doc = parser.build(new File(metadataFile));
       resource = doc.getRootElement();
       temp = (Element)(resource.clone());
       temp.detach();
       provider.addContent(temp);
     } 
    } 
    catch(Exception e){
    	LoggerUtils.logErrors(e);
    }
    metadata.addContent(provider);
    content.addContent(metadata);
    return content;
    
  }
  /**
   * Creates and returns a Diagnostics JDOM element as part of a DiGIR response
   *
   *@param DigirParserHandler which parses the DiGIR requests
   *@param numRecords the number of records contained in the response obtained from the Database
   *@param unKnownError boolean true if an error occured after parsing the requests, false otherwise
   */
  private Element getDiagnostics(int numRecords, boolean unKnownError ){
    Element diagnostics = new Element("diagnostics",digir);
    Element diagnostic = new Element("diagnostic",digir);
    
    if(dph.getErrorCode() == - 1){
      diagnostic.setAttribute("code","fatal error");
      diagnostic.setAttribute("severity","fatal");
      diagnostic.setText((String)(dph.getStack().pop()).toString());
      diagnostics.addContent(diagnostic);
    }
    else if(dph.getDataSourceErrorCode() == -1){
      diagnostic.setAttribute("code","error");
      diagnostic.setAttribute("severity","error");
      diagnostic.setText((String)(dph.getStack().pop()).toString());
      diagnostics.addContent(diagnostic);
    }
    else if(unKnownError){
      diagnostic.setAttribute("code","error");
      diagnostic.setAttribute("severity","error");
      diagnostic.setText(dph.getSearchType() + " is not a DiGIR type");
      diagnostics.addContent(diagnostic);
    }
    else{
      diagnostic.setAttribute("code","RECORD_COUNT");
      diagnostic.setAttribute("severity","info");
      diagnostic.setText(numRecords + "");
      diagnostics.addContent(diagnostic);
    }
    return diagnostics;
  }
 






}
