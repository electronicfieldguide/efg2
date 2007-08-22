<%@page import="project.efg.util.interfaces.EFGImportConstants,
java.io.File,
project.efg.util.utils.TemplateMapObjectHandler,
project.efg.util.utils.TemplateObject,
project.efg.util.utils.EFGDisplayObject" %>
<html>
  <head>
   <%@ include file="../../Header.jsp" %>
   <%@ include file="TestHeader.jsp" %>
  <%
	StringBuffer querySearch = new StringBuffer();
	String testDisp = "=10";
	String maxDisp = "=100";
	StringBuffer testString = new StringBuffer();
	StringBuffer keyString = new StringBuffer();
	querySearch.append(context);
	querySearch.append("/search?dataSourceName=");
	querySearch.append(dsName);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.GUID);
	querySearch.append("=");
	querySearch.append(guid);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.XSL_STRING);
	querySearch.append("=");
	querySearch.append(xslName);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.ALL_TABLE_NAME);
	querySearch.append("=");
	querySearch.append(alldbname);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.SEARCH_TYPE_STR);
	querySearch.append("=");
	querySearch.append(searchType);
	querySearch.append("&");
	querySearch.append(EFGImportConstants.DISPLAY_FORMAT);
	querySearch.append("=");
	querySearch.append(EFGImportConstants.HTML);
	
	if(guid != null){
		keyString.append(querySearch.toString());
		//keyString.append(maxDisp);
		String key = keyString.toString();		
		TemplateObject templateObject = new TemplateObject();
		EFGDisplayObject displayObject = new EFGDisplayObject();
		displayObject.setDisplayName(displayName);
		displayObject.setDatasourceName(dsName);
		
		templateObject.setTemplateName(uniqueName);
		templateObject.setGUID(guid);
		templateObject.setDisplayObject(displayObject);
		TemplateMapObjectHandler.add2TemplateMap(key,templateObject,null);
	}
	querySearch.append("&");
	querySearch.append(EFGImportConstants.MAX_DISPLAY);
	testString.append(querySearch.toString());
	testString.append(testDisp);
   %>
	<title>Test Search Page Configuration</title>
	<link rel="stylesheet" href="<%=context%>/efg2web.css" type="text/css"/>
  </head>
  <body>
	  	<%@ include file="../../EFGTableHeader.jsp"%>	
	  	<table class="frame" summary="">
				<tr>
					<td>
						<table class="directurl" summary="">					 
							<tr>
								<td colspan="5" class="title">The Datasource "<%=displayName%>" has been configured successfully!!!.<br/>
									<span class="subtitle"><a href="<%=testString.toString()%>"  target="TestWindow">Test Search Results Page</a></span>
								</td>
							</tr>
							<tr>
						</table>
					</td>
				</tr>
		</table>		  	
  </body>
</html>
