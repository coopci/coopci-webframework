package gubo.http.grizzly.handlergenerator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(value={ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MappingToPath {
	String value();
	String method() default "POST";
}
