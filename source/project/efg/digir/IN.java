package project.efg.digir;

public class IN extends MULTICOP 
{
  private String name;
  private EFGQueryList list;

  public IN(String name) {
      this.name = name;
  }
  public String getName(){
    return this.name;
  }
  public EFGQueryList getList(){
    if(this.list == null)
      this.list = new EFGQueryList();
    return this.list;
  }
  public void add(String value){
    if(this.list == null)
      this.list = new EFGQueryList();

    list.add(value);
  }
  public void setList(EFGQueryList list){
    this.list = list;
  }
}
