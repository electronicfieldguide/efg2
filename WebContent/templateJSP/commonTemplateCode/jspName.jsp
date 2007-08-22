<%@ page language="java" %>
<!-- $Id: jspName.jsp,v 1.1.1.1 2007/08/01 19:11:40 kasiedu Exp $ -->
<% 
String templateJSPNamePkg = ".templateJSP.";
String fullJSPName = this.getClass().getName();
String jspName = "";

if(fullJSPName != null){
	int indexOf = fullJSPName.indexOf(templateJSPNamePkg);
	if(indexOf > -1){
	String name_with_package = 
		fullJSPName.substring(indexOf+templateJSPNamePkg.length(),
				fullJSPName.length());
	jspName = name_with_package.replace('.', '/');

	jspName = jspName.replace('_', '.');
	}
}
%>
