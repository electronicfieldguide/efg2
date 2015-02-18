/**
 * 
 */
package project.efg.util.pdf.interfaces;

/**
 * @author jacob.asiedu
 *
 */
public abstract class EFGRankObject {
	private int rank;
	private String name;
	private boolean isDisplay;
	
	public EFGRankObject(){
		this.rank = 0;
		this.isDisplay = false;
	}
	public int getRank(){
		return this.rank;
	}
	public void setRank(int rank){
		
		this.rank = rank;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		
		this.name = name;
	}
	public abstract boolean equals(Object obj);
	
	public abstract int hashCode();
	public boolean isDisplay() {
		return this.isDisplay;
	}
	public void setDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}
}
