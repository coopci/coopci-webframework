package gubo.password;

import org.junit.Test;

import springless.password.Hasher;

public class HasherTest {

	@Test
	public void testEncode() {
		Hasher hasher = new Hasher();
		hasher.encode("a8wwtsdfgd", "1234567890123", 100000);
	}
}
