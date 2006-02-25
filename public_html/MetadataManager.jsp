<%@page import="java.util.*,java.io.*,java.sql.*, project.efg.util.*, project.efg.efgInterface.*" %>
<html>
	<head>
	<%
	   project.efg.efgInterface.EFGDSHelperFactory dsHelperFactory = new project.efg.efgInterface.EFGDSHelperFactory();
 	   String dataSourceName = request.getParameter("dataSourceName");
	   if(dataSourceName == null){
	   	dataSourceName = "LabelsFORBobData";
	  }
    	   String context = request.getContextPath();
	   String[] radioButtons = {"Categorical", "Narrative", "Numeric", "Numeric Range", "Media Resource"};
	   String[] checkBoxes = {"onTaxonPage","isSearchable", "isList"};
	   String[] optionBoxes= {"Weight"};

    	   String fieldName = null;
         EFGField field =null;
	   Iterator it =null;
	   RDBDataSourceHelper dsHelper = (RDBDataSourceHelper)dsHelperFactory.getDataSourceHelper(); 
	   Map table = dsHelper.getFieldsInfo(dataSourceName);
	   int numberOfFields = table.size();
	   ArrayList weightList = new ArrayList(numberOfFields);

	   for(int i = 0 ; i < numberOfFields;i++){
		int mult = (i+1);
		weightList.add(i,new String(mult+""));
	  }
	
	   SortedSet characterSet = Collections.synchronizedSortedSet(new TreeSet(new project.efg.util.CaseInsensitiveComparator()));
	   it = table.entrySet().iterator();
	
	   while (it.hasNext()) {
		Map.Entry pairs = (Map.Entry)it.next();
		fieldName = ((String)pairs.getKey()).trim();
		field = (EFGField)pairs.getValue();
		characterSet.add(fieldName.trim());	
	 }
     %>
	
	<title>Metadata Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="js/sortabletable.js"/></script>
<link type="text/css" rel="StyleSheet" href="css/sortabletable.css"/>
	
<style type="text/css">

body {
	font-family:	Verdana, Helvetica, Arial, Sans-Serif;
	font:			Message-Box;
}


</style>
	</head>
	<h2 align="center">Metadata for datasource <%=dataSourceName%></h2>
  	<body bgcolor="#ffffff">
	<center>
                   <form method="post" action="">

			<table class="sort-table" id="table-1" cellspacing="0" border="1">
				<thead><tr>
					<td title="The name of the field as it appears in your database">Field Name</td>
					<td title="Indicate whether this field is a Categorical Character">Categorical</td>
					<td title="Indicate whether this field is a descriptive(narrative) character">Narrative</td>
					<td title="Indicate whether this field is a Numeric Character">Numeric</td>
					<td title="Indicate whether this field is a Numeric range Character">Numeric Range</td>
					<td title="Indicate whether values in this field are a Mediaresource(video,image,audio) character">Media Resource</td>
					<td title="Indicate whether states(values) in this field should be searched on, on search page ">Searchable?</td>
					<td title="Indicate whether values in this field at EFG lists">List?</td>
					<td title="Indicate whether values of this field should appear on taxon pages">On Taxon Page?</td>
					<td title="Indicate the order of the characters on the search pages">Order</td>
				</tr></thead><tbody>
				<%
				it = characterSet.iterator();
				int counter = 0;
				while (it.hasNext()) {
					fieldName = ((String)it.next());
				%>
				<tr>
					<td><%=fieldName%></td>
					<%
					for(int i = 0; i < radioButtons.length; i++){
					%>
					<td align="center"><input type="radio" align="middle" name="<%=fieldName%>"   value="<%=radioButtons[i].toString()%>"/></td>
					<%}
					for(int j = 0; j < checkBoxes.length; j++){
					%>
					<td><input type="checkbox" align="middle" name="<%=checkBoxes[j].toString() + fieldName%>"/></td>
					<%}
					for(int z = 0; z < optionBoxes.length; z++){
					%>
					<td><select name="<%=optionBoxes[z].toString() + fieldName%>">
						<%
							for(int weight=0; weight < weightList.size(); weight++){
								String curWeight = (String)weightList.get(weight);
								if(counter == weight){%>
									<option selected="selected"><%=curWeight%></option>	
								<%}else{%>
									<option><%=curWeight%></option>	
								<%}
							}
						%>
						</select>
					</td>
				  <%}
				  %>
				</tr>	
				<%
				++counter;	
				}
				%>
			</tbody></table><br/>
                  <table>
				<td align="left" title="Text to use to display your datasource">			
					Enter a display name for your datasource:<input type="text" name="displayName" value="<%=dataSourceName%>"/>
				</td>
				<input type="hidden" name="dataSourceName" value="<%=dataSourceName%>"/>
			</table><br/><br/><br/>
                   <input align="center" type="submit" name="submit" value="submit" title="Click to submit selections"/>
			</form>
<script type="text/javascript">
//<![CDATA[
var isMSIE = "false";
var st;
if (/MSIE/.test(navigator.userAgent)) {
	isMSIE = "true";
}
else{
	isMSIE="false";
}
 st = new SortableTable(document.getElementById("table-1"),
	["CaseInsensitiveString","RadioCheckBox","RadioCheckBox","RadioCheckBox","RadioCheckBox","RadioCheckBox","RadioCheckBox", "RadioCheckBox", "RadioCheckBox","Number"]);
/**
*use for both radio button and check box
*returns true for the currently selected option and false otherwise.
*/
function getRadioCheckBoxValue (oRow, nColumn) {
	
	return oRow.cells[nColumn].firstChild.checked ? 1 : 0;
};

/**
* Handle options .
* returns the currently selected value
*/
function getOptionValue (oRow, nColumn) {
	if (isMSIE == "true"){
     		var select = oRow.cells[nColumn].firstChild;
	
		var options1= select.options;
		var l = options1.length;
		for(var i = 0; i < l; i++){
			var option = options1[i];
			if(option.selected){
				//parse the string into an integer
				return parseInt(option.text);
			}
		}
		
	}
	else{
		return oRow.cells[nColumn].firstChild.value * 1;
	}
	
};


// add new sort type and use the default compare
// also use custom getRowValue since the text content is not enough
st.addSortType("RadioCheckBox", null, null, getRadioCheckBoxValue);
st.addSortType("Number", null, null, getOptionValue);

// IE does not remember input values when moving DOM elements

if (/MSIE/.test(navigator.userAgent)) {
	// backup check box values
   
	st.onbeforesort = function () {
		var table = st.element;
		var inputs = table.getElementsByTagName("INPUT");

		var l = inputs.length;
           
		
		for (var i = 0; i < l; i++) {
			inputs[i].parentNode._checked = inputs[i].checked;
		}

		
	};

	// restore check box values
	st.onsort = function () {		
		var table = st.element;
		var inputs = table.getElementsByTagName("INPUT");
		var l = inputs.length;
		for (var i = 0; i < l; i++) {
			inputs[i].checked = inputs[i].parentNode._checked;
		}
		
	};
}
//]]>
</script>
	</center>
	</body>
</html>
