package project.efg.util;

import java.io.FileReader;
import java.util.Hashtable;

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

/**
 * @author kasiedu
 * 
 */
public class TemplatePopulator implements EFGImportConstants{
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(TemplatePopulator.class);
		} catch (Exception ee) {
		}
	}
	private Hashtable tableStore = new Hashtable(); 
	/**
	 * 
	 */
	public TemplatePopulator() {
	}
	public Hashtable populateTable(String fileName, String xslName, String itemType, String dsName)
	{
		String mutex="";
		synchronized (mutex) {
			String key = fileName + "_" + xslName + "_" + itemType + "_" + dsName;
			log.debug("Key: " + key);
			Hashtable table = (Hashtable)tableStore.get(key);
			
			if(table == null){
				log.debug("table is null");
				table = getTable(fileName,xslName,itemType,dsName);
				tableStore.put(key,table);
			}
			return table;
		}
		
	}
	/**
	 * @param fileName
	 * @param xslName
	 * @param itemType
	 * @return
	 */
	private Hashtable getTable(String fileName, String xslName, String itemType, String dsName) {
		
		TaxonPageTemplates tps = getTaxonPageTemplate(fileName);
		if(tps == null){
			log.debug("tps is null");
			return null;
		}
		XslPage xslPage = getXSL(tps,xslName,itemType,dsName);
		if(xslPage != null){
			log.debug("xslPage is not null");
			return populateTable(xslPage);
		}
		return new Hashtable();
	}
	/**
	 * @param xslPage
	 * @return
	 */
	private Hashtable populateTable(XslPage xslPage) {
		GroupsType groups = xslPage.getGroups();
		boolean isDefault = xslPage.getIsDefault();
		Hashtable table = new Hashtable();
		table.put(EFGImportConstants.ISDEFAULT_STR,isDefault+"");
		
		if(groups != null){
			log.debug("groups is not null");
			this.handleGroups(groups,table);
		}
		return table;
	}
	/**
	 * @param groups
	 */
	private void handleGroups(GroupsType groups,Hashtable table) {
		int counter = groups.getGroupsTypeItemCount();
		
		for(int i=0; i < counter;i++){
			GroupsTypeItem items = groups.getGroupsTypeItem(i);
			if(items == null){
				continue;
			}
			GroupType group = items.getGroup();
			if(group == null){
				continue;
			}
			int groupCount = group.getGroupTypeItemCount();
			
			String groupID = group.getId();
			int groupRank = group.getRank();			
			String groupLabel = group.getLabel();
			String groupText = group.getText();
			String key = null;
			String constructor =EFGImportConstants.COLON_SEPARATOR+  groupID + EFGImportConstants.COLON_SEPARATOR + groupRank;
			if((groupLabel != null)&&(!groupLabel.trim().equals(""))){
				key = EFGImportConstants.GROUP_LABEL + constructor;
				table.put(key,groupLabel);
			}
			if((groupText != null)&&(!groupText.trim().equals(""))){
				key = EFGImportConstants.GROUP_TEXT_LABEL + constructor;
				table.put(key,groupText);
			}
			
			for(int jj=0; jj < groupCount; jj++){
				GroupTypeItem item = group.getGroupTypeItem(jj);
				CharacterValue characterVal = item.getCharacterValue();
				String charLabel = characterVal.getLabel();
				int charRank= characterVal.getRank();
				String charText = characterVal.getText();
				String charValue = characterVal.getValue();
				if((charLabel != null)&&(!charLabel.trim().equals(""))){
					key = EFGImportConstants.CHARACTER_LABEL + constructor +
					EFGImportConstants.COLON_SEPARATOR + charRank;
					table.put(key,charLabel);
				}
				if((charText != null)&&(!charText.trim().equals(""))){
					key = EFGImportConstants.CHARACTER_TEXT_LABEL + constructor+
					EFGImportConstants.COLON_SEPARATOR + charRank;
					table.put(key,charText);
				}
				if((charValue != null)&&(!charValue.trim().equals(""))){
					key = EFGImportConstants.GROUP + constructor+
					EFGImportConstants.COLON_SEPARATOR + charRank;
					table.put(key,charValue);
				}
				GroupsType groupsT = item.getGroups();
				if(groupsT != null){
					handleGroups(groupsT,table);
				}
			}
			
		}
		
	}
	private XslPage getXSL(TaxonPageTemplates tps, String xslName, String xslType, String dsName) {

		
		XslPage page = null;
		

		try {

			int counter = tps.getTaxonPageTemplateCount();

			for (int i = 0; i < counter; i++) {
				XslPageType xslPageType = null;
				TaxonPageTemplateType tp = tps.getTaxonPageTemplate(i);
				String ds = tp.getDatasourceName();
				if (ds.equalsIgnoreCase(dsName.trim())) {
					log.debug("Found datasource: " + ds);
					XslFileNamesType xslFileNames = tp.getXSLFileNames();

					if (EFGImportConstants.TAXONPAGE_XSL
							.equalsIgnoreCase(xslType)) {
							log.debug("It is a taxon Page");
						xslPageType = xslFileNames.getXslTaxonPages();

					} else if (EFGImportConstants.SEARCHPAGE_PLATES_XSL
							.equalsIgnoreCase(xslType)) {
						log.debug("It is a plate");
						xslPageType = xslFileNames.getXslPlatePages();
					} else{
						log.debug("It is a list");
						xslPageType = xslFileNames.getXslListPages();
						
					}
					if(xslPageType == null){
						break;
					}
					for (int j = 0; j < xslPageType.getXslPageCount(); ++j) {// find
						XslPage currentPage = xslPageType.getXslPage(j);
						String currentXSLFile = currentPage.getFileName();
						if(currentXSLFile.equalsIgnoreCase(xslName)){
							page = currentPage; 
							break;
						}
					}

					break;
				}
			}
		} catch (Exception ee) {
			log.error(ee.getMessage());
			log.error("Returning null because of previous error!!");
			return null;
		}
		
		return page;
	}

	private TaxonPageTemplates getTaxonPageTemplate(String fileName) {
		String mute = "";
		synchronized (mute) {
			FileReader reader = null;
			try {
				String file = fileName;

				log.debug("File2Read: " + file);
				reader = new FileReader(file);
				TaxonPageTemplates tps = (TaxonPageTemplates) TaxonPageTemplates
						.unmarshalTaxonPageTemplates(reader);
				if (reader != null) {
					log.debug("Closing resource");
					reader.close();
				}
				return tps;

			} catch (Exception ee) {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception exe) {

					}
				}
				log.error(ee.getMessage());
			}
			return null;
		}
	}
	public static void main(String[] args) {
	
	}

}
