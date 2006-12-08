<%@page import="
project.efg.util.EFGImportConstants,java.util.Enumeration
" %>
<%@ include file="allTableName.jsp"%>
<%
	String templateName = request.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
	String forwardPage= request.getParameter("forwardPage");
	String configType = request.getParameter(EFGImportConstants.CONFIG_TYPE);
	String dsName = request.getParameter(EFGImportConstants.DATASOURCE_NAME);
	request.setAttribute(EFGImportConstants.DATASOURCE_NAME,dsName);
    String displayNameN =  request.getParameter(dsName);
	request.setAttribute(EFGImportConstants.DISPLAY_NAME,displayNameN);
	request.setAttribute(EFGImportConstants.ALL_TABLE_NAME,whichDatabase);
	
	String guidN = null;
	String jspName = null;
	RequestDispatcher dispatcher = null;

	if((templateName != null)&& (!templateName.trim().equals(""))){
		//name exists go back to where you came from and say that the page already exists
		request.setAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME,templateName);
		guidN =  request.getParameter(templateName);
		if((guidN != null) && (!guidN.trim().equals(""))){
			request.setAttribute(EFGImportConstants.GUID,guidN);

			jspName = request.getParameter(guidN);
//find out if the jspName is the same as the templateName

			
			if((jspName != null) && (!jspName.trim().equals(""))){
				if(jspName.toLowerCase().trim().endsWith("begin.jsp")){
					 if(forwardPage.toLowerCase().trim().endsWith("begin.jsp")){
						dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + jspName);
						dispatcher.forward(request, response);
					}
					else{
						int index = jspName.indexOf("Begin.jsp");
						String newPage = null;
						 if(forwardPage.toLowerCase().trim().endsWith("inter.jsp")){//forward to begin page
								newPage = jspName.substring(0,index) + "Inter.jsp";
						}
						else{//forward to advanced page
							newPage = jspName.substring(0,index) + ".jsp";
						}
						dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + newPage);
						dispatcher.forward(request, response);
					}
				}
				else if(jspName.toLowerCase().trim().endsWith("inter.jsp")){
					 if(forwardPage.toLowerCase().trim().endsWith("inter.jsp")){
						dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + jspName);
						dispatcher.forward(request, response);
					}
					else{
						int index = jspName.indexOf("Inter.jsp");
						String newPage = null;
						 if(forwardPage.toLowerCase().trim().endsWith("begin.jsp")){//forward to begin page
								newPage = jspName.substring(0,index) + "Begin.jsp";
						}
						else{//forward to advanced page
							newPage = jspName.substring(0,index) + ".jsp";
						}
						dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + newPage);
						dispatcher.forward(request, response);
					}
				}
				else{//advanced
					  if((forwardPage.toLowerCase().trim().endsWith("inter.jsp")) || (forwardPage.toLowerCase().trim().endsWith("begin.jsp"))){
						int index = jspName.indexOf(".jsp");
						String newPage = null;
						if(forwardPage.toLowerCase().trim().endsWith("inter.jsp")){
								newPage = jspName.substring(0,index) + "Inter.jsp";
						}
						else{
							newPage = jspName.substring(0,index) + "Begin.jsp";
						}
						

						dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + newPage);
						dispatcher.forward(request, response);
					}
					else{
						dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + jspName);
						dispatcher.forward(request, response);
					}
				}
			}
			else{
				dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
				dispatcher.forward(request, response);
			}
		}
		else{
			dispatcher = getServletContext().getRequestDispatcher("/templateJSP/" + forwardPage);
			dispatcher.forward(request, response);
		}
	}
%>
<html>
	<head><title>Error..Template Name required</title></head>
	<body>
	<h2> ERROR!!!. A Template Name is Required.</h2>
	<a href="javascript:history.go(-1)">Please Go Back and Enter a Name For Your Template</a>
	</body>
</html>
