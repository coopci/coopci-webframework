package gubo.http.grizzly;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import java.io.IOException;

public class ApiHttpHandler extends NannyHttpHandler {

    // subclass "can" override this method to custom serialization.
    public void sendContent(Object o, Request req, Response res) throws IOException {
        if (o == null) {
            res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
        }
        
        if (o instanceof String) {
            this.sendContent((String)o, req, res);
        } else {
            this.sendContent("not implemented yet.", req, res);
        }
            
        
    }
    
    
    public void sendContent(String s, Request req, Response response) throws IOException {
        response.setContentType("text/plain");
        response.setContentLength(s.length());
        response.getWriter().write(s);
        
    }
    
    
    
    public void serveHead(Request req, Response res) throws Exception {
        Object o = this.doHead(req, res);
        this.sendContent(o, req, res);
    }
    

    public void serveGet(Request req, Response res) throws Exception {
        Object o = this.doGet(req, res);
        this.sendContent(o, req, res);
    }
    

    public void servePost(Request req, Response res) throws Exception {
        Object o = this.doPost(req, res);
        this.sendContent(o, req, res);
    }
    
    public void servePut(Request req, Response res) throws Exception {
        Object o = this.doPut(req, res);
        this.sendContent(o, req, res);
    }
    
    public void serveDelete(Request req, Response res) throws Exception {
        Object o = this.doDelete(req, res);
        this.sendContent(o, req, res);
    }
    
    
    
    // subclasses should implement these doXXX methods. The returned Object is serialized by this class and the status code will always be 200 upon successful doXXX.
    public Object doHead(Request req, Response res) throws Exception {
        return null;
    }

    public Object doGet(Request req, Response res) throws Exception {
        return null;
    }

    public Object doPost(Request req, Response res) throws Exception {
        return null;
    }
    
    public Object doPut(Request req, Response res) throws Exception {
        return null;
    }
    
    public Object doDelete(Request req, Response res) throws Exception {
        return null;
    }
    
    
}
