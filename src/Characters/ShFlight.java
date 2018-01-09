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
 * Created by n00b_user on 07/11/2017.
 */
public class ShFlight extends Character {

    //Este tipo de personajes tienen una estructura auxiliar para almacenar los villanos a los que capturan
    private Cola<Character> capturedVillains = new Cola<Character>();

    //PRE:
    //POST: Crea un personaje flight para que sobrevuele el mapa con belleza y disimulo con sus compañeros terrestres
    public ShFlight(String name, String id, int turn, int initialSquare){
        this.name = name;
        this.id = id;
        this.characterTurn = turn;
        this.startRoom = initialSquare;
        this.roomI = initialSquare;
        this.winner = false;
        this.type = "ShFlight";
    }

    //PRE: ShFlight && DoorMan bien creados e inicializados
    //POST: Realiza la accion de este tipo de personaje con el hombre cerradura
    public void doorManAction(DoorMan d) {
        if (!characterWeapons.isEmpty()) {
            Weapon aux = characterWeapons.getFirst();
            if (d.searchWeapon(aux) == true) {
                //he considerado que el resto esta bien y  solo he tocado aca abajo, asi que si peta ya sabes donde y por culpa de quien
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

    //PRE: ShFlight, wList, c bien creadas e inicializadas
    //POST: Hace la accion correspondiente al personaje en la sala que se encuentra, al igual que el resto de heroes
    // coge el arma de la sala y comprueba si existen villanos para comerselos muy fuerte
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

    //Metodo que escribe en el fichero de salida la informacion del Characters.Character Characters.ShFlight
    //PRE: ShFlight bien creado e inicializado
    //POST: Muestra la informacion del personaje volador en el fichero de salida
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

    //PRE: ShFlight bien creado e inicializado
    //POST: Calcula el movimiento correspondiente al personaje, en este caso usa el camino menor entre la sala de inicio
    //y el daily planet
    public void calculateMovement(){
        Map map = Map.getInstance();
        LinkedList<Integer> aux = new LinkedList<Integer>();
        LinkedList<LinkedList<Integer>> paths = new LinkedList<LinkedList<Integer>>();
        bestRoute(startRoom,map.getDailyPlanet(),aux,map.getMapGraph(),paths,map.getDimX());
    }

    //PRE: ShFlight bien creado e inicializado, or >= 0, dest < dimMap
    //POST: Calcula todas las rutas posibles entre el origen y el destino, posteriormente elige la que menos movimientos
    //requiere
    public void bestRoute(int or, int dest, LinkedList<Integer> visited, Grafo mapGraph, LinkedList<LinkedList<Integer>> paths, int dimX){
        allRoutesPj(or,dest,visited, mapGraph, paths);
        int n = 0;
        int count;
        int cuentaMenor = 9999;
        for(int i=0; i<paths.size();i++){
            count = 0;
            for(int j=0; j<paths.get(i).size(); j++){
                count++;
            }
            if(count<cuentaMenor){
                cuentaMenor = count;
                n=i;
            }
        }
        transformRoute(paths.get(n),dimX);
    }

}
