<%@page import="java.util.Iterator,
project.efg.server.interfaces.ServletAbstractFactoryInterface,
project.efg.util.interfaces.EFGImportConstants,
project.efg.util.utils.EFGUniqueID" %>

<%@page import="project.efg.server.servlets.EFGContextListener"%>
<%@page import="java.util.Map"%>
<%@page import="project.efg.server.utils.ServletCacheManager"%>
<%
	  String title ="Select data to export"; 
	  String classname = "datasources"; 
	  String context = request.getContextPath();
	  String mainTableName =request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
	  Iterator listInter = null;
	  if(mainTableName.equalsIgnoreCase(EFGImportConstants.EFG_GLOSSARY_TABLES)){
	  	title = "Select glossaries to export";
	  	classname = "glossaries"; 
	  }
 	  Map map = ServletCacheManager.getDatasourceCache(mainTableName);
 	  listInter = map.keySet().iterator();
  	  	
  		boolean isEmpty = false;
  		if(listInter != null){
		 	if(listInter != null){
			 		if(map.keySet().size() < 1){
 			 			isEmpty = true;
			 		}
			 }
			 else{
			 	isEmpty = true;
			}
  			if(!isEmpty){
 		%>
		<fieldset>	
			<legend><%=title%></legend>
		<%
			if(!isEmpty){
			
			String sampleDisplayName = 
				EFGImportConstants.EFGProperties.getProperty("EFG2SampleDisplayName");
			while (listInter.hasNext()) { 
				String datasourceName = (String)listInter.next();
				String displayName = (String)map.get(datasourceName.toLowerCase());

				if(displayName.equalsIgnoreCase(sampleDisplayName)){
					continue;// don't display this
				}
				
				String id = "EFG_" + EFGUniqueID.getID();
		%>
			<input class="<%=classname%>" id="<%=id%>" type="checkbox" name="<%=datasourceName%>" onclick="javascript:checkClick(this,'exportData');"/>
			<label for="<%=id%>"><%=displayName%></label> <br/>
			<% 
			}//end while
		}
		%>	
		</fieldset>
		<%} 
		}%>