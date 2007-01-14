/**
 * 
 */
package project.efg.util;

import java.util.regex.Pattern;

/**
 * @author kasiedu
 *
 */
public interface RegularExpresionConstants {

	 String matchNumberDashNumber = "\\d+\\s*--?\\s*\\d+";
	 String COMMASEP = ",";
	 String ORSEP = "OR";
	//used for server name construction
	 String COLONSEP = ":";
	 String ESCAPECOLONSEP = "\\\\:";
	 String SEMICOLON = ";";
	 String PIPESEP = "\\|";
	 String ESCAPEPIPESEP = "\\\\\\|";
	 String PIPESEP_PARSE="|";
	 String DASHSEP = "-";
	 String DASHSEPREG = "\\-";
	 String ESCAPEDASHSEPREG = "\\\\\\-";
	 String LEFTPARENSEP = "\\(";
	 String ESCAPELEFTPARENSEP = "\\\\\\(";
	 String LEFTPAREN="(";
	 String RIGHTPAREN=")";
	 String matchNumberStr = "\\d+$";
	 String RIGHTPARENSEP = "\\)";
	 String ESCAPERIGHTPARENSEP = "\\\\\\)";
	 String[] STR_2_REMOVE = {"\\(","\\)"};
	 String CARAT_SEPARATOR="\\^";
	 String ESCAPECARAT_SEPARATOR="\\\\\\^";
	 String ORPATTERN = "\\s*or\\s*";
	 String ANDPATTERN = "\\s*and\\s*";
	 String ORCOMMAPATTERN="([,]|([ ]+(or)[ ]))";
	 String ESCAPEORCOMMAPATTERN="\\\\([,]|([ ]+(or)[ ]))";
	 String LISTSEP="#";
	 String ESCAPELISTSEP="\\\\#";
	 String COLON_SEPARATOR = COLONSEP;
	 String NOPATTERN ="----#----";
	 String matchNumbeAtEnd = "^\\d+$|^-\\d+$";
	//A number followed by 2 dashes 
	 String matchNumberDashDash = "\\d+\\s*-\\s*-";
	//	A number followed by a dashes 
	  String matchNumberDash = "\\d+\\s*-";
	 String patternStr = "[A-Z]+";// 
	//PATTERNS
	 Pattern matchNumberDashNumberPattern = Pattern.compile(matchNumberDashNumber);
	 Pattern matchNumberDashDashPattern = Pattern.compile(matchNumberDashDash);
	 Pattern matchNumberDashPattern = Pattern.compile(matchNumberDash);
	 Pattern matchNumberPattern = Pattern.compile(matchNumbeAtEnd);
	 Pattern matchNumberAtEndPattern = Pattern.compile(matchNumberStr);
	 Pattern spacePattern = Pattern.compile("\\s");
	 Pattern equalsPattern = Pattern.compile("=");
	 Pattern colonPattern = Pattern.compile(COLON_SEPARATOR);
	 Pattern caratPattern = Pattern.compile(CARAT_SEPARATOR);
	 Pattern rightParenPattern = Pattern.compile(RIGHTPARENSEP, 
	Pattern.CASE_INSENSITIVE);
	 Pattern leftParenPattern = Pattern.compile(LEFTPARENSEP, 
	Pattern.CASE_INSENSITIVE);
	 Pattern dashParenPattern =  Pattern.compile(DASHSEPREG, 
	Pattern.CASE_INSENSITIVE);
	 Pattern pipePattern = Pattern.compile(PIPESEP, Pattern.CASE_INSENSITIVE);
	 Pattern catPattern = Pattern.compile(ORCOMMAPATTERN, Pattern.CASE_INSENSITIVE);
	 Pattern commaPattern = Pattern.compile(COMMASEP, Pattern.CASE_INSENSITIVE);
	 Pattern noPattern = Pattern.compile(NOPATTERN, Pattern.CASE_INSENSITIVE);
	 Pattern listSepPattern = Pattern.compile(LISTSEP, Pattern.CASE_INSENSITIVE);
	 Pattern alphaPattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
	//ESCAPE PATTERNS
	 Pattern escapecolonPattern = Pattern.compile(ESCAPECOLONSEP);
	 Pattern escapepipePattern = Pattern.compile(ESCAPEPIPESEP,
	Pattern.CASE_INSENSITIVE);
	 Pattern escapecatPattern = Pattern.compile(ESCAPEORCOMMAPATTERN, 
	Pattern.CASE_INSENSITIVE);
	 Pattern escapecommaPattern = Pattern.compile(COMMASEP, 
	Pattern.CASE_INSENSITIVE);
	 Pattern escapenoPattern = Pattern.compile(NOPATTERN, 
	Pattern.CASE_INSENSITIVE);
	 Pattern escapelistSepPattern = Pattern.compile(ESCAPELISTSEP, 
	Pattern.CASE_INSENSITIVE);
	 Pattern escapedashParenPattern =  Pattern.compile(ESCAPEDASHSEPREG, 
	Pattern.CASE_INSENSITIVE);
	 Pattern escapeleftParenPattern = Pattern.compile(ESCAPELEFTPARENSEP, 
	Pattern.CASE_INSENSITIVE);
	 Pattern escaperightParenPattern = Pattern.compile(ESCAPERIGHTPARENSEP, 
	Pattern.CASE_INSENSITIVE);
	 Pattern escapecaratPattern = Pattern.compile(ESCAPECARAT_SEPARATOR);

}
