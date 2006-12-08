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
	<head>
	<%@ include file="jspName.jsp" %>
	<%@ include file="allTableName.jsp"%>
		<%
       		String templateMatch ="Plate Template2";
			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			String xslFileName = "itomidsMimicPlates.xsl";
			EFGDataSourceHelperInterface dsHelper = new EFGDataSourceHelperInterface();
   
			String fieldName = null;
			String fieldValue = null;
               String groupLabel= null;
			String groupLabelValue = null;
			String characterLabelValue = null;
			String characterLabel = null;

			Iterator it =null;
			List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
		 	List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);
			int tableSize = table.size();
			TemplateProducer tp = new TemplateProducer();
			boolean isNew = true;
			boolean isOld = false;
			String name = null;
			int ii = 0;
			TemplatePopulator tpop = new  TemplatePopulator();
			StringBuffer fileName = new StringBuffer(realPath);
			fileName.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME); 
			fileName.append(File.separator);
			fileName.append(datasourceName.toLowerCase());
			fileName.append(EFGImportConstants.XML_EXT);
			Hashtable groupTable = tpop.populateTable(fileName.toString(), guid, EFGImportConstants.SEARCHPAGE_PLATES_XSL, datasourceName );
			if(groupTable == null){
				groupTable = new Hashtable();
			}

		    File imageFiles = new File(realPath + File.separator +  EFGImportConstants.templateImagesDirectory);
			File[] imageFileList = imageFiles.listFiles(); 
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
		<title>Plate 2 Configuration</title>
	</head>

