package gubo.http.querystring.parsers;

public class StringFieldParser extends BaseQueryStringFieldParser {

	@Override
	public Object parse(String s) {
		if (s == null)
			return null;
		if (this.isDoTrim()) {
			return s.trim();
		} else {
			return s;
		}
	}
}
