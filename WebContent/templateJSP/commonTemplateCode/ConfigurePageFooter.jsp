<%@page %>
<!-- $Id: ConfigurePageFooter.jsp,v 1.1.1.1 2007/08/01 19:11:39 kasiedu Exp $ -->
<% 
String forwardPage="NoDatasource.jsp";
RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
dispatcher.forward(request, response);

%>