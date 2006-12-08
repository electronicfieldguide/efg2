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
	
	<%@ include file="MarshasTemplateHeader.jsp" %>

		<body>
				<%
	int numberOfLists = 0;
	int numberOfInnerLists=0;
	int numberOfItems=0;
	if(isListsExists ){
		if(efgList.size()  < 3){
			numberOfLists = efgList.size();
		}
		else{
			numberOfLists = 3;
		}
		if(efgList.size()  < 4){
			numberOfInnerLists = efgList.size();
		}
		else{
			 numberOfInnerLists=4;
		}
	}
	if(isTableExists){
		if(table.size()  < 12){
			numberOfItems = table.size();
		}
		else{
		 numberOfItems=12;
		}
	}
		
		%>
		 <form method="post" action="<%=context%>/configTaxonPage">
	<%
			name =tp.getCharacter(isNew,isNew);
			characterText = tp.getCurrentCharacterText(name);
			characterValue = (String)groupTable.get(characterText);
		  
			if(characterValue == null){
				characterValue ="";
			}

			groupLabel= tp.getCurrentGroupLabel(name);
		
				groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="title";
					}
			


				%>
				<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>	
			<span class="title"><input type="text" size="75"  title="ENTER PAGE TITLE HERE" name="<%=characterText%>" value="<%=characterValue%>"/> </span>
			<%
				name =tp.getCharacter(isOld,isOld);
			characterText = tp.getCurrentCharacterText(name);
			characterValue = (String)groupTable.get(characterText);
		
			if(characterValue == null){
				characterValue ="";
			}
				%>
				 <span class="pageauthor"><input type="text"  title="ENTER AUTHOR HERE" name="<%=characterText%>" value="<%=characterValue%>"/> </span>
			<table>
				<tr>
				

					<%
					name =tp.getCharacter(isNew,isNew);

					
					groupText = tp.getCurrentGroupText(name);
					
					String[] groupArr = groupText.split(":");

				if(groupArr.length > 3){
					groupKey = groupText.substring(0,groupText.lastIndexOf(":"));
				}
				else{
					groupKey = groupText;
				}
					groupValue = (String)groupTable.get(groupKey );
					if(groupValue == null){
						groupValue ="";
					}
			groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="header";
					}
			


				%>
				
					   <td class="headingtop"><input size="20" type="text"   title="ENTER HEADER FOR ROW" name="<%=groupText%>" value="<%=groupValue%>"/>
					   <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
					   </td>
					   <td class="datatop">
						<%
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
						<div class="comname">							
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

						</div>
						<div class="sciname">					
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
							<select   name="<%=name%>"  title="Select A Field From List" style="width:100px;">                                                                                     
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

							 <span class="scinameauth">	
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

							</span>
						 </div>
						 <div class="family">	
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
							<select name="<%=name%>"  title="Select A Field From List" style="width:150px;">            								
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
								}//while	
							%>
							</select>   
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>

							 (	<%
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
							<select name="<%=name%>"  title="Select A Field From List" style="width:150px;"> 								
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
							)
						</div>
					</td>
					<td class="imagestop">
					<%	
						name =tp.getCharacter(isOld,isOld);
						 if(isImagesExists) {
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
							<select name="<%=name%>"  title="Select An Image Field From List">     														
							<%
								ii=0;
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
								}// end while	
							%>
							</select> 
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>							

						<%}//end if images
					%>
					 </td>
				</tr>
				<%
				if(isTableExists){
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

					name =tp.getCharacter(isNew,isNew);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}//end if fieldValue == null				

			groupText = tp.getCurrentGroupText(name);
			groupArr = groupText.split(":");

				if(groupArr.length > 3){
					groupKey = groupText.substring(0,groupText.lastIndexOf(":"));
				}
				else{
					groupKey = groupText;
				}
					groupValue = (String)groupTable.get(groupKey );
					if(groupValue == null){
						groupValue ="";
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
					 if(zz < numberOfItems){
				%>
				<tr>
				   <td class="heading"><input size="20" type="text"  title="" name="<%=groupText%>" value="<%=groupValue%>"/>
					 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>					   
				   </td>
				   <td class="data">													
						<select name="<%=name%>"  title="Select A Field From List">     														
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
								if(ii == 0){
								%>
										<option>
								<%
								}//end if ii =0 
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
					</td>
					<td class="images">
					<%	
						name =tp.getCharacter(isOld,isOld);
						 if(isImagesExists) {
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
							<select name="<%=name%>"  title="Select An Image Field From List">     														
							<%
								int yy=0;
								it = mediaResourceFields.iterator();
								
								while (it.hasNext()) {
									EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
									fieldName = queueObject.getObject(1);
									if(yy==0){
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
									yy++;
								}// end while	
							%>
							</select> 
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>

						<%}//end if images
					%>
					</td>         
				</tr>
				<%
				}//end if
				}//end outer for 
			}//end if tableexist
				if(isListsExists) {
				for(int zz = 0; zz < efgList.size(); zz++){
					EFGQueueObjectInterface queueObject1 = (EFGQueueObjectInterface)efgList.get(zz);
					

				name =tp.getCharacter(isNew,isNew);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}//end if fieldValue == null		


		groupText = tp.getCurrentGroupText(name);
			groupArr = groupText.split(":");

				if(groupArr.length > 3){
					groupKey = groupText.substring(0,groupText.lastIndexOf(":"));
				}
				else{
					groupKey = groupText;
				}
					groupValue = (String)groupTable.get(groupKey );
					if(groupValue == null){
						groupValue ="";
					}
			
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="lists";
					}
						

					if( zz <  numberOfLists){
				%>
				<tr>
				   <td class="heading"><input size="20" type="text"  title="" name="<%=groupText%>" value="<%=groupValue%>"/>
							<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>		   
				   </td>
				   <td class="data">		
				   <table class="simspp">
				
				<%
				
					for(int zz2 = 0; zz2 < efgList.size(); zz2++){
					queueObject1 = (EFGQueueObjectInterface)efgList.get(zz);
				
					name =tp.getCharacter(isOld,isOld);
				
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
					if(zz2 < numberOfInnerLists){
				%>	
				<tr>
						<td class="assochead"><input size="20" type="text"  title="" name="<%=characterText%>" value="<%=characterValue%>"/></td>
						
					<td class="simspptext">
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<div class="itemlist">
						<select name="<%=name%>"  title="Select A Field From List" style="width:140px;">              														
						<%
							int zii=0;
							Iterator it1 = efgList.iterator();
							while (it1.hasNext()) {
								if(zii == 0){
								%>
										<option>
								<%
								}//end if ii =0 
								EFGQueueObjectInterface queueObject2 = (EFGQueueObjectInterface)it1.next();
								fieldName = queueObject2.getObject(1);
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
								zii++;
							}//end while	
						%>
						</select> 
						</div>
						</td>
							</tr>		
						<%}
						}//end inner for
					%>
						
					</table>
					</td>
					<td class="images">
					<%	name =tp.getCharacter(isOld,isOld);
						 if(isImagesExists) {
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

							<select name="<%=name%>"  title="Select An Image Field From List" style="width:120px;">          														
							<%
								int yy=0;
								it = mediaResourceFields.iterator();
								
								while (it.hasNext()) {
									EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
									fieldName = queueObject.getObject(1);
									if(yy==0){
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
									yy++;
								}// end while	
							%>
							</select> 
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>

						<%}//end if images
					%>
					</td>         
				</tr>
				<%
				}//end if zz < numberoflists
				}//end outer for 
				}//end if is lists exists
				%>
			</table>
			<%
				name =tp.getGroupText(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
				fieldValue ="";
				}			
					groupLabel= tp.getCurrentGroupLabel(name);
				 groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="copyright";
					}
	
			%>
			<span class="copyright">
			<input size="100" type="text"  title="ENTER CREDIT INFORMATION" name="<%=name%>" value="<%=fieldValue%>"/>
							<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>		   
			
			</span>
			<%@ include file="footerTaxonPage.jsp" %>
			
		</form>
	</body>
</html>
