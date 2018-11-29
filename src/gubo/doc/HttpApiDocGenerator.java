package gubo.doc;

import gubo.http.grizzly.handlergenerator.MappingToPath;

import java.sql.SQLException;

/**
 * 假定文档要描述的主体是 HTTP API，来生成文档，会生成curl命令的示例。
 * 和{@link Comment}配合。
 * 
 **/
public class HttpApiDocGenerator {
    
    /**
     *   interfc 上所有 同时被 {@link Comment} 和 {@link MappingToPath} 标注的 method 生成文档。
     * 
     **/
    public String generateDoc(Class<?> interfc, String urlPrefix) throws SQLException {
        
        return "";
    }
}
