package gubo.http.grizzly;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class ApiHttpHandler extends NannyHttpHandler {


	public ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		return objectMapper;
	}
	
    // subclass "can" override this method to custom serialization.
    public void sendContent(Object o, Request req, Response response) throws IOException {
        if (o == null) {
        	response.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
        }
        
        ObjectMapper mapper = getObjectMapper();
        String content = mapper.writeValueAsString(o);
        
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write(content);
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
