/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package project.efg.efgField.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Each efgFieldType attribute must have at least one of these
 * enumerated values as its value
 * 
 * @version $Revision$ $Date$
 */
public class EFGFieldTypeEnum implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Narrative type
     */
    public static final int NARRATIVE_TYPE = 0;

    /**
     * The instance of the Narrative type
     */
    public static final EFGFieldTypeEnum NARRATIVE = new EFGFieldTypeEnum(NARRATIVE_TYPE, "Narrative");

    /**
     * The MediaResource type
     */
    public static final int MEDIARESOURCE_TYPE = 1;

    /**
     * The instance of the MediaResource type
     */
    public static final EFGFieldTypeEnum MEDIARESOURCE = new EFGFieldTypeEnum(MEDIARESOURCE_TYPE, "MediaResource");

    /**
     * The Categorical type
     */
    public static final int CATEGORICAL_TYPE = 2;

    /**
     * The instance of the Categorical type
     */
    public static final EFGFieldTypeEnum CATEGORICAL = new EFGFieldTypeEnum(CATEGORICAL_TYPE, "Categorical");

    /**
     * The NumericValue type
     */
    public static final int NUMERICVALUE_TYPE = 3;

    /**
     * The instance of the NumericValue type
     */
    public static final EFGFieldTypeEnum NUMERICVALUE = new EFGFieldTypeEnum(NUMERICVALUE_TYPE, "NumericValue");

    /**
     * The NumericRange type
     */
    public static final int NUMERICRANGE_TYPE = 4;

    /**
     * The instance of the NumericRange type
     */
    public static final EFGFieldTypeEnum NUMERICRANGE = new EFGFieldTypeEnum(NUMERICRANGE_TYPE, "NumericRange");

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

    private EFGFieldTypeEnum(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- project.efg.efgField.types.EFGFieldTypeEnum(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of EFGFieldTypeEnum
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this EFGFieldTypeEnum
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
        members.put("Narrative", NARRATIVE);
        members.put("MediaResource", MEDIARESOURCE);
        members.put("Categorical", CATEGORICAL);
        members.put("NumericValue", NUMERICVALUE);
        members.put("NumericRange", NUMERICRANGE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * EFGFieldTypeEnum
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new EFGFieldTypeEnum based on the
     * given String value.
     * 
     * @param string
     */
    public static project.efg.efgField.types.EFGFieldTypeEnum valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid EFGFieldTypeEnum";
            throw new IllegalArgumentException(err);
        }
        return (EFGFieldTypeEnum) obj;
    } //-- project.efg.efgField.types.EFGFieldTypeEnum valueOf(java.lang.String) 

}
