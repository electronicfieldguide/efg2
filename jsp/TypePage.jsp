<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.ServletAbstractFactoryInterface,
project.efg.servlets.factory.ServletAbstractFactoryCreator,
project.efg.util.EFGImportConstants,
project.efg.util.EFGDisplayObjectList,
project.efg.util.EFGDisplayObject
" %>
<html>
 	<%@ include file="Head.html"%>
  <body>
  	<%@ include file="Headers.jsp"%>
  	<% 
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
						<%@ include file="Footer.jsp"%>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
