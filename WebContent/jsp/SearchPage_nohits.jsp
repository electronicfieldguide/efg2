<%@page language="java" %>   
<html>     
<!-- $Id --> 	
	<head>
	<%@ include file="Header.jsp"%>	
      <%
      String title = "No Hits!!";
      StringBuffer buf = new StringBuffer();
      buf.append("Sorry, your search returned no results. Please broaden your search criteria and try again.");
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
								<a href="javascript:history.back()"> Go back To Search Page</a>
							</td>
						</tr>		
   					</table>
   				</td>
   			</tr>
   		</table>
     </body>
</html>
<html>
 



