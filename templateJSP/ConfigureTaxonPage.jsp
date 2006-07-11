<%@page import="java.util.Iterator, java.net.URLEncoder, project.efg.util.*,project.efg.efgInterface.*" %>
<jsp:useBean id="dsHelperFactory" class="project.efg.efgInterface.EFGDSHelperFactory" 
  scope="page" />
<% 
   String context = request.getContextPath();
%>

<html>
  <head>
    <title>Configure a datasource</title>
  </head>

  <body bgcolor="#ffffff">
    <h2 align="center">Configure a datasource</h2>
  <center>
      <% EFGDataSourceHelper dsHelper = dsHelperFactory.getDataSourceHelper(); %>
      <% Iterator dsNameIter = dsHelper.getDSNames().iterator(); %>
      <table>
		  <% while (dsNameIter.hasNext()) { %>
		  <tr>
			 <td>
				<% String dsName = (String)dsNameIter.next(); %>
				<a  title="click to configure datasource" href="<%=context%>/TaxonPageTemplate.jsp?dataSourceName=<%=URLEncoder.encode(dsName)%>">
				<%= dsHelper.makeReadable(dsName) %>
				 </a>
			 </td>
		</tr>
		<br/><br/>
		  <% 
		} %>
      </table>

  </center>
  </body>
</html>
