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

	<%@ include file="../commonTemplateCode/jspName.jsp" %>
	
		<%
			String templateMatch ="Search Results - Basic Thumbnail View";
			String xslFileName = "/searchresults/plates/SearchResultsTemplateBasic.xsl";
			String cssFile ="nantucketstyleplates.css";
		%>		
		<title><%=templateMatch%></title>
		
		<%@ include file="../commonTemplateCode/commonNameHeader.jsp" %>
		