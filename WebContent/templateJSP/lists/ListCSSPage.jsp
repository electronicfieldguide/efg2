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
	templateDisplayNames.put(nantucketskin,"Nantucket Style");
	templateDisplayNames.put(nantucketskinforest,"Nantucket Forest Style");
	templateDisplayNames.put(nantucketskinnavy,"Nantucket Navy Style");
	templateDisplayNames.put(nantucketskinnectar,"Nantucket Nectarz Style");
	templateDisplayNames.put(bncskinbananarepub,"Banana Republic Style");
	templateDisplayNames.put(bncskinjennsyard,"Summer Style");
}
%>