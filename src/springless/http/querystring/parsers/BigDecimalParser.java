package springless.http.querystring.parsers;

import java.math.BigDecimal;
import java.text.ParseException;

// time only, no date.
public class BigDecimalParser extends BaseQueryStringFieldParser {

	@Override
	public Object parse(String s) throws ParseException {
		return new BigDecimal(s);
	}
}
