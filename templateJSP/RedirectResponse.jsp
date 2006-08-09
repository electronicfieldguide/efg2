<%@page import="
project.efg.util.EFGImportConstants,java.util.Enumeration
" %>
<%
	String templateName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
	String forwardPage= request.getParameter("forwardPage");
	String configType = request.getParameter(EFGImportConstants.CONFIG_TYPE);
	String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	request.setAttribute(EFGImportConstants.DATASOURCE_NAME,dsName);
    String displayName =  request.getParameter(dsName);
	request.setAttribute(EFGImportConstants.DISPLAY_NAME,displayName);

	String guid = null;
	String jspName = null;
	RequestDispatcher dispatcher = null;

	if((templateName != null)&& (!templateName.trim().equals(""))){
		//name exists go back to where you came from and say that the page already exists
		request.setAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME,templateName);
		guid =  request.getParameter(templateName);
		if((guid != null) && (!guid.trim().equals(""))){
			request.setAttribute(EFGImportConstants.GUID,guid);

			jspName = request.getParameter(guid);
			if((jspName != null) && (!jspName.trim().equals(""))){
				dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + jspName);
				dispatcher.forward(request, response);
			}
			else{
				dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
				dispatcher.forward(request, response);
			}
		}
		else{
			dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
			dispatcher.forward(request, response);
		}
	}
%>
<html>
	<head><title>Error..Template Name required</title></head>
	<body>
	<h2> ERROR!!!. A Template Name is Required.</h2>
	<a href="javascript:history.go(-1)">Please Go Back and Enter a Name For Your Template</a></h2>
	</body>
</html>
