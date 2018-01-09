package mainPackage;

/**
 * Created by n00b_user on 19/10/2017.
 */
import Characters.Character;
import Utilities.DoorMan;
import Utilities.Room;
import Utilities.Weapon;
import Structures.*;
import sun.awt.image.ImageWatched;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;


public class Map {
    //dimension X del mapa
    private int dimX;

    //dimension Y del mapa
    private int dimY;

    //dimension total del mapa, usada para algunos modulos
    private int dimMap;

    //vector que contiene las salas del mapa
    private Room[] roomVector;

    //sala del daily planet, que corresponde con el objetivo
    private int dailyPlanet;

    //altura del arbol de armas
    private int deep;

    //turno del mapa
    private int limitTurn;

    //Turno actual de la simulacion
    private int actualTurn;

    //Lista de paredes tiradas, usado para crear los caminos y atajos
    private LinkedList<int []> brokenWalls = new LinkedList<int []>();

    //Grafo del mapa
    private Grafo mapGraph;

    //Mapa auxiliar
    private static Map auxMap = null;

    //Nos dice si es la primera vez que se ejecuta el mapa
    private boolean firstTimeEx;

    //Habitacion para los magicos y misticos ganadores del juego
    private Room winnersRoom = new Room(1111);

    private boolean FirstTimeEx;

    //constructor por defecto del mapa, valores predeterminados
    //PRE:
    //POST: Crea un mapa por defecto
    public Map(){
        dimX = 6;
        dimY = 6;
        dimMap = dimX*dimY;
        dailyPlanet = dimMap-1;
        limitTurn = 0;
        actualTurn = 0;
        mapGraph = new Grafo();
        this.firstTimeEx = true;
    }

    //constructor parametrizado del mapa, se le da las dimensiones
    //PRE:
    //POST: Crea un mapa con los datos de entrada
    public Map(int dailyPlanet,int dimX,int dimY, int deep){
        this.dimX = dimX;
        this.dimY = dimY;
        this.dimMap = dimX*dimY;
        this.dailyPlanet = dailyPlanet;
        this.roomVector = new Room[dimMap];
        this.deep = deep;
        for(int i = 0; i<dimMap; i++){
            Room rAux = new Room(i);
            this.roomVector[i] = rAux;
        }
        this.limitTurn = 50;
        this.actualTurn = 0;
        this.mapGraph = new Grafo();
        this.FirstTimeEx = true;
    }

    //PRE: Map bien creado e inicializado
    //POST: Devuelve una instancia del mapa de juego
    public static Map getInstance(){
        if(auxMap == null) {
            return auxMap = new Map();
        }
        else
            return auxMap;
    }

    //PRE: Map bien creado e inicializado
    //POST: Devuelve una instancia del mapa con los parametros de entrada
    public static Map getInstance(int dimX, int dimY, int dailyPlanet, int treeControl){
        if(auxMap == null) {
            return auxMap = new Map(dailyPlanet,dimX,dimY,treeControl);
        }
        else
            return auxMap;
    }

    //Distribuye las armas en las salas del mapa
    //PRE: Map bien creado e inicializado
    //POST: Con un vector de armas fijo, se seleccionan las salas de mayor transito en la simulacion y se distribuyen
    // las armas en dichas salas
    public void weaponDistribution(){
        Weapon [] weaponinRooms = {new Weapon("Mjolnir",29), new Weapon("Anillo",1), new Weapon("Garra",27),
                new Weapon("Armadura",3), new Weapon("Red",25), new Weapon("Escudo",5), 
                new Weapon("Lucille",23), new Weapon("Lawgiver",7), new Weapon("GuanteInfinito",21), 
                new Weapon("LazoVerdad",9), new Weapon("CadenaFuego",19), new Weapon("Capa",11), 
                new Weapon("Flecha",17), new Weapon("Tridente",13), new  Weapon("Antorcha",15), 
                new Weapon("Baston",28), new Weapon("Latigo",2), new  Weapon("MazaOro",26), 
                new Weapon("CampoMagnetico",4), new Weapon("Tentaculo",24),
                new Weapon ("CampoEnergia",6), new Weapon("Cetro",22), new Weapon("RayoEnergia",8), 
                new Weapon("Laser",20), new Weapon("Bola",10), new Weapon("Espada",18), 
                new Weapon("Sable",12),  new Weapon("Acido",16), new Weapon("Gema",14), 
                new Weapon("Nullifier",23), new Weapon("Mjolnir",1), new Weapon("Anillo",29), 
                new Weapon("Garra",3), new Weapon("Armadura",27),  new Weapon("Red",5), 
                new Weapon("Escudo",25), new Weapon("Lucille",7), new  Weapon("Lawgiver",23), 
                new Weapon("GuanteInfinito",9), new Weapon("LazoVerdad",21),
                new Weapon("CadenaFuego",11), new Weapon("Capa",19), new Weapon("Flecha",13), 
                new Weapon("Tridente",17), new Weapon("Antorcha",28), new Weapon("Baston",15), 
                new Weapon("Latigo",26), new Weapon("MazaOro",2), new Weapon("CampoMagnetico",24), 
                new Weapon("Tentaculo",4), new Weapon("CampoEnergia",22), new Weapon("Cetro",6), 
                new Weapon("RayoEnergia",20), new Weapon("Laser",8), new Weapon("Bola",18), 
                new Weapon("Espada",10), new Weapon("Sable",16), new Weapon("Acido",12), 
                new Weapon("Gema",1), new Weapon("Nullifier",3)};
        int [] roomNumbers = selectRooms();

        int i = 0;
        int j;
        int k = 0;
        Room r;
        while(i<roomNumbers.length){
            r = roomVector[roomNumbers[i]];
            j=0;
            while(j<5){
                r.addWeapon(weaponinRooms[k]);
                j++;
                k++;
            }
            i++;
        }
    }

    //añade un arco desde un origen a un destino con un peso de "value" en el grafo del mapa
    //PRE: Map bien creado y mapGraph bien inicializado
    //POST: añade un arco al grafo del mapa con el valor que representa value
    public void addArc(int or, int dest, int value){
        mapGraph.nuevoArco(or,dest,value);
    }

    //Devuelve el grafo del mapa (usado para generar los caminos y atajos)
    //PRE: Map bien creado e inicializado
    //POST: REtorna el grafo del mapa
    public Grafo getMapGraph(){
        return this.mapGraph;
    }

    //Este método inserta un personaje en el mapa de la simulacion
    //PRE: Map && Character bien creados e inicializados
    //POST: Inserta al personaje c en la sala que se pasa por parametro
    public void insertCharacter(Character c, int numSala){
        roomVector[numSala].addCharacter(c);
    }

    //Este método inserta al hombre puerta en la sala daily planet
    //PRE: Map && DoorMan bien creados e inicializados
    //POST: Inserta al hombre verja en la sala del daily planet
    public void insertDoorMan(DoorMan doorMan){
        roomVector[dailyPlanet].addDoorMan(doorMan);
    }

    //En este método se simula el mapa sala por sala
    //PRE: Map bien creado e inicializado
    //POST: Simula el mapa recorriendo cada una de sus salas y llamando a los modulos de simulacion de estas, comprueba
    //tambien si la sala es el daily planet para ver si los personajes han ganado o pueden ganar el juego
    public void simulateMap() throws IOException{
        Room r;
        boolean isDoorManRoom = false;
        Cola<Character> aux = new Cola<Character>();
        //Obtenemos primero al hombre puerta
        DoorMan TheDoorMan = roomVector[dailyPlanet].getIsDoorMan();
        //Ejecutamos la simulacion mientras el hombre picaporte este cerrado y el turno no haya llegado al limite
        while(actualTurn < limitTurn && !TheDoorMan.doorOpen()){
            System.out.println();
            System.out.println("***** TURNO " + actualTurn + " *****");
            for(int i=0; i < dimMap ; i++){
                r = roomVector[i];
                //Valoramos primero la opcion de que nos encontremos en la sala del daily planet y tengamos que interactuar con
                //el increible hombre cerrojo
                if(r.getRoomID() == dailyPlanet)
                    isDoorManRoom = true;
                else
                    isDoorManRoom = false;
                //Ahora llamamos a la simulacion de cada sala
                r.simulateRoom(roomVector,actualTurn,isDoorManRoom,dimX,dimY);
            }

            //Si la verja se ha abierto comprobamos si el personaje puede ganar
            if(TheDoorMan.doorOpen()){
                boolean isWinner = false;
                Character winner;
                while(!roomVector[dailyPlanet].isEmpty() && !isWinner){
                    winner = roomVector[dailyPlanet].getCharacter();
                    roomVector[dailyPlanet].removeCharacter();
                    if(winner.isWinner()){
                        winnersRoom.addCharacter(winner);
                        isWinner = true;
                    }
                    else
                        aux.addData(winner);
                }
                while(!aux.isEmpty()){
                    winner = aux.getFirst();
                    roomVector[dailyPlanet].addCharacter(winner);
                    aux.removeData();
                }
                while(!roomVector[dailyPlanet].isEmpty()){
                    winner = roomVector[dailyPlanet].getCharacter();
                    roomVector[dailyPlanet].removeCharacter();
                    if(winner.isWinner())
                        winnersRoom.addCharacter(winner);
                    else
                        aux.addData(winner);
                }
                while(!aux.isEmpty()){
                    winner = aux.getFirst();
                    roomVector[dailyPlanet].addCharacter(winner);
                    aux.removeData();
                }
            }
            //Dibujamos el mapa en el fichero
            drawInFile();
            //Se incrementa el turno
            actualTurn++;
        }
    }

    //PRE: Map bien creado e inicializado
    //POST: Dibuja la informacion del mapa y el turno en el fichero de salida
    public void drawInFile() throws IOException{
        BufferedWriter bufferout;
        if(firstTimeEx){
            bufferout = new BufferedWriter(new FileWriter("registro.log"));
            firstTimeEx = false;
        }
        else {
            bufferout = new BufferedWriter(new FileWriter("registro.log", true));
            bufferout.write("(TURNO:" + actualTurn + ")");
            bufferout.newLine();
            bufferout.write("(MAPA:" + dailyPlanet + ")");
            bufferout.newLine();
            bufferout.close();
            roomVector[dailyPlanet].getIsDoorMan().showDoorManinfo();
            showMap();
            for (int i = 0; i < roomVector.length; i++) {
                if (!roomVector[i].emptyOfWeapons()) {
                    Room r = roomVector[i];
                    r.showRoomInfo();
                }
            }
            for (int i = 0; i < roomVector.length; i++) {
                if (!roomVector[i].isEmpty()) {
                    Room r = roomVector[i];
                    r.showCharacters();
                }
            }
            DoorMan d = roomVector[dailyPlanet].getIsDoorMan();
            bufferout = new BufferedWriter(new FileWriter("registro.log", true));
            if (d.doorOpen() || actualTurn == limitTurn-1) {
                Character winner = winnersRoom.getCharacter();
                bufferout.write("(teseractomembers)");
                bufferout.newLine();
                bufferout.write("(owneroftheworld:" + winner.getType() + ":" + winner.getId() + ":" + winnersRoom.getRoomID() + ":" + actualTurn + ")");
                winnersRoom.removeCharacter();
                if(!winnersRoom.isEmpty()){
                    while(!winnersRoom.isEmpty()){
                        winner = winnersRoom.getCharacter();
                        bufferout.write("(" + winner.getType() + ":" + winner.getId() + ":" + winnersRoom.getRoomID() + ":" + actualTurn + ")");
                        winnersRoom.removeCharacter();
                    }
                }
            }

            bufferout.newLine();
            bufferout.write("*******************************************");
            bufferout.newLine();
            bufferout.newLine();
            bufferout.close();
        }
    }

    //Muestra el mapa en el fichero de salida registro.log
    //PRE: Map bien creado e inicializado
    //POST: Dibuja la forma del mapa en el fichero de salida
    public void showMap() throws IOException{
        BufferedWriter bufferout;
        bufferout = new BufferedWriter(new FileWriter("registro.log",true));
        bufferout.newLine();
        for(int x=0; x<dimX; x++){
            bufferout.write(" _");
        }
        bufferout.newLine();
        int n = 0;
        for(int x=0; x<dimY; x++){
            bufferout.write("|");
            for(int y=0; y<dimX; y++){
                if(roomVector[n].isEmpty()){
                    if(!mapGraph.adyacente(n, n+dimX)){
                        bufferout.write("_");
                    }
                    else
                        bufferout.write(" ");
                }
                else{
                    if(roomVector[n].numCharacters()>1)
                        bufferout.write(""+roomVector[n].numCharacters()+"");
                    else
                        bufferout.write(roomVector[n].getCharacter().getId());
                }
                if(!mapGraph.adyacente(n, n+1)){
                    bufferout.write("|");
                }
                else
                    bufferout.write(" ");
                n++;
            }
            bufferout.newLine();
        }
        bufferout.close();
    }

    //Este método calcula todos los caminos entre el origen y el destino que se le pase, usado para calcular las salas mas
    //visitadas del mapa en el reparto de armas posterior
    //PRE: Map bien creado e inicializado, or >= 0, dest < dimMap
    //POST: Calcula todos los caminos posibles entre un origen y un destino usando el grafo del mapa
    public void allPaths(int or, int dest, LinkedList<Integer> visited, Grafo graph, LinkedList<LinkedList<Integer>> paths){
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
                    allPaths(neightbors.get(i),dest,visited,graph,paths);
            }
        }
        visited.removeLast();
    }

    //Este método llama al anterior para generar todos los caminos posibles e incrementar las salas segun su frecuencia
    //de aparicion, que sera devuelta en el vector visitNumber
    //PRE: Map bien creado e inicializado
    //POST: Nos devuelve en un vector de enteros las salas mas transitadas del mapa para poder distribuir las armas en ellas
    public int [] mostVisitedRooms(){
        int [] visitNumber = new int[dimX*dimY];
        LinkedList<LinkedList<Integer>> paths = new LinkedList<LinkedList<Integer>>();
        LinkedList<Integer> visited = new LinkedList<Integer>();
        allPaths(roomVector[0].getRoomID(),roomVector[dailyPlanet].getRoomID(),visited,mapGraph,paths);
        //Se recorre cada camino en este primer bucle
        for(int i = 0; i< paths.size();i++){
            //En este siguiente bucle recorremos las salas de cada camino para formar el vector
            for(int j = 0;j < visitNumber.length; j++){
                if(paths.get(i).contains(j))
                    visitNumber[j]++;
            }
        }
        return visitNumber;
    }

    //Este modulo selecciona las 12 salas con mayor aparicion en las rutas para distribuir las armas
    //PRE: Map bien creado e inicializado
    //POST: Selecciona del vector de salas las 12 primeras salas para distribuir las armas
    public int [] selectRooms(){
        int [] resultVector = new int[12];
        int j,n;
        j = 0;
        n = 0;
        int [] orVector = mostVisitedRooms();
        int number = orVector[0];
        while(j<12){
            for(int i = 0; i < orVector.length;i++){
                if(orVector[i] > number){
                    number = orVector[i];
                    n = i;
                }
            }
            orVector[n] = -1;
            resultVector[j] = n;
            number = -1;
            n = -1;
            j++;
        }
        return resultVector;
    }


    //Devuelve el vector de salas del mapa para poder usarlo
    //PRE: Map bien creado e inicializado
    //POST: Retorna el vector de salas
    public Room [] getRoomVector(){return roomVector;}

    //Devuelve la dimension total del mapa
    //PRE: Map bien creado e inicializado
    //POST: Nos da la dimension del mapa
    public int getDimMap(){return dimMap;}

    //Devuelve la dimension X del mapa
    //PRE: Map bien creado e inicializado
    //POST: Devuelve la dimension X
    public int getDimX(){return dimX;}

    //Devuelve la profundidad del arbol
    //PRE: Map bien creado e inicializado
    //POST: Nos da la profundidad del arbol
    public int getDeep(){return this.deep;}

    //Da la esquina noreste del mapa, usada para inicializar cierto tipo de personaje
    //PRE: Map bien creado e inicializado
    //POST: Devuelve la esquina noreste del mapa
    public int getNorthEastCorner(){return dimX-1;}

    //Devuelve la esquina suroeste del mapa
    //PRE: Map bien creado e inicializado
    //POST: DEvuelve la esquina suroeste del mapa
    public int getSouthWestCorner(){return dimMap-dimX;}

    //Retorna la esquina noroeste del mapa, siemore coincide con la sala 0
    //PRE: Map bien creado e inicializado
    //POST: DEvuelve la esquina noroeste del mapa
    public int getNorthWestCorner(){return 0;}

    //Añade un nodo al grafo del mapa
    //PRE: Map && mapGraph bien creados e inicializados
    //POST: Añade un nodo nuevo al grafo del mapa
     public void addNode(int number){mapGraph.nuevoNodo(number);}

     //Devuelve la dimension Y del mapa
     //PRE: Map bien creado e inicializado
     //POST: Devuelve la dimension Y del mapa
     public int getDimY(){return dimY;}


     //Retorna el numero de la sala daily planet
     //PRE: Map bien creado e inicializado
     //POST: Nos da la sala del dailyPlanet
     public int getDailyPlanet(){return dailyPlanet;}

    //PRE: Map bien creado e inicializado
    //POST: Turno limite de simulacion, se usaba en las primeras entregas para que el programa acabase en un maximo de turnos
     public int getLimitTurn(){return limitTurn;}

    //PRE: Map bien creado e inicializado
    //POST: Devuelve el turno actual del juego, no coincide a veces con el turno del personaje
    public int getActualTurn(){return actualTurn;}


    //Añade una pared rota, fue usado antes de incorporar el grafo en el proyecto
    //PRE: Map bien creado e inicializado
    //POST: Añade una pared rota, usado en entregas anteriores
    public void addBrokenWall(int [] wall){brokenWalls.add(wall);}

}


