/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package project.efg.templates.taxonPageTemplates;

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
 * Class CharacterValueType.
 * 
 * @version $Revision$ $Date$
 */
public class CharacterValueType extends project.efg.templates.taxonPageTemplates.RankLabelTextType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The name of the field/character of the taxon being
     * described..For example 'FlowerColor'. 
     */
    private java.lang.String _value;


      //----------------/
     //- Constructors -/
    //----------------/

    public CharacterValueType() {
        super();
    } //-- project.efg.templates.taxonPageTemplates.CharacterValueType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'value'. The field 'value' has
     * the following description: The name of the field/character
     * of the taxon being described..For example 'FlowerColor'. 
     * 
     * @return the value of field 'value'.
     */
    public java.lang.String getValue()
    {
        return this._value;
    } //-- java.lang.String getValue() 

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
     * Sets the value of field 'value'. The field 'value' has the
     * following description: The name of the field/character of
     * the taxon being described..For example 'FlowerColor'. 
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(java.lang.String value)
    {
        this._value = value;
    } //-- void setValue(java.lang.String) 

    /**
     * Method unmarshalCharacterValueType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCharacterValueType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.templates.taxonPageTemplates.CharacterValueType) Unmarshaller.unmarshal(project.efg.templates.taxonPageTemplates.CharacterValueType.class, reader);
    } //-- java.lang.Object unmarshalCharacterValueType(java.io.Reader) 

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
