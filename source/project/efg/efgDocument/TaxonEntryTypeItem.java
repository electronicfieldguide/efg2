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


/**
 * Class TaxonEntryTypeItem.
 * 
 * @version $Revision$ $Date$
 */
public class TaxonEntryTypeItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Anything else that is not an EFGList, MediaResources or a
     * StatisticalMeasure may fit into this container. This
     * container contains one or more Item elements
     */
    private project.efg.efgDocument.ItemsType _items;

    /**
     * Indicates the statistical measures for the current taxon.
     */
    private project.efg.efgDocument.StatisticalMeasuresType _statisticalMeasures;

    /**
     * Indicates a list of names/things For example similar species
     */
    private project.efg.efgDocument.EFGListsType _EFGLists;

    /**
     * A container that encpasulates the mediaresources(
     * audio,video,image names for e.g) for the current taxon
     */
    private project.efg.efgDocument.MediaResourcesType _mediaResources;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaxonEntryTypeItem() {
        super();
    } //-- project.efg.efgDocument.TaxonEntryTypeItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'EFGLists'. The field 'EFGLists'
     * has the following description: Indicates a list of
     * names/things For example similar species
     * 
     * @return the value of field 'EFGLists'.
     */
    public project.efg.efgDocument.EFGListsType getEFGLists()
    {
        return this._EFGLists;
    } //-- project.efg.efgDocument.EFGListsType getEFGLists() 

    /**
     * Returns the value of field 'items'. The field 'items' has
     * the following description: Anything else that is not an
     * EFGList, MediaResources or a StatisticalMeasure may fit into
     * this container. This container contains one or more Item
     * elements
     * 
     * @return the value of field 'items'.
     */
    public project.efg.efgDocument.ItemsType getItems()
    {
        return this._items;
    } //-- project.efg.efgDocument.ItemsType getItems() 

    /**
     * Returns the value of field 'mediaResources'. The field
     * 'mediaResources' has the following description: A container
     * that encpasulates the mediaresources( audio,video,image
     * names for e.g) for the current taxon
     * 
     * @return the value of field 'mediaResources'.
     */
    public project.efg.efgDocument.MediaResourcesType getMediaResources()
    {
        return this._mediaResources;
    } //-- project.efg.efgDocument.MediaResourcesType getMediaResources() 

    /**
     * Returns the value of field 'statisticalMeasures'. The field
     * 'statisticalMeasures' has the following description:
     * Indicates the statistical measures for the current taxon.
     * 
     * @return the value of field 'statisticalMeasures'.
     */
    public project.efg.efgDocument.StatisticalMeasuresType getStatisticalMeasures()
    {
        return this._statisticalMeasures;
    } //-- project.efg.efgDocument.StatisticalMeasuresType getStatisticalMeasures() 

    /**
     * Sets the value of field 'EFGLists'. The field 'EFGLists' has
     * the following description: Indicates a list of names/things
     * For example similar species
     * 
     * @param EFGLists the value of field 'EFGLists'.
     */
    public void setEFGLists(project.efg.efgDocument.EFGListsType EFGLists)
    {
        this._EFGLists = EFGLists;
    } //-- void setEFGLists(project.efg.efgDocument.EFGListsType) 

    /**
     * Sets the value of field 'items'. The field 'items' has the
     * following description: Anything else that is not an EFGList,
     * MediaResources or a StatisticalMeasure may fit into this
     * container. This container contains one or more Item elements
     * 
     * @param items the value of field 'items'.
     */
    public void setItems(project.efg.efgDocument.ItemsType items)
    {
        this._items = items;
    } //-- void setItems(project.efg.efgDocument.ItemsType) 

    /**
     * Sets the value of field 'mediaResources'. The field
     * 'mediaResources' has the following description: A container
     * that encpasulates the mediaresources( audio,video,image
     * names for e.g) for the current taxon
     * 
     * @param mediaResources the value of field 'mediaResources'.
     */
    public void setMediaResources(project.efg.efgDocument.MediaResourcesType mediaResources)
    {
        this._mediaResources = mediaResources;
    } //-- void setMediaResources(project.efg.efgDocument.MediaResourcesType) 

    /**
     * Sets the value of field 'statisticalMeasures'. The field
     * 'statisticalMeasures' has the following description:
     * Indicates the statistical measures for the current taxon.
     * 
     * @param statisticalMeasures the value of field
     * 'statisticalMeasures'.
     */
    public void setStatisticalMeasures(project.efg.efgDocument.StatisticalMeasuresType statisticalMeasures)
    {
        this._statisticalMeasures = statisticalMeasures;
    } //-- void setStatisticalMeasures(project.efg.efgDocument.StatisticalMeasuresType) 

}
