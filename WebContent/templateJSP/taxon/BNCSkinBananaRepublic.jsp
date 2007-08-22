<%@page import="
java.io.File,
java.util.List,
java.util.ArrayList,
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
		<%@ include file="../commonTemplateCode/allTableName.jsp"%>
		<%
			String templateMatch ="Taxon Page - Banana Republic Style";
			String cssFile ="bnc_bananarepub.css";
			String xslFileName = "/taxon/BNCStyleTaxonPage.xsl";
	    %>
		<%@ include file="NantucketNavNecForestHeader.jsp"%>
	</head>
	<%@include file="NantucketNavNecForestCommon.jsp" %>
</html>

