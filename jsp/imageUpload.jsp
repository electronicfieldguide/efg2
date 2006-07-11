<%@page import="project.efg.util.EFGImportConstants" %>
<html>
<head>
  <title>Upload Test</title>
  <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<center>
<a href="BrowseUploadedImages.jsp" title="Click to view uploaded template images">Browse Template Images</a>
</center>
	<form name="myform" action="upload" method="post" enctype="multipart/form-data">
	<center>
			Upload Images To use on Template Pages. Image File size must not exceed <b>1MB:</b><br/>
			<b>Note:</b> That the entire request will be rejected by server if any file size is greater than 1MB<br/>
				Also, note that if a file with the same name already exists on the server, it will be <b>replaced.</b>
			<br/>
			<br/>
			<hr/>
			<input type="file" name="myimage1" size="100"/><br/>
		
			<input type="file" name="myimage2" size="100"/><br/>
		
			<input type="file" name="myimage3" size="100"/><br/>
			
			<input type="file" name="myimage4" size="100"/><br/>
			
			<input type="file" name="myimage5" size="100"/><br/>
			
			<input type="file" name="myimage6" size="100"/><br/>
		
			<input type="file" name="myimage7" size="100"/><br/>
			
			<input type="file" name="myimage8" size="100"/><br/>
			
			<input type="file" name="myimage9" size="100"/><br/>
			
			<input type="file" name="myimage10" size="100"/><br/>
			
			
			<br/>
       <input type="hidden" name="<%=EFGImportConstants.ITEMTYPE%>" value="<%=EFGImportConstants.templateImagesDirectory%>"/>
		<input type="submit" name="Submit" value="Submit your files"/>
		</center>
	</form>

</body>
</html>
