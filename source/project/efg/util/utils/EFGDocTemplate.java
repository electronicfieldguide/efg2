/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package project.efg.util.utils;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Comment describing your root element
 * 
 * @version $Revision$ $Date$
 */
public class EFGDocTemplate implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * An EFGDocument element is the root node of all EFGDocuments. 
     */
    private project.efg.efgDocument.EFGDocument _EFGDocument;

    /**
     * The root of all datasources configured to use templates in
     * the EFG or perhaps elsewhere
     */
    private project.efg.templates.taxonPageTemplates.TaxonPageTemplates  _taxonPageTemplates;


      //----------------/
     //- Constructors -/
    //----------------/

    public EFGDocTemplate() {
        super();
    } //-- project.efg.efgDocTemplate.EFGDocTemplate()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'EFGDocument'. The field
     * 'EFGDocument' has the following description: An EFGDocument
     * element is the root node of all EFGDocuments. 
     * 
     * @return the value of field 'EFGDocument'.
     */
    public project.efg.efgDocument.EFGDocument getEFGDocument()
    {
        return this._EFGDocument;
    } //-- project.efg.efgDocument.EFGDocument getEFGDocument() 

    /**
     * Returns the value of field 'taxonPageTemplates'. The field
     * 'taxonPageTemplates' has the following description: The root
     * of all datasources configured to use templates in the EFG or
     * perhaps elsewhere
     * 
     * @return the value of field 'taxonPageTemplates'.
     */
    public project.efg.templates.taxonPageTemplates.TaxonPageTemplates getTaxonPageTemplates()
    {
        return this._taxonPageTemplates;
    } //-- project.efg.templates.taxonPageTemplates.TaxonPageTemplates getTaxonPageTemplates() 

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
     * Sets the value of field 'EFGDocument'. The field
     * 'EFGDocument' has the following description: An EFGDocument
     * element is the root node of all EFGDocuments. 
     * 
     * @param EFGDocument the value of field 'EFGDocument'.
     */
    public void setEFGDocument(project.efg.efgDocument.EFGDocument EFGDocument)
    {
        this._EFGDocument = EFGDocument;
    } //-- void setEFGDocument(project.efg.efgDocument.EFGDocument) 

    /**
     * Sets the value of field 'taxonPageTemplates'. The field
     * 'taxonPageTemplates' has the following description: The root
     * of all datasources configured to use templates in the EFG or
     * perhaps elsewhere
     * 
     * @param taxonPageTemplates the value of field
     * 'taxonPageTemplates'.
     */
    public void setTaxonPageTemplates(project.efg.templates.taxonPageTemplates.TaxonPageTemplates taxonPageTemplates)
    {
        this._taxonPageTemplates = taxonPageTemplates;
    } //-- void setTaxonPageTemplates(project.efg.templates.taxonPageTemplates.TaxonPageTemplates) 

    /**
     * Method unmarshalEFGDocTemplate
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalEFGDocTemplate(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.util.utils.EFGDocTemplate) Unmarshaller.unmarshal(project.efg.util.utils.EFGDocTemplate.class, reader);
    } //-- java.lang.Object unmarshalEFGDocTemplate(java.io.Reader) 

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
