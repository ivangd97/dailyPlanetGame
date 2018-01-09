package Utilities; /**
 * Created by n00b_user on 19/10/2017.
 */

import Characters.Character;
import Structures.Cola;
import mainPackage.Map;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Room {

    private int mark;

    //identificador de la sala, para poder numerarlas
    private int roomID;

    //lista de armas que contiene la sala
    private LinkedList<Weapon> weaponList;

    //cola de personajes que contiene la sala
    private Cola<Character> charactersInRoom;

    //Hombre puerta de la sala (si lo tiene)
    private DoorMan isDoorMan;

    //constructor por defecto de la sala
    //PRE:
    //POST: Crea e inicializa una misteriosa y potencialmente fantastica sala para jugar
    public Room() {
        roomID = 0;
        weaponList = new LinkedList<Weapon>();
        charactersInRoom = new Cola<Character>();
        this.mark = 0;
    }

    //constructor parametrizado de la sala, que le aporta un identificador
    //PRE:
    //POST: Crea e inicializa una maravillosa sala con el identificador pasado por parametro
    public Room(int ID) {
        this.roomID = ID;
        weaponList = new LinkedList<Weapon>();
        charactersInRoom = new Cola<Character>();
        this.mark = ID;
    }

    //metodo que añade un arma a la lista de armas de la sala
    //PRE: Room && Weapon bien creadas e inicializadas
    //POST: Añade el arma w a la estructura de armas de la sala
    public void addWeapon(Weapon w) {
        if (weaponList.isEmpty()) {
            weaponList.add(w);
        } else {
            weaponList.addLast(w);
        }
        orderWeapons();
    }

    //PRE: Room bien creada e inicializada
    //POST: Ordena las armas de la sala a traves del algoritmo de la burbuja
    private void orderWeapons() {
        int i, j;
        Weapon aux;
        for (i = 0; i < weaponList.size() - 1; i++)
            for (j = 0; j < weaponList.size() - i - 1; j++)
                if (weaponList.get(j + 1).getDamage() > weaponList.get(j).getDamage()) {
                    aux = weaponList.get(j + 1);
                    weaponList.remove(j + 1);
                    weaponList.add(j, aux);
                }
    }

    //modulo que inserta un personaje en la cola de la sala
    //PRE: Room && Character bien creados e inicializados
    //POST: Añade un personaje a la cola de la sala para ser procesados posteriormente
    public void addCharacter(Character c) {
        charactersInRoom.addData(c);
    }

    //PRE: Room && DoorMan bien creados e inicializados
    //POST: Añade el DoorMan a la sala si es necesario
    public void addDoorMan(DoorMan d) {
        this.isDoorMan = d;
    }

    //PRE: Room bien creada e inicializada
    //POST: Devuelve el hombre cerrojo de la sala si lo tiene
    public DoorMan getIsDoorMan() {
        return isDoorMan;
    }

    //modulo que devuelve el identificador de la sala
    //PRE: Room bien creada e inicializada
    //POST: Devuelve el identificador de la sala
    public int getRoomID() {
        return roomID;
    }

    //PRE: Room bien creada e inicializada
    //POST: Simula la sala procesando cada uno de los personajes que contiene en su estructura
    public void simulateRoom(Room[] roomVector, int turn, boolean haveDoorMan, int dimX, int dimY) {
        Character pj;
        boolean openDoor = false;
        Cola<Character> aux = new Cola<Character>();
        while (!charactersInRoom.isEmpty() && !openDoor) {
            pj = charactersInRoom.getFirst();
            //Si el turno del personaje escogido es igual al turno de juego se procesa, asi evitamos que un personaje
            //se mueva varias veces en un mismo turno
            if (pj.getCharacterTurn() == turn) {
                //Comprobacion de si la sala tiene hombre de la cerradura
                if (haveDoorMan) {
                    if (!isDoorMan.doorOpen()) {
                        pj.roomAction(this.weaponList, this.charactersInRoom);
                        pj.doorManAction(isDoorMan);
                        openDoor = isDoorMan.doorOpen();
                        //Si la puerta esta abierta comprobamos si el personaje gana (turno de pj == turn)
                        if (openDoor) {
                            Character auxch;
                            while(!charactersInRoom.isEmpty()){
                                auxch = charactersInRoom.getFirst();
                                charactersInRoom.removeData();
                                Map gameMap = Map.getInstance();
                                if(auxch.getCharacterTurn() == gameMap.getActualTurn())
                                    auxch.setWinner();
                                aux.addData(auxch);
                            }
                        }
                    } else {
                        pj.setWinner();
                    }
                }
                //si no tiene hombre puerta se realiza la accion normal de sala
                else{
                    pj.movement(dimX, dimX * dimY);
                    if(!pj.getArrived()){
                        if (this.getRoomID() == pj.getRoomI()) {
                            pj.setArrived(true);
                            aux.addData(pj);
                            pj.roomAction(this.weaponList, this.charactersInRoom);
                            orderWeapons();
                        } else {
                            pj.setArrived(false);
                            roomVector[pj.getRoomI()].addCharacter(pj);
                            pj.roomAction(roomVector[pj.getRoomI()].getWeaponList(), roomVector[pj.getRoomI()].getCharactersInRoom());
                            orderWeapons();
                        }
                        charactersInRoom.removeData();
                    }
                }
                //incrementamos el turno del personaje
            pj.increaseTurn();
            } else {
                charactersInRoom.removeData();
                aux.addData(pj);
            }
        }
        while (!aux.isEmpty()) {
            pj = aux.getFirst();
            charactersInRoom.addData(pj);
            aux.removeData();
        }
    }

    //Muestra la informacion de la sala
    //PRE: Room bien creada e inicializada
    //POST: Muestra en nuestro fabuloso archivo de salida la informacion de la sala
    public void showRoomInfo() throws IOException {
        Weapon w;
        BufferedWriter bufferout;
        bufferout = new BufferedWriter(new FileWriter("registro.log", true));
        LinkedList<Weapon> auxKeyList = new LinkedList<Weapon>();
        //Mostramos el ID de la sala y de sus armas
        bufferout.write("(square:" + roomID + ":");
        while (!weaponList.isEmpty()) {
            w = weaponList.remove();
            auxKeyList.add(w);
            bufferout.write("(" + w.getName() + "," + w.getDamage() + ")");
        }
        bufferout.write(")");
        bufferout.newLine();
        bufferout.close();
        while (!auxKeyList.isEmpty()) {
            w = auxKeyList.remove();
            weaponList.add(w);
        }
    }

    //Muestra los personajes que contiene en su cola
    //PRE: Room bien creada e inicializada
    //POST: Llama a los modulos de mostrar del personaje para cada uno de los que contiene la sala
    public void showCharacters() throws IOException {
        Character pj;
        Cola<Character> aux = new Cola<>();

        //Mostramos el ID de la sala y de cada una de sus armas

        while (!charactersInRoom.isEmpty()) {
            pj = charactersInRoom.getFirst();
            pj.show_info();
            aux.addData(pj);
            charactersInRoom.removeData();
        }
        while (!aux.isEmpty()) {
            pj = aux.getFirst();
            charactersInRoom.addData(pj);
            aux.removeData();
        }

    }

    //PRE: Room bien creada e inicializada
    //POST: Devuelve la cola de personajes de la sala
    public Cola<Character> getCharactersInRoom() {
        return charactersInRoom;
    }

    //PRE: Room bien creada e inicializada
    //POST: Devuelve la lista de armas de la sala
    public LinkedList<Weapon> getWeaponList() {
        return weaponList;
    }

    //Cuenta el numero de personajes en la sala
    //PRE: Room bien creada e inicializada
    //POST: Cuenta los personajes que se encuentran en la sala
    public int numCharacters() {
        int personajes = 0;
        Character pj;
        Cola<Character> aux = new Cola<>();
        while (!charactersInRoom.isEmpty()) {
            pj = charactersInRoom.getFirst();
            aux.addData(pj);
            charactersInRoom.removeData();
            personajes++;
        }
        while (!aux.isEmpty()) {
            pj = aux.getFirst();
            charactersInRoom.addData(pj);
            aux.removeData();
        }
        return personajes;
    }

    //PRE: Room bien creada e inicializada
    //POST: Devuelve la marca de la sala para crear caminos y atajos
    public int getMark() {
        return mark;
    }

    //PRE: Room bien creada e inicializada
    //POST: Pone a mark el valor de la marca de la sala
    public void setMark(int mark) {
        this.mark = mark;
    }

    //PRE: Room bien creada e inicializada
    //POST: True si la sala esta vacia, false en caso de tener algun personaje dentro
    public boolean isEmpty() {
        return charactersInRoom.isEmpty();
    }

    //PRE: Room bien creada e inicializada
    //POST: True si la sala tiene armas, false si no
    public boolean emptyOfWeapons() {
        return weaponList.isEmpty();
    }

    //PRE: Room bien creada e inicializada
    //POST: Devuelve el primer personaje de la cola de la sala
    public Character getCharacter() {
        return charactersInRoom.getFirst();
    }

    //PRE: Room bien creada e inicializada
    //POST: Elimina el personaje de la sala
    public void removeCharacter() {
        charactersInRoom.removeData();
    }
}
