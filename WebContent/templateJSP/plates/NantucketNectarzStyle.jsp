<%@page import="
java.io.File,
java.util.List,
java.util.Iterator,
java.util.Hashtable,
project.efg.server.utils.EFGDataSourceHelperInterface,
project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.TemplateProducer,
project.efg.server.utils.TemplatePopulator,
project.efg.util.interfaces.EFGQueueObjectInterface
" %>
<html>
	<head>
		<%@ include file="../commonTemplateCode/jspName.jsp" %>	
		<%
			String templateMatch ="Search Results - Nantucket Nectarz Style";
			String xslFileName = "/searchresults/plates/SearchResultsSkin.xsl";
			String cssFile ="nantucket_nectarz.css";
			String bodyclass="about";
		%>		
		<title><%=templateMatch%></title>
		<%@ include file="../commonTemplateCode/commonNameHeader.jsp" %>
	</head>	
	<%@ include file="SearchResultsCommonSkin.jsp"%>
</html>		