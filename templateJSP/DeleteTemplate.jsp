<%@page import="
project.efg.util.EFGImportConstants,
project.efg.util.TemplateMapObjectHandler,
project.efg.util.TemplateConfigProcessor
" %>
<%@ include file="allTableName.jsp"%>
<% 
	
	String context = request.getContextPath();
	String configType = request.getParameter(EFGImportConstants.SEARCH_TYPE_STR);
    String title = uniqueName;
    if(datasourceName == null){
		datasourceName =request.getParameter("dataSourceName");
	}
    //make me dynamic
	TemplateConfigProcessor tcp = new TemplateConfigProcessor(datasourceName,configType);
	boolean bool= TemplateMapObjectHandler.removeGuidFromTemplateMap(guid,null);
	if(bool){
		bool = tcp.removeAConfig(guid);
	}
	
	
	if(bool){
		title = title + " deleted successfully";
	}
 	else{
 		title = "Unable to delete " + title;
 	}
 	
  %>
<html>
	<head>
		<title><%=title%><title>
	</head>
	<body>
		<p><%=title%></p>
		<p><a href="/efg2/DirectURLs2SearchResultsPages.jsp">View URLs for all EFGs</a></p>
	</body>
	
</html>


