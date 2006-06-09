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
 * A type for submitters of EFGDocuments
 * 
 * @version $Revision$ $Date$
 */
public class SubmitterType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _submitterKey
     */
    private int _submitterKey;

    /**
     * keeps track of state for field: _submitterKey
     */
    private boolean _has_submitterKey;

    /**
     * Field _name
     */
    private java.lang.String _name;


      //----------------/
     //- Constructors -/
    //----------------/

    public SubmitterType() {
        super();
    } //-- project.efg.efgDocument.SubmitterType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'submitterKey'.
     * 
     * @return the value of field 'submitterKey'.
     */
    public int getSubmitterKey()
    {
        return this._submitterKey;
    } //-- int getSubmitterKey() 

    /**
     * Method hasSubmitterKey
     */
    public boolean hasSubmitterKey()
    {
        return this._has_submitterKey;
    } //-- boolean hasSubmitterKey() 

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
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'submitterKey'.
     * 
     * @param submitterKey the value of field 'submitterKey'.
     */
    public void setSubmitterKey(int submitterKey)
    {
        this._submitterKey = submitterKey;
        this._has_submitterKey = true;
    } //-- void setSubmitterKey(int) 

    /**
     * Method unmarshalSubmitterType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSubmitterType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.SubmitterType) Unmarshaller.unmarshal(project.efg.efgDocument.SubmitterType.class, reader);
    } //-- java.lang.Object unmarshalSubmitterType(java.io.Reader) 

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
