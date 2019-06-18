package gubo.http.grizzly.demo;

import gubo.http.grizzly.handlergenerator.MappingToPath;
import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler;

public class DemoService {

    @MappingToPath(value="/post-strings", method="POST")
    public String postStrings() {
        
        return "DemoService.postStrings";
    }
    
    @MappingToPath(value="/post-file", method="POST")
    public String postFile(InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler, PostFileRequest params) {
        return "DemoService.postFile";
    }
    
    /**
     *  演示 有MultipartFile类型字段的请求。
     *  只有在请求的content-type是 "multipart/form-data"的情况下，MultipartFile类型的字段才能被填对。
     *	"底层原理" 在 {@link AnnotationAwareHandler} 
     **/
    @MappingToPath(value="/post-file2", method="POST")
    public String postFile2(DemoRequest params) {
    	String s = new String(params.qualification.getBytes());
    	
    	System.out.println(s);
        return "DemoService.postFile";
    }
}
