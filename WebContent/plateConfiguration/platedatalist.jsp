<%@page import="java.util.Iterator,java.util.ArrayList,
project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.TemplateConfigProcessor,
project.efg.server.utils.GuidObject,
java.util.List, java.util.Enumeration" %>
<%
	String configType = EFGImportConstants.SEARCH_PDFS_TYPE;
	String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME); 
    String realPath = getServletContext().getRealPath("/");
    String New_Value = "NEW_EFG_TAG";
    String New_Name = "NEW";
    //make me dynamic
   
	TemplateConfigProcessor tcp = new TemplateConfigProcessor(dsName,configType);
	List list = tcp.getTemplateList();
  	Iterator iter   = null;
%>		 
<input type="hidden" id="templateUniqueNamex" name="templateUniqueNamex" value=""/>
<div id="hide">

Select an Existing Template Or New:<br/>
<select name="templateUniqueNamex" id="guidx">
	<option value="<%=New_Value%>"><%=New_Name%></option>
	<%
		iter = list.iterator();
		int ii = 0;
		while(iter.hasNext()) {
			GuidObject guidObject = (GuidObject)iter.next();
			String templateName =  guidObject.getDisplayName();
			String guid= guidObject.getGuid();
	%>
		<option value="<%=templateName%>"><%=templateName%></option>	
	<%
		}
		%>
</select>
<br/>


<input type="submit" name="submit" value="Delete" onclick="JavaScript:deleteTemplate('guidx');"/>

<input type="submit" name="submit" id="datalistsubmitID" value="Go" onclick="JavaScript:datalistResponse('querydataid','guidx');"/>
</div>
<%
iter = list.iterator();
while(iter.hasNext()) {
	GuidObject guidObject = (GuidObject)iter.next();
	String guidN = guidObject.getGuid();
	String jspName = guidObject.getJSPName();
	String templateName =  guidObject.getDisplayName();
%>
	<input type="hidden" id="<%=templateName%>" name="<%=templateName%>" value="<%=guidN%>"/>
	<input type="hidden" name="<%=guidN%>" value="<%=jspName%>"/>
<% 
}	
%>		

