<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.Iterator,
project.efg.server.interfaces.ServletAbstractFactoryInterface,
project.efg.util.interfaces.EFGImportConstants,
project.efg.server.utils.EFGDisplayObjectList,
project.efg.util.utils.EFGDisplayObject
" %>
<%@page import="project.efg.server.factory.EFGSpringFactory"%>
<%@page import="project.efg.server.utils.ServletCacheManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<html>
	<head>
		<%
			String mainTable = EFGImportConstants.EFG_RDB_TABLES;
			String ALL_TABLE_NAME = EFGImportConstants.ALL_TABLE_NAME;		
			String mainTableName = request.getParameter(ALL_TABLE_NAME);
			if(mainTableName == null || mainTableName.trim().equals("")){
				   mainTableName = mainTable;
			}
			StringBuffer mainTableConstant =
				new StringBuffer(ALL_TABLE_NAME + "=" + mainTableName);	
			String forwardPage="NoDatasource.jsp";
		   	String context = request.getContextPath();
		   	Map map = new HashMap();
		   	Map map1 = 
			   ServletCacheManager.getDatasourceCache(EFGImportConstants.EFG_RDB_TABLES);		   	
			if(map1 != null){
				map.putAll(map1);
			}
			 map1 = 
				   ServletCacheManager.getDatasourceCache(
						   EFGImportConstants.EFG_GLOSSARY_TABLES);		   	
			if(map1 != null){
				map.putAll(map1);
			}
			boolean isEmpty = false;
			if(map != null){
				if(map.size() > 0){
					isEmpty = true;
				}
			}
		%>
		<title>Flush Cache</title>
	</head>
	<body>
	<%if(!isEmpty){ %>
		<form name="myform" action="<%=context%>/../Flush.jsp" method="post">
		<select name="<%=EFGImportConstants.DATASOURCE_NAME%>" size="4" multiple="multiple">
			<%
			Iterator dsNameIter = map.keySet().iterator(); 
			while (dsNameIter.hasNext()) { 
				String datasourceName = (String)dsNameIter.next();
				String displayName = (String)map.get(datasourceName.toLowerCase());
			%>
			<option value="<%=datasourceName%>"><%=displayName%></option>
			<%}%>
		</select>
		 	<%
		 	dsNameIter = map.keySet().iterator(); 
			while (dsNameIter.hasNext()) { 
				String datasourceName = (String)dsNameIter.next();
				String displayName = (String)map.get(datasourceName.toLowerCase());
			%>
				<input type="hidden" name="<%=datasourceName%>" value="<%=displayName%>"/>
			<%}%>
		<input type="submit" name="submit" value="Click to refresh Cache"/>
		</form>
		<%} else {%>
			<p> No datasources exists</p>
		<%} %>
	</body>
</html>