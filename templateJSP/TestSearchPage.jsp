<%@page import="project.efg.util.EFGImportConstants,project.efg.util.TemplateMapObjectHandler,project.efg.util.TemplateObject,project.efg.util.EFGDisplayObject" %>
<html>
  <head>
  <%
	String context = request.getContextPath();
	String xslFileName = request.getParameter(EFGImportConstants.XSL_STRING);
	String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME);
   if((displayName == null) || (displayName.trim().equals(""))){
				displayName = (String)request.getAttribute(EFGImportConstants.DISPLAY_NAME_COL); 
	}
	String fieldName = (String)request.getAttribute("fieldName");
	String fieldValue = (String)request.getAttribute("fieldValue");
	String searchType = (String)request.getAttribute(EFGImportConstants.SEARCH_TYPE_STR);
    String templateMatch = request.getParameter(EFGImportConstants.HTML_TEMPLATE_NAME);
	StringBuffer querySearch = new StringBuffer();
	querySearch.append(context);
	querySearch.append("/search?dataSourceName=");
	querySearch.append(dsName);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.XSL_STRING);
	querySearch.append("=");
	querySearch.append(xslFileName);
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
	querySearch.append("=100000");
	
	if(xslFileName != null){
		String key = querySearch.toString();
		TemplateObject templateObject = new TemplateObject();
		EFGDisplayObject displayObject = new EFGDisplayObject();
		displayObject.setDisplayName(displayName);
		displayObject.setDatasourceName(dsName);
		templateObject.setTemplateName(templateMatch);
		templateObject.setDisplayObject(displayObject);
		TemplateMapObjectHandler.add2TemplateMap(key,templateObject);
	}
   %>
	<title>Test Search Page Configuration</title>
  </head>
  <body bgcolor="#ffffff">
  	<center>
		
			<h3>The Datasource "<%=displayName%>" has been configured successfully!!!.</h3>
			<p align="center"><a href="javascript:history.back()">Go back to configuration Page<a></p><br/><br/>
			<p align="center"> <a href="<%=querySearch.toString()%>"  target="TestWindow">Test Search Page<a></p><br/><br/>
  	</center>
  </body>
</html>
