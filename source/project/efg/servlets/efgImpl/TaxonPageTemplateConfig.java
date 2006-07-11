/**
 * 
 */
package project.efg.servlets.efgImpl;

import java.io.File;
import java.io.FileReader;
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

import org.apache.log4j.Logger;

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
import project.efg.util.GroupTypeComparator;
import project.efg.util.GroupTypeSorter;

/**
 * @author kasiedu
 * 
 */
public class TaxonPageTemplateConfig extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger log = null;

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
		try {
			log = Logger.getLogger(TaxonPageTemplateConfig.class);
		} catch (Exception ee) {
		}
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
			log.debug("Inside processParams");
			String dsName = (String) req
					.getParameter(EFGImportConstants.DATASOURCE_NAME);
			log.debug("dsName: " + dsName);
			String displayName = (String) req
					.getParameter(EFGImportConstants.DISPLAY_NAME);
			log.debug("displayName: " + displayName);
			TaxonPageTemplates tps = this.getTaxonPageTemplateRoot(dsName);

			if (tps == null) {
				throw new Exception("Datasource File not found");
			}

			XslPage xslPage = getXSLPageParams(req, tps);
			if (xslPage == null) {
				throw new Exception("xslPage is null");
			}
			GroupsType groups = xslPage.getGroups();
			if (groups == null) {
				throw new Exception("Groups is null");
			}

		
			req.setAttribute(EFGImportConstants.DATASOURCE_NAME, dsName);
			req.setAttribute(EFGImportConstants.DISPLAY_NAME_COL, displayName);
			EFGFieldObject field = this.add2Groups(req, xslPage);
			
		
			boolean done = this.writeFile(dsName, tps);
			log.debug("Done writing");
			if (done) {
				
				if (field != null) {
					log.debug("Field Name: " + field.getFieldName());
					log.debug("FieldValue: " + field.getFieldValue());
					req.setAttribute("fieldName", field.getFieldName());
					req.setAttribute("fieldValue", field.getFieldValue());
				}
				log.debug("About to forward page, Error is set to false");
				this.forwardPage(req, res, false);
			} else {
				log.debug("Error occured during saving of file");
				throw new Exception("Error occured during saving of file");
			}
		} catch (Exception ee) {
			try {
				log.error(ee.getMessage());
				log.debug("About to forward page, Error is set to true");
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
		log.debug("xslName: " + xslName);
		String searchPage = (String) req
				.getParameter(EFGImportConstants.SEARCH_PAGE_STR);// "search"

		String searchType = req
				.getParameter(EFGImportConstants.SEARCH_TYPE_STR);
		String isDefault = req.getParameter(EFGImportConstants.ISDEFAULT_STR);
		log.debug("ISDEFAULT: " + isDefault);
		return this.getXSLPageType(tps, dsName, searchPage, searchType,
				xslName, isDefault);

	}

	private EFGFieldObject add2Groups(HttpServletRequest req, XslPage xslPage) {
		EFGFieldObject field = null;
		
		GroupsType groups = xslPage.getGroups();
		for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) {
			String key = ((String) e.nextElement()).trim();
			if (key.trim().equals(EFGImportConstants.XSL_STRING)
					|| (key.equalsIgnoreCase(EFGImportConstants.DATASOURCE_NAME))
					|| key.equalsIgnoreCase(EFGImportConstants.SUBMIT)
					|| key.equalsIgnoreCase(EFGImportConstants.DISPLAY_NAME)
					|| key.equalsIgnoreCase(EFGImportConstants.HTML)
					|| key.equalsIgnoreCase(EFGImportConstants.XSL_STRING)) {
				continue;
			}
			String valuePair = req.getParameter(key);
			log.debug("valuePair: " + valuePair);
			log.debug("Key: " + key);
			String currentValue = createGroup(groups, key, valuePair);
			log.debug("currentValue: " + currentValue);
			if (field == null) {
				if (currentValue != null) {
					if (!currentValue.trim().equals("")) {
						field = new EFGFieldObject();
						field.setFieldName(key);
						field.setFieldValue(currentValue);
					}
				}
			}
		}
		//this.marshalGroupsType(groups,dsName + "before");
		groups = this.sortGroups(groups);
		xslPage.setGroups(groups);
		//log.
		//this.marshalGroupsType(groups,dsName + "after");
		return field;

	}
	/*private void marshalGroupsType(GroupsType groups,String name2Append){
		
		
		FileWriter writer = null;
		try {
			String fileName = this.getFileLocation(name2Append);
			writer = new FileWriter(fileName);
			groups.marshal(writer);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try{
					if(writer != null){
						writer.flush();
						writer.close();
					}
			}
			catch(Exception ee){
				
			}
		}
		
		
		
	}*/
	private String getFileLocation(String dsName) {
		StringBuffer fileLocationBuffer = new StringBuffer();
		fileLocationBuffer.append(realPath);
		fileLocationBuffer.append(EFGImportConstants.TEMPLATES_XML_FOLDER_NAME);
		fileLocationBuffer.append(File.separator);
		fileLocationBuffer.append(dsName.toLowerCase());
		fileLocationBuffer.append(EFGImportConstants.XML_EXT);
		log.debug("File Location: " + fileLocationBuffer.toString());
		return fileLocationBuffer.toString();
	}

	private boolean marshal(FileWriter writer,
			TaxonPageTemplates tps) {
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
					log.error(eee.getMessage());
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
					log.debug(eeer.getMessage());
				}
			}
			if (!success) {
				isExists = false;
				log.debug("File could not be renamed!!");
			}
		}
		FileWriter writer = null;
		try {
			String mute = "";
			synchronized (mute) {
				try {
					writer = new FileWriter(fileLocation);
					done = this.marshal(writer,  tps);
					writer.flush();
					log.debug("Closing resource for writing!!!");
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
					log.debug("Closing resource for writing!!!");
					writer.close();
				}
				if (isExists) {
					String mute = "";
					synchronized (mute) {
						success = file2.renameTo(file);
						
					}
					if (!success) {
						isExists = false;
						log
								.debug("File could not be renamed when exception occured");
					}
				}
			} catch (Exception ff) {
				log.error(ff.getMessage());
				done = false;
			}
		}
		return done;
	}

	private TaxonPageTemplates getTaxonPageTemplateRoot(String dsName) {
		String mute = "";
		synchronized (mute) {
			FileReader reader = null;
			try {
				String file2read = this.getFileLocation(dsName);
				log.debug("File2Read: " + file2read);
				reader = new FileReader(file2read);
				TaxonPageTemplates tps = (TaxonPageTemplates) TaxonPageTemplates
				.unmarshalTaxonPageTemplates(reader);
				if(reader != null){
					log.debug("Closing resource");
					reader.close();
				}
				return tps;
						
			} catch (Exception ee) {
				if(reader != null){
					try{
					reader.close();
					}
					catch(Exception exe){
						
					}
				}
				log.error(ee.getMessage());
			}
			return null;
		}
	}

	private XslPage getXSLPageType(TaxonPageTemplates tps, String dsName,
			String searchPage, String searchType, String xslName,
			String isDefault) {
		String mute = "";
		synchronized (mute) {

			XslPageType xslPageType = null;
			XslPage page = null;
			log.debug("isDefault: " + isDefault);
			Boolean bool = new Boolean(isDefault);
			boolean defaultFile = bool.booleanValue();
			log.debug("defaultFile: " + defaultFile);
			try {

				int counter = tps.getTaxonPageTemplateCount();

				for (int i = 0; i < counter; i++) {

					TaxonPageTemplateType tp = tps.getTaxonPageTemplate(i);
					String ds = tp.getDatasourceName();
					if (ds.equalsIgnoreCase(dsName.trim())) {
						log.debug("Found datasource: " + ds);
						XslFileNamesType xslFileNames = tp.getXSLFileNames();

						if ((searchPage == null)
								|| (searchPage.trim().equals(""))) {
							log.debug("It is a taxon Page");
							xslPageType = xslFileNames.getXslTaxonPages();
						} else {

							if (EFGImportConstants.SEARCH_PLATES_TYPE
									.equalsIgnoreCase(searchType)) {
								log.debug("It is a plate");
								xslPageType = xslFileNames.getXslPlatePages();
							} else {
								log.debug("It is a list");
								xslPageType = xslFileNames.getXslListPages();
							}
						}

						for (int j = 0; j < xslPageType.getXslPageCount(); ++j) {// find
																					// out
																					// if
																					// it
																					// exists
							XslPage currentPage = xslPageType.getXslPage(j);
							String currentXSLName = currentPage.getFileName();
							log.debug("currentXSLName : " + currentXSLName);
							if (currentXSLName.equalsIgnoreCase(xslName)) {// if
																			// this
																			// file
																			// name
																			// already
																			// exists
								log.debug("currentXSLName is found");
								currentPage.setGroups(null);
								currentPage.setIsDefault(defaultFile);
								page = currentPage;
							} else {
								if (defaultFile) {
									log
											.debug("Selected xsl file is a default file..Set current to false");
									currentPage.setIsDefault(false);
								} else {
									log.debug("Selected xsl file is not a default file");

								}
							}
						}
					
						break;
					}
				}
			} catch (Exception ee) {
				log.error(ee.getMessage());
				log.error("Returning null because of previuos error!!");
				return null;
			}
			if (page == null) {
				page = new XslPage();
				page.setFileName(xslName);
				page.setIsDefault(defaultFile);
				xslPageType.addXslPage(page);
				
				log
						.debug("Setting empty groups for current XslPage with xslName: "
								+ xslName);
			}
			GroupsType groupsType = new GroupsType();
			page.setGroups(groupsType);
			return page;
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
				log.debug("No errors");
				String searchPage = (String) req
						.getParameter(EFGImportConstants.SEARCH_PAGE_STR);// "search"

				if (searchPage != null) {// allow multiple xslNames
					log.debug("Forward to Test Search page");
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
					log.debug("Forward to Test taxon page");
					dispatcher = getServletContext().getRequestDispatcher(
							EFGImportConstants.TEST_TAXON_CONFIG_PAGE);
				}
			} else {
				log.debug("Forward to error page1");
				dispatcher = getServletContext().getRequestDispatcher(
						EFGImportConstants.TEMPLATE_ERROR_PAGE);
			}
		} catch (Exception ee) {
			log.debug("Forward to error page2");
			log.error(ee.getMessage());
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
		log.debug("ID: " + id);
		if (gt == null) {
			log.debug("Group Type is null");
		} else {
			log.debug("Group Type is not null");
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

		String[] splits = key.split(":");
		log.debug("Splits.length: " + splits.length);
		if (splits.length == 0) {
			log.debug("Returning null because length is zero");
			return null;
		}
		identifier = splits[0];

		if (splits.length > 1) {
			groupID = splits[1];
			log.debug("GroupID: " + groupID);
		}
		if (splits.length > 2) {
			groupRank = splits[2];
			log.debug("GroupRank: " + groupRank);
		}
		if (splits.length > 3) {
			characterRank = splits[3];
			log.debug("characterRank: " + characterRank);
		}
		if ("".equals(groupID)) {
			log.debug("GroupID is the empty string");
			return null;
		}
		if ("".equals(groupRank)) {
			log.debug("GroupRank is the empty string");
			return null;
		}

		// if same rank and id already exists return it
		GroupType group = checkGroup(groups, groupID, Integer
				.parseInt(groupRank));

		if ((EFGImportConstants.GROUP_LABEL)
				.equalsIgnoreCase(identifier.trim())) {// group label or groupText label
			log.debug("Is a groupLabel with text: " + text);
			if ((text != null) || (!text.trim().equals(""))) {
				group.setLabel(text);
			}
		}
		else if ((EFGImportConstants.GROUP_TEXT_LABEL)
				.equalsIgnoreCase(identifier.trim())) {// group label or groupText label
			log.debug("Is a groupText: " + text);
			if ((text != null) || (!text.trim().equals(""))) {
				group.setText(text);
			}
		}
		else {
			if (("".equals(characterRank)) || (characterRank == null)) {
				log.debug("characterRank is the empty string or null");
				return null;
			}
			if ((text == null) || (text.trim().equals(""))) {
				log.debug("text is the empty string or null");
				return null;
			}
			CharacterValue cv = null;
			cv = findCharacter(group, Integer.parseInt(characterRank));

			if (cv == null) {
				log.debug("CharacterValue is  null");
				GroupTypeItem vGroupTypeItem = new GroupTypeItem();
				cv = new CharacterValue();
				cv.setRank(Integer.parseInt(characterRank));
				vGroupTypeItem.setCharacterValue(cv);
				group.addGroupTypeItem(vGroupTypeItem);
				log.debug("characterRank is: " + characterRank);
			}
			if ((EFGImportConstants.CHARACTER_LABEL)
					.equalsIgnoreCase(identifier.trim())
					|| ((EFGImportConstants.GROUP_TEXT_LABEL)
							.equalsIgnoreCase(identifier.trim()))) {
				log.debug("CharacterValue is a label");
				if ((EFGImportConstants.CHARACTER_LABEL)
						.equalsIgnoreCase(identifier.trim())) {// character
					// label
					log.debug("CharacterValue has label: " + text);
					cv.setLabel(text);
				} else {
					log.debug("CharacterValue has text: " + text);
					cv.setText(text); // character text
				}
			} else if ((EFGImportConstants.GROUP).equalsIgnoreCase(identifier
					.trim())) {// group
				log.debug("CharacterValue is a group");
				if (text != null) {
					log.debug("CharacterValue text is not null");
					if (!text.trim().equals("")) {
						log.debug("CharacterValue text is not empty string");
						if (returnValue == null) {
							log.debug("returnValue is null");
							returnValue = text;
						}
						log.debug("CharacterValue text is set to: " + text);
						cv.setValue(text);
					}
				} else {
					log.debug("CharacterValue text is null");
				}
			}
		}
		log.debug("Return value is: " + returnValue);
		return returnValue;
	}
}
