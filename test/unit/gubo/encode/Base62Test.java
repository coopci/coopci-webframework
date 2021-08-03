package gubo.encode;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import springless.encode.Base62;

public class Base62Test {

	@Test
	public void testEncodeDecode() {
		
		Base62 base62 = new Base62();
		
		assertEquals("0", base62.encodeBase10(0));
		assertEquals("9", base62.encodeBase10(9));
		assertEquals("a", base62.encodeBase10(10));
		assertEquals("f", base62.encodeBase10(15));
		assertEquals("g", base62.encodeBase10(16));
		assertEquals("z", base62.encodeBase10(35));
		assertEquals("A", base62.encodeBase10(36));
		assertEquals("Z", base62.encodeBase10(61));
		
		long num = 4723945325L;
		String encoded = base62.encodeBase10(num);
		System.out.println("encoded=" + encoded);
		assertEquals(num, base62.decodeBase62(encoded));		
	}
}
