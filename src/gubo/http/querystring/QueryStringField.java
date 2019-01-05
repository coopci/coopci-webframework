package gubo.http.querystring;

import gubo.http.querystring.parsers.NullParser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME) 
public @interface QueryStringField {
   String name() default ""; // 在query string 中的 字段名字。
   Class<? extends IQueryStringFieldParser> deserializer() default NullParser.class;
   boolean ignoreMalFormat() default true;
   boolean required() default true;
   boolean hidden() default false;
   // 如果是字符串类型,指定是否允许是""。
   boolean canBeBlank() default true;
   
   // trim the string
   boolean doTrim() default false;
}
