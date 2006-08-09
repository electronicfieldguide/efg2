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

			String templateMatch ="Taxon Page Template4";
			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			String xslFileName = "TaxonPageTemplate4.xsl";
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
			int numberofCharacters = 3;
			int numberofLists = 3;
			int numberOfImagesPerRow = 2;
			int numberOfIdentifications=1;
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
		    String cssFile = "taxonpagetemplate3.css";
			int ww = 0;
		%>	
		<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
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
			%>		
		<link rel="stylesheet" href="<%=cssLocation%>"/>
	</head>
	<body>
		<form method="post" action="<%=context%>/configTaxonPage">
				<input type="hidden"  name="<%=name%>" value="<%=fieldValue%>"/> 
			<%
				name =tp.getCharacter(isNew,isNew);
				name =tp.getCharacter(isOld,isOld);
				name =tp.getCharacter(isOld,isOld);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
			if(isTableExists){
			%>		
			<table class="title" width="600">
				<tr />
				<tr>
					<td class="famname" align="left">
						<%
							name =tp.getCharacter(isOld,isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
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
					</td>
					<td class="famname" align="left" />
					<td class="famname" align="left">
						<%
							name =tp.getCharacter(isOld,isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
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
					</td>
					<td class="famname" align="left" />
				</tr>
			</table>
			<%}//end header iftableexists %>			
			<p />
			<table valign="top" width="600">
				<tr>
					<td>
					<%
					name =tp.getCharacter(isNew,isNew);//guaranteed to be generated at least once
					%>
						<table align="center" border="1" cellspacing="15">
							<tr>
								<td>
									<table border="0" cellspacing="5">
									<%
										if(isImagesExists) {
										for(int zz = 0; zz < 2; zz++){
										if ( (zz % numberOfImagesPerRow )== 0){
											if( zz != 0){
												name =tp.getCharacter(isOld,isOld);
											}
											fieldValue = (String)groupTable.get(name);
											if(fieldValue == null){
												fieldValue ="";
											}
										%>								
										<tr>
											<td class="id_text">
													<select name="<%=name%>"  title="Select An Image Field From List">                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
													<%
														ii=0;
														it = mediaResourceFields.iterator();
														characterText = tp.getCurrentCharacterText(name);
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
													<%
												   name = characterText;
													fieldValue = (String)groupTable.get(name);
													if(fieldValue == null){
														fieldValue ="";
													}
													%>
													<br /><input size="20" type="text"  title="ENTER IMAGE CAPTION HERE" name="<%=name%>" value="<%=fieldValue%>"/>
											</td>
											<% if( (zz + 1) <  2){
													name =tp.getCharacter(isOld,isOld);
													fieldValue = (String)groupTable.get(name);
													if(fieldValue == null){
														fieldValue ="";
													}
											%>
											<td class="id_text">
													<select name="<%=name%>"  title="Select An Image Field From List">                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
													<%
														ii=0;
														it = mediaResourceFields.iterator();
														characterText = tp.getCurrentCharacterText(name);
												
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
											}//end if for numberofimages mod zz
										}//end outer for loop over mediaResourceTable
									}//end if isimaegs exists
									%>
									</table>
								</td>
								<%
								name =tp.getCharacter(isNew,isNew);
								%>										
								<td class="identification_td" bgcolor="white" valign="top" width="150">
								<%
								if(isListsExists){
									 ww = 0;
									for(int zz = 0; zz <numberOfIdentifications; zz++){
										if(ww !=0){
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
								%>
									<p class="id_text">
										<strong><input size="20" type="text"  title="Enter Title here" name="<%=characterText%>" value="<%=characterValue%>"/>:</strong>									
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
									</p>
									<%
										ww++;
									}//end for loop numberOfIdentifications
								}//end if is efgLists exists
							%>	
								</td>
							</tr>
						</table>
						<br/>
						<hr />
						<br />
						<%
							name =tp.getCharacter(isNew,isNew);
						%>						
						<table bgcolor="white" width="100%">
							<tr>
								<td>
								<%
								if(isListsExists){
									 ww = 0;
									for(int zz = 0; zz < numberofLists; zz++){
										if(zz >= efgList.size()){
											break;
										}
										if(ww !=0){
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
								%>
									<p class="id_text">
										<strong><input size="20" type="text"  title="Enter Title here" name="<%=characterText%>" value="<%=characterValue%>"/>:</strong>									
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
									</p>
									<%
										ww++;
									}//end for loop
								}//end if is efgLists exists
								if(isTableExists){
									ww = 0;
									for(int zz = 0; zz < table.size(); zz++){
										if(ww >= numberofCharacters ){
											break;
										}
										EFGQueueObjectInterface queueObject1 = (EFGQueueObjectInterface)table.get(zz);
										if(isImagesExists) {
											if(mediaResourceFields.contains(queueObject1)){
												continue;
											}
										}// end is Images	
										 if(isListsExists) {
											if(efgList.contains(queueObject1)){
												continue;
											}
										}	//end is lists exists
									
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
															 if(isListsExists) {
																if(efgList.contains(queueObject)){
																	continue;
																}
															}	//end is lists exists

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
									</p>
									<%
										ww++;
									}//end for loop
								}//end if is table exists
						%>						
								</td>
							</tr>
						</table>
						<br />
						<hr />
						<br />
						<%
							name =tp.getCharacterText(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}						
						if(isTableExists){ %>
						<table bgcolor="white" width="100%">
							<tr>
								<td>
									<p class="credits">
										<strong>Credits: </strong><input size="100" type="text"  title="ENTER CREDIT INFORMATION" name="<%=name%>" value="<%=fieldValue%>"/>
								</td>
							</tr>
						</table>
						<%}//end if is table exists
						%>						
					</td>
				</tr>
			</table>
			<%	if(isTableExists){ 
									
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
			<%}// if is Table exists
			else{%>
			<h2>Your data has no fields</h2>
			<%}%>			
		</form>
	</body>
</html>
