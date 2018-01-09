import Characters.Character;
import Characters.ShExtrasensorial;
import Characters.Villain;
import Utilities.DoorMan;
import Utilities.Room;
import Utilities.Weapon;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by n00b_user on 01/12/2017.
 */
public class RoomTest {
    @Test
    public void insertCharacter ()throws Exception {
        Room rTest = new Room();
        Character aux = new ShExtrasensorial("TestSH","T",1,20);
        Character aux2 = new Villain("TestVillain","V",1,20);
        rTest.addCharacter(aux);
        rTest.addCharacter(aux2);
        while(!rTest.isEmpty()){
            Character test = rTest.getCharacter();
            assertTrue(test == rTest.getCharacter());
            rTest.removeCharacter();
        }

    }

    @Test
    public void getMark() throws Exception {
        Room rTest = new Room();
        assertTrue(0 == rTest.getMark());
    }

    @Test
    public void getRoomID() throws Exception {
        Room rTest = new Room();
        assertTrue(0 == rTest.getRoomID());
    }

    @Test
    public void getDoorMan() throws Exception {
        Room rTest = new Room();
        DoorMan aux = new DoorMan();
        rTest.addDoorMan(aux);
        assertEquals(aux,rTest.getIsDoorMan());
    }


    @Test
    public void isEmpty() throws Exception {
        Room rTest = new Room();
        assertTrue(rTest.isEmpty());
    }

    @Test
    public void addWeapon() throws Exception {
        Room rTest = new Room();
        Weapon wAux = new Weapon("THEWEAPON",999);
        rTest.addWeapon(wAux);
        assertEquals(wAux,rTest.getWeaponList().getFirst());
    }

    @Test
    public void emptyOfWeapons() throws Exception {
        Room rTest = new Room();
        assertTrue(rTest.emptyOfWeapons());
    }

}
