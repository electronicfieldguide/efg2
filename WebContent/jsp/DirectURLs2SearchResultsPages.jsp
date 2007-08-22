<%@page import="java.util.Hashtable,
project.efg.util.interfaces.EFGImportConstants,
project.efg.util.utils.TemplateMapObjectHandler,
project.efg.util.utils.TemplateObject,
project.efg.util.utils.EFGDisplayObject,
java.util.Collections,
java.util.Map,java.util.TreeMap,java.util.Iterator" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">
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
	<script language="JavaScript" src="js/prototype1.5.js" type="text/javascript"></script>

	<script language="JavaScript" src="plateConfiguration/javascripts/efg2_ajaxfunctions.js" type="text/javascript"></script>
	<script language="JavaScript" src="js/OtherFunction.js" type="text/javascript"></script>
	
  </head>
  <body>
	<%@ include file="EFGTableHeader.jsp"%>	
		<table class="frame" summary="">
			<tr>
				<td>
					<table class="directurl" summary="">					 
						<tr>
							<td colspan="5" class="title">The following are direct links to configured EFG pages. <br/>
								<span class="subtitle">Right-click on Template Name to copy the URL (Control-Click for Mac)</span>
							</td>
						</tr>
						<tr>
							<td colspan="5">
							<%
							if(sortedMap.size() > 0){ 
								it = sortedMap.keySet().iterator();
							%>	
								<table summary="">	
									<tr>
										<td class="efglisttitle"><span class="datasourcehead">EFG Title</span></td>
										<td class="efglistlinksdirecturl"><span class="datasourcehead">Template Name</span></td>
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
														if((datasourceName != null) && 
														(templateName != null) && 
														(key != null)){		
															//searchType=lists
															int index = key.indexOf("?");
															StringBuffer toDelete = new StringBuffer();
															toDelete.append(context);
															toDelete.append("/templateJSP/DeleteTemplate.jsp?");
															StringBuffer toEdit =new StringBuffer();
															//TODO HACK refactor
															if(key.indexOf("=lists") > -1){
																templateType = "Text List";
															}
															else if(key.indexOf("=plates") > -1){
																templateType = "Thumbnails";
															}
															else if(key.indexOf("=searches") > -1){
																templateType = "Search Page";
															}
															else if(key.indexOf("=taxon") > -1){
																templateType = "Taxon Page";
															}
															else if(key.indexOf(EFGImportConstants.BOOK_FOLDER) > -1){
																templateType = "PDF Book";
															}
															else{
																templateType = "PDF Page";
																
															}	
															
															String substr = "";
															if(index > -1){
																substr = key.substring(index+1,key.length());
															}
															StringBuffer pdfBuffer = new StringBuffer();
															if(templateType.equals("PDF Page")){
																toEdit.append(context);
																toEdit.append("/plateConfiguration/platequerydatawrapper.jsp?");
																
																
																pdfBuffer.append(EFGImportConstants.GUID);
																pdfBuffer.append("="); 
																pdfBuffer.append(templateObject.getGUID());
																pdfBuffer.append("&amp;");
																pdfBuffer.append(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
																pdfBuffer.append("="); 
																pdfBuffer.append(templateName); 
																pdfBuffer.append("&amp;");
																pdfBuffer.append(EFGImportConstants.DATASOURCE_NAME); 
																pdfBuffer.append("="); 
																pdfBuffer.append(datasourceName);
																pdfBuffer.append("&amp;");
																pdfBuffer.append(ALL_TABLE_NAME);
																pdfBuffer.append("=");
																pdfBuffer.append(mainTableName);
																
															
																//toEdit.append(substr);
																//toEdit.append("&amp;");
																toEdit.append(pdfBuffer);
																	
																pdfBuffer.append("&amp;");
																pdfBuffer.append(EFGImportConstants.SEARCH_TYPE_STR);
																pdfBuffer.append("=pdfs"); 
															}
															else{
																toEdit.append(context);
																toEdit.append("/templateJSP/EditTemplate.jsp?");
																toDelete.append(substr);
																toEdit.append(substr);

															}
															StringBuffer buff = new StringBuffer();
															buff.append("&amp;");
															buff.append(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
															buff.append("=");
															buff.append(templateName);
														boolean notDefault = false;
														if("Search Page".equals(templateType)){	
															if(key.toLowerCase().indexOf(EFGImportConstants.DEFAULT_SEARCH_PAGE_FILE.toLowerCase()) == -1){
																notDefault = true;
															}
														}
														else{
															if(key.toLowerCase().indexOf(EFGImportConstants.DEFAULT_SEARCH_FILE.toLowerCase()) == -1){
															
															notDefault = true;
															}
														}
														
													
									%>							
									<tr>
										<td class="efglisttitle"><%=displayName%></td>
										<td class="efglistlinksdirecturl">
											<% 
												if(templateType.equals("PDF Page")) {
													String js = request.getContextPath() + key.replaceAll("=xml","=HTML");
											%>		
													<a class="efglist" href="<%=js%>"><%=templateName%></a>
											<% 
												} else{
													if(key.indexOf(EFGImportConstants.EFG_GLOSSARY_TABLES) > -1){
											 			key = key + "&" +  EFGImportConstants.SHOW_ALL+ "=all";
											 		}
											%>
												<a class="efglist" href="<%=serverName + key%>"><%=templateName%></a>
											<% }%>
										</td>
										<td class="datatype">
											<%=templateType%>
										</td>
										<td class="datasourcename">
											<%=datasourceName%>
										</td>
										<td class="actionlinks">
											<%
											StringBuffer deleteMsg = new StringBuffer();
									  		  deleteMsg.append("deleteConfirmMsg('");
									  			deleteMsg.append(templateName);
									  		  deleteMsg.append("');return document.delete_returnValue");
											if(templateType.equals("PDF Page")){%>
												<a class="efglist" href="<%=toEdit.toString()%>">Edit</a> ,
												<a class="efglist" href="<%=toDelete.append(pdfBuffer.toString()).toString()%>" onclick="<%=deleteMsg.toString()%>">Delete</a> 
	
											<%}
											else if(templateType.equalsIgnoreCase("PDF Book")){
												//do nothing
											}
											else if(notDefault){
											%>
												<a class="efglist" href="<%=toEdit.append(buff.toString()).toString()%>">Edit</a> , 
												<a class="efglist" href="<%=toDelete + buff.toString()%>" onclick="<%=deleteMsg.toString()%>">Delete</a> 
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