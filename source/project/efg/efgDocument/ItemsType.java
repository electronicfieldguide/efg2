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
import project.efg.efgDocument.types.ItemTypeEnum;

/**
 * Class ItemsType.
 * 
 * @version $Revision$ $Date$
 */
public class ItemsType extends project.efg.efgDocument.BaseEFGAttributeType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _itemType
     */
    private project.efg.efgDocument.types.ItemTypeEnum _itemType = project.efg.efgDocument.types.ItemTypeEnum.valueOf("narrative");

    /**
     * Field _itemList
     */
    private java.util.ArrayList _itemList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ItemsType() {
        super();
        setItemType(project.efg.efgDocument.types.ItemTypeEnum.valueOf("narrative"));
        _itemList = new ArrayList();
    } //-- project.efg.efgDocument.ItemsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addItem
     * 
     * @param vItem
     */
    public void addItem(project.efg.efgDocument.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.add(vItem);
    } //-- void addItem(project.efg.efgDocument.Item) 

    /**
     * Method addItem
     * 
     * @param index
     * @param vItem
     */
    public void addItem(int index, project.efg.efgDocument.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.add(index, vItem);
    } //-- void addItem(int, project.efg.efgDocument.Item) 

    /**
     * Method clearItem
     */
    public void clearItem()
    {
        _itemList.clear();
    } //-- void clearItem() 

    /**
     * Method enumerateItem
     */
    public java.util.Enumeration enumerateItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_itemList.iterator());
    } //-- java.util.Enumeration enumerateItem() 

    /**
     * Method getItem
     * 
     * @param index
     */
    public project.efg.efgDocument.Item getItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.efgDocument.Item) _itemList.get(index);
    } //-- project.efg.efgDocument.Item getItem(int) 

    /**
     * Method getItem
     */
    public project.efg.efgDocument.Item[] getItem()
    {
        int size = _itemList.size();
        project.efg.efgDocument.Item[] mArray = new project.efg.efgDocument.Item[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.efgDocument.Item) _itemList.get(index);
        }
        return mArray;
    } //-- project.efg.efgDocument.Item[] getItem() 

    /**
     * Method getItemCount
     */
    public int getItemCount()
    {
        return _itemList.size();
    } //-- int getItemCount() 

    /**
     * Returns the value of field 'itemType'.
     * 
     * @return the value of field 'itemType'.
     */
    public project.efg.efgDocument.types.ItemTypeEnum getItemType()
    {
        return this._itemType;
    } //-- project.efg.efgDocument.types.ItemTypeEnum getItemType() 

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
     * Method removeItem
     * 
     * @param vItem
     */
    public boolean removeItem(project.efg.efgDocument.Item vItem)
    {
        boolean removed = _itemList.remove(vItem);
        return removed;
    } //-- boolean removeItem(project.efg.efgDocument.Item) 

    /**
     * Method setItem
     * 
     * @param index
     * @param vItem
     */
    public void setItem(int index, project.efg.efgDocument.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _itemList.set(index, vItem);
    } //-- void setItem(int, project.efg.efgDocument.Item) 

    /**
     * Method setItem
     * 
     * @param itemArray
     */
    public void setItem(project.efg.efgDocument.Item[] itemArray)
    {
        //-- copy array
        _itemList.clear();
        for (int i = 0; i < itemArray.length; i++) {
            _itemList.add(itemArray[i]);
        }
    } //-- void setItem(project.efg.efgDocument.Item) 

    /**
     * Sets the value of field 'itemType'.
     * 
     * @param itemType the value of field 'itemType'.
     */
    public void setItemType(project.efg.efgDocument.types.ItemTypeEnum itemType)
    {
        this._itemType = itemType;
    } //-- void setItemType(project.efg.efgDocument.types.ItemTypeEnum) 

    /**
     * Method unmarshalItemsType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalItemsType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.ItemsType) Unmarshaller.unmarshal(project.efg.efgDocument.ItemsType.class, reader);
    } //-- java.lang.Object unmarshalItemsType(java.io.Reader) 

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
