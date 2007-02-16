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
  <%@ include file="Header.jsp"%>	
  	<%
		String serverName = request.getScheme() +
		"://"
		+ request.getServerName() + 
		":" + request.getServerPort();
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
	<title>The EFG Project - Electronic Field Guides</title>
	<link rel="stylesheet" href="efg2web.css" type="text/css"/>
  </head>
  <body>
	<%@ include file="EFGTableHeader.jsp"%>	
		<table class="frame" summary="">
			<tr>
				<td>
					<table class="directurl" summary="">					 
						<tr>
							<td colspan="5" class="title">The following are direct links to configured EFG pages. <br/>
								<span class="subtitle">Right-click on the name to copy (Control-Click for Mac)</span>
							</td>
						</tr>
						<tr>
							<td colspan="5">
							<%if(sortedMap.size() > 0){ 
								it = sortedMap.keySet().iterator();
							%>	
								<table summary="">	
									<tr>
										<td class="efglisttitle"><span class="datasourcehead">EFG Title</span></td>
										<td class="efglistlinks"><span class="datasourcehead">Template Name</span></td>
										<td class="datatype"><span class="datasourcehead">Type</span></td>
										<td class="datasourcename"><span class="datasourcehead">Datasource Name</span></td>
										<td class="actionlinks"><span class="datasourcehead">Options</span></td>
									</tr>
									<%								
									while (it.hasNext()) {
										TemplateObject templateObject  = (TemplateObject)it.next();
										String templateType = null;
										String key = (String)sortedMap.get(templateObject);
										EFGDisplayObject displayObject = templateObject.getDisplayObject();
										String templateName = templateObject.getTemplateName();
										String displayName = displayObject.getDisplayName();
										String datasourceName = displayObject.getDatasourceName();
										if((displayName == null) || (displayName.trim().equals(""))){
											displayName = datasourceName;
										}//end if
										if((displayName != null) && 
												(templateName != null) && 
												(key != null)){		
											//searchType=lists
											int index = key.indexOf("?");
											String toDelete = context + "/templateJSP/DeleteTemplate.jsp?";
											String toEdit = context + "/templateJSP/EditTemplate.jsp?";
											if(index > -1){
												toDelete =toDelete + key.substring(index+1,key.length());
												toEdit =toEdit + key.substring(index+1,key.length());
											}
											if(key.indexOf("=lists") > -1){
												templateType = "Text List";
											}
											else{
												templateType = "Thumbnails";
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
										<td class="efglisttitle"><%=displayName%></td>
										<td class="efglistlinks">
											<a class="efglist" href="<%=serverName + key%>"><%=templateName%></a>
										</td>
										<td class="datatype">
											<%=templateType%>
										</td>
										<td class="datasourcename">
											<%=datasourceName%>
										</td>
										<td class="actionlinks">
											<% 
											if(key.toLowerCase().indexOf(EFGImportConstants.DEFAULT_SEARCH_FILE.toLowerCase()) == -1){%>
												<a href="<%=toEdit + buff.toString()%>">Edit</a> , <a href="<%=toDelete + buff.toString()%>">Delete</a> 
											<%}
											%>
										</td>
									</tr>
										<%	
										}//end inner if
									}//end while
									%>
								</table>
								<%
							}//end if
							else{%>
								<p>No templates configured</p>
							<%}//end else%>									
							</td>
						</tr>										
						<tr>
							<td class="" />
							<td class="horizspacer" />
							<td class="" />
						
							<td class="horizspacer" />
							<td class="logos">
								<a href="http://efg.cs.umb.edu">
									<img src="efglogo.gif" width="52" height="56" border="0" alt="EFG Home Page" title="EFG Home Page" />

								</a>
								<a href="http://www.umb.edu">
									<img src="umblogo.gif" width="54" height="58" border="0" alt="UMass Boston Home Page" title="UMass Boston Home Page" />
								</a>
							</td>
						
						</tr>
						<tr>
							<td class="copyright" colspan="5">&copy; 2006 EFG Group and UMass
							Boston. This research is supported in part by the NSF.
							</td>

						</tr>
						<tr>
							<td class="timestamp" colspan="5">
								<script language="JavaScript"
									type="text/javascript">
									<!--
									 document.write("Last Modified " + document.lastModified)
									// -->
								</script>
							</td>

						</tr>					
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>