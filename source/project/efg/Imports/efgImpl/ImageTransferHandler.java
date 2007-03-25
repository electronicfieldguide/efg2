/**
 * 
 */
package project.efg.Imports.efgImpl;

import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.util.DnDFileBrowser;
import project.efg.util.DnDFileBrowserMain;
import project.efg.util.EFGCopyFilesThread;
import project.efg.util.EFGImportConstants;
import project.efg.util.ServerLocator;

/**
 * @author jacob.asiedu
 *
 */
public class ImageTransferHandler {

	public static boolean importSelectedImages(DnDFileBrowser browser,JFrame frame,
			List objectsToDrop,
			JProgressBar progressBar) {
		if(objectsToDrop.size() > 0){
		
			if(isMediaMagickPromptCheckBoxSelected()) {
				Properties props = EFGUtils.getEnvVars();
				String property = null;
				if (props != null) {
					String propertyToUse = 
						EFGImportConstants.EFGProperties.getProperty(
								"efg.images.magickhome.variable"
								);
					property = props.getProperty(propertyToUse);
				}
				String pathToServer = 
					EFGImportConstants.EFGProperties.getProperty(
							"efg.imagemagicklocation.lists",
							property);
				ServerLocator locator = new ServerLocator(
						frame,
						pathToServer,
						true,
						"efg.imagemagicklocation.lists",
						"efg.imagemagicklocation.current",
						"efg.imagemagicklocation.checked",
						"Prompt Me For Image Magick Location Every Time");
					locator.setVisible(true);
	
			}
				
			if(isThumbNailsPromptCheckBoxSelected()){
				EFGThumbNailDimensions thd = 
					new EFGThumbNailDimensions(frame,"Enter Max Dimension",true);
				thd.setVisible(true);
				
				String currentDim = EFGImportConstants.EFGProperties.getProperty(
				"efg.thumbnails.dimensions.current");
				DnDFileBrowserMain.setCurrentDimLabel(currentDim);

			}
		    EFGCopyFilesThread copyFiles = new EFGCopyFilesThread(browser,
		    		objectsToDrop,
		    		progressBar);
		    copyFiles.start();
		   return false;
		}
		return true;
	}
	private static boolean isThumbNailsPromptCheckBoxSelected(){
		String property = 
			EFGImportConstants.EFGProperties.getProperty(
					"efg.thumbnails.dimensions.checked", 
					EFGImportConstants.EFG_TRUE);

		if(property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
			return true;
		}
		return false;
	}
	/**
	 * @return
	 */
	public static boolean isMediaMagickPromptCheckBoxSelected() {
		String property = 
			EFGImportConstants.EFGProperties.getProperty(
					"efg.imagemagicklocation.checked", 
					EFGImportConstants.EFG_TRUE);

		if(property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
			return true;
		}
		return false;
	}

}
