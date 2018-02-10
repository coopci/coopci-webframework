package gubo.http.querystring.parsers;

import gubo.http.querystring.IQueryStringFieldParser;

import java.lang.reflect.Field;

public abstract class BaseQueryStringFieldParser implements
		IQueryStringFieldParser {

	boolean ignoreMalFormat = true;

	@Override
	public void setIgnoreMalFormat(boolean v) {
		this.ignoreMalFormat = v;

	}

	@Override
	public boolean getIgnoreMalFormat() {
		return this.ignoreMalFormat;
	}

	
	Field field;
	@Override
	public Field getField() {
		return field;
	}
	@Override
	public void setField(Field field) {
		this.field = field;
	}

}
