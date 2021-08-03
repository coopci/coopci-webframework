package gubo.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import springless.properties.PropertiesPicker;

import java.util.Properties;

public class PropertiesPickerTest {

    @Test
    public void testPick() {
        
        String prefix = "abc.";
        
        Properties origin = new Properties();
        origin.put("abc.af", "1");
        origin.put("abc", "2");
        origin.put("abcd.af", "3");
        Properties picked = PropertiesPicker.pick(prefix, origin);
        
        
        assertEquals(1, picked.size());
        assertEquals("1", picked.getProperty("af"));
        
        assertFalse(picked.containsKey("abc"));
        assertFalse(picked.containsKey("abcd.af"));
        assertFalse(picked.containsKey("abc.af"));
    }
}
