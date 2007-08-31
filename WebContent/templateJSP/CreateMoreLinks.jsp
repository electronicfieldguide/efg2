<%@page import="java.util.Iterator,
project.efg.server.utils.TemplateProducer,
project.efg.util.interfaces.EFGQueueObjectInterface,
project.efg.util.interfaces.EFGImportConstants,
java.util.Hashtable,
java.util.List
" %>

<%
String LINK_TEXT = "button text";
String LINK_URL_TEXT = "URL";

	TemplateProducer tp =(TemplateProducer)session.getAttribute("templateProducer");
	Hashtable groupTable= (Hashtable)session.getAttribute("groupTable");
	
	String name = request.getParameter("nameValue");
	
	if(name == null || "".equals(name.trim())){
	 	name=tp.getCharacter(false,false);
	}
		 			
	String fieldValue = (String)groupTable.get(name);
	if(fieldValue == null){
		fieldValue =LINK_URL_TEXT;
	}
	String characterLabel= tp.getCurrentCharacterLabel(name);
	String characterLabelValue = (String)groupTable.get(characterLabel);
	if(characterLabelValue == null){
		characterLabelValue =LINK_TEXT;		 			
	}
%>
<br/>
<input type="text"    name="<%=characterLabel%>"  size="20"  value="<%=characterLabelValue%>" class="cleardefault"/>						
<input type="text" name="<%=name%>" size="40"  value="<%=fieldValue%>" class="cleardefault"/>


					
