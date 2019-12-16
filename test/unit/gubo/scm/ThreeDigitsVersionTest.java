package gubo.scm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.UUID;

import org.junit.Test;

public class ThreeDigitsVersionTest {

	@Test
	public void testCompareTo() {
		ThreeDigitsVersion vn1 = new ThreeDigitsVersion("1.0.11");
		ThreeDigitsVersion vn2 = new ThreeDigitsVersion("1.0.9");
		ThreeDigitsVersion vn3 = new ThreeDigitsVersion("1.0.11");
		
		// 1.0.9 < 1.0.11
		assertEquals(-1, vn2.compareTo(vn1));
		
		// 1.0.11 > 1.0.9
		assertEquals(1, vn1.compareTo(vn2));
		
		
		// 1.0.11 == 1.0.11 
		assertEquals(0, vn1.compareTo(vn1));
		
		// 1.0.9 == 1.0.9
		assertEquals(0, vn2.compareTo(vn2));
		
		// 1.0.11 == 1.0.11 
		assertEquals(0, vn1.compareTo(vn3));
	}
	
	@Test
	public void testEquals() {
		ThreeDigitsVersion vn1 = new ThreeDigitsVersion("1.0.11");
		ThreeDigitsVersion vn2 = new ThreeDigitsVersion("1.0.9");
		ThreeDigitsVersion vn3 = new ThreeDigitsVersion("1.0.11");
		assertEquals(vn1, vn1);
		assertEquals(vn2, vn2);
		assertEquals(vn1, vn3);
		assertNotEquals(vn1, vn2);
	}
	
	@Test
	public void testHashCode() {
		ThreeDigitsVersion vn1 = new ThreeDigitsVersion("1.0.11");
		ThreeDigitsVersion vn3 = new ThreeDigitsVersion("1.0.11");
		HashMap<ThreeDigitsVersion, String> map = new HashMap<ThreeDigitsVersion, String>();
		
		String value = UUID.randomUUID().toString();
		map.put(vn1, value);
		
		assertEquals(value, map.get(vn3));
		
	}
	
	@Test(expected = VersionException.class)
    public void testCheckFailed1() throws VersionException {
        ThreeDigitsVersion vn1 = new ThreeDigitsVersion("1.0.11");
        vn1.check("1.0.9");
    }
	
	@Test(expected = VersionException.class)
    public void testCheckFailed2() throws VersionException {
        ThreeDigitsVersion vn1 = new ThreeDigitsVersion("1.0.11");
        vn1.check("");
    }
	
	@Test
    public void testCheckPass() throws VersionException {
        ThreeDigitsVersion vn1 = new ThreeDigitsVersion("1.0.11");
        vn1.check("1.0.11");
    }

	
}
