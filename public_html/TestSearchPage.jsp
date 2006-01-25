<%@page import="java.util.*,java.io.*,project.efg.util.*" %>
<html>
  <head>
  <%
	String context = request.getContextPath();
	String dsName = (String)request.getAttribute("dsName");
	String searchType = (String)request.getAttribute(EFGImportConstants.SEARCH_TYPE_STR);
	String fieldName = (String)request.getAttribute("fieldName");

	StringBuffer querySearch = new StringBuffer();
	querySearch.append(context);
	querySearch.append("/search?dataSourceName=");
	querySearch.append(dsName);
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
				if(dsName != null){
					EFGServletUtils.configuredDatasources.add(dsName.toLowerCase().trim());
				}
			%>
			<H3>The Datasource "<%=dsName%>" has been configured successfully!!!.</H3>
			<p align="center"><a href="javascript:history.back()">Go back to configuration Page<a></p><br/><br/>
			<p align="center"> <a href="<%=querySearch.toString()%>"  target="TestWindow">Test Search Page<a></p><br/><br/>
		<%} else { %>
			<H3>The Datasource "<%=dsName%>" could not be configured!!!!.</H3>
			<p align="center"><a href="javascript:history.back()">Go back to configuration Page and fix any errors!!<a></p><br/><br/>
		<%}%>
  	</center>
  </body>
</html>
