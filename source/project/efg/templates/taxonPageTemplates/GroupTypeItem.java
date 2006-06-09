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

import java.io.Serializable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class GroupTypeItem.
 * 
 * @version $Revision$ $Date$
 */
public class GroupTypeItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _characterValue
     */
    private project.efg.templates.taxonPageTemplates.CharacterValue _characterValue;

    /**
     * Encapsulates the various groups making up a template chosen
     * by an author. Each group id must be unique within the scope
     * of the groups element. Each group id is resolvable by the
     * xpath leading to that group. group's could have the same id
     * as long as they do not vbelong to the same parent directly.
     */
    private project.efg.templates.taxonPageTemplates.GroupsType _groups;


      //----------------/
     //- Constructors -/
    //----------------/

    public GroupTypeItem() {
        super();
    } //-- project.efg.templates.taxonPageTemplates.GroupTypeItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'characterValue'.
     * 
     * @return the value of field 'characterValue'.
     */
    public project.efg.templates.taxonPageTemplates.CharacterValue getCharacterValue()
    {
        return this._characterValue;
    } //-- project.efg.templates.taxonPageTemplates.CharacterValue getCharacterValue() 

    /**
     * Returns the value of field 'groups'. The field 'groups' has
     * the following description: Encapsulates the various groups
     * making up a template chosen by an author. Each group id must
     * be unique within the scope of the groups element. Each group
     * id is resolvable by the xpath leading to that group. group's
     * could have the same id as long as they do not vbelong to the
     * same parent directly.
     * 
     * @return the value of field 'groups'.
     */
    public project.efg.templates.taxonPageTemplates.GroupsType getGroups()
    {
        return this._groups;
    } //-- project.efg.templates.taxonPageTemplates.GroupsType getGroups() 

    /**
     * Sets the value of field 'characterValue'.
     * 
     * @param characterValue the value of field 'characterValue'.
     */
    public void setCharacterValue(project.efg.templates.taxonPageTemplates.CharacterValue characterValue)
    {
        this._characterValue = characterValue;
    } //-- void setCharacterValue(project.efg.templates.taxonPageTemplates.CharacterValue) 

    /**
     * Sets the value of field 'groups'. The field 'groups' has the
     * following description: Encapsulates the various groups
     * making up a template chosen by an author. Each group id must
     * be unique within the scope of the groups element. Each group
     * id is resolvable by the xpath leading to that group. group's
     * could have the same id as long as they do not vbelong to the
     * same parent directly.
     * 
     * @param groups the value of field 'groups'.
     */
    public void setGroups(project.efg.templates.taxonPageTemplates.GroupsType groups)
    {
        this._groups = groups;
    } //-- void setGroups(project.efg.templates.taxonPageTemplates.GroupsType) 

}
