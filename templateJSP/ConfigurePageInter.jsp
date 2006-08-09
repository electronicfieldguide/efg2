<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.ServletAbstractFactoryInterface,
project.efg.servlets.factory.ServletAbstractFactoryCreator,
project.efg.util.EFGImportConstants,
project.efg.util.EFGDisplayObjectList,
project.efg.util.EFGDisplayObject
" %>
<% 
   String context = request.getContextPath();
   ServletAbstractFactoryInterface servFactory = ServletAbstractFactoryCreator.getInstance();
   EFGDisplayObjectList listInter = servFactory.getListOfDatasources();
   Iterator dsNameIter = null;
   String displayName = "";
  String datasourceName="";
EFGDisplayObject obj = null;
%>
<html>
  <head>
    <title>Configure a Datasource</title>
  </head>
  <body bgcolor="#ffffff">
    <h2 align="center">Configure a Datasource</h2>
  	<center>
  		<form name="configure" action="ConfigurePageResponseInter.jsp">
  			Select a Datasource:
      		<select name="<%=EFGImportConstants.DATASOURCE_NAME%>" title="Select a datasource from list below">
		  		<% 
						
					dsNameIter = listInter.getIterator();
					while (dsNameIter.hasNext()) { 
						obj = (EFGDisplayObject)dsNameIter.next();
						displayName = obj.getDisplayName();
						datasourceName = obj.getDatasourceName();
				 %>
					<option  value="<%=datasourceName%>"><%=displayName%></option>
		  		<% 
				}	
		 		%>
    		</select>
    		<br/><br/>
    		Select a Configuration type:
    		<select name="<%=EFGImportConstants.CONFIG_TYPE%>" title="Select the type of Page to Configure">
    			<option value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>">Plates</option>
    			<option value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>">Lists</option>
    			<option value="<%=EFGImportConstants.SEARCH_TAXON_TYPE%>">Taxon page</option>
    		</select><br/><br/>
    			<% 
					dsNameIter = listInter.getIterator();
					while (dsNameIter.hasNext()) { 
						obj = (EFGDisplayObject)dsNameIter.next();
						displayName = obj.getDisplayName();
						datasourceName = obj.getDatasourceName();
				 %>
					<input type="hidden" name="<%=datasourceName%>" value="<%=displayName%>"/>
		  		<% 
				}	
		 		%>
    		 <input type="submit" value="Submit"/>
    	</form>
  	</center>
  </body>
</html>
