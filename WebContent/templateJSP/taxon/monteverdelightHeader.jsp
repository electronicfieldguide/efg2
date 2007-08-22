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
					String templateMatch ="Taxon Page - Monteverde Light";
					String context = request.getContextPath();
					String realPath = getServletContext().getRealPath("/");
				           
					String xslFileName = "/taxon/MonteverdeLight.xsl";
					String fieldName = null;
					String characterText = null;
					String characterValue = null;
					String fieldValue = null;
					String catNarrativeName=null;
					String listsName = null;
					EFGDataSourceHelperInterface dsHelper = EFGSpringFactory.getDatasourceHelper();
					Iterator it =null;

					List table = dsHelper.getTaxonPageFields(displayName,datasourceName);
					List mediaResourceFields = dsHelper.getMediaResourceFields(displayName,datasourceName);
					List efgList = dsHelper.getEFGListsFields(displayName,datasourceName);

					boolean isListsExists = false;
					boolean isImagesExists = false;
					boolean isTableExists = false;
					if((efgList != null) && (efgList.size() > 0)){
						isListsExists = true;	
					}
					if((table != null) && (table.size() > 0)){
						isTableExists = true;	
					}

					if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){
						isImagesExists = true;	
					}
					String groupLabel= null;
					String groupLabelValue = null;
					String characterLabelValue = null;
					String characterLabel = null;

					int numberOfImagesPerRow = 2;
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
					Hashtable groupTable = 
						tpop.populateTable(fileName.toString(), guid, 
								EFGImportConstants.TAXONPAGE_XSL, datasourceName,whichDatabase);
					if(groupTable == null){
							groupTable = new Hashtable();
					}
					String cssLocation = context + "/" + EFGImportConstants.templateCSSDirectory  + "/";
						    String cssFile = "monteverdelight.css";
						    File cssFiles = new File(realPath + File.separator +  EFGImportConstants.templateCSSDirectory);
					File[] cssFileList = cssFiles.listFiles(); 
					if(!isTableExists){    	
					 	String forwardPage="NoDatasource.jsp";
						RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
						dispatcher.forward(request, response);
						    }
				%>

		<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<title>Taxon Page Configuration of <%=uniqueName%> for Datasource <%=displayName%> </title>
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
		<script language="JavaScript" src="<%=context%>/js/prototype1.5.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=context%>/templateJSP/javascripts/templates_ajax.js" type="text/javascript"></script>			
		<link rel="stylesheet" href="<%=cssLocation%>"/>		
	</head>
