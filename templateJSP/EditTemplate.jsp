<%@page import="
project.efg.util.EFGImportConstants,
project.efg.util.TemplateMapObjectHandler,
project.efg.util.TemplateConfigProcessor
" %>
<%@ include file="allTableName.jsp"%>
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



