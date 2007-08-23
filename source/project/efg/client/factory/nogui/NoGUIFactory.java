/**
 * 
 */
package project.efg.client.factory.nogui;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.net.URI;

import org.apache.log4j.Logger;

import project.efg.client.impl.nogui.EFGDatasourceObjectImpl;
import project.efg.client.interfaces.gui.DataManipulatorInterface;
import project.efg.client.interfaces.gui.EFGDatasourceObjectListInterface;
import project.efg.client.interfaces.gui.ImportBehavior;
import project.efg.client.interfaces.gui.SynopticKeyTreeInterface;
import project.efg.client.interfaces.nogui.CSV2DatabaseAbstract;
import project.efg.client.interfaces.nogui.EFGDataExtractorInterface;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.client.utils.gui.IllegalCharactersDataCheckerCaller;
import project.efg.client.utils.gui.MediaResourceDataCheckerCaller;
import project.efg.client.utils.nogui.DataCheckerCaller;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.DBObject;

import com.Ostermiller.util.CSVParse;
import com.Ostermiller.util.ExcelCSVParser;
import com.Ostermiller.util.LabeledCSVParser;

/**
 * @author jacob.asiedu
 *
 */
public class NoGUIFactory {
	static Logger log;
	static {
		try {
			log = Logger.getLogger(NoGUIFactory.class);
		} catch (Exception ee) {
		}
	}
	public NoGUIFactory(){
		
	}
	public static DataCheckerCaller
	getDataCheckerCallerInstance(DBObject dbObject, String ds,String checkerType){
		
		if((checkerType == null) || 
				(checkerType.trim().equals(""))){
			
			return new MediaResourceDataCheckerCaller(dbObject,ds);
		}
		else if(checkerType.trim().equalsIgnoreCase(EFGImportConstants.MEDIARESOURCE)){
			
			return new MediaResourceDataCheckerCaller(dbObject,ds);
		}
		else if(checkerType.trim().equalsIgnoreCase(EFGImportConstants.ILLEGALCHARACTER_STRING)){
			return new IllegalCharactersDataCheckerCaller(dbObject,ds);
		}
	
		return new MediaResourceDataCheckerCaller(dbObject,ds);
		
		
	}
	public static synchronized DataManipulatorInterface 
	getDataManipulatorInstance(SynopticKeyTreeInterface tree,
				String manipulatorType){
		try{
			Class cls =  Class.forName(manipulatorType);
		       Constructor constructor
		         = cls.getConstructor( new Class[]{ 
		        		 project.efg.client.interfaces.gui.SynopticKeyTreeInterface.class} 
		         );
		       Object object
		         = constructor.newInstance( new Object[]{tree});
		       return (DataManipulatorInterface)object; 
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Could not find \"" + 
					manipulatorType + 
					"\" on class path!!"
					);
		}	
		return null;
	}
	/**
	 * A factory method that creates CSVParse objects
	 * @param csvFileName - The csv file to parse
	 * @param delimiter - The delimiter to use to parse date
	 * @return a CSVParse object
	 * @throws Exception
	 * @see com.Ostermiller.util.CSVParse
	 */
	public static synchronized CSVParse getCSVParser
	(Reader csvFileName, char delimiter)throws Exception{
			return new LabeledCSVParser(new ExcelCSVParser(csvFileName,
					delimiter));
	}
	/**
	 * Factory method for making 
	 * @param datasource
	 * @param dataExtractor
	 * @param dbObject
	 * @param isUpdate
	 * @param templateConfigHome TODO
	 * @return CSV2DatabaseAbstract 
	 */
		public synchronized static CSV2DatabaseAbstract 
		getDatabaseObject(EFGDatasourceObjectInterface datasource,
				EFGDataExtractorInterface dataExtractor,
				DBObject dbObject,
				ImportBehavior isUpdate) {
			CSV2DatabaseAbstract csv = 
				SpringNoGUIFactory.getCSV2DatabaseAbstract();
			csv.setDatasource(datasource);
			csv.setDBObject(dbObject);
			csv.setDataExtractor(dataExtractor);
			csv.setISUpdate(isUpdate);
			return csv;
		}
		
		private static EFGDataExtractorInterface getType() {
			return SpringNoGUIFactory.getCSVProcessor();
		}
		
		public synchronized static  EFGDataExtractorInterface getDataExtractor(URI csvFileName) throws Exception {
			EFGDataExtractorInterface efg = getType();
			efg.setFile(csvFileName);
			return efg;
		}

		/**
		 * Defaults to a comma separated delimiter if none is specified.
		 * 
		 * @param csvFileName-
		 *            The full URI to the file to be parsed
		 * @param delimiter -
		 *            The delimiter to use in parsing file
		 * @throws Exception
		 */
		public synchronized static EFGDataExtractorInterface getDataExtractor(URI csvFileName, char delimiter)
				throws Exception {
			EFGDataExtractorInterface efg = getType();
			efg.setFile(new FileReader(new File(csvFileName)),delimiter);
			return efg;
		}

		/**
		 * Defaults to a comma separated delimiter if none is specified.
		 * 
		 * @param in -
		 *            A stream containing data to be imported
		 * @throws Exception 
		 */
		public  synchronized static EFGDataExtractorInterface getDataExtractor(InputStream in) throws Exception {
			EFGDataExtractorInterface efg = getType();
			efg.setFile(new InputStreamReader(in));
			return efg;
		}
		/* (non-Javadoc)
		 * @see project.efg.Imports.efgInterface.EFGDatasourceObjectListFactoryInterface#getEFGObjectList()
		 */
		public  static EFGDatasourceObjectListInterface 
		getEFGObjectList(DBObject dbObject) {
			return new project.efg.client.rdb.nogui.EFGDatasourceObjectListImpl(dbObject);
		}
		/**
		 * 
		 * @param in -
		 *            A stream containing data to be imported
		 * @param delimiter -
		 *            The delimiter to use in parsing stream
		 * @throws Exception 
		 */
		public synchronized static EFGDataExtractorInterface getDataExtractor(InputStream in, char delimiter) throws Exception {
			EFGDataExtractorInterface efg = getType();
			efg.setFile(in, delimiter);
			return efg;
		}

		/**
		 * Defaults to a comma separated delimiter if none is specified.
		 * 
		 * @param in -
		 *            A Reader object containing data to be imported
		 * @throws Exception 
		 */
		public synchronized static EFGDataExtractorInterface getDataExtractor(Reader in) throws Exception {
			EFGDataExtractorInterface efg = getType();
			efg.setFile(in, ',');
			return efg;
		}

		/* (non-Javadoc)
		 * @see project.efg.Imports.efgInterface.EFGDatasourceObjectFactoryInterface#getEFGDatasourceObject()
		 */
		public static EFGDatasourceObjectInterface getEFGDatasourceObject(URI dataName, String templateDisplayName,
				String displayName) {
			return new EFGDatasourceObjectImpl(dataName,templateDisplayName,
					displayName);
		}
		public static EFGDatasourceObjectInterface getEFGDatasourceObject() {
			return SpringNoGUIFactory.getEFGDatasourceObject();
		}
}
