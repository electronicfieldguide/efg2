<%@ page import ="project.efg.efgDocument.EFGDocument,org.exolab.castor.xml.Marshaller,org.exolab.castor.xml.Unmarshaller" %>
<%
    response.setContentType("text/xml");

    EFGDocument doc= (EFGDocument)request.getAttribute("resultSet");
  
    try {
      doc.marshal(response.getWriter());

    }
    catch (Exception e) {
      e.printStackTrace();
    }
%>


