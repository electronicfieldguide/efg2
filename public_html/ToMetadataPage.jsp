<%@page import="java.util.Iterator, java.net.URLEncoder, project.efg.util.*,project.efg.efgInterface.*" %>
<jsp:useBean id="dsHelperFactory" class="project.efg.efgInterface.EFGDSHelperFactory" 
  scope="page" />
<% 
   String context = request.getContextPath();
  boolean found = false;
%>
<html>
  <head>
    <title>Edit Metadata</title>
  </head>
  <body bgcolor="#ffffff">
   	<h2 align="center">Edit Metadata</h2>
  	<center>
      	<% RDBDataSourceHelper dsHelper = (RDBDataSourceHelper)dsHelperFactory.getDataSourceHelper(); %>
      	<% Iterator dsNameIter = dsHelper.getDSNames().iterator(); %>
      	<table>
		  <% while (dsNameIter.hasNext()) { %>
		 	 <tr>
			 	<td>
					<% String dsName = (String)dsNameIter.next();%> 
			 		<a  title="Edit Metadata" href="<%=context%>/MetadataManager.jsp?dataSourceName=<%=URLEncoder.encode(dsName)%>">
						<%= dsHelper.makeReadable(dsName) %>
					</a>
				</td> 
			</tr>
		 <%}%>
      	</table>
  	</center>
  </body>
</html>
