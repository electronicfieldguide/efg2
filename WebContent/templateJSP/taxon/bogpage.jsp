<%@page
	import="java.io.File,java.util.List,java.util.ArrayList,
	java.util.Iterator,java.util.Hashtable,
	project.efg.server.utils.EFGDataSourceHelperInterface,
	project.efg.util.interfaces.EFGImportConstants,
	project.efg.server.utils.TemplateProducer,
	project.efg.server.utils.TemplatePopulator,
	project.efg.util.interfaces.EFGQueueObjectInterface"%>
<%@ include file="bogpageHeader.jsp"%>
<body>
<form method="post" id="efg_form" action="<%=context%>/configTaxonPage" onsubmit="return removeConstants();">
<%
if (cssFileList.length > 0) {
%> <select name="<%=name%>" title="Select  a css file from list">
	<%
				for (ii = 0; ii < cssFileList.length; ii++) {
				File currentCSSFile = cssFileList[ii];
				fieldName = currentCSSFile.getName();
				if (fieldName.equals(fieldValue)) {
	%>
	<option selected="selected"><%=fieldName%></option>
	<%
	} else {
	%>
	<option><%=fieldName%></option>
	<%
			}
			}
	%>
</select><br />
<%
	}//end if css
	name = tp.getCharacter(isNew, isNew);
	characterText = tp.getCurrentCharacterText(name);
	characterValue = (String) groupTable.get(characterText);

	if (characterValue == null) {
		characterValue = "";
	}

	groupLabel = tp.getCurrentGroupLabel(name);

	groupLabelValue = (String) groupTable.get(groupLabel);
	if (groupLabelValue == null) {
		groupLabelValue = "title";
	}
%> <input type="hidden" name="<%=groupLabel%>"
	value="<%=groupLabelValue%>" /> <span class="title"><input
	type="text" size="75" title="ENTER PAGE TITLE HERE"
	name="<%=characterText%>" value="<%=characterValue%>" /> </span> <%
 	name = tp.getCharacter(isOld, isOld);
 	characterText = tp.getCurrentCharacterText(name);
 	characterValue = (String) groupTable.get(characterText);

 	if (characterValue == null) {
 		characterValue = "";
 	}
 %> <span class="pageauthor"><input type="text"
	title="ENTER AUTHOR HERE" name="<%=characterText%>"
	value="<%=characterValue%>" size="100"/> </span>
<table>
	<tr>


		<%
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
				groupValue = "";
			}
			groupLabel = tp.getCurrentGroupLabel(name);
			groupLabelValue = (String) groupTable.get(groupLabel);
			if (groupLabelValue == null) {
				groupLabelValue = "header";
			}
		%>

		<td class="headingtop"><input size="20" type="text"
			title="ENTER HEADER FOR ROW" name="<%=groupText%>"
			value="<%=groupValue%>" /> <input type="hidden"
			name="<%=groupLabel%>" value="<%=groupLabelValue%>" /></td>
		<td class="datatop">
		<%
			fieldValue = (String) groupTable.get(name);
			if (fieldValue == null) {
				fieldValue = "";
			}
			characterLabel = tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String) groupTable.get(characterLabel);
			if (characterLabelValue == null) {
				characterLabelValue = "item";
			}
		%>
		<div class="comname"><select name="<%=name%>"
			title="Select a column from the list">
			<%
				ii = 0;
				it = table.iterator();
				while (it.hasNext()) {
					EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
					.next();
					if (isImagesExists) {
						if (mediaResourceFields.contains(queueObject)) {
					continue;
						}
					}
					if (isListsExists) {
						if (efgList.contains(queueObject)) {
					continue;
						}
					}

					fieldName = queueObject.getObject(1);
					if (ii == 0) {
			%>
			<option>
			<%
					}
					if (fieldName.equals(fieldValue)) {
			%>
			
			<option selected="selected"><%=fieldName%></option>
			<%
			} else {
			%>
			<option><%=fieldName%></option>
			<%
					}
					ii++;
				}//end while
			%>
		</select> <input type="hidden" name="<%=characterLabel%>"
			value="<%=characterLabelValue%>" /></div>
		<div class="sciname">
		<%
			name = tp.getCharacter(isOld, isOld);
			fieldValue = (String) groupTable.get(name);
			if (fieldValue == null) {
				fieldValue = "";
			}
			characterLabel = tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String) groupTable.get(characterLabel);
			if (characterLabelValue == null) {
				characterLabelValue = "item";
			}
		%> <select name="<%=name%>" title="Select A Field From List"
			style="width:100px;">
			<%
				ii = 0;
				it = table.iterator();
				while (it.hasNext()) {
					EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
					.next();
					if (isImagesExists) {
						if (mediaResourceFields.contains(queueObject)) {
					continue;
						}
					}
					if (isListsExists) {
						if (efgList.contains(queueObject)) {
					continue;
						}
					}

					fieldName = queueObject.getObject(1);
					if (ii == 0) {
			%>
			<option>
			<%
					}
					if (fieldName.equals(fieldValue)) {
			%>
			
			<option selected="selected"><%=fieldName%></option>
			<%
			} else {
			%>
			<option><%=fieldName%></option>
			<%
					}
					ii++;
				}//end while
			%>
		</select> <input type="hidden" name="<%=characterLabel%>"
			value="<%=characterLabelValue%>" /> <%
 	name = tp.getCharacter(isOld, isOld);
 	fieldValue = (String) groupTable.get(name);
 	if (fieldValue == null) {
 		fieldValue = "";
 	}
 	characterLabel = tp.getCurrentCharacterLabel(name);
 	characterLabelValue = (String) groupTable.get(characterLabel);
 	if (characterLabelValue == null) {
 		characterLabelValue = "item";
 	}
 %> <select name="<%=name%>" title="Select A Field From List"
			style="width:100px;">
			<%
				ii = 0;
				it = table.iterator();
				while (it.hasNext()) {
					EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
					.next();
					if (isImagesExists) {
						if (mediaResourceFields.contains(queueObject)) {
					continue;
						}
					}
					if (isListsExists) {
						if (efgList.contains(queueObject)) {
					continue;
						}
					}

					fieldName = queueObject.getObject(1);
					if (ii == 0) {
			%>
			<option>
			<%
					}
					if (fieldName.equals(fieldValue)) {
			%>
			
			<option selected="selected"><%=fieldName%></option>
			<%
			} else {
			%>
			<option><%=fieldName%></option>
			<%
					}
					ii++;
				}//end while
			%>
		</select> <input type="hidden" name="<%=characterLabel%>"
			value="<%=characterLabelValue%>" /> <span class="scinameauth">
		<%
			name = tp.getCharacter(isOld, isOld);
			fieldValue = (String) groupTable.get(name);
			if (fieldValue == null) {
				fieldValue = "";
			}
			characterLabel = tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String) groupTable.get(characterLabel);
			if (characterLabelValue == null) {
				characterLabelValue = "item";
			}
		%> <select name="<%=name%>" title="Select A Field From List"
			style="width:100px;">
			<%
				ii = 0;
				it = table.iterator();
				while (it.hasNext()) {
					EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
					.next();
					if (isImagesExists) {
						if (mediaResourceFields.contains(queueObject)) {
					continue;
						}
					}
					if (isListsExists) {
						if (efgList.contains(queueObject)) {
					continue;
						}
					}

					fieldName = queueObject.getObject(1);
					if (ii == 0) {
			%>
			<option>
			<%
					}
					if (fieldName.equals(fieldValue)) {
			%>
			
			<option selected="selected"><%=fieldName%></option>
			<%
			} else {
			%>
			<option><%=fieldName%></option>
			<%
					}
					ii++;
				}//end while
			%>
		</select> <input type="hidden" name="<%=characterLabel%>"
			value="<%=characterLabelValue%>" /> </span></div>
		<div class="family">
		<%
			name = tp.getCharacter(isOld, isOld);
			fieldValue = (String) groupTable.get(name);
			if (fieldValue == null) {
				fieldValue = "";
			}
			characterLabel = tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String) groupTable.get(characterLabel);
			if (characterLabelValue == null) {
				characterLabelValue = "item";
			}
		%> <select name="<%=name%>" title="Select A Field From List"
			style="width:150px;">
			<%
				ii = 0;
				it = table.iterator();
				while (it.hasNext()) {
					EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
					.next();
					if (isImagesExists) {
						if (mediaResourceFields.contains(queueObject)) {
					continue;
						}
					}
					if (isListsExists) {
						if (efgList.contains(queueObject)) {
					continue;
						}
					}

					fieldName = queueObject.getObject(1);
					if (ii == 0) {
			%>
			<option>
			<%
					}
					if (fieldName.equals(fieldValue)) {
			%>
			
			<option selected="selected"><%=fieldName%></option>
			<%
			} else {
			%>
			<option><%=fieldName%></option>
			<%
					}
					ii++;
				}//while
			%>
		</select> <input type="hidden" name="<%=characterLabel%>"
			value="<%=characterLabelValue%>" /> ( <%
 	name = tp.getCharacter(isOld, isOld);
 	fieldValue = (String) groupTable.get(name);
 	if (fieldValue == null) {
 		fieldValue = "";
 	}
 	characterLabel = tp.getCurrentCharacterLabel(name);
 	characterLabelValue = (String) groupTable.get(characterLabel);
 	if (characterLabelValue == null) {
 		characterLabelValue = "item";
 	}
 %> <select name="<%=name%>" title="Select A Field From List"
			style="width:150px;">
			<%
				ii = 0;
				it = table.iterator();
				while (it.hasNext()) {
					EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
					.next();
					if (isImagesExists) {
						if (mediaResourceFields.contains(queueObject)) {
					continue;
						}
					}
					if (isListsExists) {
						if (efgList.contains(queueObject)) {
					continue;
						}
					}

					fieldName = queueObject.getObject(1);
					if (ii == 0) {
			%>
			<option>
			<%
					}
					if (fieldName.equals(fieldValue)) {
			%>
			
			<option selected="selected"><%=fieldName%></option>
			<%
			} else {
			%>
			<option><%=fieldName%></option>
			<%
					}
					ii++;
				}//end while
			%>
		</select> <input type="hidden" name="<%=characterLabel%>"
			value="<%=characterLabelValue%>" /> )</div>
		</td>
		<td class="imagestop">
		<%
			name = tp.getCharacter(isOld, isOld);
			if (isImagesExists) {
				fieldValue = (String) groupTable.get(name);
				if (fieldValue == null) {
					fieldValue = "";
				}
				characterLabel = tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String) groupTable.get(characterLabel);
				if (characterLabelValue == null) {
					characterLabelValue = "image";
				}
		%> <select name="<%=name%>"
			title="Select An Image Field From List">
			<%
					ii = 0;
					it = mediaResourceFields.iterator();

					while (it.hasNext()) {
						EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
						.next();
						fieldName = queueObject.getObject(1);
						if (ii == 0) {
			%>
			<option>
			<%
						}
						if (fieldName.equals(fieldValue)) {
			%>
			
			<option selected="selected"><%=fieldName%></option>
			<%
			} else {
			%>
			<option><%=fieldName%></option>
			<%
						}
						ii++;
					}// end while
			%>
		</select> <input type="hidden" name="<%=characterLabel%>"
			value="<%=characterLabelValue%>" /> <%
 		characterText = tp.getCurrentCharacterText(name);
 		characterValue = (String) groupTable.get(characterText);

 		if (characterValue == null) {
 			characterValue = "";
 		}
 %> <br />
		<span class="imagecaption"> <input type="text"
			name="<%=characterText%>" value="<%=characterValue%>" /> </span> <%
 }//end if images
 %>
		</td>
	</tr>
	<%
			for (int zz = 0; zz < table.size(); zz++) {
			EFGQueueObjectInterface queueObject1 = (EFGQueueObjectInterface) table
			.get(zz);
			if (isImagesExists) {
				if (mediaResourceFields.contains(queueObject1)) {
			continue;
				}
			}
			if (isListsExists) {
				if (efgList.contains(queueObject1)) {
			continue;
				}
			}

			name = tp.getCharacter(isNew, isNew);
			fieldValue = (String) groupTable.get(name);
			if (fieldValue == null) {
				fieldValue = "";
			}//end if fieldValue == null		

			groupText = tp.getCurrentGroupText(name);
			groupArr = groupText.split(":");

			if (groupArr.length > 3) {
				groupKey = groupText.substring(0, groupText
				.lastIndexOf(":"));
			} else {
				groupKey = groupText;
			}
			groupValue = (String) groupTable.get(groupKey);
			if (groupValue == null) {
				groupValue = "";
			}

			groupLabel = tp.getCurrentGroupLabel(name);
			groupLabelValue = (String) groupTable.get(groupLabel);
			if (groupLabelValue == null) {
				groupLabelValue = "items";
			}
			characterLabel = tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String) groupTable.get(characterLabel);
			if (characterLabelValue == null) {
				characterLabelValue = "item";
			}
	%>
	<tr>
		<td class="heading"><input size="20" type="text" title=""
			name="<%=groupText%>" value="<%=groupValue%>" /> <input type="hidden"
			name="<%=groupLabel%>" value="<%=groupLabelValue%>" /></td>
		<td class="data"><select name="<%=name%>"
			title="Select A Field From List">
			<%
					ii = 0;
					it = table.iterator();
					while (it.hasNext()) {

						EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
						.next();
						if (isImagesExists) {
					if (mediaResourceFields.contains(queueObject)) {
						continue;
					}
						}
						if (isListsExists) {
					if (efgList.contains(queueObject)) {
						continue;
					}
						}
						if (ii == 0) {
			%>
			<option>
			<%
						}//end if ii =0 
						fieldName = queueObject.getObject(1);
						if (fieldName.equals(fieldValue)) {
			%>
			
			<option selected="selected"><%=fieldName%></option>
			<%
						}//fieldName.equals(fieldValue)
						else {
			%>
			<option><%=fieldName%></option>
			<%
						}//end else
						ii++;
					}//end while
			%>
		</select> <input type="hidden" name="<%=characterLabel%>"
			value="<%=characterLabelValue%>" /></td>
		<td class="images">
		<%
				name = tp.getCharacter(isOld, isOld);
				if (isImagesExists) {
					fieldValue = (String) groupTable.get(name);
					if (fieldValue == null) {
				fieldValue = "";
					}
					characterLabel = tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String) groupTable
					.get(characterLabel);
					if (characterLabelValue == null) {
				characterLabelValue = "image";
					}
		%> <select name="<%=name%>"
			title="Select An Image Field From List">
			<%
						int yy = 0;
						it = mediaResourceFields.iterator();

						while (it.hasNext()) {
					EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
							.next();
					fieldName = queueObject.getObject(1);
					if (yy == 0) {
			%>
			<option>
			<%
					}
					if (fieldName.equals(fieldValue)) {
			%>
			
			<option selected="selected"><%=fieldName%></option>
			<%
			} else {
			%>
			<option><%=fieldName%></option>
			<%
					}
					yy++;
						}// end while
			%>
		</select> <input type="hidden" name="<%=characterLabel%>"
			value="<%=characterLabelValue%>" /> <%
 			characterText = tp.getCurrentCharacterText(name);
 			characterValue = (String) groupTable.get(characterText);

 			if (characterValue == null) {
 		characterValue = "";
 			}
 %> <br />
		<span class="imagecaption"> <input type="text"
			name="<%=characterText%>" value="<%=characterValue%>" /> </span> <%
 }//end if images
 %>
		</td>
	</tr>
	<%
		}//end outer for 

		for (int zz = 0; zz < efgList.size(); zz++) {
			EFGQueueObjectInterface queueObject1 = (EFGQueueObjectInterface) efgList
			.get(zz);

			name = tp.getCharacter(isNew, isNew);
			fieldValue = (String) groupTable.get(name);
			if (fieldValue == null) {
				fieldValue = "";
			}//end if fieldValue == null		
			groupText = tp.getCurrentGroupText(name);
			groupArr = groupText.split(":");

			if (groupArr.length > 3) {
				groupKey = groupText.substring(0, groupText
				.lastIndexOf(":"));
			} else {
				groupKey = groupText;
			}
			groupValue = (String) groupTable.get(groupKey);
			if (groupValue == null) {
				groupValue = "";
			}
			groupLabel = tp.getCurrentGroupLabel(name);
			groupLabelValue = (String) groupTable.get(groupLabel);
			if (groupLabelValue == null) {
				groupLabelValue = "lists";
			}
	%>
	<tr>
		<td class="heading"><input size="20" type="text" title=""
			name="<%=groupText%>" value="<%=groupValue%>" /> <input type="hidden"
			name="<%=groupLabel%>" value="<%=groupLabelValue%>" /></td>
		<td class="data">
		<table class="simspp">
			<%
						for (int zz2 = 0; zz2 < efgList.size(); zz2++) {
						queueObject1 = (EFGQueueObjectInterface) efgList.get(zz);
						name = tp.getCharacter(isOld, isOld);
						fieldValue = (String) groupTable.get(name);
						if (fieldValue == null) {
					fieldValue = "";
						}//end if fieldValue == null		
						characterText = tp.getCurrentCharacterText(name);
						characterValue = (String) groupTable.get(characterText);
						if (characterValue == null) {
					characterValue = "";
						}
						characterLabel = tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String) groupTable
						.get(characterLabel);
						if (characterLabelValue == null) {
					characterLabelValue = "list";
						}
			%>
			<tr>
				<td class="assochead"><input size="20" type="text" title=""
					name="<%=characterText%>" value="<%=characterValue%>" /></td>

				<td class="simspptext"><input type="hidden"
					name="<%=characterLabel%>" value="<%=characterLabelValue%>" />
				<div class="itemlist"><select name="<%=name%>"
					title="Select A Field From List" style="width:140px;">
					<%
								int zii = 0;
								Iterator it1 = efgList.iterator();
								while (it1.hasNext()) {
							if (zii == 0) {
					%>
					<option>
					<%
							}//end if ii =0 
							EFGQueueObjectInterface queueObject2 = (EFGQueueObjectInterface) it1
									.next();
							fieldName = queueObject2.getObject(1);
							if (fieldName.equals(fieldValue)) {
					%>
					
					<option selected="selected"><%=fieldName%></option>
					<%
							}//fieldName.equals(fieldValue)
							else {
					%>
					<option><%=fieldName%></option>
					<%
							}//end else
							zii++;
								}//end while
					%>
				</select></div>
				</td>
			</tr>
			<%
			}//end for
			%>
		</table>
		</td>
		<td class="image_capsule">
		<table class="images">
			<%
						if (isImagesExists) {
						for (int zz2 = 0; zz2 < efgList.size(); zz2++) {
					name = tp.getCharacter(isOld, isOld);
					fieldValue = (String) groupTable.get(name);
					if (fieldValue == null) {
						fieldValue = "";
					}
					characterLabel = tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String) groupTable
							.get(characterLabel);
					if (characterLabelValue == null) {
						characterLabelValue = "image";
					}
					characterText1 = tp.getCurrentCharacterText(name);
					characterValue1 = (String) groupTable
							.get(characterText1);
					if (characterValue1 == null) {
						characterValue1 = "";
					}
			%>
			<tr>
				<td class="images_new"><select name="<%=name%>"
					title="Select An Image Field From List" style="width:120px;">
					<%
							int yy = 0;
							it = mediaResourceFields.iterator();
							while (it.hasNext()) {
								EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
								.next();
								fieldName = queueObject.getObject(1);
								if (yy == 0) {
					%>
					<option>
					<%
								}
								if (fieldName.equals(fieldValue)) {
					%>
					
					<option selected="selected"><%=fieldName%></option>
					<%
					} else {
					%>
					<option><%=fieldName%></option>
					<%
								}
								yy++;
							}// end while
					%>
				</select> <input type="hidden" name="<%=characterLabel%>"
					value="<%=characterLabelValue%>" /></td>
				<td class="images_new">
				<%
						name = tp.getCharacter(isOld, isOld);
						fieldValue = (String) groupTable.get(name);
						if (fieldValue == null) {
							fieldValue = "";
						}
						characterLabel = tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String) groupTable
								.get(characterLabel);
						if (characterLabelValue == null) {
							characterLabelValue = "image";
						}
						characterText2 = tp.getCurrentCharacterText(name);
						characterValue2 = (String) groupTable
								.get(characterText2);
						if (characterValue2 == null) {
							characterValue2 = "";
						}
				%> <select name="<%=name%>"
					title="Select An Image Field From List" style="width:120px;">
					<%
							yy = 0;
							it = mediaResourceFields.iterator();
							while (it.hasNext()) {
								EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface) it
								.next();
								fieldName = queueObject.getObject(1);
								if (yy == 0) {
					%>
					<option>
					<%
								}
								if (fieldName.equals(fieldValue)) {
					%>
					
					<option selected="selected"><%=fieldName%></option>
					<%
					} else {
					%>
					<option><%=fieldName%></option>
					<%
								}
								yy++;
							}// end while
					%>
				</select> <input type="hidden" name="<%=characterLabel%>"
					value="<%=characterLabelValue%>" /></td>
			</tr>
			<tr>
				<td class="imagecaption"><input type="text"
					name="<%=characterText1%>" value="<%=characterValue1%>" /></td>
				<td class="imagecaption"><input type="text"
					name="<%=characterText2%>" value="<%=characterValue2%>" /></td>
			</tr>
			<%
					}//end for
					} //end if images
			%>
		</table>
		</td>
	</tr>
	<%
	}//end outer for
	%>
</table>
<%
	String copyrightname = tp.getGroupText(isNew, isNew);
	String copyrightfieldValue = (String) groupTable.get(copyrightname);
	if (copyrightfieldValue == null) {
		copyrightfieldValue = "";
	}
	String copyrightgroupLabel = tp.getCurrentGroupLabel(copyrightname);
	String copyrightgroupLabelValue = (String) groupTable.get(copyrightgroupLabel);
	if (copyrightgroupLabelValue == null) {
		copyrightgroupLabelValue = "copyright";
	}
%> 
<hr />
<p class="link">
				<%
					name =tp.getCharacter(isNew,isNew);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue = LINK_URL_TEXT;
					}
					groupLabel= tp.getCurrentGroupLabel(name);
					groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){		
						groupLabelValue ="bottomLinks";
					
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue = LINK_TEXT;
						
					}
				%>				
				<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>	
				<input type="hidden" name="<%=groupLabel%>"
					value="<%=groupLabelValue%>" /> 
					
				<%if(characterLabelValue.equals(LINK_TEXT)){ %>
				<input size="20" maxlength="19" type="text" title="" name="<%=characterLabel%>"
				value="<%=characterLabelValue%>" class="cleardefault"/>
			<%} else{ %>	
				 <input type="text" name="<%=characterLabel%>" size="20"
					maxlength="19" value="<%=characterLabelValue%>" />
			<%}%>	
			
			<%if(fieldValue.equals(LINK_URL_TEXT)){ %>
				<input size="40" maxlength="39" type="text" title="" name="<%=name%>"
				value="<%=fieldValue%>" class="cleardefault"/>
			<%} else{ %>	
				 <input type="text" name="<%=name%>" size="40" maxlength="39" value="<%=fieldValue%>" />
			<%}			    
			   	boolean moreData = true;
			    
			    while(moreData){
				    name =tp.getCharacter(false,false);
		 			
					fieldValue = (String)groupTable.get(name);
					
					if(fieldValue == null){
						fieldValue = LINK_URL_TEXT;
						moreData = false;
						break;
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						moreData = false;
						characterLabelValue = LINK_TEXT;
						break;
					}
					%>
					
						<%if(characterLabelValue.equals(LINK_TEXT)){ %>
				<input size="20" maxlength="19" type="text" title="" name="<%=characterLabel%>"
				value="<%=characterLabelValue%>" class="cleardefault"/>
			<%} else{ %>	
				 <input type="text" name="<%=characterLabel%>" size="20"
					maxlength="19" value="<%=characterLabelValue%>" />
			<%}%>	
			
			<%if(fieldValue.equals(LINK_URL_TEXT)){ %>
				<input size="40" maxlength="39" type="text" title="" name="<%=name%>"
				value="<%=fieldValue%>" class="cleardefault"/>
			<%} else{ %>	
				 <input type="text" name="<%=name%>" size="40" maxlength="39" value="<%=fieldValue%>" />
			<%}
					
					

			   	 }
			    	//find out if there are more existing
		 			session.setAttribute("templateProducer",tp);
		 			session.setAttribute("groupTable",groupTable);
				%>
				<input class="addlink" name="efg_btn" id="efg_btn" value="Add new link"
	onclick="javascript:AddLink('CreateMoreLinks.jsp','nameValueID',this.id)" type="button" />
</p>
<hr/>
<span class="copyright"> <input size="100" type="text"
	title="ENTER CREDIT INFORMATION" name="<%=copyrightname%>"
	value="<%=copyrightfieldValue%>" /> <input type="hidden" name="<%=copyrightgroupLabel%>"
	value="<%=copyrightgroupLabelValue%>" /> </span> 
	<%@ include file="footerTaxonPage.jsp"%>

</form>
<input type="hidden" id="nameValueID" name="nameValue" value="<%=name%>"/>

</body>
</html>
