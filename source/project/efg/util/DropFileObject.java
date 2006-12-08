/**
 * 
 */
package project.efg.util;

import java.io.File;

/**
 * @author kasiedu
 *
 */
public class DropFileObject {
	private File srcFile,destFile;
	private FileNode destNode;
	/**
	 * 
	 */
	public DropFileObject(File srcFile, File destFile,FileNode destNode ) {
		this.srcFile = srcFile;
		this.destFile = destFile;
		this.destNode = destNode;
	}
	public File getDestinationFile(){
		return this.destFile;
	}
	public File getSourceFile(){
		return this.srcFile;
	}
	public FileNode getDestinationNode(){
		return this.destNode;
	}
	

}
