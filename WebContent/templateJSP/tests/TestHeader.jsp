<%@page import="project.efg.util.interfaces.EFGImportConstants"%>


<%@ include file="../commonTemplateCode/allTableName.jsp"%>
<%
	
	
	String xslName = (String)request.getAttribute(EFGImportConstants.XSL_STRING);
	
	String alldbname = (String)request.getAttribute(EFGImportConstants.ALL_TABLE_NAME);
	

	if(xslName == null){
		xslName = request.getParameter(EFGImportConstants.XSL_STRING);
	} 
	String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME);
   String fieldName = (String)request.getAttribute("fieldName");
	String fieldValue = (String)request.getAttribute("fieldValue");
	
	String searchType = (String)request.getAttribute(EFGImportConstants.SEARCH_TYPE_STR);
    String templateMatch = request.getParameter(EFGImportConstants.HTML_TEMPLATE_NAME);

%>