package gubo.doc;

import gubo.http.grizzly.handlergenerator.MappingToPath;
import gubo.http.querystring.QueryStringBinder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
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
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws UnsupportedEncodingException 
     * @throws IllegalArgumentException 
     * 
     **/
    public String generateDoc(Class<?> interfc, String urlPrefix) throws InstantiationException, IllegalAccessException, IllegalArgumentException, UnsupportedEncodingException {
        Method[] methods = interfc.getMethods();

        List<String> doc = new LinkedList<String>();
        for (Method m : methods) {
            String docForMethhod = this.generateDoc( m,  urlPrefix);
            doc.add(docForMethhod);
            System.out.println(docForMethhod);
        }
        return "";
    }
    
    public String generateDoc(Method method, String urlPrefix) throws InstantiationException, IllegalAccessException, IllegalArgumentException, UnsupportedEncodingException {
        Comment comment = method.getAnnotation(Comment.class);
        MappingToPath mappingToPath = method.getAnnotation(MappingToPath.class);
        if (comment == null) {
            return "";
        }
        if (mappingToPath == null) {
            return "";
        }
        
        String url = urlPrefix + mappingToPath.value();
        String httpMethod = mappingToPath.method().toUpperCase();
        StringBuilder sb = new StringBuilder();
        sb.append(url);sb.append("\n");
        sb.append("method: " + httpMethod);sb.append("\n");
        sb.append(comment.value());sb.append("\n");

        String postData = "";
        String querystring = "";
        
        
        Class<?> parameterType = method.getParameters()[0].getType();
        
        Object parameterObj = parameterType.newInstance();
        
        QueryStringBinder binder = new QueryStringBinder();
        String ret = binder.toQueryString(parameterObj, null);
            
            
        if ("POST".equals(httpMethod)) {
            postData = " -H'Content-Type: application/x-www-form-urlencoded' -d'" + ret + "' ";
            
        } else if ("GET".equals(httpMethod)) {
            querystring = "?"+ret;
        }
        
        String curl = "curl -X" + httpMethod + postData + " '" + url + querystring + "'";
        
        sb.append("curl示例: " + curl);sb.append("\n");
        return sb.toString();
    }
    
}
