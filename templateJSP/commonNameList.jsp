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

			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			String xslFileName = "nantucketCommonNameSearchList.xsl";
	
            String templateMatch ="List Template2";
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


			Iterator it =null;
			List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
			List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);

			TemplatePopulator tpop = new  TemplatePopulator();

			Hashtable groupTable = tpop.populateTable(fileName.toString(), guid, EFGImportConstants.SEARCHPAGE_LISTS_XSL, datasourceName );
			if(groupTable == null){
					groupTable = new Hashtable();
			}

			int tableSize = table.size();
			TemplateProducer tp = new TemplateProducer();
			boolean isNew = true;
			boolean isOld = false;
			String name = null;
           String groupLabel= null;
			String groupLabelValue = null;
			String characterLabelValue = null;
			String characterLabel = null;
			int ii = 0;
            File imageFiles = new File(realPath + File.separator +  EFGImportConstants.templateImagesDirectory);
			 File[] imageFileList = imageFiles.listFiles(); 
			String defaultInfo ="Click on a name to view more information about that Taxon."; 
			if((table != null) && (table.size() > 0)){
				isTableExists = true;	
			}

			if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){
				isImagesExists = true;	
			}

		    File cssFiles = new File(realPath + File.separator +  EFGImportConstants.templateCSSDirectory);
			File[] cssFileList = cssFiles.listFiles(); 
			String cssLocation = context + "/" + EFGImportConstants.templateCSSDirectory  + "/";
			 String cssFile = "nantucketstyle.css";
		    if(!isTableExists){    	
	 			String forwardPage="NoDatasource.jsp";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
				dispatcher.forward(request, response);
		    }

		//sort and iterate
		%>
		<title>Nantucket Search List Template</title>
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
	<body alink="#660033" vlink="#660033" link="#6699FF" bgcolor="#ddeeff" text="#000000">
	 <form method="post" action="<%=context%>/configTaxonPage">
	 				 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
			<%if(cssFileList.length > 0){
				%>						
						<select name="<%=name%>"  title="Select an image from List">
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
					groupLabelValue ="imageheaders";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="imageheader";
					}
			%>
				 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			


		<table class="header">
			<tr>
				<td colspan="2" class="title">
							<%
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
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<%
							}
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
					<input type="text"  title="ENTER PAGE TITLE HERE" name="<%=name%>" value="<%=fieldValue%>"/> 
					<%}%>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="descrip">
						<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="headersInfo";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="headerInfo";
					}%>
									 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			

					<%
							if(isTableExists){
						%>
											<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input type="text"  title="ENTER HEADER INFO HERE"  size="100"   name="<%=name%>" value="<%=fieldValue%> "/> 
						<%}%>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<table class="specieslist">
						<tr>
							<td class="commonheader">
						<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="headersLeft";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="headerLeft";
					}%>
									 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			

					<%
							if(isTableExists){
						%>
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input type="text"  title="ENTER HEADER OF  LEFT COLUMN HERE" name="<%=name%>" value="<%=fieldValue%>"/> 
							<%}%>
							</td>
							<td class="sciheader">
						<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="headersRight";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="headerRight";
					}%>
									 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			

					<%
							if(isTableExists){
						%>
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input type="text"  title="ENTER HEADER OF  RIGHT COLUMN HERE" name="<%=name%>" value="<%=fieldValue%>"/> 
							<%}%>
							</td>
						</tr>
						<tr>
							<td class="spacerheight"></td>
							<td class="spacerheight"></td>
						</tr>
						<tr>
							<td class="commonname">
						<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="leftColumns";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="leftColumn";
					}%>
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
							<td class="sciname">
						<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="rightColumns";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="rightColumn1";
					}%>
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
						characterLabelValue ="rightColumn2";
					}%>
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
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<hr/>
		<br/><br/>
				<%
									
									fieldValue = (String)groupTable.get(EFGImportConstants.ISDEFAULT_STR);
									if(fieldValue == null){
										fieldValue = "false";
									}
									Boolean bool = new Boolean(fieldValue);
								
									
					%>
				<p>

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
		<input type="hidden"   name="<%=EFGImportConstants.SEARCH_TYPE_STR%>"  value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>"/>
		<%if( guid != null){%>
		<input type="hidden"   name="<%=EFGImportConstants.GUID%>"  value="<%=guid%>"/>
		<%}%>
		<input type="hidden"   name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>"  value="<%=uniqueName%>"/>
		<input type="hidden"   name="<%=EFGImportConstants.JSP_NAME%>"  value="<%=jspName%>"/>

	<input type="submit"  name="submit" value="Click to submit" align="middle" />	
		</form>
	</body>
</html>
