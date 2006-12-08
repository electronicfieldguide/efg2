<%@page import="
project.efg.util.EFGImportConstants
" %>
<%@ include file="commonConfigureJSPCode.jsp" %>
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
				<option value="<%=plate1%>">Plate Template1</option>
				<option value="<%=plate2%>">Plate Template2</option>
				<option value="<%=plate3%>">Plate Template3</option>
    		</select>
    		<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=dsName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>" value="<%=templateName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
    		<input type="submit" value="Submit"/>
    	</form>
    	<br/><br/>
    	<hr/>
    	<h2>View Existing Plate Templates</h2>
    	<table>
    		<tr>
    			<td><a href="plateTemplate1.html" target="plate1">Plate Template1</a></td>
    		</tr>
    		<tr>
     			<td><a href="plateTemplate2.html" target="plate2">Plate Template2</a></td>
    		</tr>
    		<tr>
     			<td><a href="plateTemplate3.html" target="plate3">Plate Template3</a></td>
    		</tr>
    	</table>
  	</center>
  </body>
</html>
