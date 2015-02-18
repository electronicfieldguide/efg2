/**
 * 
 */
package project.efg.util.pdf.utils;

/**
 * @author jacob.asiedu
 *
 */
public class EFGLine {
	private float lineWidth;
	private int distanceFromParagraph;
	
	public EFGLine(float lineWidth,int distanceFromParagraph){
		this.setDistanceFromParagraph(distanceFromParagraph);
		this.setLineWidth(lineWidth);
	}
	public EFGLine(){
		this(0.5f,4);
	}
	public int getDistanceFromParagraph() {
		return this.distanceFromParagraph;
	}
	public void setDistanceFromParagraph(int distanceFromParagraph) {
		this.distanceFromParagraph = distanceFromParagraph;
	}
	public float getLineWidth() {
		return this.lineWidth;
	}
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

}
