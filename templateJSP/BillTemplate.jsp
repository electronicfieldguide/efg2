<%@page import="java.util.*,java.io.*,java.sql.*, project.efg.util.*, project.efg.efgInterface.*, project.efg.templates.taxonPageTemplates.*"%><html>
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
		else{
			characterSet.add(fieldName.trim());
		}	
	 }
	 int numberOfImages = mediaResourcesTable.size();
	 int characterSetSize = characterSet.size();
	 int tableSize = table.size();
	 int groupRank = 1;
 	 String name = "group:" + groupRank + ":" + groupRank;
 	 String curName = "";
       String clName="";
	 String tlName ="";
	 int numberOfIdentifications = 4;
	 int ii=0;
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
	boolean bool = false;
	String fieldValue = null;
     %>
		<title>Templates for <%=dataSourceName%></title>
		<link href="famstyle.css" rel="stylesheet"/>
	</head>
<body>
<form method="post" action="<%=context%>/configTaxonPage">
<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_NAME%>"  value="<%=dataSourceName%>"/>
<input type="hidden"   name="<%=EFGImportConstants. XSL_STRING%>"  value="<%=xslFileName%>"/>

	<table width="600" class="title">
		<tr>
			<td class="famname" align="left">
				<%
					curName = name + ":1";
					fieldValue = (String)groupTable.get(curName);
					if(fieldValue == null){
						fieldValue ="";
					}

				%>
				<select name="<%=curName%>" size="1"> <!-- GENUS -->
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
			<td>
				<%curName = name + ":2";
					bool = false;
					fieldValue = (String)groupTable.get(curName);
					if(fieldValue == null){
						fieldValue ="";
					}
				%>

				<select name="<%=curName%>" size="1"> <!-- Species -->
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

			<td class="famname" align="left">
				<td class="famname" align="left">
					<%curName = name + ":3";
					fieldValue = (String)groupTable.get(curName);
					if(fieldValue == null){
						fieldValue ="";
					}
					%>

					<select name="<%=curName%>" size="1"> <!-- AUTHOR -->
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
					</td><!--FIELD -->
					<td class="famname" align="left"/>
				</td>
			</tr>
			<% groupRank = groupRank + 1;
			   name = "group:" + groupRank + ":" + groupRank;
			%>

			<tr>
				<td class="famname" align="left">
						<%
							curName = name + ":1";
							bool = false;
							fieldValue = (String)groupTable.get(curName);
							if(fieldValue == null){
							fieldValue ="";
							}
						%>
						<select name="<%=curName%>" size="1"> <!-- FAMILY -->
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
				</td><!--FIELDS-->

				<td class="famname" align="left">
					<td class="famname" align="left">	
					
					</td><!--FIELDS -->

					<td class="famname" align="left">	
						<%curName = name + ":2";
							bool = false;
							fieldValue = (String)groupTable.get(curName);
							if(fieldValue == null){
								fieldValue ="";
							}
						%>	
						<select name="<%=curName%>" size="1"> <!-- FAMILY COMMON NAME -->
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
					</td><!--FIELDS -->
					<td class="famname" align="left"/>
				</td>
			</tr>
		</table>
		<p/>
		<% groupRank = groupRank + 1;
		   name = "group:" + groupRank + ":" + groupRank;
		%>

		<table valign="top" width="600px">
			<tr>
				<td>
					<table align="center" cellspacing="15" border="1">
						<tr>
							<td>
								<table cellspacing="5" border="0"><!--IMAGES -->
									<%	
									for(int counter1 = 0;  counter1 < numberOfImages; counter1++){
										curName = name + ":" + (counter1 + 1);
										

									%>
									<tr>
										<td class="id_text">
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
											</select>
											<%clName = "cl:" +  groupRank + ":" + (groupRank + counter1 + 1);
												fieldValue = (String)groupTable.get(clName);
												if(fieldValue == null){
													fieldValue ="";
												}
											%>
											
											<br/><input type="text" name="<%=clName%>" value="<%=fieldValue%>"/><!-- IMAGE CAPTION -->
										</td>
										<% if((counter1 + 1) < numberOfImages){
											curName = name + ":" + (counter1 + 2);
										%>
										<td class="id_text">
											<select name="<%=curName%>" size="1"> 
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
													<%}else{%>
							 							<option><%=fieldName%></option>
													<%}
												}
											%>
											</select>
											<%clName = "cl:" +  groupRank + ":" + (groupRank + counter1 + 2);
												fieldValue = (String)groupTable.get(clName);
												if(fieldValue == null){
													fieldValue ="";
												}
											%>
											<br/><input type="text" name="<%=clName%>" value="<%=fieldValue%>"/>
										</td>
										<%}%>
									</tr>
									<%
									counter1++;
									}%>
									<tr/>
									<tr/>
									<tr/>
								</table>
							</td>
							<% groupRank = groupRank + 1;
		   					  name = "group:" + groupRank + ":" + groupRank;
							%>

							<td valign="top" width="150px" class="identification_td" bgcolor="white"> 

								<%
								int counterx = 0;	
								for(counterx = 0;  counterx < numberOfIdentifications; counterx++){
									curName = name + ":" + (counterx + 1);
									bool = false;
									fieldValue = (String)groupTable.get(curName);
									if(fieldValue == null){
										fieldValue ="";
									}

									%>
									<p class="id_text">
										<strong>
											<select name="<%=curName%>" size="1"> <!-- IDENTIFICATION -->
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
										</strong>   
								 	</p>
								<%}%>

								<p class="id_text">
 								<%clName = "cl:" +  groupRank + ":" + (groupRank + counterx + 1);
										fieldValue = (String)groupTable.get(clName);
										if(fieldValue == null){
											fieldValue ="";
										}
								%>
									 <input type="text" name="<%=clName%>" value="<%=fieldValue%>"/> 
								</p>
							</td>
						</tr>
					</table>
					<br/>
					<hr/>
					<br/>
					<% groupRank = groupRank + 1;
		   			   name = "group:" + groupRank + ":" + groupRank;
					%>

					<table width="100%" bgcolor="white">
						<tr>
							<td>
							<!-- find how many field names then create as many paragraphs 	-->
								<%	
								for(int counter = 0; counter < tableSize; counter++){
									curName = name + ":" + (counter + 1);
									clName = "cl:" +  groupRank + ":" + (groupRank + counter + 1);
									bool = false;
									fieldValue = (String)groupTable.get(clName);
									if(fieldValue == null){
										fieldValue ="";
									}
									%>
									<p class="detail_text">
									<input type="text" name="<%=clName%>" value="<%=fieldValue%>"/>				
									<select name="<%=curName%>" size="1"> 
				  					<%
									ii=0;
									fieldValue = (String)groupTable.get(curName);
									if(fieldValue == null){
										fieldValue ="";
									}
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
									</p>
								<%}%>
							</td>
						</tr>
					</table>
					<br/>
					<hr/>
					<br/>
					<% groupRank = groupRank + 1;
		   			   name = "group:" + groupRank + ":" + groupRank;
					   curName = name + ":1";
					   String groupLabel1 = "gl:" +  groupRank + ":" + groupRank;
  						String glValue1 = (String)groupTable.get(groupLabel1);
						if(glValue1 == null){
							glValue1 ="";
						}

					   clName = "gl:" +  groupRank + ":" + groupRank;
					   bool = false;
					   String clValue = (String)groupTable.get(clName);
						if(clValue == null){
							clValue ="";
						}
					   	tlName = "tl:" +  groupRank + ":" + groupRank + ":1";
						String tlValue = (String)groupTable.get(tlName);
						if(tlValue == null){
							tlValue ="";
						}
						fieldValue = (String)groupTable.get(curName);
						if(fieldValue == null){
							fieldValue ="";
						}
					%>
					<table width="100%" bgcolor="white">
						<tr>
							<td>
								<p class="detail_text">
									<input type="text" name="<%=groupLabel1%>" value="<%=glValue1%>"/> 	<!-- CREDITS -->					
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
									</select>  <input type="text" name="<%=tlName%>" value="<%=tlValue%>"/>   
									</p>
									<!--
									<p class="detail_text">
										<input type="text" name="<%=clName%>" value="<%=clValue%>"/> 	
										<input type="text" name="<%=tlName%>" value="<%=tlValue%>"/> 							
									</p>-->
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
