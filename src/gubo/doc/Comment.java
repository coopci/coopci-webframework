package gubo.doc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用来自动生成文档。
 * 
 **/

@Retention(value = RetentionPolicy.RUNTIME) 
public @interface Comment {
    String value() default ""; // 文档的正文。
    String example() default ""; // 如果被描述的主体是变量，那么这里给出一个示例。
    String since() default "";
    
    String deprecatedBy() default "";
    String deprecatedSince() default "";
    
    // 如果被注释的主体是API，那么可以用这个给API命名。
    String name() default "";
    // 如果被注释的主体是API，那么可以用这个给API划分组别。例如映射到postman的ItemGroup。
    // 用 / 表示层级关系，例如  a/b  表示 b是a下的一个group，被注释的主体是b下的一个API。
    String group() default "";
}
