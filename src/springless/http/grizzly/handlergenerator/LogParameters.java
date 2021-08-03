package springless.http.grizzly.handlergenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Log parameter in x-www-form-urlencoded before entry.
// See AnnotationAwareHandler
@Target(value = { ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LogParameters {

	// The logger to use for logging parameter
	Class<?> loggerClass() default Void.class;
	// These fields will not be logged. For sensitive data such as password.
	String hideFields() default "";
}
