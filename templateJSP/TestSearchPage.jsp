<%@page import="java.util.*,java.io.*,project.efg.util.*" %>
<html>
  <head>
  <%
	String context = request.getContextPath();
	String xslFileName = request.getParameter(EFGImportConstants.XSL_STRING);
	String dsName = (String)request.getAttribute(EFGImportConstants.DATASOURCE_NAME);
	String displayName = (String)request.getAttribute(EFGImportConstants.DISPLAY_NAME_COL);
	String fieldName = (String)request.getAttribute("fieldName");
	String fieldValue = (String)request.getAttribute("fieldValue");
	String searchType = (String)request.getAttribute(EFGImportConstants.SEARCH_TYPE_STR);

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
	
   %>
	<title>Test Search Page Configuration</title>
  </head>
  <body bgcolor="#ffffff">
  	<center>
		<% if(fieldName != null){
				
			%>
			<H3>The Datasource "<%=displayName%>" has been configured successfully!!!.</H3>
			<p align="center"><a href="javascript:history.back()">Go back to configuration Page<a></p><br/><br/>
			<p align="center"> <a href="<%=querySearch.toString()%>"  target="TestWindow">Test Search Page<a></p><br/><br/>
		<%} else { %>
			<H3>The Datasource "<%=dsName%>" could not be configured!!!!.</H3>
			<p align="center"><a href="javascript:history.back()">Go back to configuration Page and fix any errors!!<a></p><br/><br/>
		<%}%>
  	</center>
  </body>
</html>
