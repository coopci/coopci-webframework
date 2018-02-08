package gubo.http.grizzly;

import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

public class NannyHttpHandler extends HttpHandler {

    
    public void serveHead(Request req, Response res) throws Exception {
        res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
    }
    

    public void serveGet(Request req, Response res) throws Exception {
        res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
    }
    

    public void servePost(Request req, Response res) throws Exception {
        res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
    }
    
    public void servePut(Request req, Response res) throws Exception {
        res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
    }
    
    public void serveDelete(Request req, Response res) throws Exception {
        res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
    }
    
    
    public void handleException(Exception ex) throws Exception {
        throw ex;
    }
    
    @Override
    public void service(Request req, Response res) throws Exception {
        
        
        Method method = req.getMethod();
        
        
        try {
            if (method.getMethodString().equals("GET")) {
                this.serveGet(req, res);
            } else if (method.getMethodString().equals("POST")) {
                this.servePost(req, res);
            } else if (method.getMethodString().equals("HEAD")) {
                this.serveHead(req, res);
            } else if (method.getMethodString().equals("PUT")) {
                this.servePut(req, res);
            } else if (method.getMethodString().equals("DELETE")) {
                this.serveDelete(req, res);
            } 
            
        } catch (Exception ex) {
            this.handleException(ex);
        }   
    }

}
