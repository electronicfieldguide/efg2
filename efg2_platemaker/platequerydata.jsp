<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.EFGDataObjectListInterface,
project.efg.Imports.efgInterface.EFGDatasourceObjectInterface,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.Imports.efgImportsUtil.EFGTypeComparator,
project.efg.Imports.efgImportsUtil.MediaResourceTypeComparator,
project.efg.efgDocument.EFGType,
project.efg.efgDocument.ItemsType,
project.efg.servlets.efgInterface.EFGDataObject,
project.efg.efgDocument.MediaResourcesType,
project.efg.efgDocument.MediaResourceType,
project.efg.efgDocument.StatisticalMeasuresType,
project.efg.efgDocument.StatisticalMeasureType,
project.efg.efgDocument.EFGListsType,
project.efg.util.EFGImportConstants,
project.efg.util.EFGDocumentSorter,
project.efg.util.EFGListTypeSorter,
project.efg.util.EFGTypeSorter,
project.efg.util.MediaResourceTypeSorter,
project.efg.util.StatisticalMeasureTypeSorter
" %>

<% 
   String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME); 
   String datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
  
   String context = request.getContextPath();
   String tableName = request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
   if(tableName == null || tableName.trim().equals("")){
	   tableName = EFGImportConstants.EFG_RDB_TABLES;
   }
   EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
   dsHelper.setMainDataTableName(tableName);
   EFGDataObjectListInterface doSearches = dsHelper.getSearchable(displayName,datasourceName);	
  	if(doSearches != null){
	   if(datasourceName == null){
		datasourceName=doSearches.getDatasourceName();		
	   }
  }
   
%>	
<form id="form_search_id">
	<table class="searchpage">
		<tr>
			<td>      
		        <table id="searchPageID">
			        <%
			        if(doSearches != null){				
					for(int s = 0 ; s < doSearches.getEFGDataObjectCount(); s++){
						EFGDataObject searchable = doSearches.getEFGDataObject(s);
				   		
					 	String fieldName =searchable.getName();
						String legalName = searchable.getLegalName();
						
						Iterator itemsIter = null;
						ItemsType items = searchable.getStates();
					 	MediaResourcesType mediaResources = searchable.getMediaResources();
					 	EFGListsType  listsType = searchable.getEFGLists();
			            StatisticalMeasuresType stats = searchable.getStatisticalMeasures();
						EFGDocumentSorter sorter = null;
				     %>
		     		<tr>
	                 	<td align="right"><b><%=fieldName%>:</b></td>
		           		<td>	
							<% 
							if((items != null)&&(items.getItemCount() > 0)){
								sorter = new EFGTypeSorter();
								sorter.sort(new EFGTypeComparator(),items);		
								itemsIter = sorter.getIterator();		 
							%>
				 			<select name="<%=legalName%>" size="4"  multiple="multiple" onchange="evalAnySelectedValue(this);">
	                			<option selected="selected" value="<%=EFGImportConstants.EFG_ANY%>">any</option>
	            				<% 
				   					while (itemsIter.hasNext()) {
	 									EFGType item =(EFGType)itemsIter.next(); 
			         			%>
	                       				<option><%=item.getContent()%></option>
	                    		<% 
									} 
								%>
	             			</select>
			    			<% 
							}  
	              	  		else if((stats != null)&&(stats.getStatisticalMeasureCount() > 0)){
								String currentStatsName = legalName + EFGImportConstants.EFG_NUMERIC;
							%>
								<input type="text" name="<%=currentStatsName%>" maxlength="20"/>
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
								sorter = new MediaResourceTypeSorter();
								sorter.sort(new MediaResourceTypeComparator(),mediaResources);		
								itemsIter = sorter.getIterator();
							%>
				 			<select name="<%=legalName%>" size="4"  multiple="multiple" onchange="evalAnySelectedValue(this);">
                				<option selected="selected" value="<%=EFGImportConstants.EFG_ANY%>">any</option>
            					<% 
								while (itemsIter.hasNext()) {
 									MediaResourceType mediaResource =(MediaResourceType)itemsIter.next(); 
		         				%>
                       			<option><%=mediaResource.getContent()%></option>
                    			<% 
								} 
								%>
             	 			</select>
						 	<%
							}
				 			else if((listsType != null)&&(listsType.getEFGListCount() > 0)){
								sorter = new EFGListTypeSorter();
								sorter.sort(new EFGTypeComparator(),listsType);		
								itemsIter = sorter.getIterator();
							%>
				 			<select name="<%=legalName%>" size="4"  multiple="multiple" onchange="evalAnySelectedValue(this);">
                				<option selected="selected" value="<%=EFGImportConstants.EFG_ANY%>">any</option>
            					<% 	
			   					while (itemsIter.hasNext()) {
 									EFGType listType =(EFGType)itemsIter.next(); 
		         					%>
                       			<option><%=listType.getContent()%></option>
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
					<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
		        	<input type="hidden" name="<%=EFGImportConstants.DISPLAY_FORMAT%>" value="<%=EFGImportConstants.XML%>"/>
		        	<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=datasourceName%>"/>
		       		<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=tableName%>"/>
		        </table>
		         <%}%>			      			
  			</td>
		</tr>
	</table>
	
 	</form>
 	<%  if(doSearches != null){	%>
	<input type="submit" name="submit" value="Go"  onclick="javascript:postSearch('searchPageID','platedesignform','form_search_id')";/>
 	<%}
 	else{%>
 	<table id="searchPageID"><tr><td>The selected datasource has no searchable characters</td></tr><table>
 	<%}%>