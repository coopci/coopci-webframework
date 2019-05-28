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
}
