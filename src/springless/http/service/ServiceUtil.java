package springless.http.service;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import springless.http.grizzly.handlers.InMemoryMultipartEntryHandler;

import java.lang.reflect.Method;

public class ServiceUtil {
 // 选择grizzly的request 和 response以外的参数。
    public static Class<?> getParameterClass(Method method) {
        for (Class<?> c : method.getParameterTypes()) {
            if (c.equals(Request.class)) {
                continue;
            } else if (c.equals(Response.class)) {
                continue;
            } else if (c.equals(InMemoryMultipartEntryHandler.class)) {
                continue;
            } else {
                return c;
            }
        }
        return null;
    }
}
