<%@page import="
java.io.File,
java.util.List,
java.util.ArrayList,
java.util.Iterator,
java.util.Hashtable,
project.efg.server.utils.EFGDataSourceHelperInterface,
project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.TemplateProducer,
project.efg.server.utils.TemplatePopulator,
project.efg.util.interfaces.EFGQueueObjectInterface
" %>
<html>
	<%@ include file="nantucketOldHeader.jsp" %>	
	<body>
		<form method="post" id="efg_form" action="<%=context%>/configTaxonPage" onsubmit="return removeConstants();">
		<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			

			<%if(cssFileList.length > 0){
				%>						
						<select name="<%=name%>"  title="Select an image from List">
							<%
								for (ii=0; ii<cssFileList.length; ii++ ) {
									File currentCSSFile = cssFileList[ii];
									fieldName = currentCSSFile.getName();
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
			<%}
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

			<table class="title">
				<tr>
					<td class="comname" colspan="2">
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
				</tr>
				<tr>
					<td class="sciname">
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
					<td class="famname">
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
				</tr>
			</table>
			<%}//end if isTableExists
			name =tp.getCharacter(isNew,isNew);//guaranteed to be generated at least once
			groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="images";
					}
			%>
				 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
		<%	 if(isImagesExists) {
			%>

			<table class="outerimageframe">
				<tr>
					<td>
						<table class="imageframe">
							<tr>
								<td>
								<%
									List labelList = new ArrayList();
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
										%>
										<table class="images">
										<tr>
											<td class="thumb">
												<div class="thumb">
													<select name="<%=name%>"  title="Select An Image Field From List" style="width:100px;">
													<%
														ii=0;
														it = mediaResourceFields.iterator();
														characterText = tp.getCurrentCharacterText(name);
														labelList.add(characterText);
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

												</div>
											</td>
											<%if( (zz + 1) < mediaResourceFields.size()){
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
											<td class="thumb">
												<div class="thumb">
													<select name="<%=name%>"  title="Select An Image Field From List" style="width:100px;">
													<%
														ii=0;
														it = mediaResourceFields.iterator();
														characterText = tp.getCurrentCharacterText(name);
														labelList.add(characterText);
												
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

												</div>
											</td>
											<%}//end if zz+1 < mediaResources.size()
												if( (zz + 2) < mediaResourceFields.size()){
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
											<td class="thumb">
												<div class="thumb">
													<select name="<%=name%>"  title="Select An Image Field From List" style="width:100px;">
													<%
														ii=0;
														characterText = tp.getCurrentCharacterText(name);
														labelList.add(characterText);

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
														}//end while	
													%>
													</select> 
													<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  

												</div>
											</td>
											<%}//end if zz+2 < mediaResources.size()
												if( (zz + 3) < mediaResourceFields.size()){
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
											<td class="thumb">
												<div class="thumb">
													<select name="<%=name%>"  title="Select An Image Field From List" style="width:100px;">
													<%
														ii=0;
														it = mediaResourceFields.iterator();
														characterText = tp.getCurrentCharacterText(name);
														labelList.add(characterText);

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

												</div>
											</td>
											<%}// if zz+ 3 < mediaResourceFields.size()
											%>
										</tr>
										<tr>
											<%
											   name = (String)labelList.get(zz);
												fieldValue = (String)groupTable.get(name);
												if(fieldValue == null){
													fieldValue ="";
												}
											%>
											<td class="id_text"><input size="20" type="text"  title="ENTER IMAGE CAPTION HERE" name="<%=name%>" value="<%=fieldValue%>"/></td>
										    <%
												if( (zz + 1) < mediaResourceFields.size()){
											   name = (String)labelList.get(zz+1);
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
											<td class="id_text"><input size="20" type="text"  title="ENTER IMAGE CAPTION HERE" name="<%=name%>" value="<%=fieldValue%>"/></td>
											<%} //end if zz + 1
												if( (zz + 2) < mediaResourceFields.size()){
											   name = (String)labelList.get(zz+2);
												fieldValue = (String)groupTable.get(name);
												if(fieldValue == null){
													fieldValue ="";
												}
											%>
											<td class="id_text"><input size="20" type="text"  title="ENTER IMAGE CAPTION HERE" name="<%=name%>" value="<%=fieldValue%>"/></td>
											<%} //end if zz + 2
												if( (zz + 3) < mediaResourceFields.size()){
											   name = (String)labelList.get(zz+3);
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
											<td class="id_text"><input size="20" type="text"  title="ENTER IMAGE CAPTION HERE" name="<%=name%>" value="<%=fieldValue%>"/></td>
											<%}//end if zz + 3%>
										</tr>
									</table>
									<%
										}//end if for numberofimages mod zz
									}//end outer for loop over mediaResourceTable
									%>
								</td>
							</tr>
						</table>
						<%
							name =tp.getCharacterText(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
							fieldValue ="";
							}				
			groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="photocred";
					}
			
						%>
								 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			

						<div class="photocred"><input size="80" type="text"  title="ENTER IMAGE CREDITS HERE" name="<%=name%>" value="<%=fieldValue%>"/></div>
			<%
			}//end if isImagesExists
			if((isTableExists) || (isListsExists)){
			%>
						<table bgcolor="white" width="100%" class="details">
							<tr>
								<td>
								<!-- It is a lists -->
<%								name =tp.getCharacter(isNew,isNew);
			groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="lists";
					}
								if(isListsExists){%>
									<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
								<%
									for(int zz = 0; zz < efgList.size(); zz++){
											
											if(zz!=0){
												name =tp.getCharacter(isOld,isOld);
											}
											fieldValue = (String)groupTable.get(name);
											if(fieldValue == null){
												fieldValue ="";
											}//end if fieldValue == null				
											characterText = tp.getCurrentCharacterText(name);
											characterValue = (String)groupTable.get(characterText);
											if(characterValue == null){
												characterValue ="";
											}
			characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="list";
					}
											
									%>
									<p class="detail_text">
										<strong><input size="20" type="text"  title="" name="<%=characterText%>" value="<%=characterValue%>"/></strong>
													<select name="<%=name%>"  title="Select A Field From List">
													<%
														ii=0;
														it = efgList.iterator();
														while (it.hasNext()) {
															if(ii == 0){
															%>
																	<option>
															<%
															}//end if ii =0 
															EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
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
								<%}//end for loop
								}// end if isListsExists
									
										int ww = 0;
										name =tp.getCharacter(isNew,isNew);
	groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="items";
					}
%>
									<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			

<%
										for(int zz = 0; zz < table.size(); zz++){
											EFGQueueObjectInterface queueObject1 = (EFGQueueObjectInterface)table.get(zz);
											if(isImagesExists) {
												if(mediaResourceFields.contains(queueObject1)){
													continue;
												}
											}	
											if(isListsExists) {
												if(efgList.contains(queueObject1)){
													continue;
												}
											}	
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
										characterLabelValue ="item";
										}		
											characterText = tp.getCurrentCharacterText(name);
											characterValue = (String)groupTable.get(characterText);
											if(characterValue == null){
												characterValue ="";
											}
									%>
									<p class="detail_text">
										<strong><input size="20" type="text"  title="" name="<%=characterText%>" value="<%=characterValue%>"/></strong>
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
															if(isListsExists) {
																if(efgList.contains(queueObject)){
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
									}//end for loop isTableExists
								%>																	
								</td>
							</tr>
						</table>
						<%} // end if isTable or islistExists
						%>
						<br/>
						<hr/>
						<br/>
						<table class="credits">
							<tr>
								<td>
									<p class="credits">
										<%
											String creditsname =tp.getCharacterText(isNew,isNew);
											String creditsfieldValue = (String)groupTable.get(creditsname);
											if(creditsfieldValue == null){
											creditsfieldValue ="";
											}	
	String creditsgroupLabel= tp.getCurrentGroupLabel(creditsname);
					String creditsgroupLabelValue = (String)groupTable.get(creditsgroupLabel);
						if(creditsgroupLabelValue == null){
							creditsgroupLabelValue ="credits";
						}			
		String creditscharacterLabel= tp.getCurrentCharacterLabel(creditsname);
						String creditscharacterLabelValue = (String)groupTable.get(creditscharacterLabel);
						if(creditscharacterLabelValue == null){
							creditscharacterLabelValue ="credit";
						}		
										%>
<hr />
<p class="link">
<a class="link" href="javascript:void(0)">Sample Link</a>
<a class="link" href="javascript:void(0)">2nd Sample Link</a>
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
						moreData = false;
						fieldValue = LINK_URL_TEXT;
						break;
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue = LINK_TEXT;
						moreData = false;
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
						<input type="hidden"    name="<%=creditsgroupLabel%>" value="<%=creditsgroupLabelValue%>"/>			

										<strong>Credits: </strong><input size="100" type="text"  title="ENTER CREDIT INFORMATION" name="<%=creditsname%>" value="<%=creditsfieldValue%>"/>
																							<input type="hidden"    name="<%=creditscharacterLabel%>" value="<%=creditscharacterLabelValue%>"/>  

									
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<%@ include file="footerTaxonPage.jsp" %>
		</form>
	</body>
	<input type="hidden" id="nameValueID" name="nameValue" value="<%=name%>"/>
	
</html>
