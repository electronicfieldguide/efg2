<%@page import="project.efg.util.interfaces.EFGImportConstants"%>
<%

request.setAttribute(EFGImportConstants.DISPLAY_FORMAT,EFGImportConstants.XML);
RequestDispatcher dispatcher = 
	getServletContext().getRequestDispatcher("/searchPageBuilder");
dispatcher.forward(request, response);

%>


