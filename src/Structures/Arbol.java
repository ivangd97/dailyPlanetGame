package Structures;

/**
 * Implementacion de arbol binario de busqueda.
 * @version 1.0
 * @author
 * Asignatura Desarrollo de Programas<br/>
 * <b> Profesores DP </b><br>
 * Curso 14/15
 */
public class Arbol <type extends Comparable <type>> {

	/** Dato almacenado en cada nodo del árbol. */
	private type datoRaiz;
	
	/** Atributo que indica si el árbol está vacío. */
	boolean esVacio;
	
	/** Hijo izquierdo del nodo actual */
	private Arbol<type> hIzq;
	
	/** Hijo derecho del nodo actual */
	private Arbol<type> hDer;
	
	/**
	 * Constructor por defecto de la clase. Inicializa un árbol vacío.
	 */
	public Arbol(){
	    this.esVacio=true;
	    this.hIzq = null;
	    this.hDer = null;
	}

	/**
	 * Crea un nuevo árbol a partir de los datos pasados por parámetro.
	 *
	 * @param hIzq El hijo izquierdo del árbol que se está creando 
	 * @param datoRaiz Raíz del árbol que se está creando
	 * @param hDer El hijo derecho del árbol que se está creando
	 */
	public Arbol (Arbol<type> hIzq, type datoRaiz, Arbol<type> hDer){
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
	public Arbol getHijoIzq(){
		return hIzq;
	}
	
	/**
	 * Devuelve el hijo derecho del árbol
	 *
	 * @return Hijo derecho del árbol
	 */
	public Arbol getHijoDer(){
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
	            Arbol<type> aux;
	            if (dato.compareTo(this.datoRaiz)<0) { //dato < datoRaiz
	                if ((aux=getHijoIzq())==null)
	                    hIzq = aux = new Arbol();
	            }
	            else {									//dato > datoRaiz
	                if ((aux=getHijoDer())==null)
	                    hDer = aux = new Arbol();
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
	    Arbol<type> aux=null;
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
					hIzq = hIzq.borrarOrden(dato);
			}	
	        else
	            if (dato.compareTo(this.datoRaiz)>0) {		//dato>datoRaiz 
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
	private Arbol borrarOrden(type dato)
	{
	    type datoaux;
	    Arbol<type> retorno=this;
	    Arbol<type> aborrar, candidato, antecesor;

	    if (!vacio()) {
	        if (dato.compareTo(this.datoRaiz)<0){		// dato<datoRaiz
		    	        hIzq = hIzq.borrarOrden(dato);
	        }
			else
	            if (dato.compareTo(this.datoRaiz)>0) {	// dato>datoRaiz
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
	                            candidato = this.getHijoIzq();
	                            antecesor = this;
	                            while (candidato.getHijoDer()!=null) {
	                                antecesor = candidato;
	                                candidato = candidato.getHijoDer();
	                            }

	                            /*Intercambio de datos de candidato*/
	                            datoaux = datoRaiz;
	                            datoRaiz = candidato.getRaiz();
	                            candidato.datoRaiz=datoaux;
	                            aborrar = candidato;
	                            if (antecesor==this)
	                                hIzq=candidato.getHijoIzq();
	                            else
	                                antecesor.hDer=candidato.getHijoIzq();
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
	    Arbol<type 	> aux=null;
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
}
