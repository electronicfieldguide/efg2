<%@page import="
java.io.File,
java.util.List,
java.util.Iterator,
java.util.Hashtable,

project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.TemplateProducer,
project.efg.server.utils.TemplatePopulator,
project.efg.util.interfaces.EFGQueueObjectInterface
" %>
<html>
	<head>
	<%@ include file="ListViewTemplateGlossaryHeader.jsp" %>
	</head>
	<body class="main">
		 <form method="post" action="<%=context%>/configTaxonPage">
	 		<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
			<%if(cssFileList.length > 0){
				%>						
						<select name="<%=name%>"  title="Select A CSS File From List">
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
			<%}%>	
		<table class="about">
		<%
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
			<tr>
				<td class="abouttitle">
					<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
					<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
					<a name="top"><input type="text"  title="ENTER PAGE TITLE HERE" name="<%=name%>" value="<%=fieldValue%>" size="54"/></a> 
				</td>
			</tr>
		</table>
		<table class="about">
			<tr>
				<td class="alphastrip">
					<a class="glossary" href="#a"></a> | <a class="glossary" href="#b"></a>
				</td>
			</tr>
		</table>
		<table class="about">
			<tr>
				<td>
					<dl class="alphaheader">
						<dt class="alphaheader">
							<a name="a"></a></dt>
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
						</dt>  
						<dd class="glossary">
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
						</dd>
					</dl>
					<dl class="topofpage">
						<dd>
							<a class="topofpage" href="#top">Back</a>
						</dd>
					</dl>
				</td>
			</tr>
		</table>
		<%@ include file="../lists/footerLists.jsp" %>
		</form>
	</body>
</html>
