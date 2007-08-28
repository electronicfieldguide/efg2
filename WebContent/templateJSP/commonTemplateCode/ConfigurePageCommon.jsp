<%@page import="java.util.Iterator,
project.efg.util.interfaces.EFGImportConstants
" %>
<!-- $Id: ConfigurePageCommon.jsp,v 1.1.1.1 2007/08/01 19:11:39 kasiedu Exp $ -->

	Datasource:
  		<select name="<%=EFGImportConstants.DATASOURCE_NAME%>" title="Select a datasource from list below">
	  		<% 	
	  		
	  		 
	  		String displayNameN = "";
	  	  	String datasourceNameN="";
	  		
				while (listInter.hasNext()) { 
					displayNameN  = (String)listInter.next();
					datasourceNameN =(String)map.get(displayNameN);
			 %>
				<option  value="<%=datasourceNameN%>"><%=displayNameN%></option>
	  		<% 
			}	
	 		%>
		</select>
		<br/><br/>
		Template type:
		<select name="<%=EFGImportConstants.CONFIG_TYPE%>" title="Select the type of Page to Configure">
			<option value="<%=EFGImportConstants.SEARCH_SEARCH_TYPE%>">Search page</option>
			<option value="<%=EFGImportConstants.SEARCH_TAXON_TYPE%>">Taxon page</option>
			<option value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>">Text List</option>
			<option value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>">Thumbnail View</option>
		</select><br/><br/>
			<% 
				listInter = map.keySet().iterator();
			
			while (listInter.hasNext()) { 
				datasourceNameN = (String)listInter.next();
				displayNameN =(String)map.get(datasourceNameN.toLowerCase());
			 %>
				<input type="hidden" name="<%=datasourceNameN%>" value="<%=displayNameN%>"/>
	  		<% 
			}	
	 		%>
		 <input type="submit" value="Submit"/>
