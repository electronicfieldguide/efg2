<%@page import="project.efg.util.EFGImportConstants"%>
<html>
  <head>
  <%
	String context = request.getContextPath();
	String xslFileName = request.getParameter(EFGImportConstants.XSL_STRING);
	String dsName = (String)request.getAttribute(EFGImportConstants.DATASOURCE_NAME);
	String displayName = (String)request.getAttribute(EFGImportConstants.DISPLAY_NAME_COL);
	String fieldName = (String)request.getAttribute("fieldName");
	String fieldValue = (String)request.getAttribute("fieldValue");
	StringBuffer querySearch = new StringBuffer();
	querySearch.append(context);
	querySearch.append("/search?dataSourceName=");
	querySearch.append(dsName);
	querySearch.append("&");
	querySearch.append(fieldValue);
	querySearch.append("=");
	querySearch.append(EFGImportConstants.EFG_ANY);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.XSL_STRING);
	querySearch.append("=");
	querySearch.append(xslFileName);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.DISPLAY_FORMAT);
	querySearch.append("=");
	querySearch.append(EFGImportConstants.HTML);
	
	
	querySearch.append("&maxDisplay=1");
	
   %>
	<title>Test Taxon Page Configuration</title>
  </head>
  <body bgcolor="#ffffff">
  	<center>
		<% if(fieldName != null){%>
			<H3>The Datasource "<%=displayName%>" has been configured successfully!!!.</H3>
			<p align="center"><a href="javascript:history.back()">Go back to configuration Page<a></p><br/><br/>
			<p align="center"> <a href="<%=querySearch.toString()%>"  target="TestWindow">Test taxon Page<a></p><br/><br/>
		<%} else { %>
			<H3>The Datasource "<%=dsName%>" could not be configured!!!!.</H3>
			<p align="center"><a href="javascript:history.back()">Go back to configuration Page<a></p><br/><br/>
		<%}%>
  	</center>
  </body>
</html>
