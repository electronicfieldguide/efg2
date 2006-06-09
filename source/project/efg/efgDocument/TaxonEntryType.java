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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Describes the properties of a taxon
 * 
 * @version $Revision$ $Date$
 */
public class TaxonEntryType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * An optional id that uniquely identifies this
     * record.TaxonEntry elements with the same recordID points to
     * the same record in the database
     */
    private java.lang.String _recordID;

    /**
     * Field _items
     */
    private java.util.ArrayList _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaxonEntryType() {
        super();
        _items = new ArrayList();
    } //-- project.efg.efgDocument.TaxonEntryType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTaxonEntryTypeItem
     * 
     * @param vTaxonEntryTypeItem
     */
    public void addTaxonEntryTypeItem(project.efg.efgDocument.TaxonEntryTypeItem vTaxonEntryTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(vTaxonEntryTypeItem);
    } //-- void addTaxonEntryTypeItem(project.efg.efgDocument.TaxonEntryTypeItem) 

    /**
     * Method addTaxonEntryTypeItem
     * 
     * @param index
     * @param vTaxonEntryTypeItem
     */
    public void addTaxonEntryTypeItem(int index, project.efg.efgDocument.TaxonEntryTypeItem vTaxonEntryTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(index, vTaxonEntryTypeItem);
    } //-- void addTaxonEntryTypeItem(int, project.efg.efgDocument.TaxonEntryTypeItem) 

    /**
     * Method clearTaxonEntryTypeItem
     */
    public void clearTaxonEntryTypeItem()
    {
        _items.clear();
    } //-- void clearTaxonEntryTypeItem() 

    /**
     * Method enumerateTaxonEntryTypeItem
     */
    public java.util.Enumeration enumerateTaxonEntryTypeItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_items.iterator());
    } //-- java.util.Enumeration enumerateTaxonEntryTypeItem() 

    /**
     * Returns the value of field 'recordID'. The field 'recordID'
     * has the following description: An optional id that uniquely
     * identifies this record.TaxonEntry elements with the same
     * recordID points to the same record in the database
     * 
     * @return the value of field 'recordID'.
     */
    public java.lang.String getRecordID()
    {
        return this._recordID;
    } //-- java.lang.String getRecordID() 

    /**
     * Method getTaxonEntryTypeItem
     * 
     * @param index
     */
    public project.efg.efgDocument.TaxonEntryTypeItem getTaxonEntryTypeItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.efgDocument.TaxonEntryTypeItem) _items.get(index);
    } //-- project.efg.efgDocument.TaxonEntryTypeItem getTaxonEntryTypeItem(int) 

    /**
     * Method getTaxonEntryTypeItem
     */
    public project.efg.efgDocument.TaxonEntryTypeItem[] getTaxonEntryTypeItem()
    {
        int size = _items.size();
        project.efg.efgDocument.TaxonEntryTypeItem[] mArray = new project.efg.efgDocument.TaxonEntryTypeItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.efgDocument.TaxonEntryTypeItem) _items.get(index);
        }
        return mArray;
    } //-- project.efg.efgDocument.TaxonEntryTypeItem[] getTaxonEntryTypeItem() 

    /**
     * Method getTaxonEntryTypeItemCount
     */
    public int getTaxonEntryTypeItemCount()
    {
        return _items.size();
    } //-- int getTaxonEntryTypeItemCount() 

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
     * Method removeTaxonEntryTypeItem
     * 
     * @param vTaxonEntryTypeItem
     */
    public boolean removeTaxonEntryTypeItem(project.efg.efgDocument.TaxonEntryTypeItem vTaxonEntryTypeItem)
    {
        boolean removed = _items.remove(vTaxonEntryTypeItem);
        return removed;
    } //-- boolean removeTaxonEntryTypeItem(project.efg.efgDocument.TaxonEntryTypeItem) 

    /**
     * Sets the value of field 'recordID'. The field 'recordID' has
     * the following description: An optional id that uniquely
     * identifies this record.TaxonEntry elements with the same
     * recordID points to the same record in the database
     * 
     * @param recordID the value of field 'recordID'.
     */
    public void setRecordID(java.lang.String recordID)
    {
        this._recordID = recordID;
    } //-- void setRecordID(java.lang.String) 

    /**
     * Method setTaxonEntryTypeItem
     * 
     * @param index
     * @param vTaxonEntryTypeItem
     */
    public void setTaxonEntryTypeItem(int index, project.efg.efgDocument.TaxonEntryTypeItem vTaxonEntryTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.set(index, vTaxonEntryTypeItem);
    } //-- void setTaxonEntryTypeItem(int, project.efg.efgDocument.TaxonEntryTypeItem) 

    /**
     * Method setTaxonEntryTypeItem
     * 
     * @param taxonEntryTypeItemArray
     */
    public void setTaxonEntryTypeItem(project.efg.efgDocument.TaxonEntryTypeItem[] taxonEntryTypeItemArray)
    {
        //-- copy array
        _items.clear();
        for (int i = 0; i < taxonEntryTypeItemArray.length; i++) {
            _items.add(taxonEntryTypeItemArray[i]);
        }
    } //-- void setTaxonEntryTypeItem(project.efg.efgDocument.TaxonEntryTypeItem) 

    /**
     * Method unmarshalTaxonEntryType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTaxonEntryType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.TaxonEntryType) Unmarshaller.unmarshal(project.efg.efgDocument.TaxonEntryType.class, reader);
    } //-- java.lang.Object unmarshalTaxonEntryType(java.io.Reader) 

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
