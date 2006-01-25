<%@ page import ="java.io.*,java.util.*,org.jdom.*,org.jdom.output.*" %>
<%
    response.setContentType("text/xml");

    Document doc= (Document)request.getAttribute("resultSet");
   XMLOutputter outputter = new XMLOutputter();
    try {
      outputter.output(doc, response.getWriter());

    }
    catch (Exception e) {
      e.printStackTrace();
    }
%>


