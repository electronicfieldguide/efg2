<%@page language="java" %>  <html>    <!-- $Id$ -->	<!-- Empty database -->	<head>      <%      	String title = "The selected datasource no longer exists or the database is empty!!";      	StringBuffer buf = new StringBuffer();      	buf.append(title);		buf.append("\n");	      %>			<title><%=title%></title>	</head>    <body bgcolor="#FDF5E6">      <h2><%=title%></h2>      <p><%=buf.toString()%></p>       <br/>	<a href="index.html"> Go Back To Front Page</a>     </body></html>