import Utilities.DoorMan;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by n00b_user on 01/12/2017.
 */
public class DoorManTest {

    @Test
    public void isDoorOpen() throws Exception {
        DoorMan test = new DoorMan();
        Assert.assertTrue(test.doorOpen());
    }

    @Test
    public void close_door() throws Exception {
        DoorMan test = new DoorMan();
        test.setDoorOpen();
        test.close();
        Assert.assertFalse(test.doorOpen());
    }

}
