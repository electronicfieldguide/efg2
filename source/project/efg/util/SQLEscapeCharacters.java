/**
 * 
 */
package project.efg.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kasiedu
 *
 */
public class SQLEscapeCharacters {
	//read from a properties file
	public static List keyCharacters = new ArrayList();
	static{
		keyCharacters.add(";");
		keyCharacters.add("(");
		keyCharacters.add(")");
		keyCharacters.add(",");
		keyCharacters.add(":");
		keyCharacters.add("%");
		keyCharacters.add("-");
		keyCharacters.add("?");
		keyCharacters.add("'");
		keyCharacters.add("+");
		keyCharacters.add("_");
		keyCharacters.add("*");
		keyCharacters.add("/");
		keyCharacters.add("<");
		keyCharacters.add(">");
		keyCharacters.add("=");
		keyCharacters.add("&");
		keyCharacters.add("|");
		keyCharacters.add("^");
		keyCharacters.add("[");
		keyCharacters.add("]");
		keyCharacters.add("\\");
		keyCharacters.add("@");
		keyCharacters.add("~");
		keyCharacters.add("!");
		keyCharacters.add("$");
		keyCharacters.add("#");
		keyCharacters.add(".");
	}
	
}
