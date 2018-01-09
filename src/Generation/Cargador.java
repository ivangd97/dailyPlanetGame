package Generation;

import Characters.ShExtrasensorial;

import Characters.ShFlight;
import Characters.ShPhysical;
import Characters.Villain;
import Utilities.DoorMan;
import Utilities.WallGestion;
import Utilities.Weapon;
//import com.sun.xml.internal.bind.v2.TODO;
import mainPackage.Dir;
import mainPackage.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase creada para ser usada en la utilidad cargador
 * contiene el main del cargador. Se crea una instancia de la clase Estacion, una instancia de la clase Cargador
 * y se procesa el fichero de inicio, es decir, se leen todas las líneas y se van creando todas las instancias de la simulación
 * 
 * @version 5.0 -  27/10/2016 
 * @author Profesores DP
 */
public class Cargador {
	/**  
	número de elementos distintos que tendrá la simulación - Mapa, Stark, Lannister, Baratheon, Targaryen
	*/
	static final int NUMELTOSCONF = 5;
	/**  
	atributo para almacenar el mapeo de los distintos elementos
	*/
	static private DatoMapeo [] mapeo;
	
	/**
	 *  constructor parametrizado
	 */
	public Cargador()  {
		mapeo = new DatoMapeo[NUMELTOSCONF];
		mapeo[0]= new DatoMapeo("MAP", 5);
		mapeo[1]= new DatoMapeo("SHPHYSICAL", 4);
		mapeo[2]= new DatoMapeo("SHEXTRASENSORIAL", 4);
		mapeo[3]= new DatoMapeo("SHFLIGHT", 4);
		mapeo[4]= new DatoMapeo("VILLAIN", 4);
		
	}
	
	/**
	 *  busca en mapeo el elemento leído del fichero inicio.txt y devuelve la posición en la que está 
	 *  @param elto elemento a buscar en el array
	 *  @return res posición en mapeo de dicho elemento
	 */
	private int queElemento(String elto)  {
	    int res=-1;
	    boolean enc=false;

	    for (int i=0; (i < NUMELTOSCONF && !enc); i++)  {
	        if (mapeo[i].getNombre().equals(elto))      {
	            res=i;
	            enc=true;
	        }
	    }
	    return res;
	}
	
	/**
	 *  método que crea las distintas instancias de la simulación 
	 *  @param elto nombre de la instancia que se pretende crear
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo de la instancia
	 */
	public void crear(String elto, int numCampos, List<String> vCampos)	{
	    //Si existe elemento y el número de campos es correcto, procesarlo... si no, error
	    int numElto = queElemento(elto);

	    //Comprobación de datos básicos correctos
	    if ((numElto!=-1) && (mapeo[numElto].getCampos() == numCampos))   {
	        //procesar
	        switch(numElto){
	        case 0:	   
	            crearMap(numCampos,vCampos);
	            break;
	        case 1:
	            crearSHPhysical(numCampos,vCampos);
	            break;
	        case 2:
	            crearSHExtraSensorial(numCampos,vCampos);
	            break;
	        case 3:
	            crearSHFlight(numCampos,vCampos);
	            break;
	        case 4:
	            crearVillain(numCampos,vCampos);
	            break;
	     	}
	    }
	    else
	        System.out.println("ERROR Cargador::crear: Datos de configuración incorrectos... " + elto + "," + numCampos+"\n");
	}

	/**
	 *  método que crea una instancia de la clase Planta
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo
	 */
	private void crearMap(int numCampos, List<String> vCampos){
	    System.out.println("Creado Map: " + vCampos.get(1) + "\n");
	    //inicializar mapa
		Integer dimX = Integer.parseInt(vCampos.get(1));
		Integer dimY = Integer.parseInt(vCampos.get(2));
		Integer dailyPlanet = Integer.parseInt(vCampos.get(3));
		Integer portalControl = Integer.parseInt(vCampos.get(4));
		Map gameMap = Map.getInstance(dimX,dimY,dailyPlanet,portalControl);
		//Creamos la configuracion de las paredes del mapa
		WallGestion wg = new WallGestion();
		wg.breakWalls(gameMap);

		//Ahora creamos y configuramos al dueño del cerrojo
		DoorMan gameDoorMan = new DoorMan();
		Weapon[] weaponinDoorMan = {new Weapon("CampoEnergia", 5), new Weapon("Armadura",13), new Weapon("Anillo",11),
				new Weapon("Acido",1),new Weapon ("Antorcha",5), new Weapon("Bola",3), new Weapon("Baston",22),new Weapon ("CadenaFuego",11),
				new Weapon("Espada",11), new Weapon("Cetro",20), new Weapon("Capa",10), new Weapon("CampoMagnetico",5),
				new Weapon("Escudo",3), new Weapon("Garra",22), new Weapon("Flecha",12),new Weapon ("Gema",4)};
		gameDoorMan.configurateTree(weaponinDoorMan);
		//Insertamos al hombre picaporte
		gameMap.insertDoorMan(gameDoorMan);
	}

	/**
	 *  método que crea una instancia de la clase SHPhysical
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo
	 */
	private void crearSHPhysical(int numCampos, List<String> vCampos){
	    System.out.println("Creado SHPhysical: " + vCampos.get(1) + "\n");
	    //Registrar SHPhysical en el mapa
		String name = vCampos.get(1);
		String mark = vCampos.get(2);
		Integer turn = Integer.parseInt(vCampos.get(3));

		ShPhysical ph = new ShPhysical(name,mark,turn,0);
		ph.setRoomI(0);
		Map gameMap = Map.getInstance();
		ph.calculateMovement();
		gameMap.insertCharacter(ph,0);
	}

	/**
	 *  método que crea una instancia de la clase SHExtraSensorial
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo
	 */
	private void crearSHExtraSensorial(int numCampos, List<String> vCampos){
	    System.out.println("Creado SHExtraSensorial: " + vCampos.get(1) + "\n");
	    //Registrar SHExtraSensorial en el mapa

		String name = vCampos.get(1);
		String mark = vCampos.get(2);
		Integer turn = Integer.parseInt(vCampos.get(3));

		ShExtrasensorial ex = new ShExtrasensorial(name,mark,turn,0);
		ex.setRoomI(0);
		Map gameMap = Map.getInstance();
		ex.calculateMovement();
		gameMap.insertCharacter(ex,0);
	}	

	/**
	 *  método que crea una instancia de la clase SHFlight
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo
	 */
	private void crearSHFlight(int numCampos, List<String> vCampos){
	    System.out.println("Creado SHFlight: " + vCampos.get(1) + "\n");
	    //Registrar SHFlight en el mapa

		String name = vCampos.get(1);
		String mark = vCampos.get(2);
		Integer turn = Integer.parseInt(vCampos.get(3));

		Map gameMap = Map.getInstance();
		ShFlight fl = new ShFlight(name,mark,turn,gameMap.getSouthWestCorner());
		fl.setRoomI(gameMap.getSouthWestCorner());
		fl.calculateMovement();
		gameMap.insertCharacter(fl,gameMap.getSouthWestCorner());
	}	

	/**
	 *  método que crea una instancia de la clase Villain
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo
	 */
	private void crearVillain(int numCampos, List<String> vCampos){
	    System.out.println("Creado Villain: " + vCampos.get(1) + "\n");
	    //Registrar Villain en el mapa

		String name = vCampos.get(1);
		String mark = vCampos.get(2);
		Integer turn = Integer.parseInt(vCampos.get(3));
		Map gameMap = Map.getInstance();
		Villain vi = new Villain(name,mark,turn, gameMap.getNorthEastCorner());
		vi.setRoomI(gameMap.getNorthEastCorner());
		vi.calculateMovement();
		gameMap.insertCharacter(vi,gameMap.getNorthEastCorner());
	}

}
