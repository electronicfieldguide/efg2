<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.ServletAbstractFactoryInterface,
project.efg.servlets.factory.ServletAbstractFactoryCreator,
project.efg.util.EFGImportConstants,
project.efg.util.EFGDisplayObjectList,
project.efg.util.EFGDisplayObject
" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/transitional.dtd">
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
			ServletAbstractFactoryInterface servFactory = ServletAbstractFactoryCreator.getInstance();
			servFactory.setMainDataTableName(mainTableName);	
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
		<title>Plate Maker - printable PDFs for your EFGs</title>
		
		<link href="css/platemaker.css" rel="stylesheet" type="text/css" />
		<link href="../css/popUpHypertext.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
			.efgComboTxtBox
			{
			  position: relative;
			  top: 20px;
			  left: 5px;
			  width: 320px;
			  z-index: 5;
			}
			
			.efgComboDropDown
			{
			  position: relative;
			  top: 20px;
			  left: 5px;
			  width: 320px;
			  border: 0;
			}
			fieldset  {
				font: 0.8em "Helvetica Neue", helvetica, arial, sans-serif;
				color: #666;
				background-color: #efefef;
				padding: 2px;
				border: solid 1px #d3d3d3;
				width: 350px;
			}
			
			legend  {
				color: #666;
				font-weight: bold;
				font-variant: small-caps;
				background-color: #d3d3d3;
				padding: 2px 6px;
				margin-bottom: 8px;
			}
		
			label   {
				font-weight: bold;
				line-height: normal;
				text-align: right;
				margin-right: 10px;
				position: relative;
				display: block;
				float: left;
				width: 125px;
			}		
			label.fieldLabel	{
				display: inline;
				float: none;
			}	
			input.formInputText   {
				font-size: .8em;
				color: #666;
				background-color: #fee;
				
				width: 200px;
			}			
			input.formInputText:hover {
				background-color: #ccffff;
				border: solid 1px #006600;
				color: #000;
			}			
			input.formInputText:focus {
				color: #000;
				background-color: #ffffff;
				border: solid 1px #006600;
			}
			
			select.formSelect  {
				font-size: .8em;
				color: #666;
				background-color: #fee;
				padding: 2px;
				border: solid 1px #f66;
				margin-right: 5px;
				margin-bottom: 5px;
				cursor: pointer;
			}
		
			select.formSelect:hover  {
				color: #333;
				background-color: #ccffff;
				border: solid 1px #006600;
			}
			select.formSelect:focus  {
				color: #000;
				background-color: #ffffff;
				border: solid 1px #006600;
			}
		
			input.formInputButton   {
				font-size: 1.2em;
				vertical-align: middle;
				font-weight: bolder;
				text-align: center;
				color: #300;
				background: #f99 url(images/bg_button.png) repeat-x;
				padding: 1px;
				border: solid 1px #f66;
				cursor: pointer;
				float: right;
			}
				
			input.formInputButton:hover   {
				background-image: url(images/bg_button_hover.png);
			}
				
			input.formInputButton:active   {
				background-image: url(images/bg_button.png);
			}
		</style>
		<script language="JavaScript" src="../js/trimfunctions.js" type="text/javascript"></script>
		<script language="JavaScript" src="javascripts/efg2_set.js" type="text/javascript"></script>

		<script language="JavaScript" src="javascripts/efg2_validator.js" type="text/javascript"></script>
		<script language="JavaScript" src="../js/prototype1.5.js" type="text/javascript"></script>

		<script language="JavaScript" src="javascripts/efg2_ajaxfunctions.js" type="text/javascript"></script>
		<script language="JavaScript" src="javascripts/ecritterz_biz_colorpicker.js" type="text/javascript"></script>
		<script language="JavaScript" src="javascripts/efg2_otherfunctions.js" type="text/javascript"></script>
		<script language="JavaScript" src="javascripts/searchpage.js" type="text/javascript"></script>

	</head
	<body>
	<%@ include file="platemakertop.html" %>
		<table>
			<tr>
				<td class="datalist">
					<input type="hidden" id="mainTableConstant" name="mainTableConstant" value="<%=mainTableConstant.toString()%>"/>	
					<input type="hidden" id="<%=EFGImportConstants.DATASOURCE_NAME%>" name="<%=EFGImportConstants.DATASOURCE_NAME %>" value=""/>
					<input type="hidden" id="<%=EFGImportConstants.DISPLAY_NAME%>" name="<%=EFGImportConstants.DISPLAY_NAME%>" value=""/>
					<input type="hidden" id="numberoftaxa" name="numberOfResults" value=""/>
					<input type="hidden" id="searchqueryresultsxml1" name="searchqueryresultsxml1" value""/>
					<input type="hidden" id="searchquery" name="searchquery" value""/>
					
					<input type="hidden" id="templateuniquename" name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>" value""/>
				    <div id="zerothdiv">				
						<select name="states" size="1" title="Select the datasource to configure" id="statesDS" onChange="JavaScript:platechoosedataResponse(this);">
						<option>Select a Data source</option>
						<%
						Iterator dsNameIter = listInter.getIterator(); 
						while (dsNameIter.hasNext()) { 
							EFGDisplayObject obj = (EFGDisplayObject)dsNameIter.next();
							String displayName = obj.getDisplayName();
							String datasourceName = obj.getDatasourceName();
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
	<script language="JavaScript" type="text/javascript" src="wz_tooltip.js"/>
	</body>
</html>

