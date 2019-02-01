import org.junit.Test;

import static org.junit.Assert.*;

public class CamelCaserTest {

    @Test
    public void lowerCaseTest() {
        assertEquals("thisIsMyString", CamelCaser.camelCase("this is my string"));
    }

    @Test
    public void upperCaseTest() {
        assertEquals("thisIsMyString", CamelCaser.camelCase("THIS IS MY STRING"));
    }

    @Test
    public void noSpaceTest() {
        assertEquals("thisismystring", CamelCaser.camelCase("thisismystring"));
    }

    @Test
    public void emptyStringTest() {
        assertEquals("", CamelCaser.camelCase(""));
    }

    @Test
    public void tabTest() {
        assertEquals("thisIsMyString", CamelCaser.camelCase("this\tis\tmy\tstring"));
    }

    @Test
    public void newlineTest() {
        assertEquals("thisIsMyString", CamelCaser.camelCase("this\nis\nmy\nstring"));
    }

    @Test
    public void illegalFormatTest() {
        assertEquals("Exception occurred", CamelCaser.camelCase("1this is my string"));
    }

    @Test
    public void illegalCharacterTest() {
        assertEquals("Exception occurred", CamelCaser.camelCase("/ /"));
    }

    @Test
    public void nullTest() {
        assertEquals("Exception occurred", CamelCaser.camelCase(null));
    }
}