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

			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			String xslFileName = "TaxonPageTemplate5.xsl";
	
            String templateMatch ="TaxonPage Template5";
			EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
			StringBuffer fileName = new StringBuffer(realPath);
			 fileName.append(File.separator);
			 fileName.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME); 
			 fileName.append(File.separator);
			 fileName.append(datasourceName.toLowerCase());
			  fileName.append(EFGImportConstants.XML_EXT);
			String fieldName = null;
			String fieldValue = null;
			
        	boolean isImagesExists = false;
			boolean isTableExists = false;
			boolean isListsExists = false;


			Iterator it =null;
			List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
			List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);
			List efgList = dsHelper.getEFGListsFields(displayName,datasourceName);

			TemplatePopulator tpop = new  TemplatePopulator();

			Hashtable groupTable = tpop.populateTable(fileName.toString(), guid, EFGImportConstants.TAXONPAGE_XSL, datasourceName );
			if(groupTable == null){
					groupTable = new Hashtable();
			}
String groupKey = null;
           String groupLabel= null;
			String groupLabelValue = null;
			String characterLabelValue = null;
			String characterLabel = null;
			String characterText = null;
			String characterValue = null;
			String characterText1= null;
			String characterValue1= null;
			String characterText2 = null;
			String characterValue2 = null;
			String groupText = null;
			String groupValue = null;

			int tableSize = table.size();
			int listSize = efgList.size();
			TemplateProducer tp = new TemplateProducer();
			boolean isNew = true;
			boolean isOld = false;
			String name = null;
			int ii = 0;
            File imageFiles = new File(realPath + File.separator +  EFGImportConstants.templateImagesDirectory);
			 File[] imageFileList = imageFiles.listFiles(); 
			String defaultInfo ="Click on a name to view more information about that Taxon."; 
			if((table != null) && (table.size() > 0)){
				isTableExists = true;	
			}
			if((efgList != null) && (efgList.size() > 0)){
				isListsExists = true;	
			}
			if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){
				isImagesExists = true;	
			}

		    File cssFiles = new File(realPath + File.separator +  EFGImportConstants.templateCSSDirectory);
			File[] cssFileList = cssFiles.listFiles(); 
			String cssLocation = context + "/" + EFGImportConstants.templateCSSDirectory  + "/";
			 String cssFile = "bogstyle.css";
		    if(!isTableExists){    	
	 			String forwardPage="NoDatasource.jsp";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
				dispatcher.forward(request, response);
		    }

		//sort and iterate
		%>
		<title>Taxon Page Template</title>
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
			%>		
		<link rel="stylesheet" href="<%=cssLocation%>"/>
	</head>
	<body>
		 <form method="post" action="<%=context%>/configTaxonPage">
			<%if(cssFileList.length > 0){
				%>						
				<select name="<%=name%>"  title="Select  a css file from list">
					<%
						for (ii=0; ii<cssFileList.length; ii++ ) {
							File currentCSSFile = (File)cssFileList[ii];
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
				</select><br/>
			<%}//end if css
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
	<%	characterText = tp.getCurrentCharacterText(name);
									characterValue = (String)groupTable.get(characterText);
		
									if(characterValue == null){
										characterValue ="";
									}
							%>
							<br/>	<span class="imagecaption">
							<input type="text"    name="<%=characterText%>" value="<%=characterValue%>"/>
							</span>
						<%}//end if images
					%>
					 </td>
				</tr>
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
									fieldName = (String)queueObject.getObject(1);
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
							<%	characterText = tp.getCurrentCharacterText(name);
									characterValue = (String)groupTable.get(characterText);
		
									if(characterValue == null){
										characterValue ="";
									}
							%>
							<br/>	<span class="imagecaption">
							<input type="text"    name="<%=characterText%>" value="<%=characterValue%>"/>
							</span>
						<%}//end if images
					%>
					</td>         
				</tr>
				<%}//end outer for 
			
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
				%>
				<tr>
				   <td class="heading">
						 <input size="20" type="text"  title="" name="<%=groupText%>" value="<%=groupValue%>"/>
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
						%>	
							<tr>
								<td class="assochead"><input size="20" type="text"  title="" name="<%=characterText%>" value="<%=characterValue%>"/></td>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<td class="simspptext">
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
												fieldName = (String)queueObject2.getObject(1);
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
						<%}//end for
							%>
						</table>
					</td>
					<td class="image_capsule">
						<table class="images">
						<%
						 if(isImagesExists) {
							for(int zz2 = 0; zz2 < efgList.size(); zz2++){
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
								characterText1 = tp.getCurrentCharacterText(name);
								characterValue1 = (String)groupTable.get(characterText1);
								if(characterValue1== null){
									characterValue1 ="";
								}
							%>
							<tr>
								<td class="images_new">
									<select name="<%=name%>"  title="Select An Image Field From List" style="width:120px;">          														
									<%
										int yy=0;
										it = mediaResourceFields.iterator();
										while (it.hasNext()) {
											EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
											fieldName = (String)queueObject.getObject(1);
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
								</td>
								<td class="images_new">
								<%
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
									characterText2 = tp.getCurrentCharacterText(name);
									characterValue2 = (String)groupTable.get(characterText2);
									if(characterValue2== null){
										characterValue2 ="";
									}
								%>
									<select name="<%=name%>"  title="Select An Image Field From List" style="width:120px;">          														
										<%
								
												yy=0;
												it = mediaResourceFields.iterator();
												while (it.hasNext()) {
													EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
													fieldName = (String)queueObject.getObject(1);
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
								</td>								
							</tr>
							<tr>
								<td class="imagecaption"><input type="text"    name="<%=characterText1%>" value="<%=characterValue1%>"/></td>
								<td class="imagecaption"><input type="text"    name="<%=characterText2%>" value="<%=characterValue2%>"/></td>
							</tr>
							<%}//end for
							} //end if images
							%>
						</table>
					</td>         
				</tr>
				<%}//end outer for 
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
			</p>
			<br/><br/>
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
