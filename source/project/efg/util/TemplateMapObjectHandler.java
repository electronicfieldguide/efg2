/**
 * 
 */
package project.efg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;

/**
 * @author kasiedu
 * 
 */
public class TemplateMapObjectHandler {
	private static long fileDate;

	private static Hashtable currentMap;

	public static boolean hasChanged(String mapLocation) {
		try {
			if (mapLocation == null) {
				return false;
			}
			File file = new File(mapLocation);
			if (!file.exists()) {
				return false;
			}

			if (file.lastModified() > fileDate) {// reload.. file has changed
				return true;
			}

		} catch (Exception ee) {

			LoggerUtilsServlet.logErrors(ee);
		}
		return false;
	}

	private static String getMapKey(String datafn) {
		StringBuffer querySearch = new StringBuffer("/");
		querySearch.append(EFGImportConstants.EFG_APPS);
		querySearch.append("/search?dataSourceName=");
		querySearch.append(datafn);

		return querySearch.toString().toLowerCase();
	}

	/**
	 * 
	 * @param key
	 * @return the object removed from cache Sync with loaded class too
	 */
	public static boolean removeFromTemplateMap(String datasourceName,
			String mapLocation) {

		String mutex = "";
		synchronized (mutex) {
			if (mapLocation == null) {
				System.out.println("ERROR: Call createTemplateObjectMap(mapLocation) before youc all this method!!");
				return false;
			}
			FileChannel channel = null;

			// lock the file

			try {

				Hashtable templateObjectMap = getTemplateObjectMap(mapLocation);

				if (templateObjectMap == null) {
					System.out.println("Template Map object is null");
					return false;
				}
				File file = new File(mapLocation);
				// lock file
				String key = null;

				List keysToremove = new ArrayList();

				String tempKey = getMapKey(datasourceName);
				// iterate over map and remove key if found
				Iterator mapIter = templateObjectMap.keySet().iterator();

				while (mapIter.hasNext()) {
					key = (String) mapIter.next();
					if (key.toLowerCase().startsWith(tempKey)) {
						System.out.println("key: " + key);
						System.out.println("temp key: " + tempKey);
						keysToremove.add(key);
						System.out.println("removed key: " + key);
					}
				}
				System.out.println("Size of keys: " + keysToremove.size() );
				for (int i = 0; i < keysToremove.size(); i++) {
					templateObjectMap.remove((String) keysToremove.get(i));
				}
				if (keysToremove.size() > 0) {
					FileOutputStream fos = new FileOutputStream(file);
					ObjectOutputStream out = new ObjectOutputStream(fos);
					channel = fos.getChannel();
					out.writeObject(templateObjectMap);
					out.close();
					out.flush();
					fileDate = file.lastModified();
				}

				if ((channel != null) && (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
				return true;
				// release lock
			} catch (Exception ee) {
				System.err.println(ee.getMessage());
				ee.printStackTrace();
				
				if ((channel != null) && (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
				LoggerUtilsServlet.logErrors(ee);
				return false;

			} finally {

				if ((channel != null) && (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @param templateObject
	 *            sync with file, by saving it to file
	 */
	public static boolean changeDisplayNameTemplateMap(String datasourceName,
			String newDisplayName, String mapLocation) {

		String mutex = "";
		synchronized (mutex) {
			if (mapLocation == null) {
				EFGUtils
						.log("ERROR: Call createTemplateObjectMap(mapLocation) before youc all this method!!");
				return false;
			}
			FileChannel channel = null;

			// check date and reset then write to file
			StringBuffer message = new StringBuffer(
					"About to  change to new display name '");
			message.append(newDisplayName);
			message.append("'");

			// log.debug(message);
			EFGUtils.log(message.toString());
			// lock the file

			try {

				Hashtable templateObjectMap = getTemplateObjectMap(mapLocation);

				if (templateObjectMap == null) {
					System.out.println("Template Map object is null");
					return false;
				}
				File file = new File(mapLocation);
				// lock file
				String key = null;

				String tempKey = getMapKey(datasourceName);
				// iterate over map and remove key if found
				Iterator mapIter = templateObjectMap.keySet().iterator();
				boolean isChanged = false;
				while (mapIter.hasNext()) {
					key = (String) mapIter.next();
					if (key.toLowerCase().startsWith(tempKey)) {
						TemplateObject top = (TemplateObject) templateObjectMap
								.get(key);
						EFGDisplayObject edo = top.getDisplayObject();
						edo.setDisplayName(newDisplayName);
						top.setDisplayObject(edo);
						templateObjectMap.put(key, top);
						isChanged = true;
					}
				}
				if (isChanged) {
					FileOutputStream fos = new FileOutputStream(file);
					ObjectOutputStream out = new ObjectOutputStream(fos);
					channel = fos.getChannel();
					out.writeObject(templateObjectMap);
					out.close();
					out.flush();
					fileDate = file.lastModified();
				}

				if ((channel != null) && (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
				return true;
				// release lock
			} catch (Exception ee) {

				if ((channel != null) && (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
				LoggerUtilsServlet.logErrors(ee);
				return false;

			} finally {

				if ((channel != null) && (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
			}
		}

	}

	/**
	 * 
	 * @param key
	 * @param templateObject
	 *            sync with file, by saving it to file
	 */
	public static void add2TemplateMap(String key,
			TemplateObject templateObject, String mapLocation) {
		String mutex = "";
		synchronized (mutex) {
			if (templateObject == null) {
				return;
			}
			if (mapLocation == null) {
				EFGUtils
						.log("ERROR: Call createTemplateObjectMap(mapLocation) before youc all this method!!");
				return;
			}
			FileChannel channel = null;

			// check date and reset then write to file
			String message = "About to add key : " + key + " object: "
					+ templateObject.toString();
			// log.debug(message);
			EFGUtils.log(message);
			// lock the file
			
			try {
				Hashtable templateObjectMap = getTemplateObjectMap(mapLocation);
				
				if (templateObjectMap == null) {
					templateObjectMap = new Hashtable();
					currentMap = templateObjectMap;
				}

				if (templateObjectMap == null) {
					EFGUtils.log("Template is null in add");
					return;
				}
			
				// lock file
				templateObjectMap.put(key, templateObject);
				File file = new File(mapLocation);
				FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				channel = fos.getChannel();
				out.writeObject(templateObjectMap);
				out.close();
				out.flush();
				fileDate = file.lastModified();

				if ((channel != null) && (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}

				// release lock
			} catch (Exception ee) {
				System.out.println(ee.getMessage());
				ee.printStackTrace();
				if ((channel != null) && (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}

				LoggerUtilsServlet.logErrors(ee);
			} finally {

				if ((channel != null) && (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
			}
		}

	}

	// unmodifiable map
	public static Hashtable getTemplateObjectMap(String mapLocation) {
		String mutex = "";
		synchronized (mutex) {
			ObjectInputStream in = null;
			System.out.println("File : " + mapLocation);
			
			File file = new File(mapLocation);
			if (!file.exists()) {
				System.out.println("File : " + file.getAbsolutePath() + " does not exists!!");
				return null;
			}
			System.out.println("File : " + file.getAbsolutePath() + " exists!!");
			if (hasChanged(mapLocation)) {
				// lock file
				try {
					in = new ObjectInputStream(new FileInputStream(file));
					currentMap = (Hashtable) in.readObject();
					in.close();
					fileDate = file.lastModified();
				} catch (Exception ee) {
					ee.printStackTrace();
					LoggerUtilsServlet.logErrors(ee);
				}
			}

			return currentMap;
		}
	}

}
