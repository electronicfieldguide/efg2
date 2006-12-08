<%@page import="
project.efg.util.EFGImportConstants
" %>
<%@ include file="commonConfigureJSPCode.jsp" %>

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
				<option value="<%=taxonPageTemplate1%>">Taxon Page Template1</option>
				<option value="<%=taxonPageTemplate2%>">Taxon Page Template2</option>
				<option value="<%=taxonPageTemplate3%>">Taxon Page Template3</option>
				<option value="<%=taxonPageTemplate4%>">Taxon Page Template4</option>
				<option value="<%=taxonPageTemplate5%>">Taxon Page Template5</option>

    		</select>
    		<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=dsName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>" value="<%=templateName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
    		<input type="submit" value="Submit"/>
    	</form>
    	<br/><br/>
    	<hr/>
    	<h2>View Existing Taxon Page Templates</h2>
    	<table>
    		<tr>
    			<td><a href="TaxonPageTemplate1.html" target="tp1">Taxon Page Template1</a></td>
    		</tr>
    		<tr>
    			<td><a href="TaxonPageTemplate2.html" target="tp2">Taxon Page Template2</a></td>
    		</tr>
    		  <tr>
    			<td><a href="TaxonPageTemplate3.html" target="tp3">Taxon Page Template3</a></td>
    		</tr>
    		  <tr>
    			<td><a href="TaxonPageTemplate4.html" target="tp4">Taxon Page Template4</a></td>
    		</tr>
    		  <tr>
    		  
    			<td><a href="TaxonPageTemplate5.html" target="tp5">Taxon Page Template5</a></td>
    			
    		</tr>

    	</table>
  	</center>
  </body>
</html>

    