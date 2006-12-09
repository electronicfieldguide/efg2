<%@page import="project.efg.util.EFGImportConstants" %>
<!-- $Id$ -->
<% 
String guid =  (String)request.getAttribute(EFGImportConstants.GUID); 
String uniqueName = (String)request.getAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME); 
String displayName = (String)request.getAttribute(EFGImportConstants.DISPLAY_NAME); 
String datasourceName =(String)request.getAttribute(EFGImportConstants.DATASOURCE_NAME);

if(datasourceName == null){
	datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);

}

if(displayName == null){
	displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME);
}
if(uniqueName == null){
	uniqueName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME); 
}
if(guid == null){
	guid = request.getParameter(EFGImportConstants.GUID); 
}

String whichDatabase = request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
if(whichDatabase == null){
	whichDatabase = (String)request.getAttribute(EFGImportConstants.ALL_TABLE_NAME); 
}

if(whichDatabase == null || whichDatabase.trim().equals("")){
	whichDatabase =EFGImportConstants.EFG_RDB_TABLES;
}



%>
