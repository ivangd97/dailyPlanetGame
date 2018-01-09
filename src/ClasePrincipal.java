/**
 * Clase creada para ser usada en la utilidad cargador
 * contiene el main del cargador. Se crea una instancia de la clase Cargador
 * y se procesa el fichero de inicio, es decir, se leen todas las líneas y se van creando todas las instancias de la simulación
 * 
 * @version 4.0 -  15/10/2014 
 * @author Profesores DP
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import Generation.*;
import mainPackage.Map;

public class ClasePrincipal {
	public static void main(String[] args) throws IOException {
		//Primero creamos el cargador
		Cargador cargador = new Cargador();
		try {
			//Procesamos linea a linea el fichero de entrada
		     FicheroCarga.procesarFichero(args[0], cargador);
		}
		catch (FileNotFoundException valor)  {
			//si no se encuentra el fichero salta el mensaje
			System.err.println ("Excepción capturada al procesar fichero: "+valor.getMessage());
		}
		catch (IOException valor)  {
			System.err.println ("Excepción capturada al procesar fichero: "+valor.getMessage());
		}
		//Creamos el mapa de juego
		Map gameMap = Map.getInstance();
		//Distribuimos las armas en las salas
		gameMap.weaponDistribution();
		//Simulamos el mapa de juego	
		gameMap.simulateMap();
	}
}
