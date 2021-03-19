package gubo.jdbc;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class UtilsTest {

	@Test
	public void testWordsToSqlArray() {
		String sqlArray = Utils.wordsToSqlArray("w1,w2,w3");
		assertEquals("('w1', 'w2', 'w3')", sqlArray);
	}
	
	

	@Test
	public void testWordsListToSqlArray() {
		
		List<String> lst = new LinkedList<>();
		lst.add("w1");
		lst.add("w2");
		lst.add("w3");
		String sqlArray = Utils.wordsToSqlArray(lst);
		assertEquals("('w1', 'w2', 'w3')", sqlArray);
	}
}
