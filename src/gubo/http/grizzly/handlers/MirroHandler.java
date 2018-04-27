package gubo.http.grizzly.handlers;

import gubo.http.grizzly.NannyHttpHandler;

import java.util.HashMap;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;


public class MirroHandler extends NannyHttpHandler {
    public Object doGet(Request request, Response response) throws Exception {
        
        HashMap<String, Object> o = new HashMap<String, Object>();
        o.put("remote-addr", request.getRemoteAddr());
        o.put("remote-port", Integer.toString(request.getRemotePort()));
        
        o.put("uri", request.getRequestURI());
        return o;
    }
}
