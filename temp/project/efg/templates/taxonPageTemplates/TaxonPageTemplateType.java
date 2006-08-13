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
 * Class TaxonPageTemplateType.
 * 
 * @version $Revision$ $Date$
 */
public class TaxonPageTemplateType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The name of the datasource whose data will be transformed
     * using a given XSL File and the configured data for the
     * datasource. This name must be unique within the scope of a
     * TaxonPageTemplate element. see unique constraint on root
     * element.
     */
    private java.lang.String _datasourceName;

    /**
     * Field _XSLFileNames
     */
    private project.efg.templates.taxonPageTemplates.XslFileNamesType _XSLFileNames;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaxonPageTemplateType() {
        super();
    } //-- project.efg.templates.taxonPageTemplates.TaxonPageTemplateType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'datasourceName'. The field
     * 'datasourceName' has the following description: The name of
     * the datasource whose data will be transformed using a given
     * XSL File and the configured data for the datasource. This
     * name must be unique within the scope of a TaxonPageTemplate
     * element. see unique constraint on root element.
     * 
     * @return the value of field 'datasourceName'.
     */
    public java.lang.String getDatasourceName()
    {
        return this._datasourceName;
    } //-- java.lang.String getDatasourceName() 

    /**
     * Returns the value of field 'XSLFileNames'.
     * 
     * @return the value of field 'XSLFileNames'.
     */
    public project.efg.templates.taxonPageTemplates.XslFileNamesType getXSLFileNames()
    {
        return this._XSLFileNames;
    } //-- project.efg.templates.taxonPageTemplates.XslFileNamesType getXSLFileNames() 

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
     * Sets the value of field 'datasourceName'. The field
     * 'datasourceName' has the following description: The name of
     * the datasource whose data will be transformed using a given
     * XSL File and the configured data for the datasource. This
     * name must be unique within the scope of a TaxonPageTemplate
     * element. see unique constraint on root element.
     * 
     * @param datasourceName the value of field 'datasourceName'.
     */
    public void setDatasourceName(java.lang.String datasourceName)
    {
        this._datasourceName = datasourceName;
    } //-- void setDatasourceName(java.lang.String) 

    /**
     * Sets the value of field 'XSLFileNames'.
     * 
     * @param XSLFileNames the value of field 'XSLFileNames'.
     */
    public void setXSLFileNames(project.efg.templates.taxonPageTemplates.XslFileNamesType XSLFileNames)
    {
        this._XSLFileNames = XSLFileNames;
    } //-- void setXSLFileNames(project.efg.templates.taxonPageTemplates.XslFileNamesType) 

    /**
     * Method unmarshalTaxonPageTemplateType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTaxonPageTemplateType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.templates.taxonPageTemplates.TaxonPageTemplateType) Unmarshaller.unmarshal(project.efg.templates.taxonPageTemplates.TaxonPageTemplateType.class, reader);
    } //-- java.lang.Object unmarshalTaxonPageTemplateType(java.io.Reader) 

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
