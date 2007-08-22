<%@page import="java.io.File, java.util.List, java.util.ArrayList" %>
<%	
	String realPath = getServletContext().getRealPath("/");
	String[] otherresources = request.getParameterValues("deleteresource");

	
	StringBuffer buffer = new StringBuffer();
	buffer.append(realPath);
	buffer.append(File.separator);
	List deletedFiles = new ArrayList();
	List undeletedFiles = new ArrayList();
	boolean noFiles = true;
	if(otherresources != null){
		for(int i = 0; i < otherresources.length; i++){
			StringBuffer cbuffer = new StringBuffer(buffer.toString());
			cbuffer.append(File.separator);
			cbuffer.append(otherresources[i]);
			File file = new File(cbuffer.toString());
			if(file.exists()){
				noFiles = false;
				String fullPath = file.getAbsolutePath();
				String fileName = file.getName() ;
				try{
					file.delete();
					application.log("Deleted File: " + fullPath); 
					application.log("File Name: " + fileName);
					deletedFiles.add(fileName);
				}
				catch(Exception ee){
					application.log("Could not delete file: " + fullPath);
					application.log("File Name: " + fileName);
					application.log(ee.getMessage());
					undeletedFiles.add(fileName);
				}
			}
			
		}
	}
%>
<div>
	<%if(noFiles){ %>
		<div id="noFiles">No files were selected.</div> 
	<%} 
	else{
		if(deletedFiles.size() > 0){%>
		<div id="deletedFiles"> The following files deleted: 		
			<ul>
			<%for (int ii = 0; ii< deletedFiles.size();ii++){ %>
			<li><%=(String)deletedFiles.get(ii)%></li>		
			<%} %>
			</ul>
		</div>
		<%} 
		 if(undeletedFiles.size() > 0){%>
			<div id="undeletedFiles"> Unable to delete the following:		
				<ul>
					<%for (int jj = 0; jj< undeletedFiles.size();jj++){ %>
					<li><%=(String)undeletedFiles.get(jj)%></li>		
					<%} %>
				</ul>
			</div>
		<%} 	
	}%>
</div>