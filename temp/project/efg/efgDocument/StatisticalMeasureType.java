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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class StatisticalMeasureType.
 * 
 * @version $Revision$ $Date$
 */
public class StatisticalMeasureType extends project.efg.efgDocument.EFGType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The minimum value for this statistical measure
     */
    private double _min;

    /**
     * keeps track of state for field: _min
     */
    private boolean _has_min;

    /**
     * The maximum value of this statistical measure
     */
    private double _max;

    /**
     * keeps track of state for field: _max
     */
    private boolean _has_max;

    /**
     * The units if any for this statistical measure
     */
    private java.lang.String _units;


      //----------------/
     //- Constructors -/
    //----------------/

    public StatisticalMeasureType() {
        super();
    } //-- project.efg.efgDocument.StatisticalMeasureType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'max'. The field 'max' has the
     * following description: The maximum value of this statistical
     * measure
     * 
     * @return the value of field 'max'.
     */
    public double getMax()
    {
        return this._max;
    } //-- double getMax() 

    /**
     * Returns the value of field 'min'. The field 'min' has the
     * following description: The minimum value for this
     * statistical measure
     * 
     * @return the value of field 'min'.
     */
    public double getMin()
    {
        return this._min;
    } //-- double getMin() 

    /**
     * Returns the value of field 'units'. The field 'units' has
     * the following description: The units if any for this
     * statistical measure
     * 
     * @return the value of field 'units'.
     */
    public java.lang.String getUnits()
    {
        return this._units;
    } //-- java.lang.String getUnits() 

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
     * Sets the value of field 'max'. The field 'max' has the
     * following description: The maximum value of this statistical
     * measure
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
     * following description: The minimum value for this
     * statistical measure
     * 
     * @param min the value of field 'min'.
     */
    public void setMin(double min)
    {
        this._min = min;
        this._has_min = true;
    } //-- void setMin(double) 

    /**
     * Sets the value of field 'units'. The field 'units' has the
     * following description: The units if any for this statistical
     * measure
     * 
     * @param units the value of field 'units'.
     */
    public void setUnits(java.lang.String units)
    {
        this._units = units;
    } //-- void setUnits(java.lang.String) 

    /**
     * Method unmarshalStatisticalMeasureType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalStatisticalMeasureType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.efgDocument.StatisticalMeasureType) Unmarshaller.unmarshal(project.efg.efgDocument.StatisticalMeasureType.class, reader);
    } //-- java.lang.Object unmarshalStatisticalMeasureType(java.io.Reader) 

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
