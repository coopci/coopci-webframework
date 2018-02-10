package gubo.jdbc.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;


public class ParameterSetter {
	Method preparedStatementSetMethod;
	int index;
	Field pojoField;
	String columnName; // 数据库的列名。
	
	void set(PreparedStatement ps, Object pojo) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object value = pojoField.get(pojo);
		preparedStatementSetMethod.invoke(ps, this.index, value);
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
