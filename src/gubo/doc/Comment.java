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
}
