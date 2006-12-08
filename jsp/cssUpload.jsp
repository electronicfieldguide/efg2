<%@page import="project.efg.util.EFGImportConstants" %>
<html>
<head>
  <title>Upload Test</title>
  <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<center>
<a href="BrowseUploadedCSSFiles.jsp" title="Click to view uploaded css files">Browse Template CSS Files</a>
</center>
	<form name="myform" action="upload" method="post" enctype="multipart/form-data">
	<center>
			Upload CSS files to use on Template Pages. CSS File size must not exceed <b>1MB:</b><br/>
			<b>Note:</b> That the entire request will be rejected by server if any file size is greater than 1MB<br/>
				Also, note that if a file with the same name already exists on the server, it will be <b>replaced.</b>
			<br/>
			<br/>
			<hr/>
			<input type="file" name="mycss1" size="100"/><br/>
		
			<input type="file" name="mycss2" size="100"/><br/>
		
			<input type="file" name="mycss3" size="100"/><br/>
			
			<input type="file" name="mycss4" size="100"/><br/>
			
			<input type="file" name="mycss5" size="100"/><br/>
			
			<input type="file" name="mycss6" size="100"/><br/>
		
			<input type="file" name="mycss7" size="100"/><br/>
			
			<input type="file" name="mycss8" size="100"/><br/>
			
			<input type="file" name="mycss9" size="100"/><br/>
			
			<input type="file" name="mycss10" size="100"/><br/>
			
			
			<br/>
       <input type="hidden" name="<%=EFGImportConstants.ITEMTYPE%>" value="<%=EFGImportConstants.templateCSSDirectory%>"/>
		<input type="submit" name="Submit" value="Submit your files"/>
		</center>
	</form>

</body>
</html>
