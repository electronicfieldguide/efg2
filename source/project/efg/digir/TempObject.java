package project.efg.digir;
/**
 * A temporary object used in some of the stack operations
 * Should be extended to implement equals and hashcode if it is used as part of a Collection.
 */
public class TempObject
{
  private String name;
  private String value;

  public TempObject(String name, String value) {
      this.name = name;
      this.value = value;
  }
  public String getName(){
    return this.name;
  }
  public String getValue(){
    return this.value;
  }
  public TempObject getObject(){
    return this;
  }
}
