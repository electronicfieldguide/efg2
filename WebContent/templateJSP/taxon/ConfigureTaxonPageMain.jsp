<%@page import="
project.efg.util.interfaces.EFGImportConstants
" %>
<%@ include file="../commonTemplateCode/commonConfigureJSPCode.jsp" %>

</html>
  <head>
    <title>Configuration of a Taxon Page Template for <%=displayName%></title>
  </head>
  <body bgcolor="#ffffff">
    <h2 align="center">Configuration of a Taxon Page Template for <%=displayName%></h2>
  	<center>
  		<form name="configure" action="Redirect2Template.jsp">
  			Select a template to configure for your Datasource:
      		<select name="<%=EFGImportConstants.TEMPLATE_NAME%>" title="Select a template from list below">
				<option value="<%=nantucket_old%>">Old Nantucket Style</option>
				<option value="<%=monteverder_light%>">Monteverde Light</option>
				<option value="<%=monteverde_dark%>">Monteverde Dark</option>
				<option value="<%=monteverder_dark_family%>">Monteverde Dark (Family-level)</option>
				<option value="<%=bog_page%>">Bog Style</option>
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
    	<%@ include file="taxonSamples.jsp" %>
	</center>
  </body>
</html>

    