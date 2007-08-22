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
	<head>
	<%@ include file="../commonTemplateCode/jspName.jsp" %>
	<%@ include file="../commonTemplateCode/allTableName.jsp"%>
		<%
			String templateMatch ="Search Results - Fancy Monteverde Style 2";
			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			String xslFileName = "/searchresults/plates/SearchResultsTemplateMont2.xsl";
			EFGDataSourceHelperInterface dsHelper = EFGSpringFactory.getDatasourceHelper();
		   
			String fieldName = null;
			String fieldValue = null;
		    
			Iterator it =null;
			List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
			List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);
			int tableSize = table.size();
			boolean isNew = true;
			boolean isOld = false;
			String name = null;
			int ii = 0;
			StringBuffer fileName = new StringBuffer(realPath);
			fileName.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME); 
			fileName.append(File.separator);
			fileName.append(datasourceName.toLowerCase());
			fileName.append(EFGImportConstants.XML_EXT);

			TemplatePopulator tpop = new  TemplatePopulator();
			Hashtable groupTable = tpop.populateTable(fileName.toString(), guid, 
					EFGImportConstants.SEARCHPAGE_PLATES_XSL, datasourceName,whichDatabase );
			if(groupTable == null){
				groupTable = new Hashtable();
			}
		               String groupLabel= null;
			String groupLabelValue = null;
			String characterLabelValue = null;
			String characterLabel = null;


				    File imageFiles = new File(realPath + File.separator +  EFGImportConstants.templateImagesDirectory);
			File[] imageFileList = imageFiles.listFiles(); 
			TemplateProducer tp = new TemplateProducer();
			boolean isImagesExists = false;
			boolean isTableExists = false;

			if((table != null) && (table.size() > 0)){
				isTableExists = true;	
			}

			if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){
				isImagesExists = true;	
			}
				    if(!isTableExists){    	
			 	String forwardPage="NoDatasource.jsp";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
				dispatcher.forward(request, response);
				    }
		%>
		<title><%=templateMatch%></title>
	</head>
