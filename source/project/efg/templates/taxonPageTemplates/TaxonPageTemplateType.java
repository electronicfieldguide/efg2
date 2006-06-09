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
     * The xsl file that will be used together with xml sources
     * generated from this template to transform xml data of the
     * given datasource to a taxon page
     */
    private java.lang.String _xslFileName;

    /**
     * The name of the datasource whose data will be transformed
     * using a given XSL File and the configured data for the
     * datasource. This name must be unique within thescope of a
     * TaxonPageTemplate element. see unique constraint on root
     * element.
     */
    private java.lang.String _datasourceName;

    /**
     * Encapsulates the various groups making up a template chosen
     * by an author. Each group id must be unique within the scope
     * of the groups element. Each group id is resolvable by the
     * xpath leading to that group. group's could have the same id
     * as long as they do not vbelong to the same parent directly.
     */
    private project.efg.templates.taxonPageTemplates.GroupsType _groups;


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
     * name must be unique within thescope of a TaxonPageTemplate
     * element. see unique constraint on root element.
     * 
     * @return the value of field 'datasourceName'.
     */
    public java.lang.String getDatasourceName()
    {
        return this._datasourceName;
    } //-- java.lang.String getDatasourceName() 

    /**
     * Returns the value of field 'groups'. The field 'groups' has
     * the following description: Encapsulates the various groups
     * making up a template chosen by an author. Each group id must
     * be unique within the scope of the groups element. Each group
     * id is resolvable by the xpath leading to that group. group's
     * could have the same id as long as they do not vbelong to the
     * same parent directly.
     * 
     * @return the value of field 'groups'.
     */
    public project.efg.templates.taxonPageTemplates.GroupsType getGroups()
    {
        return this._groups;
    } //-- project.efg.templates.taxonPageTemplates.GroupsType getGroups() 

    /**
     * Returns the value of field 'xslFileName'. The field
     * 'xslFileName' has the following description: The xsl file
     * that will be used together with xml sources generated from
     * this template to transform xml data of the given datasource
     * to a taxon page
     * 
     * @return the value of field 'xslFileName'.
     */
    public java.lang.String getXslFileName()
    {
        return this._xslFileName;
    } //-- java.lang.String getXslFileName() 

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
     * name must be unique within thescope of a TaxonPageTemplate
     * element. see unique constraint on root element.
     * 
     * @param datasourceName the value of field 'datasourceName'.
     */
    public void setDatasourceName(java.lang.String datasourceName)
    {
        this._datasourceName = datasourceName;
    } //-- void setDatasourceName(java.lang.String) 

    /**
     * Sets the value of field 'groups'. The field 'groups' has the
     * following description: Encapsulates the various groups
     * making up a template chosen by an author. Each group id must
     * be unique within the scope of the groups element. Each group
     * id is resolvable by the xpath leading to that group. group's
     * could have the same id as long as they do not vbelong to the
     * same parent directly.
     * 
     * @param groups the value of field 'groups'.
     */
    public void setGroups(project.efg.templates.taxonPageTemplates.GroupsType groups)
    {
        this._groups = groups;
    } //-- void setGroups(project.efg.templates.taxonPageTemplates.GroupsType) 

    /**
     * Sets the value of field 'xslFileName'. The field
     * 'xslFileName' has the following description: The xsl file
     * that will be used together with xml sources generated from
     * this template to transform xml data of the given datasource
     * to a taxon page
     * 
     * @param xslFileName the value of field 'xslFileName'.
     */
    public void setXslFileName(java.lang.String xslFileName)
    {
        this._xslFileName = xslFileName;
    } //-- void setXslFileName(java.lang.String) 

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
