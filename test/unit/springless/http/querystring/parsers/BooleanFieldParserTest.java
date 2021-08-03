package springless.http.querystring.parsers;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

import springless.http.querystring.parsers.BooleanFieldParser;

public class BooleanFieldParserTest {

	@Test
	public void testParseFalse() throws ParseException {
		BooleanFieldParser p = new BooleanFieldParser();
		
		assertEquals(false, p.parse("0"));
		assertEquals(false, p.parse("false"));
		
	}
}
