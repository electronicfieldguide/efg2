<%@page import="project.efg.util.EFGImportConstants,project.efg.util.EFGMediaResourceSearchableObject,project.efg.servlets.factory.ServletAbstractFactoryImpl,project.efg.servlets.efgInterface.ServletAbstractFactoryInterface"%>
<html>
  <head>
   <head>
   <%@ include file="../Header.jsp" %>
   <%@ include file="TestHeader.jsp" %>
  <% 
    ServletAbstractFactoryInterface servFact = new ServletAbstractFactoryImpl();
  	servFact.setMainDataTableName(alldbname);
     EFGMediaResourceSearchableObject  searchO = servFact.getFirstField(displayName, dsName) ;
	String fieldToUse = null;
	if(searchO != null){
		fieldToUse  = searchO.getSearchableField();
	}
	 if((fieldToUse != null) && (!fieldToUse.trim().equals(""))){
			 fieldValue=fieldToUse;
	}
	 //DATASOURCE_NAME
	StringBuffer querySearch = new StringBuffer();
	querySearch.append(context);
	querySearch.append("/search?");
	querySearch.append(EFGImportConstants.DATASOURCE_NAME);
	querySearch.append("=");
	querySearch.append(dsName);
	querySearch.append("&");
	querySearch.append(fieldValue);
	querySearch.append("=");
	querySearch.append(EFGImportConstants.EFG_ANY);
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
	querySearch.append(EFGImportConstants.DISPLAY_FORMAT);
	querySearch.append("=");
	querySearch.append(EFGImportConstants.HTML);

	querySearch.append("&");
	querySearch.append(EFGImportConstants.MAX_DISPLAY);
	querySearch.append("=1");
	querySearch.append("&");
	querySearch.append(EFGImportConstants.SEARCH_TYPE_STR);
	querySearch.append("=");
	querySearch.append(EFGImportConstants.SEARCH_TAXON_TYPE);
   %>
	<title>Test Taxon Page Configuration</title>
	<link rel="stylesheet" href="<%=context%>/efg2web.css" type="text/css"/>
  </head>
   <body>
	  	<%@ include file="../EFGTableHeader.jsp"%>	
	  	<table class="frame" summary="">
				<tr>
					<td>
						<table class="directurl" summary="">					 
							<tr>
								<td colspan="5" class="title">The Datasource "<%=displayName%>" has been configured successfully!!!.<br/>
									<span class="subtitle"><a href="<%=querySearch.toString()%>"  target="TestWindow">Test taxon Page</a></span>
								</td>
							</tr>
							<tr>
						</table>
					</td>
				</tr>
		</table>		  	
  </body>
 </html>
