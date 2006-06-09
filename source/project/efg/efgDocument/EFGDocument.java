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
 * An EFGDocument element is the root node of all EFGDocuments. 
 * 
 * @version $Revision$ $Date$
 */
public class EFGDocument implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * A list of datasources that was used to create an instance
     * document.
     */
    private project.efg.efgDocument.DatasourcesType _datasources;

    /**
     * Field _taxonEntries
     */
    private project.efg.efgDocument.TaxonEntries _taxonEntries;

    /**
     * Field _submitters
     */
    private project.efg.efgDocument.Submitters _submitters;


      //----------------/
     //- Constructors -/
    //----------------/

    public EFGDocument() {
        super();
    } //-- project.efg.efgDocument.EFGDocument()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'datasources'. The field
     * 'datasources' has the following description: A list of
     * datasources that was used to create an instance document.
     * 
     * @return the value of field 'datasources'.
     */
    public project.efg.efgDocument.DatasourcesType getDatasources()
    {
        return this._datasources;
    } //-- project.efg.efgDocument.DatasourcesType getDatasources() 

    /**
     * Returns the value of field 'submitters'.
     * 
     * @return the value of field 'submitters'.
     */
    public project.efg.efgDocument.Submitters getSubmitters()
    {
        return this._submitters;
    } //-- project.efg.efgDocument.Submitters getSubmitters() 

    /**
     * Returns the value of field 'taxonEntries'.
     * 
     * @return the value of field 'taxonEntries'.
     */
    public project.efg.efgDocument.TaxonEntries getTaxonEntries()
    {
        return this._taxonEntries;
    } //-- project.efg.efgDocument.TaxonEntries getTaxonEntries() 

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
     * Sets the value of field 'datasources'. The field
     * 'datasources' has the following description: A list of
     * datasources that was used to create an instance document.
     * 
     * @param datasources the value of field 'datasources'.
     */
    public void setDatasources(project.efg.efgDocument.DatasourcesType datasources)
    {
        this._datasources = datasources;
    } //-- void setDatasources(project.efg.efgDocument.DatasourcesType) 

    /**
     * Sets the value of field 'submitters'.
     * 
     * @param submitters the value of field 'submitters'.
     */
    public void setSubmitters(project.efg.efgDocument.Submitters submitters)
    {
        this._submitters = submitters;
    } //-- void setSubmitters(project.efg.efgDocument.Submitters) 

    /**
     * Sets the value of field 'taxonEntries'.
     * 
     * @param taxonEntries the value of field 'taxonEntries'.
     */
    public void setTaxonEntries(project.efg.efgDocument.TaxonEntries taxonEntries)
    {
        this._taxonEntries = taxonEntries;
    } //-- void setTaxonEntries(project.efg.efgDocument.TaxonEntries) 

    /**
     * Method unmarshalEFGDocument
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalEFGDocument(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.EFGDocument) Unmarshaller.unmarshal(project.efg.efgDocument.EFGDocument.class, reader);
    } //-- java.lang.Object unmarshalEFGDocument(java.io.Reader) 

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
