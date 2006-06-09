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
 * Encapsulate the name of the actor who created a datasource and
 * the name of the datasource.(Perhaps this should be a guid?)
 * 
 * @version $Revision$ $Date$
 */
public class DatasourceType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The name of the datasource.Must be unique in this context
     */
    private java.lang.String _name;

    /**
     * The name of the actor who submitted this datasource
     */
    private int _submitterRef;

    /**
     * keeps track of state for field: _submitterRef
     */
    private boolean _has_submitterRef;

    /**
     * An xs;key for a datasource. Must be unique within the
     * context in which the datasource is defined
     */
    private int _efgKey;

    /**
     * keeps track of state for field: _efgKey
     */
    private boolean _has_efgKey;


      //----------------/
     //- Constructors -/
    //----------------/

    public DatasourceType() {
        super();
    } //-- project.efg.efgDocument.DatasourceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteEfgKey
     */
    public void deleteEfgKey()
    {
        this._has_efgKey= false;
    } //-- void deleteEfgKey() 

    /**
     * Method deleteSubmitterRef
     */
    public void deleteSubmitterRef()
    {
        this._has_submitterRef= false;
    } //-- void deleteSubmitterRef() 

    /**
     * Returns the value of field 'efgKey'. The field 'efgKey' has
     * the following description: An xs;key for a datasource. Must
     * be unique within the context in which the datasource is
     * defined
     * 
     * @return the value of field 'efgKey'.
     */
    public int getEfgKey()
    {
        return this._efgKey;
    } //-- int getEfgKey() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: The name of the datasource.Must be
     * unique in this context
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'submitterRef'. The field
     * 'submitterRef' has the following description: The name of
     * the actor who submitted this datasource
     * 
     * @return the value of field 'submitterRef'.
     */
    public int getSubmitterRef()
    {
        return this._submitterRef;
    } //-- int getSubmitterRef() 

    /**
     * Method hasEfgKey
     */
    public boolean hasEfgKey()
    {
        return this._has_efgKey;
    } //-- boolean hasEfgKey() 

    /**
     * Method hasSubmitterRef
     */
    public boolean hasSubmitterRef()
    {
        return this._has_submitterRef;
    } //-- boolean hasSubmitterRef() 

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
     * Sets the value of field 'efgKey'. The field 'efgKey' has the
     * following description: An xs;key for a datasource. Must be
     * unique within the context in which the datasource is defined
     * 
     * @param efgKey the value of field 'efgKey'.
     */
    public void setEfgKey(int efgKey)
    {
        this._efgKey = efgKey;
        this._has_efgKey = true;
    } //-- void setEfgKey(int) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: The name of the datasource.Must be
     * unique in this context
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'submitterRef'. The field
     * 'submitterRef' has the following description: The name of
     * the actor who submitted this datasource
     * 
     * @param submitterRef the value of field 'submitterRef'.
     */
    public void setSubmitterRef(int submitterRef)
    {
        this._submitterRef = submitterRef;
        this._has_submitterRef = true;
    } //-- void setSubmitterRef(int) 

    /**
     * Method unmarshalDatasourceType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalDatasourceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.DatasourceType) Unmarshaller.unmarshal(project.efg.efgDocument.DatasourceType.class, reader);
    } //-- java.lang.Object unmarshalDatasourceType(java.io.Reader) 

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
