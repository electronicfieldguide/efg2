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
			String templateMatch ="Common Name First - Nantucket Nectarz Style";
			String xslFileName = "/searchresults/lists/NantucketListItalRight.xsl";
			String cssFile ="nantucket.css";
		%>		
		<title><%=templateMatch%></title>
		<%@ include file="../commonTemplateCode/commonNameHeader.jsp" %>
	</head>	
	<%@ include file="NantucketStyleOnlyItalRightCommon.jsp"%>
</html>		