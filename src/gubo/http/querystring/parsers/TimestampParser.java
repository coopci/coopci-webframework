package gubo.http.querystring.parsers;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class TimestampParser extends BaseQueryStringFieldParser{

	
	SimpleDateFormat formatter;
	public TimestampParser() {
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	@Override
	public Object parse(String s) throws ParseException {
		Date ret = this.formatter.parse(s);
		return new Timestamp(ret.getTime());
	}
}
