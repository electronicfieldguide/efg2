<%@page import="project.efg.util.interfaces.EFGImportConstants"%>
<%@page language="java" %>
<%
	try{
	String efgSessionID = request.getParameter("efgSessionID");

	if(efgSessionID == null){
		throw new Exception("No Session associated with request");
	}
	
	if(session.getAttribute(efgSessionID) == null){
		throw new Exception("Datasource has no session attached to it");
	}
	

		String nextVal = request.getParameter("nextVal");
		String taxon_count = request.getParameter("taxon_count");
	    request.setAttribute("nextVal", nextVal);
	    request.setAttribute("efgSessionID",efgSessionID);
	    request.setAttribute("taxon_count",taxon_count);
		request.getRequestDispatcher("/ApplyXSL").forward(request,response);
	
	}
catch(Exception ee){
	application.log(ee.getMessage());
	
	request.getRequestDispatcher("/ErrorPage.jsp").forward(request,response);
}
  	
%>