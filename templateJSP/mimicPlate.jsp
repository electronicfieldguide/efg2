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
	
	<%@ include file="mimicPlateHeader.jsp" %>

	<body bgcolor="#dcdcdc">
		<form method="post" action="<%=context%>/configTaxonPage">
			<hr size="2"/>
			<table border="0" cellspacing="2" cellpadding="0" bgcolor="#f5f5f5">
				<tr>
					<td valign="top">
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
						<%	if(isTableExists){
						%>
									<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
									<h1><font color="#8b0000"><input size="100" type="text"  title="ENTER HTML PAGE TITLE HERE" name="<%=name%>" value="<%=fieldValue%>"/> </font>
									<br/>
									<%
									name =tp.getCharacter(isOld,isOld);
									characterLabel= tp.getCurrentCharacterLabel(name);
									characterLabelValue = (String)groupTable.get(characterLabel);
									if(characterLabelValue == null){
										characterLabelValue ="htmltitle";
									}
								%>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input size="100" type="text"  title="ENTER PAGE TITLE CONTINUATION HERE" name="<%=name%>" value="<%=fieldValue%>"/>
								<%}%>
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
						<%
							if(isTableExists){
						%>						
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
						<%}//end if isTable exists
						%>				
					</td>
				</tr>
				<tr>
					<td rowspan="2" valign="top">
						<h3>
							<font color="#696969">
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
						<%if(isTableExists){
							%>						
								<input size="100" type="text"  title="address1" name="<%=name%>" value="<%=fieldValue%>"/><br/>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<%
								name =tp.getCharacter(isOld,isOld);
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="address";
								}
								%>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input size="100" type="text"  title="address2" name="<%=name%>" value="<%=fieldValue%>"/><br/>
								<%
								name =tp.getCharacter(isOld,isOld);
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="address";
								}
								%>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input size="100" type="text"  title="address3" name="<%=name%>" value="<%=fieldValue%>"/>
						<%
							}
						%>
								<br/>
								<br/>
							</font>
						</h3>
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
							<%
						if(isTableExists){
						%>		
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>				
					<h3><font color="#8b0000" face="Palatino Linotype"><b><input size="50" type="text"  title="ENTER TITLE" name="<%=name%>" value="<%=fieldValue%>"/></b></font></h3>
					<%}%>
				</td>
			</tr>
			<tr>
				<td>
					<font color="black" face="Palatino Linotype">
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
						<%if(isTableExists){
						%>	
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>															
					<input size="200" type="text"  title="ENTER INTRO HERE" name="<%=name%>" value="<%=fieldValue%>"/>
					<%}
					%>
					</font>
				</td>
			</tr>
		</table>
		<hr size="2"/>
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
							 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
		
		<table width="957" border="0" cellspacing="2" cellpadding="3" bgcolor="white">
			<tr>
					<%if(isTableExists){
					%>	
				<td valign="top" nowrap>
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>									
					<h4><u><input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/></u></h4>
				</td>
				<td align="center" valign="top">
					<%
						name =tp.getCharacter(isOld,isOld);
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
					<h4><u><input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/></u></h4>
				</td>
				<td align="center" valign="top">
					<%
						name =tp.getCharacter(isOld,isOld);
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
					<h4><u><input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/></u></h4>
				</td>
				<%}//end if is table exists
				%>
			</tr>
			<tr>
				<td valign="top">
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
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>										
							
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
							
				
								<br/>
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
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>										
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
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>										
								
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
							
								<br/>
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
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>										
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
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>										
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
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>										
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
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>										
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
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>										
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
				<%	if(isImagesExists) {%>
					<td align="center">
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
				<td align="center">
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
				<%
					}//end if is image exists
				%>				
			</tr>
		</table>
		<hr/>
		<table border="0" cellspacing="2" cellpadding="4">
			<tr>
				<td>
				<%
					name =tp.getCharacterText(isNew,isNew);
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
						<%	if(isTableExists){
						%>						
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
					<font color="#006400" face="Palatino Linotype">
						<input size="50" type="text"  title="ENTER URL" name="<%=name%>" value="<%=fieldValue%>"/>
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  
					</font>
					<%}%>
				</td>
			</tr>
		</table>
		<hr/>
		<table width="504" border="0" cellspacing="0" cellpadding="4">
			<tr>
				<td><b>Ceated: </b>xxx <b>Updated:</b> xxxx </td>
			</tr>
		</table>
		<%@ include file="footerPlates.jsp" %>	
		
		
		</form>
	</body></html>
