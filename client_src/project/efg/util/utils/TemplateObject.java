/**
 * 
 */
package project.efg.util.utils;

import java.io.Serializable;

/**
 * @author kasiedu
 *
 */
public class TemplateObject implements Serializable, java.lang.Comparable{
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
public int compareTo(Object object1) {
	TemplateObject obj1 = (TemplateObject)object1;
	if(this.getDisplayObject().compareTo(obj1.getDisplayObject()) == 0){
	    if(this.getTemplateName().compareTo(obj1.getTemplateName()) == 0){
		if((this.getGUID() != null ) && (obj1.getGUID() != null)){
		    return this.getGUID().compareTo(obj1.getGUID());
		}
		return this.toString().compareTo(obj1.toString());
	    }
		return this.getTemplateName().compareTo(obj1.getTemplateName());
	}
	return this.getDisplayObject().compareTo(obj1.getDisplayObject());
	
}

public boolean equals(Object obj){
	TemplateObject obj1 = (TemplateObject)obj;
	if(this.getGUID() != null){
	    return this.getGUID().equals(obj1.getGUID());
	}
	if(this.getDisplayObject().equals(obj1.getDisplayObject())){
	    return this.getTemplateName().equals(obj1.getTemplateName());
	}
	return this.getDisplayObject().equals(obj1.getDisplayObject());
}
    public int hashCode(){
	if(this.getGUID() != null){
	    return this.getGUID().hashCode();
	}
	return this.getDisplayObject().hashCode() * this.getTemplateName().hashCode();
    }
}
