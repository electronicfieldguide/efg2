<%@page import="java.util.Iterator,
project.efg.util.interfaces.EFGImportConstants"
%>

<%@page import="project.efg.server.servlets.EFGContextListener"%>
<%@page import="java.util.Map"%>
<%@page import="project.efg.server.utils.ServletCacheManager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "
http://www.w3.org/TR/html4/strict.dtd">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd ">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN"
"http://www.w3.org/TR/html4/frameset.dtd">
<html>	
	<head>
	 	<%@ include file="Header.jsp"%>	
	 	<%
 			 	StringBuffer searchPageStr = new StringBuffer();
	 			StringBuffer searchLists = new StringBuffer();
 			 	StringBuffer searchPlates =new StringBuffer();
 			 	String dsName = "";
  			 	Iterator listInter = null;
 			 	boolean isEmpty = false;
 		 		String forwardPage="NoDatasource.jsp";
	 		 	String mainTableConstantStr ="&" + mainTableConstant.toString();
 			 	boolean found = false;
 			 	Map map = null;
 			 	map = ServletCacheManager.getDatasourceCache(mainTableName);
  			 	if(map != null){
 			 		searchPageStr.append(context);
 			 		searchPageStr.append("/searchPageBuilder?displayFormat=HTML");
 			 		searchPageStr.append("&");
 			 		searchPageStr.append(EFGImportConstants.SEARCHTYPE);
 			 		searchPageStr.append("=");
 			 		searchPageStr.append(EFGImportConstants.SEARCH_SEARCH_TYPE);
 			 		searchPageStr.append(mainTableConstantStr);
 			 		searchPageStr.append("&displayName=");
 			 		
 			 		searchLists.append(context);
 			 		searchLists.append("/search?displayFormat=HTML");
 			 		searchLists.append(mainTableConstantStr);
 			 		searchLists.append("&searchType=lists&displayName=");
 			 		
 			 		searchPlates.append(context);
 			 		searchPlates.append("/search?displayFormat=HTML");
 			 		searchPlates.append(mainTableConstantStr);
 			 		searchPlates.append("&searchType=plates&displayName=");
 			 		
 			 		dsName ="&"+ EFGImportConstants.DATASOURCE_NAME + "=";	  
  			 		listInter = map.keySet().iterator();
 			 	}
 			 	if(listInter != null){
 			 		if(map.keySet().size() < 1){
	 			 		isEmpty = true;
 			 		}
 			 	}
 			 	else{
 			 		isEmpty = true;
 			 	}
		 	%>
			<title>The EFG Project - Electronic Field Guides</title>
			<link rel="stylesheet" href="efg2web.css" type="text/css"/>
	</head>	
  	<body>
		<%@ include file="EFGTableHeader.jsp"%>			
		<table class="frame">
			<tr>
				<td>
					<table class="main">
						<tr>
							<td colspan="5" class="title">Search/Browse EFG Datasources</td>
						</tr>
						<%
						if(!isEmpty){
						
						while (listInter.hasNext()) { 
							String datasourceName= (String)listInter.next();
							String displayName = (String)map.get(datasourceName.toLowerCase());
							

							found = true;
							StringBuffer newSearchStr = new StringBuffer(searchPageStr.toString());
							newSearchStr.append(displayName);
							newSearchStr.append(dsName);
							newSearchStr.append(datasourceName);
							newSearchStr.append(mainTableConstantStr);
							
							StringBuffer newSearchPlates =new StringBuffer(searchPlates.toString());
							newSearchPlates.append(displayName);
							newSearchPlates.append(dsName);
							newSearchPlates.append(datasourceName);
							newSearchPlates.append(mainTableConstantStr);
							
							StringBuffer newSearchLists = new StringBuffer(searchLists.toString());
							newSearchLists.append(displayName);
							newSearchLists.append(dsName);
							newSearchLists.append(datasourceName);
							newSearchLists.append(mainTableConstantStr);
						%>
						<tr>
							<td colspan="5">
								<table class="rowcapsule">
									<tr>
										<td class="efglisttitle"><%=displayName%></td>
										<td class="efglistlinks">
											<a class="efglist" href="<%=newSearchStr.toString()%>" title="search datasource">search</a> | 
											<a class="efglist" href="<%=newSearchLists.toString()%>" title="browse a list of taxon names">text list</a> | 
											<a class="efglist" href="<%=newSearchPlates.toString()%>" title="browse  plates">thumbnails</a>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<% 
						}//end while
						}
						else{%>
						<tr>
							<td colspan="5">
								<table class="rowcapsule">
									<tr>
										<td class="efglisttitle">No Datasources uploaded</td>
										<td class="efglistlinks">
										</td>
									</tr>
								</table>
							</td>
						</tr>							
							
						<%}
						%>
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
							<td class="copyright" colspan="5">&copy; EFG Group and UMass
							Boston. This research is supported in part by the NSF.
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
