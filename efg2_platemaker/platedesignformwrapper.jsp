<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.EFGDataObjectListInterface,
project.efg.Imports.efgInterface.EFGDatasourceObjectInterface,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.servlets.efgInterface.EFGDataObject,
project.efg.util.EFGImportConstants,
java.util.List,
java.util.ArrayList,
java.util.Hashtable,
project.efg.util.TemplateProducer,
project.efg.util.TemplatePopulator,
project.efg.util.PDFGUIConstants,
java.util.Properties,
project.efg.Imports.efgInterface.EFGQueueObjectInterface,
java.io.*,
java.net.URLConnection,
java.net.HttpURLConnection,
project.efg.efgDocument.EFGDocument,
java.net.URL,
org.apache.commons.lang.StringEscapeUtils
" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/transitional.dtd">
<html>
	<head>
		<%
		String searchServletName =null;
		String ALL_TABLE_NAME1=null;
		String displayName1=null;
		String dataSourceName1=null;
		String guid1 = null;
		
		
		if(request.getParameter("searchServletName") != null){
			searchServletName = request.getParameter("searchServletName");
		}
		if(request.getParameter("ALL_TABLE_NAME") != null){
			ALL_TABLE_NAME1 = request.getParameter("ALL_TABLE_NAME");
		}	
		if(request.getParameter("displayName") != null){
			displayName1 = request.getParameter("displayName");
		}	
		if(request.getParameter("dataSourceName") != null){
			dataSourceName1 = request.getParameter("dataSourceName");
		}
		//String guid = request.getParameter(EFGImportConstants.GUID);  
		String uniqueName1 = "EFG_NEW";
		if(request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME) != null){
			uniqueName1 = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
		}
		
		

		//The name of the field of interest for example Species
		String fieldName1 = request.getParameter("fieldName");
		StringBuffer queryForPDFJSP = new StringBuffer();
		StringBuffer mainQuery= new StringBuffer(searchServletName);
		
		StringBuffer postQuery = new StringBuffer();
		postQuery.append("dataSourceName=");
		postQuery.append(dataSourceName1);
		postQuery.append("&");
		postQuery.append("displayName=");
		postQuery.append(displayName1);
		postQuery.append("&");
		postQuery.append("ALL_TABLE_NAME=");
		postQuery.append(ALL_TABLE_NAME1);
			
		
		queryForPDFJSP.append(postQuery.toString());
		StringBuffer currentTaxa = new StringBuffer();
		
		String numberOfResults1 = "100";		
		
		if(fieldName1 != null){
		String[] fieldValues = request.getParameterValues(fieldName1);
			numberOfResults1 = fieldValues.length + "";		
			for(int i = 0; i < fieldValues.length;i++){
				postQuery.append("&");
				postQuery.append(fieldName1);
				postQuery.append("=");
				postQuery.append(fieldValues[i]);
			}
		}
		
		queryForPDFJSP.append("&");
		queryForPDFJSP.append(PDFGUIConstants.numberOfResultsStr);
		queryForPDFJSP.append("=");
		queryForPDFJSP.append(numberOfResults1);
		
		postQuery.append("&");
		postQuery.append("displayFormat=XML");
		String searchqueryresultsxml1 =null;
		//String searchquery = mainQuery.toString() + postQuery.toString();
		String searchquery1 = mainQuery.toString() + queryForPDFJSP.toString();
		
		//queryForPDFJSP.append("&");
		//queryForPDFJSP.append(PDFGUIConstants.searchqueryStr );
		//queryForPDFJSP.append("=" );
		//queryForPDFJSP.append(StringEscapeUtils.escapeHtml(searchquery1 ));
		
		queryForPDFJSP.append("&");
		queryForPDFJSP.append(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
		queryForPDFJSP.append("=" );
		queryForPDFJSP.append(uniqueName1);
		
		URL url = new URL(mainQuery.toString());
	    URLConnection conn = null;
	     BufferedReader in= null;
	        try
	        {

	            boolean redirected = false;
	            int count = 0;

	            do {
	                conn = url.openConnection();
	            	conn.setDoOutput(true);
	            	//conn.addRequestProperty("User-Agent", USER_AGENT);
		          //  conn.addRequestProperty("Accept", "application/xml, text/xml, */*");

	            	OutputStreamWriter out1 = new OutputStreamWriter(
	                                          conn.getOutputStream());
	            	out1.write(postQuery.toString());
	            	out1.close();

	            	
	               	 if (conn instanceof HttpURLConnection)
	                {
	                    HttpURLConnection httpcon = (HttpURLConnection)conn;
	                    int code = httpcon.getResponseCode();
	                    redirected = (code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_MOVED_TEMP);
	                    if (redirected && count > 5)
	                        redirected = false;

	                    if (redirected)
	                    {
	                        String newLocation = httpcon.getHeaderField("Location");
	                        if (newLocation == null)
	                            redirected = false;
	                        else
	                        {
	                            url = new URL(newLocation);
	                            count ++;
	                        }
	                    }
	                }
	            } while (redirected);
	            try{
		        	in = new BufferedReader(
	        				new InputStreamReader(
	        				conn.getInputStream()));
	        		project.efg.efgDocument.EFGDocument	efgDoc =
					(project.efg.efgDocument.EFGDocument)
					EFGDocument.unmarshalEFGDocument(in);		
	        		if(efgDoc != null){
	        			StringWriter writer = new StringWriter();
	        			efgDoc.marshal(writer);
	        			searchqueryresultsxml1 = StringEscapeUtils.escapeXml(writer.getBuffer().toString());
	        		}
        		}
        		catch(Exception ee){
        			if (in != null)
	                in.close();
        		}
	        }
	        finally
	        {
	            if (in != null)
	                in.close();
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
	<script type="text/javascript"><!--
	function getPDFConfigPage(){
	  var query = document.getElementById("postQuery").value;
	  getDesignForm(query);
	}
	//--></script>
	
	</head>
	<body onload="getPDFConfigPage();">
	<input type="hidden" name ="searchqueryresultsxml_1" id="searchqueryresultsxml1" value="<%=searchqueryresultsxml1%>"/>
	<input type="hidden" name="query" id="postQuery" value="<%=queryForPDFJSP.toString()%>"/>
	<div id="seconddiv">
		
	</div>
	<script language="JavaScript" type="text/javascript" src="wz_tooltip.js"/>
	</body>
</html>

