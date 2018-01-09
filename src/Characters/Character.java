package Characters;
/*Nombre del grupo: La Quinta de Pepe el Sanguinario
 *Nombre y apellidos de cada componente: Ã�ngel Hortet GarcÃ­a e IvÃ¡n GonzÃ¡lez DomÃ­nguez
 *NÃºmero de entrega (EC1, EC2, febrero, junio, etc.): EC1
 *Curso: 2Âº A
 */

import Structures.Cola;
import java.lang.*;

import Structures.Grafo;
import Utilities.DoorMan;
import Utilities.Weapon;
import mainPackage.*;

import java.io.IOException;
import java.util.*;

import static mainPackage.Dir.*;

/**
 * Created by n00b_user on 19/10/2017.
 */
public abstract class Character implements Comparable<Character>{

    //Nombre del Personaje
    protected String name = null;

    //Identificador del personaje
    protected String id;

    //identificador de la sala donde comienza su juego el personaje
    protected int startRoom;

    //Esta variable nos indica si el personaje ha ganado para poder pasar a la sala del teseracto
    protected boolean winner;

    //Con este booleano vemos más adelante si el personaje ha llegado ya a la sala del daily planet para que se comporte
    //como debe sin hacer interacciones erroneas o de mas
    protected boolean arrived = false;

    //Guarda la lista de armas del personaje
    protected LinkedList<Weapon> characterWeapons = new LinkedList<Weapon>();

    //Estructura de datos que almacena las direcciones del personaje
    protected ArrayList<Dir> directions = new ArrayList<Dir>();

    //Turno en el que se empieza a mover el personaje
    protected int initialTurn;

    //Turno del personaje, se usa por si en algún momento el personaje no se mueve de sala por una imposibilidad de movimiento
    //que no interactúe dos veces en el mismo turno
    protected int characterTurn = 0;

    //Sala inicial desde la que sale el personaje
    protected int roomI = 0;

    //Tipo del personaje
    protected String type;

    //Contador de movimientos del personaje
    protected int movement = 0;

    //constructor por defecto del personaje
    //PRE:
    //POST: Construye e inicializa un personaje por defecto
    public Character(){
        this.name = "Default";
        this.id = "D";
        this.startRoom = 0;
        this.winner = false;
        this.type = "Desconocido";
    }

    //constructor parametrizado del personaje, le llegan un nombre y una letra que sirve de identificador
    //PRE:
    //POST:Construye e inicializa un personaje con los datos que recoge
    public Character(String name, String id, int turn, int initialRoom){
        this.name = name;
        this.id = id;
        this.winner = false;
        this.initialTurn = turn;
        this.roomI = initialRoom;
        this.type = "Desconocido";
    }

    //PRE: Personaje bien creado e inicializado
    //POST: Devuelve si el personaje ha llegado a la sala del daily planet
    public boolean getArrived( ){return arrived;}

    //PRE: Personaje bien creado e inicializado
    //POST: Pone el booleano arrived con el valor de entrada
    public void setArrived (boolean aux){ this.arrived = aux;}

    //metodo que devuelve el identificador del personaje
    //PRE: Personaje bien creado e inicializado
    //POST: Devuelve el id del personaje
    public String getId(){
        return id;
    }

    //metodo que devuelve el tipo del personaje
    //PRE: Personaje bien creado e inicializado
    //POST: Devuelve el tipo del personaje
    public String getType(){
        return type;
    }

    //Devuelve la sala en la que inicia la simulacion el personaje
    //PRE: Personaje bien creado e inicializado
    //POST: Devuelve la sala de inicio del personaje
    public int getStartRoom(){
        return startRoom;
    }

    //Si el personaje gana la simulacion, este metodo pone el booleano de ganador a true
    //PRE: Personaje bien creado e inicializado
    //POST: Pone el booleano winner a true, se usa solo cuando el personaje puede ganar el juego
    public void setWinner(){
        winner = true;
    }

    //PRE: Personaje bien creado e inicializado
    //POST: Devuelve el turno inicial del personaje
    public int getInitialTurn() {
        return initialTurn;
    }

    //PRE: Personaje bien creado e inicializado && Weapon bien creada e inicializada
    //POST: Inserta el arma w en la estructura de armas del personaje
    public void addWeapon(Weapon w) {
        boolean insert = false;
        //Si la estructura está vacia la inserta sin mas
        if (characterWeapons.isEmpty()) {
            characterWeapons.add(w);
        }
        //En caso contrario comprueba si el personaje la tiene, si la tiene suma los daños, si no la inserta
        else {
            for(int i=0; i<characterWeapons.size();i++){
                if(characterWeapons.get(i).getName().equals(w.getName())){
                    characterWeapons.get(i).setDamage(characterWeapons.get(i).getDamage()+w.getDamage());
                    insert=true;
                }
            }
            if(!insert){
            characterWeapons.addLast(w);
        }}
        orderWeapons();
    }

    //PRE: Personaje bien creado e inicializado && characterWeapons bien inicializada
    //POST: Ordena las armas en la estructura del personaje a traves del algoritmo de la burbuja
    private void orderWeapons() {
        int i, j;
        Weapon aux;
        for (i = 0; i < characterWeapons.size() - 1; i++)
            for (j = 0; j < characterWeapons.size() - i - 1; j++)
                if (characterWeapons.get(j + 1).getDamage() > characterWeapons.get(j).getDamage()) {
                    aux = characterWeapons.get(j + 1);
                    characterWeapons.remove(j + 1);
                    characterWeapons.add(j, aux);
                }
                else if(characterWeapons.get(j + 1).getDamage() == characterWeapons.get(j).getDamage()){
                    if(characterWeapons.get(j+1).compareTo(characterWeapons.get(j))< 0){
                        aux = characterWeapons.get(j + 1);
                        characterWeapons.remove(j + 1);
                        characterWeapons.add(j, aux);
                    }

                }
    }


    //Accion que realiza cada personaje cuando se encuentra con el increible hombre picaporte
    //PRE: Personaje bien creado e inicializado
    //POST: Realiza la accion con el hombre picaporte
    public abstract void doorManAction(DoorMan d);

    //Este metodo sera redefinido en cada personaje y es la accion que se realiza en cada sala por el personaje
    //PRE: Personaje, weaponList, c bien creados e inicializados
    //POST: Realiza la accion de la sala
    public abstract void roomAction(LinkedList<Weapon> weaponList,Cola<Character> c);

    //Este metodo añade los movimientos a la lista de direcciones del personaje
    //PRE: Personaje && Dirs bien creados e inicializados
    //POST: Añade los movimientos al personaje
    public void addMovements (ArrayList<Dir> Dirs){
        while(!Dirs.isEmpty()){
            Dir aux;
            aux = Dirs.get(0);
            directions.add(aux);
            Dirs.remove(0);
        }
    }

    //PRE: Personaje bien creado e inicializado
    //POST: Calcula el movimiento que va a realizar el personaje durante la simulacion, cada personaje tendrá el suyo propio
    public abstract void calculateMovement();

    //Transforma la ruta en direcciones para ser insertada
    //PRE: Personaje && visited bien creadas e inicializadas
    //POST: Transforma una lista de integer la cual corresponde a los identificadores de las salas en direcciones comprobando
    //mediante operaciones sencillas si el movimiento se ha hecho hacia N,S,E,W
    public void transformRoute(LinkedList<Integer> visited, int dimX){
        Integer aux,aux2;
        Dir direction;
        ArrayList<Dir> Dirs= new ArrayList<Dir>();
        for(int i = 0; i+1<visited.size();i++){
            aux = visited.get(i);
            aux2 = visited.get(i+1);
            if(aux2-aux == 1){
                direction = E;
                Dirs.add(direction);
            }
            else{
                if(aux-aux2 == 1){
                    direction = W;
                    Dirs.add(direction);
                }
                else{
                    if(aux-aux2 == dimX){
                        direction = N;
                        Dirs.add(direction);
                    }
                    else{
                        direction = S;
                        Dirs.add(direction);
                    }
                }
            }
        }
        addMovements(Dirs);
    }

    //PRE: Personaje bien creado e inicializado
    //POST: Calcula todas las posibles rutas de un personaje desde la sala inicial hasta la final
    public void allRoutesPj(int or, int dest, LinkedList<Integer> visited, Grafo graph, LinkedList<LinkedList<Integer>> paths){
        if(or == dest){
            visited.addLast(or);
            LinkedList<Integer> visitedCopy = new LinkedList<Integer>();
            //Visited va a ser modificado por lo que hay que copiar cada elemento en la variable, si copiasemos dicha variable
            //de golpe se pasaria como referencia por lo que cualquier modificacion posterior le afectaria
            for(int i = 0; i < visited.size(); i++){
                int a = visited.get(i);
                visitedCopy.add(a);
            }
            paths.add(visitedCopy);
        }
        else{
            ArrayList<Integer> neightbors = new ArrayList<Integer>();
            graph.adyacentes(or,neightbors);
            visited.addLast(or);
            for(int i = 0; i < neightbors.size();i++){
                if(!visited.contains(neightbors.get(i)))
                    allRoutesPj(neightbors.get(i),dest,visited,graph,paths);
            }
        }
        visited.removeLast();
    }

    //PRE: Personaje bien creado e inicializado
    //POST: Devuelve el valor del booleano winner
    public boolean isWinner(){return winner;}

    //PRE: Personaje bien creado e inicializado
    //POST: Retorna si la estructura de armas del personaje esta vacia
    public boolean emptyOfWeapons(){return characterWeapons.isEmpty();}

    //PRE: Personaje bien creado e inicializado
    //POST: Devuelve el turno actual del personaje
    public int getCharacterTurn(){return characterTurn;}

    //PRE: Personaje bien creado e inicializado
    //POST: Incrementa el turno del personaje
    public void increaseTurn(){characterTurn++;}

    //PRE: Personaje bien creado e inicializado
    //POST: Inicia la sala desde donde sale el personaje al valor de entrada
    public void setRoomI(int number){roomI = number;}

    //PRE: Personaje bien creado e inicializado
    //POST: Devuelve el valor de la sala inicial del personaje
    public int getRoomI(){return roomI;}

    //PRE: Personaje bien creado e inicializado
    //POST: Realiza el movimiento del personaje en el mapa, si este movimiento es posible en funcion de las dimensiones y
    //paredes de la estructura
    public void movement(int dimX, int dimMap){
        Dir d;
        //Si el personaje sigue teniendo direcciones en su estructura se procesa
        if(!directions.isEmpty()){
            d = directions.get(movement);
            //Comprobamos si se puede realizar dicho movimiento dependiendo de las dimensiones del mapa proporcionadas
            switch (d){
                case N:
                    if(roomI-dimX < 0)
                        System.out.println("No se puede realizar el movimiento al Norte");
                    else
                        roomI = roomI - dimX;
                    break;
                case E:
                    if(roomI + 1 % dimX == 0)
                        System.out.println("No se puede realizar el movimiento al Este");
                    else
                        roomI = roomI + 1;
                    break;
                case W:
                    if(roomI % dimX == 0)
                        System.out.println("No se puede realizar el movimiento al Oeste");
                    else
                        roomI = roomI - 1;
                    break;
                case S:
                    if(roomI + dimX > dimMap)
                        System.out.println("No se puede realizar el movimiento al Sur");
                    else
                        roomI = roomI + dimX;
                    break;
            }
            directions.remove(d);
        }
        else
            System.out.println("No hay mas movimientos en la estructura");
    }

    //Muestra la informacion del Characters.Character, definida por cada uno posteriormente
    //PRE: Personaje bien creado e inicializado
    //POST: Muestra la informacion del personaje en el archivo de salida
    public abstract void show_info() throws IOException;

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (!(obj instanceof Character))
            return false;
        Character pAux = (Character) obj;
        return (this.id.equals(pAux.getId()));

    }



    @Override
    public int compareTo(Character character2) {
        if (this == character2)
            return 0;
        return (this.id.compareTo(character2.getId()));
    }
}
