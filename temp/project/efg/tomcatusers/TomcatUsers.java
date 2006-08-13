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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class TomcatUsers.
 * 
 * @version $Revision$ $Date$
 */
public class TomcatUsers implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _roleList
     */
    private java.util.ArrayList _roleList;

    /**
     * Field _userList
     */
    private java.util.ArrayList _userList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TomcatUsers() {
        super();
        _roleList = new ArrayList();
        _userList = new ArrayList();
    } //-- project.efg.tomcatusers.TomcatUsers()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRole
     * 
     * @param vRole
     */
    public void addRole(project.efg.tomcatusers.RoleType vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _roleList.add(vRole);
    } //-- void addRole(project.efg.tomcatusers.RoleType) 

    /**
     * Method addRole
     * 
     * @param index
     * @param vRole
     */
    public void addRole(int index, project.efg.tomcatusers.RoleType vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _roleList.add(index, vRole);
    } //-- void addRole(int, project.efg.tomcatusers.RoleType) 

    /**
     * Method addUser
     * 
     * @param vUser
     */
    public void addUser(project.efg.tomcatusers.UserType vUser)
        throws java.lang.IndexOutOfBoundsException
    {
        _userList.add(vUser);
    } //-- void addUser(project.efg.tomcatusers.UserType) 

    /**
     * Method addUser
     * 
     * @param index
     * @param vUser
     */
    public void addUser(int index, project.efg.tomcatusers.UserType vUser)
        throws java.lang.IndexOutOfBoundsException
    {
        _userList.add(index, vUser);
    } //-- void addUser(int, project.efg.tomcatusers.UserType) 

    /**
     * Method clearRole
     */
    public void clearRole()
    {
        _roleList.clear();
    } //-- void clearRole() 

    /**
     * Method clearUser
     */
    public void clearUser()
    {
        _userList.clear();
    } //-- void clearUser() 

    /**
     * Method enumerateRole
     */
    public java.util.Enumeration enumerateRole()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_roleList.iterator());
    } //-- java.util.Enumeration enumerateRole() 

    /**
     * Method enumerateUser
     */
    public java.util.Enumeration enumerateUser()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_userList.iterator());
    } //-- java.util.Enumeration enumerateUser() 

    /**
     * Method getRole
     * 
     * @param index
     */
    public project.efg.tomcatusers.RoleType getRole(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _roleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.tomcatusers.RoleType) _roleList.get(index);
    } //-- project.efg.tomcatusers.RoleType getRole(int) 

    /**
     * Method getRole
     */
    public project.efg.tomcatusers.RoleType[] getRole()
    {
        int size = _roleList.size();
        project.efg.tomcatusers.RoleType[] mArray = new project.efg.tomcatusers.RoleType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.tomcatusers.RoleType) _roleList.get(index);
        }
        return mArray;
    } //-- project.efg.tomcatusers.RoleType[] getRole() 

    /**
     * Method getRoleCount
     */
    public int getRoleCount()
    {
        return _roleList.size();
    } //-- int getRoleCount() 

    /**
     * Method getUser
     * 
     * @param index
     */
    public project.efg.tomcatusers.UserType getUser(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _userList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.tomcatusers.UserType) _userList.get(index);
    } //-- project.efg.tomcatusers.UserType getUser(int) 

    /**
     * Method getUser
     */
    public project.efg.tomcatusers.UserType[] getUser()
    {
        int size = _userList.size();
        project.efg.tomcatusers.UserType[] mArray = new project.efg.tomcatusers.UserType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.tomcatusers.UserType) _userList.get(index);
        }
        return mArray;
    } //-- project.efg.tomcatusers.UserType[] getUser() 

    /**
     * Method getUserCount
     */
    public int getUserCount()
    {
        return _userList.size();
    } //-- int getUserCount() 

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
     * Method removeRole
     * 
     * @param vRole
     */
    public boolean removeRole(project.efg.tomcatusers.RoleType vRole)
    {
        boolean removed = _roleList.remove(vRole);
        return removed;
    } //-- boolean removeRole(project.efg.tomcatusers.RoleType) 

    /**
     * Method removeUser
     * 
     * @param vUser
     */
    public boolean removeUser(project.efg.tomcatusers.UserType vUser)
    {
        boolean removed = _userList.remove(vUser);
        return removed;
    } //-- boolean removeUser(project.efg.tomcatusers.UserType) 

    /**
     * Method setRole
     * 
     * @param index
     * @param vRole
     */
    public void setRole(int index, project.efg.tomcatusers.RoleType vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _roleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _roleList.set(index, vRole);
    } //-- void setRole(int, project.efg.tomcatusers.RoleType) 

    /**
     * Method setRole
     * 
     * @param roleArray
     */
    public void setRole(project.efg.tomcatusers.RoleType[] roleArray)
    {
        //-- copy array
        _roleList.clear();
        for (int i = 0; i < roleArray.length; i++) {
            _roleList.add(roleArray[i]);
        }
    } //-- void setRole(project.efg.tomcatusers.RoleType) 

    /**
     * Method setUser
     * 
     * @param index
     * @param vUser
     */
    public void setUser(int index, project.efg.tomcatusers.UserType vUser)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _userList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _userList.set(index, vUser);
    } //-- void setUser(int, project.efg.tomcatusers.UserType) 

    /**
     * Method setUser
     * 
     * @param userArray
     */
    public void setUser(project.efg.tomcatusers.UserType[] userArray)
    {
        //-- copy array
        _userList.clear();
        for (int i = 0; i < userArray.length; i++) {
            _userList.add(userArray[i]);
        }
    } //-- void setUser(project.efg.tomcatusers.UserType) 

    /**
     * Method unmarshalTomcatUsers
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTomcatUsers(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.tomcatusers.TomcatUsers) Unmarshaller.unmarshal(project.efg.tomcatusers.TomcatUsers.class, reader);
    } //-- java.lang.Object unmarshalTomcatUsers(java.io.Reader) 

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
