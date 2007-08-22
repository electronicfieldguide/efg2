<%@page import="project.efg.searchableDocument.Searchables"%>
<%@page import="project.efg.util.interfaces.EFGImportConstants"%>
<%
response.setContentType("text/xml");
  
  try {
	  Searchables searchables = 
			(Searchables)request.getAttribute(EFGImportConstants.XML);

   	searchables.marshal(response.getWriter());
   }
   catch (Exception e) {
     e.printStackTrace();
   }
%>



