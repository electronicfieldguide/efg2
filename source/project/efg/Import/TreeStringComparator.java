// TreeStringComparator.java
// This class compares the contents of the userObject as strings.
// It's case-insensitive.
//
package project.efg.Import; 
import project.efg.efgInterface.EFGDatasourceObjectInterface;
import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeStringComparator implements Comparator {
  public int compare(Object o1, Object o2) {
    if (!(o1 instanceof DefaultMutableTreeNode && 
          o2 instanceof DefaultMutableTreeNode)) {
	throw new IllegalArgumentException("Can only compare DefaultMutableTreeNode objects");
    }
    EFGDatasourceObjectInterface s1 = (EFGDatasourceObjectInterface)(((DefaultMutableTreeNode)o1).getUserObject());
    EFGDatasourceObjectInterface s2 = (EFGDatasourceObjectInterface)(((DefaultMutableTreeNode)o2).getUserObject());
    return s1.toString().compareToIgnoreCase(s2.toString());
  }
}
