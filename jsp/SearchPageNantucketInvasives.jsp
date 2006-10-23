<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.EFGDataObjectListInterface,
project.efg.Imports.efgInterface.EFGDatasourceObjectInterface,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.Imports.efgImportsUtil.EFGTypeComparator,
project.efg.Imports.efgImportsUtil.MediaResourceTypeComparator,
project.efg.efgDocument.EFGType,
project.efg.efgDocument.Item,
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
  
   EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
   EFGDataObjectListInterface doSearches = dsHelper.getSearchable(displayName,datasourceName);	
   if(datasourceName == null){
	datasourceName=doSearches.getDatasourceName();		
   }
EFGDocumentSorter sorter = null;
	Iterator itemsIter = null;
EFGType item = null;
//taxonomy  
String genus=null;
ItemsType genusItems = null;

String family=null;
ItemsType familyItems = null;

String commonName=null;
ItemsType commonNameItems = null;

String origin = null;
ItemsType originItems = null;

//degree of invasiveness
  String degInvNan=null;
ItemsType degInvNanItems = null;

  String degInvMass=null;
ItemsType degInvMassItems = null;

  String dispersal=null;
ItemsType dispersalItems = null;

  String reproduction = null;
ItemsType reproductionItems = null;

   //growth form
   String growthForm=null;
ItemsType growthFormItems = null;

   String flowerColor = null;
ItemsType flowerColorItems = null;

   String petalNumber = null;
  ItemsType petalNumberItems = null;

   String fruitColor = null;
ItemsType fruitColorItems = null;

for(int s = 0 ; s < doSearches.getEFGDataObjectCount(); s++){
			EFGDataObject searchable = doSearches.getEFGDataObject(s);
	   		
		 	String fieldName =searchable.getName();
			String legalName = searchable.getLegalName();
			
		
			ItemsType items = searchable.getStates();
		


	if(fieldName.equalsIgnoreCase("Genus")){
		genus = legalName;
		genusItems= items;
	}
	else if(fieldName.equalsIgnoreCase("Family")){
		family = legalName;
		familyItems= items;
	}
	else if(fieldName.equalsIgnoreCase("Common Name")){
		commonName = legalName;
		commonNameItems= items;
	}
	else if(fieldName.equalsIgnoreCase("Origin")){
		origin = legalName;
		originItems= items;
	}
	else if(fieldName.equalsIgnoreCase("Degree of invasiveness on Nantucket")){
		degInvNan =legalName; 
		degInvNanItems= items;
	}
	else if(fieldName.equalsIgnoreCase("Degree of invasiveness in Massachusetts")){
		degInvMass=legalName;
		degInvMassItems= items;
	}
	else if(fieldName.equalsIgnoreCase("DISPERSAL VECTORS (SYN)")){
		dispersal=legalName;
		dispersalItems= items;
	}
	else if(fieldName.equalsIgnoreCase("REPRODUCTION TYPE (SYN)")){
		reproduction =legalName;
		reproductionItems= items; 
	}
     else if(fieldName.equalsIgnoreCase("Growth Form")){
   		growthForm=legalName;
		growthFormItems= items;
	}
	else if(fieldName.equalsIgnoreCase("Flower Color (SYN)")){
	 	flowerColor = legalName;
		flowerColorItems= items;
	}
	else if(fieldName.equalsIgnoreCase("Petal Number (SYN)")){
	 	petalNumber = legalName;
		petalNumberItems= items;
	}
	else if(fieldName.equalsIgnoreCase("Fruit Color (SYN)")){
 		fruitColor = legalName;
		fruitColorItems = items;
	}
	

}
//1) Growth Form and Flowers
//2) Leaves
//3) Taxonomy
//4) Degree of Invasiveness
%>
<html>
  <head>
    <title>Search the Invasive Plants of Nantucket Database</title>
    <link type="text/css" rel="stylesheet" href="css/searchpage.css">
<SCRIPT LANGUAGE="JavaScript">
<!--    
// -->
//handle other checkboxes that are not the 'any' checkbox
//if the any checkbox field is checked then uncheck  the 'any' checkbox
//otherwise check the 'any' checkbox

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
 
</script>

  </head>

  <body bgcolor="#ffffff">
    <p class="title">Search the Invasive Plants of Nantucket Database</p>
    <p class="instruct">To search the database, select options from any of the following categories below, then click "Search" at the bottom of the page.<br><br>Selecting more than one option in a category will search for plants that have either characteristic (i.e. either "pink" or "white" flowers).<br/>To select more than one option in a scrolling menu, hold down the Ctrl key on a PC (Command key on a Mac) when you click the mouse button.</p>

       <form method="post" action="<%=context%>/Redirect.jsp" name="myform">
        <input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
        <input type="hidden" name="<%=EFGImportConstants.DISPLAY_FORMAT%>" value="<%=EFGImportConstants.HTML%>"/>
        <input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=datasourceName%>"/>
	  <table class="search">		  
		<tr>
			<td class="submitsearch">
				<span class="display">Display results as:
					<select name="<%=EFGImportConstants.SEARCH_TYPE_STR%>" size="1" id="firstSelect" 
						onchange="toggleDisplayFormat(this,'lastSelect');"
						onkeyup="toggleDisplayFormat(this,'lastSelect');"
						>
						<option value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>">Thumbnails</option>
						<option value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>">Text List</option>
					</select>
				</span>
				<span class="matches">Maximum Matches:
					<input type="text" size="4" id="firstMatch" name="maxDisplay" value="70" 
						onchange="toggleMax(this,'lastMatch');" 
						onkeyup="toggleMax(this,'lastMatch');"/>
				</span>
        			<span class="search">
					<input type="submit" value="Search"/>
				</span>
        			<span class="clear">
					<input type="reset" value="Clear all"/>
				</span>
			</td>
		</tr>
	 </table>
       <table>
		  <tr>
		  <th>
		  Growth Form and Flowers
		  </th>
		  </tr>          
		  <tr>
		  <td class="paddertopleft"></td>
		  <td class="paddertopright"></td>
		  </tr>
          <tr>
            <td class="sectionleft" align="right">Growth Form:</td>
            <td class="sectionright">
              <select multiple name="<%=growthForm%>" size="4" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>

		<%
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),growthFormItems);		
			itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(Item)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>


              </select>
            </td>
          </tr>
          <tr>
            <td class="sectionleft" align="right">Flower Color:</td>
            <td class="sectionright">
              <select multiple name="<%=flowerColor%>" size="4" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>
 

		<%
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),flowerColorItems);		
			itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>

              </select>
            </td>
          </tr>
          <tr>
            <td class="sectionleft" align="right">Petal Number:</td>
            <td class="sectionright">
              <select multiple name="<%=petalNumber%>" size="4" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>

		<%
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),petalNumberItems);		
			itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>

              </select>
            </td>
          </tr>
	    <tr>
            <td class="sectionleft" align="right">Fruit Color:</td>
            <td class="sectionright">
              <select multiple name="<%=fruitColor%>" size="4" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>
              

		<%
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),fruitColorItems);		
			itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>


              </select>
            </td>
          </tr>

		  <tr>
		  <td class="padderbottomleft"></td>
		  <td class="padderbottomright"></td>
		  </tr>
		  <tr>
		  <td class="spacer"></td>
		  </tr>
  		  <tr>
		  <th>
		  Leaves
		  </th>
		  </tr>          
          <tr>
            <td class="radioheader">Leaf/Leaflet Shape:</td>
            	<td>
				<table class="radiothumbs">
					<tr>
						<td class="radiothumbs"><br/>
                					<input type="checkbox" select name="leaf_Leaflet_Shape__SYN_" value="<%=EFGImportConstants.EFG_ANY%>" checked
								onclick="evalAnyCheckBox(document.myform.leaf_Leaflet_Shape__SYN_);">any
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/arrowshaped.png" width="85" height="85" border="0" alt="arrow-shaped"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="arrow-shaped"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">arrow-shaped
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/eggshaped.png" width="85" height="85" border="0" alt="egg-shaped"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="egg-shaped"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">egg-shaped
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/elliptic.png" width="85" height="85" border="0" alt="elliptic"/><br/>
					                <input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="elliptic"
									onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">elliptic
							    </input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/finelydivided.png" width="85" height="85" border="0" alt="finely divided"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="finely divided"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">finely divided
							</input>
          					</td>
					</tr>
					<tr>
						<td class="radiothumbs">
							<img src="glossary/heartshaped.png" width="85" height="85" border="0" alt="heart-shaped"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="heart-shaped"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">heart-shaped
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/kidneyshaped.png" width="85" height="85" border="0" alt="kidney-shaped"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="kidney-shaped"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">kidney-shaped
							</input>
            				</td>
						<td class="radiothumbs">
							<img src="glossary/lanceolate.png" width="85" height="85" border="0" alt="lanceolate"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="lanceolate"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">lanceolate
							</input>
            				</td>
						<td class="radiothumbs">
							<img src="glossary/linear.png" width="85" height="85" border="0" alt="linear"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="linear"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">linear
							</input>
            				</td>
						<td class="radiothumbs">
							<img src="glossary/needleshaped.png" width="85" height="85" border="0" alt="needle-shaped"/><br/>
				                	<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="needle-shaped"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">needle-shaped
							</input>
            				</td>
					</tr>
					<tr>
						<td class="radiothumbs">
							<img src="glossary/oblong.png" width="85" height="85" border="0" alt="oblong"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="oblong"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">oblong
							</input>
						</td>
						<td class="radiothumbs">            
							<img src="glossary/obovate.png" width="85" height="85" border="0" alt="obovate"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="obovate"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">obovate
							</input>
            				</td>
						<td class="radiothumbs">
							<img src="glossary/round.png" width="85" height="85" border="0" alt="round"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="round"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">round
							</input>
            				</td>
						<td class="radiothumbs">
							<img src="glossary/starshaped.png" width="85" height="85" border="0" alt="star-shaped"/><br/>
       	 		      		<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="star-shaped"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">star-shaped
							</input>
            				</td>
						<td class="radiothumbs">
							<img src="glossary/triangular.png" width="85" height="85" border="0" alt="triangular"/><br/>
                					<input type="checkbox" name="leaf_Leaflet_Shape__SYN_" value="triangular"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Leaflet_Shape__SYN_);">triangular
							</input>
            				</td>
					</tr>
				</table>
            	</td>
          </tr>
          <tr>
            	<td class="radioheader">Leaf arrangement:</td>
			<td>
				<table class="radiothumbs">
					<tr>
						<td class="radiothumbs">
							<br/>
                					<input type="checkbox" name="leaf_Arrangement__SYN_" value="<%=EFGImportConstants.EFG_ANY%>" checked
								onclick="evalAnyCheckBox(document.myform.leaf_Arrangement__SYN_);">any
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/alternate.png" width="85" height="85" border="0" alt="alternate"/><br/>
                					<input type="checkbox" name="leaf_Arrangement__SYN_" value="alternate"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Arrangement__SYN_);">alternate
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/opposite.png" width="85" height="85" border="0" alt="opposite"/><br/>
                					<input type="checkbox" name="leaf_Arrangement__SYN_" value="opposite"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Arrangement__SYN_);">opposite
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/whorled.png" width="85" height="85" border="0" alt="whorled"/><br/>
                					<input type="checkbox" name="leaf_Arrangement__SYN_" value="whorled"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Arrangement__SYN_);">whorled
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/basal.png" width="85" height="85" border="0" alt="basal"/><br/>
                					<input type="checkbox" name="leaf_Arrangement__SYN_" value="basal"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Arrangement__SYN_);">basal
							</input>
          					</td>
					</tr>
				</table>
		  	</td>
		</tr>
          	<tr>
            	<td class="radioheader">Leaf venation:</td>
			<td>
				<table class="radiothumbs">
					<tr>
						<td class="radiothumbs">
							<br/>
                					<input type="checkbox" name="leaf_Venation__SYN_" value="<%=EFGImportConstants.EFG_ANY%>" checked
								onclick="evalAnyCheckBox(document.myform.leaf_Venation__SYN_);">any
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/pinnate.png" width="85" height="85" border="0" alt="pinnate"/><br/>
                					<input type="checkbox" name="leaf_Venation__SYN_" value="pinnate" 
								onclick="evalOtherCheckBoxes(document.myform.leaf_Venation__SYN_);">pinnate
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/palmate.png" width="85" height="85" border="0" alt="palmate"/><br/>
                					<input type="checkbox" name="leaf_Venation__SYN_" value="palmate"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Venation__SYN_);">palmate
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/parallel.png" width="85" height="85" border="0" alt="parallel"/><br/>
	                				<input type="checkbox" name="leaf_Venation__SYN_" value="parallel"
								onclick="evalOtherCheckBoxes(document.myform.leaf_Venation__SYN_);">parallel
							</input>
          					</td>
					</tr>
				</table>
		 	</td>
	    </tr>
          <tr>
            <td class="radioheader">Leaf margins:</td>
		<td>
			<table class="radiothumbs">
				<tr>
					<td class="radiothumbs">
						<br/>
                				<input type="checkbox" name="leaf_Margins__SYN_" value="<%=EFGImportConstants.EFG_ANY%>" checked
							onclick="evalAnyCheckBox(document.myform.leaf_Margins__SYN_);">any
						</input>
          				</td>
					<td class="radiothumbs">
						<img src="glossary/entire.png" width="85" height="85" border="0" alt="entire"/><br/>
                				<input type="checkbox" name="leaf_Margins__SYN_" value="entire"
							onclick="evalOtherCheckBoxes(document.myform.leaf_Margins__SYN_);">entire
						</input>
          				</td>
					<td class="radiothumbs">
						<img src="glossary/toothed.png" width="85" height="85" border="0" alt="toothed"/><br/>
                				<input type="checkbox" name="leaf_Margins__SYN_" value="toothed" 
							onclick="evalOtherCheckBoxes(document.myform.leaf_Margins__SYN_);">toothed
						</input>
          				</td>
					<td class="radiothumbs">
						<img src="glossary/lobed.png" width="85" height="85" border="0" alt="lobed"/><br/>
                				<input type="checkbox" name="leaf_Margins__SYN_"value="lobed" 
							onclick="evalOtherCheckBoxes(document.myform.leaf_Margins__SYN_);">lobed
						</input>
          				</td>
				</tr>
			</table>
		</td>
	    </tr>
          <tr>
            <td class="radioheader">Leaf grouping:</td>
		<td>
			<table class="radiothumbs">
					<tr>
						<td class="radiothumbs">
							<br/>
                					<input type="checkbox" name="leaf_Grouping__SYN_" value="<%=EFGImportConstants.EFG_ANY%>" 
								checked onclick="evalAnyCheckBox(document.myform.leaf_Grouping__SYN_);">any
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/simple.png" width="85" height="85" border="0" alt="simple"/><br/>
                					<input type="checkbox" name="leaf_Grouping__SYN_" value="simple" 
								onclick="evalOtherCheckBoxes(document.myform.leaf_Grouping__SYN_);">simple
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/pinnatelycompound.png" width="85" height="85" border="0" alt="pinnately compound"/><br/>
                					<input type="checkbox" name="leaf_Grouping__SYN_" value="pinnately compound" 
								onclick="evalOtherCheckBoxes(document.myform.leaf_Grouping__SYN_);">pinnately compound
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/palmatelycompound.png" width="85" height="85" border="0" alt="palmately compound"/><br/>
                					<input type="checkbox" name="leaf_Grouping__SYN_" value="palmately compound" 
								onclick="evalOtherCheckBoxes(document.myform.leaf_Grouping__SYN_);">palmately compound
							</input>
          					</td>
						<td class="radiothumbs">
							<img src="glossary/trifoliolate.png" width="85" height="85" border="0" alt="trifoliolate"/><br/>
                					<input type="checkbox" name="leaf_Grouping__SYN_" value="trifoliolate" 
								onclick="evalOtherCheckBoxes(document.myform.leaf_Grouping__SYN_);">trifoliolate
							</input>
          					</td>
					</tr>
				</table>
			 </td>
		  </tr>
            </td>
          </tr>
          		<tr>
			<th>
		  		Taxonomy
		  	</th>
		 </tr>
		 <tr>
		  	<td class="paddertopleft"></td>
		  	<td class="paddertopright"></td>
		  </tr>
          	<tr>
            	<td class="sectionleft" align="right">Genus:</td>
            	<td class="sectionright">
              		<select multiple name="<%=genus%>" size="5" onchange="evalAnySelectedValue(this);">
                			<option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>
                			<% item =null;
							sorter = new EFGTypeSorter();
							sorter.sort(new EFGTypeComparator(),genusItems);		
							itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>

              </select>
            </td>
          </tr>
          <tr class="section">
            <td class="sectionleft" align="right">Family:</td>
            <td class="sectionright">
              <select multiple name="<%=family%>" size="5" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>
                 <%
			
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),familyItems);		
		itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>

              </select>
            </td>
          </tr>
          
          <tr class="section">
            <td class="sectionleft" align="right">Common Name:</td>
            <td class="sectionright">
              <select multiple name="<%=commonName%>" size="5" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>
		<%
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),commonNameItems);		
			itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>
              </select>
            </td>
          </tr>
 	    <tr class="section">
            <td class="sectionleft" align="right">Origin:</td>
            <td class="sectionright">
              <select multiple name="<%=origin%>" size="5" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>
		<%
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),originItems);		
			itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>

              </select>
            </td>
          </tr>

		  <tr>
		  <td class="padderbottomleft"></td>
		  <td class="padderbottomright"></td>
		  </tr>
		  <tr>
		  <td class="spacer"></td>
		  </tr>
		  <tr>
		  <th>
		  Degree of Invasiveness
		  </th>
		  </tr>
		  <tr>
		  <td class="paddertopleft"></td>
		  <td class="paddertopright"></td>
		  </tr>
          <tr>
            <td class="sectionleft" align="right">On Nantucket:</td>
            <td class="sectionright">
             <select multiple name="<%=degInvNan%>" size="4" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>
		<%
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),degInvNanItems);		
			itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>

              </select>

            </td>
          </tr>
          <tr>
            <td class="sectionleft" align="right">In Massachusetts:</td>
            <td class="sectionright">
              <select multiple name="<%=degInvMass%>" size="4" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>
        

		<%
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),degInvMassItems);		
			itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>
              </select>

            </td>
          </tr>
  		<tr>
            <td class="sectionleft" align="right">Dispersal Vectors:</td>
            <td class="sectionright">
              <select multiple name="<%=dispersal%>" size="4" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>
 
		<%
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),dispersalItems);		
			itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>

              </select>

            </td>
          </tr>
		<tr>
            <td class="sectionleft" align="right">Type of Reproduction:</td>
            <td class="sectionright">
              <select multiple name="<%=reproduction%>" size="4" onchange="evalAnySelectedValue(this);">
                <option selected value="<%=EFGImportConstants.EFG_ANY%>">any</option>

		<%
			 item =null;
			sorter = new EFGTypeSorter();
			sorter.sort(new EFGTypeComparator(),reproductionItems);		
			itemsIter = sorter.getIterator();		 
					
				while (itemsIter.hasNext()) {
 				item =(EFGType)itemsIter.next(); 
		        %>
                    <option><%=item.getContent()%>
                   <% 
			} 
			%>


              </select>

            </td>
          </tr>

		  <tr>
		  <td class="padderbottomleft"></td>
		  <td class="padderbottomright"></td>
		  </tr>
		  <tr>
		  <td class="spacer"></td>
		  </tr>

	</table>
	<table class="search">		  
		<tr>
			<td class="submitsearch">
				<span class="display">Display results as:
					<select name="<%=EFGImportConstants.SEARCH_TYPE_STR%>" size="1" 
						id="lastSelect" onchange="toggleDisplayFormat(this,'firstSelect');" 
						onkeyup="toggleDisplayFormat(this,'firstSelect');">
						<option value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>">Thumbnails</option>
						<option value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>">Text List</option>
					</select>
				</span>
				<span class="matches">Maximum Matches:
					<input type="text" id="lastMatch" size="4" name="maxDisplay" value="70" 
						onchange="toggleMax(this,'firstMatch');"  
						onkeyup="toggleMax(this,'firstMatch');"/>
				</span>
        			<span class="search"><input type="submit" value="Search"/></span>
        			<span class="clear"><input type="reset" value="Clear all"/></span>
			</td>
		</tr>
	</table> 
     </form>
  </body>
</html>

