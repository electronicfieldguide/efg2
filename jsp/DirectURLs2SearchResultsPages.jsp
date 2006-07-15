<%@page import="java.util.Map,java.util.Iterator,project.efg.util.EFGImportConstants,project.efg.util.TemplateMapObjectHandler,project.efg.util.TemplateObject,project.efg.util.EFGDisplayObject" %>
<html>
  <head>
  <%
		
		String serverName = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		Map map = TemplateMapObjectHandler.getTemplateObjectMap();
		 Iterator it = null;
		if(map != null){
			 it = map.keySet().iterator();
		}
   %>
	<title>Links</title>
  </head>
  <body bgcolor="#ffffff">
  	<center>
			<h3>You can use the following links to get directly to configured Search result Pages</h3>
			<%
				if(it != null){ %>
					<table border="1">
						<tr>
							<th>Datasource</th><th>Template Name</th><th>URL</th>
						</tr>
						<%
							while (it.hasNext()) {
								String key = (String)it.next();
								TemplateObject templateObject = (TemplateObject)map.get(key);
								EFGDisplayObject displayObject = templateObject.getDisplayObject();
								String templateName = templateObject.getTemplateName();
								String displayName = displayObject.getDisplayName();
								if((displayName == null) || (displayName.trim().equals(""))){
									displayName = displayObject.getDatasourceName();
								}//end if
								if((displayName != null) && (templateName != null) && (key != null)){%>
						<tr>
							<td><%=displayName%></td><td><%=templateName%></td><td><%=serverName + key%></td>
						</tr>
								<%	
								}//end inner if
								%>		 
						<%
							}//end while
						%>
					</table>
				<%}//end if
				else{%>
					<p>No templates configured!!</p>
				<%}//end else%>
		</center>
  </body>
</html>