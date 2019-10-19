package gubo.jdbc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testWordsToSqlArray() {
		String sqlArray = Utils.wordsToSqlArray("w1,w2,w3");
		assertEquals("('w1', 'w2', 'w3')", sqlArray);
	}
}
