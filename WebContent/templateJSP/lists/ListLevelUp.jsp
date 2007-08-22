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
	<%@ include file="../commonTemplateCode/commonConfigureJSPCode.jsp" %>
	<%@ include file="ListCSSPage.jsp"%>
	<%		
	String cssFile =request.getParameter("cssFile");
	String templateMatch = "";
	if(cssFile != null){
		templateMatch =(String)templateDisplayNames.get(cssFile);
	}
	String xslFileName = "/searchresults/lists/ListLeftLevelUpItal.xsl";
	String isLeftItal = request.getParameter("leftItal");
	String leftClass = "sciname";
	String rightClass="commonname";
	
	if(isLeftItal == null){
		xslFileName = "/searchresults/lists/ListRightLevelUpItal.xsl";
		leftClass="commonname";
		rightClass= "sciname";
	}
	String titleText = "Enter Title Here";
	String realPath = getServletContext().getRealPath("/");
    String groupLabel= null;
	String groupLabelValue = null;
	String characterLabelValue = null;
	String characterLabel = null;
	String groupText = null;
	String groupKey = null;
	String groupValue = null;
	String fieldName = null;
	String characterText = null;
	String characterValue = null;
	String fieldValue = null;
	String catNarrativeName=null;
	String listsName = null;
	 //context = request.getContextPath();
   String tableName = request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
   if(tableName == null || tableName.trim().equals("")){
	   tableName = EFGImportConstants.EFG_RDB_TABLES;
   }
   EFGDataSourceHelperInterface dsHelper = EFGSpringFactory.getDatasourceHelper();
   dsHelper.setMainDataTableName(tableName);
   
	Iterator it =null;
	List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
	List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);

   	boolean isImagesExists = false;
	boolean isTableExists = false;
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
	Hashtable groupTable = tpop.populateTable(
			fileName.toString(), 
			guid, EFGImportConstants.SEARCHPAGE_LISTS_XSL, 
			datasourceName,tableName);
	int groupCounter = tpop.getNumberOfGroups();
	
	if(groupTable == null){
			groupTable = new Hashtable();
			groupCounter = 1;
	}
	else if(groupCounter == 0 ){
		groupCounter = 1;
	}
	else{
		groupCounter = groupCounter -1;
	}

	String cssLocation = context + "/" + EFGImportConstants.templateCSSDirectory  + "/";
	//must be in parent file
	
    File cssFiles = new File(realPath + File.separator +  EFGImportConstants.templateCSSDirectory);
	File[] cssFileList = cssFiles.listFiles(); 
	%>
	<title>List Level Up Page Configuration of <%=displayName%></title>
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

	<script language="JavaScript" src="<%=context%>/js/prototype1.5.js"
		type="text/javascript"></script>

	
	<script language="JavaScript" src="<%=context%>/templateJSP/javascripts/templates_ajax.js"
		type="text/javascript"></script>
	<link rel="stylesheet" href="<%=cssLocation%>" />

	</head>
	<body class="about">
		<form method="post" action="<%=context%>/configTaxonPage" onsubmit="return removeConstants();">
	 	 	<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
			<%if(cssFileList.length > 0){
				%>						
				<select name="<%=name%>"  title="Select css from List">
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
			
			<table class="header">
				<tr>
					<td colspan="2" class="abouttitle">
					<input type="text"  title="ENTER PAGE TITLE HERE" name="<%=name%>" value="<%=fieldValue%>" size="100" class="cleardefault"/> 
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
						<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
						<input type="text"  title="ENTER HEADER INFO HERE"  size="100"   name="<%=name%>" value="<%=fieldValue%>"/> 
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table class="specieslist">
						<!-- Template builder page only needs one pull-down for choosing the higher level sorting character. 
						Because of the limit of 100 results per page, will need to either ensure higher-level category appears at the top of each page, or will need to cut each page early (or let it be greater than 100) so that categories are not broken up between pages -->
							<tr>
								<td class="famheader" colspan="2">
								<% 
								name = tp.getCharacter(isNew, isNew);
						
								groupText = tp.getCurrentGroupText(name);
						
								String[] groupArr = groupText.split(":");
						
								if (groupArr.length > 3) {
									groupKey = groupText.substring(0, groupText.lastIndexOf(":"));
								} else {
									groupKey = groupText;
								}
								groupValue = (String) groupTable.get(groupKey);
								if (groupValue == null) {
									groupValue = DEFAULT_GROUP_TEXT;
								}
								groupLabel = tp.getCurrentGroupLabel(name);
								groupLabelValue = (String) groupTable.get(groupLabel);
								if (groupLabelValue == null) {
									groupLabelValue = "groupheader";
								}
								if(groupValue.equals(DEFAULT_GROUP_TEXT)){%>
								<input size="50" type="text"
									title="<%=DEFAULT_GROUP_TEXT%>" name="<%=groupText%>"
									value="<%=groupValue%>" class="cleardefault"/> 
								<%} else{ %>
									<input size="50" type="text"
									title="<%=DEFAULT_GROUP_TEXT%>" name="<%=groupText%>"
									value="<%=groupValue%>"/> 
								<%}%>
								:
							   <select name="<%=name%>"  title="Select a field from the list" style="width:100px;">
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
								<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
								<input type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>" />
					 			</td>
							</tr>
							<tr>
								<td class="<%=leftClass%>">
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
										characterLabelValue ="leftColumn1";
									}%>
									<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>			
									<select name="<%=name%>"  title="Select a column from the list" style="width:100px;">
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
										characterLabelValue ="leftColumn2";
									}
									%>
									<select name="<%=name%>"  title="Select a column from the list" style="width:100px;">
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
									<input type="hidden" name="<%=characterLabel%>" 
									value="<%=characterLabelValue%>"/>
								</td>
								<td class="<%=rightClass%>">
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
									<select name="<%=name%>"  title="Select a column from the list" style="width:100px;">
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
									}
									%>
									<select name="<%=name%>"  title="Select a column from the list" style="width:100px;">
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
									<input type="hidden" name="<%=characterLabel%>" 
									value="<%=characterLabelValue%>"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<%@ include file="footerLists.jsp" %>
		</form>			
	</body>
</html>
