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
 * A group of attributes needed by some types in this schema
 * 
 * @version $Revision$ $Date$
 */
public class EFGType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * A reference (could be a url) to a resource. Applications
     * could use it together to create a url to query an internal
     * or external resource
     */
    private java.lang.String _resourceLink;

    /**
     * An optional caption that could be used for display purposes
     * by an application
     */
    private java.lang.String _caption;

    /**
     * An annotation for the object on which this attribute is
     * found. It could be a description of the object for instance
     */
    private java.lang.String _annotation;

    /**
     * A reference to where this object was obtained from. Must be
     * a reference to a key in the same document.
     */
    private int _efgKeyRef;

    /**
     * keeps track of state for field: _efgKeyRef
     */
    private boolean _has_efgKeyRef;

    /**
     * internal content storage
     */
    private java.lang.String _content = "";


      //----------------/
     //- Constructors -/
    //----------------/

    public EFGType() {
        super();
        setContent("");
    } //-- project.efg.efgDocument.EFGType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteEfgKeyRef
     */
    public void deleteEfgKeyRef()
    {
        this._has_efgKeyRef= false;
    } //-- void deleteEfgKeyRef() 

    /**
     * Returns the value of field 'annotation'. The field
     * 'annotation' has the following description: An annotation
     * for the object on which this attribute is found. It could be
     * a description of the object for instance
     * 
     * @return the value of field 'annotation'.
     */
    public java.lang.String getAnnotation()
    {
        return this._annotation;
    } //-- java.lang.String getAnnotation() 

    /**
     * Returns the value of field 'caption'. The field 'caption'
     * has the following description: An optional caption that
     * could be used for display purposes by an application
     * 
     * @return the value of field 'caption'.
     */
    public java.lang.String getCaption()
    {
        return this._caption;
    } //-- java.lang.String getCaption() 

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
     * Returns the value of field 'efgKeyRef'. The field
     * 'efgKeyRef' has the following description: A reference to
     * where this object was obtained from. Must be a reference to
     * a key in the same document.
     * 
     * @return the value of field 'efgKeyRef'.
     */
    public int getEfgKeyRef()
    {
        return this._efgKeyRef;
    } //-- int getEfgKeyRef() 

    /**
     * Returns the value of field 'resourceLink'. The field
     * 'resourceLink' has the following description: A reference
     * (could be a url) to a resource. Applications could use it
     * together to create a url to query an internal or external
     * resource
     * 
     * @return the value of field 'resourceLink'.
     */
    public java.lang.String getResourceLink()
    {
        return this._resourceLink;
    } //-- java.lang.String getResourceLink() 

    /**
     * Method hasEfgKeyRef
     */
    public boolean hasEfgKeyRef()
    {
        return this._has_efgKeyRef;
    } //-- boolean hasEfgKeyRef() 

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
     * Sets the value of field 'annotation'. The field 'annotation'
     * has the following description: An annotation for the object
     * on which this attribute is found. It could be a description
     * of the object for instance
     * 
     * @param annotation the value of field 'annotation'.
     */
    public void setAnnotation(java.lang.String annotation)
    {
        this._annotation = annotation;
    } //-- void setAnnotation(java.lang.String) 

    /**
     * Sets the value of field 'caption'. The field 'caption' has
     * the following description: An optional caption that could be
     * used for display purposes by an application
     * 
     * @param caption the value of field 'caption'.
     */
    public void setCaption(java.lang.String caption)
    {
        this._caption = caption;
    } //-- void setCaption(java.lang.String) 

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
     * Sets the value of field 'efgKeyRef'. The field 'efgKeyRef'
     * has the following description: A reference to where this
     * object was obtained from. Must be a reference to a key in
     * the same document.
     * 
     * @param efgKeyRef the value of field 'efgKeyRef'.
     */
    public void setEfgKeyRef(int efgKeyRef)
    {
        this._efgKeyRef = efgKeyRef;
        this._has_efgKeyRef = true;
    } //-- void setEfgKeyRef(int) 

    /**
     * Sets the value of field 'resourceLink'. The field
     * 'resourceLink' has the following description: A reference
     * (could be a url) to a resource. Applications could use it
     * together to create a url to query an internal or external
     * resource
     * 
     * @param resourceLink the value of field 'resourceLink'.
     */
    public void setResourceLink(java.lang.String resourceLink)
    {
        this._resourceLink = resourceLink;
    } //-- void setResourceLink(java.lang.String) 

    /**
     * Method unmarshalEFGType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalEFGType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.EFGType) Unmarshaller.unmarshal(project.efg.efgDocument.EFGType.class, reader);
    } //-- java.lang.Object unmarshalEFGType(java.io.Reader) 

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
