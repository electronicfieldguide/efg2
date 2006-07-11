<% 
   String context = request.getContextPath();
   String dsName = request.getParameter("dataSourceName");
   String url = "";
%>
<html>
  <head>
  </head>
  <body bgcolor="#ffffff">
    <h2 align="center">Click to view Template</h2>
  <center>
      <table>
			  <tr>
					  <td> 	 <a target="_blank" href="templateConfigFiles/NantucketLists.html">Nantucket Lists Template</a></td>
					  <td> 	 <a target="_blank" href="templateConfigFiles/EFGTemplatePlates.html">EFG Plate Template</a></td>
		
			  </tr>
			<tr/>
			<tr/>
			<tr/>
			  </table>

				<hr/><br/><br/>
			<h3 align="center"> Select a template from the list below </h3>
			</table> <table>

		  	<tr>
							
				<td>
					<% url = "xslFileName=NantucketListsTemplate.xsl&dataSourceName=" + dsName;%>
			 	 	<a  title="" href="<%=context%>/NantucketListsTemplate.jsp?<%=url%>">

						Nantucket Lists Template
				 	</a>
				</td> 
 </tr>
<tr>

				<td>
					<% url = "xslFileName=EFGPlatesTemplatesUnsorted.xsl&dataSourceName=" + dsName;%>

			 	 	<a  title="search datasource" href="<%=context%>/EFGPlatesTemplate.jsp?<%=url%>">
						EFG Plates Template
				 	</a>
				</td> 
 </tr>
			<br/><br/>
      </table>
  </center>
  </body>
</html>
