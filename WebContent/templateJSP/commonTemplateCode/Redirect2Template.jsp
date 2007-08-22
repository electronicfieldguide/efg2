<%@page import="
project.efg.util.interfaces.EFGImportConstants
" %>
<% 
	String context = request.getContextPath();
	 String forwardPage=request.getParameter(EFGImportConstants.TEMPLATE_NAME);
	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
	dispatcher.forward(request, response);
%>

