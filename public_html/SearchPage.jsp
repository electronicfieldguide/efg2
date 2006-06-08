<%@page import="java.util.*,java.sql.*, project.efg.util.*, project.efg.efgInterface.*" %>
<jsp:useBean id="dsHelperFactory" class="project.efg.efgInterface.EFGDSHelperFactory" 
  scope="page" />

<% String dataSourceName = request.getParameter("dataSourceName");
   
   EFGDataSourceHelper dsHelper = dsHelperFactory.getDataSourceHelper(); 
   String context = request.getContextPath();
%>

<html>
<!-- $Id-->
  <head>
    <title>Search Page for <%=dsHelper.makeReadable(dataSourceName)%></title>
  </head>

  <body bgcolor="#ffffff">
    <h2 align="center">
      Search Page for <%=dataSourceName%>
    </h2>
    <center>
	<!-- show only configured datasources -->
	<%//if(EFGServletUtils.configuredDatasources.contains(dataSourceName.toLowerCase().trim())){ %>
						
      <form method="post" action="<%=context%>/search">
        <P>
          <input type="submit" value="Conduct Search"/>
          <input type="reset" value="Clear all fields"/>
        </P>
        <input type="hidden" name="dataSourceName" value="<%=dataSourceName%>"/>
        <input type="hidden" name="<%=EFGImportConstants.DISPLAY_FORMAT%>" value="<%=EFGImportConstants.HTML%>"/>
        <table>
          <tr>
            <td align="right">
              <b>Maximum Matches to Display:</b><br/><br/></td>
            <td><input type="text" size="4" name="maxDisplay" value="20">
              <br/><br/></td>
          </tr>
	   <tr>
		<td align="right"><b> Display search results as : </b></td>
		<td>
			<select name="<%=EFGImportConstants.SEARCH_TYPE_STR%>" size="1">
				<option value="<%=EFGImportConstants.SEARCH_PLATES_TYPE%>">Plates</option>
				<option value="<%=EFGImportConstants.SEARCH_LISTS_TYPE%>">Lists</option>
			</select>
            </td>
	    <tr>
          <% Map searchFields = dsHelper.getSearchable(dataSourceName);
		for (Iterator iter = searchFields.keySet().iterator(); iter.hasNext();){ 
	    EFGObject entry = (EFGObject)iter.next();
	    //TreeSet treeVal = (TreeSet)searchFields.get(entry);
	    //table.put(entry.toString(),treeVal);
	
	     //for (Enumeration e = ht.keys() ; e.hasMoreElements() ;) {
	       //String fieldName = (String)e.nextElement();
          	 
		 String fieldName = entry.getName();
		Set set  = (TreeSet)searchFields.get(entry);

	       //Set set = (TreeSet) ht.get(fieldName);
	       Iterator valIter = set.iterator();
               //Skip fields with no values
               if (!valIter.hasNext())
	         continue; %>
	       <tr>
                 <td align="right"><b><%=fieldName%>:</b></td>
	         <td>
              <% Set set1 = dsHelper.getNumericFields(dataSourceName);
  			if(!set1.contains(fieldName)){
		  %>
				<select name="<%=fieldName%>" size="1">
                			<option>
            			<% 
			   		while (valIter.hasNext()) { 
		         		%>
                       			<option><%=valIter.next()%>
                    		<% 
					} 
					%>
             		</select>
		     <% }  
              	  else {
			%>
				<input type="text" name="<%=fieldName%>" maxlength="20"/><br/>
			 <%} %>
            </td>
          </tr>
          <% } %>
         
        </table>
        <p>
          <input type="submit" value="Conduct Search"/>
          <input type="reset" value="Clear all fields"/>
        </p>
      </form>
	<%//} else{%>
<!--
	<p>Datasource <%=dataSourceName%> has not yet been configured by its author.</p>
-->
	<%//}%>
    </center>
  </body>
</html>
