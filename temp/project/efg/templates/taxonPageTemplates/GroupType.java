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
 * Class GroupType.
 * 
 * @version $Revision$ $Date$
 */
public class GroupType extends project.efg.templates.taxonPageTemplates.RankLabelTextType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * All groups with the same ID are handled the same way by an
     * XSLT template
     */
    private java.lang.String _id;

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * Field _items
     */
    private java.util.ArrayList _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public GroupType() {
        super();
        setContent("");
        _items = new ArrayList();
    } //-- project.efg.templates.taxonPageTemplates.GroupType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addGroupTypeItem
     * 
     * @param vGroupTypeItem
     */
    public void addGroupTypeItem(project.efg.templates.taxonPageTemplates.GroupTypeItem vGroupTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(vGroupTypeItem);
    } //-- void addGroupTypeItem(project.efg.templates.taxonPageTemplates.GroupTypeItem) 

    /**
     * Method addGroupTypeItem
     * 
     * @param index
     * @param vGroupTypeItem
     */
    public void addGroupTypeItem(int index, project.efg.templates.taxonPageTemplates.GroupTypeItem vGroupTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(index, vGroupTypeItem);
    } //-- void addGroupTypeItem(int, project.efg.templates.taxonPageTemplates.GroupTypeItem) 

    /**
     * Method clearGroupTypeItem
     */
    public void clearGroupTypeItem()
    {
        _items.clear();
    } //-- void clearGroupTypeItem() 

    /**
     * Method enumerateGroupTypeItem
     */
    public java.util.Enumeration enumerateGroupTypeItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_items.iterator());
    } //-- java.util.Enumeration enumerateGroupTypeItem() 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
     */
    public java.lang.String getContent()
    {
        return this._content;
    } //-- java.lang.String getContent() 

    /**
     * Method getGroupTypeItem
     * 
     * @param index
     */
    public project.efg.templates.taxonPageTemplates.GroupTypeItem getGroupTypeItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.templates.taxonPageTemplates.GroupTypeItem) _items.get(index);
    } //-- project.efg.templates.taxonPageTemplates.GroupTypeItem getGroupTypeItem(int) 

    /**
     * Method getGroupTypeItem
     */
    public project.efg.templates.taxonPageTemplates.GroupTypeItem[] getGroupTypeItem()
    {
        int size = _items.size();
        project.efg.templates.taxonPageTemplates.GroupTypeItem[] mArray = new project.efg.templates.taxonPageTemplates.GroupTypeItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.templates.taxonPageTemplates.GroupTypeItem) _items.get(index);
        }
        return mArray;
    } //-- project.efg.templates.taxonPageTemplates.GroupTypeItem[] getGroupTypeItem() 

    /**
     * Method getGroupTypeItemCount
     */
    public int getGroupTypeItemCount()
    {
        return _items.size();
    } //-- int getGroupTypeItemCount() 

    /**
     * Returns the value of field 'id'. The field 'id' has the
     * following description: All groups with the same ID are
     * handled the same way by an XSLT template
     * 
     * @return the value of field 'id'.
     */
    public java.lang.String getId()
    {
        return this._id;
    } //-- java.lang.String getId() 

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
     * Method removeGroupTypeItem
     * 
     * @param vGroupTypeItem
     */
    public boolean removeGroupTypeItem(project.efg.templates.taxonPageTemplates.GroupTypeItem vGroupTypeItem)
    {
        boolean removed = _items.remove(vGroupTypeItem);
        return removed;
    } //-- boolean removeGroupTypeItem(project.efg.templates.taxonPageTemplates.GroupTypeItem) 

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content)
    {
        this._content = content;
    } //-- void setContent(java.lang.String) 

    /**
     * Method setGroupTypeItem
     * 
     * @param index
     * @param vGroupTypeItem
     */
    public void setGroupTypeItem(int index, project.efg.templates.taxonPageTemplates.GroupTypeItem vGroupTypeItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.set(index, vGroupTypeItem);
    } //-- void setGroupTypeItem(int, project.efg.templates.taxonPageTemplates.GroupTypeItem) 

    /**
     * Method setGroupTypeItem
     * 
     * @param groupTypeItemArray
     */
    public void setGroupTypeItem(project.efg.templates.taxonPageTemplates.GroupTypeItem[] groupTypeItemArray)
    {
        //-- copy array
        _items.clear();
        for (int i = 0; i < groupTypeItemArray.length; i++) {
            _items.add(groupTypeItemArray[i]);
        }
    } //-- void setGroupTypeItem(project.efg.templates.taxonPageTemplates.GroupTypeItem) 

    /**
     * Sets the value of field 'id'. The field 'id' has the
     * following description: All groups with the same ID are
     * handled the same way by an XSLT template
     * 
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id)
    {
        this._id = id;
    } //-- void setId(java.lang.String) 

    /**
     * Method unmarshalGroupType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalGroupType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.templates.taxonPageTemplates.GroupType) Unmarshaller.unmarshal(project.efg.templates.taxonPageTemplates.GroupType.class, reader);
    } //-- java.lang.Object unmarshalGroupType(java.io.Reader) 

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
