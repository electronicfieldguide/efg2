/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package project.efg.tomcatusers;

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
 * Class RoleType.
 * 
 * @version $Revision$ $Date$
 */
public class RoleType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _rolename
     */
    private java.lang.String _rolename;


      //----------------/
     //- Constructors -/
    //----------------/

    public RoleType() {
        super();
    } //-- project.efg.tomcatusers.RoleType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'rolename'.
     * 
     * @return the value of field 'rolename'.
     */
    public java.lang.String getRolename()
    {
        return this._rolename;
    } //-- java.lang.String getRolename() 

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
     * Sets the value of field 'rolename'.
     * 
     * @param rolename the value of field 'rolename'.
     */
    public void setRolename(java.lang.String rolename)
    {
        this._rolename = rolename;
    } //-- void setRolename(java.lang.String) 

    /**
     * Method unmarshalRoleType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalRoleType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.tomcatusers.RoleType) Unmarshaller.unmarshal(project.efg.tomcatusers.RoleType.class, reader);
    } //-- java.lang.Object unmarshalRoleType(java.io.Reader) 

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
