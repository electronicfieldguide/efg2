<%@page import="java.util.Iterator,
project.efg.server.utils.TemplateProducer,
project.efg.util.interfaces.EFGQueueObjectInterface,
project.efg.util.interfaces.EFGImportConstants,
java.util.Hashtable,
project.efg.util.interfaces.EFGDataObject,
java.util.List
" %>
<%@page import="project.efg.server.interfaces.EFGDataObjectListInterface"%>
<%@ include file="commonTemplateCode/ConstantLinks.jsp"%>
<%
String groupLabel= null;
String groupLabelValue = null;
String characterLabelValue = null;
String characterLabel = null;
String groupText = null;
String groupKey = null;
String groupValue = null;
String fieldName = null;
String characterText = null;
String characterValue = null;
String fieldValue = null;
String name = null;
boolean isNew = true;
boolean isOld = false;
	
	TemplateProducer tp =(TemplateProducer)session.getAttribute("templateProducer");
	EFGDataObjectListInterface doSearches  = 
		(EFGDataObjectListInterface)session.getAttribute("dosearches");
	Hashtable groupTable= (Hashtable)session.getAttribute("groupTable");

		name = tp.getCharacter(isNew, isNew);

		groupText = tp.getCurrentGroupText(name);

		String[] groupArr = groupText.split(":");

		if (groupArr.length > 3) {
			groupKey = groupText.substring(0, groupText.lastIndexOf(":"));
		} else {
			groupKey = groupText;
		}
		groupValue = (String) groupTable.get(groupKey);
		if (groupValue == null) {
			groupValue = DEFAULT_GROUP_TEXT;
		}
		groupLabel = tp.getCurrentGroupLabel(name);
		groupLabelValue = (String) groupTable.get(groupLabel);
		if (groupLabelValue == null) {
			groupLabelValue = "fieldheader";
		}
	 	%> 
<tr>
	<th class="search">
	<input type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>" />
	<%if(groupValue.equals(DEFAULT_GROUP_TEXT)){%>
		<input size="50" type="text"
		title="<%=DEFAULT_GROUP_TEXT%>" name="<%=groupText%>"
		value="<%=groupValue%>" class="cleardefault"></input> 
		
	<%} else{ %>
		<input size="50" type="text"
		title="<%=DEFAULT_GROUP_TEXT%>" name="<%=groupText%>"
		value="<%=groupValue%>"></input> 
	<%}  %>
	</th>
</tr>
<tr>
	<td class="paddertopleft"></td>
	<td class="paddertopright"></td>
</tr>
<%
	boolean moreData = true;
	while(moreData){
		if(!moreData){
			break;
		}
 		name = tp.getCharacter(isOld, isOld);
 		characterText = tp.getCurrentCharacterText(name);
 		characterValue = (String) groupTable.get(characterText);
 		if (characterValue == null) {
 			characterValue = DEFAULT_FIELD_TEXT;
 		}%>
 	<tr>
 		
	<td class="sectionleft">
	<%if(characterValue.equals(DEFAULT_FIELD_TEXT)){ %>
		<input size="45" type="text" title="<%=DEFAULT_FIELD_TEXT%>" name="<%=characterText%>"
		value="<%=characterValue%>" class="cleardefault"></input>
	<%} else{ %>	
		<input size="45" type="text" title="<%=DEFAULT_FIELD_TEXT%>" name="<%=characterText%>"
		value="<%=characterValue%>"></input>
	<%}%>		
		:</td>
	<td class="sectionright">
		<select name="<%=name%>" class="efg_select_marker">
		<%
 		fieldValue = (String) groupTable.get(name);
			if (fieldValue == null) {
				fieldValue = "";
				moreData = false;
			}//end if fieldValue == null		
		
			characterLabel = tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String) groupTable
				.get(characterLabel);
 			if (characterLabelValue  == null) {
 				characterLabelValue  = "field";
 				//moreData = false;
 			}

		%>
		
		<% 
		for(int s = 0 ; s < doSearches.getEFGDataObjectCount(); s++){
 		
			EFGDataObject searchable = doSearches.getEFGDataObject(s);
		   		
	 		fieldName =searchable.getName();
	 		if(s == 0){
		 		%>
		 		<option></option>
		 		<% }
	 		if(fieldName.equals(fieldValue)){%>
	 			<option selected="selected"><%=fieldValue%></option>
	 		<% } else{%>
	 			<option><%=fieldName%></option>			 				
	 		<%}
		}
	%>
	</select>	
	<input type="hidden" name="<%=characterLabel%>" value="<%=characterLabelValue%>"></input>  			
	</td>
	
</tr>	
<%
	}
	String newName = name.replace(':','_');
	String nameValueID = newName + "_newValue";
	StringBuffer onclickString = new StringBuffer();
	onclickString.append("javascript:AddRows('CreateFieldGroup.jsp','");
	onclickString.append(nameValueID);
	onclickString.append("','");
	onclickString.append(newName);
	onclickString.append("')");
%>
	<tr id="<%=newName%>">
		<td class="padderbottomleft"></td>
		<td class="padderbottomright">
			<input type="hidden" id="<%=nameValueID%>" name="nameValue" value="<%=name%>"/>
			<input type="button" name="efg_btn" value="Add another category" onclick="<%=onclickString.toString()%>"/>
		</td>
	</tr>

<tr>
	<td class="spacer"></td>
</tr>


					
