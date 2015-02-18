/**
 * 
 */
package project.efg.util.pdf.utils;

import com.lowagie.text.Font;

/**
 * handles the metrics for a type
 *
 */
public class MetricsObject {
	private Font font;
	private int alignment,distanceFromMargins;
	private float padding;
	public int getAlignment() {
		return this.alignment;
	}
	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}
	public int getDistanceFromMargins() {
		return this.distanceFromMargins;
	}
	public void setDistanceFromMargins(int distanceFromMargins) {
		this.distanceFromMargins = distanceFromMargins;
	}
	public Font getFont() {
		return this.font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public float getPadding() {
		return this.padding;
	}
	public void setPadding(float padding) {
		this.padding = padding;
	}
	
}
