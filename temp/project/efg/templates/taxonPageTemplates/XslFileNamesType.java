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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class XslFileNamesType.
 * 
 * @version $Revision$ $Date$
 */
public class XslFileNamesType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Encapsulates one or more xsl files to be used to generate
     * plates for each datasource.
     */
    private project.efg.templates.taxonPageTemplates.XslPageType _xslPlatePages;

    /**
     * Encapsulates one or more xsl files to be used to generate
     * lists for each datasource.
     */
    private project.efg.templates.taxonPageTemplates.XslPageType _xslListPages;

    /**
     * Encapsulates one or more xsl files to be used to generate
     * taxon pages for each datasource.
     */
    private project.efg.templates.taxonPageTemplates.XslPageType _xslTaxonPages;


      //----------------/
     //- Constructors -/
    //----------------/

    public XslFileNamesType() {
        super();
    } //-- project.efg.templates.taxonPageTemplates.XslFileNamesType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'xslListPages'. The field
     * 'xslListPages' has the following description: Encapsulates
     * one or more xsl files to be used to generate lists for each
     * datasource.
     * 
     * @return the value of field 'xslListPages'.
     */
    public project.efg.templates.taxonPageTemplates.XslPageType getXslListPages()
    {
        return this._xslListPages;
    } //-- project.efg.templates.taxonPageTemplates.XslPageType getXslListPages() 

    /**
     * Returns the value of field 'xslPlatePages'. The field
     * 'xslPlatePages' has the following description: Encapsulates
     * one or more xsl files to be used to generate plates for each
     * datasource.
     * 
     * @return the value of field 'xslPlatePages'.
     */
    public project.efg.templates.taxonPageTemplates.XslPageType getXslPlatePages()
    {
        return this._xslPlatePages;
    } //-- project.efg.templates.taxonPageTemplates.XslPageType getXslPlatePages() 

    /**
     * Returns the value of field 'xslTaxonPages'. The field
     * 'xslTaxonPages' has the following description: Encapsulates
     * one or more xsl files to be used to generate taxon pages for
     * each datasource.
     * 
     * @return the value of field 'xslTaxonPages'.
     */
    public project.efg.templates.taxonPageTemplates.XslPageType getXslTaxonPages()
    {
        return this._xslTaxonPages;
    } //-- project.efg.templates.taxonPageTemplates.XslPageType getXslTaxonPages() 

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
     * Sets the value of field 'xslListPages'. The field
     * 'xslListPages' has the following description: Encapsulates
     * one or more xsl files to be used to generate lists for each
     * datasource.
     * 
     * @param xslListPages the value of field 'xslListPages'.
     */
    public void setXslListPages(project.efg.templates.taxonPageTemplates.XslPageType xslListPages)
    {
        this._xslListPages = xslListPages;
    } //-- void setXslListPages(project.efg.templates.taxonPageTemplates.XslPageType) 

    /**
     * Sets the value of field 'xslPlatePages'. The field
     * 'xslPlatePages' has the following description: Encapsulates
     * one or more xsl files to be used to generate plates for each
     * datasource.
     * 
     * @param xslPlatePages the value of field 'xslPlatePages'.
     */
    public void setXslPlatePages(project.efg.templates.taxonPageTemplates.XslPageType xslPlatePages)
    {
        this._xslPlatePages = xslPlatePages;
    } //-- void setXslPlatePages(project.efg.templates.taxonPageTemplates.XslPageType) 

    /**
     * Sets the value of field 'xslTaxonPages'. The field
     * 'xslTaxonPages' has the following description: Encapsulates
     * one or more xsl files to be used to generate taxon pages for
     * each datasource.
     * 
     * @param xslTaxonPages the value of field 'xslTaxonPages'.
     */
    public void setXslTaxonPages(project.efg.templates.taxonPageTemplates.XslPageType xslTaxonPages)
    {
        this._xslTaxonPages = xslTaxonPages;
    } //-- void setXslTaxonPages(project.efg.templates.taxonPageTemplates.XslPageType) 

    /**
     * Method unmarshalXslFileNamesType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalXslFileNamesType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.templates.taxonPageTemplates.XslFileNamesType) Unmarshaller.unmarshal(project.efg.templates.taxonPageTemplates.XslFileNamesType.class, reader);
    } //-- java.lang.Object unmarshalXslFileNamesType(java.io.Reader) 

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
