/**
 * 
 */
package project.efg.util.utils;



/**
 * @author jacob.asiedu
 *
 */
public class ByteArrayList extends java.util.ArrayList {
	public ByteArrayList(){
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(byte[] o) {
		return this.add(o);
	}


	public Object get(int index){
		return (byte[])super.get(index);
	}

	/* (non-Javadoc)
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public  byte[] set(int index, byte[] element) {
		return (byte[])super.set(index, element);
	}
	
}
