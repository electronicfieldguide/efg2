<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.EFGDataObjectListInterface,
project.efg.Imports.efgInterface.EFGDatasourceObjectInterface,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.servlets.efgInterface.EFGDataObject,
project.efg.util.EFGImportConstants,
java.util.List,java.util.Hashtable,
project.efg.util.TemplateProducer,
project.efg.util.TemplatePopulator,
java.util.Set,
project.efg.Imports.efgInterface.EFGQueueObjectInterface,
java.io.File,
project.efg.efgDocument.EFGDocument,
java.io.StringReader
" %>
<%
	String templateMatch ="PDF Plate Maker";
 	String guid = request.getParameter(EFGImportConstants.GUID);  
	String uniqueName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
	String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME);
	String datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	String numberOfResults = request.getParameter("numberOfResults");

	String searchquery = request.getParameter("searchquery");
	String whichDatabase = request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
	String xslFileName = "efgfo.xsl";
	if(whichDatabase == null || whichDatabase.trim().equals("")){
		whichDatabase =EFGImportConstants.EFG_RDB_TABLES;
	}
 	String context = request.getContextPath();
 
	String realPath = getServletContext().getRealPath("/");
	EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
	Iterator it =null;
	List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
	List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);

	TemplatePopulator tpop = new  TemplatePopulator();
	String name = null;
    String groupLabel= null;
	String groupLabelValue = null;
	String characterLabelValue = null;
	String characterLabel = null;
	String fieldName = null;
	String fieldValue = null;
	boolean isImagesExists = false;
	boolean isTableExists = false;
	int tableSize = table.size();
	TemplateProducer tp = new TemplateProducer();
	boolean isNew = true;
	boolean isOld = false;
			
	int ii = 0;
	
	StringBuffer fileName = new StringBuffer(realPath);
	fileName.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME); 
	fileName.append(File.separator);
	fileName.append(datasourceName.toLowerCase());
	fileName.append(EFGImportConstants.XML_EXT);
	Hashtable groupTable = tpop.populateTable(fileName.toString(), guid, EFGImportConstants.SEARCHPAGE_PLATES_XSL, datasourceName );
	if(groupTable == null){
		groupTable = new Hashtable();
	}
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
    File imageFiles = new File(realPath + File.separator +  EFGImportConstants.templateImagesDirectory);
	File[] imageFileList = imageFiles.listFiles(); 
	
	
	name =tp.getCharacter(isNew,isNew);
	fieldValue = (String)groupTable.get(name);
	if(fieldValue == null){
		fieldValue =numberOfResults;
	}
	else{
		numberOfResults = fieldValue;
		
	}
	groupLabel= tp.getCurrentGroupLabel(name);
	groupLabelValue = (String)groupTable.get(groupLabel);
	if(groupLabelValue == null){
		groupLabelValue ="queries";
	}
	characterLabel= tp.getCurrentCharacterLabel(name);
	characterLabelValue = (String)groupTable.get(characterLabel);
	if(characterLabelValue == null){
		characterLabelValue ="query";
	}

	String selectedDS="EFG selected: " + displayName;
	String queryResults = "Query results: " + numberOfResults;

	Hashtable paperSizes = new Hashtable();
	paperSizes.put("EFG_LETTER","8.5\"" + "x 11\"");
	paperSizes.put("EFG_LEGAL","8.5\"" +  "x 14\"");
	paperSizes.put("EFG_792_1224","11\"" +  "x 17\"");
	paperSizes.put("A4","A4");
	paperSizes.put("EFG_216_360","3\"" + "x 5\"");
	paperSizes.put("EFG_288_432","4\"" + "x 6\"");
	paperSizes.put("EFG_360_504","5\"" + "x 7\"");
	
	int numberoftitles = 4; 
	String creditsText = "Credits/Copyright Info Goes Here";
	String[] titlesstr = {
			"Main Title Goes Here",
			"Subtitle Goes Here",
			creditsText,
			creditsText
	};
	String[] titleslabel = {
			"maintitle",
			"subtitle",
			"creditstitle1",
			"creditstitle2"
	};
	String[] titlesize = {
			"titlesize",
			"subtitlesize",
			"creditstitle1size",
			"creditstitle2size"
	};

	String[] titleformat = {
			"titleformat",
			"subtitleformat",
			"creditstitle1format",
			"creditstitle2format"
	};

	
	String[] fontslabel = {
			"maintitlefont",
			"subtitlefont",
			"creditstitle1font",
			"creditstitle2font"
	};
	//read from a properties file
	String[] fontsstr = {
		"Arial","Times"
	};

%>

 <form method="post" action="<%=context%>/efg2pdfconfig" target="_blank" onsubmit="return insertQueryResultsXML();">

	<table class="form" id="pdfFillFormID">
		<tr>
			<td class="datasourcename" colspan="2"><%=selectedDS%> </td>
			<td><input type="hidden"    name="searchqueryresultsxml" id="searchqueryresultsxml" value=""/>			
			</td>
		</tr>
		<tr>
			<td class="formitem" colspan="2"><%=queryResults%>
				<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>	
				<input type="hidden"    name="<%=name%>" value="<%=fieldValue%>"/>								  				 
			
				<%	
					//add the query
					name =tp.getCharacter(isOld,isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue =searchquery;
					}
					else{
						searchquery = fieldValue;
						
					}
					%>
					<input type="hidden"    name="<%=name%>" value="<%=fieldValue%>"/>								  				 				
				
			</td>
		</tr>
		<tr>
			<td class="instructions" colspan="2">Design your plates using the items below. Anything that you leave blank will not appear on the plates.</td>
		</tr>
	
		<tr colspan="2">
			<td class="subheading">General Settings
			<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="generalsettings";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue ="generalsettingsort";
				}
			%>
				<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
			</td>
		</tr>
		<tr>
			<td class="formitem"><span class="configitem">Sort by:</span> 
				<select name="<%=name%>">
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
					fieldName =queueObject.getObject(1);
					if(ii==0){
					%>
					<option></option>
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
			<td class="formitem" colspan="2"><span class="configitem">Image to display:</span> 
			<%
			name =tp.getCharacter(isOld,isOld);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
			characterLabel= tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String)groupTable.get(characterLabel);
			if(characterLabelValue == null){
				characterLabelValue ="generalsettingimageColumn";
			}
		
			if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){%>
				<select name="<%=name%>"  onChange="JavaScript:checkSelected(this);">
				<%ii=0;
				it = mediaResourceFields.iterator();
				while (it.hasNext()) {
					EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
					fieldName = queueObject.getObject(1);
					if(ii==0){
					%>
					<option></option>
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
			} 
			%>
			</td>
		</tr>		
		<tr>
			<td class="formitem">Dimensions <span class="note">(Min 1, Max 100):</span><br />
				 <span class="configitem">Rows:</span> 
				 <%
				 	name =tp.getCharacter(isOld,isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="4";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="generalsettingrowdimension";
					}
				 %>
				 <input type="text" size="3" maxlength="3" name="<%=name%>" value="<%=fieldValue%>" onKeyDown="resetBackgroundColor(this,'');" onMouseDown="resetBackgroundColor(this,'');" onBlur="checkNumeric(this,1,100,'');"/> 
				 <input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
				 <span class="configitem">Columns:</span> 
				 <%
				 	name =tp.getCharacter(isOld,isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="4";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="columndimension";
					}
				 %>
				 <input type="text" size="3" maxlength="3" name="<%=name%>" value="<%=fieldValue%>"  onKeyDown="resetBackgroundColor(this,'');" onMouseDown="resetBackgroundColor(this,'');" onBlur="checkNumeric(this,1,100,'');"/>
				 <input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			</td>
			<td class="formitem" colspan="2">
				<span class="configitem">Paper size:</span> 
				 <%
				 	name =tp.getCharacter(isOld,isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="generalsettingpapersize";
					}
				 %>
				<select name="<%=name%>">
					<%
					Set set = paperSizes.keySet();
					it = set.iterator();
					while (it.hasNext()) {
						fieldName = (String)it.next();
						String keyValue = (String)paperSizes.get(fieldName);
						if(fieldName.equals(fieldValue)){
						%>
							<option value="<%=fieldName%>" selected="selected">                                                           <%=keyValue%>
							</option>
						<%}
						else{
						%>
							<option value="<%=fieldName%>">                                                           <%=keyValue%>
							</option>
						<%
						}
					}
					%>						
				</select>
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			</td>
		</tr>		
		<tr>
			<td colspan="2">
			<hr />
			</td>
		</tr>	
		<tr colspan="2">	
			<td class="subheading">Text Settings		
			</td>
		</tr>	
		<tr>
			<td class="formitem" colspan="2">Captions: 
				<span class="configitem">
				<%
					name =tp.getCharacter(isNew,isNew);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}
					groupLabel= tp.getCurrentGroupLabel(name);
					groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="textsettings";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="textsettingimageabove";
					}
				%>
					<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
																
					<%if(fieldValue.equalsIgnoreCase("above")){%>
						<input type="radio" name="<%=name%>" value="above" checked="checked">above image
					<% }else { %>
						<input type="radio" name="<%=name%>" value="above" />above image
					<%} %>
				</span>
				<span class="configitem">
					 <%
					 	name =tp.getCharacter(isOld,isOld);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue ="textsettingimagebelow";
						}
					 %>
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
					<%if(fieldValue.equalsIgnoreCase("below")){%>
						<input type="radio" name="<%=name%>" value="<%=fieldValue%>" checked="checked"/>below image
					<%} else{ %>
						<input type="radio" name="<%=name%>" value="below" />below image
					<%} %>
				</span> 
				<%
					int numberofcaptions = 2;
	
					for(int i=0; i < numberofcaptions; i++){
						name =tp.getCharacter(isNew,isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							if(i == 0){
								groupLabelValue ="captionstext1";
							}
							else{
								groupLabelValue ="captionstext2";
							}
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
	
					%>
						<p class="captionrow">
						<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>	
							<% if(i ==0 ){ %>
										
								<% if(characterLabelValue == null){
									characterLabelValue ="captiontext1";
								}
							%>
								<span class="configitem">Text for Row 1:</span>
							<%} 
							else {
								characterLabelValue ="captiontext2";
							%>
								<span class="configitem">Text for Row 2:</span> 
							<%} %>
							<select name="<%=name%>"  onChange="JavaScript:checkSelected(this);">
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
								fieldName =queueObject.getObject(1);
								if(ii==0){
								%>
								<option></option>
								<%
								}
								if(fieldName.equals(fieldValue)){
								%>
								<option selected="selected">                                                                 <%=fieldName%>
								</option>
								<%
								}
								else{
								%>
								<option>                                                                 <%=fieldName%>
								</option>
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
							 %>
							<% if(i ==0 ){ 
									if(characterLabelValue == null){
										characterLabelValue ="captiontext1font";
									}
								} 
								else {
									if(characterLabelValue == null){
										characterLabelValue ="captiontext2font";
									}
								} 
							 %>
							<span class="configitem">Font:</span> 
							<select name="<%=name%>"><!-- Show built in fonts? -->
							<% 
							for(int j = 0; j < fontsstr.length; j++){
								fieldName = fontsstr[j];
								if(fieldName.equalsIgnoreCase(fieldValue)){%>
								<option value="<%=fieldName%>" selected="selected">                                                                  <%=fieldName%>
								</option>
								<%} else{ %>
								<option value="<%=fieldName%>">                                                                  <%=fieldName%>
								</option>
								<%}%>
							<%} %>
							</select>
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
							
							 <%
							 	name =tp.getCharacter(isOld,isOld);
								fieldValue = (String)groupTable.get(name);
								if(fieldValue == null){
									fieldValue ="10";
								}
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="textsettingsize";
								}
							 %>
		
		
							<span class="configitem">Size</span> 
							<span class="note">(6 to 100 pt):</span> 
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
							<input type="text" name="<%=name%>" size="2" maxlength="3" value="<%=fieldValue%>"  onKeyDown="resetBackgroundColor(this,'');" onMouseDown="resetBackgroundColor(this,'');" onBlur="checkNumeric(this,6,100,'');"/> 
							 <%
							 	name =tp.getCharacter(isOld,isOld);
								fieldValue = (String)groupTable.get(name);
								if(fieldValue == null){
									fieldValue ="";
								}
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="textsettingbold";
								}
							 %>		
							<span class="configitem">Style:</span> 
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
							<%if(fieldValue.equalsIgnoreCase("bold")){%>					
								<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked"/>
							<%}
							else{%>
								<input type="checkbox" name="<%=name%>"  value="bold"/>
							
							<%}%>
							<span class="boldcheck">bold</span> 
														
							 <%
							 	name =tp.getCharacter(isOld,isOld);
								fieldValue = (String)groupTable.get(name);
								if(fieldValue == null){
									fieldValue ="";
								}
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="textsettingitalics";
								}
							 %>
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
							<%if(fieldValue.equalsIgnoreCase("italics")){%>					
								<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked"/>
							<%}
							else{%>
								<input type="checkbox" name="<%=name%>" value="italics" />
							
							<%}%>
							<span class="italicscheck">italics</span> 
							
							 <%
							 	name =tp.getCharacter(isOld,isOld);
								fieldValue = (String)groupTable.get(name);
								if(fieldValue == null){
									fieldValue ="";
								}
								characterLabel= tp.getCurrentCharacterLabel(name);
								characterLabelValue = (String)groupTable.get(characterLabel);
								if(characterLabelValue == null){
									characterLabelValue ="textsettingunderline";
								}
							 %>
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
							<%if(fieldValue.equalsIgnoreCase("underline")){%>					
								<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked"/>
							<%}
							else{%>
								<input type="checkbox" name="<%=name%>" value="underline" />								
							<%}%>
							<span class="underlinecheck">underline</span>							
						</p>
				<%} 
				name =tp.getCharacter(isNew,isNew);//guaranteed to be generated at least once
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="titles";
				}
				%>
				<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>						
			</td>
		</tr>
		<%	for(int i = 0; i < numberoftitles; i++ ) {
				fieldName = titlesstr[i];
				if( i > 0){
					name =tp.getCharacter(isOld,isOld);
				}
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue =titleslabel[i];
				}
		%>
		<tr>
			<td class="formitem" colspan="2">
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
				<%if(fieldValue.equalsIgnoreCase(fieldName)){ %>
				<input type="text" name="<%=name%>" size="50" value="<%=fieldValue%>" /> 
				<%} else { %>
				<input type="text" name="<%=name%>" size="50" value="<%=fieldName%>" class="formInputText" onfocus="clearField(this)" title="<%=fieldName%>"/> 
				<%}	
				name =tp.getCharacter(isOld,isOld);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue =fontslabel[i];
				}						
				%>								
				<select name="<%=name%>">
					<% 
					for(int j = 0; j < fontsstr.length; j++){
						fieldName = fontsstr[j];
						if(fieldName.equalsIgnoreCase(fieldValue)){%>
						<option value="<%=fieldName%>" selected="selected">                                                                  <%=fieldName%>
						</option>
						<%} else{ %>
						<option value="<%=fieldName%>">                                                                  <%=fieldName%>
						</option>
						<%}%>
					<%} %>
				</select>
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 
				<span class="configitem">Size</span> 	
				<span class="note">(6 to 100 pt):</span> 
				<%
				name =tp.getCharacter(isOld,isOld);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="10";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue =titlesize[i];
				}						
				%>								
				<input type="text" name="<%=name%>" size="2" maxlength="3" value="<%=fieldValue%>"  onKeyDown="resetBackgroundColor(this,'');" onMouseDown="resetBackgroundColor(this,'');" onBlur="checkNumeric(this,6,100,'');"/>
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 						
				<span class="configitem">Style:</span>
	
				<%
				name =tp.getCharacter(isOld,isOld);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue =titleformat[i];
				}
				fieldName ="bold";
				if(fieldName.equals(fieldValue)){%>
					<input type="checkbox" name="<%=name%>" value="<%=fieldName%>" checked="checked"/>
				<%} else{%>
					<input type="checkbox" name="<%=name%>" value="<%=fieldName%>"/>
				
				<%}%>
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
				<span class="boldcheck">bold</span> 
				
				<%
				name =tp.getCharacter(isOld,isOld);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue =titleformat[i];
				}
				fieldName ="italics";
				if(fieldName.equals(fieldValue)){%>
					<input type="checkbox" name="<%=name%>" value="<%=fieldName%>" checked="checked"/>
				<%} else{%>
					<input type="checkbox" name="<%=name%>" value="<%=fieldName%>"/>
				
				<%}%>
				<span class="italicscheck">italics</span>
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
					characterLabelValue =titleformat[i];
				}
				fieldName ="underline";
				if(fieldName.equals(fieldValue)){%>
					<input type="checkbox" name="<%=name%>" value="<%=fieldName%>" checked="checked"/>
				<%} else{%>
					<input type="checkbox" name="<%=name%>" value="<%=fieldName%>"/>						
				<%}%>
				<span class="underlinecheck">underline</span> 
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
			</td>
		</tr>
		<%}
		%>
		<tr>
			<td colspan="2">
				<hr />
			</td>
		</tr>		
		<tr colspan="2">
			<td class="subheading">Image and Color Settings</td>
		</tr>		
		<tr>
			<td class="formitem" colspan="2">Add pre-sized images to use as logos in the credits section 
				<span class="note">(must be .jpg, .gif or .png format, less than 1 inch print size recommended):</span><br/>
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
				<select name="<%=name%>" size="5" multiple="multiple">
				<% 
				for (int i=0; i < imageFileList.length; i++ ) { 
					File imgFile = imageFileList[i];
					fieldName = imgFile.getName();
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
				}%>
				</select>
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
			</td>
		</tr>	
		<tr>
			<td class= "uploadnote" colspan="2">
			(To select multiple images, hold down the Ctrl key. To add additional images, use the 
			<a class="upload" href="/efg2/uploadTemplateResources.html#uploadIM" target="_blank">Upload interface</a>.) 
			<%
				String[] numberOptions = {"0", "1","2","3","4","5"};
			%>
			</td>
		</tr>			
		<tr>
			<td class="formitem">White space around image: 
			<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="imagewhitespaces";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue ="imagewhitespace";
				}
			%>
				<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
				<select name="<%=name%>">						
				<% 
				for(int i =0; i < numberOptions.length;i++) {
					fieldName = numberOptions[i];
					if(i==0){
							fieldName = "0 (none)"; 
					}
					if(fieldName.equals(fieldValue)){
						%>
					<option selected="selected">                                                            <%=fieldName%>
					</option>
						<%
					}
					else{
						%>
						<option>                                                            <%=fieldName%>
						</option>
						<%
					}
				}%>
				</select>
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
				
			</td>
			<td class="formitem">White space between elements <span class="note">(image plus captions):</span> 
				<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="elementwhitespaces";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue ="elementwhitespace";
				}
			%>
				<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
				<select name="<%=name%>">						
				<% 
				for(int i =0; i < numberOptions.length;i++) {
					fieldName = numberOptions[i];
					if(i==0){
						fieldName = "0 (none)"; 
					}
					if(fieldName.equals(fieldValue)){
					%>
						<option selected="selected">                                                            <%=fieldName%>
						</option>
					<%
					}
					else{
						%>
					<option>                                                            <%=fieldName%>
					</option>
					<%
					}
				}%>
				</select>
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
			</td>
		</tr>			
		<tr>
			<td class="formitem">Frame around image:<br />
				<br />
				 <span class="configitem">Weight:</span> 
				<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="imageframeweights";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue ="imageframeweight";
				}
			%>
				<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
				<select name="<%=name%>">						
				<% 
				for(int i =0; i < numberOptions.length;i++) {
					fieldName = numberOptions[i];
					if(i==0){
						fieldName = "0 (none)"; 
					}
					if(fieldName.equals(fieldValue)){
					%>
						<option selected="selected">                                                            <%=fieldName%>
						</option>
					<%
					}
					else{
						%>
					<option>                                                            <%=fieldName%>
					</option>
					<%
					}
				}%>
				</select>
				<input type="hidden"  name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
				<br/>
	 			<span class="configitem">Select a color:</span> 
				<%
				name =tp.getCharacter(isOld,isOld);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="#7C3B35";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue ="imageframecolor";
				}
				%>
	
	 				<input id="imageframecolor1id" name="<%=name%>" class="color"  value="<%=fieldValue%>" type="text"/>
	 			
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
		 	</td>
			<td class="formitem">Bounding Box <span class="note">(frame image plus captions):</span><br />
				<br />
				<span class="configitem">Weight:</span> 
				<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="boundingboxweights";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue ="boundingboxweight";
				}
				%>
				<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
				<select name="<%=name%>">						
				<% 
				for(int i =0; i < numberOptions.length;i++) {
					fieldName = numberOptions[i];
					if(i==0){
						fieldName = "0 (none)"; 
					}
					if(fieldName.equals(fieldValue)){
					%>
						<option selected="selected">                                                            <%=fieldName%>
						</option>
					<%
					}
					else{
						%>
					<option>                                                            <%=fieldName%>
					</option>
					<%
					}
				}%>
				</select>
				<br/>
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
	
	 			<span class="configitem">Select a color: 
	 				<span>
	 					<%
						name =tp.getCharacter(isOld,isOld);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="#BA575A";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue ="boundingboxcolor";
						}%>						
		 					<input id="boundingboxcolor1id" name="<%=name%>" class="color" value="<%=fieldValue%>" type="text">
							<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
	 				</span>
	 			</span>
			 </td>
		</tr>
		<tr>
		<td class="formitem" colspan="2">Fill space around disproportionate images? 
			<%
			name =tp.getCharacter(isNew,isNew);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
			groupLabel= tp.getCurrentGroupLabel(name);
			groupLabelValue = (String)groupTable.get(groupLabel);
			if(groupLabelValue == null){
				groupLabelValue ="fillbackgrounds";
			}
			characterLabel= tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String)groupTable.get(characterLabel);
			if(characterLabelValue == null){
				characterLabelValue ="fillbackground";
			}
			fieldName = "above";
			%>
			<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
			<%if(fieldName.equals(fieldValue)){%>
				<input type="checkbox" name="<%=name%>" value="<%=fieldName%>" checked="checked"/>
			<%} else {%>
				<input type="checkbox" name="<%=name%>" value="<%=fieldName%>"/>						
			<%}%>
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
			<span class="configitem">yes</span><br />
			<span class="note">Select a color:</span> 
			<%
			name =tp.getCharacter(isOld,isOld);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="#D4E5E0";
			}
			characterLabel= tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String)groupTable.get(characterLabel);
			if(characterLabelValue == null){
				characterLabelValue ="boundingboxcolor";
			}%>	
				<input type="hidden" name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
				<input id="boundingboxcolor2id" name="<%=name%>" class="color" value="<%=fieldValue%>" type="text"/>				
			</td>
		</tr>
			<input type="hidden"   name="<%=EFGImportConstants.SEARCH_PAGE_STR%>"  value="<%=EFGImportConstants.SEARCH_PAGE_STR%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.SEARCH_TYPE_STR%>"  value="<%=EFGImportConstants.SEARCH_PDFS_TYPE%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.DISPLAY_NAME%>"  value="<%=displayName%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_NAME%>"  value="<%=datasourceName%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.HTML_TEMPLATE_NAME%>"  value="<%=templateMatch%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.XSL_STRING%>"  value="<%=xslFileName%>"/>
	<%if( guid != null){%>
		<input type="hidden"   name="<%=EFGImportConstants.GUID%>"  value="<%=guid%>"/>
	<%}%>
	<input type="hidden"   name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>"  value="<%=uniqueName%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.JSP_NAME%>"  value="platedesignform.jsp"/>	
	<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
		
	</table>
	<input type="submit"  name="submit" value="Click to submit" align="middle"/>
	<!--
	<input type="submit"  name="submit" value="Save Configuration" align="middle" onclick="javascript:saveConfig('pdfFillFormID');"/>	
	-->
</form>

