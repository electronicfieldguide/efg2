/**
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Matthew Passell<mpassell@cs.umb.edu>
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it 
 * and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2, or 
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 */

package project.efg.util;
import java.io.PrintStream;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * Objects of this class represent fields of WeaklyTyped objects added
 * to the database.  It is analogous to the java.lang.reflect.Field
 * class, but provides information specific to the EFG, such as
 * whether the field holds an image name or is an ecological field. 
 */
public class EFGField 
    implements Comparable, java.io.Serializable {
  
    private Double weight;
    private String name, legalName, holdsMultipleValues, dataType, javaType;
    private String numericValue, parserable,ordered, numericRange,searchable, speciesPageData;
  
     
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(EFGField.class); 
	}
	catch(Exception ee){
	}
    }
    /**
     * Empty default constructor to allow EFGField objects to be beans.
     */
    public EFGField() {
	name = "";
	parserable="";
	legalName = "";
	holdsMultipleValues = "";
	dataType = "";
	javaType = "";
	numericValue="";
	ordered="";
	numericRange="";
	searchable="";
	speciesPageData="";
    }

    /**
     * Provided to implement the Comparable interface.  Ranks fields
     * according first to weight and then by lexicographical name order.
     * In other words, this function first compares the weights and if
     * they are equal, calls compareTo on the names.
     */
    public int compareTo(Object o) {
	EFGField oField = (EFGField)o;
	double relativeWeight = 
	    weight.doubleValue() - oField.weight.doubleValue();

	if (relativeWeight != 0.0)
	    return (int)(relativeWeight*10.0);
	else return name.compareTo(oField.name);
    }

    /**
     * Makes equals consistent with compareTo.  If compareTo returns
     * zero for Object o, equals will return true.
     */
    public boolean equals(Object o) {
	EFGField oField = (EFGField)o;
	double relativeWeight = 
	    weight.doubleValue() - oField.weight.doubleValue();

	if (relativeWeight != 0.0)
	    return false;
	else return name.equals(oField.name);    
    }

    /**
     * Returns the weight of the field.
     */
    public Double getWeight() {
	return weight;
    }

    /**
     * Sets the weight of the field.
     * @param newVal the new value for the weight
     */
    public void setWeight(Double newVal) {
	weight = newVal;
    }

    /**
     * Returns the name of the field.
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the name of the field.
     * @param newVal the new value for the name
     */
    public void setName(String newVal) {
	name = newVal;
    }

    /**
     * Get the value of legalName.
     * @return the value of legalName
     */
    public String getLegalName() {
	return legalName;
    }
  
    /**
     * Set the value of legalName.
     * @param value the value to assign to legalName
     */
    public void setLegalName(String value) {
	this.legalName = value;
    }
  
    /**
     * Get the value of searchable field.
     * @return the value of searchable
     */
    public String getSearchable() {
	return searchable;
    }
    /**
     * Set the value of searchable.
     * @param value the value to assign to searchable
     */
    public void setSearchable(String value) {
	this.searchable = value;
    }
    /**
     * Get the value of speciesPageData field.
     * @return the value of speciesPageData
     */
    public String getSpeciesPageData() {
	return speciesPageData;
    }
    /**
     * Set the value of speciesPageData.
     * @param value the value to assign to speciesPageData
     */
    public void setSpeciesPageData(String value) {
	this.speciesPageData = value;
    }
    
    /**
     * Get the value of holdsMultipleValues.
     * @return the value of holdsMultipleValues
     */
    public String getHoldsMultipleValues() {
	return holdsMultipleValues;
    }
    /**
     * Set the value of holdsMultipleValues.
     * @param value the value to assign to holdsMultipleValues
     */
    public void setHoldsMultipleValues(String value) {
	this.holdsMultipleValues = value;
    }
    
    /**
     * Returns the dataType of the field.
     */
    public String getDataType() {
	return dataType;
    }
    /**
     * Sets the dataType of the field.
     * @param newVal the new value for the dataType
     */
    public void setDataType(String newVal) {
	log.debug("name: " + name + "   DataType: " + newVal);
	dataType = newVal;
    }
    /**
     * Returns the name of the Java class used to hold values 
     * for this field.
     */
    public String getJavaType() {
	return javaType;
    }  
    /**
     * Sets the javaType of the field.
     * @param newVal the new value for the javaType
     */
    public void setJavaType(String newVal) {
	javaType = newVal;
    }
    /**
     * Get the value of numericValue.
     * @return the value of numericValue.
     */
    public String getNumericValue(){
	return this.numericValue;
    }
    /**
     * Returns true if the field ia a numeric value.
     */
    public boolean isNumericValue(){
	return this.numericValue.equalsIgnoreCase(EFGImportConstants.YES);
    }
    /**
     * Sets the numericValue of the field.
     * @param newVal the new value for the numericValue
     */
    public void setNumericValue(String newVal){
	this.numericValue = newVal;
    }
    /**
     * Get the value of numericRange.
     * @return the value of numericRange.
     */
    public String getNumericRange(){
	return this.numericRange;
    }
    /**
     * Returns true if the field ia a numeric range.
     */
    public boolean isNumericRange(){
	return this.numericRange.equalsIgnoreCase(EFGImportConstants.YES);
    }
    /**
     * Sets the numericRange of the field.
     * @param newVal the new value for the numericRange.
     */
    public void setNumericRange(String newVal){
	this.numericRange = newVal;
    }
    /**
     * Get the value of parserable.
     * @return the value of parserable.
     */
    public String getParserable(){
	return this.parserable;
    }
    /**
     * Returns true if the field is a parserable field.
     */
    public boolean isParserable(){
	return this.parserable.equalsIgnoreCase(EFGImportConstants.YES);
    }
    /**
     * Sets the numericRange of the field.
     * @param newVal the new value for the parserable field.
     */
    public void setParserable(String newVal){
	this.parserable = newVal;
    }
    /**
     * Get the value of ordered.
     * @return the value of ordered.
     */
    public String getOrdered(){
	return this.ordered;
    }
    /**
     * Returns true if the field ordered='yes'.
     */
    public boolean isOrdered(){
	return this.ordered.equalsIgnoreCase(EFGImportConstants.YES);
    }
    /**
     * Sets the ordered of the field.
     * @param newVal the new value for the ordered field.
     */
    public void setOrdered(String newVal){
	this.ordered = newVal;
    }
    /**
     * Returns true if the dataType is a mediaresource type.
     */
    public boolean isMediaResource() {
	return dataType.equalsIgnoreCase(EFGImportConstants.MEDIARESOURCETYPE);
    }
    public boolean isEFGList(){
	return dataType.equalsIgnoreCase(EFGImportConstants.EFGLISTTYPE);
    }
    /**
     * Returns true if the field is searchable.
     */
    public boolean isSearchable() {
	return this.searchable.equalsIgnoreCase(EFGImportConstants.YES);
    }
    /**
     * Returns true if the field holds species page data.
     */
    public boolean isSpeciesPageData() {
	return this.speciesPageData.equalsIgnoreCase(EFGImportConstants.YES);
    }

    /**
     * Returns true if the field holds a list of values.
     */
    public boolean isMultipleValue() {
	return holdsMultipleValues.equalsIgnoreCase(EFGImportConstants.YES);
    }

    /**
     * Displays the object's contents.
     */
    public void display(PrintStream out, String baseIndent) {
	String newIndent = baseIndent+"  ";

	out.println(baseIndent+"EFGField:");
	out.println(newIndent+"weight: "+weight);
	out.println(newIndent+"name: "+name);
	out.println(newIndent+"legalName: "+legalName);
	out.println(newIndent+"searchable: "+searchable);
	out.println(newIndent+"speciesPageData: "+speciesPageData);
	out.println(newIndent+"holdsMultipleValues: "+holdsMultipleValues);
	out.println(newIndent+"parserable: "+parserable);
	out.println(newIndent+"ordered: "+ordered);
	out.println(newIndent+"numeric: "+numericValue);
	out.println(newIndent+"numericRange: "+numericRange);
	out.println(newIndent+"dataType: "+dataType);
	out.println(newIndent+"javaType: "+javaType);
    }

    /**
     * Returns a text view on the class.
     */
    public String toString() {
	return "EFGField"+
	    "\n\t\tweight: "+weight+
	    "\n\t\tname: "+name+
	    "\n\t\tlegalName: "+legalName+
	    "\n\t\tsearchable: "+searchable+
	    "\n\t\tspeciesPageData: "+speciesPageData+
	    "\n\t\tholdsMultipleValues: "+holdsMultipleValues+
	    "\n\t\tparserable: "+parserable+
	    "\n\t\tordered: "+ordered+
	    "\n\t\tnumeric: "+numericValue+
	    "\n\t\tnumericRange: "+numericRange+
	    "\n\t\tdataType: "+dataType+
	    "\n\t\tjavaType: "+javaType;
    }
}

//$Log$
//Revision 1.1  2006/01/25 21:03:48  kasiedu
//Initial revision
//
//Revision 1.1.1.1  2003/10/17 17:03:03  kimmylin
//no message
//
//Revision 1.2  2003/08/20 18:45:40  kimmylin
//no message
//
