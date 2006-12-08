<%@page language="java" %>

  <html>

    <!-- $Id$-->

	<head>

      <%

  
		String title ="Upload Results";
      StringBuffer buf = new StringBuffer();
	  String message = (String)request.getAttribute("Message");
      buf.append(message);
      %>		

		<title><%=title%></title>

	</head>

    <body bgcolor="#FDF5E6">

      <h2><%=title%></h2>

      <p><%=buf.toString()%></p> 

      <br/>

	

     </body>

</html>


