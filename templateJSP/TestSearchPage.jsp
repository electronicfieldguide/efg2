<%@page import="project.efg.util.EFGImportConstants,java.io.File,project.efg.util.TemplateMapObjectHandler,project.efg.util.TemplateObject,project.efg.util.EFGDisplayObject" %>
<html>
  <head>
  <%@ include file="TestHeader.jsp" %>
  
  <%
	StringBuffer querySearch = new StringBuffer();
	String testDisp = "=10";
	String maxDisp = "=100";
	StringBuffer testString = new StringBuffer();
	StringBuffer keyString = new StringBuffer();
	querySearch.append(context);
	querySearch.append("/search?dataSourceName=");
	querySearch.append(dsName);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.GUID);
	querySearch.append("=");
	querySearch.append(guid);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.XSL_STRING);
	querySearch.append("=");
	querySearch.append(xslName);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.ALL_TABLE_NAME);
	querySearch.append("=");
	querySearch.append(alldbname);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.SEARCH_TYPE_STR);
	querySearch.append("=");
	querySearch.append(searchType);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.DISPLAY_FORMAT);
	querySearch.append("=");
	querySearch.append(EFGImportConstants.HTML);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.MAX_DISPLAY);
	testString.append(querySearch.toString());
	testString.append(testDisp);
	if(guid != null){
		keyString.append(querySearch.toString());
		keyString.append(maxDisp);
		String key = keyString.toString();		
		TemplateObject templateObject = new TemplateObject();
		EFGDisplayObject displayObject = new EFGDisplayObject();
		displayObject.setDisplayName(displayName);
		displayObject.setDatasourceName(dsName);
		
		templateObject.setTemplateName(uniqueName);
		templateObject.setGUID(guid);
		templateObject.setDisplayObject(displayObject);
		TemplateMapObjectHandler.add2TemplateMap(key,templateObject,null);
	}
   %>
	<title>Test Search Page Configuration</title>
  </head>
  <body bgcolor="#ffffff">
  	<center>
			<h3>The Datasource "<%=displayName%>" has been configured successfully!!!.</h3>
			<p align="center"><a href="javascript:history.back()">Go back to configuration Page</a></p><br/><br/>
			<p align="center"> <a href="<%=testString.toString()%>"  target="TestWindow">Test Search Page</a></p><br/><br/>
  	</center>
  </body>
</html>
