<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.EFGDataObjectListInterface,
project.efg.Imports.efgInterface.EFGDatasourceObjectInterface,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.servlets.efgInterface.EFGDataObject,
project.efg.util.EFGImportConstants,
java.util.List,
java.util.ArrayList,
java.util.Hashtable,
project.efg.util.TemplateProducer,
project.efg.util.TemplatePopulator,
project.efg.util.PDFGUIConstants,
java.util.Properties,
project.efg.Imports.efgInterface.EFGQueueObjectInterface,
java.io.File
" %>
<%
	List PAPER_SIZE_LIST = new ArrayList();
	PAPER_SIZE_LIST.add("EFG_LETTER");
	PAPER_SIZE_LIST.add("EFG_LEGAL");
	PAPER_SIZE_LIST.add("EFG_792_1224");
	PAPER_SIZE_LIST.add("EFG_A4");
	PAPER_SIZE_LIST.add("EFG_216_360");
	PAPER_SIZE_LIST.add("EFG_288_432");
	PAPER_SIZE_LIST.add("EFG_360_504");
 	String guid = request.getParameter(EFGImportConstants.GUID);  
	String uniqueName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
	String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME);
	String datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	String numberOfResults = request.getParameter(PDFGUIConstants.numberOfResultsStr);

	String searchquery = request.getParameter(PDFGUIConstants.searchqueryStr);
	String whichDatabase = request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
	
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
 		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + PDFGUIConstants.forwardPage);
		dispatcher.forward(request, response);
	}
    File imageFiles = new File(realPath + File.separator +  EFGImportConstants.templateImagesDirectory);
	File[] imageFileList = imageFiles.listFiles(); 
	
	
	name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
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
		groupLabelValue =PDFGUIConstants.QUERIES;
	}
	characterLabel= tp.getCurrentCharacterLabel(name);
	characterLabelValue = (String)groupTable.get(characterLabel);
	if(characterLabelValue == null){
		characterLabelValue = PDFGUIConstants.RESULTS;
	}

	String selectedDS="EFG selected: " + displayName;
	String queryResults = "Query results: " + numberOfResults;
	

	session.setAttribute("mediaResourceFields",mediaResourceFields);
	session.setAttribute("templateProducer",tp);
	session.setAttribute("listFields",table);
%>
<form method="post" action="<%=context%>/efg2pdfconfig" target="_blank" onsubmit="return insertQueryResultsXML();">	
	<table class="form" id="pdfFillFormID">
		<tr>
			<td class="datasourcename" colspan="2"><%=selectedDS%> 
				<input type="hidden"    name="searchqueryresultsxml" id="searchqueryresultsxml" value=""/>			
				
			 <!-- Name of selected datasource would go here -->
			</td>
		</tr>	
		<tr>
			<td class="formitem" colspan="2"><%=queryResults%>
				<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
				<input type="hidden"    name="<%=name%>" value="<%=fieldValue%>"/>			
				<%	
					//add the query
					name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue = PDFGUIConstants.QUERY;
					}
									
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue =searchquery;
					}
					else{
						searchquery = fieldValue;
					}
				%>
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
				<input type="hidden"    name="<%=name%>" value="<%=fieldValue%>"/>								  				 							


			 	<!-- list query results (number of taxa returned, items searched) -->
			 </td>
		</tr>	
		<tr>
			<td class="instructions" colspan="2">Design your plates using the items below. Any text windows that you leave blank will not appear on the plates.</td>
		</tr>
		<tr colspan="2">
			<td class="subheading">Data Display Settings
				<%
					name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}
					groupLabel= tp.getCurrentGroupLabel(name);
					groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue =PDFGUIConstants.GENERAL_SETTINGS;
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue =PDFGUIConstants.GENERAL_SETTING_SORT;
					}
				%>
				<input type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
			</td>
		</tr>	
		<tr>
			<td class="formitem" colspan="2"><span class="configitem">Sort by:</span> 
			<!-- Make sure no lists/mediaresources are shown -->
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
		</tr>
		<tr>
			<td class="formitem" colspan="2">
			<span class="configitem">Image to display:</span> 
				<%
				name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue =PDFGUIConstants.GENERAL_SETTING_IMAGE_COLUMN;
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
					name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue =PDFGUIConstants.GENERAL_SETTING_IMAGE_NUMBER;
					}
					%>
				<br>
				<span class="subconfigitem">
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
					<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.IMAGE_NUMBER)){%>					
						<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked"/>Show all images (default is to display the first one only)
					<%}
					else{%>
						<input type="checkbox" name="<%=name%>" value="<%=PDFGUIConstants.IMAGE_NUMBER%>" />Show all images (default is to display the first one only)
					<%}%>
				</span>
				<%
					name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue =PDFGUIConstants.GENERAL_SETTING_SHOW_CAPTIONS;
					}
					%>		
				<br>
				<span class="subconfigitem">
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
				
				<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.SHOW_CAPTIONS)){%>					
					<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked"/>Show all images (default is to display the first one only)
				<%}
				else{%>
					<input type="checkbox" name="<%=name%>" value="<%=PDFGUIConstants.SHOW_CAPTIONS%>" /> Always display captions (default is to skip if no image is available)
				<%}%>
				 </span>
					<%	
					} else{
						name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue =PDFGUIConstants.GENERAL_SETTING_SHOW_CAPTIONS;
						}
					
					%>
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
					
					<input type="checkbox" name="<%=name%>" value="<%=PDFGUIConstants.SHOW_CAPTIONS%>" checked="checked"/> Always display captions (default is to skip if no image is available)
					<%
					}		
					%>
			</td>
		</tr>	
		<tr>
			<td class="formitem">Dimensions 
			<span class="note">(Min 1, Max 100):</span>
			<br/>			
				 <span class="configitem">Rows:</span>
			 	<%
				 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="4";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue = PDFGUIConstants.GENERAL_SETTING_ROW_DIMENSION;
					}
			 	%>			  
				  <input type="text" size="3" maxlength="3" name="<%=name%>" value="<%=fieldValue%>" onKeyDown="resetBackgroundColor(this,'');" onMouseDown="resetBackgroundColor(this,'');" onBlur="checkNumeric(this,1,100,'');"/> 
				  <input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>	  
				 <span class="configitem">Columns:</span> 
				 <%
				 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="4";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue =PDFGUIConstants.GENERAL_SETTING_COLUMN_DIMENSION;
					}
				 %>
				 <input type="text" size="3" maxlength="3" name="<%=name%>" value="<%=fieldValue%>"  onKeyDown="resetBackgroundColor(this,'');" onMouseDown="resetBackgroundColor(this,'');" onBlur="checkNumeric(this,1,100,'');"/>
				 <input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			</td>
			<td class="formitem" colspan="2">
				<span class="configitem">Paper size:</span> 
				 <%
				 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue =PDFGUIConstants.GENERAL_SETTING_PAPER_SIZE;
					}
				 %>
				<select name="<%=name%>">
					<%					
					for (int index1=0; index1 < PAPER_SIZE_LIST.size();index1++) {
						fieldName = (String)PAPER_SIZE_LIST.get(index1);
						String keyValue = PDFGUIConstants.paperSizeProperties.getProperty(fieldName);
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
				<br />								
				<%
				 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="portrait";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="paperorientation";
					}
				 %>	
				<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
				<span class="subconfigitem">	 
				<%if(fieldValue.equalsIgnoreCase("portrait")){%>
					<input type="radio" name="<%=name%>" value="<%=fieldValue%>" checked="checked" />portrait 
					<input type="radio" name="<%=name%>" value="landscape"/>landscape 
				<%} else{%>
					<input type="radio" name="<%=name%>" value="<%=fieldValue%>"/>portrait 
					<input type="radio" name="<%=name%>" value="portrait" checked="checked"/>landscape 
				<%}%>				
				</span>
			</td>
		</tr>		
		<!-- submitting enhancement request for the following: -->
		<!-- <tr> -->
		<!-- <td class="formitem" colspan="2">If you would like to use custom fonts, point to the location of the fonts folder on your hard drive or the server:<br /> -->
		<!--  <input type="file" name="logo1" size="25" /> </td> -->
		<!-- Not sure if type="file" is right here -->
		<!-- </tr>  -->	
		<tr>
			<td colspan="2">
			<hr />
			</td>
		</tr>	
		<tr colspan="2">
			<td class="subheading">Text Settings</td>
		</tr>		
		<tr colspan="2">
			<td class="formitem">Image Captions:</td>
		</tr>
	
		<tr>
			<td class="formitemcaption" colspan="2">
				<table class="captions">
				<%
					int numberofcaptions = 2;
					int indexCapsAbove = 1;
					boolean isLoop = true;
					while(isLoop){
						name = tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							if(indexCapsAbove > numberofcaptions){
								isLoop = false;
								break;
							}
							fieldValue ="";
						}
						
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){						
							groupLabelValue =PDFGUIConstants.CAPTIONS_TEXT_ABOVE + indexCapsAbove;
						
						}
						else{
							if(groupLabelValue.indexOf(PDFGUIConstants.BELOW) > -1){
								isLoop = false;
								break;
							}
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue =PDFGUIConstants.CAPTION_TEXT+ indexCapsAbove;
						}
					%>				
				<tr>
					<td class="captionrow">
						<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>							
						<span class="configitemfield">Field:</span> 
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
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCapsAbove + PDFGUIConstants.FONT;
							}
						 %>
						<p class="captionline">
						<span class="configitem">Font:</span> 
						<select name="<%=name%>"><!-- Show built in fonts? -->
						<% 
						for(int j = 0; j < PDFGUIConstants.FONTS_NAME.length; j++){
							fieldName = PDFGUIConstants.FONTS_NAME[j];
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
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="8";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCapsAbove + PDFGUIConstants.SIZE;
							}
						 %>							
						<span class="configitem">Size</span> 
						<span class="note">(6 to 100 pt):</span> 
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<input type="text" name="<%=name%>" size="2" maxlength="3" value="<%=fieldValue%>"  onKeyDown="resetBackgroundColor(this,'');" onMouseDown="resetBackgroundColor(this,'');" onBlur="checkNumeric(this,6,100,'');"/> 
						 <%
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCapsAbove + PDFGUIConstants.BOLD;
							}
						 %>		
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.BOLD)){%>					
							<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked"/>
						<%}
						else{%>
							<input type="checkbox" name="<%=name%>"  value="bold" title="bold"/>							
						<%}%>
						<span class="boldcheck" title="bold">B</span> 
						 <%
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCapsAbove + PDFGUIConstants.ITALICS;
							}
						 %>
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.ITALICS)){%>					
							<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked" title="italics" />
						<%}
						else{%>
							<input type="checkbox" name="<%=name%>" value="italics" title="italics" />						
						<%}%>							 							
						<span class="italicscheck" title="italics">i</span> 
						<%
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + 
								indexCapsAbove + PDFGUIConstants.UNDER_LINE;
							}
						%>
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.UNDER_LINE)){%>					
							<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked" title="underline"/>
						<%}
						else{%>
							<input type="checkbox" name="<%=name%>" value="underline" title="underline"/>								
						<%}%>
						<span class="underlinecheck" title="underline">u</span>
						<span class="configitem">Align:</span> 
						<%
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + 
								indexCapsAbove + PDFGUIConstants.ALIGN;
							}
						 %>
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.ALIGN_CENTER)){%>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_LEFT%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_LEFT%></span>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_CENTER%>" checked="checked" />								
							<span class="align"><%=PDFGUIConstants.ALIGN_CENTER%></span> 
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_RIGHT%>"/>	
							<span class="align"><%=PDFGUIConstants.ALIGN_RIGHT%></span>
						<%}
						else if(fieldValue.equalsIgnoreCase(PDFGUIConstants.ALIGN_RIGHT)){%>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_LEFT%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_LEFT%></span>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_CENTER%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_CENTER%></span> 
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_RIGHT%>" checked="checked" />	
							<span class="align"><%=PDFGUIConstants.ALIGN_RIGHT%></span>
						<%} else {%>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_LEFT%>" checked="checked" />
							<span class="align"><%=PDFGUIConstants.ALIGN_LEFT%></span>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_CENTER%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_CENTER%></span> 
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_RIGHT%>"/>	
							<span class="align"><%=PDFGUIConstants.ALIGN_RIGHT%></span>
						<% }%>	
						</p>						
					</td>
				</tr>
				<%
					++indexCapsAbove;
				}//end is loop
								
				%>
				<tr id="spanabovebuttonid">
					<td class="captionbutton" id="captionabovebuttonid">											
						<input type="hidden" id="captionaboveid" name="<%=PDFGUIConstants.ABOVE_BELOW%>" value="<%=PDFGUIConstants.ABOVE%>"/>					
						<input type="hidden" id="numberofcaptionsaboveid" name="<%=PDFGUIConstants.INDEXCAP%>" value="<%=indexCapsAbove%>"/>
						<input class="addrow" type="button" name="Add new row" value="Add row" 
						onClick="javascript:AddCaptionRow('CreateMoreFieldRows.jsp',
						'spanabovebuttonid','captionaboveid','numberofcaptionsaboveid')"/>
					</td>
				</tr>
				<tr>
					<td class="sampleimage">
					<!-- TO DO -->		
					<%
						name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue =PDFGUIConstants.ALIGN_LEFT;
						}
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							groupLabelValue ="imagesalignment";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue ="imagealignment";
						}
					%>
					<img src="sampledragonfly.jpg" width="200" height="162" title="Sample Image" border="1" alt="Sample Image"/>
					<span class="configitem">Align Image:</span> 
						<input type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>
						<input type="hidden" name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.ALIGN_CENTER)){%>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_LEFT%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_LEFT%></span>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_CENTER%>" checked="checked" />								
							<span class="align"><%=PDFGUIConstants.ALIGN_CENTER%></span> 
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_RIGHT%>"/>	
							<span class="align"><%=PDFGUIConstants.ALIGN_RIGHT%></span>
						<%}
						else if(fieldValue.equalsIgnoreCase(PDFGUIConstants.ALIGN_RIGHT)){%>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_LEFT%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_LEFT%></span>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_CENTER%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_CENTER%></span> 
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_RIGHT%>" checked="checked" />	
							<span class="align"><%=PDFGUIConstants.ALIGN_RIGHT%></span>
						<%} else {%>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_LEFT%>" checked="checked" />
							<span class="align"><%=PDFGUIConstants.ALIGN_LEFT%></span>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_CENTER%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_CENTER%></span> 
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_RIGHT%>"/>	
							<span class="align"><%=PDFGUIConstants.ALIGN_RIGHT%></span>
						<% }%>	
					</td>
				</tr>

				<%
				numberofcaptions = 2;
				int indexCapsBelow = 1;
				isLoop = true;
				while(isLoop){
					name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						if(indexCapsBelow > numberofcaptions){
							isLoop = false;
							break;
						}
						fieldValue ="";
					}
					
					groupLabel= tp.getCurrentGroupLabel(name);
					groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){						
						groupLabelValue =PDFGUIConstants.CAPTIONS_TEXT_BELOW + indexCapsBelow;	
					}
					else{
						if(groupLabelValue.indexOf(PDFGUIConstants.ABOVE) > -1){
							isLoop = false;
							break;
						}
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue =PDFGUIConstants.CAPTION_TEXT+ indexCapsBelow;
					}
				%>			
				<tr>
					<td class="captionrow">
					<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>		
						<span class="configitemfield">Field:</span> 
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
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCapsBelow + PDFGUIConstants.FONT;
							}
						 %>
						 <p class="captionline">
						<span class="configitem">Font:</span> 
						<select name="<%=name%>"><!-- Show built in fonts? -->
						<% 
						for(int j = 0; j < PDFGUIConstants.FONTS_NAME.length; j++){
							fieldName = PDFGUIConstants.FONTS_NAME[j];
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
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="8";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCapsBelow + PDFGUIConstants.SIZE;
							}
						 %>							
						<span class="configitem">Size</span> 
						<span class="note">(6 to 100 pt):</span> 
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<input type="text" name="<%=name%>" size="2" maxlength="3" value="<%=fieldValue%>"  onKeyDown="resetBackgroundColor(this,'');" onMouseDown="resetBackgroundColor(this,'');" onBlur="checkNumeric(this,6,100,'');"/> 
						<%
					 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCapsBelow + PDFGUIConstants.BOLD;
						}
						%>		
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.BOLD)){%>					
							<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked" title="bold"/>
						<%}
						else{%>
							<input type="checkbox" name="<%=name%>"  value="bold" title="bold"/>							
						<%}%>
						<span class="boldcheck" title="bold">B</span> 
						 <%
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCapsBelow + PDFGUIConstants.ITALICS;
							}
						 %>
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.ITALICS)){%>					
							<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked"/>
						<%}
						else{%>
							<input type="checkbox" name="<%=name%>" value="italics" title="italics"/>
						
						<%}%>							 							
						<span class="italicscheck" title="italics">i</span>  
						 <%
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCapsBelow +PDFGUIConstants. UNDER_LINE;
							}
						 %>
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.UNDER_LINE)){%>					
							<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked" title="underline"/>
						<%}
						else{%>
							<input type="checkbox" name="<%=name%>" value="underline" title="underline"/>								
						<%}%>
						<span class="underlinecheck" title="underline">u</span>
						<span class="configitem">Align:</span> 
						<%
						 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.CAPTION_TEXT + 
								indexCapsBelow + PDFGUIConstants.ALIGN;
							}
						 %>
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<%if(fieldValue.equalsIgnoreCase(PDFGUIConstants.ALIGN_CENTER)){%>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_LEFT%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_LEFT%></span>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_CENTER%>" checked="checked" />								
							<span class="align"><%=PDFGUIConstants.ALIGN_CENTER%></span> 
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_RIGHT%>"/>	
							<span class="align"><%=PDFGUIConstants.ALIGN_RIGHT%></span>
						<%}
						else if(fieldValue.equalsIgnoreCase(PDFGUIConstants.ALIGN_RIGHT)){%>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_LEFT%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_LEFT%></span>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_CENTER%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_CENTER%></span> 
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_RIGHT%>" checked="checked" />	
							<span class="align"><%=PDFGUIConstants.ALIGN_RIGHT%></span>
						<%} else {%>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_LEFT%>" checked="checked" />
							<span class="align"><%=PDFGUIConstants.ALIGN_LEFT%></span>
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_CENTER%>"/>
							<span class="align"><%=PDFGUIConstants.ALIGN_CENTER%></span> 
							<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_RIGHT%>"/>	
							<span class="align"><%=PDFGUIConstants.ALIGN_RIGHT%></span>
						<% }%>	
						</p>													
					</td>
				</tr>
				<%
					indexCapsBelow++;
				}//end is loop
				%>
				<!--<span id="spanbelowbuttonid">-->
				<tr id="spanbelowbuttonid">
					<td class="captionbutton" id="captionbelowbuttonid">
						<input type="hidden" id="captionbelowid" name="<%=PDFGUIConstants.ABOVE_BELOW%>" value="<%=PDFGUIConstants.BELOW%>"/>	
						<input type="hidden" id="numberofcaptionsbelowid" name="<%=PDFGUIConstants.INDEXCAP%>" value="<%=indexCapsBelow%>"/>
						<input class="addrow" type="button" name="Add new row" value="Add row" 
						onClick="javascript:AddCaptionRow('CreateMoreFieldRows.jsp','spanbelowbuttonid','captionbelowid','numberofcaptionsbelowid')"/>
					<!-- When user clicks Add button, another row like the one above appears, allowing him/her to add another field as a line in the caption -->
					</td>
				</tr>	
				<!--</span>	-->			
			</table>
		</td>
	</tr>	
	<%	
	name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);//guaranteed to be generated at least once
	groupLabel= tp.getCurrentGroupLabel(name);
	groupLabelValue = (String)groupTable.get(groupLabel);
	if(groupLabelValue == null){
		groupLabelValue =PDFGUIConstants.TITLES;
	}
	%>
	<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>						
	<%
	for(int i = 0; i < PDFGUIConstants.NUMBER_OF_TITLES; i++ ) {
		fieldName = PDFGUIConstants.TITLES_STR[i];
		String tcaption =PDFGUIConstants.TITLES_LABEL_STR[i]; 
		if( i > 0){
			name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
		}
		fieldValue = (String)groupTable.get(name);
		if(fieldValue == null){
			fieldValue ="";
		}
		characterLabel= tp.getCurrentCharacterLabel(name);
		characterLabelValue = (String)groupTable.get(characterLabel);
		if(characterLabelValue == null){
			characterLabelValue =PDFGUIConstants.TITLES_LABEL[i];
		}
		int dropDownDize = PDFGUIConstants.TITLES_DROP_DOWN_SIZE[i];
	%>		
	<tr>
		<td class="formitem" colspan="2">
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			<span class="formitemtitle"><%=tcaption%>: </span>
			<%if(fieldValue.equalsIgnoreCase(fieldName)){ %>
			<input type="text" name="<%=name%>" size="<%=dropDownDize%>" value="<%=fieldValue%>" /> 
			<%} else { %>
			<input type="text" name="<%=name%>" size="<%=dropDownDize%>" value="<%=fieldName%>" class="formInputText" onfocus="clearField(this)" title="<%=fieldName%>"/> 
			<%}	
			name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
			characterLabel= tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String)groupTable.get(characterLabel);
			if(characterLabelValue == null){
				characterLabelValue =PDFGUIConstants.FONTS_LABEL[i];
			}						
			%>	
			<span class="configitem">Font:</span>							
			<select name="<%=name%>">
			<% 
			for(int j = 0; j < PDFGUIConstants.FONTS_NAME.length; j++){
				fieldName = PDFGUIConstants.FONTS_NAME[j];
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
			name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue =PDFGUIConstants.TITLE_FONT_SIZE[i];
			}
			characterLabel= tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String)groupTable.get(characterLabel);
			if(characterLabelValue == null){
				characterLabelValue =PDFGUIConstants.TITLE_SIZE[i];
			}						
			%>								
			<input type="text" name="<%=name%>" size="2" maxlength="3" value="<%=fieldValue%>"  onKeyDown="resetBackgroundColor(this,'');" onMouseDown="resetBackgroundColor(this,'');" onBlur="checkNumeric(this,6,100,'');"/>
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 						
			
			<%
			name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
			characterLabel= tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String)groupTable.get(characterLabel);
			if(characterLabelValue == null){
				characterLabelValue =PDFGUIConstants.TITLE_FORMAT[i];
			}
			fieldName =PDFGUIConstants.BOLD;
			if(fieldName.equals(fieldValue)){%>
			<input type="checkbox" name="<%=name%>" value="<%=fieldName%>" checked="checked" title="bold"/>
			<%} else{%>
			<input type="checkbox" name="<%=name%>" value="<%=fieldName%>" title="bold"/>
			<%}%>
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
			<span class="boldcheck" title="bold">B</span> 
			<%
			name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
			characterLabel= tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String)groupTable.get(characterLabel);
			if(characterLabelValue == null){
				characterLabelValue =PDFGUIConstants.TITLE_FORMAT[i];
			}
			fieldName =PDFGUIConstants.ITALICS;
			if(fieldName.equals(fieldValue)){%>
			<input type="checkbox" name="<%=name%>" value="<%=fieldName%>" checked="checked" title="italics"/>
			<%} else{%>
			<input type="checkbox" name="<%=name%>" value="<%=fieldName%>" title="italics"/>			
			<%}%>
			<span class="italicscheck" title="italics">i</span> 
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												

			<%
			name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
			characterLabel= tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String)groupTable.get(characterLabel);
			if(characterLabelValue == null){
				characterLabelValue =PDFGUIConstants.TITLE_FORMAT[i];
			}
			fieldName =PDFGUIConstants.UNDER_LINE;
			if(fieldName.equals(fieldValue)){%>
				<input type="checkbox" name="<%=name%>" value="<%=fieldName%>" checked="checked" title="underline"/>
			<%} else{%>
				<input type="checkbox" name="<%=name%>" value="<%=fieldName%>" title="underline"/>						
			<%}%>
			<span class="underlinecheck" title="underline">u</span>
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
		<td class="subheading">Logos and Color Settings</td>
	</tr>
	<tr>
		<td class="formitem" colspan="2">Add pre-sized images to use as logos in the credits section 
			<span class="note">(must be .jpg, .gif or .png format, less than 1 inch print size recommended):</span>
			<br>
			<%
				name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue =PDFGUIConstants.IMAGES;
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue =PDFGUIConstants.IMAGE;
				}
			%>
			<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
			<select name="<%=name%>" size="5">
			<% 
			for (int i=0; i < imageFileList.length; i++ ) { 
				if(i==0){%>
					<option></option>
				<%}
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

		</td>
	</tr>	
		<!-- submitting the following as an enhancement request -->
		<!-- <tr> -->
		<!-- <td class="formitem" colspan="2">Upload pre-sized images to use as logos in the credits section <span class="note">(must be .jpg, .gif or .png format, less than 1 inch print size recommended):</span><br /> -->
		<!-- It would be good if the user was not allowed to upload something that is not in one of these three file formats -->
		<!-- <input type="file" name="logo1" size="25" /><br /> -->
		<!-- <input type="file" name="logo2" size="25" /><br /> -->
		<!-- <input type="file" name="logo3" size="25" /></td> -->
		<!-- </tr> -->	

	<tr>
		<td class="formitem">Frame around image:<br />
			<br />
			 <span class="configitem">Weight:</span>
				<%
				name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue =PDFGUIConstants.IMAGE_FRAME_WEIGHTS;
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue =PDFGUIConstants.IMAGE_FRAME_WEIGHT;
				}
			%>
			<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
			<select name="<%=name%>">						
			<% 
			for(int i =0; i < PDFGUIConstants.NUMBER_OPTIONS.length;i++) {
				fieldName = PDFGUIConstants.NUMBER_OPTIONS[i];
				String fieldN = fieldName;
				if(i==0){
						fieldN = "0 (none)"; 
				}
				if(fieldName.equals(fieldValue)){
				%>
					<option selected="selected" value="<%=fieldName%>">                                                            <%=fieldN%>
					</option>
				<%
				}
				else{
					%>
				<option value="<%=fieldName%>">                                                            <%=fieldN%>
				</option>
				<%
				}
			}%>
			</select>
			<input type="hidden"  name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			<br/>
 			<span class="configitem">Select a color:</span> 
			<%
			name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="#7C3B35";
			}
			characterLabel= tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String)groupTable.get(characterLabel);
			if(characterLabelValue == null){
				characterLabelValue =PDFGUIConstants.IMAGE_FRAME_COLOR;
			}
			%>	
 			<input id="imageframecolor1id" name="<%=name%>" class="color"  value="<%=fieldValue%>" type="text"/>	
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
		</td>
		<td class="formitem">Bounding Box 
			<span class="note">(frame image plus captions):</span><br />
			<br />
			<span class="configitem">Weight:</span> 
			<%
				name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue =PDFGUIConstants.BOUNDING_BOX_WEIGHTS;
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue =PDFGUIConstants.BOUNDING_BOX_WEIGHT;
				}
			%>
			<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
			<select name="<%=name%>">						
			<% 
			for(int i =0; i < PDFGUIConstants.NUMBER_OPTIONS.length;i++) {
				fieldName = PDFGUIConstants.NUMBER_OPTIONS[i];
				String fieldN = fieldName;
				if(i==0){
						fieldN = "0 (none)"; 
				}
				if(fieldName.equals(fieldValue)){
				%>
					<option selected="selected" value="<%=fieldName%>">                                                            <%=fieldN%>
					</option>
				<%
				}
				else{
					%>
				<option value="<%=fieldName%>">                                                            <%=fieldN%>
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
					name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="#BA575A";
					}
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue =PDFGUIConstants.BOUNDING_BOX_COLOR;
					}%>						
		 			<input id="boundingboxcolor1id" name="<%=name%>" class="color" value="<%=fieldValue%>" type="text">
					<input type="hidden" name="<%=characterLabel%>" value="<%=characterLabelValue%>"/> 												
	 			</span>
	 		</span>
		</td>
	</tr>
	<input type="hidden"   name="<%=EFGImportConstants.SEARCH_PAGE_STR%>"  value="<%=EFGImportConstants.SEARCH_PAGE_STR%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.SEARCH_TYPE_STR%>"  value="<%=EFGImportConstants.SEARCH_PDFS_TYPE%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.DISPLAY_NAME%>"  value="<%=displayName%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_NAME%>"  value="<%=datasourceName%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.HTML_TEMPLATE_NAME%>"  value="<%=PDFGUIConstants.templateMatch%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.XSL_STRING%>"  value="<%=PDFGUIConstants.xslFileName%>"/>
	<%if( guid != null){%>
		<input type="hidden"   name="<%=EFGImportConstants.GUID%>"  value="<%=guid%>"/>
	<%}%>
	<input type="hidden"   name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>"  value="<%=uniqueName%>"/>
	<input type="hidden"   name="<%=EFGImportConstants.JSP_NAME%>"  value="platedesignform.jsp"/>	
	<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>	
	</table>
	<input type="submit"  name="submit" id="platedesignformsubmitID" value="Click to submit" align="middle"/>

</form>

