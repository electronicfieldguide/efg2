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
	<head>
	<%@ include file="jspName.jsp" %>
			<%
            String guid =  (String)request.getAttribute(EFGImportConstants.GUID); 
			String uniqueName = (String)request.getAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME); 
			String displayName = (String)request.getAttribute(EFGImportConstants.DISPLAY_NAME); 
			String datasourceName =(String)request.getAttribute(EFGImportConstants.DATASOURCE_NAME);
			if(datasourceName == null){
				datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
			
			}
			if(displayName == null){
				displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME);
			}
			if(uniqueName == null){
				uniqueName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME); 
			}
			if(guid == null){
				guid = request.getParameter(EFGImportConstants.GUID); 
			}
           String groupLabel= null;
			String groupLabelValue = null;
			String characterLabelValue = null;
			String characterLabel = null;

			String templateMatch ="Taxon Page Template1";
			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			String xslFileName = "nantucketTaxonPageTemplate.xsl";
			String fieldName = null;
			String characterText = null;
			String characterValue = null;
			String fieldValue = null;
			String catNarrativeName=null;
			String listsName = null;
			EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
			Iterator it =null;

			List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
			List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);
			List efgList = dsHelper.getEFGListsFields(displayName,datasourceName);

			boolean isListsExists = false;
			boolean isImagesExists = false;
			boolean isTableExists = false;
			if((efgList != null) && (efgList.size() > 0)){
				isListsExists = true;	
			}
			if((table != null) && (table.size() > 0)){
				isTableExists = true;	
			}

			if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){
				isImagesExists = true;	
			}
			
			int numberOfImagesPerRow = 4;
			TemplateProducer tp = new TemplateProducer();
			boolean isNew = true;
			boolean isOld = false;
			String name = null;
			int ii = 0;
			TemplatePopulator tpop = new  TemplatePopulator();
			StringBuffer fileName = new StringBuffer(realPath);
			fileName.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME); 
			fileName.append(File.separator);
			fileName.append(datasourceName.toLowerCase());
			fileName.append(EFGImportConstants.XML_EXT);
			Hashtable groupTable = tpop.populateTable(fileName.toString(), guid, EFGImportConstants.TAXONPAGE_XSL, datasourceName );
			if(groupTable == null){
					groupTable = new Hashtable();
			}

			String cssLocation = context + "/" + EFGImportConstants.templateCSSDirectory  + "/";
		    String cssFile = "nantuckettaxonpage.css";
			int numberofImages=0;
				if(isImagesExists){
				if(mediaResourceFields.size()  < 5 ){
					numberofImages = mediaResourceFields.size();
				}
				else{
				numberofImages =5;
				}
			}
			int numberofListCharacters=0;
			if(isListsExists){
				if(efgList.size()  < 10 ){
					numberofListCharacters = efgList.size();
				}
				else{
				numberofListCharacters =10;
				}
			}
			int numberofCharacters=5;
			if(isTableExists){    
				if( table.size() < 5 ){
					numberofCharacters = table.size();
				}
				else{
					numberofCharacters=5;
				}
			}	
			if(!isTableExists){    	
	 			String forwardPage="NoDatasource.jsp";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
				dispatcher.forward(request, response);
		    }
		%>

		<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
	<title>Taxon Page Configuration of <%=displayName%></title>
			<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					cssLocation =cssLocation + cssFile;
					fieldValue = cssFile;
				}
				else{
					cssLocation =cssLocation + fieldValue;
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="styles";
				}
			%>		
		<link rel="stylesheet" href="<%=cssLocation%>"/>
	</head>
	<body>
		<form method="post" action="<%=context%>/configTaxonPage">
			 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			

			<input type="hidden"  name="<%=name%>" value="<%=fieldValue%>"/> 
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
			<table class="title">
				<tr>
					<td class="comname" colspan="2">
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
									fieldName = (String)queueObject.getObject(1);
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
									fieldName = (String)queueObject.getObject(1);
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
									fieldName = (String)queueObject.getObject(1);
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
									fieldName = (String)queueObject.getObject(1);
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
		<%	 if(isImagesExists) {%>
			<table class="outerimageframe">
				<tr>
					<td>
						<table class="imageframe">
							<tr>
								<td>
								<%
									List labelList = new ArrayList();
									for(int zz = 0; zz < numberofImages; zz++){
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
													<select name="<%=name%>"  title="Select An Image Field From List">
													<%
														ii=0;
														it = mediaResourceFields.iterator();
														characterText = tp.getCurrentCharacterText(name);
														labelList.add(characterText);
														while (it.hasNext()) {
															EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
															fieldName = (String)queueObject.getObject(1);
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
											<%if( (zz + 1) < numberofImages){
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
													<select name="<%=name%>"  title="Select An Image Field From List">
													<%
														ii=0;
														it = mediaResourceFields.iterator();
														characterText = tp.getCurrentCharacterText(name);
														labelList.add(characterText);
												
														while (it.hasNext()) {
															EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
															fieldName = (String)queueObject.getObject(1);
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
												if( (zz + 2) < numberofImages){
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
													<select name="<%=name%>"  title="Select An Image Field From List">
													<%
														ii=0;
														characterText = tp.getCurrentCharacterText(name);
														labelList.add(characterText);

														it = mediaResourceFields.iterator();
														while (it.hasNext()) {
															EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
															fieldName = (String)queueObject.getObject(1);
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
												if( (zz + 3) < numberofImages){
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
													<select name="<%=name%>"  title="Select An Image Field From List">
													<%
														ii=0;
														it = mediaResourceFields.iterator();
														characterText = tp.getCurrentCharacterText(name);
														labelList.add(characterText);

														while (it.hasNext()) {
															EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
															fieldName = (String)queueObject.getObject(1);
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
												if( (zz + 1) < numberofImages){
											   name = (String)labelList.get(zz+1);
												fieldValue = (String)groupTable.get(name);
												if(fieldValue == null){
													fieldValue ="";
												}
											%>
											<td class="id_text"><input size="20" type="text"  title="ENTER IMAGE CAPTION HERE" name="<%=name%>" value="<%=fieldValue%>"/></td>
											<%} //end if zz + 1
												if( (zz + 2) < numberofImages){
											   name = (String)labelList.get(zz+2);
												fieldValue = (String)groupTable.get(name);
												if(fieldValue == null){
													fieldValue ="";
												}
											%>
											<td class="id_text"><input size="20" type="text"  title="ENTER IMAGE CAPTION HERE" name="<%=name%>" value="<%=fieldValue%>"/></td>
											<%} //end if zz + 2
												if( (zz + 3) < numberofImages){
											   name = (String)labelList.get(zz+3);
												fieldValue = (String)groupTable.get(name);
												if(fieldValue == null){
													fieldValue ="";
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
<%
								name =tp.getCharacter(isNew,isNew);
			groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="lists";
					}

								if(isListsExists){
									for(int zz = 0; zz <  numberofListCharacters; zz++){
											
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
															fieldName = (String)queueObject.getObject(1);
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
										for(int zz = 0; zz <  numberofCharacters; zz++){
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
															fieldName = (String)queueObject.getObject(1);
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

										<strong>Credits: </strong><input size="100" type="text"  title="ENTER CREDIT INFORMATION" name="<%=name%>" value="<%=fieldValue%>"/>
																											<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  
									</p>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
							<%
									
									fieldValue = (String)groupTable.get(EFGImportConstants.ISDEFAULT_STR);
									if(fieldValue == null){
										fieldValue = "false";
									}
									Boolean bool = new Boolean(fieldValue);
								
									
					%>

				<p>The following is not part of the template:
			<select name="<%=EFGImportConstants.ISDEFAULT_STR%>"  title="Indicate whether this template should be the default for search results page">
				<option value="false">Do not use as Default Template</option>
				<%if(bool.booleanValue() ){
					%>
					 <option value="true" selected="selected">Use as Default Template</option>
					 <%} else{%>
					  <option value="true">Use as Default Template</option>
					<%}%>
			</select>
			</p><br/><br/>

				<input type="hidden"   name="<%=EFGImportConstants.DISPLAY_NAME%>"  value="<%=displayName%>"/>
				<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_NAME%>"  value="<%=datasourceName%>"/>
				<input type="hidden"   name="<%=EFGImportConstants.HTML_TEMPLATE_NAME%>"  value="<%=templateMatch%>"/>
				<input type="hidden"   name="<%=EFGImportConstants.XSL_STRING%>"  value="<%=xslFileName%>"/>
		<%if( guid != null){%>
		<input type="hidden"   name="<%=EFGImportConstants.GUID%>"  value="<%=guid%>"/>
		<%}%>
				<input type="hidden"   name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>"  value="<%=uniqueName%>"/>
				<input type="hidden"   name="<%=EFGImportConstants.JSP_NAME%>"  value="<%=jspName%>"/>
				<input type="submit"  name="submit" value="Click to submit" align="middle" />	

		</form>
	</body>
</html>
