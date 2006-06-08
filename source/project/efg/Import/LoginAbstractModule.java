package project.efg.Import;

/**
 * LoginAbstractModule.java
 *$Id$
 *
 * Created: Thu Feb 23 09:31:55 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 *
 * This abstract class should  be implemented to allow a 
 * user to log into a database.
 */
public abstract class LoginAbstractModule {
    /**
     *@param userName The username to be used to log into a database
     *@param password The password to be used to log into the database
     *@parame url - The url to the databse
     */
    public abstract Object login(String userName, String password, String url);
} // LoginAbstractModule
