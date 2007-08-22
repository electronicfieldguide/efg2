<%@page import="project.efg.util.interfaces.EFGImportConstants" %>
<!-- $Id: commonJspTemplateCodeFooter.jsp,v 1.1.1.1 2007/08/01 19:11:39 kasiedu Exp $ -->
		<hr/>
		<br/><br/>
				<%
					
					fieldValue = (String)groupTable.get(EFGImportConstants.ISDEFAULT_STR);
					if(fieldValue == null){
						fieldValue = "false";
					}
					Boolean bool = new Boolean(fieldValue);
								
									
					%>
				<p>

			<select name="<%=EFGImportConstants.ISDEFAULT_STR%>"  title="Indicate whether this template should be the default for search results page">
				<option value="false">Do not use as Default Template</option>
				<%if(bool.booleanValue() ){
					%>
					 <option value="true" selected="selected">Use as Default Template</option>
					 <%} else{%>
					  <option value="true">Use as Default Template</option>
					<%}%>
			</select>
			</p><br/><br/>

		<input type="hidden"   name="<%=EFGImportConstants.DISPLAY_NAME%>"  value="<%=displayName%>"/>
				<input type="hidden"   name="<%=EFGImportConstants.DATASOURCE_NAME%>"  value="<%=datasourceName%>"/>
				<input type="hidden"   name="<%=EFGImportConstants.HTML_TEMPLATE_NAME%>"  value="<%=templateMatch%>"/>
				<input type="hidden"   name="<%=EFGImportConstants.XSL_STRING%>"  value="<%=xslFileName%>"/>
		<%if( guid != null){%>
		<input type="hidden"   name="<%=EFGImportConstants.GUID%>"  value="<%=guid%>"/>
		<%}%>
				<input type="hidden"   name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>"  value="<%=uniqueName%>"/>
				<input type="hidden"   name="<%=EFGImportConstants.JSP_NAME%>"  value="<%=jspName%>"/>	
				<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
			
			<input type="submit"  name="submit" value="Click to submit" align="middle" />	
			
	
		
			
		