<%@page import="
project.efg.util.interfaces.EFGImportConstants,
project.efg.util.utils.TemplateMapObjectHandler,
project.efg.server.utils.TemplateConfigProcessor
" %>
<%@page import="java.util.Map"%>
<%@page import="project.efg.server.servlets.EFGContextListener"%>
<%@ include file="commonTemplateCode/allTableName.jsp"%>
<% 
    if(datasourceName == null){
		datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	}

    //make me dynamic
    String configType = request.getParameter(EFGImportConstants.SEARCH_TYPE_STR);
     
	TemplateConfigProcessor tcp = new TemplateConfigProcessor(datasourceName,configType);
	String forwardPage = tcp.getJspPage(guid);
	
	//set attributes
	request.setAttribute(EFGImportConstants.GUID,guid); 
	request.setAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME,uniqueName); 
	request.setAttribute(EFGImportConstants.DISPLAY_NAME,displayName); 
	request.setAttribute(EFGImportConstants.DATASOURCE_NAME,datasourceName);
	
	
	
	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
	dispatcher.forward(request, response);
 	
  %>



