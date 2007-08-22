<%@page import="project.efg.util.interfaces.EFGImportConstants" %>
<!-- $Id: commonConfigureJSPCode.jsp,v 1.1.1.1 2007/08/01 19:11:39 kasiedu Exp $ -->
<% 
   String context = request.getContextPath();
   String dsName = (String)request.getAttribute(EFGImportConstants.DATASOURCE_NAME);
  
   String templateName = (String)request.getAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
   
	if(dsName == null){
		dsName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	}
	
	if(templateName == null){
		templateName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME); 
	}
%>	
<%@ include file="allTableName.jsp"%>

