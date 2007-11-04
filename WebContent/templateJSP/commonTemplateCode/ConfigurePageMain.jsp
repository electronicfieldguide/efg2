<%@page import="java.util.Iterator,
project.efg.server.interfaces.ServletAbstractFactoryInterface,
project.efg.server.utils.EFGDisplayObjectList,
project.efg.util.utils.EFGDisplayObject
" %>
<%@page import="project.efg.server.factory.EFGSpringFactory"%>
<!-- $Id$ -->
<%@ include file="allTableName.jsp"%>
<%
   String context = request.getContextPath();
%>

<html>
  <head>
    <title>Configure a Datasource</title>
  </head>
  <body bgcolor="#ffffff">
    <h2 align="center">Configure a Datasource</h2>
  	<center>
  	<%if(isEmpty){%>
  		<form name="configure" action="<%=ConfigurePageResponsePage%>">
  		<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
			<%@ include file="ConfigurePageCommon.jsp" %>
    	</form>
    	<%} else {%>
    		
    		<%@ include file="ConfigurePageFooter.jsp" %>
    		
    	<%}%>
  	</center>
  </body>
</html>