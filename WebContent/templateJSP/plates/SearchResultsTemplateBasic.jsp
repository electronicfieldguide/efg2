<%@page import="
java.io.File,
java.util.List,
java.util.Iterator,
java.util.Hashtable,
project.efg.server.utils.EFGDataSourceHelperInterface,
project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.TemplateProducer,
project.efg.server.utils.TemplatePopulator,
project.efg.util.interfaces.EFGQueueObjectInterface
" %>
<html>
	<head>
		<%@ include file="SearchResultsTemplateBasicHeader.jsp" %>
	</head>
	<body>
		<div id="numresults">Your search found <span class="num">xxx</span> results: </div>
			<form method="post" action="<%=context%>/configTaxonPage">
				 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
				 <%
				if(cssFileList.length > 0){
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
					groupLabelValue ="images";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="image";
					}
			%>
				 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
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
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="captions";
				}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="caption";
					}%>
									 <input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
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
					<%
						name =tp.getCharacter(isOld,isOld);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
				characterLabel= tp.getCurrentCharacterLabel(name);
				characterLabelValue = (String)groupTable.get(characterLabel);
					if(characterLabelValue == null){
						characterLabelValue ="caption";
					}%>
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
						</td>
					</tr>
					<tr>
						<td class="rowspacer"></td>
						<td class="rowspacer"></td>
						<td class="rowspacer"></td>
					</tr>
				</table>
		<%@ include file="footerPlates.jsp" %>
		</form>
	</body>
</html>