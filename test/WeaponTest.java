import Utilities.Weapon;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by n00b_user on 01/12/2017.
 */
public class WeaponTest {
    @Test
    public void getName() throws Exception {
        Weapon test = new Weapon("MasterWeapon", 1);
        assertEquals("MasterWeapon", test.getName());
    }

    @Test
    public void getDamage() throws Exception {
        Weapon test = new Weapon("AnotherMasterWeapon", 1);
        assertNotEquals(999, test.getDamage());
        //Menuda Master Weapon...
    }
}
