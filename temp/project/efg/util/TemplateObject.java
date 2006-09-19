/**
 * 
 */
package project.efg.util;

import java.io.Serializable;

/**
 * @author kasiedu
 *
 */
public class TemplateObject implements Serializable, java.util.Comparator{
 /**
	 * 
	 */
private static final long serialVersionUID = 1L;
private EFGDisplayObject displayObject;
 private String templateName;
 private String guid;
 public TemplateObject(){
	 
 }
 public void setDisplayObject(EFGDisplayObject displayObject){
	 this.displayObject = displayObject;
 }
 public EFGDisplayObject getDisplayObject(){
	 return this.displayObject;
 }
 public void setTemplateName(String templateName){
	 this.templateName = templateName;
 }
 public String getTemplateName(){
	 return this.templateName;
 }
 public String toString(){
	return "Template: " + this.getTemplateName() + " Guid: " + this.getGUID();
 }
 public void setGUID(String guid){
	 this.guid = guid;
 }
 public String getGUID(){
	 return this.guid;
 }
/* (non-Javadoc)
 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
 */
public int compare(Object object1, Object object2) {
	TemplateObject obj1 = (TemplateObject)object1;
	TemplateObject obj2 = (TemplateObject)object2;
	
	
	return obj1.getDisplayObject().compareTo(obj2.getDisplayObject());
}

public boolean equals(Object obj){
	TemplateObject obj1 = (TemplateObject)obj;
	if(this.getDisplayObject().equals(obj1.getDisplayObject())){
		return this.getTemplateName().equals(obj1.getTemplateName());
	}
	return this.getDisplayObject().equals(obj1.getDisplayObject());
}
public int hashCode(){
	return this.getDisplayObject().hashCode() * this.getTemplateName().hashCode();
}
}
