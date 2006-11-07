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
   String displayName ="EFG_2 Sample Data Title"; 
   String datasourceName ="HetaerinaData3may05_1158601662020";
   String xname= "scientific_name_EFG_WILDCARD";
   int ind = xname.indexOf(EFGImportConstants.EFG_WILDCARD);
   
   if(ind > -1){
	   xname = xname.substring(0,ind);
   }
   
   String context = request.getContextPath();
  
   EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
   EFGDataObjectListInterface doSearches = dsHelper.getSearchable(displayName,datasourceName);	
   if(datasourceName == null){
	datasourceName=doSearches.getDatasourceName();		
   }
  
   
%>

<html>
<!-- $Id-->
  <head>
    <title>Search Page for <%=xname%></title>
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

  <body bgcolor="#ffffff">
    <h2 align="center">
      Search Page for <%=displayName%>
    </h2>
	
  <center>
      <form method="post" action="<%=context%>/Redirect.jsp">
	
        <input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
        <input type="hidden" name="<%=EFGImportConstants.DISPLAY_FORMAT%>" value="<%=EFGImportConstants.HTML%>"/>
        <input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=datasourceName%>"/>

        <table>
	    	<tr>
				<td align="right"><b> Display search results as : </b></td>
				<td>
					<select name="<%=EFGImportConstants.SEARCH_TYPE_STR%>" size="1" id="firstSelect" 
						onchange="toggleDisplayFormat(this,'lastSelect');"
						onkeyup="toggleDisplayFormat(this,'lastSelect');"
						>
						<option value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>">Thumbnails</option>
						<option value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>">Text List</option>
					</select>         

		        	<input type="text" id="firstMatch" size="4" name="maxDisplay" value="70" 
						onchange="toggleMax(this,'lastMatch');"  
						onkeyup="toggleMax(this,'lastMatch');"/>
					<input type="submit" value="Search"/>
          			<input type="reset" value="Clear all"/>
         		</td>
	    	</tr>
          <%
		for(int s = 0 ; s < doSearches.getEFGDataObjectCount(); s++){
			EFGDataObject searchable = doSearches.getEFGDataObject(s);
	   		
		 	String fieldName =searchable.getName();
			String legalName = searchable.getLegalName();
			legalName = legalName + EFGImportConstants.EFG_WILDCARD;	
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
						<input type="text" name="<%=legalName%>" maxlength="20"/>
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
        					<select name="<%=EFGImportConstants.SEARCH_TYPE_STR%>" size="1" 
						id="lastSelect" onchange="toggleDisplayFormat(this,'firstSelect');" 
						onkeyup="toggleDisplayFormat(this,'firstSelect');">
						<option value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>">Thumbnails</option>
						<option value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>">Text List</option>
					</select>

        <input type="text" id="lastMatch" size="4" name="maxDisplay" value="70" 
						onchange="toggleMax(this,'firstMatch');"  
						onkeyup="toggleMax(this,'firstMatch');"/>
          <input type="submit" value="Search"/>
          <input type="reset" value="Clear all"/>          
        </p>
      </form>
    </center>
  </body>
</html>
