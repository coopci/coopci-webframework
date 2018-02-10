package gubo.http.querystring.parsers;

import java.text.ParseException;

public class NullParser extends BaseQueryStringFieldParser{

	@Override
	public Object parse(String s) throws ParseException {
		return null;
	}
}
