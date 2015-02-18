/**
 * 
 */
package project.efg.client.interfaces.nogui;


/**
 * @author kasiedu
 *
 */
public abstract class ThumbNailGeneratorInterface {
	
	/**
	 *@param src - The full path to src file .
	 *@param dest - The full path to destination file.
	 
	 *@param maxDim - The maximum length of the dimensions for the new Image 
	 */
	public abstract boolean generateThumbNail(String src, String destSrc,
			String fileName, int maxDim);
}