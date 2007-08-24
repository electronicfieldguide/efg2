/**
 * 
 */
package project.efg.server.utils;

/**
 * @author kasiedu
 *
 */
public class EFGFieldObject {
	private String fieldName;
	private String fieldValue;
	public EFGFieldObject(String fieldName,String fieldValue){
		this.setFieldName(fieldName);
		this.setFieldValue(fieldValue);
	}
	public EFGFieldObject(){
		
	}
	public void setFieldName(String fieldName){
		this.fieldName = fieldName;
	}
	public void setFieldValue(String fieldValue){
		this.fieldValue= fieldValue;
	}
	public String  getFieldName(){
		return this.fieldName;
	}
	public String  getFieldValue(){
		return this.fieldValue;
	}
	public String toString(){
		return "FieldName: " + this.getFieldName() + "  FieldValue:" + this.getFieldValue();
	}
	public boolean equals(Object obj){
		EFGFieldObject ff = (EFGFieldObject)obj;
		return this.getFieldName().equalsIgnoreCase(ff.getFieldName()) &&
		this.getFieldValue().equalsIgnoreCase(ff.getFieldValue());
	}
	public int hasCode(){
		return this.getFieldName().hashCode() + "o0o0".hashCode() + this.getFieldValue().hashCode();
	}
}
