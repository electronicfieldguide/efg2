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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class TaxonPageTemplatesType.
 * 
 * @version $Revision$ $Date$
 */
public class TaxonPageTemplatesType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _items
     */
    private java.util.ArrayList _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaxonPageTemplatesType() {
        super();
        _items = new ArrayList();
    } //-- project.efg.templates.taxonPageTemplates.TaxonPageTemplatesType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTaxonPageTemplatesTypeItem
     * 
     * @param vTaxonPageTemplatesTypeItem
     */
    public void addTaxonPageTemplatesTypeItem(project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem vTaxonPageTemplatesTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(vTaxonPageTemplatesTypeItem);
    } //-- void addTaxonPageTemplatesTypeItem(project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem) 

    /**
     * Method addTaxonPageTemplatesTypeItem
     * 
     * @param index
     * @param vTaxonPageTemplatesTypeItem
     */
    public void addTaxonPageTemplatesTypeItem(int index, project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem vTaxonPageTemplatesTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(index, vTaxonPageTemplatesTypeItem);
    } //-- void addTaxonPageTemplatesTypeItem(int, project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem) 

    /**
     * Method clearTaxonPageTemplatesTypeItem
     */
    public void clearTaxonPageTemplatesTypeItem()
    {
        _items.clear();
    } //-- void clearTaxonPageTemplatesTypeItem() 

    /**
     * Method enumerateTaxonPageTemplatesTypeItem
     */
    public java.util.Enumeration enumerateTaxonPageTemplatesTypeItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_items.iterator());
    } //-- java.util.Enumeration enumerateTaxonPageTemplatesTypeItem() 

    /**
     * Method getTaxonPageTemplatesTypeItem
     * 
     * @param index
     */
    public project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem getTaxonPageTemplatesTypeItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem) _items.get(index);
    } //-- project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem getTaxonPageTemplatesTypeItem(int) 

    /**
     * Method getTaxonPageTemplatesTypeItem
     */
    public project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem[] getTaxonPageTemplatesTypeItem()
    {
        int size = _items.size();
        project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem[] mArray = new project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem) _items.get(index);
        }
        return mArray;
    } //-- project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem[] getTaxonPageTemplatesTypeItem() 

    /**
     * Method getTaxonPageTemplatesTypeItemCount
     */
    public int getTaxonPageTemplatesTypeItemCount()
    {
        return _items.size();
    } //-- int getTaxonPageTemplatesTypeItemCount() 

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
     * Method removeTaxonPageTemplatesTypeItem
     * 
     * @param vTaxonPageTemplatesTypeItem
     */
    public boolean removeTaxonPageTemplatesTypeItem(project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem vTaxonPageTemplatesTypeItem)
    {
        boolean removed = _items.remove(vTaxonPageTemplatesTypeItem);
        return removed;
    } //-- boolean removeTaxonPageTemplatesTypeItem(project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem) 

    /**
     * Method setTaxonPageTemplatesTypeItem
     * 
     * @param index
     * @param vTaxonPageTemplatesTypeItem
     */
    public void setTaxonPageTemplatesTypeItem(int index, project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem vTaxonPageTemplatesTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.set(index, vTaxonPageTemplatesTypeItem);
    } //-- void setTaxonPageTemplatesTypeItem(int, project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem) 

    /**
     * Method setTaxonPageTemplatesTypeItem
     * 
     * @param taxonPageTemplatesTypeItemArray
     */
    public void setTaxonPageTemplatesTypeItem(project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem[] taxonPageTemplatesTypeItemArray)
    {
        //-- copy array
        _items.clear();
        for (int i = 0; i < taxonPageTemplatesTypeItemArray.length; i++) {
            _items.add(taxonPageTemplatesTypeItemArray[i]);
        }
    } //-- void setTaxonPageTemplatesTypeItem(project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem) 

    /**
     * Method unmarshalTaxonPageTemplatesType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTaxonPageTemplatesType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.templates.taxonPageTemplates.TaxonPageTemplatesType) Unmarshaller.unmarshal(project.efg.templates.taxonPageTemplates.TaxonPageTemplatesType.class, reader);
    } //-- java.lang.Object unmarshalTaxonPageTemplatesType(java.io.Reader) 

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
