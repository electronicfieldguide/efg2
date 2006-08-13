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
 * made up of one or more xslPage elements. Each element
 * encapsulates a file which must be unque within the container. If
 * isDefault is true, then applications may apply this xslFile to
 * all requests
 * 
 * @version $Revision$ $Date$
 */
public class XslPage implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fileName
     */
    private java.lang.String _fileName;

    /**
     * Field _guid
     */
    private java.lang.String _guid;

    /**
     * Field _displayName
     */
    private java.lang.String _displayName;

    /**
     * Field _jspName
     */
    private java.lang.String _jspName;

    /**
     * Field _isDefault
     */
    private boolean _isDefault = false;

    /**
     * keeps track of state for field: _isDefault
     */
    private boolean _has_isDefault;

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

    public XslPage() {
        super();
    } //-- project.efg.templates.taxonPageTemplates.XslPage()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIsDefault
     */
    public void deleteIsDefault()
    {
        this._has_isDefault= false;
    } //-- void deleteIsDefault() 

    /**
     * Returns the value of field 'displayName'.
     * 
     * @return the value of field 'displayName'.
     */
    public java.lang.String getDisplayName()
    {
        return this._displayName;
    } //-- java.lang.String getDisplayName() 

    /**
     * Returns the value of field 'fileName'.
     * 
     * @return the value of field 'fileName'.
     */
    public java.lang.String getFileName()
    {
        return this._fileName;
    } //-- java.lang.String getFileName() 

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
     * Returns the value of field 'guid'.
     * 
     * @return the value of field 'guid'.
     */
    public java.lang.String getGuid()
    {
        return this._guid;
    } //-- java.lang.String getGuid() 

    /**
     * Returns the value of field 'isDefault'.
     * 
     * @return the value of field 'isDefault'.
     */
    public boolean getIsDefault()
    {
        return this._isDefault;
    } //-- boolean getIsDefault() 

    /**
     * Returns the value of field 'jspName'.
     * 
     * @return the value of field 'jspName'.
     */
    public java.lang.String getJspName()
    {
        return this._jspName;
    } //-- java.lang.String getJspName() 

    /**
     * Method hasIsDefault
     */
    public boolean hasIsDefault()
    {
        return this._has_isDefault;
    } //-- boolean hasIsDefault() 

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
     * Sets the value of field 'displayName'.
     * 
     * @param displayName the value of field 'displayName'.
     */
    public void setDisplayName(java.lang.String displayName)
    {
        this._displayName = displayName;
    } //-- void setDisplayName(java.lang.String) 

    /**
     * Sets the value of field 'fileName'.
     * 
     * @param fileName the value of field 'fileName'.
     */
    public void setFileName(java.lang.String fileName)
    {
        this._fileName = fileName;
    } //-- void setFileName(java.lang.String) 

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
     * Sets the value of field 'guid'.
     * 
     * @param guid the value of field 'guid'.
     */
    public void setGuid(java.lang.String guid)
    {
        this._guid = guid;
    } //-- void setGuid(java.lang.String) 

    /**
     * Sets the value of field 'isDefault'.
     * 
     * @param isDefault the value of field 'isDefault'.
     */
    public void setIsDefault(boolean isDefault)
    {
        this._isDefault = isDefault;
        this._has_isDefault = true;
    } //-- void setIsDefault(boolean) 

    /**
     * Sets the value of field 'jspName'.
     * 
     * @param jspName the value of field 'jspName'.
     */
    public void setJspName(java.lang.String jspName)
    {
        this._jspName = jspName;
    } //-- void setJspName(java.lang.String) 

    /**
     * Method unmarshalXslPage
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalXslPage(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.templates.taxonPageTemplates.XslPage) Unmarshaller.unmarshal(project.efg.templates.taxonPageTemplates.XslPage.class, reader);
    } //-- java.lang.Object unmarshalXslPage(java.io.Reader) 

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
