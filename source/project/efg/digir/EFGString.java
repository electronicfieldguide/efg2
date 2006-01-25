package project.efg.digir;
//A wrapper around java.lang.String
//Used as helper class in EFGParserHandler

public class EFGString 
{
  private String contents;
  public EFGString(String contents){
    this.contents = contents;
  }
  public String getContents(){
    return contents;
  }
  public String toString(){
    return contents;
  }
  public boolean equals(EFGString efg){
    return getContents().equals(efg.getContents());
  }
  public int hashCode(){
    return contents.hashCode();
  }
}
