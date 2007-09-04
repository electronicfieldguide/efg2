<%@page import="
project.efg.util.interfaces.EFGImportConstants
" %>
<%@ include file="../commonTemplateCode/commonConfigureJSPCode.jsp" %>
<html>
  <head>
    <title>Configuration of a List Template for <%=displayName%></title>
    <link href="../../css/templates.css" rel="stylesheet" type="text/css"/>
  </head>
  <body bgcolor="#ffffff">
     <h2 align="center">Configuration of a List Template for <%=displayName%></h2>
      
  	<center>
   		<form name="configure" action="Redirect2Template.jsp">
  			Select a template to configure for your Datasource:
      		<select name="<%=EFGImportConstants.TEMPLATE_NAME%>" title="Select a template from list below">
				<%
				if(!isGlossary){
				%>
				<option value="<%=ListViewTemplateItalLeft%>">Italics Left</option>
				<option value="<%=ListViewTemplateItalRight%>">Italics Right</option>
				<option value="<%=nantucketForestCName%>">Italics Right, Nantucket Forest Style</option>
				<option value="<%=nantucketNavyCName%>">Italics Right, Nantucket Navy Style</option>
				<option value="<%=nantucketNectarzCName%>">Italics Right, Nantucket Nectarz Style</option>
				<option value="<%=nantucketOnlyCName%>">Italics Right, Nantucket Style</option>
				<option value="<%=bnc_republicCName%>">Italics Right, Banana Republic Style</option>
				<option value="<%=bnc_summerCName%>">Italics Right, Summer Style</option>
			
			
				<option value="<%=nantucketForestCNamelevelup%>">Sorted List Italics Right, Nantucket Forest Style</option>
				<option value="<%=nantucketNavyCNamelevelup%>">Sorted List Italics Right, Nantucket Navy Style</option>
				<option value="<%=nantucketNectarzCNamelevelup%>">Sorted List Italics Right, Nantucket Nectarz Style</option>
				<option value="<%=nantucketOnlyCNamelevelup%>">Sorted List Italics Right, Nantucket Style</option>
				<option value="<%=bnc_republicCNamelevelup%>">Sorted List Italics Right, Banana Republic Style</option>
				<option value="<%=bnc_summerCNamelevelup%>">Sorted List Italics Right, Summer Style</option>

				<option value="<%=nantucketForestSNamelevelup%>">Sorted List Italics Left, Nantucket Forest Style</option>
				<option value="<%=nantucketNavySNamelevelup%>">Sorted List Italics Left, Nantucket Navy Style</option>
				<option value="<%=nantucketNectarzSNamelevelup%>">Sorted List Italics Left, Nantucket Nectarz Style</option>
				<option value="<%=nantucketOnlySNamelevelup%>">Sorted List Italics Left, Nantucket Style</option>
				<option value="<%=bnc_republicSNamelevelup%>">Sorted List Italics Left, Banana Republic Style</option>
				<option value="<%=bnc_summerSNamelevelup%>">Sorted List Italics Left, Summer Style</option>
			
				<option value="<%=nantucketForestSName%>">Italics Left, Nantucket Forest Style</option>
				<option value="<%=nantucketNavySName%>">Italics Left, Nantucket Navy Style</option>
				<option value="<%=nantucketNectarzSName%>">Italics Left, Nantucket Nectarz Style</option>
				<option value="<%=nantucketOnlySName%>">Italics Left, Nantucket Style</option>
				<option value="<%=bnc_republicSName%>">Italics Left, Banana Republic Style</option>
				<option value="<%=bnc_summerSName%>">Italics Left, Summer Style</option>

				<%}
				else{%>
					<option value="<%=ListViewTemplateGlossary%>">Glossary Style</option>
				 	<option value="<%=BananaRepublicStyleG%>">Glossary with Alpha Strip - Banana Republic Style</option>
				 	<option value="<%=NantucketForestG%>">Glossary with Alpha Strip - Nantucket Forest Style</option>
				  	<option value="<%=NantucketNavyG%>">Glossary with Alpha Strip - Nantucket Navy Style</option>
	 				<option value="<%=NantucketNectarzG%>">Glossary with Alpha Strip - Nantucket Nectarz Style</option>
		 			<option value="<%=NantucketStyleG%>">Glossary with Alpha Strip - Nantucket Style</option> 	
			 		<option value="<%=SummerStyleG%>">Glossary with Alpha Strip - Summer Style</option>
		 			
				<%}
				%>
				
    		</select>
    		<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>" value="<%=dsName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>" value="<%=displayName%>"/>
    		<input type="hidden" name="<%=EFGImportConstants.TEMPLATE_UNIQUE_NAME%>" value="<%=templateName%>"/> 
    		<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>" value="<%=whichDatabase%>"/>
    		<input type="submit" value="Submit"/>
    	</form>
    	<br/><br/>
    	<hr/>
    	<%@ include file="listSamples.jsp" %>
     	
  	</center>
  </body>
</html>
