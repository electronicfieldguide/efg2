<%@page import="
java.io.File,
java.util.List,
java.util.Iterator,
java.util.Hashtable,
project.efg.server.utils.EFGDataSourceHelperInterface,
project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.TemplateProducer,
project.efg.server.utils.TemplatePopulator,
project.efg.util.interfaces.EFGQueueObjectInterface
" %>
<%@page import="project.efg.server.factory.EFGSpringFactory"%>
		<%@ include file="../commonTemplateCode/jspName.jsp" %>
		
		<%
		String xslFileName = "/glossary/ListViewTemplateGlossary.xsl";
		
        String templateMatch ="List View - Glossary Style";
        
		%>		

	<%@ include file="../commonTemplateCode/allTableName.jsp"%>
		<%

			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			EFGDataSourceHelperInterface dsHelper = EFGSpringFactory.getDatasourceHelper();
			dsHelper.setMainDataTableName(EFGImportConstants.EFG_GLOSSARY_TABLES);
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
			String configType = request.getParameter(EFGImportConstants.SEARCH_TYPE_STR);
			
			
			String searchT = EFGImportConstants.SEARCHPAGE_LISTS_XSL;
			if(EFGImportConstants.SEARCH_PLATES_TYPE.equals(configType)){
				searchT = EFGImportConstants.SEARCHPAGE_PLATES_XSL;
			}

			 StringBuffer fileName = new StringBuffer(realPath);
			 fileName.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME); 
			 fileName.append(File.separator);
			 fileName.append(datasourceName.toLowerCase());
			 fileName.append(EFGImportConstants.XML_EXT);
			Hashtable groupTable = 
				tpop.populateTable(
						fileName.toString(), guid, 
						searchT, datasourceName,whichDatabase );
			if(groupTable == null){
				groupTable = new Hashtable();
			}
				
			if((table != null) && (table.size() > 0)){
				isTableExists = true;	
			}

			if((mediaResourceFields != null) && 
					(mediaResourceFields.size() > 0)){
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
			<title><%=templateMatch%></title>
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