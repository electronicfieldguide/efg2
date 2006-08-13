/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package project.efg.efgDocument;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * This type is used by most of the elements as a base
 * type.Contains name and ordered attributes
 * 
 * @version $Revision$ $Date$
 */
public class BaseEFGAttributeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The name of the character or pseudo character
     */
    private java.lang.String _name;

    /**
     * 'true' if the order of the elements in the container matters
     */
    private boolean _ordered = false;

    /**
     * keeps track of state for field: _ordered
     */
    private boolean _has_ordered;

    /**
     * Some databases do not allow spaces in their field names,
     * this attribute is used for databases that cannot easily
     * handle field names with spaces in them.
     */
    private java.lang.String _databaseName;


      //----------------/
     //- Constructors -/
    //----------------/

    public BaseEFGAttributeType() {
        super();
    } //-- project.efg.efgDocument.BaseEFGAttributeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteOrdered
     */
    public void deleteOrdered()
    {
        this._has_ordered= false;
    } //-- void deleteOrdered() 

    /**
     * Returns the value of field 'databaseName'. The field
     * 'databaseName' has the following description: Some databases
     * do not allow spaces in their field names, this attribute is
     * used for databases that cannot easily handle field names
     * with spaces in them.
     * 
     * @return the value of field 'databaseName'.
     */
    public java.lang.String getDatabaseName()
    {
        return this._databaseName;
    } //-- java.lang.String getDatabaseName() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: The name of the character or pseudo
     * character
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'ordered'. The field 'ordered'
     * has the following description: 'true' if the order of the
     * elements in the container matters
     * 
     * @return the value of field 'ordered'.
     */
    public boolean getOrdered()
    {
        return this._ordered;
    } //-- boolean getOrdered() 

    /**
     * Method hasOrdered
     */
    public boolean hasOrdered()
    {
        return this._has_ordered;
    } //-- boolean hasOrdered() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'databaseName'. The field
     * 'databaseName' has the following description: Some databases
     * do not allow spaces in their field names, this attribute is
     * used for databases that cannot easily handle field names
     * with spaces in them.
     * 
     * @param databaseName the value of field 'databaseName'.
     */
    public void setDatabaseName(java.lang.String databaseName)
    {
        this._databaseName = databaseName;
    } //-- void setDatabaseName(java.lang.String) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: The name of the character or pseudo
     * character
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'ordered'. The field 'ordered' has
     * the following description: 'true' if the order of the
     * elements in the container matters
     * 
     * @param ordered the value of field 'ordered'.
     */
    public void setOrdered(boolean ordered)
    {
        this._ordered = ordered;
        this._has_ordered = true;
    } //-- void setOrdered(boolean) 

    /**
     * Method unmarshalBaseEFGAttributeType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalBaseEFGAttributeType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.BaseEFGAttributeType) Unmarshaller.unmarshal(project.efg.efgDocument.BaseEFGAttributeType.class, reader);
    } //-- java.lang.Object unmarshalBaseEFGAttributeType(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
