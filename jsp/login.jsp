<!DOCTYPE HTML PUBLIC "-//W3C//DTD html 4.0 transitional//EN">
<html>
	<head>
	 	<%@ include file="Header.jsp"%>	
		<title>The EFG Project - Electronic Field Guides</title>
		<link rel="stylesheet" href="efg2web.css" type="text/css"/>
	</head>
	<body>
		<%@ include file="EFGTableHeader.jsp"%>	
		<table class="frame">
			<tr>
				<td>		
					<table class="main">
						<tr>
							<td colspan="2" class="logintitle">You must log in to access this resource.
							</td>
						</tr>
						<form action="j_security_check" method="post">
								<tr>
									<td class="logintext">User name: <input type="text" name="j_username"/></td>
								</tr>
								<tr>
									<td class="logintext">Password:&nbsp;&nbsp; <input type="password" name="j_password"/></td>
								</tr>
								<tr>
									<td colspan="2" class="loginbutton"><input type="submit" value="Log In"/></td>
								</tr>
						</form>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>