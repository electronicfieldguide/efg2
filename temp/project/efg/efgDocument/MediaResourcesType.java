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
 * Class MediaResourcesType.
 * 
 * @version $Revision$ $Date$
 */
public class MediaResourcesType extends project.efg.efgDocument.BaseEFGAttributeType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mediaResourceList
     */
    private java.util.ArrayList _mediaResourceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MediaResourcesType() {
        super();
        _mediaResourceList = new ArrayList();
    } //-- project.efg.efgDocument.MediaResourcesType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMediaResource
     * 
     * @param vMediaResource
     */
    public void addMediaResource(project.efg.efgDocument.MediaResourceType vMediaResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _mediaResourceList.add(vMediaResource);
    } //-- void addMediaResource(project.efg.efgDocument.MediaResourceType) 

    /**
     * Method addMediaResource
     * 
     * @param index
     * @param vMediaResource
     */
    public void addMediaResource(int index, project.efg.efgDocument.MediaResourceType vMediaResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _mediaResourceList.add(index, vMediaResource);
    } //-- void addMediaResource(int, project.efg.efgDocument.MediaResourceType) 

    /**
     * Method clearMediaResource
     */
    public void clearMediaResource()
    {
        _mediaResourceList.clear();
    } //-- void clearMediaResource() 

    /**
     * Method enumerateMediaResource
     */
    public java.util.Enumeration enumerateMediaResource()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_mediaResourceList.iterator());
    } //-- java.util.Enumeration enumerateMediaResource() 

    /**
     * Method getMediaResource
     * 
     * @param index
     */
    public project.efg.efgDocument.MediaResourceType getMediaResource(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mediaResourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.efgDocument.MediaResourceType) _mediaResourceList.get(index);
    } //-- project.efg.efgDocument.MediaResourceType getMediaResource(int) 

    /**
     * Method getMediaResource
     */
    public project.efg.efgDocument.MediaResourceType[] getMediaResource()
    {
        int size = _mediaResourceList.size();
        project.efg.efgDocument.MediaResourceType[] mArray = new project.efg.efgDocument.MediaResourceType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.efgDocument.MediaResourceType) _mediaResourceList.get(index);
        }
        return mArray;
    } //-- project.efg.efgDocument.MediaResourceType[] getMediaResource() 

    /**
     * Method getMediaResourceCount
     */
    public int getMediaResourceCount()
    {
        return _mediaResourceList.size();
    } //-- int getMediaResourceCount() 

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
     * Method removeMediaResource
     * 
     * @param vMediaResource
     */
    public boolean removeMediaResource(project.efg.efgDocument.MediaResourceType vMediaResource)
    {
        boolean removed = _mediaResourceList.remove(vMediaResource);
        return removed;
    } //-- boolean removeMediaResource(project.efg.efgDocument.MediaResourceType) 

    /**
     * Method setMediaResource
     * 
     * @param index
     * @param vMediaResource
     */
    public void setMediaResource(int index, project.efg.efgDocument.MediaResourceType vMediaResource)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mediaResourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mediaResourceList.set(index, vMediaResource);
    } //-- void setMediaResource(int, project.efg.efgDocument.MediaResourceType) 

    /**
     * Method setMediaResource
     * 
     * @param mediaResourceArray
     */
    public void setMediaResource(project.efg.efgDocument.MediaResourceType[] mediaResourceArray)
    {
        //-- copy array
        _mediaResourceList.clear();
        for (int i = 0; i < mediaResourceArray.length; i++) {
            _mediaResourceList.add(mediaResourceArray[i]);
        }
    } //-- void setMediaResource(project.efg.efgDocument.MediaResourceType) 

    /**
     * Method unmarshalMediaResourcesType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalMediaResourcesType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.MediaResourcesType) Unmarshaller.unmarshal(project.efg.efgDocument.MediaResourcesType.class, reader);
    } //-- java.lang.Object unmarshalMediaResourcesType(java.io.Reader) 

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
