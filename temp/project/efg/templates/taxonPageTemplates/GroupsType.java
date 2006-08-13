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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class GroupsType.
 * 
 * @version $Revision$ $Date$
 */
public class GroupsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _items
     */
    private java.util.ArrayList _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public GroupsType() {
        super();
        _items = new ArrayList();
    } //-- project.efg.templates.taxonPageTemplates.GroupsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addGroupsTypeItem
     * 
     * @param vGroupsTypeItem
     */
    public void addGroupsTypeItem(project.efg.templates.taxonPageTemplates.GroupsTypeItem vGroupsTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(vGroupsTypeItem);
    } //-- void addGroupsTypeItem(project.efg.templates.taxonPageTemplates.GroupsTypeItem) 

    /**
     * Method addGroupsTypeItem
     * 
     * @param index
     * @param vGroupsTypeItem
     */
    public void addGroupsTypeItem(int index, project.efg.templates.taxonPageTemplates.GroupsTypeItem vGroupsTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(index, vGroupsTypeItem);
    } //-- void addGroupsTypeItem(int, project.efg.templates.taxonPageTemplates.GroupsTypeItem) 

    /**
     * Method clearGroupsTypeItem
     */
    public void clearGroupsTypeItem()
    {
        _items.clear();
    } //-- void clearGroupsTypeItem() 

    /**
     * Method enumerateGroupsTypeItem
     */
    public java.util.Enumeration enumerateGroupsTypeItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_items.iterator());
    } //-- java.util.Enumeration enumerateGroupsTypeItem() 

    /**
     * Method getGroupsTypeItem
     * 
     * @param index
     */
    public project.efg.templates.taxonPageTemplates.GroupsTypeItem getGroupsTypeItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.templates.taxonPageTemplates.GroupsTypeItem) _items.get(index);
    } //-- project.efg.templates.taxonPageTemplates.GroupsTypeItem getGroupsTypeItem(int) 

    /**
     * Method getGroupsTypeItem
     */
    public project.efg.templates.taxonPageTemplates.GroupsTypeItem[] getGroupsTypeItem()
    {
        int size = _items.size();
        project.efg.templates.taxonPageTemplates.GroupsTypeItem[] mArray = new project.efg.templates.taxonPageTemplates.GroupsTypeItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.templates.taxonPageTemplates.GroupsTypeItem) _items.get(index);
        }
        return mArray;
    } //-- project.efg.templates.taxonPageTemplates.GroupsTypeItem[] getGroupsTypeItem() 

    /**
     * Method getGroupsTypeItemCount
     */
    public int getGroupsTypeItemCount()
    {
        return _items.size();
    } //-- int getGroupsTypeItemCount() 

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
     * Method removeGroupsTypeItem
     * 
     * @param vGroupsTypeItem
     */
    public boolean removeGroupsTypeItem(project.efg.templates.taxonPageTemplates.GroupsTypeItem vGroupsTypeItem)
    {
        boolean removed = _items.remove(vGroupsTypeItem);
        return removed;
    } //-- boolean removeGroupsTypeItem(project.efg.templates.taxonPageTemplates.GroupsTypeItem) 

    /**
     * Method setGroupsTypeItem
     * 
     * @param index
     * @param vGroupsTypeItem
     */
    public void setGroupsTypeItem(int index, project.efg.templates.taxonPageTemplates.GroupsTypeItem vGroupsTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.set(index, vGroupsTypeItem);
    } //-- void setGroupsTypeItem(int, project.efg.templates.taxonPageTemplates.GroupsTypeItem) 

    /**
     * Method setGroupsTypeItem
     * 
     * @param groupsTypeItemArray
     */
    public void setGroupsTypeItem(project.efg.templates.taxonPageTemplates.GroupsTypeItem[] groupsTypeItemArray)
    {
        //-- copy array
        _items.clear();
        for (int i = 0; i < groupsTypeItemArray.length; i++) {
            _items.add(groupsTypeItemArray[i]);
        }
    } //-- void setGroupsTypeItem(project.efg.templates.taxonPageTemplates.GroupsTypeItem) 

    /**
     * Method unmarshalGroupsType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalGroupsType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.templates.taxonPageTemplates.GroupsType) Unmarshaller.unmarshal(project.efg.templates.taxonPageTemplates.GroupsType.class, reader);
    } //-- java.lang.Object unmarshalGroupsType(java.io.Reader) 

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
