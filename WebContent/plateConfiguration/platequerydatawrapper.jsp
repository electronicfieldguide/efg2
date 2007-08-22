<%@page import="project.efg.server.servlets.EFGContextListener"%>
<%@page import="java.util.Map"%>
<%@page import="project.efg.server.utils.ServletCacheManager"%>
<html>
	<head>
	<%
   
	   
		String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME);
		String dspName = request.getParameter(EFGImportConstants.DISPLAY_NAME);
		String tspAllTable = request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
		
		if(tspAllTable == null){
			tspAllTable = EFGImportConstants.EFG_RDB_TABLES;
		}
		String tspName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
		String tspGuid =  request.getParameter(EFGImportConstants.GUID);

		  if(dspName == null || "".equals(dspName)){
				   	Map map = ServletCacheManager.getDatasourceCache( tspAllTable );


				   	dspName = (String)map.get(dsName.toLowerCase());
				}			

		String mainTableConstant = EFGImportConstants.ALL_TABLE_NAME + "=" + tspAllTable;
		
	%>
	<title>Plate Maker - printable PDFs for your EFGs</title>
	<link href="css/platemaker.css" rel="stylesheet" type="text/css">
	<link href="../css/popUpHypertext.css" rel="stylesheet" type="text/css">
	<link href="/css/comboBox.css" rel="stylesheet" type="text/css">
	<script language="JavaScript" src="../js/trimfunctions.js" type="text/javascript"></script>

	<script language="JavaScript" src="javascripts/efg2_set.js" type="text/javascript"></script>

	<script language="JavaScript" src="javascripts/efg2_validator.js" type="text/javascript"></script>
	<script language="JavaScript" src="../js/prototype1.5.js" type="text/javascript"></script>

	<script language="JavaScript" src="javascripts/efg2_ajaxfunctions.js" type="text/javascript"></script>
	<script language="JavaScript" src="javascripts/ecritterz_biz_colorpicker.js" type="text/javascript"></script>

	<script language="JavaScript" src="javascripts/efg2_otherfunctions.js" type="text/javascript"></script>
	<script language="JavaScript" src="javascripts/searchpage.js" type="text/javascript"></script>
	</head>
	<body>
	
		<table class="menubartitle" summary="c">
			<tr>
				<td class="menubartitle">Plate Maker:</td>
				<td class="menubarsubtitle">printable pdfs for your EFGs</td>
			</tr>
		</table>
		<table class="menubarcase" summary="d">
			<tr>
				<td>
					<table class="menubar" summary="s">
						<tbody><tr>
							<td id="choosedataid" class="menubartext">
								<a href="/efg2/plateConfiguration/platechoosedata.jsp">Select<br>Datasource</a>
							</td>
		
							<td class="menubaricon">
								<img src="images/bluearrow.png" alt="arrow" border="0" height="35" width="35">
							</td>
							<td id="querydataid" class="menubartextactive">
								<a href="javascript:void(0)" onclick="JavaScript:datalistResponse('querydataid');">Query<br>Data</a>
							</td>
							<td class="menubaricon">
								<img src="images/bluearrow.png" alt="arrow" border="0" height="35" width="35">
		
							</td>
							<td class="menubartext" id="platedesignform">
								<a href="javascript:void(0)" onclick="javascript:postSearch('searchPageID','designformid','form_search_id');">Design the<br>Plate</a>
							</td>
							<td class="menubaricon">
								<img src="images/bluearrow.png" alt="arrow" border="0" height="35" width="35">
							</td>
							<td id="saveorprintid" class="menubartext"><a href="javascript:void(0)" onclick="javascript:saveConfig('pdfFillFormID');">Save<br>Configuration</a></td>
		
						</tr>
					</tbody></table>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td class="datalist">
				<!-- Constants -->
					<input id="numberoftaxa" name="numberOfResults" value="" type="hidden">
					<input id="searchqueryresultsxml1" name="searchqueryresultsxml1" value="" type="hidden">
					<input id="searchquery" name="searchquery" value="" type="hidden">
					<input id="mainTableConstant" name="mainTableConstant" value="<%=mainTableConstant%>" type="hidden">	
					<input id="dataSourceName" name="dataSourceName" value="<%=dsName%>" type="hidden">
					<input id="displayName" name="displayName" value="<%=dspName%>" type="hidden">
					<input id="templateuniquename" name="templateUniqueName" value="<%=tspName%>" type="hidden">
				    <div id="zerothdiv">				
					</div>
				</td>
			</tr>			
			<tr>
				<td class="datalist">
					<div id="firstdiv">
					<!-- Variables -->
						<input id="templateUniqueNamex" name="templateUniqueNamex" value="<%=tspName%>" type="hidden"/>
						<input id="<%=tspName%>" name="<%=tspName%>" value="<%=tspGuid%>" type="hidden"/>
						<input name="<%=tspGuid%>" value="platedesignform.jsp" type="hidden"/>
						<div id="hide"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td class="datalist">
					<div id="seconddiv">
					<!-- Insert forms here -->
						<%@ include file="platequerydata.jsp"%>	
					</div>
				</td>
			</tr>
			<tr>
				<td class="datalist">
					<div id="thirddiv"></div>
				</td>
			</tr>
			<tr>
				<td class="datalist">
					<div style="left: 10px; top: 30px; visibility: hidden;" id="popUpMessage" class="popUp">Please wait while your request is processed..</div>

				</td>
			</tr>
			
		</table>
		<div id="deleteMessageID" style="display: none;">
		</div>
								<%
					String plateChoosedata =context + "/plateConfiguration/platechoosedata.jsp";
					String designform = context + "/plateConfiguration/platedesignform.jsp";				
					String datalisturl = context + "/plateConfiguration/platedatalist.jsp";
					String querydata = context+ "/plateConfiguration/platequerydata.jsp";
					String CONFIGURE_PAGE_TYPE="searchType=pdfs";
					%>
					
					<input type="hidden" id="choosedata" name="choosedata" value="<%=plateChoosedata%>"/>
					<input type="hidden" id="designform" name="designform" value="<%=designform%>"/>
					<input type="hidden" id="datalisturl" name="datalisturl" value="<%=datalisturl%>"/>
					<input type="hidden" id="querydata" name="querydata" value="<%=querydata%>"/>
					<input type="hidden" id="configpagetype" name="querydata" value="<%=CONFIGURE_PAGE_TYPE%>"/>
		
		<script language="JavaScript" type="text/javascript" src="wz_tooltip.js"></script>
	</body>
</html>