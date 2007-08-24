<%@ page language="java" %>
<html>
  <head>

  <%@ include file="../commonTemplateCode/commonConfigureJSPCode.jsp" %>
  
    <title>Configuration of a Search Page Template for <%=displayName%></title>
    	<link href="../../css/templates.css" rel="stylesheet" type="text/css" />
    
  </head>
  <body bgcolor="#ffffff">
    <h2 align="center">Configuration of a Search Page Template for <%=displayName%></h2>
  	<center>
  	  <%@ include file="SearchCSSPage.jsp" %>
  		<form name="configure" action="<%=context%>/templateJSP/search/search.jsp">
  			Select a template to configure for your Datasource:
      		<select name="cssFile" title="Select a template from list below">
				<option value="<%=nantucketskinforest%>">Nantucket Forest Style</option>
				<option value="<%=nantucketskinnavy%>">Nantucket Navy Style</option>
				<option value="<%=nantucketskinnectar%>">Nantucket Nectarz Style</option>
				<option value="<%=nantucketskin%>">Nantucket Style</option>
				<option value="<%=bncskinbananarepub%>">Banana Republic Style</option>
				<option value="<%=bncskinjennsyard%>">Summer Style</option>
    		</select>
    		<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=dsName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>" value="<%=templateName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
    		<input type="submit" value="Submit"/>
    	</form>
    	<br/><br/>
    	<hr/>
    	<%@ include file="searchSamples.jsp" %>
	</center>
  </body>
</html>
