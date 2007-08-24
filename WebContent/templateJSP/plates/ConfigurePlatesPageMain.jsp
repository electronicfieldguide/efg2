<%@page import="
project.efg.util.interfaces.EFGImportConstants
" %>
<%@ include file="../commonTemplateCode/commonConfigureJSPCode.jsp" %>
<html>
  <head>
    <title>Configuration of a Plate Template for <%=displayName%></title>
    <link href="../../css/templates.css" rel="stylesheet" type="text/css" />
  </head>
  <body bgcolor="#ffffff">
    <h2 align="center">Configuration of a Plate Template for <%=displayName%></h2>
  	<center>
  		<form name="configure" action="../commonTemplateCode/Redirect2Template.jsp">
  			Select a template to configure for your datasource:
      		<select name="<%=EFGImportConstants.TEMPLATE_NAME%>" title="Select a datasource from list below">
				<%
				if(!isGlossary){
				%>
		
				<option value="<%=SearchResultsTemplateBasic%>">Basic Thumbnail View</option>
				<option value="<%=SearchResultsTemplateMont1%>">Fancy Monteverde Style 1</option>
				<option value="<%=SearchResultsTemplateMont2%>">Fancy Monteverde Style 2</option>
 				<option value="<%=NantucketForestStyle%>">Nantucket Forest Style</option>
  				<option value="<%=NantucketNavyStyle%>">Nantucket Navy Style</option>
 				<option value="<%=NantucketNectarzStyle%>">Nantucket Nectarz Style</option>
 				<option value="<%=NantucketStyle%>">Nantucket Style</option> 				
 				<option value="<%=BananaRepublicStyle%>">Banana Republic Style</option>
 				<option value="<%=SummerStyle%>">Summer Style</option>
				<%} else{ %>
				
				<%} %>
    		</select>
    		<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=dsName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>" value="<%=templateName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
    		<input type="submit" value="Submit"/>
    	</form>
    	<br/><br/>
    	<hr/>
    	<%@ include file="platesSamples.jsp" %>
    	
  	</center>
  </body>
</html>
