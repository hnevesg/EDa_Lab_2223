import graphsDSESIUCLM.*;

public class DecoratedElement<T> implements Element {

	  private String ID;                 
	  private T element;                
	  private boolean visited;         
	  
	  public DecoratedElement (String key, T element) {
	    this.element = element;
	    ID = key;
	    visited = false;
	  }	  

	  public T getElement() {
	    return element;
	  }
	  public boolean getVisited() {
	    return visited;
	  }
	  public void setVisited(boolean t) {
	    visited = t;
	  }	  
	  public boolean equals (Object n) {
		   return (ID.equals(((DecoratedElement) n).getID())
		        && element.equals(((DecoratedElement<T>) n).getElement()));
	  }
	  public String toString() {
	    return element.toString();   
	  }
	  public String getID() {
	    return ID;
	  }
}
