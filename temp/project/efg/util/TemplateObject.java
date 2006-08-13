/**
 * 
 */
package project.efg.util;

import java.io.Serializable;

/**
 * @author kasiedu
 *
 */
public class TemplateObject implements Serializable{
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
}
