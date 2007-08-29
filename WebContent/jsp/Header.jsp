<%@page import="project.efg.util.interfaces.EFGImportConstants" %>
<%
 	String context = request.getContextPath();

 	String mainTable = EFGImportConstants.EFG_RDB_TABLES;
 	String ALL_TABLE_NAME = EFGImportConstants.ALL_TABLE_NAME;
 		
 	String mainTableName = request.getParameter(ALL_TABLE_NAME);
 	if(mainTableName == null || mainTableName.trim().equals("")){
 	  mainTableName = mainTable;
 	}
  	StringBuffer mainTableConstant =new StringBuffer(ALL_TABLE_NAME + "=" + mainTable);		   
 	String linksFile = context+ "/Links.html"; 
 	String typePageFile =context +  "/TypePage.jsp?"+ mainTableConstant.toString();
%>
