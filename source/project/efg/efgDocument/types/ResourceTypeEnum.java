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
public class ResourceTypeEnum implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Image type
     */
    public static final int IMAGE_TYPE = 0;

    /**
     * The instance of the Image type
     */
    public static final ResourceTypeEnum IMAGE = new ResourceTypeEnum(IMAGE_TYPE, "Image");

    /**
     * The Text type
     */
    public static final int TEXT_TYPE = 1;

    /**
     * The instance of the Text type
     */
    public static final ResourceTypeEnum TEXT = new ResourceTypeEnum(TEXT_TYPE, "Text");

    /**
     * The Audio type
     */
    public static final int AUDIO_TYPE = 2;

    /**
     * The instance of the Audio type
     */
    public static final ResourceTypeEnum AUDIO = new ResourceTypeEnum(AUDIO_TYPE, "Audio");

    /**
     * The Video type
     */
    public static final int VIDEO_TYPE = 3;

    /**
     * The instance of the Video type
     */
    public static final ResourceTypeEnum VIDEO = new ResourceTypeEnum(VIDEO_TYPE, "Video");

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

    private ResourceTypeEnum(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- project.efg.efgDocument.types.ResourceTypeEnum(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ResourceTypeEnum
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ResourceTypeEnum
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
        members.put("Image", IMAGE);
        members.put("Text", TEXT);
        members.put("Audio", AUDIO);
        members.put("Video", VIDEO);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ResourceTypeEnum
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ResourceTypeEnum based on the
     * given String value.
     * 
     * @param string
     */
    public static project.efg.efgDocument.types.ResourceTypeEnum valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ResourceTypeEnum";
            throw new IllegalArgumentException(err);
        }
        return (ResourceTypeEnum) obj;
    } //-- project.efg.efgDocument.types.ResourceTypeEnum valueOf(java.lang.String) 

}
