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

package project.efg.servlet;
import project.efg.templates.taxonPageTemplates.*;
import project.efg.util.*;
import java.io.*;
import java.net.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * This servlet receives input from author about configuration of a Taxon page for a datasource
 * and creates a TaxonPageTemplate for that datasource.
 */
public class TaxonPageTemplateConfig extends HttpServlet 
{
    static Logger log = null;
    private String mutex = "";
   
    private String realPath;
    /**
     * This method is called when the servlet is first started up.
     */
      /**
     * This method is called when the servlet is first started up.
     * 
     * @params config the ServletConfig object for this servlet.
     */
    public void init(ServletConfig config) throws ServletException 
    {
	super.init(config);
	try{
	    log = Logger.getLogger(TaxonPageTemplateConfig.class); 
	}
	catch(Exception ee){
	}
	realPath = getServletContext().getRealPath("/");
    }

    /**
     * Handles an HTTP GET request - Based most likely on a clicked link.
     *
     * @param req the servlet request object
     * @param res the servlet response object
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException 
    {
	doPost(req, res);
    }

    /**
     * Handles an HTTP POST request - Based most likely on a form submission.
     * 
     * @param req the servlet request object
     * @param res the servlet response object
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException 
    {
	String returnValue = null;
	String dsName = null;
	log.debug("DatasourceName: " + dsName);
        res.setContentType(EFGImportConstants.TEXT_HTML);
	boolean done = false;
	PrintWriter out = res.getWriter();
	try{
	    dsName = (String)req.getParameter(EFGImportConstants.DATASOURCE_STR);
	    returnValue = getParams(req,res);
	}
	catch (IOException ioE){
	    done=false;
	    LoggerUtilsServlet.logErrors(ioE);
	}
	catch (ServletException ex){
	    done=false;
	    LoggerUtilsServlet.logErrors(ex);
	}
	catch(Exception ee){
	    done=false;
	    LoggerUtilsServlet.logErrors(ee);
	}
	if(returnValue != null){
	    req.setAttribute("fieldName",returnValue);
	}
	
	req.setAttribute("dsName",dsName);
	//forward to TestConfigPage.jsp
	String searchPage = (String)req.getParameter(EFGImportConstants.SEARCH_PAGE_STR);//"search"
	RequestDispatcher dispatcher = null;
	if(searchPage != null){
	    String searchType = req.getParameter(EFGImportConstants.SEARCH_TYPE_STR);
	    if(EFGImportConstants.SEARCH_PLATES_TYPE.equalsIgnoreCase(searchType)){
	    	req.setAttribute(EFGImportConstants.SEARCH_TYPE_STR,EFGImportConstants.SEARCH_PLATES_TYPE);
	    }
	    else{
		req.setAttribute(EFGImportConstants.SEARCH_TYPE_STR,EFGImportConstants.SEARCH_LISTS_TYPE);
	    }
	    dispatcher = getServletContext().getRequestDispatcher(EFGImportConstants.TEST_SEARCH_CONFIG_PAGE);
	 
	}
	else{
	    dispatcher = getServletContext().getRequestDispatcher(EFGImportConstants.TEST_TAXON_CONFIG_PAGE);  
	}
	
	dispatcher.forward(req,res);
	out.flush();
	res.flushBuffer();
    }
    private project.efg.templates.taxonPageTemplates.TaxonPageTemplateType createEFGTemplateRoot(String dsName, String xslName){
	project.efg.templates.taxonPageTemplates.TaxonPageTemplateType tp = 
	    new project.efg.templates.taxonPageTemplates.TaxonPageTemplateType();
	tp.setDatasourceName(dsName);
	tp.setXslFileName(xslName);
	return tp;
    }
    /**
     * Get the HttpServletRequest parameters and return the value.
     *
     * @param req the servlet request object
     * @param name the name of the parameter of the HttpServletRequest
     * @return the value of the parameter to be used as a test query
     */	
    private String getParams(HttpServletRequest req, HttpServletResponse res)throws Exception
    {
	String dsName = (String)req.getParameter(EFGImportConstants.DATASOURCE_STR);
	String xslName = (String)req.getParameter(EFGImportConstants.XSL_STRING);
	String searchPage = (String)req.getParameter(EFGImportConstants.SEARCH_PAGE_STR);//"search"
	String returnValue = null;
	project.efg.templates.taxonPageTemplates.TaxonPageTemplateType tptype = 
	    createEFGTemplateRoot(dsName,xslName);
	
	project.efg.templates.taxonPageTemplates.GroupsType groups = 
	    new project.efg.templates.taxonPageTemplates.GroupsType();
	tptype.setGroups(groups);
	
	for (Enumeration e = req.getParameterNames(); e.hasMoreElements(); ) {
	    String key = ((String)e.nextElement()).trim();
	    if(key.trim().equals(EFGImportConstants.XSL_STRING) || 
	       (key.equalsIgnoreCase(EFGImportConstants.DATASOURCE_STR)) 
	       || key.equalsIgnoreCase(EFGImportConstants.SUBMIT)){
		continue;
	    }
	    String value = req.getParameter(key);
	    String val = createGroup(groups,key,value);
	    if(returnValue == null){
		if(val != null){
		    if(!val.trim().equals("")){
			returnValue = val;
		    }
		}
	    }
	}
	StringBuffer fileLocationBuffer = new StringBuffer();
	fileLocationBuffer.append(realPath);
	fileLocationBuffer.append(EFGImportConstants.TEMPLATES_FOLDER_NAME);
	fileLocationBuffer.append(File.separator);
	fileLocationBuffer.append(dsName);
	if((searchPage == null) || (searchPage.trim().equals(""))){
	    fileLocationBuffer.append(EFGImportConstants.TAXONPAGE_FILLER);
	}
	else{
	    String searchType = req.getParameter(EFGImportConstants.SEARCH_TYPE_STR);
	    if(EFGImportConstants.SEARCH_PLATES_TYPE.equalsIgnoreCase(searchType)){
		log.debug("Plates");
		fileLocationBuffer.append(EFGImportConstants.SEARCHPAGE_PLATES_FILLER);
	    }
	    else{
		log.debug("Lists");
		fileLocationBuffer.append(EFGImportConstants.SEARCHPAGE_LISTS_FILLER);
	    }
	}
	String fileLocation = fileLocationBuffer.toString();

	project.efg.templates.taxonPageTemplates.TaxonPageTemplatesType tps = 
	    new  project.efg.templates.taxonPageTemplates.TaxonPageTemplatesType();
	
	project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem vTaxonPageTemplatesTypeItem =
	    new project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem();
	vTaxonPageTemplatesTypeItem.setTaxonPageTemplate(tptype);
	tps.addTaxonPageTemplatesTypeItem(vTaxonPageTemplatesTypeItem);
	
	//lock file to block this operation from happening
	File file = new File(fileLocation);
	String renamedFile = fileLocation + System.currentTimeMillis()  + "_old";
	File file2 = new File(renamedFile);
	boolean isExists = false;
	boolean success = false;
	boolean done = true;
	if(file.exists()){
	    isExists = true;
	    // Rename file (or directory)
	    synchronized(mutex){
		 try{
		     success = file.renameTo(file2);
		 }
		 catch(Exception eeer){
		     log.debug(eeer.getMessage());
		 }
	    }
	    if (!success) {
		isExists = false;
		log.debug("File could not be renamed");
	    }
	}
	FileWriter writer = null;
	try{
	    synchronized(mutex){
		try{
		    writer = new FileWriter(fileLocation);
		    tps.marshal(writer);
		    writer.flush();
		    writer.close();
		}
		catch(Exception eee){
		    done = false;
		}
	    }
	}
	catch(Exception ee){
	    done = false;
	    try{
		//rename file to a new one
		if(writer != null){
		    writer.flush();
		    writer.close();
		}
		if(isExists){
		    synchronized(mutex){
			success = file2.renameTo(file);
		    }
		    if (!success) {
			isExists = false;
			log.debug("File could not be renamed when exception occured");
		    }  
		}
	    }
	    catch(Exception ff){

	    }
	    throw new Exception(ee.getMessage());
	}
	if(done){
	    return returnValue;
	}
	return null;
	
    }	
 
    private project.efg.templates.taxonPageTemplates.GroupType checkGroup(project.efg.templates.taxonPageTemplates.GroupsType groups,
									  String groupID,
									  int groupRank){

	project.efg.templates.taxonPageTemplates.GroupType gt = null;
	project.efg.templates.taxonPageTemplates.GroupsTypeItem key = null;
	boolean found = false;
	for(java.util.Enumeration e = groups.enumerateGroupsTypeItem(); e.hasMoreElements();){
	    key =(project.efg.templates.taxonPageTemplates.GroupsTypeItem)e.nextElement(); 
	    gt = key.getGroup();
	    String id = gt.getId();
	    int rank = gt.getRank();
	    if((id.equals(groupID)) && (rank == groupRank)){
		found = true;
		break;
	    }
	}
	if(!found){
	    gt =new project.efg.templates.taxonPageTemplates.GroupType();
	    gt.setId(groupID);
	    gt.setRank(groupRank);
	    key = new project.efg.templates.taxonPageTemplates.GroupsTypeItem();
	    key.setGroup(gt);
	    groups.addGroupsTypeItem(key);
	}
	return gt;
    }
    private project.efg.templates.taxonPageTemplates.CharacterValue findCharacter(project.efg.templates.taxonPageTemplates.GroupType group, 
										  int characterRank){
	project.efg.templates.taxonPageTemplates.GroupTypeItem key = null;
	project.efg.templates.taxonPageTemplates.CharacterValue cv = null;
	boolean found = false;
	for(java.util.Enumeration e = group.enumerateGroupTypeItem(); e.hasMoreElements();){
	    key =(project.efg.templates.taxonPageTemplates.GroupTypeItem)e.nextElement(); 
	    if(key != null){
		cv = key.getCharacterValue();
		if(cv != null){
		    if(cv.getRank()== characterRank){
			found = true;
			break;
		    }
		}
	    }
	}
	if(found){return cv;}
	return null;

    }
    private String createGroup(  project.efg.templates.taxonPageTemplates.GroupsType groups,
			    String key, 
			    String text){
	/*
	  group: (groupid:grouprank)
	  groupstuff:  (group:characterrank)
	  characterstuff: (characterlabel:groupstuff)
	  grouplabelstuff: (grouplabel:group)

	  groupid: number
	  characterrank : number
	  grouprank: number
	  grouplabel: gl
	  characterlabel:cl
	  
	  group id:group rank:character rank | character label: ( 
	  cl:1:1:1 
	  gl:2:2
	  group:3:9:1
	*/

	String returnValue = null;
	String identifier="";
	String groupID="";
	String groupRank="";
	String characterRank="";
	
	String[] splits = key.split(":");
	if(splits.length == 0){
	    return null;
	}
	identifier = splits[0];
	if(splits.length > 1){
	    groupID = splits[1];
	}
	if(splits.length > 2){
	    groupRank = splits[2];
	}
	if(splits.length > 3){
	    characterRank = splits[3];
	}
	if("".equals(groupID)){
	    return null;
	}
	if("".equals(groupRank)){
	    return null;
	}

	//if same rank and id already exists return it
	project.efg.templates.taxonPageTemplates.GroupType group = 
	    checkGroup(groups,groupID,Integer.parseInt(groupRank));
	    

	if(("gl").equalsIgnoreCase(identifier.trim())){//group label
	    if((text != null)  || (!text.trim().equals(""))){
		group.setLabel(text);
	    }
	}
	else {
	    if(("".equals(characterRank)) ||(characterRank == null)){
		return null;
	    }
	    if((text == null) || (text.trim().equals(""))){
		return null;
	    }
	    project.efg.templates.taxonPageTemplates.CharacterValue cv =null;
	    cv = findCharacter(group,Integer.parseInt(characterRank));
	    if(cv == null){
		project.efg.templates.taxonPageTemplates.GroupTypeItem vGroupTypeItem
		    = new project.efg.templates.taxonPageTemplates.GroupTypeItem();	
		cv = 
		    new project.efg.templates.taxonPageTemplates.CharacterValue();
		cv.setRank(Integer.parseInt(characterRank));
		vGroupTypeItem.setCharacterValue(cv);
		group.addGroupTypeItem(vGroupTypeItem);
	    }
	    if(("cl").equalsIgnoreCase(identifier.trim()) ||
	       (("tl").equalsIgnoreCase(identifier.trim()))){
		
	
		if(("cl").equalsIgnoreCase(identifier.trim())){//character label
		    cv.setLabel(text);
		}
		else{
		    cv.setText(text); //character text
		}
	    }
	    else if(("group").equalsIgnoreCase(identifier.trim())){//group
		if(text != null){
		    if(!text.trim().equals("")){
			if(returnValue == null){
			    returnValue = text;
			}
		    }
		}
		cv.setValue(text);
	    }
	}
	return returnValue;
    }
}
//$Log$
//Revision 1.1  2006/01/25 21:03:48  kasiedu
//Initial revision
//















