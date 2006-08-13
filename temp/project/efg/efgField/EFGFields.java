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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class EFGFields.
 * 
 * @version $Revision$ $Date$
 */
public class EFGFields implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The name of the datasource for which this document was
     * createdr. TODO: Use a GUID?
     */
    private java.lang.String _datasourceName;

    /**
     * Field _EFGFieldList
     */
    private java.util.ArrayList _EFGFieldList;


      //----------------/
     //- Constructors -/
    //----------------/

    public EFGFields() {
        super();
        _EFGFieldList = new ArrayList();
    } //-- project.efg.efgField.EFGFields()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addEFGField
     * 
     * @param vEFGField
     */
    public void addEFGField(project.efg.efgField.EFGFieldType vEFGField)
        throws java.lang.IndexOutOfBoundsException
    {
        _EFGFieldList.add(vEFGField);
    } //-- void addEFGField(project.efg.efgField.EFGFieldType) 

    /**
     * Method addEFGField
     * 
     * @param index
     * @param vEFGField
     */
    public void addEFGField(int index, project.efg.efgField.EFGFieldType vEFGField)
        throws java.lang.IndexOutOfBoundsException
    {
        _EFGFieldList.add(index, vEFGField);
    } //-- void addEFGField(int, project.efg.efgField.EFGFieldType) 

    /**
     * Method clearEFGField
     */
    public void clearEFGField()
    {
        _EFGFieldList.clear();
    } //-- void clearEFGField() 

    /**
     * Method enumerateEFGField
     */
    public java.util.Enumeration enumerateEFGField()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_EFGFieldList.iterator());
    } //-- java.util.Enumeration enumerateEFGField() 

    /**
     * Returns the value of field 'datasourceName'. The field
     * 'datasourceName' has the following description: The name of
     * the datasource for which this document was createdr. TODO:
     * Use a GUID?
     * 
     * @return the value of field 'datasourceName'.
     */
    public java.lang.String getDatasourceName()
    {
        return this._datasourceName;
    } //-- java.lang.String getDatasourceName() 

    /**
     * Method getEFGField
     * 
     * @param index
     */
    public project.efg.efgField.EFGFieldType getEFGField(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _EFGFieldList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.efgField.EFGFieldType) _EFGFieldList.get(index);
    } //-- project.efg.efgField.EFGFieldType getEFGField(int) 

    /**
     * Method getEFGField
     */
    public project.efg.efgField.EFGFieldType[] getEFGField()
    {
        int size = _EFGFieldList.size();
        project.efg.efgField.EFGFieldType[] mArray = new project.efg.efgField.EFGFieldType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.efgField.EFGFieldType) _EFGFieldList.get(index);
        }
        return mArray;
    } //-- project.efg.efgField.EFGFieldType[] getEFGField() 

    /**
     * Method getEFGFieldCount
     */
    public int getEFGFieldCount()
    {
        return _EFGFieldList.size();
    } //-- int getEFGFieldCount() 

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
     * Method removeEFGField
     * 
     * @param vEFGField
     */
    public boolean removeEFGField(project.efg.efgField.EFGFieldType vEFGField)
    {
        boolean removed = _EFGFieldList.remove(vEFGField);
        return removed;
    } //-- boolean removeEFGField(project.efg.efgField.EFGFieldType) 

    /**
     * Sets the value of field 'datasourceName'. The field
     * 'datasourceName' has the following description: The name of
     * the datasource for which this document was createdr. TODO:
     * Use a GUID?
     * 
     * @param datasourceName the value of field 'datasourceName'.
     */
    public void setDatasourceName(java.lang.String datasourceName)
    {
        this._datasourceName = datasourceName;
    } //-- void setDatasourceName(java.lang.String) 

    /**
     * Method setEFGField
     * 
     * @param index
     * @param vEFGField
     */
    public void setEFGField(int index, project.efg.efgField.EFGFieldType vEFGField)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _EFGFieldList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _EFGFieldList.set(index, vEFGField);
    } //-- void setEFGField(int, project.efg.efgField.EFGFieldType) 

    /**
     * Method setEFGField
     * 
     * @param EFGFieldArray
     */
    public void setEFGField(project.efg.efgField.EFGFieldType[] EFGFieldArray)
    {
        //-- copy array
        _EFGFieldList.clear();
        for (int i = 0; i < EFGFieldArray.length; i++) {
            _EFGFieldList.add(EFGFieldArray[i]);
        }
    } //-- void setEFGField(project.efg.efgField.EFGFieldType) 

    /**
     * Method unmarshalEFGFields
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalEFGFields(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgField.EFGFields) Unmarshaller.unmarshal(project.efg.efgField.EFGFields.class, reader);
    } //-- java.lang.Object unmarshalEFGFields(java.io.Reader) 

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
