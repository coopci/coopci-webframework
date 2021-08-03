package springless.http.querystring.parsers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class DatetimeParser extends BaseQueryStringFieldParser{

	
	SimpleDateFormat formatter;
	public DatetimeParser() {
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	@Override
	public Object parse(String s) throws ParseException {
		Date ret = this.formatter.parse(s);
		return ret;
	}
}
