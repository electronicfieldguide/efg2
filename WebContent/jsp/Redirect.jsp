<%@page language="java" %>
	<!--
	$Id$
	-->
	<%
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/search");
		dispatcher.forward(request, response);
%>
