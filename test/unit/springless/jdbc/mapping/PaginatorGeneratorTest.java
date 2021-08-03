package springless.jdbc.mapping;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import springless.jdbc.mapping.PaginatorGenerator;

public class PaginatorGeneratorTest {

	@Test
	public void testGenerate() {
		PaginatorGenerator g = new PaginatorGenerator();
		
		HashMap<String, String> filter = new HashMap<String, String>();
		
		filter.put("page_size", "20");
		filter.put("page_num", "5");
		String pagination = g.generate(null, filter);
		assertEquals(" LIMIT 80, 20", pagination);
		
		filter.put("page_size", "-20");
		filter.put("page_num", "5");
		String pagination2 = g.generate(null, filter);
		assertEquals("", pagination2);
		
		filter.put("page_size", "20");
		filter.put("page_num", "-5");
		String pagination3 = g.generate(null, filter);
		assertEquals("", pagination3);
		
		filter.remove("page_size");
		filter.put("page_num", "5");
		String pagination4 = g.generate(null, filter);
		assertEquals("", pagination4);
		
		filter.put("page_size", "20");
		filter.remove("page_num");
		String pagination5 = g.generate(null, filter);
		assertEquals("", pagination5);
		
		
	}
}
