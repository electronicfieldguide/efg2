/**
 * 
 */
package project.efg.servlets.efgImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.templates.taxonPageTemplates.CharacterValue;
import project.efg.templates.taxonPageTemplates.GroupType;
import project.efg.templates.taxonPageTemplates.GroupTypeItem;
import project.efg.templates.taxonPageTemplates.GroupsType;
import project.efg.templates.taxonPageTemplates.GroupsTypeItem;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslFileNamesType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;
import project.efg.util.EFGFieldObject;
import project.efg.util.EFGImportConstants;
import project.efg.util.EFGUniqueID;
import project.efg.util.GroupTypeComparator;
import project.efg.util.GroupTypeSorter;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * @author kasiedu
 * 
 */
public class TaxonPageTemplateConfig extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static GeneralCacheAdministrator cacheAdmin = EFGContextListener.getCacheAdmin();

	

	private String realPath;

	/**
	 * This method is called when the servlet is first started up.
	 */
	/**
	 * This method is called when the servlet is first started up.
	 * 
	 * @params config the ServletConfig object for this servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		realPath = getServletContext().getRealPath("/");
	}

	/**
	 * Handles an HTTP GET request - Based most likely on a clicked link.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	/**
	 * Handles an HTTP POST request - Based most likely on a form submission.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param res
	 *            the servlet response object
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		this.processParams(req, res);
	}

	/**
	 * Get the HttpServletRequest parameters and return the value.
	 * 
	 * @param req
	 *            the servlet request object
	 * @param name
	 *            the name of the parameter of the HttpServletRequest
	 * @return the value of the parameter to be used as a test query
	 */
	private void processParams(HttpServletRequest req, HttpServletResponse res) {
		try {
		
			String dsName = (String) req
					.getParameter(EFGImportConstants.DATASOURCE_NAME);
		
			String displayName = (String) req
					.getParameter(EFGImportConstants.DISPLAY_NAME);
		
			TaxonPageTemplates tps = this.getTaxonPageTemplateRoot(dsName);

			if (tps == null) {
				throw new Exception("Datasource File not found");
			}

			XslPage xslPage = getXSLPageParams(req, tps);
			if (xslPage == null) {
				throw new Exception("xslPage is null");
			}
			String guid = xslPage.getGuid();
			if ((guid != null) && (!guid.trim().equals("")) ){
				req.setAttribute(EFGImportConstants.GUID,guid);
			}
			String uniqueName = xslPage.getDisplayName();
			if ((uniqueName != null) && (!uniqueName.trim().equals("")) ){
				req.setAttribute(EFGImportConstants.TEMPLATE_UNIQUE_NAME,uniqueName);
			}
			GroupsType groups = xslPage.getGroups();
			if (groups == null) {
				throw new Exception("Groups is null");
			}

			req.setAttribute(EFGImportConstants.DATASOURCE_NAME, dsName);
			req.setAttribute(EFGImportConstants.DISPLAY_NAME_COL, displayName);
			EFGFieldObject field = this.add2Groups(req, xslPage);

			boolean done = this.writeFile(dsName, tps);
			//log.debug("Done writing");
			if (done) {
				if (field != null) {
					req.setAttribute("fieldName", field.getFieldName());
					req.setAttribute("fieldValue", field.getFieldValue());
				}
				this.forwardPage(req, res, false);
			} else {
				throw new Exception("Error occured during saving of file");
			}
		} catch (Exception ee) {
			ee.printStackTrace();
			//System.out.println(ee.getMessage());
			try {
				this.forwardPage(req, res, true);
			} catch (Exception eef) {

			}
		}
	}

	/**
	 * @param groups
	 */
	private GroupsType sortGroups(GroupsType groupsType) {
		GroupTypeSorter cvt = new GroupTypeSorter();
		groupsType = cvt.sort(new GroupTypeComparator(), groupsType);
		return groupsType;

	}

	private XslPage getXSLPageParams(HttpServletRequest req,
			TaxonPageTemplates tps) {
		String dsName = (String) req
				.getParameter(EFGImportConstants.DATASOURCE_NAME);

		String xslName = (String) req
				.getParameter(EFGImportConstants.XSL_STRING);
		//log.debug("xslName: " + xslName);
		String searchPage = (String) req
				.getParameter(EFGImportConstants.SEARCH_PAGE_STR);// "search"

		String searchType = req
				.getParameter(EFGImportConstants.SEARCH_TYPE_STR);
		String uniqueName = req
				.getParameter(EFGImportConstants.TEMPLATE_UNIQUE_NAME);
		String guid = (String)req.getParameter(EFGImportConstants.GUID);
	
		String jspName = req.getParameter(EFGImportConstants.JSP_NAME);
		String isDefault = req.getParameter(EFGImportConstants.ISDEFAULT_STR);
		//log.debug("ISDEFAULT: " + isDefault);
		return this.getXSLPageType(tps, dsName, searchPage, searchType,
				xslName, uniqueName, guid,jspName, isDefault);

	}

	private EFGFieldObject add2Groups(HttpServletRequest req, XslPage xslPage) {
		EFGFieldObject field = null;

		GroupsType groups = xslPage.getGroups();
		for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) {
			String key = ((String) e.nextElement()).trim();
			if (key.equalsIgnoreCase(EFGImportConstants.XSL_STRING)
					|| (key
							.equalsIgnoreCase(EFGImportConstants.DATASOURCE_NAME))
					|| key.equalsIgnoreCase(EFGImportConstants.SUBMIT)
					|| key.equalsIgnoreCase(EFGImportConstants.DISPLAY_NAME)
					|| key.equalsIgnoreCase(EFGImportConstants.HTML)
					|| key.equalsIgnoreCase(EFGImportConstants.TEMPLATE_UNIQUE_NAME)
					|| key.equalsIgnoreCase(EFGImportConstants.TEMPLATE_UNIQUE_NAME)
					|| key.equalsIgnoreCase(EFGImportConstants.GUID)) {
				continue;
			}
			String valuePair = req.getParameter(key);
			if(valuePair == null){
				continue;
			}
			if("".equals(valuePair.trim())){
				continue;
			}
			if(key.trim().indexOf(":") == -1){
				continue;
			}
			String currentValue = createGroup(groups, key, valuePair);
			if(currentValue != null){
				currentValue = currentValue.trim();			
				if (field == null) {
					
					if (!currentValue.equals("") && (currentValue.indexOf(".") == -1)) {
						field = new EFGFieldObject();
						field.setFieldName(key);
						field.setFieldValue(currentValue);
					}
				}
			}
		}
	
		groups = this.sortGroups(groups);
		xslPage.setGroups(groups);
		
		return field;
	}


	private String getFileLocation(String dsName) {
		StringBuffer fileLocationBuffer = new StringBuffer();
		fileLocationBuffer.append(realPath);
		fileLocationBuffer.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME);
		fileLocationBuffer.append(File.separator);
		fileLocationBuffer.append(dsName.toLowerCase());
		fileLocationBuffer.append(EFGImportConstants.XML_EXT);
		//log.debug("File Location: " + fileLocationBuffer.toString());
		return fileLocationBuffer.toString();
	}

	private boolean marshal(FileWriter writer, TaxonPageTemplates tps) {
		try {
			String mutex = "";
			synchronized (mutex) {
				boolean done = false;

				try {

					org.exolab.castor.xml.Marshaller marshaller = new org.exolab.castor.xml.Marshaller(
							writer);
					marshaller
							.setNoNamespaceSchemaLocation(EFGImportConstants.TEMPLATE_SCHEMA_NAME);

					marshaller.setNamespaceMapping("xsi",
							org.exolab.castor.xml.Marshaller.XSI_NAMESPACE);
					// suppress the printing of xsi:type
					marshaller.setMarshalExtendedType(false);
					marshaller.setSuppressXSIType(true);
					marshaller.marshal(tps);
					done = true;

				} catch (Exception eee) {
					done = false;
					LoggerUtilsServlet.logErrors(eee);
				}
				return done;
			}
		} catch (Exception ee) {

		}
		return false;

	}

	private boolean writeFile(String dsName, TaxonPageTemplates tps) {

		String fileLocation = this.getFileLocation(dsName);
		File file = new File(fileLocation);
		String renamedFile = fileLocation + System.currentTimeMillis() + "_old";
		File file2 = new File(renamedFile);
		boolean isExists = false;
		boolean success = false;
		boolean done = true;
		if (file.exists()) {
			isExists = true;
			String mute = "";
			// Rename file (or directory)
			synchronized (mute) {
				try {
					success = file.renameTo(file2);
				} catch (Exception eeer) {
					//log.debug(eeer.getMessage());
				}
			}
			if (!success) {
				isExists = false;
				//log.debug("File could not be renamed!!");
			}
		}
		FileWriter writer = null;
		try {
			String mute = "";
			synchronized (mute) {
				try {
					writer = new FileWriter(fileLocation);
					done = this.marshal(writer, tps);
					writer.flush();
					writer.close();
				} catch (Exception eee) {
					done = false;
					throw eee;
				}
			}
		} catch (Exception ee) {
			done = false;
			try {
				// rename file to a new one
				if (writer != null) {
					writer.flush();
					//log.debug("Closing resource for writing!!!");
					writer.close();
				}
				if (isExists) {
					String mute = "";
					synchronized (mute) {
						success = file2.renameTo(file);

					}
					if (!success) {
						isExists = false;
					}
				}
			} catch (Exception ff) {
				LoggerUtilsServlet.logErrors(ff);
				done = false;
			}
		}
		return done;
	}

	
	private TaxonPageTemplates getTaxonPageTemplateRoot(String dsName) {
		TaxonPageTemplates ts = null;
			try {
				if(dsName == null){
					return null;
				}
				String templateName = dsName.toLowerCase() + EFGImportConstants.XML_EXT;
				//System.out.println("Template Name: " + templateName);
				if(templateName != null){
					ts = (TaxonPageTemplates)cacheAdmin.getFromCache(templateName.toLowerCase());
				}

			}  catch (NeedsRefreshException nre) {
				System.err.println(nre.getMessage());
				LoggerUtilsServlet.logErrors(nre);
			}
			if(ts == null){
				//System.out.println("ts is null!!");
			}
			return ts;
		}
	private XslPage getXSLPageType(TaxonPageTemplates tps, String dsName,
			String searchPage, String searchType, String xslName,
			String uniqueName, String guid, String jspName,String isDefault) {
		String mute = "";
		synchronized (mute) {
			XslPageType xslPageType= getCurrentXSLPageType(tps, dsName, searchPage,
					searchType);
			XslPage page = null;
			//log.debug("isDefault: " + isDefault);
			Boolean bool = new Boolean(isDefault);
			boolean defaultFile = bool.booleanValue();

			for (int j = 0; j < xslPageType.getXslPageCount(); ++j) {// find
				// out
				// if
				// it
				// exists
				XslPage currentPage = xslPageType.getXslPage(j);
				String currentGUIDName = currentPage.getGuid();
				String currentUniqueName =  currentPage.getDisplayName();
				//log.debug("currentGUIDName : " + currentGUIDName);
				
				
				if ((currentGUIDName.equalsIgnoreCase(guid)) || 
				(currentUniqueName.equalsIgnoreCase(uniqueName))) {// if
					// this
					// file
					// name
					// already
					// exists
					//log.debug("currentGuid is found");
					currentPage.setGroups(null);
					currentPage.setIsDefault(defaultFile);
					page = currentPage;
				} else {
					if (defaultFile) {
						currentPage.setIsDefault(false);
					} 
				}
			}
			if (page == null) {
				page = new XslPage();
				page.setFileName(xslName);
				page.setDisplayName(uniqueName);

				if ((guid == null) || (guid.trim().equals("")) ){
					guid= EFGUniqueID.getID() + "";		
					
				}				
				page.setGuid(guid);
				
				if((jspName != null) && (!jspName.trim().equals(""))){
					page.setJspName(jspName);
				}
				// generate a guid
				page.setIsDefault(defaultFile);
				xslPageType.addXslPage(page);
			}
			GroupsType groupsType = new GroupsType();
			page.setGroups(groupsType);
			return page;
		}

	}

	private XslPageType getCurrentXSLPageType(TaxonPageTemplates tps, String dsName,
			String searchPage, String searchType) {
		String mute = "";
		synchronized (mute) {

			XslPageType xslPageType = null;
			try {

				int counter = tps.getTaxonPageTemplateCount();

				for (int i = 0; i < counter; i++) {

					TaxonPageTemplateType tp = tps.getTaxonPageTemplate(i);
					String ds = tp.getDatasourceName();
					if (ds.equalsIgnoreCase(dsName.trim())) {
						//log.debug("Found datasource: " + ds);
						XslFileNamesType xslFileNames = tp.getXSLFileNames();
						if (EFGImportConstants.SEARCH_TAXON_TYPE
								.equalsIgnoreCase(searchType)) {
							//log.debug("It is a taxon Page");
							xslPageType = xslFileNames.getXslTaxonPages();
						} else if (EFGImportConstants.SEARCH_PLATES_TYPE
								.equalsIgnoreCase(searchType)) {
							//log.debug("It is a plate");
							xslPageType = xslFileNames.getXslPlatePages();
						} else if (EFGImportConstants.SEARCH_LISTS_TYPE
								.equalsIgnoreCase(searchType)) {
							//log.debug("It is a list");
							xslPageType = xslFileNames.getXslListPages();
						}
						else{
							xslPageType = xslFileNames.getXslTaxonPages();
						}
						break;
					}
				}
			} catch (Exception ee) {
				LoggerUtilsServlet.logErrors(ee);
				return null;
			}
			return xslPageType;
		}

	}

	// DO THIS ONLY once at import time probably

	private void forwardPage(HttpServletRequest req, HttpServletResponse res,
			boolean isError) throws Exception {
		RequestDispatcher dispatcher = null;
		res.setContentType(EFGImportConstants.TEXT_HTML);
		PrintWriter out = res.getWriter();
		try {
			// forward to TestConfigPage.jsp
			if (!isError) {
				//log.debug("No errors");
				String searchPage = (String) req
						.getParameter(EFGImportConstants.SEARCH_PAGE_STR);// "search"
					
				if (searchPage != null) {// allow multiple xslNames
					//log.debug("Forward to Test Search page");
					String searchType = req
							.getParameter(EFGImportConstants.SEARCH_TYPE_STR);
					if (EFGImportConstants.SEARCH_PLATES_TYPE
							.equalsIgnoreCase(searchType)) {
						req.setAttribute(EFGImportConstants.SEARCH_TYPE_STR,
								EFGImportConstants.SEARCH_PLATES_TYPE);
					} else {
						req.setAttribute(EFGImportConstants.SEARCH_TYPE_STR,
								EFGImportConstants.SEARCH_LISTS_TYPE);
					}
					dispatcher = getServletContext().getRequestDispatcher(
							EFGImportConstants.TEST_SEARCH_CONFIG_PAGE);

				} else {
					//log.debug("Forward to Test taxon page");
					dispatcher = getServletContext().getRequestDispatcher(
							EFGImportConstants.TEST_TAXON_CONFIG_PAGE);
				}
			} else {
				//log.debug("Forward to error page1");
				dispatcher = getServletContext().getRequestDispatcher(
						EFGImportConstants.TEMPLATE_ERROR_PAGE);
			}
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
			dispatcher = getServletContext().getRequestDispatcher(
					EFGImportConstants.TEMPLATE_ERROR_PAGE);
		}
		dispatcher.forward(req, res);
		out.flush();
		res.flushBuffer();
	}

	private GroupType checkGroup(GroupsType groups, String groupID,
			int groupRank) {

		GroupType gt = null;
		GroupsTypeItem key = null;
		boolean found = false;
		String id = null;
		for (Enumeration e = groups.enumerateGroupsTypeItem(); e
				.hasMoreElements();) {
			key = (GroupsTypeItem) e.nextElement();
			gt = key.getGroup();
			id = gt.getId();
			int rank = gt.getRank();
			if ((id.equals(groupID)) && (rank == groupRank)) {
				found = true;
				break;
			}
		}
		if (!found) {
			id = groupID;
			gt = new GroupType();
			gt.setId(groupID);
			gt.setRank(groupRank);
			key = new GroupsTypeItem();
			key.setGroup(gt);
			groups.addGroupsTypeItem(key);
		}
		
		return gt;
	}

	private CharacterValue findCharacter(GroupType group, int characterRank) {
		GroupTypeItem key = null;
		CharacterValue cv = null;
		boolean found = false;
		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); e
				.hasMoreElements();) {
			key = (GroupTypeItem) e.nextElement();
			if (key != null) {
				cv = key.getCharacterValue();
				if (cv != null) {
					if (cv.getRank() == characterRank) {
						found = true;
						break;
					}
				}
			}
		}
		if (found) {
			return cv;
		}
		return null;

	}

	private String createGroup(GroupsType groups, String key, String text) {
		/*
		 * group: (groupid:grouprank) groupstuff: (group:characterrank)
		 * characterstuff: (characterlabel:groupstuff) grouplabelstuff:
		 * (grouplabel:group)
		 * 
		 * groupid: number characterrank : number grouprank: number grouplabel:
		 * gl characterlabel:cl
		 * 
		 * group id:group rank:character rank | character label: ( cl:1:1:1
		 * gl:2:2 group:3:9:1
		 */

		String returnValue = null;
		String identifier = "";
		String groupID = "";
		String groupRank = "";
		String characterRank = "";
		String[] splits = EFGImportConstants.colonPattern.split(key);
	
		if (splits.length == 0) {
			
			return null;
		}
		identifier = splits[0];

		if (splits.length > 1) {
			groupID = splits[1];
			
		}
		if (splits.length > 2) {
			groupRank = splits[2];
		}
		if (splits.length > 3) {
			characterRank = splits[3];
		}
		if ("".equals(groupID)) {
			//log.debug("GroupID is the empty string");
			return null;
		}
		if ("".equals(groupRank)) {
			//log.debug("GroupRank is the empty string");
			return null;
		}

		// if same rank and id already exists return it
		GroupType group = checkGroup(groups, groupID, Integer
				.parseInt(groupRank));

		if ((EFGImportConstants.GROUP_LABEL)
				.equalsIgnoreCase(identifier.trim())) {// group label or
														// groupText label
			//log.debug("Is a groupLabel with text: " + text);
			if ((text != null) || (!text.trim().equals(""))) {
				group.setLabel(text);
			}
		} else if ((EFGImportConstants.GROUP_TEXT_LABEL)
				.equalsIgnoreCase(identifier.trim())) {// group label or
														// groupText label
			
			if ((text != null) || (!text.trim().equals(""))) {
				group.setText(text);
			}
		} else {
			if (("".equals(characterRank)) || (characterRank == null)) {
				
				return null;
			}
			if ((text == null) || (text.trim().equals(""))) {
				
				return null;
			}

			CharacterValue cv = findCharacter(group, Integer
					.parseInt(characterRank));

			if (cv == null) {
				
				GroupTypeItem vGroupTypeItem = new GroupTypeItem();
				cv = new CharacterValue();
				cv.setRank(Integer.parseInt(characterRank));
				vGroupTypeItem.setCharacterValue(cv);

				group.addGroupTypeItem(vGroupTypeItem);
			
			}
			if ((EFGImportConstants.CHARACTER_TEXT_LABEL)
					.equalsIgnoreCase(identifier.trim())) {// handle character
															// text labels
			
				cv.setText(text);
			} else if ((EFGImportConstants.CHARACTER_LABEL)
					.equalsIgnoreCase(identifier.trim())
					|| ((EFGImportConstants.GROUP_TEXT_LABEL)
							.equalsIgnoreCase(identifier.trim()))) {
			
				if ((EFGImportConstants.CHARACTER_LABEL)
						.equalsIgnoreCase(identifier.trim())) {// character
				
					cv.setLabel(text);
				} else {

					cv.setText(text); // character text
				}
			} else if ((EFGImportConstants.GROUP).equalsIgnoreCase(identifier
					.trim())) {// group

				if (text != null) {

					if (!text.trim().equals("")) {
					
						if (returnValue == null) {

							returnValue = text;
						}
				
						cv.setValue(text);
					}
				} 
			}
		}
	
		return returnValue;
	}
}
