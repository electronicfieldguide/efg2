<%@page import="java.util.Iterator,
project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface,
project.efg.Imports.efgInterface.EFGDatasourceObjectInterface,
project.efg.servlets.efgInterface.ServletAbstractFactoryInterface,
project.efg.servlets.factory.ServletAbstractFactoryCreator,
project.efg.util.EFGImportConstants" %>
					
<% 
   String context = request.getContextPath();
	boolean found = false;
      ServletAbstractFactoryInterface servFactory = ServletAbstractFactoryCreator.getInstance();
	EFGDatasourceObjectListInterface listInter = servFactory.getListOfDatasources();
	Iterator dsNameIter = listInter.getEFGDatasourceObjectListIterator(); 
%>
	
<html>
  <head>
    <title>Search/Browse EFG Datasources</title>
  </head>

  <body bgcolor="#ffffff">
    <h2 align="center">Search/Browse EFG Datasources</h2>
  <center>
      <table>
		  <% while (dsNameIter.hasNext()) { %>
		  <tr>
			 <td>
				<% 
					EFGDatasourceObjectInterface obj = (EFGDatasourceObjectInterface)dsNameIter.next();
					String displayName = obj.getDisplayName();
					
					found = true;
				 %>
					<%=displayName%>

			 </td>
			 <td>
			 	 <a  title="search datasource" href="<%=context%>/SearchPage.jsp?pageType=option&displayName=<%=displayName%>">
					search
				 </a>
				  </td> <td>
					 <a  href="<%=context%>/search?displayName=<%=displayName%>&searchType=lists" title="browse a list of taxon names">
					browse lists
				 </a>
				  </td> <td>
				 <a  href="<%=context%>/search?displayName=<%=displayName%>&searchType=plates"  title="browse  plates">
					browse plates
				 </a>	 
			 </td>
		</tr>
		<br/><br/>
		  <% 
		}
			if(!found){%>
			<h3> No datasources uploaded by author(s)</h3>
		<%}
		 %>
      </table>

  </center>
  </body>
</html>
