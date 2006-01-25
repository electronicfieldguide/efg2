<%@page import="java.util.*,java.io.*,project.efg.util.*" %>
<html>
  <head>
  <%
	String context = request.getContextPath();
	String dsName = (String)request.getAttribute("dsName");
	String fieldName = (String)request.getAttribute("fieldName");
	StringBuffer querySearch = new StringBuffer();
	querySearch.append(context);
	querySearch.append("/search?dataSourceName=");
	querySearch.append(dsName);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.WILDCARD_STR);
	querySearch.append("=");
	querySearch.append(fieldName);
	querySearch.append("&maxDisplay=1");
	
   %>
	<title>Test Taxon Page Configuration</title>
  </head>
  <body bgcolor="#ffffff">
  	<center>
		<% if(fieldName != null){%>
			<H3>The Datasource "<%=dsName%>" has been configured successfully!!!.</H3>
			<p align="center"><a href="javascript:history.back()">Go back to configuration Page<a></p><br/><br/>
			<p align="center"> <a href="<%=querySearch.toString()%>"  target="TestWindow">Test taxon Page<a></p><br/><br/>
		<%} else { %>
			<H3>The Datasource "<%=dsName%>" could not be configured!!!!.</H3>
			<p align="center"><a href="javascript:history.back()">Go back to configuration Page<a></p><br/><br/>
		<%}%>
  	</center>
  </body>
</html>
