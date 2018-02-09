package gubo.http.grizzly.handlers;

import gubo.http.grizzly.ApiHttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import java.util.HashMap;


public class MirroHandler extends ApiHttpHandler {
    public Object doGet(Request request, Response response) throws Exception {
        
        HashMap<String, Object> o = new HashMap<String, Object>();
        o.put("remote-addr", request.getRemoteAddr());
        o.put("remote-port", Integer.toString(request.getRemotePort()));
        
        o.put("uri", request.getRequestURI());
        return o;
    }
}
