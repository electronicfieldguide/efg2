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
 * Class Submitters.
 * 
 * @version $Revision$ $Date$
 */
public class Submitters implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _submitterList
     */
    private java.util.ArrayList _submitterList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Submitters() {
        super();
        _submitterList = new ArrayList();
    } //-- project.efg.efgDocument.Submitters()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addSubmitter
     * 
     * @param vSubmitter
     */
    public void addSubmitter(project.efg.efgDocument.SubmitterType vSubmitter)
        throws java.lang.IndexOutOfBoundsException
    {
        _submitterList.add(vSubmitter);
    } //-- void addSubmitter(project.efg.efgDocument.SubmitterType) 

    /**
     * Method addSubmitter
     * 
     * @param index
     * @param vSubmitter
     */
    public void addSubmitter(int index, project.efg.efgDocument.SubmitterType vSubmitter)
        throws java.lang.IndexOutOfBoundsException
    {
        _submitterList.add(index, vSubmitter);
    } //-- void addSubmitter(int, project.efg.efgDocument.SubmitterType) 

    /**
     * Method clearSubmitter
     */
    public void clearSubmitter()
    {
        _submitterList.clear();
    } //-- void clearSubmitter() 

    /**
     * Method enumerateSubmitter
     */
    public java.util.Enumeration enumerateSubmitter()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_submitterList.iterator());
    } //-- java.util.Enumeration enumerateSubmitter() 

    /**
     * Method getSubmitter
     * 
     * @param index
     */
    public project.efg.efgDocument.SubmitterType getSubmitter(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _submitterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.efgDocument.SubmitterType) _submitterList.get(index);
    } //-- project.efg.efgDocument.SubmitterType getSubmitter(int) 

    /**
     * Method getSubmitter
     */
    public project.efg.efgDocument.SubmitterType[] getSubmitter()
    {
        int size = _submitterList.size();
        project.efg.efgDocument.SubmitterType[] mArray = new project.efg.efgDocument.SubmitterType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.efgDocument.SubmitterType) _submitterList.get(index);
        }
        return mArray;
    } //-- project.efg.efgDocument.SubmitterType[] getSubmitter() 

    /**
     * Method getSubmitterCount
     */
    public int getSubmitterCount()
    {
        return _submitterList.size();
    } //-- int getSubmitterCount() 

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
     * Method removeSubmitter
     * 
     * @param vSubmitter
     */
    public boolean removeSubmitter(project.efg.efgDocument.SubmitterType vSubmitter)
    {
        boolean removed = _submitterList.remove(vSubmitter);
        return removed;
    } //-- boolean removeSubmitter(project.efg.efgDocument.SubmitterType) 

    /**
     * Method setSubmitter
     * 
     * @param index
     * @param vSubmitter
     */
    public void setSubmitter(int index, project.efg.efgDocument.SubmitterType vSubmitter)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _submitterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _submitterList.set(index, vSubmitter);
    } //-- void setSubmitter(int, project.efg.efgDocument.SubmitterType) 

    /**
     * Method setSubmitter
     * 
     * @param submitterArray
     */
    public void setSubmitter(project.efg.efgDocument.SubmitterType[] submitterArray)
    {
        //-- copy array
        _submitterList.clear();
        for (int i = 0; i < submitterArray.length; i++) {
            _submitterList.add(submitterArray[i]);
        }
    } //-- void setSubmitter(project.efg.efgDocument.SubmitterType) 

    /**
     * Method unmarshalSubmitters
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSubmitters(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.Submitters) Unmarshaller.unmarshal(project.efg.efgDocument.Submitters.class, reader);
    } //-- java.lang.Object unmarshalSubmitters(java.io.Reader) 

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
