<%@page import="java.util.Iterator, java.net.URLEncoder, project.efg.util.*,project.efg.efgInterface.*" %>
<jsp:useBean id="dsHelperFactory" class="project.efg.efgInterface.EFGDSHelperFactory" 
  scope="page" />
<% 
   String context = request.getContextPath();
	boolean found = false;
%>

<html>
  <head>
    <title>Search/Browse EFG Datasources</title>
  </head>

  <body bgcolor="#ffffff">
    <h2 align="center">Search/Browse EFG Datasources</h2>
  <center>
      <% EFGDataSourceHelper dsHelper = dsHelperFactory.getDataSourceHelper(); %>
      <% Iterator dsNameIter = dsHelper.getDSNames().iterator(); %>
      <table>
		  <% while (dsNameIter.hasNext()) { %>
		  <tr>
			 <td>
				<% String dsName = (String)dsNameIter.next();
						/*if(!EFGServletUtils.configuredDatasources.contains(dsName.toLowerCase().trim())){//show only configured datasources
							continue; //skip it
						}
						else{*/
							found = true;
					//}

				 %>
				<%= dsHelper.makeReadable(dsName) %>
			 </td>
			 <td>
			 	 <a  title="search datasource" href="<%=context%>/SearchPage.jsp?pageType=option&dataSourceName=<%=URLEncoder.encode(dsName)%>">
					search
				 </a>
				  </td> <td>
					 <a  href="<%=context%>/search?dataSourceName=<%=URLEncoder.encode(dsName)%>&searchType=lists&maxDisplay=<%=EFGImportConstants.MAX_DISPLAY_IGNORE%>" title="browse a list of taxon names">
					browse lists
				 </a>
				  </td> <td>
				 <a  href="<%=context%>/search?dataSourceName=<%=URLEncoder.encode(dsName)%>&searchType=plates&maxDisplay=<%=EFGImportConstants.MAX_DISPLAY_IGNORE%>"  title="browse a  plates">
					browse plates
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
