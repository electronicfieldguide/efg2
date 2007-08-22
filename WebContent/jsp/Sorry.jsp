<%@page language="java" %>
  <html>
    <!-- $Id: Sorry.jsp,v 1.1.1.1 2007/08/01 19:11:32 kasiedu Exp $
    Handles situations where an error occurs during the processing of a request
 -->
	<head>
	<%@ include file="Header.jsp"%>	
      <%
      String title = "An error occured during the processing of your request!!";
      StringBuffer buf = new StringBuffer();
      buf.append("Please report the error to the Electronic Field guide Group at efg'at'cs.umb.edu");
      %>		  
  	<title>The EFG Project - Electronic Field Guides</title>
	<link rel="stylesheet" href="efg2web.css" type="text/css"/>
	</head>
    <body>
    <%@ include file="EFGTableHeader.jsp"%>	
    	<table class="frame" summary="">
			<tr>
				<td>
					<table class="directurl" summary="">					 
						<tr>
							<td colspan="5" class="title"><%=title%><br/>
								<span class="subtitle"><%=buf.toString()%></span>
							</td>
						</tr>		
   					</table>
   				</td>
   			</tr>
   		</table>
     </body>
</html>

