<%@page import="java.util.*,java.sql.*, project.efg.util.*, project.efg.efgInterface.*" %>
<jsp:useBean id="dsHelperFactory" class="project.efg.efgInterface.EFGDSHelperFactory" 
  scope="page"/>

<% String dataSourceName = request.getParameter("dataSourceName");
   RDBDataSourceHelper dsHelper = (RDBDataSourceHelper)dsHelperFactory.getDataSourceHelper(); 
   String context = request.getContextPath();
   StringBuffer queryBuffer = new StringBuffer();
   queryBuffer.append(context);
   queryBuffer.append("/search?dataSourceName=");
   queryBuffer.append(dataSourceName);
   queryBuffer.append("&");
   queryBuffer.append(EFGImportConstants.MAX_DISPLAY);
   queryBuffer.append("=1&");
   queryBuffer.append(EFGImportConstants.DISPLAY_FORMAT);
   queryBuffer.append("=";
   queryBuffer.append(EFGImportConstants.HTML);	
   queryBuffer.append("&");
%>

<html>
<head><title>Search Results</title></head>
<body>
	<%
	TreeMap searchFields =(TreeMap)dsHelper.getSearchable(dataSourceName);
	EFGObject entry = (EFGObject)searchFields.lastKey();
	String fieldName = entry.getName();
	Set hs  = (TreeSet)searchFields.get(entry);
    	Iterator iter = hs.iterator();
    
    	if ( hs.size() == 0 ) { %>
      	<h3>There are no results matching your search criteria.</h3>
	<% }		
      else { %>
      	<h3>Your search found <%=hs.size()%> results.</h3>
      	<table border="0" width="100%">
   			<% while (iter.hasNext()) { %>
        			<tr>
     					<% for (int i = 0; i<3 && iter.hasNext(); i++) {
          					String dbean = (String)iter.next();
						
						queryBuffer.append(fieldName);
						queryBuffer.append("=");
						queryBuffer.append(dbean.trim());
					 %>	
	  					<td align="center">
	    						<a href="<%=url%>"><%=dbean.trim()%></a> 
	  					</td>
     					<%} %>
        			</tr>
   			<%} %>
      	</table>
 	<%} 
	%>
</body>
</html>