package Structures;

public class Cola <type extends Comparable <type>>{
	
	/** Reference to the first and last of the DE*/
	private Node first;
	private Node last;
	
    private class Node {
    	/** Data stored in each node */
        private type data;
    	/** Reference to the next node */
        private Node next;
        
        /**
    	 * Parametrized Constructor for the Node class
    	 */
        Node (type data) {
            this.data = data;
            this.next = null;
        }
    }//class Node
    
    /**
	 * Default Constructor for the Structures.Structures.Cola class
	 */
	public Cola() {
		first = null;
		last = null;
	}

	/**
	 * Parametrized method for the Structures.List class
	 *
	 * @param data the data that the DE will store
	 */
	public Cola(type data) {
		Node node = new Node(data);
		first.next = node;
		last = node;
	}
	
	/**
	 * Method that returns the data in the top of the DE
	 *
	 * @return the data in the top of the DE
	 */
	public type getFirst(){
		return first.data;
	}
	

	/**
	 * Method to check whether the DE is empty 
	 *
	 * @return true if the DE is empty and otherwise false
	 */
	public boolean isEmpty (){
		return (first==null);
	}
	

	/**
	 * Method that adds a data into the DE
	 *
	 * @param data the value that will be added 
	 */
	public void addData (type data) {
		if(isEmpty()){
			Node node = new Node(data);
			first = node;
			last = node;
		}
		else{
			Node node = new Node(data);
			last.next = node;
			last = node;
		}
	}


	/**
	* Removes a data from the DE (the data in the top)
	*/
	public void removeData() {
		if (!isEmpty()){		
			first = first.next;
		}
	}
}
