<%@page import="project.efg.util.EFGImportConstants,java.io.File,project.efg.util.EFGUniqueID" %>

	<%
			boolean isClassExists = true;
			String defaultclassname = "otherresources"; 
			String classname = request.getParameter("className");
			if(classname == null || 
					"".equals(classname.trim())){
				classname = defaultclassname;
				isClassExists = false;
			}
			String realPath = getServletContext().getRealPath("/");
			StringBuffer buffer = new StringBuffer();
			buffer.append(realPath);
			buffer.append(File.separator);
			
            String[] titles =new String[3]; 
	
			if(isClassExists){
	            titles[0] ="Select css files to delete";
	            titles[1] = "Select javascript files to delete";
	            titles[2]="Select images files to delete"; 
			}
			else{
	          titles[0] ="Select css files to export";
	          titles[1] = "Select javascript files to export";
	          titles[2]="Select images files to export"; 
			}
              String[] directories ={"templateCSSDirectory","templateJavascriptDirectory","templateImagesDirectory"}; 
			 
             
             
           
          for(int i = 0; i < titles.length; i++){
        	  StringBuffer currentBuffer = new StringBuffer(buffer.toString());
        	  currentBuffer.append(directories[i]);
        	  File dir = new File(currentBuffer.toString());
 			  File[] files = dir.listFiles(); 
              int fileSize = files.length;
              boolean isEmpty =true;
        	  if(fileSize > 0){
             	 isEmpty = false;
              }
             if(!isEmpty){
				%>
			
				<fieldset>	
					<legend><%=titles[i]%></legend>
				<% 
					for( int j = 0; j < fileSize; j++){
						File file = files[j];
						String filename = file.getName();
						String id = "Otherresource_" + EFGUniqueID.getID();				
					
					if(isClassExists){%>
						<input class="<%=classname%>" id="<%=id%>" type="checkbox" name="<%=directories[i]+ "/" + filename%>" onclick="javascript:checkClick(this,'deleteresources');"/>
					<%} else{ %>
						<input class="<%=classname%>" id="<%=id%>" type="checkbox" name="<%=directories[i]+ "/" + filename%>" onclick="javascript:checkClick(this,'exportData');"/>					
					<%} %>
					<label for="<%=id%>"><%=filename%></label> <br/>
					<% 
				}
				%>	
				</fieldset>
			<%}
             }%>