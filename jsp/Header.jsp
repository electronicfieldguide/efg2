<%@page import="project.efg.util.EFGImportConstants" %>
<% 
 	String mainTable = EFGImportConstants.EFG_RDB_TABLES;
 	String ALL_TABLE_NAME = EFGImportConstants.ALL_TABLE_NAME;
 		
 	String mainTableName = request.getParameter(ALL_TABLE_NAME);
 	if(mainTableName == null || mainTableName.trim().equals("")){
 			  mainTableName = mainTable;
 	}
 	StringBuffer mainTableConstant =new StringBuffer(ALL_TABLE_NAME + "=" + mainTableName);		   
 	String linksFile = "Links.html"; 
 	String typePageFile = "TypePage.jsp?"+ mainTableConstant.toString();
%>
