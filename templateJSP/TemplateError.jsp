<%@page language="java" %>

  <html>

    <!-- $Id$

    Handles situations where an error occurs during the configuration of a template

 -->

	<head>

      <%

      	String title = "An error occured during configuration of Template!!";

      	StringBuffer buf = new StringBuffer();

      	buf.append(title);
     	String message = (String)request.getAttribute("message");
	if(message != null){
		title = message;
		buf.append(message);
		buf.append("\n");	
	}

      	buf.append("Please report the error to the Electronic Field guide Group at efg'at'cs.umb.edu");


      %>		

	<title><%=title%></title>

	</head>

    <body bgcolor="#FDF5E6">

      <h2><%=title%></h2>

      <p><%=buf.toString()%></p> 

      <br/>

	<a href="index.html"> Go Back To Front Page</a>

     </body>

</html>


