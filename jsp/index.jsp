<%@ page import = "java.io.*,java.util.*"%>

<HTML>

<HEAD>

<%

StringBuffer buf = new StringBuffer();

	buf.append(request.getScheme()); // http

	buf.append("://");

	buf.append(request.getServerName()); // hostname.com

	buf.append(":");

	buf.append(request.getServerPort()); // 80

%>

<META HTTP-EQUIV="refresh" CONTENT="5"; 

URL="<%=buf.toString() %>/efg2">

</HEAD>

<BODY>

<SCRIPT>

window.location.href = '<%=buf.toString()%>' + '/efg2';

</SCRIPT>

</BODY>

</HTML>



<HTML>
