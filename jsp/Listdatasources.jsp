<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.ServletAbstractFactoryInterface,
project.efg.servlets.factory.ServletAbstractFactoryCreator,
project.efg.util.EFGImportConstants,
project.efg.util.EFGDisplayObjectList,
project.efg.util.EFGUniqueID,
project.efg.util.EFGDisplayObject
" %>

	  	<% 
	  		String title ="Select data to export"; 
	  		String classname = "datasources"; 
		   	String context = request.getContextPath();
		  	String mainTableName =request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
		  	
		  	if(mainTableName.equalsIgnoreCase(EFGImportConstants.EFG_GLOSSARY_TABLES)){
		  		title = "Select glossaries to export";
		  		classname = "glossaries"; 
		  		
		  		
		  	}

		  	
			ServletAbstractFactoryInterface servFactory = ServletAbstractFactoryCreator.getInstance();
			servFactory.setMainDataTableName(mainTableName);	
			EFGDisplayObjectList listInter = servFactory.getListOfDatasources();
			boolean isEmpty = false;
			if(listInter != null){
				if(listInter.getCount() < 1){
				isEmpty = true;
				}
			}
			else{
				isEmpty = true;
			}
	 
		if(!isEmpty){	 %>
		<fieldset>	
			<legend><%=title%></legend>
		<% 
		if(!isEmpty){
			Iterator dsNameIter = listInter.getIterator(); 
			String sampleDisplayName = 
				EFGImportConstants.EFGProperties.getProperty("EFG2SampleDisplayName");
			while (dsNameIter.hasNext()) { 
				EFGDisplayObject obj = (EFGDisplayObject)dsNameIter.next();
				String displayName = obj.getDisplayName();
				if(displayName.equalsIgnoreCase(sampleDisplayName)){
					continue;// don't display this
				}
				String datasourceName = obj.getDatasourceName();
				String id = "EFG_" + EFGUniqueID.getID();
				
			%>
			<input class="<%=classname%>" id="<%=id%>" type="checkbox" name="<%=datasourceName%>" onclick="javascript:checkClick(this,'exportData');"/>
			<label for="<%=id%>"><%=displayName%></label> <br/>
			<% 
			}//end while
		}
		%>	
		</fieldset>
		<%} %>
	
