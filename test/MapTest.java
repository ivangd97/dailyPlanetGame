import mainPackage.Map;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by n00b_user on 01/12/2017.
 */
public class MapTest {
    @Test
    public void getDimY() throws Exception {
        Map testMap = new Map();
        assertNotEquals(0,testMap.getDimY());
    }

    @Test
    public void getSouthWestCorner() throws Exception {
        Map testMap = new Map();
        assertEquals(0,testMap.getSouthWestCorner());
    }

    @Test
    public void getDailyPlanet() throws Exception {
        Map testMap = new Map();
        assertEquals(35,testMap.getDailyPlanet());
    }

    @Test
    public void getDimMap() throws Exception {
        Map testMap = new Map();
        assertEquals(36,testMap.getDimMap());
    }

    @Test
    public void getDimX() throws Exception {
        Map testMap = new Map();
        assertEquals(6,testMap.getDimX());
    }
}
