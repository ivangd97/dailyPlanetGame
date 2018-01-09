package Utilities;

import Structures.Arbol;
import mainPackage.Map;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*Nombre del grupo: La Quinta de Pepe el Sanguinario
 *Nombre y apellidos de cada componente: Angel Hortet Garcia e Ivan Gonzalez Dominguez
 *Número de entrega (EC1, EC2, febrero, junio, etc.): EC1
 *Curso: 2º A
 */
public class DoorMan {

    //Este arbol contiene la combinacion de armas del hombre puerta
    protected Arbol<Weapon> doorManTree = new Arbol<Weapon>();

    //Esta bandera nos indica si la puerta está o no abierta
    protected boolean isOpen;

    //Esta variable nos indica la altura del arbol de armas del hombre puerta
    protected int lockHeight;

    //Contructor por defecto del hombre puerta
    //PRE:
    //POST: Crea un fantastico, impresionante y maravilloso hombre picatoste, nunca recibe el mismo nombre en los comentarios
    // para no revelar su identidad
    public DoorMan() {
        isOpen = false;
    }

    //Método que configura el árbol de armas del hombre puerta
    //PRE: DoorMan bien creado e inicializado
    //POST: Configura el arbol del hombre
    public void configurateTree(Weapon[] newVector) {
        for(int i = 0;i < newVector.length; i++){
            doorManTree.insertar(newVector[i]);
        }
    }

    //Este metodo cierra la puerta en caso de que se encuentre abierta
    //PRE: DoorMan bien creado e inicializado, isOpen = true
    //POST: Pone a falso el booleano que indica si la verja esta abierta
    public void close() {
        isOpen = false;
    }

    //Devuelve el estado de la puerta
    //PRE: DoorMan bien creado e inicializado
    //POST: Nos dice si la puerta está abierta o no
    public boolean doorOpen() {
        return isOpen;
    }

    //Devuelve si el arma w está contenida en el árbol del hombre puerta
    //PRE: DoorMan && w bien creados e inicializados
    //POST: Busca en el arbol el arma w y devuelve si la encuentra o no, el algoritmo se basa en el "pertenece" de la
    //estructura arbol proporcionada por los profesores
    public boolean searchWeapon(Weapon w) {
        boolean found = false;
        if (doorManTree.pertenece(w)) {
            found = true;
        }
        return found;
    }

    //PRE: DoorMan bien creado e inicializado
    //POST: Este metodo es llamado si el personaje que interactua con el picaporte es un villano, coge el arma de mayor poder
    //del hombre y lucha con el villano posteriormente
    public Weapon searchVillain() {
        //este método invoca al recursivo que busca el arma con más daño del increiblemente espectacular hombre picatoste
        Weapon aux = new Weapon();
        Arbol<Weapon> taux = doorManTree;
        taux = doorManTree;
        if (!doorManTree.vacio()) {
            aux = searchByDamage(taux);
        }
        return aux;
    }

    //Este método busca el arma con mayor daño dentro del arbol de armas del hombre puerta
    //PRE: DoorMan bien creado e inicializado
    //POST: DEvuelve el arma con mayor daño del arbol ya que esta ordenado por nombre, basado en un algoritmo de la estructura
    //arbol proporcionada por los profesores
    private Weapon searchByDamage(Arbol<Weapon> ArbolAux) {
        Arbol<Weapon> aux=null;
        Weapon found = new Weapon();
        Weapon found2 = new Weapon();
        if (!ArbolAux.vacio()) {
            found = ArbolAux.getRaiz();
            if ((aux=ArbolAux.getHijoIzq())!=null) {
                found2 = searchByDamage(aux);
                if(found2.getDamage()>found.getDamage())
                    found = found2;
            }
            if ((aux=ArbolAux.getHijoDer())!=null){
                found2 = searchByDamage(aux);
                if(found2.getDamage()>found.getDamage())
                    found = found2;
            }
        }
        return found;
    }

    //Método que borra el arma w de la estructura del hombre puerta
    //PRE: DoorMan && Weapon bien creados e inicializados
    //POST: Borra el arma w pasada por parametros del arbol del hombre puerta
    public void deleteWeapon(Weapon w) {
        doorManTree.borrar(w);
    }

    //PRE: DoorMan && Weapon bien creadas e inicializadas
    //POST: Método auxiliar para las acciones de los personajes con el hombre puerta, en este caso sirve para los héroes
    //y busca el arma que tiene el mismo nombre que w (pasada por parametro)
    public Weapon searchHeroe(Weapon w) {
        /*este método invoca al recursivo que busca el arma del mismo nombre que el arma pasada por parámetros,
         dentro de la estructura del increíble señor picaportes
         */
        Weapon aux = new Weapon();
        Arbol<Weapon> taux = null;
        if (!doorManTree.vacio()) {
            taux = doorManTree;
            aux = searchByName(w, taux);
        }
        return aux;

    }

    //PRE: DoorMan, Weapon && abb bien creados e inicializados
    //POST: Busca el arma w por nombre en el arbol abb y la devuelve si la encuentra
    private Weapon searchByName(Weapon w, Arbol<Weapon> abb) {
        Arbol<Weapon> aux=null;
        Weapon found= new Weapon();
        if (!abb.vacio()) {
            if (abb.getRaiz().equals(w))
                found = abb.getRaiz();
            else {
                if (w.compareTo(abb.getRaiz())<0)	//dato < datoRaiz
                    aux= abb.getHijoIzq();
                else									//dato > datoRaiz
                    aux = abb.getHijoDer();
                if (aux!=null)
                    found = searchByName(w, aux);
            }
        }
        return found;
    }

    //devuelve el arma con mayor daño del hombre verja
    //PRE: DoorMan bien creado e inicializado
    //POST: Devuelve el arma con mayor daño del hombre
    public Weapon getBestWeapon(Arbol<Weapon> auxArbol) {
        Arbol<Weapon> aux = null;
        Weapon mayor = (Weapon) doorManTree.getRaiz();
        Weapon actual;
        if (!doorManTree.vacio()) {
            if ((aux = doorManTree.getHijoIzq()) != null) {
                mayor = getBestWeapon(aux);
                actual = (Weapon) aux.getRaiz();
                if (actual.getDamage() > mayor.getDamage())
                    mayor = actual;
            }

            if ((aux = doorManTree.getHijoDer()) != null) {
                mayor = getBestWeapon(aux);
                actual = (Weapon) aux.getRaiz();
                if (actual.getDamage() > mayor.getDamage())
                    mayor = actual;
            }
        }
        return mayor;
    }

    //PRE: DoorMan bien creado e inicializado
    //POST: Comprueba si el arbol del hombre picaporte ha llegado a la profundidad marcada por el mapa de juego para
    //poder abrirse
    public boolean checkDoor(){
        Map gameMap = Map.getInstance();
        return doorManTree.profundidadArbol() < gameMap.getDeep();
    }

    //PRE: DoorMan bien creado e inicializado
    //POST: Pone la cerradura en abierto
    public void setDoorOpen(){isOpen = true;}


    //PRE: DoorMan bien creado e inicializado
    //POSST:Muestra la informacion del picaporte
    public void showDoorManinfo() throws IOException {
        BufferedWriter bufferout;
        bufferout = new BufferedWriter(new FileWriter("registro.log",true));
        if (isOpen){
            bufferout.write("(PUERTA:abierta:"+lockHeight+":");
        }

        else
            bufferout.write("(PUERTA:cerrada:"+lockHeight+":");

        bufferout.close();
        show_Weapons(doorManTree);
        bufferout = new BufferedWriter(new FileWriter("registro.log",true));
        bufferout.write(":");
        bufferout.close();
        bufferout = new BufferedWriter(new FileWriter("registro.log",true));
        bufferout.write(")");
        bufferout.newLine();

        bufferout.close();

    }

    //PRE: abb bien creado e incializado
    //POST:Modulo que escribe las armas del arbol del hombre picatoste en el fichero de salida
    public void show_Weapons(Arbol<Weapon> abb) throws IOException {
        BufferedWriter bufferout;
        bufferout = new BufferedWriter(new FileWriter("registro.log", true));
        Weapon auxW;
        Arbol<Weapon> aux = null;

        //Comprobamos si el arbol esta vacio
        if (!abb.vacio()) {
            if ((aux = abb.getHijoIzq()) != null) {
                show_Weapons(aux);
            }

            auxW = abb.getRaiz();
            bufferout.write("(" + auxW.getName() + "," + auxW.getDamage() + ")");
            if ((aux = abb.getHijoDer()) != null) {
                bufferout.close();
                show_Weapons(aux);
            }
            bufferout.close();
        }
    }

}
