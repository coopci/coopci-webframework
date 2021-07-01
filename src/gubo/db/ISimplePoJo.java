package gubo.db;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.apache.commons.lang3.reflect.FieldUtils;

import gubo.secure.xss.XSSEscaper;

public interface ISimplePoJo {

	public void setId(long id);
	
	default Object getPk() throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = FieldUtils.getFieldsWithAnnotation(this.getClass(), Id.class);
		if (fields.length == 0) {
			throw new RuntimeException(this.getClass().toString() + "没有定义@Id字段。");
		}
		Field field = fields[0];
		field.setAccessible(true);
		return field.get(this);
	}
	
	default void escapeXSS() {
		try {
			XSSEscaper.escapeHtml(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
