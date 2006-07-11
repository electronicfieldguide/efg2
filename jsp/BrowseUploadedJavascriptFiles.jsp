<%@page import="project.efg.util.EFGImportConstants,java.io.File" %>
<html>
	<head>
	<!-- $Id$ -->
		<%
			String context = request.getContextPath();
			String servletPath = context + "/" + EFGImportConstants.templateJavascriptDirectory+ "/"; 
			String realPath = getServletContext().getRealPath("/");
			StringBuffer buffer = new StringBuffer();
			buffer.append(realPath);
			buffer.append(File.separator);
			buffer.append(EFGImportConstants.templateJavascriptDirectory);
			String templateDir = buffer.toString();
			File dir = new File(templateDir);
			File[] files = dir.listFiles(); 
            int fileSize = files.length;
			String title = "Uploaded Javascript Files";
		%>
		<title><%=title%></title>
	</head>
	<body>
		<center>
			<h2><%=title%></h2>
			<hr/>
				<table>
					<%
						for( int i = 0; i < fileSize; i++){
							File file = files[i];
							String filename = file.getName();
							String name = servletPath + filename;
					%>
					<tr>
						<td> 
							<a target="_blank" href="<%=name%>">
								<font color="#bb000f">
									<code><%=filename%></code>
								</font>
							</a>
						</td>
					</tr>
					<%
						}
					%>
				</table>
		</center>
	</body>
</html>
