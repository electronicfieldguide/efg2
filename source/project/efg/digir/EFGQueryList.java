package project.efg.digir;

import java.util.ArrayList;
import java.util.*;

/**
 * A wrapper around a java.util.ArrayList. To be used later in implementing the IN operator
 *
 */
public class EFGQueryList extends ArrayList {

  public EFGQueryList(){
    super();
  }
  public Object clone(){
    EFGQueryList clonedList = new EFGQueryList();
    for(int i = 0; i < this.size(); i++){
      String child = (String)this.get(i);
      clonedList.add(child);
    }
    return clonedList;
  }
  public String toString(){
    StringBuffer buf = new StringBuffer(this.size() * 2) ;
    for(int i = 0; i < this.size(); i++){
      buf.append((String)this.get(i));
      buf.append(" ");
    }
    return buf.toString();
  }
 
}
