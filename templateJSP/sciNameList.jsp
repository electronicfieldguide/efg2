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
		<%
			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME); 
			String datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
			String xslFileName = "nantucketSciNameSearchList.xsl";
			EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
			StringBuffer fileName = new StringBuffer(realPath);
			 fileName.append(File.separator);
			 fileName.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME); 
			 fileName.append(File.separator);
			 fileName.append(datasourceName.toLowerCase());
			  fileName.append(EFGImportConstants.XML_EXT);
			String fieldName = null;
			String fieldValue = null;
    
			Iterator it =null;
			List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
			TemplatePopulator tpop = new  TemplatePopulator();

			Hashtable groupTable = tpop.populateTable(fileName.toString(), xslFileName, EFGImportConstants.SEARCHPAGE_LISTS_XSL, datasourceName );

			int tableSize = table.size();
			TemplateProducer tp = new TemplateProducer();
			boolean isNew = true;
			boolean isOld = false;
			String name = null;
			int ii = 0;
			String cssLocation = context + "/" + EFGImportConstants.templateCSSDirectory + "/nantucketstyle.css";
            File imageFiles = new File(realPath + File.separator +  EFGImportConstants.templateImagesDirectory);
			 File[] imageFileList = imageFiles.listFiles(); 
			String defaultInfo ="Click on a name to view more information about that Taxon."; 
		//sort and iterate
		%>
		<title>Nantucket Search List Template</title>
		<link rel="stylesheet" href="<%=cssLocation%>"/>
	</head>
	<body alink="#660033" vlink="#660033" link="#6699FF" bgcolor="#ddeeff" text="#000000">
	 <form method="post" action="<%=context%>/configTaxonPage">
		<%
		  
		   name =tp.getGroupText(isNew,isNew);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
		%>
		<table class="header">
		
			<tr>
				<td colspan="2" class="title">
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
				<%
				   name =tp.getGroupText(isNew,isNew);
				   fieldValue = (String)groupTable.get(name);
				   if(fieldValue == null){
					fieldValue ="";
				   }
				%>
					<input type="text"  title="ENTER PAGE TITLE HERE" name="<%=name%>" value="<%=fieldValue%>"/> 
				</td>
			</tr>
			<tr>
				<td colspan="2" class="descrip">
				<%
				   name =tp.getGroupText(isNew,isNew);
				   fieldValue = (String)groupTable.get(name);
				   if(fieldValue == null){
					fieldValue = defaultInfo;
				   }
					
				%>
					<input type="text"  title="ENTER HEADER INFO HERE"  size="100"   name="<%=name%>" value="<%=fieldValue%> "/> 
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<table class="specieslist">
						<tr>
							<td class="commonheader">
							<%
							     name =tp.getGroupText(isNew,isNew);
								fieldValue = (String)groupTable.get(name);
								if(fieldValue == null){
									fieldValue ="";
								}
								%>
								<input type="text"  title="ENTER HEADER OF  LEFT COLUMN HERE" name="<%=name%>" value="<%=fieldValue%>"/> 
							</td>
							<td class="sciheader">
							<%
							 name =tp.getGroupText(isNew,isNew);
								fieldValue = (String)groupTable.get(name);
								if(fieldValue == null){
									fieldValue ="";
								}
								%>
								<input type="text"  title="ENTER HEADER OF  RIGHT COLUMN HERE" name="<%=name%>" value="<%=fieldValue%>"/> 
							</td>
						</tr>
						<tr>
							<td class="spacerheight"></td>
							<td class="spacerheight"></td>
						</tr>
						<tr>
							<td class="commonname">
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
							<td class="sciname">
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
		<input type="hidden"   name="<%=EFGImportConstants.XSL_STRING%>"  value="<%=xslFileName%>"/>
		<input type="hidden"   name="<%=EFGImportConstants.SEARCH_PAGE_STR%>"  value="<%=EFGImportConstants.SEARCH_PAGE_STR%>"/>
		<input type="hidden"   name="<%=EFGImportConstants.SEARCH_TYPE_STR%>"  value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>"/>


	<input type="submit"  name="submit" value="Click to submit" align="middle" />	
		</form>
	</body>
</html>
