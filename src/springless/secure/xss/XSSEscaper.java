package springless.secure.xss;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.coverity.security.Escape;

public class XSSEscaper {
	
	/**
	 * 如果obj本身是String，那么 返回obj 被excape的样子。
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * 
	 **/
	public static String escapeHtml(Object obj) throws IllegalArgumentException, IllegalAccessException {
		if(obj==null) {
			return null;
		}
		if(obj instanceof String) {
			return Escape.html((String)obj);
		}
		
		
		Field[] fields = FieldUtils.getAllFields(obj.getClass());
		for(Field f : fields) {
			if(f.getType() == String.class) {
				f.setAccessible(true);
				String s = (String)f.get(obj);
				if(s == null) {
					continue;
				}
				s = Escape.html(s);
				f.set(obj, s);
			}
		}
		
		
		return null;
	}
}
