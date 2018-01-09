package Structures;/*Nombre del grupo: Chupipandi
 *Nombre y apellidos de cada componente: Jaime Alc�ntara Arnela e Iv�n Gonz�lez Dom�nguez
 *N�mero de entrega (EC1, EC2, febrero, junio, etc.): Enero
 *Curso: 2� A
 */

/**
* Implementation of the method for the Structures.List class.
*
* @version 2.0
* @author
* <b> la chupipandi </b><br>
* Program Development<br/>
* 16/17 Course
*/
public class List <type extends Comparable <type>>{

	private Node first;

	private Node last;
	

	Integer size=0;
	
    public class Node {

        private type Data;

        private Node next;

        private Node prev;

        public Node(Node prev, type Data, Node next) {
            this.Data = Data;
            this.next = next;
            this.prev = prev;
        }

        public Node next() {
        		return next;
        }

        public Node prev() {
        		return prev;
        }

        public type get() {
        		return Data;
        }
    }

	public List() {
		first = last = null;
		size = 0;
	}

	public List(type data) {
		addLast(data);
	}

	public type getFirst() {
		return first.get();
	}

	public type getLast() {
		return last.get();
	}

	public Node first() {
		return first;
	}

	public Node last() {
		return last;
	}

	public boolean estaVacia (){
		return (size == 0);
	}

	public Integer size (){
		return size;
	}

	public type get (Integer pos){
		type d = null;
		Node iter=first;
		Integer i=0; 
		boolean encontrado = false;
		while(i<=pos && !encontrado && iter!= null) {
			if(i==pos) {
				encontrado = true;
				d = iter.get();
			}
			i++;
			iter=iter.next;
		}
		return d;
	}

	public void addLast(type Data) {
        Node l = last;
        Node nodo = new Node(l, Data, null);
        last = nodo;
        if (l == null)
            first = nodo;
        else
            l.next = nodo;
        size++;
	}

	public void removeLast() {
		if (last != null){
			last = last.prev();
		}	
		//there are not elements
		if (last == null) 	first = null;
		else last.next=null;
		size --;
	}

	// ************************************************************************************
	// ***** Additional exercises for the students ****************************************
 	// ************************************************************************************
	/**
	 * It removes the first element in the list
	 *
	 */
	public void removeFirst() {
		//TODO: Implement this method
		if(first != null)
			first = first.next;
		if(first == null)	last = null;
		else first.prev = null;
		size --;
	}
	
	
	/**
	 * It removes the data passed as parameter (if it is stored in the list)
	 *
	 * 
	 */
	public int removeDato(type dato) {
		//TODO: Implement the method
		Node aux = first;
		for(int i = 0;i < size; i++){
			if(aux.Data == dato){
				if(aux.prev == null){
					first = aux.next;
					first.prev = null;
					size--;
				}
				else{
					if(aux.next == null){
						last = aux.prev;
						last.next = null;
						size--;
					}
					else{
						aux.prev = aux.next;
						size--;
					}
				}
			}
			aux = aux.next;
		}
		return -1;
	}
	
	
	/**
	* It adds an element to the list in a sorted way
	*
	*/
	public void sortedAdd(type Data) {
		//TODO: Implement the method
		//Por ahora no nos ha hecho falta ya que es imposible comparar types como si fueran int
		//los metodos de otras clases se encargan de añadir los tipos en orden
	}
	
	/**
	 * It adds an element to the list by the beginning
	 *
	 * @param Data valor que se va a insertar
	 */
	public void addFirst(type Data) {
		//TODO: Implement the method
		Node l = first;
		Node nodo = new Node(null, Data, l);
		first = nodo;
		if (l == null)
			last = nodo;
		else
			l.prev = nodo;
		size++;
	}

	
	/**
	 * Checks whether a data is contained in the list
	 *
	 */
	public boolean contains(type Data) {
		//TODO: Implement the method
		boolean contain = false;
		Node aux = first;
		for(int i = 0;i < size;i++){
			if(aux.Data == Data)
				contain = true;
			aux = aux.next;
		}
		return contain;
   }
   
	/**
	 * It adds a data into the list before the value passed as parameter (searchedValue) 
	 *
	 * @param Data valor que se va a insertar
	 * @param searchedValue valor delante del cual se insertará el nuevo dato
	 */
	public void addBefore(type Data, type searchedValue ) {
		//TODO: Implement the method
		int i = 0;
		boolean added = false;
		Node aux = first;
		Node aux2 = null;
		while(i<size && !added){
			if(aux.Data == searchedValue){
				added = true;
				if(aux.prev == null){
					aux2 = new Node(null,Data,first);
					first = aux2;
					size++;
				}
				else{
					aux2 = new Node(aux.prev,Data,aux);
					aux.prev = aux2;
					size++;
				}
			}
			aux = aux.next;
			i++;
		}

	}

	/**
	 * It adds a data into the list after the value passed as parameter (searchedValue) 
	 *
	 */
	public void addAfter(type Data, type searchedValue ) {
		//TODO: Implement the method
		int i = 0;
		boolean added = false;
		Node aux = first;
		Node aux2 = null;
		while(i<size && !added){
			if(aux.Data == searchedValue){
				added = true;
				if(aux.next == null){
					aux2 = new Node(aux,Data,null);
					last = aux2;
					size++;
				}
				else{
					aux2 = new Node(aux,Data,aux.next);
					aux.next = aux2;
					size++;
				}
			}
			aux = aux.next;
			i++;
		}
	}
	
	/**
	 * It adds a data into the list before the position passed as parameter (index) 
	 *
	 */
	public void addBeforeIndex(type Data, int index) {
		//TODO: Implement the method
		int i = 0;
		boolean added = false;
		Node aux = first;
		Node aux2 = null;
		while(i<size && !added){
			if(i == index){
				added = true;
				if(aux.prev == null){
					aux2 = new Node(null,Data,first);
					first = aux2;
				}
				else{
					aux2 = new Node(aux.prev,Data,aux);
					aux.prev = aux2;
				}
			}
			aux = aux.next;
			i++;
		}
	}
   
	/**
	 * It adds a data into the list after the position passed as parameter (index)
	 *
	 */
	public void addAfterIndex(type Data, int index ) {
		//TODO: Implement the method
		int i = 0;
		boolean added = false;
		Node aux = first;
		Node aux2 = null;
		while(i<size && !added){
			if(i == index){
				added = true;
				if(aux.next == null){
					aux2 = new Node(aux,Data,null);
					last = aux2;
					size++;
				}
				else{
					aux2 = new Node(aux,Data,aux.next);
					aux.next = aux2;
					size++;
				}
			}
			aux = aux.next;
			i++;
		}
	}

	/**
	 * It adds a data into the list in the position passed as parameter (index)
	 *
	 */
	public void addIndex(type Data, int index ) {
		//TODO: Implement the method
		int i = 0;
		boolean added = false;
		Node aux = first;
		Node aux2 = null;
		while(i<size && !added){
			if(i == index){
				added = true;
				if(aux.prev == null){
					aux2 = new Node(null,Data,first);
					first = aux2;
					size++;
				}
				else{
					if(aux.next == null){
						aux2 = new Node(last,Data,null);
						last = aux2;
						size++;
					}
					else {
						aux2 = new Node(aux.prev, Data, aux);
						aux.prev = aux2;
						size++;
					}
				}
			}
			aux = aux.next;
			i++;
		}
	}

	/**
	 * Permite eliminar el Data almacenado en una posición dada
	 *
	 * @param index posición del Data que se eliminará
	 * @return el dato que está al inicio de la lista
	 */
	public void removeIndex(int index) {
		//TODO: Implement the method 
		int i = 0;
		boolean deleted = false;
		Node aux = first;
		Node aux2 = null;
		while(i<size && !deleted){
			if(i == index){
				deleted = true;
				if(aux.prev == null){
					first = aux.next;
					first.prev = null;
					size--;
				}
				else{
					if(aux.next == null){
						last = aux.prev;
						last.next = null;
						size--;
					}
					else {
						aux2 = aux.prev;
						aux2.next = aux.next;
						aux = null;
						aux2.next.prev = aux2;
						size--;
					}
				}
			}
			aux = aux.next;
			i++;
		}
	}

	
	/**
	* Change the value stored in the index position by the data passed as parameter
	*/
	public void set(int index, type Data) {
		//TODO: Implement the method
		int i = 0;
		boolean changed = false;
		Node aux = first;
		while(i<size && !changed){
			if(i == index){
				changed = true;
				aux.Data = Data;

			}
			aux = aux.next;
			i++;
		}

	}
}



