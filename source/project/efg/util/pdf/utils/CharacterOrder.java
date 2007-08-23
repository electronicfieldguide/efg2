/**
 * 
 */
package project.efg.util.pdf.utils;

import project.efg.util.pdf.interfaces.EFGRankObject;

/**
 * @author jacob.asiedu
 *
 */
public class CharacterOrder extends EFGRankObject {
	public CharacterOrder(){
		super();
	}
	public boolean equals(Object obj) {
		EFGRankObject ron = (EFGRankObject)obj;
		return this.getRank() == ron.getRank();
	}

	/* (non-Javadoc)
	 */
	public int hashCode() {
		return this.getRank();
	}
	public String toString(){
		return this.getName();
	}
}
