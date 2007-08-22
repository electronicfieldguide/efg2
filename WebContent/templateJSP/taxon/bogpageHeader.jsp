<%@page import="
java.io.File,
java.util.List,
java.util.ArrayList,

java.util.Iterator,
java.util.Hashtable,
project.efg.server.utils.EFGDataSourceHelperInterface,
project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.TemplateProducer,
project.efg.server.utils.TemplatePopulator,
project.efg.util.interfaces.EFGQueueObjectInterface
" %>
<%@page import="project.efg.server.factory.EFGSpringFactory"%>
<html>
	<head>
	<%@ include file="../commonTemplateCode/jspName.jsp" %>
	<%@ include file="../commonTemplateCode/allTableName.jsp"%>
		<%
			String context = request.getContextPath();
			String realPath = getServletContext().getRealPath("/");
			String xslFileName = "/taxon/BogStyle.xsl";
			
		    String templateMatch ="Taxon Page - Bog Style";
			EFGDataSourceHelperInterface dsHelper = EFGSpringFactory.getDatasourceHelper();
			StringBuffer fileName = new StringBuffer(realPath);
			 fileName.append(File.separator);
			 fileName.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME); 
			 fileName.append(File.separator);
			 fileName.append(datasourceName.toLowerCase());
			  fileName.append(EFGImportConstants.XML_EXT);
			String fieldName = null;
			String fieldValue = null;
			
		        	boolean isImagesExists = false;
			boolean isTableExists = false;
			boolean isListsExists = false;


			Iterator it =null;
			List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
			List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);
			List efgList = dsHelper.getEFGListsFields(displayName,datasourceName);

			TemplatePopulator tpop = new  TemplatePopulator();

			Hashtable groupTable = tpop.populateTable(fileName.toString(), 
					guid, EFGImportConstants.TAXONPAGE_XSL, datasourceName,whichDatabase );
			if(groupTable == null){
					groupTable = new Hashtable();
			}
			String groupKey = null;
		    String groupLabel= null;
			String groupLabelValue = null;
			String characterLabelValue = null;
			String characterLabel = null;
			String characterText = null;
			String characterValue = null;
			String characterText1= null;
			String characterValue1= null;
			String characterText2 = null;
			String characterValue2 = null;
			String groupText = null;
			String groupValue = null;

			int tableSize = table.size();
			int listSize = efgList.size();
			TemplateProducer tp = new TemplateProducer();
			boolean isNew = true;
			boolean isOld = false;
			String name = null;
			int ii = 0;
		            File imageFiles = new File(realPath + File.separator +  EFGImportConstants.templateImagesDirectory);
			 File[] imageFileList = imageFiles.listFiles(); 
			String defaultInfo ="Click on a name to view more information about that Taxon."; 
			if((table != null) && (table.size() > 0)){
				isTableExists = true;	
			}
			if((efgList != null) && (efgList.size() > 0)){
				isListsExists = true;	
			}
			if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){
				isImagesExists = true;	
			}

				    File cssFiles = new File(realPath + File.separator +  EFGImportConstants.templateCSSDirectory);
			File[] cssFileList = cssFiles.listFiles(); 
			String cssLocation = context + "/" + EFGImportConstants.templateCSSDirectory  + "/";
			 String cssFile = "bogstyle.css";
				    if(!isTableExists){    	
			 	String forwardPage="NoDatasource.jsp";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
				dispatcher.forward(request, response);
				    }

				//sort and iterate
		%>
		<title>Taxon Page Template</title>
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
			%>	
		<script language="JavaScript" src="<%=context%>/js/prototype1.5.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=context%>/templateJSP/javascripts/templates_ajax.js" type="text/javascript"></script>		

		<link rel="stylesheet" href="<%=cssLocation%>"/>
	</head>

