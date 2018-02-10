package gubo.http.querystring.parsers;

import java.text.ParseException;

public class FloatFieldParser extends BaseQueryStringFieldParser{

	@Override
	public Object parse(String s) throws ParseException {
		Float ret = Float.parseFloat(s);
		return ret;
	}
}
