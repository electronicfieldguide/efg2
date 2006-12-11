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
		String xslFileName = "GlossaryList.xsl";
		
        String templateMatch ="Glossary List";
        
		%>		
		<%@ include file="GlossaryHeader.jsp" %>