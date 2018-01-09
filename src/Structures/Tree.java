package Structures;/*Nombre del grupo: La Quinta de Pepe el Sanguinario
 *Nombre y apellidos de cada componente: �ngel Hortet Garc�a e Iv�n Gonz�lez Dom�nguez
 *N�mero de entrega (EC1, EC2, febrero, junio, etc.): EC1
 *Curso: 2� A
 */

import Utilities.Weapon;

public class Tree<type extends Comparable <type>> {

	/** Dato almacenado en cada nodo del árbol. */
	private type datoRaiz;
	
	/** Atributo que indica si el árbol está vacío. */
	boolean esVacio;
	
	protected int numElementos;
	
	/** Hijo izquierdo del nodo actual */
	private Tree<type> hIzq;
	
	/** Hijo derecho del nodo actual */
	private Tree<type> hDer;
	
	/**
	 * Constructor por defecto de la clase. Inicializa un árbol vacío.
	 */
	public Tree(){
	    this.esVacio=true;
	    this.hIzq = null;
	    this.hDer = null;
	    this.numElementos = 0;
	}

	/**
	 * Crea un nuevo árbol a partir de los datos pasados por parámetro.
	 *
	 * @param hIzq El hijo izquierdo del árbol que se está creando 
	 * @param datoRaiz Raíz del árbol que se está creando
	 * @param hDer El hijo derecho del árbol que se está creando
	 */
	public Tree(Tree<type> hIzq, type datoRaiz, Tree<type> hDer){
		this.esVacio=false;
		this.datoRaiz = datoRaiz;
		this.hIzq = hIzq;
		this.hDer=hDer;
	}
	
	/**
	 * Devuelve el hijo izquierdo del árbol
	 *
	 * @return El hijo izquierdo
	 */
	public Tree<type> getHijoIzq(){
		return hIzq;
	}
	
	/**
	 * Devuelve el hijo derecho del árbol
	 *
	 * @return Hijo derecho del árbol
	 */
	public Tree<type> getHijoDer(){
		return hDer;
	}
	
	/**
	 * Devuelve la raíz del árbol
	 *
	 * @return La raíz del árbol
	 */
	public type getRaiz(){
		return datoRaiz;
	}
	
	/**
	 * Comprueba si el árbol está vacío.
	 * @return verdadero si el árbol está vacío, falso en caso contrario
	 */
	public boolean vacio(){
		return esVacio;
	}
	
	/**
	 * Inserta un nuevo dato en el árbol.
	 *
	 * @param dato El dato a insertar
	 * @return verdadero si el dato se ha insertado correctamente, falso en caso contrario
	 */
	public boolean insertar(type dato){
		boolean resultado=true;
		if (vacio()) {
			datoRaiz = dato;
			esVacio = false;
		}
		else {
			if (!(this.datoRaiz.equals(dato))) {
				Tree aux;
				if (dato.compareTo(this.datoRaiz)<0) { //dato < datoRaiz
					if ((aux=getHijoIzq())==null)
						hIzq = aux = new Tree();
				}
				else {									//dato > datoRaiz
					if ((aux=getHijoDer())==null)
						hDer = aux = new Tree();
				}
				resultado = aux.insertar(dato);
			}
			else
				resultado=false;
		}
		return resultado;
	}
	
	/**
	 * Comprueba si un dato se encuentra almacenado en el árbol
	 *
	 * @param dato El dato a buscar
	 * @return verdadero si el dato se encuentra en el árbol, falso en caso contrario
	 */
	public boolean pertenece(type dato){
	    Tree<type> aux=null;
	    boolean encontrado=false;
	    if (!vacio()) {
	        if (this.datoRaiz.equals(dato))
	            encontrado = true;
	        else {
	            if (dato.compareTo(this.datoRaiz)<0)	//dato < datoRaiz
	                aux=getHijoIzq();
	            else									//dato > datoRaiz
	                aux = getHijoDer();
	            if (aux!=null)
	                encontrado = aux.pertenece(dato);
	        }
	    }
	    return encontrado;
	}
	
	/**
	 * Borrar un dato del árbol.
	 *
	 * @param dato El dato que se quiere borrar
	 */
	public void borrar(type dato){
	    if (!vacio()) {
	        if (dato.compareTo(this.datoRaiz)<0){			//dato<datoRaiz
				if (hIzq != null)
					hIzq = hIzq.borrarOrden(dato);
			}	
	        else
	            if (dato.compareTo(this.datoRaiz)>0) {		//dato>datoRaiz
					if (hDer != null)
	            		hDer = hDer.borrarOrden(dato);
				}
	            else //En este caso el dato es datoRaiz
	            {
	                if (hIzq==null && hDer==null)
	                {
	                    esVacio=true;
	                }
	                else
	                    borrarOrden(dato);
	            }
	    }
	}
	

	/**
	 * Borrar un dato. Este método es utilizado por el método borrar anterior.
	 *
	 * @param dato El dato a borrar
	 * @return Devuelve el árbol resultante después de haber realizado el borrado
	 */
	private Tree<type> borrarOrden(type dato)
	{
	    type datoaux;
	    Tree<type> retorno=this;
	    Tree<type> aborrar, candidato, antecesor;

	    if (!vacio()) {
	        if (dato.compareTo(this.datoRaiz)<0){		// dato<datoRaiz
				if (hIzq != null)
		    	        hIzq = hIzq.borrarOrden(dato);
	        }
			else
	            if (dato.compareTo(this.datoRaiz)>0) {	// dato>datoRaiz
					if (hDer != null)
	    	           hDer = hDer.borrarOrden(dato);
	            }
				else {
	                aborrar=this;
	                
	                if ((hDer==null)&&(hIzq==null)) { /*si es hoja*/
	                    retorno=null;
	                }
	                else {
	                    if (hDer==null) { /*Solo hijo izquierdo*/
	                        aborrar=hIzq;
	                        datoaux=this.datoRaiz;
	                        datoRaiz=hIzq.getRaiz();
	                        hIzq.datoRaiz = datoaux;
	                        hIzq=hIzq.getHijoIzq();
	                        hDer=aborrar.getHijoDer();

	                        retorno=this;
	                    }
	                    else
	                        if (hIzq==null) { /*Solo hijo derecho*/
	                            aborrar=hDer;
	                            datoaux=datoRaiz;
	                            datoRaiz=hDer.getRaiz();
	                            hDer.datoRaiz = datoaux;
	                            hDer=hDer.getHijoDer();
	                            hIzq=aborrar.getHijoIzq();

	                            retorno=this;
	                        }
	                        else { /* Tiene dos hijos */
	                            candidato = this.getHijoDer();
	                            antecesor = this;
	                            while (candidato.getHijoIzq()!=null) {
	                                antecesor = candidato;
	                                candidato = candidato.getHijoIzq();
	                            }

	                            /*Intercambio de datos de candidato*/
	                            datoaux = datoRaiz;
	                            datoRaiz = candidato.getRaiz();
	                            candidato.datoRaiz=datoaux;
	                            aborrar = candidato;
	                            if (antecesor==this)
	                                hDer=candidato.getHijoDer();
	                            else
	                                antecesor.hIzq=candidato.getHijoDer();
	                        } //Eliminar solo ese nodo, no todo el subarbol
	                    aborrar.hIzq=null;
	                    aborrar.hDer=null;
	                }
	            }
	    }
	    return retorno;
	}
	
	
	/**
	 * Recorrido inOrden del árbol.
	 */
	public void inOrden(){
	    Tree<type> aux=null;
	    if (!vacio()) {
	        if ((aux=getHijoIzq())!=null) {
	            aux.inOrden();
	        }    
	      
	        System.out.println(this.datoRaiz);
	        
	        if ((aux=getHijoDer())!=null){
	            aux.inOrden();
	        }    
	    }
	}

	//Devuelve la profundidad del arbol utilizado
	public int profundidadArbol(){
		int prof = 0;
		int profDr = 0;
		int profIz = 0;
		if(this.esVacio != true){
			profDr = 1;
			profIz = 1;
			if(this.hDer != null){
				profDr += this.getHijoDer().profundidadArbol();
			}
			if(this.hIzq != null){
				profIz += this.getHijoIzq().profundidadArbol();
			}
			if(profDr > profIz)
				prof = profDr;
			else
				prof = profIz;

		}
		return prof;
	}

	//Booleano que nos dice si un dato es o no hoja del arbol
	public boolean EsHoja(type dato){
		Tree<type> aux;
		boolean hoja = false;
		if(vacio() != true){
			if(this.datoRaiz.equals(dato)){
				if(this.hDer == null && this.hIzq == null)
					hoja = true;
			}
			else{
				if (dato.compareTo(this.datoRaiz) < 0)
					aux = getHijoIzq();
				else
					aux =getHijoDer();
				if (aux != null)
					hoja = aux.EsHoja(dato);
			}
		}
		return hoja;
	}

	//Cuenta el numero de hojas que posee el arbol
	public int contarHojas(){
		int total = 0;
		Tree<type> aux;
		if(this.esVacio != true){
			if(this.EsHoja(this.getRaiz())){
				total++;
			}
			else{
				aux = this.getHijoDer();
				if(aux != null){
					total = total + aux.contarHojas();
				}
				aux = this.getHijoIzq();
				if(aux != null){
					total = total + aux.contarHojas();
				}
			}

		}


		return total;
	}

	public void dec_elements(){numElementos--;}

	public int get_elements(){return numElementos;}

}
