<%@page import="java.util.Iterator"%>
<%@page import="project.efg.util.interfaces.EFGImportConstants" %>
<!-- $Id: allTableName.jsp,v 1.1.1.1 2007/08/01 19:11:39 kasiedu Exp $ -->

<%@page import="project.efg.server.servlets.EFGContextListener"%>
<%@page import="java.util.Map"%>
<%@page import="project.efg.server.utils.ServletCacheManager"%>
<%@ include file="ConstantLinks.jsp"%>
<% 

String guid =  (String)request.getAttribute(EFGImportConstants.GUID); 
String uniqueName = (String)request.getAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME); 
String displayName = (String)request.getAttribute(EFGImportConstants.DISPLAY_NAME); 
String datasourceName =(String)request.getAttribute(EFGImportConstants.DATASOURCE_NAME);
boolean isGlossary = false;
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
if(EFGImportConstants.EFG_GLOSSARY_TABLES.equalsIgnoreCase(whichDatabase)){
	isGlossary = true;
}
Map map = null;
boolean isEmpty = true;
Iterator listInter = null;
map = ServletCacheManager.getDatasourceCache(whichDatabase);
if(datasourceName != null){
	if(displayName == null || "".equals(displayName)){
		displayName = (String)map.get(datasourceName.toLowerCase());
	}
}

if(map != null){
	listInter = map.keySet().iterator();
	if(listInter != null){
		if(map.keySet().size() < 1){
			isEmpty = false;
		}
	}
	else{
		isEmpty = false;
	}
}
%>
