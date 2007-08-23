/**
 * 
 */
package project.efg.client.factory.gui;

import java.lang.reflect.Constructor;

import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import project.efg.client.impl.gui.CreateEFGUserDialog;
import project.efg.client.impl.gui.LoginDialog;
import project.efg.client.impl.gui.SynopticKeyTreeModelImpl;
import project.efg.client.impl.gui.SynopticTreeInterfaceImpl;
import project.efg.client.impl.nogui.ImportBehaviorImplNew;
import project.efg.client.interfaces.gui.EFGDatasourceObjectListInterface;
import project.efg.client.interfaces.gui.ImportBehavior;
import project.efg.client.interfaces.gui.LoginListenerInterface;
import project.efg.client.interfaces.gui.SynopticKeyTreeInterface;
import project.efg.client.interfaces.gui.SynopticKeyTreeModelInterface;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.util.utils.DBObject;

/**
 * @author jacob.asiedu
 *
 */
public class GUIFactory {

	
	static Logger log;
	static {
		try {
			log = Logger.getLogger(GUIFactory.class);
		} catch (Exception ee) {
		}
	}
	public GUIFactory(){
		
	}
	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.LoginListenerFactoryInterface#getLoginListener()
	 */
	public static LoginListenerInterface getLoginListener(LoginDialog login) {
		return new project.efg.client.rdb.gui.LoginListenerImpl(login);
	}
	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.SynopticKeyTreeFactoryInterface#getSynopticKeyTree(project.efg.Imports.efgImpl.DBObject)
	 */
	public static SynopticKeyTreeInterface getSynopticKeyTree(DBObject dbObject,JFrame frame) {
		return new SynopticTreeInterfaceImpl(dbObject,frame);
	}
	public static SynopticKeyTreeModelInterface getSynopticKeyTreeModel(DefaultMutableTreeNode root){
		
		return new SynopticKeyTreeModelImpl(root,
				SpringGUIFactory.getComparator("treestringcomparator"));
	}
	/**
	 * Create a Listener when user intends to create a new EFG user
	 * @param login
	 * @return
	 */
	public static LoginListenerInterface getEFGCreateUserListener(CreateEFGUserDialog login) {
		return new project.efg.client.rdb.gui.CreateEFGUserListener(login);
	}
	public static synchronized ImportBehavior getImportBehavior(EFGDatasourceObjectListInterface lists,
			EFGDatasourceObjectInterface obj, 
			 String behaviorType){
		try{
			Class cls =  Class.forName(behaviorType);
		       Constructor constructor
		         = cls.getConstructor( new Class[]{ 
		        		 project.efg.client.interfaces.gui.EFGDatasourceObjectListInterface.class,
		        		 project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface.class } 
		         );
		       Object importB
		         = constructor.newInstance( new Object[]{ lists,obj});
		       return (ImportBehavior)importB; 
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.debug("Returning default!!");
		}
		
		return new ImportBehaviorImplNew(lists,obj);
		
	}
}
