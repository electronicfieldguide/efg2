<%@page import="
java.io.File,
java.util.List,
java.util.Iterator,
java.util.Hashtable,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.util.EFGImportConstants,
project.efg.util.TemplateProducer,
project.efg.util.TemplatePopulator,
project.efg.Imports.efgInterface.EFGQueueObjectInterface
" %>
<html>
	
	<%@ include file="morningGloriesTemplatePlateHeader.jsp" %>
	<body bgcolor="#dcdcdc">
		<form method="post" action="<%=context%>/configTaxonPage">
				<hr size="2"/>
				<table border="0" cellspacing="2" cellpadding="0" bgcolor="#f5f5f5">
					<tr>
						<td valign="top">
							<h1>
							<%
								name =tp.getCharacter(isNew,isNew);
								fieldValue = (String)groupTable.get(name);
								if(fieldValue == null){
									fieldValue ="";
								}
								groupLabel= tp.getCurrentGroupLabel(name);
								groupLabelValue = (String)groupTable.get(groupLabel);
								if(groupLabelValue == null){
									groupLabelValue ="htmltitles";
								}
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
									if(characterLabelValue == null){
										characterLabelValue ="htmltitle";
									}
								%>
									 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
									<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
									<input size="100" type="text"  title="ENTER PAGE TITLE HERE" name="<%=name%>" value="<%=fieldValue%>"/>
									<br/>
									<%
									name = tp.getCharacter(isOld,isOld);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
									}
									characterLabel= tp.getCurrentCharacterLabel(name);
									characterLabelValue = (String)groupTable.get(characterLabel);
									if(characterLabelValue == null){
										characterLabelValue ="htmltitle";
									}
								%>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input size="100" type="text"  title="ENTER PAGE TITLE CONTINUATION HERE" name="<%=name%>" value="<%=fieldValue%>"/>
							</h1>
						</td>
						<td rowspan="3">
								<%
								name =tp.getCharacter(isNew,isNew);
								fieldValue = (String)groupTable.get(name);
								if(fieldValue == null){
									fieldValue ="";
								}
								groupLabel= tp.getCurrentGroupLabel(name);
								groupLabelValue = (String)groupTable.get(groupLabel);
								if(groupLabelValue == null){
									groupLabelValue ="imageheaders";
								}
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="imageheader";
								}
							%>
							 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
								<select name="<%=name%>"  title="Select an image from List">
								<%
									for (ii=0; ii<imageFileList.length; ii++ ) {
										File imgFile = imageFileList[ii];
										fieldName = imgFile.getName();
										if(ii==0){
										%>
											<option>
										<%
										}
										if(fieldName.equals(fieldValue)){
										%>
											<option selected="selected"><%=fieldName%></option>
										<%
										}
										else{
										%>
											<option><%=fieldName%></option>
										<%
										}
									}
								%>
								</select>		
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						</td>
					</tr>
					<tr>
						<td rowspan="2" valign="top">
							<%
								name =tp.getCharacter(isNew,isNew);
								fieldValue = (String)groupTable.get(name);
								if(fieldValue == null){
									fieldValue ="";
								}
								groupLabel= tp.getCurrentGroupLabel(name);
								groupLabelValue = (String)groupTable.get(groupLabel);
								if(groupLabelValue == null){
									groupLabelValue ="addresses";
								}
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="address";
								}
							%>
							 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
								<input size="100" type="text"  title="address1" name="<%=name%>" value="<%=fieldValue%>"/><br/>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<%
							name = tp.getCharacter(isOld,isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="address";
								}
								%>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input size="100" type="text"  title="address2" name="<%=name%>" value="<%=fieldValue%>"/><br/>
								<%
							name = tp.getCharacter(isOld,isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="address";
								}
								%>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input size="100" type="text"  title="address3" name="<%=name%>" value="<%=fieldValue%>"/>
								
						<%
							name = tp.getCharacter(isOld,isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="address";
								}
								%>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input size="100" type="text"  title="address4" name="<%=name%>" value="<%=fieldValue%>"/>

						</td>
					</tr>
					<tr>
					</tr>
				</table>
				<hr size="2"/>
				<table border="0" cellspacing="2" cellpadding="0" bgcolor="#f5f5f5">
					<tr>
						<td>
						<%
							name =tp.getCharacter(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							groupLabel= tp.getCurrentGroupLabel(name);
							groupLabelValue = (String)groupTable.get(groupLabel);
							if(groupLabelValue == null){
								groupLabelValue ="titles";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="title";
								}
							%>
							 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>				
							<input size="50" type="text"  title="ENTER TITLE" name="<%=name%>" value="<%=fieldValue%>"/>
						</td>
					</tr>
					<tr>
						<td>
						<%
							name =tp.getCharacter(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							groupLabel= tp.getCurrentGroupLabel(name);
							groupLabelValue = (String)groupTable.get(groupLabel);
							if(groupLabelValue == null){
								groupLabelValue ="intros";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="intro";
								}
							%>
							 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>															
							<input size="200" type="text"  title="ENTER INTRO HERE" name="<%=name%>" value="<%=fieldValue%>"/>
						</td>
					</tr>
				</table>
				<hr size="2"/>
				<table border="0" cellspacing="2" cellpadding="4" bgcolor="#f5f5f5">
					<tr>
						<%
							name =tp.getCharacter(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							groupLabel= tp.getCurrentGroupLabel(name);
							groupLabelValue = (String)groupTable.get(groupLabel);
							if(groupLabelValue == null){
								groupLabelValue ="headers";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue ="header";
							}
						%>
									
						<td>
						<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>															
							<input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/>
						</td>
						<td>
							<%
							name = tp.getCharacter(isOld,isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue ="header";
							}
							%>
							<input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/>
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>															
						</td>
						<td>
							<%
							name = tp.getCharacter(isOld,isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue ="header";
							}
						%>
							<input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/>
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>															
						</td>
						<td>
							<%
							name = tp.getCharacter(isOld,isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue ="header";
							}
							%>
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>															
							<input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/>
						</td>
					</tr>
					<tr>
						<td nowrap>
						<%
						name =tp.getCharacter(isNew,isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							groupLabelValue ="items";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue ="item";
							}
						%>
							 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
							<select name="<%=name%>"  title="Select a column from the list">
								<%
									ii = 0;
									it = table.iterator();
									while (it.hasNext()) {
										EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
									 if(isImagesExists) {
										if(mediaResourceFields.contains(queueObject)){
											continue;
										}
									}	
										fieldName = queueObject.getObject(1);
										if(ii==0){
										%>
												<option>
										<%
										}
										if(fieldName.equals(fieldValue)){
										%>
												<option selected="selected"><%=fieldName%></option>
										<%
										}
										else{
										%>
												<option><%=fieldName%></option>
										<%
										}
										ii++;
									}
								%>
							</select>   
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>															
					<%
						name =tp.getCharacter(isOld,isOld);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue ="item";
						}
					%>	
							<select name="<%=name%>"  title="Select a column from the list">
								<%
									ii = 0;
									it = table.iterator();
									while (it.hasNext()) {
										EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
									 if(isImagesExists) {
										if(mediaResourceFields.contains(queueObject)){
											continue;
										}
									}	
										fieldName = queueObject.getObject(1);
										if(ii==0){
										%>
												<option>
										<%
										}
										if(fieldName.equals(fieldValue)){
										%>
												<option selected="selected"><%=fieldName%></option>
										<%
										}
										else{
										%>
												<option><%=fieldName%></option>
										<%
										}
										ii++;
									}
								%>
							</select> 
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>																		
 
						</td>
						<td>
					<%
						name =tp.getCharacter(isOld,isOld);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue ="item";
						}
					%>	
								<select name="<%=name%>"  title="Select a column from the list">
								<%
									ii = 0;
									it = table.iterator();
									while (it.hasNext()) {
										EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
									 if(isImagesExists) {
										if(mediaResourceFields.contains(queueObject)){
											continue;
										}
									}	
										fieldName = queueObject.getObject(1);
										if(ii==0){
										%>
												<option>
										<%
										}
										if(fieldName.equals(fieldValue)){
										%>
												<option selected="selected"><%=fieldName%></option>
										<%
										}
										else{
										%>
												<option><%=fieldName%></option>
										<%
										}
										ii++;
									}		
								%>
								</select>  
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>																		
					<%
						name =tp.getCharacter(isOld,isOld);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue ="item";
						}
					%>	
								<select name="<%=name%>"  title="Select a column from the list">
								<%
									ii = 0;
									it = table.iterator();
									while (it.hasNext()) {
										EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
									 if(isImagesExists) {
										if(mediaResourceFields.contains(queueObject)){
											continue;
										}
									}	
										fieldName = queueObject.getObject(1);
										if(ii==0){
										%>
											<option>
										<%
										}
										if(fieldName.equals(fieldValue)){
										%>
												<option selected="selected"><%=fieldName%></option>
										<%
										}
										else{
										%>
												<option><%=fieldName%></option>
										<%
										}
										ii++;
									}							
								%>
								</select>  
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>										
											<%
							name =tp.getCharacter(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							groupLabel= tp.getCurrentGroupLabel(name);
							groupLabelValue = (String)groupTable.get(groupLabel);
							if(groupLabelValue == null){
								groupLabelValue ="images";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="image";
								}
						%>
					 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
					
						</td>
						<% if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){%>
							<td>
								<select name="<%=name%>"  title="Select an Image from list">
								<%
									ii = 0;
									it = mediaResourceFields.iterator();
									while (it.hasNext()) {
										EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
										fieldName = queueObject.getObject(1);
										if(ii==0){
										%>
										<option>
										<%
										}
										if(fieldName.equals(fieldValue)){
										%>
										<option selected="selected"><%=fieldName%></option>
										<%
										}
										else{
										%>
											<option><%=fieldName%></option>
										<%
										}
										ii++;
									}
								%>
								</select>   
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>										
						</td>
						<td>
						<%
							name = tp.getCharacter(isOld,isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="image";
								}
							%>
								<select name="<%=name%>"  title="Select an Image from the list">
									<%
									ii = 0;
									it = mediaResourceFields.iterator();
									while (it.hasNext()) {
										EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
										fieldName = queueObject.getObject(1);
										if(ii==0){
										%>
										<option>
										<%
										}
										if(fieldName.equals(fieldValue)){
										%>
										<option selected="selected"><%=fieldName%></option>
										<%
										}
										else{
										%>
											<option><%=fieldName%></option>
										<%
										}
										ii++;
									}
								%>
								</select>  
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>																		
						</td>
						<%}%>
					</tr>
				</table>
				<hr/>
				<table border="0" cellspacing="2" cellpadding="4">
					<tr>
						<td>
				<%
					name =tp.getCharacter(isNew,isNew);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
					fieldValue ="";
					}	
					groupLabel= tp.getCurrentGroupLabel(name);
					groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="credits";
					}			
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="credit";
					}		
					%>
						<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
					<input size="80" type="text"  title="ENTER COPYRIGHT INFORMATION" name="<%=name%>" value="<%=fieldValue%>"/>
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  
					<%
						name = tp.getCharacter(isOld,isOld);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue ="credit";
						}		
						%>
						<input size="50" type="text"  title="ENTER URL" name="<%=name%>" value="<%=fieldValue%>"/>
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  
						</td>
					</tr>
				</table>
				<hr/>
				<table width="504" border="0" cellspacing="0" cellpadding="4">
					<tr>
						<td><b>Created: </b>xxx <b>Updated:</b> xxxx </td>
					</tr>
				</table>
	<%@ include file="footerPlates.jsp" %>		</form>
	</body>
</html>
