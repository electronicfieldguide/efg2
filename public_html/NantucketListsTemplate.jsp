<%@page import="java.util.*,java.io.*,java.sql.*, project.efg.util.*, project.efg.efgInterface.*, project.efg.templates.taxonPageTemplates.*"%>
<html>
		<head>
		<%

	   project.efg.efgInterface.EFGDSHelperFactory dsHelperFactory = new project.efg.efgInterface.EFGDSHelperFactory();
 	   String dataSourceName = request.getParameter("dataSourceName");
    	   String context = request.getContextPath();
	   String xslFileName = "NantucketListsTemplate.xsl";

    	   String fieldName = null;
         EFGField field =null;
	   Iterator it =null;
	   RDBDataSourceHelper dsHelper = (RDBDataSourceHelper)dsHelperFactory.getDataSourceHelper(); 
	   Map table = dsHelper.getFieldsInfo(dataSourceName);
	   SortedSet characterSet = Collections.synchronizedSortedSet(new TreeSet());
	   it = table.entrySet().iterator();
	   while (it.hasNext()) {
		Map.Entry pairs = (Map.Entry)it.next();
		fieldName = ((String)pairs.getKey()).trim();
		field = (EFGField)pairs.getValue();
		if(!field.isMediaResource()){
			characterSet.add(fieldName.trim());
		}
	 }
	 int characterSetSize = characterSet.size();
	 int tableSize = table.size();
	 int groupRank = 1;
 	 String name = "group:" + groupRank + ":" + groupRank;
 	 String curName = "";
      String clName= "cl:" +  groupRank + ":" + groupRank + ":" +  1;
									
	 String tlName ="";

	 int numberColumns = 2;
	 int ii=0;
 	String realPath = getServletContext().getRealPath("/");
	StringBuffer fileLocationBuffer = new StringBuffer();
	fileLocationBuffer.append(realPath);
	fileLocationBuffer.append(EFGImportConstants.TEMPLATES_FOLDER_NAME);
	fileLocationBuffer.append(File.separator);
	fileLocationBuffer.append(dataSourceName);
	fileLocationBuffer.append(EFGImportConstants.SEARCHPAGE_LISTS_FILLER);
	String fileLocation = fileLocationBuffer.toString();
	Hashtable groupTable=new Hashtable();
	
	try{
		   File file = new File(fileLocation);
		    if(file.exists()){
		    	FileReader reader = new FileReader(fileLocation);
		    	project.efg.templates.taxonPageTemplates.TaxonPageTemplatesType  tptp=
				(TaxonPageTemplatesType)TaxonPageTemplatesType.unmarshalTaxonPageTemplatesType(reader);
		    	project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem  tpi = tptp.getTaxonPageTemplatesTypeItem(0);
		    	project.efg.templates.taxonPageTemplates.TaxonPageTemplateType tp = tpi.getTaxonPageTemplate();
			project.efg.templates.taxonPageTemplates.GroupsType groupsT = tp.getGroups();

			for(java.util.Enumeration e1 = groupsT.enumerateGroupsTypeItem(); e1.hasMoreElements();){
	    			project.efg.templates.taxonPageTemplates.GroupsTypeItem gti =
				(project.efg.templates.taxonPageTemplates.GroupsTypeItem)e1.nextElement(); 
				

				project.efg.templates.taxonPageTemplates.GroupType gt = gti.getGroup();
				String id = gt.getId();
				int groupRank1 = gt.getRank();
				String groupKey=null;
				String groupLabelKey = null;
				groupKey = "group:" + id + ":" + groupRank1 + ":";
			
				String keyT = null;
				ArrayList list = new ArrayList();
				for(java.util.Enumeration e = gt.enumerateGroupTypeItem(); e.hasMoreElements();){
	    				project.efg.templates.taxonPageTemplates.GroupTypeItem  key =
					(project.efg.templates.taxonPageTemplates.GroupTypeItem)e.nextElement(); 
	    				project.efg.templates.taxonPageTemplates.CharacterValue cv = key.getCharacterValue();	
					
	    			
					String val = cv.getValue();
					int characterRank = cv.getRank();
					String characterLabel = cv.getLabel();
					String characterText = cv.getText();
					if(val != null){
						keyT =  groupKey + characterRank;
						groupTable.put(keyT,val);
					}
					if((cv.getLabel() != null) && (!cv.getLabel().trim().equals(""))){
						keyT = "cl:" + id + ":" + groupRank1 + ":" + characterRank;
						groupTable.put(keyT,characterLabel);
					}
					if((cv.getText() != null) && (!cv.getText().trim().equals(""))){
						keyT = "tl:" + id + ":" + groupRank1 + ":" + characterRank;
						groupTable.put(keyT,characterText);
					}
				}
				
			}
		   }
		}
		catch(Exception ee){
		    
	}
	boolean bool = false;
	String fieldValue = null;
     %>
     <title>Template for <%=dataSourceName%>  search results pages as lists</title>
		<link href="nantucketstyle.css" rel="stylesheet"/>

		
	</head>
	<body text="#000000" bgcolor="#ddeeff" link="#6699FF" vlink="#660033" alink="#660033">
	<form method="post" action="<%=context%>/configTaxonPage">
	<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_STR%>"  value="<%=dataSourceName%>"/>
<input type="hidden"   name="<%=EFGImportConstants. XSL_STRING%>"  value="<%=xslFileName%>"/>
<input type="hidden" name="<%=EFGImportConstants.SEARCH_PAGE_STR%>" value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>"/>
<input type="hidden"   name="<%=EFGImportConstants.SEARCH_TYPE_STR%>"  value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>"/>

		<table>
			<tr>
				<td class="title">
				</td>
			</tr>
			<tr>
				<td class="descrip">
					<%
						
							fieldValue = (String)groupTable.get(clName);
							if(fieldValue == null){
									fieldValue ="";
							}
					%>

					<input type="text " size="100"  value="<%=fieldValue%>"  name="<%=clName%>"/><br/>
					Click on a name to view more information about it.
				</td>
			</tr>
			<tr>
				<td>
					<table class="specieslist">
						<tr>
								<%
									groupRank = groupRank + 1;
					 				clName= "cl:" +  groupRank + ":" + groupRank + ":" +  1;

									fieldValue = (String)groupTable.get(clName);
									if(fieldValue == null){
										fieldValue ="";
									}
								%>

							<td class="commonheader"><input type="text " size="50"  value="<%=fieldValue%>"  name="<%=clName%>"/></td>
							<%
									groupRank = groupRank + 1;
					 				clName= "cl:" +  groupRank + ":" + groupRank + ":" +  1;

									fieldValue = (String)groupTable.get(clName);
									if(fieldValue == null){
										fieldValue ="";
									}
								%>

							<td class="sciheader"><input type="text " size="50"  value="<%=fieldValue%>"  name="<%=clName%>"/></td>
						</tr>
						<tr>
							<td class="spacerheight"></td>
							<td class="spacerheight"></td>
						</tr>
						<tr>
							<td class="commonname">
							
								<%
								
									 groupRank = groupRank + 1;
									 curName = "group:" + groupRank + ":" + groupRank + ":1";
									fieldValue = (String)groupTable.get(curName);
									if(fieldValue == null){
										fieldValue ="";
									}
								%>

									<select name="<%=curName%>" size="1"> 
				  					<%
										ii=0;
										it = characterSet.iterator();
										fieldValue = (String)groupTable.get(curName);
												if(fieldValue == null){
													fieldValue ="";
												}

										while (it.hasNext()) {
											fieldName = ((String)it.next());
											if(ii==0){
											%>
							 				<option>
							 				<%
											}
							 				if(fieldName.equals(fieldValue)){%>
											<option selected="selected"><%=fieldName%></option>
											<%}else{%>
												<option><%=fieldName%></option>
											<%}
											ii++;
										}
									%>
									</select>    <br/>
							</td>
							<td class="sciname">
								<%
									//3:3
									groupRank = groupRank + 1;
									 curName = "group:" + groupRank + ":" + groupRank  + ":1";
									fieldValue = (String)groupTable.get(curName);
									if(fieldValue == null){
										fieldValue ="";
									}
								%>
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
							 				if(fieldName.equals(fieldValue)){%>
											<option selected="selected"><%=fieldName%></option>
											<%}else{%>
												<option><%=fieldName%></option>
											<%}
											ii++;
										}
									%>
									</select>  
							</td>
<td class="sciname">

								<%
									//3:3
									groupRank = groupRank + 1;
									 curName = "group:" + groupRank + ":" + groupRank  + ":1";
									fieldValue = (String)groupTable.get(curName);
									if(fieldValue == null){
										fieldValue ="";
									}
								%>
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
							 				if(fieldName.equals(fieldValue)){%>
											<option selected="selected"><%=fieldName%></option>
											<%}else{%>
												<option><%=fieldName%></option>
											<%}
											ii++;
										}
									%>
									</select>  
									<br/>
							</td>
						
						</tr>
					
					</table>
				</td>
			</tr>
		</table>
		<input type="submit"  name="submit" value="Click to submit" align="middle" />	

		</form>
	</body>
</html>
