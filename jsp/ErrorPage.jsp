<%@page language="java" %>   
<html>     
<!-- $Id$     Handles situations where an error occurs during the processing of a request  --> 	
<head>      
 <%       String title = "An error occured during the processing of your request!!";       
StringBuffer buf = new StringBuffer();       
buf.append("An error occured during the processing of your request!!\n");       
buf.append("Please report the error to the Electronic Field guide Group at efg'at'cs.umb.edu");       
%>		 		
<title><%=title%></title> 	
<link type="text/css" rel="stylesheet" href="searchpage.css">
</head>     
 <body BGCOLOR="#ffffff">
    <p class="title">><%=title%></p>      
<p class="nohits"><%=buf.toString()%></p>        
<br/> 	
<a href="javascript:history.back()"> Go back </a>      
</body> 
</html> 
