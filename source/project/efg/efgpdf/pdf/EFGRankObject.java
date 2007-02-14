/**
 * 
 */
package project.efg.efgpdf.pdf;

/**
 * @author jacob.asiedu
 *
 */
public abstract class EFGRankObject {
	private int rank;
	public EFGRankObject(){
		this.rank = 0;
	}
	public int getRank(){
		return this.rank;
	}
	public void setRank(int rank){
		this.rank = rank;
	}
	public abstract boolean equals(Object obj);
	
	public abstract int hashCode();
}
