<%@page import="java.util.Iterator, java.net.URLEncoder, project.efg.util.*,project.efg.efgInterface.*" %>
<jsp:useBean id="dsHelperFactory" class="project.efg.efgInterface.EFGDSHelperFactory" 
  scope="page" />
<% 
   String context = request.getContextPath();
	boolean found = false;
%>

<html>
  <head>
  
  </head>

  <body bgcolor="#ffffff">
    <h2 align="center">Click to view Template..</h2>
  <center>
      <% EFGDataSourceHelper dsHelper = dsHelperFactory.getDataSourceHelper(); %>
      <% Iterator dsNameIter = dsHelper.getDSNames().iterator(); %>
      <table>
			  <tr>
					  <td> 	 <a target="_blank" href="http://orca.cs.umb.edu:8080/efg/taxondisplay?uniqueID=a192.168.104.1151136995870433&isTaxon=true">Jenn's Template  Sample</a></td>
					  <td> 	 <a target="_blank" href="MarshaSpeciesPageData.html">Marsha's Template Sample </a></td>
					  <td> 	 <a target="_blank" href="http://alpaca.cs.umb.edu/efg/taxondisplay?uniqueID=a158.121.95.1511127484375171&isTaxon=true">Bill's Template Sample </a></td>
			  </tr>
			<tr/>
			<tr/>
			<tr/>
		  <% while (dsNameIter.hasNext()) { %>
		  		  <tr>

			 <td>
				<% String dsName = (String)dsNameIter.next();
						if(!EFGServletUtils.configuredDatasources.contains(dsName.toLowerCase().trim())){//show only configured datasources
							continue; //skip it
						}
						else{
							found = true;
						}
						
				 %>
				<%= dsHelper.makeReadable(dsName)%>
				 </td> 
			
				<td>
			 	 <a  title="search datasource" href="<%=context%>/BillTemplate.jsp?dataSourceName=<%=URLEncoder.encode(dsName)%>">
					Bill's Template
				 </a>
				  </td> 
				<td>
			 	 <a  title="search datasource" href="<%=context%>/marshaTemplate.jsp?dataSourceName=<%=URLEncoder.encode(dsName)%>">
					Marsha's Template
				 </a>
				  </td> 
				<td>
			 	 <a  title="search datasource" href="<%=context%>/jennTemplate.jsp?dataSourceName=<%=URLEncoder.encode(dsName)%>">
					Jenn's Template
				 </a>
				  </td> 
					<td>
			 	 <a  title="search datasource" href="<%=context%>/nanTemplate.jsp?dataSourceName=<%=URLEncoder.encode(dsName)%>">
					Nantucket Template
				 </a>
				  </td> 



		</tr>
		<br/><br/>
		  <% 
		}
			if(!found){%>
			<h3> No datasources configured by author(s)</h3>
		<%}
		 %>
      </table>

  </center>
  </body>
</html>
