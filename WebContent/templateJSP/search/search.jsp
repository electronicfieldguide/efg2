<%@page import="java.io.File,
java.util.Iterator,
java.util.Hashtable,
project.efg.server.utils.TemplateProducer,
project.efg.server.utils.TemplatePopulator,
project.efg.util.interfaces.EFGDataObject,
project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.EFGDataSourceHelperInterface
"%>
<%@page import="project.efg.server.interfaces.EFGDataObjectListInterface"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="project.efg.util.utils.EFGUtils"%>
<%@page import="project.efg.server.factory.EFGSpringFactory"%>
<html>
	<head>
	
	<%@ include file="../commonTemplateCode/jspName.jsp"%>
	<%@ include file="../commonTemplateCode/commonConfigureJSPCode.jsp" %>
	<%@ include file="SearchCSSPage.jsp"%>
	<%

		String cssFile =request.getParameter("cssFile");
		String templateMatch = "";
		if(cssFile != null){
			templateMatch =(String)templateDisplayNames.get(cssFile);
		}
		String xslFileName = "/search/SearchPage.xsl";
		
		
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
	   EFGDataObjectListInterface doSearches = 
		   dsHelper.getSearchable(displayName,
			   datasourceName);	
	   if(datasourceName == null){
		datasourceName=doSearches.getDatasourceName();		
	   }
		Iterator it =null;
	
		
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
				guid, EFGImportConstants.SEARCHPAGE_XSL, 
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
		<title>Search Page Configuration of <%=displayName%></title>
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
	<body class="search">
		<form method="post" id="efg_form" action="<%=context%>/configTaxonPage" onsubmit="return removeConstants();">
		<input
		type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>" /> <% //set css file location in another file
 			if (cssFileList.length > 0) {
 			%> 
 			<select name="<%=name%>" title="Select a CSS File">
			<%
			for (ii = 0; ii < cssFileList.length; ii++) {
				File currentCSSFile = cssFileList[ii];
				fieldName = currentCSSFile.getName();
				if (fieldName.equals(fieldValue)) {
				%>
				<option selected="selected"><%=fieldName%></option>
			<%
				} else {
				%>
				<option><%=fieldName%></option>
		<%
			}
			}
			%>
	</select>
 	<%}
	
	 	%> 

	<table class="searchmodule">
		<% 
		for(int i = 0; i < groupCounter; i++){ 
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
			groupLabelValue = "fieldheader";
		}
		%>
		<tr>
			<th class="search">
			<%if(groupValue.equals(DEFAULT_GROUP_TEXT)){%>
				<input size="50" type="text"
				title="<%=DEFAULT_GROUP_TEXT%>" name="<%=groupText%>"
				value="<%=groupValue%>" class="cleardefault"/> 
				
			<%} else{ %>
				<input size="50" type="text"
				title="<%=DEFAULT_GROUP_TEXT%>" name="<%=groupText%>"
				value="<%=groupValue%>"/> 
			<%}%>
			</th>
		</tr>
		<tr>
			<td class="paddertopleft"> <input type="hidden" name="<%=groupLabel%>" value="<%=groupLabelValue%>" />
			</td>
			<td class="paddertopright"></td>
		</tr>
		<%
			boolean moreData = true;
			boolean isPrinted = false; //we have gone through the loop more than once
			while(moreData){
				if(!moreData){
					break;
				}
		 		name = tp.getCharacter(isOld, isOld);
		 		fieldValue = (String) groupTable.get(name);
	 			if (fieldValue == null) {
	 				fieldValue = "";
	 				if(isPrinted){
		 				break;
		 			}
	 				moreData = false;
	 			}//end if fieldValue == null		
	 			
		 		characterText = tp.getCurrentCharacterText(name);
		 		characterValue = (String) groupTable.get(characterText);
		 		if (characterValue == null) {
		 			characterValue = DEFAULT_FIELD_TEXT;
		 		}
		 		
	 		
	 			characterLabel = tp.getCurrentCharacterLabel(name);
	 			characterLabelValue = (String) groupTable
	 				.get(characterLabel);
	 			if (characterLabelValue  == null) {
	 				characterLabelValue  = "field";
	 				moreData = false;
	 			}
		 %>
		 <tr>
			<td class="sectionleft">
			<%if(characterValue.equals(DEFAULT_FIELD_TEXT)){ %>
				<input size="45" type="text" title="<%=DEFAULT_FIELD_TEXT%>" name="<%=characterText%>"
				value="<%=characterValue%>" class="cleardefault"/>
			<%} else{ %>	
				<input size="45" type="text" title="<%=DEFAULT_FIELD_TEXT%>" name="<%=characterText%>"
				value="<%=characterValue%>" />
			<%}%>		
				:</td>
			<td class="sectionright">
				<select name="<%=name%>" class="efg_select_marker">
				<%
				for(int s = 0 ; s < doSearches.getEFGDataObjectCount(); s++){
		 		
					EFGDataObject searchable = doSearches.getEFGDataObject(s);
				   		
			 		fieldName =searchable.getName();
			 		if(s == 0){
				 	%>
				 		<option></option>
				 	<% }
			 		if(fieldName.equals(fieldValue)){%>
			 			<option selected="selected"><%=fieldValue%></option>
			 		<% } else{%>
			 			<option><%=fieldName%></option>			 				
			 		<%}
				}
			%>
			</select>	
			<input type="hidden" name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>  			
		</td>
	</tr>	
	<%
		isPrinted = true;
			}
		
	
	String newName = name.replace(':','_');
	String nameValueID = newName + "_newValue";
	StringBuffer onclickString = new StringBuffer();
	onclickString.append("javascript:AddRows('CreateFieldGroup.jsp','");
	onclickString.append(nameValueID);
	onclickString.append("','");
	onclickString.append(newName);
	onclickString.append("')");
	%>
	<tr id="<%=newName%>">
		<td class="padderbottomleft"></td>
		<td class="padderbottomright">
			<input type="hidden" id="<%=nameValueID%>" name="nameValue" value="<%=name%>"/>
			<input type="button" name="efg_btn" value="Add another category" onclick="<%=onclickString.toString()%>"/>
		</td>
	</tr>

	<tr>
		<td class="spacer"></td>
	</tr>
	<%}
	
		session.setAttribute("templateProducer", tp);
	 	session.setAttribute("groupTable", groupTable);
	 	session.setAttribute("dosearches",doSearches);
	%>
	<tr id= "insertGroupID">
		<td class="spacer">	
		<input type="hidden" id="nameValueGroupID" name="nameValue" value="<%=name%>"/>
		
		<input name="efg_btn"  type="button" 
		value="Create a new group" onclick="javascript:AddRows('CreateSearchGroup.jsp','nameValueGroupID','insertGroupID')"/>
	   </td>
	</tr>
	</table>
	 <table class="search">
		<tr>
			<td class="submitsearch">
				<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=dsName%>"/>
	    		<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
	    		<input type="hidden" name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>" value="<%=templateName%>"/>
	    		<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
			
				<span class="search">
					<%@ include file="footerSearch.jsp" %>
				</span> 
			</td>
		</tr>
	</table>
	
</form>

</body>