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
import java.nio.channels.FileLock;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;

/**
 * @author kasiedu
 * 
 */
public class TemplateMapObjectHandler {

	private static Map templateObjectMap;

	private static long fileDate;

	private static String mapLocation;

	/**
	 * 
	 * @param mapLoc
	 */
	private static void initTemplateObjectMap() {
		// find out if it exists load it if it does
		FileChannel channel = null;

		// Use the file channel to create a lock on the file.
		// This method blocks until it can retrieve the lock.
		FileLock lock = null;
		try {
			File mapFile = new File(mapLocation);
			if (!mapFile.exists()) {// create this file
				// use file locking here
				mapFile.createNewFile();
				FileOutputStream fos= new FileOutputStream(mapFile);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				channel = fos.getChannel();
				lock = channel.lock();
				templateObjectMap = Collections.synchronizedMap(new HashMap());
				out.writeObject(templateObjectMap);
				out.close();
				out.flush();
				fileDate = mapFile.lastModified();
				if ((lock != null) && (lock.isValid())){
					try {
						lock.release();
					} catch (IOException e) {
					}
				}
				if( (channel != null)&& (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}

			} else {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(mapFile));
				templateObjectMap = (Map) in.readObject();
				in.close();
				fileDate = mapFile.lastModified();
				// release lock
			}
		} catch (Exception ee) {
			
			if ((lock != null) && (lock.isValid())){
				try {
					lock.release();
				} catch (IOException e) {
				}
			}
			if( (channel != null)&& (channel.isOpen())) {
				try {
					channel.close();
				} catch (IOException e) {

				}
			}
			LoggerUtilsServlet.logErrors(ee);
		} finally {
			try {
				if ((lock != null) && (lock.isValid())){
					try {
						lock.release();
					} catch (IOException e) {
					}
				}
				if( (channel != null)&& (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
			} catch (Exception eee) {

			}
		}

	}

	private static boolean hasChanged() {
	

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
	/**
	 * Load the Map into memory. Refresh it if it is already in memory
	 * 
	 * @param mapLoc
	 */
	public static void createTemplateObjectMap(String mapLoc) {
		String mutex = "";
		synchronized (mutex) {
			mapLocation = mapLoc;
			initTemplateObjectMap();
		}
	}

	/**
	 * 
	 * @param key
	 * @param templateObject
	 *            sync with file, by saving it to file
	 */
	public static void add2TemplateMap(String key, TemplateObject templateObject) {
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
			FileLock lock = null;
			// check date and reset then write to file
			String message = "About to add key : " + key + " object: "
					+ templateObject.toString();
			// log.debug(message);
			EFGUtils.log(message);
			// lock the file
			ObjectInputStream in;
			try {
				File file = new File(mapLocation);
				if (hasChanged()) {
					in = new ObjectInputStream(new FileInputStream(file));

					templateObjectMap = (Map) in.readObject();
					in.close();
					// release lock
				}

				if (templateObjectMap == null) {
					return;
				}
				
				// lock file
				
				FileOutputStream fos= new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				channel = fos.getChannel();
				templateObjectMap.put(key, templateObject);
				out.writeObject(templateObjectMap);
				out.close();
				out.flush();
				fileDate = file.lastModified();
				

				if ((lock != null) && (lock.isValid())){
					try {
						lock.release();
					} catch (IOException e) {
					}
				}
				if( (channel != null)&& (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
			
				// release lock
			} catch (Exception ee) {
				
				if ((lock != null) && (lock.isValid())){
					try {
						lock.release();
					} catch (IOException e) {
					}
				}
				if( (channel != null)&& (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
			
				LoggerUtilsServlet.logErrors(ee);
			} finally {
				if ((lock != null) && (lock.isValid())){
					try {
						lock.release();
					} catch (IOException e) {
					}
				}
				if( (channel != null)&& (channel.isOpen())) {
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
	 * @return the object removed from cache Sync with loaded class too
	 */
	public static Object removeFromTemplateMap(String key) {
		String mutex = "";
		synchronized (mutex) {
			Object obj = null;
			if (mapLocation == null) {
				EFGUtils
						.log("ERROR: Call createTemplateObjectMap(mapLocation) before youc all this method!!");
				return null;
			}
			FileChannel channel = null;
			FileLock lock = null;
			ObjectInputStream in;
			try {
				File file = new File(mapLocation);
				if (hasChanged()) {
					in = new ObjectInputStream(new FileInputStream(file));
					templateObjectMap = (Map)in.readObject();
					in.close();
				}
				if (templateObjectMap != null) {
					FileOutputStream fos= new FileOutputStream(file);
					ObjectOutputStream out = new ObjectOutputStream(fos);
					channel = fos.getChannel();
					// find the key here and remove
					obj = templateObjectMap.remove(key);
					// lock file
					
					out.writeObject(templateObjectMap);
					out.close();
					out.flush();
					fileDate = file.lastModified();
				

					if ((lock != null) && (lock.isValid())){
						try {
							lock.release();
						} catch (IOException e) {
						}
					}
					if( (channel != null)&& (channel.isOpen())) {
						try {
							channel.close();
						} catch (IOException e) {

						}
					}
				}
			} catch (Exception ee) {
				
				if ((lock != null) && (lock.isValid())){
					try {
						lock.release();
					} catch (IOException e) {
					}
				}
				if( (channel != null)&& (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
			
				LoggerUtilsServlet.logErrors(ee);
			} finally {
				if ((lock != null) && (lock.isValid())){
					try {
						lock.release();
					} catch (IOException e) {
					}
				}
				if( (channel != null)&& (channel.isOpen())) {
					try {
						channel.close();
					} catch (IOException e) {

					}
				}
			}
			return obj;
		}
	}

	// unmodifiable map
	public static Map getTemplateObjectMap() {
		String mutex = "";
		synchronized (mutex) {
			ObjectInputStream in = null;
		

			File file = new File(mapLocation);
			if (hasChanged()) {

				// lock file
				try {				
					in = new ObjectInputStream(new FileInputStream(file));
					templateObjectMap = (Map)in.readObject();
					in.close();
					fileDate = file.lastModified();
				} catch (Exception ee) {
					ee.printStackTrace();
					LoggerUtilsServlet.logErrors(ee);
				} 
				// release lock
			}
			if (templateObjectMap != null) {
				return Collections.unmodifiableMap(templateObjectMap);
			}
			return null;
		}
	}

}
