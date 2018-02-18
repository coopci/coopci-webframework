package gubo.http.querystring.parsers;

import java.sql.Time;
import java.text.ParseException;

// time only, no date.
public class TimeParser extends BaseQueryStringFieldParser {

	@Override
	public Object parse(String s) throws ParseException {
		return Time.valueOf(s);
	}
}
