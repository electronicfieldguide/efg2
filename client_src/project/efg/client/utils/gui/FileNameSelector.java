/*
*   Class   FileNameSelector
*
*   Methods for selecting the name and path of a file for use in a program
*   The selected file is NOT opened
*
*   See FileChooser for a class that contains methods that select and OPEN a file
*
*   All folders and files may be displayed or a specific extension, e.g. txt,
*   may be set (the extension filter uses the class FileTypeFilter which is
*   the SUN JAVA filter, ExampleFileFilter, retitled)
*
*   WRITTEN BY: Dr Michael Thomas Flanagan
*
*   DATE:       13 September 2005
*   REVISED:    30 November 2005
*
*   DOCUMENTATION:
*   See Michael Thomas Flanagan's Java library on-line web page:
*   FileNameSelector.html
*
*   Copyright (c) September 2005
*
*   PERMISSION TO COPY:
*   Permission to use, copy and modify this software and its documentation for
*   NON-COMMERCIAL purposes is granted, without fee, provided that an acknowledgement
*   to the author, Michael Thomas Flanagan at www.ee.ucl.ac.uk/~mflanaga, appears in all copies.
*
*   Dr Michael Thomas Flanagan makes no representations about the suitability
*   or fitness of the software for any or for a particular purpose.
*   Michael Thomas Flanagan shall not be liable for any damages suffered
*   as a result of using, modifying or distributing this software or its derivatives.
*
***************************************************************************************/


package project.efg.client.utils.gui;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class FileNameSelector{
  

            // file whose name is to be selected
   
    private String[] extn = null;     // file type extension of files to be displayed
                                    //      default (extn=null) - all file types displayed
	private String dirPath = null;  // path to directory containing selected file name
	private boolean fileFound=false;// true if file named is found
	private JComponent component;
    // constructor
    // opens home directory
    public FileNameSelector(){
    	this(null,null);
      /*  String property = 
        	EFGImportConstants.EFGProperties.getProperty("efg.data.last.file",null);
        this.path = property;*/
    }

    // constructor
    // opens directory given by path
    public FileNameSelector(String path){
        this(null,path);
    }
    // constructor
    // opens directory given by path
    public FileNameSelector(JComponent component,String path){
    	this.component = component;
        this.dirPath = path;
    }

    // use JFileChooser to select the required file
    // uses default prompt ("Select File")
    public List selectFiles(){
        return this.selectFiles("Select Files");
    }

    // use a JFileChooser to select the required file
    // display user supplied prompt
    public List selectFiles(String prompt){

        JFileChooser chooser = new JFileChooser(this.dirPath);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        if(this.extn!=null && this.extn.length > 0){
            // Add filter
        	FileTypeFilter f = new FileTypeFilter(this.extn);
            //f.addExtension(extn);
        	/*StringBuilder descriptions = new StringBuilder();
        	for(int i = 0; i < extn.length;i++) {
        		if(i > 0) {
        			descriptions.append(",");
        		}
        		descriptions.append(extn);
        	}*/
            f.setDescription(f.getDescription());
            chooser.setFileFilter(f);
        }
        else{
            // enable all files displayed option
            chooser.setAcceptAllFileFilterUsed(true);
        }

	    chooser.setDialogTitle(prompt);

	    chooser.showOpenDialog(this.component);
	    
	    File[] files = chooser.getSelectedFiles();
	    if(files==null || files.length == 0) {
	      
	        this.dirPath = null;
	        this.fileFound=false;	    	
	    }
	    else{
	    	for(int i = 0; i < files.length; i++) {
	    		File file = files[i];
	    		this.dirPath = (file.getParentFile()).toString();
	    		this.fileFound=true;	 
	 	        break;
	    	}
	    	return Arrays.asList(files);
	    }

	   // return this.fileName;
	    return null;
	}

    // set path
    public void setDirectoryPath(String path){
        this.dirPath = path;
    }

    // get path
    public String getDirectoryPath(){
        return this.dirPath;
    }

    // set extension - display files with extension extn
    public void setExtension(String[] extn){
        this.extn = extn;
    }
    // Get the file existence status, fileFound.
    public boolean fileFound(){
        return fileFound;
    }
    /**
     * A convenience implementation of FileFilter that filters out
     * all files except for those type extensions that it knows about.
     *
     * Extensions are of the type ".foo", which is typically found on
     * Windows and Unix boxes, but not on Macinthosh. Case is ignored.
     *
     * Example - create a new filter that filerts out all files
     * but gif and jpg image files:
     *
     *     JFileChooser chooser = new JFileChooser();
     *     ExampleFileFilter filter = new ExampleFileFilter(
     *                   new String{"gif", "jpg"}, "JPEG & GIF Images")
     *     chooser.addChoosableFileFilter(filter);
     *     chooser.showOpenDialog(this);
     *
     * @version 1.14 01/23/03
     * @author Jeff Dinkins
     */
    class FileTypeFilter extends javax.swing.filechooser.FileFilter {

 
        private Hashtable filters = null;
        private String description = null;
        private String fullDescription = null;
        private boolean useExtensionsInDescription = true;

        /**
         * Creates a file filter. If no filters are added, then all
         * files are accepted.
         *
         * @see #addExtension
         */
        public FileTypeFilter() {
    	this.filters = new Hashtable();
        }

        /**
         * Creates a file filter that accepts files with the given extension.
         * Example: new FileTypeFilter("jpg");
         *
         * @see #addExtension
         */
        public FileTypeFilter(String extension) {
    	this(extension,null);
        }

        /**
         * Creates a file filter that accepts the given file type.
         * Example: new FileTypeFilter("jpg", "JPEG Image Images");
         *
         * Note that the "." before the extension is not needed. If
         * provided, it will be ignored.
         *
         * @see #addExtension
         */
        public FileTypeFilter(String extension, String description) {
    	this();
    	if(extension!=null) addExtension(extension);
     	if(description!=null) setDescription(description);
        }

        /**
         * Creates a file filter from the given string array.
         * Example: new FileTypeFilter(String {"gif", "jpg"});
         *
         * Note that the "." before the extension is not needed adn
         * will be ignored.
         *
         * @see #addExtension
         */
        public FileTypeFilter(String[] filters) {
    	this(filters, null);
        }

        /**
         * Creates a file filter from the given string array and description.
         * Example: new FileTypeFilter(String {"gif", "jpg"}, "Gif and JPG Images");
         *
         * Note that the "." before the extension is not needed and will be ignored.
         *
         * @see #addExtension
         */
        public FileTypeFilter(String[] filters, String description) {
    	this();
    	for (int i = 0; i < filters.length; i++) {
    	    // add filters one by one
    	    addExtension(filters[i]);
    	}
     	if(description!=null) setDescription(description);
        }

        /**
         * Return true if this file should be shown in the directory pane,
         * false if it shouldn't.
         *
         * Files that begin with "." are ignored.
         *
         * @see #getExtension
         * @see FileFilter#accepts
         */
        public boolean accept(File f) {
    	if(f != null) {
    	    if(f.isDirectory()) {
    		return true;
    	    }
    	    String extension = getExtension(f);
    	    if(extension != null && filters.get(getExtension(f)) != null) {
    		return true;
    	    }
    	}
    	return false;
        }

        /**
         * Return the extension portion of the file's name .
         *
         * @see #getExtension
         * @see FileFilter#accept
         */
         public String getExtension(File f) {
    	if(f != null) {
    	    String filename = f.getName();
    	    int i = filename.lastIndexOf('.');
    	    if(i>0 && i<filename.length()-1) {
    		return filename.substring(i+1).toLowerCase();
    	    }
    	}
    	return null;
        }

        /**
         * Adds a filetype "dot" extension to filter against.
         *
         * For example: the following code will create a filter that filters
         * out all files except those that end in ".jpg" and ".tif":
         *
         *   FileTypeFilter filter = new FileTypeFilter();
         *   filter.addExtension("jpg");
         *   filter.addExtension("tif");
         *
         * Note that the "." before the extension is not needed and will be ignored.
         */
        public void addExtension(String extension) {
    	if(filters == null) {
    	    filters = new Hashtable(5);
    	}
    	filters.put(extension.toLowerCase(), this);
    	fullDescription = null;
        }


        /**
         * Returns the human readable description of this filter. For
         * example: "JPEG and GIF Image Files (*.jpg, *.gif)"
         *
         * @see setDescription
         * @see setExtensionListInDescription
         * @see isExtensionListInDescription
         * @see FileFilter#getDescription
         */
        public String getDescription() {
    	if(fullDescription == null) {
    	    if(description == null || isExtensionListInDescription()) {
     		fullDescription = description==null ? "(" : description + " (";
    		// build the description from the extension list
    		Enumeration extensions = filters.keys();
    		if(extensions != null) {
    		    fullDescription += "." + (String) extensions.nextElement();
    		    while (extensions.hasMoreElements()) {
    			fullDescription += ", ." + (String) extensions.nextElement();
    		    }
    		}
    		fullDescription += ")";
    	    } else {
    		fullDescription = description;
    	    }
    	}
    	return fullDescription;
        }

        /**
         * Sets the human readable description of this filter. For
         * example: filter.setDescription("Gif and JPG Images");
         *
         * @see setDescription
         * @see setExtensionListInDescription
         * @see isExtensionListInDescription
         */
        public void setDescription(String description) {
    	this.description = description;
    	fullDescription = null;
        }

        /**
         * Determines whether the extension list (.jpg, .gif, etc) should
         * show up in the human readable description.
         *
         * Only relevent if a description was provided in the constructor
         * or using setDescription();
         *
         * @see getDescription
         * @see setDescription
         * @see isExtensionListInDescription
         */
        public void setExtensionListInDescription(boolean b) {
    	useExtensionsInDescription = b;
    	fullDescription = null;
        }

        /**
         * Returns whether the extension list (.jpg, .gif, etc) should
         * show up in the human readable description.
         *
         * Only relevent if a description was provided in the constructor
         * or using setDescription();
         *
         * @see getDescription
         * @see setDescription
         * @see setExtensionListInDescription
         */
        public boolean isExtensionListInDescription() {
    	return useExtensionsInDescription;
        }
    }
}