<%@page import="
java.util.List,
java.util.Iterator,
java.util.Hashtable,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.util.EFGImportConstants,
project.efg.util.TemplateProducer,
project.efg.Imports.efgInterface.EFGQueueObjectInterface
" %>
<html>
	<head>
	<%
	String context = request.getContextPath();

        String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME); 
        String datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
        String xslFileName = "marshaTaxonPage.xsl";
  	
    
  
    EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
   
    String fieldName = null;
    String fieldValue = null;
    
	Iterator it =null;
	List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
	Hashtable groupTable = new Hashtable();
	int tableSize = table.size();
	TemplateProducer tp = new TemplateProducer();
	boolean isNew = true;
	boolean isOld = false;
	//sort and iterate
	%>
	<title><%=datasourceName%></title>
	<link href="bogstyle.css" rel="stylesheet" />
	</head>
	<body>
	 <form method="post" action="<%=context%>/configTaxonPage">
		<%
		  
		   String name =tp.getCharacterText(isNew,isNew);
		   name =tp.getCurrentGroupText(name);
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
		%>
		<input type="hidden"   name="<%=EFGImportConstants.DISPLAY_NAME%>"  value="<%=displayName%>"/>
		<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_STR%>"  value="<%=datasourceName%>"/>
		<input type="hidden"   name="<%=EFGImportConstants.XSL_STRING%>"  value="<%=xslFileName%>"/>
		<span class="title"> 
			<input type="text" name="<%=name%>" value="<%=fieldValue%>" size="50"/>
		</span>
		<%
			
			name =tp.getCharacterText(isOld,isOld);
		   name =tp.getCurrentGroupText(name);
			
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}
			
			
		%>
		<span class="pageauthor">
			<input type="text" name="<%=name%>"  value="<%=fieldValue%>" size="20"/>
		</span>
		<table>
			<tr>
			<%
				
				name = tp.getGroupLabel(isNew,isNew);
				
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
						
						name = tp.getCharacter(isNew,isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
						%>
						<select name="<%=name%>" size="1">
				  		<%
							int ii = 0;
							it = table.iterator();
							while (it.hasNext()) {
							EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
								fieldName = (String)queueObject.getObject(1);
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
					
						name = tp.getCharacter(isOld,isNew);
						
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
					%>		
					<input type="hidden" name="<%=name%>" value="<%=fieldValue%>"/>
					<%
						name = tp.getCharacter(isOld,isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
					%>		
					<input type="hidden" name="<%=name%>" value="<%=fieldValue%>"/>
					<div class="sciname">
						<%
							name = tp.getCharacter(isOld,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
						%>				
						<select name="<%=name%>" size="1">
					  	<%
							ii = 0;
							it = table.iterator();
							while (it.hasNext()) {
							EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
								fieldName = (String)queueObject.getObject(1);
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
							
							name = tp.getCharacter(isOld,isNew);
							fieldValue = (String)groupTable.get(name);
							if(fieldValue == null){
								fieldValue ="";
							}
						%>				
						<select name="<%=name%>" size="1">
				  		<%
							ii = 0;
							it = table.iterator();
							while (it.hasNext()) {
							EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
								fieldName = (String)queueObject.getObject(1);
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
						
						name = tp.getCharacter(isOld,isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
					%>				
						<select name="<%=name%>" size="1">
				 	 	<%
							it = table.iterator();
							while (it.hasNext()) {
							EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
								fieldName = (String)queueObject.getObject(1);
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
		
					name = tp.getCharacter(isOld,isNew);
					fieldValue = (String)groupTable.get(name);
					if(fieldValue == null){
						fieldValue ="";
					}

				%>				
					<select name="<%=name%>" size="1">			
				  	<%
						ii = 0;
						it = table.iterator();
							while (it.hasNext()) {
							EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
								fieldName = (String)queueObject.getObject(1);
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
			
						name = tp.getCharacter(isOld,isNew);
						fieldValue = (String)groupTable.get(name);
						if(fieldValue == null){
							fieldValue ="";
						}
					%>				
					<select name="<%=name%>" size="1">		
				  	<%
						ii = 0;
						it = table.iterator();
							while (it.hasNext()) {
							EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
								fieldName = (String)queueObject.getObject(1);
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
			<%	//groupRank = groupRank + 1;
			%>
			<td class="imagestop">
					<%	
					
						String group4 =name = tp.getCharacter(isNew,isNew);
						fieldValue = (String)groupTable.get(group4);
						if(fieldValue == null){
							fieldValue ="";
						}

					%>
					<select name="<%=group4%>" size="1">
						<%
							for (ii = 0; ii < table.size(); ii++){
								EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)table.get(ii);
								fieldName = (String)queueObject.getObject(1);
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
						
							String clMed2  = tp.getCurrentCharacterLabel(group4);
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
			if(counter == 0){
				name = tp.getCharacter(isNew,isNew);
				
			}
			else{
				name = tp.getCharacter(isOld,isNew);
			}
			String clname = tp.getCurrentCharacterLabel(name);//groups do not have character ranks
			fieldValue = (String)groupTable.get(name);
			if(fieldValue == null){
				fieldValue ="";
			}

			
			String clValue = (String)groupTable.get(clname);
			if(clValue == null){
				clValue ="";
			}
			
			String imageName = tp.getCharacter(isOld,isOld);
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
					it = table.iterator();
							while (it.hasNext()) {
							EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
								fieldName = (String)queueObject.getObject(1);
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
					for (ii = 0; ii <table.size(); ii++){
						EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)table.get(ii);
						fieldName = (String)queueObject.getObject(1);
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
					
					String clMed1 = tp.getCurrentCharacterLabel(imageName);
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
			name = tp.getCharacter(isNew,isNew);
			String clname =tp.getCurrentCharacterLabel(name);
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
				it = table.iterator();
				while (it.hasNext()) {
					EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
					fieldName = (String)queueObject.getObject(1);
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
