/**
 * 
 */
package project.efg.server.utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
/**
 * @author jacob.asiedu
 *
 */
public class GlossaryObject implements Comparator{
	private Set mediaresources;
	private Set alsoSee;
	private String term;
	private Set definitions;
	
	public GlossaryObject(){
		this.mediaresources = new TreeSet();
		this.alsoSee = new TreeSet();
		this.definitions = new TreeSet();
	}
	/**
	 * The term under discussion
	 * @param term
	 */
	public void setTerm(String term){
		this.term = term;
	}
	/**
	 * The defintion associated with the current term
	 * @param defn
	 */
	public void addDefinition(String defn){
		this.definitions.add(defn);	
	}
	/**
	 * Add an object that could be associated with the current term
	 * @param alsosee
	 */
	public void addAlsoSee(GlossaryObject alsosee){
		this.alsoSee.add(alsosee);
	}
	/**
	 * Add a Media resource associated with the current term
	 * @param mediaResource
	 */
	public void addMediaresource(String mediaResource){
		this.mediaresources.add(mediaResource);
	}
	/**
	 * The current term
	 * @return
	 */
	public String getTerm(){
		return this.term;
	}
	/**
	 * The definition associated with the current term
	 * @return
	 */
	public List getDefinitions(){
		return new ArrayList(this.definitions);
	}
	/**
	 * A list of Media resources associated with the current term
	 * @return
	 */
	public List getMediaResouces(){
		return new ArrayList(this.mediaresources);
	}
	/**
	 * The terms that could be referenced from the current term
	 * @return
	 */
	public List getAlsoSee(){
		return new ArrayList(this.alsoSee);
	}


	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object object1, Object object2) {
		GlossaryObject obj1 = (GlossaryObject)object1;
		GlossaryObject obj2 = (GlossaryObject)object2;
		
		return this.compare(obj1.getTerm(), obj2.getTerm());
	}
	/**
	 * 
	 */
	public boolean equals(Object object1){
		GlossaryObject obj1 = (GlossaryObject)object1;
		return this.getTerm().equalsIgnoreCase(obj1.getTerm());
	}
	public int hashCode(){
		return this.getTerm().hashCode();
	}
	/**
	 * @param definitions
	 */
	public void addDefinitions(List definitions) {
		for(int i = 0; i < definitions.size();i++){
			String definition = (String)definitions.get(i);
			this.addDefinition(definition);
		}
		
	}
}
