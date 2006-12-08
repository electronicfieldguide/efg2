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
			String templateMatch ="Plate Template1";
			String xslFileName = "nantucketCommonNameSearchPlate.xsl";
		%>		
		<title>Nantucket Search Plate Template</title>
		
		<%@ include file="commonNameHeader.jsp" %>
		