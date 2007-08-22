<%@ page language="java" %>
<%@page import="
project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.TemplateConfigProcessor,
project.efg.server.utils.GuidObject,
java.util.List,
java.util.Iterator,
java.util.Enumeration
" %>
<% 
	String platesPage= "plates/ConfigurePlatesPage.jsp";
	String listsPage="lists/ConfigureListsPage.jsp";
	String taxonPage= "taxon/ConfigureTaxonPage.jsp";
	String searchPage= "search/ConfigureSearchPage.jsp";
	String title = "Configure Page";
%>
<%@ include file="allTableName.jsp"%>
<% 
	String errorPage= "../../ErrorPage.jsp";
	String forwardPage="";
	
	String context = request.getContextPath();
	String configType = request.getParameter(EFGImportConstants.CONFIG_TYPE);
	String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME);
  
    String realPath = getServletContext().getRealPath("/");
    //make me dynamic
	TemplateConfigProcessor tcp = new TemplateConfigProcessor(dsName,configType);
	List list = tcp.getTemplateList();

	
	if(configType.equalsIgnoreCase(EFGImportConstants.SEARCH_PLATES_TYPE)){
			forwardPage = platesPage;
	}
	else if(configType.equalsIgnoreCase(EFGImportConstants.SEARCH_LISTS_TYPE)){
		forwardPage = listsPage;
	}
	 else if(configType.equalsIgnoreCase(EFGImportConstants.SEARCH_TAXON_TYPE)){
		forwardPage = taxonPage;
	}
	 else if(configType.equalsIgnoreCase(EFGImportConstants.SEARCH_SEARCH_TYPE)){
			forwardPage = searchPage;
		}
	else{//forward to error page
		forwardPage =errorPage;
	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
	dispatcher.forward(request, response);
	}
  Iterator iter   = null;
  %>
<html>
	<head>
	<script language="JavaScript" src="../../js/menuactive.js" type="text/javascript"></script>
	
		<title><%=title%></title>
	</head>
	<form action="RedirectResponse.jsp" method="post" name="myForm" onsubmit="return valid('templateUniqueName')">
		<p>Enter a unique name for the template or select from list to edit:
		  <input type="text" id="templateUniqueName" size="20" style="width:200" name="templateUniqueName"/>
		  <input type="button" hidefocus="1" value="&#9660;"
			style="height:23; width:22; font-family: helvetica;"
			onclick="JavaScript:menuActivate('templateUniqueName', 'combodiv', 'guid')"/>
			<div id="combodiv" style="position:absolute; display:none; top:0px;
				left:0px;z-index:10000" onmouseover="javascript:oOverMenu='combodiv';"
				onmouseout="javascript:oOverMenu=false;">
			 <select name="templateUniqueName" size="10" id="guid" style="width: 220; border-style: none"
				onclick="JavaScript:textSet('templateUniqueName',this.value);"
				onkeypress="JavaScript:comboKey('templateUniqueName', this);">
							<%
						iter = list.iterator();
						int ii = 0;
						while(iter.hasNext()) {
							GuidObject guidObject = (GuidObject)iter.next();
							String templateName =  guidObject.getDisplayName();
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
			<input type="hidden" name="<%=templateName%>" value="<%=guidN%>"/>
			<input type="hidden" name="<%=guidN%>" value="<%=jspName%>"/>
			<% 
			}	
			for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
				String fieldName = ((String) e.nextElement()).trim();
				String fieldValue = request.getParameter(fieldName);
			 %>
			<input type="hidden" name="<%=fieldName%>" value="<%=fieldValue%>"/>
			<% 
			}	
			%>
			<input type="hidden" name="forwardPage" value="<%=forwardPage%>"/>
			<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
		   <input type="submit" value="Submit"/>
	
	</form>
</html>