import Characters.Character;
import Characters.ShExtrasensorial;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by n00b_user on 01/12/2017.
 */
public class CharacterTest {
    @Test
    public void emptyOfWeapons() throws Exception {
        Character test = new ShExtrasensorial("TestSH","T",1,20);
        assertTrue(test.emptyOfWeapons());
    }

    @Test
    public void getRoomI() throws Exception {
        Character test = new ShExtrasensorial("TestSH","T",1,20);
        assertEquals(20,test.getRoomI());
    }

    @Test
    public void getId() throws Exception {
        Character test = new ShExtrasensorial("TestSH","T",1,20);
        assertEquals("T",test.getId());
    }

    @Test
    public void getType() throws Exception {
        Character test = new ShExtrasensorial("TestSH","T",1,20);
        assertEquals("ShExtrasensorial",test.getType());
    }
}
