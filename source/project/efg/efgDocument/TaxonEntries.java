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
 * Class TaxonEntries.
 * 
 * @version $Revision$ $Date$
 */
public class TaxonEntries implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * A container that holds data(characters and states for e.g)
     * for a taxon.
     */
    private java.util.ArrayList _taxonEntryList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaxonEntries() {
        super();
        _taxonEntryList = new ArrayList();
    } //-- project.efg.efgDocument.TaxonEntries()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTaxonEntry
     * 
     * @param vTaxonEntry
     */
    public void addTaxonEntry(project.efg.efgDocument.TaxonEntryType vTaxonEntry)
        throws java.lang.IndexOutOfBoundsException
    {
        _taxonEntryList.add(vTaxonEntry);
    } //-- void addTaxonEntry(project.efg.efgDocument.TaxonEntryType) 

    /**
     * Method addTaxonEntry
     * 
     * @param index
     * @param vTaxonEntry
     */
    public void addTaxonEntry(int index, project.efg.efgDocument.TaxonEntryType vTaxonEntry)
        throws java.lang.IndexOutOfBoundsException
    {
        _taxonEntryList.add(index, vTaxonEntry);
    } //-- void addTaxonEntry(int, project.efg.efgDocument.TaxonEntryType) 

    /**
     * Method clearTaxonEntry
     */
    public void clearTaxonEntry()
    {
        _taxonEntryList.clear();
    } //-- void clearTaxonEntry() 

    /**
     * Method enumerateTaxonEntry
     */
    public java.util.Enumeration enumerateTaxonEntry()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_taxonEntryList.iterator());
    } //-- java.util.Enumeration enumerateTaxonEntry() 

    /**
     * Method getTaxonEntry
     * 
     * @param index
     */
    public project.efg.efgDocument.TaxonEntryType getTaxonEntry(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _taxonEntryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.efgDocument.TaxonEntryType) _taxonEntryList.get(index);
    } //-- project.efg.efgDocument.TaxonEntryType getTaxonEntry(int) 

    /**
     * Method getTaxonEntry
     */
    public project.efg.efgDocument.TaxonEntryType[] getTaxonEntry()
    {
        int size = _taxonEntryList.size();
        project.efg.efgDocument.TaxonEntryType[] mArray = new project.efg.efgDocument.TaxonEntryType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.efgDocument.TaxonEntryType) _taxonEntryList.get(index);
        }
        return mArray;
    } //-- project.efg.efgDocument.TaxonEntryType[] getTaxonEntry() 

    /**
     * Method getTaxonEntryCount
     */
    public int getTaxonEntryCount()
    {
        return _taxonEntryList.size();
    } //-- int getTaxonEntryCount() 

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
     * Method removeTaxonEntry
     * 
     * @param vTaxonEntry
     */
    public boolean removeTaxonEntry(project.efg.efgDocument.TaxonEntryType vTaxonEntry)
    {
        boolean removed = _taxonEntryList.remove(vTaxonEntry);
        return removed;
    } //-- boolean removeTaxonEntry(project.efg.efgDocument.TaxonEntryType) 

    /**
     * Method setTaxonEntry
     * 
     * @param index
     * @param vTaxonEntry
     */
    public void setTaxonEntry(int index, project.efg.efgDocument.TaxonEntryType vTaxonEntry)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _taxonEntryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _taxonEntryList.set(index, vTaxonEntry);
    } //-- void setTaxonEntry(int, project.efg.efgDocument.TaxonEntryType) 

    /**
     * Method setTaxonEntry
     * 
     * @param taxonEntryArray
     */
    public void setTaxonEntry(project.efg.efgDocument.TaxonEntryType[] taxonEntryArray)
    {
        //-- copy array
        _taxonEntryList.clear();
        for (int i = 0; i < taxonEntryArray.length; i++) {
            _taxonEntryList.add(taxonEntryArray[i]);
        }
    } //-- void setTaxonEntry(project.efg.efgDocument.TaxonEntryType) 

    /**
     * Method unmarshalTaxonEntries
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTaxonEntries(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.TaxonEntries) Unmarshaller.unmarshal(project.efg.efgDocument.TaxonEntries.class, reader);
    } //-- java.lang.Object unmarshalTaxonEntries(java.io.Reader) 

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
