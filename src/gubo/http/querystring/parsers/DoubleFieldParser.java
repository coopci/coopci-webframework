package gubo.http.querystring.parsers;

import java.text.ParseException;

public class DoubleFieldParser extends BaseQueryStringFieldParser{

	@Override
	public Object parse(String s) throws ParseException {
		Double ret = Double.parseDouble(s);
		return ret;
	}
}
