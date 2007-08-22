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

String characterLabelValue = null;
String characterLabel = null;


String fieldName = null;
String characterText = null;
String characterValue = null;
String fieldValue = null;

boolean isNew = true;
boolean isOld = false;
	TemplateProducer tp =(TemplateProducer)session.getAttribute("templateProducer");
	EFGDataObjectListInterface doSearches  = 
		(EFGDataObjectListInterface)session.getAttribute("dosearches");
	Hashtable groupTable= (Hashtable)session.getAttribute("groupTable");

	String name = request.getParameter("nameValue");
	
	if(name == null || "".equals(name.trim())){
		name = tp.getCharacter(isOld, isOld);
	}
	
	fieldValue = (String) groupTable.get(name);
	if (fieldValue == null) {
		fieldValue = "";
	}//end if fieldValue == null		
 		
 		characterText = tp.getCurrentCharacterText(name);
 		characterValue = (String) groupTable.get(characterText);
 		if (characterValue == null) {
 			characterValue = DEFAULT_FIELD_TEXT;
 		}%>
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
	 			<option selected="selected"><%=fieldName%></option>
	 		<% } else{%>
	 			<option><%=fieldName%></option>			 				
	 		<%}
		}
	%>
	</select>	
	<input type="hidden" name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  			
	</td>


					
