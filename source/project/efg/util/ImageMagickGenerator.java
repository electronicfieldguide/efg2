/**
 * 
 */
package project.efg.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgInterface.ThumbNailGeneratorInterface;

/**
 * @author kasiedu
 * 
 */
public class ImageMagickGenerator extends ThumbNailGeneratorInterface {

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ImageMagickGenerator.class);
		} catch (Exception ee) {
		}
	}

	private static String CONVERT_PROG = "convert";

	private String magicHome;

	static protected int IMAGE_QUALITY = 100;

	private ThumbNailGeneratorInterface defaultGenerator;

	public ImageMagickGenerator(String environmentalVariable) {
		
		this.init(environmentalVariable);

	}

	private void init(String environmentalVariable) {
		Properties props = EFGUtils.getEnvVars();
		if (props != null) {
			this.magicHome = props.getProperty(environmentalVariable);
		}
		//this.magicHome = System.getenv(environmentalVariable);
	
		if (this.magicHome == null || this.magicHome.trim().equals("")) {
			this.defaultGenerator = new ThumbNailGenerator();
		} else {
			this.magicHome = this.magicHome + File.separator + CONVERT_PROG;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project.efg.Imports.efgInterface.ThumbNailGeneratorInterface#generateThumbNail(java.lang.String,
	 *      java.lang.String, java.lang.String, int)
	 */
	public boolean generateThumbNail(String src, String destSrc,
			String fileName, int maxDim) {
		try {
			if (this.defaultGenerator == null) {
				
				
				create(src, destSrc, fileName, maxDim);
			} else {
				
				this.defaultGenerator.generateThumbNail(src, destSrc, fileName,
						maxDim);
			}
			return true;
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
		return false;
	}

	/**
	 * Uses a Runtime.exec()to use imagemagick to perform the given conversion
	 * operation. Returns true on success, false on failure. Does not check if
	 * either file exists.
	 * 
	 * @param in
	 *            Description of the Parameter
	 * @param out
	 *            Description of the Parameter
	 * @param newSize
	 *            Description of the Parameter
	 * @param quality
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private boolean convert(File in, File out, int width, int height) {
		// System.out.println("convert(" + in.getPath()+ ", " + out.getPath()+
		// ", " + newSize + ", " + quality);

		/*
		 * if (IMAGE_QUALITY < 0 || IMAGE_QUALITY > 100) { IMAGE_QUALITY = 75; }
		 */

		List command = new ArrayList(10);

		// note: CONVERT_PROG is a class variable that stores the location of
		// ImageMagick's convert command
		// it might be something like "/usr/local/magick/bin/convert" or
		// something else, depending on where you installed it.
		command.add(this.magicHome);
		command.add("-geometry");
		command.add(width + "x" + height);
		command.add("-quality");
		command.add("" + IMAGE_QUALITY);
		command.add(in.getAbsolutePath());
		command.add(out.getAbsolutePath());

		// System.out.println(command);

		return exec((String[]) command.toArray(new String[1]));
	}

	/**
	 * Tries to exec the command, waits for it to finsih, logs errors if exit
	 * status is nonzero, and returns true if exit status is 0 (success).
	 * 
	 * @param command
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private boolean exec(String[] command) {
		Process proc;

		try {
			// System.out.println("Trying to execute command " +
			// Arrays.asList(command));
			proc = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			log.error("IOException while trying to execute " + command);

			return false;
		}

		// System.out.println("Got process object, waiting to return.");

		int exitStatus;

		while (true) {
			try {
				exitStatus = proc.waitFor();
				break;
			} catch (java.lang.InterruptedException e) {
				System.out.println("Interrupted: Ignoring and waiting");
			}
		}
		if (exitStatus != 0) {
			// System.out.println("Error executing command: " + exitStatus);
		}
		return (exitStatus == 0);
	}

	private void create(String srcDir, String outDir, String imageName, int dim) {

		try {
			String currentInDir = srcDir + File.separator + imageName;
			String currentOutDir = outDir + File.separator + imageName;
			File in = new File(currentInDir);
			File out = new File(currentOutDir);
			convert(in, out, dim, dim);

		} catch (Exception ioe) {
			log.error(ioe.getMessage());
		}
	}
}
