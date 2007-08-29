<%@page import="project.efg.util.interfaces.EFGImportConstants" %>	<head>	<%@ include file="../Header.jsp"%>	      <%
      StringBuffer title =new StringBuffer();
      if(EFGImportConstants.EFG_GLOSSARY_TABLES.equalsIgnoreCase(mainTableName)){
    	  title.append("There are not yet any glossary files to configure.\n");
    	  title.append("Please go back and select another option.");
      }      else{
    	  title.append("The selected datasource no longer exists or the database is empty!!");
      }      	      %>			<title>No Data source</title>	<link rel="stylesheet" href="<%=context%>/efg2web.css" type="text/css"/>  </head>  <body>	  	<%@ include file="../EFGTableHeader.jsp"%>		  	<table class="frame" summary="">				<tr>					<td>						<table class="directurl" summary="">					 							<tr>								<td colspan="5" class="title"><br/>									<span class="subtitle"><%=title.toString()%></span>								</td>							</tr>							<tr>						</table>					</td>				</tr>		</table>		  	  </body></html> 