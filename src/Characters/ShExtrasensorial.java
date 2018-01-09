package Characters;

import Structures.Cola;
import Structures.Grafo;
import mainPackage.Map;
import Utilities.DoorMan;
import Utilities.Weapon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by n00b_user on 07/11/2017.
 */
public class ShExtrasensorial extends Character {

    // Nos dice la direccion en la que se esta moviendo
    private int direction;

    //Este tipo de personajes tienen una estructura auxiliar para almacenar los villanos a los que capturan
    private Cola<Character> capturedVillains = new Cola<Character>();

    //PRE:
    //POST: Crea un personaje de tipo extrasensorial para que pueda divertirse con sus amigos en el mapa
    public ShExtrasensorial(String name, String id, int turn, int initialSquare) {
        this.name = name;
        this.id = id;
        this.characterTurn = turn;
        this.startRoom = initialSquare;
        this.roomI = initialSquare;
        this.winner = false;
        this.type = "ShExtrasensorial";
        direction = 0;
    }

    //PRE: ShExtrasensorial && DoorMan bien creados e inicializados
    //POST: Realiza la accion pertinente del personaje con el hombre del cerrojo
    public void doorManAction(DoorMan d) {
        //Si la estructura de armas del personaje no esta vacia coge la primera (que al estar ordenadas por daño coincide con la mejor)
        if (!characterWeapons.isEmpty()) {
            Weapon aux = characterWeapons.getFirst();
            //Comprobamos que el Hombre d contenga el arma del personaje
            if (d.searchWeapon(aux) == true) {
                //he considerado que el resto está bien y  solo he tocado acá abajo, así que si peta ya sabes donde y por culpa de quien
                Weapon aux2 = d.searchHeroe(aux);
                //Se realiza la lucha de armas y luego se comprueba si el hombre picatoste se ha abierto
                if (aux.getDamage() > aux2.getDamage()) {
                    d.deleteWeapon(aux2);
                    if(d.checkDoor())
                        d.setDoorOpen();
                }
            }
            //Siempre se elimina el arma usada por el heroe
            characterWeapons.removeFirst();
        }
    }

    //Metodo que escribe en el fichero de salida la informacion del Characters.Character Characters.ShExtrasensorial
    //PRE: ShExtrasensorial bien creado e inicializado
    //POST: Escribe en el archivo de salida la información del heroe
    public void show_info() throws IOException{
        BufferedWriter bufferout;
        bufferout = new BufferedWriter(new FileWriter("registro.log", true));
        Cola<Weapon> aux = new Cola<Weapon>();
        Cola<Character> aux2 = new Cola<>();

        bufferout.write("(" + type + ":" + id + ":" + roomI + ":" + characterTurn + ":");

        while (!characterWeapons.isEmpty()) {
            Weapon w = characterWeapons.getFirst();
            bufferout.write("(" + w.getName() + "," + w.getDamage() + ")");
            aux.addData(w);
            characterWeapons.removeFirst();
        }
        while (!aux.isEmpty()) {
            Weapon w = aux.getFirst();
            characterWeapons.add(w);
            aux.removeData();
        }
        bufferout.write(":");
        while (!capturedVillains.isEmpty()) {
            Character pj = capturedVillains.getFirst();
            bufferout.write(pj.name + " ");
            aux2.addData(pj);
            capturedVillains.removeData();
        }
        bufferout.write(")");
        bufferout.newLine();
        while (!aux2.isEmpty()) {
            Character pj = aux2.getFirst();
            capturedVillains.addData(pj);
            aux2.removeData();
        }
        bufferout.close();
    }

    //PRE: ShExtrasensorial, wList, c bien creadas e inicializadas
    //POST: Realiza la accion del heroe en la sala que se encuentra (una vez por turno)
    public void roomAction(LinkedList<Weapon> wlist, Cola<Character> c) {
        Weapon wAux;
        //Comprobamos si wList tiene armas y cogemos la primera para quedarnosla
        if(!wlist.isEmpty()){
            wAux=wlist.getFirst();
            addWeapon(wAux);
            wlist.removeFirst();
        }
        //Al ser un heroe, contiene una estructura para atrapar villanos, en esta parte del algoritmo comprobamos si existe
        //algun villano en la sala obteniendo su tipo y lo capturamos si el personaje posee un arma de igual nombre pero mayor daño
        //que el malo malisimo
        Cola<Character> auxCola = new Cola<>();
        Character ch;
        boolean enc1=false;
        if(!c.isEmpty()){
            while (!c.isEmpty()) {
                ch = c.getFirst();
                if (ch.getType().equals("Villain") && !enc1) {
                    for (int j = 0; j < characterWeapons.size(); j++) {
                        if (characterWeapons.get(j).getName().equals(ch.characterWeapons.getFirst().getName())) {
                            if (characterWeapons.get(j).getDamage() > ch.characterWeapons.getFirst().getDamage()) {
                                capturedVillains.addData(ch);
                                enc1 = true;
                            }
                        }
                    }
                }
                if(!enc1){
                    auxCola.addData(ch);
                    c.removeData();
                }
                else
                    c.removeData();
            }
            while (!auxCola.isEmpty()) {
                ch = auxCola.getFirst();
                c.addData(ch);
                auxCola.removeData();
            }
        }
    }

    //PRE: ShExtrasensorial bien creado e inicializado
    //POST:  Calcula el movimiento del heroe extrasensorial, en esta caso se usa el algoritmo de la mano derecha desde la
    //sala inicial hasta la ultima del mapa
    public void calculateMovement(){
        Map map = Map.getInstance();
        routesPj(0,map.getDimMap()-1,map.getMapGraph(),map.getDimX());

    }

    //PRE: origen >= 0 && destino <= n?casillas del tablero
    //POST: Este metodo calcula la ruta del personaje siguiendo el algoritmo de la mano derecha
    public void routesPj(int or, int dest, Grafo mapGraph, int dimX){
        LinkedList<Integer> visitados = new LinkedList<>();
        //Este vector contiene los movimientos que se hacen para cada direccion siendo 0:sur, 1:oeste, 2:norte, 3:este
        int[] posDirections = new int[4];
        posDirections[0] = dimX;
        posDirections[1] = -1;
        posDirections[2] = -dimX;
        posDirections[3] = 1;
        //Mientras no se haya llegado al destino se ejecuta
        while(or != dest){
            int nextDirection = nextDir(posDirections,direction);
            //Primero intenta girar hacia la derecha
            if(mapGraph.adyacente(or, or + posDirections[nextDirection])){
                direction=nextDirection;
                visitados.addLast(or);
                or = or + posDirections[direction];
            }
            //Si habia una pared a la derecha, se mueve hacia adelante
            else{
                if(mapGraph.adyacente(or, or+posDirections[direction])){
                    visitados.addLast(or);
                    or = or + posDirections[direction];
                }
                //En caso de no poder moverse adelante, cambia su direccion
                else{
                    direction=previousDir(posDirections,direction);
                }
            }

        }
        visitados.addLast(dest);
        transformRoute(visitados,dimX);
    }

    //Estos modulos se utilizan como auxiliar en el modulo anterior. Devuelven la direccion siguiente y anterior del vector
    //teniendo en cuenta que la ultima posicion enlaza con la primera
    public int nextDir(int[] vector, int pos){
        int res = 0;
        if(vector[vector.length-1] != vector[pos]){
            res = pos+1;
        }
        return res;
    }
    public int previousDir(int[] vector, int pos){
        int res = vector.length-1;
        if(vector[0] != vector[pos]){
            res = pos-1;
        }
        return res;
    }
}
