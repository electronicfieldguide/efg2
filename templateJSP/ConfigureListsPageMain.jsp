<%@page import="
project.efg.util.EFGImportConstants
" %>
<%@ include file="commonConfigureJSPCode.jsp" %>
<html>
  <head>
    <title>Configuration of a List Template for <%=displayName%></title>
  </head>
  <body bgcolor="#ffffff">
    <h2 align="center">Configuration of a List Template for <%=displayName%></h2>
  	<center>
  		<form name="configure" action="Redirect2Template.jsp">
  			Select a template to configure for your Datasource:
      		<select name="<%=EFGImportConstants.TEMPLATE_NAME%>" title="Select a template from list below">
				<option value="<%=lists1%>">List Template1</option>
				<option value="<%=lists2%>">List Template2</option>
    		</select>
    		<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=dsName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>" value="<%=templateName%>"/> 
    		<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
    		<input type="submit" value="Submit"/>
    	</form>
    	<br/><br/>
    	<hr/>
    	<h2>View Existing List Templates</h2>
    	<table>
    		<tr>
    			<td><a href="listTemplate1.html" target="list1">List Template1</a></td>
    		</tr>
    		<tr>
    			<td><a href="listTemplate2.html" target="list2">List Template2</a></td>
    		</tr>
    	</table>
  	</center>
  </body>
</html>
