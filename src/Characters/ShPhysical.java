package Characters;

import Structures.Cola;
import Structures.Grafo;
import Utilities.DoorMan;
import Utilities.Weapon;
import mainPackage.Map;

import javax.print.attribute.IntegerSyntax;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by n00b_user on 07/11/2017.
 */
public class ShPhysical extends Character{

    //Este tipo de personajes tienen una estructura auxiliar para almacenar los villanos a los que capturan
    private Cola<Character> capturedVillains = new Cola<Character>();

    //Constructor parametrizado del superheroe con poderes fisicos
    //PRE:
    //POST: Crea un personaje fisico para que se pegue con el resto de personajes de la simulacion, no es muy social
    public ShPhysical(String name, String id, int turn, int initialSquare){
        this.name = name;
        this.id = id;
        this.characterTurn = turn;
        this.startRoom = initialSquare;
        this.roomI = initialSquare;
        this.winner = false;
        this.type = "ShPhysical";
    }

    //Accion realizada por el superheroe si se encuentra en la sala del daily planet para interactuar con el hombre del porton
    //PRE: ShPhysical && DoorMan bien creados e inicializados
    //POST: Realiza la accion del personaje con la estructura que cierra cosas
    public void doorManAction(DoorMan d) {
        if (!characterWeapons.isEmpty()) {
            Weapon aux = characterWeapons.getFirst();
            if (d.searchWeapon(aux)) {
                //he considerado que el resto está bien y  solo he tocado acá abajo, así que si peta ya sabes donde y por culpa de quien
                Weapon aux2 = d.searchHeroe(aux);
                if (aux.getDamage() > aux2.getDamage()) {
                    d.deleteWeapon(aux2);
                    if(d.checkDoor())
                        d.setDoorOpen();
                }
            }
            characterWeapons.removeFirst();
        }
    }

    //Accion que realiza el personaje en la sala que se encuentra
    //PRE: ShPhysical, wlist, c bien creadas e inicializadas
    //POST: Realiza la accion correspondiente a los heroes en la sala, capturando al villano si las condiciones son favorables
    public void roomAction(LinkedList<Weapon> wlist, Cola<Character> c) {
        Weapon wAux;
        if(!wlist.isEmpty()){
            wAux=wlist.getFirst();
            addWeapon(wAux);
            wlist.removeFirst();
        }
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
        }}

    //PRE: ShPhysical bien creado e inicializado
    //POST: Calcula el movimiento del personaje
    public void calculateMovement(){
        Map map = Map.getInstance();
        LinkedList<Integer> visited = new LinkedList<Integer>();
        LinkedList<LinkedList<Integer>> paths = new LinkedList<LinkedList<Integer>>();
        bestRoutePj(0,map.getDailyPlanet(),visited,map.getMapGraph(),paths);
        transformRoute(paths.getFirst(),map.getDimX());
    }

    //PRE: ShPhysical bien creado e inicializado, or >= 0, dest < dimMap
    //POST: Calcula la mejor ruta para este tipo de personaje
    public void bestRoutePj(int or, int dest, LinkedList<Integer> visited, Grafo graph,LinkedList<LinkedList<Integer>> paths){
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
            ArrayList <Integer> neightbors = new ArrayList<Integer>();
            graph.adyacentes(or,neightbors);
            visited.addLast(or);
            int aux;
            for(int i=0;i < neightbors.size()-1;i++)
                for(int j=0;j < neightbors.size()-i-1;j++)
                    if(neightbors.get(j+1) < neightbors.get(j)){
                        aux = neightbors.get(j+1);
                        neightbors.set(j+1,neightbors.get(j));
                        neightbors.set(j,aux);
                    }
            for(int i = 0; i < neightbors.size();i++){
                if(!visited.contains(neightbors.get(i)))
                    bestRoutePj(neightbors.get(i),dest,visited,graph,paths);
            }
        }
        visited.removeLast();
    }

    //Metodo que escribe en el fichero de salida la informacion del ShPhysical
    //PRE: ShPhysical bien creado e inicializado
    //POST: Escribe en el fichero de salida la informacion del superheroe
    public void show_info() throws IOException {
        BufferedWriter bufferout;
        bufferout = new BufferedWriter(new FileWriter("registro.log",true));
        Cola<Weapon> aux = new Cola<Weapon>();
        Cola<Character> aux2 = new Cola<>();

        bufferout.write("("+type+":"+id+":"+roomI+":"+characterTurn+":");

        while(!characterWeapons.isEmpty()){
            Weapon w = characterWeapons.getFirst();
            bufferout.write("("+w.getName()+","+w.getDamage()+")");
            aux.addData(w);
            characterWeapons.removeFirst();
        }
        while(!aux.isEmpty()){
            Weapon w = aux.getFirst();
            characterWeapons.add(w);
            aux.removeData();
        }
        bufferout.write(":");
        while(!capturedVillains.isEmpty()){
            Character pj =capturedVillains.getFirst();
            bufferout.write(pj.name+" ");
            aux2.addData(pj);
            capturedVillains.removeData();
        }
        bufferout.write(")");
        bufferout.newLine();
        while(!aux2.isEmpty()){
            Character pj = aux2.getFirst();
            capturedVillains.addData(pj);
            aux2.removeData();
        }
        bufferout.close();
    }
}
