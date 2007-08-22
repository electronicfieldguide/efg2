<%@page import="project.efg.util.interfaces.EFGImportConstants,
project.efg.util.interfaces.EFGImagesConstants,
java.io.File,project.efg.util.utils.EFGUniqueID" %>

	<%
			String realPath = getServletContext().getRealPath("/");
			StringBuffer buffer = new StringBuffer();
			buffer.append(realPath);
			buffer.append(File.separator);
			buffer.append(EFGImagesConstants.EFG_IMAGES_DIR);
			String classname = "mediaresources"; 
			 File dir = new File(buffer.toString());
			 File[] files = dir.listFiles(); 
		             int fileSize = files.length;
		             String title ="Select Media resources to export"; 
		           
		             boolean isEmpty =true;
		             
		             if(fileSize > 0){
		            	 isEmpty = false;
		             }
		          
		             if(!isEmpty){
	%>
			
				<fieldset>	
					<legend><%=title%></legend>
				<%
						for( int i = 0; i < fileSize; i++){
						File file = files[i];
						String filename = file.getName();
						String id = "Images_" + EFGUniqueID.getID();
				%>
					<input class="<%=classname%>" id="<%=id%>" type="checkbox" name="<%=filename%>" onclick="javascript:checkClick(this,'exportData');"/>
					<label for="<%=id%>"><%=filename%></label> <br/>
					<% 
				}
				%>	
				</fieldset>
			<%}%>