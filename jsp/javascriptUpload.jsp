<%@page import="project.efg.util.EFGImportConstants" %>
<html>
<head>
  <title>Upload Test</title>
  <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<center>
<a href="BrowseUploadedJavascriptFiles.jsp" title="Click to view uploaded javascript files">Browse Javascript Files</a>
</center>
	<form name="myform" action="upload" method="post" enctype="multipart/form-data">
	<center>
			Upload Javascript files to use on Template Pages. Javascript File size must not exceed <b>1MB:</b><br/>
			<b>Note:</b> That the entire request will be rejected by server if any file size is greater than 1MB<br/>
				Also, note that if a file with the same name already exists on the server, it will be <b>replaced.</b>
			<br/>
			<br/>
			<hr/>
			<input type="file" name="myjs1" size="100"/><br/>
		
			<input type="file" name="myjs2" size="100"/><br/>
		
			<input type="file" name="myjs3" size="100"/><br/>
			
			<input type="file" name="myjs4" size="100"/><br/>
			
			<input type="file" name="myjs5" size="100"/><br/>
			
			<input type="file" name="myjs6" size="100"/><br/>
		
			<input type="file" name="myjs7" size="100"/><br/>
			
			<input type="file" name="myjs8" size="100"/><br/>
			
			<input type="file" name="myjs9" size="100"/><br/>
			
			<input type="file" name="myjs10" size="100"/><br/>
			
			
			<br/>
       <input type="hidden" name="<%=EFGImportConstants.ITEMTYPE%>" value="<%=EFGImportConstants.templateJavascriptDirectory%>"/>
		<input type="submit" name="Submit" value="Submit your files"/>
		</center>
	</form>

</body>
</html>
