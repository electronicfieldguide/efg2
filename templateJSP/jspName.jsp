<%@ page language="java" %>
<!-- $Id$ -->

<% 
String fullJSPName = this.getClass().getName();
String jspName = "";

if(fullJSPName != null){
	int lastIndex = fullJSPName.lastIndexOf(".");
	if(lastIndex > -1){
		String newJSPName =  fullJSPName.substring(lastIndex+1, fullJSPName.length());
		jspName = newJSPName.replaceAll("_",".");
	}
}
%>
