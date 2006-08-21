<%@page import="java.util.Map,java.util.Iterator,project.efg.util.EFGImportConstants,project.efg.util.TemplateMapObjectHandler,project.efg.util.TemplateObject,project.efg.util.EFGDisplayObject, java.util.Collections,java.util.Set,java.util.HashSet,java.util.Iterator" %>
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
			<h3>You can use the following links to get directly to configured Search result Pages.Right-click the mouse on the link to copy (Control-Click for Mac)</h3>
			<%
				if(it != null){ %>
					<table border="1">
						<tr>
							<th>Datasource</th><th>Template Name</th>
						</tr>
						<%
							 Set s = Collections.synchronizedSet(new HashSet());
							while (it.hasNext()) {
								String key = (String)it.next();
								TemplateObject templateObject = (TemplateObject)map.get(key);
								EFGDisplayObject displayObject = templateObject.getDisplayObject();
								String templateName = templateObject.getTemplateName();
								String displayName = displayObject.getDisplayName();
								if((displayName == null) || (displayName.trim().equals(""))){
									displayName = displayObject.getDatasourceName();
								}//end if
								if((displayName != null) && (templateName != null) && (key != null)){
								s.add(displayObject);
							%>
						<tr>
							<td><%=displayName%></td><td><a href="<%=serverName + key%>"><%=templateName%></a></td>
						</tr>
								<%	
								}//end inner if
								%>		 
						<%
							}//end while
							it = s.iterator();
							String plates = "plates";
							String lists = "lists";
							while (it.hasNext()) {
								EFGDisplayObject dso = (EFGDisplayObject)it.next();
								String ds = dso.getDatasourceName();
								String dp = dso.getDisplayName();
								if(dp == null){
									dp = ds;
								}
								StringBuffer buffer = new StringBuffer(serverName);
								buffer.append("/efg2/search?");
								buffer.append("dataSourceName=" );
								buffer.append(ds);
								buffer.append("&xslName=defaultSearchFile.xsl&displayFormat=HTML&maxDisplay=70&searchType=");
						%>
						<!--
						<tr>
							<td><%=ds%></td><td><a href="<%=buffer.toString() + plates%>">default Thumbnails</a></td>
						</tr>
							<tr>
							<td><%=ds%></td><td><a href="<%=buffer.toString() + lists%>">default Text List</a></td>
						</tr>
					-->
				<%
				}//end 2nd while
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
