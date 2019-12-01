package gubo.http.querystring.parsers;

public class EnumFieldParser extends BaseQueryStringFieldParser {

	private final Class<? extends Enum> enumClass;
	public EnumFieldParser(Class<? extends Enum> enumClass) {
		this.enumClass = enumClass;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object parse(String s) throws Exception {
		return Enum.valueOf(this.enumClass, s);
	}

}
