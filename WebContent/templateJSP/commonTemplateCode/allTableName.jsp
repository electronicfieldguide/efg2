<%@page import="java.util.SortedMap"%>
<%@page import="java.util.TreeMap"%>
<%@page import="project.efg.util.utils.CaseInsensitiveComparatorImpl"%>
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
Map map1 = null;
boolean isEmpty = true;
Iterator listInter = null;
map1 = ServletCacheManager.getDatasourceCache(whichDatabase);
if(datasourceName != null){
	if(displayName == null || "".equals(displayName)){
		displayName = (String)map1.get(datasourceName.toLowerCase());
	}
}
TreeMap map = new TreeMap(new CaseInsensitiveComparatorImpl());
if(map1 != null){
	listInter = map1.keySet().iterator();
	if(listInter != null){
		if(map1.keySet().size() < 1){
			isEmpty = false;
		}
		while (listInter.hasNext()) { 
			String dsourceName = (String)listInter.next();
			String dplayName =(String)map1.get(dsourceName.toLowerCase());
			map.put(dplayName,dsourceName);
		}
		listInter = map.keySet().iterator();
	}
	else{
		isEmpty = false;
	}
}
%>
