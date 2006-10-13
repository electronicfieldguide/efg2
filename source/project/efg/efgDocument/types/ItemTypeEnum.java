/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package project.efg.efgDocument.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * An enumertaion of Image,Text,Audio and Video
 * 
 * @version $Revision$ $Date$
 */
public class ItemTypeEnum implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The narrative type
     */
    public static final int NARRATIVE_TYPE = 0;

    /**
     * The instance of the narrative type
     */
    public static final ItemTypeEnum NARRATIVE = new ItemTypeEnum(NARRATIVE_TYPE, "narrative");

    /**
     * The categorical type
     */
    public static final int CATEGORICAL_TYPE = 1;

    /**
     * The instance of the categorical type
     */
    public static final ItemTypeEnum CATEGORICAL = new ItemTypeEnum(CATEGORICAL_TYPE, "categorical");

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private ItemTypeEnum(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- project.efg.efgDocument.types.ItemTypeEnum(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ItemTypeEnum
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ItemTypeEnum
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("narrative", NARRATIVE);
        members.put("categorical", CATEGORICAL);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ItemTypeEnum
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ItemTypeEnum based on the given
     * String value.
     * 
     * @param string
     */
    public static project.efg.efgDocument.types.ItemTypeEnum valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ItemTypeEnum";
            throw new IllegalArgumentException(err);
        }
        return (ItemTypeEnum) obj;
    } //-- project.efg.efgDocument.types.ItemTypeEnum valueOf(java.lang.String) 

}
