<%@page import="java.util.Iterator,
project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface,
project.efg.Imports.efgInterface.EFGDatasourceObjectInterface,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.efgDocument.EFGType,
project.efg.efgDocument.ItemsType,
project.efg.servlets.efgInterface.SearchableObject,
project.efg.servlets.efgInterface.SearchableListInterface,
project.efg.efgDocument.MediaResourcesType,
project.efg.efgDocument.MediaResourceType,
project.efg.efgDocument.StatisticalMeasuresType,
project.efg.efgDocument.StatisticalMeasureType,
project.efg.efgDocument.EFGListsType,
project.efg.util.EFGImportConstants" %>

<% 
   String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME); 
   String datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
  
   String context = request.getContextPath();
  
   EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
   SearchableListInterface searchables = dsHelper.getSearchable(displayName,datasourceName);	
   if(datasourceName == null){
	datasourceName=searchables.getDatasourceName();		
   }
  
   
%>

<html>
<!-- $Id-->
  <head>
    <title>Search Page for <%=displayName%></title>
  </head>

  <body bgcolor="#ffffff">
    <h2 align="center">
      Search Page for <%=displayName%>
    </h2>
	
  <center>
      <form method="post" action="<%=context%>/search">
        <p>
          <input type="submit" value="Conduct Search"/>
          <input type="reset" value="Clear all fields"/>
        </p>
	
        <input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
        <input type="hidden" name="<%=EFGImportConstants.DISPLAY_FORMAT%>" value="<%=EFGImportConstants.HTML%>"/>
        <input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=datasourceName%>"/>

        <table>
          <tr>
            <td align="right">
              <b>Maximum Matches to Display:</b><br/><br/>
		</td>
            <td><input type="text" size="4" name="maxDisplay" value="20"/>
              <br/><br/>
		</td>
          </tr>
	    <tr>
		<td align="right"><b> Display search results as : </b></td>
		<td>
			<select name="<%=EFGImportConstants.SEARCH_TYPE_STR%>" size="1">
				<option value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>">Plates</option>
				<option value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>">Lists</option>
			</select>
            </td>
	    </tr>
          <%
		
		for(int s = 0 ; s < searchables.getSearchleObjectsCount(); s++){
			SearchableObject searchable = searchables.getSearchableObject(s);
	   		
		 	String fieldName =searchable.getName();
			String legalName = searchable.getLegalName();
			ItemsType items = searchable.getStates();
		 	MediaResourcesType mediaResources = searchable.getMediaResources();
		 	EFGListsType  listsType = searchable.getEFGLists();
             		StatisticalMeasuresType stats = searchable.getStatisticalMeasures();
	     		%>
	     		<tr>
                 		<td align="right"><b><%=fieldName%>:</b></td>
	           		<td>	
					<% 
						if((items != null)&&(items.getItemCount() > 0)){		 
					%>
			 				<select name="<%=legalName%>" size="4"  multiple="multiple">
                						<option value="<%=EFGImportConstants.EFG_ANY%>">any</option>
            						<% 
			   					for (int i=0; i < items.getItemCount(); i++ ) {
 									EFGType item = items.getItem(i); 
		         					%>
                       					<option><%=item.getContent()%>
                    					<% 
								} 
								%>
             					</select>
		    				<% 
						}  
              	  		  	else if((stats != null)&&(stats.getStatisticalMeasureCount() > 0)){
						%>
							<input type="text" name="<%=fieldName%>" maxlength="20"/>
							<% String str ="";
 							   String units = stats.getUnit();
 							   if(units == null){
							   	units ="";
							   }
 							   if(stats.getMax() > stats.getMin()){
								str = stats.getMin() + "-" + stats.getMax() + " " + units;
							   }else{
								str = units;
							   }	
							%>
							<%=str%><br/>	
			 			<%
						}
  					  	else if((mediaResources!= null)&&(mediaResources.getMediaResourceCount() > 0)){
						%>
				 			<select name="<%=legalName%>" size="4"  multiple="multiple">
                						<option>any</option>
            						<% 
			   					for (int m=0; m < mediaResources.getMediaResourceCount();m++ ) {
 									MediaResourceType mediaResource = mediaResources.getMediaResource(m); 
		         					%>
                       					<option><%=mediaResource.getContent()%>
                    					<% 
								} 
								%>
             	 				</select>
					 	<%
						}
			 			else if((listsType != null)&&(listsType.getEFGListCount() > 0)){
						%>
				 			<select name="<%=legalName%>" size="4"  multiple="multiple">
                						<option>any</option>
            						<% 	
			   					for (int l=0; l< listsType.getEFGListCount();l++ ) {
 									EFGType listType = listsType.getEFGList(l); 
		         					%>
                       					<option><%=listType.getContent()%>
                    					<% 
								} 
								%>
             	 				</select>
			 			<%
						}
						%>
              		 </td>
           		</tr>
          	<%
		}
		%>
        </table>
        <p>
          <input type="submit" value="Conduct Search"/>
          <input type="reset" value="Clear all fields"/>
        </p>
      </form>
    </center>
  </body>
</html>
