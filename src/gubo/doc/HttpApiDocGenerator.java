package gubo.doc;

import gubo.http.grizzly.handlergenerator.MappingToPath;
import gubo.http.grizzly.handlergenerator.HandlerGenerator.SourceFile;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * 假定文档要描述的主体是 HTTP API，来生成文档，会生成curl命令的示例。
 * 和{@link Comment}配合。
 * 
 **/
public class HttpApiDocGenerator {
    
    /**
     *   为 interfc 上所有 同时被 {@link Comment} 和 {@link MappingToPath} 标注的 method 生成文档。
     * 
     **/
    public String generateDoc(Class<?> interfc, String urlPrefix) {
        Method[] methods = interfc.getMethods();

        List<String> doc = new LinkedList<String>();
        for (Method m : methods) {
            String docForMethhod = this.generateDoc( m,  urlPrefix);
            doc.add(docForMethhod);
            System.out.println(docForMethhod);
        }
        return "";
    }
    
    public String generateDoc(Method method, String urlPrefix) {
        Comment comment = method.getAnnotation(Comment.class);
        MappingToPath mappingToPath = method.getAnnotation(MappingToPath.class);
        if (comment == null) {
            return "";
        }
        if (mappingToPath == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(urlPrefix + mappingToPath.value());
        sb.append("\n");
        sb.append(comment.value());
        
        return sb.toString();
    }
    
}
