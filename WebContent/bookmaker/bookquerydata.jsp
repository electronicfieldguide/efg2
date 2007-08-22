<%@page
	import="java.util.Iterator,project.efg.server.interfaces.EFGDataObjectListInterface,project.efg.server.utils.EFGDataSourceHelperInterface,project.efg.server.utils.EFGTypeComparator,project.efg.server.utils.MediaResourceTypeComparator,project.efg.efgDocument.EFGType,project.efg.efgDocument.ItemsType,project.efg.util.interfaces.EFGDataObject,project.efg.efgDocument.MediaResourcesType,project.efg.efgDocument.MediaResourceType,project.efg.efgDocument.StatisticalMeasuresType,project.efg.efgDocument.StatisticalMeasureType,project.efg.efgDocument.EFGListsType,project.efg.util.interfaces.EFGImportConstants,project.efg.server.interfaces.EFGDocumentSorter,project.efg.server.utils.EFGListTypeSorter,project.efg.server.utils.EFGTypeSorter,project.efg.server.utils.MediaResourceTypeSorter,project.efg.server.utils.StatisticalMeasureTypeSorter,project.efg.util.utils.TemplateMapObjectHandler,project.efg.util.utils.TemplateObject,project.efg.util.utils.EFGDisplayObject,project.efg.server.factory.EFGSpringFactory"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Enumeration"%>
<%@page import="project.efg.server.utils.HttpUtils"%>
<%@page import="project.efg.server.servlets.EFGContextListener"%>
<%@page import="java.util.Map"%>
<%@page import="project.efg.server.utils.ServletCacheManager"%>
<form id="form_search_id">
<%
	String guid_xx = request.getParameter(EFGImportConstants.GUID);

	Enumeration ee = request.getParameterNames();
	Iterator it = null;
	String qry = null;
	if (guid_xx != null) {
		Hashtable map = TemplateMapObjectHandler
		.getTemplateObjectMap(null);
		it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			TemplateObject templateObject = (TemplateObject) map
			.get(key);
			String currentGuid = templateObject.getGUID();
			if (guid_xx.equals(currentGuid)) {
		qry = key;
		break;
			}
		}//end while
	}
	Hashtable table = null;
	if (qry != null) {
		int index = qry.indexOf("?");
		if (index > -1) {
			qry = qry.substring(index + 1, qry.length());
		}
		table = HttpUtils.parseQueryString(qry);
	} else {
		table = new Hashtable(request.getParameterMap());
	}
	if (table == null) {
		table = new Hashtable();
	}

	String displayName = request
			.getParameter(EFGImportConstants.DISPLAY_NAME);
	String datasourceName = request
			.getParameter(EFGImportConstants.DATASOURCE_NAME);

	String context = request.getContextPath();
	String tableName = request
			.getParameter(EFGImportConstants.ALL_TABLE_NAME);
	if (tableName == null || tableName.trim().equals("")) {
		tableName = EFGImportConstants.EFG_RDB_TABLES;
	}
	if (displayName == null || "".equals(displayName)) {
		Map map = ServletCacheManager.getDatasourceCache(tableName);

		displayName = (String) map.get(datasourceName.toLowerCase());
	}
	EFGDataSourceHelperInterface dsHelper = EFGSpringFactory
			.getDatasourceHelper();
	dsHelper.setMainDataTableName(tableName);
	EFGDataObjectListInterface doSearches = dsHelper.getSearchable(
			displayName, datasourceName);
	if (doSearches != null) {
		if (datasourceName == null) {
			datasourceName = doSearches.getDatasourceName();
		}
	}
%>
<table class="searchpage" id="searchPageID">
	<%
	if (doSearches != null) {
		for (int s = 0; s < doSearches.getEFGDataObjectCount(); s++) {
			EFGDataObject searchable = doSearches.getEFGDataObject(s);

			String fieldName = searchable.getName();
			String legalName = searchable.getLegalName();
			List myList = new ArrayList();
			String[] oldValue = (String[]) table.get(legalName);

			if (oldValue != null) {
				myList = new ArrayList(Arrays.asList(oldValue));
			}

			Iterator itemsIter = null;
			ItemsType items = searchable.getStates();
			MediaResourcesType mediaResources = searchable
			.getMediaResources();
			EFGListsType listsType = searchable.getEFGLists();
			StatisticalMeasuresType stats = searchable
			.getStatisticalMeasures();
			EFGDocumentSorter sorter = null;
	%>
	<tr>
		<td align="right"><b><%=fieldName%>:</b></td>
		<td>
			<%
			if ((items != null) && (items.getItemCount() > 0)) {
				sorter = new EFGTypeSorter();
				sorter.sort(new EFGTypeComparator(), items);
				itemsIter = sorter.getIterator();
			%> 
			<select name="<%=legalName%>" size="4" multiple="multiple"
			onchange="evalAnySelectedValue(this);">
			<%
				if ((myList.size() > 0)
				&& !myList.contains(EFGImportConstants.EFG_ANY)) {
			%>
				<option value="<%=EFGImportConstants.EFG_ANY%>">any</option>
			<%
				} else {
			%>
				<option selected="selected" value="<%=EFGImportConstants.EFG_ANY%>">any</option>
			<%
				}
				while (itemsIter.hasNext()) {
					EFGType item = (EFGType) itemsIter.next();
					if (myList.contains(item.getContent())) {
					%>
					<option selected="selected"><%=item.getContent()%></option>
					<%
					} else {
					%>
					<option><%=item.getContent()%></option>
					<%
					}
				}
				%>
			</select> 
			<%
 			} else if ((stats != null)
 			&& (stats.getStatisticalMeasureCount() > 0)) {
 			String currentStatsName = legalName
 				+ EFGImportConstants.EFG_NUMERIC;
 			%> 
 			<input type="text" name="<%=currentStatsName%>" maxlength="20" /> <%
 			String str = "";
 			String units = stats.getUnit();
 			if (units == null) {
 				units = "";
 			}
 			if (stats.getMax() > stats.getMin()) {
 				str = stats.getMin() + "-" + stats.getMax() + " "
 			+ units;
 			} else {
 				str = units;
 			}
 			if (myList.size() > 0) {
 				str = (String) myList.get(0);
 			}
 			%> 
 			<%=str%><br/>
			<%
			} else if ((mediaResources != null)
					&& (mediaResources.getMediaResourceCount() > 0)) {
				sorter = new MediaResourceTypeSorter();
				sorter.sort(new MediaResourceTypeComparator(),
						mediaResources);
				itemsIter = sorter.getIterator();
			%> 
			<select name="<%=legalName%>" size="4" multiple="multiple"
			onchange="evalAnySelectedValue(this);">
			<%
							if ((myList.size() > 0)
							&& !myList.contains(EFGImportConstants.EFG_ANY)) {
			%>
			<option value="<%=EFGImportConstants.EFG_ANY%>">any</option>

			<%
			} else {
			%>
			<option selected="selected" value="<%=EFGImportConstants.EFG_ANY%>">any</option>

			<%
					}

					while (itemsIter.hasNext()) {
						MediaResourceType mediaResource = (MediaResourceType) itemsIter
						.next();

						if (myList.contains(mediaResource.getContent())) {
			%>
			<option selected="selected"><%=mediaResource.getContent()%></option>
			<%
			} else {
			%>
			<option><%=mediaResource.getContent()%></option>
			<%
					}
					}
			%>
		</select> <%
 			} else if ((listsType != null)
 			&& (listsType.getEFGListCount() > 0)) {
 		sorter = new EFGListTypeSorter();
 		sorter.sort(new EFGTypeComparator(), listsType);
 		itemsIter = sorter.getIterator();
 %> <select name="<%=legalName%>" size="4" multiple="multiple"
			onchange="evalAnySelectedValue(this);">
			<%
							if ((myList.size() > 0)
							&& !myList.contains(EFGImportConstants.EFG_ANY)) {
			%>
			<option value="<%=EFGImportConstants.EFG_ANY%>">any</option>

			<%
			} else {
			%>
			<option selected="selected" value="<%=EFGImportConstants.EFG_ANY%>">any</option>

			<%
					}

					while (itemsIter.hasNext()) {
						EFGType listType = (EFGType) itemsIter.next();

						if (myList.contains(listType.getContent())) {
			%>
			<option selected="selected"><%=listType.getContent()%></option>
			<%
			} else {
			%>
			<option><%=listType.getContent()%></option>
			<%
					}

					}
			%>
		</select> <%
 }
 %>
		</td>
	</tr>
	<%
	}
	%>
	<input type="hidden" name="<%=EFGImportConstants.DISPLAY_NAME%>"
		value="<%=displayName%>" />
	<input type="hidden" name="<%=EFGImportConstants.DISPLAY_FORMAT%>"
		value="<%=EFGImportConstants.XML%>" />
	<input type="hidden" name="<%=EFGImportConstants.DATASOURCE_NAME%>"
		value="<%=datasourceName%>" />
	<input type="hidden" name="<%=EFGImportConstants.ALL_TABLE_NAME%>"
		value="<%=tableName%>" />

	<%
	}
	%>
</table>
</form>
<%
if (doSearches != null) {
%>
<input type="submit" name="submit" id="querydatasubmitID" value="Go"
	onclick="javascript:postSearch('searchPageID','designformid','form_search_id')" />
<%
} else {
%>
<table id="searchPageID">
	<tr>
		<td>The selected datasource has no searchable characters</td>
	</tr>
	<table>
		<%
		}
		%>