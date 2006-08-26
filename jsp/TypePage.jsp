<%@page import="java.util.Iterator,

project.efg.servlets.efgInterface.ServletAbstractFactoryInterface,
project.efg.servlets.factory.ServletAbstractFactoryCreator,
project.efg.util.EFGImportConstants,
project.efg.util.EFGDisplayObjectList,
project.efg.util.EFGDisplayObject
" %>
					
<% 
 	String forwardPage="NoDatasource.jsp";
   String context = request.getContextPath();
	boolean found = false;
    ServletAbstractFactoryInterface servFactory = ServletAbstractFactoryCreator.getInstance();
	EFGDisplayObjectList listInter = servFactory.getListOfDatasources();
	boolean isEmpty = false;
	if(listInter != null){
		if(listInter.getCount() < 1){
		isEmpty = true;
		}
	}
	else{
		isEmpty = true;
	}
	
  %>
	
<html>
  <head>
    <title>Search/Browse EFG Datasources</title>
  </head>

  <body bgcolor="#ffffff">
    <h2 align="center">Search/Browse EFG Datasources</h2>
  <center>
      <table>
		  <% 
		  if(!isEmpty){
		    Iterator dsNameIter = listInter.getIterator(); 
		    String searchPageStr = context + "/SearchPage.jsp?pageType=option&displayFormat=HTML&displayName=";
    		String searchLists = context + "/search?maxDisplay=100&displayFormat=HTML&searchType=lists&displayName=";
  			String searchPlates =context + "/search?maxDisplay=100&displayFormat=HTML&searchType=plates&displayName=";
			String dsName ="&"+ EFGImportConstants.DATASOURCE_NAME + "=";	  
		  	while (dsNameIter.hasNext()) { 
		  	%>
		  <tr>
			 <td>
				<% 
					EFGDisplayObject obj = (EFGDisplayObject)dsNameIter.next();
					String displayName = obj.getDisplayName();
					String datasourceName = obj.getDatasourceName();
					found = true;
					String newSearchStr =searchPageStr + displayName +  dsName + datasourceName;
					String newSearchPlates =searchPlates+ displayName +  dsName + datasourceName;
					String newSearchLists = searchLists+ displayName +  dsName + datasourceName;
				 %>
				<%=displayName%>
			 </td>
			 <td>
			 	 <a  href="<%=newSearchStr%>"  title="search datasource">
					search
				 </a>
			 </td> 
			 <td>
				 <a  href="<%=newSearchLists%>" title="browse a list of taxon names">
					browse lists
				 </a>
			  </td> 
			  <td>
				 <a  href="<%=newSearchPlates%>"  title="browse  plates">
					browse plates
				 </a>	 
			 </td>
		</tr>
		<br/><br/>
		  <% 
		}//end while
		if(!found){%>
			<h3> No datasources uploaded by author(s)</h3>
		<%}
		}//end outer if 
		else{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
		dispatcher.forward(request, response);
   
		}
		 %>
      </table>

  </center>
  </body>
</html>
