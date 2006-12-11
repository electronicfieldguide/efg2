<%@page import="
java.io.File,
java.util.List,
java.util.Iterator,
java.util.Hashtable,
project.efg.servlets.efgInterface.EFGDataSourceHelperInterface,
project.efg.util.EFGImportConstants,
project.efg.util.TemplateProducer,
project.efg.util.TemplatePopulator,
project.efg.Imports.efgInterface.EFGQueueObjectInterface
" %>
<%@ include file="allTableName.jsp"%>
		<%
			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
			Iterator it =null;
			List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
			List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);

			TemplatePopulator tpop = new  TemplatePopulator();
			String name = null;
            String groupLabel= null;
			String groupLabelValue = null;
			String characterLabelValue = null;
			String characterLabel = null;
			String fieldName = null;
			String fieldValue = null;
        	boolean isImagesExists = false;
			boolean isTableExists = false;
			int tableSize = table.size();
			TemplateProducer tp = new TemplateProducer();
			boolean isNew = true;
			boolean isOld = false;
			
			int ii = 0;
		
			 StringBuffer fileName = new StringBuffer(realPath);
			 fileName.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME); 
			 fileName.append(File.separator);
			 fileName.append(datasourceName.toLowerCase());
			 fileName.append(EFGImportConstants.XML_EXT);
			Hashtable groupTable = tpop.populateTable(fileName.toString(), guid, EFGImportConstants.SEARCHPAGE_PLATES_XSL, datasourceName );
			if(groupTable == null){
				groupTable = new Hashtable();
			}
		
			if((table != null) && (table.size() > 0)){
				isTableExists = true;	
			}

			if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){
				isImagesExists = true;	
			}
		    File cssFiles = new File(realPath + File.separator +  EFGImportConstants.templateCSSDirectory);
			File[] cssFileList = cssFiles.listFiles(); 
			String cssLocation = context + "/" + EFGImportConstants.templateCSSDirectory  + "/";
			 String cssFile ="nantucketGlossary.css";
		    if(!isTableExists){    	
	 			String forwardPage="NoDatasource.jsp";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
				dispatcher.forward(request, response);
		    }
		    File imageFiles = new File(realPath + File.separator +  EFGImportConstants.templateImagesDirectory);
			 File[] imageFileList = imageFiles.listFiles(); 

		%>		
			<title>EFG Glossary</title>
			<%
				name =tp.getCharacter(isNew,isNew);
				fieldValue = (String)groupTable.get(name);
				if(fieldValue == null){
					cssLocation =cssLocation + cssFile;
					fieldValue = cssFile;
				}
				else{
					cssLocation =cssLocation + fieldValue;
				}
				groupLabel= tp.getCurrentGroupLabel(name);
				groupLabelValue = (String)groupTable.get(groupLabel);
				if(groupLabelValue == null){
					groupLabelValue ="styles";
				}
			%>	
	<link rel="stylesheet" href="<%=cssLocation%>"/>	