package springless.http.querystring.parsers;

import java.lang.reflect.Field;

import springless.http.querystring.IQueryStringFieldParser;

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

	boolean canBeBlank = true;

	public boolean isCanBeBlank() {
		return canBeBlank;
	}

	public void setCanBeBlank(boolean canBeBlank) {
		this.canBeBlank = canBeBlank;
	}

	boolean doTrim = false;

	public boolean isDoTrim() {
		return doTrim;
	}

	public void setDoTrim(boolean doTrim) {
		this.doTrim = doTrim;
	}
}
