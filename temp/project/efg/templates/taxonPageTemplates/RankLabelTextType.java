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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class RankLabelTextType.
 * 
 * @version $Revision$ $Date$
 */
public class RankLabelTextType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Determines the order in which fields/characters are arranged
     * within a container (group or characterValue Type)
     */
    private int _rank;

    /**
     * keeps track of state for field: _rank
     */
    private boolean _has_rank;

    /**
     * A label for the whole group/character.Could be used to tag a
     * set of characters for example on an html page..A label to be
     * used instead of the field or character name..For instance an
     * author could chose to display 'FlowerColor' as "Flower
     * Color"(note the space between the two words)
     */
    private java.lang.String _label;

    /**
     * A piece of text that could be displayed instead of the value
     * of the character(states) from the datasource. 
     */
    private java.lang.String _text;


      //----------------/
     //- Constructors -/
    //----------------/

    public RankLabelTextType() {
        super();
    } //-- project.efg.templates.taxonPageTemplates.RankLabelTextType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'label'. The field 'label' has
     * the following description: A label for the whole
     * group/character.Could be used to tag a set of characters for
     * example on an html page..A label to be used instead of the
     * field or character name..For instance an author could chose
     * to display 'FlowerColor' as "Flower Color"(note the space
     * between the two words)
     * 
     * @return the value of field 'label'.
     */
    public java.lang.String getLabel()
    {
        return this._label;
    } //-- java.lang.String getLabel() 

    /**
     * Returns the value of field 'rank'. The field 'rank' has the
     * following description: Determines the order in which
     * fields/characters are arranged within a container (group or
     * characterValue Type)
     * 
     * @return the value of field 'rank'.
     */
    public int getRank()
    {
        return this._rank;
    } //-- int getRank() 

    /**
     * Returns the value of field 'text'. The field 'text' has the
     * following description: A piece of text that could be
     * displayed instead of the value of the character(states) from
     * the datasource. 
     * 
     * @return the value of field 'text'.
     */
    public java.lang.String getText()
    {
        return this._text;
    } //-- java.lang.String getText() 

    /**
     * Method hasRank
     */
    public boolean hasRank()
    {
        return this._has_rank;
    } //-- boolean hasRank() 

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
     * Sets the value of field 'label'. The field 'label' has the
     * following description: A label for the whole
     * group/character.Could be used to tag a set of characters for
     * example on an html page..A label to be used instead of the
     * field or character name..For instance an author could chose
     * to display 'FlowerColor' as "Flower Color"(note the space
     * between the two words)
     * 
     * @param label the value of field 'label'.
     */
    public void setLabel(java.lang.String label)
    {
        this._label = label;
    } //-- void setLabel(java.lang.String) 

    /**
     * Sets the value of field 'rank'. The field 'rank' has the
     * following description: Determines the order in which
     * fields/characters are arranged within a container (group or
     * characterValue Type)
     * 
     * @param rank the value of field 'rank'.
     */
    public void setRank(int rank)
    {
        this._rank = rank;
        this._has_rank = true;
    } //-- void setRank(int) 

    /**
     * Sets the value of field 'text'. The field 'text' has the
     * following description: A piece of text that could be
     * displayed instead of the value of the character(states) from
     * the datasource. 
     * 
     * @param text the value of field 'text'.
     */
    public void setText(java.lang.String text)
    {
        this._text = text;
    } //-- void setText(java.lang.String) 

    /**
     * Method unmarshalRankLabelTextType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalRankLabelTextType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (project.efg.templates.taxonPageTemplates.RankLabelTextType) Unmarshaller.unmarshal(project.efg.templates.taxonPageTemplates.RankLabelTextType.class, reader);
    } //-- java.lang.Object unmarshalRankLabelTextType(java.io.Reader) 

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
