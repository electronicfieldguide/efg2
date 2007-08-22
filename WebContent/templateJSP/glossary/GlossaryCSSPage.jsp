<%@ page language="java" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%!

String nantucketskinforest="nantucket_forest.css";
String nantucketskinnavy="nantucket_navy.css";
String nantucketskinnectar="nantucket_nectarz.css";

String nantucketskin="nantucket.css";
String bncskinbananarepub="bnc_bananarepub.css";
String bncskinjennsyard="bnc_forthyard.css";

Map templateDisplayNames;
public void jspInit(){
	templateDisplayNames = new HashMap();
	templateDisplayNames.put(nantucketskin,"Glossary with Alpha Strip Nantucket Style");
	templateDisplayNames.put(nantucketskinforest,"Glossary with Alpha Strip - Nantucket Forest Style");
	templateDisplayNames.put(nantucketskinnavy,"Glossary with Alpha Strip - Nantucket Navy Style");
	templateDisplayNames.put(nantucketskinnectar,"Glossary with Alpha Strip - Nantucket Nectarz Style");
	templateDisplayNames.put(bncskinbananarepub,"Glossary with Alpha Strip - Banana Republic Style");
	templateDisplayNames.put(bncskinjennsyard,"Glossary with Alpha Strip - Summer Style");
}
%>