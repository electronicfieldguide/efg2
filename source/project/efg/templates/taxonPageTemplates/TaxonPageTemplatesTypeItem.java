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

import java.io.Serializable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class TaxonPageTemplatesTypeItem.
 * 
 * @version $Revision$ $Date$
 */
public class TaxonPageTemplatesTypeItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Encapsulates information needed to transform a document to a
     * chosen template by author.
     */
    private project.efg.templates.taxonPageTemplates.TaxonPageTemplateType _taxonPageTemplate;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaxonPageTemplatesTypeItem() {
        super();
    } //-- project.efg.templates.taxonPageTemplates.TaxonPageTemplatesTypeItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'taxonPageTemplate'. The field
     * 'taxonPageTemplate' has the following description:
     * Encapsulates information needed to transform a document to a
     * chosen template by author.
     * 
     * @return the value of field 'taxonPageTemplate'.
     */
    public project.efg.templates.taxonPageTemplates.TaxonPageTemplateType getTaxonPageTemplate()
    {
        return this._taxonPageTemplate;
    } //-- project.efg.templates.taxonPageTemplates.TaxonPageTemplateType getTaxonPageTemplate() 

    /**
     * Sets the value of field 'taxonPageTemplate'. The field
     * 'taxonPageTemplate' has the following description:
     * Encapsulates information needed to transform a document to a
     * chosen template by author.
     * 
     * @param taxonPageTemplate the value of field
     * 'taxonPageTemplate'.
     */
    public void setTaxonPageTemplate(project.efg.templates.taxonPageTemplates.TaxonPageTemplateType taxonPageTemplate)
    {
        this._taxonPageTemplate = taxonPageTemplate;
    } //-- void setTaxonPageTemplate(project.efg.templates.taxonPageTemplates.TaxonPageTemplateType) 

}
