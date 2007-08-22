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
		String xslFileName = "/searchresults/lists/ListViewTemplateItalRight.xsl";
		
        String templateMatch ="List View - Italics Right";
        String cssFile = "nantucketstyle.css";
		%>		
		<title><%=templateMatch%></title>
		<%@ include file="../commonTemplateCode/commonNameHeader.jsp" %>