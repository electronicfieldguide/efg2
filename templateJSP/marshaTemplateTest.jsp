<%@page import="java.util.*,java.io.*,java.sql.*, project.efg.util.*, project.efg.templates.taxonPageTemplates.*"%>
<html>
	<head>
	<%
	TemplateProducer tp = new TemplateProducer();
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
	
	
	Hashtable groupTable=new Hashtable();
	   
		
	String fieldValue = "";
	//sort and iterate
	%>
	<title><%=dataSourceName%></title>
	<link href="bogstyle.css" rel="stylesheet" />
	</head>
	<body>
	 <form method="post" action="<%=context%>/configTaxonPage">
		<%String name =tp.getGrouptText(true,true);%>
		<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_NAME%>"  value="<%=dataSourceName%>"/>
		<input type="hidden"   name="<%=EFGImportConstants.XSL_STRING%>"  value="<%=xslFileName%>"/>
		<span class="title"> 
			<input type="text" name="<%=name%>" value="<%=fieldValue%>" size="50"/>
		</span>
		<%
			name = tp.getGrouptText(false,false);
			
		%>
		<span class="pageauthor">
			<input type="text" name="<%=name%>"  value="<%=fieldValue%>" size="20"/>
		</span>
		<table>
			<tr>
			<%
				name = tp.getGroupLabel(true,true);
			%>
			<td class="headingtop">
				<input type="text" name="<%=name%>" value="<%=fieldValue%>"/></td>
				<td class="datatop">
					<div class="comname">
						<%
						name =tp.getGroup(true,true);
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
						
						name = tp.getGroup(false,true);
					%>		
					<input type="hidden" name="<%=name%>" value="<%=fieldValue%>"/>
					<%
						
						name = name = tp.getGroup(false,true);
					%>		
					<input type="hidden" name="<%=name%>" value="<%=fieldValue%>"/>
					<div class="sciname">
						<%
							
							name = tp.getGroup(false,true);
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
							name = tp.getGroup(false,true);
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
						name = tp.getGroup(false,true);
						
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
					name = tp.getGroup(false,true);
					
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
						name = tp.getGroup(false,true);
						
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
			
			<td class="imagestop">
					<%	
						name = tp.getGroup(true,true);
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
							String clMed2 = tp.getCharacterLabel(true,true);
						%>
						<input type="text" name="<%=clMed2%>" value="<%=fieldValue%>"/>
					</span>
			</td>
		</tr>
		<%
		for(int counter = 0; counter < tableSize; counter++){
			
			name = tp.getGroup(true,true);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}

			String imageName=tp.getGroup(true,true);
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
					//FIX ME
					String clMed1 = "";//"cl:5:" + groupRank + ":2";
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
			name =tp.getGroup(true,true);
			String clname =tp.getCharacterLabel(true,true);
			
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
