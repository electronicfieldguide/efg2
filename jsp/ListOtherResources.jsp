<%@page import="project.efg.util.EFGImportConstants,java.io.File,project.efg.util.EFGUniqueID" %>

	<%
			String realPath = getServletContext().getRealPath("/");
			StringBuffer buffer = new StringBuffer();
			buffer.append(realPath);
			buffer.append(File.separator);
			
			String classname = "otherresources"; 
			
             String[] titles ={"Select css files to export","Select javascript files to export","Select images files to export"}; 
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
					%>
					<input class="<%=classname%>" id="<%=id%>" type="checkbox" name="<%=directories[i]+ "/" + filename%>" onclick="javascript:checkClick(this,'exportData');"/>
					<label for="<%=id%>"><%=filename%></label> <br/>
					<% 
				}
				%>	
				</fieldset>
			<%}
             }%>