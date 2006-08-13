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

			String templateMatch ="Plate Template3";
			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			String xslFileName = "morningGloriesPlates.xsl";
			EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
   
			String fieldName = null;
			String fieldValue = null;
    
			Iterator it =null;
			List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
			List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);
			int tableSize = table.size();
			boolean isNew = true;
			boolean isOld = false;
			String name = null;
			int ii = 0;
			StringBuffer fileName = new StringBuffer(realPath);
			fileName.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME); 
			fileName.append(File.separator);
			fileName.append(datasourceName.toLowerCase());
			fileName.append(EFGImportConstants.XML_EXT);

			TemplatePopulator tpop = new  TemplatePopulator();
			Hashtable groupTable = tpop.populateTable(fileName.toString(), guid, EFGImportConstants.SEARCHPAGE_PLATES_XSL, datasourceName );
			if(groupTable == null){
					groupTable = new Hashtable();
			}

		    File imageFiles = new File(realPath + File.separator +  EFGImportConstants.templateImagesDirectory);
			File[] imageFileList = imageFiles.listFiles(); 
			TemplateProducer tp = new TemplateProducer();
			boolean isImagesExists = false;
			boolean isTableExists = false;

			if((table != null) && (table.size() > 0)){
				isTableExists = true;	
			}

			if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){
				isImagesExists = true;	
			}
		    if(!isTableExists){    	
	 			String forwardPage="NoDatasource.jsp";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
				dispatcher.forward(request, response);
		    }

		%>
		<title><%=groupTable.size()%></title>
	</head>
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
									if(isTableExists){
								%>
									<input size="100" type="text"  title="ENTER PAGE TITLE HERE" name="<%=name%>" value="<%=fieldValue%>"/>
								<%}%>
									<br/>
								<%
									name =tp.getCharacter(isNew,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
									}
									if(isTableExists){
								%>
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
							if(isTableExists){
						%>
							<select name="<%=name%>"  title="Select an image from List">
							<%
								for (ii=0; ii<imageFileList.length; ii++ ) {
									File imgFile = (File)imageFileList[ii];
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
							<%}%>		
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
							if(isTableExists){
						%>
							<input size="100" type="text"  title="address1" name="<%=name%>" value="<%=fieldValue%>"/><br/>
							<%
							}
									name = tp.getCharacter(isOld,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
									}
							if(isTableExists){
							%>
							<input size="100" type="text"  title="address2" name="<%=name%>" value="<%=fieldValue%>"/><br/>
								<%
									}
									name = tp.getCharacter(isOld,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
									}
							if(isTableExists){
							%>
							<input size="100" type="text"  title="address3" name="<%=name%>" value="<%=fieldValue%>"/><br/>
								<%
								}
									name = tp.getCharacter(isOld,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
									}
							if(isTableExists){
							%>
							<input size="100" type="text"  title="address3" name="<%=name%>" value="<%=fieldValue%>"/><br/>
							<%}%>
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
									name = tp.getCharacter(isNew,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
									}
							if(isTableExists){
							%>
							<input size="50" type="text"  title="ENTER TITLE" name="<%=name%>" value="<%=fieldValue%>"/>
							<%}%>
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
							if(isTableExists){
							%>
							<input size="200" type="text"  title="ENTER INTRO HERE" name="<%=name%>" value="<%=fieldValue%>"/>
							<%}%>
						</td>
					</tr>
				</table>
				<hr size="2"/>
				<table border="0" cellspacing="2" cellpadding="4" bgcolor="#f5f5f5">
					<tr>
						<td>
							<%
							name =tp.getCharacter(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							if(isTableExists){%>
							<input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/>
							<%}%>
						</td>
						<td>
							<%
							name =tp.getCharacter(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							if(isTableExists){%>
							<input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/>
							<%}%>
						</td>
						<td>
							<%
							name =tp.getCharacter(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							if(isTableExists){%>
							<input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/>
							<%}%>
						</td>
						<td>
							<%
							name =tp.getCharacter(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							if(isTableExists){%>
							<input size="50" type="text"  title="ENTER Column Header" name="<%=name%>" value="<%=fieldValue%>"/>
							<%}%>
						</td>
					</tr>
					<tr>
						<td nowrap>
							<%
									name = tp.getCharacter(isNew,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
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
								<%
									name = tp.getCharacter(isOld,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
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
						</td>
						<td>
							<%
									name = tp.getCharacter(isOld,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
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
								<%
									name = tp.getCharacter(isOld,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
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
						</td>
						<td>
							<%
									name = tp.getCharacter(isNew,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
									}
								%>
								<% if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){%>
									<select name="<%=name%>"  title="Select an Image from list">
									<%
										ii = 0;
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
												}
										
								%>
									</select>   
									<%}	%>	
						</td>
						<td>
							<%
									name = tp.getCharacter(isOld,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
									}
								%>
								<% if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){%>
									<select name="<%=name%>"  title="Select an Image from the list">
										<%
										ii = 0;
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
										}
								%>
									</select>  
									<%}	%>					
						</td>
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
							if(isTableExists){%>
							<input size="80" type="text"  title="ENTER COPYRIGHT INFORMATION" name="<%=name%>" value="<%=fieldValue%>"/>
							<%
						}
							name =tp.getCharacter(isNew,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							if(isTableExists){%>
							<input size="50" type="text"  title="ENTER URL" name="<%=name%>" value="<%=fieldValue%>"/>
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
				<hr/>
				<p></p>
				<br/><br/>
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
				<input type="hidden"   name="<%=EFGImportConstants.SEARCH_PAGE_STR%>"  value="<%=EFGImportConstants.SEARCH_PAGE_STR%>"/>
				<input type="hidden"   name="<%=EFGImportConstants.SEARCH_TYPE_STR%>"  value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>"/>
		<%if( guid != null){%>
		<input type="hidden"   name="<%=EFGImportConstants.GUID%>"  value="<%=guid%>"/>
		<%}%>
				<input type="hidden"   name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>"  value="<%=uniqueName%>"/>
				<input type="hidden"   name="<%=EFGImportConstants.JSP_NAME%>"  value="<%=jspName%>"/>
				
				<input type="submit"  name="submit" value="Click to submit" align="middle" />	
		</form>
	</body>
</html>
