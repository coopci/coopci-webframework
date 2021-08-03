package springless.http.querystring.parsers;

import java.text.ParseException;

public class IntegerFieldParser extends BaseQueryStringFieldParser{

	@Override
	public Object parse(String s) throws ParseException {
		Integer ret = Integer.parseInt(s);
		return ret;
	}
}
