package gubo.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.reflect.FieldUtils;


public class FieldCopy {

	private static boolean shouldIgnorForCopy(Field f) {
		if(f == null) {
			return true;
		}
		if (f.isSynthetic()) {
			return true;
		}
		int mod = f.getModifiers();
		if (Modifier.isStatic(mod)) {
			return true;
		}
		return false;
	}
	/**
	 * Copy fields from `from` to `to`
	 * e.g. do to.a = from.a if from.a is assignable from to.a and a is not static nor synthetic 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 **/
	public static void copy(Object from, Object to) throws IllegalArgumentException, IllegalAccessException {
		
		Field[] fromFields = FieldUtils.getAllFields(from.getClass());
		for (Field f : fromFields) {
			if(shouldIgnorForCopy(f)) {
				continue;
			}
			Field toField = FieldUtils.getField(to.getClass(), f.getName(), true);
			if(shouldIgnorForCopy(toField)) {
				continue;
			}
			if (toField.getType().isAssignableFrom(f.getType())) {
				toField.set(to, f.get(from));	
			}
		}
	}
}
