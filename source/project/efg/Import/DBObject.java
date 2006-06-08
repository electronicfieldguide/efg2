package project.efg.Import;

/**
 * DBObject.java
 *
 *
 * Created: Fri Feb 24 12:13:13 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */
public class DBObject {
    protected String userName;
    protected String password;
    protected String url;
    
    public DBObject(String url, String username, String password) {
	this.url = url;
	this.password = password;
	this.userName = username;
    } // DBObject constructor
    
} // DBObject
