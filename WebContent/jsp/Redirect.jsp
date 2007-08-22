<%@page language="java" %>
	<!--
	$Id: Redirect.jsp,v 1.1.1.1 2007/08/01 19:11:32 kasiedu Exp $
	-->
	<%
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/search");
		dispatcher.forward(request, response);
%>
