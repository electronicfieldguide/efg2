package project.efg.Import;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * LoginModule.java
 *
 *
 * Created: Thu Feb 23 09:36:27 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */
/**
 * Emulator for login module
 */
public class LoginModule extends project.efg.Import.LoginAbstractModule {
    static Logger log = null;

    
    static{
	try{
	    log = Logger.getLogger(LoginModule.class); 
	}
	catch(Exception ee){
	}
    }
    public LoginModule() {
	
    } // LoginModule constructor
    public Object login(String userName, String password, String url) {
	try{
	    return EFGRDBImportUtils.getConnection(url, userName,password);
	}
	catch(Exception ee){
	    log.error(ee.getMessage());
	}
	return null;
    }
} // LoginModule
