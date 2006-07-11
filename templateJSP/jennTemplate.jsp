<%@page import="java.util.*,java.io.*,java.sql.*, project.efg.util.*, project.efg.efgInterface.*" %>
<html>
	<head>
	<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<link href="http://orca.cs.umb.edu/efg/monteverde/newenglandstyle.css" rel="stylesheet"/>

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

//sort and iterate
%>
</head>
<body>
<form method="post" action="test.jsp">

	<table width="600" class="title">
		<tr>
			<td>
				<table align="center">
					<tr>
						<td align="left" class="comname">	
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
										 <option><%=fieldName%></option>
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
				</table>
			</td>
			<td class="image">
				<table cellspacing="5" border="0">
					<tr>
						<td class="id_text">
							<%	
							for(int counter1 = 0;  counter1 < imagesSize; counter1++){
							%>
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
							<%
							}
							%>
						</td>
						<br/>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<p>
	</p>
	<table class="characters">
		<tr>
			<td>
				<%for(int counter = 0; counter < tableSize; counter++){%>
					<p class="detail_text">
						<strong>	<input type="text" name="title2"/>: </strong>
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
								 <option><%=fieldName%></option>
								<% 	ii++;
							}
						%>
						</select>
					</p>
				<%
				}%>	
			</td>
		</tr>
	</table>
	<table class="credits">
		<tr>
			<td height="20px"></td>
		</tr>
		<tr>
			<td class="credits">
				<p class="credits">
				<input type="text" name="title2"/>
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
								 <option><%=fieldName%></option>
						<% 	ii++;
						}
					%>
					</select>
				</p>
			</td>
		</tr>
	</table>
	<input type="submit"  name="submit" value="Click to submit" align="middle" />	
	</form>
</body>
</html>
