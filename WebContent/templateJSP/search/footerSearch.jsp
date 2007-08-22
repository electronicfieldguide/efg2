<%@page import="project.efg.util.interfaces.EFGImportConstants" %>
<!-- $Id: footerSearch.jsp,v 1.1.1.1 2007/08/01 19:12:28 kasiedu Exp $ -->
		<input type="hidden"   name="<%=EFGImportConstants.SEARCH_PAGE_STR%>"  value="<%=EFGImportConstants.SEARCH_PAGE_STR%>"/>
		<input type="hidden"   name="<%=EFGImportConstants.SEARCH_TYPE_STR%>"  value="<%=EFGImportConstants.SEARCH_SEARCH_TYPE%>"/>
	
	<%@ include file="../commonTemplateCode/commonJspTemplateCodeFooter.jsp" %>
	