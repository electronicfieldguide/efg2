/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package project.efg.efgField;

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
import project.efg.efgField.types.EFGFieldTypeEnum;

/**
 * A type for the EFGField element
 * 
 * @version $Revision$ $Date$
 */
public class EFGFieldType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The original name of the field
     */
    private java.lang.String _name;

    /**
     * The legal name of the field
     */
    private java.lang.String _legalName;

    /**
     * A choice of what this field is
     */
    private project.efg.efgField.types.EFGFieldTypeEnum _efgFieldType = project.efg.efgField.types.EFGFieldTypeEnum.valueOf("Categorical");

    /**
     * true if this field is searchable, false otherwise
     */
    private boolean _isSearchable = true;

    /**
     * keeps track of state for field: _isSearchable
     */
    private boolean _has_isSearchable;

    /**
     * true if this field is a list false otherwise
     */
    private boolean _isList = false;

    /**
     * keeps track of state for field: _isList
     */
    private boolean _has_isList;

    /**
     * True if this field should appear on the taxon page, false
     * otherwise. This is just a suggestion to application writers
     */
    private boolean _onTaxonPage = false;

    /**
     * keeps track of state for field: _onTaxonPage
     */
    private boolean _has_onTaxonPage;

    /**
     * The order in which this field should appear on a user
     * interface. This is just a suggestion to an application.
     */
    private int _order;

    /**
     * keeps track of state for field: _order
     */
    private boolean _has_order;


      //----------------/
     //- Constructors -/
    //----------------/

    public EFGFieldType() {
        super();
        setEfgFieldType(project.efg.efgField.types.EFGFieldTypeEnum.valueOf("Categorical"));
    } //-- project.efg.efgField.EFGFieldType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIsList
     */
    public void deleteIsList()
    {
        this._has_isList= false;
    } //-- void deleteIsList() 

    /**
     * Method deleteIsSearchable
     */
    public void deleteIsSearchable()
    {
        this._has_isSearchable= false;
    } //-- void deleteIsSearchable() 

    /**
     * Method deleteOnTaxonPage
     */
    public void deleteOnTaxonPage()
    {
        this._has_onTaxonPage= false;
    } //-- void deleteOnTaxonPage() 

    /**
     * Returns the value of field 'efgFieldType'. The field
     * 'efgFieldType' has the following description: A choice of
     * what this field is
     * 
     * @return the value of field 'efgFieldType'.
     */
    public project.efg.efgField.types.EFGFieldTypeEnum getEfgFieldType()
    {
        return this._efgFieldType;
    } //-- project.efg.efgField.types.EFGFieldTypeEnum getEfgFieldType() 

    /**
     * Returns the value of field 'isList'. The field 'isList' has
     * the following description: true if this field is a list
     * false otherwise
     * 
     * @return the value of field 'isList'.
     */
    public boolean getIsList()
    {
        return this._isList;
    } //-- boolean getIsList() 

    /**
     * Returns the value of field 'isSearchable'. The field
     * 'isSearchable' has the following description: true if this
     * field is searchable, false otherwise
     * 
     * @return the value of field 'isSearchable'.
     */
    public boolean getIsSearchable()
    {
        return this._isSearchable;
    } //-- boolean getIsSearchable() 

    /**
     * Returns the value of field 'legalName'. The field
     * 'legalName' has the following description: The legal name of
     * the field
     * 
     * @return the value of field 'legalName'.
     */
    public java.lang.String getLegalName()
    {
        return this._legalName;
    } //-- java.lang.String getLegalName() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: The original name of the field
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'onTaxonPage'. The field
     * 'onTaxonPage' has the following description: True if this
     * field should appear on the taxon page, false otherwise. This
     * is just a suggestion to application writers
     * 
     * @return the value of field 'onTaxonPage'.
     */
    public boolean getOnTaxonPage()
    {
        return this._onTaxonPage;
    } //-- boolean getOnTaxonPage() 

    /**
     * Returns the value of field 'order'. The field 'order' has
     * the following description: The order in which this field
     * should appear on a user interface. This is just a suggestion
     * to an application.
     * 
     * @return the value of field 'order'.
     */
    public int getOrder()
    {
        return this._order;
    } //-- int getOrder() 

    /**
     * Method hasIsList
     */
    public boolean hasIsList()
    {
        return this._has_isList;
    } //-- boolean hasIsList() 

    /**
     * Method hasIsSearchable
     */
    public boolean hasIsSearchable()
    {
        return this._has_isSearchable;
    } //-- boolean hasIsSearchable() 

    /**
     * Method hasOnTaxonPage
     */
    public boolean hasOnTaxonPage()
    {
        return this._has_onTaxonPage;
    } //-- boolean hasOnTaxonPage() 

    /**
     * Method hasOrder
     */
    public boolean hasOrder()
    {
        return this._has_order;
    } //-- boolean hasOrder() 

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
     * Sets the value of field 'efgFieldType'. The field
     * 'efgFieldType' has the following description: A choice of
     * what this field is
     * 
     * @param efgFieldType the value of field 'efgFieldType'.
     */
    public void setEfgFieldType(project.efg.efgField.types.EFGFieldTypeEnum efgFieldType)
    {
        this._efgFieldType = efgFieldType;
    } //-- void setEfgFieldType(project.efg.efgField.types.EFGFieldTypeEnum) 

    /**
     * Sets the value of field 'isList'. The field 'isList' has the
     * following description: true if this field is a list false
     * otherwise
     * 
     * @param isList the value of field 'isList'.
     */
    public void setIsList(boolean isList)
    {
        this._isList = isList;
        this._has_isList = true;
    } //-- void setIsList(boolean) 

    /**
     * Sets the value of field 'isSearchable'. The field
     * 'isSearchable' has the following description: true if this
     * field is searchable, false otherwise
     * 
     * @param isSearchable the value of field 'isSearchable'.
     */
    public void setIsSearchable(boolean isSearchable)
    {
        this._isSearchable = isSearchable;
        this._has_isSearchable = true;
    } //-- void setIsSearchable(boolean) 

    /**
     * Sets the value of field 'legalName'. The field 'legalName'
     * has the following description: The legal name of the field
     * 
     * @param legalName the value of field 'legalName'.
     */
    public void setLegalName(java.lang.String legalName)
    {
        this._legalName = legalName;
    } //-- void setLegalName(java.lang.String) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: The original name of the field
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'onTaxonPage'. The field
     * 'onTaxonPage' has the following description: True if this
     * field should appear on the taxon page, false otherwise. This
     * is just a suggestion to application writers
     * 
     * @param onTaxonPage the value of field 'onTaxonPage'.
     */
    public void setOnTaxonPage(boolean onTaxonPage)
    {
        this._onTaxonPage = onTaxonPage;
        this._has_onTaxonPage = true;
    } //-- void setOnTaxonPage(boolean) 

    /**
     * Sets the value of field 'order'. The field 'order' has the
     * following description: The order in which this field should
     * appear on a user interface. This is just a suggestion to an
     * application.
     * 
     * @param order the value of field 'order'.
     */
    public void setOrder(int order)
    {
        this._order = order;
        this._has_order = true;
    } //-- void setOrder(int) 

    /**
     * Method unmarshalEFGFieldType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalEFGFieldType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgField.EFGFieldType) Unmarshaller.unmarshal(project.efg.efgField.EFGFieldType.class, reader);
    } //-- java.lang.Object unmarshalEFGFieldType(java.io.Reader) 

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
