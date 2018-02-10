package gubo.http.querystring;

import java.lang.reflect.Field;

public interface IQueryStringFieldParser {
	Object parse(String s) throws Exception;
	
	
	void setField(Field f);
	Field getField();
	void setIgnoreMalFormat(boolean v);
	boolean getIgnoreMalFormat();
}
