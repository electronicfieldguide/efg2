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

			 String templateMatch ="Plate Template1";
			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			String xslFileName = "nantucketCommonNameSearchPlate.xsl";
			EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
   
			String fieldName = null;
			String fieldValue = null;
    
			Iterator it =null;
			List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
		    List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);
			int tableSize = table.size();
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
			Hashtable groupTable = tpop.populateTable(fileName.toString(), guid, EFGImportConstants.SEARCHPAGE_PLATES_XSL, datasourceName );
			if(groupTable == null){
				groupTable = new Hashtable();
			}
			boolean isTableExists = false;	
			boolean isImagesExists = false;	
			if((table != null) && (table.size() > 0)){
				isTableExists = true;	
			}

			if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){
				isImagesExists = true;	
			}
		    File cssFiles = new File(realPath + File.separator +  EFGImportConstants.templateCSSDirectory);
			File[] cssFileList = cssFiles.listFiles(); 
			String cssLocation = context + "/" + EFGImportConstants.templateCSSDirectory  + "/";
			 String cssFile ="nantucketstyleplates.css";
		    if(!isTableExists){    	
	 			String forwardPage="NoDatasource.jsp";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
				dispatcher.forward(request, response);
		    }
			 
		%>
		
	<title>Nantucket Search Plate Template</title>
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
		<div id="numresults">Your search found <span class="num">xxx</span> results: </div>
			<form method="post" action="<%=context%>/configTaxonPage">
			 <%
				if(cssFileList.length > 0){
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
			%>
				<table class="resultsdisplay">
					<tr>
						<td class="thumbnail">
							<% if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){%>
								<select name="<%=name%>"  title="Select an  an Image Field From the List">
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
									}
								%>
								</select>   
							<%}%>
						</td>
					</tr>
					<tr>
						<td class="caption">
								<%
									name =tp.getCharacter(isNew,isNew);
									fieldValue = (String)groupTable.get(name);
									if(fieldValue == null){
										fieldValue ="";
									}
								%>
								<select name="<%=name%>"  title="Select A Field To Be Used As Caption">
								<%
									ii=0;
									it = table.iterator();
									while (it.hasNext()) {
										EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
										 if(mediaResourceFields != null) {
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
								<select name="<%=name%>"  title="Select A Field That Should be Appended To The Selected Field(on the left), To Be Used As Caption">
								<%
									ii=0;
									it = table.iterator();
									while (it.hasNext()) {
										EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
										 if(mediaResourceFields != null) {
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
					</tr>
					<tr>
						<td class="rowspacer"></td>
						<td class="rowspacer"></td>
						<td class="rowspacer"></td>
					</tr>
				</table>
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