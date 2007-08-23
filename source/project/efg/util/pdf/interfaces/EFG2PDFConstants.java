package project.efg.util.pdf.interfaces;

import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

public interface EFG2PDFConstants {

	// read from a properties file
	int DEFAULT_LEFT_MARGIN = 36;

	int DEFAULT_RIGHT_MARGIN = 36;

	int DEFAULT_TOP_MARGIN = 72;

	int DEFAULT_BOTTOM_MARGIN = 36;

	// General Settings

	Rectangle DEFAULT_PAPER_SIZE = PageSize.A4; // map these sizes with a properties
										// file

	// Image Color Settings

	int DEFAULT_WEIGHT_WHITE_SPACE_AROUND_IMAGE = 0;

	/**
	 * maps to Padding around image
	 */
	float DEFAULT_WEIGHT_FRAME_AROUND_IMAGE = 0;

	/**
	 * border weight around image
	 */
	String DEFAULT_COLOR_FOR_FRAME_AROUND_IMAGE = "#0000FF";// border color
															// around image

	int DEFAULT_WEIGHT_WHITE_SPACE_BETWEEN_ELEMENTS = 0;

	/*
	 * image plus captions maps to outer cell padding
	 * 
	 */
	int DEFAULT_WEIGHT_BOUNDING_BOX = 0;

	/*
	 * frame image plus captions maps to border around outer cell
	 */
	String DEFAULT_COLOR_FOR_BOUNDING_BOX = "#00FF00";

	/**
	 * maps to border color around outer cell
	 */

	boolean DEFAULT_FILL_SPACE_AROUND_DISP_IMAGES = false;
	
	boolean DEFAULT_TEXT_ABOVE_IMAGE=false;

		
	int DEFAULT_ROWS = 4;

	int DEFAULT_COLUMNS = 4;
	
	

	
	int DEFAULT_FONTNAME = Font.HELVETICA;

	int DEFAULT_FONTSIZE = Font.DEFAULTSIZE;

	int DEFAULT_FONTTYPE = Font.NORMAL;

	boolean DEFAULT_ISBOLD = false;

	boolean DEFAULT_ISITALICS = false;
	
	boolean DEFAULT_ISUNDERLINE = false;

	boolean DEFAULT_SHOW_ALL_IMAGES = false;

	boolean DEFAULT_SKIP_CELL_IF_NO_IMAGES = true;
	
	float DEFAULT_UNDERLINE_THICKNESS= 0.2f;
	float DEFAULT_UNDERLINE_Y_POSITION=-0.2f;
	
	float DEFAULT_UNDERLINE_HEADER_THICKNESS= 0.8f;
	float DEFAULT_UNDERLINE_HEADER_Y_POSITION=-0.3f;
	float DEFAULT_UNDERLINE_FOOTER_THICKNESS= 0.4f;
	float DEFAULT_UNDERLINE_FOOTER_Y_POSITION=-1f;
}
