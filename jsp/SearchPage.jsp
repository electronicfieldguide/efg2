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


<html>
	<head>
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
	   if(datasourceName == null){
		datasourceName=doSearches.getDatasourceName();		
	   }
	  
	   
		%>
		<title>EFG Search</title>
		<link type="text/css" rel="stylesheet" href="css/defaultsearch.css" />
		<script language="JavaScript">
		<!--    
		// -->
		//handle other checkboxes that are not the 'any' checkbox
		//if the any checkbox field is checked then uncheck  the 'any' checkbox
		//otherwise check the 'any' checkbox
		
		
		// Find out that if any check box but the 'any' checkbox is selected
		
		function toggleDisplayFormat(field,idVar){
		
		   var selectedIndex = field.selectedIndex;
		   var other = document.getElementById(idVar);
		
		   other.options[selectedIndex] .selected= true;
		}
		function toggleMax(field,idVar){
		   var text = field.value;
		   var other = document.getElementById(idVar);
		   other.value = text;
		}
		 function evalOtherCheckBoxes(field)
		{
		
		    if(isFieldChecked(field)){
		        field[0].checked = false;
		    }
		    else{
		            field[0].checked = true ;
		    }
		}
		//f some checkBox other than the 'any' checkbox is checked
		//then uncheck them and check the 'any' check box
		//otherwise just check the 'any' checkbox
		//means that if user try's to uncheck the 'any' checkbox if nothing is selected it willnot be deselected
		function evalAnyCheckBox(field)
		{
		
		 if(isFieldChecked(field)){
		           for (i = 1; i < field.length; i++){
		            if(field[i].checked){
		                field[i].checked = false;
		            }
		        }
		    }
		    field[0].checked = true;
		}
		// Find out that if any check box but the 'any' checkbox is selected
		
		function isFieldChecked(field){
		    checked = false;
		    for (i = 1; i < field.length; i++){
		        if(field[i].checked){
		            checked = true;
		        }
		    }
		    return checked;
		}
		function isFieldSelected(field){
		    var selected = false;
		    for (i = 1; i < field.length; i++){
			if(field.options[i].selected == true){
				selected = true;
			}
		    }
		    return selected;
		}
		function evalAnySelectedValue(field)
		{ 
		  
		    if(isFieldSelected(field)){
		 	field.options[0].selected = false;
		    }
		    else{
			field.options[0].selected  = true;
		    }
		   }
		</script>
	</head>
	<body class="search">
		<p class="searchtitle">Search the EFG</p>	
		<p class="instruct">Select options from any of the categories below, then click "Search" at the bottom of the page.<br />
		<br />
		Selecting more than one option in a category will search for either characteristic (i.e. either "pink" or "white" flowers).<br />
		To select more than one option in a scrolling menu, hold down the Ctrl key on a PC (Command key on a Mac) when you click the mouse button.</p>
     	<form method="post" action="<%=context%>/Redirect.jsp">	
	        <input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
	        <input type="hidden" name="<%=EFGImportConstants.DISPLAY_FORMAT%>" value="<%=EFGImportConstants.HTML%>"/>
	        <input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=datasourceName%>"/>
	       	<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=tableName%>"/>
			<table class="search">
				<tr>
					<td class="submitsearch">
						<span class="searchdisplay">Display results as: 
						<select name="<%=EFGImportConstants.SEARCH_TYPE_STR%>" size="1" 
						id="firstSelect" 
						onchange="toggleDisplayFormat(this,'lastSelect');" 
						onkeyup="toggleDisplayFormat(this,'lastSelect');">
						<option value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>">Thumbnails</option>
						<option value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>">Text List</option>
						</select>
						</span> 
						<span class="searchmatches">Maximum Matches: 
							<input type="text" size="4" id="firstMatch" name="maxDisplay" 
							value="70" 
							onchange="toggleMax(this,'lastMatch');" 
							onkeyup="toggleMax(this,'lastMatch');" />
						</span> 
						<span class="search">
							<input type="submit" value="Search" />
						</span> 
						<span class="clear"><input type="reset" value="Clear all" /></span>					
					</td>
				</tr>
			</table>
			<table class="searchmodule">
				<tr>
					<td class="paddertopleft"></td>
					<td class="paddertopright"></td>
				</tr>
				<%
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
					<td class="sectionleft"><%=fieldName%>:</td>
					<td class="sectionright"><% 
						if((items != null)&&(items.getItemCount() > 0)){
							sorter = new EFGTypeSorter();
							sorter.sort(new EFGTypeComparator(),items);		
							itemsIter = sorter.getIterator();		 
						%><select name="<%=legalName%>" size="4"  multiple="multiple" onchange="evalAnySelectedValue(this);">
		              			<option selected="selected" value="<%=EFGImportConstants.EFG_ANY%>">any
		              			</option>
		          				<% 
			   					while (itemsIter.hasNext()) {
									EFGType item =(EFGType)itemsIter.next(); 
		         				%>
		                     	<option><%=item.getContent()%>
		                     	</option>
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
              				<option selected="selected" value="<%=EFGImportConstants.EFG_ANY%>">any
              				</option>
          					<% 	
	   						while (itemsIter.hasNext()) {
								EFGType listType =(EFGType)itemsIter.next(); 
         					%>
                     		<option><%=listType.getContent()%>
                     		</option>
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
				<tr>
					<td class="padderbottomleft"></td>
					<td class="padderbottomright"></td>
				</tr>
				<tr>
					<td class="spacer"></td>
					<td class="spacer"></td>
				</tr>
			</table>
			<table class="search">
				<tr>
					<td class="submitsearch">
						<span class="searchdisplay">Display results as: 
      					<select name="<%=EFGImportConstants.SEARCH_TYPE_STR%>" size="1" 
						id="lastSelect" onchange="toggleDisplayFormat(this,'firstSelect');" 
						onkeyup="toggleDisplayFormat(this,'firstSelect');">
						<option value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>">Thumbnails</option>
						<option value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>">Text List</option>
						</select>
						</span> 
						<span class="searchmatches">Maximum Matches: 
						<input type="text" id="lastMatch" size="4" name="maxDisplay" value="70" 
						onchange="toggleMax(this,'firstMatch');" 
						onkeyup="toggleMax(this,'firstMatch');" />
						</span> <span class="search">
						<input type="submit" value="Search" />
						</span> 
						<span class="clear"><input type="reset" value="Clear all" /></span>			
					
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>

