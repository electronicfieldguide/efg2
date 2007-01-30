<%@page import="java.util.Iterator,
project.efg.util.EFGImportConstants,
project.efg.util.TemplateConfigProcessor,
project.efg.util.GuidObject,
java.util.List, java.util.Enumeration" %>
<%
	String configType = EFGImportConstants.SEARCH_PDFS_TYPE;
	String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME); 
    String realPath = getServletContext().getRealPath("/");
    //make me dynamic
	TemplateConfigProcessor tcp = new TemplateConfigProcessor(dsName,configType);
	List list = tcp.getTemplateList();
  	Iterator iter   = null;

	%>
		
		 
		 
		  <input type="text" id="templateUniqueNamex" 
		  size="20" style="width:200" 
		  class="formInputText" name="templateUniqueNamex" 
		  onfocus="clearField(this)" 
		  value="Enter or select a name" title="Enter or select a name" 
		  onmouseover="return escape(this.title);" />
		  <input type="button" hidefocus="1" value="&#9660;"
			style="height:23; width:22; font-family: helvetica;"
			 onclick="JavaScript:menuActivate('templateUniqueNamex', 'combodivC', 'guidx')"/>
			<div id="combodivC" style="position:relative; display:none; top:0px;
				left:0px;z-index:10000" onmouseover="javascript:oOverMenu='combodivC';"
				onmouseout="javascript:oOverMenu=false;">
			 <select name="templateUniqueNamex" size="10" id="guidx" style="width: 220; border-style: none"
				onclick="JavaScript:textSetCombo('templateUniqueNamex',this.value);"
				onkeypress="JavaScript:comboKey('templateUniqueNamex', this);">
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
       <br/> <br/>
        <br/> <br/>
    <input type="submit" name="submit" value="Delete" onclick="JavaScript:deleteTemplate('templateUniqueNamex');"/>
    <input type="submit" name="submit" value="Go" onclick="JavaScript:platedatalistResponse('platequerydata');"/>

