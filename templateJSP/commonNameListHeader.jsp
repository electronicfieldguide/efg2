<%@page import="
java.io.File,
java.util.List,
java.util.Iterator,
java.util.Hashtable,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.util.EFGImportConstants,
project.efg.util.TemplateProducer,
project.efg.util.TemplatePopulator,
project.efg.Imports.efgInterface.EFGQueueObjectInterface
" %>

	<%@ include file="jspName.jsp" %>
	
		<%
		String xslFileName = "nantucketCommonNameSearchList.xsl";
		
        String templateMatch ="List Template2";
        
		%>		
		<title>Nantucket Search List Template</title>
		<%@ include file="commonNameHeader.jsp" %>