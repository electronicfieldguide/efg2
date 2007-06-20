<%@page import="project.efg.searchableDocument.Searchable"%>
<%@page import="project.efg.searchableDocument.Searchables"%>
<%@page import="java.util.Iterator,
project.efg.servlets.efgInterface.EFGDataObjectListInterface,
project.efg.Imports.efgInterface.EFGDatasourceObjectInterface,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.Imports.efgImportsUtil.EFGTypeComparator,
project.efg.Imports.efgImportsUtil.MediaResourceTypeComparator,
project.efg.efgDocument.EFGType,
project.efg.efgDocument.ItemsType,
project.efg.servlets.efgInterface.EFGDataObject,
project.efg.efgDocument.MediaResourcesType,
project.efg.efgDocument.MediaResourceType,
project.efg.efgDocument.StatisticalMeasuresType,
project.efg.efgDocument.StatisticalMeasureType,
project.efg.efgDocument.EFGListsType,
project.efg.util.EFGImportConstants,
project.efg.util.EFGDocumentSorter,
project.efg.util.EFGListTypeSorter,
project.efg.util.EFGTypeSorter,
project.efg.util.MediaResourceTypeSorter,
project.efg.util.StatisticalMeasureTypeSorter
" %>
		<% 
		 response.setContentType("text/xml");
	   String displayName = request.getParameter(EFGImportConstants.DISPLAY_NAME); 
	   String datasourceName =request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	  
	   String context = request.getContextPath();
	   String tableName = request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
	   if(tableName == null || tableName.trim().equals("")){
		   tableName = EFGImportConstants.EFG_RDB_TABLES;
	   }
	   EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
	   dsHelper.setMainDataTableName(tableName);
	   EFGDataObjectListInterface doSearches = dsHelper.getSearchable(displayName,datasourceName);	
	   if(datasourceName == null){
		datasourceName=doSearches.getDatasourceName();		
	   }  
	   Searchables searchables = toXML(datasourceName,doSearches);
	   
	   try {
		   searchables.marshal(response.getWriter());

		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
	   %>
 <%!
 	public Searchables toXML( String datasourceName,
 			EFGDataObjectListInterface doSearches){
	 Searchables searchables = new Searchables();
	 searchables.setDatasourceName(datasourceName);
		for(int s = 0 ; s < doSearches.getEFGDataObjectCount(); s++){
			EFGDataObject searchable = doSearches.getEFGDataObject(s);
	   		
		 	String fieldName =searchable.getName();
			String legalName = searchable.getLegalName();
			int order = searchable.getOrder();
			Searchable searchable1 = new Searchable();
			searchable1.setDbFieldName(legalName);
			searchable1.setFieldName(fieldName);
			searchable1.setOrder(order);
			searchables.addSearchable(s,searchable1);
		}
	 return searchables;
 }
 %>


