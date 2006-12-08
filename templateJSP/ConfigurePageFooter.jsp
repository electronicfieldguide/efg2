<%@page %>
<!-- $Id$ -->
<% 
String forwardPage="NoDatasource.jsp";
RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
dispatcher.forward(request, response);

%>