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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class EFGListsType.
 * 
 * @version $Revision$ $Date$
 */
public class EFGListsType extends project.efg.efgDocument.BaseEFGAttributeType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resourceLink
     */
    private java.lang.String _resourceLink;

    /**
     * Field _EFGListList
     */
    private java.util.ArrayList _EFGListList;


      //----------------/
     //- Constructors -/
    //----------------/

    public EFGListsType() {
        super();
        _EFGListList = new ArrayList();
    } //-- project.efg.efgDocument.EFGListsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addEFGList
     * 
     * @param vEFGList
     */
    public void addEFGList(project.efg.efgDocument.EFGType vEFGList)
        throws java.lang.IndexOutOfBoundsException
    {
        _EFGListList.add(vEFGList);
    } //-- void addEFGList(project.efg.efgDocument.EFGType) 

    /**
     * Method addEFGList
     * 
     * @param index
     * @param vEFGList
     */
    public void addEFGList(int index, project.efg.efgDocument.EFGType vEFGList)
        throws java.lang.IndexOutOfBoundsException
    {
        _EFGListList.add(index, vEFGList);
    } //-- void addEFGList(int, project.efg.efgDocument.EFGType) 

    /**
     * Method clearEFGList
     */
    public void clearEFGList()
    {
        _EFGListList.clear();
    } //-- void clearEFGList() 

    /**
     * Method enumerateEFGList
     */
    public java.util.Enumeration enumerateEFGList()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_EFGListList.iterator());
    } //-- java.util.Enumeration enumerateEFGList() 

    /**
     * Method getEFGList
     * 
     * @param index
     */
    public project.efg.efgDocument.EFGType getEFGList(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _EFGListList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.efgDocument.EFGType) _EFGListList.get(index);
    } //-- project.efg.efgDocument.EFGType getEFGList(int) 

    /**
     * Method getEFGList
     */
    public project.efg.efgDocument.EFGType[] getEFGList()
    {
        int size = _EFGListList.size();
        project.efg.efgDocument.EFGType[] mArray = new project.efg.efgDocument.EFGType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.efgDocument.EFGType) _EFGListList.get(index);
        }
        return mArray;
    } //-- project.efg.efgDocument.EFGType[] getEFGList() 

    /**
     * Method getEFGListCount
     */
    public int getEFGListCount()
    {
        return _EFGListList.size();
    } //-- int getEFGListCount() 

    /**
     * Returns the value of field 'resourceLink'.
     * 
     * @return the value of field 'resourceLink'.
     */
    public java.lang.String getResourceLink()
    {
        return this._resourceLink;
    } //-- java.lang.String getResourceLink() 

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
     * Method removeEFGList
     * 
     * @param vEFGList
     */
    public boolean removeEFGList(project.efg.efgDocument.EFGType vEFGList)
    {
        boolean removed = _EFGListList.remove(vEFGList);
        return removed;
    } //-- boolean removeEFGList(project.efg.efgDocument.EFGType) 

    /**
     * Method setEFGList
     * 
     * @param index
     * @param vEFGList
     */
    public void setEFGList(int index, project.efg.efgDocument.EFGType vEFGList)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _EFGListList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _EFGListList.set(index, vEFGList);
    } //-- void setEFGList(int, project.efg.efgDocument.EFGType) 

    /**
     * Method setEFGList
     * 
     * @param EFGListArray
     */
    public void setEFGList(project.efg.efgDocument.EFGType[] EFGListArray)
    {
        //-- copy array
        _EFGListList.clear();
        for (int i = 0; i < EFGListArray.length; i++) {
            _EFGListList.add(EFGListArray[i]);
        }
    } //-- void setEFGList(project.efg.efgDocument.EFGType) 

    /**
     * Sets the value of field 'resourceLink'.
     * 
     * @param resourceLink the value of field 'resourceLink'.
     */
    public void setResourceLink(java.lang.String resourceLink)
    {
        this._resourceLink = resourceLink;
    } //-- void setResourceLink(java.lang.String) 

    /**
     * Method unmarshalEFGListsType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalEFGListsType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.EFGListsType) Unmarshaller.unmarshal(project.efg.efgDocument.EFGListsType.class, reader);
    } //-- java.lang.Object unmarshalEFGListsType(java.io.Reader) 

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
