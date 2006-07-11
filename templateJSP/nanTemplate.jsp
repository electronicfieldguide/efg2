<%@page import="java.util.*,java.io.*,java.sql.*, project.efg.util.*, project.efg.efgInterface.*" %><html>
	<head>
	<%
	    project.efg.efgInterface.EFGDSHelperFactory dsHelperFactory = new project.efg.efgInterface.EFGDSHelperFactory();
 String dataSourceName = request.getParameter("dataSourceName");
   
//  EFGDataSourceHelper dsHelper = dsHelperFactory.getDataSourceHelper(); 
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
int imagesSize = mediaResourcesTable.size();
int characterSetSize = characterSet.size();
int tableSize = table.size();
int ii=0;
%>
		<title>Templates for <%=dataSourceName%></title>
		<link href="http://orca.cs.umb.edu/efg/monteverde/nantucket.css" rel="stylesheet"/>
	</head>
<body>
<form method="post" action="test.jsp">
		<table width="600" class="title">
			<tr>
				<td colspan="2" align="left" class="comname">
					<select name="group1" size="1">
				  <%
						ii=0;
						it = characterSet.iterator();
						while (it.hasNext()) {
						
							fieldName = ((String)it.next());
							if(ii==0){
							%>
							 <option>
							 <%
							}%>
							 <option><%=fieldName%></option>
						<% 	ii++;
						}
				%>
					</select>    
				</td>
				<td class="famname" align="left">
						<select name="group1" size="1">
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
									%>
										  <option><i><%=fieldName%></i></option>
									<% 	ii++;
								}
							%>
							</select>		
				</td>
			</tr>
			<tr>
				<td align="left" class="sciname">
						<select name="group1" size="1">
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
									%>
										  <option><i><%=fieldName%></i></option>
									<% 	ii++;
								}
							%>
							</select>		
				</td><!--FIELDS-->
				<td class="famname" align="left">
					<td class="famname" align="left">		
						<select name="group1" size="1">
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
									%>
										  <option><i><%=fieldName%></i></option>
									<% 	ii++;
								}
							%>
							</select>		
					</td><!--FIELDS -->
					<td class="famname" align="left"/>
				</td>
			</tr>
		</table>
		<p/>
		<table background-color="#ffffff" width="600px" valign="top">
			<tr>
				<td align="center"> 
					<table class="imageframe">
						<tr>
							<td>
									<table align="center" cellspacing="5" class="images">									
									<%	
										for(int counter1 = 0;  counter1 < imagesSize; counter1++){
									%>
									<tr>
										<td class="id_text">
											<select name="group1" size="1">
											<!-- Image fields -->
											<%
												for (ii = 0; ii < mediaResourcesTable.size(); ii++){
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
											<br/><input type="text" name="caption1"/>
										</td>
										<td class="id_text">
											<select name="group1" size="1">
											<%
												for (ii = 0; ii < mediaResourcesTable.size(); ii++){
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
											<br/><input type="text" name="caption1"/>
										</td>
									</tr>
									<%
									counter1++;
									}%>
									<tr/>
									<tr/>
									<tr/>
								</table>
							</td>
						
						</tr>
					</table>
					<div class="photocred">		
						<select name="group1" size="1">
				  <%
						ii=0;
						it = characterSet.iterator();
						while (it.hasNext()) {
						
							fieldName = ((String)it.next());
							if(ii==0){
							%>
							 <option>
							 <%
							}%>
							 <option><%=fieldName%></option>
						<% 	ii++;
						}
				%>
					</select>   <input type="text" name="credits"/>	 
					</div>
					<table></table>
					<br/>
					<hr/>
					<br/>
					<table class="details" width="100%" bgcolor="white">
						<tr>
							<td>
							<!-- find how many field names then create as many paragraphs 	-->
							<%	
								for(int counter = 0; counter < tableSize; counter++){
									//only do things that are not lists
								%>
									<p class="detail_text">
									<input type="text" name="credits"/>	:					
								<select name="group1" size="1">
							  <%
								ii=0;
								it = characterSet.iterator();
								while (it.hasNext()) {
									fieldName = ((String)it.next());
									if(ii==0){
									%>
									 <option>
									 <%
									}%>
									 <option><%=fieldName%></option>
									<% 	ii++;
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
					<table class="credits">
						<tr>
							<td>
								<p class="credits">
									<input type="text" name="credits"/>						
									<select name="group1" size="1">
									 <%
										ii=0;
										it = characterSet.iterator();
										while (it.hasNext()) {
											fieldName = ((String)it.next());
											if(ii==0){
											%>
											 <option>
											 <%
											}%>
										 <option><%=fieldName%></option>
										<% 	ii++;
										}
										%>
									</select>    
								</p>
								<p class="credits">
									<input type="text" name="credits"/>				
									<input type="text" name="credits"/>																	
								</p>
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
