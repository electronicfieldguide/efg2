<%@page import="
project.efg.util.EFGImportConstants,
project.efg.util.TemplateConfigProcessor,
project.efg.util.GuidObject,
java.util.List,
java.util.Iterator,
java.util.Enumeration
" %>
<%@ include file="allTableName.jsp"%>
<% 
	String errorPage= "../ErrorPage.jsp";
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
	else{//forward to error page
		forwardPage =errorPage;
	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
	dispatcher.forward(request, response);
	}
  Iterator iter   = null;
  %>
<html>
	<head>
		<script type="text/javascript" language="JavaScript">
			<!--
			var fActiveMenu = false;
			var oOverMenu = false;
			
			function mouseSelect(e)
			{
				if (fActiveMenu)
				{
					if (oOverMenu == false)
					{
						oOverMenu = false;
						document.getElementById(fActiveMenu).style.display = "none";
						fActiveMenu = false;
						return false;
					}
					return false;
				}
				return true;
			}
			
			function menuActivate(idEdit, idMenu, idSel)
			{
				if (fActiveMenu) return mouseSelect(0);
			
				oMenu = document.getElementById(idMenu);
				oEdit = document.getElementById(idEdit);
				nTop = oEdit.offsetTop + oEdit.offsetHeight;
				nLeft = oEdit.offsetLeft;
				while (oEdit.offsetParent != document.body)
				{
					oEdit = oEdit.offsetParent;
					nTop += oEdit.offsetTop;
					nLeft += oEdit.offsetLeft;
				}
				oMenu.style.left = nLeft;
				oMenu.style.top = nTop;
				oMenu.style.display = "";
				fActiveMenu = idMenu;
				document.getElementById(idSel).focus();
				return false;
			}
			
			function textSet(idEdit, text)
			{
				document.getElementById(idEdit).value = text;
				oOverMenu = false;
				mouseSelect(0);
				document.getElementById(idEdit).focus();
			}
			
			function comboKey(idEdit, idSel)
			{
				if (window.event.keyCode == 13 || window.event.keyCode == 32)
					textSet(idEdit,idSel.value);
				else if (window.event.keyCode == 27)
				{
					mouseSelect(0);
					document.getElementById(idEdit).focus();
				}
			}
			function valid(idEdit) {
				txt = document.getElementById(idEdit).value ;
				if(txt == '') {
				  alert('You need to type your choice into the text box  or make a selection from the list');
				  return false;
				}
				return true;
			}
			document.onmousedown = mouseSelect;
		//-->
		</script>
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
		</p>
	
	</form>
</html>


