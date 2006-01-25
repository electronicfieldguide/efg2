<%@page import="java.util.*,java.io.*,java.sql.*, project.efg.util.*, project.efg.efgInterface.*" %>
<html>
	<head>
	<%

	   project.efg.efgInterface.EFGDSHelperFactory dsHelperFactory = new project.efg.efgInterface.EFGDSHelperFactory();
 	   String dataSourceName = request.getParameter("dataSourceName");
    	   String context = request.getContextPath();
	   String xslFileName = request.getParameter("xslFileName");

    	   String fieldName = null;
         EFGField field =null;
	   Iterator it =null;
	   RDBDataSourceHelper dsHelper = (RDBDataSourceHelper)dsHelperFactory.getDataSourceHelper(); 
	   Map table = dsHelper.getFieldsInfo(dataSourceName);
	   ArrayList  mediaResourcesTable = new ArrayList();
	   SortedSet characterSet = Collections.synchronizedSortedSet(new TreeSet());
	   it = table.entrySet().iterator();
	   while (it.hasNext()) {
		Map.Entry pairs = (Map.Entry)it.next();
		fieldName = ((String)pairs.getKey()).trim();
		field = (EFGField)pairs.getValue();
		if(field.isMediaResource()){
			if(fieldName != null){
				mediaResourcesTable.add(fieldName);
			}
		}
		characterSet.add(fieldName.trim());;	
	 }
	 int numberOfImages = mediaResourcesTable.size();
	 int characterSetSize = characterSet.size();
	 int tableSize = table.size();
	 int groupRank = 1;
 	 String name = "group:" + groupRank + ":" + groupRank;
 	 String curName = "";
	 int ii=0;
     %>
	<title>Browse EFG Datasources</title>
	</head>
  	<body bgcolor="#ffffff">
	<center>
 		<h2 align="center">This template displays three images on each row sorted alpahbetically by an author supplied label</h2>
		<form method="post" action="<%=context%>/configTaxonPage">
			<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_STR%>"  value="<%=dataSourceName%>"/>
			<input type="hidden"   name="<%=EFGImportConstants.XSL_STRING%>"  value="<%=xslFileName%>"/>
			<input type="hidden"   name="<%=EFGImportConstants.SEARCHPAGE%>"  value="searchPage"/>

		 
			<h3 align="center">Your search found xxx results</h3>
			<table width="100%" border="0">
				<tr>
					<td align="center">
						<% curName =name + ":1"; %>
						Select Image Field
						<select name="<%=curName%>" size="1"> 
							<%
							for (ii = 0; ii < numberOfImages; ii++){
								fieldName = (String)mediaResourcesTable.get(ii);
								if(ii==0){%>
								<option>
								<%
								}
								%>
								<option><%=fieldName%></option>
							<%
							}
							%>
						</select>
						<br clear="all"/>
						<% curName =name + ":2"; %>

						<h4>Select Caption Fields</h4>
					
						<select name="<%=curName%>" size="1">
						<%
							ii=0;
							it = characterSet.iterator();
							while (it.hasNext()) {
								fieldName = ((String)it.next());
								if(ii==0){
								%>
							 	<option>
							 	<%
								}
								%>
							 	<option><%=fieldName%></option>
							<% ii++;
							}
							%>
						</select>  
						<select name="<%=curName%>" size="1">
						<%
							ii=0;
							it = characterSet.iterator();
							while (it.hasNext()) {
								fieldName = ((String)it.next());
								if(ii==0){
								%>
							 	<option>
							 	<%
								}
								%>
							 	<option><%=fieldName%></option>
							<% ii++;
							}
							%>
						</select>    
						<br clear="all"/>
  
						
					</td>
				</tr>
			</table>
		</form>
	</center>
	</body>
</html>
