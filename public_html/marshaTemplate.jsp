<%@page import="java.util.*,java.io.*,java.sql.*, project.efg.util.*, project.efg.efgInterface.*, project.efg.templates.taxonPageTemplates.*"%>
<html>
	<head>
	<%
 	String context = request.getContextPath();
    	project.efg.efgInterface.EFGDSHelperFactory dsHelperFactory = new project.efg.efgInterface.EFGDSHelperFactory();
 	String dataSourceName = request.getParameter("dataSourceName");

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
		else{
			characterSet.add(fieldName.trim());
		}	
	}
	int imagesSize = mediaResourcesTable.size();	
	int characterSetSize = characterSet.size();
	int tableSize = table.size();
	int groupRank = 1;
	String realPath = getServletContext().getRealPath("/");
	StringBuffer fileLocationBuffer = new StringBuffer();
	fileLocationBuffer.append(realPath);
	fileLocationBuffer.append(EFGImportConstants.TEMPLATES_FOLDER_NAME);
	fileLocationBuffer.append(File.separator);
	fileLocationBuffer.append(dataSourceName);
	fileLocationBuffer.append(EFGImportConstants.TAXONPAGE_FILLER);
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
				if((gt.getLabel() != null) && (!gt.getLabel().trim().equals(""))){
					groupLabelKey = "gl:" + id + ":" + groupRank1;
					groupTable.put(groupLabelKey,gt.getLabel());
				}
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
		String fieldValue = null;
	//sort and iterate
	%>
	<title><%=dataSourceName%></title>
	<link href="bogstyle.css" rel="stylesheet" />
	</head>
	<body>
	 <form method="post" action="<%=context%>/configTaxonPage">
		<%String name = "tl:1:" + groupRank + ":1";
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
		%>
		<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_STR%>"  value="<%=dataSourceName%>"/>
		<input type="hidden"   name="<%=EFGImportConstants. XSL_STRING%>"  value="<%=xslFileName%>"/>
		<span class="title"> 
			<input type="text" name="<%=name%>" value="<%=fieldValue%>" size="50"/>
		</span>
		<%
			name = "tl:1:" + groupRank + ":2";
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
			groupRank = groupRank + 1;
			
		%>
		<span class="pageauthor">
			<input type="text" name="<%=name%>"  value="<%=fieldValue%>" size="20"/>
		</span>
		<table>
			<tr>
			<%
				name = "gl:2:" + groupRank;
				groupRank = groupRank + 1;
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}
			%>
			<td class="headingtop">
				<input type="text" name="<%=name%>" value="<%=fieldValue%>"/></td>
				<td class="datatop">
					<div class="comname">
						<%
						name = "group:3:" + groupRank +":1";
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						%>
						<select name="<%=name%>" size="1">
				  		<%
							int ii = 0;
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
					</div>
					<%
						groupRank = groupRank + 1;
						name = "group:3:" + groupRank +":1";
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
					%>		
					<input type="hidden" name="<%=name%>" value="<%=fieldValue%>"/>
					<%
						groupRank = groupRank + 1;
						name = "group:3:" + groupRank +":1";
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
					%>		
					<input type="hidden" name="<%=name%>" value="<%=fieldValue%>"/>
					<div class="sciname">
						<%
							groupRank = groupRank + 1;
							name = "group:3:" + groupRank +":1";
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
						%>				
						<select name="<%=name%>" size="1">
					  	<%
							ii = 0;
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
							name = "group:3:" + groupRank +":1";
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
						%>				
						<select name="<%=name%>" size="1">
				  		<%
							ii = 0;
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
					<span class="scinameauth">
					<%
						groupRank = groupRank + 1;
						name = "group:3:" + groupRank +":1";
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
					%>				
						<select name="<%=name%>" size="1">
				 	 	<%
							ii = 0;
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
					</span>
				</div>
				<div class="family">	
				<%
					groupRank = groupRank + 1;
					name = "group:3:" + groupRank +":1";
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}

				%>				
					<select name="<%=name%>" size="1">			
				  	<%
						ii = 0;
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
					</select> (	
					<%
						groupRank = groupRank + 1;
						name = "group:3:" + groupRank +":1";
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
					%>				
					<select name="<%=name%>" size="1">		
				  	<%
						ii = 0;
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
					</select>   )
				</div>
			</td>
			<%	groupRank = groupRank + 1;%>
			<td class="imagestop">
					<%	
						String group4 = "group:4:" + groupRank + ":1";;
						fieldValue = (String)groupTable.get(group4);
						if(fieldValue == null){
							fieldValue ="";
						}

					%>
					<select name="<%=group4%>" size="1">
						<%
							for (ii = 0; ii < mediaResourcesTable.size(); ii++){
								fieldName = (String)mediaResourcesTable.get(ii);
								if(ii==0){%>
									<option>
								<%
								}
								if(fieldName.equals(fieldValue)){%>
								 <option selected="selected"><%=fieldName%></option>
								<%}else{%>
							 	<option><%=fieldName%></option>
								<%}
							}
						%>
					</select>
					<br/>
					<span class="imagecaption">	
						<%
							String clMed2 = "cl:4:" + groupRank + ":1";
							fieldValue = (String)groupTable.get(clMed2);
							if(fieldValue == null){
								fieldValue ="";
							}

						%>
						<input type="text" name="<%=clMed2%>" value="<%=fieldValue%>"/>
					</span>
			</td>
		</tr>
		<%
		for(int counter = 0; counter < tableSize; counter++){
			groupRank = groupRank + 1;
			String str1 = ":5:" +  groupRank;
			String str = str1 + ":1";
			name = "group" + str;
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}

			String clname ="cl" +   str;
			String clValue = (String)groupTable.get(clname);
			if(clValue == null){
				clValue ="";
			}
			String imageName = "group"  + str1 + ":2";
			String imageValue = (String)groupTable.get(imageName);
			if(imageValue == null){
				imageValue ="";
			}
		%>
		<tr>
			<td class="heading">
				<input type="text" name="<%=clname%>" value="<%=clValue%>"/>
			</td>
			<td class="data">
				<select name="<%=name%>" size="1">
				<%
					ii = 0;
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
			<td class="images">
				<select name="<%=imageName%>" size="1">
				<%
					for (ii = 0; ii < mediaResourcesTable.size(); ii++){
						fieldName = (String)mediaResourcesTable.get(ii);
						if(ii==0){%>
							<option>
						<%
						}
						if(fieldName.equals(imageValue)){%>
							<option selected="selected"><%=fieldName%></option>
						<%}else{%>
							 <option><%=fieldName%></option>
						<%}
					}
				%>
				</select>
				<br/>
				<span class="imagecaption">	
					<%
					String clMed1 = "cl:5:" + groupRank + ":2";
					fieldValue = (String)groupTable.get(clMed1);
					if(fieldValue == null){
						fieldValue ="";
					}
					%>
					<input type="text" name="<%=clMed1%>" value="<%=fieldValue%>"/>
				</span>
			</td>
		</tr>
		<%
		}
		%>
		</table>
	</body>
	<span class="copyright">
		<%
			groupRank = groupRank + 1;
			String str1 = ":6:" +  groupRank;
			String str = str1 + ":1";
			name = "group" + str;
			String clname ="cl" +   str;
			fieldValue = (String)groupTable.get(clname);
			if(fieldValue == null){
				fieldValue ="";
			}
		%>
		<input type="text" name="<%=clname%>" value="<%=fieldValue%>"/>
		<select name="<%=name%>" size="1">
			<%
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					fieldValue ="";
				}

				ii = 0;
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
	</span>
	<br/><br/>
	<input type="submit"  name="submit" value="Click to submit" align="middle" />	
	</form>

</html>
