package gubo.http.querystring.parsers;

import java.text.ParseException;

public class LongFieldParser extends BaseQueryStringFieldParser{

	@Override
	public Object parse(String s) throws ParseException {
		Long ret = Long.parseLong(s);
		return ret;
	}
}
