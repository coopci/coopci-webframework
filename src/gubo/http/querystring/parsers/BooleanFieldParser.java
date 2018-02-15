package gubo.http.querystring.parsers;

import java.text.ParseException;
import java.util.concurrent.ConcurrentHashMap;

public class BooleanFieldParser extends BaseQueryStringFieldParser{

	static ConcurrentHashMap<String, Boolean> dict = new ConcurrentHashMap<String, Boolean>();
	
	static {
		dict.put("1", true);
		dict.put("true", true);
		dict.put("on", true);
		dict.put("yes", true);
		
		dict.put("0", false);
		dict.put("false", false);
		dict.put("off", false);
		dict.put("no", false);
		
	}
	@Override
	public Object parse(String s) throws ParseException {
		s = s.toLowerCase();
		if(dict.containsKey(s)) {
			return dict.get(s);
		}
		throw new java.text.ParseException("Unparseable boolean: " + s, 0);
		
	}
}
