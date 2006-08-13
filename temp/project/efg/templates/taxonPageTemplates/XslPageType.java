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
 * The page type as determined by the element.
 * 
 * @version $Revision$ $Date$
 */
public class XslPageType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * made up of one or more xslPage elements. Each element
     * encapsulates a file which must be unque within the
     * container. If isDefault is true, then applications may apply
     * this xslFile to all requests
     */
    private java.util.ArrayList _xslPageList;


      //----------------/
     //- Constructors -/
    //----------------/

    public XslPageType() {
        super();
        _xslPageList = new ArrayList();
    } //-- project.efg.templates.taxonPageTemplates.XslPageType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addXslPage
     * 
     * @param vXslPage
     */
    public void addXslPage(project.efg.templates.taxonPageTemplates.XslPage vXslPage)
        throws java.lang.IndexOutOfBoundsException
    {
        _xslPageList.add(vXslPage);
    } //-- void addXslPage(project.efg.templates.taxonPageTemplates.XslPage) 

    /**
     * Method addXslPage
     * 
     * @param index
     * @param vXslPage
     */
    public void addXslPage(int index, project.efg.templates.taxonPageTemplates.XslPage vXslPage)
        throws java.lang.IndexOutOfBoundsException
    {
        _xslPageList.add(index, vXslPage);
    } //-- void addXslPage(int, project.efg.templates.taxonPageTemplates.XslPage) 

    /**
     * Method clearXslPage
     */
    public void clearXslPage()
    {
        _xslPageList.clear();
    } //-- void clearXslPage() 

    /**
     * Method enumerateXslPage
     */
    public java.util.Enumeration enumerateXslPage()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_xslPageList.iterator());
    } //-- java.util.Enumeration enumerateXslPage() 

    /**
     * Method getXslPage
     * 
     * @param index
     */
    public project.efg.templates.taxonPageTemplates.XslPage getXslPage(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _xslPageList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.templates.taxonPageTemplates.XslPage) _xslPageList.get(index);
    } //-- project.efg.templates.taxonPageTemplates.XslPage getXslPage(int) 

    /**
     * Method getXslPage
     */
    public project.efg.templates.taxonPageTemplates.XslPage[] getXslPage()
    {
        int size = _xslPageList.size();
        project.efg.templates.taxonPageTemplates.XslPage[] mArray = new project.efg.templates.taxonPageTemplates.XslPage[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.templates.taxonPageTemplates.XslPage) _xslPageList.get(index);
        }
        return mArray;
    } //-- project.efg.templates.taxonPageTemplates.XslPage[] getXslPage() 

    /**
     * Method getXslPageCount
     */
    public int getXslPageCount()
    {
        return _xslPageList.size();
    } //-- int getXslPageCount() 

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
     * Method removeXslPage
     * 
     * @param vXslPage
     */
    public boolean removeXslPage(project.efg.templates.taxonPageTemplates.XslPage vXslPage)
    {
        boolean removed = _xslPageList.remove(vXslPage);
        return removed;
    } //-- boolean removeXslPage(project.efg.templates.taxonPageTemplates.XslPage) 

    /**
     * Method setXslPage
     * 
     * @param index
     * @param vXslPage
     */
    public void setXslPage(int index, project.efg.templates.taxonPageTemplates.XslPage vXslPage)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _xslPageList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _xslPageList.set(index, vXslPage);
    } //-- void setXslPage(int, project.efg.templates.taxonPageTemplates.XslPage) 

    /**
     * Method setXslPage
     * 
     * @param xslPageArray
     */
    public void setXslPage(project.efg.templates.taxonPageTemplates.XslPage[] xslPageArray)
    {
        //-- copy array
        _xslPageList.clear();
        for (int i = 0; i < xslPageArray.length; i++) {
            _xslPageList.add(xslPageArray[i]);
        }
    } //-- void setXslPage(project.efg.templates.taxonPageTemplates.XslPage) 

    /**
     * Method unmarshalXslPageType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalXslPageType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.templates.taxonPageTemplates.XslPageType) Unmarshaller.unmarshal(project.efg.templates.taxonPageTemplates.XslPageType.class, reader);
    } //-- java.lang.Object unmarshalXslPageType(java.io.Reader) 

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
