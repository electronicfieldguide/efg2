<%@page import="
project.efg.util.EFGImportConstants
" %>
<% 
	
	String context = request.getContextPath();
	String configType = request.getParameter(EFGImportConstants.CONFIG_TYPE);

	 String forwardPage="";
	if(configType.equalsIgnoreCase(EFGImportConstants.SEARCH_PLATES_TYPE)){
			forwardPage = "ConfigurePlatesPage.jsp";
	}
	else if(configType.equalsIgnoreCase(EFGImportConstants.SEARCH_LISTS_TYPE)){
		forwardPage = "ConfigureListsPage.jsp";
	}
	 else if(configType.equalsIgnoreCase(EFGImportConstants.SEARCH_TAXON_TYPE)){
		forwardPage = "ConfigureTaxonPage.jsp";
	}
	else{
		forwardPage = "../ErrorPage.jsp";
	}
	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
	dispatcher.forward(request, response);
%>

