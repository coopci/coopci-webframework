package springless.http.querystring;

import java.lang.reflect.Field;

import org.jtwig.JtwigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JtwigTool {
	public static Logger logger = LoggerFactory.getLogger(JtwigTool.class);
	
	public void populateJtwigModel(JtwigModel model, Object pojo) throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = pojo.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			QueryStringField anno = f.getAnnotation(QueryStringField.class);
			if (anno == null) {
				logger.debug("anno == null, class: {}, field name: {} ",
						clazz.getName(), f.getName());
				continue;
			}
			String modelfieldName = f.getName();
			if (anno.name() != null && anno.name().length() > 0) {
				modelfieldName = anno.name();
			}
			f.setAccessible(true);
			model.with(modelfieldName, f.get(pojo));
		}
	}
}
