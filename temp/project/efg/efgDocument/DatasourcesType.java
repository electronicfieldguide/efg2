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
 * Contains a list of datasource elements
 * 
 * @version $Revision$ $Date$
 */
public class DatasourcesType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _datasourceList
     */
    private java.util.ArrayList _datasourceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public DatasourcesType() {
        super();
        _datasourceList = new ArrayList();
    } //-- project.efg.efgDocument.DatasourcesType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDatasource
     * 
     * @param vDatasource
     */
    public void addDatasource(project.efg.efgDocument.DatasourceType vDatasource)
        throws java.lang.IndexOutOfBoundsException
    {
        _datasourceList.add(vDatasource);
    } //-- void addDatasource(project.efg.efgDocument.DatasourceType) 

    /**
     * Method addDatasource
     * 
     * @param index
     * @param vDatasource
     */
    public void addDatasource(int index, project.efg.efgDocument.DatasourceType vDatasource)
        throws java.lang.IndexOutOfBoundsException
    {
        _datasourceList.add(index, vDatasource);
    } //-- void addDatasource(int, project.efg.efgDocument.DatasourceType) 

    /**
     * Method clearDatasource
     */
    public void clearDatasource()
    {
        _datasourceList.clear();
    } //-- void clearDatasource() 

    /**
     * Method enumerateDatasource
     */
    public java.util.Enumeration enumerateDatasource()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_datasourceList.iterator());
    } //-- java.util.Enumeration enumerateDatasource() 

    /**
     * Method getDatasource
     * 
     * @param index
     */
    public project.efg.efgDocument.DatasourceType getDatasource(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _datasourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.efgDocument.DatasourceType) _datasourceList.get(index);
    } //-- project.efg.efgDocument.DatasourceType getDatasource(int) 

    /**
     * Method getDatasource
     */
    public project.efg.efgDocument.DatasourceType[] getDatasource()
    {
        int size = _datasourceList.size();
        project.efg.efgDocument.DatasourceType[] mArray = new project.efg.efgDocument.DatasourceType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.efgDocument.DatasourceType) _datasourceList.get(index);
        }
        return mArray;
    } //-- project.efg.efgDocument.DatasourceType[] getDatasource() 

    /**
     * Method getDatasourceCount
     */
    public int getDatasourceCount()
    {
        return _datasourceList.size();
    } //-- int getDatasourceCount() 

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
     * Method removeDatasource
     * 
     * @param vDatasource
     */
    public boolean removeDatasource(project.efg.efgDocument.DatasourceType vDatasource)
    {
        boolean removed = _datasourceList.remove(vDatasource);
        return removed;
    } //-- boolean removeDatasource(project.efg.efgDocument.DatasourceType) 

    /**
     * Method setDatasource
     * 
     * @param index
     * @param vDatasource
     */
    public void setDatasource(int index, project.efg.efgDocument.DatasourceType vDatasource)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _datasourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _datasourceList.set(index, vDatasource);
    } //-- void setDatasource(int, project.efg.efgDocument.DatasourceType) 

    /**
     * Method setDatasource
     * 
     * @param datasourceArray
     */
    public void setDatasource(project.efg.efgDocument.DatasourceType[] datasourceArray)
    {
        //-- copy array
        _datasourceList.clear();
        for (int i = 0; i < datasourceArray.length; i++) {
            _datasourceList.add(datasourceArray[i]);
        }
    } //-- void setDatasource(project.efg.efgDocument.DatasourceType) 

    /**
     * Method unmarshalDatasourcesType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalDatasourcesType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.DatasourcesType) Unmarshaller.unmarshal(project.efg.efgDocument.DatasourcesType.class, reader);
    } //-- java.lang.Object unmarshalDatasourcesType(java.io.Reader) 

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
