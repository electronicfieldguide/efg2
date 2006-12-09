<%@page import="java.util.Hashtable,
project.efg.util.EFGImportConstants,
project.efg.util.TemplateMapObjectHandler,
project.efg.util.TemplateObject,
project.efg.util.EFGDisplayObject, 
java.util.Collections,
java.util.Map,
java.util.TreeMap,
java.util.Iterator" %>
<html>
  <head>
  	<%
		String serverName = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		Hashtable map = 
			TemplateMapObjectHandler.getTemplateObjectMap(null);
		Iterator it = null;
		 Map sortedMap = Collections.synchronizedMap(new TreeMap());
		if(map != null){
			it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = (String)it.next();
				
				TemplateObject templateObject = (TemplateObject)map.get(key);
				sortedMap.put(templateObject,key);
				
			}//end while
		}
   	%>
	<title>Links</title>
  </head>
  <body bgcolor="#ffffff">
  	<center>
		<h3>You can use the following links to get directly to configured Search result Pages.Right-click the mouse on the link to copy (Control-Click for Mac)</h3>
		<%if(sortedMap.size() > 0){ 
			it = sortedMap.keySet().iterator();
		%>
			<table border="1">
				<tr>
					<th>Datasource</th><th>Template Name</th><th>Edit/Delete Template</th>
				</tr>
				<%
				
				while (it.hasNext()) {
					TemplateObject templateObject  = (TemplateObject)it.next();
					//String key = (String)it.next();
					String key = (String)sortedMap.get(templateObject);
					EFGDisplayObject displayObject = templateObject.getDisplayObject();
					String templateName = templateObject.getTemplateName();
					String displayName = displayObject.getDisplayName();
					if((displayName == null) || (displayName.trim().equals(""))){
						displayName = displayObject.getDatasourceName();
					}//end if
					if((displayName != null) && 
							(templateName != null) && 
							(key != null)){		
							int index = key.indexOf("?");
							String toDelete = "/efg2/templateJSP/DeleteTemplate.jsp?";
							String toEdit = "/efg2/templateJSP/EditTemplate.jsp?";
							if(index > -1){
								toDelete =toDelete + key.substring(index+1,key.length());
								toEdit =toEdit + key.substring(index+1,key.length());
							}
							
							StringBuffer buff = new StringBuffer();
							buff.append("&amp;");
							buff.append(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
							buff.append("=");
							buff.append(templateName);
							buff.append("&amp;");
							buff.append(EFGImportConstants.DISPLAY_NAME); 
							 buff.append("="); 
							 buff.append(displayName);
				%>
						<tr>
							<td><%=displayName%></td>
							<td><a href="<%=serverName + key%>"><%=templateName%></a></td>
							<td>
							<% 
								if(key.toLowerCase().indexOf(EFGImportConstants.DEFAULT_SEARCH_FILE.toLowerCase()) == -1){%>
									<a href="<%=toEdit + buff.toString()%>">Edit</a> , <a href="<%=toDelete + buff.toString()%>">Delete</a> </td>
								<%}
							%>
							
							
						</tr>
				<%	
					}//end inner if
				}//end while
				%>
			</table>
		<%
		}//end if
		else{%>
			<p>No templates configured!!</p>
		<%}//end else%>
	</center>
  </body>
</html>
