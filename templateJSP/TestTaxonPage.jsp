<%@page import="project.efg.util.EFGImportConstants,project.efg.util.EFGMediaResourceSearchableObject,project.efg.servlets.factory.ServletAbstractFactoryImpl,project.efg.servlets.efgInterface.ServletAbstractFactoryInterface"%>
<html>
  <head>
  <%
	String context = request.getContextPath();
	String guid =  (String)request.getAttribute(EFGImportConstants.GUID);
	String xslName = (String)request.getAttribute(EFGImportConstants.XSL_STRING);
	String uniqueName	= (String)request.getAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
	if(uniqueName == null){
		uniqueName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
	} 
	if(xslName == null){
		xslName = request.getParameter(EFGImportConstants.XSL_STRING);
	} 
	String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME);
   if((displayName == null) || (displayName.trim().equals(""))){
		displayName = (String)request.getAttribute(EFGImportConstants.DISPLAY_NAME_COL); 
	}
	String searchType = (String)request.getAttribute(EFGImportConstants.SEARCH_TYPE_STR);
    String templateMatch = request.getParameter(EFGImportConstants.HTML_TEMPLATE_NAME);
	
	String fieldName = (String)request.getAttribute("fieldName");
	String fieldValue = (String)request.getAttribute("fieldValue");
    ServletAbstractFactoryInterface servFact = new ServletAbstractFactoryImpl();
     EFGMediaResourceSearchableObject  searchO = servFact.getFirstField(displayName, dsName) ;
	String fieldToUse = null;
	if(searchO != null){
		fieldToUse  = searchO.getSearchableField();
	}
	 if((fieldToUse != null) && (!fieldToUse.trim().equals(""))){
			 fieldValue=fieldToUse;
	}
		
	StringBuffer querySearch = new StringBuffer();
	querySearch.append(context);
	querySearch.append("/search?dataSourceName=");
	querySearch.append(dsName);
	querySearch.append("&");
	querySearch.append(fieldValue);
	querySearch.append("=");
	querySearch.append(EFGImportConstants.EFG_ANY);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.GUID);
	querySearch.append("=");
	querySearch.append(guid);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.XSL_STRING);
	querySearch.append("=");
	querySearch.append(xslName);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.DISPLAY_FORMAT);
	querySearch.append("=");
	querySearch.append(EFGImportConstants.HTML);

	querySearch.append("&");
	querySearch.append(EFGImportConstants.MAX_DISPLAY);
	querySearch.append("=1");
	querySearch.append("&");
	querySearch.append(EFGImportConstants.SEARCH_TYPE_STR);
	querySearch.append("=");
	querySearch.append(EFGImportConstants.SEARCH_TAXON_TYPE);
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
