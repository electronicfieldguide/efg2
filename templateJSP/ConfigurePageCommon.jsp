<%@page import="java.util.Iterator,
project.efg.util.EFGImportConstants,
project.efg.util.EFGDisplayObject
" %>
<!-- $Id$ -->

	Select a Datasource:
  		<select name="<%=EFGImportConstants.DATASOURCE_NAME%>" title="Select a datasource from list below">
	  		<% 	
	  		 Iterator dsNameIter = listInter.getIterator();
	  		 
	  		String displayNameN = "";
	  	  	String datasourceNameN="";
	  		EFGDisplayObject obj = null;
				while (dsNameIter.hasNext()) { 
					obj = (EFGDisplayObject)dsNameIter.next();
					displayNameN = obj.getDisplayName();
					datasourceNameN = obj.getDatasourceName();
			 %>
				<option  value="<%=datasourceNameN%>"><%=displayNameN%></option>
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
					displayNameN = obj.getDisplayName();
					datasourceNameN = obj.getDatasourceName();
			 %>
				<input type="hidden" name="<%=datasourceNameN%>" value="<%=displayNameN%>"/>
	  		<% 
			}	
	 		%>
		 <input type="submit" value="Submit"/>
