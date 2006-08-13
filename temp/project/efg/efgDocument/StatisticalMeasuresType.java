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
 * Class StatisticalMeasuresType.
 * 
 * @version $Revision$ $Date$
 */
public class StatisticalMeasuresType extends project.efg.efgDocument.BaseEFGAttributeType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The minimum value of all the minimum values in the list of
     * StatisticalMesure's.It is just a hint to applications that
     * are interested only in the least value of the
     * StatisticalMeasure elements
     */
    private double _min;

    /**
     * keeps track of state for field: _min
     */
    private boolean _has_min;

    /**
     * The maximum value of all the maximum values in the list of
     * StatisticalMesure's.It is just a hint to applications that
     * are interested only in the highest value of the
     * StatisticalMeasure elements
     */
    private double _max;

    /**
     * keeps track of state for field: _max
     */
    private boolean _has_max;

    /**
     * The default unit to be used if non is specified in the list
     * of StatisticalMeasure's.This could also be used as a hint to
     * applications that all of the StatisticalMeasure elements
     * have this unit of measure
     */
    private java.lang.String _unit;

    /**
     * Field _statisticalMeasureList
     */
    private java.util.ArrayList _statisticalMeasureList;


      //----------------/
     //- Constructors -/
    //----------------/

    public StatisticalMeasuresType() {
        super();
        _statisticalMeasureList = new ArrayList();
    } //-- project.efg.efgDocument.StatisticalMeasuresType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addStatisticalMeasure
     * 
     * @param vStatisticalMeasure
     */
    public void addStatisticalMeasure(project.efg.efgDocument.StatisticalMeasureType vStatisticalMeasure)
        throws java.lang.IndexOutOfBoundsException
    {
        _statisticalMeasureList.add(vStatisticalMeasure);
    } //-- void addStatisticalMeasure(project.efg.efgDocument.StatisticalMeasureType) 

    /**
     * Method addStatisticalMeasure
     * 
     * @param index
     * @param vStatisticalMeasure
     */
    public void addStatisticalMeasure(int index, project.efg.efgDocument.StatisticalMeasureType vStatisticalMeasure)
        throws java.lang.IndexOutOfBoundsException
    {
        _statisticalMeasureList.add(index, vStatisticalMeasure);
    } //-- void addStatisticalMeasure(int, project.efg.efgDocument.StatisticalMeasureType) 

    /**
     * Method clearStatisticalMeasure
     */
    public void clearStatisticalMeasure()
    {
        _statisticalMeasureList.clear();
    } //-- void clearStatisticalMeasure() 

    /**
     * Method deleteMax
     */
    public void deleteMax()
    {
        this._has_max= false;
    } //-- void deleteMax() 

    /**
     * Method deleteMin
     */
    public void deleteMin()
    {
        this._has_min= false;
    } //-- void deleteMin() 

    /**
     * Method enumerateStatisticalMeasure
     */
    public java.util.Enumeration enumerateStatisticalMeasure()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_statisticalMeasureList.iterator());
    } //-- java.util.Enumeration enumerateStatisticalMeasure() 

    /**
     * Returns the value of field 'max'. The field 'max' has the
     * following description: The maximum value of all the maximum
     * values in the list of StatisticalMesure's.It is just a hint
     * to applications that are interested only in the highest
     * value of the StatisticalMeasure elements
     * 
     * @return the value of field 'max'.
     */
    public double getMax()
    {
        return this._max;
    } //-- double getMax() 

    /**
     * Returns the value of field 'min'. The field 'min' has the
     * following description: The minimum value of all the minimum
     * values in the list of StatisticalMesure's.It is just a hint
     * to applications that are interested only in the least value
     * of the StatisticalMeasure elements
     * 
     * @return the value of field 'min'.
     */
    public double getMin()
    {
        return this._min;
    } //-- double getMin() 

    /**
     * Method getStatisticalMeasure
     * 
     * @param index
     */
    public project.efg.efgDocument.StatisticalMeasureType getStatisticalMeasure(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _statisticalMeasureList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (project.efg.efgDocument.StatisticalMeasureType) _statisticalMeasureList.get(index);
    } //-- project.efg.efgDocument.StatisticalMeasureType getStatisticalMeasure(int) 

    /**
     * Method getStatisticalMeasure
     */
    public project.efg.efgDocument.StatisticalMeasureType[] getStatisticalMeasure()
    {
        int size = _statisticalMeasureList.size();
        project.efg.efgDocument.StatisticalMeasureType[] mArray = new project.efg.efgDocument.StatisticalMeasureType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (project.efg.efgDocument.StatisticalMeasureType) _statisticalMeasureList.get(index);
        }
        return mArray;
    } //-- project.efg.efgDocument.StatisticalMeasureType[] getStatisticalMeasure() 

    /**
     * Method getStatisticalMeasureCount
     */
    public int getStatisticalMeasureCount()
    {
        return _statisticalMeasureList.size();
    } //-- int getStatisticalMeasureCount() 

    /**
     * Returns the value of field 'unit'. The field 'unit' has the
     * following description: The default unit to be used if non is
     * specified in the list of StatisticalMeasure's.This could
     * also be used as a hint to applications that all of the
     * StatisticalMeasure elements have this unit of measure
     * 
     * @return the value of field 'unit'.
     */
    public java.lang.String getUnit()
    {
        return this._unit;
    } //-- java.lang.String getUnit() 

    /**
     * Method hasMax
     */
    public boolean hasMax()
    {
        return this._has_max;
    } //-- boolean hasMax() 

    /**
     * Method hasMin
     */
    public boolean hasMin()
    {
        return this._has_min;
    } //-- boolean hasMin() 

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
     * Method removeStatisticalMeasure
     * 
     * @param vStatisticalMeasure
     */
    public boolean removeStatisticalMeasure(project.efg.efgDocument.StatisticalMeasureType vStatisticalMeasure)
    {
        boolean removed = _statisticalMeasureList.remove(vStatisticalMeasure);
        return removed;
    } //-- boolean removeStatisticalMeasure(project.efg.efgDocument.StatisticalMeasureType) 

    /**
     * Sets the value of field 'max'. The field 'max' has the
     * following description: The maximum value of all the maximum
     * values in the list of StatisticalMesure's.It is just a hint
     * to applications that are interested only in the highest
     * value of the StatisticalMeasure elements
     * 
     * @param max the value of field 'max'.
     */
    public void setMax(double max)
    {
        this._max = max;
        this._has_max = true;
    } //-- void setMax(double) 

    /**
     * Sets the value of field 'min'. The field 'min' has the
     * following description: The minimum value of all the minimum
     * values in the list of StatisticalMesure's.It is just a hint
     * to applications that are interested only in the least value
     * of the StatisticalMeasure elements
     * 
     * @param min the value of field 'min'.
     */
    public void setMin(double min)
    {
        this._min = min;
        this._has_min = true;
    } //-- void setMin(double) 

    /**
     * Method setStatisticalMeasure
     * 
     * @param index
     * @param vStatisticalMeasure
     */
    public void setStatisticalMeasure(int index, project.efg.efgDocument.StatisticalMeasureType vStatisticalMeasure)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _statisticalMeasureList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _statisticalMeasureList.set(index, vStatisticalMeasure);
    } //-- void setStatisticalMeasure(int, project.efg.efgDocument.StatisticalMeasureType) 

    /**
     * Method setStatisticalMeasure
     * 
     * @param statisticalMeasureArray
     */
    public void setStatisticalMeasure(project.efg.efgDocument.StatisticalMeasureType[] statisticalMeasureArray)
    {
        //-- copy array
        _statisticalMeasureList.clear();
        for (int i = 0; i < statisticalMeasureArray.length; i++) {
            _statisticalMeasureList.add(statisticalMeasureArray[i]);
        }
    } //-- void setStatisticalMeasure(project.efg.efgDocument.StatisticalMeasureType) 

    /**
     * Sets the value of field 'unit'. The field 'unit' has the
     * following description: The default unit to be used if non is
     * specified in the list of StatisticalMeasure's.This could
     * also be used as a hint to applications that all of the
     * StatisticalMeasure elements have this unit of measure
     * 
     * @param unit the value of field 'unit'.
     */
    public void setUnit(java.lang.String unit)
    {
        this._unit = unit;
    } //-- void setUnit(java.lang.String) 

    /**
     * Method unmarshalStatisticalMeasuresType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalStatisticalMeasuresType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.StatisticalMeasuresType) Unmarshaller.unmarshal(project.efg.efgDocument.StatisticalMeasuresType.class, reader);
    } //-- java.lang.Object unmarshalStatisticalMeasuresType(java.io.Reader) 

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
