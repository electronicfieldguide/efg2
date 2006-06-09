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
 * Class UserType.
 * 
 * @version $Revision$ $Date$
 */
public class UserType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _username
     */
    private java.lang.String _username;

    /**
     * Field _password
     */
    private java.lang.String _password;

    /**
     * A comma separated list of role names that must be one of the
     * names given in the rolename attribute. There is no way of
     * enforcing that right now since the roles are user defined
     * values
     */
    private java.lang.String _roles;


      //----------------/
     //- Constructors -/
    //----------------/

    public UserType() {
        super();
    } //-- project.efg.tomcatusers.UserType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'password'.
     * 
     * @return the value of field 'password'.
     */
    public java.lang.String getPassword()
    {
        return this._password;
    } //-- java.lang.String getPassword() 

    /**
     * Returns the value of field 'roles'. The field 'roles' has
     * the following description: A comma separated list of role
     * names that must be one of the names given in the rolename
     * attribute. There is no way of enforcing that right now since
     * the roles are user defined values
     * 
     * @return the value of field 'roles'.
     */
    public java.lang.String getRoles()
    {
        return this._roles;
    } //-- java.lang.String getRoles() 

    /**
     * Returns the value of field 'username'.
     * 
     * @return the value of field 'username'.
     */
    public java.lang.String getUsername()
    {
        return this._username;
    } //-- java.lang.String getUsername() 

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
     * Sets the value of field 'password'.
     * 
     * @param password the value of field 'password'.
     */
    public void setPassword(java.lang.String password)
    {
        this._password = password;
    } //-- void setPassword(java.lang.String) 

    /**
     * Sets the value of field 'roles'. The field 'roles' has the
     * following description: A comma separated list of role names
     * that must be one of the names given in the rolename
     * attribute. There is no way of enforcing that right now since
     * the roles are user defined values
     * 
     * @param roles the value of field 'roles'.
     */
    public void setRoles(java.lang.String roles)
    {
        this._roles = roles;
    } //-- void setRoles(java.lang.String) 

    /**
     * Sets the value of field 'username'.
     * 
     * @param username the value of field 'username'.
     */
    public void setUsername(java.lang.String username)
    {
        this._username = username;
    } //-- void setUsername(java.lang.String) 

    /**
     * Method unmarshalUserType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalUserType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.tomcatusers.UserType) Unmarshaller.unmarshal(project.efg.tomcatusers.UserType.class, reader);
    } //-- java.lang.Object unmarshalUserType(java.io.Reader) 

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
