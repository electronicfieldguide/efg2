<%@page import="java.util.Iterator,
project.efg.server.interfaces.EFGDataObjectListInterface,
project.efg.server.utils.EFGDataSourceHelperInterface,
project.efg.util.interfaces.EFGDataObject,
project.efg.util.interfaces.EFGImportConstants,
java.util.List,
java.util.ArrayList,
java.util.Hashtable,
project.efg.server.utils.TemplateProducer,
project.efg.server.utils.TemplatePopulator,
project.efg.util.pdf.interfaces.PDFGUIConstants,
java.util.Properties,
project.efg.util.interfaces.EFGQueueObjectInterface,
java.io.File,
project.efg.server.factory.EFGSpringFactory
" %>
<%@page import="project.efg.server.utils.ServletCacheManager"%>
<%@page import="project.efg.server.servlets.EFGContextListener"%>
<%@page import="java.util.Map"%>
<%
 	String guid = request.getParameter(EFGImportConstants.GUID);  
 	
	String uniqueName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
	String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME);
	String datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);

	String whichDatabase = request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
	
	if(whichDatabase == null || whichDatabase.trim().equals("")){
		whichDatabase =EFGImportConstants.EFG_RDB_TABLES;
	}
	
	if(displayName == null || "".equals(displayName)){
	   	Map map = ServletCacheManager.getDatasourceCache(whichDatabase);
		displayName = (String)map.get(datasourceName.toLowerCase());
	}

 	String context = request.getContextPath();
 
	String realPath = getServletContext().getRealPath("/");
	EFGDataSourceHelperInterface dsHelper =
		EFGSpringFactory.getDatasourceHelper();
	Iterator it =null;
	List table = dsHelper.getAllFields(displayName,datasourceName);
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
	String searchquery = request.getParameter(PDFGUIConstants.searchqueryStr);

	StringBuffer fileName = new StringBuffer();
	fileName.append(datasourceName.toLowerCase());
	Hashtable groupTable = tpop.populateTable(fileName.toString(), guid,
			EFGImportConstants.SEARCHPAGE_PDFBOOK_XSL, 
			datasourceName,whichDatabase );
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
	String numberOfResults = request.getParameter(PDFGUIConstants.numberOfResultsStr);
	name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
	fieldValue = (String)groupTable.get(name);
	if(fieldValue == null){
		fieldValue = numberOfResults;
	}
	else{
		if(numberOfResults != null){
			fieldValue = numberOfResults;
		}
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

	//session.setAttribute("mediaResourceFields",mediaResourceFields);
	//session.setAttribute("templateProducer",tp);
	//session.setAttribute("listFields",table);
	
	StringBuffer configpagetype1 = new StringBuffer();
	configpagetype1.append(EFGImportConstants.SEARCHTYPE);
	configpagetype1.append( "=");
	configpagetype1.append(EFGImportConstants.SEARCH_PDFS_BOOK_TYPE);

%>
<form name="pdfmakerform" method="post" action="<%=context%>/efg2pdfconfig" target="_blank" onsubmit="return insertQueryResultsXML();">	

			<!-- start of code for bookmaking msg added 08/17/2007 by Jenn -->
			<div id="downloadMessageID">
			
			</div>
			<div class="clear"></div>
			<!-- end of code added 08/17/2007 by Jenn -->
		<table class="form" id="pdfFillFormID">
			<tr>
				<td class="datasourcename" colspan="2"><%=selectedDS%> 
					<input type="hidden" name="searchqueryresultsxml" 
					id="searchqueryresultsxml" value=""/>	
					<input type="hidden" name="<%=EFGImportConstants.SEARCH_PDFS_BOOK_TYPE%>" value="<%=EFGImportConstants.SEARCH_PDFS_BOOK_TYPE%>"/> 
					<input type="hidden" id="searchQueryPDF" name="searchQuery" value=""/>
					<input type="hidden"  name="querydata" value="<%=configpagetype1.toString()%>"/>

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
				</td>
			</tr>
			<tr>
				<td class="instructions" colspan="2">Design your book using the form below. Any text windows that you leave blank will not appear in the book.</td>
			</tr>
			<tr colspan="2">
				<td class="subheading">Display Settings</td>
			</tr>
			<tr>
				<td class="formitem" colspan="2">Title and Headings:</td>
			</tr>
			<tr>
				<td class="formitem_mini" colspan="2">Enter the book title and indicate which fields you wish to appear in the "header" section of each taxon account.</td>
			</tr>
			<tr>
				<td class="formitem">
				<span class="configitem">Title:</span> 
					<% 		
						String title ="";
						name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue = title;
						}
						else{
							if(title != null){
								fieldValue = title;
							}
						}
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							groupLabelValue =PDFGUIConstants.FRONT_PAGE_TITLES;
						}
					%>				
					<input type="text" name="<%=name%>" size="54" maxlength="150" value="<%=fieldValue%>"/>
					<input type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
				</td>
			</tr>
			<tr>
				<td class="formitem">
					<span class="configitem">SubTitle:</span> 
					<% 		
						title ="";
						name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue = title;
						}
						else{
							if(title != null){
								fieldValue = title;
							}
						}
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							groupLabelValue =PDFGUIConstants.FRONT_PAGE_SUBTITLES;
						}
					%>				
					<input type="text" name="<%=name%>" size="50" maxlength="150" value="<%=fieldValue%>"/>
					<input type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
				</td>
			</tr>
			<tr>
				<td class="formitem">
				<% 		
					title ="";
					name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue = title;
					}
					else{
						if(title != null){
							fieldValue = title;
						}
					}
					groupLabel= tp.getCurrentGroupLabel(name);
					groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue =PDFGUIConstants.CREDITS1_TEXT;
					}
				%>				
					<span class="configitem">Credits 1</span> 
					<input type="text" name="<%=name%>" size="50" maxlength="150" value="<%=fieldValue%>"/>
					<input type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
				</td>
			</tr>
			<tr>
				<td class="formitem">
				<% 		
					title ="";
					name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue = title;
					}
					else{
						if(title != null){
							fieldValue = title;
						}
					}
					groupLabel= tp.getCurrentGroupLabel(name);
					groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue =PDFGUIConstants.CREDITS2_TEXT;
					}
				%>				
					<span class="configitem">Credits 2</span> 
					<input type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
					<input type="text" name="<%=name%>" size="50" maxlength="150" value="<%=fieldValue%>"/>
				</td>
			</tr>
			<!--  
			<tr>
			
				<td class="formitem">
					<span class="configitem">Document Title:</span> 
					<% 		
						title ="";
						name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue = title;
						}
						else{
							if(title != null){
								fieldValue = title;
							}
						}
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							groupLabelValue =PDFGUIConstants.TITLES;
						}
					%>				
					<input type="text" name="<%=name%>" size="50" maxlength="150" value="<%=fieldValue%>"/>
					<input type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>											
				</td>
			</tr>
			-->
			<tr>
				<td class="formitem">
					<span class="configitem">Item header (ex. Scientific Name):</span> 
					<%
						name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							groupLabelValue ="taxonomy";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue =PDFGUIConstants.MAIN_HEADER;
						}
					%>
					<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>
					<select name="<%=name%>" 
					onChange="JavaScript:checkSelected(this);" 
					class="mustselect"><!-- Show only data fields -->
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
			<%
						int ww = 0;
						int numberOfSubHeaders = 4;
						while(ww < numberOfSubHeaders){
							EFGQueueObjectInterface queueObject1 = (EFGQueueObjectInterface)table.get(ww);
							if(isImagesExists) {
								if(mediaResourceFields.contains(queueObject1)){
									continue;
								}
							}// end is Images	
							
							name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
							
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}//end if fieldValue == null	
							characterLabel= tp.getCurrentCharacterLabel(name);
							characterLabelValue = (String)groupTable.get(characterLabel);
							if(characterLabelValue == null){
								characterLabelValue =PDFGUIConstants.SUB_HEADER;
							}		
					%>								
			
			<tr>
				<td class="formitem">
					<span class="configitem">Item subheader (ex. Family):</span> 
							<select name="<%=name%>" onChange="JavaScript:checkSelected(this);" class="mustselect"><!-- Show only data fields -->
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
				</td>
			</tr>
			<%
				ww++;
			}//end while loop
			%>	
			<tr>
				<td class="formitem" colspan="2">Data:
					<%
						name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							groupLabelValue ="descriptions";
						}
					%>	
					<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>	
				</td>
			</tr>
			<tr>
				<td class="formitem_mini" colspan="2">Indicate the other data you want displayed for each taxonomic account. They will appear in the order you place them on this page.</td>
			</tr>
			<%
				ww = 0;
				for(int i =0; i < tableSize;i++){
					EFGQueueObjectInterface queueObject1 = 
						(EFGQueueObjectInterface)table.get(i);
					
					if(isImagesExists) {
						if(mediaResourceFields.contains(queueObject1)){
							continue;
						}
					}// end is Images	
					if(ww !=0){
						name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
					}
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}//end if fieldValue == null	
					characterLabel= tp.getCurrentCharacterLabel(name);
					characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="field";
					}		
			%>								
			<tr>
				<td class="formitem"><span class="configitem">Data to display:</span> 
					<select name="<%=name%>" onChange="JavaScript:checkSelected(this);" class="mustselect"><!-- Show only data fields -->
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
					<input type="hidden" name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  											
				</td>
			</tr>
			<%
			ww++;
			}//end for loop
			%>	
			<tr>
				<td class="formitem" colspan="2">Images:
				</td>
			</tr>
			<tr>
				<td class="formitem_mini" colspan="2">Select which images to display and how to arrange them on the page.</td>
			</tr>
			<%
			for(int zz = 0; zz < mediaResourceFields.size(); zz++){
				
				name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);//guaranteed to be generated at least once
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="mediaresources";
				}
				%>
						
				<%
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue ="mediaresource";
				}
			%>	
			<tr>
				<td class="formitem">
							
					<span class="configitem">Image to display:<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>	</span> 
						<select name="<%=name%>"  title="Select An Image Field From List" onChange="JavaScript:checkSelected(this);" class="mustselect">
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
					<span class="subconfigitem">
							
					<%
			 			name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
			 			fieldValue = (String)groupTable.get(name);
			 			if(fieldValue == null){
			 				fieldValue ="no";
			 			}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue ="displayallimages";
						}
 						if(fieldValue.equalsIgnoreCase("yes")){
						%>					
							<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" checked="checked" onclick="JavaScript:changeCheckedValue(this)"/>
						<%
						}
						else{
						%>
							<input type="checkbox" name="<%=name%>"  value="<%=fieldValue%>" onclick="JavaScript:changeCheckedValue(this)"/>							
						<%}%>
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  													
						
 					Show all images for this category</span><br/>
 					<span class="subconfigitemparen">(default displays first one only)</span>
				</td>
			</tr>
				<%}
					%>
			<tr>
				<td class="formitem">
					<span class="configitem">Arrangment (Number of Columns):</span> 
					<%
					name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);//guaranteed to be generated at least once
					groupLabel= tp.getCurrentGroupLabel(name);
					groupLabelValue = (String)groupTable.get(groupLabel);
					if(groupLabelValue == null){
						groupLabelValue ="numberofcolumns";
					}
					%>
					<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
					
					<%
		 				
		 				fieldValue = (String)groupTable.get(name);
		 				if(fieldValue == null){
		 					fieldValue ="4";
		 				}
			 		%>
			 		<input type="text" size="3" maxlength="3" name="<%=name%>" value="<%=fieldValue%>"/> 
				</td>
			</tr>
			<tr>
				<td class="formitem" colspan="2">Sorting:
					<%
						name =tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);//guaranteed to be generated at least once
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}

						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							groupLabelValue ="sort";
						}
						
					%>
					<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
				</td>
			</tr>
			<tr>
				<td class="formitem_mini" colspan="2">Indicate how you would like your taxonomic accounts to be ordered in the book.</td>
			</tr>
			<tr>
				<td class="formitem" colspan="2">
				<span class="configitem">Sort by:</span> 
					<select name="<%=name%>"><!-- Show only data fields -->
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
					<span class="configitem">Then by:
					<% name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld); %>
					</span> 
					<select name="<%=name%>"><!-- Show only data fields -->
						<%
							ii=0;
							it = table.iterator();
							while (it.hasNext()) {
								if(ii == 0){
								%>
								<option></option>
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
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="hidden"   name="<%=EFGImportConstants.SEARCH_PAGE_STR%>"  value="<%=EFGImportConstants.SEARCH_PAGE_STR%>"/>
					<input type="hidden"   name="<%=EFGImportConstants.SEARCH_TYPE_STR%>"  value="<%=EFGImportConstants.SEARCH_PDFS_BOOK_TYPE%>"/>
					<input type="hidden"   name="<%=EFGImportConstants.DISPLAY_NAME%>"  value="<%=displayName%>"/>
					<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_NAME%>"  value="<%=datasourceName%>"/>
					<input type="hidden"   name="<%=EFGImportConstants.HTML_TEMPLATE_NAME%>"  value="<%=PDFGUIConstants.templateMatch%>"/>
					<input type="hidden"   name="<%=EFGImportConstants.XSL_STRING%>"  value="<%=PDFGUIConstants.xslFileName%>"/>
					<%if( guid != null){%>
						<input type="hidden"   name="<%=EFGImportConstants.GUID%>"  value="<%=guid%>"/>
					<%}%>
					<input type="hidden"   name="<%=EFGImportConstants.JSP_NAME%>"  value="platedesignform.jsp"/>	
					<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>	
					<hr />
				</td>
			</tr>
		</table>
		<input type="submit"  name="submit" id="designformsubmitID" value="Preview" align="middle"/>
		<input type="button"  name="saveConfigs" id="designformconfigsubmitID" value="Get Book" onclick="javascript:saveConfig('pdfFillFormID','Enter a unique name to save your book configuration: ')"/>
	</form>

	
