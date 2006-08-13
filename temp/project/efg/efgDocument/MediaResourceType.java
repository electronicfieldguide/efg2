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
import project.efg.efgDocument.types.ResourceTypeEnum;

/**
 * Class MediaResourceType.
 * 
 * @version $Revision$ $Date$
 */
public class MediaResourceType extends project.efg.efgDocument.EFGType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Must be one of VIDEO, AUDIO,IMAGE as defiend in resourcesType
     */
    private project.efg.efgDocument.types.ResourceTypeEnum _type = project.efg.efgDocument.types.ResourceTypeEnum.valueOf("Image");

    /**
     * internal content storage
     */
    private java.lang.String _content = "";


      //----------------/
     //- Constructors -/
    //----------------/

    public MediaResourceType() {
        super();
        setType(project.efg.efgDocument.types.ResourceTypeEnum.valueOf("Image"));
        setContent("");
    } //-- project.efg.efgDocument.MediaResourceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
     */
    public java.lang.String getContent()
    {
        return this._content;
    } //-- java.lang.String getContent() 

    /**
     * Returns the value of field 'type'. The field 'type' has the
     * following description: Must be one of VIDEO, AUDIO,IMAGE as
     * defiend in resourcesType
     * 
     * @return the value of field 'type'.
     */
    public project.efg.efgDocument.types.ResourceTypeEnum getType()
    {
        return this._type;
    } //-- project.efg.efgDocument.types.ResourceTypeEnum getType() 

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
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content)
    {
        this._content = content;
    } //-- void setContent(java.lang.String) 

    /**
     * Sets the value of field 'type'. The field 'type' has the
     * following description: Must be one of VIDEO, AUDIO,IMAGE as
     * defiend in resourcesType
     * 
     * @param type the value of field 'type'.
     */
    public void setType(project.efg.efgDocument.types.ResourceTypeEnum type)
    {
        this._type = type;
    } //-- void setType(project.efg.efgDocument.types.ResourceTypeEnum) 

    /**
     * Method unmarshalMediaResourceType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalMediaResourceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.MediaResourceType) Unmarshaller.unmarshal(project.efg.efgDocument.MediaResourceType.class, reader);
    } //-- java.lang.Object unmarshalMediaResourceType(java.io.Reader) 

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
