/**
 * 
 */
package project.efg.util;
import java.io.File;
/**
 * @author kasiedu
 *
 */
public class FileUtils {

	

	/**
	 *
	 * Some simple file manipulation utilities.
	 *
	 */


	  //Add any additional type that we have valid readers for later.
	  private static final String[] image_type = {"jpg", "jpeg", "png", "gif"};


	  /**
	   *
	   * Extracts the file extension from the given file.
	   *
	   * @param File f - The file to get the extension from.
	   *
	   * @return String - The file extension.
	   *
	   */
	  public static String getFileExtension(File f)
	  {
	    if(f != null)
	    {
	      return getFileExtension(f.getName());
	    }
		return null;
	  }

	  public static String getFileExtension(String name)
	  {
	    if(name != null)
	    {
	      int index = name.lastIndexOf(".");
	      if(index == -1) return null;

	      if(index == 0) return null;

	      if(index == (name.length()-1)) return null;

	      return name.substring(index+1);
	    }
		return null;
	  }

	  /**
	   *
	   * Determines whether or no the given file has an extension that matches
	   * one of the valid image types listed in the image_type arrary.
	   *
	   * @param File f - The image file to determine from.
	   * 
	   * @return boolean - true if this image has valid format, false otherwise.
	   *
	   */
	  public static boolean isValidImageType(File f)
	  {
	    return isValidImageType(getFileExtension(f));
	  }

	  public static boolean isValidImageType(String ext)
	  {
	    if(ext != null)
	    {
	      ext = ext.toLowerCase();  
	      for(int i=0; i<image_type.length; i++)
	      {
	        if(ext.equals(image_type[i]))
	        {
	          return true;
	        }
	      }
	    }
	    return false;
	  }
}
