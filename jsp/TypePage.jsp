<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.ServletAbstractFactoryInterface,
project.efg.servlets.factory.ServletAbstractFactoryCreator,
project.efg.util.EFGImportConstants,
project.efg.util.EFGDisplayObjectList,
project.efg.util.EFGDisplayObject
" %>
<html>
 	<head>	
 	
 	  	<% 
 	  	String mainTable = EFGImportConstants.EFG_RDB_TABLES;
 		String ALL_TABLE_NAME = EFGImportConstants.ALL_TABLE_NAME;
 		
 		String mainTableName = request.getParameter(ALL_TABLE_NAME);
 		if(mainTableName == null || mainTableName.trim().equals("")){
 			   mainTableName = mainTable;
 		   }
 		   StringBuffer mainTableConstant =new StringBuffer(ALL_TABLE_NAME + "=" + mainTableName);

 		   
 		String linksFile = "Links.html"; 
 		String typePageFile = "TypePage.jsp?"+ mainTableConstant.toString();
 		
 			
 		   String beginString = "/efg2/templateJSP/ConfigurePageBegin.jsp?" +  mainTableConstant.toString(); 
 		   String advancedString = "/efg2/templateJSP/ConfigurePage.jsp?" +  mainTableConstant.toString(); 

   	String forwardPage="NoDatasource.jsp";
   	String context = request.getContextPath();
	String mainTableConstantStr ="&" + mainTableConstant.toString();
	boolean found = false;
	ServletAbstractFactoryInterface servFactory = ServletAbstractFactoryCreator.getInstance();
	servFactory.setMainDataTableName(mainTableName);	
	
	String searchPageStr = context + "/SearchPage.jsp?pageType=option" + mainTableConstantStr + "&displayFormat=HTML&displayName=";
	String searchLists = context + "/search?maxDisplay=100&displayFormat=HTML" + mainTableConstantStr + "&searchType=lists&displayName=";
	String searchPlates =context + "/search?maxDisplay=100&displayFormat=HTML"+ mainTableConstantStr + "&searchType=plates&displayName=";
	String dsName ="&"+ EFGImportConstants.DATASOURCE_NAME + "=";	  

	EFGDisplayObjectList listInter = servFactory.getListOfDatasources();
	boolean isEmpty = false;
	if(listInter != null){
		if(listInter.getCount() < 1){
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
		<table class="header">
			<tr>
				<td class="minilogo">
					<a href="index.html"><img src="efgpagelogomini.jpg" width="410" height="38" border="0" alt="The Electronic Field Guide Project" title="The Electronic Field Guide Project" align="left" /></a>
				</td>
				<td>
					<table class="iconbar">
						<tr>
							<td class="iconbar">
								<a href="<%=typePageFile %>">
									<img src="viewiconmini.jpg" width="41" border="0" alt="View Sample EFGs" title="View Sample EFGs" />
								</a>
							</td>
							<td class="iconbar">
								<a href="<%=linksFile %>">
									<img src="configiconmini.jpg" width="41" border="0" alt="Configure Your EFG" title="Configure Your EFG - Login Required" />
								</a>
							</td>
							<td class="iconbar">
								<a href="http://efg.cs.umb.edu/EFGsoftware/">
									<img src="downloadiconmini.jpg" width="41" border="0" alt="Download the EFG Software" title="Download the EFG Software" />
								</a>
							</td>
						</tr>
						<tr>
							<td class="icontext">View</td>
							<td class="icontext">Config</td>
							<td class="icontext">Download</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

		<table class="frame">
			<tr>
				<td>
					<table class="main">
						 <% 
						  if(!isEmpty){
						%>
						<tr>
							<td colspan="5" class="title">Search/Browse EFG Datasources</td>
						</tr>
						<%
						Iterator dsNameIter = listInter.getIterator(); 
						while (dsNameIter.hasNext()) { 
							EFGDisplayObject obj = (EFGDisplayObject)dsNameIter.next();
							String displayName = obj.getDisplayName();
							String datasourceName = obj.getDatasourceName();
							found = true;
							String newSearchStr = searchPageStr + displayName +  dsName + datasourceName + mainTableConstantStr;
							String newSearchPlates =searchPlates+ displayName +  dsName + datasourceName + mainTableConstantStr;
							String newSearchLists = searchLists+ displayName +  dsName + datasourceName + mainTableConstantStr;
						%>
						<tr>
							<td colspan="5">
								<table>
									<tr>
										<td class="efglisttitle"><%=displayName%></td>
										<td class="efglistlinks">
											<a class="efglist" href="<%=newSearchStr%>" title="search datasource">search</a> | 
											<a class="efglist" href="<%=newSearchLists%>" title="browse a list of taxon names">browse lists</a> | 
											<a class="efglist" href="<%=newSearchPlates%>" title="browse  plates">browse plates</a>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<% 
						}//end while
						if(!found){%>
							<h3> No datasources uploaded by author(s)</h3>
						<%}
						}//end outer if 
						else{
							RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
							dispatcher.forward(request, response);
						}
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
							<td class="copyright" colspan="5">&copy; 2006 EFG Group and UMass
							Boston. This research is supported in part by the NSF.
							</td>
						</tr>
						<tr>
							<td class="credits" colspan="5">Icon photos via Flickr, from left
							to right: urtica, Myownbiggestfan, Auntie_P.
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
