<%@page import="project.efg.util.EFGImportConstants" %>
<!-- $Id$ -->
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

