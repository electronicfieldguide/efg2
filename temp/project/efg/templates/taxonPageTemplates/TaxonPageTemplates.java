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
 * The root of all datasources configured to use templates in the
 * EFG or perhaps elsewhere
 * 
 * @version $Revision$ $Date$
 */
public class TaxonPageTemplates implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _taxonPageTemplateList
     */
    private java.util.ArrayList _taxonPageTemplateList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaxonPageTemplates() {
        super();
        _taxonPageTemplateList = new ArrayList();
    } //-- project.efg.templates.taxonPageTemplates.TaxonPageTemplates()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTaxonPageTemplate
     * 
     * @param vTaxonPageTemplate
     */
    public void addTaxonPageTemplate(project.efg.templates.taxonPageTemplates.TaxonPageTemplateType vTaxonPageTemplate)
        throws java.lang.IndexOutOfBoundsException
    {
        _taxonPageTemplateList.add(vTaxonPageTemplate);
    } //-- void addTaxonPageTemplate(project.efg.templates.taxonPageTemplates.TaxonPageTemplateType) 

    /**
     * Method addTaxonPageTemplate
     * 
     * @param index
     * @param vTaxonPageTemplate
     */
    public void addTaxonPageTemplate(int index, project.efg.templates.taxonPageTemplates.TaxonPageTemplateType vTaxonPageTemplate)
        throws java.lang.IndexOutOfBoundsException
    {
        _taxonPageTemplateList.add(index, vTaxonPageTemplate);
    } //-- void addTaxonPageTemplate(int, project.efg.templates.taxonPageTemplates.TaxonPageTemplateType) 

    /**
     * Method clearTaxonPageTemplate
     */
    public void clearTaxonPageTemplate()
    {
        _taxonPageTemplateList.clear();
    } //-- void clearTaxonPageTemplate() 

    /**
     * Method enumerateTaxonPageTemplate
     */
    public java.util.Enumeration enumerateTaxonPageTemplate()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_taxonPageTemplateList.iterator());
    } //-- java.util.Enumeration enumerateTaxonPageTemplate() 

    /**
     * Method getTaxonPageTemplate
     * 
     * @param index
     */
    public project.efg.templates.taxonPageTemplates.TaxonPageTemplateType getTaxonPageTemplate(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _taxonPageTemplateList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.templates.taxonPageTemplates.TaxonPageTemplateType) _taxonPageTemplateList.get(index);
    } //-- project.efg.templates.taxonPageTemplates.TaxonPageTemplateType getTaxonPageTemplate(int) 

    /**
     * Method getTaxonPageTemplate
     */
    public project.efg.templates.taxonPageTemplates.TaxonPageTemplateType[] getTaxonPageTemplate()
    {
        int size = _taxonPageTemplateList.size();
        project.efg.templates.taxonPageTemplates.TaxonPageTemplateType[] mArray = new project.efg.templates.taxonPageTemplates.TaxonPageTemplateType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.templates.taxonPageTemplates.TaxonPageTemplateType) _taxonPageTemplateList.get(index);
        }
        return mArray;
    } //-- project.efg.templates.taxonPageTemplates.TaxonPageTemplateType[] getTaxonPageTemplate() 

    /**
     * Method getTaxonPageTemplateCount
     */
    public int getTaxonPageTemplateCount()
    {
        return _taxonPageTemplateList.size();
    } //-- int getTaxonPageTemplateCount() 

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
     * Method removeTaxonPageTemplate
     * 
     * @param vTaxonPageTemplate
     */
    public boolean removeTaxonPageTemplate(project.efg.templates.taxonPageTemplates.TaxonPageTemplateType vTaxonPageTemplate)
    {
        boolean removed = _taxonPageTemplateList.remove(vTaxonPageTemplate);
        return removed;
    } //-- boolean removeTaxonPageTemplate(project.efg.templates.taxonPageTemplates.TaxonPageTemplateType) 

    /**
     * Method setTaxonPageTemplate
     * 
     * @param index
     * @param vTaxonPageTemplate
     */
    public void setTaxonPageTemplate(int index, project.efg.templates.taxonPageTemplates.TaxonPageTemplateType vTaxonPageTemplate)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _taxonPageTemplateList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _taxonPageTemplateList.set(index, vTaxonPageTemplate);
    } //-- void setTaxonPageTemplate(int, project.efg.templates.taxonPageTemplates.TaxonPageTemplateType) 

    /**
     * Method setTaxonPageTemplate
     * 
     * @param taxonPageTemplateArray
     */
    public void setTaxonPageTemplate(project.efg.templates.taxonPageTemplates.TaxonPageTemplateType[] taxonPageTemplateArray)
    {
        //-- copy array
        _taxonPageTemplateList.clear();
        for (int i = 0; i < taxonPageTemplateArray.length; i++) {
            _taxonPageTemplateList.add(taxonPageTemplateArray[i]);
        }
    } //-- void setTaxonPageTemplate(project.efg.templates.taxonPageTemplates.TaxonPageTemplateType) 

    /**
     * Method unmarshalTaxonPageTemplates
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTaxonPageTemplates(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.templates.taxonPageTemplates.TaxonPageTemplates) Unmarshaller.unmarshal(project.efg.templates.taxonPageTemplates.TaxonPageTemplates.class, reader);
    } //-- java.lang.Object unmarshalTaxonPageTemplates(java.io.Reader) 

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
