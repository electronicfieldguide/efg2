<%@page import="
java.io.File,
java.util.List,
java.util.ArrayList,
java.util.Iterator,
java.util.Hashtable,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.util.EFGImportConstants,
project.efg.util.TemplateProducer,
project.efg.util.TemplatePopulator,
project.efg.Imports.efgInterface.EFGQueueObjectInterface
" %>
<html>
	
	<%@ include file="TaxonPageTemplate3Header.jsp" %>
	
	<body>
			<%
 			
			numberOfImagesPerRow = 2;
			numberOfIdentifications=0;
			int numberofImages=0;
			int numberofCharacters=0;
			if(isImagesExists){
				if(mediaResourceFields.size() <4){
					 numberofImages=mediaResourceFields.size();
				}
				else{
					 numberofImages=4;
				}
			}
			if(isTableExists){
				if(table.size() <3){
					numberOfIdentifications = table.size();
				}
				else{
				numberOfIdentifications=3;
				}
			
				if(table.size() <7){
					 numberofCharacters=table.size();
				}
				else{
					 numberofCharacters=7;
				}
			}
		%>
		<form method="post" action="<%=context%>/configTaxonPage">
			<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
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
			<%
			if(isTableExists){
			%>		
			<table class="title" width="600">
				<tr>
					<td class="famname" align="left">
							<select name="<%=name%>"  title="Select A Field From List" style="width:100px;">
							<%
								ii=0;
								it = table.iterator();
								while (it.hasNext()) {
									EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
									 if(isImagesExists) {
										if(mediaResourceFields.contains(queueObject)){
											continue;
										}
									}//isImageExists	
									 if(isListsExists) {
										if(efgList.contains(queueObject)){
											continue;
										}
									}	//end is lists exists

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
								}	//end while
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
									characterLabelValue ="header";
								}
						%>
						<select name="<%=name%>"  title="Select A Field From List" style="width:100px;">
							<%
								ii=0;
								it = table.iterator();
								while (it.hasNext()) {
									EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
									 if(isImagesExists) {
										if(mediaResourceFields.contains(queueObject)){
											continue;
										}
									}
									 if(isListsExists) {
										if(efgList.contains(queueObject)){
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
								}//end while	
							%>
						</select>   
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  
					</td>
					<td class="famname" align="left" />
					<td class="famname" align="left">
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
						<select name="<%=name%>"  title="Select A Field From List" style="width:100px;">
							<%
								ii=0;
								it = table.iterator();
								while (it.hasNext()) {
									EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
									 if(isImagesExists) {
										if(mediaResourceFields.contains(queueObject)){
											continue;
										}
									}	
									 if(isListsExists) {
										if(efgList.contains(queueObject)){
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
								}//end while	
							%>
						</select>  
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  
 					
					</td>
					<td class="famname" align="left" />
				</tr>
				<tr />
			</table>
			<%}//end header iftableexists %>			
			<p />
			<table valign="top" width="600">
				<tr>
					<td>
					<%
					name =tp.getCharacter(isNew,isNew);//guaranteed to be generated at least once
					groupLabel= tp.getCurrentGroupLabel(name);
					groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="images";
					}
			%>
				 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
						<table align="center" border="1" cellspacing="15">
							<tr>
								<td>
									<table border="0" cellspacing="5">
									<%
										if(isImagesExists) {
										for(int zz = 0; zz < mediaResourceFields.size(); zz++){
										if ( (zz % numberOfImagesPerRow )== 0){
											if( zz != 0){
												name =tp.getCharacter(isOld,isOld);
											}
											fieldValue = (String)groupTable.get(name);
											if(fieldValue == null){
												fieldValue ="";
											}
											characterLabel= tp.getCurrentCharacterLabel(name);
											characterLabelValue = (String)groupTable.get(characterLabel);
											if(characterLabelValue == null){
												characterLabelValue ="image";
											}
											if(zz < numberofImages){
										%>								
										<tr>
											<td class="id_text">
													<select name="<%=name%>"  title="Select An Image Field From List" style="width:100px;">
													<%
														ii=0;
														it = mediaResourceFields.iterator();
														characterText = tp.getCurrentCharacterText(name);
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
														}// end while	
													%>
													</select> 
													<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  

													<%
												   name = characterText;
													fieldValue = (String)groupTable.get(name);
													if(fieldValue == null){
														fieldValue ="";
													}
													%>
													<br /><input size="20" type="text"  title="ENTER IMAGE CAPTION HERE" name="<%=name%>" value="<%=fieldValue%>"/>
											</td>
											<% if( (zz + 1) < mediaResourceFields.size()){
													name =tp.getCharacter(isOld,isOld);
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
											<td class="id_text">
													<select name="<%=name%>"  title="Select An Image Field From List" style="width:100px;">
													<%
														ii=0;
														it = mediaResourceFields.iterator();
														characterText = tp.getCurrentCharacterText(name);
												
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
														}//end while	
													%>
													</select> 
													<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  

													<%
												   name = characterText;
													fieldValue = (String)groupTable.get(name);
													if(fieldValue == null){
														fieldValue ="";
													}
													%>
													<br /><input size="20" type="text"  title="ENTER IMAGE CAPTION HERE" name="<%=name%>" value="<%=fieldValue%>"/>
											</td>
											<%}//end if zz+1%>
										</tr>
										<tr/>
											<%
											}//if numberofimages
											}//end if for numberofimages mod zz
										}//end outer for loop over mediaResourceTable
									}//end if isimaegs exists
									%>
									</table>
								</td>
								<%
								name =tp.getCharacter(isNew,isNew);
								groupLabel= tp.getCurrentGroupLabel(name);
								groupLabelValue = (String)groupTable.get(groupLabel);
								if(groupLabelValue == null){
									groupLabelValue ="identifications";
								}
								%>
											
								<td class="identification_td" bgcolor="white" valign="top" width="150">
								<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>
								<%
								if(isTableExists){
									int ww = 0;
									for(int zz = 0; zz <numberOfIdentifications; zz++){
										EFGQueueObjectInterface queueObject1 = (EFGQueueObjectInterface)table.get(zz);
										if(isImagesExists) {
											if(mediaResourceFields.contains(queueObject1)){
												continue;
											}
										}// end is Images	
										if(ww !=0){
											name =tp.getCharacter(isOld,isOld);
										}
										fieldValue = (String)groupTable.get(name);
										if(fieldValue == null){
											fieldValue ="";
										}//end if fieldValue == null			
										characterLabel= tp.getCurrentCharacterLabel(name);
										characterLabelValue = (String)groupTable.get(characterLabel);
										if(characterLabelValue == null){
											characterLabelValue ="identification";
										}		
										characterText = tp.getCurrentCharacterText(name);
										characterValue = (String)groupTable.get(characterText);
										if(characterValue == null){
											characterValue ="";
										}
								%>
									<p class="id_text">
										<strong><input size="20" type="text"  title="Enter Title here" name="<%=characterText%>" value="<%=characterValue%>"/>:</strong>									
										<select name="<%=name%>"  title="Select A Field From List">
													<%
														ii=0;
														it = table.iterator();
														while (it.hasNext()) {
															if(ii == 0){
															%>
															<option>
															<%
															}//end if ii =0 
															EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
															if(isImagesExists) {
																if(mediaResourceFields.contains(queueObject)){
																	continue;
																}
															}	
															fieldName = queueObject.getObject(1);
															if(fieldName.equals(fieldValue)){
															%>
															<option selected="selected"><%=fieldName%></option>
															<%
															}//fieldName.equals(fieldValue)
															else{
															%>
																<option><%=fieldName%></option>
															<%
															}//end else
															ii++;
														}//end while	
													%>
										</select> 
										<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  
									</p>
									<%
										ww++;
									}//end for loop numberOfIdentifications
								}//end if is table exists
									name =tp.getCharacterText(isNew,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
									}		
								groupLabel= tp.getCurrentGroupLabel(name);
								groupLabelValue = (String)groupTable.get(groupLabel);
								if(groupLabelValue == null){
									groupLabelValue ="identificationsText";
								}
									characterLabel= tp.getCurrentCharacterLabel(name);
									characterLabelValue = (String)groupTable.get(characterLabel);
									if(characterLabelValue == null){
										characterLabelValue ="identificationText";
										}		%>
									<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
							<%
								if(isTableExists){	%>	
									<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  

									<p class="id_text"><input size="100" type="text"  title="Enter some information" name="<%=name%>" value="<%=fieldValue%>"/></p>
							<%}//end if is table exists%>
								</td>
							</tr>
						</table>
						<br/>
						<hr />
						<br />
						<%
							name =tp.getCharacter(isNew,isNew);
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="itemsorlists";
					}
				%>
					<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			

				<%
							if(isTableExists){
						%>						
						<table bgcolor="white" width="100%">
							<tr>
								<td>
								<%
									int ww = 0;
									for(int zz = 0; zz < table.size(); zz++){
										EFGQueueObjectInterface queueObject1 = (EFGQueueObjectInterface)table.get(zz);
										if(isImagesExists) {
											if(mediaResourceFields.contains(queueObject1)){
												continue;
											}
										}// end is Images	
										if(ww !=0){
											name =tp.getCharacter(isOld,isOld);
										}
										fieldValue = (String)groupTable.get(name);
										if(fieldValue == null){
											fieldValue ="";
										}//end if fieldValue == null		
									characterLabel= tp.getCurrentCharacterLabel(name);
									characterLabelValue = (String)groupTable.get(characterLabel);
									if(characterLabelValue == null){
										characterLabelValue ="itemorlist";
										}		
		
										characterText = tp.getCurrentCharacterText(name);
										characterValue = (String)groupTable.get(characterText);
										if(characterValue == null){
											characterValue ="";
										}
									if(zz < numberofCharacters){
								%>								
									<p class="detail_text">
										<strong><input size="20" type="text"  title="Enter Title here" name="<%=characterText%>" value="<%=characterValue%>"/>:</strong>
										<select name="<%=name%>"  title="Select A Field From List">
													<%
														ii=0;
														it = table.iterator();
														while (it.hasNext()) {
															if(ii == 0){
															%>
															<option>
															<%
															}//end if ii =0 
															EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
															if(isImagesExists) {
																if(mediaResourceFields.contains(queueObject)){
																	continue;
																}
															}	
															fieldName = queueObject.getObject(1);
															if(fieldName.equals(fieldValue)){
															%>
															<option selected="selected"><%=fieldName%></option>
															<%
															}//fieldName.equals(fieldValue)
															else{
															%>
																<option><%=fieldName%></option>
															<%
															}//end else
															ii++;
														}//end while	
													%>
										</select>
										<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  
									</p>
									<%
								}//end if
									ww++;
								
									}//end for loop tablesize
								%>	
								</td>
							</tr>
						</table>
						<%}// end if isTableExists
						%>						
						<br />
						<hr />
						<br />
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
						}	%>
					<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
					<%	if(isTableExists){ %>
						<table bgcolor="white" width="100%">
							<tr>
								<td>
									<p class="credits">
									<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  
									<strong>Credits: </strong><input size="100" type="text"  title="ENTER CREDIT INFORMATION" name="<%=name%>" value="<%=fieldValue%>"/>
								</td>
							</tr>
						</table>
						<%}//end if is table exists
						%>						
					</td>
				</tr>
			</table>
			<%@ include file="footerTaxonPage.jsp" %>
		</form>
	</body>
</html>
