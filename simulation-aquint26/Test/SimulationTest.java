import com.example.Animal;
import com.example.Forest;
import com.example.Shrub;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimulationTest {
    private Forest testLocation = new Forest();

    @Test
    public void testRain() {
        testLocation.rain();
        assertEquals(60.0, testLocation.getWater(), 0.0);
    }

    @Test
    public void testDisaster() {
        testLocation.disaster();
        assertEquals(60.0, testLocation.getWater(), 0.0);
    }

    @Test
    public void testReset() {
        testLocation.rain();
        testLocation.disaster();
        testLocation.reset();
        assertEquals(50.0, testLocation.getWater(), 0.0);
    }

    @Test
    public void testNextEpochAnimals() {
        for (int i = 0; i < 100; i++) {
            testLocation.getAnimals().add(new Animal(50.0, 50.0, 50.0, 50.0));
        }
        testLocation.nextEpoch();
        assertEquals(false, testLocation.getAnimals().size() == 100);
    }

    @Test
    public void testNextEpochShrubs() {
        for (int i = 0; i < 100; i++) {
            testLocation.getShrubs().add(new Shrub());
        }
        testLocation.nextEpoch();
        assertEquals(false, testLocation.getShrubs().size() == 100);
    }

    @Test
    public void testNextEpochReset() {
        testLocation.rain();
        testLocation.disaster();
        testLocation.nextEpoch();
        assertEquals(50.0, testLocation.getWater(), 0.0);
    }

    @Test
    public void testFindMate() {
        for (int i = 0; i < 100; i++) {
            testLocation.getAnimals().add(new Animal(50.0, 50.0, 50.0, 50.0));
        }

        for (int i = 0; i < testLocation.getAnimals().size(); i++) {
            testLocation.getAnimals().get(i).setHasMated(false);
            testLocation.getAnimals().get(i).findMate(testLocation.getAnimals());
        }

        // 300 is an arbitrarily chosen large number
        assertEquals(true, testLocation.getAnimals().size() > 300);
    }
}
