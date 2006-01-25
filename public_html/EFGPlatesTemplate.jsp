<%@page import="java.util.*,java.io.*,java.sql.*, project.efg.util.*, project.efg.efgInterface.*, project.efg.templates.taxonPageTemplates.*"%>
<html>
		<head>
		<%

	project.efg.efgInterface.EFGDSHelperFactory dsHelperFactory = new project.efg.efgInterface.EFGDSHelperFactory();
 	   String dataSourceName = request.getParameter("dataSourceName");
    	   String context = request.getContextPath();
 String xslFileName = "EFGPlatesTemplatesUnsorted.xsl";


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
		else{
			if(!field.isEFGList()){

			}
					characterSet.add(fieldName.trim());	
			}
		
	 }
	 int numberOfImages = mediaResourcesTable.size();
	 int characterSetSize = characterSet.size();
	 int tableSize = table.size();
	 int groupRank = 1;
 	 String name = "group:" + groupRank + ":" + groupRank;
 	 String curName = "";
       String clName= "cl:" +  groupRank + ":" + groupRank + ":" +  1;

	 String tlName ="";
	
	 int ii=0;
 	String realPath = getServletContext().getRealPath("/");
	StringBuffer fileLocationBuffer = new StringBuffer();
	fileLocationBuffer.append(realPath);

	
	fileLocationBuffer.append(EFGImportConstants.TEMPLATES_FOLDER_NAME);
	fileLocationBuffer.append(File.separator);
	fileLocationBuffer.append(dataSourceName);
	fileLocationBuffer.append("_search_page_plates.xml");
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
     <title>Template for <%=dataSourceName%>  search results pages as Plates</title>

		
	</head>

 		
	<body>
	<center>
			<h3>Your search found XX results.</h3>
	<form method="post" action="<%=context%>/configTaxonPage">
<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_STR%>"  value="<%=dataSourceName%>"/>
<input type="hidden"   name="<%=EFGImportConstants. XSL_STRING%>"  value="<%=xslFileName%>"/>
<input type="hidden" name="<%=EFGImportConstants.SEARCH_PAGE_STR%>" value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>"/>
<input type="hidden"   name="<%=EFGImportConstants.SEARCH_TYPE_STR%>"  value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>"/>
<%
					
							fieldValue = (String)groupTable.get(clName);
							if(fieldValue == null){
									fieldValue ="";
							}
					%>

			Title for search page:<input type="text " size="100"  value="<%=fieldValue%>"  name="<%=clName%>"/><br/>
	
		<table Border="0" Width="100%" align="center">
				<tr>		
						<%
								
									 groupRank = groupRank + 1;
									 curName = "group:" + groupRank + ":" + groupRank + ":1";
							fieldValue = (String)groupTable.get(curName);
							if(fieldValue == null){
									fieldValue ="";
							}
								%>
									<td align="center">
								<select name="<%=curName%>" size="1"> 
											<!-- Image fields -->
											<%
												
												fieldValue = (String)groupTable.get(curName);
												if(fieldValue == null){
													fieldValue ="";
												}
												for (ii = 0; ii < mediaResourcesTable.size(); ii++){
													fieldName = (String)mediaResourcesTable.get(ii);
													
													if(ii==0){%>
														<option>
													<%
													}
													if(fieldName.equals(fieldValue)){%>
								 						<option selected="selected"><%=fieldName%></option>
													<%}else {%>
							 							<option><%=fieldName%></option>
													<%}
												}
											%>
											</select><br/>
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
		<input type="submit"  name="submit" value="Click to submit" align="middle" />	

		</form>
		</center>
	</body>
</html>
