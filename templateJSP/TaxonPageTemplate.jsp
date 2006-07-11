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
					  <td> 	</td>
					  <td> 	 <a target="_blank" href="MarshaSpeciesPageTemplate.html">Marsha's Template Sample </a></td>
					  <td> 	 <a target="_blank" href="SolaData.html">Bill's Template Sample </a></td>
			  </tr>
			<tr/>
			<tr/>
			<tr/>
			  </table>

				<hr/><br/><br/>
			<h3 align="center"> Select a template from the list below</h3>
			</table> <table>

		  	<tr>
							
				<td>
					<% url = "xslFileName=billFamilyTaxonPage.xsl&dataSourceName=" + dsName;%>
			 	 	<a  title="" href="<%=context%>/BillTemplate.jsp?<%=url%>">

						Bill's Template
				 	</a>
				</td> 
 </tr>
<tr>

				<td>
					<% url = "xslFileName=marshaTaxonPage.xsl&dataSourceName=" + dsName;%>

			 	 	<a  title="search datasource" href="<%=context%>/marshaTemplate.jsp?<%=url%>">
						Marsha's Template
				 	</a>
				</td> 
 </tr>
<!--
<tr>


				<td>
					<% url = "xslFileName=jennInvasivesTaxonPage.xsl&dataSourceName=" + dsName;%>

			 	 	<a  title="search datasource" href="<%=context%>/jennTemplate.jsp?<%=url%>">

						Jenn's Template
				 	</a>
				 </td> 
 </tr><tr>


				<td>
					<% url = "xslFileName=nantucketTaxonPage.xsl&dataSourceName=" + dsName;%>

			 	 	<a  title="search datasource" href="<%=context%>/nanTemplate.jsp?<%=url%>">

						Nantucket Template
				 	</a>
				</td> 
			</tr>
-->
			<br/><br/>
      </table>
  </center>
  </body>
</html>
