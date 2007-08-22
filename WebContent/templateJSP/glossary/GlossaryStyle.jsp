<%@page import="java.io.File,
java.util.Iterator,
java.util.Hashtable,
project.efg.server.utils.TemplateProducer,
project.efg.server.utils.TemplatePopulator,
project.efg.util.interfaces.EFGDataObject,
project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.EFGDataSourceHelperInterface,
project.efg.util.interfaces.EFGQueueObjectInterface,
java.util.List
"%>
<%@page import="project.efg.server.interfaces.EFGDataObjectListInterface"%>
<%@page import="project.efg.server.factory.EFGSpringFactory"%>


<html>
	<head>
	<%@ include file="../commonTemplateCode/jspName.jsp"%>
	<%@ include file="GlossaryCSSPage.jsp"%>
		<%
		String cssFile =request.getParameter("cssFile");
		String templateMatch = "";
		if(cssFile != null){
			templateMatch =(String)templateDisplayNames.get(cssFile);
		}
		String xslFileName = "/glossary/glossarySkins.xsl";
		%>		
		<title><%=templateMatch%></title>
		
		<%@ include file="../commonTemplateCode/commonNameHeader.jsp" %>
	
	

	<script language="JavaScript" src="<%=context%>/js/prototype1.5.js"
		type="text/javascript"></script>

	
	<script language="JavaScript" src="<%=context%>/templateJSP/javascripts/templates_ajax.js"
		type="text/javascript"></script>
	<link rel="stylesheet" href="<%=cssLocation%>" />

	</head>
	<body class="main">
		<form method="post" action="<%=context%>/configTaxonPage">
	 	 	<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
			<%if(cssFileList.length > 0){
				%>						
				<select name="<%=name%>"  title="Select an image from List">
					<%
					for (ii=0; ii<cssFileList.length; ii++ ) {
						File currentCSSFile = cssFileList[ii];
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
				groupLabelValue ="titles";
			}
			characterLabel= tp.getCurrentCharacterLabel(name);
			characterLabelValue = (String)groupTable.get(characterLabel);
				if(characterLabelValue == null){
					characterLabelValue ="title";
				}
			%>
					<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
			<table class="about">
				<tr>
					<td class="abouttitle"><a name="top"></a><input type="text"  title="ENTER PAGE TITLE HERE" name="<%=name%>" value="<%=fieldValue%>" size="100"/></td>
				</tr>
			</table>
			<table class="about">
				<tr>
					<td class="alphastrip">
					<a class="glossary" href="javascript: void(0)">A</a> | 
					<a class="glossary" href="javascript: void(0)">B</a> | 
					<a class="glossary" href="javascript: void(0)">C</a> | 
					<a class="glossary" href="javascript: void(0)">D</a> | 
					<a class="glossary"
					href="javascript: void(0)">E</a> 
					</td>
				</tr>
			</table>
			<table class="about">
				<tr>
					<td>
						<dl class="alphaheader">
							<dt class="alphaheader"><a name="a"></a>- A -</dt>
						</dl>
						<dl class="glossary">
						<%
						name =tp.getCharacter(isNew,isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							groupLabelValue ="terms";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue ="term";
						}%>
						
							<dt class="glossary">
								<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>	
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
							
							 	<select name="<%=name%>"  title="Select a field from the list">
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
									}
								%>
								</select> 
							</dt>
							<dd class="glossary"><div class="glossaryicon">
							<!-- The user selects a character from the glossary datasource that has 
							image filenames, image appears here. 
							User will have to make thumbnails using the software, 
							and for this template we recommend 75px maximum width or length -->
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
							<% if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){%>
								<select name="<%=name%>"  title="Select an  an Image Field From the List">
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
									}
								%>
								</select> 
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>								  
							<%}%>
						</div> 
						<%
						name =tp.getCharacter(isNew,isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						groupLabel= tp.getCurrentGroupLabel(name);
						groupLabelValue = (String)groupTable.get(groupLabel);
						if(groupLabelValue == null){
							groupLabelValue ="definitions";
						}
						characterLabel= tp.getCurrentCharacterLabel(name);
						characterLabelValue = (String)groupTable.get(characterLabel);
						if(characterLabelValue == null){
							characterLabelValue ="definition";
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
								fieldName =queueObject.getObject(1);
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
						</dl>
						<dl class="topofpage">
							<dd><a class="topofpage" href="#top">Back</a></dd>
						</dl>
						<div style="clear:both;"></div>
					</td>
				</tr>
			</table>
			<%@ include file="../lists/footerLists.jsp" %>
		</form>			
	</body>
</html>
