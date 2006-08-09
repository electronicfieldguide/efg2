<%@page import="
project.efg.util.EFGImportConstants
" %>
<% 
   String context = request.getContextPath();
   String dsName = (String)request.getAttribute(EFGImportConstants.DATASOURCE_NAME);
   String displayName = (String)request.getAttribute(EFGImportConstants.DISPLAY_NAME);
   String templateName = (String)request.getAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
	if(dsName == null){
		dsName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	}
	if(displayName == null){
		displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME);
	}
	if(templateName == null){
		templateName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME); 
	}
%>
<html>
  <head>
    <title>Configuration of a Plate Template for <%=displayName%></title>
  </head>
  <body bgcolor="#ffffff">
    <h2 align="center">Configuration of a Plate Template for <%=displayName%></h2>
  	<center>
  		<form name="configure" action="Redirect2Template.jsp">
  			Select a template to configure for your datasource:
      		<select name="<%=EFGImportConstants.TEMPLATE_NAME%>" title="Select a datasource from list below">
				<option value="commonNamePlate.jsp">Plate Template1</option>
				<option value="mimicPlate.jsp">Plate Template2</option>
				<option value="morningGloriesTemplatePlate.jsp">Plate Template3</option>
    		</select>
    		<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=dsName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>" value="<%=templateName%>"/>
    		<input type="submit" value="Submit"/>
    	</form>
    	<br/><br/>
    	<hr/>
    	<h2>View Existing Plate Templates</h2>
    	<table>
    		<tr>
    			<td><a href="plateTemplate1.html" target="plate1">Plate Template1</td>
    		</tr>
    		<tr>
    			<td><a href="plateTemplate2.html" target="plate2">Plate Template2</td>
    		</tr>
    		<tr>
    			<td><a href="plateTemplate3.html" target="plate3">Plate Template3</td>
    		</tr>
    	</table>
  	</center>
  </body>
</html>
