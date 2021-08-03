package springless.http.grizzly.demo;

import org.glassfish.grizzly.http.multipart.ContentDisposition;
import org.glassfish.grizzly.http.multipart.MultipartEntry;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import springless.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import springless.http.grizzly.handlers.InMemoryMultipartHttpHandler;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class DemoInMemoryMultipartHttpHandler extends InMemoryMultipartHttpHandler {

    final HashMap<String, Integer> sizeLimit;
    {
        sizeLimit = new HashMap<String, Integer>();
        sizeLimit.put("ignore-this",
                InMemoryMultipartEntryHandler.SIZE_LIMIT_IGNORE);
        sizeLimit.put("reject-this",
                InMemoryMultipartEntryHandler.SIZE_LIMIT_REJECT);
        sizeLimit.put("big-one", 1024 * 1024);
        sizeLimit.put("fff", 1024 * 1024);
    }
    public HashMap<String, Integer> getSizeLimit() {
        return sizeLimit;
    }
    public int getDefaultSizeLimit() {
     // 忽略 big-one 和 fff 以外的所有参数。
        return InMemoryMultipartEntryHandler.SIZE_LIMIT_IGNORE;
    }
    
    @Override
    public void onMultipartScanCompleted(Request request, Response response, InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler) throws IOException {
        
    	System.out.println("request.getContentType(): " + request.getContentType());
    	response.setContentType("text/plain");
        final Writer writer = response.getWriter();
        writer.write(inMemoryMultipartEntryHandler
                .toString());

        String s1 = inMemoryMultipartEntryHandler
                .getString("sfsd");
        String s2 = inMemoryMultipartEntryHandler
                .getString("fff");
        
        @SuppressWarnings("unused")
        MultipartEntry multipartEntry = inMemoryMultipartEntryHandler.getMultipartEntry("fff");
        ContentDisposition contentDisposition = inMemoryMultipartEntryHandler.getContentDisposition("fff");
        
        String filename = contentDisposition.getDispositionParam("filename");
        
        String s3 = inMemoryMultipartEntryHandler
                .getString("big-one");

        writer.write(s1 + "\n");
        writer.write(s3 + "\n");
        writer.write("filename: " + filename + "\n");
        writer.write("fileconent: " + s2 + "\n");
    }

}
