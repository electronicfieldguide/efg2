<%@page import="project.efg.server.servlets.EFGContextListener"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="project.efg.util.interfaces.EFGImportConstants" %>
<%@page import="project.efg.util.utils.EFGUtils"%>
<html>
	<head>
	<%
		StringBuffer refreshedDatasources = null;
		String tableName = request.getParameter(EFGImportConstants.ALL_TABLE_NAME);
		if(tableName == null){
			refreshedDatasources = new StringBuffer(getRefreshed(request,EFGImportConstants.EFG_RDB_TABLES));
			refreshedDatasources.append(getRefreshed(request,EFGImportConstants.EFG_GLOSSARY_TABLES));
		}
		else{
			refreshedDatasources = new StringBuffer(getRefreshed(request,tableName));
		}
	%>
	<%! 
      public String getRefreshed(HttpServletRequest request, String tableName) {
		StringBuffer refreshedDatasources = new StringBuffer();
		String[] datasources = 
			request.getParameterValues(EFGImportConstants.DATASOURCE_NAME);
			for(int i = 0; i < datasources.length; i++){
				try{
					String ds = datasources[i];
					String dsp = request.getParameter(ds);
					String key =  EFGUtils.constructCacheKey(ds,"true");
					if(key != null){
						flushFromCache(key);
						
					}
					key = EFGUtils.constructCacheKey(ds, 
					"_metadataName");
					if(key != null){
						flushFromCache(key);
					}
					flushFromCache(ds.toLowerCase());
					flushFromCache( 
							tableName.toLowerCase());
					//reload cache
	 			 	EFGContextListener.populateCacheWithDatasources(tableName.toLowerCase());
					if(dsp != null){
						refreshedDatasources.append(dsp);
					}
					else{
						refreshedDatasources.append(ds);
					}
					refreshedDatasources.append(" , ");
				}
				catch(Exception ee){
					
				}
			}
		return refreshedDatasources.toString();
	}

	private void flushFromCache(String key){
		try{
			EFGContextListener.getCacheAdmin().flushEntry(key);
			EFGContextListener.getCacheAdmin().removeEntry(key);
		}
		catch(Exception ee){
		}
		try{
			EFGContextListener.getCacheAdminMap().flushEntry(key);
			EFGContextListener.getCacheAdminMap().removeEntry(key);
		}
		catch(Exception e){
		}
	
	}

	%>
	</head>
	<body>
		<%if(refreshedDatasources.length() > 0 ){%>
			<p> The following datasources were refreshed in cache: <%=refreshedDatasources.toString()%></p>
		<% } else{%>
				<p> No datasources refreshed </p>
		<%}%>
	</body>
</html>	
	
