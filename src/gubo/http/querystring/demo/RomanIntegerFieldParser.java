package gubo.http.querystring.demo;

import gubo.http.querystring.parsers.BaseQueryStringFieldParser;

import java.text.ParseException;
import java.util.concurrent.ConcurrentHashMap;

public class RomanIntegerFieldParser extends BaseQueryStringFieldParser{
static ConcurrentHashMap<String, Integer> dict = new ConcurrentHashMap<String, Integer>();
	
	static {
		dict.put("I", 1);
		dict.put("II", 2);
		dict.put("III", 3);
		dict.put("IV", 4);
		dict.put("V", 5);
		dict.put("VI", 6);
		dict.put("VII", 7);
		dict.put("VIII", 8);
		dict.put("IX", 9);
		dict.put("X", 10);
		
		
		
	}
	
	@Override
	public Object parse(String s) throws ParseException {
		// System.out.println("RomanIntegerFieldParser");
		Integer ret = dict.get(s.toUpperCase());
		return ret;
	}
}
