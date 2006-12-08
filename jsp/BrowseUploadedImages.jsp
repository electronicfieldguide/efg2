<%@page import="project.efg.util.EFGImportConstants,java.io.File" %>
<html>
	<head>
	<!-- $Id$ -->
	<%
			String context = request.getContextPath();
		   String servletPath = context + "/" + EFGImportConstants.templateImagesDirectory+ "/"; 
			String realPath = getServletContext().getRealPath("/");
			StringBuffer buffer = new StringBuffer();
			buffer.append(realPath);
			buffer.append(File.separator);
			buffer.append(EFGImportConstants.templateImagesDirectory);
			String templateDir = buffer.toString();
			 File dir = new File(templateDir);
			 File[] files = dir.listFiles(); 
             int fileSize = files.length;
			String title = "Uploaded Template Images";
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
						if((i % 3) == 0){%>
							<tr>
								<%File file = files[i];
									String filename = file.getName();
								    String name = servletPath + filename;
								%>
								<td> <img src="<%=name%>"/><br/><%=filename%></td>
							<%
								if((i + 1) < fileSize){
									file = files[i + 1];
									filename = file.getName();
									name = servletPath + filename;
							%>
							<td> <img src="<%=name%>"/><br/><%=filename%></td>
								<%}
								  if((i + 2) < fileSize){
								   file = files[i + 2];
								   filename = file.getName();
								   name = servletPath + filename;
							%>
							<td> <img src="<%=name%>"/><br/><%=filename%></td>
							  <%}%>
							  </tr>
						<%
						}
				}%>
			</table>
			</center>
	</body>
</html>
