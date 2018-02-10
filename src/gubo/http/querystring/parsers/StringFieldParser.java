package gubo.http.querystring.parsers;

public class StringFieldParser extends BaseQueryStringFieldParser{

	@Override
	public Object parse(String s) {
		return s;
	}

}
