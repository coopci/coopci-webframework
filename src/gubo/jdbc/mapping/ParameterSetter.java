package gubo.jdbc.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Set;

/**
 * 用来把pojo的字段set到 PreparedStatement的参数上。 
 **/
public class ParameterSetter {
	Method preparedStatementSetMethod;
	int index;
	private Field pojoField;

	public Field getPojoField() {
		return pojoField;
	}

	public void setPojoField(Field pojoField) {
		this.pojoField = pojoField;
		this.pojoField.setAccessible(true);
	}

	String columnName; // 数据库的列名。

	String convertToMysqlSetString(Set<String> set) {
		String result = String.join(",", set);
		return result;
	}
	
	void set(PreparedStatement ps, Object pojo) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Object value = pojoField.get(pojo);
		if (value instanceof Set) {
			// mysql 的集合 
			preparedStatementSetMethod.invoke(ps, this.index, convertToMysqlSetString((Set<String>)value));
		} else {
			preparedStatementSetMethod.invoke(ps, this.index, value);	
		}
		
	}

	public static void f(Object o) {
		System.out.println("o:" + o);
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException, ParseException {
		Object o = 9;
		f(o);

		return;
	}
}
