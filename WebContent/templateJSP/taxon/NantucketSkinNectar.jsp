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
		String xslFileName = "/taxon/NantucketSkinForNavNecTaxonPage.xsl";

			String templateMatch ="Taxon Page - Nantucket Nectarz Style";
			String cssFile ="nantucket_nectarz.css";
	    %>
		<%@ include file="NantucketNavNecForestHeader.jsp"%>
	</head>
	<%@include file="NantucketNavNecForestCommon.jsp" %>
</html>

