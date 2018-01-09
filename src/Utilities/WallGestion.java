package Utilities;

/**
 * Created by n00b_user on 07/11/2017.
 */

import Structures.Grafo;
import mainPackage.Map;
import Generation.GenAleatorios;
import java.util.LinkedList;

public class WallGestion {

    //Esta clase genera todas las paredes del mapa en el orden propuesto de NORTE-ESTE-SUR-OESTE utilizando los
    //identificadores de la sala para crear parejas de numeros (paredes) que se almacenan en una estructura para su posterior
    //gestión
    //PRE: WallGestion bien creada e inicializada
    //POST: Genera las parejas de numeros que forman las paredes del mapa para posteriormente derribarlas
    public LinkedList<int[]> generateWalls(Map gameMap){
        Room[] roomVector;
        LinkedList<int[]> walls = new LinkedList<int []>();
        roomVector = gameMap.getRoomVector();
        Room rAux, rAux2;
        int [] pair;
        for(int i = 0; i<gameMap.getDimMap();i++){
            rAux = roomVector[i];
            //Primero comprobamos que la sala posee una pared NORTE
            if(rAux.getRoomID() - gameMap.getDimX() >= 0){
                pair = new int [2];
                rAux2 = roomVector[rAux.getRoomID() - gameMap.getDimX()];
                pair[0] = rAux.getRoomID();
                pair[1] = rAux2.getRoomID();
                walls.add(pair);
            }
            //Despues comprobamos que la sala tiene una pared ESTE
            if((rAux.getRoomID() + 1)% gameMap.getDimX() != 0){
                pair = new int[2];
                rAux2 = roomVector[rAux.getRoomID()+1];
                pair[0] = rAux.getRoomID();
                pair[1] = rAux2.getRoomID();
                walls.add(pair);
            }
            //Ahora comprobamos si la sala tiene una pared SUR
            if(rAux.getRoomID() + gameMap.getDimX() < gameMap.getDimMap()){
                pair = new int[2];
                rAux2 = roomVector[rAux.getRoomID() + gameMap.getDimX()];
                pair[0] = rAux.getRoomID();
                pair[1] = rAux2.getRoomID();
                walls.add(pair);
            }
            //Por ultimo comprobamos que la sala posee una pared OESTE
            if((rAux.getRoomID())%gameMap.getDimX() != 0){
                pair = new int[2];
                rAux2 = roomVector[rAux.getRoomID()-1];
                pair[0] = rAux.getRoomID();
                pair[1] = rAux2.getRoomID();
                walls.add(pair);
            }
            gameMap.addNode(rAux.getRoomID());
        }
        return walls;
    }

    //PRE: WallGestion bien creada e inicializada
    //POST: Derriba las paredes segun el algoritmo propuesto en las entregas
    public void breakWalls(Map gameMap){
        Room [] roomVector = gameMap.getRoomVector();
        LinkedList<int []> allWalls = generateWalls(gameMap);
        int wallsNumber = (gameMap.getRoomVector().length*5)/100;
        int brokenWalls = 0;
        int dimX = gameMap.getDimX();
        Grafo auxGraph = gameMap.getMapGraph();
        while(allWalls.size() > 0){
            int i = GenAleatorios.generarNumero(allWalls.size());
            int [] wall = allWalls.get(i);
            allWalls.remove(i);
            //Si las marcas de las salas son distintas las paredes pueden tirarse
            if(roomVector[wall[0]].getMark() != roomVector[wall[1]].getMark()){
                gameMap.addArc(wall[0], wall[1], 1);
                gameMap.addArc(wall[1], wall[0], 1);
                int desMarc = roomVector[wall[1]].getMark();
                //Las salas que tuviesen la marca de la sala con la que trabajamos cambian su marca
                for(int x = 0;x<roomVector.length;x++){
                    if(desMarc == roomVector[x].getMark()){
                        roomVector[x].setMark(roomVector[wall[0]].getMark());
                    }
                }
            }
        }
        //En la segunda parte del algoritmo buscamos las paredes extra para crear mas caminos
        while (brokenWalls < wallsNumber){
            int adjacentRoom = -1;
            int [] wall = new int[2];
            boolean done = false;
            int room = GenAleatorios.generarNumero(gameMap.getRoomVector().length);
            wall[0] = room;
            //Si no esta en el limite del mapa norte, comprobamos si hay pared
            if(room-dimX >= 0 && !auxGraph.adyacente(room, room-dimX)){
                adjacentRoom = room - dimX;
                wall[1] = adjacentRoom;
                done = tryWall(gameMap, wall);
            }
            if(!done){
                //Se comprueba si hay pared en el caso de que no este en el limite sur
                if(room + dimX < gameMap.getDimMap() && !auxGraph.adyacente(room,room + dimX)){
                    adjacentRoom = room + dimX;
                    wall[1] = adjacentRoom;
                    done = tryWall(gameMap, wall);
                }
                if(!done){
                    //Si no esta en el limite oeste comprobamos si hay pared
                    if(room%dimX != 0 && !auxGraph.adyacente(room, room-1)){
                        adjacentRoom = room - 1;
                        wall[1] = adjacentRoom;
                        done = tryWall(gameMap, wall);
                    }
                    if(!done){
                        //Comprobamos si hay pared en el caso de que no este en el limite este
                        if((room + 1)%dimX != 0 && !auxGraph.adyacente(room, room + 1)){
                            adjacentRoom = room + 1;
                            wall[1] = adjacentRoom;
                            done = tryWall(gameMap, wall);
                        }
                    }
                }
            }
            if(done)
                brokenWalls++;

        }
    }

    //PRE: WallGestion bien creada e inicializada
    //POST: Prueba una pared, usado como auxiliar para crear los atajos
    public boolean tryWall(Map gameMap, int [] walls){
        Grafo auxGraph = gameMap.getMapGraph();
        int dimX = gameMap.getDimX();
        int smaller = smaller(walls[0], walls[1]);
        boolean brokenWall = false;
        //Si la pared tiene una orientacion vertical
        if(walls[0]-walls[1] == 1 || walls[0]-walls[1] == -1){
            //Consideramos la opcion de que la pared no esté en los limites
            if(walls[0] - dimX >= 0 && walls[0] + dimX < gameMap.getDimMap()){
                if((!auxGraph.adyacente(smaller, smaller-dimX)||!auxGraph.adyacente(smaller-dimX, smaller-dimX+1)||!auxGraph.adyacente(smaller-dimX+1, smaller+1))
                        &&(!auxGraph.adyacente(smaller, smaller+dimX)||!auxGraph.adyacente(smaller+dimX, smaller+dimX+1)||!auxGraph.adyacente(smaller+dimX+1, smaller+1))){
                    gameMap.addArc(walls[0], walls[1], 1);
                    gameMap.addArc(walls[1], walls[0], 1);
                    brokenWall = true;
                }
            }
            else{
                //si la pared estuviese en el limite superior
                if(walls[0] - dimX < 0){
                    if(!auxGraph.adyacente(smaller, smaller+dimX)||!auxGraph.adyacente(smaller+dimX, smaller+dimX+1)||!auxGraph.adyacente(smaller+dimX+1, smaller+1)){
                        gameMap.addArc(walls[0], walls[1], 1);
                        gameMap.addArc(walls[1], walls[0], 1);
                        brokenWall = true;
                    }
                }
                //Consideracion de que la pared este en el limite inferior
                else{
                    if(!auxGraph.adyacente(smaller, smaller-dimX)||!auxGraph.adyacente(smaller-dimX, smaller-dimX+1)||!auxGraph.adyacente(smaller-dimX+1, smaller+1)){
                        gameMap.addArc(walls[0], walls[1], 1);
                        gameMap.addArc(walls[1], walls[0], 1);
                        brokenWall = true;
                    }
                }
            }
        }
        //Si la pared es horizontal
        else{
            //Consideramos que la pared pueda no estar en los limites
            if(walls[0]%dimX != 0 && (walls[0] + 1)%dimX != 0){
                if((!auxGraph.adyacente(smaller, smaller-1)||!auxGraph.adyacente(smaller-1, smaller+dimX-1)||!auxGraph.adyacente(smaller+dimX-1, smaller+dimX))
                        &&(!auxGraph.adyacente(smaller, smaller+1)||!auxGraph.adyacente(smaller+1, smaller+dimX+1)||!auxGraph.adyacente(smaller+dimX+1, smaller+dimX))){
                    gameMap.addArc(walls[0], walls[1], 1);
                    gameMap.addArc(walls[1], walls[0], 1);
                    brokenWall = true;
                }
            }
            else{
                //Consideracion de que la pared este en el limite izquierdo
                if(walls[0]%dimX == 0){
                    if(!auxGraph.adyacente(smaller, smaller+1)||!auxGraph.adyacente(smaller+1, smaller+dimX+1)||!auxGraph.adyacente(smaller+dimX+1, smaller+dimX)){
                        gameMap.addArc(walls[0], walls[1], 1);
                        gameMap.addArc(walls[1], walls[0], 1);
                        brokenWall = true;
                    }
                }
                //O en el limite derecho
                else{
                    if(!auxGraph.adyacente(smaller, smaller-1)||!auxGraph.adyacente(smaller-1, smaller+dimX-1)||!auxGraph.adyacente(smaller+dimX-1, smaller+dimX)){
                        gameMap.addArc(walls[0], walls[1], 1);
                        gameMap.addArc(walls[1], walls[0], 1);
                        brokenWall = true;
                    }
                }
            }
        }
        return brokenWall;
    }

    //PRE:
    //POST: Modulo auxiliar que nos indica el mayor de dos numeros pasados por parametros
    public int smaller(int num1, int num2){
        if(num1<num2)
            return num1;
            else
                return num2;
    }
}
