package project.efg.util;

import org.apache.log4j.Logger;

/**
 * @author kasiedu
 * 
 */
public class TemplateProducer implements EFGImportConstants{
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(TemplateProducer.class);
		} catch (Exception ee) {
		}
	}

	

	int characterRankCounter = 0;

	int groupRankCounter = 0;

	int groupIDCounter = 0;

	String currentGroup = "";

	String currentGroupLabel = "";

	String currentGroupText = "";

	String currentCharacter = "";

	String currentCharacterLabel = "";

	String currentCharacterText = "";

	/**
	 * 
	 */
	public TemplateProducer() {
	}
	/**
	 * 
	 * @param isNewGroupID - true, means Create a new group id
	 * @param isNewGroupRank - true means Create a new group rank
	 * @return a unique string that signifies this group text
	 */
	public String getGroupText(boolean isNewGroupID, boolean isNewGroupRank){
		this.currentGroupText = add(isNewGroupID, isNewGroupRank,false, GROUP_TEXT_LABEL);
		return this.currentGroupText;
	}
	/**
	 * 
	 * @param label - Generate the current group text for the given label
	 * @return a groupText representation of the given label
	 */
	public String getCurrentGroupText(String label) {
		this.currentGroupText = this.extractType(label,GROUP_TEXT_LABEL);
		return this.currentGroupText;
	}
	/**
	 * 
	 * @param isNewGroupID - 
	 * @param isNewGroupRank
	 * @return
	 */
	public String getGroup(boolean isNewGroupID, boolean isNewGroupRank){
		this.currentGroup = this.add(isNewGroupID, isNewGroupRank,false, GROUP);
		return this.currentGroup;
	}

	public String getCurrentGroup(String label) {
		this.currentGroup = this.extractType(label,GROUP);
		return this.currentGroup;
	}
/**
 * Create a label for a group
 * @param isNewGroupID
 * @param isNewGroupRank
 * @return
 */
	public String getGroupLabel(boolean isNewGroupID, boolean isNewGroupRank){
		this.currentGroupLabel = add(isNewGroupID, isNewGroupRank,false, GROUP_LABEL);
		return this.currentGroupLabel;
	}

	public String getCurrentGroupLabel(String label) {
		this.currentGroupLabel = this.extractType(label,GROUP_LABEL);
		return this.currentGroupLabel;
	}

	/**
	 * Create a label for a character
	 * @param isNewGroupID
	 * @param isNewGroupRank
	 * @return
	 */
	public String getCharacterLabel(boolean isNewGroupID, boolean isNewGroupRank){
		this.currentCharacterLabel = add(isNewGroupID, isNewGroupRank,true, CHARACTER_LABEL);
		return this.currentCharacterLabel;
	}

	public String getCurrentCharacterLabel(String label) {
		this.currentCharacterLabel = this.extractType(label,CHARACTER_LABEL);
		return this.currentCharacterLabel;
	}
	public String getCharacter(boolean isNewGroupID, boolean isNewGroupRank){
		this.currentCharacter = this.add(isNewGroupID, isNewGroupRank,true,GROUP);
		return this.currentCharacter;
	}

	public String getCurrentCharacter(String label) {
		this.currentCharacter = this.extractType(label,GROUP);
		return this.currentCharacter;
	}

	public String getCharacterText(boolean isNewGroupID, boolean isNewGroupRank) {
		this.currentCharacterText = add(isNewGroupID, isNewGroupRank,true,
				CHARACTER_TEXT_LABEL);
		return this.currentCharacterText;
	}

	public String getCurrentCharacterText(String label) {
		this.currentCharacterText = this.extractType(label,CHARACTER_TEXT_LABEL);
		return this.currentCharacterText;
	}
	/**
	 * Replace the Group or Character string with replacement
	 * @param label
	 * @param replacement
	 * @return
	 */
	private String extractType(String label, String replacement){
		String type = "";
		if((label == null) || (label.trim().equals(""))){
			label = this.getGroup(true,true);
		}
		if(label.indexOf(GROUP) > -1){
			type = label.replaceFirst(GROUP, replacement);
		}
		else if(label.indexOf(GROUP_TEXT_LABEL) > -1){
			type = label.replaceFirst(GROUP_TEXT_LABEL, replacement);
		}
		else if(label.indexOf(GROUP_LABEL) > -1){
			type = label.replaceFirst(GROUP_LABEL, replacement);
		}
		else if(label.indexOf(CHARACTER_TEXT_LABEL) > -1){
			type = label.replaceFirst(CHARACTER_TEXT_LABEL, replacement);
		}
		else{
			type = label;
		}
		return type;
	}

/**
 * 
 * @param isNewGroupID
 * @param isNewGroupRank
 * @param isCharacter
 * @param type
 * @return
 */
	private String add(boolean isNewGroupID, boolean isNewGroupRank, boolean isCharacter,String type) {
		incrementGroupID(isNewGroupID);
		incrementGroupRank(isNewGroupRank);
		if(isCharacter){
			incrementCharacter(isCharacter);
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(type);
		buffer.append(groupConstants(isCharacter));
		return buffer.toString();
	}

	

	private String groupConstants(boolean isCharacter) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(COLON_SEPARATOR);
		buffer.append(this.groupIDCounter);
		buffer.append(COLON_SEPARATOR);
		buffer.append(this.groupRankCounter);
		if(isCharacter){
			buffer.append(COLON_SEPARATOR);
			buffer.append(this.characterRankCounter);
		}
		return buffer.toString();
	}
	private void incrementCharacter(boolean isCharacter){
		if(isCharacter){
			++this.characterRankCounter;
		}
	}
	private void incrementGroupID(boolean isNewGroupID) {
		if (isNewGroupID) {
			++this.groupIDCounter;
			resetGroupRank();
		}
	}
	private void resetGroupRank() {
		//this.groupRankCounter = 0;
		this.resetCharacterRank();
	}
	private void resetCharacterRank() {
		this.characterRankCounter = 0;

	}
	private void incrementGroupRank(boolean isNewGroupRank) {
		if (isNewGroupRank) {
			++this.groupRankCounter;
			this.resetCharacterRank();
		}
	}

	public static void main(String[] args) {
		/*TemplateProducer tp = new TemplateProducer();
		boolean isNewID = true;
		boolean isNewRank = true;
		System.out.println("Group Text: "
				+ tp.getGrouptText(isNewID, isNewRank));
		System.out.println("Group Label: "
				+ tp.getGroupLabel(isNewID, isNewRank));
		System.out.println("Group: " + tp.getGroup(isNewID, isNewRank));
		System.out.println("CharacterLabel: "
				+ tp.getCharacterLabel(isNewID, isNewRank));
		System.out.println("Character: " + tp.getCharacter(isNewID, isNewRank));*/
	}

}
