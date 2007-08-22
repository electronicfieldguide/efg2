<%@page import="
project.efg.util.interfaces.EFGImportConstants,
project.efg.util.utils.TemplateMapObjectHandler,
project.efg.server.utils.TemplateConfigProcessor
" %>
<%@ include file="commonTemplateCode/allTableName.jsp"%>
<% 
	
	String context = request.getContextPath();
	String configType = request.getParameter(EFGImportConstants.SEARCH_TYPE_STR);
    String message = "";
     if(datasourceName == null){
    	datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	}
    //make me dynamic
	TemplateConfigProcessor tcp = new TemplateConfigProcessor(datasourceName,configType);
	 boolean bool = tcp.removeAConfig(guid);
  	if(bool){
		bool= TemplateMapObjectHandler.removeGuidFromTemplateMap(guid,null);
	}
  %>
<html>
	<head>
		<title>Delete Message</title>
	</head>
	<body>	
	
		<% 
		if(bool){
			message = uniqueName + " deleted successfully";
		%>
			<p id="successDeleteID">
				<%=message%>
			</p>
		<%} else {
			message = "Unable to delete " + uniqueName;
		%>
			<p id="failureDeleteID">
				<%=message%>
			</p>		
		<%} %>		
		<p><a href="/efg2/DirectURLs2SearchResultsPages.jsp">View URLs for all EFGs</a></p>
	</body>	
</html>


