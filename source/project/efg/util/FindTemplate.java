/**
 * 
 */
package project.efg.util;

import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;

import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslFileNamesType;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;

/**
 * @author kasiedu
 * 
 */
public class FindTemplate {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(FindTemplate.class);
		} catch (Exception ee) {
		}
	}

	private String realPath;

	private String dsName;

	private String xslType;

	private TaxonPageTemplates tps;

	private String xslFileName;

	public FindTemplate(String realPath, String dsName, String xslType) {
		this.realPath = realPath;
		this.dsName = dsName;
		this.xslType = xslType;
		this.tps = this.getTaxonPageTemplate();
	}

	public String getXSLFileName() {
		if (this.xslFileName == null) {
			if (this.tps != null) {
				this.xslFileName = this.getXSL();
			}
		}
		return this.xslFileName;
	}

	private String getXSL() {

		XslPageType xslPageType = null;
		String xslFile = null;
		String currentXSLFile = null;

		try {

			int counter = tps.getTaxonPageTemplateCount();

			for (int i = 0; i < counter; i++) {

				TaxonPageTemplateType tp = tps.getTaxonPageTemplate(i);
				String ds = tp.getDatasourceName();
				if (ds.equalsIgnoreCase(this.dsName.trim())) {
					log.debug("Found datasource: " + ds);
					XslFileNamesType xslFileNames = tp.getXSLFileNames();

					if (EFGImportConstants.TAXONPAGE_XSL
							.equalsIgnoreCase(this.xslType)) {
							log.debug("It is a taxon Page");
						xslPageType = xslFileNames.getXslTaxonPages();

					} else if (EFGImportConstants.SEARCHPAGE_PLATES_XSL
							.equalsIgnoreCase(this.xslType)) {
						log.debug("It is a palte");
						xslPageType = xslFileNames.getXslPlatePages();
					} else{
						log.debug("It is a list");
						xslPageType = xslFileNames.getXslListPages();
						
					}

					for (int j = 0; j < xslPageType.getXslPageCount(); ++j) {// find
						XslPage currentPage = xslPageType.getXslPage(j);
						currentXSLFile = currentPage.getFileName();
						boolean isDefault = currentPage.getIsDefault();
						log.debug("currentXSLName : " + currentXSLFile);
						if (isDefault) {// if
							xslFile = currentXSLFile;
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
		if (xslFile == null) {
			return currentXSLFile;
		}
		return xslFile;
	}

	private TaxonPageTemplates getTaxonPageTemplate() {
		String mute = "";
		synchronized (mute) {
			FileReader reader = null;
			try {
				String file = this.realPath + File.separator + this.dsName.toLowerCase()
						+ EFGImportConstants.XML_EXT;

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
}
