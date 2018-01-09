package Characters;

import Structures.Cola;
import Structures.Grafo;
import Utilities.DoorMan;
import Utilities.Weapon;
import mainPackage.Map;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by n00b_user on 24/10/2017.
 */
public class Villain extends Character {

    private int direction;

    //constructor parametrizado de villano, entran el nombre, el turno y la sala inicial donde empieza su simulacion
    //PRE:
    //POST: Crea un malo malisimo para que juegue a disgusto con sus colegas los heroes, le hacen un poco de bullying
    public Villain(String name, String id, int turn, int initialSquare) {
        this.name = name;
        this.id = id;
        this.characterTurn = turn;
        this.startRoom = initialSquare;
        this.roomI = initialSquare;
        this.winner = false;
        this.type = "Villain";
        this.direction = 0;
    }

    //PRE: Villain && DoorMan bien creados e inicializados
    //POST: Realiza la accion del villano con el hombre acceso, en este tipo de personaje no se elimina el arma usada ya
    //que solo pueden llevar una
    public void doorManAction(DoorMan d) {
        if (!characterWeapons.isEmpty()) {
            Weapon aux;
            aux = d.searchVillain();
            if (characterWeapons.getFirst().getDamage() >= aux.getDamage()) {
                d.deleteWeapon(aux);
                if(d.checkDoor())
                    d.setDoorOpen();
            }
        }
    }

    //Metodo que escribe en el fichero de salida la informacion del Characters.Character Characters.Villain
    //PRE: Villain bien creado e inicializado
    //POST: Muestra en el fichero de salida a nuestro querido y amado villano
    public void show_info() throws IOException {
        BufferedWriter bufferout;
        bufferout = new BufferedWriter(new FileWriter("registro.log", true));

        bufferout.write("(" + type + ":" + id + ":" + roomI + ":" + characterTurn + ":");

        if (!characterWeapons.isEmpty()) {
            Weapon w = characterWeapons.getFirst();
            bufferout.write("(" + w.getName() + "," + w.getDamage() + ")");
        }
        bufferout.write(")");
        bufferout.newLine();

        bufferout.close();
    }

    public boolean characterAction(Character v) {
        boolean villainWins = false;
                Weapon auxW = new Weapon();
        if (v.getType() != "Villain") {
            if (!characterWeapons.isEmpty()) {
                for (int j = 0; j < v.characterWeapons.size(); j++) {
                    if (v.characterWeapons.get(j).getName() == characterWeapons.getFirst().getName()) {
                        if (v.characterWeapons.get(j).getDamage() < characterWeapons.getFirst().getDamage()) {
                            auxW = v.characterWeapons.get(j);
                            v.characterWeapons.remove(auxW);
                            villainWins = true;
                        }
                    }
                }

            }
        }
        return villainWins;
    }

    //PRE: Villain bien creado e inicializado
    //POST: Calcula el movimiento del villano desde el origen hasta el destino, en este caso se sigue el algoritmo de la
    //mano izquierda
    public void calculateMovement(){
        Map map = Map.getInstance();
        routesPj(map.getNorthEastCorner(),map.getDimMap()-1,map.getMapGraph(),map.getDimX());

    }

    //PRE: origen >= 0 && destino <= nº casillas del tablero
    //POST: Este metodo calcula la ruta del personaje siguiendo el algoritmo de la mano izquierda
    public void routesPj(int or, int dest, Grafo mapGraph, int dimX){
        LinkedList<Integer> visitados = new LinkedList<>();
        //Este vector contiene los movimientos que se hacen para cada direccion siendo 0:sur, 1:este, 2:norte, 3:oeste
        int[] posDirections = new int[4];
        posDirections[0] = dimX;
        posDirections[1] = 1;
        posDirections[2] = -dimX;
        posDirections[3] = -1;
        //Mientras no se haya llegado al destino se ejecuta
        while(or != dest){
            int nextDirection = nextDir(posDirections,direction);
            //Primero intenta girar a la izquierda
            if(mapGraph.adyacente(or, or + posDirections[nextDirection])){
                direction=nextDirection;
                visitados.addLast(or);
                or = or + posDirections[direction];
            }
            //Si habia una pared, se mueve hacia adelante
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

    //PRE: Villain, wList, c bien creadas e inicializadas
    //POST: Realiza la accion del villano en la sala, en este caso si el arma de la sala es mejor que la suya, coge dicho arma y
    //suelta la que lleva en la habitacion
    public void roomAction(LinkedList<Weapon> wlist, Cola<Character> c) {
        if(!wlist.isEmpty()){
            Weapon w = wlist.getFirst();
            Cola<Character> auxC = new Cola<>();
            Character pj;
            Weapon auxW;
            boolean enc1 = false;
            if (!characterWeapons.isEmpty()) {
                if (w.getDamage() > characterWeapons.getFirst().getDamage()) {
                    auxW = characterWeapons.getFirst();
                    characterWeapons.removeFirst();
                    characterWeapons.addFirst(w);
                    wlist.add(auxW);
                    wlist.remove(w);
                }
            }
            else{
                characterWeapons.addLast(w);
            	wlist.removeFirst();
            }
            while (!c.isEmpty()) {
                pj = c.getFirst();
                auxC.addData(pj);
                c.removeData();
                if (!enc1 && pj.getType().equals("Villain")) {
                    for (int j = 0; j < characterWeapons.size()-1; j++) {
                        if (characterWeapons.getFirst().getName().equals(pj.characterWeapons.get(j).getName())){
                            if (characterWeapons.getFirst().getDamage() > pj.characterWeapons.get(j).getDamage()) {
                                auxW = pj.characterWeapons.get(j);
                                pj.characterWeapons.remove(auxW);
                                enc1 = true;
                            }
                        }
                    }
                }
            }
            while (!auxC.isEmpty()) {
                pj = auxC.getFirst();
                c.addData(pj);
                auxC.removeData();
            }

        }
    }


}
