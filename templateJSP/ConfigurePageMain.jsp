<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.ServletAbstractFactoryInterface,
project.efg.servlets.factory.ServletAbstractFactoryCreator,
project.efg.util.EFGDisplayObjectList,
project.efg.util.EFGDisplayObject
" %>
<!-- $Id$ -->
<%@ include file="allTableName.jsp"%>
<% 
   String context = request.getContextPath();
   ServletAbstractFactoryInterface servFactory = ServletAbstractFactoryCreator.getInstance();
   servFactory.setMainDataTableName(whichDatabase);	
   EFGDisplayObjectList listInter = servFactory.getListOfDatasources();	
	boolean isEmpty = true;
	if(listInter != null){
		if(listInter.getCount() < 1){
		isEmpty = false;
		}
	}
	else{
		isEmpty = false;
	}
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