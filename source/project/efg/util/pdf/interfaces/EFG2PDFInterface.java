/**
 * 
 */
package project.efg.util.pdf.interfaces;

import java.io.File;
import java.io.OutputStream;

import project.efg.templates.taxonPageTemplates.XslPage;


/**
 * @author jacob.asiedu
 *
 */
public interface EFG2PDFInterface {


	public abstract void writePdfToStream(project.efg.efgDocument.EFGDocument efgdoc,
			XslPage xslPage,OutputStream output,File mediaResourcesDirectory, String authors);

	
}