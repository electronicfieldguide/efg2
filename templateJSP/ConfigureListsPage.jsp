<%@page import="
project.efg.util.EFGImportConstants
" %>
<% 
   String context = request.getContextPath();
   String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME);
   String displayName = request.getParameter(dsName);
%>
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
				<option value="commonNameList.jsp">List Template1</option>
				<option value="sciNameList.jsp">List Template2</option>
    		</select>
    		<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=dsName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
    		<input type="submit" value="Submit"/>
    	</form>
    	<br/><br/>
    	<hr/>
    	<h2>View Existing List Templates</h2>
    	<table>
    		<tr>
    			<td><a href="listTemplate1.html" target="list1">List Template1</td>
    		</tr>
    		<tr>
    			<td><a href="listTemplate2.html" target="list2">List Template2</td>
    		</tr>
    	</table>
  	</center>
  </body>
</html>
