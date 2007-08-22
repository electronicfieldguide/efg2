<%@page import="java.util.Iterator,
project.efg.server.interfaces.ServletAbstractFactoryInterface,
project.efg.util.interfaces.EFGImportConstants
" %>
<%@page import="project.efg.server.servlets.EFGContextListener"%>
<%@page import="java.util.Map"%>
<%@page import="project.efg.server.factory.EFGSpringFactory"%>
<%@page import="project.efg.server.utils.ServletCacheManager"%>
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
			String forwardPage="NoDatasource.jsp";
		   	String context = request.getContextPath();
		   	Iterator listInter = null;   	
		   	Map map = ServletCacheManager.getDatasourceCache(mainTableName);
			listInter = map.keySet().iterator();
			boolean isEmpty = false;
			
			if(listInter != null){
		 		if(map.keySet().size() < 1){
			 		isEmpty = true;
		 		}
		 	}
		 	else{
		 		isEmpty = true;
		 	}
				
		%>
		<title>BookMaker - printable PDFs for your EFGs</title>
		<link href="../pdfcss/bookmaker.css" rel="stylesheet" type="text/css" />
		<link href="../css/popUpHypertext.css" rel="stylesheet" type="text/css"/>
		<link href="../pdfcss/comboBox.css" rel="stylesheet" type="text/css"/>

		<script language="JavaScript" src="../js/trimfunctions.js" type="text/javascript"></script>
		<script language="JavaScript" src="../pdfjavascripts/efg2_set.js" type="text/javascript"></script>

		<script language="JavaScript" src="../pdfjavascripts/efg2_validator.js" type="text/javascript"></script>
		<script language="JavaScript" src="../js/prototype1.5.js" type="text/javascript"></script>
		<script language="JavaScript" src="../js/OtherFunction.js" type="text/javascript"></script>

		<script language="JavaScript" src="../pdfjavascripts/efg2_ajaxfunctions.js" type="text/javascript"></script>
		<script language="JavaScript" src="../pdfjavascripts/ecritterz_biz_colorpicker.js" type="text/javascript"></script>
		<script language="JavaScript" src="../pdfjavascripts/efg2_otherfunctions.js" type="text/javascript"></script>
		<script language="JavaScript" src="../pdfjavascripts/searchpage.js" type="text/javascript"></script>
		
	</head>
	<body>
		<%@ include file="bookmakertop.html" %>
		<table>
			<tr>
				<td class="datalist">
					<input type="hidden" id="mainTableConstant" name="mainTableConstant" value="<%=mainTableConstant.toString()%>"/>	
					<input type="hidden" id="<%=EFGImportConstants.DATASOURCE_NAME%>" name="<%=EFGImportConstants.DATASOURCE_NAME %>" value=""/>
					<input type="hidden" id="<%=EFGImportConstants.DISPLAY_NAME%>" name="<%=EFGImportConstants.DISPLAY_NAME%>" value=""/>
					<input type="hidden" id="numberoftaxa" name="numberOfResults" value=""/>
					<input type="hidden" id="searchqueryresultsxml1" name="searchqueryresultsxml1" value=""/>
					<input type="hidden" id="searchquery" name="searchquery" value=""/>
					<%
					String choosedata =context + "/bookmaker/bookchoosedata.jsp";
					String designform = context + "/bookmaker/bookdesignform.jsp";				
					String datalisturl = context + "/bookmaker/bookdatalist.jsp";
					String querydata = context+ "/bookmaker/bookquerydata.jsp";
					StringBuffer configpagetype = new StringBuffer();
					configpagetype.append(EFGImportConstants.SEARCHTYPE);
					configpagetype.append( "=");
					configpagetype.append(EFGImportConstants.SEARCH_PDFS_BOOK_TYPE);
					
					%>
					<input type="hidden" id="choosedata" name="choosedata" value="<%=choosedata%>"/>
					<input type="hidden" id="designform" name="designform" value="<%=designform%>"/>
					<input type="hidden" id="datalisturl" name="datalisturl" value="<%=datalisturl%>"/>
					<input type="hidden" id="querydata" name="querydata" value="<%=querydata%>"/>
					<input type="hidden" id="configpagetype" name="querydata" value="<%=configpagetype.toString()%>"/>
					<input type="hidden" id="numberoftaxa" name="numberOfResults" value=""/>
				
					<input type="hidden" id="templateuniquename" name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>" value""/>
					<input type="hidden" id="efgsearchurl" name="efgsearchurl"   value="/efg2/search"/>
				    <div id="zerothdiv">				
						<select name="states" size="1" title="Select the datasource to configure" id="statesDS" onChange="JavaScript:choosedataResponse(this,$('choosedata').value);">
							<option>Select a Data source</option>
							<%
							while (listInter.hasNext()) { 
								String datasourceName= (String)listInter.next();
								String displayName = (String)map.get(datasourceName.toLowerCase());
							%>
							<option value="<%=datasourceName%>"><%=displayName%></option>
							<%}%>
						</select>
					</div>
				</td>
			</tr>
			<tr>
				<td class="datalist">
					<div id="firstdiv"></div>
				</td>
			</tr>
			
			<tr>
				<td class="datalist">
					<div id="seconddiv"></div>
				</td>
			</tr>
			<tr>
				<td class="datalist">
					<div id="thirddiv"></div>
				</td>
			</tr>
			<tr>
				<td class="datalist">
					<div id="popUpMessage" class="popUp"> Loading </div>
				</td>
			</tr>
			
		</table>
	<div id="deleteMessageID" style="display:none">
		
	</div>
	</body>
</html>
